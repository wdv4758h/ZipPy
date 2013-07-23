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

import org.python.ast.*;
import org.python.ast.nodes.expressions.BooleanCastNode;
import org.python.ast.utils.*;
import org.python.core.truffle.*;

import com.oracle.truffle.api.frame.*;

public class WhileNode extends StatementNode {

    @Child protected BooleanCastNode condition;

    @Child protected BlockNode body;

    @Child protected BlockNode orelse;

    public WhileNode(BooleanCastNode condition, BlockNode body, BlockNode orelse) {
        this.condition = adoptChild(condition);
        this.body = adoptChild(body);
        this.orelse = adoptChild(orelse);
    }

    public void setInternal(BlockNode body, BlockNode orelse) {
        this.body = adoptChild(body);
        this.orelse = adoptChild(orelse);
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        // try {
        while (condition.executeBoolean(frame)) {
            try {
                body.executeVoid(frame);
                if (reachedReturn() || isBreak()) {
                    this.isBreak = false;
                    return;
                }
            } catch (ContinueException ex) {
                // Fall through to next loop iteration.
            }
        }
        // } catch (BreakException ex) {
        // Done executing this loop, exit method to execute statement
        // following the loop.
        // return;
        // }

        /**
         * while for might have an orelse part which is only executed when loop terminates
         * regularly(without break)
         */
        orelse.executeVoid(frame);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        // try {
        while (condition.executeBoolean(frame)) {
            try {
                body.executeVoid(frame);
                if (reachedReturn() || isBreak()) {
                    this.isBreak = false;
                    return null;
                }
            } catch (ContinueException ex) {
                // Fall through to next loop iteration.
            }
        }
        // } catch (BreakException ex) {
        // Done executing this loop, exit method to execute statement
        // following the loop.
        // return;
        // }

        /**
         * while for might have an orelse part which is only executed when loop terminates
         * regularly(without break)
         */
        orelse.executeVoid(frame);
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + condition + ")";
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitWhileNode(this);
    }

    @Override
    public void visualize(int level) {
        for (int i = 0; i < level; i++) {
            ASTInterpreter.trace("    ");
        }
        ASTInterpreter.trace(this);

        level++;
        condition.visualize(level);
        body.visualize(level);
    }

}
