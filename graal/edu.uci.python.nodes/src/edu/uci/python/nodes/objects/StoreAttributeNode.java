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
package edu.uci.python.nodes.objects;

import org.python.core.PyObject;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.access.*;
import edu.uci.python.nodes.statements.*;
import edu.uci.python.runtime.objects.*;

public abstract class StoreAttributeNode extends StatementNode implements WriteNode {

    protected final String attributeId;
    @Child protected PNode primary;
    @Child protected PNode rhs;

    public StoreAttributeNode(String name, PNode primary, PNode rhs) {
        this.attributeId = name;
        this.primary = adoptChild(primary);
        this.rhs = adoptChild(rhs);
    }

    public String getAttributeId() {
        return attributeId;
    }

    public PNode getPrimary() {
        return primary;
    }

    @Override
    public PNode getRhs() {
        return rhs;
    }

    @Override
    public PNode makeReadNode() {
        return new UninitializedLoadAttributeNode(attributeId, primary);
    }

    public StoreAttributeNode specialize(Object primaryObj) {
        if (primaryObj instanceof PyObject) {
            return new StoreGenericAttributeNode.StorePyObjectAttributeNode(this);
        }

        final PythonBasicObject pythonBasicObj = (PythonBasicObject) primaryObj;
        final StorageLocation storageLocation = pythonBasicObj.getObjectLayout().findStorageLocation(attributeId);

        if (storageLocation == null) {
            throw new RuntimeException("Storage location should be found at this point");
        }

        if (storageLocation instanceof IntStorageLocation) {
            return new StoreIntAttributeNode(attributeId, primary, rhs, storageLocation.getObjectLayout(), (IntStorageLocation) storageLocation);
        } else if (storageLocation instanceof FloatStorageLocation) {
            return new StoreFloatAttributeNode(attributeId, primary, rhs, storageLocation.getObjectLayout(), (FloatStorageLocation) storageLocation);
        } else {
            return new StoreObjectAttributeNode(attributeId, primary, rhs, storageLocation.getObjectLayout(), (ObjectStorageLocation) storageLocation);
        }
    }

}
