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

import org.antlr.runtime.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.statements.*;

public class ErrorHandlerMsg {

    public PNode errorExpr(Token t) {
        throw new ParseException("Bad Expr Node", t.getLine(), t.getCharPositionInLine());
    }

    public PNode errorMod(Token t) {
        throw new ParseException("Bad Mod Node", t.getLine(), t.getCharPositionInLine());
    }

    public PNode errorSlice(Token t) {
        throw new ParseException("Bad Slice Node", t.getLine(), t.getCharPositionInLine());
    }

    public StatementNode errorStmt(Token t) {
        throw new ParseException("Bad Stmt Node", t.getLine(), t.getCharPositionInLine());
    }

    public void error(String message, Token t) {
        throw new ParseException(message, t.getLine(), t.getCharPositionInLine());
    }

    public void recover(BaseRecognizer br, IntStream input, RecognitionException re) {
        throw new ParseException(message(br, re));
    }

    public void reportError(BaseRecognizer br, RecognitionException re) {
        throw new ParseException(message(br, re));
    }

    public void recover(Lexer lex, RecognitionException re) {
        throw new ParseException(message(lex, re));
    }

    private String message(BaseRecognizer br, RecognitionException re) {
        return br.getErrorMessage(re, br.getTokenNames());
    }

    public Object recoverFromMismatchedToken(BaseRecognizer br, IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype, input);
    }

    public boolean mismatch(BaseRecognizer br, IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype, input);
    }

}
