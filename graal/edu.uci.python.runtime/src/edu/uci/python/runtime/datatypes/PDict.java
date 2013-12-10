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

import edu.uci.python.runtime.*;
import edu.uci.python.runtime.builtins.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.iterator.*;
import edu.uci.python.runtime.standardtypes.*;

public class PDict extends PythonBuiltinObject implements PIterable {

    private static final PythonBuiltinClass __class__ = PythonContext.getBuiltinTypeFor(PDict.class);

    private final Map<Object, Object> map;

    public PDict() {
        map = new HashMap<>();
    }

    public PDict(Map<Object, Object> map) {
        this();
        this.map.putAll(map);
    }

    @Override
    public PythonBuiltinClass __class__() {
        return __class__;
    }

    @Override
    public PythonCallable __getattribute__(String name) {
        return (PythonCallable) __class__.getAttribute(name);
    }

    public Object getItem(Object key) {
        return map.get(key);
    }

    public void setItem(Object key, Object value) {
        map.put(key, value);
    }

    public void delItem(Object key) {
        map.remove(key);
    }

    public Collection<Object> items() {
        return map.values();
    }

    public Collection<Object> keys() {
        return map.keySet();
    }

    public Map<Object, Object> getMap() {
        return map;
    }

    public boolean hasKey(Object[] args) {
        if (args.length == 1) {
            return this.map.containsKey(args[0]);
        } else {
            throw new RuntimeException("invalid arguments for has_key()");
        }
    }

    public PIterator __iter__() {
        return new PDictIterator(map.keySet());
    }

    public PIterator values() {
        return new PDictIterator(map.values());
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        int length = map.size();
        int i = 0;

        for (Object key : map.keySet()) {
            buf.append(key.toString() + " : " + map.get(key));

            if (i < length - 1) {
                buf.append(", ");
            }

            i++;
        }

        buf.append("}");
        return buf.toString();
    }

    @Override
    public int len() {
        return map.size();
    }

    @Override
    public Object getMax() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getMin() {
        throw new UnsupportedOperationException();
    }
}
