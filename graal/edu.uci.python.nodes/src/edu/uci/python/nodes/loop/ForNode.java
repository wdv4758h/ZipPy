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
package edu.uci.python.nodes.loop;

import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.access.*;
import edu.uci.python.nodes.profiler.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.exception.*;
import edu.uci.python.runtime.iterator.*;

@NodeChild(value = "iterator", type = GetIteratorNode.class)
public abstract class ForNode extends LoopNode {

    @Child protected PNode target;

    public long forCounter = 0;

    public ForNode(PNode target, PNode body) {
        super(body);
        this.target = target;
        NodeProfiler.getInstance().addForNode(this);
    }

    protected ForNode(ForNode previous) {
        this(previous.target, previous.body);
        NodeProfiler.getInstance().deleteForNode(previous);
    }

    @Specialization(order = 0)
    public Object doPIterator(VirtualFrame frame, PRangeIterator iterator) {
        final RuntimeValueNode rvn = (RuntimeValueNode) ((WriteNode) target).getRhs();
        final int start = iterator.getStart();
        final int stop = iterator.getStop();
        final int step = iterator.getStep();

        for (int i = start; i < stop; i += step) {
            rvn.setValue(i);
            target.execute(frame);
            body.executeVoid(frame);
            forCounter++;
        }

        return PNone.NONE;
    }

    @Specialization
    public Object doPIterator(VirtualFrame frame, PIterator iterator) {
        final RuntimeValueNode rvn = (RuntimeValueNode) ((WriteNode) target).getRhs();
        try {
            while (true) {
                rvn.setValue(iterator.__next__());
                target.execute(frame);
                body.executeVoid(frame);
                forCounter++;
            }
        } catch (StopIterationException e) {
            // fall through
        }

        return PNone.NONE;
    }
}
