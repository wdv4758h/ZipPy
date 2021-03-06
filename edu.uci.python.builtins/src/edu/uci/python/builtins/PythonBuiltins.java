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

import java.io.*;
import java.util.*;

import com.oracle.truffle.api.*;

import edu.uci.python.profiler.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.builtin.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.argument.*;
import edu.uci.python.nodes.function.*;

import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.source.*;

/**
 * @author Gulfem
 * @author zwei
 */
public abstract class PythonBuiltins {

    private final Map<String, PBuiltinFunction> builtinFunctions = new HashMap<>();
    private final Map<String, PythonBuiltinClass> builtinClasses = new HashMap<>();

    protected abstract List<? extends NodeFactory<? extends PythonBuiltinNode>> getNodeFactories();

    private static int index = 1;

    @SuppressWarnings("unchecked")
    public void initialize(PythonContext context) {
        Source source = null;
        if (PythonOptions.ProfileCalls) {
            try {
                source = Source.fromFileName("graal/edu.uci.python.test/src/tests/builtins.py");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<NodeFactory<PythonBuiltinNode>> factories = (List<NodeFactory<PythonBuiltinNode>>) getNodeFactories();
        assert factories != null : "No factories found. Override getFactories() to resolve this.";

        for (NodeFactory<PythonBuiltinNode> factory : factories) {
            Builtin builtin = factory.getNodeClass().getAnnotation(Builtin.class);
            RootCallTarget callTarget = createBuiltinCallTarget(factory, builtin.name(), createArgumentsList(builtin), context);
            PBuiltinFunction function = new PBuiltinFunction(builtin.name(), createArity(builtin), callTarget);

            if (builtin.isConstructor()) {
                PythonBuiltinClass builtinClass;

                if (builtin.name().equals("object")) {
                    // Special case for object constructor.
                    builtinClass = context.getObjectClass();
                } else {
                    builtinClass = new PythonBuiltinClass(context, builtin.name(), context.getTypeClass());
                }

                builtinClass.setAttributeUnsafe("__init__", function);
                setBuiltinClass(builtin.name(), builtinClass);
            } else {
                setBuiltinFunction(builtin.name(), function);
                if (PythonOptions.ProfileCalls) {
                    PNode body = ((BuiltinFunctionRootNode) callTarget.getRootNode()).getBody();
                    SourceSection sourceSection = source.createSection("builtin-in", index);
                    body.assignSourceSection(sourceSection);

                    PythonWrapperNode wrapperNode = null;
                    wrapperNode = PythonProfilerNodeProber.getInstance().probeAsMethodBody(body, context);

                    body.replace(wrapperNode);
                    index++;
                }
            }
        }
    }

    private static RootCallTarget createBuiltinCallTarget(NodeFactory<PythonBuiltinNode> factory, String name, PNode[] argsKeywords, PythonContext context) {
        PythonBuiltinNode builtinNode = factory.createNode(argsKeywords, context);
        BuiltinFunctionRootNode rootNode = new BuiltinFunctionRootNode(name, builtinNode);
        RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
        return callTarget;
    }

    private static Arity createArity(Builtin builtin) {
        if (builtin.hasFixedNumOfArguments()) {
            return new Arity(builtin.name(), builtin.fixedNumOfArguments(), builtin.fixedNumOfArguments(), builtin.hasFixedNumOfArguments(), builtin.takesKeywordArguments(),
                            builtin.takesVariableArguments(), false, false, Arrays.asList(builtin.keywordNames()));

        } else {
            return new Arity(builtin.name(), builtin.minNumOfArguments(), builtin.maxNumOfArguments(), builtin.hasFixedNumOfArguments(), builtin.takesKeywordArguments(),
                            builtin.takesVariableArguments(), false, false, Arrays.asList(builtin.keywordNames()));
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
            args.add(ReadIndexedArgumentNode.create(i));
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

    private void setBuiltinFunction(String name, PBuiltinFunction function) {
        builtinFunctions.put(name, function);
    }

    private void setBuiltinClass(String name, PythonBuiltinClass clazz) {
        builtinClasses.put(name, clazz);
    }

    protected Map<String, PBuiltinFunction> getBuiltinFunctions() {
        return builtinFunctions;
    }

    protected Map<String, PythonBuiltinClass> getBuiltinClasses() {
        return builtinClasses;
    }

}
