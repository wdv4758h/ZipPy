package edu.uci.python.antlr;

import org.antlr.runtime.*;
import org.python.antlr.ParseException;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.statements.*;

public class ErrorHandlerMsg {

    public PNode errorExpr(PNode t) {
        throw new ParseException("Bad Expr Node", t.getLine(), t.getCharPositionInLine());
    }

    public PNode errorMod(PNode t) {
        throw new ParseException("Bad Mod Node", t.getLine(), t.getCharPositionInLine());
    }

    public PNode errorSlice(PNode t) {
        throw new ParseException("Bad Slice Node", t.getLine(), t.getCharPositionInLine());
    }

    public StatementNode errorStmt(PNode t) {
        throw new ParseException("Bad Stmt Node", t.getLine(), t.getCharPositionInLine());
    }

    public void error(String message, PNode t) {
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
