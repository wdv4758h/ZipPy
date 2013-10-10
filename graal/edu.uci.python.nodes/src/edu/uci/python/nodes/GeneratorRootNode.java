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
package edu.uci.python.nodes;

import com.oracle.truffle.api.frame.*;

import edu.uci.python.datatypes.*;
import edu.uci.python.nodes.statements.*;
import edu.uci.python.nodes.utils.*;

public class GeneratorRootNode extends FunctionRootNode {

    private StatementNode continuingNode;

    private VirtualFrame continuingFrame;

    public GeneratorRootNode(String functionName, ParametersNode parameters, StatementNode body, PNode returnValue) {
        super(functionName, parameters, body, returnValue);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        parameters.executeVoid(frame);
        continuingNode = body;
        this.continuingFrame = frame;
        return new PGenerator(this);
    }

    public Object next() throws ImplicitReturnException {
        StatementNode current = continuingNode;

        while (current != null) {
            try {
                current.executeVoid(continuingFrame);
                current = current.next();
            } catch (ExplicitYieldException eye) {
                continuingNode = eye.getResumingNode();
                return eye.getValue();
            }
        }

        throw new ImplicitReturnException();
    }
}
