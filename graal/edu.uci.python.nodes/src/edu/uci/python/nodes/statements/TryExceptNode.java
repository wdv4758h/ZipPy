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
package edu.uci.python.nodes.statements;

import org.python.core.*;

import com.oracle.truffle.api.frame.*;

import edu.uci.python.nodes.*;

public class TryExceptNode extends StatementNode {

    @Child protected BlockNode body;

    @Child protected BlockNode orelse;

    @Child protected PNode exceptType;
    @Child protected PNode exceptName;
    @Child protected BlockNode exceptBody;

    protected TryExceptNode(BlockNode body, BlockNode orelse, PNode exceptType, PNode exceptName, BlockNode exceptBody) {
        this.body = adoptChild(body);
        this.orelse = adoptChild(orelse);

        this.exceptName = adoptChild(exceptName);
        this.exceptType = adoptChild(exceptType);
        this.exceptBody = adoptChild(exceptBody);
    }

    public static TryExceptNode create(BlockNode body, BlockNode orelse, PNode exceptType, PNode exceptName, BlockNode exceptBody) {
        return new TryOnlyNode(body, orelse, exceptType, exceptName, exceptBody);
    }

    @SuppressWarnings("unused")
    protected Object executeExcept(VirtualFrame frame, RuntimeException ex) {
        throw new UnsupportedOperationException("cannot execute executeExcept on TryOnlyNode");
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return null;
    }
}

class TryOnlyNode extends TryExceptNode {

    protected TryOnlyNode(BlockNode body, BlockNode orelse, PNode exceptType, PNode exceptName, BlockNode exceptBody) {
        super(body, orelse, exceptType, exceptName, exceptBody);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        try {
            if (orelse != null) {
                body.execute(frame);
                return orelse.execute(frame);
            } else {
                return body.execute(frame);
            }
        } catch (RuntimeException ex) {
            return this.replace(new GenericTryExceptNode(body, orelse, exceptType, exceptName, exceptBody)).executeExcept(frame, ex);
        }
    }
}

class GenericTryExceptNode extends TryExceptNode {

    protected GenericTryExceptNode(BlockNode body, BlockNode orelse, PNode exceptType, PNode exceptName, BlockNode exceptBody) {
        super(body, orelse, exceptType, exceptName, exceptBody);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        try {
            if (orelse != null) {
                body.execute(frame);
                return orelse.execute(frame);
            } else {
                return body.execute(frame);
            }
        } catch (RuntimeException ex) {
            return executeExcept(frame, ex);
        }
    }

    @Override
    protected Object executeExcept(VirtualFrame frame, RuntimeException excep) {
        PyException e = null;
        if (excep instanceof PyException) {
            e = (PyException) excep;
        } else if (excep instanceof ArithmeticException && excep.getMessage().endsWith("divide by zero")) {
            e = Py.ZeroDivisionError("divide by zero");
        } else {
            throw excep;
        }

        if (exceptType != null) {
            PyObject type = (PyObject) exceptType.execute(frame);
            if (exceptName != null) {
                exceptName.execute(frame);
            }

            if (e.type == type) {
                return exceptBody.execute(frame);
            }
        }
        throw excep;

    }
}
