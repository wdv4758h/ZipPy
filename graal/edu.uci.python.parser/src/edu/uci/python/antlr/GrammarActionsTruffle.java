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
// CheckStyle: stop system..print check
package edu.uci.python.antlr;

import java.math.*;
import java.util.*;

import org.antlr.runtime.*;
import org.python.antlr.ast.boolopType;
import org.python.antlr.ast.cmpopType;
import org.python.antlr.ast.expr_contextType;
import org.python.antlr.ast.operatorType;
import org.python.antlr.ast.unaryopType;
import org.python.core.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.impl.*;
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

    public String makeFromText(List<?> dots, List<PNode> names) {
        StringBuilder d = new StringBuilder();
        d.append(GrammarUtilities.dottedNameListToString(names));
        return d.toString();
    }

    public StatementNode makeYield(Token t, PNode node) {
        StatementNode retVal = factory.createYield(node);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeTuple(Token t, List<PNode> elts, expr_contextType ctx) {
        PNode retVal = null;
        assert ctx == expr_contextType.Load : "Left hand side node should not reach here!";
        retVal = factory.createTupleLiteral(elts);
        ((TupleLiteralNode) retVal).setElts(elts);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeDictComp(Token t, PNode key, PNode paramValue, List<PComprehension> generators) {
        PNode retVal = null;

        String tmp = "_{" + t.getLine() + "_" + t.getCharPositionInLine() + "}";
        ParserEnvironment.def(tmp);
        PNode value = fixGlobalReadToLocal(paramValue);
        transformComprehensions(generators, value);
        // retVal.setToken(t);
        return retVal;
    }

    private static void transformComprehensions(List<PComprehension> generators, PNode body) {
        for (int i = 0; i < generators.size(); i++) {
            PComprehension c = generators.get(i);
            if (i + 1 <= generators.size() - 1) { // has next
                c.setInnerLoop(generators.get(i + 1));
            } else { // last/inner most
                c.setLoopBody(body);
            }
        }
    }

    private PNode processPComprehension(PComprehension node) {
        if (Options.debug) {
            System.out.println("processPComprehension:: " + node);
        }
        PNode retVal = null;

        boolean isInner = true;

        Amendable incomplete = (Amendable) fixWriteLocalSlot(node.getInternalTarget());
        PNode target = incomplete.updateRhs(factory.createRuntimeValueNode());
        target.setToken(node.getInternalTarget().getToken());
        PNode iterator = (node.getInternalIter());

        // inner loop
        PComprehension inner = node.getInnerLoop();
        PNode innerLoop = inner != null ? (processPComprehension(inner)) : null;
        isInner = inner != null ? false : true;

        // transformed loop body (only exist if it's inner most comprehension)
        PNode body = node.getLoopBody();
        PNode loopBody = body != null ? node.getLoopBody() : null;
        isInner = body != null ? true : false;

        // Just deal with one condition.
        List<PNode> conditions = node.getInternalIfs();
        PNode condition = (conditions == null || conditions.isEmpty()) ? null : (PNode) (conditions.get(0));

        assert inner == null || body == null : "Cannot be inner and outer at the same time";

        if (isInner) {
            retVal = factory.createInnerComprehension(target, iterator, factory.toBooleanCastNode(condition), loopBody);
        } else {
            retVal = factory.createOuterComprehension(target, iterator, factory.toBooleanCastNode(condition), innerLoop);
        }

        return retVal;

    }

    public PNode makeComprehension(Token t, PNode target, PNode iterator, List<PNode> ifs) {
        return new PComprehension(t, target, iterator, ifs);
    }

    public PNode recuFixWriteLocalSlots(PNode tree, int level) {
        Iterator<Node> list = null;
        PNode retVal = tree;
        PNode newNode = null;
        if (Options.debug) {
            System.out.println(level + ":: fixWriteLocalSlots:: Node: " + tree);
        }

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

                        if (Options.debug) {
                            System.out.println("fixWriteLocalSlots:: newNode: " + newNode + "  parent: " + newNode.getParent());
                        }
                    }

                } else {
                    System.out.println("null child!! :: parent: " + tree);
                }
            }
        }

        return retVal;
    }

    public PNode makeIndex(Token t, PNode value) {
        PNode retVal = factory.createIndex(value);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeIndex(PNode node, PNode value) {
        PNode retVal = factory.createIndex(value);
        retVal.setToken(node);
        return retVal;
    }

    public PNode makeCompare(Token t, PNode left, java.util.List<cmpopType> ops, List<PNode> comparators) {
        PNode retVal = factory.createComparisonOperations(left, ops, comparators);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeSetComp(Token t, PNode paramElt, List<PComprehension> generators) {
        PNode retVal = PNode.EMPTY_NODE;
        String tmp = "_{" + t.getLine() + "_" + t.getCharPositionInLine() + "}";
        ParserEnvironment.def(tmp);
        PNode elt = fixGlobalReadToLocal(paramElt);
        transformComprehensions(generators, elt);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeAttribute(Token t, PNode value, PNode attr, expr_contextType ctx) {
        PNode retVal = null;

        if (ctx != expr_contextType.Load) {
            retVal = factory.createAttributeUpdate(value, attr.getText(), PNode.DUMMY_NODE);
        } else {
            retVal = factory.createAttributeRef(value, attr.getText());
        }

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeListComp(Token t, PNode paramElt, List<PComprehension> generators) {
        PNode retVal = null;

        String tmp = "_[" + t.getLine() + "_" + t.getCharPositionInLine() + "]";
        FrameSlot slot = ParserEnvironment.def(tmp);

        PNode elt = fixGlobalReadToLocal(paramElt);
        transformComprehensions(generators, elt);

        assert generators.size() <= 1 : "More than one generator!";
        ComprehensionNode comprehension = (ComprehensionNode) processPComprehension(generators.get(0));

        retVal = factory.createListComprehension(comprehension);
        retVal.setToken(t);
        retVal.setSlot(slot);

        return retVal;
    }

    public PNode makeList(Token t, List<PNode> elts, expr_contextType ctx) {
        ListLiteralNode retVal = (ListLiteralNode) factory.createListLiteral(elts);
        retVal.setElts(elts);

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeDict(Token t, List<PNode> keys, List<PNode> values) {
        PNode retVal = factory.createDictLiteral(keys, values);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeStr(Token t, Object s) {
        PNode retVal = factory.createStringLiteral((PyString) s);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeFalse(Token t) {
        PNode retVal = factory.createBooleanLiteral(false);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeTrue(Token t) {
        PNode retVal = factory.createBooleanLiteral(true);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeNone(Token t) {
        PNode retVal = factory.createNoneLiteral();
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeUnaryOp(Token t, unaryopType op, PNode operand) {
        PNode retVal = factory.createUnaryOperation(op, operand);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeIfExp(Token t, PNode test, PNode body, PNode orelse) {
        PNode retVal = factory.createIfExpNode(test, body, orelse);
        retVal.setToken(t);
        return retVal;
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
        StatementNode retVal = null;
        BlockNode thenPart = factory.createBlock(body);
        BlockNode elsePart = factory.createBlock(orelse);
        retVal = factory.createIf(factory.toBooleanCastNode(test), thenPart, elsePart);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeGlobal(Token t, List<String> names, List<PNode> nameNodes) {
        PNode retVal = PNode.EMPTY_NODE;
        for (String name : names) {
            ParserEnvironment.defGlobal(name);
            ParserEnvironment.localGlobals.add(name);
        }
        retVal.setToken(t);
        return retVal;
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
        // retVal.setToken(t);
        return retVal;
    }

    public PAlias makeAliasImport(Token paramName, Token paramAsName) {
        PAlias retVal = null;
        PNode name = makeNameNode(paramName);
        PNode asname = (paramAsName != null) ? makeNameNode(paramAsName) : null;
        retVal = new PAlias(name, asname);
        if (paramAsName != null) {
            retVal.setSlot(ParserEnvironment.def(asname.getText()));
        } else {
            retVal.setSlot(ParserEnvironment.def(name.getText()));
        }
        // retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeImportFrom(Token t, String module, List<PNode> moduleNames, List<PAlias> aliases, Integer level) {
        StatementNode retVal = null;

        FrameSlot[] slots = new FrameSlot[aliases.size()];
        String[] names = new String[aliases.size()];

        for (int i = 0; i < aliases.size(); i++) {
            slots[i] = aliases.get(i).getSlot();
            names[i] = aliases.get(i).getInternalName();
        }
        retVal = factory.createImport(slots, module, names);

        retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeImport(Token t, List<PAlias> aliases) {
        StatementNode retVal = null;
        FrameSlot[] slots = new FrameSlot[aliases.size()];
        String[] names = new String[aliases.size()];

        for (int i = 0; i < aliases.size(); i++) {
            slots[i] = aliases.get(i).getSlot();
            names[i] = aliases.get(i).getInternalName();
        }

        retVal = factory.createImport(slots, null, names);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeExpr(Token t, PNode value) {
        PNode retVal = value;
        retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeReturn(Token t, PNode paramValue) {
        if (Options.debug) {
            System.out.println("makeReturn:: value: " + paramValue);
        }

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
        retVal.setToken(t);

        if (Options.debug) {
            System.out.println("makeReturn:: retVal: " + retVal + "  value: " + value + "  FrameRet: " + write);
        }
        return retVal;
    }

    public StatementNode makeBreak(Token t) {
        StatementNode retVal = factory.createBreak();
        retVal.setToken(t);
        return retVal;
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

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeAugAssign(PNode t, PNode target, operatorType op, PNode value) {
        if (Options.debug) {
            System.out.println("makeAugAssign:: t: " + t.getText() + "  Target: " + target);
        }
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

        retVal.setSlot(slot);

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeName(PNode tree, String id, expr_contextType ctx) {
        PNode retVal = makeName(tree.getToken(), id, ctx);
        retVal.setToken(tree);

        return retVal;
    }

    private PNode makeNameRead(Token t) {
        PNode retVal = null;
        FrameSlot slot = ParserEnvironment.find(t.getText());

        if (slot == null && ParserEnvironment.scopeLevel > 1) {
            slot = ParserEnvironment.probeEnclosingScopes(t.getText());
        }

        if (Options.debug) {
            System.out.println("makeNameRead:: Var: " + t + "  Slot: " + slot + "  Slot Class: " + ((slot != null) ? slot.getClass() : "null"));
        }

        if (slot != null) {
            if (slot instanceof EnvironmentFrameSlot) {
                retVal = factory.createReadEnvironment(slot, ((EnvironmentFrameSlot) slot).getLevel());
            } else {
                retVal = factory.createReadLocal(slot);
            }
            retVal.setSlot(slot);
        } else {
            retVal = factory.createReadGlobal(t.getText());
        }

        retVal.setToken(t);
        return retVal;
    }

    private PNode makeNameWrite(Token t, PNode rhs) {
        PNode retVal = null;
        String id = t.getText();
        FrameSlot slot = null;

        if ((ParserEnvironment.scopeLevel == 1 && GlobalScope.getInstance().isGlobalOrBuiltin(id)) || ParserEnvironment.localGlobals.contains(id)) {
            retVal = factory.createWriteGlobal(id, rhs);
        } else {
            // slot = ParserEnvironment.def(id);
            retVal = factory.createWriteLocal(rhs, slot);
            retVal.setSlot(slot);
        }

        return retVal;
    }

    public PNode makeName(Token t, String id, expr_contextType ctx) {
        if (Options.debug) {
            System.out.println("makeName ParserEnvironment.scopeLevel: " + ParserEnvironment.scopeLevel + " Var: " + id + " ctx: " + ctx.toString());
        }
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

        retVal.setToken(t);
        return retVal;
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
            retVal = factory.createWriteLocal(((WriteLocalNode) retVal).getRhs(), slot);
        } else if (retVal instanceof ReadLocalNode || retVal instanceof ReadGlobalNode) {
            retVal = makeNameRead(retVal.getToken());
        }

        if (retVal != null) {
            retVal.setToken(elt.getToken());
            retVal.setSlot(slot);

            Iterable<Node> list = retVal.getChildren();
            PNode newNode = null;

            for (Node n : list) {
                newNode = reAssignElt((PNode) n);
                if (newNode != n) {
                    n.replace(newNode);
                }
            }
        }
        if (Options.debug) {
            System.out.println("reAssignElt:: elt: " + elt + "  Fixed to: " + retVal);
        }
        return retVal;
    }

    public PNode makeGeneratorExp(Token t, PNode paramElt, List<PComprehension> generators) {

        PNode retVal = null;
        // PNode elt = fixGlobalReadToLocal(_elt);
        PComprehension generator = generators.get(0);
        generator.setInternalTarget(fixWriteLocalSlot(generator.getInternalTarget()));

        PNode elt = reAssignElt(paramElt);
        transformComprehensions(generators, elt);

        ComprehensionNode comprehension = (ComprehensionNode) processPComprehension(generator);
        GeneratorNode gnode = factory.createGenerator(comprehension, factory.createReadLocal(ParserEnvironment.getReturnSlot()));
        FrameDescriptor fd = ParserEnvironment.currentFrame;
        retVal = factory.createGeneratorExpression(gnode, fd);
        retVal.setToken(t);

        if (Options.debug) {
            System.out.println("makeGeneratorExp:: retVal: " + retVal);
        }

        return retVal;
    }

    public List<PNode> makeAssignTargets(PNode paramLhs, List<?> rhs) {
        PNode lhs = fixWriteLocalSlot(paramLhs);
        if (Options.debug) {
            System.out.println("makeAssignTargets:: lhs: " + lhs + "  rhs: " + rhs.get(0));
        }

        List<PNode> e = new ArrayList<PNode>();
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
        if (Options.debug) {
            System.out.println("makeAssignValue:: value: " + retVal);
        }
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
            retVal = factory.createWriteLocal(PNode.DUMMY_NODE, slot);
            retVal.setSlot(slot);
            retVal.setToken(writeLocalToken);
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
        retVal.setToken(node.getToken());
        return retVal;
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
        FrameSlot slot = null;
        String id = target.getText();

        if (lhs instanceof ReadGlobalNode) {
            if ((ParserEnvironment.scopeLevel == 1 && GlobalScope.getInstance().isGlobalOrBuiltin(id)) || ParserEnvironment.localGlobals.contains(id)) {
                retVal = factory.createWriteGlobal(id, right);
            } else {
                slot = ParserEnvironment.def(id);
                retVal = factory.createWriteLocal(right, slot);
                retVal.setSlot(slot);
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

        retVal.setToken(target.getToken());
        retVal.setSlot(target.getSlot());
        return retVal;
    }

    private static List<PNode> decompose(PNode node) {
        List<PNode> retVal = new ArrayList<>();

        if (node instanceof TupleLiteralNode) {
            retVal = ((TupleLiteralNode) node).getElts();
        } else if (node instanceof ListLiteralNode) {
            retVal = ((ListLiteralNode) node).getElts();
        } else {
            throw new RuntimeException("Unexpected decomposable type");
        }

        return retVal;
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

            retVal.setToken(t);

        } catch (Exception e) {
            e.printStackTrace();
            throw new NotCovered();
        }
        return retVal;
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
        StatementNode retVal = null;
        if (test == null) {
            return ErrorHandler.errorStmt(t);
        }
        List<PNode> o = GrammarUtilities.castStmts(orelse);
        List<PNode> b = GrammarUtilities.castStmts(body);
        BlockNode bodyPart = factory.createBlock(b);
        BlockNode orelsePart = factory.createBlock(o);
        retVal = factory.createWhile(factory.toBooleanCastNode(test), bodyPart, orelsePart);
        retVal.setToken(t);
        return retVal;
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
        if (Options.debug) {
            System.out.println("makeFor:: Target: " + target + "  iter: " + iter + "  budy: " + paramBody + " orelse: " + paramOrelse);
        }

        StatementNode retVal = null;
        if (target == null || iter == null) {
            return ErrorHandler.errorStmt(t);
        }

        List<PNode> o = GrammarUtilities.castStmts(paramOrelse);
        List<PNode> b = GrammarUtilities.castStmts(paramBody);

        List<PNode> body = new ArrayList<>();
        List<PNode> orelse = new ArrayList<>();

        BlockNode bodyPart = null;
        BlockNode orelsePart = null;

        List<PNode> lhs = new ArrayList<>();
        lhs.add(target);

        List<PNode> targets = null;

        try {
            targets = walkLeftHandSideList(lhs);
        } catch (Exception e) {
            e.printStackTrace();
// throw new NotCovered();
        }

        Amendable incomplete = (Amendable) targets.remove(0);
        PNode runtimeValue = factory.createRuntimeValueNode();
        PNode iteratorWrite = incomplete.updateRhs(runtimeValue);

        b.addAll(0, targets);

        for (PNode n : b) {
            body.add(fixGlobalReadToLocal(n));
        }

        for (PNode n : o) {
            orelse.add(fixGlobalReadToLocal(n));
        }

        bodyPart = factory.createBlock(body);
        orelsePart = factory.createBlock(orelse);

        retVal = dirtySpecialization(iteratorWrite, iter, bodyPart, orelsePart);

        retVal.setToken(t);
        return retVal;
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

        if (Options.debug) {
            System.out.println("makeFuncDef DONE: FrameDes: " + fd.getSlots() + "  Slot: " + "!" + " current FrameDes: " + ParserEnvironment.currentFrame.getSlots() + " Level: " +
                            ParserEnvironment.scopeLevel);
        }
        retVal.setSlot(slot);
        retVal.setToken(t);
        return retVal;
    }

    public PNode recurseSetContext(PNode tree, expr_contextType context) {
        if (Options.debug) {
            System.out.println("recurseSetContext Node: " + tree + " Context: " + context);
        }

        Iterable<Node> list = null;
        PNode retVal = tree;
        PNode newNode = null;
        PNode value = null;
        PNode slice = null;
        FrameSlot slot = null;

        if (context == expr_contextType.Load) {
            if (tree instanceof WriteGlobalNode) {
                if (GlobalScope.getInstance().isGlobalOrBuiltin(((WriteGlobalNode) tree).getName())) {
                    retVal = factory.createReadGlobal(((WriteGlobalNode) tree).getName());
                } else {
                    slot = ParserEnvironment.find(tree.getText());
                    if (tree.getSlot() != null) {
                        retVal = factory.createReadLocal(tree.getSlot());
                    } else if (slot != null) {
                        retVal = factory.createReadLocal(slot);
                    }
                }
            } else if (tree instanceof WriteLocalNode) {
                retVal = writeLocalToRead(tree);
            } else if (tree instanceof SubscriptStoreNode) {
                value = ((SubscriptStoreNode) tree).getPrimary();
                slice = ((SubscriptStoreNode) tree).getSlice();
                retVal = factory.createSubscriptLoad(value, slice);
            } else if (tree instanceof ListComprehensionNode) {
                retVal = fixGlobalReadToLocal(tree);
            }
        } else {
            if (tree instanceof ReadGlobalNode) {
                retVal = factory.createWriteGlobal(tree.getText(), PNode.DUMMY_NODE);
            } else if (tree instanceof ReadLocalNode) {
                retVal = factory.createWriteLocal(PNode.DUMMY_NODE, tree.getSlot());
            } else if (tree instanceof SubscriptLoadNode) {
                value = ((SubscriptLoadNode) tree).getPrimary();
                slice = ((SubscriptLoadNode) tree).getSlice();
                retVal = factory.createSubscriptStore(value, slice, PNode.DUMMY_NODE);
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
        if (retVal != null && retVal.getToken() != null) {
            retVal.setToken(tree.getToken());
        }
        if (Options.debug) {
            System.out.println("recurseSetContext retVal: " + retVal);
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
        if (Options.debug) {
            System.out.println("fixGlobalReadToLocal:: tree: " + tree + "  Fixed to: " + retVal);
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
                    retVal = factory.createReadEnvironment(slot, ((EnvironmentFrameSlot) slot).getLevel());
                }
                retVal.setSlot(slot);
            } else {
                retVal = factory.createReadGlobal(retVal.getText());
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
        retVal.setToken(t);
        return retVal;
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
        PNode s;
        PNode k;
        if (snameToken == null) {
            s = null;
        } else {
            s = cantBeNoneName(snameToken);
        }
        if (knameToken == null) {
            k = null;
        } else {
            k = cantBeNoneName(knameToken);
        }
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
        PNode retVal = factory.createDoubleLiteral(Double.valueOf(t.getText()));
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeComplex(Token t) {
        PNode retVal;
        String s = t.getText();
        s = s.substring(0, s.length() - 1);
        PyComplex pyComplex = Py.newImaginary(Double.valueOf(s));
        PComplex complex = new PComplex(pyComplex.real, pyComplex.imag);
        retVal = factory.createComplexLiteral(complex);

        retVal.setToken(t);
        return retVal;
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

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeCall(Token t, PNode func) {
        return makeCall(t, func, null, null, null, null);
    }

    public PNode makeCall(Token t, PNode func, List<?> args, List<?> keywords, PNode starargs, PNode kwargs) {
        if (Options.debug) {
            System.out.println("makeCall func:" + func.toString() + " Parent of Func: " + func.getParent());
        }
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

        if (Options.debug) {
            System.out.println("makeCall:: retVal:" + retVal + "  Token: " + t);
        }

        retVal.setToken(t);
        return retVal;
    }

    public PNode negate(PNode t, PNode o) {
        return negate(t.getToken(), o);
    }

    public PNode negate(Token t, PNode o) {
        PNode retVal = makeUnaryOp(t, unaryopType.USub, o);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeSubscript(Token t, PNode pPrimary, PNode paramSlice, expr_contextType ctx) {
        if (Options.debug) {
            System.out.println("makeSubscript:: Var: " + t.getText() + "  Primary: " + pPrimary + "  Ctx: " + ctx);
        }
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

        if (Options.debug) {
            System.out.println("makeSubscript:: retVal:" + retVal + "  Primary: " + primary + " slice: " + slice);
        }
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeSubscript(PNode lower, Token colon, PNode upper, PNode sliceop) {

        boolean isSlice = false;
        PNode retVal = null;
        PNode s = null;
        PNode e = null;
        PNode o = null;
        if (lower != null) {
            s = GrammarUtilities.castExpr(lower);
        }
        if (colon != null) {
            isSlice = true;
            if (upper != null) {
                e = GrammarUtilities.castExpr(upper);
            }
        }
        if (sliceop != null) {
            isSlice = true;
            if (sliceop != null) {
                o = GrammarUtilities.castExpr(sliceop);
            }
        }

        Token tok = null;
        if (s == null) {
            tok = colon;
        } else {
            s.getToken();
        }
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
            retVal.setToken(tok);
        }

        return retVal;
    }

    public PNode makePowerSpecific(PNode parent, Object o) {
        if (Options.debug) {
            System.out.println("makePowerSpecific:: Parent: " + parent + "   Object: " + o);
        }
        PNode retVal = null;
        PNode newNode = recurseSetContext(parent, expr_contextType.Load);

        if (newNode != parent) {
            parent.replace(newNode);
        }

        if (o instanceof CallBuiltInNode) {
            CallBuiltInNode c = (CallBuiltInNode) o;
            retVal = c;
        } else if (o instanceof CallBuiltInWithOneArgNoKeywordNode) {
            CallBuiltInWithOneArgNoKeywordNode c = (CallBuiltInWithOneArgNoKeywordNode) o;
            retVal = c;
        } else if (o instanceof CallBuiltInWithTwoArgsNoKeywordNode) {
            CallBuiltInWithTwoArgsNoKeywordNode c = (CallBuiltInWithTwoArgsNoKeywordNode) o;
            retVal = c;
        } else if (o instanceof CallNode) {
            CallNode c = (CallNode) o;
            retVal = c;
        } else if (o instanceof CallWithOneArgumentNoKeywordNode) {
            CallWithOneArgumentNoKeywordNode c = (CallWithOneArgumentNoKeywordNode) o;
            retVal = c;
        } else if (o instanceof CallWithTwoArgumentsNoKeywordNode) {
            CallWithTwoArgumentsNoKeywordNode c = (CallWithTwoArgumentsNoKeywordNode) o;
            retVal = c;
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
            SubscriptStoreNode c = (SubscriptStoreNode) o;
            retVal = c;
        } else if (o instanceof AttributeLoadNode) {
            AttributeLoadNode c = (AttributeLoadNode) o;

            c = (AttributeLoadNode) factory.createAttributeRef(newNode, c.getName());
            newNode.setParent(c);
            c.setToken(((AttributeLoadNode) o).getToken());
            ((AttributeLoadNode) o).replace(c);
            retVal = c;
        } else {
            retVal = newNode;
        }

        if (Options.debug) {
            System.out.println("makePowerSpecific:: Parent:" + newNode + " retVal: " + retVal);
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
        PNode retVal = factory.createBooleanOperations(GrammarUtilities.castExpr(left), op, GrammarUtilities.castExprs(right));
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeBinOp(Token t, PNode left, operatorType op, List<?> rights) {
        PNode rightTN = GrammarUtilities.castExpr(rights.get(0));
        PNode leftTN = GrammarUtilities.castExpr(left);
        PNode currentTN = factory.createBinaryOperation(op, leftTN, rightTN);
        currentTN.setToken(t);
        for (int i = 1; i < rights.size(); i++) {

            rightTN = GrammarUtilities.castExpr(rights.get(i));
            currentTN = factory.createBinaryOperation(op, currentTN, rightTN);
            currentTN.setToken(t);
        }
        return currentTN;
    }

    public PNode makeBinOp(Token t, PNode left, List<?> ops, List<?> rights, List<?> toks) {
        PNode leftTN = GrammarUtilities.castExpr(left);
        PNode rightTN = GrammarUtilities.castExpr(rights.get(0));
        operatorType op = (operatorType) ops.get(0);
        PNode currentTN = factory.createBinaryOperation(op, leftTN, rightTN);
        currentTN.setToken(t);

        for (int i = 1; i < rights.size(); i++) {
            rightTN = GrammarUtilities.castExpr(rights.get(i));
            op = (operatorType) ops.get(i);
            currentTN = factory.createBinaryOperation(op, currentTN, rightTN);
            currentTN.setToken(t);
        }
        return currentTN;
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

    void throwNotCovered() {
        throw new NotCovered();
    }

    // truffle
    public static void trace(String s) {
        System.out.print(s);
    }

    public static void traceln(String s) {
        System.out.println(s);
    }

    @SuppressWarnings("serial")
    class NotCovered extends RuntimeException {

        public NotCovered() {
            super("This case is not covered!");
        }

    }

    public PNode makeExceptHandler(Token eXCEPT177, PNode castExpr, PNode castExpr2, List<PNode> castStmts) {
        // TODO Auto-generated method stub
        return null;
    }
}
