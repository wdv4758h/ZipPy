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
package edu.uci.python.parser;

import java.util.*;

import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.access.*;
import edu.uci.python.nodes.function.*;
import edu.uci.python.nodes.generator.*;
import edu.uci.python.nodes.loop.*;
import edu.uci.python.nodes.statement.*;
import edu.uci.python.runtime.*;

public class GeneratorTranslator {

    private int numOfGeneratorBlockNode;
    private int numOfGeneratorForNode;

    public void translate(FunctionRootNode root) {
        /**
         * Replace {@link ReturnTargetNode}.
         */
        List<ReturnTargetNode> returnTargets = NodeUtil.findAllNodeInstances(root, ReturnTargetNode.class);
        assert returnTargets.size() == 1;
        splitArgumentLoads(returnTargets.get(0));

        /**
         * Redirect local variable accesses to materialized persistent frame.
         */
        for (WriteLocalVariableNode write : NodeUtil.findAllNodeInstances(root, WriteLocalVariableNode.class)) {
            write.replace(WriteGeneratorFrameVariableNodeFactory.create(write.getSlot(), write.getRhs()));
        }

        Class<? extends FrameSlotNode> readLocalClass = PythonOptions.UsePolymorphicReadLocal ? PolymorphicReadLocalVariableNode.class : ReadLocalVariableNode.class;
        for (FrameSlotNode read : NodeUtil.findAllNodeInstances(root, readLocalClass)) {
            read.replace(ReadGeneratorFrameVariableNodeFactory.create(read.getSlot()));
        }

        for (YieldNode yield : NodeUtil.findAllNodeInstances(root, YieldNode.class)) {
            int depth = 0;
            PNode current = yield;

            while (current.getParent() != root) {
                current = (PNode) current.getParent();
                replaceControls(current, depth++);
            }
        }

        for (GeneratorExpressionDefinitionNode def : NodeUtil.findAllNodeInstances(root, GeneratorExpressionDefinitionNode.class)) {
            def.replace(new GeneratorGeneratorExpressionDefinitionNode(def));
        }
    }

    @SuppressWarnings("static-method")
    private void splitArgumentLoads(ReturnTargetNode returnTarget) {
        if (returnTarget.getBody() instanceof BlockNode) {
            BlockNode body = (BlockNode) returnTarget.getBody();
            assert body.getStatements().length == 2;
            BlockNode argumentLoads = (BlockNode) body.getStatements()[0];
            body = (BlockNode) body.getStatements()[1];
            returnTarget.replace(new GeneratorReturnTargetNode(argumentLoads, body, returnTarget.getReturn()));
        } else {
            returnTarget.replace(new GeneratorReturnTargetNode(BlockNode.getEmptyBlock(), returnTarget.getBody(), returnTarget.getReturn()));
        }
    }

    private void replaceControls(PNode node, int depth) {
        /**
         * Has it been replace already?
         */
        if (node.getClass().getSimpleName().contains("Generator")) {
            return;
        }

        if (node instanceof ForWithLocalTargetNode) {
            ForWithLocalTargetNode forNode = (ForWithLocalTargetNode) node;
            AdvanceIteratorNode next = (AdvanceIteratorNode) forNode.getTarget();
            WriteGeneratorFrameVariableNode target = (WriteGeneratorFrameVariableNode) next.getTarget();
            GetIteratorNode getIter = (GetIteratorNode) forNode.getIterator();
            int iteratorSlot = nextGeneratorForNodeSlot();

            if (depth == 0) {
                node.replace(new GeneratorForNode.InnerGeneratorForNode(target, getIter, forNode.getBody(), iteratorSlot));
            } else {
                node.replace(new GeneratorForNode(target, getIter, forNode.getBody(), iteratorSlot));
            }
        } else if (node instanceof BlockNode) {
            BlockNode block = (BlockNode) node;
            int slotOfBlockIndex = nextGeneratorBlockIndexSlot();

            if (depth == 0) {
                node.replace(new GeneratorBlockNode.InnerGeneratorBlockNode(block.getStatements(), slotOfBlockIndex));
            } else {
                node.replace(new GeneratorBlockNode(block.getStatements(), slotOfBlockIndex));
            }
        } else if (node instanceof IfNode) {
            // do nothing for now
        } else {
            TranslationUtil.notCovered();
        }
    }

    private int nextGeneratorBlockIndexSlot() {
        return numOfGeneratorBlockNode++;
    }

    public int getNumOfGeneratorBlockNode() {
        return numOfGeneratorBlockNode;
    }

    private int nextGeneratorForNodeSlot() {
        return numOfGeneratorForNode++;
    }

    public int getNumOfGeneratorForNode() {
        return numOfGeneratorForNode;
    }

}
