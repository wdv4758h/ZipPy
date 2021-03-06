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

import org.python.google.common.primitives.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.control.*;
import edu.uci.python.nodes.control.LoopNode;
import edu.uci.python.nodes.frame.*;
import edu.uci.python.nodes.function.*;
import edu.uci.python.nodes.generator.*;
import edu.uci.python.nodes.generator.GeneratorIfNode.GeneratorIfWithoutElseNode;
import edu.uci.python.nodes.statement.*;
import edu.uci.python.runtime.*;

public class GeneratorTranslator {

    private final FunctionRootNode root;
    private final PythonContext context;
    private int numOfActiveFlags;
    private int numOfGeneratorBlockNode;
    private int numOfGeneratorForNode;
    private boolean needToHandleComplicatedYieldExpression;

    public GeneratorTranslator(PythonContext context, FunctionRootNode root) {
        this.context = context;
        this.root = root;
    }

    public RootCallTarget translate() {
        RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(root);

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

        for (ReadLocalVariableNode read : NodeUtil.findAllNodeInstances(root, ReadLocalVariableNode.class)) {
            read.replace(ReadGeneratorFrameVariableNode.create(read.getSlot()));
        }

        assert NodeUtil.findFirstNodeInstance(root, ReadLocalVariableNode.class) == null;

        for (YieldNode yield : NodeUtil.findAllNodeInstances(root, YieldNode.class)) {
            replaceYield(yield);
        }

        for (YieldNode yield : NodeUtil.findAllNodeInstances(root, YieldNode.class)) {
            assert yield.getParentBlockIndexSlot() != -1;
        }

        for (GeneratorExpressionNode genexp : NodeUtil.findAllNodeInstances(root, GeneratorExpressionNode.class)) {
            genexp.setEnclosingFrameGenerator(true);
            PNodeUtil.findMatchingNodeIn(genexp, root.getUninitializedBody()).setEnclosingFrameGenerator(true);
        }

        for (BreakNode breakNode : NodeUtil.findAllNodeInstances(root, BreakNode.class)) {
            replaceBreak(breakNode);
        }

        for (ContinueNode continueNode : NodeUtil.findAllNodeInstances(root, ContinueNode.class)) {
            replaceContinue(continueNode);
        }

        return callTarget;
    }

    private static void replaceBreak(BreakNode breakNode) {
        // look for it's breaking loop node
        Node current = breakNode.getParent();
        List<Integer> indexSlots = new ArrayList<>();
        List<Integer> flagSlots = new ArrayList<>();

        while (current instanceof GeneratorBlockNode || current instanceof ContinueTargetNode || current instanceof IfNode) {
            if (current instanceof GeneratorBlockNode) {
                int indexSlot = ((GeneratorBlockNode) current).getIndexSlot();
                indexSlots.add(indexSlot);
            } else if (current instanceof GeneratorIfWithoutElseNode) {
                GeneratorIfWithoutElseNode ifNode = (GeneratorIfWithoutElseNode) current;
                flagSlots.add(ifNode.getThenFlagSlot());
            } else if (current instanceof GeneratorIfNode) {
                GeneratorIfNode ifNode = (GeneratorIfNode) current;
                flagSlots.add(ifNode.getThenFlagSlot());
                flagSlots.add(ifNode.getElseFlagSlot());
            }

            current = current.getParent();
        }

        if (current instanceof GeneratorForNode) {
            int iteratorSlot = ((GeneratorForNode) current).getIteratorSlot();
            int[] indexSlotsArray = Ints.toArray(indexSlots);
            int[] flagSlotsArray = Ints.toArray(flagSlots);
            breakNode.replace(new GeneratorBreakNode(iteratorSlot, indexSlotsArray, flagSlotsArray));
        }
    }

    private static void replaceContinue(ContinueNode continueNode) {
        Node current = continueNode.getParent();
        List<Integer> indexSlots = new ArrayList<>();
        List<Integer> flagSlots = new ArrayList<>();

        while (!(current instanceof LoopNode)) {
            if (current instanceof GeneratorBlockNode) {
                int indexSlot = ((GeneratorBlockNode) current).getIndexSlot();
                indexSlots.add(indexSlot);
            } else if (current instanceof GeneratorIfWithoutElseNode) {
                GeneratorIfWithoutElseNode ifNode = (GeneratorIfWithoutElseNode) current;
                flagSlots.add(ifNode.getThenFlagSlot());
            } else if (current instanceof GeneratorIfNode) {
                GeneratorIfNode ifNode = (GeneratorIfNode) current;
                flagSlots.add(ifNode.getThenFlagSlot());
                flagSlots.add(ifNode.getElseFlagSlot());
            }

            current = current.getParent();
        }

        int[] indexSlotsArray = Ints.toArray(indexSlots);
        int[] flagSlotsArray = Ints.toArray(flagSlots);
        continueNode.replace(new GeneratorContinueNode(indexSlotsArray, flagSlotsArray));
    }

    private void replaceYield(YieldNode yield) {
        int depth = 0;
        PNode current = yield;

        while (current.getParent() != root) {
            current = (PNode) current.getParent();
            replaceControl(current, yield, depth++);
        }

        if (needToHandleComplicatedYieldExpression) {
            needToHandleComplicatedYieldExpression = false;
            // TranslationUtil.notCovered("Yield expression used in a complicated expressin");
            handleComplicatedYieldExpression(yield);
        }

        // Last pass to fix yield nodes which its parent block index has not been updated yet.
        if (yield.getParentBlockIndexSlot() == -1 && yield.getParent() instanceof GeneratorBlockNode) {
            GeneratorBlockNode block = (GeneratorBlockNode) yield.getParent();
            yield.replace(new YieldNode(yield, block.getIndexSlot()));
        }
    }

    public void handleComplicatedYieldExpression(YieldNode yield) {
        // Find the dominating StatementNode.
        PNode targetingStatement = (PNode) PNodeUtil.getParentFor(yield, WriteNode.class);

        List<PNode> subExpressions = PNodeUtil.getListOfSubExpressionsInOrder(targetingStatement);
        List<PNode> extractedExpressions = new ArrayList<>();
        List<PNode> extractedWrites = new ArrayList<>();
        for (PNode expr : subExpressions) {
            if (expr.equals(yield)) {
                break;
            }

            if (!expr.hasSideEffectAsAnExpression()) {
                continue;
            }

            if (isExtracted(extractedExpressions, expr)) {
                continue;
            }

            FrameSlot slot = TranslationEnvironment.makeTempLocalVariable(root.getFrameDescriptor());
            ReadNode read = ReadGeneratorFrameVariableNode.create(slot);
            expr.replace((Node) read);
            extractedWrites.add(read.makeWriteNode(expr));
            extractedExpressions.add(expr);
        }

        GeneratorBlockNode targetingBlock = (GeneratorBlockNode) targetingStatement.getParent();
        GeneratorBlockNode extendedBlock = targetingBlock.insertNodesBefore(targetingStatement, extractedWrites);
        targetingBlock.replace(extendedBlock);
    }

    private static boolean isExtracted(List<PNode> extractedExpressins, PNode expr) {
        for (PNode item : extractedExpressins) {
            if (expressionDominates(expr, item)) {
                return true;
            }
        }

        return false;
    }

    private static boolean expressionDominates(PNode expr, PNode potentialDominator) {
        if (expr.equals(potentialDominator)) {
            return false;
        }

        Node current = expr.getParent();
        while (!(current instanceof GeneratorBlockNode)) {
            if (current.equals(potentialDominator)) {
                return true;
            }

            current.getParent();
        }

        return false;
    }

    private void splitArgumentLoads(ReturnTargetNode returnTarget) {
        assert context != null;

        if (returnTarget.getBody() instanceof BlockNode) {
            BlockNode body = (BlockNode) returnTarget.getBody();
            assert body.getStatements().length == 2;
            PNode argumentLoads = body.getStatements()[0];
            returnTarget.replace(new GeneratorReturnTargetNode(argumentLoads, body.getStatements()[1], returnTarget.getReturn(), nextActiveFlagSlot()));
        } else {
            returnTarget.replace(new GeneratorReturnTargetNode(EmptyNode.create(), returnTarget.getBody(), returnTarget.getReturn(), nextActiveFlagSlot()));
        }
    }

    private void replaceControl(PNode node, YieldNode yield, int depth) {
        /**
         * Has it been replaced already?
         */
        if (node instanceof GeneratorControlNode) {
            return;
        }

        if (node instanceof WhileNode) {
            WhileNode whileNode = (WhileNode) node;

            if (node.getParent() instanceof BreakTargetNode) {
                node.getParent().replace(new GeneratorWhileNode(whileNode.getCondition(), whileNode.getBody(), nextActiveFlagSlot()));
            } else {
                node.replace(new GeneratorWhileNode(whileNode.getCondition(), whileNode.getBody(), nextActiveFlagSlot()));
            }
        } else if (node instanceof IfNode) {
            IfNode ifNode = (IfNode) node;
            int ifFlag = nextActiveFlagSlot();
            int elseFlag = nextActiveFlagSlot();
            node.replace(GeneratorIfNode.create(ifNode.getCondition(), ifNode.getThen(), ifNode.getElse(), ifFlag, elseFlag));
        } else if (node instanceof ForNode) {
            assert depth > 0;
            ForNode forNode = (ForNode) node;
            WriteGeneratorFrameVariableNode target = (WriteGeneratorFrameVariableNode) forNode.getTarget();
            GetIteratorNode getIter = (GetIteratorNode) forNode.getIterator();
            node.replace(GeneratorForNode.create(target, getIter, forNode.getBody(), nextGeneratorForNodeSlot()));
        } else if (node instanceof BlockNode) {
            BlockNode block = (BlockNode) node;
            int slotOfBlockIndex = nextGeneratorBlockIndexSlot();

            if (yield.getParent().equals(block)) {
                yield.replace(new YieldNode(yield, slotOfBlockIndex));
            }

            node.replace(new GeneratorBlockNode(block.getStatements(), slotOfBlockIndex));
        } else if (node instanceof StatementNode) {
            // do nothing for now
        } else {
            replaceYieldExpression(node, yield, depth);
        }
    }

    /**
     * Yield expression.
     * <p>
     * The parent nodes of yield are expressions. If all the other sub-expressions evaluated before
     * yield are side affect free, we simply re-evaluate those sub-expressions when resuming.
     * Otherwise we give up.
     */
    private void replaceYieldExpression(PNode node, YieldNode yield, int depth) {

        // Wraps yield and the inserted YieldSendValueNode with a GenBlockNode.
        if (depth == 0) {
            yield.replace(new GeneratorBlockNode(new PNode[]{yield, new YieldSendValueNode()}, nextGeneratorBlockIndexSlot()));
        }

        /**
         * Search for child expressions for ones that are not side-affect free (does not change any
         * local or non-local state).
         */
        for (Node child : node.getChildren()) {
            if (!(child instanceof PNode)) {
                continue;
            }

            if (NodeUtil.findFirstNodeInstance(child, YieldNode.class) != null) {
                continue;
            }

            child.accept(new NodeVisitor() {
                public boolean visit(Node childNode) {
                    assert !(child instanceof StatementNode);

                    if (childNode instanceof PNode) {
                        PNode childPNode = (PNode) childNode;
                        if (childPNode.hasSideEffectAsAnExpression()) {
                            needToHandleComplicatedYieldExpression = true;
                        }
                    }
                    return true;
                }
            });
        }
    }

    private int nextActiveFlagSlot() {
        return numOfActiveFlags++;
    }

    public int getNumOfActiveFlags() {
        return numOfActiveFlags;
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
