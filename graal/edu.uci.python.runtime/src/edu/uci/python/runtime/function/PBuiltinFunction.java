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
package edu.uci.python.runtime.function;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.impl.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.runtime.standardtypes.*;

public class PBuiltinFunction extends PythonBuiltinObject implements PythonCallable {

    private final String name;
    private final CallTarget callTarget;
    private final Arity arity;

    public PBuiltinFunction(String name, Arity arity, CallTarget callTarget) {
        this.name = name;
        this.arity = arity;
        this.callTarget = callTarget;
    }

    public PBuiltinFunction(String name, CallTarget callTarget) {
        this.name = name;
        this.callTarget = callTarget;
        this.arity = null;
    }

    public static PBuiltinFunction duplicate(PBuiltinFunction function, CallTarget newCallTarget) {
        return new PBuiltinFunction(function.name, function.arity, newCallTarget);
    }

    public RootNode getFunctionRootNode() {
        DefaultCallTarget defaultTarget = (DefaultCallTarget) callTarget;
        return defaultTarget.getRootNode();
    }

    @Override
    public Object call(PackedFrame caller, Object[] args) {
        return callTarget.call(caller, new PArguments(PNone.NONE, null, args));
    }

    @Override
    public Object call(PackedFrame caller, Object[] args, PKeyword[] keywords) {
        assert keywords != null && keywords.length > 0;
        return callTarget.call(caller, new PArguments(PNone.NONE, null, null, args, keywords));
    }

    @Override
    public void arityCheck(int numOfArgs, int numOfKeywords, String[] keywords) {
        arity.arityCheck(numOfArgs, numOfKeywords, keywords);
    }

    public CallTarget getCallTarget() {
        return callTarget;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "<built-in function " + name + ">";
    }

}
