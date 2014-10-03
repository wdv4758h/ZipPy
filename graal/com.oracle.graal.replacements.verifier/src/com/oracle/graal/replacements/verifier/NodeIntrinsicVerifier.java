/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.graal.replacements.verifier;

import java.lang.annotation.*;
import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;
import javax.tools.Diagnostic.Kind;

import com.oracle.graal.graph.Node.ConstantNodeParameter;
import com.oracle.graal.graph.Node.InjectedNodeParameter;
import com.oracle.graal.graph.Node.NodeIntrinsic;

public final class NodeIntrinsicVerifier extends AbstractVerifier {

    private static final String NODE_CLASS_NAME = "value";

    private TypeMirror nodeType() {
        return env.getElementUtils().getTypeElement("com.oracle.graal.graph.Node").asType();
    }

    private TypeMirror valueNodeType() {
        return env.getElementUtils().getTypeElement("com.oracle.graal.nodes.ValueNode").asType();
    }

    private TypeMirror classType() {
        return env.getElementUtils().getTypeElement("java.lang.Class").asType();
    }

    private TypeMirror resolvedJavaTypeType() {
        return env.getElementUtils().getTypeElement("com.oracle.graal.api.meta.ResolvedJavaType").asType();
    }

    public NodeIntrinsicVerifier(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return NodeIntrinsic.class;
    }

    @Override
    public void verify(Element element, AnnotationMirror annotation) {
        if (element.getKind() != ElementKind.METHOD) {
            assert false : "Element is guaranteed to be a method.";
            return;
        }

        ExecutableElement intrinsicMethod = (ExecutableElement) element;
        if (!intrinsicMethod.getModifiers().contains(Modifier.STATIC)) {
            env.getMessager().printMessage(Kind.ERROR, String.format("A @%s method must be static.", NodeIntrinsic.class.getSimpleName()), element, annotation);
        }

        TypeMirror nodeClassMirror = resolveAnnotationValue(TypeMirror.class, findAnnotationValue(annotation, NODE_CLASS_NAME));
        TypeElement nodeClass = (TypeElement) env.getTypeUtils().asElement(nodeClassMirror);
        if (nodeClass.getSimpleName().contentEquals(NodeIntrinsic.class.getSimpleName())) {
            // default value
            Element enclosingElement = intrinsicMethod.getEnclosingElement();
            while (enclosingElement != null && enclosingElement.getKind() != ElementKind.CLASS) {
                enclosingElement = enclosingElement.getEnclosingElement();
            }
            if (enclosingElement != null) {
                nodeClass = (TypeElement) enclosingElement;
            }
        }

        if (isNodeType(nodeClass)) {
            if (nodeClass.getModifiers().contains(Modifier.ABSTRACT)) {
                env.getMessager().printMessage(Kind.ERROR, String.format("Cannot make @NodeIntrinsic for abstract node class %s.", nodeClass.getSimpleName()), element, annotation);
            } else {
                TypeMirror[] constructorSignature = constructorSignature(intrinsicMethod);
                findFactory(nodeClass, constructorSignature, intrinsicMethod, annotation);
            }
        } else {
            env.getMessager().printMessage(Kind.ERROR, String.format("The class %s is not a Node subclass.", nodeClass.getSimpleName()), element, annotation);
        }
    }

    private boolean isNodeType(TypeElement nodeClass) {
        return env.getTypeUtils().isSubtype(nodeClass.asType(), nodeType());
    }

    private TypeMirror[] constructorSignature(ExecutableElement method) {
        TypeMirror[] parameters = new TypeMirror[method.getParameters().size()];
        for (int i = 0; i < method.getParameters().size(); i++) {
            VariableElement parameter = method.getParameters().get(i);
            if (parameter.getAnnotation(ConstantNodeParameter.class) == null) {
                parameters[i] = valueNodeType();
            } else {
                TypeMirror type = parameter.asType();
                if (isTypeCompatible(type, classType())) {
                    type = resolvedJavaTypeType();
                }
                parameters[i] = type;
            }
        }
        return parameters;
    }

    private void findFactory(TypeElement nodeClass, TypeMirror[] signature, ExecutableElement intrinsicMethod, AnnotationMirror intrinsicAnnotation) {
        List<ExecutableElement> methods = ElementFilter.methodsIn(nodeClass.getEnclosedElements());
        List<String> failureReasons = new ArrayList<>();

        nextMethod: for (ExecutableElement method : methods) {
            if (!method.getSimpleName().contentEquals("create") || !method.getModifiers().contains(Modifier.STATIC)) {
                continue;
            }
            int sIdx = 0;
            int mIdx = 0;
            while (mIdx < method.getParameters().size()) {
                VariableElement parameter = method.getParameters().get(mIdx++);
                if (parameter.getAnnotation(InjectedNodeParameter.class) != null) {
                    // skip injected parameters
                    continue;
                }

                TypeMirror paramType = parameter.asType();
                if (mIdx == method.getParameters().size() && paramType.getKind() == TypeKind.ARRAY) {
                    // last argument of method is varargs, match remaining intrinsic arguments
                    TypeMirror varargsType = ((ArrayType) paramType).getComponentType();
                    while (sIdx < signature.length) {
                        if (!isTypeCompatible(varargsType, signature[sIdx++])) {
                            failureReasons.add(String.format("Factory method %s failed because the types of argument %d are incompatible: %s != %s", method, sIdx, varargsType, signature[sIdx - 1]));
                            continue nextMethod;
                        }
                    }
                } else if (sIdx >= signature.length) {
                    // too many arguments in intrinsic method
                    failureReasons.add(String.format("Too many arguments for %s", method));
                    continue nextMethod;
                } else if (!isTypeCompatible(paramType, signature[sIdx++])) {
                    failureReasons.add(String.format("Factory method %s failed because the types of argument %d are incompatible: %s != %s", method, sIdx, paramType, signature[sIdx - 1]));
                    continue nextMethod;
                }
            }

            if (sIdx == signature.length) {
                // found
                return;
            }

            // too many arguments in constructor
            failureReasons.add(String.format("Not enough arguments for %s", method));
        }

        // not found
        if (failureReasons.isEmpty()) {
            env.getMessager().printMessage(Kind.ERROR, "Could not find matching factory method for node intrinsic.", intrinsicMethod, intrinsicAnnotation);
        } else {
            for (String reason : failureReasons) {
                env.getMessager().printMessage(Kind.ERROR, reason, intrinsicMethod, intrinsicAnnotation);
            }
        }
    }

    private boolean isTypeCompatible(TypeMirror originalType, TypeMirror substitutionType) {
        TypeMirror original = originalType;
        TypeMirror substitution = substitutionType;
        if (needsErasure(original)) {
            original = env.getTypeUtils().erasure(original);
        }
        if (needsErasure(substitution)) {
            substitution = env.getTypeUtils().erasure(substitution);
        }
        return env.getTypeUtils().isSameType(original, substitution);
    }

    private static boolean needsErasure(TypeMirror typeMirror) {
        return typeMirror.getKind() != TypeKind.NONE && typeMirror.getKind() != TypeKind.VOID && !typeMirror.getKind().isPrimitive() && typeMirror.getKind() != TypeKind.OTHER &&
                        typeMirror.getKind() != TypeKind.NULL;
    }
}
