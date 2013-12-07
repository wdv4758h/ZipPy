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
package edu.uci.python.runtime.builtins;

import java.util.*;

import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.runtime.modules.*;

/**
 * Storage for initialized Python built-in modules and types.
 * 
 * @author zwei
 * 
 */
public class PythonBuiltinsLookup {

    private final Map<String, PythonModule> builtinModules;
    private final Map<Class<? extends PythonBuiltinObject>, PythonBuiltinClass> builtinTypes;

    public PythonBuiltinsLookup() {
        builtinModules = new HashMap<>();
        builtinTypes = new HashMap<>();
    }

    public void addModule(String name, PythonModule module) {
        builtinModules.put(name, module);
    }

    public void addType(Class<? extends PythonBuiltinObject> clazz, PythonBuiltinClass type) {
        builtinTypes.put(clazz, type);
    }

    public PythonModule lookupModule(String name) {
        PythonModule module = builtinModules.get(name);
        return module;
    }

    public PythonBuiltinClass lookupType(Class<? extends PythonBuiltinObject> clazz) {
        PythonBuiltinClass type = builtinTypes.get(clazz);
        return type;
    }
}
