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

import edu.uci.python.runtime.*;
import edu.uci.python.runtime.builtins.*;
import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.modules.*;
import edu.uci.python.runtime.sequence.*;

/**
 * @author Gulfem
 */

public class PythonBuiltinsInitializer {

    // public static void initialize(PythonContext context) {
    public static void initialize(PythonContext context) {
        PythonBuiltinsLookup lookup = context.getPythonBuiltinsLookup();
        lookup.addModule("array", new ArrayModule(context, new ArrayModuleBuiltins(), "array"));
        lookup.addModule("bisect", new BisectModule(context, new BisectModuleBuiltins(), "bisect"));
        lookup.addModule("time", new TimeModule(context, new TimeModuleBuiltins(), "time"));
        BuiltinsClassAttributesContainer.initialize(context, new ListBuiltins(), new StringBuiltins(), new DictionaryBuiltins());

        PythonBuiltinClass typeClass = context.getTypeClass();
        lookup.addType(PList.class, initBuiltinClass(context, typeClass, "list", new ListBuiltins()));
        lookup.addType(PString.class, initBuiltinClass(context, typeClass, "str", new StringBuiltins()));
        lookup.addType(PDictionary.class, initBuiltinClass(context, typeClass, "dict", new DictionaryBuiltins()));
    }

    private static PythonBuiltinClass initBuiltinClass(PythonContext context, PythonBuiltinClass superClass, String name, PythonBuiltins classBuiltin) {
        // classBuiltin.initialize(context);
        classBuiltin.initialize();
        // PythonBuiltinClass clazz = new PythonBuiltinClass(context, superClass, name);
        PythonBuiltinClass clazz = new PythonBuiltinClass(context, superClass, name);
        Map<String, PBuiltinFunction> builtinFunctions = classBuiltin.getBuiltinFunctions();

        for (Map.Entry<String, PBuiltinFunction> entry : builtinFunctions.entrySet()) {
            String methodName = entry.getKey();
            PBuiltinFunction function = entry.getValue();
            clazz.setAttributeUnsafe(methodName, function);
        }

        return clazz;
    }
}
