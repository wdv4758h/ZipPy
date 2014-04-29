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
package edu.uci.python.nodes.call.legacy;

import org.python.core.*;

import com.oracle.truffle.api.dsl.Generic;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.frame.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.literal.*;
import edu.uci.python.runtime.*;
import static edu.uci.python.nodes.truffle.PythonTypesUtil.*;

@NodeChild(value = "callee", type = PNode.class)
public abstract class CallFunctionNode extends PNode {

    @Children protected final PNode[] arguments;
    @Children protected final KeywordLiteralNode[] keywords;
    private final PythonContext context;

    public CallFunctionNode(PNode[] arguments, KeywordLiteralNode[] keywords, PythonContext context) {
        this.arguments = arguments;
        this.keywords = keywords;
        this.context = context;
    }

    protected CallFunctionNode(CallFunctionNode node) {
        this(node.arguments, node.keywords, node.context);
    }

    public abstract PNode getCallee();

    public PNode[] getArguments() {
        return arguments;
    }

    public PythonContext getContext() {
        return context;
    }

    @SuppressWarnings("unused")
    @Generic
    public Object doGeneric(VirtualFrame frame, Object callee) {
        throw Py.TypeError("'" + getPythonTypeName(callee) + "' object is not callable");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(callee=" + getCallee() + ")";
    }

}