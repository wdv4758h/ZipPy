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
package edu.uci.python.runtime.sequence.storage;

import edu.uci.python.runtime.*;

public abstract class SequenceStorage {

    public abstract int length();

    public abstract SequenceStorage copy();

    public abstract Object[] getInternalArray();

    public abstract Object[] getCopyOfInternalArray();

    public abstract Object getItemInBound(int idx);

    public abstract void setItemInBound(int idx, Object value) throws SequenceStoreException;

    public abstract void insertItem(int idx, Object value) throws SequenceStoreException;

    public abstract SequenceStorage getSliceInBound(int start, int stop, int step, int length);

    public abstract void setSliceInBound(int start, int stop, int step, SequenceStorage sequence) throws SequenceStoreException;

    public abstract void delItemInBound(int idx);

    public abstract Object popInBound(int idx);

    public abstract int index(Object value);

    public abstract void append(Object value) throws SequenceStoreException;

    public abstract void extend(SequenceStorage other) throws SequenceStoreException;

    public abstract void reverse();

    public abstract void sort();

    public abstract SequenceStorage generalizeFor(Object value);

    public abstract Object getIndicativeValue();

    public static SequenceStorage createStorage(Object[] values) {
        if (!PythonOptions.UnboxSequenceStorage) {
            if (values == null) {
                return new ObjectSequenceStorage();
            } else {
                return new ObjectSequenceStorage(values);
            }
        }

        /**
         * Try to use unboxed SequenceStorage.
         */
        if (values == null || values.length == 0) {
            return EmptySequenceStorage.INSTANCE;
        }

        boolean canSpecializeToInt = true;

        for (Object item : values) {
            if (!(item instanceof Integer)) {
                canSpecializeToInt = false;
                break;
            }
        }

        if (canSpecializeToInt) {
            final int[] intVals = new int[values.length];

            for (int i = 0; i < values.length; i++) {
                intVals[i] = (int) values[i];
            }

            return new IntSequenceStorage(intVals);
        } else {
            return new ObjectSequenceStorage(values);
        }
    }
}
