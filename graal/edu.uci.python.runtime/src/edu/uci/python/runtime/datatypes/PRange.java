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
package edu.uci.python.runtime.datatypes;

import java.util.*;

import org.python.core.*;

import com.oracle.truffle.api.CompilerDirectives.SlowPath;

public class PRange extends PImmutableSequence {

    private final int start;
    @SuppressWarnings("unused") private final int stop;
    private final int step;
    private final int length;

    public PRange(int hi) {
        this(0, hi, 1);
    }

    public PRange(int low, int hi) {
        this(low, hi, 1);
    }

    public PRange(int low, int hi, int step) {
        if (step == 0) {
            throw Py.ValueError("xrange() arg 3 must not be zero");
        }

        int n;
        if (step > 0) {
            n = getLenOfRange(low, hi, step);
        } else {
            n = getLenOfRange(hi, low, -step);
        }
        if (n < 0) {
            throw Py.OverflowError("xrange() result has too many items");
        }

        this.start = low;
        this.stop = hi;
        this.step = step;
        this.length = n;
    }

    static int getLenOfRange(int lo, int hi, int step) {
        int n = 0;
        if (lo < hi) {
            // the base difference may be > Integer.MAX_VALUE
            long diff = (long) hi - (long) lo - 1;
            // any long > Integer.MAX_VALUE or < Integer.MIN_VALUE gets casted
            // to a
            // negative number
            n = (int) ((diff / step) + 1);
        }
        return n;
    }

    @Override
    public int len() {
        return length;
    }

    @Override
    public Object getItem(int idx) {
        int index = idx;
        if (index < 0) {
            index = -idx;
            index = length - index;
        }

        if (index > length - 1) {
            getItemIndexOutOfBound();
        }

        return index * step + start;
    }

    @SlowPath
    private static void getItemIndexOutOfBound() {
        throw Py.IndexError("range object index out of range");
    }

    @SuppressWarnings("hiding")
    @Override
    public Object getSlice(int start, int stop, int step, int length) {
        return PNone.NONE;
    }

    @Override
    public Object getSlice(PSlice slice) {
        return PNone.NONE;
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {

            private int index = 0;

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public boolean hasNext() {
                return index < length - 1;
            }

            public Object next() {
                return index++ * step + start;
            }
        };
    }

    @Override
    public Object[] getSequence() {
        return null;
    }

    @Override
    public boolean lessThan(PSequence sequence) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getMin() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getMax() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object multiply(int value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PCallable findAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }

}
