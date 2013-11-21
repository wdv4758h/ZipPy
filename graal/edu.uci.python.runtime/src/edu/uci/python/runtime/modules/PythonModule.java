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
package edu.uci.python.runtime.modules;

import java.lang.reflect.*;
import java.util.*;

import com.oracle.truffle.api.*;

import edu.uci.python.runtime.*;
import edu.uci.python.runtime.assumptions.*;
import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.modules.annotations.*;
import edu.uci.python.runtime.objects.*;

public class PythonModule extends PythonBasicObject {

    public static final String __NAME__ = "__name__";

    private final List<AnnotatedBuiltinMethod> builtinMethods = new ArrayList<>();
    private final List<AnnotatedBuiltinConstant> builtinConstants = new ArrayList<>();

    private final CyclicAssumption unmodifiedAssumption;

    @SuppressWarnings("unused") private final PythonContext context;

    public PythonModule(PythonContext context, String name) {
        super(context.getModuleClass());
        this.context = context;
        unmodifiedAssumption = new CyclicAssumption("unmodified");
        setAttribute(__NAME__, name);
        addBuiltinMethodsAndConstants(PythonModule.class);
    }

    @Override
    public Assumption getUnmodifiedAssumption() {
        return unmodifiedAssumption.getAssumption();
    }

    private void addBuiltinMethodsAndConstants(Class definingClass) {
        findBuiltinMethodsAndConstantsByReflection(definingClass);

        for (AnnotatedBuiltinConstant constant : builtinConstants) {
            Object value = constant.getValue();
            if (getAttribute(constant.getName()) == PNone.NONE) {
                setAttribute(constant.getName(), value);
            }
        }

        for (AnnotatedBuiltinMethod method : builtinMethods) {
            final PBuiltinFunction function = new PBuiltinFunction(method.getNames().get(0), method.getCallTarget());
            String methodName = method.getNames().get(0);
            if (getAttribute(methodName) == PNone.NONE) {
                setAttribute(methodName, function);
            }
        }
    }

    private void findBuiltinMethodsAndConstantsByReflection(Class definingClass) {
        for (Field field : definingClass.getDeclaredFields()) {
            if (field.getAnnotation(BuiltinConstant.class) != null) {
                builtinConstants.add(buildConstant(field));
            } else if (field.getAnnotation(BuiltinMethod.class) != null) {
                builtinMethods.add(buildMethod(field));
            }
        }
    }

    public static AnnotatedBuiltinConstant buildConstant(Field field) {
        assert Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers());
        assert field.getAnnotation(BuiltinConstant.class) != null;

        try {
            return new AnnotatedBuiltinConstant(field.getName(), field.get(null));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static AnnotatedBuiltinMethod buildMethod(Field field) {
        assert Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers());
        final BuiltinMethod methodAnnotation = field.getAnnotation(BuiltinMethod.class);

        final List<String> names = new ArrayList<>();
        if (!methodAnnotation.unmangledName().equals("")) {
            names.add(methodAnnotation.unmangledName());
        } else {
            names.add(field.getName());
        }
        names.addAll(Arrays.asList(methodAnnotation.aliases()));

        try {
            return new AnnotatedBuiltinMethod(names, methodAnnotation.isClassMethod(), (PythonCallTarget) field.get(null));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setAttribute(String name, Object value) {
        assert value != null;
        unmodifiedAssumption.invalidate();
        super.setAttribute(name, value);
    }

    @Override
    public String toString() {
        return "<module '" + this.getAttribute(__NAME__) + "'>";

    }

    /**
     * The default constant values of Python modules.
     */
    @BuiltinConstant public static final Object __name__ = PNone.NONE;

    @BuiltinConstant public static final Object __doc__ = PNone.NONE;

    @BuiltinConstant public static final Object __package__ = PNone.NONE;
}
