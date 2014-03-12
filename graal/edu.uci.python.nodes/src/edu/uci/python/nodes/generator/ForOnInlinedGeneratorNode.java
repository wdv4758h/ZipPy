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
package edu.uci.python.nodes.generator;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.access.*;
import edu.uci.python.nodes.loop.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.exception.*;
import edu.uci.python.runtime.function.PArguments.GeneratorArguments;

public class ForOnInlinedGeneratorNode extends LoopNode {

    @Child protected FrameSlotNode target;
    @Child protected GetGeneratorArgumentsNode getGenArg;
    @Child protected AdvanceInlinedGeneratorNode next;

    public ForOnInlinedGeneratorNode(PNode body, FrameSlotNode target, GetGeneratorArgumentsNode getGenArg, AdvanceInlinedGeneratorNode next) {
        super(body);
        this.target = adoptChild(target);
        this.getGenArg = adoptChild(getGenArg);
        this.next = adoptChild(next);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        int count = 0;
        GeneratorArguments genArgs = (GeneratorArguments) getGenArg.execute(frame);

        try {
            while (true) {
                target.executeWrite(frame, next.executeWith(frame, genArgs));
                body.executeVoid(frame);

                if (CompilerDirectives.inInterpreter()) {
                    count++;
                }
            }
        } catch (StopIterationException e) {

        } finally {
            if (CompilerDirectives.inInterpreter()) {
                reportLoopCount(count);
            }
        }

        return PNone.NONE;
    }

}