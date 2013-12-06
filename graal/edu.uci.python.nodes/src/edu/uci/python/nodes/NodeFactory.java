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
package edu.uci.python.nodes;

import java.math.*;
import java.util.*;
import java.util.List;
import java.util.Set;

import org.python.antlr.PythonTree;
import org.python.antlr.ast.*;
import org.python.antlr.base.*;

import edu.uci.python.nodes.literals.*;
import edu.uci.python.nodes.loop.*;
import edu.uci.python.nodes.function.*;
import edu.uci.python.nodes.generator.*;
import edu.uci.python.nodes.generator.GeneratorLoopNodeFactory.InnerGeneratorLoopNodeFactory;
import edu.uci.python.nodes.generator.GeneratorLoopNodeFactory.OuterGeneratorLoopNodeFactory;
import edu.uci.python.nodes.objects.*;
import edu.uci.python.nodes.statements.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.access.*;
import edu.uci.python.nodes.argument.*;
import edu.uci.python.nodes.attribute.*;
import edu.uci.python.nodes.calls.*;
import edu.uci.python.nodes.expressions.*;
import edu.uci.python.nodes.expressions.CastToBooleanNodeFactory.NotNodeFactory;
import edu.uci.python.nodes.expressions.CastToBooleanNodeFactory.YesNodeFactory;
import edu.uci.python.nodes.expressions.BinaryBooleanNodeFactory.*;
import edu.uci.python.nodes.expressions.BinaryComparisonNodeFactory.*;
import edu.uci.python.nodes.expressions.BinaryBitwiseNodeFactory.*;
import edu.uci.python.nodes.expressions.BinaryArithmeticNodeFactory.*;
import edu.uci.python.nodes.expressions.UnaryArithmeticNodeFactory.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.runtime.standardtypes.*;

public class NodeFactory {

    @CompilationFinal private static NodeFactory factory;

    public static NodeFactory getInstance() {
        if (factory == null) {
            factory = new NodeFactory();
        }
        return factory;
    }

    public RootNode createModule(List<PNode> body, FrameDescriptor fd) {
        BlockNode block = createBlock(body);
        return new ModuleNode(block, fd);
    }

    public PNode createFunctionDef(String name, ParametersNode parameters, CallTarget callTarget, FrameDescriptor frameDescriptor, boolean needsDeclarationFrame) {
        return new FunctionDefinitionNode(name, parameters, callTarget, frameDescriptor, needsDeclarationFrame);
    }

    public FunctionRootNode createFunctionRoot(String functionName, ParametersNode parameters, StatementNode body, PNode returnValue) {
        return new FunctionRootNode(functionName, parameters, body, returnValue);
    }

    public RootNode createGeneratorRoot(String functionName, ParametersNode parameters, StatementNode body, PNode returnValue) {
        return new GeneratorDefinitionNode(functionName, parameters, body, returnValue);
    }

    public PNode createAddClassAttribute(String attributeId, PNode rhs) {
        return new AddClassAttributeNode(attributeId, rhs);
    }

    public PNode createReadClassAttribute(String attributeId) {
        return new AddClassAttributeNode.ReadClassAttributeNode(attributeId);
    }

    public ParametersNode createParameters(List<PNode> args, List<String> paramNames) {
        if (args.size() == 1) {
            return createParametersOfSizeOne(args.get(0), paramNames);
        } else if (args.size() == 2) {
            return createParametersOfSizeTwo(args.get(0), args.get(1), paramNames);
        } else {
            return createParametersWithNoDefaults(args, paramNames);
        }
    }

    public ParametersNode createParametersOfSizeOne(PNode parameter, List<String> paramNames) {
        return new ParametersOfSizeOneNode(paramNames, parameter);
    }

    public ParametersNode createParametersOfSizeTwo(PNode parameter0, PNode parameter1, List<String> paramNames) {
        return new ParametersOfSizeTwoNode(paramNames, parameter0, parameter1);
    }

    public ParametersNode createParametersWithDefaults(List<PNode> parameters, List<PNode> defaults, List<String> paramNames) {
        ReadDefaultArgumentNode[] defaultReads = new ReadDefaultArgumentNode[defaults.size()];
        int index = 0;

        for (PNode right : defaults) {
            defaultReads[index] = new ReadDefaultArgumentNode(right);
            index++;
        }

        /**
         * The alignment between default argument and parameter relies on the restriction that
         * default arguments are right aligned. The Vararg case is not covered yet.
         */
        PNode[] defaultWrites = new PNode[defaults.size()];
        index = 0;
        int offset = parameters.size() - defaults.size();

        for (ReadDefaultArgumentNode read : defaultReads) {
            FrameSlotNode slotNode = (FrameSlotNode) parameters.get(index + offset);
            FrameSlot slot = slotNode.getSlot();
            defaultWrites[index] = createWriteLocalVariable(read, slot);
            index++;
        }

        return new ParametersWithDefaultsNode(parameters.toArray(new PNode[parameters.size()]), paramNames, defaultReads, defaultWrites);
    }

    public ParametersNode createParametersWithNoDefaults(List<PNode> parameters, List<String> paramNames) {
        return new ParametersWithNoDefaultsNode(parameters.toArray(new PNode[parameters.size()]), paramNames);
    }

    public ClassDefinitionNode createClassDef(String name, PNode superclass, FunctionDefinitionNode definitnionFunction) {
        return new ClassDefinitionNode(name, superclass, definitnionFunction);
    }

    public BlockNode createSingleStatementBlock(PNode stmt) {
        return new BlockNode(new PNode[]{stmt});
    }

    public BlockNode createBlock(List<PNode> statements) {
        PNode[] array = statements.toArray(new PNode[statements.size()]);
        return new BlockNode(array);
    }

    public PNode createImport(PythonContext context, String fromModuleName, String importee) {
        return new ImportNode(context, fromModuleName, importee);
    }

    public LoopNode createWhile(CastToBooleanNode condition, StatementNode body) {
        return new WhileNode(condition, body);
    }

    public StatementNode createIf(CastToBooleanNode condition, BlockNode thenPart, BlockNode elsePart) {
        return new IfNode(condition, thenPart, elsePart);
    }

    public GetIteratorNode createGetIterator(PNode collection) {
        return GetIteratorNodeFactory.create(collection);
    }

    public LoopNode createFor(PNode target, GetIteratorNode iterator, StatementNode body) {
        return ForNodeFactory.create(target, body, iterator);
    }

    public LoopNode createForWithLocalTarget(WriteLocalVariableNode target, GetIteratorNode iterator, StatementNode body) {
        return ForWithLocalTargetNodeFactory.create(target, body, iterator);
    }

    public StatementNode createElse(StatementNode then, BlockNode orelse) {
        return new ElseNode(then, orelse);
    }

    public StatementNode createReturn() {
        return new ReturnNode();
    }

    public StatementNode createExplicitReturn(PNode value) {
        return new ReturnNode.ExplicitReturnNode(value);
    }

    public StatementNode createFrameReturn(PNode value) {
        return new ReturnNode.FrameReturnNode(value);
    }

    public StatementNode createBreak() {
        return new BreakNode();
    }

    public StatementNode createContinue() {
        return new ContinueNode();
    }

    public StatementNode createContinueTarget(BlockNode child) {
        return new ContinueTargetNode(child);
    }

    public StatementNode createBreakTarget(StatementNode child) {
        return new BreakTargetNode(child);
    }

    public StatementNode createYield(PNode right) {
        return new YieldNode(right);
    }

    public StatementNode createPrint(List<PNode> values, boolean nl, PythonContext context) {
        return new PrintNode(values.toArray(new PNode[values.size()]), nl, context);
    }

    public PNode createIntegerLiteral(int value) {
        return new IntegerLiteralNode(value);
    }

    public PNode createBigIntegerLiteral(BigInteger value) {
        return new BigIntegerLiteralNode(value);
    }

    public PNode createDoubleLiteral(double value) {
        return new DoubleLiteralNode(value);
    }

    public PNode createComplexLiteral(PComplex value) {
        return new ComplexLiteralNode(value);
    }

    public PNode createStringLiteral(String value) {
        return new StringLiteralNode(value);
    }

    public PNode createDictLiteral(List<PNode> keys, List<PNode> values) {
        PNode[] convertedKeys = keys.toArray(new PNode[keys.size()]);
        PNode[] convertedValues = values.toArray(new PNode[values.size()]);
        return new DictLiteralNode(convertedKeys, convertedValues);
    }

    public PNode createTupleLiteral(List<PNode> values) {
        PNode[] convertedValues = values.toArray(new PNode[values.size()]);
        return new TupleLiteralNode(convertedValues);
    }

    public PNode createListLiteral(List<PNode> values) {
        PNode[] convertedValues = values.toArray(new PNode[values.size()]);
        return new ListLiteralNode(convertedValues);
    }

    public PNode createSetLiteral(Set<PNode> values) {
        PNode[] convertedValues = values.toArray(new PNode[values.size()]);
        return new ListLiteralNode(convertedValues);
    }

    public PNode createListComprehension(FrameSlot frameSlot, PNode comprehension) {
        return new ListComprehensionNode(frameSlot, comprehension);
    }

    public PNode createListAppend(FrameSlot frameSlot, PNode right) {
        return ListAppendNodeFactory.create(frameSlot, right);
    }

    public PNode createOuterGeneratorLoop(PNode target, PNode iterator, CastToBooleanNode condition, PNode innerLoop) {
        return OuterGeneratorLoopNodeFactory.create(target, condition, innerLoop, iterator);
    }

    public PNode createInnerGeneratorLoop(PNode target, PNode iterator, CastToBooleanNode condition, PNode loopBody) {
        return InnerGeneratorLoopNodeFactory.create(target, condition, loopBody, iterator);
    }

    public PNode createGeneratorExpression(CallTarget callTarget, GeneratorExpressionRootNode generator, FrameDescriptor descriptor, boolean needsDeclarationFrame) {
        // replace write local with write materialized frame
        for (WriteLocalVariableNode write : NodeUtil.findAllNodeInstances(generator, WriteLocalVariableNode.class)) {
            write.replace(WriteMaterializedFrameVariableNodeFactory.create(write.getSlot(), write.getRhs()));
        }

        for (ReadLocalVariableNode read : NodeUtil.findAllNodeInstances(generator, ReadLocalVariableNode.class)) {
            read.replace(ReadMaterializedFrameVariableNodeFactory.create(read.getSlot()));
        }

        return new GeneratorExpressionDefinitionNode(callTarget, descriptor, needsDeclarationFrame);
    }

    public GeneratorExpressionRootNode createGenerator(GeneratorLoopNode comprehension, PNode returnValue) {
        return new GeneratorExpressionRootNode("generator_exp", ParametersNode.EMPTY_PARAMS, comprehension, returnValue);
    }

    public PNode createUnaryOperation(unaryopType operator, PNode operand) {
        PNode typedNodeOperand = operand;

        switch (operator) {
            case UAdd:
                return PlusNodeFactory.create(typedNodeOperand);
            case USub:
                return MinusNodeFactory.create(typedNodeOperand);
            case Invert:
                return InvertNodeFactory.create(typedNodeOperand);
            case Not:
                return NotNodeFactory.create(typedNodeOperand);
            default:
                throw new RuntimeException("unexpected operation: " + operator);
        }
    }

    public PNode createBinaryOperation(operatorType operator, PNode left, PNode right) {
        switch (operator) {
            case Add:
                return AddNodeFactory.create(left, right);
            case Sub:
                return SubNodeFactory.create(left, right);
            case Mult:
                return MulNodeFactory.create(left, right);
            case Div:
                return DivNodeFactory.create(left, right);
            case FloorDiv:
                return FloorDivNodeFactory.create(left, right);
            case Mod:
                return ModuloNodeFactory.create(left, right);
            case Pow:
                return PowerNodeFactory.create(left, right);
            case LShift:
                return LeftShiftNodeFactory.create(left, right);
            case RShift:
                return RightShiftNodeFactory.create(left, right);
            case BitAnd:
                return BitAndNodeFactory.create(left, right);
            case BitOr:
                return BitOrNodeFactory.create(left, right);
            case BitXor:
                return BitXorNodeFactory.create(left, right);
            default:
                throw new RuntimeException("unexpected operation: " + operator);
        }
    }

    public PNode createAttributeCall(PNode primary, String name, PNode[] args) {
        return CallAttributeNodeFactory.create(name, args, primary);
    }

    public PNode createBinaryOperations(PNode left, operatorType op, List<PNode> rights) {
        PNode current = createBinaryOperation(op, left, rights.get(0));

        for (int i = 1; i < rights.size(); i++) {
            PNode right = rights.get(i);
            current = createBinaryOperation(op, current, right);
        }

        return current;
    }

    public PNode createComparisonOperation(cmpopType operator, PNode left, PNode right) {
        switch (operator) {
            case Eq:
                return EqualNodeFactory.create(left, right);
            case NotEq:
                return NotEqualNodeFactory.create(left, right);
            case Lt:
                return LessThanNodeFactory.create(left, right);
            case LtE:
                return LessThanEqualNodeFactory.create(left, right);
            case Gt:
                return GreaterThanNodeFactory.create(left, right);
            case GtE:
                return GreaterThanEqualNodeFactory.create(left, right);
            case Is:
                return IsNodeFactory.create(left, right);
            case IsNot:
                return IsNotNodeFactory.create(left, right);
            case In:
                return InNodeFactory.create(left, right);
            default:
                throw new RuntimeException("unexpected operation: " + operator);
        }
    }

    public PNode createComparisonOperations(PNode left, List<cmpopType> ops, List<PNode> rights) {
        PNode current = createComparisonOperation(ops.get(0), left, rights.get(0));

        for (int i = 1; i < rights.size(); i++) {
            PNode right = rights.get(i);
            current = createComparisonOperation(ops.get(i), current, right);
        }

        return current;
    }

    PNode createBooleanOperation(boolopType operator, PNode left, PNode right) {
        switch (operator) {
            case And:
                return AndNodeFactory.create(left, right);
            case Or:
                return OrNodeFactory.create(left, right);
            default:
                throw new RuntimeException("unexpected operation: " + operator);
        }
    }

    public PNode createBooleanOperations(PNode left, boolopType operator, List<PNode> rights) {
        PNode current = createBooleanOperation(operator, left, rights.get(0));

        for (int i = 1; i < rights.size(); i++) {
            PNode right = rights.get(i);
            current = createBooleanOperation(operator, current, right);
        }

        return current;
    }

    public PNode createGetAttribute(PythonContext context, PNode primary, String name) {
        return new GetAttributeNode.UninitializedGetAttributeNode(context, name, primary);
    }

    public PNode createLoadAttribute(PNode operand, String name) {
        return new UninitializedLoadAttributeNode(name, operand);
    }

    public PNode createStoreAttribute(PNode primary, String name, PNode value) {
        return new UninitializedStoreAttributeNode(name, primary, value);
    }

    public PNode createSlice(PNode lower, PNode upper, PNode step) {
        return SliceNodeFactory.create(lower, upper, step);
    }

    public PNode createIndex(PNode operand) {
        return IndexNodeFactory.create(operand);
    }

    public PNode createSubscriptLoad(PNode primary, PNode slice) {
        return SubscriptLoadNodeFactory.create(primary, slice);
    }

    public PNode createSubscriptStore(PNode primary, PNode slice, PNode value) {
        return SubscriptStoreNodeFactory.create(primary, slice, value);
    }

    public PNode createReadLocalVariable(FrameSlot slot) {
        assert slot != null;
        return ReadLocalVariableNodeFactory.create(slot);
    }

    public PNode createReadLevelVariable(FrameSlot slot, int level) {
        return ReadLevelVariableNodeFactory.create(slot, level);
    }

    public PNode createWriteLocalVariable(PNode right, FrameSlot slot) {
        return WriteLocalVariableNodeFactory.create(slot, right);
    }

    public PNode createReadGlobalScope(PythonContext context, PythonModule globalScope, String attributeId) {
        return new ReadGlobalScopeNode(context, globalScope, attributeId);
    }

    public PNode createBooleanLiteral(boolean value) {
        return new BooleanLiteralNode(value);
    }

    public PNode createNoneLiteral() {
        return new NoneLiteralNode();
    }

    public PNode createObjectLiteral(Object obj) {
        return new ObjectLiteralNode(obj);
    }

    // public PNode createCallFunction(PNode callee, PNode[] arguments, PNode[] keywords,
// PythonContext context) {
    public PNode createCallFunction(PNode callee, PNode[] arguments, KeywordLiteralNode[] keywords, PythonContext context) {
        return new UninitializedCallFunctionNode(callee, arguments, keywords, context);
    }

    public PNode createKeywordLiteral(PNode value, String name) {
        return new KeywordLiteralNode(value, name);
    }

    public List<PythonTree> castToPythonTreeList(List<stmt> argsInit) {
        List<PythonTree> pythonTreeList = new ArrayList<>();

        for (stmt s : argsInit) {
            pythonTreeList.add(s);
        }

        return pythonTreeList;
    }

    public CastToBooleanNode toBooleanCastNode(PNode node) {
        // TODO: should fix the thing that this fixes
        if (node == null) {
            return null;
        }

        if (node instanceof CastToBooleanNode) {
            return (CastToBooleanNode) node;
        } else {
            return createYesNode(node);
        }
    }

    public CastToBooleanNode createYesNode(PNode operand) {
        return YesNodeFactory.create(operand);
    }

    public PNode createIfExpNode(CastToBooleanNode condition, PNode then, PNode orelse) {
        return new IfExpressionNode(condition, then, orelse);
    }

    public StatementNode createTryFinallyNode(BlockNode body, BlockNode finalbody) {
        return new TryFinallyNode(body, finalbody);
    }

    public StatementNode createTryExceptNode(BlockNode body, BlockNode orelse, PNode exceptType, PNode exceptName, BlockNode exceptBody) {
        return TryExceptNode.create(body, orelse, exceptType, exceptName, exceptBody);
    }

    public PNode createRaiseNode(PNode type, PNode inst) {
        return new RaiseNode(type, inst);
    }

    public StatementNode createAssert(CastToBooleanNode condition, PNode message) {
        return new AssertNode(condition, message);
    }

    public PNode createRuntimeValueNode() {
        return new RuntimeValueNode(null);
    }
}
