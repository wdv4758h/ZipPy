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
package edu.uci.python.nodes.attribute;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.truffle.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.objects.*;

public abstract class UnboxedAttributeCacheNode extends AbstractUnboxedAttributeNode {

    @Child protected UnboxedCheckNode primaryCheck;
    private final PythonBasicObject cachedStorage;

    public UnboxedAttributeCacheNode(PythonContext context, String attributeId, UnboxedCheckNode checkNode, PythonBasicObject storage) {
        super(context, attributeId);
        this.primaryCheck = adoptChild(checkNode);
        this.cachedStorage = storage;
    }

    public static AbstractUnboxedAttributeNode createUninitialized(PythonContext context, String attributeId) {
        return new AbstractUnboxedAttributeNode.UninitializedCachedAttributeNode(context, attributeId);
    }

    public static UnboxedAttributeCacheNode create(PythonContext context, String attributeId, UnboxedCheckNode checkNode, PythonBasicObject storage, StorageLocation location) {
        if (location instanceof IntStorageLocation) {
            return new UnboxedAttributeCacheNode.CachedIntAttributeNode(context, attributeId, checkNode, storage, (IntStorageLocation) location);
        } else if (location instanceof FloatStorageLocation) {
            return new UnboxedAttributeCacheNode.CachedDoubleAttributeNode(context, attributeId, checkNode, storage, (FloatStorageLocation) location);
        } else {
            return new UnboxedAttributeCacheNode.CachedObjectAttributeNode(context, attributeId, checkNode, storage, (ObjectStorageLocation) location);
        }
    }

    @Override
    public Object getValue(VirtualFrame frame, Object primaryObj) {
        if (primaryCheck.accept(frame, primaryObj)) {
            return getValueUnsafe(frame, cachedStorage);
        } else {
            // TODO: rewrite
            CompilerDirectives.transferToInterpreter();
            return null;
        }
    }

    @Override
    public int getIntValue(VirtualFrame frame, Object primaryObj) throws UnexpectedResultException {
        if (primaryCheck.accept(frame, primaryObj)) {
            return getIntValueUnsafe(frame, cachedStorage);
        } else {
            // TODO: rewrite
            CompilerDirectives.transferToInterpreter();
            return 0;
        }
    }

    public double getDoulbeValue(VirtualFrame frame, Object primaryObj) throws UnexpectedResultException {
        if (primaryCheck.accept(frame, primaryObj)) {
            return getDoubleValueUnsafe(frame, cachedStorage);
        } else {
            // TODO: rewrite
            CompilerDirectives.transferToInterpreter();
            return 0;
        }
    }

    @Override
    public boolean getBooleanValue(VirtualFrame frame, Object primaryObj) throws UnexpectedResultException {
        if (primaryCheck.accept(frame, primaryObj)) {
            return getBooleanValueUnsafe(frame, cachedStorage);
        } else {
            // TODO: rewrite
            CompilerDirectives.transferToInterpreter();
            return false;
        }
    }

    public abstract Object getValueUnsafe(VirtualFrame frame, PythonBasicObject storage);

    public int getIntValueUnsafe(VirtualFrame frame, PythonBasicObject storage) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectInteger(getValueUnsafe(frame, storage));
    }

    public double getDoubleValueUnsafe(VirtualFrame frame, PythonBasicObject storage) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectDouble(getValueUnsafe(frame, storage));
    }

    public boolean getBooleanValueUnsafe(VirtualFrame frame, PythonBasicObject storage) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectBoolean(getValueUnsafe(frame, storage));
    }

    public static class CachedObjectAttributeNode extends UnboxedAttributeCacheNode {

        private final ObjectStorageLocation objLocation;

        public CachedObjectAttributeNode(PythonContext context, String attributeId, UnboxedCheckNode checkNode, PythonBasicObject storage, ObjectStorageLocation objLocation) {
            super(context, attributeId, checkNode, storage);
            this.objLocation = objLocation;
        }

        @Override
        public Object getValueUnsafe(VirtualFrame frame, PythonBasicObject primaryObj) {
            return objLocation.read(primaryObj);
        }
    }

    public static class CachedIntAttributeNode extends UnboxedAttributeCacheNode {

        private final IntStorageLocation intLocation;

        public CachedIntAttributeNode(PythonContext context, String attributeId, UnboxedCheckNode checkNode, PythonBasicObject storage, IntStorageLocation intLocation) {
            super(context, attributeId, checkNode, storage);
            this.intLocation = intLocation;
        }

        @Override
        public Object getValueUnsafe(VirtualFrame frame, PythonBasicObject storage) {
            return intLocation.read(storage);
        }

        @Override
        public int getIntValueUnsafe(VirtualFrame frame, PythonBasicObject storage) throws UnexpectedResultException {
            return intLocation.readInt(storage);
        }
    }

    public static class CachedDoubleAttributeNode extends UnboxedAttributeCacheNode {

        private final FloatStorageLocation floatLocation;

        public CachedDoubleAttributeNode(PythonContext context, String attributeId, UnboxedCheckNode checkNode, PythonBasicObject storage, FloatStorageLocation floatLocation) {
            super(context, attributeId, checkNode, storage);
            this.floatLocation = floatLocation;
        }

        @Override
        public Object getValueUnsafe(VirtualFrame frame, PythonBasicObject storage) {
            return floatLocation.read(storage);
        }

        @Override
        public double getDoubleValueUnsafe(VirtualFrame frame, PythonBasicObject storage) throws UnexpectedResultException {
            return floatLocation.readDouble(storage);
        }
    }

    public static class CachedBooleanAttributeNode extends UnboxedAttributeCacheNode {

        private final IntStorageLocation intLocation;

        public CachedBooleanAttributeNode(PythonContext context, String attributeId, UnboxedCheckNode checkNode, PythonBasicObject storage, IntStorageLocation intLocation) {
            super(context, attributeId, checkNode, storage);
            this.intLocation = intLocation;
        }

        @Override
        public Object getValueUnsafe(VirtualFrame frame, PythonBasicObject storage) {
            try {
                return intLocation.readBoolean(storage);
            } catch (UnexpectedResultException e) {
                return e.getResult();
            }
        }

        @Override
        public boolean getBooleanValueUnsafe(VirtualFrame frame, PythonBasicObject storage) throws UnexpectedResultException {
            return intLocation.readBoolean(storage);
        }
    }
}
