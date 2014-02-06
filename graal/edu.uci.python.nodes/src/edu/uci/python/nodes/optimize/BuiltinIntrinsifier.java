/*
 * Copyright (c) 2014, Regents of the University of California
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
package edu.uci.python.nodes.optimize;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.access.*;
import edu.uci.python.nodes.call.*;
import edu.uci.python.nodes.function.*;
import edu.uci.python.nodes.generator.*;
import edu.uci.python.nodes.loop.*;
import edu.uci.python.nodes.statement.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.function.*;

public class BuiltinIntrinsifier {

    private final PythonContext context;
    @SuppressWarnings("unused") private final Assumption globalScopeUnchanged;
    @SuppressWarnings("unused") private final Assumption builtinModuleUnchanged;
    private final CallBuiltinInlinedNode call;
    private GeneratorExpressionDefinitionNode genexp;

    public BuiltinIntrinsifier(PythonContext context, Assumption globalScopeUnchanged, Assumption builtinModuleUnchanged, CallBuiltinInlinedNode call) {
        this.context = context;
        this.globalScopeUnchanged = globalScopeUnchanged;
        this.builtinModuleUnchanged = builtinModuleUnchanged;
        this.call = call;
    }

    public void intrinsify() {
        if (isCallToBuiltinFunctionList() && isNotCallingFromGenerator()) {
            transformToListComp();
        }
    }

    public boolean isNotCallingFromGenerator() {
        Node current = call;
        while (!(current instanceof ReturnTargetNode)) {
            current = current.getParent();
        }

        if (current instanceof GeneratorReturnTargetNode) {
            return false;
        }

        return true;
    }

    public boolean isCallToBuiltinFunctionList() {
        PBuiltinFunction callee = call.getCallee();
        if (!callee.getName().equals("list")) {
            return false;
        }

        if (call.getArguments().length != 1) {
            return false;
        }

        PNode arg = call.getArguments()[0];
        if (arg instanceof GeneratorExpressionDefinitionNode) {
            this.genexp = (GeneratorExpressionDefinitionNode) arg;
            return true;
        }

        return false;
    }

    /**
     * The caller of the built-in function could be inlined at this point.
     */
    private static FrameDescriptor findEnclosingFrameDescriptor(PNode genExp) {
        Node current = genExp;
        while (true) {
            current = current.getParent();

            if (current instanceof RootNode || current instanceof InlinedCallNode) {
                break;
            }
        }

        FrameSlotNode slotNode = NodeUtil.findFirstNodeInstance(current, FrameSlotNode.class);
        return slotNode.getSlot().getFrameDescriptor();
    }

    private void transformToListComp() {
        FrameDescriptor genexpFrame = genexp.getFrameDescriptor();
        FrameDescriptor enclosingFrame = findEnclosingFrameDescriptor(call);
        PNode uninitializedGenexpBody = ((FunctionRootNode) genexp.getFunctionRootNode()).getUninitializedBody();
        uninitializedGenexpBody = (PNode) NodeUtil.findFirstNodeInstance(uninitializedGenexpBody, ForWithLocalTargetNode.class).copy();

        for (FrameSlot genexpSlot : genexpFrame.getSlots()) {
            if (genexpSlot.getIdentifier().equals("<return_val>")) {
                continue;
            }

            // Name does not collide
            // assert enclosingFrame.findFrameSlot(genexpSlot.getIdentifier()) == null;
            FrameSlot enclosingSlot = enclosingFrame.findOrAddFrameSlot(genexpSlot.getIdentifier());

            redirectLocalRead(genexpSlot, enclosingSlot, uninitializedGenexpBody);
            redirectLocalWrite(genexpSlot, enclosingSlot, uninitializedGenexpBody);
        }

        redirectLevelRead(uninitializedGenexpBody);

        FrameSlot listCompSlot = enclosingFrame.addFrameSlot("<list_comp_val" + genexp.hashCode() + ">");
        YieldNode yield = NodeUtil.findFirstNodeInstance(uninitializedGenexpBody, YieldNode.class);
        WriteLocalVariableNode write = (WriteLocalVariableNode) yield.getRhs();
        yield.replace(ListAppendNodeFactory.create(listCompSlot, write.getRhs()));
        call.replace(new ListComprehensionNode(listCompSlot, uninitializedGenexpBody));

        context.getStandardOut().println("[ZipPy] builtin intrinsifier: transform " + genexp + " with call to 'list' to list comprehension");
    }

    private static void redirectLocalRead(FrameSlot orig, FrameSlot target, PNode root) {
        for (ReadLocalVariableNode read : NodeUtil.findAllNodeInstances(root, ReadLocalVariableNode.class)) {
            if (read.getSlot().equals(orig)) {
                read.replace(ReadLocalVariableNode.create(target));
            }
        }
    }

    private static void redirectLocalWrite(FrameSlot orig, FrameSlot target, PNode root) {
        for (WriteLocalVariableNode write : NodeUtil.findAllNodeInstances(root, WriteLocalVariableNode.class)) {
            if (write.getSlot().equals(orig)) {
                write.replace(WriteLocalVariableNodeFactory.create(target, write.getRhs()));
            }
        }
    }

    private static void redirectLevelRead(PNode root) {
        for (ReadLevelVariableNode read : NodeUtil.findAllNodeInstances(root, ReadLevelVariableNode.class)) {
            read.replace(ReadLocalVariableNode.create(read.getSlot()));
        }
    }

}
