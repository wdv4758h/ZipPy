/*
 * Copyright (c) 2014, Regents of the University of California
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
package edu.uci.python.nodes.call;

import com.oracle.truffle.api.frame.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.call.DispatchNode.UninitializedDispatchNode;
import edu.uci.python.runtime.function.*;

public class DispatchCallNode extends PNode {

    @Children protected final PNode[] argumentNodes;
    @Child protected DispatchNode dispatchNode;

    public DispatchCallNode(PNode[] arguments, DispatchNode dispatch) {
        this.argumentNodes = arguments;
        this.dispatchNode = dispatch;
    }

    public static DispatchCallNode create(PythonCallable callee, PNode calleeNode, PNode[] argumentNodes) {
        UninitializedDispatchNode uninitialized = new DispatchNode.UninitializedDispatchNode(calleeNode);
        return new DispatchCallNode(argumentNodes, DispatchNode.create(callee, uninitialized));
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object[] arguments = CallFunctionNode.executeArguments(frame, argumentNodes);
        return dispatchNode.executeCall(frame, arguments);
    }

}
