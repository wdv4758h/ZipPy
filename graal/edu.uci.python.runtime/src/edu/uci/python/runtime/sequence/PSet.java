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

import java.util.*;

import edu.uci.python.runtime.iterator.*;

public class PSet extends PBaseSet {

    public PSet() {
        super();
    }

    public PSet(Set<Object> elements) {
        super(elements);
    }

    public PSet(PIterator iterator) {
        super(iterator);
    }

    public PSet(PBaseSet pBaseSet) {
        super(pBaseSet);
    }

    // update

    @Override
    public void update(PBaseSet other) {
        this.updateInternal(other);
    }

    @Override
    public void update(PIterator other) {
        this.updateInternal(other);
    }

    @Override
    protected PBaseSet cloneThisSet() {
        return new PSet(this);
    }

    // TODO

    // add
    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    // remove
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    // discard
    public boolean discard(Object o) {
        throw new UnsupportedOperationException();
    }

    // pop
    public boolean pop() {
        throw new UnsupportedOperationException();
    }

    // clear
    public boolean clear() {
        throw new UnsupportedOperationException();
    }
}
