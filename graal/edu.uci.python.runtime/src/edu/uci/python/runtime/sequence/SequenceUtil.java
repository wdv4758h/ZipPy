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
package edu.uci.python.runtime.sequence;

public class SequenceUtil {

    public static final int MISSING_INDEX = Integer.MIN_VALUE;

    public static int normalizeSliceStart(int start, int step, int size) {
        if (start == MISSING_INDEX) {
            return step < 0 ? size - 1 : 0;
        }

        return start;
    }

    public static int normalizeSliceStop(int stop, int step, int size) {
        if (stop == MISSING_INDEX) {
            return step < 0 ? -1 : size;
        }

        return stop;
    }

    /**
     * Make step a long in case adding the start, stop and step together overflows an int.
     */
    public static final int sliceLength(int start, int stop, long step) {
        int ret;
        if (step > 0) {
            ret = (int) ((stop - start + step - 1) / step);
        } else {
            ret = (int) ((stop - start + step + 1) / step);
        }

        if (ret < 0) {
            return 0;
        }

        return ret;
    }

    /*
     * Compare the specified object/length pairs.
     *
     * @return value >= 0 is the index where the sequences differs. -1: reached the end of sequence1
     * without a difference -2: reached the end of both seqeunces without a difference -3: reached
     * the end of sequence2 without a difference
     */
    public static int cmp(PSequence sequence1, PSequence sequence2) {
        int length1 = sequence1.len();
        int length2 = sequence2.len();

        for (int i = 0; i < length1 && i < length2; i++) {
            if (!sequence1.getItem(i).equals(sequence2.getItem(i))) {
                return i;
            }
        }
        if (length1 == length2) {
            return -2;
        }
        return length1 < length2 ? -1 : -3;
    }

    public static int normalizeIndex(int index, int length) {
        if (index < 0) {
            return index + length;
        } else {
            return index;
        }
    }

}
