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
import java.util.List;
import java.util.Set;

import org.python.antlr.*;
import org.python.antlr.ast.*;
import org.python.antlr.base.*;
import org.python.compiler.*;
import org.python.core.*;
import org.python.google.common.collect.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.access.*;
import edu.uci.python.nodes.argument.*;
import edu.uci.python.nodes.expression.*;
import edu.uci.python.nodes.function.*;
import edu.uci.python.nodes.literal.*;
import edu.uci.python.nodes.loop.*;
import edu.uci.python.nodes.object.*;
import edu.uci.python.nodes.statement.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.sequence.*;
import static edu.uci.python.parser.TranslationUtil.*;

public class PythonTreeTranslator extends Visitor {

    private final PythonContext context;
    private final NodeFactory factory;
    private final TranslationEnvironment environment;
    private final LoopsBookKeeper loops;
    private final AssignmentTranslator assigns;
    private final PythonParseResult result;

    public PythonTreeTranslator(TranslationEnvironment environment, PythonContext context) {
        this.context = context;
        this.factory = new NodeFactory();
        this.environment = environment.reset();
        this.loops = new LoopsBookKeeper();
        this.assigns = new AssignmentTranslator(environment, this);
        this.result = new PythonParseResult();
    }

    public PythonParseResult translate(PythonTree root) {
        ModuleNode module;

        try {
            module = (ModuleNode) visit(root);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("Failed in " + this + " with error " + t);
        }

        result.setModule(module);
        result.setContext(context);

        return result;
    }

    @Override
    public Object visitModule(org.python.antlr.ast.Module node) throws Exception {
        environment.beginScope(node, ScopeInfo.ScopeKind.Module);
        List<PNode> body = visitStatements(node.getInternalBody());
        FrameDescriptor fd = environment.getCurrentFrame();
        environment.endScope(node);
        RootNode newNode = new NodeFactory().createModule(body, fd);
        return newNode;
    }

    public List<PNode> visitStatements(List<stmt> stmts) throws Exception {
        List<PNode> statements = new ArrayList<>();

        for (int i = 0; i < stmts.size(); i++) {
            PNode statement = (PNode) visit(stmts.get(i));

            // Statements like Global is ignored
            if (statement != null) {
                statements.add(statement);
            }
        }

        return statements;
    }

    @Override
    public Object visitFunctionDef(FunctionDef node) throws Exception {
        /**
         * translate default arguments in FunctionDef's declaring scope.
         */
        List<PNode> defaultArgs = walkExprList(node.getInternalArgs().getInternalDefaults());

        String name = node.getInternalName();
        environment.beginScope(node, ScopeInfo.ScopeKind.Function);
        environment.setDefaultArgumentNodes(defaultArgs);

        /**
         * Parameters
         */
        Arity arity = createArity(name, node.getInternalArgs());
        ParametersNode parameters = visitArgs(node.getInternalArgs());

        /**
         * Funciton body
         */
        List<PNode> statements = visitStatements(node.getInternalBody());
        StatementNode body = factory.createBlock(statements);
        body = new ReturnTargetNode(parameters, body, factory.createReadLocalVariable(environment.getReturnSlot()));

        /**
         * Definition
         */
        PNode funcDef;
        if (environment.isInGeneratorScope()) {
            funcDef = createGeneratorFunctionDefinition(name, arity, parameters, body);
        } else {
            funcDef = createFunctionDefinitnion(name, arity, parameters, body);
        }

        environment.endScope(node);
        return environment.findVariable(name).makeWriteNode(funcDef);
    }

    private PNode createFunctionDefinitnion(String name, Arity arity, ParametersNode parameters, StatementNode body) {
        FunctionRootNode funcRoot = factory.createFunctionRoot(name, body);
        result.addParsedFunction(name, funcRoot);
        CallTarget ct = Truffle.getRuntime().createCallTarget(funcRoot, environment.getCurrentFrame());

        if (environment.hasDefaultArguments()) {
            List<PNode> defaultParameters = environment.getDefaultArgumentNodes();
            ReadDefaultArgumentNode[] defaultReads = (ReadDefaultArgumentNode[]) ((ParametersWithDefaultsNode) parameters).getDefaultReads();
            StatementNode defaults = new DefaultParametersNode(defaultParameters.toArray(new PNode[defaultParameters.size()]), defaultReads);
            return new FunctionDefinitionNode(name, arity, defaults, ct, environment.getCurrentFrame(), environment.needsDeclarationFrame());
        } else {
            return new FunctionDefinitionNode(name, arity, BlockNode.EMPTYBLOCK, ct, environment.getCurrentFrame(), environment.needsDeclarationFrame());
        }
    }

    private PNode createGeneratorFunctionDefinition(String name, Arity arity, ParametersNode parameters, StatementNode body) {
        GeneratorRootNode funcRoot = factory.createGeneratorRoot(name, body);
        GeneratorTranslator.translate(funcRoot);
        result.addParsedFunction(name, funcRoot);
        FrameDescriptor fd = environment.getCurrentFrame();
        CallTarget ct = Truffle.getRuntime().createCallTarget(funcRoot, fd);

        if (environment.hasDefaultArguments()) {
            List<PNode> defaultParameters = environment.getDefaultArgumentNodes();
            ReadDefaultArgumentNode[] defaultReads = (ReadDefaultArgumentNode[]) ((ParametersWithDefaultsNode) parameters).getDefaultReads();
            StatementNode defaults = new DefaultParametersNode(defaultParameters.toArray(new PNode[defaultParameters.size()]), defaultReads);
            return new GeneratorFunctionDefinitionNode(name, arity, defaults, ct, fd, environment.needsDeclarationFrame());
        } else {
            return new GeneratorFunctionDefinitionNode(name, arity, BlockNode.EMPTYBLOCK, ct, fd, environment.needsDeclarationFrame());
        }
    }

    private PNode createGeneratorExpressionDefinition(StatementNode body) {
        PNode wrappedBody = new ReturnTargetNode(ParametersNode.EMPTY_PARAMS, body, factory.createReadLocalVariable(environment.getReturnSlot()));
        GeneratorRootNode funcRoot = factory.createGeneratorRoot("generator_exp", wrappedBody);
        GeneratorTranslator.translate(funcRoot);
        FrameDescriptor fd = environment.getCurrentFrame();
        return factory.createGeneratorExpression(Truffle.getRuntime().createCallTarget(funcRoot, fd), fd, environment.needsDeclarationFrame());
    }

    public Arity createArity(String functionName, arguments node) {
        int numOfArguments = node.getInternalArgs().size();
        int numOfDefaultArguments = node.getInternalDefaults().size();
        List<String> parameterIds = new ArrayList<>();

        for (expr arg : node.getInternalArgs()) {
            parameterIds.add(((Name) arg).getInternalId());
        }

        return new Arity(functionName, numOfArguments - numOfDefaultArguments, numOfArguments, parameterIds);
    }

    public ParametersNode visitArgs(arguments node) throws Exception {
        // parse arguments
        new ArgListCompiler().visitArgs(node);
        List<PNode> args = new ArrayList<>();
        List<String> paramNames = new ArrayList<>();

        for (int i = 0; i < node.getInternalArgs().size(); i++) {
            expr arg = node.getInternalArgs().get(i);

            if (arg instanceof Name) {
                args.add((PNode) visit(arg));
                paramNames.add(((Name) arg).getInternalId());
            } else {
                throw notCovered("Unexpected parameter type " + arg.getClass().getSimpleName());
            }
        }

        if (node.getInternalDefaults().size() == 0) {
            return factory.createParameters(args, paramNames);
        }

        return factory.createParametersWithDefaults(args, environment.getDefaultArgumentNodes(), paramNames);
    }

    List<PNode> walkExprList(List<expr> exprs) throws Exception {
        List<PNode> targets = new ArrayList<>();

        for (expr source : exprs) {
            targets.add((PNode) visit(source));
        }

        return targets;
    }

    List<KeywordLiteralNode> walkKeywordList(List<keyword> keywords) throws Exception {
        List<KeywordLiteralNode> targets = new ArrayList<>();

        for (keyword source : keywords) {
            targets.add(visitKeyword(source));
        }

        return targets;
    }

    private PNode createSingleImportStatement(alias aliaz, String fromModuleName) {
        String importName = aliaz.getInternalName();
        String target = aliaz.getInternalAsname() != null ? aliaz.getInternalAsname() : importName;
        PNode importNode = factory.createImport(context, fromModuleName, importName);
        ReadNode read = environment.findVariable(target);
        return read.makeWriteNode(importNode);
    }

    @Override
    public Object visitImport(Import node) throws Exception {
        List<alias> aliases = node.getInternalNames();
        assert !aliases.isEmpty();

        if (aliases.size() == 1) {
            return createSingleImportStatement(aliases.get(0), null);
        }

        List<PNode> imports = new ArrayList<>();
        for (int i = 0; i < aliases.size(); i++) {
            imports.add(createSingleImportStatement(aliases.get(i), null));

        }

        return factory.createBlock(imports);
    }

    @Override
    public Object visitImportFrom(ImportFrom node) throws Exception {
        if (node.getInternalModule().compareTo("__future__") == 0) {
            return null;
        }
        List<alias> aliases = node.getInternalNames();
        assert !aliases.isEmpty();

        if (aliases.size() == 1) {
            return createSingleImportStatement(aliases.get(0), node.getInternalModule());
        }

        List<PNode> imports = new ArrayList<>();
        for (int i = 0; i < aliases.size(); i++) {
            imports.add(createSingleImportStatement(aliases.get(i), node.getInternalModule()));

        }

        return factory.createBlock(imports);
    }

    protected KeywordLiteralNode visitKeyword(keyword node) throws Exception {
        PNode value = (PNode) visit(node.getInternalValue());
        return new KeywordLiteralNode(value, node.getInternalArg());
    }

    @Override
    public Object visitClassDef(ClassDef node) throws Exception {
        String name = node.getInternalName();
        List<PNode> bases = walkExprList(node.getInternalBases());
        assert bases.size() <= 1 : "Multiple super class is not supported yet!";

        environment.beginScope(node, ScopeInfo.ScopeKind.Class);
        BlockNode body = factory.createBlock(visitStatements(node.getInternalBody()));
        FunctionDefinitionNode funcDef = (FunctionDefinitionNode) createFunctionDefinitnion(name, new Arity(name, 0, 0, new ArrayList<String>()), ParametersNode.EMPTY_PARAMS, body);
        environment.endScope(node);

        // The default super class is the <class 'object'>.
        PNode base;
        if (bases.size() == 0 || bases.get(0) == null) {
            base = factory.createObjectLiteral(context.getObjectClass());
        } else {
            base = bases.get(0);
        }

        PNode classDef = factory.createClassDef(name, base, funcDef);
        ReadNode read = environment.findVariable(name);
        return read.makeWriteNode(classDef);
    }

    @Override
    public Object visitCall(Call node) throws Exception {
        PNode callee = (PNode) visit(node.getInternalFunc());
        List<PNode> arguments = walkExprList(node.getInternalArgs());
        PNode[] argumentsArray = arguments.toArray(new PNode[arguments.size()]);
        List<KeywordLiteralNode> keywords = walkKeywordList(node.getInternalKeywords());
        KeywordLiteralNode[] keywordsArray = keywords.toArray(new KeywordLiteralNode[keywords.size()]);

        if (callee instanceof LoadAttributeNode) {
            LoadAttributeNode attr = (LoadAttributeNode) callee;
            return factory.createAttributeCall(attr.getPrimary(), attr.getAttributeId(), argumentsArray);
        }

        return factory.createCallFunction(callee, argumentsArray, keywordsArray, context);
    }

    @Override
    public Object visitName(Name node) throws Exception {
        if (isBoolOrNone(node)) {
            return getBoolOrNode(node);
        }

        return isParam(node) ? environment.getWriteArgumentToLocal(node.getInternalId()) : environment.findVariable(node.getInternalId());
    }

    @Override
    public Object visitExpr(Expr node) throws Exception {
        return visit(node.getInternalValue());
    }

    @Override
    public Object visitList(org.python.antlr.ast.List node) throws Exception {
        List<PNode> elts = walkExprList(node.getInternalElts());
        return factory.createListLiteral(elts);
    }

    @Override
    public Object visitSet(org.python.antlr.ast.Set node) throws Exception {
        List<PNode> elts = walkExprList(node.getInternalElts());
        Set<PNode> setFromLost = new HashSet<>();
        for (PNode listNode : elts) {
            setFromLost.add(listNode);
        }

        return factory.createSetLiteral(setFromLost);
    }

    @Override
    public Object visitTuple(Tuple node) throws Exception {
        List<PNode> elts = walkExprList(node.getInternalElts());
        return factory.createTupleLiteral(elts);
    }

    @Override
    public Object visitDict(Dict node) throws Exception {
        List<PNode> keys = walkExprList(node.getInternalKeys());
        List<PNode> vals = walkExprList(node.getInternalValues());
        return factory.createDictLiteral(keys, vals);
    }

    // TODO: Translate AugAssign to in-place operations ?
    @Override
    public Object visitAugAssign(AugAssign node) throws Exception {
        PNode target = (PNode) visit(node.getInternalTarget());
        PNode value = (PNode) visit(node.getInternalValue());
        PNode binaryOp = factory.createBinaryOperation(node.getInternalOp(), target, value);
        return ((ReadNode) target).makeWriteNode(binaryOp);
    }

    @Override
    public Object visitAssign(Assign node) throws Exception {
        return assigns.translate(node);
    }

    @Override
    public Object visitBinOp(BinOp node) throws Exception {
        PNode left = (PNode) visit(node.getInternalLeft());
        PNode right = (PNode) visit(node.getInternalRight());
        operatorType op = node.getInternalOp();
        return factory.createBinaryOperation(op, left, right);
    }

    @Override
    public Object visitBoolOp(BoolOp node) throws Exception {
        boolopType op = node.getInternalOp();
        List<PNode> values = walkExprList(node.getInternalValues());
        PNode left = values.get(0);
        List<PNode> rights = values.subList(1, values.size());
        return factory.createBooleanOperations(left, op, rights);
    }

    @Override
    public Object visitCompare(Compare node) throws Exception {
        List<cmpopType> ops = node.getInternalOps();
        PNode left = (PNode) visit(node.getInternalLeft());
        List<PNode> rights = walkExprList(node.getInternalComparators());
        return factory.createComparisonOperations(left, ops, rights);
    }

    @Override
    public Object visitUnaryOp(UnaryOp node) throws Exception {
        unaryopType op = node.getInternalOp();
        PNode operand = (PNode) visit(node.getInternalOperand());
        return factory.createUnaryOperation(op, operand);
    }

    @Override
    public Object visitAttribute(Attribute node) throws Exception {
        PNode primary = (PNode) visit(node.getInternalValue());

        if (PythonOptions.CacheAttributeLoads) {
            return factory.createGetAttribute(context, primary, node.getInternalAttr());
        } else {
            return factory.createLoadAttribute(primary, node.getInternalAttr());
        }
    }

    @Override
    public Object visitSlice(Slice node) throws Exception {
        PNode lower = (PNode) (node.getInternalLower() == null ? null : visit(node.getInternalLower()));
        PNode upper = (PNode) (node.getInternalUpper() == null ? null : visit(node.getInternalUpper()));
        PNode step = (PNode) (node.getInternalStep() == null ? null : visit(node.getInternalStep()));

        if (lower == null || lower instanceof NoneLiteralNode) {
            lower = factory.createIntegerLiteral(SequenceUtil.MISSING_INDEX);
        }
        if (upper == null || upper instanceof NoneLiteralNode) {
            upper = factory.createIntegerLiteral(SequenceUtil.MISSING_INDEX);
        }
        if (step == null || step instanceof NoneLiteralNode) {
            step = factory.createIntegerLiteral(1);
        }

        return factory.createSlice(lower, upper, step);
    }

    @Override
    public Object visitIndex(Index node) throws Exception {
        PNode index = (PNode) visit(node.getInternalValue());
        return factory.createIndex(index);
    }

    @Override
    public Object visitSubscript(Subscript node) throws Exception {
        PNode primary = (PNode) visit(node.getInternalValue());
        PNode slice = (PNode) visit(node.getInternalSlice());

        if (primary instanceof StoreAttributeNode) {
            primary = ((StoreAttributeNode) primary).makeReadNode();
        }

        if (!(node.getInternalSlice() instanceof Slice)) {
            return factory.createSubscriptLoadIndex(primary, slice);
        } else {
            return factory.createSubscriptLoad(primary, slice);
        }
    }

    @Override
    public Object visitListComp(ListComp node) throws Exception {
        FrameSlot slot = environment.nextListComprehensionSlot();
        PNode body = factory.createListAppend(environment.getListComprehensionSlot(), (PNode) visit(node.getInternalElt()));
        PNode comp = visitComprehensions(node.getInternalGenerators(), body);
        return factory.createListComprehension(slot, comp);

    }

    private PNode visitComprehensions(List<comprehension> comprehensions, PNode body) throws Exception {
        assert body != null;
        PNode current = body;
        List<comprehension> reversed = Lists.reverse(comprehensions);

        for (int i = 0; i < reversed.size(); i++) {
            comprehension comp = reversed.get(i);

            List<expr> conditions = comp.getInternalIfs();
            if (conditions != null && !conditions.isEmpty()) {
                current = factory.createIf(factory.toBooleanCastNode((PNode) visit(conditions.get(0))), current, PNode.EMPTYNODE);
            }

            PNode target = ((ReadNode) visit(comp.getInternalTarget())).makeWriteNode(factory.createRuntimeValueNode());
            PNode iterator = (PNode) visit(comp.getInternalIter());
            current = createForInScope(target, iterator, current);
        }

        assert current != null;
        return current;
    }

    private LoopNode createForInScope(PNode target, PNode iterator, PNode body) {
        GetIteratorNode getIterator = factory.createGetIterator(iterator);

        if (environment.isInFunctionScope()) {
            return factory.createForWithLocalTarget((WriteLocalVariableNode) target, getIterator, body);
        } else {
            return factory.createFor(target, getIterator, body);
        }
    }

    @Override
    public Object visitGeneratorExp(GeneratorExp node) throws Exception {
        environment.beginScope(node, ScopeInfo.ScopeKind.Generator);
        PNode body = factory.createYield((PNode) visit(node.getInternalElt()));
        LoopNode comprehension = (LoopNode) visitComprehensions(node.getInternalGenerators(), factory.createSingleStatementBlock(body));
        PNode genExprDef = createGeneratorExpressionDefinition(comprehension);
        environment.endScope(node);
        return genExprDef;
    }

    @Override
    public Object visitReturn(Return node) throws Exception {
        PNode value = null;
        StatementNode returnNode = null;

        if (node.getInternalValue() != null) {
            value = (PNode) visit(node.getInternalValue());
        }

        if (value == null) {
            returnNode = factory.createReturn();
        } else if (PythonOptions.ReturnValueInFrame) {
            PNode write = factory.createWriteLocalVariable(value, environment.getReturnSlot());
            returnNode = factory.createFrameReturn(write);
        } else {
            returnNode = factory.createExplicitReturn(value);
        }

        return returnNode;
    }

    @Override
    public Object visitBreak(Break node) throws Exception {
        loops.addBreak();
        return factory.createBreak();
    }

    @Override
    public Object visitContinue(Continue node) throws Exception {
        loops.addContinue();
        return factory.createContinue();
    }

    @Override
    public Object visitIf(If node) throws Exception {
        List<PNode> then = visitStatements(node.getInternalBody());
        List<PNode> orelse = visitStatements(node.getInternalOrelse());
        PNode test = (PNode) visit(node.getInternalTest());
        BlockNode thenPart = factory.createBlock(then);
        BlockNode elsePart = factory.createBlock(orelse);
        return factory.createIf(factory.toBooleanCastNode(test), thenPart, elsePart);
    }

    @Override
    public Object visitWhile(While node) throws Exception {
        loops.beginLoop(node);
        PNode test = (PNode) visit(node.getInternalTest());
        List<PNode> body = visitStatements(node.getInternalBody());
        List<PNode> orelse = visitStatements(node.getInternalOrelse());
        BlockNode bodyPart = factory.createBlock(body);
        BlockNode orelsePart = factory.createBlock(orelse);
        return createWhileNode(test, bodyPart, orelsePart, loops.endLoop());
    }

    private StatementNode createWhileNode(PNode test, BlockNode body, BlockNode orelse, LoopInfo info) {
        StatementNode wrappedBody = body;
        if (info.hasContinue()) {
            wrappedBody = factory.createContinueTarget(body);
        }

        StatementNode whileNode = factory.createWhile(factory.toBooleanCastNode(test), wrappedBody);

        if (!orelse.isEmpty()) {
            whileNode = factory.createElse(whileNode, orelse);
        }

        if (info.hasBreak()) {
            return factory.createBreakTarget(whileNode);
        } else {
            return whileNode;
        }
    }

    @Override
    public Object visitFor(For node) throws Exception {
        loops.beginLoop(node);
        List<expr> lhs = new ArrayList<>();
        lhs.add(node.getInternalTarget());

        PNode runtimeValue = factory.createRuntimeValueNode();
        List<PNode> targets = assigns.walkTargetList(lhs, runtimeValue);
        PNode iteratorWrite = targets.remove(0);

        PNode iter = (PNode) visit(node.getInternalIter());
        List<PNode> body = visitStatements(node.getInternalBody());
        List<PNode> orelse = visitStatements(node.getInternalOrelse());
        body.addAll(0, targets);
        BlockNode bodyPart = factory.createBlock(body);
        BlockNode orelsePart = factory.createBlock(orelse);
        return createForNode(iteratorWrite, iter, bodyPart, orelsePart, loops.endLoop());
    }

    private StatementNode createForNode(PNode target, PNode iter, BlockNode body, BlockNode orelse, LoopInfo info) {
        StatementNode wrappedBody = body;
        if (info.hasContinue()) {
            wrappedBody = factory.createContinueTarget(body);
        }

        StatementNode forNode = createForInScope(target, iter, wrappedBody);

        if (!orelse.isEmpty()) {
            forNode = factory.createElse(forNode, orelse);
        }

        if (info.hasBreak()) {
            return factory.createBreakTarget(forNode);
        } else {
            return forNode;
        }
    }

    @Override
    public Object visitPrint(Print node) throws Exception {
        /**
         * This is a workaround for what produced by Jython's parser. <br>
         * It parses print to a statement with multiple arguments nested in a tuple. It causes the
         * output to always be a tuple string. Therefore, unwrap the tuple is necessary. <br>
         * However, we cannot distinguish between a real tuple parameter and an artificial one. The
         * real fix should be in the parser.
         */
        List<expr> exprs = node.getInternalValues();
        if (exprs.size() == 1 && exprs.get(0) instanceof Tuple) {
            Tuple tuple = (Tuple) exprs.get(0);
            List<PNode> values = walkExprList(tuple.getInternalElts());
            return factory.createPrint(values, node.getInternalNl(), context);
        } else {
            List<PNode> values = walkExprList(node.getInternalValues());
            StatementNode newNode = factory.createPrint(values, node.getInternalNl(), context);
            return newNode;
        }
    }

    @Override
    public Object visitNum(Num node) throws Exception {
        Object value = node.getInternalN();

        if (value instanceof PyInteger) {
            return factory.createIntegerLiteral(((PyInteger) value).getValue());
        } else if (value instanceof PyLong) {
            return factory.createBigIntegerLiteral(((PyLong) value).getValue());
        } else if (value instanceof PyFloat) {
            return factory.createDoubleLiteral(((PyFloat) value).getValue());
        } else if (value instanceof PyComplex) {
            PyComplex pyComplex = (PyComplex) value;
            PComplex complex = new PComplex(pyComplex.real, pyComplex.imag);
            return factory.createComplexLiteral(complex);
        } else {
            throw notCovered();
        }
    }

    @Override
    public Object visitGlobal(Global node) throws Exception {
        return null;
    }

    @Override
    public Object visitYield(Yield node) throws Exception {
        environment.setToGeneratorScope();
        PNode right = (PNode) visit(node.getInternalValue());
        return factory.createYield(right);
    }

    @Override
    public Object visitStr(Str node) throws Exception {
        PyString s = (PyString) node.getInternalS();
        return factory.createStringLiteral(s.getString());
    }

    @Override
    public Object visitIfExp(IfExp node) throws Exception {
        PNode test = (PNode) visit(node.getInternalTest());
        PNode then = (PNode) visit(node.getInternalBody());
        PNode orelse = (PNode) visit(node.getInternalOrelse());
        return factory.createIfExpNode(factory.toBooleanCastNode(test), then, orelse);
    }

    @Override
    public Object visitPass(Pass node) throws Exception {
        return null;
    }

    @Override
    public Object visitTryExcept(TryExcept node) throws Exception {
        StatementNode retVal = null;
        List<PNode> b = visitStatements(node.getInternalBody());
        List<PNode> o = visitStatements(node.getInternalOrelse());
        BlockNode body = null;
        BlockNode orelse = null;
        List<excepthandler> excepts = node.getInternalHandlers();

        for (int i = 0; i < excepts.size(); i++) {
            if (i == 0) {
                body = factory.createBlock(b);
            } else {
                List<PNode> trynode = new ArrayList<>();
                trynode.add(retVal);
                body = factory.createBlock(trynode);
            }

            if (i == excepts.size() - 1) {
                orelse = factory.createBlock(o);
            }

            ExceptHandler except = (ExceptHandler) excepts.get(i);
            PNode exceptType = (PNode) visit(except.getInternalType());
            PNode exceptName = (except.getInternalName() == null) ? null : ((ReadNode) visit(except.getInternalName())).makeWriteNode(PNode.EMPTYNODE);
            List<PNode> exceptbody = visitStatements(except.getInternalBody());
            BlockNode exceptBody = factory.createBlock(exceptbody);
            retVal = factory.createTryExceptNode(body, orelse, exceptType, exceptName, exceptBody);
        }

        return retVal;
    }

    @Override
    public Object visitTryFinally(TryFinally node) throws Exception {
        List<PNode> b = visitStatements(node.getInternalBody());
        List<PNode> f = visitStatements(node.getInternalFinalbody());
        BlockNode body = factory.createBlock(b);
        BlockNode finalbody = factory.createBlock(f);
        return factory.createTryFinallyNode(body, finalbody);
    }

    @Override
    public Object visitRaise(Raise node) throws Exception {
        PNode type = (PNode) visit(node.getInternalType());
        PNode inst = (node.getInternalInst() == null) ? null : (PNode) visit(node.getInternalInst());
        return factory.createRaiseNode(type, inst);
    }

    @Override
    public Object visitAssert(Assert node) throws Exception {
        PNode test = (PNode) visit(node.getInternalTest());
        CastToBooleanNode condition = factory.toBooleanCastNode(test);
        PNode msg = node.getInternalMsg() == null ? null : (PNode) visit(node.getInternalMsg());
        return factory.createAssert(condition, msg);
    }

}
