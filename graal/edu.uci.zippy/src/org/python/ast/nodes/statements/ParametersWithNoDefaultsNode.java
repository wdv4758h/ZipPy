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
package org.python.ast.nodes.statements;

import java.util.Arrays;
import java.util.List;

import org.python.ast.nodes.PNode;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class ParametersWithNoDefaultsNode extends ParametersNode {

    @Children protected final PNode[] parameters;

    public ParametersWithNoDefaultsNode(PNode[] arguments, List<String> paramNames) {
        super(paramNames);
        this.parameters = adoptChildren(arguments);
    }

    /**
     * invoked by FunctionRootNode after new Frame is created. It applies runtime arguments to the
     * newly created VirtualFrame.
     */
    @ExplodeLoop
    @Override
    public void executeVoid(VirtualFrame frame) {
        for (int i = 0; i < parameters.length; i++) {
            parameters[i].executeVoid(frame);
        }
    }

    /*
     * @Override public <R> R accept(PNodeVisitor<R> visitor) { return
     * visitor.visitParametersDefaultNode(this); }
     */

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + Arrays.toString(parameters) + ")";
    }

}
