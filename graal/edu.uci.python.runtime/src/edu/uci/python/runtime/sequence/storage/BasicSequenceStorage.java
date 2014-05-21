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

public abstract class BasicSequenceStorage extends SequenceStorage {

    // nominated storage length
    int length;

    // physical storage length
    int capacity;

    @Override
    public final int length() {
        return length;
    }

    /**
     * The capacity we should allocate for a given length.
     */
    public static int capacityFor(int length) {
        return Math.max(16, length * 2);
    }

    /**
     * Ensure that the current capacity is big enough. If not, we increase capacity to the next
     * designated size (not necessarily the requested one).
     */
    protected void ensureCapacity(int newCapacity) {
        if (newCapacity > capacity) {
            increaseCapacityExactWithCopy(capacityFor(newCapacity));
        }
    }

    protected abstract void increaseCapacityExactWithCopy(int newCapacity);

    protected abstract void increaseCapacityExact(int newCapacity);

    protected void minimizeCapacity() {
        capacity = length;
    }

    @Override
    public int index(Object value) {
        for (int i = 0; i < length; i++) {
            if (getItemInBound(i).equals(value)) {
                return i;
            }
        }

        return -1;
    }
}
