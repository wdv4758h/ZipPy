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

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.statement.*;
import edu.uci.python.runtime.function.*;

public class FunctionDefinitionNode extends PNode {

    private final String name;
    private final CallTarget callTarget;
    private final FrameDescriptor frameDescriptor;
    private final boolean needsDeclarationFrame;

    // It's parked here, but not adopted.
    @Child protected ParametersNode parameters;
    @Children final PNode[] functionDefaults;

    public FunctionDefinitionNode(String name, ParametersNode parameters, PNode[] functionDefaults, CallTarget callTarget, FrameDescriptor frameDescriptor, boolean needsDeclarationFrame) {
        this.name = name;
        this.callTarget = callTarget;
        this.frameDescriptor = frameDescriptor;
        this.needsDeclarationFrame = needsDeclarationFrame;
        this.parameters = parameters;
        this.functionDefaults = (functionDefaults == null || functionDefaults.length == 0) ? null : adoptChildren(functionDefaults);
    }

    public String getName() {
        return name;
    }

    protected CallTarget getCallTarget() {
        return callTarget;
    }

    protected FrameDescriptor getFrameDescriptor() {
        return frameDescriptor;
    }

    protected boolean needsDeclarationFrame() {
        return needsDeclarationFrame;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        parameters.evaluateDefaults(frame);
        Object[] defaults = executeDefaults(frame);
        MaterializedFrame declarationFrame = needsDeclarationFrame ? frame.materialize() : null;
        return new PFunction(name, parameters.getParameterNames(), defaults, callTarget, frameDescriptor, declarationFrame);
    }

    @ExplodeLoop
    private Object[] executeDefaults(VirtualFrame frame) {
        if (functionDefaults == null) {
            return null;
        }

        assert functionDefaults.length > 0;
        Object[] defaults = new Object[functionDefaults.length];

        for (int i = 0; i < functionDefaults.length; i++) {
            defaults[i] = functionDefaults[i].execute(frame);
        }

        return defaults;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + name + ")";
    }
}
