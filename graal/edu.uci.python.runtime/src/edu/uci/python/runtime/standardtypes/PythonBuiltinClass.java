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

import java.lang.reflect.*;
import java.util.*;

import edu.uci.python.runtime.*;
import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.runtime.modules.annotations.*;

public class PythonBuiltinClass extends PythonClass {

    private final List<AnnotatedBuiltinMethod> builtinMethods = new ArrayList<>();
    private final List<AnnotatedBuiltinConstant> builtinConstants = new ArrayList<>();

    public PythonBuiltinClass(PythonContext context, PythonClass superClass, String name) {
        super(context, superClass, name);
    }

    private void findBuiltinMethodsAndConstantsByReflection(Class definingClass) {
        for (Field field : definingClass.getDeclaredFields()) {
            if (field.getAnnotation(BuiltinConstant.class) != null) {
                builtinConstants.add(PythonModule.buildConstant(field));
            } else if (field.getAnnotation(BuiltinMethod.class) != null) {
                builtinMethods.add(PythonModule.buildMethod(field));
            }
        }
    }

    public void addBuiltinMethodsAndConstants(Class definingClass) {
        findBuiltinMethodsAndConstantsByReflection(definingClass);

        for (AnnotatedBuiltinConstant constant : builtinConstants) {
            Object value = constant.getValue();
            setAttribute(constant.getName(), value);
        }

        for (AnnotatedBuiltinMethod method : builtinMethods) {
            final PBuiltinFunction function = new PBuiltinFunction(method.getNames().get(0), method.getCallTarget());
            String methodName = method.getNames().get(0);
            setAttribute(methodName, function);
        }
    }
}
