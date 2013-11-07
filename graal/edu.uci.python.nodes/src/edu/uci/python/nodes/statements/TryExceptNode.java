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
import edu.uci.python.nodes.access.*;

public abstract class TryExceptNode extends StatementNode {

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
        if (orelse == null) {
            return new TryOnlyNode(body, orelse, exceptType, exceptName, exceptBody);
        }
        return new TryElseNode(body, orelse, exceptType, exceptName, exceptBody);

    }
}

class TryOnlyNode extends TryExceptNode {

    protected TryOnlyNode(BlockNode body, BlockNode orelse, PNode exceptType, PNode exceptName, BlockNode exceptBody) {
        super(body, orelse, exceptType, exceptName, exceptBody);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        try {
            return body.execute(frame);
        } catch (RuntimeException ex) {
            return this.replace(new GenericTryExceptNode(body, orelse, exceptType, exceptName, exceptBody)).executeExcept(frame, ex);
        }
    }
}

class TryElseNode extends TryExceptNode {

    protected TryElseNode(BlockNode body, BlockNode orelse, PNode exceptType, PNode exceptName, BlockNode exceptBody) {
        super(body, orelse, exceptType, exceptName, exceptBody);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        try {
            body.execute(frame);
        } catch (RuntimeException ex) {
            return this.replace(new GenericTryExceptNode(body, orelse, exceptType, exceptName, exceptBody)).executeExcept(frame, ex);
        }
        return orelse.execute(frame);
    }
}

class GenericTryExceptNode extends TryExceptNode {

    protected GenericTryExceptNode(BlockNode body, BlockNode orelse, PNode exceptType, PNode exceptName, BlockNode exceptBody) {
        super(body, orelse, exceptType, exceptName, exceptBody);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        try {
            body.execute(frame);
        } catch (RuntimeException ex) {
            return executeExcept(frame, ex);
        }
        return orelse.execute(frame);
    }

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
            if (e.type == type) {
                if (exceptName != null) {
                    ((WriteLocalNode) exceptName).execute(frame, excep);
                }
                return exceptBody.execute(frame);
            }
        }
        throw excep;
    }
}
