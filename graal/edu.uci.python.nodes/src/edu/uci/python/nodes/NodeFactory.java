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

import edu.uci.python.nodes.literal.*;
import edu.uci.python.nodes.loop.*;
import edu.uci.python.nodes.function.*;
import edu.uci.python.nodes.generator.*;
import edu.uci.python.nodes.object.*;
import edu.uci.python.nodes.statement.*;
import edu.uci.python.nodes.subscript.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.access.*;
import edu.uci.python.nodes.attribute.*;
import edu.uci.python.nodes.call.*;
import edu.uci.python.nodes.expression.*;
import edu.uci.python.nodes.expression.BinaryBooleanNodeFactory.*;
import edu.uci.python.nodes.expression.BinaryArithmeticNodeFactory.*;
import edu.uci.python.nodes.expression.BinaryComparisonNodeFactory.*;
import edu.uci.python.nodes.expression.CastToBooleanNodeFactory.*;
import edu.uci.python.nodes.expression.BinaryBitwiseNodeFactory.*;
import edu.uci.python.nodes.expression.UnaryArithmeticNodeFactory.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.standardtype.*;

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

    public FunctionRootNode createFunctionRoot(PythonContext context, String functionName, FrameDescriptor frameDescriptor, PNode body) {
        return new FunctionRootNode(context, functionName, frameDescriptor, body);
    }

    public PNode createAddClassAttribute(String attributeId, PNode rhs) {
        return new AddClassAttributeNode(attributeId, rhs);
    }

    public PNode createReadClassAttribute(String attributeId) {
        return new AddClassAttributeNode.ReadClassAttributeNode(attributeId);
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

    public BlockNode createBlock(PNode[] statments) {
        return new BlockNode(statments);
    }

    public PNode createImport(PythonContext context, String importee) {
        return new ImportNode(context, importee);
    }

    public PNode createImportFrom(PythonContext context, String fromModuleName, String importee) {
        return new ImportFromNode(context, fromModuleName, importee);
    }

    public PNode createImportStar(PythonContext context, String fromModuleName) {
        return new ImportStarNode(context, fromModuleName);
    }

    public LoopNode createWhile(CastToBooleanNode condition, StatementNode body) {
        return new WhileNode(condition, body);
    }

    public StatementNode createIf(CastToBooleanNode condition, PNode thenPart, PNode elsePart) {
        return new IfNode(condition, thenPart, elsePart);
    }

    public GetIteratorNode createGetIterator(PNode collection) {
        return GetIteratorNodeFactory.create(collection);
    }

    public LoopNode createFor(PNode target, GetIteratorNode iterator, PNode body) {
        return ForNodeFactory.create(target, body, iterator);
    }

    public StatementNode createElse(StatementNode then, BlockNode orelse) {
        return new ElseNode(then, orelse);
    }

    public StatementNode createReturn() {
        return new ReturnNode();
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

    public StatementNode createYield(PNode right, FrameSlot returnSlot) {
        return new YieldNode(this.createWriteLocal(right, returnSlot));
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
        return new ListLiteralNode.UninitializedListLiteralNode(convertedValues);
    }

    public PNode createSetLiteral(Set<PNode> values) {
        PNode[] convertedValues = values.toArray(new PNode[values.size()]);
        return new SetLiteralNode(convertedValues);
    }

    public PNode createListComprehension(FrameSlot frameSlot, PNode comprehension) {
        return new ListComprehensionNode(frameSlot, comprehension);
    }

    public PNode createListAppend(FrameSlot frameSlot, PNode right) {
        return ListAppendNodeFactory.create(frameSlot, right);
    }

    public LoopNode createGeneratorForNode(WriteLocalVariableNode target, PNode getIterator, PNode body, int iteratorSlot) {
        return new GeneratorForNode(WriteGeneratorFrameVariableNodeFactory.create(target.getSlot(), target.getRhs()), (GetIteratorNode) getIterator, body, iteratorSlot);
    }

    public LoopNode createInnerGeneratorForNode(WriteLocalVariableNode target, PNode getIterator, PNode body, int iteratorSlot) {
        return new GeneratorForNode.InnerGeneratorForNode(WriteGeneratorFrameVariableNodeFactory.create(target.getSlot(), target.getRhs()), (GetIteratorNode) getIterator, body, iteratorSlot);
    }

    public PNode createGeneratorExpression(CallTarget callTarget, FrameDescriptor descriptor, boolean needsDeclarationFrame, int numOfGeneratorBlockNode, int numOfGeneratorForNode) {
        return new GeneratorExpressionDefinitionNode(callTarget, descriptor, needsDeclarationFrame, numOfGeneratorBlockNode, numOfGeneratorForNode);
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
            PNode newCompare = createComparisonOperation(ops.get(i), rights.get(i - 1), rights.get(i));
            current = AndNodeFactory.create(current, newCompare);
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
        return SubscriptLoadSliceNodeFactory.create(primary, slice);
    }

    public PNode createSubscriptLoadIndex(PNode primary, PNode slice) {
        return SubscriptLoadIndexNodeFactory.create(primary, slice);
    }

    public PNode createSubscriptStore(PNode primary, PNode slice, PNode value) {
        return SubscriptStoreSliceNodeFactory.create(primary, slice, value);
    }

    public PNode createSubscriptStoreIndex(PNode primary, PNode slice, PNode value) {
        return SubscriptStoreIndexNodeFactory.create(primary, slice, value);
    }

    public PNode createReadLocal(FrameSlot slot) {
        assert slot != null;
        return ReadLocalVariableNode.create(slot);
    }

    public PNode createReadLevel(FrameSlot slot, int level) {
        return ReadLevelVariableNode.create(slot, level);
    }

    public PNode createWriteLocal(PNode right, FrameSlot slot) {
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

    public StatementNode createAssert(CastToBooleanNode condition, PNode message) {
        return new AssertNode(condition, message);
    }

    public PNode createRuntimeValueNode() {
        return new RuntimeValueNode(null);
    }
}
