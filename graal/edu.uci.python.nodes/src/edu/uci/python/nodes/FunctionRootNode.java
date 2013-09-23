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
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.statements.*;
import edu.uci.python.nodes.utils.*;

/**
 * RootNode of a Python Function body. It should invoked by a CallTarget Object.
 * 
 * @author zwei
 * 
 */
public class FunctionRootNode extends RootNode {

    @Child protected final ParametersNode parameters;

    @Child protected StatementNode body;

    @Child protected PNode returnValue;

    public FunctionRootNode(ParametersNode parameters, StatementNode body, PNode returnValue) {
        this.parameters = adoptChild(parameters);
        this.body = adoptChild(body);
        this.returnValue = adoptChild(returnValue);
    }

    public void setBody(StatementNode body) {
        this.body = adoptChild(body);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        parameters.executeVoid(frame);

        try {
            return body.execute(frame);
        } catch (ImplicitReturnException ire) {
            return null;
        } catch (ExplicitReturnException ere) {
            return returnValue.execute(frame);
        }
    }
}
