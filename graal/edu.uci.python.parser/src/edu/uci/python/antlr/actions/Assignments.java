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
package edu.uci.python.antlr.actions;

import java.util.*;

import org.antlr.runtime.*;

import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.antlr.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.expressions.*;
import edu.uci.python.nodes.literals.*;
import edu.uci.python.nodes.statements.*;
import edu.uci.python.nodes.truffle.*;
import edu.uci.python.types.*;

public class Assignments {

    private NodeFactory factory = null;
    private Variables var = null;

    public Assignments(NodeFactory factory) {
        this.factory = factory;
        this.var = new Variables(factory);
    }

    public PNode makeAssign(Token t, List<PNode> lhs, PNode rhs) {
        PNode retVal = null;

        try {
            PNode firstTarget = lhs.get(0);
            PNode validTarget = null;

            if (lhs.size() == 1 && isDecomposable(firstTarget)) {
                List<PNode> targets = decompose(firstTarget);

                if (isDecomposable(rhs)) {
                    List<PNode> rights = decompose(rhs);

                    if (targets.size() == rights.size()) {
                        retVal = transformBalancedMultiAssignment(targets, rights);
                    } else {
                        throw new RuntimeException("Unbalanced multi-assignment");
                    }
                } else {
                    retVal = transformUnpackingAssignment(targets, rhs);
                }
            } else if (lhs.size() == 1) {
                firstTarget = var.fixWriteLocalSlot(firstTarget);
                retVal = processSingleAssignment(firstTarget, rhs);
            } else {
                List<PNode> assignments = new ArrayList<>();
                for (PNode targetnode : lhs) {
                    validTarget = processSingleAssignment(var.fixWriteLocalSlot(targetnode), rhs);
                    assignments.add(validTarget);
                }

                retVal = factory.createBlock(assignments);
            }

        } catch (Exception e) {
            e.printStackTrace();
            UnCovered.uncoveredException("Unbalanced MultiAssignment");
        }
        return GrammarUtil.make(t, retVal);
    }

    public PNode makeAssign(PNode t, List<PNode> targets, PNode value) {
        return makeAssign(t.getToken(), targets, value);
    }

    public PNode reAssignElt(PNode elt) {
        PNode retVal = elt;
        FrameSlot slot = null;

        if (retVal instanceof WriteLocalNode) {
            slot = ParserEnvironment.def(retVal.getText());
            retVal = GrammarUtil.make(elt.getToken(), slot, factory.createWriteLocal(((WriteLocalNode) retVal).getRhs(), slot));
        } else if (retVal instanceof ReadLocalNode || retVal instanceof ReadGlobalNode) {
            retVal = GrammarUtil.make(elt.getToken(), var.makeNameRead(retVal.getToken()));
        }

        if (retVal != null) {
            Iterable<Node> list = retVal.getChildren();
            PNode newNode = null;
            for (Node n : list) {
                newNode = reAssignElt((PNode) n);
                GrammarUtil.replace(n, newNode);
            }
        }
        return retVal;
    }

    public List<PNode> makeAssignTargets(PNode paramLhs, List<?> rhs) {
        PNode lhs = var.fixWriteLocalSlot(paramLhs);
        List<PNode> e = new ArrayList<>();
        GrammarUtil.checkAssign(lhs);

        e.add(lhs);
        for (int i = 0; i < rhs.size() - 1; i++) {
            PNode r = GrammarUtil.castExpr(rhs.get(i));
            GrammarUtil.checkAssign(r);
            e.add(r);
        }
        return e;
    }

    public PNode makeAssignValue(List<?> rhs) {
        PNode retVal = null;
        PNode value = GrammarUtil.castExpr(rhs.get(rhs.size() - 1));
        retVal = var.recurseSetContext(value, ContextType.Load);
        return GrammarUtil.replace(value, retVal);
    }

    private List<PNode> makeTemporaryWrites(List<PNode> rights) {
        List<PNode> tempWrites = new ArrayList<>();

        for (int i = 0; i < rights.size(); i++) {
            PNode right = rights.get(i);
            PNode tempWrite = ((Amendable) makeTemporaryWrite()).updateRhs(right);
            tempWrites.add(tempWrite);
        }
        return tempWrites;
    }

    private PNode makeTemporaryWrite() {
        String tempName = ParserEnvironment.TEMP_LOCAL_PREFIX + ParserEnvironment.currentFrame.getSize();
        FrameSlot tempSlot = ParserEnvironment.currentFrame.addFrameSlot(tempName);
        PNode tempWrite = factory.createWriteLocal(PNode.DUMMY_NODE, tempSlot);
        tempWrite.setSlot(tempSlot);
        return tempWrite;
    }

    private static boolean isDecomposable(PNode node) {
        return node instanceof TupleLiteralNode || node instanceof ListLiteralNode;
    }

    private List<PNode> decomposeAll(List<PNode> list) {
        List<PNode> retVal = new ArrayList<>();

        for (PNode n : list) {
            if (isDecomposable(n)) {
                retVal.addAll(decomposeAll(decompose(n)));
            } else {
                retVal.add(n);
            }
        }
        return retVal;
    }

    private BlockNode transformBalancedMultiAssignment(List<PNode> ptargets, List<PNode> prights) {
        /**
         * Transform a, b = c, d. <br>
         * To: temp_c = c; temp_d = d; a = temp_c; b = temp_d
         */
        List<PNode> targets = decomposeAll(ptargets);
        List<PNode> rights = decomposeAll(prights);

        List<PNode> tempWrites = makeTemporaryWrites(rights);
        PNode read = null;
        PNode processSingleWrite = null;

        for (int i = 0; i < targets.size(); i++) {
            read = ((WriteNode) tempWrites.get(i)).makeReadNode();
            processSingleWrite = GrammarUtil.replace(targets.get(i), var.fixWriteLocalSlot(targets.get(i)));
            processSingleWrite = ((Amendable) processSingleWrite).updateRhs(read);
            tempWrites.add(processSingleWrite);
        }
        return factory.createBlock(tempWrites);
    }

    private List<PNode> processDecomposedTargetList(List<PNode> nestedWrites, int sizeOfCurrentLevelLeftHandSide, PNode tempWrite, boolean isUnpacking) {
        for (int idx = 0; idx < nestedWrites.size(); idx++) {
            if (idx < sizeOfCurrentLevelLeftHandSide) {
                PNode transformedRhs;
                if (isUnpacking) {
                    PNode read = ((WriteLocalNode) tempWrite).makeReadNode();
                    PNode indexNode = factory.createIntegerLiteral(idx);
                    transformedRhs = factory.createSubscriptLoad(read, indexNode);
                } else {
                    transformedRhs = ((WriteNode) tempWrite).makeReadNode();
                }
                PNode write = ((Amendable) nestedWrites.get(idx)).updateRhs(transformedRhs);
                nestedWrites.set(idx, write);
            }
        }
        return nestedWrites;
    }

    public List<PNode> walkLeftHandSideList(List<PNode> lhs) {
        List<PNode> writes = new ArrayList<>();
        List<PNode> additionalWrites = new ArrayList<>();

        for (int i = 0; i < lhs.size(); i++) {
            PNode target = lhs.get(i);
            target = var.fixWriteLocalSlot(target);
            if (isDecomposable(target)) {
                PNode tempWrite = makeTemporaryWrite();
                writes.add(tempWrite);
                List<PNode> targets = decompose(target);
                List<PNode> nestedWrites = walkLeftHandSideList(targets);
                additionalWrites.addAll(processDecomposedTargetList(nestedWrites, targets.size(), tempWrite, true));
            } else {
                writes.add(target);
            }
        }
        writes.addAll(additionalWrites);
        return writes;
    }

    private BlockNode transformUnpackingAssignment(List<PNode> lhs, PNode right) throws Exception {
        /**
         * Transform a, b = c. <br>
         * To: temp_c = c; a = temp_c[0]; b = temp_d[1]
         */
        List<PNode> writes = new ArrayList<>();
        PNode updateRight = null;
        Amendable tempWrite = (Amendable) makeTemporaryWrite();
        updateRight = tempWrite.updateRhs(var.writeLocalToRead(right));
        writes.add(updateRight);
        List<PNode> targets = walkLeftHandSideList(lhs);
        writes.addAll(processDecomposedTargetList(targets, lhs.size(), (PNode) tempWrite, true));
        return factory.createBlock(writes);
    }

    private PNode processSingleAssignment(PNode target, PNode right) throws Exception {
        PNode retVal = null;
        PNode lhs = target;
        FrameSlot slot = target.getSlot();
        String id = target.getText();

        if (lhs instanceof ReadGlobalNode) {
            if ((ParserEnvironment.scopeLevel == 1 && GlobalScope.getInstance().isGlobalOrBuiltin(id)) || ParserEnvironment.localGlobals.contains(id)) {
                retVal = factory.createWriteGlobal(id, right);
            } else {
                slot = ParserEnvironment.def(id);
                retVal = factory.createWriteLocal(right, slot);
            }
        } else if (lhs instanceof ReadLocalNode) {
            retVal = factory.createWriteLocal(right, ((ReadLocalNode) lhs).getSlot());
        } else if (lhs instanceof SubscriptLoadNode) {
            PNode value = ((SubscriptLoadNode) lhs).getPrimary();
            PNode slice = ((SubscriptLoadNode) lhs).getSlice();
            retVal = factory.createSubscriptStore(value, slice, right);
        } else if (lhs instanceof Amendable) {
            Amendable lhTarget = (Amendable) lhs;
            retVal = lhTarget.updateRhs(right);
        } else {
            UnCovered.uncoveredException("Unknown");
        }

        return GrammarUtil.make(target.getToken(), slot, retVal);
    }

    private static List<PNode> decompose(PNode node) {
        if (node instanceof TupleLiteralNode) {
            return ((TupleLiteralNode) node).getElts();
        } else if (node instanceof ListLiteralNode) {
            return ((ListLiteralNode) node).getElts();
        } else {
            throw new RuntimeException("Unexpected decomposable type");
        }
    }

    public PNode makeAugAssign(PNode t, PNode target, OperationType op, PNode value) {
        PNode retVal = null;
        FrameSlot slot = null;
        PNode readfs = null;
        PNode binaryOp = null;
        SubscriptLoadNode readsl = null;
        WriteGlobalNode writeGlobal = null;
        PNode readGlobal = null;

        if (target instanceof FrameSlotNode) {
            slot = ParserEnvironment.def(((FrameSlotNode) target).getText());
            // Only works for locals
            readfs = factory.createReadLocal(slot);
            binaryOp = factory.createBinaryOperation(op, readfs, value);
            retVal = factory.createWriteLocal(binaryOp, slot);
        } else if (target instanceof SubscriptLoadNode) {
            readsl = (SubscriptLoadNode) target;
            binaryOp = factory.createBinaryOperation(op, readsl, value);
            retVal = factory.createSubscriptStore(readsl.getPrimary(), readsl.getSlice(), binaryOp);
        } else if (target instanceof WriteGlobalNode) {
            writeGlobal = (WriteGlobalNode) target;
            readGlobal = factory.createReadGlobal(writeGlobal.getName());
            readGlobal.setToken(writeGlobal.getToken());
            binaryOp = factory.createBinaryOperation(op, readGlobal, value);
            retVal = factory.createWriteGlobal(writeGlobal.getName(), binaryOp);
        } else {
            UnCovered.uncoveredException("Unknown");
        }
        return GrammarUtil.make(t.getToken(), slot, retVal);
    }

    public PNode recuFixWriteLocalSlots(PNode tree, int level) {
        Iterator<Node> list = null;
        PNode retVal = tree;
        PNode newNode = null;
        if (tree != null) {
            retVal = var.fixWriteLocalSlot(tree);
            list = tree.getChildren().iterator();
            Node n = null;
            while (list.hasNext()) {
                n = list.next();
                if (n != null) {
                    if (((PNode) n).getToken() != null) {
                        newNode = recuFixWriteLocalSlots((PNode) n, level + 1);
                        GrammarUtil.replace(n, newNode);
                    }
                }
            }
        }

        return retVal;
    }
}
