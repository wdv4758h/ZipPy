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
package edu.uci.python.nodes.call;

import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.access.*;
import edu.uci.python.nodes.function.*;
import edu.uci.python.runtime.function.*;

public abstract class InlinedCallNode extends CallFunctionNoKeywordNode implements InlinedCallSite {

    private final FrameFactory frameFactory;
    private final FrameDescriptor frameDescriptor;

    public InlinedCallNode(PNode callee, PNode[] arguments, FrameDescriptor frameDescriptor, FrameFactory frameFactory) {
        super(callee, arguments);
        this.frameDescriptor = frameDescriptor;
        this.frameFactory = frameFactory;
    }

    public abstract PythonCallable getCallee();

    protected FrameFactory getFrameFactory() {
        return frameFactory;
    }

    protected VirtualFrame createInlinedFrame(VirtualFrame caller, PArguments argument) {
        return frameFactory.create(frameDescriptor, caller.pack(), argument);
    }

    protected PNode prepareBody(PNode clonedBody) {
        clonedBody.accept(new NodeVisitor() {

            public boolean visit(Node node) {
                prepareBodyNode(node);
                assert !(node instanceof FunctionRootNode);
                return true;
            }

        });

        return clonedBody;
    }

    protected void prepareBodyNode(Node node) {
        NodeFactory factory = NodeFactory.getInstance();

        /**
         * Redirecting all frame accesses to the new {@link FrameDescriptor}.
         */
        if (node instanceof FrameSlotNode) {
            FrameSlotNode fsNode = (FrameSlotNode) node;
            FrameSlot origSlot = fsNode.getSlot();
            FrameSlot newSlot = frameDescriptor.findFrameSlot(origSlot.getIdentifier());
            assert newSlot != null && !origSlot.equals(newSlot);

            if (node instanceof ReadLocalVariableNode) {
                node.replace(factory.createReadLocal(newSlot));
            } else if (node instanceof WriteLocalVariableNode) {
                node.replace(factory.createWriteLocal(((WriteLocalVariableNode) node).getRhs(), newSlot));
            }
        }

        /**
         * Update {@link GeneratorExpressionDefinitionNode}'s enclosing frame to the new
         * {@link FrameDescriptor}.
         */
        if (node instanceof GeneratorExpressionNode) {
            GeneratorExpressionNode genexp = (GeneratorExpressionNode) node;
            genexp.setEnclosingFrameDescriptor(frameDescriptor);
        }
    }

}
