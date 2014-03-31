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
package edu.uci.python.nodes.statement;

import com.oracle.truffle.api.frame.VirtualFrame;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.expression.*;
import edu.uci.python.nodes.profiler.*;
import edu.uci.python.runtime.datatype.*;

public class IfNode extends StatementNode {

    @Child protected CastToBooleanNode condition;
    @Child protected PNode then;
    @Child protected PNode orelse;

    public long thenCounter = 0;
    public long elseCounter = 0;

    public IfNode(CastToBooleanNode condition, PNode then, PNode orelse) {
        this.condition = adoptChild(condition);
        this.then = adoptChild(then);
        this.orelse = adoptChild(orelse);
        NodeProfiler.getInstance().addIfNode(this);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (condition.executeBoolean(frame)) {
            then.executeVoid(frame);
            thenCounter++;
        } else {
            orelse.executeVoid(frame);
            elseCounter++;
        }

        return PNone.NONE;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + condition + ")";
    }

}
