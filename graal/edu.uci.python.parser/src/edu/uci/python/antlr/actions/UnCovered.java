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
// CheckStyle: stop system..print check
package edu.uci.python.antlr.actions;

import java.util.*;

import org.antlr.runtime.*;

import edu.uci.python.antlr.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.literals.*;
import edu.uci.python.nodes.statements.*;
import edu.uci.python.runtime.*;

public class UnCovered {

    public PNode makeEllipsis(Token t) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeEllipsis");
        }
        PNode retVal = null;

        // retVal = new Ellipsis(t);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public PNode makeSet(Token t, List<PNode> elts) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeSet");
        }
        PNode retVal = null;

        // retVal = new Set(t, elts);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public PNode makeRepr(Token t, PNode value) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeRepr");
        }

        PNode retVal = null;

        // retVal = new Repr(t, value);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makeExec(Token t, PNode body, PNode globals, PNode locals) { // TODO: No
// Translation!!

        if (Options.debug) {
            System.out.println("makeExec");
        }

        StatementNode retVal = null;

        // retVal = new Exec(t, body, globals, locals);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makeAssert(Token t, PNode test, PNode msg) // TODO: No Translation!!
    {
        if (Options.debug) {
            System.out.println("makeAssert");
        }

        StatementNode retVal = null;

        // retVal = new Assert(t, test, msg);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makeRaise(Token t, PNode type, PNode inst, PNode tback) { // TODO: No
// Translation!!

        if (Options.debug) {
            System.out.println("makeRaise");
        }
        StatementNode retVal = null;

        // retVal = new Raise(t, type, inst, tback);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makeContinue(Token t) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeContinue");
        }
        StatementNode retVal = null;

        // retVal = new Continue(t);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makePass(Token t) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makePass");
        }
        StatementNode retVal = null;

        // retVal = new Pass(t);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makeDelete(Token t, List<PNode> targets) // TODO: No Translation!!
    {
        if (Options.debug) {
            System.out.println("makeDelete");
        }

        StatementNode retVal = null;

        // retVal = new Delete(t, targets);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makeNonlocal(Token t, List<?> paramNames, List<?> paramNameNodes) {
        // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeNonlocal");
        }
        StatementNode retVal = null;
// List<String> names = makeNames(paramNames);
// List<PNode> nameNodes = makeNameNodes(paramNameNodes);
        // retVal = new Nonlocal(t, names, nameNodes, 0);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public PNode makeInteractive(Token t, List<PNode> body) // TODO: No Translation!!
    {
        if (Options.debug) {
            System.out.println("makeInteractive");
        }
        PNode retVal = null;

        // retVal = new Interactive(t, body);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public PNode makeExpression(Token t, PNode body) { // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeExpression");
        }
        PNode retVal = null;

        // retVal = new Expression(t, body);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public PNode makeLambda(Token t, ParametersNode a, PNode body) // TODO: No Translation!!
    {

        if (Options.debug) {
            System.out.println("makeLambda");
        }
        // PNode retVal = null;
        PNode retVal = null;

        ParametersNode args = a;
// if (args == null) {
// args = makeArguments(t, new ArrayList<PNode>(), null, null, new ArrayList<PNode>());
// }

        // beginScope();

        // retVal = new Lambda(t, args, body);

        // ParserEnvironment.endScope();

        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makeClassDef(Token t, Token name, PNode pbases, List<?> body, List<?> decoratorlist) { // TODO:
// No Translation!!

        if (Options.debug) {
            System.out.println("makeClassDef");
        }
        // PNode retVal = null;
        StatementNode retVal = null;
// PNode n = cantBeNoneName(name);
// List<PNode> bases = makeBases(castExpr(pbases));
// List<PNode> b = GrammarUtilities.castStmts(body);
// List<PNode> d = GrammarUtilities.castExprs(decoratorlist);

        // retVal = new ClassDef(t, n, bases, b, d);

        ParserEnvironment.endScope();
        ParserEnvironment.def(name.getText());

        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makeWith(Token t, PNode contextPNode, PNode optionalvars, List<StatementNode> body) { // TODO:
// No Translation!!

        if (Options.debug) {
            System.out.println("makeWith ctx");
        }
        StatementNode retVal = null;

        // retVal = new With(t, context_PNode, optional_vars, body);

        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makeWith(Token t, List<?> items, List<?> body) // TODO: No Translation!!
    {
        if (Options.debug) {
            System.out.println("makeWith");
        }
        int last = items.size() - 1;
        StatementNode result = null;
        for (int i = last; i >= 0; i--) {
            // With current = items.get(i);
            if (i != last) {
                body = new ArrayList<StatementNode>();
                // body.add(result);
            }
            // result = new With(current.getToken(),
            // current.getInternalContext_PNode(),
            // current.getInternalOptional_vars(), body);
        }

        uncoveredException("Ellipsis");
        return result;
    }

    public StatementNode makeTryExcept(Token t, List<?> body, List<?> handlers, List<?> orelse, List<?> finBody) {
        // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeTryExcept");
        }
        StatementNode retVal = null;
        List<PNode> b = GrammarUtil.castStmts(body);
        // List<excepthandler> e = handlers;
        List<PNode> o = GrammarUtil.castStmts(orelse);
        // StatementNode te = new TryExcept(t, b, e, o);
        if (finBody == null) {
            // return te;
        }
        List<PNode> f = GrammarUtil.castStmts(finBody);
        List<PNode> mainBody = new ArrayList<>();
        // mainBody.add(te);
        // return new TryFinally(t, mainBody, f);
        uncoveredException("Ellipsis");
        return retVal;
    }

    public StatementNode makeTryFinally(Token t, List<?> body, List<?> finBody) {
        // TODO: No Translation!!

        if (Options.debug) {
            System.out.println("makeTryFinally");
        }
        StatementNode retVal = null;
        List<PNode> b = GrammarUtil.castStmts(body);
        List<PNode> f = GrammarUtil.castStmts(finBody);
        // return new TryFinally(t, b, f);

        UnCovered.uncoveredException("Ellipsis");
        return retVal;
    }

    public static void uncoveredException(String msg) {
        throw new RuntimeException(msg + ": is not covered!");
    }
}
