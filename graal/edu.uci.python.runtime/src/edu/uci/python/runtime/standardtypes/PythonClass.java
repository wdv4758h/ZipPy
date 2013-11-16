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

import java.util.*;

import org.python.core.*;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

import edu.uci.python.runtime.*;
import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.objects.*;

/**
 * Mutable class.
 */
public class PythonClass extends PythonObject {

    private final String className;

    private final PythonContext context;

    // TODO: Multiple inheritance and MRO...
    @CompilationFinal private PythonClass superClass;

    private final Set<PythonClass> subClasses = Collections.newSetFromMap(new WeakHashMap<PythonClass, Boolean>());

    public PythonClass(PythonClass superClass, String name) {
        this(superClass.getContext(), superClass, name);
    }

    /**
     * This constructor supports initialization and solves boot-order problems and should not
     * normally be used from outside this class.
     */
    public PythonClass(PythonContext context, PythonClass superClass, String name) {
        super(context.getPythonCore().getTypeClass());
        this.context = context;
        this.className = name;

        if (superClass == null) {
            this.superClass = context.getPythonCore().getObjectClass();
        } else {
            unsafeSetSuperClass(superClass);
        }
    }

    public PythonClass getSuperClass() {
        assert superClass != null;
        return superClass;
    }

    public String getClassName() {
        return className;
    }

    public PythonContext getContext() {
        return context;
    }

    public PythonCallable lookUpMethod(String methodName) {
        Object attr = getAttribute(methodName);
        assert attr != null;

        if (attr instanceof PythonCallable) {
            return (PythonCallable) attr;
        }

        throw Py.TypeError(attr + " object is not callable");
    }

    public void addMethod(PFunction method) {
        setAttribute(method.getName(), method);
    }

    @Override
    public Object getAttribute(String name) {
        // Find the storage location
        final StorageLocation storageLocation = getObjectLayout().findStorageLocation(name);

        // Continue the look up in PythonType.
        if (storageLocation == null) {
            return superClass == null ? PNone.NONE : superClass.getAttribute(name);
        }

        return storageLocation.read(this);
    }

    /**
     * Invalidate the unmodified assumption of the PythonClass itself and propagate the invalidation
     * to its subclasses too.
     */
    @Override
    public void setAttribute(String name, Object value) {
        unmodifiedAssumption.invalidate();

        for (PythonClass subClass : subClasses) {
            subClass.unmodifiedAssumption.invalidate();
        }

        super.setAttribute(name, value);
    }

    /**
     * This method supports initialization and solves boot-order problems and should not normally be
     * used.
     */
    public void unsafeSetSuperClass(PythonClass newSuperClass) {
        assert superClass == null;
        superClass = newSuperClass;
        superClass.subClasses.add(this);
    }

    @Override
    public String toString() {
        return "<class \'" + className + "\'>";
    }

}
