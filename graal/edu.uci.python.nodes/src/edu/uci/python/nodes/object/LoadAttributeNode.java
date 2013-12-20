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
package edu.uci.python.nodes.object;

import org.python.core.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.access.*;
import edu.uci.python.runtime.objects.*;
import edu.uci.python.runtime.standardtypes.*;

public abstract class LoadAttributeNode extends PNode implements ReadNode {

    protected final String attributeId;

    @Child protected PNode primary;

    public LoadAttributeNode(String name, PNode primary) {
        this.attributeId = name;
        this.primary = adoptChild(primary);
    }

    public String getAttributeId() {
        return attributeId;
    }

    public PNode getPrimary() {
        return primary;
    }

    @Override
    public PNode makeWriteNode(PNode rhs) {
        return new UninitializedStoreAttributeNode(this.attributeId, this.primary, rhs);
    }

    public LoadAttributeNode specialize(Object primaryObj) {
        if (primaryObj instanceof PyObject) {
            return new LoadGenericAttributeNode.LoadPyObjectAttributeNode(this);
        }

        if (primaryObj instanceof PythonBuiltinObject) {
            return new LoadGenericAttributeNode.LoadPObjectAttributeNode(this);
        }

        final PythonBasicObject pythonBasicObj = (PythonBasicObject) primaryObj;
        final StorageLocation storageLocation = pythonBasicObj.getObjectLayout().findStorageLocation(attributeId);

        if (storageLocation == null) {
            return this;
        }

        if (storageLocation instanceof IntStorageLocation) {
            return new LoadIntAttributeNode(attributeId, primary, storageLocation.getObjectLayout(), (IntStorageLocation) storageLocation);
        } else if (storageLocation instanceof FloatStorageLocation) {
            return new LoadFloatAttributeNode(attributeId, primary, storageLocation.getObjectLayout(), (FloatStorageLocation) storageLocation);
        } else {
            return new LoadObjectAttributeNode(attributeId, primary, storageLocation.getObjectLayout(), (ObjectStorageLocation) storageLocation);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " ( " + primary + ", " + attributeId + ")";
    }

}
