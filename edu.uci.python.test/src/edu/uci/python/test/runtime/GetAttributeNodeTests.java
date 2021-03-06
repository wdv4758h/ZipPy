/*
 * Copyright (c) 2013, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.uci.python.test.runtime;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.python.core.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.function.*;
import edu.uci.python.nodes.literal.*;
import edu.uci.python.nodes.object.*;
import edu.uci.python.nodes.object.AttributeReadNode.ReadArrayObjectAttributeNode;
import edu.uci.python.nodes.object.AttributeReadNode.ReadFieldObjectAttributeNode;
import edu.uci.python.nodes.object.DispatchBoxedNode.*;
import edu.uci.python.nodes.object.DispatchUnboxedNode.*;
import edu.uci.python.nodes.object.GetAttributeNode.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.object.*;
import edu.uci.python.runtime.standardtype.*;
import edu.uci.python.test.*;

public class GetAttributeNodeTests {

    @Test
    public void builtinObjectAttribute() {
        // environment
        PythonContext context = PythonTests.getContext();
        NodeFactory factory = new NodeFactory();

        // AST assembling
        List<PNode> values = new ArrayList<>();
        values.add(factory.createIntegerLiteral(0));
        values.add(factory.createIntegerLiteral(42));
        PNode plist = factory.createListLiteral(values);
        PNode getattr = factory.createGetAttribute(plist, "append");

        FunctionRootNode root = new FunctionRootNode(context, "test", false, new FrameDescriptor(), getattr);
        Truffle.getRuntime().createCallTarget(root);

        // 1st execute
        VirtualFrame frame = PythonTests.createVirtualFrame();
        root.execute(frame);

        // check rewrite of UninitializedGetAttributeNode
        PNode getAttr = root.getBody();
        assertTrue(getAttr instanceof UnboxedGetMethodNode);

        // 2nd execute
        frame = PythonTests.createVirtualFrame();
        root.execute(frame);

        // check rewrite of UninitializedCachedAttributeNode
        UnboxedGetMethodNode getMethod = (UnboxedGetMethodNode) getAttr;
        LinkedDispatchUnboxedNode cache = NodeUtil.findFirstNodeInstance(getMethod, LinkedDispatchUnboxedNode.class);
        assertTrue(cache.extractReadNode() instanceof ReadFieldObjectAttributeNode || cache.extractReadNode() instanceof ReadArrayObjectAttributeNode);

        // 3rd execute
        frame = PythonTests.createVirtualFrame();
        root.execute(frame);

        // make sure cache node stay unchanged
        cache = NodeUtil.findFirstNodeInstance(getMethod, LinkedDispatchUnboxedNode.class);
        assertTrue(cache.extractReadNode() instanceof ReadFieldObjectAttributeNode || cache.extractReadNode() instanceof ReadArrayObjectAttributeNode);

        /**
         * test fall back.
         */
        // replace primary to a string
        PNode pstr = factory.createStringLiteral("yy");
        NodeUtil.findFirstNodeInstance(getMethod, ListLiteralNode.class).replace(pstr);

        // 4th execute
        frame = PythonTests.createVirtualFrame();
        try {
            root.execute(frame);
        } catch (PyException pe) {
            assertTrue(pe.value.toString().contains("no attribute"));
        }

        /**
         * At this point AST failed to rewrite itself due to unexpected attribute lookup. Since we
         * recovered from the no attribute exception, we should be able to continue.
         */
        // replace primary to a full PythonBasicObject
        PythonClass classA = new PythonClass(context, "A", new PythonClass[]{});
        PythonObject pbObj = PythonContext.newPythonObjectInstance(classA);
        pbObj.setAttribute("append", 42);
        PNode objNode = factory.createObjectLiteral(pbObj);
        NodeUtil.findFirstNodeInstance(getMethod, StringLiteralNode.class).replace(objNode);

        // 5th execute
        frame = PythonTests.createVirtualFrame();
        root.execute(frame);

        // check rewrite of UnboxedGetAttributeNode to BoxedGetAttributeNode
        getAttr = root.getBody();
        assertTrue(getAttr instanceof BoxedGetAttributeNode);
    }

    @Test
    public void pythonObjectAttribute() {
        // environment
        PythonContext context = PythonTests.getContext();
        NodeFactory factory = new NodeFactory();

        // in object attribute
        PythonClass classA = new PythonClass(context, "A", new PythonClass[]{});
        PythonObject pbObj = PythonContext.newPythonObjectInstance(classA);
        pbObj.setAttribute("foo", 42);

        // assemble AST
        PNode objNode = factory.createObjectLiteral(pbObj);
        PNode getattr = factory.createGetAttribute(objNode, "foo");

        FunctionRootNode root = new FunctionRootNode(context, "test", false, new FrameDescriptor(), getattr);
        Truffle.getRuntime().createCallTarget(root);

        // 1st execute
        VirtualFrame frame = PythonTests.createVirtualFrame();
        root.execute(frame);

        // check rewrite of UninitializedGetAttributeNode
        PNode getAttr = root.getBody();
        assertTrue(getAttr instanceof BoxedGetAttributeNode);

        // 2nd execute
        frame = PythonTests.createVirtualFrame();
        root.execute(frame);

        // check rewrite of UninitializedCachedAttributeNode
        LinkedDispatchBoxedNode cache = NodeUtil.findFirstNodeInstance(getAttr, LinkedDispatchBoxedNode.class);
        assertTrue(cache.extractReadNode() instanceof AttributeReadNode.ReadIntAttributeNode);

        // 3rd execute
        frame = PythonTests.createVirtualFrame();
        root.execute(frame);

        // make sure cache node stay unchanged
        cache = NodeUtil.findFirstNodeInstance(getAttr, LinkedDispatchBoxedNode.class);
        assertTrue(cache.extractReadNode() instanceof AttributeReadNode.ReadIntAttributeNode);

        /**
         * Test fall back
         */
        // modify object layout
        pbObj.deleteAttribute("foo");

        // 4th execute
        frame = PythonTests.createVirtualFrame();
        try {
            root.execute(frame);
        } catch (PyException pe) {
            assertTrue(pe.value.toString().contains("no attribute"));
        }
    }

}
