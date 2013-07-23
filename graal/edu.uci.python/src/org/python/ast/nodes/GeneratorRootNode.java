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
package org.python.ast.nodes;

import org.python.ast.datatypes.*;
import org.python.ast.nodes.statements.*;
import org.python.ast.utils.*;

import com.oracle.truffle.api.frame.*;

public class GeneratorRootNode extends FunctionRootNode {

    StatementNode resumingNode = null;

    VirtualFrame frame;

    public GeneratorRootNode(ParametersNode parameters, StatementNode body) {
        super(parameters, body);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        parameters.executeVoid(frame);
        resumingNode = body;
        this.frame = frame;
        return new PGenerator(this);
    }

    public Object next() throws ImplicitReturnException {
        StatementNode current = resumingNode;

        while (current != null) {
            try {
                current.executeVoid(frame);
                current = current.next();
            } catch (ExplicitYieldException eye) {
                resumingNode = eye.getResumingNode();
                return eye.getValue();
            }
        }

        throw new ImplicitReturnException();
    }
}
