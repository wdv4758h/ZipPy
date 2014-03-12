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
package edu.uci.python.nodes.generator;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.loop.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.exception.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.iterator.*;

public class GeneratorForNode extends LoopNode {

    @Child protected WriteGeneratorFrameVariableNode target;
    @Child protected GetIteratorNode getIterator;

    protected final int iteratorSlot;
    protected int count;

    public GeneratorForNode(WriteGeneratorFrameVariableNode target, GetIteratorNode getIterator, PNode body, int iteratorSlot) {
        super(body);
        this.target = adoptChild(target);
        this.getIterator = adoptChild(getIterator);
        this.iteratorSlot = iteratorSlot;
    }

    public PNode getTarget() {
        return target;
    }

    public int getIteratorSlot() {
        return iteratorSlot;
    }

    protected PIterator getIterator(VirtualFrame frame) {
        return PArguments.getGeneratorArguments(frame).getIteratorAt(iteratorSlot);
    }

    protected void setIterator(VirtualFrame frame, PIterator value) {
        PArguments.getGeneratorArguments(frame).setIteratorAt(iteratorSlot, value);
    }

    protected void reset(VirtualFrame frame) {
        setIterator(frame, null);
        count = 0;
    }

    protected void incrementCounter() {
        if (CompilerDirectives.inInterpreter()) {
            count++;
        }
    }

    @Override
    public Object execute(VirtualFrame frame) {
        try {
            executeIterator(frame);
        } catch (StopIterationException e) {
            reset(frame);
            return PNone.NONE;
        }

        try {
            while (true) {
                body.executeVoid(frame);
                target.executeWith(frame, getIterator(frame).__next__());
                incrementCounter();
            }
        } catch (StopIterationException e) {
            if (CompilerDirectives.inInterpreter()) {
                reportLoopCount(count);
            }
        }

        reset(frame);
        return PNone.NONE;
    }

    protected void executeIterator(VirtualFrame frame) throws StopIterationException {
        if (getIterator(frame) != null) {
            return;
        }

        try {
            setIterator(frame, getIterator.executePIterator(frame));
        } catch (UnexpectedResultException e) {
            throw new RuntimeException();
        }

        target.executeWith(frame, getIterator(frame).__next__());
        incrementCounter();
    }

    public static final class InnerGeneratorForNode extends GeneratorForNode {

        public InnerGeneratorForNode(WriteGeneratorFrameVariableNode target, GetIteratorNode getIterator, PNode body, int iteratorSlot) {
            super(target, getIterator, body, iteratorSlot);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            executeIterator(frame);

            try {
                while (true) {
                    target.executeWith(frame, getIterator(frame).__next__());
                    body.executeVoid(frame);
                    incrementCounter();
                }
            } catch (StopIterationException e) {
                if (CompilerDirectives.inInterpreter()) {
                    reportLoopCount(count);
                }
            }

            reset(frame);
            return PNone.NONE;
        }

        @Override
        protected void executeIterator(VirtualFrame frame) {
            if (getIterator(frame) != null) {
                return;
            }
            try {
                setIterator(frame, getIterator.executePIterator(frame));
            } catch (UnexpectedResultException e) {
                throw new RuntimeException();
            }
        }
    }

}