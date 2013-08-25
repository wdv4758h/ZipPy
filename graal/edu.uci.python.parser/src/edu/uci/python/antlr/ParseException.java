package edu.uci.python.antlr;

import org.antlr.runtime.*;
import org.python.core.*;

import edu.uci.python.nodes.*;

public class ParseException extends RuntimeException {

    public transient IntStream input;
    public int index;
    public Token token;
    public Object node;
    public int c;
    public int line;
    public int charPositionInLine;
    public boolean approximateLineInfo;

    private PyObject type = Py.SyntaxError;

    public ParseException() {
        super();
    }

    public ParseException(String message, int lin, int charPos) {
        super(message);
        this.line = lin;
        this.charPositionInLine = charPos;
    }

    public ParseException(String message) {
        this(message, 0, 0);
    }

    /**
     * n must not be null to use this constructor
     */

    public ParseException(String message, PNode n) {
        this(message, n.getLine(), n.getCharPositionInLine());
        this.node = n;
        this.token = n.getToken();
    }

    public ParseException(String message, RecognitionException r) {
        super(message);
        this.input = r.input;
        this.index = r.index;
        this.token = r.token;
        this.node = r.node;
        this.c = r.c;
        this.line = r.line;
        this.charPositionInLine = r.charPositionInLine;
        this.approximateLineInfo = r.approximateLineInfo;
    }

    public void setType(PyObject t) {
        this.type = t;
    }

    public PyObject getType() {
        return this.type;
    }

}
