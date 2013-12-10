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
package edu.uci.python.runtime.standardtypes;

import edu.uci.python.runtime.*;
import edu.uci.python.runtime.builtins.*;
import edu.uci.python.runtime.function.*;

/**
 * The base class of all Python built-in data types (int, complex, tuple...). Subclasses of
 * PythonBuiltinObject are immutable. In other words, PythonBuiltinObjects don't have the __dict__
 * attribute. On the other hand, PythonObject models mutable Python data type that has __dict__.
 * <p>
 * Special methods for PythonBuiltinObjects are implemented as Java methods and dispatched at Java
 * level. Any explicit user level access to a PythonBuiltinObject's attributes is considered as slow
 * path and implemented presumably using Java reflection...
 * 
 */
public abstract class PythonBuiltinObject {

    private static final PythonBuiltinClass __class__ = PythonContext.getBuiltinTypeFor(PythonBuiltinObject.class);

    public PythonBuiltinClass __class__() {
        return __class__;
    }

    public int len() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public PythonCallable __getattribute__(String name) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public PythonBuiltinObject __mul__(int value) {
        throw new UnsupportedOperationException();
    }
}
