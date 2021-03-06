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

import com.oracle.truffle.api.CompilerDirectives.SlowPath;

import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.exception.*;
import edu.uci.python.runtime.iterator.*;
import edu.uci.python.runtime.standardtype.*;

public abstract class PBaseSet extends PythonBuiltinObject implements PIterable {

    protected final TreeSet<Object> set;

    public PBaseSet() {
        this.set = new TreeSet<>();
    }

    public PBaseSet(TreeSet<Object> elements) {
        this.set = elements;
    }

    public PBaseSet(PIterator iter) {
        this();
        try {
            while (true) {
                this.set.add(iter.__next__());
            }
        } catch (StopIterationException e) {
            // fall through
        }
    }

    public PBaseSet(PBaseSet baseSet) {
        this();
        addAll(baseSet);
    }

    @SlowPath
    private void addAll(PBaseSet baseSet) {
        this.set.addAll(baseSet.set);
    }

    public Set<Object> getSet() {
        return set;
    }

    public PIterator __iter__() {
        return new PBaseSetIterator(set.iterator());
    }

    public final boolean contains(Object o) {
        return this.set.contains(o);
    }

    // disjoint
    public boolean isDisjoint(PBaseSet other) {
        return Collections.disjoint(this.set, other.set);
    }

    @SuppressWarnings("unused")
    public boolean isDisjoint(PIterator other) {
        throw new UnsupportedOperationException();
    }

    // subset
    public boolean isSubset(PBaseSet other) {
        if (this.len() > other.len()) {
            return false;
        }

        for (Object p : this.set) {
            if (!other.set.contains(p)) {
                return false;
            }
        }
        return true;
    }

    public boolean isSubset(PIterator other) {
        return this.isSubset(new PSet(other)); // pack the iterable into a PSet
    }

    public boolean isProperSubset(PBaseSet other) {
        return this.len() < other.len() && this.isSubset(other);
    }

    // superset
    public boolean isSuperset(PBaseSet other) {
        return other.isSubset(this); // use subset comparison with this/other
                                     // order changed
    }

    public boolean isSuperset(PIterator other) {
        return this.isSuperset(new PSet(other));
    }

    public boolean isProperSuperset(PBaseSet other) { // is proper superset
        return this.len() > other.len() && this.isSuperset(other);
    }

    // union
    public PBaseSet union(PBaseSet other) {
        PBaseSet newSet = cloneThisSet();
        newSet.set.addAll(other.set);
        return newSet;
    }

    public PBaseSet union(PIterator other) {
        return this.union(new PSet(other));
    }

    // intersection
    public PBaseSet intersection(PBaseSet other) {
        boolean set1IsLarger = set.size() > other.set.size();
        PBaseSet cloneSet = (set1IsLarger ? other.cloneThisSet() : this.cloneThisSet());
        cloneSet.set.retainAll(set1IsLarger ? this.set : other.set);
        return cloneSet;
    }

    public PBaseSet intersection(PIterator other) {
        return this.intersection(new PSet(other));
    }

    // difference
    public PBaseSet difference(PBaseSet other) {
        PBaseSet newSet = cloneThisSet();
        newSet.set.removeAll(other.set);
        return newSet;
    }

    public PBaseSet difference(PIterator other) {
        return this.intersection(new PSet(other));
    }

    // symmetric_difference
    @SuppressWarnings("unused")
    public PBaseSet symmetricDifference(PBaseSet other) {
        throw new UnsupportedOperationException();
    }

    // copy
    PBaseSet copy() {
        return cloneThisSet();
    }

    // update
    public abstract void update(PBaseSet other);

    public abstract void update(PIterator iterator);

    // intersection_update
    @SuppressWarnings("unused")
    public void intersectionUpdate(PBaseSet other) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public void intersectionUpdate(PIterator iterator) {
        throw new UnsupportedOperationException();
    }

    // difference_update
    @SuppressWarnings("unused")
    public void differenceUpdate(PBaseSet other) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public void differenceUpdate(PIterator iterator) {
        throw new UnsupportedOperationException();
    }

    // symmetric_difference_update
    @SuppressWarnings("unused")
    public void symmetricDifferenceUpdate(PBaseSet other) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public void symmetricDifferenceUpdate(PIterator iterator) {
        throw new UnsupportedOperationException();
    }

    protected abstract PBaseSet cloneThisSet();

    // update methods needed for updating both sets and frozen sets, internally
    // "Binary operations that mix set instances with frozenset return
    // the type of the first operand.
    // For example: frozenset('ab') | set('bc') returns an instance of
    // frozenset."
    protected void updateInternal(Object data) {
        if (data instanceof PBaseSet) {
            updateInternal((PBaseSet) data);
        } else if (data instanceof PIterator) {
            throw new UnsupportedOperationException();
        }
    }

    protected void updateInternal(PBaseSet data) {
        // Skip the iteration if both are sets
        set.addAll(data.set);
    }

    protected void updateInternal(PIterator iterator) {
        try {
            while (true) {
                set.add(iterator.__next__());
            }
        } catch (StopIterationException e) {
            // fall through
        }
    }

    @Override
    public Object getMax() {
        return this.set.last();
    }

    @Override
    public Object getMin() {
        return this.set.first();
    }

    @Override
    public int len() {
        return this.set.size();
    }

    @Override
    public String toString() {
        if (set.size() == 0) {
            return "set()";
        }

        StringBuilder buf = new StringBuilder().append("{");

        for (Iterator<Object> i = set.iterator(); i.hasNext();) {
            String str = PSequence.toString(i.next());
            buf.append(str);

            if (i.hasNext()) {
                buf.append(", ");
            }
        }

        buf.append("}");
        return buf.toString();
    }

}
