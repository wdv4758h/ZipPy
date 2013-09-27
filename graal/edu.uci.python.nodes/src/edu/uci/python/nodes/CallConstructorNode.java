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

import edu.uci.python.runtime.modules.*;
import edu.uci.python.runtime.objects.*;

public class CallConstructorNode extends PNode {

    @Child protected PNode targetClass;

    @Children private final PNode[] arguments;

    public CallConstructorNode(PNode targetClass, PNode[] arguments) {
        this.targetClass = adoptChild(targetClass);
        this.arguments = adoptChildren(arguments);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object[] args = CallNode.executeArguments(frame, arguments);
        PythonClass clazz = (PythonClass) targetClass.execute(frame);
        PythonBasicObject obj = new PythonBasicObject(clazz);
        Object[] selfWithArgs = new Object[args.length + 1];

        selfWithArgs[0] = obj;
        for (int i = 1; i < args.length + 1; i++) {
            selfWithArgs[i] = args[i - 1];
        }

        return clazz.lookUpMethod("__init__").call(frame.pack(), selfWithArgs);
    }
}
