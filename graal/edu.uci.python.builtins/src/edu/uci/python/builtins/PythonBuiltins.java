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
package edu.uci.python.builtins;

import java.util.*;

import com.oracle.truffle.api.*;

import edu.uci.python.runtime.*;
import edu.uci.python.runtime.builtins.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.argument.*;
import edu.uci.python.nodes.function.*;

/**
 * @author Gulfem
 */
public abstract class PythonBuiltins extends PythonBuiltinsContainer {

    protected abstract List<? extends com.oracle.truffle.api.dsl.NodeFactory<? extends PythonBuiltinNode>> getNodeFactories();

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(PythonContext context) {

        List<com.oracle.truffle.api.dsl.NodeFactory<PythonBuiltinNode>> factories = (List<com.oracle.truffle.api.dsl.NodeFactory<PythonBuiltinNode>>) getNodeFactories();

        if (factories == null) {
            throw new IllegalArgumentException("No factories found. Override getFactories() to resolve this.");
        }

        for (com.oracle.truffle.api.dsl.NodeFactory<PythonBuiltinNode> factory : factories) {
            Builtin builtin = factory.getNodeClass().getAnnotation(Builtin.class);
            PNode[] argsKeywords = createArgumentsList(builtin);

            PythonBuiltinNode builtinNode;

            if (builtin.requiresContext()) {
                builtinNode = factory.createNode(builtin.name(), context, argsKeywords);
            } else {
                builtinNode = factory.createNode(builtin.name(), argsKeywords);
            }

            BuiltinFunctionRootNode rootNode = new BuiltinFunctionRootNode(builtinNode);
            CallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);

            if (builtin.isClass()) {
                Arity arity;

                if (builtin.hasFixedNumOfArguments()) {
                    arity = new Arity(builtin.name(), builtin.fixedNumOfArguments(), builtin.fixedNumOfArguments(), builtin.hasFixedNumOfArguments(), builtin.takesKeywordArguments(),
                                    builtin.takesVariableArguments(), builtin.keywordNames());

                } else {
                    arity = new Arity(builtin.name(), builtin.minNumOfArguments(), builtin.maxNumOfArguments(), builtin.hasFixedNumOfArguments(), builtin.takesKeywordArguments(),
                                    builtin.takesVariableArguments(), builtin.keywordNames());
                }

                PythonBuiltinClass builtinClass;
                builtinClass = new PythonBuiltinClass(context, context.getTypeClass(), builtin.name(), arity, callTarget);
                setBuiltinClass(builtin.name(), builtinClass);
            } else {
                Arity arity;
                PBuiltinFunction function;

                if (builtin.hasFixedNumOfArguments()) {
                    arity = new Arity(builtin.name(), builtin.fixedNumOfArguments(), builtin.fixedNumOfArguments(), builtin.hasFixedNumOfArguments(), builtin.takesKeywordArguments(),
                                    builtin.takesVariableArguments(), builtin.keywordNames());

                    function = new PBuiltinFunction(builtin.name(), arity, callTarget);
                } else {
                    arity = new Arity(builtin.name(), builtin.minNumOfArguments(), builtin.maxNumOfArguments(), builtin.hasFixedNumOfArguments(), builtin.takesKeywordArguments(),
                                    builtin.takesVariableArguments(), builtin.keywordNames());
                    function = new PBuiltinFunction(builtin.name(), arity, callTarget);
                }

                setBuiltinFunction(builtin.name(), function);
            }

        }
    }

    private static PNode[] createArgumentsList(Builtin builtin) {
        ArrayList<PNode> args = new ArrayList<>();
        int totalNumOfArgsKeywords = 0;

        if (builtin.hasFixedNumOfArguments()) {
            totalNumOfArgsKeywords = builtin.fixedNumOfArguments();
        } else if (builtin.takesVariableArguments()) {
            totalNumOfArgsKeywords = builtin.minNumOfArguments();
        } else {
            totalNumOfArgsKeywords = builtin.maxNumOfArguments();
        }

        for (int i = 0; i < totalNumOfArgsKeywords; i++) {
            args.add(new ReadArgumentNode(i));
        }

        if (builtin.takesVariableArguments()) {
            args.add(new ReadVarArgsNode(args.size()));
        }

        if (builtin.takesKeywordArguments()) {
            if (builtin.takesVariableKeywords()) {
                args.add(new ReadVarKeywordsNode(builtin.keywordNames()));
            } else {
                args.add(new ReadKeywordNode(builtin.keywordNames()[0]));
            }
        }

        PNode[] argsKeywords = args.toArray(new PNode[args.size()]);
        return argsKeywords;
    }
}
