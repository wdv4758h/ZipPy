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
package edu.uci.python.nodes.function;

import com.oracle.truffle.api.CompilerDirectives.SlowPath;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.PNode;
import edu.uci.python.nodes.truffle.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.object.*;
import edu.uci.python.runtime.sequence.*;
import edu.uci.python.runtime.sequence.storage.*;

/**
 * @author Gulfem
 * @author zwei
 */
@NodeField(name = "context", type = PythonContext.class)
@NodeChild(value = "arguments", type = PNode[].class)
public abstract class PythonBuiltinNode extends PNode {

    public abstract PythonContext getContext();

    /**
     * Argument guards.
     */
    protected static boolean emptyArguments(VirtualFrame frame) {
        return PArguments.getUserArgumentLength(frame) == 0;
    }

    protected static boolean isIntStorage(PList list) {
        return list.getStorage() instanceof IntSequenceStorage;
    }

    protected static boolean isDoubleStorage(PList list) {
        return list.getStorage() instanceof DoubleSequenceStorage;
    }

    protected static boolean isObjectStorage(PList list) {
        return list.getStorage() instanceof ObjectSequenceStorage;
    }

    protected static boolean isBasicStorage(PList list) {
        return list.getStorage() instanceof BasicSequenceStorage;
    }

    protected static boolean isEmptyStorage(PList list) {
        return list.getStorage() instanceof EmptySequenceStorage;
    }

    /**
     * This is obviously a slow path.
     */
    @SlowPath
    public static String callAttributeSlowPath(PythonObject obj, String attributeId) {
        PythonCallable callable;

        try {
            callable = PythonTypesGen.PYTHONTYPES.expectPythonCallable(obj.getAttribute(attributeId));
        } catch (UnexpectedResultException e) {
            throw new IllegalStateException();
        }

        return callable.call(PArguments.createWithUserArguments(obj)).toString();
    }

}
