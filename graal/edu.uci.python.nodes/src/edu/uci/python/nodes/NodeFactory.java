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

import org.python.antlr.PythonTree;
import org.python.antlr.ast.*;
import org.python.antlr.base.*;
import edu.uci.python.nodes.literals.*;
import edu.uci.python.nodes.statements.*;

import org.python.core.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.expressions.*;
import edu.uci.python.nodes.expressions.BinaryBooleanNodeFactory.*;
import edu.uci.python.nodes.expressions.BinaryComparisonNodeFactory.*;
import edu.uci.python.nodes.expressions.BinaryBitwiseNodeFactory.*;
import edu.uci.python.nodes.expressions.BinaryArithmeticNodeFactory.*;
import edu.uci.python.nodes.expressions.ComprehensionNodeFactory.*;
import edu.uci.python.nodes.expressions.BooleanCastNodeFactory.*;
import edu.uci.python.nodes.expressions.UnaryArithmeticNodeFactory.*;
import edu.uci.python.runtime.datatypes.*;

public class NodeFactory {

    public RootNode createModule(List<PNode> body, FrameDescriptor fd) {
        BlockNode block = createBlock(body);
        return new ModuleNode(block, fd);
    }

    public StatementNode createFunctionDef(FrameSlot slot, String name, ParametersNode parameters, CallTarget callTarget, RootNode funcRoot) {
        return new FunctionDefNode(slot, name, parameters, callTarget, funcRoot);
    }

    public FunctionRootNode createFunctionRoot(ParametersNode parameters, StatementNode body, PNode returnValue) {
        return new FunctionRootNode(parameters, body, returnValue);
    }

    public RootNode createGeneratorRoot(ParametersNode parameters, StatementNode body, PNode returnValue) {
        return new GeneratorRootNode(parameters, body, returnValue);
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
            defaultWrites[index] = createWriteLocal(read, slot);
            index++;
        }

        return new ParametersWithDefaultsNode(parameters.toArray(new PNode[parameters.size()]), paramNames, defaultReads, defaultWrites);
    }

    public ParametersNode createParametersWithNoDefaults(List<PNode> parameters, List<String> paramNames) {
        return new ParametersWithNoDefaultsNode(parameters.toArray(new PNode[parameters.size()]), paramNames);
    }

    public BlockNode createBlock(List<PNode> statements) {
        PNode[] array = statements.toArray(new PNode[statements.size()]);
        return new BlockNode(array);
    }

    public StatementNode createImport(FrameSlot[] slots, String fromModule, String[] names) {
        return new ImportNode(slots, fromModule, names);
    }

    public StatementNode createWhile(BooleanCastNode condition, BlockNode body, BlockNode orelse) {
        return new WhileNode(condition, body, orelse);
    }

    public StatementNode createWhileTrue(BlockNode body) {
        return new WhileTrueNode(body);
    }

    public StatementNode createIf(BooleanCastNode condition, BlockNode thenPart, BlockNode elsePart) {
        return new IfNode(condition, thenPart, elsePart);
    }

    public StatementNode createFor(StatementNode target, PNode iterator, BlockNode body, BlockNode orelse) {
        return new ForNode(target, iterator, body, orelse);
    }

    public StatementNode createForRangeWithOneValue(StatementNode target, PNode start, BlockNode body, BlockNode orelse) {
        return new ForRangeWithOneValueNode(target, start, body, orelse);
    }

    public StatementNode createForRangeWithTwoValues(StatementNode target, PNode start, PNode stop, BlockNode body, BlockNode orelse) {
        return new ForRangeWithTwoValuesNode(target, start, stop, body, orelse);
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

    public StatementNode createYield(PNode right) {
        return new YieldNode(right);
    }

    public StatementNode createPrint(List<PNode> values, boolean nl) {
        return new PrintNode(values.toArray(new PNode[values.size()]), nl);
    }

    public PNode createIntegerLiteral(int value) {
        return IntegerLiteralNodeFactory.create(value);
    }

    public PNode createBigIntegerLiteral(BigInteger value) {
        return BigIntegerLiteralNodeFactory.create(value);
    }

    public PNode createDoubleLiteral(double value) {
        return DoubleLiteralNodeFactory.create(value);
    }

    public PNode createComplexLiteral(PComplex value) {
        return ComplexLiteralNodeFactory.create(value);
    }

    public PNode createStringLiteral(PyString value) {
        return StringLiteralNodeFactory.create(value.getString());
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

    public PNode createListComprehension(ComprehensionNode comprehension) {
        return ListComprehensionNodeFactory.create(comprehension);
    }

    public PNode createOuterComprehension(StatementNode target, PNode iterator, BooleanCastNode condition, PNode innerLoop) {
        return OuterComprehensionNodeFactory.create(target, condition, innerLoop, iterator);
    }

    public PNode createInnerComprehension(StatementNode target, PNode iterator, BooleanCastNode condition, PNode loopBody) {
        return InnerComprehensionNodeFactory.create(target, condition, loopBody, iterator);
    }

    public PNode createGeneratorExpression(GeneratorNode generator, FrameDescriptor descriptor) {
        return GeneratorExpressionNodeFactory.create(generator, descriptor);
    }

    public GeneratorNode createGenerator(ComprehensionNode comprehension, PNode returnValue) {
        return new GeneratorNode(ParametersNode.EMPTY_PARAMS, comprehension, returnValue);
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

    public PNode createAttributeCall(PNode primary, PNode[] args, String name) {
        return AttributeCallNodeFactory.create(args, name, primary);
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

    public PNode createAttributeRef(PNode operand, String name) {
        return AttributeRefNodeFactory.create(name, operand);
    }

    public PNode createAttributeUpdate(PNode primary, String name, PNode value) {
        return AttributeUpdateNodeFactory.create(name, primary, value);
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

    public PNode createReadLocal(FrameSlot slot) {
        return ReadLocalNodeFactory.create(slot);
    }

    public PNode createReadEnvironment(FrameSlot slot, int level) {
        return ReadEnvironmentNodeFactory.create(slot, level);
    }

    public PNode createWriteLocal(PNode right, FrameSlot slot) {
        return WriteLocalNodeFactory.create(slot, right);
    }

    public PNode createWriteGlobal(String name, PNode right) {
        return WriteGlobalNodeFactory.create(name, right);
    }

    public PNode createReadGlobal(String name) {
        return ReadGlobalNodeFactory.create(name);
    }

    public PNode createBooleanLiteral(boolean value) {
        return BooleanLiteralNodeFactory.create(value);
    }

    public PNode createNoneLiteral() {
        return NoneLiteralNodeFactory.create();
    }

    public PNode createCall(PNode callee, PNode[] arguments, PNode[] keywords) {
        return CallNodeFactory.create(arguments, keywords, callee);
    }

    public PNode createCallBuiltIn(PCallable callee, String name, PNode[] arguments, PNode[] keywords) {
        return CallBuiltInNodeFactory.create(callee, name, arguments, keywords);
    }

    public PNode createKeywordLiteral(PNode value, String name) {
        return KeywordLiteralNodeFactory.create(value, name);
    }

    public PNode createCallWithOneArgumentNoKeyword(PNode callee, PNode argument) {
        return CallWithOneArgumentNoKeywordNodeFactory.create(argument, callee);
    }

    public PNode createCallBuiltInWithOneArgNoKeyword(PCallable callee, String name, PNode argument) {
        return CallBuiltInWithOneArgNoKeywordNodeFactory.create(callee, name, argument);
    }

    public PNode createCallWithTwoArgumentsNoKeyword(PNode callee, PNode argument0, PNode argument1) {
        return CallWithTwoArgumentsNoKeywordNodeFactory.create(argument0, argument1, callee);
    }

    public PNode createCallBuiltInWithTwoArgsNoKeyword(PCallable callee, String name, PNode argument0, PNode argument1) {
        return CallBuiltInWithTwoArgsNoKeywordNodeFactory.create(callee, name, argument0, argument1);
    }

    public List<PythonTree> castToPythonTreeList(List<stmt> argsInit) {
        List<PythonTree> pythonTreeList = new ArrayList<>();

        for (stmt s : argsInit) {
            pythonTreeList.add(s);
        }

        return pythonTreeList;
    }

    public BooleanCastNode toBooleanCastNode(PNode node) {
        // TODO: should fix the thing that this fixes
        if (node == null) {
            return null;
        }

        if (node instanceof BooleanCastNode) {
            return (BooleanCastNode) node;
        } else {
            return createYesNode(node);
        }
    }

    public BooleanCastNode createYesNode(PNode operand) {
        return YesNodeFactory.create(operand);
    }

    public PNode createIfExpNode(PNode body, PNode test, PNode orelse) {
        return IfExpressionNodeFactory.create(test, body, orelse);
    }

    public PNode createRuntimeValueNode() {
        return new RuntimeValueNode(null);
    }

}
