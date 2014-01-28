/*
 * Copyright (c) 2014, Regents of the University of California
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
package edu.uci.python.runtime.datatype;

import java.io.*;
import java.util.concurrent.atomic.*;

import edu.uci.python.runtime.exception.*;

public class SingleProducerCircularBuffer {

    private static final PrintStream OUT = System.out;

    private static final int INDEX_MASK = 0B1111;
    private final Object[] buffer;
    private long readCursor;
    private AtomicLong writeCursor;
    private boolean isTerminated;

    public SingleProducerCircularBuffer() {
        this.buffer = new Object[INDEX_MASK + 1];
        this.writeCursor = new AtomicLong();
    }

    public void setAsTerminated() {
        advanceWriteIndex();
        isTerminated = true;

// OUT.println("BUFFER Thread " + Thread.currentThread().getId() + " write terminated at index " +
// getIndex(writeCursor.get()));
    }

    public void put(Object value) {
        buffer[getIndex(writeCursor.get())] = value;

// OUT.println("BUFFER Thread " + Thread.currentThread().getId() + " write " + value + " at index "
// + getIndex(writeCursor.get()));

        advanceWriteIndex();
    }

    private void advanceWriteIndex() {
        while ((writeCursor.get() - readCursor) > INDEX_MASK - 1) {
            // Spin
        }
        // writeCursor.getAndIncrement();
        writeCursor.lazySet(writeCursor.get() + 1);
    }

    private int getIndex(long cursor) {
        int index = (int) (cursor & INDEX_MASK);
        assert index < buffer.length;
        return index;
    }

    public Object take() {
        Object result;

        while (readCursor >= writeCursor.get()) {
            // Spin
        }

        if (isTerminated && readCursor == writeCursor.get() - 1) {
            throw StopIterationException.INSTANCE;
        }

        result = buffer[getIndex(readCursor)];

// OUT.println("BUFFER Thread " + Thread.currentThread().getId() + " read " + result + " at index "
// + getIndex(readCursor));

        advanceReadIndex();
        return result;
    }

    private void advanceReadIndex() {
        readCursor++;
    }

}
