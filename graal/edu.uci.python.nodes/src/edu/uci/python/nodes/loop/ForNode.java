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

import java.util.*;

import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.statements.*;
import edu.uci.python.nodes.translation.*;
import edu.uci.python.runtime.datatypes.*;

@NodeChild(value = "iterator", type = PNode.class)
public abstract class ForNode extends LoopNode {

    @Child protected PNode target;

    public ForNode(PNode target, StatementNode body) {
        super(body);
        this.target = adoptChild(target);
    }

    protected ForNode(ForNode previous) {
        this(previous.target, previous.body);
    }

    public abstract PNode getIterator();

    @Specialization
    public Object doPSequence(VirtualFrame frame, PSequence sequence) {
        loopOnIterator(frame, sequence);
        return PNone.NONE;
    }

    @Specialization
    public Object doPBaseSet(VirtualFrame frame, PBaseSet set) {
        loopOnIterator(frame, set);
        return PNone.NONE;
    }

    @Specialization
    public Object doString(VirtualFrame frame, String string) {
        PString pstring = new PString(string);
        loopOnIterator(frame, pstring);
        return PNone.NONE;
    }

    private void loopOnIterator(VirtualFrame frame, Iterable iterable) {
        Iterator<?> iter = iterable.iterator();
        RuntimeValueNode rvn = (RuntimeValueNode) ((WriteNode) target).getRhs();

        while (iter.hasNext()) {
            rvn.setValue(iter.next());
            target.execute(frame);
            body.executeVoid(frame);
        }
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitForNode(this);
    }
}
