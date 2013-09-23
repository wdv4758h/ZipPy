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

import java.util.*;

import org.antlr.runtime.*;
import org.python.core.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.expressions.*;
import edu.uci.python.nodes.literals.*;
import edu.uci.python.nodes.statements.*;

public class GrammarUtilities {

    public StringBuilder output = null;

    public static String makeFromText(List<?> dots, List<PNode> names) {
        StringBuilder d = new StringBuilder();
        d.append(GrammarUtilities.dottedNameListToString(names));
        return d.toString();
    }

    public static String dottedNameListToString(List<PNode> names) {
        if (names == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean leadingDot = true;
        for (int i = 0, len = names.size(); i < len; i++) {
            PNode name = names.get(i);
            String id = ((ReadGlobalNode) name).getName();
            if (id == null) {
                continue;
            }
            if (!".".equals(id)) {
                leadingDot = false;
            }
            sb.append(id);
            if (i < len - 1 && !leadingDot) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    public static List<PNode> castSlices(List<?> slices) {
        List<PNode> result = new ArrayList<>();
        if (slices != null) {
            for (Object o : slices) {
                result.add(castSlice(o));
            }
        }
        return result;
    }

    public static PNode castSlice(Object o) {
        if (o instanceof IndexNode) {
            return (PNode) o;
        }
        if (o instanceof SliceNode) {
            return (PNode) o;
        }
        return ErrorHandler.errorSlice(((PNode) o).getToken());
    }

    private static void checkGenericAssign(PNode e) { // TODO: Not done yet!!

        // if (e instanceof Name && ((Name) e).getInternalId().equals("None"))
        // {
        // ErrorHandler.error("assignment to None", e);
        // }
        // else if (e instanceof GeneratorExp)
        // {
        // ErrorHandler.error("can't assign to generator expression", e);
        // }
        // else if (e instanceof Num)
        // {
        // ErrorHandler.error("can't assign to number", e);
        // }
        // else if (e instanceof Str)
        // {
        // ErrorHandler.error("can't assign to string", e);
        // }
        // else if (e instanceof Yield)
        // {
        // ErrorHandler.error("can't assign to yield expression", e);
        // }
        // else if (e instanceof BinOp)
        // {
        // ErrorHandler.error("can't assign to operator", e);
        // }
        // else if (e instanceof BoolOp)
        // {
        // ErrorHandler.error("can't assign to operator", e);
        // }
        // else if (e instanceof Lambda)
        // {
        // ErrorHandler.error("can't assign to lambda", e);
        // }
        // else if (e instanceof Call)
        // {
        // ErrorHandler.error("can't assign to function call", e);
        // }
        // else if (e instanceof Repr)
        // {
        // ErrorHandler.error("can't assign to repr", e);
        // }
        // else if (e instanceof IfExp)
        // {
        // ErrorHandler.error("can't assign to conditional expression", e);
        // }
        // else if (e instanceof ListComp)
        // {
        // ErrorHandler.error("can't assign to list PComprehension", e);
        // }
    }

    public static void checkAugAssign(PNode e) {
        checkGenericAssign(e);
        if (e instanceof TupleLiteralNode) {
            ErrorHandler.error("assignment to tuple illegal for augmented assignment", e.getToken());
        } else if (e instanceof ListLiteralNode) {
            ErrorHandler.error("assignment to list illegal for augmented assignment", e.getToken());
        }
    }

    public static void checkAssign(PNode e) {
        checkGenericAssign(e);
        if (e instanceof TupleLiteralNode) {
            // XXX: performance problem? Any way to do this better?
            List<PNode> elts = ((TupleLiteralNode) e).getElts();
            if (elts.size() == 0) {
                ErrorHandler.error("can't assign to ()", e.getToken());
            }
            for (int i = 0; i < elts.size(); i++) {
                checkAssign(elts.get(i));
            }
        } else if (e instanceof ListLiteralNode) {
            // XXX: performance problem? Any way to do this better?
            List<PNode> elts = ((ListLiteralNode) e).getElts();
            for (int i = 0; i < elts.size(); i++) {
                checkAssign(elts.get(i));
            }
        }
    }

    public static class StringPair {

        private String s;
        private boolean unicode;

        StringPair(String s, boolean unicode) {
            this.s = s;
            this.unicode = unicode;
        }

        String getString() {
            return s;
        }

        boolean isUnicode() {
            return unicode;
        }
    }

    public static String extractStrings(List<?> s, String encoding, boolean unicodeLiterals) {
        boolean ustring = false;
        Token last = null;
        StringBuffer sb = new StringBuffer();
        Iterator<?> iter = s.iterator();
        while (iter.hasNext()) {
            last = (Token) iter.next();
            StringPair sp = extractString(last, encoding, unicodeLiterals);
            if (sp.isUnicode()) {
                ustring = true;
            }
            sb.append(sp.getString());
        }
// if (ustring) {
// return new PyUnicode(sb.toString());
// }
        return sb.toString();
    }

    public static StringPair extractString(Token t, String encoding, boolean unicodeLiterals) {
        String string = t.getText();
        char quoteChar = string.charAt(0);
        int start = 0;
        int end;
        boolean ustring = unicodeLiterals;

        if (quoteChar == 'u' || quoteChar == 'U') {
            ustring = true;
            start++;
        }
        if (quoteChar == 'b' || quoteChar == 'B') {
            // In 2.x this is just a str, and the parser prevents a 'u' and a
            // 'b' in the same identifier, so just advance start.
            ustring = false;
            start++;
        }
        quoteChar = string.charAt(start);
        boolean raw = false;
        if (quoteChar == 'r' || quoteChar == 'R') {
            raw = true;
            start++;
        }
        int quotes = 3;
        if (string.length() - start == 2) {
            quotes = 1;
        }
        if (string.charAt(start) != string.charAt(start + 1)) {
            quotes = 1;
        }

        start = quotes + start;
        end = string.length() - quotes;
        // string is properly decoded according to the source encoding
        // XXX: No need to re-encode when the encoding is iso-8859-1, but
        // ParserFacade
        // needs to normalize the encoding name
        if (!ustring && encoding != null) {
            // str with a specified encoding: first re-encode back out
            string = new PyUnicode(string.substring(start, end)).encode(encoding);
            if (!raw) {
                // Handle escapes in non-raw strs
                string = PyString.decode_UnicodeEscape(string, 0, string.length(), "strict", ustring);
            }
        } else if (raw) {
            // Raw str without an encoding or raw unicode
            string = string.substring(start, end);
            if (ustring) {
                // Raw unicode: handle unicode escapes
                string = codecs.PyUnicode_DecodeRawUnicodeEscape(string, "strict");
            }
        } else {
            // Plain unicode: already decoded, just handle escapes
            string = PyString.decode_UnicodeEscape(string, start, end, "strict", ustring);
        }
        StringPair retVal = new StringPair(string, ustring);
        return retVal;
    }

    public static Token extractStringToken(List<?> s) {
        return (Token) s.get(0);
        // return (Token)s.get(s.size() - 1);
    }

    public static void errorGenExpNotSoleArg(PNode t) {
        ErrorHandler.error("Generator expression must be parenthesized if not sole argument", t.getToken());
    }

    public static void errorGenExpNotSoleArg(Token t) {
        ErrorHandler.error("Generator expression must be parenthesized if not sole argument", t);
    }

    public static PNode castExpr(Object o) {
        PNode retVal = null;
        if (o instanceof PNode) {
            retVal = (PNode) o;
        }
        return retVal;
    }

    public static List<PNode> castExprs(List<?> exprs) {
        return castExprs(exprs, 0);
    }

    public static List<PNode> castExprs(List<?> exprs, int start) {
        List<PNode> result = new ArrayList<>();
        if (exprs != null) {
            for (int i = start; i < exprs.size(); i++) {
                Object o = exprs.get(i);
                if (o instanceof PNode) {
                    result.add((PNode) o);
                } else if (o instanceof TruffleParser.test_return) {
                    result.add(((TruffleParser.test_return) o).getTree());
                }
            }
        }
        return result;
    }

    public static PNode castStmt(Object o) {
        if (o instanceof StatementNode) {
            return (StatementNode) o;
        } else if (o instanceof TruffleParser.stmt_return) {
            return ((TruffleParser.stmt_return) o).getTree();
        } else if (o instanceof PNode) {
            return ((PNode) o);
        }
        return null;
    }

    public static List<PNode> castStmts(PNode t) {
        PNode s = t;
        List<PNode> statementNodes = new ArrayList<>();
        statementNodes.add(s);
        return statementNodes;
    }

    public static List<PNode> castStmts(List<?> statementNodes) {
        if (statementNodes != null) {
            List<PNode> result = new ArrayList<>();
            for (Object o : statementNodes) {
                result.add(castStmt(o));
            }
            return result;
        }
        return new ArrayList<>();
    }

}
