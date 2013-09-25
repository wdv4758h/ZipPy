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

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.antlr.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.expressions.*;
import edu.uci.python.nodes.literals.*;
import edu.uci.python.nodes.statements.*;
import edu.uci.python.nodes.translation.*;
import edu.uci.python.nodes.truffle.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.types.*;

public class Functions {

    private NodeFactory factory = null;
    private Variables var = null;
    private Assignments ass = null;
    private Actions actions = null;

    public Functions(NodeFactory factory) {
        this.factory = factory;
        this.var = new Variables(factory);
        this.ass = new Assignments(factory);
        this.actions = new Actions(factory);
    }

    public StatementNode makeYield(Token t, PNode node) {
        return (StatementNode) GrammarUtil.make(t, factory.createYield(node));
    }

    public PNode makeCall(Token t, PNode func) {
        return makeCall(t, func, null, null, null, null);
    }

    public PNode makeCall(Token t, PNode func, List<?> args, List<?> keywords, PNode starargs, PNode kwargs) {
        PNode retVal = null;
        if (func == null) {
            return ErrorHandler.errorExpr(t);
        }

        List<PNode> a = GrammarUtil.castExprs(args);
        PNode[] argumentsArray = a.toArray(new PNode[a.size()]);
        List<PNode> k = actions.makeKeywords(keywords);
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
        return GrammarUtil.make(t, retVal);
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

        List<PNode> s = GrammarUtil.castStmts(funcStatements);
        List<PNode> d = GrammarUtil.castExprs(decorators);

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
        return (StatementNode) GrammarUtil.make(t, slot, retVal);
    }

    public PNode makeAttribute(Token t, PNode value, PNode attr, ContextType ctx) {
        PNode retVal = null;
        if (ctx != ContextType.Load) {
            retVal = GrammarUtil.make(t, factory.createAttributeUpdate(value, attr.getText(), PNode.DUMMY_NODE));
        } else {
            retVal = GrammarUtil.make(t, factory.createAttributeRef(value, attr.getText()));
        }
        return retVal;
    }

    public PNode makeDottedAttr(Token nameToken, List<?> attrs) {
        PNode current = var.makeName(nameToken, nameToken.getText(), ContextType.Load);
        for (Object o : attrs) {
            Token t = (Token) o;
            current = makeAttribute(t, current, cantBeNoneName(t), ContextType.Load);
        }
        return current;
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
                defaults.add(var.readLocalToGlobal(def));
            }
        }

        for (PNode arg : args) {
            paramNames.add(arg.getText());

            if (arg instanceof TupleLiteralNode) {
                targets = new ArrayList<>();
                targets.add(arg);
                tupleAssign = ass.makeAssign(arg, targets, var.makeName(arg, arg.getText(), ContextType.Load));
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
        return (ParametersNode) GrammarUtil.make(t, retVal);
    }

    private PNode cantBeNoneName(Token t) {
        if (t == null || t.getText().equals("None")) {
            ErrorHandler.error("can't be None", t);
        }
        return var.makeName(t, t.getText(), ContextType.Load);
    }

    public ParametersNode makeArgumentsType(Token t, List<?> params, Token snameToken, Token knameToken, List<?> defaults) {
        List<PNode> p = GrammarUtil.castExprs(params);
        List<PNode> d = GrammarUtil.castExprs(defaults);
        PNode s = (snameToken == null) ? null : cantBeNoneName(snameToken);
        PNode k = (knameToken == null) ? null : cantBeNoneName(knameToken);
        return makeArguments(t, p, s, k, d);
    }

    public List<PNode> extractArgs(List<?> args) {
        return GrammarUtil.castExprs(args);
    }

    public StatementNode makeReturn(Token t, PNode paramValue) {
        StatementNode retVal = null;
        PNode write = null;
        PNode value = var.fixGlobalReadToLocal(paramValue);

        if (value == null) {
            retVal = factory.createReturn();
        } else if (TranslationOptions.RETURN_VALUE_IN_FRAME) {
            write = factory.createWriteLocal(value, ParserEnvironment.getReturnSlot());
            retVal = factory.createFrameReturn(write);
        } else {
            retVal = factory.createExplicitReturn(value);
        }
        return (StatementNode) GrammarUtil.make(t, retVal);
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

        return (StatementNode) GrammarUtil.make(t, retVal);
    }
}
