/*
 [The 'BSD licence']
 Copyright (c) 2004 Terence Parr and Loring Craymer
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/** Python 2.3.3 Grammar
 *
 *  Terence Parr and Loring Craymer
 *  February 2004
 *
 *  Converted to ANTLR v3 November 2005 by Terence Parr.
 *
 *  This grammar was derived automatically from the Python 2.3.3
 *  parser grammar to get a syntactically correct ANTLR grammar
 *  for Python.  Then Terence hand tweaked it to be semantically
 *  correct; i.e., removed lookahead issues etc...  It is LL(1)
 *  except for the (sometimes optional) trailing commas and semi-colons.
 *  It needs two symbols of lookahead in this case.
 *
 *  Starting with Loring's preliminary lexer for Python, I modified it
 *  to do my version of the whole nasty INDENT/DEDENT issue just so I
 *  could understand the problem better.  This grammar requires
 *  PythonTokenStream.java to work.  Also I used some rules from the
 *  semi-formal grammar on the web for Python (automatically
 *  translated to ANTLR format by an ANTLR grammar, naturally <grin>).
 *  The lexical rules for python are particularly nasty and it took me
 *  a long time to get it 'right'; i.e., think about it in the proper
 *  way.  Resist changing the lexer unless you've used ANTLR a lot. ;)
 *
 *  I (Terence) tested this by running it on the jython-2.1/Lib
 *  directory of 40k lines of Python.
 *
 *  REQUIRES ANTLR v3
 *
 *
 *  Updated the original parser for Python 2.5 features. The parser has been
 *  altered to produce an AST - the AST work started from tne newcompiler
 *  grammar from Jim Baker.  The current parsing and compiling strategy looks
 *  like this:
 *
 *  Python source->Python.g->AST (org/python/parser/ast/*)->CodeCompiler(ASM)->.class
 */

grammar Truffle;
options {
    ASTLabelType=PNode;
    output=AST;
}

tokens {
    INDENT;
    DEDENT;
    TRAILBACKSLASH; //For dangling backslashes when partial parsing.
}

@header {
package edu.uci.python.antlr;

import org.antlr.runtime.CommonToken;

import edu.uci.python.datatypes.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.statements.*;
import edu.uci.python.types.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
}

@members {

    private GrammarActions actions = new GrammarActions();
    private UnCovered uncovered = new UnCovered();

    private String encoding;

    private boolean printFunction = true;
    private boolean unicodeLiterals = false;

    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow)
        throws RecognitionException {

        Object o = ErrorHandler.recoverFromMismatchedToken(this, input, ttype, follow);
        if (o != null) {
            return o;
        }
        return super.recoverFromMismatchedToken(input, ttype, follow);
    }

    public TruffleParser(TokenStream input, String encoding) {
        this(input);
        this.encoding = encoding;
    }

    @Override
    public void reportError(RecognitionException e) {
      // Update syntax error count and output error.
      super.reportError(e);
      ErrorHandler.reportError(this, e);
    }

    @Override
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        //Do nothing. We will handle error display elsewhere.
    }
}

@rulecatch {
catch (RecognitionException re) {
    reportError(re);
    ErrorHandler.recover(this, input,re);
    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
}
}

@lexer::header {
package edu.uci.python.antlr;
import edu.uci.python.*;
import org.antlr.runtime.tree.*;

//package org.python.ast;
}

@lexer::members {
/** Handles context-sensitive lexing of implicit line joining such as
 *  the case where newline is ignored in cases like this:
 *  a = [3,
 *       4]
 */
int implicitLineJoiningLevel = 0;
int startPos=-1;

//For use in partial parsing.
public boolean eofWhileNested = false;
public boolean partial = false;
public boolean single = false;

    /**
     *  Taken directly from antlr's Lexer.java -- needs to be re-integrated every time
     *  we upgrade from Antlr (need to consider a Lexer subclass, though the issue would
     *  remain).
     */
    public Token nextToken() {
        startPos = getCharPositionInLine();
        while (true) {
            state.token = null;
            state.channel = Token.DEFAULT_CHANNEL;
            state.tokenStartCharIndex = input.index();
            state.tokenStartCharPositionInLine = input.getCharPositionInLine();
            state.tokenStartLine = input.getLine();
            state.text = null;
            if ( input.LA(1)==CharStream.EOF ) {
                if (implicitLineJoiningLevel > 0) {
                    eofWhileNested = true;
                }
                return (new CommonTreeAdaptor()).createToken(Token.EOF,"End of file");
            }
            try {
                mTokens();
                if ( state.token==null ) {
                    emit();
                }
                else if ( state.token==Token.SKIP_TOKEN ) {
                    continue;
                }
                return state.token;
            } catch (NoViableAltException nva) {
                reportError(nva);
                ErrorHandler.recover(this, nva); // throw out current char and try again
            } catch (FailedPredicateException fp) {
                //XXX: added this for failed STRINGPART -- the FailedPredicateException
                //     hides a NoViableAltException.  This should be the only
                //     FailedPredicateException that gets thrown by the lexer.
                reportError(fp);
                ErrorHandler.recover(this, fp); // throw out current char and try again
            } catch (RecognitionException re) {
                reportError(re);
                // match() routine has already called recover()
            }
        }
    }
    @Override
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        //Do nothing. We will handle error display elsewhere.
    }

}

//START OF PARSER RULES

//single_input: NEWLINE | simple_stmt | compound_stmt NEWLINE
single_input
@init {
    PNode mtype = null;
}
@after {
    $single_input.tree = mtype;
}
    : NEWLINE* EOF
      {
        mtype = uncovered.makeInteractive($single_input.start, new ArrayList<PNode>());
      }
    | simple_stmt NEWLINE* EOF
      {
        mtype = uncovered.makeInteractive($single_input.start, GrammarUtilities.castStmts($simple_stmt.stypes));
      }
    | compound_stmt NEWLINE+ EOF
      {
        mtype = uncovered.makeInteractive($single_input.start, GrammarUtilities.castStmts($compound_stmt.tree));
      }
    ;
    //XXX: this block is duplicated in three places, how to extract?
    catch [RecognitionException re] {
        reportError(re);
        ErrorHandler.recover(this, input,re);
        PNode badNode = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
        retval.tree = ErrorHandler.errorMod(badNode.getToken());
    }

//file_input: (NEWLINE | stmt)* ENDMARKER
file_input
@init {
    PNode mtype = null;
    List stypes = new ArrayList();    
}
@after {
    if (!stypes.isEmpty()) {
        //The EOF token messes up the end offsets, so set them manually.
        //XXX: this may no longer be true now that PythonTokenSource is
        //     adjusting EOF offsets -- but needs testing before I remove
        //     this.
        PNode stop = (PNode)stypes.get(stypes.size() -1);
        mtype.setCharStopIndex(stop.getCharStopIndex());
        mtype.setTokenStopIndex(stop.getTokenStopIndex());
    }

    $file_input.tree = mtype;
}
    : (NEWLINE
      | stmt
      {
          if ($stmt.stypes != null) {
              stypes.addAll($stmt.stypes);
          }
      }
      )* EOF
         {
//             mtype = new Module($file_input.start, GrammarUtilities.castStmts(stypes));
             mtype = actions.makeModule($file_input.start,stypes);
         }
    ;
    //XXX: this block is duplicated in three places, how to extract?
    catch [RecognitionException re] {
        reportError(re);
        ErrorHandler.recover(this, input,re);
        PNode badNode = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
        retval.tree = ErrorHandler.errorMod(badNode.getToken());
    }

//eval_input: testlist NEWLINE* ENDMARKER
eval_input
@init {
    PNode mtype = null;
}
@after {
    $eval_input.tree = mtype;
}
    : LEADING_WS? (NEWLINE)* testlist[ContextType.Load] (NEWLINE)* EOF
      {
        mtype = uncovered.makeExpression($eval_input.start, GrammarUtilities.castExpr($testlist.tree));
      }
    ;
    //XXX: this block is duplicated in three places, how to extract?
    catch [RecognitionException re] {
        reportError(re);
        ErrorHandler.recover(this, input,re);
        PNode badNode = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
        retval.tree = ErrorHandler.errorMod(badNode.getToken());
    }


//not in CPython's Grammar file
dotted_attr
    returns [PNode etype]
    : n1=NAME
      ( (DOT n2+=NAME)+
        {
            $etype = actions.makeDottedAttr($n1, $n2);
        }
      |
        {
            $etype = actions.makeNameNode($n1);
        }
      )
    ;

//not in CPython's Grammar file
// This is used to allow PRINT as a NAME for the __future__ print_function.
name_or_print
    returns [Token tok]
    : NAME {
        $tok = $name_or_print.start;
    }
    | {printFunction}? => PRINT {
        $tok = $name_or_print.start;
    }
    ;

//not in CPython's Grammar file
//attr is here for Java  compatibility.  A Java foo.getIf() can be called from Jython as foo.if
//     so we need to support any keyword as an attribute.

attr
    : NAME
    | AND
    | AS
    | ASSERT
    | BREAK
    | CLASS
    | CONTINUE
    | DEF
    | DELETE
    | ELIF
    | EXCEPT
    //| EXEC
    | FINALLY
    | FROM
    | FOR
    | GLOBAL
    | IF
    | IMPORT
    | IN
    | IS
    | LAMBDA
    | NOT
    | OR
    | ORELSE
    | PASS
    | PRINT
    | RAISE
    | RETURN
    | TRY
    | WHILE
    | WITH
    | YIELD
    | NONE
    | TRUE
    | FALSE
    | NONLOCAL
    ;

//decorator: '@' dotted_name [ '(' [arglist] ')' ] NEWLINE
decorator
    returns [PNode etype]
@after {
   $decorator.tree = $etype;
}
    : AT dotted_attr
    ( LPAREN
      ( arglist
        {
            $etype = actions.makeCall($LPAREN, $dotted_attr.etype, $arglist.args, $arglist.keywords, $arglist.starargs, $arglist.kwargs);
        }
      |
        {
            $etype = actions.makeCall($LPAREN, $dotted_attr.etype);
        }
      )
      RPAREN
    |
      {
          $etype = $dotted_attr.etype;
      }
    ) NEWLINE
    ;

//decorators: decorator+
decorators
    returns [List etypes]
    : d+=decorator+
      {
          $etypes = $d;
      }
    ;

//funcdef: [decorators] 'def' NAME parameters ':' suite
funcdef
@init {
    StatementNode stype = null;
    ParserEnvironment.beginScope();
}

@after {
    $funcdef.tree = stype;
}
    : decorators? DEF name_or_print parameters COLON suite[false]
    {
        Token t = $DEF;
        if ($decorators.start != null) {
            t = $decorators.start;
        }
        stype = actions.makeFuncdef(t, $name_or_print.start, $parameters.args, $suite.stypes, $decorators.etypes);
    }
    ;

//parameters: '(' [varargslist] ')'
parameters
    returns [ParametersNode args]
    : LPAREN
      (varargslist
        {
              $args = $varargslist.args;
        }
      |
        {
            $args = actions.makeArguments($parameters.start, null, null, null, null);
//            $args = new arguments($parameters.start, new ArrayList<PNode>(), null, null, new ArrayList<PNode>());
        }
      )
      RPAREN
    ;

//not in CPython's Grammar file
defparameter
    [List defaults] returns [PNode etype]
@after {
   $defparameter.tree = $etype;
}
    : fpdef[ContextType.Param] (ASSIGN test[ContextType.Load])?
      {
          $etype = GrammarUtilities.castExpr($fpdef.tree);
          if ($ASSIGN != null) {
              defaults.add($test.tree);
          } else if (!defaults.isEmpty()) {
              throw new ParseException("non-default argument follows default argument");
          }
      }
    ;

//varargslist: ((fpdef ['=' test] ',')*
//              ('*' NAME [',' '**' NAME] | '**' NAME) |
//              fpdef ['=' test] (',' fpdef ['=' test])* [','])
varargslist
    returns [ParametersNode args]
@init {
    List defaults = new ArrayList();
}
    : d+=defparameter[defaults] (options {greedy=true;}:COMMA d+=defparameter[defaults])*
      (COMMA
          (STAR starargs=NAME (COMMA DOUBLESTAR kwargs=NAME)?
          | DOUBLESTAR kwargs=NAME
          )?
      )?
      {
          $args = actions.makeArgumentsType($varargslist.start, $d, $starargs, $kwargs, defaults);
      }
    | STAR starargs=NAME (COMMA DOUBLESTAR kwargs=NAME)?
      {
          $args = actions.makeArgumentsType($varargslist.start, $d, $starargs, $kwargs, defaults);
      }
    | DOUBLESTAR kwargs=NAME
      {
          $args = actions.makeArgumentsType($varargslist.start, $d, null, $kwargs, defaults);
      }
    ;

//fpdef: NAME | '(' fplist ')'
fpdef[ContextType ctype]
@init {
    PNode etype = null;
}
@after {
    if (etype != null) {
        $fpdef.tree = etype;
    }
    GrammarUtilities.checkAssign(GrammarUtilities.castExpr($fpdef.tree));
}
    : NAME
      {
          etype = actions.makeName($NAME, $NAME.text, ctype);
      }
    | (LPAREN fpdef[null] COMMA) => LPAREN fplist RPAREN
      {
          etype = actions.makeTuple($fplist.start, GrammarUtilities.castExprs($fplist.etypes), ContextType.Store);
      }
    | LPAREN! fplist RPAREN!
    ;

//fplist: fpdef (',' fpdef)* [',']
fplist
    returns [List etypes]
    : f+=fpdef[ContextType.Store]
      (options {greedy=true;}:COMMA f+=fpdef[ContextType.Store])* (COMMA)?
      {
          $etypes = $f;
      }
    ;

//stmt: simple_stmt | compound_stmt
stmt
    returns [List stypes]
    : simple_stmt
      {
          $stypes = $simple_stmt.stypes;
      }
    | compound_stmt
      {
          $stypes = new ArrayList();
          $stypes.add($compound_stmt.tree);
      }
    ;

//simple_stmt: small_stmt (';' small_stmt)* [';'] NEWLINE
simple_stmt
    returns [List stypes]
    : s+=small_stmt (options {greedy=true;}:SEMI s+=small_stmt)* (SEMI)? NEWLINE
      {
          $stypes = $s;
      }
    ;

//small_stmt: (expr_stmt | print_stmt  | del_stmt | pass_stmt | flow_stmt |
//             import_stmt | global_stmt | exec_stmt | assert_stmt)
small_stmt : expr_stmt
           | del_stmt
           | pass_stmt
           | flow_stmt
           | import_stmt
           | global_stmt
           //| exec_stmt
           | assert_stmt
           | {!printFunction}? => print_stmt
           | nonlocal_stmt
           ;

//nonlocal_stmt: 'nonlocal' NAME (',' NAME)*
nonlocal_stmt
@init {
    StatementNode stype = null;
}
@after {
   $nonlocal_stmt.tree = stype;
}
    : NONLOCAL n+=NAME (options {k=2;}: COMMA n+=NAME)*
      {
         stype = uncovered.makeNonlocal($NONLOCAL, $n, $n);
      }
    ;

//expr_stmt: testlist (augassign (yield_expr|testlist) |
//                     ('=' (yield_expr|testlist))*)
expr_stmt
@init {
    PNode stype = null;
}
@after {
    if (stype != null) {
        $expr_stmt.tree = stype;
    }
}
    : ((testlist[null] augassign) => lhs=testlist[ContextType.AugStore]
        ( (aay=augassign y1=yield_expr
           {
               GrammarUtilities.checkAugAssign(GrammarUtilities.castExpr($lhs.tree));
               stype = actions.makeAugAssign($lhs.tree, GrammarUtilities.castExpr($lhs.tree), $aay.op, GrammarUtilities.castExpr($y1.etype));
           }
          )
        | (aat=augassign rhs=testlist[ContextType.Load]
           {
               GrammarUtilities.checkAugAssign(GrammarUtilities.castExpr($lhs.tree));
               stype = actions.makeAugAssign($lhs.tree, GrammarUtilities.castExpr($lhs.tree), $aat.op, GrammarUtilities.castExpr($rhs.tree));
           }
          )
        )
    | (testlist[null] ASSIGN) => lhs=testlist[ContextType.Store]
        (
        | ((at=ASSIGN t+=testlist[ContextType.Load])+
            {

                List<PNode> targetslist = actions.makeAssignTargets(GrammarUtilities.castExpr($lhs.tree), $t);
                PNode valueTN = actions.makeAssignValue($t);
                
                stype = actions.makeAssign($lhs.tree, targetslist, valueTN);
            }
          )
        | ((ay=ASSIGN y2+=yield_expr)+
            {
//                stype = new Assign($lhs.start, actions.makeAssignTargets(GrammarUtilities.castExpr($lhs.tree), $y2), actions.makeAssignValue($y2));
                stype = actions.makeAssign($lhs.start, actions.makeAssignTargets(GrammarUtilities.castExpr($lhs.tree), $y2), actions.makeAssignValue($y2));
            }
          )
        )
    | lhs=testlist[ContextType.Load]
      {
          stype = actions.makeExpr($lhs.start, GrammarUtilities.castExpr($lhs.tree));
      }
    )
    ;

//augassign: ('+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=' |
//            '<<=' | '>>=' | '**=' | '//=')
augassign
    returns [OperationType op]
    : PLUSEQUAL
        {
            $op = OperationType.Add;
        }
    | MINUSEQUAL
        {
            $op = OperationType.Sub;
        }
    | STAREQUAL
        {
            $op = OperationType.Mult;
        }
    | SLASHEQUAL
        {
            $op = OperationType.Div;
        }
    | PERCENTEQUAL
        {
            $op = OperationType.Mod;
        }
    | AMPEREQUAL
        {
            $op = OperationType.BitAnd;
        }
    | VBAREQUAL
        {
            $op = OperationType.BitOr;
        }
    | CIRCUMFLEXEQUAL
        {
            $op = OperationType.BitXor;
        }
    | LEFTSHIFTEQUAL
        {
            $op = OperationType.LShift;
        }
    | RIGHTSHIFTEQUAL
        {
            $op = OperationType.RShift;
        }
    | DOUBLESTAREQUAL
        {
            $op = OperationType.Pow;
        }
    | DOUBLESLASHEQUAL
        {
            $op = OperationType.FloorDiv;
        }
    ;

//print_stmt: 'print' ( [ test (',' test)* [','] ] |
//                      '>>' test [ (',' test)+ [','] ] )
print_stmt
@init {
    StatementNode stype = null;
}

@after {
    $print_stmt.tree = stype;
}
    : PRINT
      (t1=printlist
       {
           stype = actions.makePrint($PRINT, null, GrammarUtilities.castExprs($t1.elts), $t1.newline);
       }
      | RIGHTSHIFT t2=printlist2
       {
           stype = actions.makePrint($PRINT, GrammarUtilities.castExpr($t2.elts.get(0)), GrammarUtilities.castExprs($t2.elts, 1), $t2.newline);
       }
      |
       {
           stype = actions.makePrint($PRINT, null, new ArrayList<PNode>(), true);
       }
      )
      ;

//not in CPython's Grammar file
printlist
    returns [boolean newline, List elts]
    : (test[null] COMMA) =>
       t+=test[ContextType.Load] (options {k=2;}: COMMA t+=test[ContextType.Load])* (trailcomma=COMMA)?
       {
           $elts=$t;
           if ($trailcomma == null) {
               $newline = true;
           } else {
               $newline = false;
           }
       }
    | t+=test[ContextType.Load]
      {
          $elts=$t;
          $newline = true;
      }
    ;

//XXX: would be nice if printlist and printlist2 could be merged.
//not in CPython's Grammar file
printlist2
    returns [boolean newline, List elts]
    : (test[null] COMMA test[null]) =>
       t+=test[ContextType.Load] (options {k=2;}: COMMA t+=test[ContextType.Load])* (trailcomma=COMMA)?
       { $elts=$t;
           if ($trailcomma == null) {
               $newline = true;
           } else {
               $newline = false;
           }
       }
    | t+=test[ContextType.Load]
      {
          $elts=$t;
          $newline = true;
      }
    ;

//del_stmt: 'del' exprlist
del_stmt
@init {
    StatementNode stype = null;
}
@after {
   $del_stmt.tree = stype;
}
    : DELETE del_list
      {
          stype = uncovered.makeDelete($DELETE, $del_list.etypes);
      }
    ;

//pass_stmt: 'pass'
pass_stmt
@init {
    StatementNode stype = null;
}
@after {
   $pass_stmt.tree = stype;
}
    : PASS
      {
          stype = uncovered.makePass($PASS);
      }
    ;

//flow_stmt: break_stmt | continue_stmt | return_stmt | raise_stmt | yield_stmt
flow_stmt
    : break_stmt
    | continue_stmt
    | return_stmt
    | raise_stmt
    | yield_stmt
    ;

//break_stmt: 'break'
break_stmt
@init {
    StatementNode stype = null;
}
@after {
   $break_stmt.tree = stype;
}
    : BREAK
      {
          stype = actions.makeBreak($BREAK);
      }
    ;

//continue_stmt: 'continue'
continue_stmt
@init {
    StatementNode stype = null;
}
@after {
   $continue_stmt.tree = stype;
}
    : CONTINUE
      {
          if (!$suite.isEmpty() && $suite::continueIllegal) {
              ErrorHandler.error("'continue' not supported inside 'finally' clause", $continue_stmt.start);
          }
          stype = uncovered.makeContinue($CONTINUE);
      }
    ;

//return_stmt: 'return' [testlist]
return_stmt
@init {
    StatementNode stype = null;
}
@after {
   $return_stmt.tree = stype;
}
    : RETURN
      (testlist[ContextType.Load]
       {
           stype = actions.makeReturn($RETURN, GrammarUtilities.castExpr($testlist.tree));
       }
      |
       {
           stype = actions.makeReturn($RETURN, null);
       }
      )
      ;

//yield_stmt: yield_expr
yield_stmt
@init {
    PNode stype = null;
}
@after {
   $yield_stmt.tree = stype;
}
    : yield_expr
      {
        stype = actions.makeExpr($yield_expr.start, GrammarUtilities.castExpr($yield_expr.etype));
      }
    ;

//raise_stmt: 'raise' [test [',' test [',' test]]]
raise_stmt
@init {
    StatementNode stype = null;
}
@after {
   $raise_stmt.tree = stype;
}
    : RAISE (t1=test[ContextType.Load] (COMMA t2=test[ContextType.Load]
        (COMMA t3=test[ContextType.Load])?)?)?
      {
          stype = uncovered.makeRaise($RAISE, GrammarUtilities.castExpr($t1.tree), GrammarUtilities.castExpr($t2.tree), GrammarUtilities.castExpr($t3.tree));
      }
    ;

//import_stmt: import_name | import_from
import_stmt
    : import_name
    | import_from
    ;

//import_name: 'import' dotted_as_names
import_name
@init {
    StatementNode stype = null;
}
@after {
   $import_name.tree = stype;
}
    : IMPORT dotted_as_names
      {
          stype =actions.makeImport($IMPORT, $dotted_as_names.atypes);
      }
    ;

//import_from: ('from' ('.'* dotted_name | '.'+)
//              'import' ('*' | '(' import_as_names ')' | import_as_names))
import_from
@init {
    StatementNode stype = null;
//    ParserEnvironment.beginScope();
}
@after {
   $import_from.tree = stype;
}
    : FROM (d+=DOT* dotted_name | d+=DOT+) IMPORT
        (STAR
         {
             stype = actions.makeImportFrom($FROM, GrammarUtilities.makeFromText($d, $dotted_name.names),
                 actions.makeModuleNameNode($d, $dotted_name.names),
                 actions.makeStarAlias($STAR), actions.makeLevel($d));
         }
        | i1=import_as_names
         {
             String dottedText = $dotted_name.text;
             if (dottedText != null && dottedText.equals("__future__")) {
                 List aliases = $i1.atypes;
                 for(Object a: aliases) {
                     if (a != null) {
                         if (((PAlias)a).getInternalName().equals("print_function")) {
                             printFunction = true;
                         } else if (((PAlias)a).getInternalName().equals("unicode_literals")) {
                             unicodeLiterals = true;
                         }
                     }
                 }
             }
             stype = actions.makeImportFrom($FROM, GrammarUtilities.makeFromText($d, $dotted_name.names),
                 actions.makeModuleNameNode($d, $dotted_name.names),
                 actions.makeAliases($i1.atypes), actions.makeLevel($d));
         }
        | LPAREN i2=import_as_names COMMA? RPAREN
         {
             //XXX: this is almost a complete C&P of the code above - is there some way
             //     to factor it out?
             String dottedText = $dotted_name.text;
             if (dottedText != null && dottedText.equals("__future__")) {
                 List aliases = $i2.atypes;
                 for(Object a: aliases) {
                     if (a != null) {
                         if (((PAlias)a).getInternalName().equals("print_function")) {
                             printFunction = true;
                         } else if (((PAlias)a).getInternalName().equals("unicode_literals")) {
                             unicodeLiterals = true;
                         }
                     }
                 }
             }
             stype = actions.makeImportFrom($FROM, GrammarUtilities.makeFromText($d, $dotted_name.names),
                 actions.makeModuleNameNode($d, $dotted_name.names),
                 actions.makeAliases($i2.atypes), actions.makeLevel($d));
         }
        )
    ;

//import_as_names: import_as_name (',' import_as_name)* [',']
import_as_names
    returns [List atypes]
    : n+=import_as_name (COMMA! n+=import_as_name)*
    {
        $atypes = $n;
    }
    ;

//import_as_name: NAME [('as' | NAME) NAME]
import_as_name
    returns [PNode atype]
@after {
    $import_as_name.tree = $atype;
}
    : name=NAME (AS asname=NAME)?
    {
        $atype = actions.makeAliasImport($name, $asname);
    }
    ;

//XXX: when does CPython Grammar match "dotted_name NAME NAME"?
//dotted_as_name: dotted_name [('as' | NAME) NAME]
dotted_as_name
    returns [PAlias atype]
@after {
    $dotted_as_name.tree = $atype;
}
    : dotted_name (AS asname=NAME)?
    {
        $atype = actions.makeAliasDotted($dotted_name.names, $asname);
    }
    ;

//dotted_as_names: dotted_as_name (',' dotted_as_name)*
dotted_as_names
    returns [List atypes]
    : d+=dotted_as_name (COMMA! d+=dotted_as_name)*
    {
        $atypes = $d;
    }
    ;

//dotted_name: NAME ('.' NAME)*
dotted_name
    returns [List<PNode> names]
    : NAME (DOT dn+=attr)*
    {
        $names = actions.makeDottedName($NAME, $dn);
    }
    ;

//global_stmt: 'global' NAME (',' NAME)*
global_stmt
@init {
    PNode stype = null;
}
@after {
   $global_stmt.tree = stype;
}
    : GLOBAL n+=NAME (COMMA n+=NAME)*
      {
          stype = actions.makeGlobal($GLOBAL, actions.makeNames($n), actions.makeNameNodes($n));
      }
    ;

//exec_stmt: 'exec' expr ['in' test [',' test]]
exec_stmt
@init {
    StatementNode stype = null;
}
@after {
   $exec_stmt.tree = stype;
}
    : EXEC expr[ContextType.Load] (IN t1=test[ContextType.Load] (COMMA t2=test[ContextType.Load])?)?
      {
         stype = uncovered.makeExec($EXEC, GrammarUtilities.castExpr($expr.tree), GrammarUtilities.castExpr($t1.tree), GrammarUtilities.castExpr($t2.tree));
      }
    ;

//assert_stmt: 'assert' test [',' test]
assert_stmt
@init {
    StatementNode stype = null;
}
@after {
   $assert_stmt.tree = stype;
}
    : ASSERT t1=test[ContextType.Load] (COMMA t2=test[ContextType.Load])?
      {
          stype = uncovered.makeAssert($ASSERT, GrammarUtilities.castExpr($t1.tree), GrammarUtilities.castExpr($t2.tree));
      }
    ;

//compound_stmt: if_stmt | while_stmt | for_stmt | try_stmt | funcdef | classdef
compound_stmt
    : if_stmt
    | while_stmt
    | for_stmt
    | try_stmt
    | with_stmt
    | (decorators? DEF) => funcdef
    | classdef
    ;

//if_stmt: 'if' test ':' suite ('elif' test ':' suite)* ['else' ':' suite]
if_stmt
@init {
    StatementNode stype = null;
}
@after {
   $if_stmt.tree = stype;
}
    : IF test[ContextType.Load] COLON ifsuite=suite[false] elif_clause?
      {
          stype = actions.makeIf($IF, GrammarUtilities.castExpr($test.tree), GrammarUtilities.castStmts($ifsuite.stypes),
              actions.makeElse($elif_clause.stypes, $elif_clause.tree));
      }
    ;

//not in CPython's Grammar file
elif_clause
    returns [List stypes]
@init {
    StatementNode stype = null;
}
@after {
   if (stype != null) {
       $elif_clause.tree = stype;
   }
}
    : else_clause
      {
          $stypes = $else_clause.stypes;
      }
    | ELIF test[ContextType.Load] COLON suite[false]
      (e2=elif_clause
       {
           stype = actions.makeIf($test.start, GrammarUtilities.castExpr($test.tree), GrammarUtilities.castStmts($suite.stypes), actions.makeElse($e2.stypes, $e2.tree));
       }
      |
       {
           stype = actions.makeIf($test.start, GrammarUtilities.castExpr($test.tree), GrammarUtilities.castStmts($suite.stypes), new ArrayList<PNode>());
       }
      )
    ;

//not in CPython's Grammar file
else_clause
    returns [List stypes]
    : ORELSE COLON elsesuite=suite[false]
      {
          $stypes = $suite.stypes;
      }
    ;

//while_stmt: 'while' test ':' suite ['else' ':' suite]
while_stmt
@init {
    StatementNode stype = null;
//    actions.beginLoopLevel();
}
@after {
   $while_stmt.tree = stype;
}
    : WHILE test[ContextType.Load] COLON s1=suite[false] (ORELSE COLON s2=suite[false])?
      {
          stype = actions.makeWhile($WHILE, GrammarUtilities.castExpr($test.tree), $s1.stypes, $s2.stypes);
      }
    ;

//for_stmt: 'for' exprlist 'in' testlist ':' suite ['else' ':' suite]
for_stmt
@init {
    StatementNode stype = null;
//    actions.beginLoopLevel();
}
@after {
   $for_stmt.tree = stype;
}
    : FOR exprlist[ContextType.Store] IN testlist[ContextType.Load] COLON s1=suite[false]
        (ORELSE COLON s2=suite[false])?
      {
          stype = actions.makeFor($FOR, $exprlist.etype, GrammarUtilities.castExpr($testlist.tree), $s1.stypes, $s2.stypes);
      }
    ;

//try_stmt: ('try' ':' suite
//           ((except_clause ':' suite)+
//           ['else' ':' suite]
//           ['finally' ':' suite] |
//           'finally' ':' suite))
try_stmt
@init {
    StatementNode stype = null;
}
@after {
   $try_stmt.tree = stype;
}
    : TRY COLON trysuite=suite[!$suite.isEmpty() && $suite::continueIllegal]
      ( e+=except_clause+ (ORELSE COLON elsesuite=suite[!$suite.isEmpty() && $suite::continueIllegal])? (FINALLY COLON finalsuite=suite[true])?
        {
            stype = uncovered.makeTryExcept($TRY, $trysuite.stypes, $e, $elsesuite.stypes, $finalsuite.stypes);
        }
      | FINALLY COLON finalsuite=suite[true]
        {
            stype = uncovered.makeTryFinally($TRY, $trysuite.stypes, $finalsuite.stypes);
        }
      )
      ;

//with_stmt: 'with' with_item (',' with_item)*  ':' suite
with_stmt
@init {
    StatementNode stype = null;
}
@after {
   $with_stmt.tree = stype;
}
    : WITH w+=with_item (options {greedy=true;}:COMMA w+=with_item)* COLON suite[false]
      {
          stype = uncovered.makeWith($WITH, $w, $suite.stypes);
      }
    ;

//with_item: test ['as' expr]
with_item
@init {
    StatementNode stype = null;
}
@after {
   $with_item.tree = stype;
}
    : test[ContextType.Load] (AS expr[ContextType.Store])?
      {
          PNode item = GrammarUtilities.castExpr($test.tree);
          PNode var = null;
          if ($expr.start != null) {
              var = GrammarUtilities.castExpr($expr.tree);
              GrammarUtilities.checkAssign(var);
          }
          stype = uncovered.makeWith($test.start, item, var, null);
      }
    ;

//except_clause: 'except' [test [('as' | ',') test]]
except_clause
@init {
    PNode extype = null;
}
@after {
   $except_clause.tree = extype;
}
    : EXCEPT (t1=test[ContextType.Load] ((COMMA | AS) t2=test[ContextType.Store])?)? COLON suite[!$suite.isEmpty() && $suite::continueIllegal]
      {
          extype = actions.makeExceptHandler($EXCEPT, GrammarUtilities.castExpr($t1.tree), GrammarUtilities.castExpr($t2.tree),
              GrammarUtilities.castStmts($suite.stypes));
      }
    ;

//suite: simple_stmt | NEWLINE INDENT stmt+ DEDENT
suite
    [boolean fromFinally] returns [List stypes]
scope {
    boolean continueIllegal;
}
@init {
    if ($suite::continueIllegal || fromFinally) {
        $suite::continueIllegal = true;
    } else {
        $suite::continueIllegal = false;
    }
    $stypes = new ArrayList();
}
    : simple_stmt
      {
          $stypes = $simple_stmt.stypes;
      }
    | NEWLINE INDENT
      (stmt
       {
           if ($stmt.stypes != null) {
               $stypes.addAll($stmt.stypes);
           }
       }
      )+ DEDENT
    ;

//test: or_test ['if' or_test 'else' test] | lambdef
test[ContextType ctype]
@init {
    PNode etype = null;
}
@after {
   if (etype != null) {
       $test.tree = etype;
   }
}
    :o1=or_test[ctype]
      ( (IF or_test[null] ORELSE) => IF o2=or_test[ctype] ORELSE e=test[ContextType.Load]
         {
             etype = actions.makeIfExp($o1.start, GrammarUtilities.castExpr($o2.tree), GrammarUtilities.castExpr($o1.tree), GrammarUtilities.castExpr($e.tree));
         }
      |
     -> or_test
      )
    | lambdef
    ;

//or_test: and_test ('or' and_test)*
or_test
    [ContextType ctype] returns [Token leftTok]
@after {
    if ($or != null) {
        Token tok = $left.start;
        if ($left.leftTok != null) {
            tok = $left.leftTok;
        }
        $or_test.tree = actions.makeBoolOp(tok, $left.tree, BoolOpType.Or, $right);
    }
}
    : left=and_test[ctype]
        ( (or=OR right+=and_test[ctype]
          )+
        |
       -> $left
        )
    ;

//and_test: not_test ('and' not_test)*
and_test
    [ContextType ctype] returns [Token leftTok]
@after {
    if ($and != null) {
        Token tok = $left.start;
        if ($left.leftTok != null) {
            tok = $left.leftTok;
        }
        $and_test.tree = actions.makeBoolOp(tok, $left.tree, BoolOpType.And, $right);
    }
}
    : left=not_test[ctype]
        ( (and=AND right+=not_test[ctype]
          )+
        |
       -> $left
        )
    ;

//not_test: 'not' not_test | comparison
not_test
    [ContextType ctype] returns [Token leftTok]
@init {
    PNode etype = null;
}
@after {
   if (etype != null) {
       $not_test.tree = etype;
   }
}
    : NOT nt=not_test[ctype]
      {
          etype = actions.makeUnaryOp($NOT, UnaryOpType.Not, GrammarUtilities.castExpr($nt.tree));
      }
    | comparison[ctype]
      {
          $leftTok = $comparison.leftTok;
      }
    ;

//comparison: expr (comp_op expr)*
comparison
    [ContextType ctype] returns [Token leftTok]
@init {
    List cmps = new ArrayList();
}
@after {
    $leftTok = $left.leftTok;
    if (!cmps.isEmpty()) {
        $comparison.tree = actions.makeCompare($left.start, GrammarUtilities.castExpr($left.tree), actions.makeCmpOps(cmps),
            GrammarUtilities.castExprs($right));
    }
}
    : left=expr[ctype]
       ( ( comp_op right+=expr[ctype]
           {
               cmps.add($comp_op.op);
           }
         )+
       |
      -> $left
       )
    ;

//comp_op: '<'|'>'|'=='|'>='|'<='|'<>'|'!='|'in'|'not' 'in'|'is'|'is' 'not'
comp_op
    returns [CmpOpType op]
    : LESS
      {
          $op = CmpOpType.Lt;
      }
    | GREATER
      {
          $op = CmpOpType.Gt;
      }
    | EQUAL
      {
          $op = CmpOpType.Eq;
      }
    | GREATEREQUAL
      {
          $op = CmpOpType.GtE;
      }
    | LESSEQUAL
      {
          $op = CmpOpType.LtE;
      }
    //| ALT_NOTEQUAL
    //  {
    //      $op = CmpOpType.NotEq;
    //  }
    | NOTEQUAL
      {
          $op = CmpOpType.NotEq;
      }
    | IN
      {
          $op = CmpOpType.In;
      }
    | NOT IN
      {
          $op = CmpOpType.NotIn;
      }
    | IS
      {
          $op = CmpOpType.Is;
      }
    | IS NOT
      {
          $op = CmpOpType.IsNot;
      }
    ;

//expr: xor_expr ('|' xor_expr)*
expr
    [ContextType ect] returns [Token leftTok]
scope {
    ContextType ctype;
}
@init {
    $expr::ctype = ect;
}
@after {
    $leftTok = $left.lparen;
    if ($op != null) {
        Token tok = $left.start;
        if ($left.lparen != null) {
            tok = $left.lparen;
        }
        $expr.tree = actions.makeBinOp(tok, $left.tree, OperationType.BitOr, $right);
    }
}
    : left=xor_expr
        ( (op=VBAR right+=xor_expr
          )+
        |
       -> $left
        )
    ;


//xor_expr: and_expr ('^' and_expr)*
xor_expr
    returns [Token lparen = null]
@after {
    if ($op != null) {
        Token tok = $left.start;
        if ($left.lparen != null) {
            tok = $left.lparen;
        }
        $xor_expr.tree = actions.makeBinOp(tok, $left.tree, OperationType.BitXor, $right);
    }
    $lparen = $left.lparen;
}
    : left=and_expr
        ( (op=CIRCUMFLEX right+=and_expr
          )+
        |
       -> $left
        )
    ;

//and_expr: shift_expr ('&' shift_expr)*
and_expr
    returns [Token lparen = null]
@after {
    if ($op != null) {
        Token tok = $left.start;
        if ($left.lparen != null) {
            tok = $left.lparen;
        }
        $and_expr.tree = actions.makeBinOp(tok, $left.tree, OperationType.BitAnd, $right);
    }
    $lparen = $left.lparen;
}
    : left=shift_expr
        ( (op=AMPER right+=shift_expr
          )+
        |
       -> $left
        )
    ;

//shift_expr: arith_expr (('<<'|'>>') arith_expr)*
shift_expr
    returns [Token lparen = null]
@init {
    List ops = new ArrayList();
    List toks = new ArrayList();
}
@after {
    if (!ops.isEmpty()) {
        Token tok = $left.start;
        if ($left.lparen != null) {
            tok = $left.lparen;
        }
        $shift_expr.tree = actions.makeBinOp(tok, $left.tree, ops, $right, toks);
    }
    $lparen = $left.lparen;
}
    : left=arith_expr
        ( ( shift_op right+=arith_expr
            {
                ops.add($shift_op.op);
                toks.add($shift_op.start);
            }
          )+
        |
       -> $left
        )
    ;

shift_op
    returns [OperationType op]
    : LEFTSHIFT
      {
          $op = OperationType.LShift;
      }
    | RIGHTSHIFT
      {
          $op = OperationType.RShift;
      }
    ;

//arith_expr: term (('+'|'-') term)*
arith_expr
    returns [Token lparen = null]
@init {
    List ops = new ArrayList();
    List toks = new ArrayList();
}
@after {
    if (!ops.isEmpty()) {
        Token tok = $left.start;
        if ($left.lparen != null) {
            tok = $left.lparen;
        }
        $arith_expr.tree = actions.makeBinOp(tok, $left.tree, ops, $right, toks);
    }
    $lparen = $left.lparen;
}
    : left=term
        ( (arith_op right+=term
           {
               ops.add($arith_op.op);
               toks.add($arith_op.start);
           }
          )+
        |
       -> $left
        )
    ;
    // This only happens when Antlr is allowed to do error recovery (for example if ListErrorHandler
    // is used.  It is at least possible that this is a bug in Antlr itself, so this needs further
    // investigation.  To reproduce, set ErrorHandler to ListErrorHandler and try to parse "[".
    catch [RewriteCardinalityException rce] {
        PNode badNode = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), null);
        retval.tree = badNode;
        ErrorHandler.error("Internal Parser Error", badNode.getToken());
    }

arith_op
    returns [OperationType op]
    : PLUS
      {
          $op = OperationType.Add;
      }
    | MINUS
      {
          $op = OperationType.Sub;
      }
    ;

//term: factor (('*'|'/'|'%'|'//') factor)*
term
    returns [Token lparen = null]
@init {
    List ops = new ArrayList();
    List toks = new ArrayList();
}
@after {
    $lparen = $left.lparen;
    if (!ops.isEmpty()) {
        Token tok = $left.start;
        if ($left.lparen != null) {
            tok = $left.lparen;
        }
        $term.tree = actions.makeBinOp(tok, $left.tree, ops, $right, toks);
    }
}
    : left=factor
        ( (term_op right+=factor
          {
              ops.add($term_op.op);
              toks.add($term_op.start);
          }
          )+
        |
       -> $left
        )
    ;

term_op
    returns [OperationType op]
    : STAR
      {
          $op = OperationType.Mult;
      }
    | SLASH
      {
          $op = OperationType.Div;
      }
    | PERCENT
      {
          $op = OperationType.Mod;
      }
    | DOUBLESLASH
      {
          $op = OperationType.FloorDiv;
      }
    ;

//factor: ('+'|'-'|'~') factor | power
factor
    returns [PNode etype, Token lparen = null]
@after {
    $factor.tree = $etype;
}
    : PLUS p=factor
      {
            $etype = actions.makeUnaryOp($PLUS, UnaryOpType.UAdd, $p.etype);
      }
    | MINUS m=factor
      {
	          $etype = actions.negate($MINUS, $m.etype);
      }
    | TILDE t=factor
      {
	          $etype = actions.makeUnaryOp($TILDE, UnaryOpType.Invert, $t.etype);
      }
    | power
      {
          $etype = GrammarUtilities.castExpr($power.tree);
          $lparen = $power.lparen;
      }
    ;

//power: atom trailer* ['**' factor]
power
    returns [PNode etype, Token lparen = null]
@after {
    $power.tree = $etype;
}
    : atom (t+=trailer[$atom.start, $atom.tree])* (options {greedy=true;}:d=DOUBLESTAR factor)?
      {
          $lparen = $atom.lparen;
          //XXX: This could be better.
          $etype = GrammarUtilities.castExpr($atom.tree);
          if ($t != null) {
              for(Object o : $t) {
                  $etype = actions.makePowerSpecific($etype, o);
              }
          }
          if ($d != null) {
              List right = new ArrayList();
              right.add($factor.tree);
              $etype = actions.makeBinOp($atom.start, $etype, OperationType.Pow, right);
          }
      }
    ;

//atom: ('(' [yield_expr|testlist_gexp] ')' |
//       '[' [listmaker] ']' |
//       '{' [dictorsetmaker] '}' |
//       '`' testlist1 '`' |
//       NAME | NUMBER | STRING+)
atom
    returns [Token lparen = null]
@init {
    PNode etype = null;
}
@after {
   if (etype != null) {
       $atom.tree = etype;
   }
}
    : LPAREN
      {
          $lparen = $LPAREN;
      }
      ( yield_expr
        {
            etype = $yield_expr.etype;
        }
      | testlist_gexp
     -> testlist_gexp
      |
        {
            etype = actions.makeTuple($LPAREN, new ArrayList<PNode>(), $expr::ctype);
        }
      )
      RPAREN
    | LBRACK
      (listmaker[$LBRACK]
     -> listmaker
      |
       {
           etype = actions.makeList($LBRACK, new ArrayList<PNode>(), $expr::ctype);
       }
      )
      RBRACK
    | LCURLY
      (dictorsetmaker[$LCURLY]
     -> dictorsetmaker
      |
       {
           etype = actions.makeDict($LCURLY, new ArrayList<PNode>(), new ArrayList<PNode>());
       }
      )
       RCURLY
     | lb=BACKQUOTE testlist[ContextType.Load] rb=BACKQUOTE
       {
           etype = uncovered.makeRepr($lb, GrammarUtilities.castExpr($testlist.tree));
       }
     | name_or_print
       {
           etype = actions.makeName($name_or_print.start, $name_or_print.text, $expr::ctype);
     }
     | NONE 
       {
       	   etype = actions.makeNone($NONE);
       }
     | TRUE 
       {
       	   etype = actions.makeTrue($TRUE);
       }
     | FALSE 
       {
       	   etype = actions.makeFalse($FALSE);
       }
     | INT
       {
//	         etype = new Num($INT, actions.makeInt($INT));
           etype = actions.makeInt($INT);
           etype.setToken($INT);
       }
     //| LONGINT
     //  {
	 //        etype = new Num($LONGINT, actions.makeInt($LONGINT));
     //  }
     | FLOAT
       {
//           etype = new Num($FLOAT, actions.makeFloat($FLOAT));
           etype = actions.makeFloat($FLOAT);
       }
     | COMPLEX
       {
//            etype = new Num($COMPLEX, actions.makeComplex($COMPLEX));
            etype = actions.makeComplex($COMPLEX);
       }
     | (S+=STRING)+
       {
           etype = actions.makeStr(GrammarUtilities.extractStringToken($S), GrammarUtilities.extractStrings($S, encoding, unicodeLiterals));
       }
     ;

//listmaker: test ( list_for | (',' test)* [','] )
listmaker[Token lbrack]
@init {
    List gens = new ArrayList();
    PNode etype = null;
}
@after {
   $listmaker.tree = etype;
}
    : t+=test[$expr::ctype]
        (list_for[gens]
         {
             Collections.reverse(gens);
             List<PComprehension> c = gens;
             etype = actions.makeListComp($listmaker.start, GrammarUtilities.castExpr($t.get(0)), c);
         }
        | (options {greedy=true;}:COMMA t+=test[$expr::ctype])*
           {
               etype = actions.makeList($lbrack, GrammarUtilities.castExprs($t), $expr::ctype);
           }
        ) (COMMA)?
    ;

//testlist_gexp: test ( comp_for | (',' test)* [','] )
testlist_gexp
@init {
    PNode etype = null;
    List gens = new ArrayList();
    
}
@after {
    if (etype != null) {
        $testlist_gexp.tree = etype;
    }
}
    : t+=test[$expr::ctype]
        ( (options {k=2;}: c1=COMMA t+=test[$expr::ctype])* (c2=COMMA)?
         { $c1 != null || $c2 != null }? 
           {
               etype = actions.makeTuple($testlist_gexp.start, GrammarUtilities.castExprs($t), $expr::ctype);
           }
        | -> test
        | ({ParserEnvironment.beginScope();} comp_for[gens]
           {
               Collections.reverse(gens);
               List<PComprehension> c = gens;
               PNode e = GrammarUtilities.castExpr($t.get(0));
//               if (e instanceof Context) {
//                   ((Context)e).setContext(ContextType.Load);
//               }
               etype = actions.makeGeneratorExp($testlist_gexp.start, e, c);
               ParserEnvironment.endScope();
           }
          )
        )
    ;

//lambdef: 'lambda' [varargslist] ':' test
lambdef
@init {
    PNode etype = null;
}
@after {
    $lambdef.tree = etype;
}
    : LAMBDA (varargslist)? COLON test[ContextType.Load]
      {
//          arguments a = $varargslist.args;
//          if (a == null) {
//              a = new arguments($LAMBDA, new ArrayList<PNode>(), null, null, new ArrayList<PNode>());
//          }
          etype = uncovered.makeLambda($LAMBDA, $varargslist.args, GrammarUtilities.castExpr($test.tree));
      }
    ;

//trailer: '(' [arglist] ')' | '[' subscriptlist ']' | '.' NAME
trailer [Token begin, PNode ptree]
@init {
    PNode etype = null;
}
@after {
    if (etype != null) {
        $trailer.tree = etype;
    }
}
    : LPAREN
      (arglist
       {
//           etype = new Call($begin, GrammarUtilities.castExpr($ptree), GrammarUtilities.castExprs($arglist.args), actions.makeKeywords($arglist.keywords), $arglist.starargs, $arglist.kwargs);
           PNode func = GrammarUtilities.castExpr($ptree);
           etype = actions.makeCall($begin, func, $arglist.args, $arglist.keywords, $arglist.starargs, $arglist.kwargs);
           
       }
      |
       {
//           etype = new Call($begin, GrammarUtilities.castExpr($ptree), new ArrayList<PNode>(), new ArrayList<keyword>(), null, null);
           etype = actions.makeCall($begin, GrammarUtilities.castExpr($ptree),  null, null,  null, null);
       }
      )
      RPAREN
    | LBRACK subscriptlist[$begin] RBRACK
      {
          etype = actions.makeSubscript($begin, GrammarUtilities.castExpr($ptree), GrammarUtilities.castSlice($subscriptlist.tree), $expr::ctype);
      }
    | DOT attr
      {
          PNode name = actions.makeName($attr.tree, $attr.text, ContextType.Load);
          etype = actions.makeAttribute($begin, GrammarUtilities.castExpr($ptree), name, $expr::ctype);
          $ptree = etype;
      }
    ;

//subscriptlist: subscript (',' subscript)* [',']
subscriptlist[Token begin]
@init {
    PNode sltype = null;
}
@after {
   $subscriptlist.tree = sltype;
}
    : sub+=subscript (options {greedy=true;}:c1=COMMA sub+=subscript)* (c2=COMMA)?
      {
          sltype = actions.makeSliceType($begin, $c1, $c2, $sub);
      }
    ;

//subscript: '.' '.' '.' | test | [test] ':' [test] [sliceop]
subscript
    returns [PNode sltype]
@after {
    $subscript.tree = $sltype;
}
    : d1=DOT DOT DOT
      {
          $sltype = uncovered.makeEllipsis($d1);
      }
    | (test[null] COLON)
   => lower=test[ContextType.Load] (c1=COLON (upper1=test[ContextType.Load])? (sliceop)?)?
      {
          $sltype = actions.makeSubscript($lower.tree, $c1, $upper1.tree, $sliceop.tree);
      }
    | (COLON)
   => c2=COLON (upper2=test[ContextType.Load])? (sliceop)?
      {
          $sltype = actions.makeSubscript(null, $c2, $upper2.tree, $sliceop.tree);
      }
    | test[ContextType.Load]
      {
          $sltype = actions.makeIndex($test.start, GrammarUtilities.castExpr($test.tree));
      }
    ;

//sliceop: ':' [test]
sliceop
@init {
    PNode etype = null;
}
@after {
    if (etype != null) {
        $sliceop.tree = etype;
    }
}
    : COLON
     (test[ContextType.Load]
    -> test
     |
       {
           etype = actions.makeName($COLON, "None", ContextType.Load);
       }
     )
    ;

//exprlist: expr (',' expr)* [',']
exprlist
    [ContextType ctype] returns [PNode etype]
    : (expr[null] COMMA) => e+=expr[ctype] (options {k=2;}: COMMA e+=expr[ctype])* (COMMA)?
       {
           $etype = actions.makeTuple($exprlist.start, GrammarUtilities.castExprs($e), ctype);
           $etype = actions.recuFixWriteLocalSlots($etype,0);
       }
    | expr[ctype]
      {
        $etype = GrammarUtilities.castExpr($expr.tree);
        $etype = actions.recuFixWriteLocalSlots($etype,0);
      }
    ;

//not in CPython's Grammar file
//Needed as an exprlist that does not produce tuples for del_stmt.
del_list
    returns [List<PNode> etypes]
    : e+=expr[ContextType.Del] (options {k=2;}: COMMA e+=expr[ContextType.Del])* (COMMA)?
      {
          $etypes = uncovered.makeDeleteList($e);
      }
    ;

//testlist: test (',' test)* [',']
testlist[ContextType ctype]
@init {
    PNode etype = null;
}
@after {
    if (etype != null) {
        $testlist.tree = etype;
    }
}
    : (test[null] COMMA)
   => t+=test[ctype] (options {k=2;}: COMMA t+=test[ctype])* (COMMA)?
      {
          etype = actions.makeTuple($testlist.start, GrammarUtilities.castExprs($t), ctype);
      }
    | test[ctype]
    ;

//dictorsetmaker: ( (test ':' test (comp_for | (',' test ':' test)* [','])) |
//                  (test (comp_for | (',' test)* [','])) )

//dictmaker: test ':' test (',' test ':' test)* [',']
dictorsetmaker[Token lcurly]
@init {
    List gens = new ArrayList();
    PNode etype = null;
}
@after {
    if (etype != null) {
        $dictorsetmaker.tree = etype;
    }
}
    : k+=test[ContextType.Load]
         (
             (COLON v+=test[ContextType.Load]
               ( comp_for[gens]
                 {
                     Collections.reverse(gens);
                     List<PComprehension> c = gens;
                     etype = actions.makeDictComp($dictorsetmaker.start, GrammarUtilities.castExpr($k.get(0)), GrammarUtilities.castExpr($v.get(0)), c);
                 }
               | (options {k=2;}:COMMA k+=test[ContextType.Load] COLON v+=test[ContextType.Load])*
                 {
                     etype = actions.makeDict($lcurly, GrammarUtilities.castExprs($k), GrammarUtilities.castExprs($v));
                 }
               )
             |(COMMA k+=test[ContextType.Load])*
              {
                  etype = uncovered.makeSet($lcurly, GrammarUtilities.castExprs($k));
              }
             )
             (COMMA)?
         | comp_for[gens]
           {
               Collections.reverse(gens);
               List<PComprehension> c = gens;
               PNode e = GrammarUtilities.castExpr($k.get(0));
//               if (e instanceof Context) {
//                   ((Context)e).setContext(ContextType.Load);
//               }
               etype = actions.makeSetComp($lcurly, GrammarUtilities.castExpr($k.get(0)), c);
           }
         )
    ;

//classdef: 'class' NAME ['(' [testlist] ')'] ':' suite
classdef
@init {
    StatementNode stype = null;
}
@after {
   $classdef.tree = stype;
}
    : decorators? CLASS NAME (LPAREN testlist[ContextType.Load]? RPAREN)? COLON suite[false]
      {
          Token t = $CLASS;
          if ($decorators.start != null) {
              t = $decorators.start;
          }
//          stype = new ClassDef(t, actions.cantBeNoneName($NAME),
//              actions.makeBases(GrammarUtilities.castExpr($testlist.tree)),
//              GrammarUtilities.castStmts($suite.stypes),
//              GrammarUtilities.castExprs($decorators.etypes));
            stype = uncovered.makeClassDef(t,$NAME,$testlist.tree,$suite.stypes,$decorators.etypes);
      }
    ;

//arglist: (argument ',')* (argument [',']
//                         |'*' test (',' argument)* [',' '**' test]
//                         |'**' test)
arglist
    returns [List args, List keywords, PNode starargs, PNode kwargs]
@init {
    List arguments = new ArrayList();
    List kws = new ArrayList();
    List gens = new ArrayList();
}
    : argument[arguments, kws, gens, true, false] (COMMA argument[arguments, kws, gens, false, false])*
          (COMMA
              ( STAR s=test[ContextType.Load] (COMMA argument[arguments, kws, gens, false, true])* (COMMA DOUBLESTAR k=test[ContextType.Load])?
              | DOUBLESTAR k=test[ContextType.Load]
              )?
          )?
      {
          if (arguments.size() > 1 && gens.size() > 0) {
              GrammarUtilities.errorGenExpNotSoleArg($arglist.start);
          }
          $args=arguments;
          $keywords=kws;
          $starargs=GrammarUtilities.castExpr($s.tree);
          $kwargs=GrammarUtilities.castExpr($k.tree);
      }
    | STAR s=test[ContextType.Load] (COMMA argument[arguments, kws, gens, false, true])* (COMMA DOUBLESTAR k=test[ContextType.Load])?
      {
          $starargs=GrammarUtilities.castExpr($s.tree);
          $keywords=kws;
          $kwargs=GrammarUtilities.castExpr($k.tree);
      }
    | DOUBLESTAR k=test[ContextType.Load]
      {
          $kwargs=GrammarUtilities.castExpr($k.tree);
      }
    ;

//argument: test [comp_for] | test '=' test  # Really [keyword '='] test
argument
    [List arguments, List kws, List gens, boolean first, boolean afterStar] returns [boolean genarg]
    : t1=test[ContextType.Load]
        ((ASSIGN t2=test[ContextType.Load])
          {
              PNode newkey = GrammarUtilities.castExpr($t1.tree);
              //Loop through all current keys and fail on duplicate.
              for(Object o: $kws) {
                  List list = (List)o;
                  Object oldkey = list.get(0);
//                  if (oldkey instanceof Name && newkey instanceof Name) { //TODO: need to check if keyword != old keyword
//                      if (((Name)oldkey).getId().equals(((Name)newkey).getId())) {
//                          ErrorHandler.error("keyword arguments repeated", $t1.tree);
//                      }
//                  }
              }
              List<PNode> exprs = new ArrayList<PNode>();
              exprs.add(newkey);
              exprs.add(GrammarUtilities.castExpr($t2.tree));
              $kws.add(exprs);
          }
        | {ParserEnvironment.beginScope();} comp_for[$gens]
          {
              if (!first) {
                  GrammarUtilities.errorGenExpNotSoleArg($comp_for.tree);
              }
              $genarg = true;
              Collections.reverse($gens);
              List<PComprehension> c = $gens;
              arguments.add(actions.makeGeneratorExp($t1.start, GrammarUtilities.castExpr($t1.tree), c));
              ParserEnvironment.endScope();
          }
        |
          {
              if (kws.size() > 0) {
                  ErrorHandler.error("non-keyword arg after keyword arg", $t1.tree.getToken());
              } else if (afterStar) {
                  ErrorHandler.error("only named arguments may follow *expression", $t1.tree.getToken());
              }
              $arguments.add($t1.tree);
          }
        )
    ;

//list_iter: list_for | list_if
list_iter [List gens, List ifs]
    : list_for[gens]
    | list_if[gens, ifs]
    ;

//list_for: 'for' exprlist 'in' testlist_safe [list_iter]
list_for [List gens]
@init {
    List ifs = new ArrayList();
}
    : FOR exprlist[ContextType.Store] IN testlist[ContextType.Load] (list_iter[gens, ifs])?
      {
          Collections.reverse(ifs);
          gens.add(actions.makeComprehension($FOR, $exprlist.etype, GrammarUtilities.castExpr($testlist.tree), ifs));
      }
    ;

//list_if: 'if' test [list_iter]
list_if[List gens, List ifs]
    : IF test[ContextType.Load] (list_iter[gens, ifs])?
      {
        ifs.add(GrammarUtilities.castExpr($test.tree));
      }
    ;

//comp_iter: comp_for | comp_if
comp_iter [List gens, List ifs]
    : comp_for[gens]
    | comp_if[gens, ifs]
    ;

//comp_for: 'for' exprlist 'in' or_test [comp_iter]
comp_for [List gens]
@init {
    List ifs = new ArrayList();
}
    : FOR exprlist[ContextType.Store] IN or_test[ContextType.Load] comp_iter[gens, ifs]?
      {
          Collections.reverse(ifs);
          gens.add(actions.makeComprehension($FOR, $exprlist.etype, GrammarUtilities.castExpr($or_test.tree), ifs));
      }
    ;

//comp_if: 'if' old_test [comp_iter]
comp_if[List gens, List ifs]
    : IF test[ContextType.Load] comp_iter[gens, ifs]?
      {
        ifs.add(GrammarUtilities.castExpr($test.tree));
      }
    ;

//yield_expr: 'yield' [testlist]
yield_expr
    returns [StatementNode etype]
@after {
    //needed for y2+=yield_expr
    $yield_expr.tree = $etype;
}
    : YIELD testlist[ContextType.Load]?
      {
          $etype = actions.makeYield($YIELD, GrammarUtilities.castExpr($testlist.tree));
      }
    ;

//START OF LEXER RULES
AS        : 'as' ;
ASSERT    : 'assert' ;
BREAK     : 'break' ;
CLASS     : 'class' ;
CONTINUE  : 'continue' ;
DEF       : 'def' ;
DELETE    : 'del' ;
ELIF      : 'elif' ;
EXCEPT    : 'except' ;
//EXEC      : 'exec' ;
FINALLY   : 'finally' ;
FROM      : 'from' ;
FOR       : 'for' ;
GLOBAL    : 'global' ;
IF        : 'if' ;
IMPORT    : 'import' ;
IN        : 'in' ;
IS        : 'is' ;
LAMBDA    : 'lambda' ;
ORELSE    : 'else' ;
PASS      : 'pass'  ;
PRINT     : 'print' ;
RAISE     : 'raise' ;
RETURN    : 'return' ;
TRY       : 'try' ;
WHILE     : 'while' ;
WITH      : 'with' ;
YIELD     : 'yield' ;
NONE      : 'None' ;
TRUE      : 'True' ;
FALSE     : 'False' ;
NONLOCAL  : 'nonlocal' ;

LPAREN    : '(' {implicitLineJoiningLevel++;} ;

RPAREN    : ')' {implicitLineJoiningLevel--;} ;

LBRACK    : '[' {implicitLineJoiningLevel++;} ;

RBRACK    : ']' {implicitLineJoiningLevel--;} ;

COLON     : ':' ;

COMMA    : ',' ;

SEMI    : ';' ;

PLUS    : '+' ;

MINUS    : '-' ;

STAR    : '*' ;

SLASH    : '/' ;

VBAR    : '|' ;

AMPER    : '&' ;

LESS    : '<' ;

GREATER    : '>' ;

ASSIGN    : '=' ;

PERCENT    : '%' ;

BACKQUOTE    : '`' ;

LCURLY    : '{' {implicitLineJoiningLevel++;} ;

RCURLY    : '}' {implicitLineJoiningLevel--;} ;

CIRCUMFLEX    : '^' ;

TILDE    : '~' ;

EQUAL    : '==' ;

NOTEQUAL    : '!=' ;

//ALT_NOTEQUAL: '<>' ;

LESSEQUAL    : '<=' ;

LEFTSHIFT    : '<<' ;

GREATEREQUAL    : '>=' ;

RIGHTSHIFT    : '>>' ;

PLUSEQUAL    : '+=' ;

MINUSEQUAL    : '-=' ;

DOUBLESTAR    : '**' ;

STAREQUAL    : '*=' ;

DOUBLESLASH    : '//' ;

SLASHEQUAL    : '/=' ;

VBAREQUAL    : '|=' ;

PERCENTEQUAL    : '%=' ;

AMPEREQUAL    : '&=' ;

CIRCUMFLEXEQUAL    : '^=' ;

LEFTSHIFTEQUAL    : '<<=' ;

RIGHTSHIFTEQUAL    : '>>=' ;

DOUBLESTAREQUAL    : '**=' ;

DOUBLESLASHEQUAL    : '//=' ;

DOT : '.' ;

AT : '@' ;

AND : 'and' ;

OR : 'or' ;

NOT : 'not' ;

FLOAT
    :   '.' DIGITS (Exponent)?
    |   DIGITS '.' Exponent
    |   DIGITS ('.' (DIGITS (Exponent)?)? | Exponent)
    ;

//LONGINT
//    :   INT ('l'|'L')
//    ;

fragment
Exponent
    :    ('e' | 'E') ( '+' | '-' )? DIGITS
    ;

INT :   // Hex
        '0' ('x' | 'X') ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )+
    |   // Octal
        '0' ('o' | 'O') ( '0' .. '7' )*
    |   '0'  ( '0' .. '7' )*
    |   // Binary
        '0' ('b' | 'B') ( '0' .. '1' )*
    |   // Decimal
        '1'..'9' DIGITS*
;

COMPLEX
    :   DIGITS+ ('j'|'J')
    |   FLOAT ('j'|'J')
    ;

fragment
DIGITS : ( '0' .. '9' )+ ;

NAME:    ( 'a' .. 'z' | 'A' .. 'Z' | '_')
        ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
    ;

/** Match various string types.  Note that greedy=false implies '''
 *  should make us exit loop not continue.
 */
STRING
    :   ('r'|'u'|'b'|'ur'|'br'|'R'|'U'|'B'|'UR'|'BR'|'uR'|'Ur'|'Br'|'bR')?
        (   '\'\'\'' (options {greedy=false;}:TRIAPOS)* '\'\'\''
        |   '"""' (options {greedy=false;}:TRIQUOTE)* '"""'
        |   '"' (ESC|~('\\'|'\n'|'"'))* '"'
        |   '\'' (ESC|~('\\'|'\n'|'\''))* '\''
        ) {
           if (state.tokenStartLine != input.getLine()) {
               state.tokenStartLine = input.getLine();
               state.tokenStartCharPositionInLine = -2;
           }
        }
    ;

/** the two '"'? cause a warning -- is there a way to avoid that? */
fragment
TRIQUOTE
    : '"'? '"'? (ESC|~('\\'|'"'))+
    ;

/** the two '\''? cause a warning -- is there a way to avoid that? */
fragment
TRIAPOS
    : '\''? '\''? (ESC|~('\\'|'\''))+
    ;

fragment
ESC
    :    '\\' .
    ;

/** Consume a newline and any whitespace at start of next line
 *  unless the next line contains only white space, in that case
 *  emit a newline.
 */
CONTINUED_LINE
    :    '\\' ('\r')? '\n' (' '|'\t')*  { $channel=HIDDEN; }
         ( COMMENT
         | nl=NEWLINE
           {
               emit(new CommonToken(NEWLINE,nl.getText()));
           }
         |
         ) {
               if (input.LA(1) == -1) {
                   throw new ParseException("unexpected character after line continuation character");
               }
           }
    ;

/** Treat a sequence of blank lines as a single blank line.  If
 *  nested within a (..), {..}, or [..], then ignore newlines.
 *  If the first newline starts in column one, they are to be ignored.
 *
 *  Frank Wierzbicki added: Also ignore FORMFEEDS (\u000C).
 */
NEWLINE
@init {
    int newlines = 0;
}
    :   (('\u000C')?('\r')? '\n' {newlines++; } )+ {
         if ( startPos==0 || implicitLineJoiningLevel>0 )
            $channel=HIDDEN;
        }
    ;

WS  :    {startPos>0}?=> (' '|'\t'|'\u000C')+ {$channel=HIDDEN;}
    ;

/** Grab everything before a real symbol.  Then if newline, kill it
 *  as this is a blank line.  If whitespace followed by comment, kill it
 *  as it's a comment on a line by itself.
 *
 *  Ignore leading whitespace when nested in [..], (..), {..}.
 */
LEADING_WS
@init {
    int spaces = 0;
    int newlines = 0;
}
    :   {startPos==0}?=>
        (   {implicitLineJoiningLevel>0}? ( ' ' | '\t' )+ {$channel=HIDDEN;}
        |    (     ' '  { spaces++; }
             |    '\t' { spaces += 8; spaces -= (spaces \% 8); }
             )+
             ( ('\r')? '\n' {newlines++; }
             )* {
                   if (input.LA(1) != -1 || newlines == 0) {
                       // make a string of n spaces where n is column number - 1
                       char[] indentation = new char[spaces];
                       for (int i=0; i<spaces; i++) {
                           indentation[i] = ' ';
                       }
                       CommonToken c = new CommonToken(LEADING_WS,new String(indentation));
                       c.setLine(input.getLine());
                       c.setCharPositionInLine(input.getCharPositionInLine());
                       c.setStartIndex(input.index() - 1);
                       c.setStopIndex(input.index() - 1);
                       emit(c);
                       // kill trailing newline if present and then ignore
                       if (newlines != 0) {
                           if (state.token!=null) {
                               state.token.setChannel(HIDDEN);
                           } else {
                               $channel=HIDDEN;
                           }
                       }
                   } else if (this.single && newlines == 1) {
                       // This is here for this case in interactive mode:
                       //
                       // def foo():
                       //   print 1
                       //   <spaces but no code>
                       //
                       // The above would complete in interactive mode instead
                       // of giving ... to wait for more input.
                       //
                       throw new ParseException("Trailing space in single mode.");
                   } else {
                       // make a string of n newlines
                       char[] nls = new char[newlines];
                       for (int i=0; i<newlines; i++) {
                           nls[i] = '\n';
                       }
                       CommonToken c = new CommonToken(NEWLINE,new String(nls));
                       c.setLine(input.getLine());
                       c.setCharPositionInLine(input.getCharPositionInLine());
                       c.setStartIndex(input.index() - 1);
                       c.setStopIndex(input.index() - 1);
                       emit(c);
                   }
                }
        )
    ;

/** Comments not on line by themselves are turned into newlines.

    b = a # end of line comment

    or

    a = [1, # weird
         2]

    This rule is invoked directly by nextToken when the comment is in
    first column or when comment is on end of nonwhitespace line.

    Only match \n here if we didn't start on left edge; let NEWLINE return that.
    Kill if newlines if we live on a line by ourselves

    Consume any leading whitespace if it starts on left edge.
 */
COMMENT
@init {
    $channel=HIDDEN;
}
    :    {startPos==0}?=> (' '|'\t')* '#' (~'\n')* '\n'+
    |    '#' (~'\n')* // let NEWLINE handle \n unless char pos==0 for '#'
    ;
