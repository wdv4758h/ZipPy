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

import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.statement.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.exception.*;

/**
 * RootNode of a Python Function body. It is invoked by a CallTarget.
 * 
 * @author zwei
 */
public class FunctionRootNode extends RootNode {

    private final String functionName;

    @Child protected ParametersNode parameters;
    @Child protected StatementNode body;
    @Child protected PNode returnValue;

    private final ParametersNode uninitializedParams;
    private final StatementNode uninitializedBody;
    private final PNode uninitializedReturn;

    public FunctionRootNode(String functionName, ParametersNode parameters, StatementNode body, PNode returnValue) {
        this.functionName = functionName;
        this.parameters = adoptChild(parameters);
        this.body = adoptChild(body);
        this.returnValue = adoptChild(returnValue);
        this.uninitializedParams = NodeUtil.cloneNode(parameters);
        this.uninitializedBody = NodeUtil.cloneNode(body);
        this.uninitializedReturn = NodeUtil.cloneNode(returnValue);
    }

    public void setBody(StatementNode body) {
        this.body = adoptChild(body);
    }

    public InlinedFunctionRootNode getInlinedRootNode() {
        return new InlinedFunctionRootNode(this);
    }

    public ParametersNode getUninitializedParams() {
        return uninitializedParams;
    }

    public StatementNode getUninitializedBody() {
        return uninitializedBody;
    }

    public PNode getUninitializedReturn() {
        return uninitializedReturn;
    }

    @Override
    public FunctionRootNode copy() {
        return new FunctionRootNode(this.functionName, this.uninitializedParams, this.uninitializedBody, this.uninitializedReturn);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        parameters.executeVoid(frame);

        try {
            return body.execute(frame);
        } catch (ImplicitReturnException ire) {
            return PNone.NONE;
        } catch (ExplicitReturnException ere) {
            return returnValue.execute(frame);
        }
    }

    @Override
    public String toString() {
        return "<function " + functionName + " at " + Integer.toHexString(hashCode()) + ">";
    }

    public static class InlinedFunctionRootNode extends PNode {

        private final String functionName;
        @Child protected ParametersNode parameters;
        @Child protected StatementNode body;
        @Child protected PNode returnValue;

        public InlinedFunctionRootNode(String functionName, ParametersNode params, StatementNode body, PNode returnNode) {
            this.functionName = functionName;
            this.parameters = adoptChild(params);
            this.body = adoptChild(body);
            this.returnValue = adoptChild(returnNode);
        }

        protected InlinedFunctionRootNode(FunctionRootNode node) {
            this.functionName = node.functionName;
            this.parameters = adoptChild(NodeUtil.cloneNode(node.uninitializedParams));
            this.body = adoptChild(NodeUtil.cloneNode(node.uninitializedBody));
            this.returnValue = adoptChild(NodeUtil.cloneNode(node.uninitializedReturn));
        }

        @Override
        public Object execute(VirtualFrame frame) {
            parameters.executeVoid(frame);

            try {
                return body.execute(frame);
            } catch (ImplicitReturnException ire) {
                return PNone.NONE;
            } catch (ExplicitReturnException ere) {
                return returnValue.execute(frame);
            }
        }

        @Override
        public String toString() {
            return "<inlined function root " + functionName + " at " + Integer.toHexString(hashCode()) + ">";
        }
    }

}
