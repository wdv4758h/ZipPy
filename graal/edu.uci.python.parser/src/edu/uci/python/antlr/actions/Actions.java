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
package edu.uci.python.antlr.actions;

import java.math.*;
import java.util.*;

import org.antlr.runtime.*;

import com.oracle.truffle.api.frame.*;

import edu.uci.python.antlr.*;
import edu.uci.python.datatypes.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.expressions.*;
import edu.uci.python.nodes.literals.*;
import edu.uci.python.nodes.statements.*;
import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.types.*;

public class Actions {

    private NodeFactory factory = null;
    private Variables var = null;

    public Actions(NodeFactory factory) {
        this.factory = factory;
        var = new Variables(factory);

    }

    public PNode makeIndex(PNode node, PNode value) {
        return GrammarUtil.make(node.getToken(), factory.createIndex(value));
    }

    public PNode makeCompare(Token t, PNode left, java.util.List<CmpOpType> ops, List<PNode> comparators) {
        return GrammarUtil.make(t, factory.createComparisonOps(left, ops, comparators));
    }

    public PNode makeFalse(Token t) {
        return GrammarUtil.make(t, factory.createBooleanLiteral(false));
    }

    public PNode makeTrue(Token t) {
        return GrammarUtil.make(t, factory.createBooleanLiteral(true));
    }

    public PNode makeNone(Token t) {
        return GrammarUtil.make(t, factory.createNoneLiteral());
    }

    public PNode makeUnaryOp(Token t, UnaryOpType op, PNode operand) {
        return GrammarUtil.make(t, factory.createUnaryOperation(op, operand));
    }

    public PNode makeIfExp(Token t, PNode test, PNode body, PNode orelse) {
        return GrammarUtil.make(t, factory.createIfExpNode(test, body, orelse));
    }

    public List<PNode> makeElse(List<?> elseSuite, PNode elif) {
        if (elseSuite != null) {
            return GrammarUtil.castStmts(elseSuite);
        } else if (elif == null) {
            return new ArrayList<>();
        }
        List<PNode> s = new ArrayList<>();
        s.add(GrammarUtil.castStmt(elif));
        return s;
    }

    public StatementNode makeIf(Token t, PNode test, List<PNode> body, List<PNode> orelse) {
        BlockNode thenPart = factory.createBlock(body);
        BlockNode elsePart = factory.createBlock(orelse);
        return (StatementNode) GrammarUtil.make(t, factory.createIf(factory.toBooleanCastNode(test), thenPart, elsePart));
    }

    public PNode makeGlobal(Token t, List<String> names, List<PNode> nameNodes) {
        for (String name : names) {
            ParserEnvironment.defGlobal(name);
            ParserEnvironment.localGlobals.add(name);
        }
        return GrammarUtil.make(t, PNode.EMPTY_NODE);
    }

    public PAlias makeAliasDotted(List<PNode> nameNodes, Token paramAsName) {
        PAlias retVal = null;
        PNode asname = var.makeNameNode(paramAsName);
        String snameNode = GrammarUtil.dottedNameListToString(nameNodes);
        retVal = new PAlias(nameNodes, snameNode, asname);
        if (asname != null) {
            retVal.setSlot(ParserEnvironment.def(asname.getText()));
        } else {
            retVal.setSlot(ParserEnvironment.def(snameNode));
        }
        return retVal;
    }

    public PAlias makeAliasImport(Token paramName, Token paramAsName) {
        PNode name = var.makeNameNode(paramName);
        PNode asname = (paramAsName != null) ? var.makeNameNode(paramAsName) : null;
        PAlias retVal = new PAlias(name, asname);
        if (paramAsName != null) {
            retVal.setSlot(ParserEnvironment.def(asname.getText()));
        } else {
            retVal.setSlot(ParserEnvironment.def(name.getText()));
        }
        return retVal;
    }

    public StatementNode makeImportFrom(Token t, String module, List<PNode> moduleNames, List<PAlias> aliases, Integer level) {
        FrameSlot[] slots = new FrameSlot[aliases.size()];
        String[] names = new String[aliases.size()];
        for (int i = 0; i < aliases.size(); i++) {
            slots[i] = aliases.get(i).getSlot();
            names[i] = aliases.get(i).getInternalName();
        }
        return (StatementNode) GrammarUtil.make(t, factory.createImport(slots, module, names));
    }

    public StatementNode makeImport(Token t, List<PAlias> aliases) {
        FrameSlot[] slots = new FrameSlot[aliases.size()];
        String[] names = new String[aliases.size()];
        for (int i = 0; i < aliases.size(); i++) {
            slots[i] = aliases.get(i).getSlot();
            names[i] = aliases.get(i).getInternalName();
        }
        return (StatementNode) GrammarUtil.make(t, factory.createImport(slots, null, names));
    }

    public PNode makeExpr(Token t, PNode value) {
        return GrammarUtil.make(t, value);
    }

    public PNode makeModule(Token t, List<?> stmts) {
        PNode retVal = factory.createModule(GrammarUtil.castStmts(stmts), ParserEnvironment.endScope());
        retVal.setToken(t);
        return retVal;
    }

    public int makeLevel(List<?> lev) {
        if (lev == null) {
            return 0;
        }
        return lev.size();
    }

    public List<PAlias> makeStarAlias(Token t) {
        List<PAlias> result = new ArrayList<>();
        result.add(new PAlias(t, "*", null));
        return result;
    }

    public List<PAlias> makeAliases(List<PAlias> atypes) {
        if (atypes == null) {
            return new ArrayList<>();
        }
        return atypes;
    }

    public List<PNode> makeBases(PNode etype) {
        List<PNode> result = new ArrayList<>();
        if (etype != null) {
            if (etype instanceof TupleLiteralNode) {
                return ((TupleLiteralNode) etype).getElts();
            }
            result.add(etype);
        }
        return result;
    }

// private String cantBeNone(Token t) {
// if (t == null || t.getText().equals("None")) {
// ErrorHandler.error("can't be None", t);
// }
// return t.getText();
// }
//
// private void cantBeNone(PNode e) {
// if (e.getText().equals("None")) {
// ErrorHandler.error("can't be None", e.getToken());
// }
// }

    public List<PNode> makeKeywords(List<?> args) {
        List<PNode> keywords = new ArrayList<>();
        PNode singleKeyword = null;
        if (args != null) {
            for (Object o : args) {
                List<?> e = (List<?>) o;
                Object k = e.get(0);
                Object v = e.get(1);
                GrammarUtil.checkAssign(GrammarUtil.castExpr(k));
                if (k instanceof PNode) {
                    singleKeyword = factory.createKeywordLiteral(GrammarUtil.castExpr(v), ((PNode) k).getText());
                    keywords.add(singleKeyword);
                } else {
                    ErrorHandler.error("keyword must be a name", ((PNode) k).getToken());
                }
            }
        }
        return keywords;
    }

    public PNode makeFloat(Token t) {
        return GrammarUtil.make(t, factory.createDoubleLiteral(Double.valueOf(t.getText())));
    }

    public PNode makeComplex(Token t) {
        String s = t.getText();
        s = s.substring(0, s.length() - 1);
        PComplex complex = new PComplex(0.0, Double.valueOf(s));
        return GrammarUtil.make(t, factory.createComplexLiteral(complex));
    }

    public PNode makeInt(Token t) {
        PNode retVal = null;
        long l;
        String s = t.getText();
        int ndigits = 0;
        int i = 0;

        int radix = 10;
        if (s.startsWith("0x") || s.startsWith("0X")) {
            radix = 16;
            s = s.substring(2, s.length());
        } else if (s.startsWith("0o") || s.startsWith("0O")) {
            radix = 8;
            s = s.substring(2, s.length());
        } else if (s.startsWith("0b") || s.startsWith("0B")) {
            radix = 2;
            s = s.substring(2, s.length());
        } else if (s.startsWith("0")) {
            radix = 8;
        }
        if (s.endsWith("L") || s.endsWith("l")) {
            s = s.substring(0, s.length() - 1);
            retVal = factory.createBigIntegerLiteral(new BigInteger(s, radix));
        } else {
            ndigits = s.length();
            i = 0;
            while (i < ndigits && s.charAt(i) == '0') {
                i++;
            }
            if ((ndigits - i) > 11) {
                retVal = factory.createBigIntegerLiteral(new BigInteger(s, radix));
            } else {
                l = Long.valueOf(s, radix).longValue();
                if (l > 0xffffffffL || (l > Integer.MAX_VALUE)) {
                    retVal = factory.createBigIntegerLiteral(new BigInteger(s, radix));
                } else {
                    retVal = factory.createIntegerLiteral((int) l);
                }
            }
        }
        return GrammarUtil.make(t, retVal);
    }

    public PNode negate(PNode t, PNode o) {
        return negate(t.getToken(), o);
    }

    public PNode negate(Token t, PNode o) {
        return GrammarUtil.make(t, makeUnaryOp(t, UnaryOpType.USub, o));
    }

    public PNode makePowerSpecific(PNode parent, Object object) {
        PNode newNode = var.recurseSetContext(parent, ContextType.Load);
        PNode retVal = GrammarUtil.replace(parent, newNode);
        PNode o = null;
        if (object instanceof PNode) {
            o = (PNode) object;
        }

        if (o instanceof CallBuiltInNode || o instanceof CallBuiltInWithOneArgNoKeywordNode || o instanceof CallBuiltInWithTwoArgsNoKeywordNode || o instanceof CallNode ||
                        o instanceof CallWithOneArgumentNoKeywordNode || o instanceof CallWithTwoArgumentsNoKeywordNode || o instanceof SubscriptStoreNode) {
            retVal = o;
        } else if (o instanceof AttributeCallNode) {
            AttributeCallNode c = (AttributeCallNode) o;
            c = c.updatePrimary(((AttributeLoadNode) parent).getPrimary());
            newNode.setParent(c);
            retVal = GrammarUtil.replace(o, c);
        } else if (o instanceof SubscriptLoadNode) {
            SubscriptLoadNode c = (SubscriptLoadNode) o;
            c = (SubscriptLoadNode) factory.createSubscriptLoad(newNode, c.getSlice());
            newNode.setParent(c);
            retVal = GrammarUtil.replace(o, c);
        } else if (o instanceof AttributeLoadNode) {
            AttributeLoadNode c = (AttributeLoadNode) o;
            c = (AttributeLoadNode) factory.createAttributeRef(newNode, c.getName());
            newNode.setParent(c);
            retVal = GrammarUtil.replace(o, c);
        }
        return retVal;
    }

    public List<CmpOpType> makeCmpOps(List<?> cmps) {
        List<CmpOpType> result = new ArrayList<>();
        if (cmps != null) {
            for (Object o : cmps) {
                result.add((CmpOpType) o);
            }
        }
        return result;
    }

    public PNode makeBoolOp(Token t, PNode left, BoolOpType op, List<?> right) {
        return GrammarUtil.make(t, factory.createBooleanOperations(GrammarUtil.castExpr(left), op, GrammarUtil.castExprs(right)));
    }

    public PNode makeBinOp(Token t, PNode pleft, OperationType op, List<?> rights) {
        PNode right = GrammarUtil.castExpr(rights.get(0));
        PNode left = GrammarUtil.castExpr(pleft);
        PNode current = GrammarUtil.make(t, factory.createBinaryOperation(op, left, right));
        for (int i = 1; i < rights.size(); i++) {
            right = GrammarUtil.castExpr(rights.get(i));
            current = GrammarUtil.make(t, factory.createBinaryOperation(op, current, right));
        }
        return current;
    }

    public PNode makeBinOp(Token t, PNode pleft, List<?> ops, List<?> rights, List<?> toks) {
        PNode left = GrammarUtil.castExpr(pleft);
        PNode right = GrammarUtil.castExpr(rights.get(0));
        OperationType op = (OperationType) ops.get(0);
        PNode current = GrammarUtil.make(t, factory.createBinaryOperation(op, left, right));
        for (int i = 1; i < rights.size(); i++) {
            right = GrammarUtil.castExpr(rights.get(i));
            op = (OperationType) ops.get(i);
            current = GrammarUtil.make(t, factory.createBinaryOperation(op, current, right));
        }
        return current;
    }

    public void checkDelete(PNode e) {

        // else if (e instanceof Num)
        // {
        // ErrorHandler.error("can't delete number", e);
        // }
        // else if (e instanceof Str)
        // {
        // ErrorHandler.error("can't delete string", e);
        // }

        if (e instanceof CallNode) {
            ErrorHandler.error("can't delete function call", e.getToken());
        } else if (e instanceof TupleLiteralNode) {
            // XXX: performance problem? Any way to do this better?
            List<PNode> elts = ((TupleLiteralNode) e).getElts();
            if (elts.size() == 0) {
                ErrorHandler.error("can't delete ()", e.getToken());
            }
            for (int i = 0; i < elts.size(); i++) {
                checkDelete(elts.get(i));
            }
        } else if (e instanceof ListLiteralNode) {
            // XXX: performance problem? Any way to do this better?
            List<PNode> elts = ((ListLiteralNode) e).getElts();
            for (int i = 0; i < elts.size(); i++) {
                checkDelete(elts.get(i));
            }
        }
    }

    public List<PNode> makeDeleteList(List<?> deletes) {
        List<PNode> exprs = GrammarUtil.castExprs(deletes);
        for (PNode e : exprs) {
            checkDelete(e);
        }
        return exprs;
    }

    public PNode makeExceptHandler(Token eXCEPT177, PNode castExpr, PNode castExpr2, List<PNode> castStmts) {
        return null;
    }
}
