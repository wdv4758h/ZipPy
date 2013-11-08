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
package edu.uci.python.runtime.datatypes;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;

public class PArguments extends Arguments {

    public static final Object[] EMPTY_ARGUMENTS_ARRAY = new Object[0];
    private final MaterializedFrame declarationFrame;
    private final Object self;
    private final Object[] arguments;
    private final PKeyword[] keywards;

    public PArguments() {
        this.self = null;
        this.declarationFrame = null;
        this.arguments = new Object[0];
        this.keywards = PKeyword.EMPTY_KEYWORDS;
    }

    public PArguments(Object self, Object[] arguments) {
        this.self = self;
        this.declarationFrame = null;
        this.arguments = arguments;
        this.keywards = PKeyword.EMPTY_KEYWORDS;
    }

    public PArguments(Object self, Object[] arguments, PKeyword[] keywards) {
        this.self = self;
        this.declarationFrame = null;
        this.arguments = arguments;
        this.keywards = keywards;
    }

    public static PArguments get(Frame frame) {
        return frame.getArguments(PArguments.class);
    }

    public Object getSelf() {
        return self;
    }

    public final Object[] getArgumentsArray() {
        return CompilerDirectives.unsafeCast(arguments, Object[].class, true);
    }

    public MaterializedFrame getDeclarationFrame() {
        return CompilerDirectives.unsafeFrameCast(declarationFrame);
    }

    public final Object getArgument(int index) {
        if (index >= arguments.length) {
            return PNone.NONE;
        }

        return arguments[index];
    }

    public PKeyword[] getKeywords() {
        return keywards;
    }

    public int getLength() {
        return arguments.length;
    }

}
