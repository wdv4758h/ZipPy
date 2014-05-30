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
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.function.*;

public final class GeneratorExpressionNode extends PNode {

    // name = "generator_exp:" + line number of the generator;
    private final String name;
    protected final PythonContext context;
    private final RootCallTarget callTarget;
    private final FrameDescriptor frameDescriptor;
    private final boolean needsDeclarationFrame;
    private final int numOfActiveFlags;
    private final int numOfGeneratorBlockNode;
    private final int numOfGeneratorForNode;

    @CompilationFinal private FrameDescriptor enclosingFrameDescriptor;
    @CompilationFinal private boolean isEnclosingFrameGenerator;
    @CompilationFinal private boolean isOptimized;

    public GeneratorExpressionNode(String name, PythonContext context, RootCallTarget callTarget, FrameDescriptor descriptor, boolean needsDeclarationFrame, int numOfActiveFlags,
                    int numOfGeneratorBlockNode, int numOfGeneratorForNode) {
        this.name = name;
        this.context = context;
        this.callTarget = callTarget;
        this.frameDescriptor = descriptor;
        this.needsDeclarationFrame = needsDeclarationFrame;
        this.numOfActiveFlags = numOfActiveFlags;
        this.numOfGeneratorBlockNode = numOfGeneratorBlockNode;
        this.numOfGeneratorForNode = numOfGeneratorForNode;
    }

    public String getName() {
        return name;
    }

    public RootCallTarget getCallTarget() {
        return callTarget;
    }

    public FrameDescriptor getFrameDescriptor() {
        return frameDescriptor;
    }

    public boolean needsDeclarationFrame() {
        return needsDeclarationFrame;
    }

    public FrameDescriptor getEnclosingFrameDescriptor() {
        return enclosingFrameDescriptor;
    }

    public void setEnclosingFrameDescriptor(FrameDescriptor frameDescriptor) {
        CompilerAsserts.neverPartOfCompilation();
        enclosingFrameDescriptor = frameDescriptor;
    }

    public boolean isEnclosingFrameGenerator() {
        return isEnclosingFrameGenerator;
    }

    public void setEnclosingFrameGenerator(boolean value) {
        isEnclosingFrameGenerator = value;
    }

    public boolean isOptimized() {
        return isOptimized;
    }

    public void setAsOptimized() {
        isOptimized = true;
    }

    public int getNumOfActiveFlags() {
        return numOfActiveFlags;
    }

    public int getNumOfGeneratorBlockNode() {
        return numOfGeneratorBlockNode;
    }

    public int getNumOfGeneratorForNode() {
        return numOfGeneratorForNode;
    }

    public RootNode getFunctionRootNode() {
        RootCallTarget defaultTarget = callTarget;
        return defaultTarget.getRootNode();
    }

    @Override
    public Object execute(VirtualFrame frame) {
        MaterializedFrame declarationFrame = null;

        if (needsDeclarationFrame) {
            declarationFrame = isEnclosingFrameGenerator ? PArguments.getGeneratorFrame(frame) : frame.materialize();
        }

        return PGenerator.create(name, callTarget, frameDescriptor, declarationFrame, PArguments.create(), numOfActiveFlags, numOfGeneratorBlockNode, numOfGeneratorForNode);
    }

    @Override
    public String toString() {
        return name;
    }

}
