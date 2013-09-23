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
package edu.uci.python.antlr;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;

import com.oracle.truffle.api.frame.*;

import edu.uci.python.nodes.*;

public class TrufflePNodeAdaptor extends CommonTreeAdaptor {

    @Override
    public void setTokenBoundaries(Object t, Token startToken, Token stopToken) {
        if (t == null) {
            return;
        }
        int start = 0;
        int stop = 0;
        int startChar = 0;
        int stopChar = 0;
        if (startToken != null) {
            start = startToken.getTokenIndex();
            startChar = ((CommonToken) startToken).getStartIndex();
        }
        if (stopToken != null) {
            stop = stopToken.getTokenIndex();
            stopChar = ((CommonToken) stopToken).getStopIndex() + 1;
        }
        PNode pt = (PNode) t;
        pt.setTokenStartIndex(start);
        pt.setTokenStopIndex(stop);
        pt.setCharStartIndex(startChar);
        pt.setCharStopIndex(stopChar);
    }

    @Override
    public Object create(Token token) {
        return new PNode(token) {

            @Override
            public Object execute(VirtualFrame frame) {
                return null;
            }
        };
    }

    @Override
    public Object errorNode(TokenStream input, Token start, Token stop, RecognitionException e) {
        TruffleErrorNode t = new TruffleErrorNode(input, start, stop, e);
        return t;
    }

    @Override
    public void addChild(Object t, Object child) {
        if (t != null && child != null) {
            /*
             * Truffle This is a hack. For some reason when both parent and child are Nil.
             * addChild() will recursively set its children's parent to be the parent, which is also
             * Nil. This process ruins some of the parent child relations and causes problems in
             * PythonTreeProcessor.
             */
            PNode tree = (PNode) t;
            PNode childTree = (PNode) child;
            if (tree.isNil() && childTree.isNil()) {
                return;
            }

            ((PNode) t).addChild((PNode) child);
        }
    }

    @Override
    public Object rulePostProcessing(Object root) {
        PNode r = (PNode) root;
        if (r != null && r.isNil()) {
            if (r.getChildCount() == 0) {
                r = null;
            } else if (r.getChildCount() == 1) {
                r = r.getChild(0);
                r.setChildIndex(-1);
            }
        }
        return r;
    }

}
