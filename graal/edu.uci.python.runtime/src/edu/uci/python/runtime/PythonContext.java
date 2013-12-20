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
package edu.uci.python.runtime;

import java.io.*;

import com.oracle.truffle.api.nodes.*;

import edu.uci.python.runtime.builtin.*;
import edu.uci.python.runtime.sequence.*;
import edu.uci.python.runtime.standardtype.*;

public class PythonContext {

    private final PythonOptions options;
    private final PythonBuiltinsLookup lookup;
    private PythonBuiltinClass typeClass;
    private PythonBuiltinClass objectClass;
    private PythonBuiltinClass moduleClass;
    private PythonModule builtinsModule;
    private PythonModule mainModule;

    private static PythonContext currentContext;

    public PythonContext(PythonOptions opts, PythonBuiltinsLookup lookup) {
        this.options = opts;
        this.lookup = lookup;
        currentContext = this;
        initialize();
        this.lookup.addBuiltins(this);
    }

    public void initialize() {
        typeClass = new PythonBuiltinClass(this, null, "type");
        objectClass = new PythonObjectClass(this);
        typeClass.unsafeSetSuperClass(objectClass);
        moduleClass = new PythonBuiltinClass(this, objectClass, "module");

        builtinsModule = new PythonModule("__builtins__", this);
        builtinsModule.setAttribute("object", objectClass);

        mainModule = new PythonModule("__main__", this);
        mainModule.setAttribute("__builtins__", builtinsModule);
    }

    public PythonModule getBuiltinsModule() {
        return builtinsModule;
    }

    public PythonModule getMainModule() {
        return mainModule;
    }

    public PythonOptions getPythonOptions() {
        return options;
    }

    public PythonBuiltinsLookup getPythonBuiltinsLookup() {
        return lookup;
    }

    public static PythonBuiltinClass getBuiltinTypeFor(Class<? extends PythonBuiltinObject> javaClass) {
        return currentContext.lookup.lookupType(javaClass);
    }

    public PrintStream getStandardOut() {
        return options.getStandardOut();
    }

    public boolean getUseUnsafe() {
        return PythonOptions.UseUnsafe;
    }

    public PythonBuiltinClass getTypeClass() {
        return typeClass;
    }

    public PythonBuiltinObject boxAsPythonBuiltinObject(Object obj) throws UnexpectedResultException {
        if (obj instanceof PythonBuiltinObject) {
            return (PythonBuiltinObject) obj;
        }

        /**
         * TODO: missing int, double, boolean... and maybe more.
         */
        if (obj instanceof String) {
            return new PString((String) obj);
        }

        throw new UnexpectedResultException(obj);
    }

    public PythonBuiltinClass getObjectClass() {
        return objectClass;
    }

    public PythonClass getModuleClass() {
        return moduleClass;
    }
}
