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
package edu.uci.python.nodes.call;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.function.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.function.*;

public class CallGeneratorInlinedNode extends InlinedCallNode {

    private final PGeneratorFunction generator;
    private final Assumption globalScopeUnchanged;
    @Child protected PNode generatorRoot;

    public CallGeneratorInlinedNode(PNode callee, PNode[] arguments, PGeneratorFunction generator, GeneratorRootNode generatorRoot, Assumption globalScopeUnchanged, FrameFactory frameFactory) {
        super(callee, arguments, generator.getFrameDescriptor().copy(), frameFactory);
        this.generator = generator;
        this.globalScopeUnchanged = globalScopeUnchanged;
        GeneratorRootNode original = generatorRoot;
        PNode root = new FunctionRootNode.InlinedFunctionRootNode(generator.getName(), original.getUninitializedParams(), original.getUninitializedBody(), original.getUninitializedReturn());
        this.generatorRoot = adoptChild(prepareBody(root));
    }

    public CallTarget getCallTarget() {
        return generator.getCallTarget();
    }

    public PNode getGeneratorRoot() {
        return generatorRoot;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        try {
            globalScopeUnchanged.check();
        } catch (InvalidAssumptionException e) {
            return uninitialize(frame);
        }

        final Object[] args = CallFunctionNode.executeArguments(frame, arguments);
        final PArguments pargs = new PArguments.VirtualFrameCargoArguments(PNone.NONE, null, frame, args);
        return generatorRoot.execute(createInlinedFrame(frame, pargs));
    }

}
