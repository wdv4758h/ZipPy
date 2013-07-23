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
package org.python.ast.datatypes;

import org.python.core.*;

public class PSlice {

    private int start;
    private int stop;
    private int step;

    public PSlice(int start, int stop) {
        this(start, stop, 1);
    }

    public PSlice(int start, int stop, int step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int computeActualIndices(int len) {
        int length;

        if (step == 0) {
            throw Py.ValueError("slice step cannot be zero");
        }

        if (start == Integer.MIN_VALUE) {
            start = step < 0 ? len - 1 : 0;
        } else {
            if (start < 0) {
                start += len;
            }
            if (start < 0) {
                start = step < 0 ? -1 : 0;
            }
            if (start >= len) {
                start = step < 0 ? len - 1 : len;
            }
        }

        if (stop == Integer.MIN_VALUE) {
            stop = step < 0 ? -1 : len;
        } else {
            if (stop < 0) {
                stop += len;
            }
            if (stop < 0) {
                stop = -1;
            }
            if (stop > len) {
                stop = len;
            }
        }

        if (step > 0 && stop < start) {
            stop = start;
        }

        if (step > 0) {
            length = (int) ((stop - start + step - 1) / step);
        } else {
            length = (int) ((stop - start + step + 1) / step);
        }

        if (length < 0) {
            length = 0;
        }

        return length;
    }

}
