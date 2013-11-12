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
package edu.uci.python.nodes.truffle;

import java.math.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.python.core.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.CompilerDirectives.SlowPath;

import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.runtime.sequence.*;

public class PythonTypesUtil {

    public static PyTuple createPyTuple(Object[] values) {
        PyObject[] adaptedValue = new PyObject[values.length];

        for (int i = 0; i < values.length; i++) {
            adaptedValue[i] = adaptToPyObject(values[i]);
        }

        return new PyTuple(adaptedValue);
    }

    public static PTuple createTuple(Object[] values) {
        return new PTuple(values);
    }

    public static PyList createPyList(Object[] values) {
        PyObject[] adaptedValue = new PyObject[values.length];

        for (int i = 0; i < values.length; i++) {
            adaptedValue[i] = adaptToPyObject(values[i]);
        }

        return new PyList(adaptedValue);
    }

    public static PList createList(List<Object> values) {
        return new PList(values);
    }

    public static PSet createSet(List<Object> values) {
        return new PSet(values);
    }

    public static PDictionary createDictionary(Map<Object, Object> map) {
        return new PDictionary(map);
    }

    public static PyObject adaptToPyObject(Object value) {
        CompilerAsserts.neverPartOfCompilation();

        if (value instanceof PyObject) {
            return (PyObject) value;
        }

        if (value instanceof Integer) {
            return Py.newInteger((int) value);
        } else if (value instanceof BigInteger) {
            return Py.newLong((BigInteger) value);
        } else if (value instanceof Double) {
            return Py.newFloat((double) value);
        } else if (value instanceof PComplex) {
            PComplex complex = (PComplex) value;
            PyComplex pyComplex = new PyComplex(complex.getReal(), complex.getImag());
            return pyComplex;
        } else if (value instanceof String) {
            return Py.newString((String) value);
        } else if (value instanceof Boolean) {
            return Py.newBoolean((boolean) value);
        } else if (value instanceof PTuple) {
            PTuple tuple = (PTuple) value;
            return new PyTuple(adaptToPyObjects(tuple.getArray()));
        } else if (value instanceof PSet) {
            PSet set = (PSet) value;
            return new PySet(adaptToPyObjects(set.getSet().toArray()));
        } else if (value instanceof PFrozenSet) {
            PFrozenSet set = (PFrozenSet) value;
            return new PySet(adaptToPyObjects(set.getSet().toArray()));
        } else if (value instanceof PDictionary) {
            PDictionary dict = (PDictionary) value;
            ConcurrentHashMap<PyObject, PyObject> map = new ConcurrentHashMap<>();
            for (Object key : dict.keys()) {
                map.put(adaptToPyObject(key), adaptToPyObject(dict.getItem(key)));
            }
            return new PyDictionary(map);
        } else if (value instanceof PIntegerArray) {
            return new PyArray(int.class, ((PIntegerArray) value).getSequence());
        } else if (value instanceof PDoubleArray) {
            return new PyArray(double.class, ((PDoubleArray) value).getSequence());
        } else if (value instanceof PCharArray) {
            return new PyArray(char.class, ((PCharArray) value).getSequence());
        } else if (value instanceof PFunction) {
            /**
             * zwei: This is a fix to deal with string modulo operation. It only works if this
             * static function is invoked by the binary modulo node.
             */
            return new PyString(value.toString());
        }

        throw new RuntimeException("unexpected type! " + value.getClass());
    }

    @SlowPath
    public static PyObject[] adaptToPyObjects(Object[] values) {
        List<PyObject> converted = new ArrayList<>(values.length);

        for (Object value : values) {
            converted.add(adaptToPyObject(value));
        }

        return converted.toArray(new PyObject[values.length]);
    }

    @SlowPath
    public static Object unboxPyObject(PyObject value) {
        if (value instanceof PyInteger) {
            return ((PyInteger) value).getValue();
        } else if (value instanceof PyString) {
            return ((PyString) value).getString();
        } else if (value instanceof PyFloat) {
            return ((PyFloat) value).getValue();
        } else if (value instanceof PyLong) {
            return ((PyLong) value).getValue();
        } else if (value instanceof PyTuple) {
            PyTuple tuple = (PyTuple) value;
            return new PTuple(unboxPyObjects(tuple.getArray()));
        } else if (value instanceof PyList) {
            PyList list = (PyList) value;
            PyObject[] values = list.getArray();
            return new PList(unboxPyObjects(values));
        } else if (value instanceof PyArray) {
            // TODO Temporary fix
            PyList array = (PyList) ((PyArray) value).tolist();
            return unboxPyObject(array);
        }
        // fall back is just return as it is.
        return value;
    }

    public static Object[] unboxPyObjects(PyObject[] values) {
        Object[] unboxed = new Object[values.length];

        int i = 0;
        for (PyObject value : values) {
            unboxed[i++] = unboxPyObject(value);
        }

        return unboxed;
    }

    /**
     * TODO This method should be extended to support the remaining types.
     */
    public static String getPythonTypeName(Object object) {
        if (object instanceof Integer || object instanceof BigInteger) {
            return "int";
        } else if (object instanceof Double) {
            return "float";
        } else if (object instanceof PComplex) {
            return "complex";
        } else if (object instanceof String) {
            return "str";
        } else if (object instanceof PList) {
            return "list";
        } else if (object instanceof PTuple) {
            return "tuple";
        } else if (object instanceof PRange) {
            return "range";
        } else if (object instanceof PSet) {
            return "set";
        }

        throw new RuntimeException("Unsupported type name " + object.getClass());
    }
}
