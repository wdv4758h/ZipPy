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
package org.python.ast.nodes.expressions;

import org.python.ast.datatypes.*;
import org.python.ast.nodes.PNode;

import com.oracle.truffle.api.dsl.Generic;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class SubscriptLoadNode extends BinaryOpNode {

    public PNode getPrimary() {
        return getLeftNode();
    }

    public PNode getSlice() {
        return getRightNode();
    }

    @Specialization(order = 0)
    public String doString(String primary, PSlice slice) {
        int length = slice.computeActualIndices(primary.length());
        int start = slice.getStart();
        int stop = slice.getStop();
        int step = slice.getStep();

        if (step > 0 && stop < start) {
            stop = start;
        }
        if (step == 1) {
            return primary.substring(start, stop);
        } else {
            char[] newChars = new char[length];
            int j = 0;
            for (int i = start; j < length; i += step) {
                newChars[j++] = primary.charAt(i);
            }

            return new String(newChars);
        }
    }

    @Specialization(order = 1)
    public String doString(String primary, int slice) {
        if (slice < 0) {
            slice += primary.length();
        }
        return String.valueOf(primary.charAt(slice));
    }

    @Specialization(order = 2)
    public Object doPDictionary(PDictionary primary, Object slice) {
        return primary.getItem(slice);
    }

    @Specialization(order = 3)
    public Object doPSequence(PSequence primary, int slice) {
        return primary.getItem(slice);
    }

    @Specialization(order = 4)
    public Object doPSequence(PSequence primary, PSlice slice) {
        return primary.getSlice(slice);
    }

    @Specialization(order = 5)
    public Object doPIntegerArray(PIntegerArray primary, int slice) {
        return primary.getItem(slice);
    }

    @Specialization(order = 6)
    public Object doPIntegerArray(PIntegerArray primary, PSlice slice) {
        return primary.getSlice(slice);
    }

    @Specialization(order = 7)
    public Object doPDoubleArray(PDoubleArray primary, int slice) {
        return primary.getItem(slice);
    }

    @Specialization(order = 8)
    public Object doPDoubleArray(PDoubleArray primary, PSlice slice) {
        return primary.getSlice(slice);
    }

    @Specialization(order = 9)
    public Object doPCharArray(PCharArray primary, int slice) {
        return primary.getItem(slice);
    }

    @Specialization(order = 10)
    public Object doPCharArray(PCharArray primary, PSlice slice) {
        return primary.getSlice(slice);
    }

    @Generic
    public Object doGeneric(Object primary, Object slice) {
        throw new RuntimeException("Unsupported Type!");
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " = " + getLeftNode() + "[" + getRightNode() + "]";
    }

}
