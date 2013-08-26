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
package edu.uci.python.datatypes;

import java.util.*;

import org.antlr.runtime.Token;

import com.oracle.truffle.api.frame.*;

import edu.uci.python.nodes.*;

public class PComprehension extends PNode {

    private PNode target;
    private PNode iter;
    private List<PNode> ifs;
    private PComprehension innerLoop;
    private PNode loopBody;

    public PComprehension(Token token, PNode target, PNode iter, java.util.List<PNode> ifs) {
        super(token);
        this.target = target;
        this.iter = iter;
        this.ifs = ifs;
        if (ifs == null) {
            this.ifs = new ArrayList<>();
        }
    }

    /**
     * Truffle If we expand list comprehension in to a conventional nested loop, The left most
     * expression of list comprehension become the body of the inner most loop. So we transform
     * ListComp Node by attaching its elts expression to the inner most comprehension Node instead.
     * The interpreter would interpret ListComp as if it was a nested loop.
     */

    public void setLoopBody(PNode body) {
        loopBody = body;
    }

    public PNode getLoopBody() {
        return loopBody;
    }

    public void setInnerLoop(PComprehension loop) {
        innerLoop = loop;
    }

    public PComprehension getInnerLoop() {
        return innerLoop;
    }

    public PNode getInternalTarget() {
        return target;
    }

    public void setInternalTarget(PNode target) {
        this.target = target;
    }

    public PNode getInternalIter() {
        return iter;
    }

    public List<PNode> getInternalIfs() {
        return ifs;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        // TODO Auto-generated method stub
        return null;
    }
}
