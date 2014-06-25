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
package edu.uci.python.runtime.standardtype;

import org.python.core.*;

/**
 * See {@link PyType}.
 *
 * @author zwei
 */
class MROMergeState {

    /** The mro of the base type we're representing. */
    public PythonClass[] mro;

    /**
     * The index of the next item to be merged from mro, or mro.length if this base has been
     * completely merged.
     */
    public int next;

    public boolean isMerged() {
        return mro.length == next;
    }

    public PythonClass getCandidate() {
        return mro[next];
    }

    /**
     * Marks candidate as merged for this base if it's the next item to be merged.
     */
    public void noteMerged(PythonClass candidate) {
        if (!isMerged() && getCandidate() == candidate) {
            next++;
        }
    }

    /**
     * Returns true if candidate is in the items past this state's next item to be merged.
     */
    public boolean pastnextContains(PythonClass candidate) {
        for (int i = next + 1; i < mro.length; i++) {
            if (mro[i] == candidate) {
                return true;
            }
        }
        return false;
    }

}
