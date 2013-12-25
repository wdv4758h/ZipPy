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
package edu.uci.python.runtime.datatype;

import org.python.core.*;

import com.oracle.truffle.api.CompilerDirectives.SlowPath;

import edu.uci.python.runtime.iterator.*;
import edu.uci.python.runtime.sequence.*;
import edu.uci.python.runtime.sequence.storage.*;

public class PRange extends PImmutableSequence {

    private final int start;
    private final int stop;
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
            rangeIsZeroError();
        }

        int n;
        if (step > 0) {
            n = getLenOfRange(low, hi, step);
        } else {
            n = getLenOfRange(hi, low, -step);
        }

        this.start = low;
        this.stop = hi;
        this.step = step;
        this.length = n;
    }

    @SlowPath
    private static void rangeIsZeroError() {
        throw Py.ValueError("range() arg 3 must not be zero");
    }

    public static int getLenOfRange(int lo, int hi, int step) {
        int n = 0;
        if (lo < hi) {
            // the base difference may be > Integer.MAX_VALUE
            long diff = (long) hi - (long) lo - 1;
            // any long > Integer.MAX_VALUE or < Integer.MIN_VALUE gets casted
            // to a
            // negative number
            n = (int) ((diff / step) + 1);
            if (n < 0) {
                overFlowError();
            }
        }
        return n;
    }

    @SlowPath
    private static void overFlowError() {
        throw Py.OverflowError("range() result has too many items");
    }

    public int getStart() {
        return start;
    }

    public int getStep() {
        return step;
    }

    public int getStop() {
        return stop;
    }

    @Override
    public PRangeIterator __iter__() {
        return new PRangeIterator(this);
    }

    @Override
    public Object getItem(int idx) {
        int index = SequenceUtil.normalizeIndex(idx, length);

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
    public boolean lessThan(PSequence sequence) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getMax() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getMin() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int len() {
        return length;
    }

    @Override
    public SequenceStorage getStorage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int index(Object value) {
        throw new UnsupportedOperationException();
    }
}
