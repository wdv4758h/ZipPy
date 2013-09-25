// $ANTLR 3.5 Truffle.g 2013-09-24 16:57:32

package edu.uci.python.antlr;
import edu.uci.python.*;
import org.antlr.runtime.tree.*;

//package org.python.ast;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class TruffleLexer extends Lexer {
	public static final int EOF=-1;
	public static final int AMPER=4;
	public static final int AMPEREQUAL=5;
	public static final int AND=6;
	public static final int AS=7;
	public static final int ASSERT=8;
	public static final int ASSIGN=9;
	public static final int AT=10;
	public static final int BACKQUOTE=11;
	public static final int BREAK=12;
	public static final int CIRCUMFLEX=13;
	public static final int CIRCUMFLEXEQUAL=14;
	public static final int CLASS=15;
	public static final int COLON=16;
	public static final int COMMA=17;
	public static final int COMMENT=18;
	public static final int COMPLEX=19;
	public static final int CONTINUE=20;
	public static final int CONTINUED_LINE=21;
	public static final int DEDENT=22;
	public static final int DEF=23;
	public static final int DELETE=24;
	public static final int DIGITS=25;
	public static final int DOT=26;
	public static final int DOUBLESLASH=27;
	public static final int DOUBLESLASHEQUAL=28;
	public static final int DOUBLESTAR=29;
	public static final int DOUBLESTAREQUAL=30;
	public static final int ELIF=31;
	public static final int EQUAL=32;
	public static final int ESC=33;
	public static final int EXCEPT=34;
	public static final int EXEC=35;
	public static final int Exponent=36;
	public static final int FALSE=37;
	public static final int FINALLY=38;
	public static final int FLOAT=39;
	public static final int FOR=40;
	public static final int FROM=41;
	public static final int GLOBAL=42;
	public static final int GREATER=43;
	public static final int GREATEREQUAL=44;
	public static final int IF=45;
	public static final int IMPORT=46;
	public static final int IN=47;
	public static final int INDENT=48;
	public static final int INT=49;
	public static final int IS=50;
	public static final int LAMBDA=51;
	public static final int LBRACK=52;
	public static final int LCURLY=53;
	public static final int LEADING_WS=54;
	public static final int LEFTSHIFT=55;
	public static final int LEFTSHIFTEQUAL=56;
	public static final int LESS=57;
	public static final int LESSEQUAL=58;
	public static final int LPAREN=59;
	public static final int MINUS=60;
	public static final int MINUSEQUAL=61;
	public static final int NAME=62;
	public static final int NEWLINE=63;
	public static final int NONE=64;
	public static final int NONLOCAL=65;
	public static final int NOT=66;
	public static final int NOTEQUAL=67;
	public static final int OR=68;
	public static final int ORELSE=69;
	public static final int PASS=70;
	public static final int PERCENT=71;
	public static final int PERCENTEQUAL=72;
	public static final int PLUS=73;
	public static final int PLUSEQUAL=74;
	public static final int PRINT=75;
	public static final int RAISE=76;
	public static final int RBRACK=77;
	public static final int RCURLY=78;
	public static final int RETURN=79;
	public static final int RIGHTSHIFT=80;
	public static final int RIGHTSHIFTEQUAL=81;
	public static final int RPAREN=82;
	public static final int SEMI=83;
	public static final int SLASH=84;
	public static final int SLASHEQUAL=85;
	public static final int STAR=86;
	public static final int STAREQUAL=87;
	public static final int STRING=88;
	public static final int TILDE=89;
	public static final int TRAILBACKSLASH=90;
	public static final int TRIAPOS=91;
	public static final int TRIQUOTE=92;
	public static final int TRUE=93;
	public static final int TRY=94;
	public static final int VBAR=95;
	public static final int VBAREQUAL=96;
	public static final int WHILE=97;
	public static final int WITH=98;
	public static final int WS=99;
	public static final int YIELD=100;

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



	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public TruffleLexer() {} 
	public TruffleLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public TruffleLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "Truffle.g"; }

	// $ANTLR start "AS"
	public final void mAS() throws RecognitionException {
		try {
			int _type = AS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2183:11: ( 'as' )
			// Truffle.g:2183:13: 'as'
			{
			match("as"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AS"

	// $ANTLR start "ASSERT"
	public final void mASSERT() throws RecognitionException {
		try {
			int _type = ASSERT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2184:11: ( 'assert' )
			// Truffle.g:2184:13: 'assert'
			{
			match("assert"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ASSERT"

	// $ANTLR start "BREAK"
	public final void mBREAK() throws RecognitionException {
		try {
			int _type = BREAK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2185:11: ( 'break' )
			// Truffle.g:2185:13: 'break'
			{
			match("break"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BREAK"

	// $ANTLR start "CLASS"
	public final void mCLASS() throws RecognitionException {
		try {
			int _type = CLASS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2186:11: ( 'class' )
			// Truffle.g:2186:13: 'class'
			{
			match("class"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CLASS"

	// $ANTLR start "CONTINUE"
	public final void mCONTINUE() throws RecognitionException {
		try {
			int _type = CONTINUE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2187:11: ( 'continue' )
			// Truffle.g:2187:13: 'continue'
			{
			match("continue"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CONTINUE"

	// $ANTLR start "DEF"
	public final void mDEF() throws RecognitionException {
		try {
			int _type = DEF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2188:11: ( 'def' )
			// Truffle.g:2188:13: 'def'
			{
			match("def"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DEF"

	// $ANTLR start "DELETE"
	public final void mDELETE() throws RecognitionException {
		try {
			int _type = DELETE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2189:11: ( 'del' )
			// Truffle.g:2189:13: 'del'
			{
			match("del"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DELETE"

	// $ANTLR start "ELIF"
	public final void mELIF() throws RecognitionException {
		try {
			int _type = ELIF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2190:11: ( 'elif' )
			// Truffle.g:2190:13: 'elif'
			{
			match("elif"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ELIF"

	// $ANTLR start "EXCEPT"
	public final void mEXCEPT() throws RecognitionException {
		try {
			int _type = EXCEPT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2191:11: ( 'except' )
			// Truffle.g:2191:13: 'except'
			{
			match("except"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EXCEPT"

	// $ANTLR start "FINALLY"
	public final void mFINALLY() throws RecognitionException {
		try {
			int _type = FINALLY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2193:11: ( 'finally' )
			// Truffle.g:2193:13: 'finally'
			{
			match("finally"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FINALLY"

	// $ANTLR start "FROM"
	public final void mFROM() throws RecognitionException {
		try {
			int _type = FROM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2194:11: ( 'from' )
			// Truffle.g:2194:13: 'from'
			{
			match("from"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FROM"

	// $ANTLR start "FOR"
	public final void mFOR() throws RecognitionException {
		try {
			int _type = FOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2195:11: ( 'for' )
			// Truffle.g:2195:13: 'for'
			{
			match("for"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FOR"

	// $ANTLR start "GLOBAL"
	public final void mGLOBAL() throws RecognitionException {
		try {
			int _type = GLOBAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2196:11: ( 'global' )
			// Truffle.g:2196:13: 'global'
			{
			match("global"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GLOBAL"

	// $ANTLR start "IF"
	public final void mIF() throws RecognitionException {
		try {
			int _type = IF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2197:11: ( 'if' )
			// Truffle.g:2197:13: 'if'
			{
			match("if"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IF"

	// $ANTLR start "IMPORT"
	public final void mIMPORT() throws RecognitionException {
		try {
			int _type = IMPORT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2198:11: ( 'import' )
			// Truffle.g:2198:13: 'import'
			{
			match("import"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IMPORT"

	// $ANTLR start "IN"
	public final void mIN() throws RecognitionException {
		try {
			int _type = IN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2199:11: ( 'in' )
			// Truffle.g:2199:13: 'in'
			{
			match("in"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IN"

	// $ANTLR start "IS"
	public final void mIS() throws RecognitionException {
		try {
			int _type = IS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2200:11: ( 'is' )
			// Truffle.g:2200:13: 'is'
			{
			match("is"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IS"

	// $ANTLR start "LAMBDA"
	public final void mLAMBDA() throws RecognitionException {
		try {
			int _type = LAMBDA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2201:11: ( 'lambda' )
			// Truffle.g:2201:13: 'lambda'
			{
			match("lambda"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LAMBDA"

	// $ANTLR start "ORELSE"
	public final void mORELSE() throws RecognitionException {
		try {
			int _type = ORELSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2202:11: ( 'else' )
			// Truffle.g:2202:13: 'else'
			{
			match("else"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ORELSE"

	// $ANTLR start "PASS"
	public final void mPASS() throws RecognitionException {
		try {
			int _type = PASS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2203:11: ( 'pass' )
			// Truffle.g:2203:13: 'pass'
			{
			match("pass"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PASS"

	// $ANTLR start "PRINT"
	public final void mPRINT() throws RecognitionException {
		try {
			int _type = PRINT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2204:11: ( 'print' )
			// Truffle.g:2204:13: 'print'
			{
			match("print"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PRINT"

	// $ANTLR start "RAISE"
	public final void mRAISE() throws RecognitionException {
		try {
			int _type = RAISE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2205:11: ( 'raise' )
			// Truffle.g:2205:13: 'raise'
			{
			match("raise"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RAISE"

	// $ANTLR start "RETURN"
	public final void mRETURN() throws RecognitionException {
		try {
			int _type = RETURN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2206:11: ( 'return' )
			// Truffle.g:2206:13: 'return'
			{
			match("return"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RETURN"

	// $ANTLR start "TRY"
	public final void mTRY() throws RecognitionException {
		try {
			int _type = TRY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2207:11: ( 'try' )
			// Truffle.g:2207:13: 'try'
			{
			match("try"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TRY"

	// $ANTLR start "WHILE"
	public final void mWHILE() throws RecognitionException {
		try {
			int _type = WHILE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2208:11: ( 'while' )
			// Truffle.g:2208:13: 'while'
			{
			match("while"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WHILE"

	// $ANTLR start "WITH"
	public final void mWITH() throws RecognitionException {
		try {
			int _type = WITH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2209:11: ( 'with' )
			// Truffle.g:2209:13: 'with'
			{
			match("with"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WITH"

	// $ANTLR start "YIELD"
	public final void mYIELD() throws RecognitionException {
		try {
			int _type = YIELD;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2210:11: ( 'yield' )
			// Truffle.g:2210:13: 'yield'
			{
			match("yield"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "YIELD"

	// $ANTLR start "NONE"
	public final void mNONE() throws RecognitionException {
		try {
			int _type = NONE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2211:11: ( 'None' )
			// Truffle.g:2211:13: 'None'
			{
			match("None"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NONE"

	// $ANTLR start "TRUE"
	public final void mTRUE() throws RecognitionException {
		try {
			int _type = TRUE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2212:11: ( 'True' )
			// Truffle.g:2212:13: 'True'
			{
			match("True"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TRUE"

	// $ANTLR start "FALSE"
	public final void mFALSE() throws RecognitionException {
		try {
			int _type = FALSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2213:11: ( 'False' )
			// Truffle.g:2213:13: 'False'
			{
			match("False"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FALSE"

	// $ANTLR start "NONLOCAL"
	public final void mNONLOCAL() throws RecognitionException {
		try {
			int _type = NONLOCAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2214:11: ( 'nonlocal' )
			// Truffle.g:2214:13: 'nonlocal'
			{
			match("nonlocal"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NONLOCAL"

	// $ANTLR start "LPAREN"
	public final void mLPAREN() throws RecognitionException {
		try {
			int _type = LPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2216:11: ( '(' )
			// Truffle.g:2216:13: '('
			{
			match('('); 
			implicitLineJoiningLevel++;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LPAREN"

	// $ANTLR start "RPAREN"
	public final void mRPAREN() throws RecognitionException {
		try {
			int _type = RPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2218:11: ( ')' )
			// Truffle.g:2218:13: ')'
			{
			match(')'); 
			implicitLineJoiningLevel--;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RPAREN"

	// $ANTLR start "LBRACK"
	public final void mLBRACK() throws RecognitionException {
		try {
			int _type = LBRACK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2220:11: ( '[' )
			// Truffle.g:2220:13: '['
			{
			match('['); 
			implicitLineJoiningLevel++;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LBRACK"

	// $ANTLR start "RBRACK"
	public final void mRBRACK() throws RecognitionException {
		try {
			int _type = RBRACK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2222:11: ( ']' )
			// Truffle.g:2222:13: ']'
			{
			match(']'); 
			implicitLineJoiningLevel--;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RBRACK"

	// $ANTLR start "COLON"
	public final void mCOLON() throws RecognitionException {
		try {
			int _type = COLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2224:11: ( ':' )
			// Truffle.g:2224:13: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COLON"

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2226:10: ( ',' )
			// Truffle.g:2226:12: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "SEMI"
	public final void mSEMI() throws RecognitionException {
		try {
			int _type = SEMI;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2228:9: ( ';' )
			// Truffle.g:2228:11: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SEMI"

	// $ANTLR start "PLUS"
	public final void mPLUS() throws RecognitionException {
		try {
			int _type = PLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2230:9: ( '+' )
			// Truffle.g:2230:11: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUS"

	// $ANTLR start "MINUS"
	public final void mMINUS() throws RecognitionException {
		try {
			int _type = MINUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2232:10: ( '-' )
			// Truffle.g:2232:12: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MINUS"

	// $ANTLR start "STAR"
	public final void mSTAR() throws RecognitionException {
		try {
			int _type = STAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2234:9: ( '*' )
			// Truffle.g:2234:11: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STAR"

	// $ANTLR start "SLASH"
	public final void mSLASH() throws RecognitionException {
		try {
			int _type = SLASH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2236:10: ( '/' )
			// Truffle.g:2236:12: '/'
			{
			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SLASH"

	// $ANTLR start "VBAR"
	public final void mVBAR() throws RecognitionException {
		try {
			int _type = VBAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2238:9: ( '|' )
			// Truffle.g:2238:11: '|'
			{
			match('|'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "VBAR"

	// $ANTLR start "AMPER"
	public final void mAMPER() throws RecognitionException {
		try {
			int _type = AMPER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2240:10: ( '&' )
			// Truffle.g:2240:12: '&'
			{
			match('&'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AMPER"

	// $ANTLR start "LESS"
	public final void mLESS() throws RecognitionException {
		try {
			int _type = LESS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2242:9: ( '<' )
			// Truffle.g:2242:11: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LESS"

	// $ANTLR start "GREATER"
	public final void mGREATER() throws RecognitionException {
		try {
			int _type = GREATER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2244:12: ( '>' )
			// Truffle.g:2244:14: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GREATER"

	// $ANTLR start "ASSIGN"
	public final void mASSIGN() throws RecognitionException {
		try {
			int _type = ASSIGN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2246:11: ( '=' )
			// Truffle.g:2246:13: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ASSIGN"

	// $ANTLR start "PERCENT"
	public final void mPERCENT() throws RecognitionException {
		try {
			int _type = PERCENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2248:12: ( '%' )
			// Truffle.g:2248:14: '%'
			{
			match('%'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PERCENT"

	// $ANTLR start "BACKQUOTE"
	public final void mBACKQUOTE() throws RecognitionException {
		try {
			int _type = BACKQUOTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2250:14: ( '`' )
			// Truffle.g:2250:16: '`'
			{
			match('`'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BACKQUOTE"

	// $ANTLR start "LCURLY"
	public final void mLCURLY() throws RecognitionException {
		try {
			int _type = LCURLY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2252:11: ( '{' )
			// Truffle.g:2252:13: '{'
			{
			match('{'); 
			implicitLineJoiningLevel++;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LCURLY"

	// $ANTLR start "RCURLY"
	public final void mRCURLY() throws RecognitionException {
		try {
			int _type = RCURLY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2254:11: ( '}' )
			// Truffle.g:2254:13: '}'
			{
			match('}'); 
			implicitLineJoiningLevel--;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RCURLY"

	// $ANTLR start "CIRCUMFLEX"
	public final void mCIRCUMFLEX() throws RecognitionException {
		try {
			int _type = CIRCUMFLEX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2256:15: ( '^' )
			// Truffle.g:2256:17: '^'
			{
			match('^'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CIRCUMFLEX"

	// $ANTLR start "TILDE"
	public final void mTILDE() throws RecognitionException {
		try {
			int _type = TILDE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2258:10: ( '~' )
			// Truffle.g:2258:12: '~'
			{
			match('~'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TILDE"

	// $ANTLR start "EQUAL"
	public final void mEQUAL() throws RecognitionException {
		try {
			int _type = EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2260:10: ( '==' )
			// Truffle.g:2260:12: '=='
			{
			match("=="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EQUAL"

	// $ANTLR start "NOTEQUAL"
	public final void mNOTEQUAL() throws RecognitionException {
		try {
			int _type = NOTEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2262:13: ( '!=' )
			// Truffle.g:2262:15: '!='
			{
			match("!="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOTEQUAL"

	// $ANTLR start "LESSEQUAL"
	public final void mLESSEQUAL() throws RecognitionException {
		try {
			int _type = LESSEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2266:14: ( '<=' )
			// Truffle.g:2266:16: '<='
			{
			match("<="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LESSEQUAL"

	// $ANTLR start "LEFTSHIFT"
	public final void mLEFTSHIFT() throws RecognitionException {
		try {
			int _type = LEFTSHIFT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2268:14: ( '<<' )
			// Truffle.g:2268:16: '<<'
			{
			match("<<"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LEFTSHIFT"

	// $ANTLR start "GREATEREQUAL"
	public final void mGREATEREQUAL() throws RecognitionException {
		try {
			int _type = GREATEREQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2270:17: ( '>=' )
			// Truffle.g:2270:19: '>='
			{
			match(">="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GREATEREQUAL"

	// $ANTLR start "RIGHTSHIFT"
	public final void mRIGHTSHIFT() throws RecognitionException {
		try {
			int _type = RIGHTSHIFT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2272:15: ( '>>' )
			// Truffle.g:2272:17: '>>'
			{
			match(">>"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RIGHTSHIFT"

	// $ANTLR start "PLUSEQUAL"
	public final void mPLUSEQUAL() throws RecognitionException {
		try {
			int _type = PLUSEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2274:14: ( '+=' )
			// Truffle.g:2274:16: '+='
			{
			match("+="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUSEQUAL"

	// $ANTLR start "MINUSEQUAL"
	public final void mMINUSEQUAL() throws RecognitionException {
		try {
			int _type = MINUSEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2276:15: ( '-=' )
			// Truffle.g:2276:17: '-='
			{
			match("-="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MINUSEQUAL"

	// $ANTLR start "DOUBLESTAR"
	public final void mDOUBLESTAR() throws RecognitionException {
		try {
			int _type = DOUBLESTAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2278:15: ( '**' )
			// Truffle.g:2278:17: '**'
			{
			match("**"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOUBLESTAR"

	// $ANTLR start "STAREQUAL"
	public final void mSTAREQUAL() throws RecognitionException {
		try {
			int _type = STAREQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2280:14: ( '*=' )
			// Truffle.g:2280:16: '*='
			{
			match("*="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STAREQUAL"

	// $ANTLR start "DOUBLESLASH"
	public final void mDOUBLESLASH() throws RecognitionException {
		try {
			int _type = DOUBLESLASH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2282:16: ( '//' )
			// Truffle.g:2282:18: '//'
			{
			match("//"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOUBLESLASH"

	// $ANTLR start "SLASHEQUAL"
	public final void mSLASHEQUAL() throws RecognitionException {
		try {
			int _type = SLASHEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2284:15: ( '/=' )
			// Truffle.g:2284:17: '/='
			{
			match("/="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SLASHEQUAL"

	// $ANTLR start "VBAREQUAL"
	public final void mVBAREQUAL() throws RecognitionException {
		try {
			int _type = VBAREQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2286:14: ( '|=' )
			// Truffle.g:2286:16: '|='
			{
			match("|="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "VBAREQUAL"

	// $ANTLR start "PERCENTEQUAL"
	public final void mPERCENTEQUAL() throws RecognitionException {
		try {
			int _type = PERCENTEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2288:17: ( '%=' )
			// Truffle.g:2288:19: '%='
			{
			match("%="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PERCENTEQUAL"

	// $ANTLR start "AMPEREQUAL"
	public final void mAMPEREQUAL() throws RecognitionException {
		try {
			int _type = AMPEREQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2290:15: ( '&=' )
			// Truffle.g:2290:17: '&='
			{
			match("&="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AMPEREQUAL"

	// $ANTLR start "CIRCUMFLEXEQUAL"
	public final void mCIRCUMFLEXEQUAL() throws RecognitionException {
		try {
			int _type = CIRCUMFLEXEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2292:20: ( '^=' )
			// Truffle.g:2292:22: '^='
			{
			match("^="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CIRCUMFLEXEQUAL"

	// $ANTLR start "LEFTSHIFTEQUAL"
	public final void mLEFTSHIFTEQUAL() throws RecognitionException {
		try {
			int _type = LEFTSHIFTEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2294:19: ( '<<=' )
			// Truffle.g:2294:21: '<<='
			{
			match("<<="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LEFTSHIFTEQUAL"

	// $ANTLR start "RIGHTSHIFTEQUAL"
	public final void mRIGHTSHIFTEQUAL() throws RecognitionException {
		try {
			int _type = RIGHTSHIFTEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2296:20: ( '>>=' )
			// Truffle.g:2296:22: '>>='
			{
			match(">>="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RIGHTSHIFTEQUAL"

	// $ANTLR start "DOUBLESTAREQUAL"
	public final void mDOUBLESTAREQUAL() throws RecognitionException {
		try {
			int _type = DOUBLESTAREQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2298:20: ( '**=' )
			// Truffle.g:2298:22: '**='
			{
			match("**="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOUBLESTAREQUAL"

	// $ANTLR start "DOUBLESLASHEQUAL"
	public final void mDOUBLESLASHEQUAL() throws RecognitionException {
		try {
			int _type = DOUBLESLASHEQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2300:21: ( '//=' )
			// Truffle.g:2300:23: '//='
			{
			match("//="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOUBLESLASHEQUAL"

	// $ANTLR start "DOT"
	public final void mDOT() throws RecognitionException {
		try {
			int _type = DOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2302:5: ( '.' )
			// Truffle.g:2302:7: '.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOT"

	// $ANTLR start "AT"
	public final void mAT() throws RecognitionException {
		try {
			int _type = AT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2304:4: ( '@' )
			// Truffle.g:2304:6: '@'
			{
			match('@'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AT"

	// $ANTLR start "AND"
	public final void mAND() throws RecognitionException {
		try {
			int _type = AND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2306:5: ( 'and' )
			// Truffle.g:2306:7: 'and'
			{
			match("and"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AND"

	// $ANTLR start "OR"
	public final void mOR() throws RecognitionException {
		try {
			int _type = OR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2308:4: ( 'or' )
			// Truffle.g:2308:6: 'or'
			{
			match("or"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OR"

	// $ANTLR start "NOT"
	public final void mNOT() throws RecognitionException {
		try {
			int _type = NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2310:5: ( 'not' )
			// Truffle.g:2310:7: 'not'
			{
			match("not"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOT"

	// $ANTLR start "FLOAT"
	public final void mFLOAT() throws RecognitionException {
		try {
			int _type = FLOAT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2313:5: ( '.' DIGITS ( Exponent )? | DIGITS '.' Exponent | DIGITS ( '.' ( DIGITS ( Exponent )? )? | Exponent ) )
			int alt5=3;
			alt5 = dfa5.predict(input);
			switch (alt5) {
				case 1 :
					// Truffle.g:2313:9: '.' DIGITS ( Exponent )?
					{
					match('.'); 
					mDIGITS(); 

					// Truffle.g:2313:20: ( Exponent )?
					int alt1=2;
					int LA1_0 = input.LA(1);
					if ( (LA1_0=='E'||LA1_0=='e') ) {
						alt1=1;
					}
					switch (alt1) {
						case 1 :
							// Truffle.g:2313:21: Exponent
							{
							mExponent(); 

							}
							break;

					}

					}
					break;
				case 2 :
					// Truffle.g:2314:9: DIGITS '.' Exponent
					{
					mDIGITS(); 

					match('.'); 
					mExponent(); 

					}
					break;
				case 3 :
					// Truffle.g:2315:9: DIGITS ( '.' ( DIGITS ( Exponent )? )? | Exponent )
					{
					mDIGITS(); 

					// Truffle.g:2315:16: ( '.' ( DIGITS ( Exponent )? )? | Exponent )
					int alt4=2;
					int LA4_0 = input.LA(1);
					if ( (LA4_0=='.') ) {
						alt4=1;
					}
					else if ( (LA4_0=='E'||LA4_0=='e') ) {
						alt4=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 4, 0, input);
						throw nvae;
					}

					switch (alt4) {
						case 1 :
							// Truffle.g:2315:17: '.' ( DIGITS ( Exponent )? )?
							{
							match('.'); 
							// Truffle.g:2315:21: ( DIGITS ( Exponent )? )?
							int alt3=2;
							int LA3_0 = input.LA(1);
							if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
								alt3=1;
							}
							switch (alt3) {
								case 1 :
									// Truffle.g:2315:22: DIGITS ( Exponent )?
									{
									mDIGITS(); 

									// Truffle.g:2315:29: ( Exponent )?
									int alt2=2;
									int LA2_0 = input.LA(1);
									if ( (LA2_0=='E'||LA2_0=='e') ) {
										alt2=1;
									}
									switch (alt2) {
										case 1 :
											// Truffle.g:2315:30: Exponent
											{
											mExponent(); 

											}
											break;

									}

									}
									break;

							}

							}
							break;
						case 2 :
							// Truffle.g:2315:45: Exponent
							{
							mExponent(); 

							}
							break;

					}

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FLOAT"

	// $ANTLR start "Exponent"
	public final void mExponent() throws RecognitionException {
		try {
			// Truffle.g:2325:5: ( ( 'e' | 'E' ) ( '+' | '-' )? DIGITS )
			// Truffle.g:2325:10: ( 'e' | 'E' ) ( '+' | '-' )? DIGITS
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// Truffle.g:2325:22: ( '+' | '-' )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0=='+'||LA6_0=='-') ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// Truffle.g:
					{
					if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			mDIGITS(); 

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Exponent"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2327:5: ( '0' ( 'x' | 'X' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )+ | '0' ( 'o' | 'O' ) ( '0' .. '7' )* | '0' ( '0' .. '7' )* | '0' ( 'b' | 'B' ) ( '0' .. '1' )* | '1' .. '9' ( DIGITS )* )
			int alt12=5;
			int LA12_0 = input.LA(1);
			if ( (LA12_0=='0') ) {
				switch ( input.LA(2) ) {
				case 'X':
				case 'x':
					{
					alt12=1;
					}
					break;
				case 'O':
				case 'o':
					{
					alt12=2;
					}
					break;
				case 'B':
				case 'b':
					{
					alt12=4;
					}
					break;
				default:
					alt12=3;
				}
			}
			else if ( ((LA12_0 >= '1' && LA12_0 <= '9')) ) {
				alt12=5;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 12, 0, input);
				throw nvae;
			}

			switch (alt12) {
				case 1 :
					// Truffle.g:2328:9: '0' ( 'x' | 'X' ) ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )+
					{
					match('0'); 
					if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					// Truffle.g:2328:25: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )+
					int cnt7=0;
					loop7:
					while (true) {
						int alt7=2;
						int LA7_0 = input.LA(1);
						if ( ((LA7_0 >= '0' && LA7_0 <= '9')||(LA7_0 >= 'A' && LA7_0 <= 'F')||(LA7_0 >= 'a' && LA7_0 <= 'f')) ) {
							alt7=1;
						}

						switch (alt7) {
						case 1 :
							// Truffle.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt7 >= 1 ) break loop7;
							EarlyExitException eee = new EarlyExitException(7, input);
							throw eee;
						}
						cnt7++;
					}

					}
					break;
				case 2 :
					// Truffle.g:2330:9: '0' ( 'o' | 'O' ) ( '0' .. '7' )*
					{
					match('0'); 
					if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					// Truffle.g:2330:25: ( '0' .. '7' )*
					loop8:
					while (true) {
						int alt8=2;
						int LA8_0 = input.LA(1);
						if ( ((LA8_0 >= '0' && LA8_0 <= '7')) ) {
							alt8=1;
						}

						switch (alt8) {
						case 1 :
							// Truffle.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop8;
						}
					}

					}
					break;
				case 3 :
					// Truffle.g:2331:9: '0' ( '0' .. '7' )*
					{
					match('0'); 
					// Truffle.g:2331:14: ( '0' .. '7' )*
					loop9:
					while (true) {
						int alt9=2;
						int LA9_0 = input.LA(1);
						if ( ((LA9_0 >= '0' && LA9_0 <= '7')) ) {
							alt9=1;
						}

						switch (alt9) {
						case 1 :
							// Truffle.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop9;
						}
					}

					}
					break;
				case 4 :
					// Truffle.g:2333:9: '0' ( 'b' | 'B' ) ( '0' .. '1' )*
					{
					match('0'); 
					if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					// Truffle.g:2333:25: ( '0' .. '1' )*
					loop10:
					while (true) {
						int alt10=2;
						int LA10_0 = input.LA(1);
						if ( ((LA10_0 >= '0' && LA10_0 <= '1')) ) {
							alt10=1;
						}

						switch (alt10) {
						case 1 :
							// Truffle.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '1') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop10;
						}
					}

					}
					break;
				case 5 :
					// Truffle.g:2335:9: '1' .. '9' ( DIGITS )*
					{
					matchRange('1','9'); 
					// Truffle.g:2335:18: ( DIGITS )*
					loop11:
					while (true) {
						int alt11=2;
						int LA11_0 = input.LA(1);
						if ( ((LA11_0 >= '0' && LA11_0 <= '9')) ) {
							alt11=1;
						}

						switch (alt11) {
						case 1 :
							// Truffle.g:2335:18: DIGITS
							{
							mDIGITS(); 

							}
							break;

						default :
							break loop11;
						}
					}

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT"

	// $ANTLR start "COMPLEX"
	public final void mCOMPLEX() throws RecognitionException {
		try {
			int _type = COMPLEX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2339:5: ( ( DIGITS )+ ( 'j' | 'J' ) | FLOAT ( 'j' | 'J' ) )
			int alt14=2;
			alt14 = dfa14.predict(input);
			switch (alt14) {
				case 1 :
					// Truffle.g:2339:9: ( DIGITS )+ ( 'j' | 'J' )
					{
					// Truffle.g:2339:9: ( DIGITS )+
					int cnt13=0;
					loop13:
					while (true) {
						int alt13=2;
						int LA13_0 = input.LA(1);
						if ( ((LA13_0 >= '0' && LA13_0 <= '9')) ) {
							alt13=1;
						}

						switch (alt13) {
						case 1 :
							// Truffle.g:2339:9: DIGITS
							{
							mDIGITS(); 

							}
							break;

						default :
							if ( cnt13 >= 1 ) break loop13;
							EarlyExitException eee = new EarlyExitException(13, input);
							throw eee;
						}
						cnt13++;
					}

					if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;
				case 2 :
					// Truffle.g:2340:9: FLOAT ( 'j' | 'J' )
					{
					mFLOAT(); 

					if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMPLEX"

	// $ANTLR start "DIGITS"
	public final void mDIGITS() throws RecognitionException {
		try {
			// Truffle.g:2345:8: ( ( '0' .. '9' )+ )
			// Truffle.g:2345:10: ( '0' .. '9' )+
			{
			// Truffle.g:2345:10: ( '0' .. '9' )+
			int cnt15=0;
			loop15:
			while (true) {
				int alt15=2;
				int LA15_0 = input.LA(1);
				if ( ((LA15_0 >= '0' && LA15_0 <= '9')) ) {
					alt15=1;
				}

				switch (alt15) {
				case 1 :
					// Truffle.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt15 >= 1 ) break loop15;
					EarlyExitException eee = new EarlyExitException(15, input);
					throw eee;
				}
				cnt15++;
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DIGITS"

	// $ANTLR start "NAME"
	public final void mNAME() throws RecognitionException {
		try {
			int _type = NAME;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2346:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
			// Truffle.g:2346:10: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// Truffle.g:2347:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
			loop16:
			while (true) {
				int alt16=2;
				int LA16_0 = input.LA(1);
				if ( ((LA16_0 >= '0' && LA16_0 <= '9')||(LA16_0 >= 'A' && LA16_0 <= 'Z')||LA16_0=='_'||(LA16_0 >= 'a' && LA16_0 <= 'z')) ) {
					alt16=1;
				}

				switch (alt16) {
				case 1 :
					// Truffle.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop16;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NAME"

	// $ANTLR start "STRING"
	public final void mSTRING() throws RecognitionException {
		try {
			int _type = STRING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2357:5: ( ( 'r' | 'u' | 'b' | 'ur' | 'br' | 'R' | 'U' | 'B' | 'UR' | 'BR' | 'uR' | 'Ur' | 'Br' | 'bR' )? ( '\\'\\'\\'' ( options {greedy=false; } : TRIAPOS )* '\\'\\'\\'' | '\"\"\"' ( options {greedy=false; } : TRIQUOTE )* '\"\"\"' | '\"' ( ESC |~ ( '\\\\' | '\\n' | '\"' ) )* '\"' | '\\'' ( ESC |~ ( '\\\\' | '\\n' | '\\'' ) )* '\\'' ) )
			// Truffle.g:2357:9: ( 'r' | 'u' | 'b' | 'ur' | 'br' | 'R' | 'U' | 'B' | 'UR' | 'BR' | 'uR' | 'Ur' | 'Br' | 'bR' )? ( '\\'\\'\\'' ( options {greedy=false; } : TRIAPOS )* '\\'\\'\\'' | '\"\"\"' ( options {greedy=false; } : TRIQUOTE )* '\"\"\"' | '\"' ( ESC |~ ( '\\\\' | '\\n' | '\"' ) )* '\"' | '\\'' ( ESC |~ ( '\\\\' | '\\n' | '\\'' ) )* '\\'' )
			{
			// Truffle.g:2357:9: ( 'r' | 'u' | 'b' | 'ur' | 'br' | 'R' | 'U' | 'B' | 'UR' | 'BR' | 'uR' | 'Ur' | 'Br' | 'bR' )?
			int alt17=15;
			switch ( input.LA(1) ) {
				case 'r':
					{
					alt17=1;
					}
					break;
				case 'u':
					{
					switch ( input.LA(2) ) {
						case 'r':
							{
							alt17=4;
							}
							break;
						case 'R':
							{
							alt17=11;
							}
							break;
						case '\"':
						case '\'':
							{
							alt17=2;
							}
							break;
					}
					}
					break;
				case 'b':
					{
					switch ( input.LA(2) ) {
						case 'r':
							{
							alt17=5;
							}
							break;
						case 'R':
							{
							alt17=14;
							}
							break;
						case '\"':
						case '\'':
							{
							alt17=3;
							}
							break;
					}
					}
					break;
				case 'R':
					{
					alt17=6;
					}
					break;
				case 'U':
					{
					switch ( input.LA(2) ) {
						case 'R':
							{
							alt17=9;
							}
							break;
						case 'r':
							{
							alt17=12;
							}
							break;
						case '\"':
						case '\'':
							{
							alt17=7;
							}
							break;
					}
					}
					break;
				case 'B':
					{
					switch ( input.LA(2) ) {
						case 'R':
							{
							alt17=10;
							}
							break;
						case 'r':
							{
							alt17=13;
							}
							break;
						case '\"':
						case '\'':
							{
							alt17=8;
							}
							break;
					}
					}
					break;
			}
			switch (alt17) {
				case 1 :
					// Truffle.g:2357:10: 'r'
					{
					match('r'); 
					}
					break;
				case 2 :
					// Truffle.g:2357:14: 'u'
					{
					match('u'); 
					}
					break;
				case 3 :
					// Truffle.g:2357:18: 'b'
					{
					match('b'); 
					}
					break;
				case 4 :
					// Truffle.g:2357:22: 'ur'
					{
					match("ur"); 

					}
					break;
				case 5 :
					// Truffle.g:2357:27: 'br'
					{
					match("br"); 

					}
					break;
				case 6 :
					// Truffle.g:2357:32: 'R'
					{
					match('R'); 
					}
					break;
				case 7 :
					// Truffle.g:2357:36: 'U'
					{
					match('U'); 
					}
					break;
				case 8 :
					// Truffle.g:2357:40: 'B'
					{
					match('B'); 
					}
					break;
				case 9 :
					// Truffle.g:2357:44: 'UR'
					{
					match("UR"); 

					}
					break;
				case 10 :
					// Truffle.g:2357:49: 'BR'
					{
					match("BR"); 

					}
					break;
				case 11 :
					// Truffle.g:2357:54: 'uR'
					{
					match("uR"); 

					}
					break;
				case 12 :
					// Truffle.g:2357:59: 'Ur'
					{
					match("Ur"); 

					}
					break;
				case 13 :
					// Truffle.g:2357:64: 'Br'
					{
					match("Br"); 

					}
					break;
				case 14 :
					// Truffle.g:2357:69: 'bR'
					{
					match("bR"); 

					}
					break;

			}

			// Truffle.g:2358:9: ( '\\'\\'\\'' ( options {greedy=false; } : TRIAPOS )* '\\'\\'\\'' | '\"\"\"' ( options {greedy=false; } : TRIQUOTE )* '\"\"\"' | '\"' ( ESC |~ ( '\\\\' | '\\n' | '\"' ) )* '\"' | '\\'' ( ESC |~ ( '\\\\' | '\\n' | '\\'' ) )* '\\'' )
			int alt22=4;
			int LA22_0 = input.LA(1);
			if ( (LA22_0=='\'') ) {
				int LA22_1 = input.LA(2);
				if ( (LA22_1=='\'') ) {
					int LA22_3 = input.LA(3);
					if ( (LA22_3=='\'') ) {
						alt22=1;
					}

					else {
						alt22=4;
					}

				}
				else if ( ((LA22_1 >= '\u0000' && LA22_1 <= '\t')||(LA22_1 >= '\u000B' && LA22_1 <= '&')||(LA22_1 >= '(' && LA22_1 <= '\uFFFF')) ) {
					alt22=4;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA22_0=='\"') ) {
				int LA22_2 = input.LA(2);
				if ( (LA22_2=='\"') ) {
					int LA22_5 = input.LA(3);
					if ( (LA22_5=='\"') ) {
						alt22=2;
					}

					else {
						alt22=3;
					}

				}
				else if ( ((LA22_2 >= '\u0000' && LA22_2 <= '\t')||(LA22_2 >= '\u000B' && LA22_2 <= '!')||(LA22_2 >= '#' && LA22_2 <= '\uFFFF')) ) {
					alt22=3;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 22, 0, input);
				throw nvae;
			}

			switch (alt22) {
				case 1 :
					// Truffle.g:2358:13: '\\'\\'\\'' ( options {greedy=false; } : TRIAPOS )* '\\'\\'\\''
					{
					match("'''"); 

					// Truffle.g:2358:22: ( options {greedy=false; } : TRIAPOS )*
					loop18:
					while (true) {
						int alt18=2;
						int LA18_0 = input.LA(1);
						if ( (LA18_0=='\'') ) {
							int LA18_1 = input.LA(2);
							if ( (LA18_1=='\'') ) {
								int LA18_3 = input.LA(3);
								if ( (LA18_3=='\'') ) {
									alt18=2;
								}
								else if ( ((LA18_3 >= '\u0000' && LA18_3 <= '&')||(LA18_3 >= '(' && LA18_3 <= '\uFFFF')) ) {
									alt18=1;
								}

							}
							else if ( ((LA18_1 >= '\u0000' && LA18_1 <= '&')||(LA18_1 >= '(' && LA18_1 <= '\uFFFF')) ) {
								alt18=1;
							}

						}
						else if ( ((LA18_0 >= '\u0000' && LA18_0 <= '&')||(LA18_0 >= '(' && LA18_0 <= '\uFFFF')) ) {
							alt18=1;
						}

						switch (alt18) {
						case 1 :
							// Truffle.g:2358:47: TRIAPOS
							{
							mTRIAPOS(); 

							}
							break;

						default :
							break loop18;
						}
					}

					match("'''"); 

					}
					break;
				case 2 :
					// Truffle.g:2359:13: '\"\"\"' ( options {greedy=false; } : TRIQUOTE )* '\"\"\"'
					{
					match("\"\"\""); 

					// Truffle.g:2359:19: ( options {greedy=false; } : TRIQUOTE )*
					loop19:
					while (true) {
						int alt19=2;
						int LA19_0 = input.LA(1);
						if ( (LA19_0=='\"') ) {
							int LA19_1 = input.LA(2);
							if ( (LA19_1=='\"') ) {
								int LA19_3 = input.LA(3);
								if ( (LA19_3=='\"') ) {
									alt19=2;
								}
								else if ( ((LA19_3 >= '\u0000' && LA19_3 <= '!')||(LA19_3 >= '#' && LA19_3 <= '\uFFFF')) ) {
									alt19=1;
								}

							}
							else if ( ((LA19_1 >= '\u0000' && LA19_1 <= '!')||(LA19_1 >= '#' && LA19_1 <= '\uFFFF')) ) {
								alt19=1;
							}

						}
						else if ( ((LA19_0 >= '\u0000' && LA19_0 <= '!')||(LA19_0 >= '#' && LA19_0 <= '\uFFFF')) ) {
							alt19=1;
						}

						switch (alt19) {
						case 1 :
							// Truffle.g:2359:44: TRIQUOTE
							{
							mTRIQUOTE(); 

							}
							break;

						default :
							break loop19;
						}
					}

					match("\"\"\""); 

					}
					break;
				case 3 :
					// Truffle.g:2360:13: '\"' ( ESC |~ ( '\\\\' | '\\n' | '\"' ) )* '\"'
					{
					match('\"'); 
					// Truffle.g:2360:17: ( ESC |~ ( '\\\\' | '\\n' | '\"' ) )*
					loop20:
					while (true) {
						int alt20=3;
						int LA20_0 = input.LA(1);
						if ( (LA20_0=='\\') ) {
							alt20=1;
						}
						else if ( ((LA20_0 >= '\u0000' && LA20_0 <= '\t')||(LA20_0 >= '\u000B' && LA20_0 <= '!')||(LA20_0 >= '#' && LA20_0 <= '[')||(LA20_0 >= ']' && LA20_0 <= '\uFFFF')) ) {
							alt20=2;
						}

						switch (alt20) {
						case 1 :
							// Truffle.g:2360:18: ESC
							{
							mESC(); 

							}
							break;
						case 2 :
							// Truffle.g:2360:22: ~ ( '\\\\' | '\\n' | '\"' )
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop20;
						}
					}

					match('\"'); 
					}
					break;
				case 4 :
					// Truffle.g:2361:13: '\\'' ( ESC |~ ( '\\\\' | '\\n' | '\\'' ) )* '\\''
					{
					match('\''); 
					// Truffle.g:2361:18: ( ESC |~ ( '\\\\' | '\\n' | '\\'' ) )*
					loop21:
					while (true) {
						int alt21=3;
						int LA21_0 = input.LA(1);
						if ( (LA21_0=='\\') ) {
							alt21=1;
						}
						else if ( ((LA21_0 >= '\u0000' && LA21_0 <= '\t')||(LA21_0 >= '\u000B' && LA21_0 <= '&')||(LA21_0 >= '(' && LA21_0 <= '[')||(LA21_0 >= ']' && LA21_0 <= '\uFFFF')) ) {
							alt21=2;
						}

						switch (alt21) {
						case 1 :
							// Truffle.g:2361:19: ESC
							{
							mESC(); 

							}
							break;
						case 2 :
							// Truffle.g:2361:23: ~ ( '\\\\' | '\\n' | '\\'' )
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop21;
						}
					}

					match('\''); 
					}
					break;

			}


			           if (state.tokenStartLine != input.getLine()) {
			               state.tokenStartLine = input.getLine();
			               state.tokenStartCharPositionInLine = -2;
			           }
			        
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING"

	// $ANTLR start "TRIQUOTE"
	public final void mTRIQUOTE() throws RecognitionException {
		try {
			// Truffle.g:2372:5: ( ( '\"' )? ( '\"' )? ( ESC |~ ( '\\\\' | '\"' ) )+ )
			// Truffle.g:2372:7: ( '\"' )? ( '\"' )? ( ESC |~ ( '\\\\' | '\"' ) )+
			{
			// Truffle.g:2372:7: ( '\"' )?
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0=='\"') ) {
				alt23=1;
			}
			switch (alt23) {
				case 1 :
					// Truffle.g:2372:7: '\"'
					{
					match('\"'); 
					}
					break;

			}

			// Truffle.g:2372:12: ( '\"' )?
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0=='\"') ) {
				alt24=1;
			}
			switch (alt24) {
				case 1 :
					// Truffle.g:2372:12: '\"'
					{
					match('\"'); 
					}
					break;

			}

			// Truffle.g:2372:17: ( ESC |~ ( '\\\\' | '\"' ) )+
			int cnt25=0;
			loop25:
			while (true) {
				int alt25=3;
				int LA25_0 = input.LA(1);
				if ( (LA25_0=='\\') ) {
					alt25=1;
				}
				else if ( ((LA25_0 >= '\u0000' && LA25_0 <= '!')||(LA25_0 >= '#' && LA25_0 <= '[')||(LA25_0 >= ']' && LA25_0 <= '\uFFFF')) ) {
					alt25=2;
				}

				switch (alt25) {
				case 1 :
					// Truffle.g:2372:18: ESC
					{
					mESC(); 

					}
					break;
				case 2 :
					// Truffle.g:2372:22: ~ ( '\\\\' | '\"' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt25 >= 1 ) break loop25;
					EarlyExitException eee = new EarlyExitException(25, input);
					throw eee;
				}
				cnt25++;
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TRIQUOTE"

	// $ANTLR start "TRIAPOS"
	public final void mTRIAPOS() throws RecognitionException {
		try {
			// Truffle.g:2378:5: ( ( '\\'' )? ( '\\'' )? ( ESC |~ ( '\\\\' | '\\'' ) )+ )
			// Truffle.g:2378:7: ( '\\'' )? ( '\\'' )? ( ESC |~ ( '\\\\' | '\\'' ) )+
			{
			// Truffle.g:2378:7: ( '\\'' )?
			int alt26=2;
			int LA26_0 = input.LA(1);
			if ( (LA26_0=='\'') ) {
				alt26=1;
			}
			switch (alt26) {
				case 1 :
					// Truffle.g:2378:7: '\\''
					{
					match('\''); 
					}
					break;

			}

			// Truffle.g:2378:13: ( '\\'' )?
			int alt27=2;
			int LA27_0 = input.LA(1);
			if ( (LA27_0=='\'') ) {
				alt27=1;
			}
			switch (alt27) {
				case 1 :
					// Truffle.g:2378:13: '\\''
					{
					match('\''); 
					}
					break;

			}

			// Truffle.g:2378:19: ( ESC |~ ( '\\\\' | '\\'' ) )+
			int cnt28=0;
			loop28:
			while (true) {
				int alt28=3;
				int LA28_0 = input.LA(1);
				if ( (LA28_0=='\\') ) {
					alt28=1;
				}
				else if ( ((LA28_0 >= '\u0000' && LA28_0 <= '&')||(LA28_0 >= '(' && LA28_0 <= '[')||(LA28_0 >= ']' && LA28_0 <= '\uFFFF')) ) {
					alt28=2;
				}

				switch (alt28) {
				case 1 :
					// Truffle.g:2378:20: ESC
					{
					mESC(); 

					}
					break;
				case 2 :
					// Truffle.g:2378:24: ~ ( '\\\\' | '\\'' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt28 >= 1 ) break loop28;
					EarlyExitException eee = new EarlyExitException(28, input);
					throw eee;
				}
				cnt28++;
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TRIAPOS"

	// $ANTLR start "ESC"
	public final void mESC() throws RecognitionException {
		try {
			// Truffle.g:2382:5: ( '\\\\' . )
			// Truffle.g:2382:10: '\\\\' .
			{
			match('\\'); 
			matchAny(); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ESC"

	// $ANTLR start "CONTINUED_LINE"
	public final void mCONTINUED_LINE() throws RecognitionException {
		try {
			int _type = CONTINUED_LINE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			CommonToken nl=null;

			// Truffle.g:2393:5: ( '\\\\' ( '\\r' )? '\\n' ( ' ' | '\\t' )* ( COMMENT |nl= NEWLINE |) )
			// Truffle.g:2393:10: '\\\\' ( '\\r' )? '\\n' ( ' ' | '\\t' )* ( COMMENT |nl= NEWLINE |)
			{
			match('\\'); 
			// Truffle.g:2393:15: ( '\\r' )?
			int alt29=2;
			int LA29_0 = input.LA(1);
			if ( (LA29_0=='\r') ) {
				alt29=1;
			}
			switch (alt29) {
				case 1 :
					// Truffle.g:2393:16: '\\r'
					{
					match('\r'); 
					}
					break;

			}

			match('\n'); 
			// Truffle.g:2393:28: ( ' ' | '\\t' )*
			loop30:
			while (true) {
				int alt30=2;
				int LA30_0 = input.LA(1);
				if ( (LA30_0=='\t'||LA30_0==' ') ) {
					alt30=1;
				}

				switch (alt30) {
				case 1 :
					// Truffle.g:
					{
					if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop30;
				}
			}

			 _channel=HIDDEN; 
			// Truffle.g:2394:10: ( COMMENT |nl= NEWLINE |)
			int alt31=3;
			int LA31_0 = input.LA(1);
			if ( (LA31_0=='\t'||LA31_0==' ') && ((startPos==0))) {
				alt31=1;
			}
			else if ( (LA31_0=='#') ) {
				alt31=1;
			}
			else if ( (LA31_0=='\n'||(LA31_0 >= '\f' && LA31_0 <= '\r')) ) {
				alt31=2;
			}

			else {
				alt31=3;
			}

			switch (alt31) {
				case 1 :
					// Truffle.g:2394:12: COMMENT
					{
					mCOMMENT(); 

					}
					break;
				case 2 :
					// Truffle.g:2395:12: nl= NEWLINE
					{
					int nlStart1660 = getCharIndex();
					int nlStartLine1660 = getLine();
					int nlStartCharPos1660 = getCharPositionInLine();
					mNEWLINE(); 
					nl = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nlStart1660, getCharIndex()-1);
					nl.setLine(nlStartLine1660);
					nl.setCharPositionInLine(nlStartCharPos1660);


					               emit(new CommonToken(NEWLINE,nl.getText()));
					           
					}
					break;
				case 3 :
					// Truffle.g:2400:10: 
					{
					}
					break;

			}


			               if (input.LA(1) == -1) {
			                   throw new ParseException("unexpected character after line continuation character");
			               }
			           
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CONTINUED_LINE"

	// $ANTLR start "NEWLINE"
	public final void mNEWLINE() throws RecognitionException {
		try {
			int _type = NEWLINE;
			int _channel = DEFAULT_TOKEN_CHANNEL;

			    int newlines = 0;

			// Truffle.g:2419:5: ( ( ( '\\u000C' )? ( '\\r' )? '\\n' )+ )
			// Truffle.g:2419:9: ( ( '\\u000C' )? ( '\\r' )? '\\n' )+
			{
			// Truffle.g:2419:9: ( ( '\\u000C' )? ( '\\r' )? '\\n' )+
			int cnt34=0;
			loop34:
			while (true) {
				int alt34=2;
				int LA34_0 = input.LA(1);
				if ( (LA34_0=='\n'||(LA34_0 >= '\f' && LA34_0 <= '\r')) ) {
					alt34=1;
				}

				switch (alt34) {
				case 1 :
					// Truffle.g:2419:10: ( '\\u000C' )? ( '\\r' )? '\\n'
					{
					// Truffle.g:2419:10: ( '\\u000C' )?
					int alt32=2;
					int LA32_0 = input.LA(1);
					if ( (LA32_0=='\f') ) {
						alt32=1;
					}
					switch (alt32) {
						case 1 :
							// Truffle.g:2419:11: '\\u000C'
							{
							match('\f'); 
							}
							break;

					}

					// Truffle.g:2419:21: ( '\\r' )?
					int alt33=2;
					int LA33_0 = input.LA(1);
					if ( (LA33_0=='\r') ) {
						alt33=1;
					}
					switch (alt33) {
						case 1 :
							// Truffle.g:2419:22: '\\r'
							{
							match('\r'); 
							}
							break;

					}

					match('\n'); 
					newlines++; 
					}
					break;

				default :
					if ( cnt34 >= 1 ) break loop34;
					EarlyExitException eee = new EarlyExitException(34, input);
					throw eee;
				}
				cnt34++;
			}


			         if ( startPos==0 || implicitLineJoiningLevel>0 )
			            _channel=HIDDEN;
			        
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NEWLINE"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Truffle.g:2419:5: ({...}? => ( ' ' | '\\t' | '\\u000C' )+ )
			// Truffle.g:2419:10: {...}? => ( ' ' | '\\t' | '\\u000C' )+
			{
			if ( !((startPos>0)) ) {
				throw new FailedPredicateException(input, "WS", "startPos>0");
			}
			// Truffle.g:2419:26: ( ' ' | '\\t' | '\\u000C' )+
			int cnt35=0;
			loop35:
			while (true) {
				int alt35=2;
				int LA35_0 = input.LA(1);
				if ( (LA35_0=='\t'||LA35_0=='\f'||LA35_0==' ') ) {
					alt35=1;
				}

				switch (alt35) {
				case 1 :
					// Truffle.g:
					{
					if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt35 >= 1 ) break loop35;
					EarlyExitException eee = new EarlyExitException(35, input);
					throw eee;
				}
				cnt35++;
			}

			_channel=HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	// $ANTLR start "LEADING_WS"
	public final void mLEADING_WS() throws RecognitionException {
		try {
			int _type = LEADING_WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;

			    int spaces = 0;
			    int newlines = 0;

			// Truffle.g:2439:5: ({...}? => ({...}? ( ' ' | '\\t' )+ | ( ' ' | '\\t' )+ ( ( '\\r' )? '\\n' )* ) )
			// Truffle.g:2439:9: {...}? => ({...}? ( ' ' | '\\t' )+ | ( ' ' | '\\t' )+ ( ( '\\r' )? '\\n' )* )
			{
			if ( !((startPos==0)) ) {
				throw new FailedPredicateException(input, "LEADING_WS", "startPos==0");
			}
			// Truffle.g:2440:9: ({...}? ( ' ' | '\\t' )+ | ( ' ' | '\\t' )+ ( ( '\\r' )? '\\n' )* )
			int alt40=2;
			int LA40_0 = input.LA(1);
			if ( (LA40_0==' ') ) {
				int LA40_1 = input.LA(2);
				if ( ((implicitLineJoiningLevel>0)) ) {
					alt40=1;
				}
				else if ( (true) ) {
					alt40=2;
				}

			}
			else if ( (LA40_0=='\t') ) {
				int LA40_2 = input.LA(2);
				if ( ((implicitLineJoiningLevel>0)) ) {
					alt40=1;
				}
				else if ( (true) ) {
					alt40=2;
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 40, 0, input);
				throw nvae;
			}

			switch (alt40) {
				case 1 :
					// Truffle.g:2440:13: {...}? ( ' ' | '\\t' )+
					{
					if ( !((implicitLineJoiningLevel>0)) ) {
						throw new FailedPredicateException(input, "LEADING_WS", "implicitLineJoiningLevel>0");
					}
					// Truffle.g:2440:43: ( ' ' | '\\t' )+
					int cnt36=0;
					loop36:
					while (true) {
						int alt36=2;
						int LA36_0 = input.LA(1);
						if ( (LA36_0=='\t'||LA36_0==' ') ) {
							alt36=1;
						}

						switch (alt36) {
						case 1 :
							// Truffle.g:
							{
							if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt36 >= 1 ) break loop36;
							EarlyExitException eee = new EarlyExitException(36, input);
							throw eee;
						}
						cnt36++;
					}

					_channel=HIDDEN;
					}
					break;
				case 2 :
					// Truffle.g:2441:14: ( ' ' | '\\t' )+ ( ( '\\r' )? '\\n' )*
					{
					// Truffle.g:2441:14: ( ' ' | '\\t' )+
					int cnt37=0;
					loop37:
					while (true) {
						int alt37=3;
						int LA37_0 = input.LA(1);
						if ( (LA37_0==' ') ) {
							alt37=1;
						}
						else if ( (LA37_0=='\t') ) {
							alt37=2;
						}

						switch (alt37) {
						case 1 :
							// Truffle.g:2441:20: ' '
							{
							match(' '); 
							 spaces++; 
							}
							break;
						case 2 :
							// Truffle.g:2442:19: '\\t'
							{
							match('\t'); 
							 spaces += 8; spaces -= (spaces % 8); 
							}
							break;

						default :
							if ( cnt37 >= 1 ) break loop37;
							EarlyExitException eee = new EarlyExitException(37, input);
							throw eee;
						}
						cnt37++;
					}

					// Truffle.g:2444:14: ( ( '\\r' )? '\\n' )*
					loop39:
					while (true) {
						int alt39=2;
						int LA39_0 = input.LA(1);
						if ( (LA39_0=='\n'||LA39_0=='\r') ) {
							alt39=1;
						}

						switch (alt39) {
						case 1 :
							// Truffle.g:2444:16: ( '\\r' )? '\\n'
							{
							// Truffle.g:2444:16: ( '\\r' )?
							int alt38=2;
							int LA38_0 = input.LA(1);
							if ( (LA38_0=='\r') ) {
								alt38=1;
							}
							switch (alt38) {
								case 1 :
									// Truffle.g:2444:17: '\\r'
									{
									match('\r'); 
									}
									break;

							}

							match('\n'); 
							newlines++; 
							}
							break;

						default :
							break loop39;
						}
					}


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
					                               _channel=HIDDEN;
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
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LEADING_WS"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;

			    _channel=HIDDEN;

			// Truffle.g:2526:5: ({...}? => ( ' ' | '\\t' )* '#' (~ '\\n' )* ( '\\n' )+ | '#' (~ '\\n' )* )
			int alt45=2;
			alt45 = dfa45.predict(input);
			switch (alt45) {
				case 1 :
					// Truffle.g:2526:10: {...}? => ( ' ' | '\\t' )* '#' (~ '\\n' )* ( '\\n' )+
					{
					if ( !((startPos==0)) ) {
						throw new FailedPredicateException(input, "COMMENT", "startPos==0");
					}
					// Truffle.g:2526:27: ( ' ' | '\\t' )*
					loop41:
					while (true) {
						int alt41=2;
						int LA41_0 = input.LA(1);
						if ( (LA41_0=='\t'||LA41_0==' ') ) {
							alt41=1;
						}

						switch (alt41) {
						case 1 :
							// Truffle.g:
							{
							if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop41;
						}
					}

					match('#'); 
					// Truffle.g:2526:43: (~ '\\n' )*
					loop42:
					while (true) {
						int alt42=2;
						int LA42_0 = input.LA(1);
						if ( ((LA42_0 >= '\u0000' && LA42_0 <= '\t')||(LA42_0 >= '\u000B' && LA42_0 <= '\uFFFF')) ) {
							alt42=1;
						}

						switch (alt42) {
						case 1 :
							// Truffle.g:
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop42;
						}
					}

					// Truffle.g:2526:52: ( '\\n' )+
					int cnt43=0;
					loop43:
					while (true) {
						int alt43=2;
						int LA43_0 = input.LA(1);
						if ( (LA43_0=='\n') ) {
							alt43=1;
						}

						switch (alt43) {
						case 1 :
							// Truffle.g:2526:52: '\\n'
							{
							match('\n'); 
							}
							break;

						default :
							if ( cnt43 >= 1 ) break loop43;
							EarlyExitException eee = new EarlyExitException(43, input);
							throw eee;
						}
						cnt43++;
					}

					}
					break;
				case 2 :
					// Truffle.g:2527:10: '#' (~ '\\n' )*
					{
					match('#'); 
					// Truffle.g:2527:14: (~ '\\n' )*
					loop44:
					while (true) {
						int alt44=2;
						int LA44_0 = input.LA(1);
						if ( ((LA44_0 >= '\u0000' && LA44_0 <= '\t')||(LA44_0 >= '\u000B' && LA44_0 <= '\uFFFF')) ) {
							alt44=1;
						}

						switch (alt44) {
						case 1 :
							// Truffle.g:
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop44;
						}
					}

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	@Override
	public void mTokens() throws RecognitionException {
		// Truffle.g:1:8: ( AS | ASSERT | BREAK | CLASS | CONTINUE | DEF | DELETE | ELIF | EXCEPT | FINALLY | FROM | FOR | GLOBAL | IF | IMPORT | IN | IS | LAMBDA | ORELSE | PASS | PRINT | RAISE | RETURN | TRY | WHILE | WITH | YIELD | NONE | TRUE | FALSE | NONLOCAL | LPAREN | RPAREN | LBRACK | RBRACK | COLON | COMMA | SEMI | PLUS | MINUS | STAR | SLASH | VBAR | AMPER | LESS | GREATER | ASSIGN | PERCENT | BACKQUOTE | LCURLY | RCURLY | CIRCUMFLEX | TILDE | EQUAL | NOTEQUAL | LESSEQUAL | LEFTSHIFT | GREATEREQUAL | RIGHTSHIFT | PLUSEQUAL | MINUSEQUAL | DOUBLESTAR | STAREQUAL | DOUBLESLASH | SLASHEQUAL | VBAREQUAL | PERCENTEQUAL | AMPEREQUAL | CIRCUMFLEXEQUAL | LEFTSHIFTEQUAL | RIGHTSHIFTEQUAL | DOUBLESTAREQUAL | DOUBLESLASHEQUAL | DOT | AT | AND | OR | NOT | FLOAT | INT | COMPLEX | NAME | STRING | CONTINUED_LINE | NEWLINE | WS | LEADING_WS | COMMENT )
		int alt46=88;
		alt46 = dfa46.predict(input);
		switch (alt46) {
			case 1 :
				// Truffle.g:1:10: AS
				{
				mAS(); 

				}
				break;
			case 2 :
				// Truffle.g:1:13: ASSERT
				{
				mASSERT(); 

				}
				break;
			case 3 :
				// Truffle.g:1:20: BREAK
				{
				mBREAK(); 

				}
				break;
			case 4 :
				// Truffle.g:1:26: CLASS
				{
				mCLASS(); 

				}
				break;
			case 5 :
				// Truffle.g:1:32: CONTINUE
				{
				mCONTINUE(); 

				}
				break;
			case 6 :
				// Truffle.g:1:41: DEF
				{
				mDEF(); 

				}
				break;
			case 7 :
				// Truffle.g:1:45: DELETE
				{
				mDELETE(); 

				}
				break;
			case 8 :
				// Truffle.g:1:52: ELIF
				{
				mELIF(); 

				}
				break;
			case 9 :
				// Truffle.g:1:57: EXCEPT
				{
				mEXCEPT(); 

				}
				break;
			case 10 :
				// Truffle.g:1:64: FINALLY
				{
				mFINALLY(); 

				}
				break;
			case 11 :
				// Truffle.g:1:72: FROM
				{
				mFROM(); 

				}
				break;
			case 12 :
				// Truffle.g:1:77: FOR
				{
				mFOR(); 

				}
				break;
			case 13 :
				// Truffle.g:1:81: GLOBAL
				{
				mGLOBAL(); 

				}
				break;
			case 14 :
				// Truffle.g:1:88: IF
				{
				mIF(); 

				}
				break;
			case 15 :
				// Truffle.g:1:91: IMPORT
				{
				mIMPORT(); 

				}
				break;
			case 16 :
				// Truffle.g:1:98: IN
				{
				mIN(); 

				}
				break;
			case 17 :
				// Truffle.g:1:101: IS
				{
				mIS(); 

				}
				break;
			case 18 :
				// Truffle.g:1:104: LAMBDA
				{
				mLAMBDA(); 

				}
				break;
			case 19 :
				// Truffle.g:1:111: ORELSE
				{
				mORELSE(); 

				}
				break;
			case 20 :
				// Truffle.g:1:118: PASS
				{
				mPASS(); 

				}
				break;
			case 21 :
				// Truffle.g:1:123: PRINT
				{
				mPRINT(); 

				}
				break;
			case 22 :
				// Truffle.g:1:129: RAISE
				{
				mRAISE(); 

				}
				break;
			case 23 :
				// Truffle.g:1:135: RETURN
				{
				mRETURN(); 

				}
				break;
			case 24 :
				// Truffle.g:1:142: TRY
				{
				mTRY(); 

				}
				break;
			case 25 :
				// Truffle.g:1:146: WHILE
				{
				mWHILE(); 

				}
				break;
			case 26 :
				// Truffle.g:1:152: WITH
				{
				mWITH(); 

				}
				break;
			case 27 :
				// Truffle.g:1:157: YIELD
				{
				mYIELD(); 

				}
				break;
			case 28 :
				// Truffle.g:1:163: NONE
				{
				mNONE(); 

				}
				break;
			case 29 :
				// Truffle.g:1:168: TRUE
				{
				mTRUE(); 

				}
				break;
			case 30 :
				// Truffle.g:1:173: FALSE
				{
				mFALSE(); 

				}
				break;
			case 31 :
				// Truffle.g:1:179: NONLOCAL
				{
				mNONLOCAL(); 

				}
				break;
			case 32 :
				// Truffle.g:1:188: LPAREN
				{
				mLPAREN(); 

				}
				break;
			case 33 :
				// Truffle.g:1:195: RPAREN
				{
				mRPAREN(); 

				}
				break;
			case 34 :
				// Truffle.g:1:202: LBRACK
				{
				mLBRACK(); 

				}
				break;
			case 35 :
				// Truffle.g:1:209: RBRACK
				{
				mRBRACK(); 

				}
				break;
			case 36 :
				// Truffle.g:1:216: COLON
				{
				mCOLON(); 

				}
				break;
			case 37 :
				// Truffle.g:1:222: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 38 :
				// Truffle.g:1:228: SEMI
				{
				mSEMI(); 

				}
				break;
			case 39 :
				// Truffle.g:1:233: PLUS
				{
				mPLUS(); 

				}
				break;
			case 40 :
				// Truffle.g:1:238: MINUS
				{
				mMINUS(); 

				}
				break;
			case 41 :
				// Truffle.g:1:244: STAR
				{
				mSTAR(); 

				}
				break;
			case 42 :
				// Truffle.g:1:249: SLASH
				{
				mSLASH(); 

				}
				break;
			case 43 :
				// Truffle.g:1:255: VBAR
				{
				mVBAR(); 

				}
				break;
			case 44 :
				// Truffle.g:1:260: AMPER
				{
				mAMPER(); 

				}
				break;
			case 45 :
				// Truffle.g:1:266: LESS
				{
				mLESS(); 

				}
				break;
			case 46 :
				// Truffle.g:1:271: GREATER
				{
				mGREATER(); 

				}
				break;
			case 47 :
				// Truffle.g:1:279: ASSIGN
				{
				mASSIGN(); 

				}
				break;
			case 48 :
				// Truffle.g:1:286: PERCENT
				{
				mPERCENT(); 

				}
				break;
			case 49 :
				// Truffle.g:1:294: BACKQUOTE
				{
				mBACKQUOTE(); 

				}
				break;
			case 50 :
				// Truffle.g:1:304: LCURLY
				{
				mLCURLY(); 

				}
				break;
			case 51 :
				// Truffle.g:1:311: RCURLY
				{
				mRCURLY(); 

				}
				break;
			case 52 :
				// Truffle.g:1:318: CIRCUMFLEX
				{
				mCIRCUMFLEX(); 

				}
				break;
			case 53 :
				// Truffle.g:1:329: TILDE
				{
				mTILDE(); 

				}
				break;
			case 54 :
				// Truffle.g:1:335: EQUAL
				{
				mEQUAL(); 

				}
				break;
			case 55 :
				// Truffle.g:1:341: NOTEQUAL
				{
				mNOTEQUAL(); 

				}
				break;
			case 56 :
				// Truffle.g:1:350: LESSEQUAL
				{
				mLESSEQUAL(); 

				}
				break;
			case 57 :
				// Truffle.g:1:360: LEFTSHIFT
				{
				mLEFTSHIFT(); 

				}
				break;
			case 58 :
				// Truffle.g:1:370: GREATEREQUAL
				{
				mGREATEREQUAL(); 

				}
				break;
			case 59 :
				// Truffle.g:1:383: RIGHTSHIFT
				{
				mRIGHTSHIFT(); 

				}
				break;
			case 60 :
				// Truffle.g:1:394: PLUSEQUAL
				{
				mPLUSEQUAL(); 

				}
				break;
			case 61 :
				// Truffle.g:1:404: MINUSEQUAL
				{
				mMINUSEQUAL(); 

				}
				break;
			case 62 :
				// Truffle.g:1:415: DOUBLESTAR
				{
				mDOUBLESTAR(); 

				}
				break;
			case 63 :
				// Truffle.g:1:426: STAREQUAL
				{
				mSTAREQUAL(); 

				}
				break;
			case 64 :
				// Truffle.g:1:436: DOUBLESLASH
				{
				mDOUBLESLASH(); 

				}
				break;
			case 65 :
				// Truffle.g:1:448: SLASHEQUAL
				{
				mSLASHEQUAL(); 

				}
				break;
			case 66 :
				// Truffle.g:1:459: VBAREQUAL
				{
				mVBAREQUAL(); 

				}
				break;
			case 67 :
				// Truffle.g:1:469: PERCENTEQUAL
				{
				mPERCENTEQUAL(); 

				}
				break;
			case 68 :
				// Truffle.g:1:482: AMPEREQUAL
				{
				mAMPEREQUAL(); 

				}
				break;
			case 69 :
				// Truffle.g:1:493: CIRCUMFLEXEQUAL
				{
				mCIRCUMFLEXEQUAL(); 

				}
				break;
			case 70 :
				// Truffle.g:1:509: LEFTSHIFTEQUAL
				{
				mLEFTSHIFTEQUAL(); 

				}
				break;
			case 71 :
				// Truffle.g:1:524: RIGHTSHIFTEQUAL
				{
				mRIGHTSHIFTEQUAL(); 

				}
				break;
			case 72 :
				// Truffle.g:1:540: DOUBLESTAREQUAL
				{
				mDOUBLESTAREQUAL(); 

				}
				break;
			case 73 :
				// Truffle.g:1:556: DOUBLESLASHEQUAL
				{
				mDOUBLESLASHEQUAL(); 

				}
				break;
			case 74 :
				// Truffle.g:1:573: DOT
				{
				mDOT(); 

				}
				break;
			case 75 :
				// Truffle.g:1:577: AT
				{
				mAT(); 

				}
				break;
			case 76 :
				// Truffle.g:1:580: AND
				{
				mAND(); 

				}
				break;
			case 77 :
				// Truffle.g:1:584: OR
				{
				mOR(); 

				}
				break;
			case 78 :
				// Truffle.g:1:587: NOT
				{
				mNOT(); 

				}
				break;
			case 79 :
				// Truffle.g:1:591: FLOAT
				{
				mFLOAT(); 

				}
				break;
			case 80 :
				// Truffle.g:1:597: INT
				{
				mINT(); 

				}
				break;
			case 81 :
				// Truffle.g:1:601: COMPLEX
				{
				mCOMPLEX(); 

				}
				break;
			case 82 :
				// Truffle.g:1:609: NAME
				{
				mNAME(); 

				}
				break;
			case 83 :
				// Truffle.g:1:614: STRING
				{
				mSTRING(); 

				}
				break;
			case 84 :
				// Truffle.g:1:621: CONTINUED_LINE
				{
				mCONTINUED_LINE(); 

				}
				break;
			case 85 :
				// Truffle.g:1:636: NEWLINE
				{
				mNEWLINE(); 

				}
				break;
			case 86 :
				// Truffle.g:1:644: WS
				{
				mWS(); 

				}
				break;
			case 87 :
				// Truffle.g:1:647: LEADING_WS
				{
				mLEADING_WS(); 

				}
				break;
			case 88 :
				// Truffle.g:1:658: COMMENT
				{
				mCOMMENT(); 

				}
				break;

		}
	}


	protected DFA5 dfa5 = new DFA5(this);
	protected DFA14 dfa14 = new DFA14(this);
	protected DFA45 dfa45 = new DFA45(this);
	protected DFA46 dfa46 = new DFA46(this);
	static final String DFA5_eotS =
		"\3\uffff\1\4\2\uffff";
	static final String DFA5_eofS =
		"\6\uffff";
	static final String DFA5_minS =
		"\1\56\1\uffff\1\56\1\105\2\uffff";
	static final String DFA5_maxS =
		"\1\71\1\uffff\2\145\2\uffff";
	static final String DFA5_acceptS =
		"\1\uffff\1\1\2\uffff\1\3\1\2";
	static final String DFA5_specialS =
		"\6\uffff}>";
	static final String[] DFA5_transitionS = {
			"\1\1\1\uffff\12\2",
			"",
			"\1\3\1\uffff\12\2\13\uffff\1\4\37\uffff\1\4",
			"\1\5\37\uffff\1\5",
			"",
			""
	};

	static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
	static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
	static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
	static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
	static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
	static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
	static final short[][] DFA5_transition;

	static {
		int numStates = DFA5_transitionS.length;
		DFA5_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
		}
	}

	protected class DFA5 extends DFA {

		public DFA5(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 5;
			this.eot = DFA5_eot;
			this.eof = DFA5_eof;
			this.min = DFA5_min;
			this.max = DFA5_max;
			this.accept = DFA5_accept;
			this.special = DFA5_special;
			this.transition = DFA5_transition;
		}
		@Override
		public String getDescription() {
			return "2312:1: FLOAT : ( '.' DIGITS ( Exponent )? | DIGITS '.' Exponent | DIGITS ( '.' ( DIGITS ( Exponent )? )? | Exponent ) );";
		}
	}

	static final String DFA14_eotS =
		"\4\uffff";
	static final String DFA14_eofS =
		"\4\uffff";
	static final String DFA14_minS =
		"\2\56\2\uffff";
	static final String DFA14_maxS =
		"\1\71\1\152\2\uffff";
	static final String DFA14_acceptS =
		"\2\uffff\1\2\1\1";
	static final String DFA14_specialS =
		"\4\uffff}>";
	static final String[] DFA14_transitionS = {
			"\1\2\1\uffff\12\1",
			"\1\2\1\uffff\12\1\13\uffff\1\2\4\uffff\1\3\32\uffff\1\2\4\uffff\1\3",
			"",
			""
	};

	static final short[] DFA14_eot = DFA.unpackEncodedString(DFA14_eotS);
	static final short[] DFA14_eof = DFA.unpackEncodedString(DFA14_eofS);
	static final char[] DFA14_min = DFA.unpackEncodedStringToUnsignedChars(DFA14_minS);
	static final char[] DFA14_max = DFA.unpackEncodedStringToUnsignedChars(DFA14_maxS);
	static final short[] DFA14_accept = DFA.unpackEncodedString(DFA14_acceptS);
	static final short[] DFA14_special = DFA.unpackEncodedString(DFA14_specialS);
	static final short[][] DFA14_transition;

	static {
		int numStates = DFA14_transitionS.length;
		DFA14_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA14_transition[i] = DFA.unpackEncodedString(DFA14_transitionS[i]);
		}
	}

	protected class DFA14 extends DFA {

		public DFA14(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 14;
			this.eot = DFA14_eot;
			this.eof = DFA14_eof;
			this.min = DFA14_min;
			this.max = DFA14_max;
			this.accept = DFA14_accept;
			this.special = DFA14_special;
			this.transition = DFA14_transition;
		}
		@Override
		public String getDescription() {
			return "2338:1: COMPLEX : ( ( DIGITS )+ ( 'j' | 'J' ) | FLOAT ( 'j' | 'J' ) );";
		}
	}

	static final String DFA45_eotS =
		"\2\uffff\2\4\1\uffff";
	static final String DFA45_eofS =
		"\5\uffff";
	static final String DFA45_minS =
		"\1\11\1\uffff\2\0\1\uffff";
	static final String DFA45_maxS =
		"\1\43\1\uffff\2\uffff\1\uffff";
	static final String DFA45_acceptS =
		"\1\uffff\1\1\2\uffff\1\2";
	static final String DFA45_specialS =
		"\1\0\1\uffff\1\2\1\1\1\uffff}>";
	static final String[] DFA45_transitionS = {
			"\1\1\26\uffff\1\1\2\uffff\1\2",
			"",
			"\12\3\1\1\ufff5\3",
			"\12\3\1\1\ufff5\3",
			""
	};

	static final short[] DFA45_eot = DFA.unpackEncodedString(DFA45_eotS);
	static final short[] DFA45_eof = DFA.unpackEncodedString(DFA45_eofS);
	static final char[] DFA45_min = DFA.unpackEncodedStringToUnsignedChars(DFA45_minS);
	static final char[] DFA45_max = DFA.unpackEncodedStringToUnsignedChars(DFA45_maxS);
	static final short[] DFA45_accept = DFA.unpackEncodedString(DFA45_acceptS);
	static final short[] DFA45_special = DFA.unpackEncodedString(DFA45_specialS);
	static final short[][] DFA45_transition;

	static {
		int numStates = DFA45_transitionS.length;
		DFA45_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA45_transition[i] = DFA.unpackEncodedString(DFA45_transitionS[i]);
		}
	}

	protected class DFA45 extends DFA {

		public DFA45(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 45;
			this.eot = DFA45_eot;
			this.eof = DFA45_eof;
			this.min = DFA45_min;
			this.max = DFA45_max;
			this.accept = DFA45_accept;
			this.special = DFA45_special;
			this.transition = DFA45_transition;
		}
		@Override
		public String getDescription() {
			return "2522:1: COMMENT : ({...}? => ( ' ' | '\\t' )* '#' (~ '\\n' )* ( '\\n' )+ | '#' (~ '\\n' )* );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA45_0 = input.LA(1);
						 
						int index45_0 = input.index();
						input.rewind();
						s = -1;
						if ( (LA45_0=='\t'||LA45_0==' ') && ((startPos==0))) {s = 1;}
						else if ( (LA45_0=='#') ) {s = 2;}
						 
						input.seek(index45_0);
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA45_3 = input.LA(1);
						 
						int index45_3 = input.index();
						input.rewind();
						s = -1;
						if ( (LA45_3=='\n') && ((startPos==0))) {s = 1;}
						else if ( ((LA45_3 >= '\u0000' && LA45_3 <= '\t')||(LA45_3 >= '\u000B' && LA45_3 <= '\uFFFF')) ) {s = 3;}
						else s = 4;
						 
						input.seek(index45_3);
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA45_2 = input.LA(1);
						 
						int index45_2 = input.index();
						input.rewind();
						s = -1;
						if ( ((LA45_2 >= '\u0000' && LA45_2 <= '\t')||(LA45_2 >= '\u000B' && LA45_2 <= '\uFFFF')) ) {s = 3;}
						else if ( (LA45_2=='\n') && ((startPos==0))) {s = 1;}
						else s = 4;
						 
						input.seek(index45_2);
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 45, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	static final String DFA46_eotS =
		"\1\uffff\22\63\7\uffff\1\132\1\134\1\137\1\142\1\144\1\146\1\151\1\154"+
		"\1\156\1\160\3\uffff\1\162\2\uffff\1\163\1\uffff\1\63\2\166\4\63\3\uffff"+
		"\1\u0083\1\uffff\2\u0084\1\uffff\1\u0088\14\63\1\u0096\1\63\1\u0098\1"+
		"\u0099\15\63\4\uffff\1\u00a9\2\uffff\1\u00ab\7\uffff\1\u00ad\2\uffff\1"+
		"\u00af\10\uffff\1\u00b0\1\u00b2\1\uffff\1\u00b0\1\166\3\uffff\1\166\6"+
		"\63\4\uffff\1\63\1\uffff\1\u00b8\3\63\1\u00bc\1\u00bd\5\63\1\u00c3\1\63"+
		"\1\uffff\1\63\2\uffff\5\63\1\u00cb\7\63\1\u00d3\14\uffff\1\u00b0\1\uffff"+
		"\1\u00b0\1\63\1\uffff\3\63\2\uffff\1\u00dd\1\u00de\2\63\1\u00e1\1\uffff"+
		"\3\63\1\u00e5\3\63\1\uffff\1\63\1\u00ea\1\63\1\u00ec\1\u00ed\2\63\2\uffff"+
		"\1\u00b0\1\uffff\1\u00b0\1\uffff\1\63\1\u00f3\1\u00f4\1\63\2\uffff\2\63"+
		"\1\uffff\3\63\1\uffff\1\u00fb\1\u00fc\1\63\1\u00fe\1\uffff\1\u00ff\2\uffff"+
		"\1\u0100\1\63\1\uffff\1\u00b0\1\u0102\2\uffff\1\63\1\u0104\1\63\1\u0106"+
		"\1\u0107\1\u0108\2\uffff\1\u0109\3\uffff\1\63\1\uffff\1\63\1\uffff\1\u010c"+
		"\4\uffff\1\63\1\u010e\1\uffff\1\u010f\2\uffff";
	static final String DFA46_eofS =
		"\u0110\uffff";
	static final String DFA46_minS =
		"\1\11\1\156\1\42\1\154\1\145\1\154\1\151\1\154\1\146\2\141\1\42\1\162"+
		"\1\150\1\151\1\157\1\162\1\141\1\157\7\uffff\2\75\1\52\1\57\2\75\1\74"+
		"\3\75\3\uffff\1\75\2\uffff\1\60\1\uffff\1\162\2\56\4\42\3\uffff\1\12\1"+
		"\uffff\2\11\1\uffff\1\60\1\144\2\42\1\141\1\156\1\146\1\151\1\143\1\156"+
		"\1\157\1\162\1\157\1\60\1\160\2\60\1\155\1\163\2\151\1\164\1\171\1\151"+
		"\1\164\1\145\1\156\1\165\1\154\1\156\4\uffff\1\75\2\uffff\1\75\7\uffff"+
		"\1\75\2\uffff\1\75\10\uffff\2\60\1\uffff\1\60\1\56\1\53\1\56\1\uffff\1"+
		"\56\6\42\1\uffff\1\0\2\uffff\1\145\1\uffff\1\60\1\141\1\163\1\164\2\60"+
		"\1\146\2\145\1\141\1\155\1\60\1\142\1\uffff\1\157\2\uffff\1\142\1\163"+
		"\1\156\1\163\1\165\1\60\1\154\1\150\1\154\2\145\1\163\1\154\1\60\11\uffff"+
		"\1\53\1\uffff\1\53\3\60\1\162\1\uffff\1\153\1\163\1\151\2\uffff\2\60\1"+
		"\160\1\154\1\60\1\uffff\1\141\1\162\1\144\1\60\1\164\1\145\1\162\1\uffff"+
		"\1\145\1\60\1\144\2\60\1\145\1\157\1\uffff\4\60\1\53\1\164\2\60\1\156"+
		"\2\uffff\1\164\1\154\1\uffff\1\154\1\164\1\141\1\uffff\2\60\1\156\1\60"+
		"\1\uffff\1\60\2\uffff\1\60\1\143\3\60\2\uffff\1\165\1\60\1\171\3\60\2"+
		"\uffff\1\60\3\uffff\1\141\1\uffff\1\145\1\uffff\1\60\4\uffff\1\154\1\60"+
		"\1\uffff\1\60\2\uffff";
	static final String DFA46_maxS =
		"\1\176\1\163\1\162\1\157\1\145\1\170\1\162\1\154\1\163\1\141\1\162\1\145"+
		"\1\162\2\151\1\157\1\162\1\141\1\157\7\uffff\7\75\1\76\2\75\3\uffff\1"+
		"\75\2\uffff\1\71\1\uffff\1\162\2\152\1\162\1\47\2\162\3\uffff\1\15\1\uffff"+
		"\2\43\1\uffff\1\172\1\144\1\145\1\47\1\141\1\156\1\154\1\163\1\143\1\156"+
		"\1\157\1\162\1\157\1\172\1\160\2\172\1\155\1\163\2\151\1\164\1\171\1\151"+
		"\1\164\1\145\1\156\1\165\1\154\1\164\4\uffff\1\75\2\uffff\1\75\7\uffff"+
		"\1\75\2\uffff\1\75\10\uffff\1\152\1\172\1\uffff\2\152\1\71\1\152\1\uffff"+
		"\1\152\6\47\1\uffff\1\0\2\uffff\1\145\1\uffff\1\172\1\141\1\163\1\164"+
		"\2\172\1\146\2\145\1\141\1\155\1\172\1\142\1\uffff\1\157\2\uffff\1\142"+
		"\1\163\1\156\1\163\1\165\1\172\1\154\1\150\1\154\2\145\1\163\1\154\1\172"+
		"\11\uffff\1\71\1\uffff\1\71\1\152\1\71\1\152\1\162\1\uffff\1\153\1\163"+
		"\1\151\2\uffff\2\172\1\160\1\154\1\172\1\uffff\1\141\1\162\1\144\1\172"+
		"\1\164\1\145\1\162\1\uffff\1\145\1\172\1\144\2\172\1\145\1\157\1\uffff"+
		"\1\71\1\152\1\71\1\152\1\71\1\164\2\172\1\156\2\uffff\1\164\1\154\1\uffff"+
		"\1\154\1\164\1\141\1\uffff\2\172\1\156\1\172\1\uffff\1\172\2\uffff\1\172"+
		"\1\143\1\71\1\152\1\172\2\uffff\1\165\1\172\1\171\3\172\2\uffff\1\172"+
		"\3\uffff\1\141\1\uffff\1\145\1\uffff\1\172\4\uffff\1\154\1\172\1\uffff"+
		"\1\172\2\uffff";
	static final String DFA46_acceptS =
		"\23\uffff\1\40\1\41\1\42\1\43\1\44\1\45\1\46\12\uffff\1\61\1\62\1\63\1"+
		"\uffff\1\65\1\67\1\uffff\1\113\7\uffff\1\122\1\123\1\124\1\uffff\1\125"+
		"\2\uffff\1\130\36\uffff\1\74\1\47\1\75\1\50\1\uffff\1\77\1\51\1\uffff"+
		"\1\101\1\52\1\102\1\53\1\104\1\54\1\70\1\uffff\1\55\1\72\1\uffff\1\56"+
		"\1\66\1\57\1\103\1\60\1\105\1\64\1\112\2\uffff\1\120\4\uffff\1\121\7\uffff"+
		"\1\126\1\uffff\1\127\1\130\1\uffff\1\1\15\uffff\1\16\1\uffff\1\20\1\21"+
		"\16\uffff\1\110\1\76\1\111\1\100\1\106\1\71\1\107\1\73\1\117\1\uffff\1"+
		"\115\5\uffff\1\114\3\uffff\1\6\1\7\5\uffff\1\14\7\uffff\1\30\7\uffff\1"+
		"\116\11\uffff\1\10\1\23\2\uffff\1\13\3\uffff\1\24\4\uffff\1\32\1\uffff"+
		"\1\34\1\35\5\uffff\1\3\1\4\6\uffff\1\25\1\26\1\uffff\1\31\1\33\1\36\1"+
		"\uffff\1\2\1\uffff\1\11\1\uffff\1\15\1\17\1\22\1\27\2\uffff\1\12\1\uffff"+
		"\1\5\1\37";
	static final String DFA46_specialS =
		"\1\4\65\uffff\1\0\1\uffff\1\3\1\2\112\uffff\1\1\u008b\uffff}>";
	static final String[] DFA46_transitionS = {
			"\1\71\1\67\1\uffff\1\66\1\67\22\uffff\1\70\1\51\1\64\1\72\1\uffff\1\43"+
			"\1\37\1\64\1\23\1\24\1\34\1\32\1\30\1\33\1\52\1\35\1\55\11\56\1\27\1"+
			"\31\1\40\1\42\1\41\1\uffff\1\53\1\63\1\62\3\63\1\21\7\63\1\17\3\63\1"+
			"\60\1\63\1\20\1\61\5\63\1\25\1\65\1\26\1\47\1\63\1\44\1\1\1\2\1\3\1\4"+
			"\1\5\1\6\1\7\1\63\1\10\2\63\1\11\1\63\1\22\1\54\1\12\1\63\1\13\1\63\1"+
			"\14\1\57\1\63\1\15\1\63\1\16\1\63\1\45\1\36\1\46\1\50",
			"\1\74\4\uffff\1\73",
			"\1\64\4\uffff\1\64\52\uffff\1\76\37\uffff\1\75",
			"\1\77\2\uffff\1\100",
			"\1\101",
			"\1\102\13\uffff\1\103",
			"\1\104\5\uffff\1\106\2\uffff\1\105",
			"\1\107",
			"\1\110\6\uffff\1\111\1\112\4\uffff\1\113",
			"\1\114",
			"\1\115\20\uffff\1\116",
			"\1\64\4\uffff\1\64\71\uffff\1\117\3\uffff\1\120",
			"\1\121",
			"\1\122\1\123",
			"\1\124",
			"\1\125",
			"\1\126",
			"\1\127",
			"\1\130",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\131",
			"\1\133",
			"\1\135\22\uffff\1\136",
			"\1\140\15\uffff\1\141",
			"\1\143",
			"\1\145",
			"\1\150\1\147",
			"\1\152\1\153",
			"\1\155",
			"\1\157",
			"",
			"",
			"",
			"\1\161",
			"",
			"",
			"\12\164",
			"",
			"\1\165",
			"\1\167\1\uffff\10\170\2\172\13\uffff\1\171\4\uffff\1\173\32\uffff\1"+
			"\171\4\uffff\1\173",
			"\1\167\1\uffff\12\174\13\uffff\1\171\4\uffff\1\173\32\uffff\1\171\4"+
			"\uffff\1\173",
			"\1\64\4\uffff\1\64\52\uffff\1\176\37\uffff\1\175",
			"\1\64\4\uffff\1\64",
			"\1\64\4\uffff\1\64\52\uffff\1\177\37\uffff\1\u0080",
			"\1\64\4\uffff\1\64\52\uffff\1\u0081\37\uffff\1\u0082",
			"",
			"",
			"",
			"\1\67\2\uffff\1\67",
			"",
			"\1\71\1\u0085\1\uffff\1\u0083\1\u0085\22\uffff\1\70\2\uffff\1\u0086",
			"\1\71\1\u0085\1\uffff\1\u0083\1\u0085\22\uffff\1\70\2\uffff\1\u0086",
			"",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\22\63\1\u0087\7\63",
			"\1\u0089",
			"\1\64\4\uffff\1\64\75\uffff\1\u008a",
			"\1\64\4\uffff\1\64",
			"\1\u008b",
			"\1\u008c",
			"\1\u008d\5\uffff\1\u008e",
			"\1\u008f\11\uffff\1\u0090",
			"\1\u0091",
			"\1\u0092",
			"\1\u0093",
			"\1\u0094",
			"\1\u0095",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u0097",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u009a",
			"\1\u009b",
			"\1\u009c",
			"\1\u009d",
			"\1\u009e",
			"\1\u009f",
			"\1\u00a0",
			"\1\u00a1",
			"\1\u00a2",
			"\1\u00a3",
			"\1\u00a4",
			"\1\u00a5",
			"\1\u00a6\5\uffff\1\u00a7",
			"",
			"",
			"",
			"",
			"\1\u00a8",
			"",
			"",
			"\1\u00aa",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\u00ac",
			"",
			"",
			"\1\u00ae",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\12\164\13\uffff\1\u00b1\4\uffff\1\173\32\uffff\1\u00b1\4\uffff\1\173",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"\12\u00b4\13\uffff\1\u00b3\4\uffff\1\173\32\uffff\1\u00b3\4\uffff\1"+
			"\173",
			"\1\167\1\uffff\10\170\2\172\13\uffff\1\171\4\uffff\1\173\32\uffff\1"+
			"\171\4\uffff\1\173",
			"\1\u00b5\1\uffff\1\u00b5\2\uffff\12\u00b6",
			"\1\167\1\uffff\12\172\13\uffff\1\171\4\uffff\1\173\32\uffff\1\171\4"+
			"\uffff\1\173",
			"",
			"\1\167\1\uffff\12\174\13\uffff\1\171\4\uffff\1\173\32\uffff\1\171\4"+
			"\uffff\1\173",
			"\1\64\4\uffff\1\64",
			"\1\64\4\uffff\1\64",
			"\1\64\4\uffff\1\64",
			"\1\64\4\uffff\1\64",
			"\1\64\4\uffff\1\64",
			"\1\64\4\uffff\1\64",
			"",
			"\1\uffff",
			"",
			"",
			"\1\u00b7",
			"",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u00b9",
			"\1\u00ba",
			"\1\u00bb",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u00be",
			"\1\u00bf",
			"\1\u00c0",
			"\1\u00c1",
			"\1\u00c2",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u00c4",
			"",
			"\1\u00c5",
			"",
			"",
			"\1\u00c6",
			"\1\u00c7",
			"\1\u00c8",
			"\1\u00c9",
			"\1\u00ca",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u00cc",
			"\1\u00cd",
			"\1\u00ce",
			"\1\u00cf",
			"\1\u00d0",
			"\1\u00d1",
			"\1\u00d2",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\u00d4\1\uffff\1\u00d4\2\uffff\12\u00d5",
			"",
			"\1\u00d6\1\uffff\1\u00d6\2\uffff\12\u00d7",
			"\12\u00b4\13\uffff\1\u00d8\4\uffff\1\173\32\uffff\1\u00d8\4\uffff\1"+
			"\173",
			"\12\u00b6",
			"\12\u00b6\20\uffff\1\173\37\uffff\1\173",
			"\1\u00d9",
			"",
			"\1\u00da",
			"\1\u00db",
			"\1\u00dc",
			"",
			"",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u00df",
			"\1\u00e0",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"\1\u00e2",
			"\1\u00e3",
			"\1\u00e4",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u00e6",
			"\1\u00e7",
			"\1\u00e8",
			"",
			"\1\u00e9",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u00eb",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u00ee",
			"\1\u00ef",
			"",
			"\12\u00d5",
			"\12\u00d5\20\uffff\1\173\37\uffff\1\173",
			"\12\u00d7",
			"\12\u00d7\20\uffff\1\173\37\uffff\1\173",
			"\1\u00f0\1\uffff\1\u00f0\2\uffff\12\u00f1",
			"\1\u00f2",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u00f5",
			"",
			"",
			"\1\u00f6",
			"\1\u00f7",
			"",
			"\1\u00f8",
			"\1\u00f9",
			"\1\u00fa",
			"",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u00fd",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u0101",
			"\12\u00f1",
			"\12\u00f1\20\uffff\1\173\37\uffff\1\173",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"",
			"\1\u0103",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\u0105",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"",
			"",
			"\1\u010a",
			"",
			"\1\u010b",
			"",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"",
			"",
			"",
			"\1\u010d",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			""
	};

	static final short[] DFA46_eot = DFA.unpackEncodedString(DFA46_eotS);
	static final short[] DFA46_eof = DFA.unpackEncodedString(DFA46_eofS);
	static final char[] DFA46_min = DFA.unpackEncodedStringToUnsignedChars(DFA46_minS);
	static final char[] DFA46_max = DFA.unpackEncodedStringToUnsignedChars(DFA46_maxS);
	static final short[] DFA46_accept = DFA.unpackEncodedString(DFA46_acceptS);
	static final short[] DFA46_special = DFA.unpackEncodedString(DFA46_specialS);
	static final short[][] DFA46_transition;

	static {
		int numStates = DFA46_transitionS.length;
		DFA46_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA46_transition[i] = DFA.unpackEncodedString(DFA46_transitionS[i]);
		}
	}

	protected class DFA46 extends DFA {

		public DFA46(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 46;
			this.eot = DFA46_eot;
			this.eof = DFA46_eof;
			this.min = DFA46_min;
			this.max = DFA46_max;
			this.accept = DFA46_accept;
			this.special = DFA46_special;
			this.transition = DFA46_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( AS | ASSERT | BREAK | CLASS | CONTINUE | DEF | DELETE | ELIF | EXCEPT | FINALLY | FROM | FOR | GLOBAL | IF | IMPORT | IN | IS | LAMBDA | ORELSE | PASS | PRINT | RAISE | RETURN | TRY | WHILE | WITH | YIELD | NONE | TRUE | FALSE | NONLOCAL | LPAREN | RPAREN | LBRACK | RBRACK | COLON | COMMA | SEMI | PLUS | MINUS | STAR | SLASH | VBAR | AMPER | LESS | GREATER | ASSIGN | PERCENT | BACKQUOTE | LCURLY | RCURLY | CIRCUMFLEX | TILDE | EQUAL | NOTEQUAL | LESSEQUAL | LEFTSHIFT | GREATEREQUAL | RIGHTSHIFT | PLUSEQUAL | MINUSEQUAL | DOUBLESTAR | STAREQUAL | DOUBLESLASH | SLASHEQUAL | VBAREQUAL | PERCENTEQUAL | AMPEREQUAL | CIRCUMFLEXEQUAL | LEFTSHIFTEQUAL | RIGHTSHIFTEQUAL | DOUBLESTAREQUAL | DOUBLESLASHEQUAL | DOT | AT | AND | OR | NOT | FLOAT | INT | COMPLEX | NAME | STRING | CONTINUED_LINE | NEWLINE | WS | LEADING_WS | COMMENT );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA46_54 = input.LA(1);
						 
						int index46_54 = input.index();
						input.rewind();
						s = -1;
						if ( (LA46_54=='\n'||LA46_54=='\r') ) {s = 55;}
						else s = 131;
						 
						input.seek(index46_54);
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA46_132 = input.LA(1);
						 
						int index46_132 = input.index();
						input.rewind();
						s = -1;
						if ( ((startPos>0)) ) {s = 131;}
						else if ( ((startPos==0)) ) {s = 133;}
						 
						input.seek(index46_132);
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA46_57 = input.LA(1);
						 
						int index46_57 = input.index();
						input.rewind();
						s = -1;
						if ( (LA46_57==' ') && (((startPos==0)||(startPos>0)))) {s = 56;}
						else if ( (LA46_57=='\f') && ((startPos>0))) {s = 131;}
						else if ( (LA46_57=='\n'||LA46_57=='\r') && ((startPos==0))) {s = 133;}
						else if ( (LA46_57=='\t') && (((startPos==0)||(startPos>0)))) {s = 57;}
						else if ( (LA46_57=='#') && ((startPos==0))) {s = 134;}
						else s = 132;
						 
						input.seek(index46_57);
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA46_56 = input.LA(1);
						 
						int index46_56 = input.index();
						input.rewind();
						s = -1;
						if ( (LA46_56==' ') && (((startPos==0)||(startPos>0)))) {s = 56;}
						else if ( (LA46_56=='\f') && ((startPos>0))) {s = 131;}
						else if ( (LA46_56=='\n'||LA46_56=='\r') && ((startPos==0))) {s = 133;}
						else if ( (LA46_56=='\t') && (((startPos==0)||(startPos>0)))) {s = 57;}
						else if ( (LA46_56=='#') && ((startPos==0))) {s = 134;}
						else s = 132;
						 
						input.seek(index46_56);
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA46_0 = input.LA(1);
						 
						int index46_0 = input.index();
						input.rewind();
						s = -1;
						if ( (LA46_0=='a') ) {s = 1;}
						else if ( (LA46_0=='b') ) {s = 2;}
						else if ( (LA46_0=='c') ) {s = 3;}
						else if ( (LA46_0=='d') ) {s = 4;}
						else if ( (LA46_0=='e') ) {s = 5;}
						else if ( (LA46_0=='f') ) {s = 6;}
						else if ( (LA46_0=='g') ) {s = 7;}
						else if ( (LA46_0=='i') ) {s = 8;}
						else if ( (LA46_0=='l') ) {s = 9;}
						else if ( (LA46_0=='p') ) {s = 10;}
						else if ( (LA46_0=='r') ) {s = 11;}
						else if ( (LA46_0=='t') ) {s = 12;}
						else if ( (LA46_0=='w') ) {s = 13;}
						else if ( (LA46_0=='y') ) {s = 14;}
						else if ( (LA46_0=='N') ) {s = 15;}
						else if ( (LA46_0=='T') ) {s = 16;}
						else if ( (LA46_0=='F') ) {s = 17;}
						else if ( (LA46_0=='n') ) {s = 18;}
						else if ( (LA46_0=='(') ) {s = 19;}
						else if ( (LA46_0==')') ) {s = 20;}
						else if ( (LA46_0=='[') ) {s = 21;}
						else if ( (LA46_0==']') ) {s = 22;}
						else if ( (LA46_0==':') ) {s = 23;}
						else if ( (LA46_0==',') ) {s = 24;}
						else if ( (LA46_0==';') ) {s = 25;}
						else if ( (LA46_0=='+') ) {s = 26;}
						else if ( (LA46_0=='-') ) {s = 27;}
						else if ( (LA46_0=='*') ) {s = 28;}
						else if ( (LA46_0=='/') ) {s = 29;}
						else if ( (LA46_0=='|') ) {s = 30;}
						else if ( (LA46_0=='&') ) {s = 31;}
						else if ( (LA46_0=='<') ) {s = 32;}
						else if ( (LA46_0=='>') ) {s = 33;}
						else if ( (LA46_0=='=') ) {s = 34;}
						else if ( (LA46_0=='%') ) {s = 35;}
						else if ( (LA46_0=='`') ) {s = 36;}
						else if ( (LA46_0=='{') ) {s = 37;}
						else if ( (LA46_0=='}') ) {s = 38;}
						else if ( (LA46_0=='^') ) {s = 39;}
						else if ( (LA46_0=='~') ) {s = 40;}
						else if ( (LA46_0=='!') ) {s = 41;}
						else if ( (LA46_0=='.') ) {s = 42;}
						else if ( (LA46_0=='@') ) {s = 43;}
						else if ( (LA46_0=='o') ) {s = 44;}
						else if ( (LA46_0=='0') ) {s = 45;}
						else if ( ((LA46_0 >= '1' && LA46_0 <= '9')) ) {s = 46;}
						else if ( (LA46_0=='u') ) {s = 47;}
						else if ( (LA46_0=='R') ) {s = 48;}
						else if ( (LA46_0=='U') ) {s = 49;}
						else if ( (LA46_0=='B') ) {s = 50;}
						else if ( (LA46_0=='A'||(LA46_0 >= 'C' && LA46_0 <= 'E')||(LA46_0 >= 'G' && LA46_0 <= 'M')||(LA46_0 >= 'O' && LA46_0 <= 'Q')||LA46_0=='S'||(LA46_0 >= 'V' && LA46_0 <= 'Z')||LA46_0=='_'||LA46_0=='h'||(LA46_0 >= 'j' && LA46_0 <= 'k')||LA46_0=='m'||LA46_0=='q'||LA46_0=='s'||LA46_0=='v'||LA46_0=='x'||LA46_0=='z') ) {s = 51;}
						else if ( (LA46_0=='\"'||LA46_0=='\'') ) {s = 52;}
						else if ( (LA46_0=='\\') ) {s = 53;}
						else if ( (LA46_0=='\f') ) {s = 54;}
						else if ( (LA46_0=='\n'||LA46_0=='\r') ) {s = 55;}
						else if ( (LA46_0==' ') && (((startPos==0)||(startPos>0)))) {s = 56;}
						else if ( (LA46_0=='\t') && (((startPos==0)||(startPos>0)))) {s = 57;}
						else if ( (LA46_0=='#') ) {s = 58;}
						 
						input.seek(index46_0);
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 46, _s, input);
			error(nvae);
			throw nvae;
		}
	}

}
