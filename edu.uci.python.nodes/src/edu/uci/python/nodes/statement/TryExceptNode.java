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
package edu.uci.python.nodes.statement;

import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.api.utilities.*;

import edu.uci.python.nodes.*;

public class TryExceptNode extends StatementNode {

    @Child protected PNode body;
    @Children final ExceptNode[] exceptNodes;
    @Child protected PNode orelse;

    private final BranchProfile exceptProfile = new BranchProfile();

    public TryExceptNode(PNode body, ExceptNode[] exceptNodes, PNode orelse) {
        this.body = body;
        this.exceptNodes = exceptNodes;
        this.orelse = orelse;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        try {
            body.execute(frame);
            return orelse.execute(frame);
        } catch (RuntimeException ex) {
            exceptProfile.enter();
            return catchException(frame, ex);
        }
    }

    private Object catchException(VirtualFrame frame, RuntimeException exception) {
        for (ExceptNode exceptNode : exceptNodes) {
            try {
                exceptNode.executeExcept(frame, exception);
            } catch (ControlFlowException cf) {
                return cf;
            } catch (RuntimeException ex) {
                if (exception != ex) {
                    throw ex;
                }
            }
        }

        throw exception;
    }

}
