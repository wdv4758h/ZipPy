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
package edu.uci.python.test.runtime;

import static org.junit.Assert.*;

import org.junit.*;

import edu.uci.python.runtime.sequence.storage.*;

public class SequenceStorageTests {

    private static Object[] getObjectValues() {
        return new Object[]{1, 2, 3, 4, 5, 6};
    }

    @Test
    public void objectsGetAndSet() {
        ObjectSequenceStorage store = new ObjectSequenceStorage(getObjectValues());
        assertEquals(4, store.getItemInBound(3));
        store.setItemInBound(5, 10);
        assertEquals(10, store.getItemInBound(5));
    }

    @Test
    public void objectsGetSlice() {
        ObjectSequenceStorage store = new ObjectSequenceStorage(getObjectValues());
        ObjectSequenceStorage slice = (ObjectSequenceStorage) store.getSliceInBound(1, 4, 1, 3);

        for (int i = 0; i < 3; i++) {
            assertEquals(i + 2, slice.getItemInBound(i));
        }
    }

    @Test
    public void objectsSetSlice() {
        ObjectSequenceStorage store = new ObjectSequenceStorage(getObjectValues());
        ObjectSequenceStorage slice = new ObjectSequenceStorage(new Object[]{42, 42, 42});

        store.setSliceInBound(1, 4, 1, slice);

        for (int i = 1; i < 4; i++) {
            assertEquals(42, store.getItemInBound(i));
        }
    }

    @Test
    public void objectsSetSliceOutOfBound() {
        ObjectSequenceStorage store = new ObjectSequenceStorage(getObjectValues());
        ObjectSequenceStorage slice = new ObjectSequenceStorage(new Object[]{42, 42, 42});

        store.setSliceInBound(5, 8, 1, slice);

        for (int i = 5; i < 8; i++) {
            assertEquals(42, store.getItemInBound(i));
        }
    }

    @Test
    public void objectsDel() {
        ObjectSequenceStorage store = new ObjectSequenceStorage(getObjectValues());
        store.delItemInBound(4);

        for (int i = 0; i < 4; i++) {
            assertEquals(i + 1, store.getItemInBound(i));
        }

        assertEquals(6, store.getItemInBound(4));
        assertEquals(5, store.length());
    }

    @Test
    public void objectsInsert() {
        ObjectSequenceStorage store = new ObjectSequenceStorage(getObjectValues());
        store.insertItem(3, 42);
        assertEquals(42, store.getItemInBound(3));
        assertEquals(6, store.getItemInBound(6));
        assertEquals(7, store.length());
    }

    @Test
    public void objectAppend() {
        ObjectSequenceStorage store = new ObjectSequenceStorage(getObjectValues());
        store.append(42);
        assertEquals(42, store.getItemInBound(6));
        assertEquals(7, store.length());
    }

    @Test
    public void objectExtend() throws SequenceStoreException {
        ObjectSequenceStorage store = new ObjectSequenceStorage(getObjectValues());
        ObjectSequenceStorage other = new ObjectSequenceStorage(getObjectValues());
        store.extend(other);

        for (int i = 6; i < 12; i++) {
            assertEquals(i - 5, store.getItemInBound(i));
        }

        assertEquals(12, store.length());
    }

    /**
     * IntSequenceStorage tests.
     */
    private static int[] getIntValues() {
        return new int[]{1, 2, 3, 4, 5, 6};
    }

    @Test
    public void intGetAndSet() throws SequenceStoreException {
        IntSequenceStorage store = new IntSequenceStorage(getIntValues());
        assertEquals(4, store.getItemInBound(3));
        store.setItemInBound(5, 10);
        assertEquals(10, store.getItemInBound(5));
    }

    @Test
    public void intGetSlice() {
        IntSequenceStorage store = new IntSequenceStorage(getIntValues());
        IntSequenceStorage slice = (IntSequenceStorage) store.getSliceInBound(1, 4, 1, 3);

        for (int i = 0; i < 3; i++) {
            assertEquals(i + 2, slice.getItemInBound(i));
        }
    }

    @Test
    public void intSetSlice() throws SequenceStoreException {
        IntSequenceStorage store = new IntSequenceStorage(getIntValues());
        IntSequenceStorage slice = new IntSequenceStorage(new int[]{42, 42, 42});

        store.setSliceInBound(1, 4, 1, slice);

        for (int i = 1; i < 4; i++) {
            assertEquals(42, store.getItemInBound(i));
        }
    }

    @Test
    public void intSetSliceOutOfBound() throws SequenceStoreException {
        IntSequenceStorage store = new IntSequenceStorage(getIntValues());
        IntSequenceStorage slice = new IntSequenceStorage(new int[]{42, 42, 42});

        store.setSliceInBound(5, 8, 1, slice);

        for (int i = 5; i < 8; i++) {
            assertEquals(42, store.getItemInBound(i));
        }
    }

    @Test
    public void intDel() {
        IntSequenceStorage store = new IntSequenceStorage(getIntValues());
        store.delItemInBound(4);

        for (int i = 0; i < 4; i++) {
            assertEquals(i + 1, store.getItemInBound(i));
        }

        assertEquals(6, store.getItemInBound(4));
        assertEquals(5, store.length());
    }

    @Test
    public void intInsert() throws SequenceStoreException {
        IntSequenceStorage store = new IntSequenceStorage(getIntValues());
        store.insertItem(3, 42);
        assertEquals(42, store.getItemInBound(3));
        assertEquals(6, store.getItemInBound(6));
        assertEquals(7, store.length());
    }

    @Test
    public void intAppend() throws SequenceStoreException {
        IntSequenceStorage store = new IntSequenceStorage(getIntValues());
        store.append(42);
        assertEquals(42, store.getItemInBound(6));
        assertEquals(7, store.length());
    }

    @Test
    public void intExtend() throws SequenceStoreException {
        IntSequenceStorage store = new IntSequenceStorage(getIntValues());
        IntSequenceStorage other = new IntSequenceStorage(getIntValues());
        store.extend(other);

        for (int i = 6; i < 12; i++) {
            assertEquals(i - 5, store.getItemInBound(i));
        }

        assertEquals(12, store.length());
    }

}
