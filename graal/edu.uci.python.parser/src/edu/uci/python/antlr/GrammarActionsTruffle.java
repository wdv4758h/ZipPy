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
package edu.uci.python.antlr;

import java.math.*;
import java.util.*;
import java.util.List;

import org.antlr.runtime.*;
import org.python.antlr.ast.*;
import org.python.core.*;
import org.python.google.common.collect.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.datatypes.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.expressions.*;
import edu.uci.python.nodes.literals.*;
import edu.uci.python.nodes.statements.*;
import edu.uci.python.nodes.translation.*;
import edu.uci.python.nodes.truffle.*;
import edu.uci.python.runtime.Options;
import edu.uci.python.runtime.datatypes.*;

public class GrammarActionsTruffle {

    public final NodeFactory factory = new NodeFactory();

    public GrammarActionsTruffle() {
        ParserEnvironment.beginScope();
    }

    private static PNode make(Token t, PNode node) {
        node.setToken(t);
        return node;
    }

    private static PNode make(Token t, FrameSlot slot, PNode node) {
        node.setToken(t);
        node.setSlot(slot);
        return node;
    }

    public StatementNode makeYield(Token t, PNode node) {
        return (StatementNode) make(t, factory.createYield(node));
    }

    public PNode makeTuple(Token t, List<PNode> elts, expr_contextType ctx) {
        assert ctx == expr_contextType.Load : "Left hand side node should not reach here!";
        PNode retVal = make(t, factory.createTupleLiteral(elts));
        ((TupleLiteralNode) retVal).setElts(elts);
        return retVal;
    }

    public PNode makeDictComp(Token t, PNode key, PNode paramValue, List<PComprehension> generators) {
        String tmp = "_{" + t.getLine() + "_" + t.getCharPositionInLine() + "}";
        ParserEnvironment.def(tmp);
        PNode value = fixGlobalReadToLocal(paramValue);
// processPComprehension(generators, value);
        return make(t, PNode.EMPTY_NODE);
    }

    private PNode processPComprehension(List<PComprehension> generators, PNode body) {
        PNode retVal = null;
        assert body != null;
        List<PComprehension> reversed = Lists.reverse(generators);

        for (int i = 0; i < reversed.size(); i++) {
            PComprehension comp = reversed.get(i);

            // target and iterator
            Amendable incomplete = (Amendable) fixWriteLocalSlot(comp.getInternalTarget());
            PNode target = incomplete.updateRhs(factory.createRuntimeValueNode());
            PNode iterator = comp.getInternalIter();

            // Just deal with one condition.
            List<PNode> conditions = comp.getInternalIfs();
            PNode condition = (conditions == null || conditions.isEmpty()) ? null : conditions.get(0);

            if (i == 0) {
                // inner most
                retVal = factory.createInnerComprehension(target, iterator, factory.toBooleanCastNode(condition), body);
            } else if (i < reversed.size() - 1) {
                // inner
                retVal = factory.createInnerComprehension(target, iterator, factory.toBooleanCastNode(condition), retVal);
            } else {
                // outer
                retVal = factory.createOuterComprehension(target, iterator, factory.toBooleanCastNode(condition), retVal);
            }
        }
        assert retVal != null;
        return retVal;
    }

    public PNode makeComprehension(Token t, PNode target, PNode iterator, List<PNode> ifs) {
        return new PComprehension(t, target, iterator, ifs);
    }

    public PNode recuFixWriteLocalSlots(PNode tree, int level) {
        Iterator<Node> list = null;
        PNode retVal = tree;
        PNode newNode = null;
        if (tree != null) {
            retVal = fixWriteLocalSlot(tree);
            list = tree.getChildren().iterator();
            Node n = null;
            while (list.hasNext()) {
                n = list.next();
                if (n != null) {
                    if (((PNode) n).getToken() != null) {
                        newNode = recuFixWriteLocalSlots((PNode) n, level + 1);
                        if (newNode != n) {
                            n.replace(newNode);
                        }
                    }
                }
            }
        }

        return retVal;
    }

    public PNode makeIndex(Token t, PNode value) {
        return make(t, factory.createIndex(value));
    }

    public PNode makeIndex(PNode node, PNode value) {
        return make(node.getToken(), factory.createIndex(value));
    }

    public PNode makeCompare(Token t, PNode left, java.util.List<cmpopType> ops, List<PNode> comparators) {
        return make(t, factory.createComparisonOperations(left, ops, comparators));
    }

    public PNode makeSetComp(Token t, PNode paramElt, List<PComprehension> generators) {
        String tmp = "_{" + t.getLine() + "_" + t.getCharPositionInLine() + "}";
        ParserEnvironment.def(tmp);
        PNode elt = fixGlobalReadToLocal(paramElt);
// processPComprehension(generators, elt);
        return make(t, PNode.EMPTY_NODE);
    }

    public PNode makeAttribute(Token t, PNode value, PNode attr, expr_contextType ctx) {
        PNode retVal = null;
        if (ctx != expr_contextType.Load) {
            retVal = make(t, factory.createAttributeUpdate(value, attr.getText(), PNode.DUMMY_NODE));
        } else {
            retVal = make(t, factory.createAttributeRef(value, attr.getText()));
        }
        return retVal;
    }

    public PNode makeListComp(Token t, PNode paramElt, List<PComprehension> generators) {
        String tmp = "_[" + t.getLine() + "_" + t.getCharPositionInLine() + "]";
        FrameSlot slot = ParserEnvironment.def(tmp);

        PNode elt = fixGlobalReadToLocal(paramElt);
        assert generators.size() <= 1 : "More than one generator!";
        ComprehensionNode comprehension = (ComprehensionNode) processPComprehension(generators, elt);
        return make(t, slot, factory.createListComprehension(comprehension));
    }

    public PNode makeList(Token t, List<PNode> elts, expr_contextType ctx) {
        ListLiteralNode retVal = (ListLiteralNode) make(t, factory.createListLiteral(elts));
        retVal.setElts(elts);
        return retVal;
    }

    public PNode makeDict(Token t, List<PNode> keys, List<PNode> values) {
        return make(t, factory.createDictLiteral(keys, values));
    }

    public PNode makeStr(Token t, Object s) {
        return make(t, factory.createStringLiteral((PyString) s));
    }

    public PNode makeFalse(Token t) {
        return make(t, factory.createBooleanLiteral(false));
    }

    public PNode makeTrue(Token t) {
        return make(t, factory.createBooleanLiteral(true));
    }

    public PNode makeNone(Token t) {
        return make(t, factory.createNoneLiteral());
    }

    public PNode makeUnaryOp(Token t, unaryopType op, PNode operand) {
        return make(t, factory.createUnaryOperation(op, operand));
    }

    public PNode makeIfExp(Token t, PNode test, PNode body, PNode orelse) {
        return make(t, factory.createIfExpNode(test, body, orelse));
    }

    public List<PNode> makeElse(List<?> elseSuite, PNode elif) {
        if (elseSuite != null) {
            return GrammarUtilities.castStmts(elseSuite);
        } else if (elif == null) {
            return new ArrayList<>();
        }
        List<PNode> s = new ArrayList<>();
        s.add(GrammarUtilities.castStmt(elif));
        return s;
    }

    public StatementNode makeIf(Token t, PNode test, List<PNode> body, List<PNode> orelse) {
        BlockNode thenPart = factory.createBlock(body);
        BlockNode elsePart = factory.createBlock(orelse);
        return (StatementNode) make(t, factory.createIf(factory.toBooleanCastNode(test), thenPart, elsePart));
    }

    public PNode makeGlobal(Token t, List<String> names, List<PNode> nameNodes) {
        for (String name : names) {
            ParserEnvironment.defGlobal(name);
            ParserEnvironment.localGlobals.add(name);
        }
        return make(t, PNode.EMPTY_NODE);
    }

    public PAlias makeAliasDotted(List<PNode> nameNodes, Token paramAsName) {
        PAlias retVal = null;
        PNode asname = makeNameNode(paramAsName);
        String snameNode = GrammarUtilities.dottedNameListToString(nameNodes);
        retVal = new PAlias(nameNodes, snameNode, asname);
        if (asname != null) {
            retVal.setSlot(ParserEnvironment.def(asname.getText()));
        } else {
            retVal.setSlot(ParserEnvironment.def(snameNode));
        }
        return retVal;
    }

    public PAlias makeAliasImport(Token paramName, Token paramAsName) {
        PNode name = makeNameNode(paramName);
        PNode asname = (paramAsName != null) ? makeNameNode(paramAsName) : null;
        PAlias retVal = new PAlias(name, asname);
        if (paramAsName != null) {
            retVal.setSlot(ParserEnvironment.def(asname.getText()));
        } else {
            retVal.setSlot(ParserEnvironment.def(name.getText()));
        }
        return retVal;
    }

    public StatementNode makeImportFrom(Token t, String module, List<PNode> moduleNames, List<PAlias> aliases, Integer level) {
        FrameSlot[] slots = new FrameSlot[aliases.size()];
        String[] names = new String[aliases.size()];
        for (int i = 0; i < aliases.size(); i++) {
            slots[i] = aliases.get(i).getSlot();
            names[i] = aliases.get(i).getInternalName();
        }
        return (StatementNode) make(t, factory.createImport(slots, module, names));
    }

    public StatementNode makeImport(Token t, List<PAlias> aliases) {
        FrameSlot[] slots = new FrameSlot[aliases.size()];
        String[] names = new String[aliases.size()];
        for (int i = 0; i < aliases.size(); i++) {
            slots[i] = aliases.get(i).getSlot();
            names[i] = aliases.get(i).getInternalName();
        }
        return (StatementNode) make(t, factory.createImport(slots, null, names));
    }

    public PNode makeExpr(Token t, PNode value) {
        return make(t, value);
    }

    public StatementNode makeReturn(Token t, PNode paramValue) {
        StatementNode retVal = null;
        PNode write = null;
        PNode value = fixGlobalReadToLocal(paramValue);

        if (value == null) {
            retVal = factory.createReturn();
        } else if (TranslationOptions.RETURN_VALUE_IN_FRAME) {
            write = factory.createWriteLocal(value, ParserEnvironment.getReturnSlot());
            retVal = factory.createFrameReturn(write);
        } else {
            retVal = factory.createExplicitReturn(value);
        }
        return (StatementNode) make(t, retVal);
    }

    public StatementNode makeBreak(Token t) {
        return (StatementNode) make(t, factory.createBreak());
    }

    public StatementNode makePrint(Token t, PNode dest, List<PNode> values, Boolean nl) {
        StatementNode retVal = null;

        if (values.size() == 1 && values.get(0) instanceof TupleLiteralNode) {
            TupleLiteralNode tuple = (TupleLiteralNode) values.get(0);
            List<PNode> tupleElts = tuple.getElts();
            retVal = factory.createPrint(tupleElts, nl);
        } else {
            retVal = factory.createPrint(values, nl);
        }

        return (StatementNode) make(t, retVal);
    }

    public PNode makeAugAssign(PNode t, PNode target, operatorType op, PNode value) {
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
            throw new NotCovered();
        }
        return make(t.getToken(), slot, retVal);
    }

    public PNode makeName(PNode tree, String id, expr_contextType ctx) {
        return make(tree.getToken(), makeName(tree.getToken(), id, ctx));
    }

    private PNode makeNameRead(Token t) {
        PNode retVal = null;
        FrameSlot slot = ParserEnvironment.find(t.getText());

        if (slot == null && ParserEnvironment.scopeLevel > 1) {
            slot = ParserEnvironment.probeEnclosingScopes(t.getText());
        }
        if (slot != null) {
            if (slot instanceof EnvironmentFrameSlot) {
                retVal = make(t, factory.createReadEnvironment(slot, ((EnvironmentFrameSlot) slot).getLevel()));
            } else {
                retVal = make(t, factory.createReadLocal(slot));
            }
            retVal.setSlot(slot);
        } else {
            retVal = make(t, factory.createReadGlobal(t.getText()));
        }
        return retVal;
    }

    private PNode makeNameWrite(Token t, PNode rhs) {
        PNode retVal = null;
        String id = t.getText();
        FrameSlot slot = null;

        if ((ParserEnvironment.scopeLevel == 1 && GlobalScope.getInstance().isGlobalOrBuiltin(id)) || ParserEnvironment.localGlobals.contains(id)) {
            retVal = make(t, factory.createWriteGlobal(id, rhs));
        } else {
            // slot = ParserEnvironment.def(id);
            retVal = make(t, slot, factory.createWriteLocal(rhs, slot));
        }
        return retVal;
    }

    public PNode makeName(Token t, String id, expr_contextType ctx) {
        PNode retVal = null;
        FrameSlot slot = null;

        if (ctx == expr_contextType.Param) {
            if (ParserEnvironment.scopeLevel == 1) {
                // Module global scope
                /**
                 * Variables in module's scope are also treated as globals This is why slot is not
                 * set for variables in module's scope WriteGlobal or ReadGlobal
                 */
                if (!GlobalScope.getInstance().isGlobalOrBuiltin(id)) {
                    slot = ParserEnvironment.def(id);
                }
            } else if (!ParserEnvironment.localGlobals.contains(id)) {
                // function scope
                slot = ParserEnvironment.def(id);
            }

            ReadArgumentNode right = new ReadArgumentNode(slot.getIndex());
            retVal = factory.createWriteLocal(right, slot);
        } else if (ctx != expr_contextType.Load) {
            retVal = makeNameWrite(t, PNode.DUMMY_NODE);
        } else {
            retVal = makeNameRead(t);
        }
        return make(t, retVal);
    }

    public PNode makeModule(Token t, List<?> stmts) {
        PNode retVal = factory.createModule(GrammarUtilities.castStmts(stmts), ParserEnvironment.endScope());
        retVal.setToken(t);
        return retVal;
    }

    public PNode reAssignElt(PNode elt) {
        PNode retVal = elt;
        FrameSlot slot = null;

        if (retVal instanceof WriteLocalNode) {
            slot = ParserEnvironment.def(retVal.getText());
            retVal = make(elt.getToken(), slot, factory.createWriteLocal(((WriteLocalNode) retVal).getRhs(), slot));
        } else if (retVal instanceof ReadLocalNode || retVal instanceof ReadGlobalNode) {
            retVal = make(elt.getToken(), makeNameRead(retVal.getToken()));
        }

        if (retVal != null) {
            Iterable<Node> list = retVal.getChildren();
            PNode newNode = null;
            for (Node n : list) {
                newNode = reAssignElt((PNode) n);
                if (newNode != n) {
                    n.replace(newNode);
                }
            }
        }
        return retVal;
    }

    public PNode makeGeneratorExp(Token t, PNode paramElt, List<PComprehension> generators) {
        PComprehension generator = generators.get(0);
        generator.setInternalTarget(fixWriteLocalSlot(generator.getInternalTarget()));
        PNode elt = reAssignElt(paramElt);
        ComprehensionNode comprehension = (ComprehensionNode) processPComprehension(generators, elt);
        GeneratorNode gnode = factory.createGenerator(comprehension, factory.createReadLocal(ParserEnvironment.getReturnSlot()));
        FrameDescriptor fd = ParserEnvironment.currentFrame;
        return make(t, factory.createGeneratorExpression(gnode, fd));
    }

    public List<PNode> makeAssignTargets(PNode paramLhs, List<?> rhs) {
        PNode lhs = fixWriteLocalSlot(paramLhs);
        List<PNode> e = new ArrayList<>();
        GrammarUtilities.checkAssign(lhs);

        e.add(lhs);
        for (int i = 0; i < rhs.size() - 1; i++) {
            PNode r = GrammarUtilities.castExpr(rhs.get(i));
            GrammarUtilities.checkAssign(r);
            e.add(r);
        }
        return e;
    }

    public PNode makeAssignValue(List<?> rhs) {
        PNode retVal = null;
        PNode value = GrammarUtilities.castExpr(rhs.get(rhs.size() - 1));
        retVal = recurseSetContext(value, expr_contextType.Load);
        value.replace(retVal);
        return retVal;
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

    public boolean isDecomposable(PNode node) {
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

            processSingleWrite = fixWriteLocalSlot(targets.get(i));
            targets.get(i).replace(processSingleWrite);
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

    public PNode fixWriteLocalSlot(PNode broken) {
        PNode retVal = broken;
        Token writeLocalToken = null;
        FrameSlot slot = null;
        if (retVal instanceof WriteLocalNode && (retVal.getSlot() == null || ((WriteLocalNode) retVal).getSlot() == null)) {
            writeLocalToken = retVal.getToken();
            slot = ParserEnvironment.def(writeLocalToken.getText());
            retVal = make(writeLocalToken, slot, factory.createWriteLocal(PNode.DUMMY_NODE, slot));
        }
        return retVal;
    }

    private List<PNode> walkLeftHandSideList(List<PNode> lhs) throws Exception {
        List<PNode> writes = new ArrayList<>();
        List<PNode> additionalWrites = new ArrayList<>();

        for (int i = 0; i < lhs.size(); i++) {
            PNode target = lhs.get(i);
            target = fixWriteLocalSlot(target);
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

    private PNode writeLocalToRead(PNode node) {
        PNode retVal = node;
        FrameSlot slot = null;

        if (node instanceof WriteLocalNode) {
            slot = ParserEnvironment.find(node.getText());
            if (node.getSlot() != null) {
                retVal = factory.createReadLocal(node.getSlot());
            } else if (slot != null) {
                retVal = factory.createReadLocal(slot);
            } else {
                retVal = factory.createReadGlobal(node.getText());
            }
        }
        return make(node.getToken(), slot, retVal);
    }

    private BlockNode transformUnpackingAssignment(List<PNode> lhs, PNode right) throws Exception {
        /**
         * Transform a, b = c. <br>
         * To: temp_c = c; a = temp_c[0]; b = temp_d[1]
         */
        List<PNode> writes = new ArrayList<>();
        PNode updateRight = null;
        Amendable tempWrite = (Amendable) makeTemporaryWrite();
        updateRight = tempWrite.updateRhs(writeLocalToRead(right));
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
            throw new NotCovered();
        }

        return make(target.getToken(), slot, retVal);
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
                firstTarget = fixWriteLocalSlot(firstTarget);
                retVal = processSingleAssignment(firstTarget, rhs);
            } else {
                List<PNode> assignments = new ArrayList<>();
                for (PNode targetnode : lhs) {
                    validTarget = processSingleAssignment(fixWriteLocalSlot(targetnode), rhs);
                    assignments.add(validTarget);
                }

                retVal = factory.createBlock(assignments);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new NotCovered();
        }
        return make(t, retVal);
    }

    public PNode makeAssign(PNode t, List<PNode> targets, PNode value) {
        return makeAssign(t.getToken(), targets, value);
    }

    public List<PNode> makeModuleNameNode(List<?> dots, List<PNode> names) {
        List<PNode> result = new ArrayList<>();
        if (dots != null) {
            for (Object o : dots) {
                Token tok = (Token) o;
                result.add(makeName(tok, tok.getText(), expr_contextType.Load));
            }
        }
        if (null != names) {
            result.addAll(names);
        }
        return result;
    }

    public List<PNode> makeDottedName(Token top, List<?> paramAttrs) {
        List<PNode> attrs = GrammarUtilities.castExprs(paramAttrs);
        List<PNode> result = new ArrayList<>();
        result.add(makeName(top, top.getText(), expr_contextType.Load));
        if (attrs != null) {
            for (PNode attr : attrs) {
                Token token = attr.getToken();
                result.add(makeName(token, token.getText(), expr_contextType.Load));
            }
        }
        return result;
    }

    public int makeLevel(List<?> lev) {
        if (lev == null) {
            return 0;
        }
        return lev.size();
    }

    public List<PAlias> makeStarAlias(Token t) {
        List<PAlias> result = new ArrayList<>();
        result.add(new PAlias(t, "*", null));
        return result;
    }

    public List<PAlias> makeAliases(List<PAlias> atypes) {
        if (atypes == null) {
            return new ArrayList<>();
        }
        return atypes;
    }

    public List<PNode> makeBases(PNode etype) {
        List<PNode> result = new ArrayList<>();
        if (etype != null) {
            if (etype instanceof TupleLiteralNode) {
                return ((TupleLiteralNode) etype).getElts();
            }
            result.add(etype);
        }
        return result;
    }

    public List<String> makeNames(List<?> names) {
        List<String> s = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            s.add(((Token) names.get(i)).getText());
        }
        return s;
    }

    public PNode makeNameNode(Token t) {
        if (t == null) {
            return null;
        }
        return makeName(t, t.getText(), expr_contextType.Load);
    }

    public List<PNode> makeNameNodes(List<?> names) {
        List<PNode> s = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            s.add(makeNameNode((Token) names.get(i)));
        }
        return s;
    }

    public PNode makeDottedAttr(Token nameToken, List<?> attrs) {
        PNode current = makeName(nameToken, nameToken.getText(), expr_contextType.Load);
        for (Object o : attrs) {
            Token t = (Token) o;
            current = makeAttribute(t, current, cantBeNoneName(t), expr_contextType.Load);
        }
        return current;
    }

    public StatementNode makeWhile(Token t, PNode test, List<?> body, List<?> orelse) {
        if (test == null) {
            return ErrorHandler.errorStmt(t);
        }
        List<PNode> o = GrammarUtilities.castStmts(orelse);
        List<PNode> b = GrammarUtilities.castStmts(body);
        BlockNode bodyPart = factory.createBlock(b);
        BlockNode orelsePart = factory.createBlock(o);
        return (StatementNode) make(t, factory.createWhile(factory.toBooleanCastNode(test), bodyPart, orelsePart));
    }

    private StatementNode dirtySpecialization(PNode target, PNode iter, BlockNode bodyPart, BlockNode orelsePart) {
        StatementNode forNode;
        if (Options.OptimizeNode) {
            if (iter instanceof CallBuiltInWithOneArgNoKeywordNode && ((CallBuiltInWithOneArgNoKeywordNode) iter).getName().equals("range")) {
                forNode = factory.createForRangeWithOneValue(target, ((CallBuiltInWithOneArgNoKeywordNode) iter).getArgument(), bodyPart, orelsePart);
            } else if (iter instanceof CallBuiltInWithTwoArgsNoKeywordNode && ((CallBuiltInWithTwoArgsNoKeywordNode) iter).getName().equals("range")) {
                forNode = factory.createForRangeWithTwoValues(target, ((CallBuiltInWithTwoArgsNoKeywordNode) iter).getArg0(), ((CallBuiltInWithTwoArgsNoKeywordNode) iter).getArg1(), bodyPart,
                                orelsePart);
            } else {
                forNode = factory.createFor(target, iter, bodyPart, orelsePart);
            }
        } else {
            forNode = factory.createFor(target, iter, bodyPart, orelsePart);
        }
        return forNode;
    }

    public StatementNode makeFor(Token t, PNode target, PNode iter, List<?> paramBody, List<?> paramOrelse) {
        if (target == null || iter == null) {
            return ErrorHandler.errorStmt(t);
        }
        List<PNode> lhs = new ArrayList<>();
        lhs.add(target);

        List<PNode> targets = null;
        try {
            targets = walkLeftHandSideList(lhs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotCovered();
        }

        Amendable incomplete = (Amendable) targets.remove(0);

        List<PNode> b = GrammarUtilities.castStmts(paramBody);
        b.addAll(0, targets);
        List<PNode> body = new ArrayList<>();

        for (PNode n : b) {
            body.add(fixGlobalReadToLocal(n));
        }
        BlockNode bodyPart = factory.createBlock(body);

        List<PNode> o = GrammarUtilities.castStmts(paramOrelse);
        List<PNode> orelse = new ArrayList<>();
        for (PNode n : o) {
            orelse.add(fixGlobalReadToLocal(n));
        }
        BlockNode orelsePart = factory.createBlock(orelse);

        PNode runtimeValue = factory.createRuntimeValueNode();
        PNode iteratorWrite = incomplete.updateRhs(runtimeValue);

        return (StatementNode) make(t, dirtySpecialization(iteratorWrite, iter, bodyPart, orelsePart));
    }

    public StatementNode makeFuncdef(Token t, Token nameToken, ParametersNode args, List<?> funcStatements, List<?> decorators) {
        StatementNode retVal = null;
        boolean isGenerator = false;
        if (nameToken == null) {
            return ErrorHandler.errorStmt(t);
        }
        PNode n = cantBeNoneName(nameToken);
        ParametersNode a = null;
        if (args != null) {
            a = args;
        } else {
            a = makeArguments(t, new ArrayList<PNode>(), null, null, new ArrayList<PNode>());
        }

        List<PNode> s = GrammarUtilities.castStmts(funcStatements);
        List<PNode> d = GrammarUtilities.castExprs(decorators);

        if (Options.OptimizeNode) {
            s.addAll(0, args.getTupleAssignment());
        }

        StatementNode body = factory.createBlock(s);

        PNode returnValue = factory.createReadLocal(ParserEnvironment.getReturnSlot());
        FrameDescriptor fd = ParserEnvironment.endScope();
        FrameSlot slot = ParserEnvironment.def(n.getText());

        if (isGenerator) {
            body = new ASTLinearizer((BlockNode) body).linearize();
            RootNode genRoot = factory.createGeneratorRoot(a, body, returnValue);
            CallTarget ct = Truffle.getRuntime().createCallTarget(genRoot, fd);
            retVal = factory.createFunctionDef(slot, n.getText(), a, ct, genRoot);
        } else {
            FunctionRootNode funcRoot = factory.createFunctionRoot(a, body, returnValue);
            CallTarget ct = Truffle.getRuntime().createCallTarget(funcRoot, fd);
            retVal = factory.createFunctionDef(slot, n.getText(), a, ct, funcRoot);
        }
        return (StatementNode) make(t, slot, retVal);
    }

    public PNode recurseSetContext(PNode tree, expr_contextType context) {
        Iterable<Node> list = null;
        PNode retVal = tree;
        PNode newNode = null;
        PNode value = null;
        PNode slice = null;
        FrameSlot slot = null;

        if (context == expr_contextType.Load) {
            if (tree instanceof WriteGlobalNode) {
                if (GlobalScope.getInstance().isGlobalOrBuiltin(((WriteGlobalNode) tree).getName())) {
                    retVal = make(tree.getToken(), factory.createReadGlobal(((WriteGlobalNode) tree).getName()));
                } else {
                    slot = ParserEnvironment.find(tree.getText());
                    if (tree.getSlot() != null) {
                        retVal = make(tree.getToken(), factory.createReadLocal(tree.getSlot()));
                    } else if (slot != null) {
                        retVal = make(tree.getToken(), factory.createReadLocal(slot));
                    }
                }
            } else if (tree instanceof WriteLocalNode) {
                retVal = make(tree.getToken(), writeLocalToRead(tree));
            } else if (tree instanceof SubscriptStoreNode) {
                value = ((SubscriptStoreNode) tree).getPrimary();
                slice = ((SubscriptStoreNode) tree).getSlice();
                retVal = make(tree.getToken(), factory.createSubscriptLoad(value, slice));
            } else if (tree instanceof ListComprehensionNode) {
                retVal = make(tree.getToken(), fixGlobalReadToLocal(tree));
            }
        } else {
            if (tree instanceof ReadGlobalNode) {
                retVal = make(tree.getToken(), factory.createWriteGlobal(tree.getText(), PNode.DUMMY_NODE));
            } else if (tree instanceof ReadLocalNode) {
                retVal = make(tree.getToken(), factory.createWriteLocal(PNode.DUMMY_NODE, tree.getSlot()));
            } else if (tree instanceof SubscriptLoadNode) {
                value = ((SubscriptLoadNode) tree).getPrimary();
                slice = ((SubscriptLoadNode) tree).getSlice();
                retVal = make(tree.getToken(), factory.createSubscriptStore(value, slice, PNode.DUMMY_NODE));
            }

        }

        if (tree != null && !(tree instanceof ListComprehensionNode) && !(tree instanceof GeneratorNode)) {
            // && (!(tree instanceof DictComp)) && (!(tree instanceof SetComp)))
            if (retVal == tree) {
                retVal = fixWriteLocalSlot(tree);
            }
            list = retVal.getChildren();

            for (Node n : list) {
                newNode = recurseSetContext((PNode) n, context);
                if (newNode != n) {
                    n.replace(newNode);
                }
            }

        }
        return retVal;
    }

    private PNode fixGlobalReadToLocal(PNode tree) {
        PNode retVal = tree;
        PNode newNode = null;
        Iterable<Node> list = null;

        if (retVal instanceof ReadGlobalNode) {
            retVal = makeNameRead(retVal.getToken());
        }
        if (retVal != null) {
            list = tree.getChildren();

            for (Node n : list) {
                if (!(n instanceof ListComprehensionNode) && !(n instanceof GeneratorNode)) {
                    newNode = fixGlobalReadToLocal((PNode) n);
                    if (newNode != n) {
                        n.replace(newNode);
                    }
                }
            }
        }
        return retVal;
    }

    private PNode readLocalToGlobal(PNode readNode) {
        PNode retVal = readNode;
        FrameSlot slot = null;
        PNode newNode = null;

        if (retVal instanceof ReadLocalNode) {
            if (ParserEnvironment.scopeLevel > 1) {
                slot = ParserEnvironment.probeEnclosingScopes(retVal.getText());
            }

            if (slot != null) {
                if (slot instanceof EnvironmentFrameSlot) {
                    retVal = make(readNode.getToken(), slot, factory.createReadEnvironment(slot, ((EnvironmentFrameSlot) slot).getLevel()));
                }
            } else {
                retVal = make(readNode.getToken(), factory.createReadGlobal(retVal.getText()));
            }
        }

        if (retVal != null) {
            Iterable<Node> list = retVal.getChildren();

            for (Node n : list) {
                newNode = readLocalToGlobal((PNode) n);
                if (newNode != n) {
                    n.replace(newNode);
                }
            }
        }
        return retVal;
    }

    public ParametersNode makeArguments(Token t, List<PNode> paramArgs, PNode vararg, PNode kwarg, List<PNode> paramDefaults) {
        ParametersNode retVal = null;
        List<String> paramNames = new ArrayList<>();
        List<PNode> tupleAssignments = new ArrayList<>();
        PNode tupleAssign = null;
        List<PNode> targets = null;
        List<PNode> args = (paramArgs != null) ? paramArgs : new ArrayList<PNode>();
        List<PNode> defaults = new ArrayList<>();

        if (paramDefaults != null) {
            for (PNode def : paramDefaults) {
                defaults.add(readLocalToGlobal(def));
            }
        }

        for (PNode arg : args) {
            paramNames.add(arg.getText());

            if (arg instanceof TupleLiteralNode) {
                targets = new ArrayList<>();
                targets.add(arg);
                tupleAssign = makeAssign(arg, targets, makeName(arg, arg.getText(), expr_contextType.Load));
                tupleAssignments.add(tupleAssign);
            }
        }

        if (defaults.isEmpty()) {
            if (args.size() == 1) {
                retVal = factory.createParametersOfSizeOne(args.get(0), paramNames);
            } else if (args.size() == 2) {
                retVal = factory.createParametersOfSizeTwo(args.get(0), args.get(1), paramNames);
            } else {
                retVal = factory.createParametersWithNoDefaults(args, paramNames);
            }
        } else {
            retVal = factory.createParametersWithDefaults(args, defaults, paramNames);
        }
        retVal.setTupleAssignment(tupleAssignments);
        return (ParametersNode) make(t, retVal);
    }

    public String cantBeNone(Token t) {
        if (t == null || t.getText().equals("None")) {
            ErrorHandler.error("can't be None", t);
        }
        return t.getText();
    }

    public PNode cantBeNoneName(Token t) {
        if (t == null || t.getText().equals("None")) {
            ErrorHandler.error("can't be None", t);
        }
        return makeName(t, t.getText(), expr_contextType.Load);
    }

    public void cantBeNone(PNode e) {
        if (e.getText().equals("None")) {
            ErrorHandler.error("can't be None", e.getToken());
        }
    }

    public ParametersNode makeArgumentsType(Token t, List<?> params, Token snameToken, Token knameToken, List<?> defaults) {
        List<PNode> p = GrammarUtilities.castExprs(params);
        List<PNode> d = GrammarUtilities.castExprs(defaults);
        PNode s = (snameToken == null) ? null : cantBeNoneName(snameToken);
        PNode k = (knameToken == null) ? null : cantBeNoneName(knameToken);
        return makeArguments(t, p, s, k, d);
    }

    public List<PNode> extractArgs(List<?> args) {
        return GrammarUtilities.castExprs(args);
    }

    public List<PNode> makeKeywords(List<?> args) {
        List<PNode> keywords = new ArrayList<>();
        PNode singleKeyword = null;
        if (args != null) {
            for (Object o : args) {
                List<?> e = (List<?>) o;
                Object k = e.get(0);
                Object v = e.get(1);
                GrammarUtilities.checkAssign(GrammarUtilities.castExpr(k));
                if (k instanceof PNode) {
                    singleKeyword = factory.createKeywordLiteral(GrammarUtilities.castExpr(v), ((PNode) k).getText());
                    keywords.add(singleKeyword);
                } else {
                    ErrorHandler.error("keyword must be a name", ((PNode) k).getToken());
                }
            }
        }
        return keywords;
    }

    public PNode makeFloat(Token t) {
        return make(t, factory.createDoubleLiteral(Double.valueOf(t.getText())));
    }

    public PNode makeComplex(Token t) {
        String s = t.getText();
        s = s.substring(0, s.length() - 1);
        PyComplex pyComplex = Py.newImaginary(Double.valueOf(s));
        PComplex complex = new PComplex(pyComplex.real, pyComplex.imag);
        return make(t, factory.createComplexLiteral(complex));
    }

    public PNode makeInt(Token t) {
        PNode retVal = null;
        long l;
        String s = t.getText();
        int ndigits = 0;
        int i = 0;

        int radix = 10;
        if (s.startsWith("0x") || s.startsWith("0X")) {
            radix = 16;
            s = s.substring(2, s.length());
        } else if (s.startsWith("0o") || s.startsWith("0O")) {
            radix = 8;
            s = s.substring(2, s.length());
        } else if (s.startsWith("0b") || s.startsWith("0B")) {
            radix = 2;
            s = s.substring(2, s.length());
        } else if (s.startsWith("0")) {
            radix = 8;
        }
        if (s.endsWith("L") || s.endsWith("l")) {
            s = s.substring(0, s.length() - 1);
            retVal = factory.createBigIntegerLiteral(new BigInteger(s, radix));
        } else {
            ndigits = s.length();
            i = 0;
            while (i < ndigits && s.charAt(i) == '0') {
                i++;
            }
            if ((ndigits - i) > 11) {
                retVal = factory.createBigIntegerLiteral(new BigInteger(s, radix));
            } else {
                l = Long.valueOf(s, radix).longValue();
                if (l > 0xffffffffL || (l > Integer.MAX_VALUE)) {
                    retVal = factory.createBigIntegerLiteral(new BigInteger(s, radix));
                } else {
                    retVal = factory.createIntegerLiteral((int) l);
                }
            }
        }
        return make(t, retVal);
    }

    public PNode makeCall(Token t, PNode func) {
        return makeCall(t, func, null, null, null, null);
    }

    public PNode makeCall(Token t, PNode func, List<?> args, List<?> keywords, PNode starargs, PNode kwargs) {
        PNode retVal = null;
        if (func == null) {
            return ErrorHandler.errorExpr(t);
        }

        List<PNode> a = GrammarUtilities.castExprs(args);
        PNode[] argumentsArray = a.toArray(new PNode[a.size()]);
        List<PNode> k = makeKeywords(keywords);
        PNode[] keywordsArray = k.toArray(new PNode[k.size()]);
        PCallable builtIn = null;
        PNode callee = func;

        if (callee.getParent() instanceof AttributeLoadNode) {
            AttributeLoadNode attr = (AttributeLoadNode) callee.getParent();
            retVal = factory.createAttributeCall(attr.getPrimary(), argumentsArray, attr.getName());

        } else if (argumentsArray.length == 1 && keywordsArray.length == 0) {
            // Specializing call node.
            if (Options.OptimizeNode) {
                if (callee instanceof ReadGlobalNode && (builtIn = GlobalScope.getTruffleBuiltIns().lookupMethod(((ReadGlobalNode) callee).getName())) != null) {
                    retVal = factory.createCallBuiltInWithOneArgNoKeyword(builtIn, ((ReadGlobalNode) callee).getName(), argumentsArray[0]);
                } else {
                    retVal = factory.createCallWithOneArgumentNoKeyword(callee, argumentsArray[0]);
                }
            } else {
                retVal = factory.createCallWithOneArgumentNoKeyword(callee, argumentsArray[0]);
            }
        } else if (argumentsArray.length == 2 && keywordsArray.length == 0) {
            if (Options.OptimizeNode) {
                if (callee instanceof ReadGlobalNode && (builtIn = GlobalScope.getTruffleBuiltIns().lookupMethod(((ReadGlobalNode) callee).getName())) != null) {
                    retVal = factory.createCallBuiltInWithTwoArgsNoKeyword(builtIn, ((ReadGlobalNode) callee).getName(), argumentsArray[0], argumentsArray[1]);
                } else {
                    retVal = factory.createCallWithTwoArgumentsNoKeyword(callee, argumentsArray[0], argumentsArray[1]);
                }
            } else {
                retVal = factory.createCallWithTwoArgumentsNoKeyword(callee, argumentsArray[0], argumentsArray[1]);
            }
        } else if (Options.OptimizeNode) {
            if (callee instanceof ReadGlobalNode && (builtIn = GlobalScope.getTruffleBuiltIns().lookupMethod(((ReadGlobalNode) callee).getName())) != null) {
                retVal = factory.createCallBuiltIn(builtIn, ((ReadGlobalNode) callee).getName(), argumentsArray, keywordsArray);
            } else {
                retVal = factory.createCall(callee, argumentsArray, keywordsArray);
            }
        } else {
            retVal = factory.createCall(callee, argumentsArray, keywordsArray);
        }
        return make(t, retVal);
    }

    public PNode negate(PNode t, PNode o) {
        return negate(t.getToken(), o);
    }

    public PNode negate(Token t, PNode o) {
        return make(t, makeUnaryOp(t, unaryopType.USub, o));
    }

    public PNode makeSubscript(Token t, PNode pPrimary, PNode paramSlice, expr_contextType ctx) {
        PNode retVal = null;
        PNode slice = paramSlice;
        PNode primary = pPrimary;
        if (ctx == expr_contextType.Load) {
            retVal = factory.createSubscriptLoad(primary, slice);
        } else if (ctx == expr_contextType.Store) {
            primary = makeNameRead(primary.getToken());
            retVal = factory.createSubscriptStore(primary, slice, PNode.DUMMY_NODE);
        } else {
            retVal = factory.createSubscriptLoad(primary, slice);
        }
        return make(t, retVal);
    }

    public PNode makeSubscript(PNode lower, Token colon, PNode upper, PNode sliceop) {

        boolean isSlice = ((colon != null) || (sliceop != null));
        PNode retVal = null;
        PNode s = (lower != null) ? GrammarUtilities.castExpr(lower) : null;
        PNode e = ((colon != null) && (upper != null)) ? GrammarUtilities.castExpr(upper) : null;
        PNode o = (sliceop != null) ? GrammarUtilities.castExpr(sliceop) : null;
        Token tok = (s == null) ? colon : s.getToken();

        if (isSlice) {
            if (s == null || s instanceof NoneLiteralNode) {
                s = factory.createIntegerLiteral(Integer.MIN_VALUE);
            }
            if (e == null || e instanceof NoneLiteralNode) {
                e = factory.createIntegerLiteral(Integer.MIN_VALUE);
            }
            if (o == null || o instanceof NoneLiteralNode) {
                o = factory.createIntegerLiteral(1);
            }
            retVal = factory.createSlice(s, e, o);

        } else {
            retVal = makeIndex(tok, s);
        }
        return make(tok, retVal);
    }

    public PNode makePowerSpecific(PNode parent, Object o) {
        PNode retVal = parent;
        PNode newNode = recurseSetContext(parent, expr_contextType.Load);

        if (newNode != parent) {
            parent.replace(newNode);
            retVal = newNode;
        }

        if (o instanceof CallBuiltInNode) {
            retVal = (PNode) o;
        } else if (o instanceof CallBuiltInWithOneArgNoKeywordNode) {
            retVal = (PNode) o;
        } else if (o instanceof CallBuiltInWithTwoArgsNoKeywordNode) {
            retVal = (PNode) o;
        } else if (o instanceof CallNode) {
            retVal = (PNode) o;
        } else if (o instanceof CallWithOneArgumentNoKeywordNode) {
            retVal = (PNode) o;
        } else if (o instanceof CallWithTwoArgumentsNoKeywordNode) {
            retVal = (PNode) o;
        } else if (o instanceof AttributeCallNode) {
            AttributeCallNode c = (AttributeCallNode) o;
            c = c.updatePrimary(((AttributeLoadNode) parent).getPrimary());
            newNode.setParent(c);
            c.setToken(((AttributeCallNode) o).getToken());
            ((AttributeCallNode) o).replace(c);
            retVal = c;
        } else if (o instanceof SubscriptLoadNode) {
            SubscriptLoadNode c = (SubscriptLoadNode) o;
            c = (SubscriptLoadNode) factory.createSubscriptLoad(newNode, c.getSlice());
            newNode.setParent(c);
            c.setToken(((SubscriptLoadNode) o).getToken());
            ((SubscriptLoadNode) o).replace(c);
            retVal = c;
        } else if (o instanceof SubscriptStoreNode) {
            retVal = (PNode) o;
        } else if (o instanceof AttributeLoadNode) {
            AttributeLoadNode c = (AttributeLoadNode) o;
            c = (AttributeLoadNode) factory.createAttributeRef(newNode, c.getName());
            newNode.setParent(c);
            c.setToken(((AttributeLoadNode) o).getToken());
            ((AttributeLoadNode) o).replace(c);
            retVal = c;
        }
        return retVal;
    }

    public List<cmpopType> makeCmpOps(List<?> cmps) {
        List<cmpopType> result = new ArrayList<>();
        if (cmps != null) {
            for (Object o : cmps) {
                result.add((cmpopType) o);
            }
        }
        return result;
    }

    public PNode makeBoolOp(Token t, PNode left, boolopType op, List<?> right) {
        return make(t, factory.createBooleanOperations(GrammarUtilities.castExpr(left), op, GrammarUtilities.castExprs(right)));
    }

    public PNode makeBinOp(Token t, PNode pleft, operatorType op, List<?> rights) {
        PNode right = GrammarUtilities.castExpr(rights.get(0));
        PNode left = GrammarUtilities.castExpr(pleft);
        PNode current = make(t, factory.createBinaryOperation(op, left, right));
        for (int i = 1; i < rights.size(); i++) {
            right = GrammarUtilities.castExpr(rights.get(i));
            current = make(t, factory.createBinaryOperation(op, current, right));
        }
        return current;
    }

    public PNode makeBinOp(Token t, PNode pleft, List<?> ops, List<?> rights, List<?> toks) {
        PNode left = GrammarUtilities.castExpr(pleft);
        PNode right = GrammarUtilities.castExpr(rights.get(0));
        operatorType op = (operatorType) ops.get(0);
        PNode current = make(t, factory.createBinaryOperation(op, left, right));
        for (int i = 1; i < rights.size(); i++) {
            right = GrammarUtilities.castExpr(rights.get(i));
            op = (operatorType) ops.get(i);
            current = make(t, factory.createBinaryOperation(op, current, right));
        }
        return current;
    }

    public PNode makeSliceType(Token begin, Token c1, Token c2, List<?> sltypes) {
        boolean isTuple = false;
        if (c1 != null || c2 != null) {
            isTuple = true;
        }
        PNode s = null;
        boolean extslice = false;

        if (isTuple) {
            List<PNode> etypes = new ArrayList<>();
            for (Object o : sltypes) {
                if (o instanceof IndexNode) {
                    IndexNode i = (IndexNode) o;
                    etypes.add(i.getOperand());
                } else {
                    extslice = true;
                    break;
                }
            }
            if (!extslice) {
                PNode t = makeTuple(begin, etypes, expr_contextType.Load);
                s = makeIndex(begin, t);
            }
        } else if (sltypes.size() == 1) {
            s = GrammarUtilities.castSlice(sltypes.get(0));
        } else if (sltypes.size() != 0) {
            extslice = true;
        }
        if (extslice) {
            List<PNode> st = GrammarUtilities.castSlices(sltypes);
            throw new NotCovered();
        }
        return s;
    }

    @SuppressWarnings("serial")
    class NotCovered extends RuntimeException {

        public NotCovered() {
            super("This case is not covered!");
        }
    }

    public PNode makeExceptHandler(Token eXCEPT177, PNode castExpr, PNode castExpr2, List<PNode> castStmts) {
        return null;
    }
}
