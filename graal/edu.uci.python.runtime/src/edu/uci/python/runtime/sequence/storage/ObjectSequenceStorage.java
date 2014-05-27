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

import java.util.*;

import edu.uci.python.runtime.sequence.*;

public final class ObjectSequenceStorage extends BasicSequenceStorage {

    private Object[] values;

    public ObjectSequenceStorage() {
        values = new Object[]{};
    }

    public ObjectSequenceStorage(Object[] elements) {
        this.values = elements;
        length = elements.length;
        capacity = elements.length;
    }

    @Override
    public Object getItemInBound(int idx) {
        return values[idx];
    }

    @Override
    public void setItemInBound(int idx, Object value) {
        values[idx] = value;
    }

    @Override
    public void insertItem(int idx, Object value) {
        ensureCapacity(length + 1);

        // shifting tail to the right by one slot
        for (int i = values.length - 1; i > idx; i--) {
            values[i] = values[i - 1];
        }

        values[idx] = value;
        length++;
    }

    @Override
    public SequenceStorage getSliceInBound(int start, int stop, int step, int sliceLength) {
        Object[] newArray = new Object[sliceLength];

        if (step == 1) {
            System.arraycopy(values, start, newArray, 0, sliceLength);
            return new ObjectSequenceStorage(newArray);
        }

        for (int i = start, j = 0; j < sliceLength; i += step, j++) {
            newArray[j] = values[i];
        }

        return new ObjectSequenceStorage(newArray);
    }

    @Override
    public void setSliceInBound(int start, int stop, int step, SequenceStorage sequence) {
        int otherLength = sequence.length();

        // range is the whole sequence?
        if (start == 0 && stop == length) {
            values = sequence.getCopyOfInternalArray();
            length = otherLength;
            minimizeCapacity();
            return;
        }

        ensureCapacity(stop);

        for (int i = start, j = 0; i < stop; i += step, j++) {
            values[i] = sequence.getInternalArray()[j];
        }

        length = length > stop ? length : stop;
    }

    @Override
    public void delSlice(int start, int stop) {
        if (stop == SequenceUtil.MISSING_INDEX) {
            length = start;
        }
    }

    @Override
    public void delItemInBound(int idx) {
        popInBound(idx);
    }

    @Override
    public SequenceStorage copy() {
        return new ObjectSequenceStorage(getCopyOfInternalArray());
    }

    @Override
    public Object[] getInternalArray() {
        return values;
    }

    @Override
    public Object[] getCopyOfInternalArray() {
        return Arrays.copyOf(values, length);
    }

    @Override
    public void increaseCapacityExactWithCopy(int newCapacity) {
        values = Arrays.copyOf(values, newCapacity);
        capacity = values.length;
    }

    @Override
    public void increaseCapacityExact(int newCapacity) {
        values = new Object[newCapacity];
        capacity = values.length;
    }

    @Override
    public void append(Object value) throws SequenceStoreException {
        ensureCapacity(length + 1);
        values[length] = value;
        length++;
    }

    @Override
    public void extend(SequenceStorage other) throws SequenceStoreException {
        int extendedLength = length + other.length();
        ensureCapacity(extendedLength);
        Object[] otherValues = other.getInternalArray();

        for (int i = length, j = 0; i < extendedLength; i++, j++) {
            values[i] = otherValues[j];
        }

        length = extendedLength;
    }

    @Override
    public Object popInBound(int idx) {
        Object pop = values[idx];

        for (int i = idx; i < values.length - 1; i++) {
            values[i] = values[i + 1];
        }

        length--;
        return pop;
    }

    public Object popObject() {
        Object pop = values[capacity - 1];
        length--;
        return pop;
    }

    @Override
    public void reverse() {
        int head = 0;
        int tail = length - 1;
        int middle = (length - 1) / 2;

        for (; head <= middle; head++, tail--) {
            Object temp = values[head];
            values[head] = values[tail];
            values[tail] = temp;
        }
    }

    @Override
    public void sort() {
        Object[] copy = getCopyOfInternalArray();
        Arrays.sort(copy);
        values = copy;
        minimizeCapacity();
    }

    @Override
    public ObjectSequenceStorage generalizeFor(Object value) {
        return this;
    }

    @Override
    public Object getIndicativeValue() {
        return null;
    }

    @Override
    public boolean equals(SequenceStorage other) {
        if (other.length() != length()) {
            return false;
        }

        return Arrays.equals(values, other.getInternalArray());
    }

}
