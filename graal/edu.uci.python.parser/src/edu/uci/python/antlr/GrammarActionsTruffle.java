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
import java.util.List;

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

    private ErrorHandlerMsg errorHandler = null;
    public static Stack<FrameDescriptor> frames;
    public static FrameDescriptor globalFrame;
    public static FrameDescriptor currentFrame;
    public static int scopeLevel = 0;
    public static List<String> localGlobals = new ArrayList<>();
    public final NodeFactory factory = new NodeFactory();
    public static FrameDescriptor frameDescriptor = new FrameDescriptor();
// private Stack<FunctionRootNode> funcRoots = new Stack<>();

    // Loop Header Setter
    private ArrayList<PNode> currentLoopFixes = new ArrayList<>();
    private Stack<ArrayList<PNode>> fixLoopHeaders = new Stack<>();

    public StringBuilder output = null;

    private static final String TEMP_LOCAL_PREFIX = "temp_";
    public static final String RETURN_SLOT_ID = "<return_val>";

    public GrammarActionsTruffle() {
        frames = new Stack<>();
        beginScope();
    }

    public void setErrorHandler(ErrorHandlerMsg eh) {
        this.errorHandler = eh;
    }

    public void beginLoopLevel() {
        fixLoopHeaders.push(currentLoopFixes);

        currentLoopFixes = new ArrayList<>();
    }

    private void fixLoopHeaders(StatementNode loopheader) {
        for (PNode node : currentLoopFixes) {
            if (node instanceof BreakNode) {
                ((BreakNode) node).setLoopHeader(loopheader);
            } else if (node instanceof BlockNode) {
                ((BlockNode) node).setLoopHeader(loopheader);
            } else {
                throwNotCovered();
            }
        }

        currentLoopFixes = fixLoopHeaders.pop();
    }

    public void beginScope() {
        if (Options.debug) {
            System.out.println(">>>>======>>>>Before Begining level: " + scopeLevel + "   current frame: " + ((currentFrame != null) ? currentFrame.getSlots() : "null"));
        }
        scopeLevel++;

        if (currentFrame != null) {
            frames.push(currentFrame);
        }

        // temporary fix!
        currentFrame = new FrameDescriptor(DefaultFrameTypeConversion.getInstance());

        if (globalFrame == null) {
            globalFrame = currentFrame;
        }

        if (Options.debug) {
            System.out.println(">>>>>>>>>>>Begin level: " + scopeLevel + "   current frame: " + ((currentFrame != null) ? currentFrame.getSlots() : "null"));
        }
    }

    public FrameDescriptor endScope() {
        if (Options.debug) {
            System.out.println("<<<<<<<<<<End level: " + scopeLevel + "   current frame: " + currentFrame.getSlots());
        }
        scopeLevel--;
        FrameDescriptor fd = currentFrame;
        if (!frames.empty()) {
            currentFrame = frames.pop();
        }

        // reset locally declared globals
        localGlobals.clear();
        return fd;
    }

    private static FrameSlot def(String name) {
        if (Options.debug) {
            System.out.println("Current frame: " + currentFrame.getSlots());
        }

        FrameSlot retVal = currentFrame.findOrAddFrameSlot(name);
        if (Options.debug) {
            System.out.println("FrameSlot Def: Name: " + name + " current frame: " + currentFrame.getSlots());
        }
        return retVal;
    }

    private static FrameSlot defGlobal(String name) {
        return globalFrame.findOrAddFrameSlot(name);
    }

    private static FrameSlot find(String name) {
        return currentFrame.findFrameSlot(name);
    }

    public FrameSlot getReturnSlot() {
        return currentFrame.findOrAddFrameSlot(RETURN_SLOT_ID);
    }

    private static FrameSlot probeEnclosingScopes(String name) {
        FrameSlot retVal = null;
        int level = 0;
        for (int i = frames.size() - 1; i > 0 && retVal == null; i--) {
            FrameDescriptor fd = frames.get(i);
            level++;

            if (fd == globalFrame) {
                break;
            }

            FrameSlot candidate = fd.findFrameSlot(name);
            if (candidate != null) {
                retVal = EnvironmentFrameSlot.pack(candidate, level);
            }
        }

        if (Options.debug) {
            System.out.println("probeEnclosingScopes Name:" + name + " slot:" + retVal + "  Frames size: " + frames.size());
        }
        return retVal;
    }

    public String makeFromText(List<?> dots, List<PNode> names) {
        if (Options.debug) {
            System.out.println("makeFromText");
        }
        StringBuilder d = new StringBuilder();
        d.append(dottedNameListToString(names));
        return d.toString();
    }

    public StatementNode makeYield(Token t, PNode node) {
        if (Options.debug) {
            System.out.println("makeYield");
        }
        StatementNode retVal = factory.createYield(node);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeTuple(Token t, List<PNode> elts, expr_contextType ctx) {
        if (Options.debug) {
            System.out.println("makeTuple:: " + t.getText());
        }
        PNode retVal = null;

        assert ctx == expr_contextType.Load : "Left hand side node should not reach here!";
        retVal = factory.createTupleLiteral(elts);

        ((TupleLiteralNode) retVal).setElts(elts);
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeDictComp(Token t, PNode key, PNode paramValue, List<PComprehension> generators) {
        if (Options.debug) {
            System.out.println("makeDictComp");
        }
        PNode retVal = null;

        // retVal = new DictComp(t, key, value, generators);
        String tmp = "_{" + t.getLine() + "_" + t.getCharPositionInLine() + "}";
        def(tmp);
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
        StatementNode target = incomplete.updateRhs(factory.createRuntimeValueNode());
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

    public PNode makeComprehension(Token t, PNode paramTarget, PNode iterator, List<PNode> ifs) {
        PNode target = paramTarget; // fixWriteLocalSlot(paramTarget);
        if (Options.debug) {
            System.out.println("makeComprehension:: " + t.toString() + "  Target: " + target.getText() + " = " + target + " iter: " + iterator);
        }
        PNode retVal = null;

        retVal = new PComprehension(t, target, iterator, ifs);

        return retVal;
    }

    public PNode makeEllipsis(Token t) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeEllipsis");
        }
        PNode retVal = null;

        // retVal = new Ellipsis(t);

        // retVal.setToken(t);
        return retVal;
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
        if (Options.debug) {
            System.out.println("makeIndex");
        }
        PNode retVal = null;

        // retVal = new Index(t, value);
        retVal = factory.createIndex(value);

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeIndex(PNode node, PNode value) {
        if (Options.debug) {
            System.out.println("makeIndex");
        }
        PNode retVal = null;

        // retVal = new Index(node, value);
        retVal = factory.createIndex(value);

        retVal.setToken(node);
        return retVal;
    }

    public PNode makeCompare(Token t, PNode left, java.util.List<cmpopType> ops, List<PNode> comparators) {
        if (Options.debug) {
            System.out.println("makeCompare");
        }
        PNode retVal = null;

        // retVal = new Index(node, value);
        retVal = factory.createComparisonOperations(left, ops, comparators);

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeSetComp(Token t, PNode paramElt, List<PComprehension> generators) { // TODO: No
// Translation!!

        if (Options.debug) {
            System.out.println("makeSetComp");
        }
        PNode retVal = null;

        // retVal = new SetComp(t, elt, generators);
        String tmp = "_{" + t.getLine() + "_" + t.getCharPositionInLine() + "}";
        def(tmp);
        PNode elt = fixGlobalReadToLocal(paramElt);
        transformComprehensions(generators, elt);

        // retVal.setToken(t);
        return retVal;
    }

    public PNode makeSet(Token t, List<PNode> elts) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeSet");
        }
        PNode retVal = null;

        // retVal = new Set(t, elts);

        // retVal.setToken(t);
        return retVal;
    }

    public PNode makeAttribute(Token t, PNode value, PNode attr, expr_contextType ctx) {
        if (Options.debug) {
            System.out.println("makeAttribute:: Token: " + t.getText() + " Value: " + value + "  attr: " + attr + " ctx: " + ctx);
            // Exception exp = new Exception();
            // exp.printStackTrace();
        }
        PNode retVal = null;

        // retVal = new Attribute(t, value, attr, ctx);

        if (ctx != expr_contextType.Load) // TODO: was isLeftHandSide .. might need fix later
        {
            retVal = factory.createAttributeUpdate(value, attr.getText(), PNode.DUMMY_NODE);
        } else {
            retVal = factory.createAttributeRef(value, attr.getText());
        }
        // retVal.setSlot(def(attr.getText()));
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeListComp(Token t, PNode paramElt, List<PComprehension> generators) {
        PNode retVal = null;
        PNode listComp = null;
        String tmp = "_[" + t.getLine() + "_" + t.getCharPositionInLine() + "]";
        FrameSlot slot = def(tmp);

        PNode elt = fixGlobalReadToLocal(paramElt);
        transformComprehensions(generators, elt);

        if (Options.debug) {
            System.out.println("makeListComp:: " + t.getText() + " elt: " + paramElt + " Generator: " + generators.get(0));
        }

        assert generators.size() <= 1 : "More than one generator!";
        ComprehensionNode comprehension = (ComprehensionNode) processPComprehension(generators.get(0));

        retVal = factory.createListComprehension(comprehension);
        retVal.setToken(t);
        retVal.setSlot(slot);

        if (Options.debug) {
            System.out.println("makeListComp:: retVal: " + retVal);
        }

        return retVal;
    }

    public PNode makeList(Token t, List<PNode> elts, expr_contextType ctx) {
        if (Options.debug) {
            System.out.println("makeList t: " + t);
        }
        PNode retVal = null;

        retVal = factory.createListLiteral(elts);

        ((ListLiteralNode) retVal).setElts(elts);

        if (Options.debug) {
            System.out.println("makeList retVal: " + retVal);
        }

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeDict(Token t, List<PNode> keys, List<PNode> values) {
        if (Options.debug) {
            System.out.println("makeDict");
        }

        PNode retVal = factory.createDictLiteral(keys, values);

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeStr(Token t, Object s) {
        if (Options.debug) {
            System.out.println("makeStr");
        }

        PNode retVal = factory.createStringLiteral((PyString) s);

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeFalse(Token t) {
        if (Options.debug) {
            System.out.println("makeFalse");
        }

        PNode retVal = factory.createBooleanLiteral(false);

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeTrue(Token t) {
        if (Options.debug) {
            System.out.println("makeTrue");
        }

        PNode retVal = factory.createBooleanLiteral(true);

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeNone(Token t) {
        if (Options.debug) {
            System.out.println("makeNone");
        }

        PNode retVal = factory.createNoneLiteral();
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeUnaryOp(Token t, unaryopType op, PNode operand) {
        if (Options.debug) {
            System.out.println("makeUnaryOp");
        }

        PNode retVal = factory.createUnaryOperation(op, operand);

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeRepr(Token t, PNode value) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeRepr");
        }

        PNode retVal = null;

        // retVal = new Repr(t, value);

        // retVal.setToken(t);
        return retVal;
    }

    public PNode makeIfExp(Token t, PNode test, PNode body, PNode orelse) {
        if (Options.debug) {
            System.out.println("makeNone");
        }

        PNode retVal = factory.createIfExpNode(test, body, orelse);

        retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeIf(Token t, PNode test, List<PNode> body, List<PNode> orelse) {
        if (Options.debug) {
            System.out.println("makeIf");
        }
        StatementNode retVal = null;

        BlockNode thenPart = factory.createBlock(body);
        BlockNode elsePart = factory.createBlock(orelse);

        retVal = factory.createIf(factory.toBooleanCastNode(test), thenPart, elsePart);

// retVal.setFuncRootNode(getCurrentFuncRoot());
// thenPart.setFuncRootNode(getCurrentFuncRoot());
// elsePart.setFuncRootNode(getCurrentFuncRoot());

        currentLoopFixes.add(thenPart);
        currentLoopFixes.add(elsePart);

        retVal.setToken(t);

        return retVal;
    }

    public PNode makeGlobal(Token t, List<String> names, List<PNode> nameNodes) {
        if (Options.debug) {
            System.out.println("makeGlobal");
        }

        PNode retVal = PNode.EMPTY_NODE;

        // retVal = new Global(t, names, nameNodes, 0);
        for (String name : names) {
            defGlobal(name);
            localGlobals.add(name);
        }

        retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeExec(Token t, PNode body, PNode globals, PNode locals) { // TODO: No
// Translation!!

        if (Options.debug) {
            System.out.println("makeExec");
        }

        StatementNode retVal = null;

        // retVal = new Exec(t, body, globals, locals);

        // retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeAssert(Token t, PNode test, PNode msg) // TODO: No Translation!!
    {
        if (Options.debug) {
            System.out.println("makeAssert");
        }

        StatementNode retVal = null;

        // retVal = new Assert(t, test, msg);

        // retVal.setToken(t);
        return retVal;
    }

    public String dottedNameListToString(List<PNode> names) {
        if (names == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean leadingDot = true;
        for (int i = 0, len = names.size(); i < len; i++) {
            PNode name = names.get(i);
            String id = ((ReadGlobalNode) name).getName();
            if (id == null) {
                continue;
            }
            if (!".".equals(id)) {
                leadingDot = false;
            }
            sb.append(id);
            if (i < len - 1 && !leadingDot) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    public PAlias makeAliasDotted(List<PNode> nameNodes, Token paramAsName) {
        if (Options.debug) {
            System.out.println("makeAliasDotted");
        }

        PAlias retVal = null;
        PNode asname = makeNameNode(paramAsName);
        String snameNode = dottedNameListToString(nameNodes);
        retVal = new PAlias(nameNodes, snameNode, asname);
        // retVal = new alias(nameNodes, asname, 0);
        if (asname != null) {
            retVal.setSlot(def(asname.getText()));
        } else {
            retVal.setSlot(def(snameNode));
        }

        // retVal.setToken(t);
        return retVal;
    }

    public PAlias makeAliasImport(Token paramName, Token paramAsName) {
        if (Options.debug) {
            System.out.println("makeAliasImport");
        }
        PAlias retVal = null;
        PNode name = makeNameNode(paramName);
        PNode asname = (paramAsName != null) ? makeNameNode(paramAsName) : null;
        retVal = new PAlias(name, asname);
        if (paramAsName != null) {
            retVal.setSlot(def(asname.getText()));
        } else {
            retVal.setSlot(def(name.getText()));
        }

        // retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeImportFrom(Token t, String module, List<PNode> moduleNames, List<PAlias> aliases, Integer level) {
        if (Options.debug) {
            System.out.println("makeImportFrom");
        }
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
            if (Options.debug) {
                System.out.println("makeImport:: Loop: slot:" + slots[i] + "  name: " + names[i]);
            }
        }

        retVal = factory.createImport(slots, null, names);

        if (Options.debug) {
            System.out.println("makeImport:: " + retVal);
        }
        retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeRaise(Token t, PNode type, PNode inst, PNode tback) { // TODO: No
// Translation!!

        if (Options.debug) {
            System.out.println("makeRaise");
        }
        StatementNode retVal = null;

        // retVal = new Raise(t, type, inst, tback);

        // retVal.setToken(t);
        return retVal;
    }

    public PNode makeExpr(Token t, PNode value) {
        if (Options.debug) {
            System.out.println("makeExpr");
        }
        PNode retVal = null;

        // retVal = new Expr(t, value);
        retVal = value;

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
            write = factory.createWriteLocal(value, getReturnSlot());
            retVal = factory.createFrameReturn(write);
        } else {
            retVal = factory.createExplicitReturn(value);
        }

// retVal.setFuncRootNode(getCurrentFuncRoot());

        retVal.setToken(t);
        if (Options.debug) {
            System.out.println("makeReturn:: retVal: " + retVal + "  value: " + value + "  FrameRet: " + write);
        }
        return retVal;
    }

    public StatementNode makeContinue(Token t) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeContinue");
        }
        StatementNode retVal = null;

        // retVal = new Continue(t);

        // retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeBreak(Token t) {
        if (Options.debug) {
            System.out.println("makeBreak");
        }
        StatementNode retVal = null;

        // retVal = new Break(t);
        retVal = factory.createBreak();

        currentLoopFixes.add(retVal);
        // retVal.setLoopHeader(getCurrentLoopHeader());

        retVal.setToken(t);
        return retVal;
    }

    public StatementNode makePass(Token t) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makePass");
        }
        StatementNode retVal = null;

        // retVal = new Pass(t);

        // retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeDelete(Token t, List<PNode> targets) // TODO: No Translation!!
    {
        if (Options.debug) {
            System.out.println("makeDelete");
        }

        StatementNode retVal = null;

        // retVal = new Delete(t, targets);

        // retVal.setToken(t);
        return retVal;
    }

    public StatementNode makePrint(Token t, PNode dest, List<PNode> values, Boolean nl) {
        if (Options.debug) {
            System.out.println("makePrint");
        }

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

    public StatementNode makeNonlocal(Token t, List<?> paramNames, List<?> paramNameNodes) { // TODO:
// No Translation!!

        if (Options.debug) {
            System.out.println("makeNonlocal");
        }
        StatementNode retVal = null;
        List<String> names = makeNames(paramNames);
        List<PNode> nameNodes = makeNameNodes(paramNameNodes);
        // retVal = new Nonlocal(t, names, nameNodes, 0);

        // retVal.setToken(t);
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

        // retVal = new AugAssign(t, target, op, value);

        if (target instanceof FrameSlotNode) {
            slot = def(((FrameSlotNode) target).getText());
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
            // if (GlobalScope.getInstance().isGlobalOrBuiltin(writeGlobal.getName())) //TODO: Test
// it to make sure!
            // readGlobal = nodeFactory.createReadGlobal(writeGlobal.getName());
            // else
            // {
            // slot = def(writeGlobal.getName());
            // readGlobal = nodeFactory.createReadLocal(slot);
            // }
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

    public PNode makeInteractive(Token t, List<PNode> body) // TODO: No Translation!!
    {
        if (Options.debug) {
            System.out.println("makeInteractive");
        }
        PNode retVal = null;

        // retVal = new Interactive(t, body);

        // retVal.setToken(t);
        return retVal;
    }

    public PNode makeExpression(Token t, PNode body) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeExpression");
        }
        PNode retVal = null;

        // retVal = new Expression(t, body);

        // retVal.setToken(t);
        return retVal;
    }

    public PNode makeName(PNode tree, String id, expr_contextType ctx) {
        if (Options.debug) {
            System.out.println("makeName pnode");
        }

        PNode retVal = makeName(tree.getToken(), id, ctx);
        retVal.setToken(tree);

        return retVal;
    }

    FrameSlot findSlot(String id, expr_contextType ctx) {
        FrameSlot retVal = null;

        if (ctx != expr_contextType.Load) {
            if (scopeLevel == 1) {
                // Module global scope
                /**
                 * Variables in module's scope are also treated as globals This is why slot is not
                 * set for variables in module's scope WriteGlobal or ReadGlobal
                 */
                if (!GlobalScope.getInstance().isGlobalOrBuiltin(id)) {
                    retVal = def(id);
                }
            } else if (!localGlobals.contains(id)) {
                // function scope
                retVal = def(id);
            }
        } else {
            retVal = find(id);
            if (retVal == null && scopeLevel > 1) {
                retVal = probeEnclosingScopes(id);
            }
        }

        return retVal;
    }

    private PNode makeNameRead(Token t) {
        PNode retVal = null;
        FrameSlot slot = find(t.getText());

        if (slot == null && scopeLevel > 1) {
            slot = probeEnclosingScopes(t.getText());
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

        if ((scopeLevel == 1 && GlobalScope.getInstance().isGlobalOrBuiltin(id)) || localGlobals.contains(id)) {
            retVal = factory.createWriteGlobal(id, rhs);
        } else {
            // slot = def(id);
            retVal = factory.createWriteLocal(rhs, slot);
            retVal.setSlot(slot);
        }

        return retVal;
    }

    public PNode makeName(Token t, String id, expr_contextType ctx) {
        if (Options.debug) {
            System.out.println("makeName scopeLevel: " + scopeLevel + " Var: " + id + " ctx: " + ctx.toString());
        }
        PNode retVal = null;
        FrameSlot slot = null;

        if (ctx == expr_contextType.Param) {
            if (scopeLevel == 1) {
                // Module global scope
                /**
                 * Variables in module's scope are also treated as globals This is why slot is not
                 * set for variables in module's scope WriteGlobal or ReadGlobal
                 */
                if (!GlobalScope.getInstance().isGlobalOrBuiltin(id)) {
                    slot = def(id);
                }
            } else if (!localGlobals.contains(id)) {
                // function scope
                slot = def(id);
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
        if (Options.debug) {
            System.out.println("makeModule");
        }
        // beginScope();
        // PNode retVal = null;
        PNode retVal = null;

        // retVal = new Module(t, castStmts(stmts));

        FrameDescriptor fd = endScope();

        retVal = factory.createModule(castStmts(stmts), fd);

        if (Options.debug) {
            System.out.println("makeModule DONE: FrameDes: " + currentFrame.getSlots());
        }
        retVal.setToken(t);
        return retVal;
    }

    public PNode makeLambda(Token t, ParametersNode a, PNode body) // TODO: No Translation!!
    {

        if (Options.debug) {
            System.out.println("makeLambda");
        }
        // PNode retVal = null;
        PNode retVal = null;

        ParametersNode args = a;
        if (args == null) {
            args = makeArguments(t, new ArrayList<PNode>(), null, null, new ArrayList<PNode>());
        }

        // beginScope();

        // retVal = new Lambda(t, args, body);

        // endScope();

        // retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeClassDef(Token t, Token name, PNode pbases, List<?> body, List<?> decoratorlist) { // TODO:
// No Translation!!

        if (Options.debug) {
            System.out.println("makeClassDef");
        }
        // PNode retVal = null;
        StatementNode retVal = null;
        PNode n = cantBeNoneName(name);
        List<PNode> bases = makeBases(castExpr(pbases));
        List<PNode> b = castStmts(body);
        List<PNode> d = castExprs(decoratorlist);

        // retVal = new ClassDef(t, n, bases, b, d);

        endScope();
        def(name.getText());

        // retVal.setToken(t);
        return retVal;
    }

    public PNode reAssignElt(PNode elt) {
        PNode retVal = elt;
        FrameSlot slot = null;

        if (retVal instanceof WriteLocalNode) {
            slot = def(retVal.getText());
            retVal = factory.createWriteLocal(((WriteLocalNode) retVal).getRhs(), slot);
        } else if (retVal instanceof ReadLocalNode || retVal instanceof ReadGlobalNode) {
            retVal = makeNameRead(retVal.getToken());
        }

        if (retVal != null) {
            retVal.setToken(elt.getToken());
            retVal.setSlot(slot);

            if (Options.debug) {
                System.out.println("reAssignElt Going for each child");
            }
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

        if (Options.debug) {
            System.out.println("makeGeneratorExp:: " + t.getText() + "  elt: " + paramElt + "  GenTarget: " + generators.get(0).getInternalTarget());
        }

        PNode retVal = null;
        // PNode elt = fixGlobalReadToLocal(_elt);
        PComprehension generator = generators.get(0);
        generator.setInternalTarget(fixWriteLocalSlot(generator.getInternalTarget()));

        PNode elt = reAssignElt(paramElt);
        transformComprehensions(generators, elt);

        ComprehensionNode comprehension = (ComprehensionNode) processPComprehension(generator);
        GeneratorNode gnode = factory.createGenerator(comprehension, factory.createReadLocal(getReturnSlot()));
        FrameDescriptor fd = currentFrame;
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
        checkAssign(lhs);

        e.add(lhs);
        for (int i = 0; i < rhs.size() - 1; i++) {
            PNode r = castExpr(rhs.get(i));
            checkAssign(r);
            e.add(r);
        }
        if (Options.debug) {
            System.out.println("makeAssignTargets END");
        }

        return e;
    }

    public PNode makeAssignValue(List<?> rhs) {
        if (Options.debug) {
            System.out.println("makeAssignValue BEGIN");
        }
        PNode retVal = null;
        PNode value = castExpr(rhs.get(rhs.size() - 1));
        retVal = recurseSetContext(value, expr_contextType.Load);
        value.replace(retVal);

        if (Options.debug) {
            System.out.println("makeAssignValue:: value: " + retVal);
        }
        return retVal;
    }

    private static boolean isDecomposable(PNode node) {
        return node instanceof TupleLiteralNode || node instanceof ListLiteralNode;
    }

    private List<PNode> makeTemporaryWrites(List<PNode> rights) {
        List<PNode> tempWrites = new ArrayList<>();

        for (int i = 0; i < rights.size(); i++) {
            PNode right = rights.get(i);
            StatementNode tempWrite = ((Amendable) makeTemporaryWrite()).updateRhs(right);
            if (Options.debug) {
                System.out.println("makeTemporaryWrites:: tempWrite Rhs: " + ((WriteLocalNode) tempWrite).getRhs());
            }

            tempWrites.add(tempWrite);
        }

        return tempWrites;
    }

    private PNode makeTemporaryWrite() {
        String tempName = TEMP_LOCAL_PREFIX + currentFrame.getSize();
        FrameSlot tempSlot = currentFrame.addFrameSlot(tempName);
        PNode tempWrite = factory.createWriteLocal(PNode.DUMMY_NODE, tempSlot);
        tempWrite.setSlot(tempSlot);
        return tempWrite;
    }

    private List<PNode> decomposeAll(List<PNode> list) {
        List<PNode> retVal = new ArrayList<PNode>();

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
        if (Options.debug) {
            System.out.println("transformBalancedMultiAssignment");
        }

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

            if (Options.debug) {
                System.out.println("tranBalMultiAss:: processSingleWrite: " + processSingleWrite + " Token: " + processSingleWrite.getToken());
            }

            tempWrites.add(processSingleWrite);
        }

        return factory.createBlock(tempWrites);
    }

    private List<PNode> processDecomposedTargetList(List<PNode> nestedWrites, int sizeOfCurrentLevelLeftHandSide, PNode tempWrite, boolean isUnpacking) {
        if (Options.debug) {
            System.out.println("processDecomposedTargetList:: nestedWrites0: " + nestedWrites.get(0));
        }

        for (int idx = 0; idx < nestedWrites.size(); idx++) {
            if (idx < sizeOfCurrentLevelLeftHandSide) {
                PNode transformedRhs;

                if (isUnpacking) {
                    PNode read = ((WriteLocalNode) tempWrite).makeReadNode();
                    PNode indexNode = factory.createIntegerLiteral(idx);
                    transformedRhs = factory.createSubscriptLoad(read, indexNode);
                    // transformedRhs = makeSubscriptLoad((WriteLocalNode) tempWrite, idx);
                } else {
                    transformedRhs = ((WriteNode) tempWrite).makeReadNode();
                }

                StatementNode write = ((Amendable) nestedWrites.get(idx)).updateRhs(transformedRhs);
                if (Options.debug) {
                    System.out.println("processDecomposedTargetList:: lhs: " + write + "  rhs: " + transformedRhs);
                }

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
            slot = def(writeLocalToken.getText());
            retVal = factory.createWriteLocal(PNode.DUMMY_NODE, slot);
            retVal.setSlot(slot);
            retVal.setToken(writeLocalToken);

            if (Options.debug) {
                System.out.println("fixWriteLocalSlot:: broken: " + broken + "  fixed to: " + writeLocalToken + " = " + retVal);
            }
        }

        return retVal;
    }

    private List<PNode> walkLeftHandSideList(List<PNode> lhs) throws Exception {
        if (Options.debug) {
            System.out.println("walkLeftHandSideList:: lhs0: " + lhs.get(0));
        }

        List<PNode> writes = new ArrayList<>();
        List<PNode> additionalWrites = new ArrayList<>();

        for (int i = 0; i < lhs.size(); i++) {
            PNode target = lhs.get(i);
            target = fixWriteLocalSlot(target);
            if (Options.debug) {
                System.out.println("walkLeftHandSideList:: Processing: " + target);
            }

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

        if (Options.debug) {
            System.out.println("walkLeftHandSideList::Done!! retVal0: " + writes.get(0));
        }

        return writes;
    }

    private PNode writeLocalToRead(PNode node) {
        PNode retVal = node;
        FrameSlot slot = null;

        if (node instanceof WriteLocalNode) {
            slot = find(node.getText());
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
        if (Options.debug) {
            System.out.println("transformUnpackingAssignment:: lhs0: " + lhs.get(0).getText() + "  right: " + right);
        }
        /**
         * Transform a, b = c. <br>
         * To: temp_c = c; a = temp_c[0]; b = temp_d[1]
         */
        List<PNode> writes = new ArrayList<>();
        PNode updateRight = null;
        // PNode rhs = (PNode) visit(right);
        Amendable tempWrite = (Amendable) makeTemporaryWrite();

        updateRight = tempWrite.updateRhs(writeLocalToRead(right));

        writes.add(updateRight);

        // isLeftHandSide = true;
        List<PNode> targets = walkLeftHandSideList(lhs);
        // isLeftHandSide = false;

        writes.addAll(processDecomposedTargetList(targets, lhs.size(), (PNode) tempWrite, true));
        return factory.createBlock(writes);
    }

    private PNode processSingleAssignment(PNode target, PNode right) throws Exception {
        if (Options.debug) {
            System.out.println("processSingleAssignment:: Target: " + target + " Token: " + target.getToken());
        }
        PNode retVal = null;
        PNode lhs = target; // recurseSetContext(target,expr_contextType.Store);
        FrameSlot slot = null;
        String id = target.getText();

        if (lhs instanceof ReadGlobalNode) {
            if ((scopeLevel == 1 && GlobalScope.getInstance().isGlobalOrBuiltin(id)) || localGlobals.contains(id)) {
                retVal = factory.createWriteGlobal(id, right);
            } else {
                slot = def(id);
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
        List<PNode> elts = null;

        if (node instanceof TupleLiteralNode) {
            elts = ((TupleLiteralNode) node).getElts();
        } else if (node instanceof ListLiteralNode) {
            elts = ((ListLiteralNode) node).getElts();
        } else {
            throw new RuntimeException("Unexpected decomposable type");
        }

        retVal = elts;

        return retVal;
    }

    public PNode makeAssign(Token t, List<PNode> lhs, PNode rhs) {

        if (Options.debug) {
            System.out.println("makeAssign:: lhs: " + lhs + " lhs size: " + lhs.size() + "  lhs0: " + lhs.get(0).getText() + "  rhs: " + rhs + "  rhs slot: " + rhs.getSlot());
        }
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
                    if (Options.debug) {
                        System.out.println("makeAssign:: validTarget: " + validTarget);
                    }
                    assignments.add(validTarget);
                }

                retVal = factory.createBlock(assignments);
            }

            retVal.setToken(t);
            if (Options.debug) {
                System.out.println("makeAssign:: retVal: " + retVal);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new NotCovered();
        }
        return retVal;
    }

    public PNode makeAssign(PNode t, List<PNode> targets, PNode value) {

        if (Options.debug) {
            System.out.println("makeAssign PNode --> Token");
        }

        PNode retVal = makeAssign(t.getToken(), targets, value);

        // retVal.setToken(t);
        if (Options.debug) {
            System.out.println("makeAssign PNode:: retVal: " + retVal);
        }

        return retVal;
    }

    public List<PNode> makeModuleNameNode(List<?> dots, List<PNode> names) {
        if (Options.debug) {
            System.out.println("makeModuleNameNode");
        }
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
        if (Options.debug) {
            System.out.println("makeDottedName");
        }

        List<PNode> attrs = castExprs(paramAttrs);
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
        if (Options.debug) {
            System.out.println("makeLevel:: " + ((lev != null) ? lev.size() : "null"));
        }
        if (lev == null) {
            return 0;
        }
        return lev.size();
    }

    public List<PAlias> makeStarAlias(Token t) {
        if (Options.debug) {
            System.out.println("makeStarAlias");
        }
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
        if (Options.debug) {
            System.out.println("makeBases");
        }
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
        if (Options.debug) {
            System.out.println("makeNames");
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            s.add(((Token) names.get(i)).getText());
        }
        return s;
    }

    public PNode makeNameNode(Token t) {
        if (Options.debug) {
            System.out.println("makeNameNode");
        }
        if (t == null) {
            return null;
        }
        return makeName(t, t.getText(), expr_contextType.Load);
    }

    public List<PNode> makeNameNodes(List<?> names) {
        if (Options.debug) {
            System.out.println("makeNameNodes");
        }
        List<PNode> s = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            s.add(makeNameNode((Token) names.get(i)));
        }
        return s;
    }

    public void errorGenExpNotSoleArg(PNode t) {
        errorHandler.error("Generator expression must be parenthesized if not sole argument", t.getToken());
    }

    public void errorGenExpNotSoleArg(Token t) {
        errorHandler.error("Generator expression must be parenthesized if not sole argument", t);
    }

    public PNode castExpr(Object o) {
        PNode retVal = null;
        PNode object = null;
        if (o instanceof PNode) {
            object = (PNode) o;
            retVal = object;
        } else if (o instanceof PNode) {
            retVal = errorHandler.errorExpr(((PNode) o).getToken());
        } else {
            retVal = null;
        }

        if (Options.debug) {
            System.out.println("castExpr:: " + retVal);
        }
        return retVal;
    }

    public List<PNode> castExprs(List<?> exprs) {
        if (Options.debug) {
            System.out.println("castExprs");
        }
        return castExprs(exprs, 0);
    }

    public List<PNode> castExprs(List<?> exprs, int start) {
        if (Options.debug) {
            System.out.println("castExprs");
        }
        List<PNode> result = new ArrayList<>();
        if (exprs != null) {
            for (int i = start; i < exprs.size(); i++) {
                Object o = exprs.get(i);
                if (o instanceof PNode) {
                    result.add((PNode) o);
                } else if (o instanceof TruffleParser.test_return) {
                    result.add(((TruffleParser.test_return) o).getTree());
                }
            }
        }
        return result;
    }

    public List<PNode> makeElse(List<?> elseSuite, PNode elif) {
        if (Options.debug) {
            System.out.println("makeElse");
        }
        if (elseSuite != null) {
            return castStmts(elseSuite);
        } else if (elif == null) {
            return new ArrayList<>();
        }
        List<PNode> s = new ArrayList<>();
        s.add(castStmt(elif));
        return s;
    }

    public PNode castStmt(Object o) {
        if (Options.debug) {
            System.out.println("castStmt node: " + o);
        }
        if (o instanceof StatementNode) {
            return (StatementNode) o;
        } else if (o instanceof TruffleParser.stmt_return) {
            return ((TruffleParser.stmt_return) o).getTree();
        } else if (o instanceof PNode) {
            return ((PNode) o);
            // return errorHandler.errorStmt((PNode) o);
        }
        return null;
    }

    public List<PNode> castStmts(PNode t) { // TODO: add frame slot stuff

        if (Options.debug) {
            System.out.println("castStmts node: " + t);
        }
        PNode s = t;
        List<PNode> statementNodes = new ArrayList<>();
        statementNodes.add(s);
        return statementNodes;
    }

    public List<PNode> castStmts(List<?> statementNodes) {
        if (Options.debug) {
            System.out.println("castStmts");
        }
        if (statementNodes != null) {
            List<PNode> result = new ArrayList<>();
            for (Object o : statementNodes) {
                result.add(castStmt(o));
            }
            return result;
        }
        return new ArrayList<>();
    }

    public PNode makeDottedAttr(Token nameToken, List<?> attrs) {
        if (Options.debug) {
            System.out.println("makeDottedAttr");
        }
        PNode current = makeName(nameToken, nameToken.getText(), expr_contextType.Load);
        for (Object o : attrs) {
            Token t = (Token) o;
            current = makeAttribute(t, current, cantBeNoneName(t), expr_contextType.Load);
        }

        // retVal.setToken(t);
        return current;
    }

    public StatementNode makeWhile(Token t, PNode test, List<?> body, List<?> orelse) {
        if (Options.debug) {
            System.out.println("makeWhile");
        }

        StatementNode retVal = null;
        if (test == null) {
            return errorHandler.errorStmt(t);
        }
        List<PNode> o = castStmts(orelse);
        List<PNode> b = castStmts(body);
        BlockNode bodyPart = null;
        BlockNode orelsePart = null;
        // retVal = new While(t, test, b, o);

        if (test instanceof BooleanLiteralNode && ((BooleanLiteralNode) test).getValue()) {
            retVal = factory.createWhileTrue(null);

            bodyPart = factory.createBlock(b);

            currentLoopFixes.add(bodyPart);

// bodyPart.setFuncRootNode(getCurrentFuncRoot());
// retVal.setFuncRootNode(getCurrentFuncRoot());

            ((WhileTrueNode) retVal).setInternal(bodyPart);
        } else {
            retVal = factory.createWhile(factory.toBooleanCastNode(test), null, null);

            bodyPart = factory.createBlock(b);
            orelsePart = factory.createBlock(o);

            currentLoopFixes.add(bodyPart);
            currentLoopFixes.add(orelsePart);

// bodyPart.setFuncRootNode(getCurrentFuncRoot());
// orelsePart.setFuncRootNode(getCurrentFuncRoot());
// retVal.setFuncRootNode(getCurrentFuncRoot());

            ((WhileNode) retVal).setInternal(bodyPart, orelsePart);
        }
        fixLoopHeaders(retVal);

        retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeWith(Token t, PNode contextPNode, PNode optionalvars, List<StatementNode> body) { // TODO:
// No Translation!!

        if (Options.debug) {
            System.out.println("makeWith ctx");
        }
        StatementNode retVal = null;

        // retVal = new With(t, context_PNode, optional_vars, body);

        return retVal;
    }

    public StatementNode makeWith(Token t, List<?> items, List<?> body) // TODO: No Translation!!
    {
        if (Options.debug) {
            System.out.println("makeWith");
        }
        int last = items.size() - 1;
        StatementNode result = null;
        for (int i = last; i >= 0; i--) {
            // With current = items.get(i);
            if (i != last) {
                body = new ArrayList<StatementNode>();
                // body.add(result);
            }
            // result = new With(current.getToken(),
            // current.getInternalContext_PNode(),
            // current.getInternalOptional_vars(), body);
        }
        return result;
    }

    private StatementNode dirtySpecialization(StatementNode target, PNode iter) {
        StatementNode forNode;
        if (Options.OptimizeNode) {
            if (iter instanceof CallBuiltInWithOneArgNoKeywordNode && ((CallBuiltInWithOneArgNoKeywordNode) iter).getName().equals("range")) {
                forNode = factory.createForRangeWithOneValue(target, ((CallBuiltInWithOneArgNoKeywordNode) iter).getArgument(), null, null);
            } else if (iter instanceof CallBuiltInWithTwoArgsNoKeywordNode && ((CallBuiltInWithTwoArgsNoKeywordNode) iter).getName().equals("range")) {
                forNode = factory.createForRangeWithTwoValues(target, ((CallBuiltInWithTwoArgsNoKeywordNode) iter).getArg0(), ((CallBuiltInWithTwoArgsNoKeywordNode) iter).getArg1(), null, null);
            } else {
                forNode = factory.createFor(target, iter, null, null);
            }
        } else {
            forNode = factory.createFor(target, iter, null, null);
        }
        return forNode;
    }

    public StatementNode makeFor(Token t, PNode target, PNode iter, List<?> paramBody, List<?> paramOrelse) {
        if (Options.debug) {
            System.out.println("makeFor:: Target: " + target + "  iter: " + iter + "  budy: " + paramBody + " orelse: " + paramOrelse);
        }

        StatementNode retVal = null;
        if (target == null || iter == null) {
            return errorHandler.errorStmt(t);
        }

        List<PNode> o = castStmts(paramOrelse);
        List<PNode> b = castStmts(paramBody);

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
        StatementNode iteratorWrite = incomplete.updateRhs(runtimeValue);

        retVal = dirtySpecialization(iteratorWrite, iter);
// retVal.setFuncRootNode(getCurrentFuncRoot());

        b.addAll(0, targets);

        for (PNode n : b) {
            body.add(fixGlobalReadToLocal(n));
        }

        for (PNode n : o) {
            orelse.add(fixGlobalReadToLocal(n));
        }

        bodyPart = factory.createBlock(body);
        orelsePart = factory.createBlock(orelse);

        currentLoopFixes.add(bodyPart);
        currentLoopFixes.add(orelsePart);

// bodyPart.setFuncRootNode(getCurrentFuncRoot());
// orelsePart.setFuncRootNode(getCurrentFuncRoot());
// retVal.setFuncRootNode(getCurrentFuncRoot());

        if (retVal instanceof ForNode) {
            ((ForNode) retVal).setInternal(bodyPart, orelsePart);
        } else if (retVal instanceof ForRangeWithOneValueNode) {
            ((ForRangeWithOneValueNode) retVal).setInternal(bodyPart, orelsePart);
        } else {
            ((ForRangeWithTwoValuesNode) retVal).setInternal(bodyPart, orelsePart);
        }

        fixLoopHeaders(retVal);

        retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeTryExcept(Token t, List<?> body, List<?> handlers, List<?> orelse, List<?> finBody) { // TODO:
// No Translation!!

        if (Options.debug) {
            System.out.println("makeTryExcept");
        }
        StatementNode retVal = null;
        List<PNode> b = castStmts(body);
        // List<excepthandler> e = handlers;
        List<PNode> o = castStmts(orelse);
        // StatementNode te = new TryExcept(t, b, e, o);
        if (finBody == null) {
            // return te;
        }
        List<PNode> f = castStmts(finBody);
        List<PNode> mainBody = new ArrayList<>();
        // mainBody.add(te);
        // return new TryFinally(t, mainBody, f);
        // retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeTryFinally(Token t, List<?> body, List<?> finBody) { // TODO: No
// Translation!!

        if (Options.debug) {
            System.out.println("makeTryFinally");
        }
        StatementNode retVal = null;
        List<PNode> b = castStmts(body);
        List<PNode> f = castStmts(finBody);
        // return new TryFinally(t, b, f);

        // retVal.setToken(t);
        return retVal;
    }

    public StatementNode makeFuncdef(Token t, Token nameToken, ParametersNode args, List<?> funcStatements, List<?> decorators) {
        if (Options.debug) {
            System.out.println("makeFuncDef:: " + t.getText());
        }

        StatementNode retVal = null;
        boolean isGenerator = false;
        // beginScope();
        if (nameToken == null) {
            return errorHandler.errorStmt(t);
        }
        PNode n = cantBeNoneName(nameToken);
        // n = makeName(nameToken, nameToken.getText(),
        // expr_contextType.AugLoad);
        ParametersNode a = null;
        if (args != null) {
            a = args;
        } else {
            a = makeArguments(t, new ArrayList<PNode>(), null, null, new ArrayList<PNode>());
        }

        List<PNode> s = castStmts(funcStatements);
        List<PNode> d = castExprs(decorators);

        if (Options.OptimizeNode) {
            s.addAll(0, args.getTupleAssignment());
        }

        StatementNode body = factory.createBlock(s);

        // cache
// body.setFuncRootNode(getCurrentFuncRoot());
        PNode returnValue = factory.createReadLocal(getReturnSlot());
        FrameDescriptor fd = endScope();
        FrameSlot slot = def(n.getText());

        if (isGenerator) {
            body = new ASTLinearizer((BlockNode) body).linearize();
            RootNode genRoot = factory.createGeneratorRoot(args, body, returnValue);
            CallTarget ct = Truffle.getRuntime().createCallTarget(genRoot, fd);
            retVal = factory.createFunctionDef(slot, n.getText(), args, ct, genRoot);
        } else {
            // cache
// FunctionRootNode fufncRoot = funcRoots.pop();
// funcRoot.setBody(body);

            FunctionRootNode funcRoot = factory.createFunctionRoot(args, body, returnValue);
            CallTarget ct = Truffle.getRuntime().createCallTarget(funcRoot, fd);
            retVal = factory.createFunctionDef(slot, n.getText(), args, ct, funcRoot);
        }

        if (Options.debug) {
            System.out.println("makeFuncDef DONE: FrameDes: " + fd.getSlots() + "  Slot: " + "!" + " current FrameDes: " + currentFrame.getSlots() + " Level: " + scopeLevel);
        }
        retVal.setSlot(slot);
        retVal.setToken(t);
        return retVal;
    }

    public PNode recurseSetContext(PNode tree, expr_contextType context) // TODO: Not done yet!!
    {
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
                    slot = find(tree.getText());
                    if (tree.getSlot() != null) {
                        retVal = factory.createReadLocal(tree.getSlot());
                    } else if (slot != null) {
                        retVal = factory.createReadLocal(slot);
                    }
                }
            } else if (tree instanceof WriteLocalNode) {
                retVal = writeLocalToRead(tree);
            } else if (tree instanceof SubscriptStoreNode) {
                value = ((SubscriptStoreNode) tree).getFirst();
                slice = ((SubscriptStoreNode) tree).getSecond();
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
        if (Options.debug) {
            System.out.println("recurseSetContext tree is now: " + retVal);
        }

        if (tree != null && !(tree instanceof ListComprehensionNode) && !(tree instanceof GeneratorNode)) {
            // && (!(tree instanceof DictComp)) && (!(tree instanceof SetComp)))
            if (retVal == tree) {
                retVal = fixWriteLocalSlot(tree);
            }

            if (Options.debug) {
                System.out.println("recurseSetContext Going for each child");
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
            if (Options.debug) {
                System.out.println("fixGlobalReadToLocal Going for each child");
            }
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
            if (scopeLevel > 1) {
                slot = probeEnclosingScopes(retVal.getText());
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
            if (Options.debug) {
                System.out.println("readLocalToGlobal Going for each child");
            }
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
        if (Options.debug) {
            System.out.println("makeArguments");
        }

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
                targets = new ArrayList<PNode>();
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

        // cache
// FunctionRootNode funcRoot = factory.createFunctionRoot(retVal, null);
// funcRoots.push(funcRoot);

        return retVal;
    }

    public ParametersNode makeArgumentsType(Token t, List<?> params, Token snameToken, Token knameToken, List<?> defaults) {
        if (Options.debug) {
            System.out.println("makeArgumentsType");
        }

        List<PNode> p = castExprs(params);
        List<PNode> d = castExprs(defaults);
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
        if (Options.debug) {
            System.out.println("extractArgs");
        }
        return castExprs(args);
    }

    public List<PNode> makeKeywords(List<?> args) {
        List<PNode> keywords = new ArrayList<>();
        PNode singleKeyword = null;
        if (args != null) {
            if (Options.debug) {
                System.out.println("makeKeywords:: args0: " + ((args.size() > 0) ? args.get(0) : "EMPTY!"));
            }

            for (Object o : args) {
                List<?> e = (List<?>) o;
                Object k = e.get(0);
                Object v = e.get(1);
                checkAssign(castExpr(k));
                if (k instanceof PNode) {
                    singleKeyword = factory.createKeywordLiteral(castExpr(v), ((PNode) k).getText());
                    // singleKeyword.setToken(((Name) k).getToken());
                    keywords.add(singleKeyword);
                } else {
                    errorHandler.error("keyword must be a name", ((PNode) k).getToken());
                }
            }
        }
        return keywords;
    }

    public PNode makeFloat(Token t) {
        if (Options.debug) {
            System.out.println("makeFloat");
        }
        PNode retVal = factory.createDoubleLiteral(Double.valueOf(t.getText()));

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeComplex(Token t) {
        if (Options.debug) {
            System.out.println("makeComplex");
        }
        PNode retVal;
        String s = t.getText();
        s = s.substring(0, s.length() - 1);
        PyComplex pyComplex = Py.newImaginary(Double.valueOf(s));
        PComplex complex = new PComplex(pyComplex.real, pyComplex.imag);
        retVal = factory.createComplexLiteral(complex);

        retVal.setToken(t);
        return retVal;
    }

    // XXX: needs to handle NumberFormatException (on input like 0b2) and needs
    // a better long guard than ndigits > 11 (this is much to short for
    // binary for example)
    public PNode makeInt(Token t) {
        if (Options.debug) {
            System.out.println("makeInt");
        }

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
            // return Py.newLong(new BigInteger(s, radix));
        } else {
            ndigits = s.length();
            i = 0;
            while (i < ndigits && s.charAt(i) == '0') {
                i++;
            }
            if ((ndigits - i) > 11) {
                retVal = factory.createBigIntegerLiteral(new BigInteger(s, radix));
                // return Py.newLong(new BigInteger(s, radix));
            } else {

                l = Long.valueOf(s, radix).longValue();
                if (l > 0xffffffffL || (l > Integer.MAX_VALUE)) {
                    retVal = factory.createBigIntegerLiteral(new BigInteger(s, radix));
                    // return Py.newLong(new BigInteger(s, radix));
                } else {
                    retVal = factory.createIntegerLiteral((int) l);
                }
            }
        }

        retVal.setToken(t);
        return retVal;
    }

    public class StringPair {

        private String s;
        private boolean unicode;

        StringPair(String s, boolean unicode) {
            this.s = s;
            this.unicode = unicode;
        }

        String getString() {
            return s;
        }

        boolean isUnicode() {
            return unicode;
        }
    }

    public PyString extractStrings(List<?> s, String encoding, boolean unicodeLiterals) {
        boolean ustring = false;
        Token last = null;
        StringBuffer sb = new StringBuffer();
        Iterator<?> iter = s.iterator();
        while (iter.hasNext()) {
            last = (Token) iter.next();
            StringPair sp = extractString(last, encoding, unicodeLiterals);
            if (sp.isUnicode()) {
                ustring = true;
            }
            sb.append(sp.getString());
        }
        if (ustring) {
            return new PyUnicode(sb.toString());
        }
        return new PyString(sb.toString());
    }

    public StringPair extractString(Token t, String encoding, boolean unicodeLiterals) {
        String string = t.getText();
        char quoteChar = string.charAt(0);
        int start = 0;
        int end;
        boolean ustring = unicodeLiterals;

        if (quoteChar == 'u' || quoteChar == 'U') {
            ustring = true;
            start++;
        }
        if (quoteChar == 'b' || quoteChar == 'B') {
            // In 2.x this is just a str, and the parser prevents a 'u' and a
            // 'b' in the same identifier, so just advance start.
            ustring = false;
            start++;
        }
        quoteChar = string.charAt(start);
        boolean raw = false;
        if (quoteChar == 'r' || quoteChar == 'R') {
            raw = true;
            start++;
        }
        int quotes = 3;
        if (string.length() - start == 2) {
            quotes = 1;
        }
        if (string.charAt(start) != string.charAt(start + 1)) {
            quotes = 1;
        }

        start = quotes + start;
        end = string.length() - quotes;
        // string is properly decoded according to the source encoding
        // XXX: No need to re-encode when the encoding is iso-8859-1, but
        // ParserFacade
        // needs to normalize the encoding name
        if (!ustring && encoding != null) {
            // str with a specified encoding: first re-encode back out
            string = new PyUnicode(string.substring(start, end)).encode(encoding);
            if (!raw) {
                // Handle escapes in non-raw strs
                string = PyString.decode_UnicodeEscape(string, 0, string.length(), "strict", ustring);
            }
        } else if (raw) {
            // Raw str without an encoding or raw unicode
            string = string.substring(start, end);
            if (ustring) {
                // Raw unicode: handle unicode escapes
                string = codecs.PyUnicode_DecodeRawUnicodeEscape(string, "strict");
            }
        } else {
            // Plain unicode: already decoded, just handle escapes
            string = PyString.decode_UnicodeEscape(string, start, end, "strict", ustring);
        }
        return new StringPair(string, ustring);
    }

    public Token extractStringToken(List<?> s) {
        if (Options.debug) {
            System.out.println("extractStringToken");
        }
        return (Token) s.get(0);
        // return (Token)s.get(s.size() - 1);
    }

    public PNode makeCall(Token t, PNode func) {
        if (Options.debug) {
            System.out.println("makeCall");
        }
        return makeCall(t, func, null, null, null, null);
    }

    public PNode makeCall(Token t, PNode func, List<?> args, List<?> keywords, PNode starargs, PNode kwargs) {
        if (Options.debug) {
            System.out.println("makeCall func:" + func.toString() + " Parent of Func: " + func.getParent());
        }
        PNode retVal = null;
        if (func == null) {
            return errorHandler.errorExpr(t);
        }

        List<PNode> a = castExprs(args);
        PNode[] argumentsArray = a.toArray(new PNode[a.size()]);
        List<PNode> k = makeKeywords(keywords);
        PNode[] keywordsArray = k.toArray(new PNode[k.size()]);
        PCallable builtIn = null;
        PNode callee = func;

        if (callee.getParent() instanceof AttributeRefNode) {
            AttributeRefNode attr = (AttributeRefNode) callee.getParent();
            if (Options.debug) {
                System.out.println("makeCall:: attr: " + attr + "  callee: " + callee + "  attr.primary: " + attr.getOperand() + " attr.name: " + attr.getName());
            }
            retVal = factory.createAttributeCall(attr.getOperand(), argumentsArray, attr.getName());
        } else if (argumentsArray.length == 1 && keywordsArray.length == 0) { // Specializing call
// node.
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
        if (Options.debug) {
            System.out.println("negate");
        }
        return negate(t.getToken(), o);
    }

    public PNode negate(Token t, PNode o) {
        if (Options.debug) {
            System.out.println("negate");
        }
        PNode retVal = null;

        retVal = makeUnaryOp(t, unaryopType.USub, o);

        retVal.setToken(t);
        return retVal;
    }

    public String cantBeNone(Token t) {
        if (t == null || t.getText().equals("None")) {
            errorHandler.error("can't be None", t);
        }
        return t.getText();
    }

    public PNode cantBeNoneName(Token t) {
        if (t == null || t.getText().equals("None")) {
            errorHandler.error("can't be None", t);
        }
        return makeName(t, t.getText(), expr_contextType.Load);
    }

    public void cantBeNone(PNode e) {
        if (e.getText().equals("None")) {
            errorHandler.error("can't be None", e.getToken());
        }
    }

    private void checkGenericAssign(PNode e) { // TODO: Not done yet!!

        // if (e instanceof Name && ((Name) e).getInternalId().equals("None"))
        // {
        // errorHandler.error("assignment to None", e);
        // }
        // else if (e instanceof GeneratorExp)
        // {
        // errorHandler.error("can't assign to generator expression", e);
        // }
        // else if (e instanceof Num)
        // {
        // errorHandler.error("can't assign to number", e);
        // }
        // else if (e instanceof Str)
        // {
        // errorHandler.error("can't assign to string", e);
        // }
        // else if (e instanceof Yield)
        // {
        // errorHandler.error("can't assign to yield expression", e);
        // }
        // else if (e instanceof BinOp)
        // {
        // errorHandler.error("can't assign to operator", e);
        // }
        // else if (e instanceof BoolOp)
        // {
        // errorHandler.error("can't assign to operator", e);
        // }
        // else if (e instanceof Lambda)
        // {
        // errorHandler.error("can't assign to lambda", e);
        // }
        // else if (e instanceof Call)
        // {
        // errorHandler.error("can't assign to function call", e);
        // }
        // else if (e instanceof Repr)
        // {
        // errorHandler.error("can't assign to repr", e);
        // }
        // else if (e instanceof IfExp)
        // {
        // errorHandler.error("can't assign to conditional expression", e);
        // }
        // else if (e instanceof ListComp)
        // {
        // errorHandler.error("can't assign to list PComprehension", e);
        // }
    }

    public void checkAugAssign(PNode e) {
        checkGenericAssign(e);
        if (e instanceof TupleLiteralNode) {
            errorHandler.error("assignment to tuple illegal for augmented assignment", e.getToken());
        } else if (e instanceof ListLiteralNode) {
            errorHandler.error("assignment to list illegal for augmented assignment", e.getToken());
        }
    }

    public void checkAssign(PNode e) {
        if (Options.debug) {
            System.out.println("checkAssign");
        }
        checkGenericAssign(e);
        if (e instanceof TupleLiteralNode) {
            // XXX: performance problem? Any way to do this better?
            List<PNode> elts = ((TupleLiteralNode) e).getElts();
            if (elts.size() == 0) {
                errorHandler.error("can't assign to ()", e.getToken());
            }
            for (int i = 0; i < elts.size(); i++) {
                checkAssign(elts.get(i));
            }
        } else if (e instanceof ListLiteralNode) {
            // XXX: performance problem? Any way to do this better?
            List<PNode> elts = ((ListLiteralNode) e).getElts();
            for (int i = 0; i < elts.size(); i++) {
                checkAssign(elts.get(i));
            }
        }
    }

    public List<PNode> makeDeleteList(List<?> deletes) {
        if (Options.debug) {
            System.out.println("makeDeleteList");
        }
        List<PNode> exprs = castExprs(deletes);
        for (PNode e : exprs) {
            checkDelete(e);
        }
        return exprs;
    }

    public void checkDelete(PNode e) { // TODO: Not done yet!!

        if (Options.debug) {
            System.out.println("checkDelete.. Not done yet!!");
        }

        // else if (e instanceof Num)
        // {
        // errorHandler.error("can't delete number", e);
        // }
        // else if (e instanceof Str)
        // {
        // errorHandler.error("can't delete string", e);
        // }

        if (e instanceof CallNode) {
            errorHandler.error("can't delete function call", e.getToken());
        } else if (e instanceof TupleLiteralNode) {
            // XXX: performance problem? Any way to do this better?
            List<PNode> elts = ((TupleLiteralNode) e).getElts();
            if (elts.size() == 0) {
                errorHandler.error("can't delete ()", e.getToken());
            }
            for (int i = 0; i < elts.size(); i++) {
                checkDelete(elts.get(i));
            }
        } else if (e instanceof ListLiteralNode) {
            // XXX: performance problem? Any way to do this better?
            List<PNode> elts = ((ListLiteralNode) e).getElts();
            for (int i = 0; i < elts.size(); i++) {
                checkDelete(elts.get(i));
            }
        }
    }

    public PNode makeSubscript(Token t, PNode primary, PNode paramSlice, expr_contextType ctx) {
        if (Options.debug) {
            System.out.println("makeSubscript:: Var: " + t.getText() + "  Primary: " + primary + "  Ctx: " + ctx);
        }
        PNode retVal = null;
        PNode slice = paramSlice; // fixGlobalReadToLocal(paramSlice);
        if (ctx == expr_contextType.Load) {
            retVal = factory.createSubscriptLoad(primary, slice);
        } else if (ctx == expr_contextType.Store) {
            // assert isLeftHandSide;
            primary = makeNameRead(primary.getToken());
            // _slice = fixWriteLocalSlot(_slice);
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
        if (Options.debug) {
            System.out.println("makeSubscript lower: " + lower + "  colon: " + colon + "  upper: " + upper + "  sliceop: " + sliceop);
        }
        boolean isSlice = false;
        PNode retVal = null;
        PNode s = null;
        PNode e = null;
        PNode o = null;
        if (lower != null) {
            s = castExpr(lower);
        }
        if (colon != null) {
            isSlice = true;
            if (upper != null) {
                e = castExpr(upper);
            }
        }
        if (sliceop != null) {
            isSlice = true;
            if (sliceop != null) {
                o = castExpr(sliceop);
            }
// else {
// o = makeName(sliceop, "None", expr_contextType.Load);
// }
        }

        Token tok = null;
        if (s == null) {
            tok = colon;
        } else {
            s.getToken();
        }
        if (isSlice) {
            // retVal = new Slice(tok, s, e, o);

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
            // retVal = new Index(tok, s);
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
        expr_contextType ctx = null;

        // if(o instanceof SubscriptStoreNode)
        // ctx = expr_contextType.Store;
        // else
        ctx = expr_contextType.Load;

        PNode newNode = recurseSetContext(parent, ctx);

        if (newNode != parent) {
            parent.replace(newNode);
        }

        if (o instanceof CallBuiltInNode) {
            CallBuiltInNode c = (CallBuiltInNode) o;
            // c.setFunc(retVal);
            retVal = c;
        } else if (o instanceof CallBuiltInWithOneArgNoKeywordNode) {
            CallBuiltInWithOneArgNoKeywordNode c = (CallBuiltInWithOneArgNoKeywordNode) o;
            // c.setFunc(retVal);
            retVal = c;
        } else if (o instanceof CallBuiltInWithTwoArgsNoKeywordNode) {
            CallBuiltInWithTwoArgsNoKeywordNode c = (CallBuiltInWithTwoArgsNoKeywordNode) o;
            // c.setFunc(retVal);
            retVal = c;
        } else if (o instanceof CallNode) {
            CallNode c = (CallNode) o;
            // c.setFunc(retVal);
            retVal = c;
        } else if (o instanceof CallWithOneArgumentNoKeywordNode) {
            CallWithOneArgumentNoKeywordNode c = (CallWithOneArgumentNoKeywordNode) o;
            // c.setFunc(retVal);
            retVal = c;
        } else if (o instanceof CallWithTwoArgumentsNoKeywordNode) {
            CallWithTwoArgumentsNoKeywordNode c = (CallWithTwoArgumentsNoKeywordNode) o;
            // c.setFunc(retVal);
            retVal = c;
        } else if (o instanceof AttributeCallNode) {

            AttributeCallNode c = (AttributeCallNode) o;
            if (Options.debug) {
                System.out.println("makePowerSpecific:: AttributeCallNode c: " + c + "  parent: " + parent);
            }
            c = c.updatePrimary(((AttributeRefNode) parent).getOperand());
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
            // c.setValue(retVal);
            retVal = c;
        } else if (o instanceof AttributeRefNode) {
            AttributeRefNode c = (AttributeRefNode) o;

            c = (AttributeRefNode) factory.createAttributeRef(newNode, c.getName());
            newNode.setParent(c);
            c.setToken(((AttributeRefNode) o).getToken());
            ((AttributeRefNode) o).replace(c);
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
        if (Options.debug) {
            System.out.println("makeCmpOps");
        }

        List<cmpopType> result = new ArrayList<>();
        if (cmps != null) {
            for (Object o : cmps) {
                result.add((cmpopType) o);
            }
        }
        return result;
    }

    public PNode makeBoolOp(Token t, PNode left, boolopType op, List<?> right) {
        if (Options.debug) {
            System.out.println("makeBoolOp L: " + castExpr(left).toString() + " R: " + castExpr(right.get(0)).toString());
        }
        PNode retVal = null;

        retVal = factory.createBooleanOperations(castExpr(left), op, castExprs(right));

        retVal.setToken(t);
        return retVal;
    }

    public PNode makeBinOp(Token t, PNode left, operatorType op, List<?> rights) {
        if (Options.debug) {
            System.out.println("makeBinOp1: L:" + left.toString() + " R:" + rights.get(0).toString() + " Op:" + op.toString());
        }

        PNode rightTN = castExpr(rights.get(0));
        PNode leftTN = castExpr(left);

        PNode currentTN = factory.createBinaryOperation(op, leftTN, rightTN);
        currentTN.setToken(t);

        for (int i = 1; i < rights.size(); i++) {

            rightTN = castExpr(rights.get(i));
            currentTN = factory.createBinaryOperation(op, currentTN, rightTN);
            currentTN.setToken(t);
        }
        return currentTN;
    }

    public PNode makeBinOp(Token t, PNode left, List<?> ops, List<?> rights, List<?> toks) {
        if (Options.debug) {
            System.out.println("makeBinOp2: L: " + left.toString() + " R: " + rights.get(0).toString() + " Op: " + ops.get(0).toString());
        }
        PNode leftTN = castExpr(left);
        PNode rightTN = castExpr(rights.get(0));
        operatorType op = (operatorType) ops.get(0);

        if (Options.debug) {
            System.out.println("makeBinOp2: L: " + left.toString() + " R: " + rights.get(0).toString() + " Op: " + ops.get(0).toString());
        }

        PNode currentTN = factory.createBinaryOperation(op, leftTN, rightTN);
        currentTN.setToken(t);

        for (int i = 1; i < rights.size(); i++) {
            rightTN = castExpr(rights.get(i));
            op = (operatorType) ops.get(i);

            currentTN = factory.createBinaryOperation(op, currentTN, rightTN);
            currentTN.setToken(t);

            if (Options.debug) {
                System.out.println("makeBinOp2: L: " + currentTN + " R: " + rights.get(i) + " Op: " + ops.get(i));
            }

        }

        if (Options.debug) {
            System.out.println("makeBinOp2: retVal: " + currentTN + " DONE!!");
        }

        return currentTN;
    }

    public List<PNode> castSlices(List<?> slices) {
        List<PNode> result = new ArrayList<>();
        if (slices != null) {
            for (Object o : slices) {
                result.add(castSlice(o));
            }
        }
        return result;
    }

    public PNode castSlice(Object o) {
        if (Options.debug) {
            System.out.println("castSlice node: " + o);
        }
        if (o instanceof IndexNode) {
            return (PNode) o;
        }
        if (o instanceof SliceNode) {
            return (PNode) o;
        }
        return errorHandler.errorSlice(((PNode) o).getToken());
    }

    public PNode makeSliceType(Token begin, Token c1, Token c2, List<?> sltypes) {
        if (Options.debug) {
            System.out.println("makeSliceType");
        }
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
            s = castSlice(sltypes.get(0));
        } else if (sltypes.size() != 0) {
            extslice = true;
        }
        if (extslice) {
            List<PNode> st = castSlices(sltypes);

            throw new NotCovered();
        }

        if (Options.debug) {
            System.out.println("makeSliceType DONE!");
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
