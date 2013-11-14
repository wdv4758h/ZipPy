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

import com.oracle.truffle.api.*;

import edu.uci.python.runtime.assumptions.*;
import edu.uci.python.runtime.objects.*;

/**
 * Application level mutable Python object.
 */
public class PythonObject extends PythonBasicObject {

    /**
     * Assumption used by attribute call sites or attribute access sites that access attribute
     * stored in this object.
     * <p>
     * The unmodifiedAssumption is invalidated whenever the object itself is modified. <br>
     * 1. Invalidated by setAttribute slow path here. <br>
     * 2. Invalidated by StoreObjectAttributeNode... (TODO:)
     */
    protected final CyclicAssumption unmodifiedAssumption;

    public PythonObject(PythonClass pythonClass) {
        super(pythonClass);
        unmodifiedAssumption = new CyclicAssumption("unmodified");
    }

    public Assumption getUnmodifiedAssumption() {
        return unmodifiedAssumption.getAssumption();
    }

    @Override
    public void setAttribute(String name, Object value) {
        unmodifiedAssumption.invalidate();
        super.setAttribute(name, value);
    }

    @Override
    public String toString() {
        return "<" + pythonClass.getClassName() + " object at " + hashCode() + ">";
    }
}
