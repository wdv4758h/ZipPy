// $ANTLR 3.5 Truffle.g 2013-08-25 21:08:32

package edu.uci.python.antlr;

// CheckStyle: stop check
import org.antlr.runtime.CommonToken;

import edu.uci.python.nodes.PNode;
import edu.uci.python.*;
import edu.uci.python.nodes.statements.StatementNode;
import edu.uci.python.nodes.statements.ParametersNode;
import edu.uci.python.nodes.CallBuiltInNode;
import edu.uci.python.nodes.CallBuiltInWithOneArgNoKeywordNode;
import edu.uci.python.nodes.CallBuiltInWithTwoArgsNoKeywordNode;
import edu.uci.python.nodes.CallNode;
import edu.uci.python.nodes.CallWithOneArgumentNoKeywordNode;
import edu.uci.python.nodes.CallWithTwoArgumentsNoKeywordNode;
import edu.uci.python.nodes.expressions.SubscriptLoadNode;
import edu.uci.python.nodes.expressions.AttributeRefNode;
import edu.uci.python.datatypes.PAlias;
import edu.uci.python.datatypes.PComprehension;
import org.python.antlr.ast.boolopType;
import org.python.antlr.ast.cmpopType;
import org.python.antlr.ast.expr_contextType;
import org.python.antlr.ast.operatorType;
import org.python.antlr.ast.unaryopType;


import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


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
@SuppressWarnings("all")
public class TruffleParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "AMPER", "AMPEREQUAL", "AND", 
		"AS", "ASSERT", "ASSIGN", "AT", "BACKQUOTE", "BREAK", "CIRCUMFLEX", "CIRCUMFLEXEQUAL", 
		"CLASS", "COLON", "COMMA", "COMMENT", "COMPLEX", "CONTINUE", "CONTINUED_LINE", 
		"DEDENT", "DEF", "DELETE", "DIGITS", "DOT", "DOUBLESLASH", "DOUBLESLASHEQUAL", 
		"DOUBLESTAR", "DOUBLESTAREQUAL", "ELIF", "EQUAL", "ESC", "EXCEPT", "EXEC", 
		"Exponent", "FALSE", "FINALLY", "FLOAT", "FOR", "FROM", "GLOBAL", "GREATER", 
		"GREATEREQUAL", "IF", "IMPORT", "IN", "INDENT", "INT", "IS", "LAMBDA", 
		"LBRACK", "LCURLY", "LEADING_WS", "LEFTSHIFT", "LEFTSHIFTEQUAL", "LESS", 
		"LESSEQUAL", "LPAREN", "MINUS", "MINUSEQUAL", "NAME", "NEWLINE", "NONE", 
		"NONLOCAL", "NOT", "NOTEQUAL", "OR", "ORELSE", "PASS", "PERCENT", "PERCENTEQUAL", 
		"PLUS", "PLUSEQUAL", "PRINT", "RAISE", "RBRACK", "RCURLY", "RETURN", "RIGHTSHIFT", 
		"RIGHTSHIFTEQUAL", "RPAREN", "SEMI", "SLASH", "SLASHEQUAL", "STAR", "STAREQUAL", 
		"STRING", "TILDE", "TRAILBACKSLASH", "TRIAPOS", "TRIQUOTE", "TRUE", "TRY", 
		"VBAR", "VBAREQUAL", "WHILE", "WITH", "WS", "YIELD"
	};
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

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public TruffleParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public TruffleParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return TruffleParser.tokenNames; }
	@Override public String getGrammarFileName() { return "Truffle.g"; }


	    private ErrorHandlerMsg errorHandler;

	    private GrammarActionsTruffle actions = new GrammarActionsTruffle();

	    private String encoding;

	    private boolean printFunction = true;
	    private boolean unicodeLiterals = false;
	    
	    public void setErrorHandler(ErrorHandlerMsg eh) {
	        this.errorHandler = eh;
	        actions.setErrorHandler(eh);
	    }

	    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow)
	        throws RecognitionException {

	        Object o = errorHandler.recoverFromMismatchedToken(this, input, ttype, follow);
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
	      errorHandler.reportError(this, e);
	    }

	    @Override
	    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
	        //Do nothing. We will handle error display elsewhere.
	    }


	public static class single_input_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "single_input"
	// Truffle.g:249:1: single_input : ( ( NEWLINE )* EOF | simple_stmt ( NEWLINE )* EOF | compound_stmt ( NEWLINE )+ EOF );
	public final TruffleParser.single_input_return single_input() throws RecognitionException {
		TruffleParser.single_input_return retval = new TruffleParser.single_input_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token NEWLINE1=null;
		Token EOF2=null;
		Token NEWLINE4=null;
		Token EOF5=null;
		Token NEWLINE7=null;
		Token EOF8=null;
		ParserRuleReturnScope simple_stmt3 =null;
		ParserRuleReturnScope compound_stmt6 =null;

		PNode NEWLINE1_tree=null;
		PNode EOF2_tree=null;
		PNode NEWLINE4_tree=null;
		PNode EOF5_tree=null;
		PNode NEWLINE7_tree=null;
		PNode EOF8_tree=null;


		    PNode mtype = null;

		try {
			// Truffle.g:256:5: ( ( NEWLINE )* EOF | simple_stmt ( NEWLINE )* EOF | compound_stmt ( NEWLINE )+ EOF )
			int alt4=3;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==EOF||LA4_0==NEWLINE) ) {
				alt4=1;
			}
			else if ( (LA4_0==BACKQUOTE||(LA4_0 >= LBRACK && LA4_0 <= LCURLY)||(LA4_0 >= LPAREN && LA4_0 <= MINUS)||LA4_0==NAME||LA4_0==NOT||LA4_0==PLUS||LA4_0==TILDE) ) {
				alt4=2;
			}
			else if ( (LA4_0==PRINT) && (((!printFunction)||(printFunction)))) {
				alt4=2;
			}
			else if ( (LA4_0==ASSERT||LA4_0==BREAK||(LA4_0 >= COMPLEX && LA4_0 <= CONTINUE)||LA4_0==DELETE||LA4_0==FALSE||LA4_0==FLOAT||(LA4_0 >= FROM && LA4_0 <= GLOBAL)||LA4_0==IMPORT||LA4_0==INT||LA4_0==LAMBDA||(LA4_0 >= NONE && LA4_0 <= NONLOCAL)||LA4_0==PASS||LA4_0==RAISE||LA4_0==RETURN||LA4_0==STRING||LA4_0==TRUE||LA4_0==YIELD) ) {
				alt4=2;
			}
			else if ( (LA4_0==AT||LA4_0==CLASS||LA4_0==DEF||LA4_0==FOR||LA4_0==IF||LA4_0==TRY||(LA4_0 >= WHILE && LA4_0 <= WITH)) ) {
				alt4=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}

			switch (alt4) {
				case 1 :
					// Truffle.g:256:7: ( NEWLINE )* EOF
					{
					root_0 = (PNode)adaptor.nil();


					// Truffle.g:256:7: ( NEWLINE )*
					loop1:
					while (true) {
						int alt1=2;
						int LA1_0 = input.LA(1);
						if ( (LA1_0==NEWLINE) ) {
							alt1=1;
						}

						switch (alt1) {
						case 1 :
							// Truffle.g:256:7: NEWLINE
							{
							NEWLINE1=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_single_input119); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							NEWLINE1_tree = (PNode)adaptor.create(NEWLINE1);
							adaptor.addChild(root_0, NEWLINE1_tree);
							}

							}
							break;

						default :
							break loop1;
						}
					}

					EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_single_input122); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					EOF2_tree = (PNode)adaptor.create(EOF2);
					adaptor.addChild(root_0, EOF2_tree);
					}

					if ( state.backtracking==0 ) {
					        mtype = actions.makeInteractive((retval.start), new ArrayList<PNode>());
					      }
					}
					break;
				case 2 :
					// Truffle.g:260:7: simple_stmt ( NEWLINE )* EOF
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_simple_stmt_in_single_input138);
					simple_stmt3=simple_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_stmt3.getTree());

					// Truffle.g:260:19: ( NEWLINE )*
					loop2:
					while (true) {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( (LA2_0==NEWLINE) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							// Truffle.g:260:19: NEWLINE
							{
							NEWLINE4=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_single_input140); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							NEWLINE4_tree = (PNode)adaptor.create(NEWLINE4);
							adaptor.addChild(root_0, NEWLINE4_tree);
							}

							}
							break;

						default :
							break loop2;
						}
					}

					EOF5=(Token)match(input,EOF,FOLLOW_EOF_in_single_input143); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					EOF5_tree = (PNode)adaptor.create(EOF5);
					adaptor.addChild(root_0, EOF5_tree);
					}

					if ( state.backtracking==0 ) {
					        mtype = actions.makeInteractive((retval.start), actions.castStmts((simple_stmt3!=null?((TruffleParser.simple_stmt_return)simple_stmt3).stypes:null)));
					      }
					}
					break;
				case 3 :
					// Truffle.g:264:7: compound_stmt ( NEWLINE )+ EOF
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_compound_stmt_in_single_input159);
					compound_stmt6=compound_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, compound_stmt6.getTree());

					// Truffle.g:264:21: ( NEWLINE )+
					int cnt3=0;
					loop3:
					while (true) {
						int alt3=2;
						int LA3_0 = input.LA(1);
						if ( (LA3_0==NEWLINE) ) {
							alt3=1;
						}

						switch (alt3) {
						case 1 :
							// Truffle.g:264:21: NEWLINE
							{
							NEWLINE7=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_single_input161); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							NEWLINE7_tree = (PNode)adaptor.create(NEWLINE7);
							adaptor.addChild(root_0, NEWLINE7_tree);
							}

							}
							break;

						default :
							if ( cnt3 >= 1 ) break loop3;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(3, input);
							throw eee;
						}
						cnt3++;
					}

					EOF8=(Token)match(input,EOF,FOLLOW_EOF_in_single_input164); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					EOF8_tree = (PNode)adaptor.create(EOF8);
					adaptor.addChild(root_0, EOF8_tree);
					}

					if ( state.backtracking==0 ) {
					        mtype = actions.makeInteractive((retval.start), actions.castStmts((compound_stmt6!=null?((PNode)compound_stmt6.getTree()):null)));
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.tree = mtype;
			}
		}
		catch (RecognitionException re) {

			        reportError(re);
			        errorHandler.recover(this, input,re);
			        PNode badNode = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
			        retval.tree = errorHandler.errorMod(badNode.getToken());
			    
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "single_input"


	public static class file_input_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "file_input"
	// Truffle.g:278:1: file_input : ( NEWLINE | stmt )* EOF ;
	public final TruffleParser.file_input_return file_input() throws RecognitionException {
		TruffleParser.file_input_return retval = new TruffleParser.file_input_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token NEWLINE9=null;
		Token EOF11=null;
		ParserRuleReturnScope stmt10 =null;

		PNode NEWLINE9_tree=null;
		PNode EOF11_tree=null;


		    PNode mtype = null;
		    List stypes = new ArrayList();    

		try {
			// Truffle.g:296:5: ( ( NEWLINE | stmt )* EOF )
			// Truffle.g:296:7: ( NEWLINE | stmt )* EOF
			{
			root_0 = (PNode)adaptor.nil();


			// Truffle.g:296:7: ( NEWLINE | stmt )*
			loop5:
			while (true) {
				int alt5=3;
				int LA5_0 = input.LA(1);
				if ( (LA5_0==NEWLINE) ) {
					alt5=1;
				}
				else if ( (LA5_0==BACKQUOTE||(LA5_0 >= LBRACK && LA5_0 <= LCURLY)||(LA5_0 >= LPAREN && LA5_0 <= MINUS)||LA5_0==NAME||LA5_0==NOT||LA5_0==PLUS||LA5_0==TILDE) ) {
					alt5=2;
				}
				else if ( (LA5_0==PRINT) && (((!printFunction)||(printFunction)))) {
					alt5=2;
				}
				else if ( (LA5_0==ASSERT||LA5_0==AT||LA5_0==BREAK||LA5_0==CLASS||(LA5_0 >= COMPLEX && LA5_0 <= CONTINUE)||(LA5_0 >= DEF && LA5_0 <= DELETE)||LA5_0==FALSE||(LA5_0 >= FLOAT && LA5_0 <= GLOBAL)||(LA5_0 >= IF && LA5_0 <= IMPORT)||LA5_0==INT||LA5_0==LAMBDA||(LA5_0 >= NONE && LA5_0 <= NONLOCAL)||LA5_0==PASS||LA5_0==RAISE||LA5_0==RETURN||LA5_0==STRING||(LA5_0 >= TRUE && LA5_0 <= TRY)||(LA5_0 >= WHILE && LA5_0 <= WITH)||LA5_0==YIELD) ) {
					alt5=2;
				}

				switch (alt5) {
				case 1 :
					// Truffle.g:296:8: NEWLINE
					{
					NEWLINE9=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_file_input216); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NEWLINE9_tree = (PNode)adaptor.create(NEWLINE9);
					adaptor.addChild(root_0, NEWLINE9_tree);
					}

					}
					break;
				case 2 :
					// Truffle.g:297:9: stmt
					{
					pushFollow(FOLLOW_stmt_in_file_input226);
					stmt10=stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, stmt10.getTree());

					if ( state.backtracking==0 ) {
					          if ((stmt10!=null?((TruffleParser.stmt_return)stmt10).stypes:null) != null) {
					              stypes.addAll((stmt10!=null?((TruffleParser.stmt_return)stmt10).stypes:null));
					          }
					      }
					}
					break;

				default :
					break loop5;
				}
			}

			EOF11=(Token)match(input,EOF,FOLLOW_EOF_in_file_input245); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			EOF11_tree = (PNode)adaptor.create(EOF11);
			adaptor.addChild(root_0, EOF11_tree);
			}

			if ( state.backtracking==0 ) {
			//             mtype = new Module((retval.start), actions.castStmts(stypes));
			             mtype = actions.makeModule((retval.start),stypes);
			         }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (!stypes.isEmpty()) {
			        //The EOF token messes up the end offsets, so set them manually.
			        //XXX: this may no longer be true now that PythonTokenSource is
			        //     adjusting EOF offsets -- but needs testing before I remove
			        //     this.
			        PNode stop = (PNode)stypes.get(stypes.size() -1);
			        mtype.setCharStopIndex(stop.getCharStopIndex());
			        mtype.setTokenStopIndex(stop.getTokenStopIndex());
			    }

			    retval.tree = mtype;
			}
		}
		catch (RecognitionException re) {

			        reportError(re);
			        errorHandler.recover(this, input,re);
			        PNode badNode = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
			        retval.tree = errorHandler.errorMod(badNode.getToken());
			    
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "file_input"


	public static class eval_input_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "eval_input"
	// Truffle.g:318:1: eval_input : ( LEADING_WS )? ( NEWLINE )* testlist[expr_contextType.Load] ( NEWLINE )* EOF ;
	public final TruffleParser.eval_input_return eval_input() throws RecognitionException {
		TruffleParser.eval_input_return retval = new TruffleParser.eval_input_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token LEADING_WS12=null;
		Token NEWLINE13=null;
		Token NEWLINE15=null;
		Token EOF16=null;
		ParserRuleReturnScope testlist14 =null;

		PNode LEADING_WS12_tree=null;
		PNode NEWLINE13_tree=null;
		PNode NEWLINE15_tree=null;
		PNode EOF16_tree=null;


		    PNode mtype = null;

		try {
			// Truffle.g:325:5: ( ( LEADING_WS )? ( NEWLINE )* testlist[expr_contextType.Load] ( NEWLINE )* EOF )
			// Truffle.g:325:7: ( LEADING_WS )? ( NEWLINE )* testlist[expr_contextType.Load] ( NEWLINE )* EOF
			{
			root_0 = (PNode)adaptor.nil();


			// Truffle.g:325:7: ( LEADING_WS )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==LEADING_WS) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// Truffle.g:325:7: LEADING_WS
					{
					LEADING_WS12=(Token)match(input,LEADING_WS,FOLLOW_LEADING_WS_in_eval_input299); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LEADING_WS12_tree = (PNode)adaptor.create(LEADING_WS12);
					adaptor.addChild(root_0, LEADING_WS12_tree);
					}

					}
					break;

			}

			// Truffle.g:325:19: ( NEWLINE )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==NEWLINE) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// Truffle.g:325:20: NEWLINE
					{
					NEWLINE13=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_eval_input303); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NEWLINE13_tree = (PNode)adaptor.create(NEWLINE13);
					adaptor.addChild(root_0, NEWLINE13_tree);
					}

					}
					break;

				default :
					break loop7;
				}
			}

			pushFollow(FOLLOW_testlist_in_eval_input307);
			testlist14=testlist(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, testlist14.getTree());

			// Truffle.g:325:62: ( NEWLINE )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==NEWLINE) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// Truffle.g:325:63: NEWLINE
					{
					NEWLINE15=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_eval_input311); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NEWLINE15_tree = (PNode)adaptor.create(NEWLINE15);
					adaptor.addChild(root_0, NEWLINE15_tree);
					}

					}
					break;

				default :
					break loop8;
				}
			}

			EOF16=(Token)match(input,EOF,FOLLOW_EOF_in_eval_input315); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			EOF16_tree = (PNode)adaptor.create(EOF16);
			adaptor.addChild(root_0, EOF16_tree);
			}

			if ( state.backtracking==0 ) {
			        mtype = actions.makeExpression((retval.start), actions.castExpr((testlist14!=null?((PNode)testlist14.getTree()):null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.tree = mtype;
			}
		}
		catch (RecognitionException re) {

			        reportError(re);
			        errorHandler.recover(this, input,re);
			        PNode badNode = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
			        retval.tree = errorHandler.errorMod(badNode.getToken());
			    
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "eval_input"


	public static class dotted_attr_return extends ParserRuleReturnScope {
		public PNode etype;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "dotted_attr"
	// Truffle.g:340:1: dotted_attr returns [PNode etype] : n1= NAME ( ( DOT n2+= NAME )+ |) ;
	public final TruffleParser.dotted_attr_return dotted_attr() throws RecognitionException {
		TruffleParser.dotted_attr_return retval = new TruffleParser.dotted_attr_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token n1=null;
		Token DOT17=null;
		Token n2=null;
		List<Object> list_n2=null;

		PNode n1_tree=null;
		PNode DOT17_tree=null;
		PNode n2_tree=null;

		try {
			// Truffle.g:342:5: (n1= NAME ( ( DOT n2+= NAME )+ |) )
			// Truffle.g:342:7: n1= NAME ( ( DOT n2+= NAME )+ |)
			{
			root_0 = (PNode)adaptor.nil();


			n1=(Token)match(input,NAME,FOLLOW_NAME_in_dotted_attr367); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			n1_tree = (PNode)adaptor.create(n1);
			adaptor.addChild(root_0, n1_tree);
			}

			// Truffle.g:343:7: ( ( DOT n2+= NAME )+ |)
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0==DOT) ) {
				alt10=1;
			}
			else if ( (LA10_0==LPAREN||LA10_0==NEWLINE) ) {
				alt10=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}

			switch (alt10) {
				case 1 :
					// Truffle.g:343:9: ( DOT n2+= NAME )+
					{
					// Truffle.g:343:9: ( DOT n2+= NAME )+
					int cnt9=0;
					loop9:
					while (true) {
						int alt9=2;
						int LA9_0 = input.LA(1);
						if ( (LA9_0==DOT) ) {
							alt9=1;
						}

						switch (alt9) {
						case 1 :
							// Truffle.g:343:10: DOT n2+= NAME
							{
							DOT17=(Token)match(input,DOT,FOLLOW_DOT_in_dotted_attr378); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							DOT17_tree = (PNode)adaptor.create(DOT17);
							adaptor.addChild(root_0, DOT17_tree);
							}

							n2=(Token)match(input,NAME,FOLLOW_NAME_in_dotted_attr382); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							n2_tree = (PNode)adaptor.create(n2);
							adaptor.addChild(root_0, n2_tree);
							}

							if (list_n2==null) list_n2=new ArrayList<Object>();
							list_n2.add(n2);
							}
							break;

						default :
							if ( cnt9 >= 1 ) break loop9;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(9, input);
							throw eee;
						}
						cnt9++;
					}

					if ( state.backtracking==0 ) {
					            retval.etype = actions.makeDottedAttr(n1, list_n2);
					        }
					}
					break;
				case 2 :
					// Truffle.g:348:9: 
					{
					if ( state.backtracking==0 ) {
					            retval.etype = actions.makeNameNode(n1);
					        }
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "dotted_attr"


	public static class name_or_print_return extends ParserRuleReturnScope {
		public Token tok;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "name_or_print"
	// Truffle.g:356:1: name_or_print returns [Token tok] : ( NAME |{...}? => PRINT );
	public final TruffleParser.name_or_print_return name_or_print() throws RecognitionException {
		TruffleParser.name_or_print_return retval = new TruffleParser.name_or_print_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token NAME18=null;
		Token PRINT19=null;

		PNode NAME18_tree=null;
		PNode PRINT19_tree=null;

		try {
			// Truffle.g:358:5: ( NAME |{...}? => PRINT )
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0==NAME) ) {
				alt11=1;
			}
			else if ( (LA11_0==PRINT) && ((printFunction))) {
				alt11=2;
			}

			switch (alt11) {
				case 1 :
					// Truffle.g:358:7: NAME
					{
					root_0 = (PNode)adaptor.nil();


					NAME18=(Token)match(input,NAME,FOLLOW_NAME_in_name_or_print447); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NAME18_tree = (PNode)adaptor.create(NAME18);
					adaptor.addChild(root_0, NAME18_tree);
					}

					if ( state.backtracking==0 ) {
					        retval.tok = (retval.start);
					    }
					}
					break;
				case 2 :
					// Truffle.g:361:7: {...}? => PRINT
					{
					root_0 = (PNode)adaptor.nil();


					if ( !((printFunction)) ) {
						if (state.backtracking>0) {state.failed=true; return retval;}
						throw new FailedPredicateException(input, "name_or_print", "printFunction");
					}
					PRINT19=(Token)match(input,PRINT,FOLLOW_PRINT_in_name_or_print461); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					PRINT19_tree = (PNode)adaptor.create(PRINT19);
					adaptor.addChild(root_0, PRINT19_tree);
					}

					if ( state.backtracking==0 ) {
					        retval.tok = (retval.start);
					    }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "name_or_print"


	public static class attr_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "attr"
	// Truffle.g:370:1: attr : ( NAME | AND | AS | ASSERT | BREAK | CLASS | CONTINUE | DEF | DELETE | ELIF | EXCEPT | FINALLY | FROM | FOR | GLOBAL | IF | IMPORT | IN | IS | LAMBDA | NOT | OR | ORELSE | PASS | PRINT | RAISE | RETURN | TRY | WHILE | WITH | YIELD | NONE | TRUE | FALSE | NONLOCAL );
	public final TruffleParser.attr_return attr() throws RecognitionException {
		TruffleParser.attr_return retval = new TruffleParser.attr_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token set20=null;

		PNode set20_tree=null;

		try {
			// Truffle.g:371:5: ( NAME | AND | AS | ASSERT | BREAK | CLASS | CONTINUE | DEF | DELETE | ELIF | EXCEPT | FINALLY | FROM | FOR | GLOBAL | IF | IMPORT | IN | IS | LAMBDA | NOT | OR | ORELSE | PASS | PRINT | RAISE | RETURN | TRY | WHILE | WITH | YIELD | NONE | TRUE | FALSE | NONLOCAL )
			// Truffle.g:
			{
			root_0 = (PNode)adaptor.nil();


			set20=input.LT(1);
			if ( (input.LA(1) >= AND && input.LA(1) <= ASSERT)||input.LA(1)==BREAK||input.LA(1)==CLASS||input.LA(1)==CONTINUE||(input.LA(1) >= DEF && input.LA(1) <= DELETE)||input.LA(1)==ELIF||input.LA(1)==EXCEPT||(input.LA(1) >= FALSE && input.LA(1) <= FINALLY)||(input.LA(1) >= FOR && input.LA(1) <= GLOBAL)||(input.LA(1) >= IF && input.LA(1) <= IN)||(input.LA(1) >= IS && input.LA(1) <= LAMBDA)||input.LA(1)==NAME||(input.LA(1) >= NONE && input.LA(1) <= NOT)||(input.LA(1) >= OR && input.LA(1) <= PASS)||(input.LA(1) >= PRINT && input.LA(1) <= RAISE)||input.LA(1)==RETURN||(input.LA(1) >= TRUE && input.LA(1) <= TRY)||(input.LA(1) >= WHILE && input.LA(1) <= WITH)||input.LA(1)==YIELD ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (PNode)adaptor.create(set20));
				state.errorRecovery=false;
				state.failed=false;
			}
			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "attr"


	public static class decorator_return extends ParserRuleReturnScope {
		public PNode etype;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "decorator"
	// Truffle.g:410:1: decorator returns [PNode etype] : AT dotted_attr ( LPAREN ( arglist |) RPAREN |) NEWLINE ;
	public final TruffleParser.decorator_return decorator() throws RecognitionException {
		TruffleParser.decorator_return retval = new TruffleParser.decorator_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token AT21=null;
		Token LPAREN23=null;
		Token RPAREN25=null;
		Token NEWLINE26=null;
		ParserRuleReturnScope dotted_attr22 =null;
		ParserRuleReturnScope arglist24 =null;

		PNode AT21_tree=null;
		PNode LPAREN23_tree=null;
		PNode RPAREN25_tree=null;
		PNode NEWLINE26_tree=null;

		try {
			// Truffle.g:415:5: ( AT dotted_attr ( LPAREN ( arglist |) RPAREN |) NEWLINE )
			// Truffle.g:415:7: AT dotted_attr ( LPAREN ( arglist |) RPAREN |) NEWLINE
			{
			root_0 = (PNode)adaptor.nil();


			AT21=(Token)match(input,AT,FOLLOW_AT_in_decorator792); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			AT21_tree = (PNode)adaptor.create(AT21);
			adaptor.addChild(root_0, AT21_tree);
			}

			pushFollow(FOLLOW_dotted_attr_in_decorator794);
			dotted_attr22=dotted_attr();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, dotted_attr22.getTree());

			// Truffle.g:416:5: ( LPAREN ( arglist |) RPAREN |)
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==LPAREN) ) {
				alt13=1;
			}
			else if ( (LA13_0==NEWLINE) ) {
				alt13=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}

			switch (alt13) {
				case 1 :
					// Truffle.g:416:7: LPAREN ( arglist |) RPAREN
					{
					LPAREN23=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_decorator802); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LPAREN23_tree = (PNode)adaptor.create(LPAREN23);
					adaptor.addChild(root_0, LPAREN23_tree);
					}

					// Truffle.g:417:7: ( arglist |)
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0==BACKQUOTE||(LA12_0 >= LBRACK && LA12_0 <= LCURLY)||(LA12_0 >= LPAREN && LA12_0 <= MINUS)||LA12_0==NAME||LA12_0==NOT||LA12_0==PLUS||LA12_0==TILDE) ) {
						alt12=1;
					}
					else if ( (LA12_0==PRINT) && ((printFunction))) {
						alt12=1;
					}
					else if ( (LA12_0==COMPLEX||LA12_0==DOUBLESTAR||LA12_0==FALSE||LA12_0==FLOAT||LA12_0==INT||LA12_0==LAMBDA||LA12_0==NONE||LA12_0==STAR||LA12_0==STRING||LA12_0==TRUE) ) {
						alt12=1;
					}
					else if ( (LA12_0==RPAREN) ) {
						alt12=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 12, 0, input);
						throw nvae;
					}

					switch (alt12) {
						case 1 :
							// Truffle.g:417:9: arglist
							{
							pushFollow(FOLLOW_arglist_in_decorator812);
							arglist24=arglist();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arglist24.getTree());

							if ( state.backtracking==0 ) {
							            retval.etype = actions.makeCall(LPAREN23, (dotted_attr22!=null?((TruffleParser.dotted_attr_return)dotted_attr22).etype:null), (arglist24!=null?((TruffleParser.arglist_return)arglist24).args:null), (arglist24!=null?((TruffleParser.arglist_return)arglist24).keywords:null), (arglist24!=null?((TruffleParser.arglist_return)arglist24).starargs:null), (arglist24!=null?((TruffleParser.arglist_return)arglist24).kwargs:null));
							        }
							}
							break;
						case 2 :
							// Truffle.g:422:9: 
							{
							if ( state.backtracking==0 ) {
							            retval.etype = actions.makeCall(LPAREN23, (dotted_attr22!=null?((TruffleParser.dotted_attr_return)dotted_attr22).etype:null));
							        }
							}
							break;

					}

					RPAREN25=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_decorator856); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					RPAREN25_tree = (PNode)adaptor.create(RPAREN25);
					adaptor.addChild(root_0, RPAREN25_tree);
					}

					}
					break;
				case 2 :
					// Truffle.g:428:7: 
					{
					if ( state.backtracking==0 ) {
					          retval.etype = (dotted_attr22!=null?((TruffleParser.dotted_attr_return)dotted_attr22).etype:null);
					      }
					}
					break;

			}

			NEWLINE26=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_decorator878); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			NEWLINE26_tree = (PNode)adaptor.create(NEWLINE26);
			adaptor.addChild(root_0, NEWLINE26_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = retval.etype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "decorator"


	public static class decorators_return extends ParserRuleReturnScope {
		public List etypes;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "decorators"
	// Truffle.g:435:1: decorators returns [List etypes] : (d+= decorator )+ ;
	public final TruffleParser.decorators_return decorators() throws RecognitionException {
		TruffleParser.decorators_return retval = new TruffleParser.decorators_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		List<Object> list_d=null;
		RuleReturnScope d = null;

		try {
			// Truffle.g:437:5: ( (d+= decorator )+ )
			// Truffle.g:437:7: (d+= decorator )+
			{
			root_0 = (PNode)adaptor.nil();


			// Truffle.g:437:8: (d+= decorator )+
			int cnt14=0;
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( (LA14_0==AT) ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
					// Truffle.g:437:8: d+= decorator
					{
					pushFollow(FOLLOW_decorator_in_decorators906);
					d=decorator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, d.getTree());

					if (list_d==null) list_d=new ArrayList<Object>();
					list_d.add(d.getTree());
					}
					break;

				default :
					if ( cnt14 >= 1 ) break loop14;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(14, input);
					throw eee;
				}
				cnt14++;
			}

			if ( state.backtracking==0 ) {
			          retval.etypes = list_d;
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "decorators"


	public static class funcdef_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "funcdef"
	// Truffle.g:444:1: funcdef : ( decorators )? DEF name_or_print parameters COLON suite[false] ;
	public final TruffleParser.funcdef_return funcdef() throws RecognitionException {
		TruffleParser.funcdef_return retval = new TruffleParser.funcdef_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token DEF28=null;
		Token COLON31=null;
		ParserRuleReturnScope decorators27 =null;
		ParserRuleReturnScope name_or_print29 =null;
		ParserRuleReturnScope parameters30 =null;
		ParserRuleReturnScope suite32 =null;

		PNode DEF28_tree=null;
		PNode COLON31_tree=null;


		    StatementNode stype = null;
		    actions.beginScope();

		try {
			// Truffle.g:453:5: ( ( decorators )? DEF name_or_print parameters COLON suite[false] )
			// Truffle.g:453:7: ( decorators )? DEF name_or_print parameters COLON suite[false]
			{
			root_0 = (PNode)adaptor.nil();


			// Truffle.g:453:7: ( decorators )?
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==AT) ) {
				alt15=1;
			}
			switch (alt15) {
				case 1 :
					// Truffle.g:453:7: decorators
					{
					pushFollow(FOLLOW_decorators_in_funcdef944);
					decorators27=decorators();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, decorators27.getTree());

					}
					break;

			}

			DEF28=(Token)match(input,DEF,FOLLOW_DEF_in_funcdef947); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			DEF28_tree = (PNode)adaptor.create(DEF28);
			adaptor.addChild(root_0, DEF28_tree);
			}

			pushFollow(FOLLOW_name_or_print_in_funcdef949);
			name_or_print29=name_or_print();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, name_or_print29.getTree());

			pushFollow(FOLLOW_parameters_in_funcdef951);
			parameters30=parameters();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, parameters30.getTree());

			COLON31=(Token)match(input,COLON,FOLLOW_COLON_in_funcdef953); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON31_tree = (PNode)adaptor.create(COLON31);
			adaptor.addChild(root_0, COLON31_tree);
			}

			pushFollow(FOLLOW_suite_in_funcdef955);
			suite32=suite(false);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, suite32.getTree());

			if ( state.backtracking==0 ) {
			        Token t = DEF28;
			        if ((decorators27!=null?(decorators27.start):null) != null) {
			            t = (decorators27!=null?(decorators27.start):null);
			        }
			        stype = actions.makeFuncdef(t, (name_or_print29!=null?(name_or_print29.start):null), (parameters30!=null?((TruffleParser.parameters_return)parameters30).args:null), (suite32!=null?((TruffleParser.suite_return)suite32).stypes:null), (decorators27!=null?((TruffleParser.decorators_return)decorators27).etypes:null));
			    }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "funcdef"


	public static class parameters_return extends ParserRuleReturnScope {
		public ParametersNode args;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "parameters"
	// Truffle.g:464:1: parameters returns [ParametersNode args] : LPAREN ( varargslist |) RPAREN ;
	public final TruffleParser.parameters_return parameters() throws RecognitionException {
		TruffleParser.parameters_return retval = new TruffleParser.parameters_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token LPAREN33=null;
		Token RPAREN35=null;
		ParserRuleReturnScope varargslist34 =null;

		PNode LPAREN33_tree=null;
		PNode RPAREN35_tree=null;

		try {
			// Truffle.g:466:5: ( LPAREN ( varargslist |) RPAREN )
			// Truffle.g:466:7: LPAREN ( varargslist |) RPAREN
			{
			root_0 = (PNode)adaptor.nil();


			LPAREN33=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_parameters988); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			LPAREN33_tree = (PNode)adaptor.create(LPAREN33);
			adaptor.addChild(root_0, LPAREN33_tree);
			}

			// Truffle.g:467:7: ( varargslist |)
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==DOUBLESTAR||LA16_0==LPAREN||LA16_0==NAME||LA16_0==STAR) ) {
				alt16=1;
			}
			else if ( (LA16_0==RPAREN) ) {
				alt16=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 16, 0, input);
				throw nvae;
			}

			switch (alt16) {
				case 1 :
					// Truffle.g:467:8: varargslist
					{
					pushFollow(FOLLOW_varargslist_in_parameters997);
					varargslist34=varargslist();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, varargslist34.getTree());

					if ( state.backtracking==0 ) {
					              retval.args = (varargslist34!=null?((TruffleParser.varargslist_return)varargslist34).args:null);
					        }
					}
					break;
				case 2 :
					// Truffle.g:472:9: 
					{
					if ( state.backtracking==0 ) {
					            retval.args = actions.makeArguments((retval.start), null, null, null, null);
					//            retval.args = new arguments((retval.start), new ArrayList<PNode>(), null, null, new ArrayList<PNode>());
					        }
					}
					break;

			}

			RPAREN35=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_parameters1041); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			RPAREN35_tree = (PNode)adaptor.create(RPAREN35);
			adaptor.addChild(root_0, RPAREN35_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "parameters"


	public static class defparameter_return extends ParserRuleReturnScope {
		public PNode etype;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "defparameter"
	// Truffle.g:481:1: defparameter[List defaults] returns [PNode etype] : fpdef[expr_contextType.Param] ( ASSIGN test[expr_contextType.Load] )? ;
	public final TruffleParser.defparameter_return defparameter(List defaults) throws RecognitionException {
		TruffleParser.defparameter_return retval = new TruffleParser.defparameter_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token ASSIGN37=null;
		ParserRuleReturnScope fpdef36 =null;
		ParserRuleReturnScope test38 =null;

		PNode ASSIGN37_tree=null;

		try {
			// Truffle.g:486:5: ( fpdef[expr_contextType.Param] ( ASSIGN test[expr_contextType.Load] )? )
			// Truffle.g:486:7: fpdef[expr_contextType.Param] ( ASSIGN test[expr_contextType.Load] )?
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_fpdef_in_defparameter1074);
			fpdef36=fpdef(expr_contextType.Param);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, fpdef36.getTree());

			// Truffle.g:486:37: ( ASSIGN test[expr_contextType.Load] )?
			int alt17=2;
			int LA17_0 = input.LA(1);
			if ( (LA17_0==ASSIGN) ) {
				alt17=1;
			}
			switch (alt17) {
				case 1 :
					// Truffle.g:486:38: ASSIGN test[expr_contextType.Load]
					{
					ASSIGN37=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_defparameter1078); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ASSIGN37_tree = (PNode)adaptor.create(ASSIGN37);
					adaptor.addChild(root_0, ASSIGN37_tree);
					}

					pushFollow(FOLLOW_test_in_defparameter1080);
					test38=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, test38.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          retval.etype = actions.castExpr((fpdef36!=null?((PNode)fpdef36.getTree()):null));
			          if (ASSIGN37 != null) {
			              defaults.add((test38!=null?((PNode)test38.getTree()):null));
			          } else if (!defaults.isEmpty()) {
			              throw new ParseException("non-default argument follows default argument");
			          }
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = retval.etype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "defparameter"


	public static class varargslist_return extends ParserRuleReturnScope {
		public ParametersNode args;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "varargslist"
	// Truffle.g:500:1: varargslist returns [ParametersNode args] : (d+= defparameter[defaults] ( options {greedy=true; } : COMMA d+= defparameter[defaults] )* ( COMMA ( STAR starargs= NAME ( COMMA DOUBLESTAR kwargs= NAME )? | DOUBLESTAR kwargs= NAME )? )? | STAR starargs= NAME ( COMMA DOUBLESTAR kwargs= NAME )? | DOUBLESTAR kwargs= NAME );
	public final TruffleParser.varargslist_return varargslist() throws RecognitionException {
		TruffleParser.varargslist_return retval = new TruffleParser.varargslist_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token starargs=null;
		Token kwargs=null;
		Token COMMA39=null;
		Token COMMA40=null;
		Token STAR41=null;
		Token COMMA42=null;
		Token DOUBLESTAR43=null;
		Token DOUBLESTAR44=null;
		Token STAR45=null;
		Token COMMA46=null;
		Token DOUBLESTAR47=null;
		Token DOUBLESTAR48=null;
		List<Object> list_d=null;
		RuleReturnScope d = null;
		PNode starargs_tree=null;
		PNode kwargs_tree=null;
		PNode COMMA39_tree=null;
		PNode COMMA40_tree=null;
		PNode STAR41_tree=null;
		PNode COMMA42_tree=null;
		PNode DOUBLESTAR43_tree=null;
		PNode DOUBLESTAR44_tree=null;
		PNode STAR45_tree=null;
		PNode COMMA46_tree=null;
		PNode DOUBLESTAR47_tree=null;
		PNode DOUBLESTAR48_tree=null;


		    List defaults = new ArrayList();

		try {
			// Truffle.g:505:5: (d+= defparameter[defaults] ( options {greedy=true; } : COMMA d+= defparameter[defaults] )* ( COMMA ( STAR starargs= NAME ( COMMA DOUBLESTAR kwargs= NAME )? | DOUBLESTAR kwargs= NAME )? )? | STAR starargs= NAME ( COMMA DOUBLESTAR kwargs= NAME )? | DOUBLESTAR kwargs= NAME )
			int alt23=3;
			switch ( input.LA(1) ) {
			case LPAREN:
			case NAME:
				{
				alt23=1;
				}
				break;
			case STAR:
				{
				alt23=2;
				}
				break;
			case DOUBLESTAR:
				{
				alt23=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 23, 0, input);
				throw nvae;
			}
			switch (alt23) {
				case 1 :
					// Truffle.g:505:7: d+= defparameter[defaults] ( options {greedy=true; } : COMMA d+= defparameter[defaults] )* ( COMMA ( STAR starargs= NAME ( COMMA DOUBLESTAR kwargs= NAME )? | DOUBLESTAR kwargs= NAME )? )?
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_defparameter_in_varargslist1126);
					d=defparameter(defaults);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, d.getTree());

					if (list_d==null) list_d=new ArrayList<Object>();
					list_d.add(d.getTree());
					// Truffle.g:505:33: ( options {greedy=true; } : COMMA d+= defparameter[defaults] )*
					loop18:
					while (true) {
						int alt18=2;
						int LA18_0 = input.LA(1);
						if ( (LA18_0==COMMA) ) {
							int LA18_1 = input.LA(2);
							if ( (LA18_1==LPAREN||LA18_1==NAME) ) {
								alt18=1;
							}

						}

						switch (alt18) {
						case 1 :
							// Truffle.g:505:57: COMMA d+= defparameter[defaults]
							{
							COMMA39=(Token)match(input,COMMA,FOLLOW_COMMA_in_varargslist1137); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA39_tree = (PNode)adaptor.create(COMMA39);
							adaptor.addChild(root_0, COMMA39_tree);
							}

							pushFollow(FOLLOW_defparameter_in_varargslist1141);
							d=defparameter(defaults);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, d.getTree());

							if (list_d==null) list_d=new ArrayList<Object>();
							list_d.add(d.getTree());
							}
							break;

						default :
							break loop18;
						}
					}

					// Truffle.g:506:7: ( COMMA ( STAR starargs= NAME ( COMMA DOUBLESTAR kwargs= NAME )? | DOUBLESTAR kwargs= NAME )? )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( (LA21_0==COMMA) ) {
						alt21=1;
					}
					switch (alt21) {
						case 1 :
							// Truffle.g:506:8: COMMA ( STAR starargs= NAME ( COMMA DOUBLESTAR kwargs= NAME )? | DOUBLESTAR kwargs= NAME )?
							{
							COMMA40=(Token)match(input,COMMA,FOLLOW_COMMA_in_varargslist1153); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA40_tree = (PNode)adaptor.create(COMMA40);
							adaptor.addChild(root_0, COMMA40_tree);
							}

							// Truffle.g:507:11: ( STAR starargs= NAME ( COMMA DOUBLESTAR kwargs= NAME )? | DOUBLESTAR kwargs= NAME )?
							int alt20=3;
							int LA20_0 = input.LA(1);
							if ( (LA20_0==STAR) ) {
								alt20=1;
							}
							else if ( (LA20_0==DOUBLESTAR) ) {
								alt20=2;
							}
							switch (alt20) {
								case 1 :
									// Truffle.g:507:12: STAR starargs= NAME ( COMMA DOUBLESTAR kwargs= NAME )?
									{
									STAR41=(Token)match(input,STAR,FOLLOW_STAR_in_varargslist1166); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									STAR41_tree = (PNode)adaptor.create(STAR41);
									adaptor.addChild(root_0, STAR41_tree);
									}

									starargs=(Token)match(input,NAME,FOLLOW_NAME_in_varargslist1170); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									starargs_tree = (PNode)adaptor.create(starargs);
									adaptor.addChild(root_0, starargs_tree);
									}

									// Truffle.g:507:31: ( COMMA DOUBLESTAR kwargs= NAME )?
									int alt19=2;
									int LA19_0 = input.LA(1);
									if ( (LA19_0==COMMA) ) {
										alt19=1;
									}
									switch (alt19) {
										case 1 :
											// Truffle.g:507:32: COMMA DOUBLESTAR kwargs= NAME
											{
											COMMA42=(Token)match(input,COMMA,FOLLOW_COMMA_in_varargslist1173); if (state.failed) return retval;
											if ( state.backtracking==0 ) {
											COMMA42_tree = (PNode)adaptor.create(COMMA42);
											adaptor.addChild(root_0, COMMA42_tree);
											}

											DOUBLESTAR43=(Token)match(input,DOUBLESTAR,FOLLOW_DOUBLESTAR_in_varargslist1175); if (state.failed) return retval;
											if ( state.backtracking==0 ) {
											DOUBLESTAR43_tree = (PNode)adaptor.create(DOUBLESTAR43);
											adaptor.addChild(root_0, DOUBLESTAR43_tree);
											}

											kwargs=(Token)match(input,NAME,FOLLOW_NAME_in_varargslist1179); if (state.failed) return retval;
											if ( state.backtracking==0 ) {
											kwargs_tree = (PNode)adaptor.create(kwargs);
											adaptor.addChild(root_0, kwargs_tree);
											}

											}
											break;

									}

									}
									break;
								case 2 :
									// Truffle.g:508:13: DOUBLESTAR kwargs= NAME
									{
									DOUBLESTAR44=(Token)match(input,DOUBLESTAR,FOLLOW_DOUBLESTAR_in_varargslist1195); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									DOUBLESTAR44_tree = (PNode)adaptor.create(DOUBLESTAR44);
									adaptor.addChild(root_0, DOUBLESTAR44_tree);
									}

									kwargs=(Token)match(input,NAME,FOLLOW_NAME_in_varargslist1199); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									kwargs_tree = (PNode)adaptor.create(kwargs);
									adaptor.addChild(root_0, kwargs_tree);
									}

									}
									break;

							}

							}
							break;

					}

					if ( state.backtracking==0 ) {
					          retval.args = actions.makeArgumentsType((retval.start), list_d, starargs, kwargs, defaults);
					      }
					}
					break;
				case 2 :
					// Truffle.g:514:7: STAR starargs= NAME ( COMMA DOUBLESTAR kwargs= NAME )?
					{
					root_0 = (PNode)adaptor.nil();


					STAR45=(Token)match(input,STAR,FOLLOW_STAR_in_varargslist1237); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					STAR45_tree = (PNode)adaptor.create(STAR45);
					adaptor.addChild(root_0, STAR45_tree);
					}

					starargs=(Token)match(input,NAME,FOLLOW_NAME_in_varargslist1241); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					starargs_tree = (PNode)adaptor.create(starargs);
					adaptor.addChild(root_0, starargs_tree);
					}

					// Truffle.g:514:26: ( COMMA DOUBLESTAR kwargs= NAME )?
					int alt22=2;
					int LA22_0 = input.LA(1);
					if ( (LA22_0==COMMA) ) {
						alt22=1;
					}
					switch (alt22) {
						case 1 :
							// Truffle.g:514:27: COMMA DOUBLESTAR kwargs= NAME
							{
							COMMA46=(Token)match(input,COMMA,FOLLOW_COMMA_in_varargslist1244); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA46_tree = (PNode)adaptor.create(COMMA46);
							adaptor.addChild(root_0, COMMA46_tree);
							}

							DOUBLESTAR47=(Token)match(input,DOUBLESTAR,FOLLOW_DOUBLESTAR_in_varargslist1246); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							DOUBLESTAR47_tree = (PNode)adaptor.create(DOUBLESTAR47);
							adaptor.addChild(root_0, DOUBLESTAR47_tree);
							}

							kwargs=(Token)match(input,NAME,FOLLOW_NAME_in_varargslist1250); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							kwargs_tree = (PNode)adaptor.create(kwargs);
							adaptor.addChild(root_0, kwargs_tree);
							}

							}
							break;

					}

					if ( state.backtracking==0 ) {
					          retval.args = actions.makeArgumentsType((retval.start), list_d, starargs, kwargs, defaults);
					      }
					}
					break;
				case 3 :
					// Truffle.g:518:7: DOUBLESTAR kwargs= NAME
					{
					root_0 = (PNode)adaptor.nil();


					DOUBLESTAR48=(Token)match(input,DOUBLESTAR,FOLLOW_DOUBLESTAR_in_varargslist1268); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOUBLESTAR48_tree = (PNode)adaptor.create(DOUBLESTAR48);
					adaptor.addChild(root_0, DOUBLESTAR48_tree);
					}

					kwargs=(Token)match(input,NAME,FOLLOW_NAME_in_varargslist1272); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					kwargs_tree = (PNode)adaptor.create(kwargs);
					adaptor.addChild(root_0, kwargs_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.args = actions.makeArgumentsType((retval.start), list_d, null, kwargs, defaults);
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "varargslist"


	public static class fpdef_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "fpdef"
	// Truffle.g:525:1: fpdef[expr_contextType ctype] : ( NAME | ( LPAREN fpdef[null] COMMA )=> LPAREN fplist RPAREN | LPAREN ! fplist RPAREN !);
	public final TruffleParser.fpdef_return fpdef(expr_contextType ctype) throws RecognitionException {
		TruffleParser.fpdef_return retval = new TruffleParser.fpdef_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token NAME49=null;
		Token LPAREN50=null;
		Token RPAREN52=null;
		Token LPAREN53=null;
		Token RPAREN55=null;
		ParserRuleReturnScope fplist51 =null;
		ParserRuleReturnScope fplist54 =null;

		PNode NAME49_tree=null;
		PNode LPAREN50_tree=null;
		PNode RPAREN52_tree=null;
		PNode LPAREN53_tree=null;
		PNode RPAREN55_tree=null;


		    PNode etype = null;

		try {
			// Truffle.g:535:5: ( NAME | ( LPAREN fpdef[null] COMMA )=> LPAREN fplist RPAREN | LPAREN ! fplist RPAREN !)
			int alt24=3;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==NAME) ) {
				alt24=1;
			}
			else if ( (LA24_0==LPAREN) ) {
				int LA24_2 = input.LA(2);
				if ( (synpred1_Truffle()) ) {
					alt24=2;
				}
				else if ( (true) ) {
					alt24=3;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 24, 0, input);
				throw nvae;
			}

			switch (alt24) {
				case 1 :
					// Truffle.g:535:7: NAME
					{
					root_0 = (PNode)adaptor.nil();


					NAME49=(Token)match(input,NAME,FOLLOW_NAME_in_fpdef1309); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NAME49_tree = (PNode)adaptor.create(NAME49);
					adaptor.addChild(root_0, NAME49_tree);
					}

					if ( state.backtracking==0 ) {
					          etype = actions.makeName(NAME49, (NAME49!=null?NAME49.getText():null), ctype);
					      }
					}
					break;
				case 2 :
					// Truffle.g:539:7: ( LPAREN fpdef[null] COMMA )=> LPAREN fplist RPAREN
					{
					root_0 = (PNode)adaptor.nil();


					LPAREN50=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_fpdef1336); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LPAREN50_tree = (PNode)adaptor.create(LPAREN50);
					adaptor.addChild(root_0, LPAREN50_tree);
					}

					pushFollow(FOLLOW_fplist_in_fpdef1338);
					fplist51=fplist();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, fplist51.getTree());

					RPAREN52=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_fpdef1340); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					RPAREN52_tree = (PNode)adaptor.create(RPAREN52);
					adaptor.addChild(root_0, RPAREN52_tree);
					}

					if ( state.backtracking==0 ) {
					          etype = actions.makeTuple((fplist51!=null?(fplist51.start):null), actions.castExprs((fplist51!=null?((TruffleParser.fplist_return)fplist51).etypes:null)), expr_contextType.Store);
					      }
					}
					break;
				case 3 :
					// Truffle.g:543:7: LPAREN ! fplist RPAREN !
					{
					root_0 = (PNode)adaptor.nil();


					LPAREN53=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_fpdef1356); if (state.failed) return retval;
					pushFollow(FOLLOW_fplist_in_fpdef1359);
					fplist54=fplist();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, fplist54.getTree());

					RPAREN55=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_fpdef1361); if (state.failed) return retval;
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (etype != null) {
			        retval.tree = etype;
			    }
			    actions.checkAssign(actions.castExpr(retval.tree));
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "fpdef"


	public static class fplist_return extends ParserRuleReturnScope {
		public List etypes;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "fplist"
	// Truffle.g:547:1: fplist returns [List etypes] :f+= fpdef[expr_contextType.Store] ( options {greedy=true; } : COMMA f+= fpdef[expr_contextType.Store] )* ( COMMA )? ;
	public final TruffleParser.fplist_return fplist() throws RecognitionException {
		TruffleParser.fplist_return retval = new TruffleParser.fplist_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token COMMA56=null;
		Token COMMA57=null;
		List<Object> list_f=null;
		RuleReturnScope f = null;
		PNode COMMA56_tree=null;
		PNode COMMA57_tree=null;

		try {
			// Truffle.g:549:5: (f+= fpdef[expr_contextType.Store] ( options {greedy=true; } : COMMA f+= fpdef[expr_contextType.Store] )* ( COMMA )? )
			// Truffle.g:549:7: f+= fpdef[expr_contextType.Store] ( options {greedy=true; } : COMMA f+= fpdef[expr_contextType.Store] )* ( COMMA )?
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_fpdef_in_fplist1390);
			f=fpdef(expr_contextType.Store);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, f.getTree());

			if (list_f==null) list_f=new ArrayList<Object>();
			list_f.add(f.getTree());
			// Truffle.g:550:7: ( options {greedy=true; } : COMMA f+= fpdef[expr_contextType.Store] )*
			loop25:
			while (true) {
				int alt25=2;
				int LA25_0 = input.LA(1);
				if ( (LA25_0==COMMA) ) {
					int LA25_1 = input.LA(2);
					if ( (LA25_1==LPAREN||LA25_1==NAME) ) {
						alt25=1;
					}

				}

				switch (alt25) {
				case 1 :
					// Truffle.g:550:31: COMMA f+= fpdef[expr_contextType.Store]
					{
					COMMA56=(Token)match(input,COMMA,FOLLOW_COMMA_in_fplist1407); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA56_tree = (PNode)adaptor.create(COMMA56);
					adaptor.addChild(root_0, COMMA56_tree);
					}

					pushFollow(FOLLOW_fpdef_in_fplist1411);
					f=fpdef(expr_contextType.Store);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, f.getTree());

					if (list_f==null) list_f=new ArrayList<Object>();
					list_f.add(f.getTree());
					}
					break;

				default :
					break loop25;
				}
			}

			// Truffle.g:550:72: ( COMMA )?
			int alt26=2;
			int LA26_0 = input.LA(1);
			if ( (LA26_0==COMMA) ) {
				alt26=1;
			}
			switch (alt26) {
				case 1 :
					// Truffle.g:550:73: COMMA
					{
					COMMA57=(Token)match(input,COMMA,FOLLOW_COMMA_in_fplist1417); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA57_tree = (PNode)adaptor.create(COMMA57);
					adaptor.addChild(root_0, COMMA57_tree);
					}

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          retval.etypes = list_f;
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "fplist"


	public static class stmt_return extends ParserRuleReturnScope {
		public List stypes;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "stmt"
	// Truffle.g:557:1: stmt returns [List stypes] : ( simple_stmt | compound_stmt );
	public final TruffleParser.stmt_return stmt() throws RecognitionException {
		TruffleParser.stmt_return retval = new TruffleParser.stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		ParserRuleReturnScope simple_stmt58 =null;
		ParserRuleReturnScope compound_stmt59 =null;


		try {
			// Truffle.g:559:5: ( simple_stmt | compound_stmt )
			int alt27=2;
			int LA27_0 = input.LA(1);
			if ( (LA27_0==BACKQUOTE||(LA27_0 >= LBRACK && LA27_0 <= LCURLY)||(LA27_0 >= LPAREN && LA27_0 <= MINUS)||LA27_0==NAME||LA27_0==NOT||LA27_0==PLUS||LA27_0==TILDE) ) {
				alt27=1;
			}
			else if ( (LA27_0==PRINT) && (((!printFunction)||(printFunction)))) {
				alt27=1;
			}
			else if ( (LA27_0==ASSERT||LA27_0==BREAK||(LA27_0 >= COMPLEX && LA27_0 <= CONTINUE)||LA27_0==DELETE||LA27_0==FALSE||LA27_0==FLOAT||(LA27_0 >= FROM && LA27_0 <= GLOBAL)||LA27_0==IMPORT||LA27_0==INT||LA27_0==LAMBDA||(LA27_0 >= NONE && LA27_0 <= NONLOCAL)||LA27_0==PASS||LA27_0==RAISE||LA27_0==RETURN||LA27_0==STRING||LA27_0==TRUE||LA27_0==YIELD) ) {
				alt27=1;
			}
			else if ( (LA27_0==AT||LA27_0==CLASS||LA27_0==DEF||LA27_0==FOR||LA27_0==IF||LA27_0==TRY||(LA27_0 >= WHILE && LA27_0 <= WITH)) ) {
				alt27=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 27, 0, input);
				throw nvae;
			}

			switch (alt27) {
				case 1 :
					// Truffle.g:559:7: simple_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_simple_stmt_in_stmt1453);
					simple_stmt58=simple_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_stmt58.getTree());

					if ( state.backtracking==0 ) {
					          retval.stypes = (simple_stmt58!=null?((TruffleParser.simple_stmt_return)simple_stmt58).stypes:null);
					      }
					}
					break;
				case 2 :
					// Truffle.g:563:7: compound_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_compound_stmt_in_stmt1469);
					compound_stmt59=compound_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, compound_stmt59.getTree());

					if ( state.backtracking==0 ) {
					          retval.stypes = new ArrayList();
					          retval.stypes.add((compound_stmt59!=null?((PNode)compound_stmt59.getTree()):null));
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "stmt"


	public static class simple_stmt_return extends ParserRuleReturnScope {
		public List stypes;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "simple_stmt"
	// Truffle.g:571:1: simple_stmt returns [List stypes] :s+= small_stmt ( options {greedy=true; } : SEMI s+= small_stmt )* ( SEMI )? NEWLINE ;
	public final TruffleParser.simple_stmt_return simple_stmt() throws RecognitionException {
		TruffleParser.simple_stmt_return retval = new TruffleParser.simple_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token SEMI60=null;
		Token SEMI61=null;
		Token NEWLINE62=null;
		List<Object> list_s=null;
		RuleReturnScope s = null;
		PNode SEMI60_tree=null;
		PNode SEMI61_tree=null;
		PNode NEWLINE62_tree=null;

		try {
			// Truffle.g:573:5: (s+= small_stmt ( options {greedy=true; } : SEMI s+= small_stmt )* ( SEMI )? NEWLINE )
			// Truffle.g:573:7: s+= small_stmt ( options {greedy=true; } : SEMI s+= small_stmt )* ( SEMI )? NEWLINE
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_small_stmt_in_simple_stmt1505);
			s=small_stmt();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, s.getTree());

			if (list_s==null) list_s=new ArrayList<Object>();
			list_s.add(s.getTree());
			// Truffle.g:573:21: ( options {greedy=true; } : SEMI s+= small_stmt )*
			loop28:
			while (true) {
				int alt28=2;
				int LA28_0 = input.LA(1);
				if ( (LA28_0==SEMI) ) {
					int LA28_1 = input.LA(2);
					if ( (LA28_1==ASSERT||(LA28_1 >= BACKQUOTE && LA28_1 <= BREAK)||(LA28_1 >= COMPLEX && LA28_1 <= CONTINUE)||LA28_1==DELETE||LA28_1==FALSE||LA28_1==FLOAT||(LA28_1 >= FROM && LA28_1 <= GLOBAL)||LA28_1==IMPORT||LA28_1==INT||(LA28_1 >= LAMBDA && LA28_1 <= LCURLY)||(LA28_1 >= LPAREN && LA28_1 <= MINUS)||LA28_1==NAME||(LA28_1 >= NONE && LA28_1 <= NOT)||LA28_1==PASS||LA28_1==PLUS||(LA28_1 >= PRINT && LA28_1 <= RAISE)||LA28_1==RETURN||(LA28_1 >= STRING && LA28_1 <= TILDE)||LA28_1==TRUE||LA28_1==YIELD) ) {
						alt28=1;
					}

				}

				switch (alt28) {
				case 1 :
					// Truffle.g:573:45: SEMI s+= small_stmt
					{
					SEMI60=(Token)match(input,SEMI,FOLLOW_SEMI_in_simple_stmt1515); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SEMI60_tree = (PNode)adaptor.create(SEMI60);
					adaptor.addChild(root_0, SEMI60_tree);
					}

					pushFollow(FOLLOW_small_stmt_in_simple_stmt1519);
					s=small_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, s.getTree());

					if (list_s==null) list_s=new ArrayList<Object>();
					list_s.add(s.getTree());
					}
					break;

				default :
					break loop28;
				}
			}

			// Truffle.g:573:66: ( SEMI )?
			int alt29=2;
			int LA29_0 = input.LA(1);
			if ( (LA29_0==SEMI) ) {
				alt29=1;
			}
			switch (alt29) {
				case 1 :
					// Truffle.g:573:67: SEMI
					{
					SEMI61=(Token)match(input,SEMI,FOLLOW_SEMI_in_simple_stmt1524); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SEMI61_tree = (PNode)adaptor.create(SEMI61);
					adaptor.addChild(root_0, SEMI61_tree);
					}

					}
					break;

			}

			NEWLINE62=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_simple_stmt1528); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			NEWLINE62_tree = (PNode)adaptor.create(NEWLINE62);
			adaptor.addChild(root_0, NEWLINE62_tree);
			}

			if ( state.backtracking==0 ) {
			          retval.stypes = list_s;
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "simple_stmt"


	public static class small_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "small_stmt"
	// Truffle.g:581:1: small_stmt : ( expr_stmt | del_stmt | pass_stmt | flow_stmt | import_stmt | global_stmt | assert_stmt |{...}? => print_stmt | nonlocal_stmt );
	public final TruffleParser.small_stmt_return small_stmt() throws RecognitionException {
		TruffleParser.small_stmt_return retval = new TruffleParser.small_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		ParserRuleReturnScope expr_stmt63 =null;
		ParserRuleReturnScope del_stmt64 =null;
		ParserRuleReturnScope pass_stmt65 =null;
		ParserRuleReturnScope flow_stmt66 =null;
		ParserRuleReturnScope import_stmt67 =null;
		ParserRuleReturnScope global_stmt68 =null;
		ParserRuleReturnScope assert_stmt69 =null;
		ParserRuleReturnScope print_stmt70 =null;
		ParserRuleReturnScope nonlocal_stmt71 =null;


		try {
			// Truffle.g:581:12: ( expr_stmt | del_stmt | pass_stmt | flow_stmt | import_stmt | global_stmt | assert_stmt |{...}? => print_stmt | nonlocal_stmt )
			int alt30=9;
			int LA30_0 = input.LA(1);
			if ( (LA30_0==BACKQUOTE||LA30_0==COMPLEX||LA30_0==FALSE||LA30_0==FLOAT||LA30_0==INT||(LA30_0 >= LAMBDA && LA30_0 <= LCURLY)||(LA30_0 >= LPAREN && LA30_0 <= MINUS)||LA30_0==NAME||LA30_0==NONE||LA30_0==NOT||LA30_0==PLUS||(LA30_0 >= STRING && LA30_0 <= TILDE)||LA30_0==TRUE) ) {
				alt30=1;
			}
			else if ( (LA30_0==PRINT) && (((!printFunction)||(printFunction)))) {
				int LA30_2 = input.LA(2);
				if ( ((printFunction)) ) {
					alt30=1;
				}
				else if ( ((!printFunction)) ) {
					alt30=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 30, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA30_0==DELETE) ) {
				alt30=2;
			}
			else if ( (LA30_0==PASS) ) {
				alt30=3;
			}
			else if ( (LA30_0==BREAK||LA30_0==CONTINUE||LA30_0==RAISE||LA30_0==RETURN||LA30_0==YIELD) ) {
				alt30=4;
			}
			else if ( (LA30_0==FROM||LA30_0==IMPORT) ) {
				alt30=5;
			}
			else if ( (LA30_0==GLOBAL) ) {
				alt30=6;
			}
			else if ( (LA30_0==ASSERT) ) {
				alt30=7;
			}
			else if ( (LA30_0==NONLOCAL) ) {
				alt30=9;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 30, 0, input);
				throw nvae;
			}

			switch (alt30) {
				case 1 :
					// Truffle.g:581:14: expr_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_expr_stmt_in_small_stmt1551);
					expr_stmt63=expr_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, expr_stmt63.getTree());

					}
					break;
				case 2 :
					// Truffle.g:582:14: del_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_del_stmt_in_small_stmt1566);
					del_stmt64=del_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, del_stmt64.getTree());

					}
					break;
				case 3 :
					// Truffle.g:583:14: pass_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_pass_stmt_in_small_stmt1581);
					pass_stmt65=pass_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pass_stmt65.getTree());

					}
					break;
				case 4 :
					// Truffle.g:584:14: flow_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_flow_stmt_in_small_stmt1596);
					flow_stmt66=flow_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, flow_stmt66.getTree());

					}
					break;
				case 5 :
					// Truffle.g:585:14: import_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_import_stmt_in_small_stmt1611);
					import_stmt67=import_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, import_stmt67.getTree());

					}
					break;
				case 6 :
					// Truffle.g:586:14: global_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_global_stmt_in_small_stmt1626);
					global_stmt68=global_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, global_stmt68.getTree());

					}
					break;
				case 7 :
					// Truffle.g:588:14: assert_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_assert_stmt_in_small_stmt1653);
					assert_stmt69=assert_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, assert_stmt69.getTree());

					}
					break;
				case 8 :
					// Truffle.g:589:14: {...}? => print_stmt
					{
					root_0 = (PNode)adaptor.nil();


					if ( !((!printFunction)) ) {
						if (state.backtracking>0) {state.failed=true; return retval;}
						throw new FailedPredicateException(input, "small_stmt", "!printFunction");
					}
					pushFollow(FOLLOW_print_stmt_in_small_stmt1672);
					print_stmt70=print_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, print_stmt70.getTree());

					}
					break;
				case 9 :
					// Truffle.g:590:14: nonlocal_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_nonlocal_stmt_in_small_stmt1687);
					nonlocal_stmt71=nonlocal_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, nonlocal_stmt71.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "small_stmt"


	public static class nonlocal_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "nonlocal_stmt"
	// Truffle.g:594:1: nonlocal_stmt : NONLOCAL n+= NAME ( options {k=2; } : COMMA n+= NAME )* ;
	public final TruffleParser.nonlocal_stmt_return nonlocal_stmt() throws RecognitionException {
		TruffleParser.nonlocal_stmt_return retval = new TruffleParser.nonlocal_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token NONLOCAL72=null;
		Token COMMA73=null;
		Token n=null;
		List<Object> list_n=null;

		PNode NONLOCAL72_tree=null;
		PNode COMMA73_tree=null;
		PNode n_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:601:5: ( NONLOCAL n+= NAME ( options {k=2; } : COMMA n+= NAME )* )
			// Truffle.g:601:7: NONLOCAL n+= NAME ( options {k=2; } : COMMA n+= NAME )*
			{
			root_0 = (PNode)adaptor.nil();


			NONLOCAL72=(Token)match(input,NONLOCAL,FOLLOW_NONLOCAL_in_nonlocal_stmt1722); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			NONLOCAL72_tree = (PNode)adaptor.create(NONLOCAL72);
			adaptor.addChild(root_0, NONLOCAL72_tree);
			}

			n=(Token)match(input,NAME,FOLLOW_NAME_in_nonlocal_stmt1726); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			n_tree = (PNode)adaptor.create(n);
			adaptor.addChild(root_0, n_tree);
			}

			if (list_n==null) list_n=new ArrayList<Object>();
			list_n.add(n);
			// Truffle.g:601:24: ( options {k=2; } : COMMA n+= NAME )*
			loop31:
			while (true) {
				int alt31=2;
				int LA31_0 = input.LA(1);
				if ( (LA31_0==COMMA) ) {
					alt31=1;
				}

				switch (alt31) {
				case 1 :
					// Truffle.g:601:41: COMMA n+= NAME
					{
					COMMA73=(Token)match(input,COMMA,FOLLOW_COMMA_in_nonlocal_stmt1737); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA73_tree = (PNode)adaptor.create(COMMA73);
					adaptor.addChild(root_0, COMMA73_tree);
					}

					n=(Token)match(input,NAME,FOLLOW_NAME_in_nonlocal_stmt1741); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					n_tree = (PNode)adaptor.create(n);
					adaptor.addChild(root_0, n_tree);
					}

					if (list_n==null) list_n=new ArrayList<Object>();
					list_n.add(n);
					}
					break;

				default :
					break loop31;
				}
			}

			if ( state.backtracking==0 ) {
			         stype = actions.makeNonlocal(NONLOCAL72, list_n, list_n);
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "nonlocal_stmt"


	public static class expr_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "expr_stmt"
	// Truffle.g:609:1: expr_stmt : ( ( testlist[null] augassign )=>lhs= testlist[expr_contextType.AugStore] ( (aay= augassign y1= yield_expr ) | (aat= augassign rhs= testlist[expr_contextType.Load] ) ) | ( testlist[null] ASSIGN )=>lhs= testlist[expr_contextType.Store] (| ( (at= ASSIGN t+= testlist[expr_contextType.Load] )+ ) | ( (ay= ASSIGN y2+= yield_expr )+ ) ) |lhs= testlist[expr_contextType.Load] ) ;
	public final TruffleParser.expr_stmt_return expr_stmt() throws RecognitionException {
		TruffleParser.expr_stmt_return retval = new TruffleParser.expr_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token at=null;
		Token ay=null;
		List<Object> list_t=null;
		List<Object> list_y2=null;
		ParserRuleReturnScope lhs =null;
		ParserRuleReturnScope aay =null;
		ParserRuleReturnScope y1 =null;
		ParserRuleReturnScope aat =null;
		ParserRuleReturnScope rhs =null;
		RuleReturnScope t = null;
		RuleReturnScope y2 = null;
		PNode at_tree=null;
		PNode ay_tree=null;


		    PNode stype = null;

		try {
			// Truffle.g:618:5: ( ( ( testlist[null] augassign )=>lhs= testlist[expr_contextType.AugStore] ( (aay= augassign y1= yield_expr ) | (aat= augassign rhs= testlist[expr_contextType.Load] ) ) | ( testlist[null] ASSIGN )=>lhs= testlist[expr_contextType.Store] (| ( (at= ASSIGN t+= testlist[expr_contextType.Load] )+ ) | ( (ay= ASSIGN y2+= yield_expr )+ ) ) |lhs= testlist[expr_contextType.Load] ) )
			// Truffle.g:618:7: ( ( testlist[null] augassign )=>lhs= testlist[expr_contextType.AugStore] ( (aay= augassign y1= yield_expr ) | (aat= augassign rhs= testlist[expr_contextType.Load] ) ) | ( testlist[null] ASSIGN )=>lhs= testlist[expr_contextType.Store] (| ( (at= ASSIGN t+= testlist[expr_contextType.Load] )+ ) | ( (ay= ASSIGN y2+= yield_expr )+ ) ) |lhs= testlist[expr_contextType.Load] )
			{
			root_0 = (PNode)adaptor.nil();


			// Truffle.g:618:7: ( ( testlist[null] augassign )=>lhs= testlist[expr_contextType.AugStore] ( (aay= augassign y1= yield_expr ) | (aat= augassign rhs= testlist[expr_contextType.Load] ) ) | ( testlist[null] ASSIGN )=>lhs= testlist[expr_contextType.Store] (| ( (at= ASSIGN t+= testlist[expr_contextType.Load] )+ ) | ( (ay= ASSIGN y2+= yield_expr )+ ) ) |lhs= testlist[expr_contextType.Load] )
			int alt36=3;
			int LA36_0 = input.LA(1);
			if ( (LA36_0==NOT) ) {
				int LA36_1 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==PLUS) ) {
				int LA36_2 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==MINUS) ) {
				int LA36_3 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==TILDE) ) {
				int LA36_4 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==LPAREN) ) {
				int LA36_5 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==LBRACK) ) {
				int LA36_6 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==LCURLY) ) {
				int LA36_7 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==BACKQUOTE) ) {
				int LA36_8 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==NAME) ) {
				int LA36_9 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==PRINT) && ((printFunction))) {
				int LA36_10 = input.LA(2);
				if ( (((printFunction)&&synpred2_Truffle())) ) {
					alt36=1;
				}
				else if ( (((printFunction)&&synpred3_Truffle())) ) {
					alt36=2;
				}
				else if ( ((printFunction)) ) {
					alt36=3;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA36_0==NONE) ) {
				int LA36_11 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==TRUE) ) {
				int LA36_12 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==FALSE) ) {
				int LA36_13 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==INT) ) {
				int LA36_14 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==FLOAT) ) {
				int LA36_15 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==COMPLEX) ) {
				int LA36_16 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==STRING) ) {
				int LA36_17 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}
			else if ( (LA36_0==LAMBDA) ) {
				int LA36_18 = input.LA(2);
				if ( (synpred2_Truffle()) ) {
					alt36=1;
				}
				else if ( (synpred3_Truffle()) ) {
					alt36=2;
				}
				else if ( (true) ) {
					alt36=3;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 36, 0, input);
				throw nvae;
			}

			switch (alt36) {
				case 1 :
					// Truffle.g:618:8: ( testlist[null] augassign )=>lhs= testlist[expr_contextType.AugStore] ( (aay= augassign y1= yield_expr ) | (aat= augassign rhs= testlist[expr_contextType.Load] ) )
					{
					pushFollow(FOLLOW_testlist_in_expr_stmt1792);
					lhs=testlist(expr_contextType.AugStore);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, lhs.getTree());

					// Truffle.g:619:9: ( (aay= augassign y1= yield_expr ) | (aat= augassign rhs= testlist[expr_contextType.Load] ) )
					int alt32=2;
					switch ( input.LA(1) ) {
					case PLUSEQUAL:
						{
						int LA32_1 = input.LA(2);
						if ( (LA32_1==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_1==BACKQUOTE||LA32_1==COMPLEX||LA32_1==FALSE||LA32_1==FLOAT||LA32_1==INT||(LA32_1 >= LAMBDA && LA32_1 <= LCURLY)||(LA32_1 >= LPAREN && LA32_1 <= MINUS)||LA32_1==NAME||LA32_1==NONE||LA32_1==NOT||LA32_1==PLUS||LA32_1==PRINT||(LA32_1 >= STRING && LA32_1 <= TILDE)||LA32_1==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 1, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case MINUSEQUAL:
						{
						int LA32_2 = input.LA(2);
						if ( (LA32_2==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_2==BACKQUOTE||LA32_2==COMPLEX||LA32_2==FALSE||LA32_2==FLOAT||LA32_2==INT||(LA32_2 >= LAMBDA && LA32_2 <= LCURLY)||(LA32_2 >= LPAREN && LA32_2 <= MINUS)||LA32_2==NAME||LA32_2==NONE||LA32_2==NOT||LA32_2==PLUS||LA32_2==PRINT||(LA32_2 >= STRING && LA32_2 <= TILDE)||LA32_2==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 2, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case STAREQUAL:
						{
						int LA32_3 = input.LA(2);
						if ( (LA32_3==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_3==BACKQUOTE||LA32_3==COMPLEX||LA32_3==FALSE||LA32_3==FLOAT||LA32_3==INT||(LA32_3 >= LAMBDA && LA32_3 <= LCURLY)||(LA32_3 >= LPAREN && LA32_3 <= MINUS)||LA32_3==NAME||LA32_3==NONE||LA32_3==NOT||LA32_3==PLUS||LA32_3==PRINT||(LA32_3 >= STRING && LA32_3 <= TILDE)||LA32_3==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 3, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case SLASHEQUAL:
						{
						int LA32_4 = input.LA(2);
						if ( (LA32_4==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_4==BACKQUOTE||LA32_4==COMPLEX||LA32_4==FALSE||LA32_4==FLOAT||LA32_4==INT||(LA32_4 >= LAMBDA && LA32_4 <= LCURLY)||(LA32_4 >= LPAREN && LA32_4 <= MINUS)||LA32_4==NAME||LA32_4==NONE||LA32_4==NOT||LA32_4==PLUS||LA32_4==PRINT||(LA32_4 >= STRING && LA32_4 <= TILDE)||LA32_4==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 4, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case PERCENTEQUAL:
						{
						int LA32_5 = input.LA(2);
						if ( (LA32_5==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_5==BACKQUOTE||LA32_5==COMPLEX||LA32_5==FALSE||LA32_5==FLOAT||LA32_5==INT||(LA32_5 >= LAMBDA && LA32_5 <= LCURLY)||(LA32_5 >= LPAREN && LA32_5 <= MINUS)||LA32_5==NAME||LA32_5==NONE||LA32_5==NOT||LA32_5==PLUS||LA32_5==PRINT||(LA32_5 >= STRING && LA32_5 <= TILDE)||LA32_5==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 5, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case AMPEREQUAL:
						{
						int LA32_6 = input.LA(2);
						if ( (LA32_6==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_6==BACKQUOTE||LA32_6==COMPLEX||LA32_6==FALSE||LA32_6==FLOAT||LA32_6==INT||(LA32_6 >= LAMBDA && LA32_6 <= LCURLY)||(LA32_6 >= LPAREN && LA32_6 <= MINUS)||LA32_6==NAME||LA32_6==NONE||LA32_6==NOT||LA32_6==PLUS||LA32_6==PRINT||(LA32_6 >= STRING && LA32_6 <= TILDE)||LA32_6==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 6, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case VBAREQUAL:
						{
						int LA32_7 = input.LA(2);
						if ( (LA32_7==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_7==BACKQUOTE||LA32_7==COMPLEX||LA32_7==FALSE||LA32_7==FLOAT||LA32_7==INT||(LA32_7 >= LAMBDA && LA32_7 <= LCURLY)||(LA32_7 >= LPAREN && LA32_7 <= MINUS)||LA32_7==NAME||LA32_7==NONE||LA32_7==NOT||LA32_7==PLUS||LA32_7==PRINT||(LA32_7 >= STRING && LA32_7 <= TILDE)||LA32_7==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case CIRCUMFLEXEQUAL:
						{
						int LA32_8 = input.LA(2);
						if ( (LA32_8==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_8==BACKQUOTE||LA32_8==COMPLEX||LA32_8==FALSE||LA32_8==FLOAT||LA32_8==INT||(LA32_8 >= LAMBDA && LA32_8 <= LCURLY)||(LA32_8 >= LPAREN && LA32_8 <= MINUS)||LA32_8==NAME||LA32_8==NONE||LA32_8==NOT||LA32_8==PLUS||LA32_8==PRINT||(LA32_8 >= STRING && LA32_8 <= TILDE)||LA32_8==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 8, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case LEFTSHIFTEQUAL:
						{
						int LA32_9 = input.LA(2);
						if ( (LA32_9==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_9==BACKQUOTE||LA32_9==COMPLEX||LA32_9==FALSE||LA32_9==FLOAT||LA32_9==INT||(LA32_9 >= LAMBDA && LA32_9 <= LCURLY)||(LA32_9 >= LPAREN && LA32_9 <= MINUS)||LA32_9==NAME||LA32_9==NONE||LA32_9==NOT||LA32_9==PLUS||LA32_9==PRINT||(LA32_9 >= STRING && LA32_9 <= TILDE)||LA32_9==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 9, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case RIGHTSHIFTEQUAL:
						{
						int LA32_10 = input.LA(2);
						if ( (LA32_10==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_10==BACKQUOTE||LA32_10==COMPLEX||LA32_10==FALSE||LA32_10==FLOAT||LA32_10==INT||(LA32_10 >= LAMBDA && LA32_10 <= LCURLY)||(LA32_10 >= LPAREN && LA32_10 <= MINUS)||LA32_10==NAME||LA32_10==NONE||LA32_10==NOT||LA32_10==PLUS||LA32_10==PRINT||(LA32_10 >= STRING && LA32_10 <= TILDE)||LA32_10==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 10, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case DOUBLESTAREQUAL:
						{
						int LA32_11 = input.LA(2);
						if ( (LA32_11==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_11==BACKQUOTE||LA32_11==COMPLEX||LA32_11==FALSE||LA32_11==FLOAT||LA32_11==INT||(LA32_11 >= LAMBDA && LA32_11 <= LCURLY)||(LA32_11 >= LPAREN && LA32_11 <= MINUS)||LA32_11==NAME||LA32_11==NONE||LA32_11==NOT||LA32_11==PLUS||LA32_11==PRINT||(LA32_11 >= STRING && LA32_11 <= TILDE)||LA32_11==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 11, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case DOUBLESLASHEQUAL:
						{
						int LA32_12 = input.LA(2);
						if ( (LA32_12==YIELD) ) {
							alt32=1;
						}
						else if ( (LA32_12==BACKQUOTE||LA32_12==COMPLEX||LA32_12==FALSE||LA32_12==FLOAT||LA32_12==INT||(LA32_12 >= LAMBDA && LA32_12 <= LCURLY)||(LA32_12 >= LPAREN && LA32_12 <= MINUS)||LA32_12==NAME||LA32_12==NONE||LA32_12==NOT||LA32_12==PLUS||LA32_12==PRINT||(LA32_12 >= STRING && LA32_12 <= TILDE)||LA32_12==TRUE) ) {
							alt32=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 32, 12, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					default:
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 32, 0, input);
						throw nvae;
					}
					switch (alt32) {
						case 1 :
							// Truffle.g:619:11: (aay= augassign y1= yield_expr )
							{
							// Truffle.g:619:11: (aay= augassign y1= yield_expr )
							// Truffle.g:619:12: aay= augassign y1= yield_expr
							{
							pushFollow(FOLLOW_augassign_in_expr_stmt1808);
							aay=augassign();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, aay.getTree());

							pushFollow(FOLLOW_yield_expr_in_expr_stmt1812);
							y1=yield_expr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, y1.getTree());

							if ( state.backtracking==0 ) {
							               actions.checkAugAssign(actions.castExpr((lhs!=null?((PNode)lhs.getTree()):null)));
							               stype = actions.makeAugAssign((lhs!=null?((PNode)lhs.getTree()):null), actions.castExpr((lhs!=null?((PNode)lhs.getTree()):null)), (aay!=null?((TruffleParser.augassign_return)aay).op:null), actions.castExpr((y1!=null?((TruffleParser.yield_expr_return)y1).etype:null)));
							           }
							}

							}
							break;
						case 2 :
							// Truffle.g:625:11: (aat= augassign rhs= testlist[expr_contextType.Load] )
							{
							// Truffle.g:625:11: (aat= augassign rhs= testlist[expr_contextType.Load] )
							// Truffle.g:625:12: aat= augassign rhs= testlist[expr_contextType.Load]
							{
							pushFollow(FOLLOW_augassign_in_expr_stmt1852);
							aat=augassign();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, aat.getTree());

							pushFollow(FOLLOW_testlist_in_expr_stmt1856);
							rhs=testlist(expr_contextType.Load);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, rhs.getTree());

							if ( state.backtracking==0 ) {
							               actions.checkAugAssign(actions.castExpr((lhs!=null?((PNode)lhs.getTree()):null)));
							               stype = actions.makeAugAssign((lhs!=null?((PNode)lhs.getTree()):null), actions.castExpr((lhs!=null?((PNode)lhs.getTree()):null)), (aat!=null?((TruffleParser.augassign_return)aat).op:null), actions.castExpr((rhs!=null?((PNode)rhs.getTree()):null)));
							           }
							}

							}
							break;

					}

					}
					break;
				case 2 :
					// Truffle.g:632:7: ( testlist[null] ASSIGN )=>lhs= testlist[expr_contextType.Store] (| ( (at= ASSIGN t+= testlist[expr_contextType.Load] )+ ) | ( (ay= ASSIGN y2+= yield_expr )+ ) )
					{
					pushFollow(FOLLOW_testlist_in_expr_stmt1911);
					lhs=testlist(expr_contextType.Store);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, lhs.getTree());

					// Truffle.g:633:9: (| ( (at= ASSIGN t+= testlist[expr_contextType.Load] )+ ) | ( (ay= ASSIGN y2+= yield_expr )+ ) )
					int alt35=3;
					int LA35_0 = input.LA(1);
					if ( (LA35_0==NEWLINE||LA35_0==SEMI) ) {
						alt35=1;
					}
					else if ( (LA35_0==ASSIGN) ) {
						int LA35_2 = input.LA(2);
						if ( (LA35_2==BACKQUOTE||LA35_2==COMPLEX||LA35_2==FALSE||LA35_2==FLOAT||LA35_2==INT||(LA35_2 >= LAMBDA && LA35_2 <= LCURLY)||(LA35_2 >= LPAREN && LA35_2 <= MINUS)||LA35_2==NAME||LA35_2==NONE||LA35_2==NOT||LA35_2==PLUS||LA35_2==PRINT||(LA35_2 >= STRING && LA35_2 <= TILDE)||LA35_2==TRUE) ) {
							alt35=2;
						}
						else if ( (LA35_2==YIELD) ) {
							alt35=3;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 35, 2, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 35, 0, input);
						throw nvae;
					}

					switch (alt35) {
						case 1 :
							// Truffle.g:634:9: 
							{
							}
							break;
						case 2 :
							// Truffle.g:634:11: ( (at= ASSIGN t+= testlist[expr_contextType.Load] )+ )
							{
							// Truffle.g:634:11: ( (at= ASSIGN t+= testlist[expr_contextType.Load] )+ )
							// Truffle.g:634:12: (at= ASSIGN t+= testlist[expr_contextType.Load] )+
							{
							// Truffle.g:634:12: (at= ASSIGN t+= testlist[expr_contextType.Load] )+
							int cnt33=0;
							loop33:
							while (true) {
								int alt33=2;
								int LA33_0 = input.LA(1);
								if ( (LA33_0==ASSIGN) ) {
									alt33=1;
								}

								switch (alt33) {
								case 1 :
									// Truffle.g:634:13: at= ASSIGN t+= testlist[expr_contextType.Load]
									{
									at=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_expr_stmt1938); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									at_tree = (PNode)adaptor.create(at);
									adaptor.addChild(root_0, at_tree);
									}

									pushFollow(FOLLOW_testlist_in_expr_stmt1942);
									t=testlist(expr_contextType.Load);
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

									if (list_t==null) list_t=new ArrayList<Object>();
									list_t.add(t.getTree());
									}
									break;

								default :
									if ( cnt33 >= 1 ) break loop33;
									if (state.backtracking>0) {state.failed=true; return retval;}
									EarlyExitException eee = new EarlyExitException(33, input);
									throw eee;
								}
								cnt33++;
							}

							if ( state.backtracking==0 ) {

							                List<PNode> targetslist = actions.makeAssignTargets(actions.castExpr((lhs!=null?((PNode)lhs.getTree()):null)), list_t);
							                PNode valueTN = actions.makeAssignValue(list_t);
							                
							                stype = actions.makeAssign((lhs!=null?((PNode)lhs.getTree()):null), targetslist, valueTN);
							            }
							}

							}
							break;
						case 3 :
							// Truffle.g:643:11: ( (ay= ASSIGN y2+= yield_expr )+ )
							{
							// Truffle.g:643:11: ( (ay= ASSIGN y2+= yield_expr )+ )
							// Truffle.g:643:12: (ay= ASSIGN y2+= yield_expr )+
							{
							// Truffle.g:643:12: (ay= ASSIGN y2+= yield_expr )+
							int cnt34=0;
							loop34:
							while (true) {
								int alt34=2;
								int LA34_0 = input.LA(1);
								if ( (LA34_0==ASSIGN) ) {
									alt34=1;
								}

								switch (alt34) {
								case 1 :
									// Truffle.g:643:13: ay= ASSIGN y2+= yield_expr
									{
									ay=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_expr_stmt1987); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									ay_tree = (PNode)adaptor.create(ay);
									adaptor.addChild(root_0, ay_tree);
									}

									pushFollow(FOLLOW_yield_expr_in_expr_stmt1991);
									y2=yield_expr();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, y2.getTree());

									if (list_y2==null) list_y2=new ArrayList<Object>();
									list_y2.add(y2.getTree());
									}
									break;

								default :
									if ( cnt34 >= 1 ) break loop34;
									if (state.backtracking>0) {state.failed=true; return retval;}
									EarlyExitException eee = new EarlyExitException(34, input);
									throw eee;
								}
								cnt34++;
							}

							if ( state.backtracking==0 ) {
							//                stype = new Assign((lhs!=null?(lhs.start):null), actions.makeAssignTargets(actions.castExpr((lhs!=null?((PNode)lhs.getTree()):null)), list_y2), actions.makeAssignValue(list_y2));
							                stype = actions.makeAssign((lhs!=null?(lhs.start):null), actions.makeAssignTargets(actions.castExpr((lhs!=null?((PNode)lhs.getTree()):null)), list_y2), actions.makeAssignValue(list_y2));
							            }
							}

							}
							break;

					}

					}
					break;
				case 3 :
					// Truffle.g:650:7: lhs= testlist[expr_contextType.Load]
					{
					pushFollow(FOLLOW_testlist_in_expr_stmt2039);
					lhs=testlist(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, lhs.getTree());

					if ( state.backtracking==0 ) {
					          stype = actions.makeExpr((lhs!=null?(lhs.start):null), actions.castExpr((lhs!=null?((PNode)lhs.getTree()):null)));
					      }
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (stype != null) {
			        retval.tree = stype;
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "expr_stmt"


	public static class augassign_return extends ParserRuleReturnScope {
		public operatorType op;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "augassign"
	// Truffle.g:659:1: augassign returns [operatorType op] : ( PLUSEQUAL | MINUSEQUAL | STAREQUAL | SLASHEQUAL | PERCENTEQUAL | AMPEREQUAL | VBAREQUAL | CIRCUMFLEXEQUAL | LEFTSHIFTEQUAL | RIGHTSHIFTEQUAL | DOUBLESTAREQUAL | DOUBLESLASHEQUAL );
	public final TruffleParser.augassign_return augassign() throws RecognitionException {
		TruffleParser.augassign_return retval = new TruffleParser.augassign_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token PLUSEQUAL74=null;
		Token MINUSEQUAL75=null;
		Token STAREQUAL76=null;
		Token SLASHEQUAL77=null;
		Token PERCENTEQUAL78=null;
		Token AMPEREQUAL79=null;
		Token VBAREQUAL80=null;
		Token CIRCUMFLEXEQUAL81=null;
		Token LEFTSHIFTEQUAL82=null;
		Token RIGHTSHIFTEQUAL83=null;
		Token DOUBLESTAREQUAL84=null;
		Token DOUBLESLASHEQUAL85=null;

		PNode PLUSEQUAL74_tree=null;
		PNode MINUSEQUAL75_tree=null;
		PNode STAREQUAL76_tree=null;
		PNode SLASHEQUAL77_tree=null;
		PNode PERCENTEQUAL78_tree=null;
		PNode AMPEREQUAL79_tree=null;
		PNode VBAREQUAL80_tree=null;
		PNode CIRCUMFLEXEQUAL81_tree=null;
		PNode LEFTSHIFTEQUAL82_tree=null;
		PNode RIGHTSHIFTEQUAL83_tree=null;
		PNode DOUBLESTAREQUAL84_tree=null;
		PNode DOUBLESLASHEQUAL85_tree=null;

		try {
			// Truffle.g:661:5: ( PLUSEQUAL | MINUSEQUAL | STAREQUAL | SLASHEQUAL | PERCENTEQUAL | AMPEREQUAL | VBAREQUAL | CIRCUMFLEXEQUAL | LEFTSHIFTEQUAL | RIGHTSHIFTEQUAL | DOUBLESTAREQUAL | DOUBLESLASHEQUAL )
			int alt37=12;
			switch ( input.LA(1) ) {
			case PLUSEQUAL:
				{
				alt37=1;
				}
				break;
			case MINUSEQUAL:
				{
				alt37=2;
				}
				break;
			case STAREQUAL:
				{
				alt37=3;
				}
				break;
			case SLASHEQUAL:
				{
				alt37=4;
				}
				break;
			case PERCENTEQUAL:
				{
				alt37=5;
				}
				break;
			case AMPEREQUAL:
				{
				alt37=6;
				}
				break;
			case VBAREQUAL:
				{
				alt37=7;
				}
				break;
			case CIRCUMFLEXEQUAL:
				{
				alt37=8;
				}
				break;
			case LEFTSHIFTEQUAL:
				{
				alt37=9;
				}
				break;
			case RIGHTSHIFTEQUAL:
				{
				alt37=10;
				}
				break;
			case DOUBLESTAREQUAL:
				{
				alt37=11;
				}
				break;
			case DOUBLESLASHEQUAL:
				{
				alt37=12;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 37, 0, input);
				throw nvae;
			}
			switch (alt37) {
				case 1 :
					// Truffle.g:661:7: PLUSEQUAL
					{
					root_0 = (PNode)adaptor.nil();


					PLUSEQUAL74=(Token)match(input,PLUSEQUAL,FOLLOW_PLUSEQUAL_in_augassign2081); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					PLUSEQUAL74_tree = (PNode)adaptor.create(PLUSEQUAL74);
					adaptor.addChild(root_0, PLUSEQUAL74_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.Add;
					        }
					}
					break;
				case 2 :
					// Truffle.g:665:7: MINUSEQUAL
					{
					root_0 = (PNode)adaptor.nil();


					MINUSEQUAL75=(Token)match(input,MINUSEQUAL,FOLLOW_MINUSEQUAL_in_augassign2099); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					MINUSEQUAL75_tree = (PNode)adaptor.create(MINUSEQUAL75);
					adaptor.addChild(root_0, MINUSEQUAL75_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.Sub;
					        }
					}
					break;
				case 3 :
					// Truffle.g:669:7: STAREQUAL
					{
					root_0 = (PNode)adaptor.nil();


					STAREQUAL76=(Token)match(input,STAREQUAL,FOLLOW_STAREQUAL_in_augassign2117); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					STAREQUAL76_tree = (PNode)adaptor.create(STAREQUAL76);
					adaptor.addChild(root_0, STAREQUAL76_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.Mult;
					        }
					}
					break;
				case 4 :
					// Truffle.g:673:7: SLASHEQUAL
					{
					root_0 = (PNode)adaptor.nil();


					SLASHEQUAL77=(Token)match(input,SLASHEQUAL,FOLLOW_SLASHEQUAL_in_augassign2135); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SLASHEQUAL77_tree = (PNode)adaptor.create(SLASHEQUAL77);
					adaptor.addChild(root_0, SLASHEQUAL77_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.Div;
					        }
					}
					break;
				case 5 :
					// Truffle.g:677:7: PERCENTEQUAL
					{
					root_0 = (PNode)adaptor.nil();


					PERCENTEQUAL78=(Token)match(input,PERCENTEQUAL,FOLLOW_PERCENTEQUAL_in_augassign2153); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					PERCENTEQUAL78_tree = (PNode)adaptor.create(PERCENTEQUAL78);
					adaptor.addChild(root_0, PERCENTEQUAL78_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.Mod;
					        }
					}
					break;
				case 6 :
					// Truffle.g:681:7: AMPEREQUAL
					{
					root_0 = (PNode)adaptor.nil();


					AMPEREQUAL79=(Token)match(input,AMPEREQUAL,FOLLOW_AMPEREQUAL_in_augassign2171); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AMPEREQUAL79_tree = (PNode)adaptor.create(AMPEREQUAL79);
					adaptor.addChild(root_0, AMPEREQUAL79_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.BitAnd;
					        }
					}
					break;
				case 7 :
					// Truffle.g:685:7: VBAREQUAL
					{
					root_0 = (PNode)adaptor.nil();


					VBAREQUAL80=(Token)match(input,VBAREQUAL,FOLLOW_VBAREQUAL_in_augassign2189); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					VBAREQUAL80_tree = (PNode)adaptor.create(VBAREQUAL80);
					adaptor.addChild(root_0, VBAREQUAL80_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.BitOr;
					        }
					}
					break;
				case 8 :
					// Truffle.g:689:7: CIRCUMFLEXEQUAL
					{
					root_0 = (PNode)adaptor.nil();


					CIRCUMFLEXEQUAL81=(Token)match(input,CIRCUMFLEXEQUAL,FOLLOW_CIRCUMFLEXEQUAL_in_augassign2207); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					CIRCUMFLEXEQUAL81_tree = (PNode)adaptor.create(CIRCUMFLEXEQUAL81);
					adaptor.addChild(root_0, CIRCUMFLEXEQUAL81_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.BitXor;
					        }
					}
					break;
				case 9 :
					// Truffle.g:693:7: LEFTSHIFTEQUAL
					{
					root_0 = (PNode)adaptor.nil();


					LEFTSHIFTEQUAL82=(Token)match(input,LEFTSHIFTEQUAL,FOLLOW_LEFTSHIFTEQUAL_in_augassign2225); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LEFTSHIFTEQUAL82_tree = (PNode)adaptor.create(LEFTSHIFTEQUAL82);
					adaptor.addChild(root_0, LEFTSHIFTEQUAL82_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.LShift;
					        }
					}
					break;
				case 10 :
					// Truffle.g:697:7: RIGHTSHIFTEQUAL
					{
					root_0 = (PNode)adaptor.nil();


					RIGHTSHIFTEQUAL83=(Token)match(input,RIGHTSHIFTEQUAL,FOLLOW_RIGHTSHIFTEQUAL_in_augassign2243); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					RIGHTSHIFTEQUAL83_tree = (PNode)adaptor.create(RIGHTSHIFTEQUAL83);
					adaptor.addChild(root_0, RIGHTSHIFTEQUAL83_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.RShift;
					        }
					}
					break;
				case 11 :
					// Truffle.g:701:7: DOUBLESTAREQUAL
					{
					root_0 = (PNode)adaptor.nil();


					DOUBLESTAREQUAL84=(Token)match(input,DOUBLESTAREQUAL,FOLLOW_DOUBLESTAREQUAL_in_augassign2261); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOUBLESTAREQUAL84_tree = (PNode)adaptor.create(DOUBLESTAREQUAL84);
					adaptor.addChild(root_0, DOUBLESTAREQUAL84_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.Pow;
					        }
					}
					break;
				case 12 :
					// Truffle.g:705:7: DOUBLESLASHEQUAL
					{
					root_0 = (PNode)adaptor.nil();


					DOUBLESLASHEQUAL85=(Token)match(input,DOUBLESLASHEQUAL,FOLLOW_DOUBLESLASHEQUAL_in_augassign2279); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOUBLESLASHEQUAL85_tree = (PNode)adaptor.create(DOUBLESLASHEQUAL85);
					adaptor.addChild(root_0, DOUBLESLASHEQUAL85_tree);
					}

					if ( state.backtracking==0 ) {
					            retval.op = operatorType.FloorDiv;
					        }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "augassign"


	public static class print_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "print_stmt"
	// Truffle.g:713:1: print_stmt : PRINT (t1= printlist | RIGHTSHIFT t2= printlist2 |) ;
	public final TruffleParser.print_stmt_return print_stmt() throws RecognitionException {
		TruffleParser.print_stmt_return retval = new TruffleParser.print_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token PRINT86=null;
		Token RIGHTSHIFT87=null;
		ParserRuleReturnScope t1 =null;
		ParserRuleReturnScope t2 =null;

		PNode PRINT86_tree=null;
		PNode RIGHTSHIFT87_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:721:5: ( PRINT (t1= printlist | RIGHTSHIFT t2= printlist2 |) )
			// Truffle.g:721:7: PRINT (t1= printlist | RIGHTSHIFT t2= printlist2 |)
			{
			root_0 = (PNode)adaptor.nil();


			PRINT86=(Token)match(input,PRINT,FOLLOW_PRINT_in_print_stmt2319); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			PRINT86_tree = (PNode)adaptor.create(PRINT86);
			adaptor.addChild(root_0, PRINT86_tree);
			}

			// Truffle.g:722:7: (t1= printlist | RIGHTSHIFT t2= printlist2 |)
			int alt38=3;
			int LA38_0 = input.LA(1);
			if ( (LA38_0==BACKQUOTE||(LA38_0 >= LBRACK && LA38_0 <= LCURLY)||(LA38_0 >= LPAREN && LA38_0 <= MINUS)||LA38_0==NAME||LA38_0==NOT||LA38_0==PLUS||LA38_0==TILDE) ) {
				alt38=1;
			}
			else if ( (LA38_0==PRINT) && ((printFunction))) {
				alt38=1;
			}
			else if ( (LA38_0==COMPLEX||LA38_0==FALSE||LA38_0==FLOAT||LA38_0==INT||LA38_0==LAMBDA||LA38_0==NONE||LA38_0==STRING||LA38_0==TRUE) ) {
				alt38=1;
			}
			else if ( (LA38_0==RIGHTSHIFT) ) {
				alt38=2;
			}
			else if ( (LA38_0==NEWLINE||LA38_0==SEMI) ) {
				alt38=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 38, 0, input);
				throw nvae;
			}

			switch (alt38) {
				case 1 :
					// Truffle.g:722:8: t1= printlist
					{
					pushFollow(FOLLOW_printlist_in_print_stmt2330);
					t1=printlist();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t1.getTree());

					if ( state.backtracking==0 ) {
					           stype = actions.makePrint(PRINT86, null, actions.castExprs((t1!=null?((TruffleParser.printlist_return)t1).elts:null)), (t1!=null?((TruffleParser.printlist_return)t1).newline:false));
					       }
					}
					break;
				case 2 :
					// Truffle.g:726:9: RIGHTSHIFT t2= printlist2
					{
					RIGHTSHIFT87=(Token)match(input,RIGHTSHIFT,FOLLOW_RIGHTSHIFT_in_print_stmt2349); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					RIGHTSHIFT87_tree = (PNode)adaptor.create(RIGHTSHIFT87);
					adaptor.addChild(root_0, RIGHTSHIFT87_tree);
					}

					pushFollow(FOLLOW_printlist2_in_print_stmt2353);
					t2=printlist2();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t2.getTree());

					if ( state.backtracking==0 ) {
					           stype = actions.makePrint(PRINT86, actions.castExpr((t2!=null?((TruffleParser.printlist2_return)t2).elts:null).get(0)), actions.castExprs((t2!=null?((TruffleParser.printlist2_return)t2).elts:null), 1), (t2!=null?((TruffleParser.printlist2_return)t2).newline:false));
					       }
					}
					break;
				case 3 :
					// Truffle.g:731:8: 
					{
					if ( state.backtracking==0 ) {
					           stype = actions.makePrint(PRINT86, null, new ArrayList<PNode>(), true);
					       }
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "print_stmt"


	public static class printlist_return extends ParserRuleReturnScope {
		public boolean newline;
		public List elts;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "printlist"
	// Truffle.g:738:1: printlist returns [boolean newline, List elts] : ( ( test[null] COMMA )=>t+= test[expr_contextType.Load] ( options {k=2; } : COMMA t+= test[expr_contextType.Load] )* (trailcomma= COMMA )? |t+= test[expr_contextType.Load] );
	public final TruffleParser.printlist_return printlist() throws RecognitionException {
		TruffleParser.printlist_return retval = new TruffleParser.printlist_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token trailcomma=null;
		Token COMMA88=null;
		List<Object> list_t=null;
		RuleReturnScope t = null;
		PNode trailcomma_tree=null;
		PNode COMMA88_tree=null;

		try {
			// Truffle.g:740:5: ( ( test[null] COMMA )=>t+= test[expr_contextType.Load] ( options {k=2; } : COMMA t+= test[expr_contextType.Load] )* (trailcomma= COMMA )? |t+= test[expr_contextType.Load] )
			int alt41=2;
			int LA41_0 = input.LA(1);
			if ( (LA41_0==NOT) ) {
				int LA41_1 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==PLUS) ) {
				int LA41_2 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==MINUS) ) {
				int LA41_3 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==TILDE) ) {
				int LA41_4 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==LPAREN) ) {
				int LA41_5 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==LBRACK) ) {
				int LA41_6 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==LCURLY) ) {
				int LA41_7 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==BACKQUOTE) ) {
				int LA41_8 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==NAME) ) {
				int LA41_9 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==PRINT) && ((printFunction))) {
				int LA41_10 = input.LA(2);
				if ( ((synpred4_Truffle()&&(printFunction))) ) {
					alt41=1;
				}
				else if ( ((printFunction)) ) {
					alt41=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 41, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA41_0==NONE) ) {
				int LA41_11 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==TRUE) ) {
				int LA41_12 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==FALSE) ) {
				int LA41_13 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==INT) ) {
				int LA41_14 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==FLOAT) ) {
				int LA41_15 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==COMPLEX) ) {
				int LA41_16 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==STRING) ) {
				int LA41_17 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}
			else if ( (LA41_0==LAMBDA) ) {
				int LA41_18 = input.LA(2);
				if ( (synpred4_Truffle()) ) {
					alt41=1;
				}
				else if ( (true) ) {
					alt41=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 41, 0, input);
				throw nvae;
			}

			switch (alt41) {
				case 1 :
					// Truffle.g:740:7: ( test[null] COMMA )=>t+= test[expr_contextType.Load] ( options {k=2; } : COMMA t+= test[expr_contextType.Load] )* (trailcomma= COMMA )?
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_test_in_printlist2433);
					t=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

					if (list_t==null) list_t=new ArrayList<Object>();
					list_t.add(t.getTree());
					// Truffle.g:741:39: ( options {k=2; } : COMMA t+= test[expr_contextType.Load] )*
					loop39:
					while (true) {
						int alt39=2;
						int LA39_0 = input.LA(1);
						if ( (LA39_0==COMMA) ) {
							int LA39_1 = input.LA(2);
							if ( (LA39_1==BACKQUOTE||LA39_1==COMPLEX||LA39_1==FALSE||LA39_1==FLOAT||LA39_1==INT||(LA39_1 >= LAMBDA && LA39_1 <= LCURLY)||(LA39_1 >= LPAREN && LA39_1 <= MINUS)||LA39_1==NAME||LA39_1==NONE||LA39_1==NOT||LA39_1==PLUS||LA39_1==PRINT||(LA39_1 >= STRING && LA39_1 <= TILDE)||LA39_1==TRUE) ) {
								alt39=1;
							}

						}

						switch (alt39) {
						case 1 :
							// Truffle.g:741:56: COMMA t+= test[expr_contextType.Load]
							{
							COMMA88=(Token)match(input,COMMA,FOLLOW_COMMA_in_printlist2445); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA88_tree = (PNode)adaptor.create(COMMA88);
							adaptor.addChild(root_0, COMMA88_tree);
							}

							pushFollow(FOLLOW_test_in_printlist2449);
							t=test(expr_contextType.Load);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

							if (list_t==null) list_t=new ArrayList<Object>();
							list_t.add(t.getTree());
							}
							break;

						default :
							break loop39;
						}
					}

					// Truffle.g:741:95: (trailcomma= COMMA )?
					int alt40=2;
					int LA40_0 = input.LA(1);
					if ( (LA40_0==COMMA) ) {
						alt40=1;
					}
					switch (alt40) {
						case 1 :
							// Truffle.g:741:96: trailcomma= COMMA
							{
							trailcomma=(Token)match(input,COMMA,FOLLOW_COMMA_in_printlist2457); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							trailcomma_tree = (PNode)adaptor.create(trailcomma);
							adaptor.addChild(root_0, trailcomma_tree);
							}

							}
							break;

					}

					if ( state.backtracking==0 ) {
					           retval.elts =list_t;
					           if (trailcomma == null) {
					               retval.newline = true;
					           } else {
					               retval.newline = false;
					           }
					       }
					}
					break;
				case 2 :
					// Truffle.g:750:7: t+= test[expr_contextType.Load]
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_test_in_printlist2478);
					t=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

					if (list_t==null) list_t=new ArrayList<Object>();
					list_t.add(t.getTree());
					if ( state.backtracking==0 ) {
					          retval.elts =list_t;
					          retval.newline = true;
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "printlist"


	public static class printlist2_return extends ParserRuleReturnScope {
		public boolean newline;
		public List elts;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "printlist2"
	// Truffle.g:759:1: printlist2 returns [boolean newline, List elts] : ( ( test[null] COMMA test[null] )=>t+= test[expr_contextType.Load] ( options {k=2; } : COMMA t+= test[expr_contextType.Load] )* (trailcomma= COMMA )? |t+= test[expr_contextType.Load] );
	public final TruffleParser.printlist2_return printlist2() throws RecognitionException {
		TruffleParser.printlist2_return retval = new TruffleParser.printlist2_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token trailcomma=null;
		Token COMMA89=null;
		List<Object> list_t=null;
		RuleReturnScope t = null;
		PNode trailcomma_tree=null;
		PNode COMMA89_tree=null;

		try {
			// Truffle.g:761:5: ( ( test[null] COMMA test[null] )=>t+= test[expr_contextType.Load] ( options {k=2; } : COMMA t+= test[expr_contextType.Load] )* (trailcomma= COMMA )? |t+= test[expr_contextType.Load] )
			int alt44=2;
			int LA44_0 = input.LA(1);
			if ( (LA44_0==NOT) ) {
				int LA44_1 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==PLUS) ) {
				int LA44_2 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==MINUS) ) {
				int LA44_3 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==TILDE) ) {
				int LA44_4 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==LPAREN) ) {
				int LA44_5 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==LBRACK) ) {
				int LA44_6 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==LCURLY) ) {
				int LA44_7 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==BACKQUOTE) ) {
				int LA44_8 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==NAME) ) {
				int LA44_9 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==PRINT) && ((printFunction))) {
				int LA44_10 = input.LA(2);
				if ( (((printFunction)&&synpred5_Truffle())) ) {
					alt44=1;
				}
				else if ( ((printFunction)) ) {
					alt44=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 44, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA44_0==NONE) ) {
				int LA44_11 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==TRUE) ) {
				int LA44_12 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==FALSE) ) {
				int LA44_13 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==INT) ) {
				int LA44_14 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==FLOAT) ) {
				int LA44_15 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==COMPLEX) ) {
				int LA44_16 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==STRING) ) {
				int LA44_17 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}
			else if ( (LA44_0==LAMBDA) ) {
				int LA44_18 = input.LA(2);
				if ( (synpred5_Truffle()) ) {
					alt44=1;
				}
				else if ( (true) ) {
					alt44=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 44, 0, input);
				throw nvae;
			}

			switch (alt44) {
				case 1 :
					// Truffle.g:761:7: ( test[null] COMMA test[null] )=>t+= test[expr_contextType.Load] ( options {k=2; } : COMMA t+= test[expr_contextType.Load] )* (trailcomma= COMMA )?
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_test_in_printlist22535);
					t=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

					if (list_t==null) list_t=new ArrayList<Object>();
					list_t.add(t.getTree());
					// Truffle.g:762:39: ( options {k=2; } : COMMA t+= test[expr_contextType.Load] )*
					loop42:
					while (true) {
						int alt42=2;
						int LA42_0 = input.LA(1);
						if ( (LA42_0==COMMA) ) {
							int LA42_1 = input.LA(2);
							if ( (LA42_1==BACKQUOTE||LA42_1==COMPLEX||LA42_1==FALSE||LA42_1==FLOAT||LA42_1==INT||(LA42_1 >= LAMBDA && LA42_1 <= LCURLY)||(LA42_1 >= LPAREN && LA42_1 <= MINUS)||LA42_1==NAME||LA42_1==NONE||LA42_1==NOT||LA42_1==PLUS||LA42_1==PRINT||(LA42_1 >= STRING && LA42_1 <= TILDE)||LA42_1==TRUE) ) {
								alt42=1;
							}

						}

						switch (alt42) {
						case 1 :
							// Truffle.g:762:56: COMMA t+= test[expr_contextType.Load]
							{
							COMMA89=(Token)match(input,COMMA,FOLLOW_COMMA_in_printlist22547); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA89_tree = (PNode)adaptor.create(COMMA89);
							adaptor.addChild(root_0, COMMA89_tree);
							}

							pushFollow(FOLLOW_test_in_printlist22551);
							t=test(expr_contextType.Load);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

							if (list_t==null) list_t=new ArrayList<Object>();
							list_t.add(t.getTree());
							}
							break;

						default :
							break loop42;
						}
					}

					// Truffle.g:762:95: (trailcomma= COMMA )?
					int alt43=2;
					int LA43_0 = input.LA(1);
					if ( (LA43_0==COMMA) ) {
						alt43=1;
					}
					switch (alt43) {
						case 1 :
							// Truffle.g:762:96: trailcomma= COMMA
							{
							trailcomma=(Token)match(input,COMMA,FOLLOW_COMMA_in_printlist22559); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							trailcomma_tree = (PNode)adaptor.create(trailcomma);
							adaptor.addChild(root_0, trailcomma_tree);
							}

							}
							break;

					}

					if ( state.backtracking==0 ) { retval.elts =list_t;
					           if (trailcomma == null) {
					               retval.newline = true;
					           } else {
					               retval.newline = false;
					           }
					       }
					}
					break;
				case 2 :
					// Truffle.g:770:7: t+= test[expr_contextType.Load]
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_test_in_printlist22580);
					t=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

					if (list_t==null) list_t=new ArrayList<Object>();
					list_t.add(t.getTree());
					if ( state.backtracking==0 ) {
					          retval.elts =list_t;
					          retval.newline = true;
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "printlist2"


	public static class del_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "del_stmt"
	// Truffle.g:778:1: del_stmt : DELETE del_list ;
	public final TruffleParser.del_stmt_return del_stmt() throws RecognitionException {
		TruffleParser.del_stmt_return retval = new TruffleParser.del_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token DELETE90=null;
		ParserRuleReturnScope del_list91 =null;

		PNode DELETE90_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:785:5: ( DELETE del_list )
			// Truffle.g:785:7: DELETE del_list
			{
			root_0 = (PNode)adaptor.nil();


			DELETE90=(Token)match(input,DELETE,FOLLOW_DELETE_in_del_stmt2617); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			DELETE90_tree = (PNode)adaptor.create(DELETE90);
			adaptor.addChild(root_0, DELETE90_tree);
			}

			pushFollow(FOLLOW_del_list_in_del_stmt2619);
			del_list91=del_list();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, del_list91.getTree());

			if ( state.backtracking==0 ) {
			          stype = actions.makeDelete(DELETE90, (del_list91!=null?((TruffleParser.del_list_return)del_list91).etypes:null));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "del_stmt"


	public static class pass_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "pass_stmt"
	// Truffle.g:792:1: pass_stmt : PASS ;
	public final TruffleParser.pass_stmt_return pass_stmt() throws RecognitionException {
		TruffleParser.pass_stmt_return retval = new TruffleParser.pass_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token PASS92=null;

		PNode PASS92_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:799:5: ( PASS )
			// Truffle.g:799:7: PASS
			{
			root_0 = (PNode)adaptor.nil();


			PASS92=(Token)match(input,PASS,FOLLOW_PASS_in_pass_stmt2655); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			PASS92_tree = (PNode)adaptor.create(PASS92);
			adaptor.addChild(root_0, PASS92_tree);
			}

			if ( state.backtracking==0 ) {
			          stype = actions.makePass(PASS92);
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "pass_stmt"


	public static class flow_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "flow_stmt"
	// Truffle.g:806:1: flow_stmt : ( break_stmt | continue_stmt | return_stmt | raise_stmt | yield_stmt );
	public final TruffleParser.flow_stmt_return flow_stmt() throws RecognitionException {
		TruffleParser.flow_stmt_return retval = new TruffleParser.flow_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		ParserRuleReturnScope break_stmt93 =null;
		ParserRuleReturnScope continue_stmt94 =null;
		ParserRuleReturnScope return_stmt95 =null;
		ParserRuleReturnScope raise_stmt96 =null;
		ParserRuleReturnScope yield_stmt97 =null;


		try {
			// Truffle.g:807:5: ( break_stmt | continue_stmt | return_stmt | raise_stmt | yield_stmt )
			int alt45=5;
			switch ( input.LA(1) ) {
			case BREAK:
				{
				alt45=1;
				}
				break;
			case CONTINUE:
				{
				alt45=2;
				}
				break;
			case RETURN:
				{
				alt45=3;
				}
				break;
			case RAISE:
				{
				alt45=4;
				}
				break;
			case YIELD:
				{
				alt45=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 45, 0, input);
				throw nvae;
			}
			switch (alt45) {
				case 1 :
					// Truffle.g:807:7: break_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_break_stmt_in_flow_stmt2681);
					break_stmt93=break_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, break_stmt93.getTree());

					}
					break;
				case 2 :
					// Truffle.g:808:7: continue_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_continue_stmt_in_flow_stmt2689);
					continue_stmt94=continue_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, continue_stmt94.getTree());

					}
					break;
				case 3 :
					// Truffle.g:809:7: return_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_return_stmt_in_flow_stmt2697);
					return_stmt95=return_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, return_stmt95.getTree());

					}
					break;
				case 4 :
					// Truffle.g:810:7: raise_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_raise_stmt_in_flow_stmt2705);
					raise_stmt96=raise_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, raise_stmt96.getTree());

					}
					break;
				case 5 :
					// Truffle.g:811:7: yield_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_yield_stmt_in_flow_stmt2713);
					yield_stmt97=yield_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, yield_stmt97.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "flow_stmt"


	public static class break_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "break_stmt"
	// Truffle.g:815:1: break_stmt : BREAK ;
	public final TruffleParser.break_stmt_return break_stmt() throws RecognitionException {
		TruffleParser.break_stmt_return retval = new TruffleParser.break_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token BREAK98=null;

		PNode BREAK98_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:822:5: ( BREAK )
			// Truffle.g:822:7: BREAK
			{
			root_0 = (PNode)adaptor.nil();


			BREAK98=(Token)match(input,BREAK,FOLLOW_BREAK_in_break_stmt2741); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			BREAK98_tree = (PNode)adaptor.create(BREAK98);
			adaptor.addChild(root_0, BREAK98_tree);
			}

			if ( state.backtracking==0 ) {
			          stype = actions.makeBreak(BREAK98);
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "break_stmt"


	public static class continue_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "continue_stmt"
	// Truffle.g:829:1: continue_stmt : CONTINUE ;
	public final TruffleParser.continue_stmt_return continue_stmt() throws RecognitionException {
		TruffleParser.continue_stmt_return retval = new TruffleParser.continue_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token CONTINUE99=null;

		PNode CONTINUE99_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:836:5: ( CONTINUE )
			// Truffle.g:836:7: CONTINUE
			{
			root_0 = (PNode)adaptor.nil();


			CONTINUE99=(Token)match(input,CONTINUE,FOLLOW_CONTINUE_in_continue_stmt2777); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			CONTINUE99_tree = (PNode)adaptor.create(CONTINUE99);
			adaptor.addChild(root_0, CONTINUE99_tree);
			}

			if ( state.backtracking==0 ) {
			          if (!suite_stack.isEmpty() && suite_stack.peek().continueIllegal) {
			              errorHandler.error("'continue' not supported inside 'finally' clause", (retval.start));
			          }
			          stype = actions.makeContinue(CONTINUE99);
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "continue_stmt"


	public static class return_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "return_stmt"
	// Truffle.g:846:1: return_stmt : RETURN ( testlist[expr_contextType.Load] |) ;
	public final TruffleParser.return_stmt_return return_stmt() throws RecognitionException {
		TruffleParser.return_stmt_return retval = new TruffleParser.return_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token RETURN100=null;
		ParserRuleReturnScope testlist101 =null;

		PNode RETURN100_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:853:5: ( RETURN ( testlist[expr_contextType.Load] |) )
			// Truffle.g:853:7: RETURN ( testlist[expr_contextType.Load] |)
			{
			root_0 = (PNode)adaptor.nil();


			RETURN100=(Token)match(input,RETURN,FOLLOW_RETURN_in_return_stmt2813); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			RETURN100_tree = (PNode)adaptor.create(RETURN100);
			adaptor.addChild(root_0, RETURN100_tree);
			}

			// Truffle.g:854:7: ( testlist[expr_contextType.Load] |)
			int alt46=2;
			int LA46_0 = input.LA(1);
			if ( (LA46_0==BACKQUOTE||(LA46_0 >= LBRACK && LA46_0 <= LCURLY)||(LA46_0 >= LPAREN && LA46_0 <= MINUS)||LA46_0==NAME||LA46_0==NOT||LA46_0==PLUS||LA46_0==TILDE) ) {
				alt46=1;
			}
			else if ( (LA46_0==PRINT) && ((printFunction))) {
				alt46=1;
			}
			else if ( (LA46_0==COMPLEX||LA46_0==FALSE||LA46_0==FLOAT||LA46_0==INT||LA46_0==LAMBDA||LA46_0==NONE||LA46_0==STRING||LA46_0==TRUE) ) {
				alt46=1;
			}
			else if ( (LA46_0==NEWLINE||LA46_0==SEMI) ) {
				alt46=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 46, 0, input);
				throw nvae;
			}

			switch (alt46) {
				case 1 :
					// Truffle.g:854:8: testlist[expr_contextType.Load]
					{
					pushFollow(FOLLOW_testlist_in_return_stmt2822);
					testlist101=testlist(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, testlist101.getTree());

					if ( state.backtracking==0 ) {
					           stype = actions.makeReturn(RETURN100, actions.castExpr((testlist101!=null?((PNode)testlist101.getTree()):null)));
					       }
					}
					break;
				case 2 :
					// Truffle.g:859:8: 
					{
					if ( state.backtracking==0 ) {
					           stype = actions.makeReturn(RETURN100, null);
					       }
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "return_stmt"


	public static class yield_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "yield_stmt"
	// Truffle.g:866:1: yield_stmt : yield_expr ;
	public final TruffleParser.yield_stmt_return yield_stmt() throws RecognitionException {
		TruffleParser.yield_stmt_return retval = new TruffleParser.yield_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		ParserRuleReturnScope yield_expr102 =null;



		    PNode stype = null;

		try {
			// Truffle.g:873:5: ( yield_expr )
			// Truffle.g:873:7: yield_expr
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_yield_expr_in_yield_stmt2887);
			yield_expr102=yield_expr();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, yield_expr102.getTree());

			if ( state.backtracking==0 ) {
			        stype = actions.makeExpr((yield_expr102!=null?(yield_expr102.start):null), actions.castExpr((yield_expr102!=null?((TruffleParser.yield_expr_return)yield_expr102).etype:null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "yield_stmt"


	public static class raise_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "raise_stmt"
	// Truffle.g:880:1: raise_stmt : RAISE (t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] ( COMMA t3= test[expr_contextType.Load] )? )? )? ;
	public final TruffleParser.raise_stmt_return raise_stmt() throws RecognitionException {
		TruffleParser.raise_stmt_return retval = new TruffleParser.raise_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token RAISE103=null;
		Token COMMA104=null;
		Token COMMA105=null;
		ParserRuleReturnScope t1 =null;
		ParserRuleReturnScope t2 =null;
		ParserRuleReturnScope t3 =null;

		PNode RAISE103_tree=null;
		PNode COMMA104_tree=null;
		PNode COMMA105_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:887:5: ( RAISE (t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] ( COMMA t3= test[expr_contextType.Load] )? )? )? )
			// Truffle.g:887:7: RAISE (t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] ( COMMA t3= test[expr_contextType.Load] )? )? )?
			{
			root_0 = (PNode)adaptor.nil();


			RAISE103=(Token)match(input,RAISE,FOLLOW_RAISE_in_raise_stmt2923); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			RAISE103_tree = (PNode)adaptor.create(RAISE103);
			adaptor.addChild(root_0, RAISE103_tree);
			}

			// Truffle.g:887:13: (t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] ( COMMA t3= test[expr_contextType.Load] )? )? )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==BACKQUOTE||(LA49_0 >= LBRACK && LA49_0 <= LCURLY)||(LA49_0 >= LPAREN && LA49_0 <= MINUS)||LA49_0==NAME||LA49_0==NOT||LA49_0==PLUS||LA49_0==TILDE) ) {
				alt49=1;
			}
			else if ( (LA49_0==PRINT) && ((printFunction))) {
				alt49=1;
			}
			else if ( (LA49_0==COMPLEX||LA49_0==FALSE||LA49_0==FLOAT||LA49_0==INT||LA49_0==LAMBDA||LA49_0==NONE||LA49_0==STRING||LA49_0==TRUE) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// Truffle.g:887:14: t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] ( COMMA t3= test[expr_contextType.Load] )? )?
					{
					pushFollow(FOLLOW_test_in_raise_stmt2928);
					t1=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t1.getTree());

					// Truffle.g:887:45: ( COMMA t2= test[expr_contextType.Load] ( COMMA t3= test[expr_contextType.Load] )? )?
					int alt48=2;
					int LA48_0 = input.LA(1);
					if ( (LA48_0==COMMA) ) {
						alt48=1;
					}
					switch (alt48) {
						case 1 :
							// Truffle.g:887:46: COMMA t2= test[expr_contextType.Load] ( COMMA t3= test[expr_contextType.Load] )?
							{
							COMMA104=(Token)match(input,COMMA,FOLLOW_COMMA_in_raise_stmt2932); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA104_tree = (PNode)adaptor.create(COMMA104);
							adaptor.addChild(root_0, COMMA104_tree);
							}

							pushFollow(FOLLOW_test_in_raise_stmt2936);
							t2=test(expr_contextType.Load);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, t2.getTree());

							// Truffle.g:888:9: ( COMMA t3= test[expr_contextType.Load] )?
							int alt47=2;
							int LA47_0 = input.LA(1);
							if ( (LA47_0==COMMA) ) {
								alt47=1;
							}
							switch (alt47) {
								case 1 :
									// Truffle.g:888:10: COMMA t3= test[expr_contextType.Load]
									{
									COMMA105=(Token)match(input,COMMA,FOLLOW_COMMA_in_raise_stmt2948); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									COMMA105_tree = (PNode)adaptor.create(COMMA105);
									adaptor.addChild(root_0, COMMA105_tree);
									}

									pushFollow(FOLLOW_test_in_raise_stmt2952);
									t3=test(expr_contextType.Load);
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, t3.getTree());

									}
									break;

							}

							}
							break;

					}

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          stype = actions.makeRaise(RAISE103, actions.castExpr((t1!=null?((PNode)t1.getTree()):null)), actions.castExpr((t2!=null?((PNode)t2.getTree()):null)), actions.castExpr((t3!=null?((PNode)t3.getTree()):null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "raise_stmt"


	public static class import_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "import_stmt"
	// Truffle.g:895:1: import_stmt : ( import_name | import_from );
	public final TruffleParser.import_stmt_return import_stmt() throws RecognitionException {
		TruffleParser.import_stmt_return retval = new TruffleParser.import_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		ParserRuleReturnScope import_name106 =null;
		ParserRuleReturnScope import_from107 =null;


		try {
			// Truffle.g:896:5: ( import_name | import_from )
			int alt50=2;
			int LA50_0 = input.LA(1);
			if ( (LA50_0==IMPORT) ) {
				alt50=1;
			}
			else if ( (LA50_0==FROM) ) {
				alt50=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 50, 0, input);
				throw nvae;
			}

			switch (alt50) {
				case 1 :
					// Truffle.g:896:7: import_name
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_import_name_in_import_stmt2985);
					import_name106=import_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, import_name106.getTree());

					}
					break;
				case 2 :
					// Truffle.g:897:7: import_from
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_import_from_in_import_stmt2993);
					import_from107=import_from();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, import_from107.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "import_stmt"


	public static class import_name_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "import_name"
	// Truffle.g:901:1: import_name : IMPORT dotted_as_names ;
	public final TruffleParser.import_name_return import_name() throws RecognitionException {
		TruffleParser.import_name_return retval = new TruffleParser.import_name_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token IMPORT108=null;
		ParserRuleReturnScope dotted_as_names109 =null;

		PNode IMPORT108_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:908:5: ( IMPORT dotted_as_names )
			// Truffle.g:908:7: IMPORT dotted_as_names
			{
			root_0 = (PNode)adaptor.nil();


			IMPORT108=(Token)match(input,IMPORT,FOLLOW_IMPORT_in_import_name3021); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IMPORT108_tree = (PNode)adaptor.create(IMPORT108);
			adaptor.addChild(root_0, IMPORT108_tree);
			}

			pushFollow(FOLLOW_dotted_as_names_in_import_name3023);
			dotted_as_names109=dotted_as_names();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, dotted_as_names109.getTree());

			if ( state.backtracking==0 ) {
			          stype =actions.makeImport(IMPORT108, (dotted_as_names109!=null?((TruffleParser.dotted_as_names_return)dotted_as_names109).atypes:null));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "import_name"


	public static class import_from_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "import_from"
	// Truffle.g:916:1: import_from : FROM ( (d+= DOT )* dotted_name | (d+= DOT )+ ) IMPORT ( STAR |i1= import_as_names | LPAREN i2= import_as_names ( COMMA )? RPAREN ) ;
	public final TruffleParser.import_from_return import_from() throws RecognitionException {
		TruffleParser.import_from_return retval = new TruffleParser.import_from_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token FROM110=null;
		Token IMPORT112=null;
		Token STAR113=null;
		Token LPAREN114=null;
		Token COMMA115=null;
		Token RPAREN116=null;
		Token d=null;
		List<Object> list_d=null;
		ParserRuleReturnScope i1 =null;
		ParserRuleReturnScope i2 =null;
		ParserRuleReturnScope dotted_name111 =null;

		PNode FROM110_tree=null;
		PNode IMPORT112_tree=null;
		PNode STAR113_tree=null;
		PNode LPAREN114_tree=null;
		PNode COMMA115_tree=null;
		PNode RPAREN116_tree=null;
		PNode d_tree=null;


		    StatementNode stype = null;
		//    actions.beginScope();

		try {
			// Truffle.g:924:5: ( FROM ( (d+= DOT )* dotted_name | (d+= DOT )+ ) IMPORT ( STAR |i1= import_as_names | LPAREN i2= import_as_names ( COMMA )? RPAREN ) )
			// Truffle.g:924:7: FROM ( (d+= DOT )* dotted_name | (d+= DOT )+ ) IMPORT ( STAR |i1= import_as_names | LPAREN i2= import_as_names ( COMMA )? RPAREN )
			{
			root_0 = (PNode)adaptor.nil();


			FROM110=(Token)match(input,FROM,FOLLOW_FROM_in_import_from3060); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			FROM110_tree = (PNode)adaptor.create(FROM110);
			adaptor.addChild(root_0, FROM110_tree);
			}

			// Truffle.g:924:12: ( (d+= DOT )* dotted_name | (d+= DOT )+ )
			int alt53=2;
			alt53 = dfa53.predict(input);
			switch (alt53) {
				case 1 :
					// Truffle.g:924:13: (d+= DOT )* dotted_name
					{
					// Truffle.g:924:14: (d+= DOT )*
					loop51:
					while (true) {
						int alt51=2;
						int LA51_0 = input.LA(1);
						if ( (LA51_0==DOT) ) {
							alt51=1;
						}

						switch (alt51) {
						case 1 :
							// Truffle.g:924:14: d+= DOT
							{
							d=(Token)match(input,DOT,FOLLOW_DOT_in_import_from3065); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							d_tree = (PNode)adaptor.create(d);
							adaptor.addChild(root_0, d_tree);
							}

							if (list_d==null) list_d=new ArrayList<Object>();
							list_d.add(d);
							}
							break;

						default :
							break loop51;
						}
					}

					pushFollow(FOLLOW_dotted_name_in_import_from3068);
					dotted_name111=dotted_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, dotted_name111.getTree());

					}
					break;
				case 2 :
					// Truffle.g:924:35: (d+= DOT )+
					{
					// Truffle.g:924:36: (d+= DOT )+
					int cnt52=0;
					loop52:
					while (true) {
						int alt52=2;
						int LA52_0 = input.LA(1);
						if ( (LA52_0==DOT) ) {
							alt52=1;
						}

						switch (alt52) {
						case 1 :
							// Truffle.g:924:36: d+= DOT
							{
							d=(Token)match(input,DOT,FOLLOW_DOT_in_import_from3074); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							d_tree = (PNode)adaptor.create(d);
							adaptor.addChild(root_0, d_tree);
							}

							if (list_d==null) list_d=new ArrayList<Object>();
							list_d.add(d);
							}
							break;

						default :
							if ( cnt52 >= 1 ) break loop52;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(52, input);
							throw eee;
						}
						cnt52++;
					}

					}
					break;

			}

			IMPORT112=(Token)match(input,IMPORT,FOLLOW_IMPORT_in_import_from3078); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IMPORT112_tree = (PNode)adaptor.create(IMPORT112);
			adaptor.addChild(root_0, IMPORT112_tree);
			}

			// Truffle.g:925:9: ( STAR |i1= import_as_names | LPAREN i2= import_as_names ( COMMA )? RPAREN )
			int alt55=3;
			switch ( input.LA(1) ) {
			case STAR:
				{
				alt55=1;
				}
				break;
			case NAME:
				{
				alt55=2;
				}
				break;
			case LPAREN:
				{
				alt55=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 55, 0, input);
				throw nvae;
			}
			switch (alt55) {
				case 1 :
					// Truffle.g:925:10: STAR
					{
					STAR113=(Token)match(input,STAR,FOLLOW_STAR_in_import_from3089); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					STAR113_tree = (PNode)adaptor.create(STAR113);
					adaptor.addChild(root_0, STAR113_tree);
					}

					if ( state.backtracking==0 ) {
					             stype = actions.makeImportFrom(FROM110, actions.makeFromText(list_d, (dotted_name111!=null?((TruffleParser.dotted_name_return)dotted_name111).names:null)),
					                 actions.makeModuleNameNode(list_d, (dotted_name111!=null?((TruffleParser.dotted_name_return)dotted_name111).names:null)),
					                 actions.makeStarAlias(STAR113), actions.makeLevel(list_d));
					         }
					}
					break;
				case 2 :
					// Truffle.g:931:11: i1= import_as_names
					{
					pushFollow(FOLLOW_import_as_names_in_import_from3114);
					i1=import_as_names();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, i1.getTree());

					if ( state.backtracking==0 ) {
					             String dottedText = (dotted_name111!=null?input.toString(dotted_name111.start,dotted_name111.stop):null);
					             if (dottedText != null && dottedText.equals("__future__")) {
					                 List aliases = (i1!=null?((TruffleParser.import_as_names_return)i1).atypes:null);
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
					             stype = actions.makeImportFrom(FROM110, actions.makeFromText(list_d, (dotted_name111!=null?((TruffleParser.dotted_name_return)dotted_name111).names:null)),
					                 actions.makeModuleNameNode(list_d, (dotted_name111!=null?((TruffleParser.dotted_name_return)dotted_name111).names:null)),
					                 actions.makeAliases((i1!=null?((TruffleParser.import_as_names_return)i1).atypes:null)), actions.makeLevel(list_d));
					         }
					}
					break;
				case 3 :
					// Truffle.g:950:11: LPAREN i2= import_as_names ( COMMA )? RPAREN
					{
					LPAREN114=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_import_from3137); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LPAREN114_tree = (PNode)adaptor.create(LPAREN114);
					adaptor.addChild(root_0, LPAREN114_tree);
					}

					pushFollow(FOLLOW_import_as_names_in_import_from3141);
					i2=import_as_names();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, i2.getTree());

					// Truffle.g:950:37: ( COMMA )?
					int alt54=2;
					int LA54_0 = input.LA(1);
					if ( (LA54_0==COMMA) ) {
						alt54=1;
					}
					switch (alt54) {
						case 1 :
							// Truffle.g:950:37: COMMA
							{
							COMMA115=(Token)match(input,COMMA,FOLLOW_COMMA_in_import_from3143); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA115_tree = (PNode)adaptor.create(COMMA115);
							adaptor.addChild(root_0, COMMA115_tree);
							}

							}
							break;

					}

					RPAREN116=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_import_from3146); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					RPAREN116_tree = (PNode)adaptor.create(RPAREN116);
					adaptor.addChild(root_0, RPAREN116_tree);
					}

					if ( state.backtracking==0 ) {
					             //XXX: this is almost a complete C&P of the code above - is there some way
					             //     to factor it out?
					             String dottedText = (dotted_name111!=null?input.toString(dotted_name111.start,dotted_name111.stop):null);
					             if (dottedText != null && dottedText.equals("__future__")) {
					                 List aliases = (i2!=null?((TruffleParser.import_as_names_return)i2).atypes:null);
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
					             stype = actions.makeImportFrom(FROM110, actions.makeFromText(list_d, (dotted_name111!=null?((TruffleParser.dotted_name_return)dotted_name111).names:null)),
					                 actions.makeModuleNameNode(list_d, (dotted_name111!=null?((TruffleParser.dotted_name_return)dotted_name111).names:null)),
					                 actions.makeAliases((i2!=null?((TruffleParser.import_as_names_return)i2).atypes:null)), actions.makeLevel(list_d));
					         }
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "import_from"


	public static class import_as_names_return extends ParserRuleReturnScope {
		public List atypes;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "import_as_names"
	// Truffle.g:975:1: import_as_names returns [List atypes] :n+= import_as_name ( COMMA !n+= import_as_name )* ;
	public final TruffleParser.import_as_names_return import_as_names() throws RecognitionException {
		TruffleParser.import_as_names_return retval = new TruffleParser.import_as_names_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token COMMA117=null;
		List<Object> list_n=null;
		RuleReturnScope n = null;
		PNode COMMA117_tree=null;

		try {
			// Truffle.g:977:5: (n+= import_as_name ( COMMA !n+= import_as_name )* )
			// Truffle.g:977:7: n+= import_as_name ( COMMA !n+= import_as_name )*
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_import_as_name_in_import_as_names3195);
			n=import_as_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, n.getTree());

			if (list_n==null) list_n=new ArrayList<Object>();
			list_n.add(n.getTree());
			// Truffle.g:977:25: ( COMMA !n+= import_as_name )*
			loop56:
			while (true) {
				int alt56=2;
				int LA56_0 = input.LA(1);
				if ( (LA56_0==COMMA) ) {
					int LA56_2 = input.LA(2);
					if ( (LA56_2==NAME) ) {
						alt56=1;
					}

				}

				switch (alt56) {
				case 1 :
					// Truffle.g:977:26: COMMA !n+= import_as_name
					{
					COMMA117=(Token)match(input,COMMA,FOLLOW_COMMA_in_import_as_names3198); if (state.failed) return retval;
					pushFollow(FOLLOW_import_as_name_in_import_as_names3203);
					n=import_as_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, n.getTree());

					if (list_n==null) list_n=new ArrayList<Object>();
					list_n.add(n.getTree());
					}
					break;

				default :
					break loop56;
				}
			}

			if ( state.backtracking==0 ) {
			        retval.atypes = list_n;
			    }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "import_as_names"


	public static class import_as_name_return extends ParserRuleReturnScope {
		public PNode atype;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "import_as_name"
	// Truffle.g:984:1: import_as_name returns [PNode atype] : name= NAME ( AS asname= NAME )? ;
	public final TruffleParser.import_as_name_return import_as_name() throws RecognitionException {
		TruffleParser.import_as_name_return retval = new TruffleParser.import_as_name_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token name=null;
		Token asname=null;
		Token AS118=null;

		PNode name_tree=null;
		PNode asname_tree=null;
		PNode AS118_tree=null;

		try {
			// Truffle.g:989:5: (name= NAME ( AS asname= NAME )? )
			// Truffle.g:989:7: name= NAME ( AS asname= NAME )?
			{
			root_0 = (PNode)adaptor.nil();


			name=(Token)match(input,NAME,FOLLOW_NAME_in_import_as_name3244); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			name_tree = (PNode)adaptor.create(name);
			adaptor.addChild(root_0, name_tree);
			}

			// Truffle.g:989:17: ( AS asname= NAME )?
			int alt57=2;
			int LA57_0 = input.LA(1);
			if ( (LA57_0==AS) ) {
				alt57=1;
			}
			switch (alt57) {
				case 1 :
					// Truffle.g:989:18: AS asname= NAME
					{
					AS118=(Token)match(input,AS,FOLLOW_AS_in_import_as_name3247); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AS118_tree = (PNode)adaptor.create(AS118);
					adaptor.addChild(root_0, AS118_tree);
					}

					asname=(Token)match(input,NAME,FOLLOW_NAME_in_import_as_name3251); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					asname_tree = (PNode)adaptor.create(asname);
					adaptor.addChild(root_0, asname_tree);
					}

					}
					break;

			}

			if ( state.backtracking==0 ) {
			        retval.atype = actions.makeAliasImport(name, asname);
			    }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.tree = retval.atype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "import_as_name"


	public static class dotted_as_name_return extends ParserRuleReturnScope {
		public PAlias atype;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "dotted_as_name"
	// Truffle.g:997:1: dotted_as_name returns [PAlias atype] : dotted_name ( AS asname= NAME )? ;
	public final TruffleParser.dotted_as_name_return dotted_as_name() throws RecognitionException {
		TruffleParser.dotted_as_name_return retval = new TruffleParser.dotted_as_name_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token asname=null;
		Token AS120=null;
		ParserRuleReturnScope dotted_name119 =null;

		PNode asname_tree=null;
		PNode AS120_tree=null;

		try {
			// Truffle.g:1002:5: ( dotted_name ( AS asname= NAME )? )
			// Truffle.g:1002:7: dotted_name ( AS asname= NAME )?
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_dotted_name_in_dotted_as_name3291);
			dotted_name119=dotted_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, dotted_name119.getTree());

			// Truffle.g:1002:19: ( AS asname= NAME )?
			int alt58=2;
			int LA58_0 = input.LA(1);
			if ( (LA58_0==AS) ) {
				alt58=1;
			}
			switch (alt58) {
				case 1 :
					// Truffle.g:1002:20: AS asname= NAME
					{
					AS120=(Token)match(input,AS,FOLLOW_AS_in_dotted_as_name3294); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AS120_tree = (PNode)adaptor.create(AS120);
					adaptor.addChild(root_0, AS120_tree);
					}

					asname=(Token)match(input,NAME,FOLLOW_NAME_in_dotted_as_name3298); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					asname_tree = (PNode)adaptor.create(asname);
					adaptor.addChild(root_0, asname_tree);
					}

					}
					break;

			}

			if ( state.backtracking==0 ) {
			        retval.atype = actions.makeAliasDotted((dotted_name119!=null?((TruffleParser.dotted_name_return)dotted_name119).names:null), asname);
			    }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.tree = retval.atype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "dotted_as_name"


	public static class dotted_as_names_return extends ParserRuleReturnScope {
		public List atypes;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "dotted_as_names"
	// Truffle.g:1009:1: dotted_as_names returns [List atypes] :d+= dotted_as_name ( COMMA !d+= dotted_as_name )* ;
	public final TruffleParser.dotted_as_names_return dotted_as_names() throws RecognitionException {
		TruffleParser.dotted_as_names_return retval = new TruffleParser.dotted_as_names_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token COMMA121=null;
		List<Object> list_d=null;
		RuleReturnScope d = null;
		PNode COMMA121_tree=null;

		try {
			// Truffle.g:1011:5: (d+= dotted_as_name ( COMMA !d+= dotted_as_name )* )
			// Truffle.g:1011:7: d+= dotted_as_name ( COMMA !d+= dotted_as_name )*
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_dotted_as_name_in_dotted_as_names3334);
			d=dotted_as_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, d.getTree());

			if (list_d==null) list_d=new ArrayList<Object>();
			list_d.add(d.getTree());
			// Truffle.g:1011:25: ( COMMA !d+= dotted_as_name )*
			loop59:
			while (true) {
				int alt59=2;
				int LA59_0 = input.LA(1);
				if ( (LA59_0==COMMA) ) {
					alt59=1;
				}

				switch (alt59) {
				case 1 :
					// Truffle.g:1011:26: COMMA !d+= dotted_as_name
					{
					COMMA121=(Token)match(input,COMMA,FOLLOW_COMMA_in_dotted_as_names3337); if (state.failed) return retval;
					pushFollow(FOLLOW_dotted_as_name_in_dotted_as_names3342);
					d=dotted_as_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, d.getTree());

					if (list_d==null) list_d=new ArrayList<Object>();
					list_d.add(d.getTree());
					}
					break;

				default :
					break loop59;
				}
			}

			if ( state.backtracking==0 ) {
			        retval.atypes = list_d;
			    }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "dotted_as_names"


	public static class dotted_name_return extends ParserRuleReturnScope {
		public List<PNode> names;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "dotted_name"
	// Truffle.g:1018:1: dotted_name returns [List<PNode> names] : NAME ( DOT dn+= attr )* ;
	public final TruffleParser.dotted_name_return dotted_name() throws RecognitionException {
		TruffleParser.dotted_name_return retval = new TruffleParser.dotted_name_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token NAME122=null;
		Token DOT123=null;
		List<Object> list_dn=null;
		RuleReturnScope dn = null;
		PNode NAME122_tree=null;
		PNode DOT123_tree=null;

		try {
			// Truffle.g:1020:5: ( NAME ( DOT dn+= attr )* )
			// Truffle.g:1020:7: NAME ( DOT dn+= attr )*
			{
			root_0 = (PNode)adaptor.nil();


			NAME122=(Token)match(input,NAME,FOLLOW_NAME_in_dotted_name3376); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			NAME122_tree = (PNode)adaptor.create(NAME122);
			adaptor.addChild(root_0, NAME122_tree);
			}

			// Truffle.g:1020:12: ( DOT dn+= attr )*
			loop60:
			while (true) {
				int alt60=2;
				int LA60_0 = input.LA(1);
				if ( (LA60_0==DOT) ) {
					alt60=1;
				}

				switch (alt60) {
				case 1 :
					// Truffle.g:1020:13: DOT dn+= attr
					{
					DOT123=(Token)match(input,DOT,FOLLOW_DOT_in_dotted_name3379); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOT123_tree = (PNode)adaptor.create(DOT123);
					adaptor.addChild(root_0, DOT123_tree);
					}

					pushFollow(FOLLOW_attr_in_dotted_name3383);
					dn=attr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, dn.getTree());

					if (list_dn==null) list_dn=new ArrayList<Object>();
					list_dn.add(dn.getTree());
					}
					break;

				default :
					break loop60;
				}
			}

			if ( state.backtracking==0 ) {
			        retval.names = actions.makeDottedName(NAME122, list_dn);
			    }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "dotted_name"


	public static class global_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "global_stmt"
	// Truffle.g:1027:1: global_stmt : GLOBAL n+= NAME ( COMMA n+= NAME )* ;
	public final TruffleParser.global_stmt_return global_stmt() throws RecognitionException {
		TruffleParser.global_stmt_return retval = new TruffleParser.global_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token GLOBAL124=null;
		Token COMMA125=null;
		Token n=null;
		List<Object> list_n=null;

		PNode GLOBAL124_tree=null;
		PNode COMMA125_tree=null;
		PNode n_tree=null;


		    PNode stype = null;

		try {
			// Truffle.g:1034:5: ( GLOBAL n+= NAME ( COMMA n+= NAME )* )
			// Truffle.g:1034:7: GLOBAL n+= NAME ( COMMA n+= NAME )*
			{
			root_0 = (PNode)adaptor.nil();


			GLOBAL124=(Token)match(input,GLOBAL,FOLLOW_GLOBAL_in_global_stmt3419); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			GLOBAL124_tree = (PNode)adaptor.create(GLOBAL124);
			adaptor.addChild(root_0, GLOBAL124_tree);
			}

			n=(Token)match(input,NAME,FOLLOW_NAME_in_global_stmt3423); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			n_tree = (PNode)adaptor.create(n);
			adaptor.addChild(root_0, n_tree);
			}

			if (list_n==null) list_n=new ArrayList<Object>();
			list_n.add(n);
			// Truffle.g:1034:22: ( COMMA n+= NAME )*
			loop61:
			while (true) {
				int alt61=2;
				int LA61_0 = input.LA(1);
				if ( (LA61_0==COMMA) ) {
					alt61=1;
				}

				switch (alt61) {
				case 1 :
					// Truffle.g:1034:23: COMMA n+= NAME
					{
					COMMA125=(Token)match(input,COMMA,FOLLOW_COMMA_in_global_stmt3426); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA125_tree = (PNode)adaptor.create(COMMA125);
					adaptor.addChild(root_0, COMMA125_tree);
					}

					n=(Token)match(input,NAME,FOLLOW_NAME_in_global_stmt3430); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					n_tree = (PNode)adaptor.create(n);
					adaptor.addChild(root_0, n_tree);
					}

					if (list_n==null) list_n=new ArrayList<Object>();
					list_n.add(n);
					}
					break;

				default :
					break loop61;
				}
			}

			if ( state.backtracking==0 ) {
			          stype = actions.makeGlobal(GLOBAL124, actions.makeNames(list_n), actions.makeNameNodes(list_n));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "global_stmt"


	public static class exec_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "exec_stmt"
	// Truffle.g:1041:1: exec_stmt : EXEC expr[expr_contextType.Load] ( IN t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] )? )? ;
	public final TruffleParser.exec_stmt_return exec_stmt() throws RecognitionException {
		TruffleParser.exec_stmt_return retval = new TruffleParser.exec_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token EXEC126=null;
		Token IN128=null;
		Token COMMA129=null;
		ParserRuleReturnScope t1 =null;
		ParserRuleReturnScope t2 =null;
		ParserRuleReturnScope expr127 =null;

		PNode EXEC126_tree=null;
		PNode IN128_tree=null;
		PNode COMMA129_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:1048:5: ( EXEC expr[expr_contextType.Load] ( IN t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] )? )? )
			// Truffle.g:1048:7: EXEC expr[expr_contextType.Load] ( IN t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] )? )?
			{
			root_0 = (PNode)adaptor.nil();


			EXEC126=(Token)match(input,EXEC,FOLLOW_EXEC_in_exec_stmt3468); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			EXEC126_tree = (PNode)adaptor.create(EXEC126);
			adaptor.addChild(root_0, EXEC126_tree);
			}

			pushFollow(FOLLOW_expr_in_exec_stmt3470);
			expr127=expr(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, expr127.getTree());

			// Truffle.g:1048:40: ( IN t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] )? )?
			int alt63=2;
			int LA63_0 = input.LA(1);
			if ( (LA63_0==IN) ) {
				alt63=1;
			}
			switch (alt63) {
				case 1 :
					// Truffle.g:1048:41: IN t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] )?
					{
					IN128=(Token)match(input,IN,FOLLOW_IN_in_exec_stmt3474); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					IN128_tree = (PNode)adaptor.create(IN128);
					adaptor.addChild(root_0, IN128_tree);
					}

					pushFollow(FOLLOW_test_in_exec_stmt3478);
					t1=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t1.getTree());

					// Truffle.g:1048:75: ( COMMA t2= test[expr_contextType.Load] )?
					int alt62=2;
					int LA62_0 = input.LA(1);
					if ( (LA62_0==COMMA) ) {
						alt62=1;
					}
					switch (alt62) {
						case 1 :
							// Truffle.g:1048:76: COMMA t2= test[expr_contextType.Load]
							{
							COMMA129=(Token)match(input,COMMA,FOLLOW_COMMA_in_exec_stmt3482); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA129_tree = (PNode)adaptor.create(COMMA129);
							adaptor.addChild(root_0, COMMA129_tree);
							}

							pushFollow(FOLLOW_test_in_exec_stmt3486);
							t2=test(expr_contextType.Load);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, t2.getTree());

							}
							break;

					}

					}
					break;

			}

			if ( state.backtracking==0 ) {
			         stype = actions.makeExec(EXEC126, actions.castExpr((expr127!=null?((PNode)expr127.getTree()):null)), actions.castExpr((t1!=null?((PNode)t1.getTree()):null)), actions.castExpr((t2!=null?((PNode)t2.getTree()):null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "exec_stmt"


	public static class assert_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "assert_stmt"
	// Truffle.g:1055:1: assert_stmt : ASSERT t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] )? ;
	public final TruffleParser.assert_stmt_return assert_stmt() throws RecognitionException {
		TruffleParser.assert_stmt_return retval = new TruffleParser.assert_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token ASSERT130=null;
		Token COMMA131=null;
		ParserRuleReturnScope t1 =null;
		ParserRuleReturnScope t2 =null;

		PNode ASSERT130_tree=null;
		PNode COMMA131_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:1062:5: ( ASSERT t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] )? )
			// Truffle.g:1062:7: ASSERT t1= test[expr_contextType.Load] ( COMMA t2= test[expr_contextType.Load] )?
			{
			root_0 = (PNode)adaptor.nil();


			ASSERT130=(Token)match(input,ASSERT,FOLLOW_ASSERT_in_assert_stmt3527); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ASSERT130_tree = (PNode)adaptor.create(ASSERT130);
			adaptor.addChild(root_0, ASSERT130_tree);
			}

			pushFollow(FOLLOW_test_in_assert_stmt3531);
			t1=test(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, t1.getTree());

			// Truffle.g:1062:45: ( COMMA t2= test[expr_contextType.Load] )?
			int alt64=2;
			int LA64_0 = input.LA(1);
			if ( (LA64_0==COMMA) ) {
				alt64=1;
			}
			switch (alt64) {
				case 1 :
					// Truffle.g:1062:46: COMMA t2= test[expr_contextType.Load]
					{
					COMMA131=(Token)match(input,COMMA,FOLLOW_COMMA_in_assert_stmt3535); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA131_tree = (PNode)adaptor.create(COMMA131);
					adaptor.addChild(root_0, COMMA131_tree);
					}

					pushFollow(FOLLOW_test_in_assert_stmt3539);
					t2=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t2.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          stype = actions.makeAssert(ASSERT130, actions.castExpr((t1!=null?((PNode)t1.getTree()):null)), actions.castExpr((t2!=null?((PNode)t2.getTree()):null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "assert_stmt"


	public static class compound_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "compound_stmt"
	// Truffle.g:1069:1: compound_stmt : ( if_stmt | while_stmt | for_stmt | try_stmt | with_stmt | ( ( decorators )? DEF )=> funcdef | classdef );
	public final TruffleParser.compound_stmt_return compound_stmt() throws RecognitionException {
		TruffleParser.compound_stmt_return retval = new TruffleParser.compound_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		ParserRuleReturnScope if_stmt132 =null;
		ParserRuleReturnScope while_stmt133 =null;
		ParserRuleReturnScope for_stmt134 =null;
		ParserRuleReturnScope try_stmt135 =null;
		ParserRuleReturnScope with_stmt136 =null;
		ParserRuleReturnScope funcdef137 =null;
		ParserRuleReturnScope classdef138 =null;


		try {
			// Truffle.g:1070:5: ( if_stmt | while_stmt | for_stmt | try_stmt | with_stmt | ( ( decorators )? DEF )=> funcdef | classdef )
			int alt65=7;
			int LA65_0 = input.LA(1);
			if ( (LA65_0==IF) ) {
				alt65=1;
			}
			else if ( (LA65_0==WHILE) ) {
				alt65=2;
			}
			else if ( (LA65_0==FOR) ) {
				alt65=3;
			}
			else if ( (LA65_0==TRY) ) {
				alt65=4;
			}
			else if ( (LA65_0==WITH) ) {
				alt65=5;
			}
			else if ( (LA65_0==AT) ) {
				int LA65_6 = input.LA(2);
				if ( (synpred6_Truffle()) ) {
					alt65=6;
				}
				else if ( (true) ) {
					alt65=7;
				}

			}
			else if ( (LA65_0==DEF) && (synpred6_Truffle())) {
				alt65=6;
			}
			else if ( (LA65_0==CLASS) ) {
				alt65=7;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 65, 0, input);
				throw nvae;
			}

			switch (alt65) {
				case 1 :
					// Truffle.g:1070:7: if_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_if_stmt_in_compound_stmt3568);
					if_stmt132=if_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, if_stmt132.getTree());

					}
					break;
				case 2 :
					// Truffle.g:1071:7: while_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_while_stmt_in_compound_stmt3576);
					while_stmt133=while_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, while_stmt133.getTree());

					}
					break;
				case 3 :
					// Truffle.g:1072:7: for_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_for_stmt_in_compound_stmt3584);
					for_stmt134=for_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, for_stmt134.getTree());

					}
					break;
				case 4 :
					// Truffle.g:1073:7: try_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_try_stmt_in_compound_stmt3592);
					try_stmt135=try_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, try_stmt135.getTree());

					}
					break;
				case 5 :
					// Truffle.g:1074:7: with_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_with_stmt_in_compound_stmt3600);
					with_stmt136=with_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, with_stmt136.getTree());

					}
					break;
				case 6 :
					// Truffle.g:1075:7: ( ( decorators )? DEF )=> funcdef
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_funcdef_in_compound_stmt3617);
					funcdef137=funcdef();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, funcdef137.getTree());

					}
					break;
				case 7 :
					// Truffle.g:1076:7: classdef
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_classdef_in_compound_stmt3625);
					classdef138=classdef();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, classdef138.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "compound_stmt"


	public static class if_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "if_stmt"
	// Truffle.g:1080:1: if_stmt : IF test[expr_contextType.Load] COLON ifsuite= suite[false] ( elif_clause )? ;
	public final TruffleParser.if_stmt_return if_stmt() throws RecognitionException {
		TruffleParser.if_stmt_return retval = new TruffleParser.if_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token IF139=null;
		Token COLON141=null;
		ParserRuleReturnScope ifsuite =null;
		ParserRuleReturnScope test140 =null;
		ParserRuleReturnScope elif_clause142 =null;

		PNode IF139_tree=null;
		PNode COLON141_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:1087:5: ( IF test[expr_contextType.Load] COLON ifsuite= suite[false] ( elif_clause )? )
			// Truffle.g:1087:7: IF test[expr_contextType.Load] COLON ifsuite= suite[false] ( elif_clause )?
			{
			root_0 = (PNode)adaptor.nil();


			IF139=(Token)match(input,IF,FOLLOW_IF_in_if_stmt3653); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IF139_tree = (PNode)adaptor.create(IF139);
			adaptor.addChild(root_0, IF139_tree);
			}

			pushFollow(FOLLOW_test_in_if_stmt3655);
			test140=test(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, test140.getTree());

			COLON141=(Token)match(input,COLON,FOLLOW_COLON_in_if_stmt3658); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON141_tree = (PNode)adaptor.create(COLON141);
			adaptor.addChild(root_0, COLON141_tree);
			}

			pushFollow(FOLLOW_suite_in_if_stmt3662);
			ifsuite=suite(false);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, ifsuite.getTree());

			// Truffle.g:1087:65: ( elif_clause )?
			int alt66=2;
			int LA66_0 = input.LA(1);
			if ( (LA66_0==ELIF||LA66_0==ORELSE) ) {
				alt66=1;
			}
			switch (alt66) {
				case 1 :
					// Truffle.g:1087:65: elif_clause
					{
					pushFollow(FOLLOW_elif_clause_in_if_stmt3665);
					elif_clause142=elif_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, elif_clause142.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          stype = actions.makeIf(IF139, actions.castExpr((test140!=null?((PNode)test140.getTree()):null)), actions.castStmts((ifsuite!=null?((TruffleParser.suite_return)ifsuite).stypes:null)),
			              actions.makeElse((elif_clause142!=null?((TruffleParser.elif_clause_return)elif_clause142).stypes:null), (elif_clause142!=null?((PNode)elif_clause142.getTree()):null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "if_stmt"


	public static class elif_clause_return extends ParserRuleReturnScope {
		public List stypes;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "elif_clause"
	// Truffle.g:1095:1: elif_clause returns [List stypes] : ( else_clause | ELIF test[expr_contextType.Load] COLON suite[false] (e2= elif_clause |) );
	public final TruffleParser.elif_clause_return elif_clause() throws RecognitionException {
		TruffleParser.elif_clause_return retval = new TruffleParser.elif_clause_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token ELIF144=null;
		Token COLON146=null;
		ParserRuleReturnScope e2 =null;
		ParserRuleReturnScope else_clause143 =null;
		ParserRuleReturnScope test145 =null;
		ParserRuleReturnScope suite147 =null;

		PNode ELIF144_tree=null;
		PNode COLON146_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:1105:5: ( else_clause | ELIF test[expr_contextType.Load] COLON suite[false] (e2= elif_clause |) )
			int alt68=2;
			int LA68_0 = input.LA(1);
			if ( (LA68_0==ORELSE) ) {
				alt68=1;
			}
			else if ( (LA68_0==ELIF) ) {
				alt68=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 68, 0, input);
				throw nvae;
			}

			switch (alt68) {
				case 1 :
					// Truffle.g:1105:7: else_clause
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_else_clause_in_elif_clause3710);
					else_clause143=else_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, else_clause143.getTree());

					if ( state.backtracking==0 ) {
					          retval.stypes = (else_clause143!=null?((TruffleParser.else_clause_return)else_clause143).stypes:null);
					      }
					}
					break;
				case 2 :
					// Truffle.g:1109:7: ELIF test[expr_contextType.Load] COLON suite[false] (e2= elif_clause |)
					{
					root_0 = (PNode)adaptor.nil();


					ELIF144=(Token)match(input,ELIF,FOLLOW_ELIF_in_elif_clause3726); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ELIF144_tree = (PNode)adaptor.create(ELIF144);
					adaptor.addChild(root_0, ELIF144_tree);
					}

					pushFollow(FOLLOW_test_in_elif_clause3728);
					test145=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, test145.getTree());

					COLON146=(Token)match(input,COLON,FOLLOW_COLON_in_elif_clause3731); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COLON146_tree = (PNode)adaptor.create(COLON146);
					adaptor.addChild(root_0, COLON146_tree);
					}

					pushFollow(FOLLOW_suite_in_elif_clause3733);
					suite147=suite(false);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, suite147.getTree());

					// Truffle.g:1110:7: (e2= elif_clause |)
					int alt67=2;
					int LA67_0 = input.LA(1);
					if ( (LA67_0==ELIF||LA67_0==ORELSE) ) {
						alt67=1;
					}
					else if ( (LA67_0==EOF||LA67_0==ASSERT||(LA67_0 >= AT && LA67_0 <= BREAK)||LA67_0==CLASS||(LA67_0 >= COMPLEX && LA67_0 <= CONTINUE)||(LA67_0 >= DEDENT && LA67_0 <= DELETE)||LA67_0==FALSE||(LA67_0 >= FLOAT && LA67_0 <= GLOBAL)||(LA67_0 >= IF && LA67_0 <= IMPORT)||LA67_0==INT||(LA67_0 >= LAMBDA && LA67_0 <= LCURLY)||(LA67_0 >= LPAREN && LA67_0 <= MINUS)||(LA67_0 >= NAME && LA67_0 <= NOT)||LA67_0==PASS||LA67_0==PLUS||(LA67_0 >= PRINT && LA67_0 <= RAISE)||LA67_0==RETURN||(LA67_0 >= STRING && LA67_0 <= TILDE)||(LA67_0 >= TRUE && LA67_0 <= TRY)||(LA67_0 >= WHILE && LA67_0 <= WITH)||LA67_0==YIELD) ) {
						alt67=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 67, 0, input);
						throw nvae;
					}

					switch (alt67) {
						case 1 :
							// Truffle.g:1110:8: e2= elif_clause
							{
							pushFollow(FOLLOW_elif_clause_in_elif_clause3745);
							e2=elif_clause();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, e2.getTree());

							if ( state.backtracking==0 ) {
							           stype = actions.makeIf((test145!=null?(test145.start):null), actions.castExpr((test145!=null?((PNode)test145.getTree()):null)), actions.castStmts((suite147!=null?((TruffleParser.suite_return)suite147).stypes:null)), actions.makeElse((e2!=null?((TruffleParser.elif_clause_return)e2).stypes:null), (e2!=null?((PNode)e2.getTree()):null)));
							       }
							}
							break;
						case 2 :
							// Truffle.g:1115:8: 
							{
							if ( state.backtracking==0 ) {
							           stype = actions.makeIf((test145!=null?(test145.start):null), actions.castExpr((test145!=null?((PNode)test145.getTree()):null)), actions.castStmts((suite147!=null?((TruffleParser.suite_return)suite147).stypes:null)), new ArrayList<PNode>());
							       }
							}
							break;

					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   if (stype != null) {
			       retval.tree = stype;
			   }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "elif_clause"


	public static class else_clause_return extends ParserRuleReturnScope {
		public List stypes;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "else_clause"
	// Truffle.g:1122:1: else_clause returns [List stypes] : ORELSE COLON elsesuite= suite[false] ;
	public final TruffleParser.else_clause_return else_clause() throws RecognitionException {
		TruffleParser.else_clause_return retval = new TruffleParser.else_clause_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token ORELSE148=null;
		Token COLON149=null;
		ParserRuleReturnScope elsesuite =null;

		PNode ORELSE148_tree=null;
		PNode COLON149_tree=null;

		try {
			// Truffle.g:1124:5: ( ORELSE COLON elsesuite= suite[false] )
			// Truffle.g:1124:7: ORELSE COLON elsesuite= suite[false]
			{
			root_0 = (PNode)adaptor.nil();


			ORELSE148=(Token)match(input,ORELSE,FOLLOW_ORELSE_in_else_clause3805); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ORELSE148_tree = (PNode)adaptor.create(ORELSE148);
			adaptor.addChild(root_0, ORELSE148_tree);
			}

			COLON149=(Token)match(input,COLON,FOLLOW_COLON_in_else_clause3807); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON149_tree = (PNode)adaptor.create(COLON149);
			adaptor.addChild(root_0, COLON149_tree);
			}

			pushFollow(FOLLOW_suite_in_else_clause3811);
			elsesuite=suite(false);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, elsesuite.getTree());

			if ( state.backtracking==0 ) {
			          retval.stypes = (elsesuite!=null?((TruffleParser.suite_return)elsesuite).stypes:null);
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "else_clause"


	public static class while_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "while_stmt"
	// Truffle.g:1131:1: while_stmt : WHILE test[expr_contextType.Load] COLON s1= suite[false] ( ORELSE COLON s2= suite[false] )? ;
	public final TruffleParser.while_stmt_return while_stmt() throws RecognitionException {
		TruffleParser.while_stmt_return retval = new TruffleParser.while_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token WHILE150=null;
		Token COLON152=null;
		Token ORELSE153=null;
		Token COLON154=null;
		ParserRuleReturnScope s1 =null;
		ParserRuleReturnScope s2 =null;
		ParserRuleReturnScope test151 =null;

		PNode WHILE150_tree=null;
		PNode COLON152_tree=null;
		PNode ORELSE153_tree=null;
		PNode COLON154_tree=null;


		    StatementNode stype = null;
		    actions.beginLoopLevel();

		try {
			// Truffle.g:1139:5: ( WHILE test[expr_contextType.Load] COLON s1= suite[false] ( ORELSE COLON s2= suite[false] )? )
			// Truffle.g:1139:7: WHILE test[expr_contextType.Load] COLON s1= suite[false] ( ORELSE COLON s2= suite[false] )?
			{
			root_0 = (PNode)adaptor.nil();


			WHILE150=(Token)match(input,WHILE,FOLLOW_WHILE_in_while_stmt3848); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WHILE150_tree = (PNode)adaptor.create(WHILE150);
			adaptor.addChild(root_0, WHILE150_tree);
			}

			pushFollow(FOLLOW_test_in_while_stmt3850);
			test151=test(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, test151.getTree());

			COLON152=(Token)match(input,COLON,FOLLOW_COLON_in_while_stmt3853); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON152_tree = (PNode)adaptor.create(COLON152);
			adaptor.addChild(root_0, COLON152_tree);
			}

			pushFollow(FOLLOW_suite_in_while_stmt3857);
			s1=suite(false);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, s1.getTree());

			// Truffle.g:1139:63: ( ORELSE COLON s2= suite[false] )?
			int alt69=2;
			int LA69_0 = input.LA(1);
			if ( (LA69_0==ORELSE) ) {
				alt69=1;
			}
			switch (alt69) {
				case 1 :
					// Truffle.g:1139:64: ORELSE COLON s2= suite[false]
					{
					ORELSE153=(Token)match(input,ORELSE,FOLLOW_ORELSE_in_while_stmt3861); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ORELSE153_tree = (PNode)adaptor.create(ORELSE153);
					adaptor.addChild(root_0, ORELSE153_tree);
					}

					COLON154=(Token)match(input,COLON,FOLLOW_COLON_in_while_stmt3863); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COLON154_tree = (PNode)adaptor.create(COLON154);
					adaptor.addChild(root_0, COLON154_tree);
					}

					pushFollow(FOLLOW_suite_in_while_stmt3867);
					s2=suite(false);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, s2.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          stype = actions.makeWhile(WHILE150, actions.castExpr((test151!=null?((PNode)test151.getTree()):null)), (s1!=null?((TruffleParser.suite_return)s1).stypes:null), (s2!=null?((TruffleParser.suite_return)s2).stypes:null));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "while_stmt"


	public static class for_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "for_stmt"
	// Truffle.g:1146:1: for_stmt : FOR exprlist[expr_contextType.Store] IN testlist[expr_contextType.Load] COLON s1= suite[false] ( ORELSE COLON s2= suite[false] )? ;
	public final TruffleParser.for_stmt_return for_stmt() throws RecognitionException {
		TruffleParser.for_stmt_return retval = new TruffleParser.for_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token FOR155=null;
		Token IN157=null;
		Token COLON159=null;
		Token ORELSE160=null;
		Token COLON161=null;
		ParserRuleReturnScope s1 =null;
		ParserRuleReturnScope s2 =null;
		ParserRuleReturnScope exprlist156 =null;
		ParserRuleReturnScope testlist158 =null;

		PNode FOR155_tree=null;
		PNode IN157_tree=null;
		PNode COLON159_tree=null;
		PNode ORELSE160_tree=null;
		PNode COLON161_tree=null;


		    StatementNode stype = null;
		    actions.beginLoopLevel();

		try {
			// Truffle.g:1154:5: ( FOR exprlist[expr_contextType.Store] IN testlist[expr_contextType.Load] COLON s1= suite[false] ( ORELSE COLON s2= suite[false] )? )
			// Truffle.g:1154:7: FOR exprlist[expr_contextType.Store] IN testlist[expr_contextType.Load] COLON s1= suite[false] ( ORELSE COLON s2= suite[false] )?
			{
			root_0 = (PNode)adaptor.nil();


			FOR155=(Token)match(input,FOR,FOLLOW_FOR_in_for_stmt3906); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			FOR155_tree = (PNode)adaptor.create(FOR155);
			adaptor.addChild(root_0, FOR155_tree);
			}

			pushFollow(FOLLOW_exprlist_in_for_stmt3908);
			exprlist156=exprlist(expr_contextType.Store);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, exprlist156.getTree());

			IN157=(Token)match(input,IN,FOLLOW_IN_in_for_stmt3911); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN157_tree = (PNode)adaptor.create(IN157);
			adaptor.addChild(root_0, IN157_tree);
			}

			pushFollow(FOLLOW_testlist_in_for_stmt3913);
			testlist158=testlist(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, testlist158.getTree());

			COLON159=(Token)match(input,COLON,FOLLOW_COLON_in_for_stmt3916); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON159_tree = (PNode)adaptor.create(COLON159);
			adaptor.addChild(root_0, COLON159_tree);
			}

			pushFollow(FOLLOW_suite_in_for_stmt3920);
			s1=suite(false);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, s1.getTree());

			// Truffle.g:1155:9: ( ORELSE COLON s2= suite[false] )?
			int alt70=2;
			int LA70_0 = input.LA(1);
			if ( (LA70_0==ORELSE) ) {
				alt70=1;
			}
			switch (alt70) {
				case 1 :
					// Truffle.g:1155:10: ORELSE COLON s2= suite[false]
					{
					ORELSE160=(Token)match(input,ORELSE,FOLLOW_ORELSE_in_for_stmt3932); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ORELSE160_tree = (PNode)adaptor.create(ORELSE160);
					adaptor.addChild(root_0, ORELSE160_tree);
					}

					COLON161=(Token)match(input,COLON,FOLLOW_COLON_in_for_stmt3934); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COLON161_tree = (PNode)adaptor.create(COLON161);
					adaptor.addChild(root_0, COLON161_tree);
					}

					pushFollow(FOLLOW_suite_in_for_stmt3938);
					s2=suite(false);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, s2.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          stype = actions.makeFor(FOR155, (exprlist156!=null?((TruffleParser.exprlist_return)exprlist156).etype:null), actions.castExpr((testlist158!=null?((PNode)testlist158.getTree()):null)), (s1!=null?((TruffleParser.suite_return)s1).stypes:null), (s2!=null?((TruffleParser.suite_return)s2).stypes:null));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "for_stmt"


	public static class try_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "try_stmt"
	// Truffle.g:1166:1: try_stmt : TRY COLON trysuite= suite[!$suite.isEmpty() && $suite::continueIllegal] ( (e+= except_clause )+ ( ORELSE COLON elsesuite= suite[!$suite.isEmpty() && $suite::continueIllegal] )? ( FINALLY COLON finalsuite= suite[true] )? | FINALLY COLON finalsuite= suite[true] ) ;
	public final TruffleParser.try_stmt_return try_stmt() throws RecognitionException {
		TruffleParser.try_stmt_return retval = new TruffleParser.try_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token TRY162=null;
		Token COLON163=null;
		Token ORELSE164=null;
		Token COLON165=null;
		Token FINALLY166=null;
		Token COLON167=null;
		Token FINALLY168=null;
		Token COLON169=null;
		List<Object> list_e=null;
		ParserRuleReturnScope trysuite =null;
		ParserRuleReturnScope elsesuite =null;
		ParserRuleReturnScope finalsuite =null;
		RuleReturnScope e = null;
		PNode TRY162_tree=null;
		PNode COLON163_tree=null;
		PNode ORELSE164_tree=null;
		PNode COLON165_tree=null;
		PNode FINALLY166_tree=null;
		PNode COLON167_tree=null;
		PNode FINALLY168_tree=null;
		PNode COLON169_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:1173:5: ( TRY COLON trysuite= suite[!$suite.isEmpty() && $suite::continueIllegal] ( (e+= except_clause )+ ( ORELSE COLON elsesuite= suite[!$suite.isEmpty() && $suite::continueIllegal] )? ( FINALLY COLON finalsuite= suite[true] )? | FINALLY COLON finalsuite= suite[true] ) )
			// Truffle.g:1173:7: TRY COLON trysuite= suite[!$suite.isEmpty() && $suite::continueIllegal] ( (e+= except_clause )+ ( ORELSE COLON elsesuite= suite[!$suite.isEmpty() && $suite::continueIllegal] )? ( FINALLY COLON finalsuite= suite[true] )? | FINALLY COLON finalsuite= suite[true] )
			{
			root_0 = (PNode)adaptor.nil();


			TRY162=(Token)match(input,TRY,FOLLOW_TRY_in_try_stmt3981); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			TRY162_tree = (PNode)adaptor.create(TRY162);
			adaptor.addChild(root_0, TRY162_tree);
			}

			COLON163=(Token)match(input,COLON,FOLLOW_COLON_in_try_stmt3983); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON163_tree = (PNode)adaptor.create(COLON163);
			adaptor.addChild(root_0, COLON163_tree);
			}

			pushFollow(FOLLOW_suite_in_try_stmt3987);
			trysuite=suite(!suite_stack.isEmpty() && suite_stack.peek().continueIllegal);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, trysuite.getTree());

			// Truffle.g:1174:7: ( (e+= except_clause )+ ( ORELSE COLON elsesuite= suite[!$suite.isEmpty() && $suite::continueIllegal] )? ( FINALLY COLON finalsuite= suite[true] )? | FINALLY COLON finalsuite= suite[true] )
			int alt74=2;
			int LA74_0 = input.LA(1);
			if ( (LA74_0==EXCEPT) ) {
				alt74=1;
			}
			else if ( (LA74_0==FINALLY) ) {
				alt74=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 74, 0, input);
				throw nvae;
			}

			switch (alt74) {
				case 1 :
					// Truffle.g:1174:9: (e+= except_clause )+ ( ORELSE COLON elsesuite= suite[!$suite.isEmpty() && $suite::continueIllegal] )? ( FINALLY COLON finalsuite= suite[true] )?
					{
					// Truffle.g:1174:10: (e+= except_clause )+
					int cnt71=0;
					loop71:
					while (true) {
						int alt71=2;
						int LA71_0 = input.LA(1);
						if ( (LA71_0==EXCEPT) ) {
							alt71=1;
						}

						switch (alt71) {
						case 1 :
							// Truffle.g:1174:10: e+= except_clause
							{
							pushFollow(FOLLOW_except_clause_in_try_stmt4000);
							e=except_clause();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, e.getTree());

							if (list_e==null) list_e=new ArrayList<Object>();
							list_e.add(e.getTree());
							}
							break;

						default :
							if ( cnt71 >= 1 ) break loop71;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(71, input);
							throw eee;
						}
						cnt71++;
					}

					// Truffle.g:1174:27: ( ORELSE COLON elsesuite= suite[!$suite.isEmpty() && $suite::continueIllegal] )?
					int alt72=2;
					int LA72_0 = input.LA(1);
					if ( (LA72_0==ORELSE) ) {
						alt72=1;
					}
					switch (alt72) {
						case 1 :
							// Truffle.g:1174:28: ORELSE COLON elsesuite= suite[!$suite.isEmpty() && $suite::continueIllegal]
							{
							ORELSE164=(Token)match(input,ORELSE,FOLLOW_ORELSE_in_try_stmt4004); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							ORELSE164_tree = (PNode)adaptor.create(ORELSE164);
							adaptor.addChild(root_0, ORELSE164_tree);
							}

							COLON165=(Token)match(input,COLON,FOLLOW_COLON_in_try_stmt4006); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COLON165_tree = (PNode)adaptor.create(COLON165);
							adaptor.addChild(root_0, COLON165_tree);
							}

							pushFollow(FOLLOW_suite_in_try_stmt4010);
							elsesuite=suite(!suite_stack.isEmpty() && suite_stack.peek().continueIllegal);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, elsesuite.getTree());

							}
							break;

					}

					// Truffle.g:1174:105: ( FINALLY COLON finalsuite= suite[true] )?
					int alt73=2;
					int LA73_0 = input.LA(1);
					if ( (LA73_0==FINALLY) ) {
						alt73=1;
					}
					switch (alt73) {
						case 1 :
							// Truffle.g:1174:106: FINALLY COLON finalsuite= suite[true]
							{
							FINALLY166=(Token)match(input,FINALLY,FOLLOW_FINALLY_in_try_stmt4016); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							FINALLY166_tree = (PNode)adaptor.create(FINALLY166);
							adaptor.addChild(root_0, FINALLY166_tree);
							}

							COLON167=(Token)match(input,COLON,FOLLOW_COLON_in_try_stmt4018); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COLON167_tree = (PNode)adaptor.create(COLON167);
							adaptor.addChild(root_0, COLON167_tree);
							}

							pushFollow(FOLLOW_suite_in_try_stmt4022);
							finalsuite=suite(true);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, finalsuite.getTree());

							}
							break;

					}

					if ( state.backtracking==0 ) {
					            stype = actions.makeTryExcept(TRY162, (trysuite!=null?((TruffleParser.suite_return)trysuite).stypes:null), list_e, (elsesuite!=null?((TruffleParser.suite_return)elsesuite).stypes:null), (finalsuite!=null?((TruffleParser.suite_return)finalsuite).stypes:null));
					        }
					}
					break;
				case 2 :
					// Truffle.g:1178:9: FINALLY COLON finalsuite= suite[true]
					{
					FINALLY168=(Token)match(input,FINALLY,FOLLOW_FINALLY_in_try_stmt4045); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					FINALLY168_tree = (PNode)adaptor.create(FINALLY168);
					adaptor.addChild(root_0, FINALLY168_tree);
					}

					COLON169=(Token)match(input,COLON,FOLLOW_COLON_in_try_stmt4047); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COLON169_tree = (PNode)adaptor.create(COLON169);
					adaptor.addChild(root_0, COLON169_tree);
					}

					pushFollow(FOLLOW_suite_in_try_stmt4051);
					finalsuite=suite(true);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, finalsuite.getTree());

					if ( state.backtracking==0 ) {
					            stype = actions.makeTryFinally(TRY162, (trysuite!=null?((TruffleParser.suite_return)trysuite).stypes:null), (finalsuite!=null?((TruffleParser.suite_return)finalsuite).stypes:null));
					        }
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "try_stmt"


	public static class with_stmt_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "with_stmt"
	// Truffle.g:1186:1: with_stmt : WITH w+= with_item ( options {greedy=true; } : COMMA w+= with_item )* COLON suite[false] ;
	public final TruffleParser.with_stmt_return with_stmt() throws RecognitionException {
		TruffleParser.with_stmt_return retval = new TruffleParser.with_stmt_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token WITH170=null;
		Token COMMA171=null;
		Token COLON172=null;
		List<Object> list_w=null;
		ParserRuleReturnScope suite173 =null;
		RuleReturnScope w = null;
		PNode WITH170_tree=null;
		PNode COMMA171_tree=null;
		PNode COLON172_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:1193:5: ( WITH w+= with_item ( options {greedy=true; } : COMMA w+= with_item )* COLON suite[false] )
			// Truffle.g:1193:7: WITH w+= with_item ( options {greedy=true; } : COMMA w+= with_item )* COLON suite[false]
			{
			root_0 = (PNode)adaptor.nil();


			WITH170=(Token)match(input,WITH,FOLLOW_WITH_in_with_stmt4100); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WITH170_tree = (PNode)adaptor.create(WITH170);
			adaptor.addChild(root_0, WITH170_tree);
			}

			pushFollow(FOLLOW_with_item_in_with_stmt4104);
			w=with_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, w.getTree());

			if (list_w==null) list_w=new ArrayList<Object>();
			list_w.add(w.getTree());
			// Truffle.g:1193:25: ( options {greedy=true; } : COMMA w+= with_item )*
			loop75:
			while (true) {
				int alt75=2;
				int LA75_0 = input.LA(1);
				if ( (LA75_0==COMMA) ) {
					alt75=1;
				}

				switch (alt75) {
				case 1 :
					// Truffle.g:1193:49: COMMA w+= with_item
					{
					COMMA171=(Token)match(input,COMMA,FOLLOW_COMMA_in_with_stmt4114); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA171_tree = (PNode)adaptor.create(COMMA171);
					adaptor.addChild(root_0, COMMA171_tree);
					}

					pushFollow(FOLLOW_with_item_in_with_stmt4118);
					w=with_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, w.getTree());

					if (list_w==null) list_w=new ArrayList<Object>();
					list_w.add(w.getTree());
					}
					break;

				default :
					break loop75;
				}
			}

			COLON172=(Token)match(input,COLON,FOLLOW_COLON_in_with_stmt4122); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON172_tree = (PNode)adaptor.create(COLON172);
			adaptor.addChild(root_0, COLON172_tree);
			}

			pushFollow(FOLLOW_suite_in_with_stmt4124);
			suite173=suite(false);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, suite173.getTree());

			if ( state.backtracking==0 ) {
			          stype = actions.makeWith(WITH170, list_w, (suite173!=null?((TruffleParser.suite_return)suite173).stypes:null));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "with_stmt"


	public static class with_item_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "with_item"
	// Truffle.g:1200:1: with_item : test[expr_contextType.Load] ( AS expr[expr_contextType.Store] )? ;
	public final TruffleParser.with_item_return with_item() throws RecognitionException {
		TruffleParser.with_item_return retval = new TruffleParser.with_item_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token AS175=null;
		ParserRuleReturnScope test174 =null;
		ParserRuleReturnScope expr176 =null;

		PNode AS175_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:1207:5: ( test[expr_contextType.Load] ( AS expr[expr_contextType.Store] )? )
			// Truffle.g:1207:7: test[expr_contextType.Load] ( AS expr[expr_contextType.Store] )?
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_test_in_with_item4161);
			test174=test(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, test174.getTree());

			// Truffle.g:1207:35: ( AS expr[expr_contextType.Store] )?
			int alt76=2;
			int LA76_0 = input.LA(1);
			if ( (LA76_0==AS) ) {
				alt76=1;
			}
			switch (alt76) {
				case 1 :
					// Truffle.g:1207:36: AS expr[expr_contextType.Store]
					{
					AS175=(Token)match(input,AS,FOLLOW_AS_in_with_item4165); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					AS175_tree = (PNode)adaptor.create(AS175);
					adaptor.addChild(root_0, AS175_tree);
					}

					pushFollow(FOLLOW_expr_in_with_item4167);
					expr176=expr(expr_contextType.Store);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, expr176.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          PNode item = actions.castExpr((test174!=null?((PNode)test174.getTree()):null));
			          PNode var = null;
			          if ((expr176!=null?(expr176.start):null) != null) {
			              var = actions.castExpr((expr176!=null?((PNode)expr176.getTree()):null));
			              actions.checkAssign(var);
			          }
			          stype = actions.makeWith((test174!=null?(test174.start):null), item, var, null);
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "with_item"


	public static class except_clause_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "except_clause"
	// Truffle.g:1220:1: except_clause : EXCEPT (t1= test[expr_contextType.Load] ( ( COMMA | AS ) t2= test[expr_contextType.Store] )? )? COLON suite[!$suite.isEmpty() && $suite::continueIllegal] ;
	public final TruffleParser.except_clause_return except_clause() throws RecognitionException {
		TruffleParser.except_clause_return retval = new TruffleParser.except_clause_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token EXCEPT177=null;
		Token set178=null;
		Token COLON179=null;
		ParserRuleReturnScope t1 =null;
		ParserRuleReturnScope t2 =null;
		ParserRuleReturnScope suite180 =null;

		PNode EXCEPT177_tree=null;
		PNode set178_tree=null;
		PNode COLON179_tree=null;


		    PNode extype = null;

		try {
			// Truffle.g:1227:5: ( EXCEPT (t1= test[expr_contextType.Load] ( ( COMMA | AS ) t2= test[expr_contextType.Store] )? )? COLON suite[!$suite.isEmpty() && $suite::continueIllegal] )
			// Truffle.g:1227:7: EXCEPT (t1= test[expr_contextType.Load] ( ( COMMA | AS ) t2= test[expr_contextType.Store] )? )? COLON suite[!$suite.isEmpty() && $suite::continueIllegal]
			{
			root_0 = (PNode)adaptor.nil();


			EXCEPT177=(Token)match(input,EXCEPT,FOLLOW_EXCEPT_in_except_clause4206); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			EXCEPT177_tree = (PNode)adaptor.create(EXCEPT177);
			adaptor.addChild(root_0, EXCEPT177_tree);
			}

			// Truffle.g:1227:14: (t1= test[expr_contextType.Load] ( ( COMMA | AS ) t2= test[expr_contextType.Store] )? )?
			int alt78=2;
			int LA78_0 = input.LA(1);
			if ( (LA78_0==BACKQUOTE||(LA78_0 >= LBRACK && LA78_0 <= LCURLY)||(LA78_0 >= LPAREN && LA78_0 <= MINUS)||LA78_0==NAME||LA78_0==NOT||LA78_0==PLUS||LA78_0==TILDE) ) {
				alt78=1;
			}
			else if ( (LA78_0==PRINT) && ((printFunction))) {
				alt78=1;
			}
			else if ( (LA78_0==COMPLEX||LA78_0==FALSE||LA78_0==FLOAT||LA78_0==INT||LA78_0==LAMBDA||LA78_0==NONE||LA78_0==STRING||LA78_0==TRUE) ) {
				alt78=1;
			}
			switch (alt78) {
				case 1 :
					// Truffle.g:1227:15: t1= test[expr_contextType.Load] ( ( COMMA | AS ) t2= test[expr_contextType.Store] )?
					{
					pushFollow(FOLLOW_test_in_except_clause4211);
					t1=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t1.getTree());

					// Truffle.g:1227:46: ( ( COMMA | AS ) t2= test[expr_contextType.Store] )?
					int alt77=2;
					int LA77_0 = input.LA(1);
					if ( (LA77_0==AS||LA77_0==COMMA) ) {
						alt77=1;
					}
					switch (alt77) {
						case 1 :
							// Truffle.g:1227:47: ( COMMA | AS ) t2= test[expr_contextType.Store]
							{
							set178=input.LT(1);
							if ( input.LA(1)==AS||input.LA(1)==COMMA ) {
								input.consume();
								if ( state.backtracking==0 ) adaptor.addChild(root_0, (PNode)adaptor.create(set178));
								state.errorRecovery=false;
								state.failed=false;
							}
							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							pushFollow(FOLLOW_test_in_except_clause4225);
							t2=test(expr_contextType.Store);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, t2.getTree());

							}
							break;

					}

					}
					break;

			}

			COLON179=(Token)match(input,COLON,FOLLOW_COLON_in_except_clause4232); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON179_tree = (PNode)adaptor.create(COLON179);
			adaptor.addChild(root_0, COLON179_tree);
			}

			pushFollow(FOLLOW_suite_in_except_clause4234);
			suite180=suite(!suite_stack.isEmpty() && suite_stack.peek().continueIllegal);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, suite180.getTree());

			if ( state.backtracking==0 ) {
			          extype = actions.makeExceptHandler(EXCEPT177, actions.castExpr((t1!=null?((PNode)t1.getTree()):null)), actions.castExpr((t2!=null?((PNode)t2.getTree()):null)),
			              actions.castStmts((suite180!=null?((TruffleParser.suite_return)suite180).stypes:null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = extype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "except_clause"


	protected static class suite_scope {
		boolean continueIllegal;
	}
	protected Stack<suite_scope> suite_stack = new Stack<suite_scope>();

	public static class suite_return extends ParserRuleReturnScope {
		public List stypes;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "suite"
	// Truffle.g:1235:1: suite[boolean fromFinally] returns [List stypes] : ( simple_stmt | NEWLINE INDENT ( stmt )+ DEDENT );
	public final TruffleParser.suite_return suite(boolean fromFinally) throws RecognitionException {
		suite_stack.push(new suite_scope());
		TruffleParser.suite_return retval = new TruffleParser.suite_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token NEWLINE182=null;
		Token INDENT183=null;
		Token DEDENT185=null;
		ParserRuleReturnScope simple_stmt181 =null;
		ParserRuleReturnScope stmt184 =null;

		PNode NEWLINE182_tree=null;
		PNode INDENT183_tree=null;
		PNode DEDENT185_tree=null;


		    if (suite_stack.peek().continueIllegal || fromFinally) {
		        suite_stack.peek().continueIllegal = true;
		    } else {
		        suite_stack.peek().continueIllegal = false;
		    }
		    retval.stypes = new ArrayList();

		try {
			// Truffle.g:1248:5: ( simple_stmt | NEWLINE INDENT ( stmt )+ DEDENT )
			int alt80=2;
			int LA80_0 = input.LA(1);
			if ( (LA80_0==BACKQUOTE||(LA80_0 >= LBRACK && LA80_0 <= LCURLY)||(LA80_0 >= LPAREN && LA80_0 <= MINUS)||LA80_0==NAME||LA80_0==NOT||LA80_0==PLUS||LA80_0==TILDE) ) {
				alt80=1;
			}
			else if ( (LA80_0==PRINT) && (((!printFunction)||(printFunction)))) {
				alt80=1;
			}
			else if ( (LA80_0==ASSERT||LA80_0==BREAK||(LA80_0 >= COMPLEX && LA80_0 <= CONTINUE)||LA80_0==DELETE||LA80_0==FALSE||LA80_0==FLOAT||(LA80_0 >= FROM && LA80_0 <= GLOBAL)||LA80_0==IMPORT||LA80_0==INT||LA80_0==LAMBDA||(LA80_0 >= NONE && LA80_0 <= NONLOCAL)||LA80_0==PASS||LA80_0==RAISE||LA80_0==RETURN||LA80_0==STRING||LA80_0==TRUE||LA80_0==YIELD) ) {
				alt80=1;
			}
			else if ( (LA80_0==NEWLINE) ) {
				alt80=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 80, 0, input);
				throw nvae;
			}

			switch (alt80) {
				case 1 :
					// Truffle.g:1248:7: simple_stmt
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_simple_stmt_in_suite4280);
					simple_stmt181=simple_stmt();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_stmt181.getTree());

					if ( state.backtracking==0 ) {
					          retval.stypes = (simple_stmt181!=null?((TruffleParser.simple_stmt_return)simple_stmt181).stypes:null);
					      }
					}
					break;
				case 2 :
					// Truffle.g:1252:7: NEWLINE INDENT ( stmt )+ DEDENT
					{
					root_0 = (PNode)adaptor.nil();


					NEWLINE182=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_suite4296); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NEWLINE182_tree = (PNode)adaptor.create(NEWLINE182);
					adaptor.addChild(root_0, NEWLINE182_tree);
					}

					INDENT183=(Token)match(input,INDENT,FOLLOW_INDENT_in_suite4298); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					INDENT183_tree = (PNode)adaptor.create(INDENT183);
					adaptor.addChild(root_0, INDENT183_tree);
					}

					// Truffle.g:1253:7: ( stmt )+
					int cnt79=0;
					loop79:
					while (true) {
						int alt79=2;
						int LA79_0 = input.LA(1);
						if ( (LA79_0==BACKQUOTE||(LA79_0 >= LBRACK && LA79_0 <= LCURLY)||(LA79_0 >= LPAREN && LA79_0 <= MINUS)||LA79_0==NAME||LA79_0==NOT||LA79_0==PLUS||LA79_0==TILDE) ) {
							alt79=1;
						}
						else if ( (LA79_0==PRINT) && (((!printFunction)||(printFunction)))) {
							alt79=1;
						}
						else if ( (LA79_0==ASSERT||LA79_0==AT||LA79_0==BREAK||LA79_0==CLASS||(LA79_0 >= COMPLEX && LA79_0 <= CONTINUE)||(LA79_0 >= DEF && LA79_0 <= DELETE)||LA79_0==FALSE||(LA79_0 >= FLOAT && LA79_0 <= GLOBAL)||(LA79_0 >= IF && LA79_0 <= IMPORT)||LA79_0==INT||LA79_0==LAMBDA||(LA79_0 >= NONE && LA79_0 <= NONLOCAL)||LA79_0==PASS||LA79_0==RAISE||LA79_0==RETURN||LA79_0==STRING||(LA79_0 >= TRUE && LA79_0 <= TRY)||(LA79_0 >= WHILE && LA79_0 <= WITH)||LA79_0==YIELD) ) {
							alt79=1;
						}

						switch (alt79) {
						case 1 :
							// Truffle.g:1253:8: stmt
							{
							pushFollow(FOLLOW_stmt_in_suite4307);
							stmt184=stmt();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, stmt184.getTree());

							if ( state.backtracking==0 ) {
							           if ((stmt184!=null?((TruffleParser.stmt_return)stmt184).stypes:null) != null) {
							               retval.stypes.addAll((stmt184!=null?((TruffleParser.stmt_return)stmt184).stypes:null));
							           }
							       }
							}
							break;

						default :
							if ( cnt79 >= 1 ) break loop79;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(79, input);
							throw eee;
						}
						cnt79++;
					}

					DEDENT185=(Token)match(input,DEDENT,FOLLOW_DEDENT_in_suite4327); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DEDENT185_tree = (PNode)adaptor.create(DEDENT185);
					adaptor.addChild(root_0, DEDENT185_tree);
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
			suite_stack.pop();
		}
		return retval;
	}
	// $ANTLR end "suite"


	public static class test_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "test"
	// Truffle.g:1263:1: test[expr_contextType ctype] : (o1= or_test[ctype] ( ( IF or_test[null] ORELSE )=> IF o2= or_test[ctype] ORELSE e= test[expr_contextType.Load] | -> or_test ) | lambdef );
	public final TruffleParser.test_return test(expr_contextType ctype) throws RecognitionException {
		TruffleParser.test_return retval = new TruffleParser.test_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token IF186=null;
		Token ORELSE187=null;
		ParserRuleReturnScope o1 =null;
		ParserRuleReturnScope o2 =null;
		ParserRuleReturnScope e =null;
		ParserRuleReturnScope lambdef188 =null;

		PNode IF186_tree=null;
		PNode ORELSE187_tree=null;
		RewriteRuleTokenStream stream_ORELSE=new RewriteRuleTokenStream(adaptor,"token ORELSE");
		RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
		RewriteRuleSubtreeStream stream_test=new RewriteRuleSubtreeStream(adaptor,"rule test");
		RewriteRuleSubtreeStream stream_or_test=new RewriteRuleSubtreeStream(adaptor,"rule or_test");


		    PNode etype = null;

		try {
			// Truffle.g:1272:5: (o1= or_test[ctype] ( ( IF or_test[null] ORELSE )=> IF o2= or_test[ctype] ORELSE e= test[expr_contextType.Load] | -> or_test ) | lambdef )
			int alt82=2;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==BACKQUOTE||(LA82_0 >= LBRACK && LA82_0 <= LCURLY)||(LA82_0 >= LPAREN && LA82_0 <= MINUS)||LA82_0==NAME||LA82_0==NOT||LA82_0==PLUS||LA82_0==TILDE) ) {
				alt82=1;
			}
			else if ( (LA82_0==PRINT) && ((printFunction))) {
				alt82=1;
			}
			else if ( (LA82_0==COMPLEX||LA82_0==FALSE||LA82_0==FLOAT||LA82_0==INT||LA82_0==NONE||LA82_0==STRING||LA82_0==TRUE) ) {
				alt82=1;
			}
			else if ( (LA82_0==LAMBDA) ) {
				alt82=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 82, 0, input);
				throw nvae;
			}

			switch (alt82) {
				case 1 :
					// Truffle.g:1272:6: o1= or_test[ctype] ( ( IF or_test[null] ORELSE )=> IF o2= or_test[ctype] ORELSE e= test[expr_contextType.Load] | -> or_test )
					{
					pushFollow(FOLLOW_or_test_in_test4357);
					o1=or_test(ctype);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_or_test.add(o1.getTree());
					// Truffle.g:1273:7: ( ( IF or_test[null] ORELSE )=> IF o2= or_test[ctype] ORELSE e= test[expr_contextType.Load] | -> or_test )
					int alt81=2;
					int LA81_0 = input.LA(1);
					if ( (LA81_0==IF) ) {
						int LA81_1 = input.LA(2);
						if ( (synpred7_Truffle()) ) {
							alt81=1;
						}
						else if ( (true) ) {
							alt81=2;
						}

					}
					else if ( (LA81_0==EOF||LA81_0==AMPEREQUAL||LA81_0==AS||LA81_0==ASSIGN||LA81_0==BACKQUOTE||LA81_0==CIRCUMFLEXEQUAL||(LA81_0 >= COLON && LA81_0 <= COMMA)||LA81_0==DOUBLESLASHEQUAL||LA81_0==DOUBLESTAREQUAL||LA81_0==FOR||LA81_0==LEFTSHIFTEQUAL||LA81_0==MINUSEQUAL||LA81_0==NEWLINE||LA81_0==PERCENTEQUAL||LA81_0==PLUSEQUAL||(LA81_0 >= RBRACK && LA81_0 <= RCURLY)||(LA81_0 >= RIGHTSHIFTEQUAL && LA81_0 <= SEMI)||LA81_0==SLASHEQUAL||LA81_0==STAREQUAL||LA81_0==VBAREQUAL) ) {
						alt81=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 81, 0, input);
						throw nvae;
					}

					switch (alt81) {
						case 1 :
							// Truffle.g:1273:9: ( IF or_test[null] ORELSE )=> IF o2= or_test[ctype] ORELSE e= test[expr_contextType.Load]
							{
							IF186=(Token)match(input,IF,FOLLOW_IF_in_test4379); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_IF.add(IF186);

							pushFollow(FOLLOW_or_test_in_test4383);
							o2=or_test(ctype);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_or_test.add(o2.getTree());
							ORELSE187=(Token)match(input,ORELSE,FOLLOW_ORELSE_in_test4386); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ORELSE.add(ORELSE187);

							pushFollow(FOLLOW_test_in_test4390);
							e=test(expr_contextType.Load);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_test.add(e.getTree());
							if ( state.backtracking==0 ) {
							             etype = actions.makeIfExp((o1!=null?(o1.start):null), actions.castExpr((o2!=null?((PNode)o2.getTree()):null)), actions.castExpr((o1!=null?((PNode)o1.getTree()):null)), actions.castExpr((e!=null?((PNode)e.getTree()):null)));
							         }
							}
							break;
						case 2 :
							// Truffle.g:1278:6: 
							{
							// AST REWRITE
							// elements: or_test
							// token labels: 
							// rule labels: retval
							// token list labels: 
							// rule list labels: 
							// wildcard labels: 
							if ( state.backtracking==0 ) {
							retval.tree = root_0;
							RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

							root_0 = (PNode)adaptor.nil();
							// 1278:6: -> or_test
							{
								adaptor.addChild(root_0, stream_or_test.nextTree());
							}


							retval.tree = root_0;
							}

							}
							break;

					}

					}
					break;
				case 2 :
					// Truffle.g:1280:7: lambdef
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_lambdef_in_test4435);
					lambdef188=lambdef();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, lambdef188.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   if (etype != null) {
			       retval.tree = etype;
			   }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "test"


	public static class or_test_return extends ParserRuleReturnScope {
		public Token leftTok;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "or_test"
	// Truffle.g:1284:1: or_test[expr_contextType ctype] returns [Token leftTok] : left= and_test[ctype] ( (or= OR right+= and_test[ctype] )+ | -> $left) ;
	public final TruffleParser.or_test_return or_test(expr_contextType ctype) throws RecognitionException {
		TruffleParser.or_test_return retval = new TruffleParser.or_test_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token or=null;
		List<Object> list_right=null;
		ParserRuleReturnScope left =null;
		RuleReturnScope right = null;
		PNode or_tree=null;
		RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");
		RewriteRuleSubtreeStream stream_and_test=new RewriteRuleSubtreeStream(adaptor,"rule and_test");

		try {
			// Truffle.g:1295:5: (left= and_test[ctype] ( (or= OR right+= and_test[ctype] )+ | -> $left) )
			// Truffle.g:1295:7: left= and_test[ctype] ( (or= OR right+= and_test[ctype] )+ | -> $left)
			{
			pushFollow(FOLLOW_and_test_in_or_test4470);
			left=and_test(ctype);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_and_test.add(left.getTree());
			// Truffle.g:1296:9: ( (or= OR right+= and_test[ctype] )+ | -> $left)
			int alt84=2;
			int LA84_0 = input.LA(1);
			if ( (LA84_0==OR) ) {
				alt84=1;
			}
			else if ( (LA84_0==EOF||LA84_0==AMPEREQUAL||LA84_0==AS||LA84_0==ASSIGN||LA84_0==BACKQUOTE||LA84_0==CIRCUMFLEXEQUAL||(LA84_0 >= COLON && LA84_0 <= COMMA)||LA84_0==DOUBLESLASHEQUAL||LA84_0==DOUBLESTAREQUAL||LA84_0==FOR||LA84_0==IF||LA84_0==LEFTSHIFTEQUAL||LA84_0==MINUSEQUAL||LA84_0==NEWLINE||LA84_0==ORELSE||LA84_0==PERCENTEQUAL||LA84_0==PLUSEQUAL||(LA84_0 >= RBRACK && LA84_0 <= RCURLY)||(LA84_0 >= RIGHTSHIFTEQUAL && LA84_0 <= SEMI)||LA84_0==SLASHEQUAL||LA84_0==STAREQUAL||LA84_0==VBAREQUAL) ) {
				alt84=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 84, 0, input);
				throw nvae;
			}

			switch (alt84) {
				case 1 :
					// Truffle.g:1296:11: (or= OR right+= and_test[ctype] )+
					{
					// Truffle.g:1296:11: (or= OR right+= and_test[ctype] )+
					int cnt83=0;
					loop83:
					while (true) {
						int alt83=2;
						int LA83_0 = input.LA(1);
						if ( (LA83_0==OR) ) {
							alt83=1;
						}

						switch (alt83) {
						case 1 :
							// Truffle.g:1296:12: or= OR right+= and_test[ctype]
							{
							or=(Token)match(input,OR,FOLLOW_OR_in_or_test4486); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_OR.add(or);

							pushFollow(FOLLOW_and_test_in_or_test4490);
							right=and_test(ctype);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_and_test.add(right.getTree());
							if (list_right==null) list_right=new ArrayList<Object>();
							list_right.add(right.getTree());
							}
							break;

						default :
							if ( cnt83 >= 1 ) break loop83;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(83, input);
							throw eee;
						}
						cnt83++;
					}

					}
					break;
				case 2 :
					// Truffle.g:1299:8: 
					{
					// AST REWRITE
					// elements: left
					// token labels: 
					// rule labels: retval, left
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1299:8: -> $left
					{
						adaptor.addChild(root_0, stream_left.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (or != null) {
			        Token tok = (left!=null?(left.start):null);
			        if ((left!=null?((TruffleParser.and_test_return)left).leftTok:null) != null) {
			            tok = (left!=null?((TruffleParser.and_test_return)left).leftTok:null);
			        }
			        retval.tree = actions.makeBoolOp(tok, (left!=null?((PNode)left.getTree()):null), boolopType.Or, list_right);
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "or_test"


	public static class and_test_return extends ParserRuleReturnScope {
		public Token leftTok;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "and_test"
	// Truffle.g:1304:1: and_test[expr_contextType ctype] returns [Token leftTok] : left= not_test[ctype] ( (and= AND right+= not_test[ctype] )+ | -> $left) ;
	public final TruffleParser.and_test_return and_test(expr_contextType ctype) throws RecognitionException {
		TruffleParser.and_test_return retval = new TruffleParser.and_test_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token and=null;
		List<Object> list_right=null;
		ParserRuleReturnScope left =null;
		RuleReturnScope right = null;
		PNode and_tree=null;
		RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
		RewriteRuleSubtreeStream stream_not_test=new RewriteRuleSubtreeStream(adaptor,"rule not_test");

		try {
			// Truffle.g:1315:5: (left= not_test[ctype] ( (and= AND right+= not_test[ctype] )+ | -> $left) )
			// Truffle.g:1315:7: left= not_test[ctype] ( (and= AND right+= not_test[ctype] )+ | -> $left)
			{
			pushFollow(FOLLOW_not_test_in_and_test4571);
			left=not_test(ctype);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_not_test.add(left.getTree());
			// Truffle.g:1316:9: ( (and= AND right+= not_test[ctype] )+ | -> $left)
			int alt86=2;
			int LA86_0 = input.LA(1);
			if ( (LA86_0==AND) ) {
				alt86=1;
			}
			else if ( (LA86_0==EOF||LA86_0==AMPEREQUAL||LA86_0==AS||LA86_0==ASSIGN||LA86_0==BACKQUOTE||LA86_0==CIRCUMFLEXEQUAL||(LA86_0 >= COLON && LA86_0 <= COMMA)||LA86_0==DOUBLESLASHEQUAL||LA86_0==DOUBLESTAREQUAL||LA86_0==FOR||LA86_0==IF||LA86_0==LEFTSHIFTEQUAL||LA86_0==MINUSEQUAL||LA86_0==NEWLINE||(LA86_0 >= OR && LA86_0 <= ORELSE)||LA86_0==PERCENTEQUAL||LA86_0==PLUSEQUAL||(LA86_0 >= RBRACK && LA86_0 <= RCURLY)||(LA86_0 >= RIGHTSHIFTEQUAL && LA86_0 <= SEMI)||LA86_0==SLASHEQUAL||LA86_0==STAREQUAL||LA86_0==VBAREQUAL) ) {
				alt86=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 86, 0, input);
				throw nvae;
			}

			switch (alt86) {
				case 1 :
					// Truffle.g:1316:11: (and= AND right+= not_test[ctype] )+
					{
					// Truffle.g:1316:11: (and= AND right+= not_test[ctype] )+
					int cnt85=0;
					loop85:
					while (true) {
						int alt85=2;
						int LA85_0 = input.LA(1);
						if ( (LA85_0==AND) ) {
							alt85=1;
						}

						switch (alt85) {
						case 1 :
							// Truffle.g:1316:12: and= AND right+= not_test[ctype]
							{
							and=(Token)match(input,AND,FOLLOW_AND_in_and_test4587); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_AND.add(and);

							pushFollow(FOLLOW_not_test_in_and_test4591);
							right=not_test(ctype);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_not_test.add(right.getTree());
							if (list_right==null) list_right=new ArrayList<Object>();
							list_right.add(right.getTree());
							}
							break;

						default :
							if ( cnt85 >= 1 ) break loop85;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(85, input);
							throw eee;
						}
						cnt85++;
					}

					}
					break;
				case 2 :
					// Truffle.g:1319:8: 
					{
					// AST REWRITE
					// elements: left
					// token labels: 
					// rule labels: retval, left
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1319:8: -> $left
					{
						adaptor.addChild(root_0, stream_left.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (and != null) {
			        Token tok = (left!=null?(left.start):null);
			        if ((left!=null?((TruffleParser.not_test_return)left).leftTok:null) != null) {
			            tok = (left!=null?((TruffleParser.not_test_return)left).leftTok:null);
			        }
			        retval.tree = actions.makeBoolOp(tok, (left!=null?((PNode)left.getTree()):null), boolopType.And, list_right);
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "and_test"


	public static class not_test_return extends ParserRuleReturnScope {
		public Token leftTok;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "not_test"
	// Truffle.g:1324:1: not_test[expr_contextType ctype] returns [Token leftTok] : ( NOT nt= not_test[ctype] | comparison[ctype] );
	public final TruffleParser.not_test_return not_test(expr_contextType ctype) throws RecognitionException {
		TruffleParser.not_test_return retval = new TruffleParser.not_test_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token NOT189=null;
		ParserRuleReturnScope nt =null;
		ParserRuleReturnScope comparison190 =null;

		PNode NOT189_tree=null;


		    PNode etype = null;

		try {
			// Truffle.g:1334:5: ( NOT nt= not_test[ctype] | comparison[ctype] )
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==NOT) ) {
				alt87=1;
			}
			else if ( (LA87_0==BACKQUOTE||(LA87_0 >= LBRACK && LA87_0 <= LCURLY)||(LA87_0 >= LPAREN && LA87_0 <= MINUS)||LA87_0==NAME||LA87_0==PLUS||LA87_0==TILDE) ) {
				alt87=2;
			}
			else if ( (LA87_0==PRINT) && ((printFunction))) {
				alt87=2;
			}
			else if ( (LA87_0==COMPLEX||LA87_0==FALSE||LA87_0==FLOAT||LA87_0==INT||LA87_0==NONE||LA87_0==STRING||LA87_0==TRUE) ) {
				alt87=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 87, 0, input);
				throw nvae;
			}

			switch (alt87) {
				case 1 :
					// Truffle.g:1334:7: NOT nt= not_test[ctype]
					{
					root_0 = (PNode)adaptor.nil();


					NOT189=(Token)match(input,NOT,FOLLOW_NOT_in_not_test4675); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT189_tree = (PNode)adaptor.create(NOT189);
					adaptor.addChild(root_0, NOT189_tree);
					}

					pushFollow(FOLLOW_not_test_in_not_test4679);
					nt=not_test(ctype);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, nt.getTree());

					if ( state.backtracking==0 ) {
					          etype = actions.makeUnaryOp(NOT189, unaryopType.Not, actions.castExpr((nt!=null?((PNode)nt.getTree()):null)));
					      }
					}
					break;
				case 2 :
					// Truffle.g:1338:7: comparison[ctype]
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_comparison_in_not_test4696);
					comparison190=comparison(ctype);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison190.getTree());

					if ( state.backtracking==0 ) {
					          retval.leftTok = (comparison190!=null?((TruffleParser.comparison_return)comparison190).leftTok:null);
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   if (etype != null) {
			       retval.tree = etype;
			   }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "not_test"


	public static class comparison_return extends ParserRuleReturnScope {
		public Token leftTok;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "comparison"
	// Truffle.g:1345:1: comparison[expr_contextType ctype] returns [Token leftTok] : left= expr[ctype] ( ( comp_op right+= expr[ctype] )+ | -> $left) ;
	public final TruffleParser.comparison_return comparison(expr_contextType ctype) throws RecognitionException {
		TruffleParser.comparison_return retval = new TruffleParser.comparison_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		List<Object> list_right=null;
		ParserRuleReturnScope left =null;
		ParserRuleReturnScope comp_op191 =null;
		RuleReturnScope right = null;
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
		RewriteRuleSubtreeStream stream_comp_op=new RewriteRuleSubtreeStream(adaptor,"rule comp_op");


		    List cmps = new ArrayList();

		try {
			// Truffle.g:1357:5: (left= expr[ctype] ( ( comp_op right+= expr[ctype] )+ | -> $left) )
			// Truffle.g:1357:7: left= expr[ctype] ( ( comp_op right+= expr[ctype] )+ | -> $left)
			{
			pushFollow(FOLLOW_expr_in_comparison4745);
			left=expr(ctype);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_expr.add(left.getTree());
			// Truffle.g:1358:8: ( ( comp_op right+= expr[ctype] )+ | -> $left)
			int alt89=2;
			int LA89_0 = input.LA(1);
			if ( (LA89_0==EQUAL||(LA89_0 >= GREATER && LA89_0 <= GREATEREQUAL)||LA89_0==IN||LA89_0==IS||(LA89_0 >= LESS && LA89_0 <= LESSEQUAL)||(LA89_0 >= NOT && LA89_0 <= NOTEQUAL)) ) {
				alt89=1;
			}
			else if ( (LA89_0==EOF||(LA89_0 >= AMPEREQUAL && LA89_0 <= AS)||LA89_0==ASSIGN||LA89_0==BACKQUOTE||LA89_0==CIRCUMFLEXEQUAL||(LA89_0 >= COLON && LA89_0 <= COMMA)||LA89_0==DOUBLESLASHEQUAL||LA89_0==DOUBLESTAREQUAL||LA89_0==FOR||LA89_0==IF||LA89_0==LEFTSHIFTEQUAL||LA89_0==MINUSEQUAL||LA89_0==NEWLINE||(LA89_0 >= OR && LA89_0 <= ORELSE)||LA89_0==PERCENTEQUAL||LA89_0==PLUSEQUAL||(LA89_0 >= RBRACK && LA89_0 <= RCURLY)||(LA89_0 >= RIGHTSHIFTEQUAL && LA89_0 <= SEMI)||LA89_0==SLASHEQUAL||LA89_0==STAREQUAL||LA89_0==VBAREQUAL) ) {
				alt89=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 89, 0, input);
				throw nvae;
			}

			switch (alt89) {
				case 1 :
					// Truffle.g:1358:10: ( comp_op right+= expr[ctype] )+
					{
					// Truffle.g:1358:10: ( comp_op right+= expr[ctype] )+
					int cnt88=0;
					loop88:
					while (true) {
						int alt88=2;
						int LA88_0 = input.LA(1);
						if ( (LA88_0==EQUAL||(LA88_0 >= GREATER && LA88_0 <= GREATEREQUAL)||LA88_0==IN||LA88_0==IS||(LA88_0 >= LESS && LA88_0 <= LESSEQUAL)||(LA88_0 >= NOT && LA88_0 <= NOTEQUAL)) ) {
							alt88=1;
						}

						switch (alt88) {
						case 1 :
							// Truffle.g:1358:12: comp_op right+= expr[ctype]
							{
							pushFollow(FOLLOW_comp_op_in_comparison4759);
							comp_op191=comp_op();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_comp_op.add(comp_op191.getTree());
							pushFollow(FOLLOW_expr_in_comparison4763);
							right=expr(ctype);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_expr.add(right.getTree());
							if (list_right==null) list_right=new ArrayList<Object>();
							list_right.add(right.getTree());
							if ( state.backtracking==0 ) {
							               cmps.add((comp_op191!=null?((TruffleParser.comp_op_return)comp_op191).op:null));
							           }
							}
							break;

						default :
							if ( cnt88 >= 1 ) break loop88;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(88, input);
							throw eee;
						}
						cnt88++;
					}

					}
					break;
				case 2 :
					// Truffle.g:1364:7: 
					{
					// AST REWRITE
					// elements: left
					// token labels: 
					// rule labels: retval, left
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1364:7: -> $left
					{
						adaptor.addChild(root_0, stream_left.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.leftTok = (left!=null?((TruffleParser.expr_return)left).leftTok:null);
			    if (!cmps.isEmpty()) {
			        retval.tree = actions.makeCompare((left!=null?(left.start):null), actions.castExpr((left!=null?((PNode)left.getTree()):null)), actions.makeCmpOps(cmps),
			            actions.castExprs(list_right));
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "comparison"


	public static class comp_op_return extends ParserRuleReturnScope {
		public cmpopType op;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "comp_op"
	// Truffle.g:1369:1: comp_op returns [cmpopType op] : ( LESS | GREATER | EQUAL | GREATEREQUAL | LESSEQUAL | NOTEQUAL | IN | NOT IN | IS | IS NOT );
	public final TruffleParser.comp_op_return comp_op() throws RecognitionException {
		TruffleParser.comp_op_return retval = new TruffleParser.comp_op_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token LESS192=null;
		Token GREATER193=null;
		Token EQUAL194=null;
		Token GREATEREQUAL195=null;
		Token LESSEQUAL196=null;
		Token NOTEQUAL197=null;
		Token IN198=null;
		Token NOT199=null;
		Token IN200=null;
		Token IS201=null;
		Token IS202=null;
		Token NOT203=null;

		PNode LESS192_tree=null;
		PNode GREATER193_tree=null;
		PNode EQUAL194_tree=null;
		PNode GREATEREQUAL195_tree=null;
		PNode LESSEQUAL196_tree=null;
		PNode NOTEQUAL197_tree=null;
		PNode IN198_tree=null;
		PNode NOT199_tree=null;
		PNode IN200_tree=null;
		PNode IS201_tree=null;
		PNode IS202_tree=null;
		PNode NOT203_tree=null;

		try {
			// Truffle.g:1371:5: ( LESS | GREATER | EQUAL | GREATEREQUAL | LESSEQUAL | NOTEQUAL | IN | NOT IN | IS | IS NOT )
			int alt90=10;
			switch ( input.LA(1) ) {
			case LESS:
				{
				alt90=1;
				}
				break;
			case GREATER:
				{
				alt90=2;
				}
				break;
			case EQUAL:
				{
				alt90=3;
				}
				break;
			case GREATEREQUAL:
				{
				alt90=4;
				}
				break;
			case LESSEQUAL:
				{
				alt90=5;
				}
				break;
			case NOTEQUAL:
				{
				alt90=6;
				}
				break;
			case IN:
				{
				alt90=7;
				}
				break;
			case NOT:
				{
				alt90=8;
				}
				break;
			case IS:
				{
				int LA90_9 = input.LA(2);
				if ( (LA90_9==NOT) ) {
					alt90=10;
				}
				else if ( (LA90_9==BACKQUOTE||LA90_9==COMPLEX||LA90_9==FALSE||LA90_9==FLOAT||LA90_9==INT||(LA90_9 >= LBRACK && LA90_9 <= LCURLY)||(LA90_9 >= LPAREN && LA90_9 <= MINUS)||LA90_9==NAME||LA90_9==NONE||LA90_9==PLUS||LA90_9==PRINT||(LA90_9 >= STRING && LA90_9 <= TILDE)||LA90_9==TRUE) ) {
					alt90=9;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 90, 9, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 90, 0, input);
				throw nvae;
			}
			switch (alt90) {
				case 1 :
					// Truffle.g:1371:7: LESS
					{
					root_0 = (PNode)adaptor.nil();


					LESS192=(Token)match(input,LESS,FOLLOW_LESS_in_comp_op4844); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LESS192_tree = (PNode)adaptor.create(LESS192);
					adaptor.addChild(root_0, LESS192_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = cmpopType.Lt;
					      }
					}
					break;
				case 2 :
					// Truffle.g:1375:7: GREATER
					{
					root_0 = (PNode)adaptor.nil();


					GREATER193=(Token)match(input,GREATER,FOLLOW_GREATER_in_comp_op4860); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					GREATER193_tree = (PNode)adaptor.create(GREATER193);
					adaptor.addChild(root_0, GREATER193_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = cmpopType.Gt;
					      }
					}
					break;
				case 3 :
					// Truffle.g:1379:7: EQUAL
					{
					root_0 = (PNode)adaptor.nil();


					EQUAL194=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_comp_op4876); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					EQUAL194_tree = (PNode)adaptor.create(EQUAL194);
					adaptor.addChild(root_0, EQUAL194_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = cmpopType.Eq;
					      }
					}
					break;
				case 4 :
					// Truffle.g:1383:7: GREATEREQUAL
					{
					root_0 = (PNode)adaptor.nil();


					GREATEREQUAL195=(Token)match(input,GREATEREQUAL,FOLLOW_GREATEREQUAL_in_comp_op4892); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					GREATEREQUAL195_tree = (PNode)adaptor.create(GREATEREQUAL195);
					adaptor.addChild(root_0, GREATEREQUAL195_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = cmpopType.GtE;
					      }
					}
					break;
				case 5 :
					// Truffle.g:1387:7: LESSEQUAL
					{
					root_0 = (PNode)adaptor.nil();


					LESSEQUAL196=(Token)match(input,LESSEQUAL,FOLLOW_LESSEQUAL_in_comp_op4908); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LESSEQUAL196_tree = (PNode)adaptor.create(LESSEQUAL196);
					adaptor.addChild(root_0, LESSEQUAL196_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = cmpopType.LtE;
					      }
					}
					break;
				case 6 :
					// Truffle.g:1395:7: NOTEQUAL
					{
					root_0 = (PNode)adaptor.nil();


					NOTEQUAL197=(Token)match(input,NOTEQUAL,FOLLOW_NOTEQUAL_in_comp_op4944); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOTEQUAL197_tree = (PNode)adaptor.create(NOTEQUAL197);
					adaptor.addChild(root_0, NOTEQUAL197_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = cmpopType.NotEq;
					      }
					}
					break;
				case 7 :
					// Truffle.g:1399:7: IN
					{
					root_0 = (PNode)adaptor.nil();


					IN198=(Token)match(input,IN,FOLLOW_IN_in_comp_op4960); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					IN198_tree = (PNode)adaptor.create(IN198);
					adaptor.addChild(root_0, IN198_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = cmpopType.In;
					      }
					}
					break;
				case 8 :
					// Truffle.g:1403:7: NOT IN
					{
					root_0 = (PNode)adaptor.nil();


					NOT199=(Token)match(input,NOT,FOLLOW_NOT_in_comp_op4976); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT199_tree = (PNode)adaptor.create(NOT199);
					adaptor.addChild(root_0, NOT199_tree);
					}

					IN200=(Token)match(input,IN,FOLLOW_IN_in_comp_op4978); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					IN200_tree = (PNode)adaptor.create(IN200);
					adaptor.addChild(root_0, IN200_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = cmpopType.NotIn;
					      }
					}
					break;
				case 9 :
					// Truffle.g:1407:7: IS
					{
					root_0 = (PNode)adaptor.nil();


					IS201=(Token)match(input,IS,FOLLOW_IS_in_comp_op4994); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					IS201_tree = (PNode)adaptor.create(IS201);
					adaptor.addChild(root_0, IS201_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = cmpopType.Is;
					      }
					}
					break;
				case 10 :
					// Truffle.g:1411:7: IS NOT
					{
					root_0 = (PNode)adaptor.nil();


					IS202=(Token)match(input,IS,FOLLOW_IS_in_comp_op5010); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					IS202_tree = (PNode)adaptor.create(IS202);
					adaptor.addChild(root_0, IS202_tree);
					}

					NOT203=(Token)match(input,NOT,FOLLOW_NOT_in_comp_op5012); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NOT203_tree = (PNode)adaptor.create(NOT203);
					adaptor.addChild(root_0, NOT203_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = cmpopType.IsNot;
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "comp_op"


	protected static class expr_scope {
		expr_contextType ctype;
	}
	protected Stack<expr_scope> expr_stack = new Stack<expr_scope>();

	public static class expr_return extends ParserRuleReturnScope {
		public Token leftTok;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "expr"
	// Truffle.g:1418:1: expr[expr_contextType ect] returns [Token leftTok] : left= xor_expr ( (op= VBAR right+= xor_expr )+ | -> $left) ;
	public final TruffleParser.expr_return expr(expr_contextType ect) throws RecognitionException {
		expr_stack.push(new expr_scope());
		TruffleParser.expr_return retval = new TruffleParser.expr_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token op=null;
		List<Object> list_right=null;
		ParserRuleReturnScope left =null;
		RuleReturnScope right = null;
		PNode op_tree=null;
		RewriteRuleTokenStream stream_VBAR=new RewriteRuleTokenStream(adaptor,"token VBAR");
		RewriteRuleSubtreeStream stream_xor_expr=new RewriteRuleSubtreeStream(adaptor,"rule xor_expr");


		    expr_stack.peek().ctype = ect;

		try {
			// Truffle.g:1436:5: (left= xor_expr ( (op= VBAR right+= xor_expr )+ | -> $left) )
			// Truffle.g:1436:7: left= xor_expr ( (op= VBAR right+= xor_expr )+ | -> $left)
			{
			pushFollow(FOLLOW_xor_expr_in_expr5064);
			left=xor_expr();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_xor_expr.add(left.getTree());
			// Truffle.g:1437:9: ( (op= VBAR right+= xor_expr )+ | -> $left)
			int alt92=2;
			int LA92_0 = input.LA(1);
			if ( (LA92_0==VBAR) ) {
				alt92=1;
			}
			else if ( (LA92_0==EOF||(LA92_0 >= AMPEREQUAL && LA92_0 <= AS)||LA92_0==ASSIGN||LA92_0==BACKQUOTE||LA92_0==CIRCUMFLEXEQUAL||(LA92_0 >= COLON && LA92_0 <= COMMA)||LA92_0==DOUBLESLASHEQUAL||LA92_0==DOUBLESTAREQUAL||LA92_0==EQUAL||LA92_0==FOR||(LA92_0 >= GREATER && LA92_0 <= IF)||LA92_0==IN||LA92_0==IS||(LA92_0 >= LEFTSHIFTEQUAL && LA92_0 <= LESSEQUAL)||LA92_0==MINUSEQUAL||LA92_0==NEWLINE||(LA92_0 >= NOT && LA92_0 <= ORELSE)||LA92_0==PERCENTEQUAL||LA92_0==PLUSEQUAL||(LA92_0 >= RBRACK && LA92_0 <= RCURLY)||(LA92_0 >= RIGHTSHIFTEQUAL && LA92_0 <= SEMI)||LA92_0==SLASHEQUAL||LA92_0==STAREQUAL||LA92_0==VBAREQUAL) ) {
				alt92=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 92, 0, input);
				throw nvae;
			}

			switch (alt92) {
				case 1 :
					// Truffle.g:1437:11: (op= VBAR right+= xor_expr )+
					{
					// Truffle.g:1437:11: (op= VBAR right+= xor_expr )+
					int cnt91=0;
					loop91:
					while (true) {
						int alt91=2;
						int LA91_0 = input.LA(1);
						if ( (LA91_0==VBAR) ) {
							alt91=1;
						}

						switch (alt91) {
						case 1 :
							// Truffle.g:1437:12: op= VBAR right+= xor_expr
							{
							op=(Token)match(input,VBAR,FOLLOW_VBAR_in_expr5079); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_VBAR.add(op);

							pushFollow(FOLLOW_xor_expr_in_expr5083);
							right=xor_expr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_xor_expr.add(right.getTree());
							if (list_right==null) list_right=new ArrayList<Object>();
							list_right.add(right.getTree());
							}
							break;

						default :
							if ( cnt91 >= 1 ) break loop91;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(91, input);
							throw eee;
						}
						cnt91++;
					}

					}
					break;
				case 2 :
					// Truffle.g:1440:8: 
					{
					// AST REWRITE
					// elements: left
					// token labels: 
					// rule labels: retval, left
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1440:8: -> $left
					{
						adaptor.addChild(root_0, stream_left.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.leftTok = (left!=null?((TruffleParser.xor_expr_return)left).lparen:null);
			    if (op != null) {
			        Token tok = (left!=null?(left.start):null);
			        if ((left!=null?((TruffleParser.xor_expr_return)left).lparen:null) != null) {
			            tok = (left!=null?((TruffleParser.xor_expr_return)left).lparen:null);
			        }
			        retval.tree = actions.makeBinOp(tok, (left!=null?((PNode)left.getTree()):null), operatorType.BitOr, list_right);
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
			expr_stack.pop();
		}
		return retval;
	}
	// $ANTLR end "expr"


	public static class xor_expr_return extends ParserRuleReturnScope {
		public Token lparen = null;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "xor_expr"
	// Truffle.g:1446:1: xor_expr returns [Token lparen = null] : left= and_expr ( (op= CIRCUMFLEX right+= and_expr )+ | -> $left) ;
	public final TruffleParser.xor_expr_return xor_expr() throws RecognitionException {
		TruffleParser.xor_expr_return retval = new TruffleParser.xor_expr_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token op=null;
		List<Object> list_right=null;
		ParserRuleReturnScope left =null;
		RuleReturnScope right = null;
		PNode op_tree=null;
		RewriteRuleTokenStream stream_CIRCUMFLEX=new RewriteRuleTokenStream(adaptor,"token CIRCUMFLEX");
		RewriteRuleSubtreeStream stream_and_expr=new RewriteRuleSubtreeStream(adaptor,"rule and_expr");

		try {
			// Truffle.g:1458:5: (left= and_expr ( (op= CIRCUMFLEX right+= and_expr )+ | -> $left) )
			// Truffle.g:1458:7: left= and_expr ( (op= CIRCUMFLEX right+= and_expr )+ | -> $left)
			{
			pushFollow(FOLLOW_and_expr_in_xor_expr5162);
			left=and_expr();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_and_expr.add(left.getTree());
			// Truffle.g:1459:9: ( (op= CIRCUMFLEX right+= and_expr )+ | -> $left)
			int alt94=2;
			int LA94_0 = input.LA(1);
			if ( (LA94_0==CIRCUMFLEX) ) {
				alt94=1;
			}
			else if ( (LA94_0==EOF||(LA94_0 >= AMPEREQUAL && LA94_0 <= AS)||LA94_0==ASSIGN||LA94_0==BACKQUOTE||LA94_0==CIRCUMFLEXEQUAL||(LA94_0 >= COLON && LA94_0 <= COMMA)||LA94_0==DOUBLESLASHEQUAL||LA94_0==DOUBLESTAREQUAL||LA94_0==EQUAL||LA94_0==FOR||(LA94_0 >= GREATER && LA94_0 <= IF)||LA94_0==IN||LA94_0==IS||(LA94_0 >= LEFTSHIFTEQUAL && LA94_0 <= LESSEQUAL)||LA94_0==MINUSEQUAL||LA94_0==NEWLINE||(LA94_0 >= NOT && LA94_0 <= ORELSE)||LA94_0==PERCENTEQUAL||LA94_0==PLUSEQUAL||(LA94_0 >= RBRACK && LA94_0 <= RCURLY)||(LA94_0 >= RIGHTSHIFTEQUAL && LA94_0 <= SEMI)||LA94_0==SLASHEQUAL||LA94_0==STAREQUAL||(LA94_0 >= VBAR && LA94_0 <= VBAREQUAL)) ) {
				alt94=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 94, 0, input);
				throw nvae;
			}

			switch (alt94) {
				case 1 :
					// Truffle.g:1459:11: (op= CIRCUMFLEX right+= and_expr )+
					{
					// Truffle.g:1459:11: (op= CIRCUMFLEX right+= and_expr )+
					int cnt93=0;
					loop93:
					while (true) {
						int alt93=2;
						int LA93_0 = input.LA(1);
						if ( (LA93_0==CIRCUMFLEX) ) {
							alt93=1;
						}

						switch (alt93) {
						case 1 :
							// Truffle.g:1459:12: op= CIRCUMFLEX right+= and_expr
							{
							op=(Token)match(input,CIRCUMFLEX,FOLLOW_CIRCUMFLEX_in_xor_expr5177); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_CIRCUMFLEX.add(op);

							pushFollow(FOLLOW_and_expr_in_xor_expr5181);
							right=and_expr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_and_expr.add(right.getTree());
							if (list_right==null) list_right=new ArrayList<Object>();
							list_right.add(right.getTree());
							}
							break;

						default :
							if ( cnt93 >= 1 ) break loop93;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(93, input);
							throw eee;
						}
						cnt93++;
					}

					}
					break;
				case 2 :
					// Truffle.g:1462:8: 
					{
					// AST REWRITE
					// elements: left
					// token labels: 
					// rule labels: retval, left
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1462:8: -> $left
					{
						adaptor.addChild(root_0, stream_left.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (op != null) {
			        Token tok = (left!=null?(left.start):null);
			        if ((left!=null?((TruffleParser.and_expr_return)left).lparen:null) != null) {
			            tok = (left!=null?((TruffleParser.and_expr_return)left).lparen:null);
			        }
			        retval.tree = actions.makeBinOp(tok, (left!=null?((PNode)left.getTree()):null), operatorType.BitXor, list_right);
			    }
			    retval.lparen = (left!=null?((TruffleParser.and_expr_return)left).lparen:null);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "xor_expr"


	public static class and_expr_return extends ParserRuleReturnScope {
		public Token lparen = null;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "and_expr"
	// Truffle.g:1467:1: and_expr returns [Token lparen = null] : left= shift_expr ( (op= AMPER right+= shift_expr )+ | -> $left) ;
	public final TruffleParser.and_expr_return and_expr() throws RecognitionException {
		TruffleParser.and_expr_return retval = new TruffleParser.and_expr_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token op=null;
		List<Object> list_right=null;
		ParserRuleReturnScope left =null;
		RuleReturnScope right = null;
		PNode op_tree=null;
		RewriteRuleTokenStream stream_AMPER=new RewriteRuleTokenStream(adaptor,"token AMPER");
		RewriteRuleSubtreeStream stream_shift_expr=new RewriteRuleSubtreeStream(adaptor,"rule shift_expr");

		try {
			// Truffle.g:1479:5: (left= shift_expr ( (op= AMPER right+= shift_expr )+ | -> $left) )
			// Truffle.g:1479:7: left= shift_expr ( (op= AMPER right+= shift_expr )+ | -> $left)
			{
			pushFollow(FOLLOW_shift_expr_in_and_expr5259);
			left=shift_expr();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_shift_expr.add(left.getTree());
			// Truffle.g:1480:9: ( (op= AMPER right+= shift_expr )+ | -> $left)
			int alt96=2;
			int LA96_0 = input.LA(1);
			if ( (LA96_0==AMPER) ) {
				alt96=1;
			}
			else if ( (LA96_0==EOF||(LA96_0 >= AMPEREQUAL && LA96_0 <= AS)||LA96_0==ASSIGN||LA96_0==BACKQUOTE||(LA96_0 >= CIRCUMFLEX && LA96_0 <= CIRCUMFLEXEQUAL)||(LA96_0 >= COLON && LA96_0 <= COMMA)||LA96_0==DOUBLESLASHEQUAL||LA96_0==DOUBLESTAREQUAL||LA96_0==EQUAL||LA96_0==FOR||(LA96_0 >= GREATER && LA96_0 <= IF)||LA96_0==IN||LA96_0==IS||(LA96_0 >= LEFTSHIFTEQUAL && LA96_0 <= LESSEQUAL)||LA96_0==MINUSEQUAL||LA96_0==NEWLINE||(LA96_0 >= NOT && LA96_0 <= ORELSE)||LA96_0==PERCENTEQUAL||LA96_0==PLUSEQUAL||(LA96_0 >= RBRACK && LA96_0 <= RCURLY)||(LA96_0 >= RIGHTSHIFTEQUAL && LA96_0 <= SEMI)||LA96_0==SLASHEQUAL||LA96_0==STAREQUAL||(LA96_0 >= VBAR && LA96_0 <= VBAREQUAL)) ) {
				alt96=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 96, 0, input);
				throw nvae;
			}

			switch (alt96) {
				case 1 :
					// Truffle.g:1480:11: (op= AMPER right+= shift_expr )+
					{
					// Truffle.g:1480:11: (op= AMPER right+= shift_expr )+
					int cnt95=0;
					loop95:
					while (true) {
						int alt95=2;
						int LA95_0 = input.LA(1);
						if ( (LA95_0==AMPER) ) {
							alt95=1;
						}

						switch (alt95) {
						case 1 :
							// Truffle.g:1480:12: op= AMPER right+= shift_expr
							{
							op=(Token)match(input,AMPER,FOLLOW_AMPER_in_and_expr5274); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_AMPER.add(op);

							pushFollow(FOLLOW_shift_expr_in_and_expr5278);
							right=shift_expr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_shift_expr.add(right.getTree());
							if (list_right==null) list_right=new ArrayList<Object>();
							list_right.add(right.getTree());
							}
							break;

						default :
							if ( cnt95 >= 1 ) break loop95;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(95, input);
							throw eee;
						}
						cnt95++;
					}

					}
					break;
				case 2 :
					// Truffle.g:1483:8: 
					{
					// AST REWRITE
					// elements: left
					// token labels: 
					// rule labels: retval, left
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1483:8: -> $left
					{
						adaptor.addChild(root_0, stream_left.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (op != null) {
			        Token tok = (left!=null?(left.start):null);
			        if ((left!=null?((TruffleParser.shift_expr_return)left).lparen:null) != null) {
			            tok = (left!=null?((TruffleParser.shift_expr_return)left).lparen:null);
			        }
			        retval.tree = actions.makeBinOp(tok, (left!=null?((PNode)left.getTree()):null), operatorType.BitAnd, list_right);
			    }
			    retval.lparen = (left!=null?((TruffleParser.shift_expr_return)left).lparen:null);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "and_expr"


	public static class shift_expr_return extends ParserRuleReturnScope {
		public Token lparen = null;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "shift_expr"
	// Truffle.g:1488:1: shift_expr returns [Token lparen = null] : left= arith_expr ( ( shift_op right+= arith_expr )+ | -> $left) ;
	public final TruffleParser.shift_expr_return shift_expr() throws RecognitionException {
		TruffleParser.shift_expr_return retval = new TruffleParser.shift_expr_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		List<Object> list_right=null;
		ParserRuleReturnScope left =null;
		ParserRuleReturnScope shift_op204 =null;
		RuleReturnScope right = null;
		RewriteRuleSubtreeStream stream_arith_expr=new RewriteRuleSubtreeStream(adaptor,"rule arith_expr");
		RewriteRuleSubtreeStream stream_shift_op=new RewriteRuleSubtreeStream(adaptor,"rule shift_op");


		    List ops = new ArrayList();
		    List toks = new ArrayList();

		try {
			// Truffle.g:1504:5: (left= arith_expr ( ( shift_op right+= arith_expr )+ | -> $left) )
			// Truffle.g:1504:7: left= arith_expr ( ( shift_op right+= arith_expr )+ | -> $left)
			{
			pushFollow(FOLLOW_arith_expr_in_shift_expr5361);
			left=arith_expr();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_arith_expr.add(left.getTree());
			// Truffle.g:1505:9: ( ( shift_op right+= arith_expr )+ | -> $left)
			int alt98=2;
			int LA98_0 = input.LA(1);
			if ( (LA98_0==LEFTSHIFT||LA98_0==RIGHTSHIFT) ) {
				alt98=1;
			}
			else if ( (LA98_0==EOF||(LA98_0 >= AMPER && LA98_0 <= AS)||LA98_0==ASSIGN||LA98_0==BACKQUOTE||(LA98_0 >= CIRCUMFLEX && LA98_0 <= CIRCUMFLEXEQUAL)||(LA98_0 >= COLON && LA98_0 <= COMMA)||LA98_0==DOUBLESLASHEQUAL||LA98_0==DOUBLESTAREQUAL||LA98_0==EQUAL||LA98_0==FOR||(LA98_0 >= GREATER && LA98_0 <= IF)||LA98_0==IN||LA98_0==IS||(LA98_0 >= LEFTSHIFTEQUAL && LA98_0 <= LESSEQUAL)||LA98_0==MINUSEQUAL||LA98_0==NEWLINE||(LA98_0 >= NOT && LA98_0 <= ORELSE)||LA98_0==PERCENTEQUAL||LA98_0==PLUSEQUAL||(LA98_0 >= RBRACK && LA98_0 <= RCURLY)||(LA98_0 >= RIGHTSHIFTEQUAL && LA98_0 <= SEMI)||LA98_0==SLASHEQUAL||LA98_0==STAREQUAL||(LA98_0 >= VBAR && LA98_0 <= VBAREQUAL)) ) {
				alt98=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 98, 0, input);
				throw nvae;
			}

			switch (alt98) {
				case 1 :
					// Truffle.g:1505:11: ( shift_op right+= arith_expr )+
					{
					// Truffle.g:1505:11: ( shift_op right+= arith_expr )+
					int cnt97=0;
					loop97:
					while (true) {
						int alt97=2;
						int LA97_0 = input.LA(1);
						if ( (LA97_0==LEFTSHIFT||LA97_0==RIGHTSHIFT) ) {
							alt97=1;
						}

						switch (alt97) {
						case 1 :
							// Truffle.g:1505:13: shift_op right+= arith_expr
							{
							pushFollow(FOLLOW_shift_op_in_shift_expr5375);
							shift_op204=shift_op();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_shift_op.add(shift_op204.getTree());
							pushFollow(FOLLOW_arith_expr_in_shift_expr5379);
							right=arith_expr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_arith_expr.add(right.getTree());
							if (list_right==null) list_right=new ArrayList<Object>();
							list_right.add(right.getTree());
							if ( state.backtracking==0 ) {
							                ops.add((shift_op204!=null?((TruffleParser.shift_op_return)shift_op204).op:null));
							                toks.add((shift_op204!=null?(shift_op204.start):null));
							            }
							}
							break;

						default :
							if ( cnt97 >= 1 ) break loop97;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(97, input);
							throw eee;
						}
						cnt97++;
					}

					}
					break;
				case 2 :
					// Truffle.g:1512:8: 
					{
					// AST REWRITE
					// elements: left
					// token labels: 
					// rule labels: retval, left
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1512:8: -> $left
					{
						adaptor.addChild(root_0, stream_left.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (!ops.isEmpty()) {
			        Token tok = (left!=null?(left.start):null);
			        if ((left!=null?((TruffleParser.arith_expr_return)left).lparen:null) != null) {
			            tok = (left!=null?((TruffleParser.arith_expr_return)left).lparen:null);
			        }
			        retval.tree = actions.makeBinOp(tok, (left!=null?((PNode)left.getTree()):null), ops, list_right, toks);
			    }
			    retval.lparen = (left!=null?((TruffleParser.arith_expr_return)left).lparen:null);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "shift_expr"


	public static class shift_op_return extends ParserRuleReturnScope {
		public operatorType op;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "shift_op"
	// Truffle.g:1516:1: shift_op returns [operatorType op] : ( LEFTSHIFT | RIGHTSHIFT );
	public final TruffleParser.shift_op_return shift_op() throws RecognitionException {
		TruffleParser.shift_op_return retval = new TruffleParser.shift_op_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token LEFTSHIFT205=null;
		Token RIGHTSHIFT206=null;

		PNode LEFTSHIFT205_tree=null;
		PNode RIGHTSHIFT206_tree=null;

		try {
			// Truffle.g:1518:5: ( LEFTSHIFT | RIGHTSHIFT )
			int alt99=2;
			int LA99_0 = input.LA(1);
			if ( (LA99_0==LEFTSHIFT) ) {
				alt99=1;
			}
			else if ( (LA99_0==RIGHTSHIFT) ) {
				alt99=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 99, 0, input);
				throw nvae;
			}

			switch (alt99) {
				case 1 :
					// Truffle.g:1518:7: LEFTSHIFT
					{
					root_0 = (PNode)adaptor.nil();


					LEFTSHIFT205=(Token)match(input,LEFTSHIFT,FOLLOW_LEFTSHIFT_in_shift_op5463); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LEFTSHIFT205_tree = (PNode)adaptor.create(LEFTSHIFT205);
					adaptor.addChild(root_0, LEFTSHIFT205_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = operatorType.LShift;
					      }
					}
					break;
				case 2 :
					// Truffle.g:1522:7: RIGHTSHIFT
					{
					root_0 = (PNode)adaptor.nil();


					RIGHTSHIFT206=(Token)match(input,RIGHTSHIFT,FOLLOW_RIGHTSHIFT_in_shift_op5479); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					RIGHTSHIFT206_tree = (PNode)adaptor.create(RIGHTSHIFT206);
					adaptor.addChild(root_0, RIGHTSHIFT206_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = operatorType.RShift;
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "shift_op"


	public static class arith_expr_return extends ParserRuleReturnScope {
		public Token lparen = null;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "arith_expr"
	// Truffle.g:1529:1: arith_expr returns [Token lparen = null] : left= term ( ( arith_op right+= term )+ | -> $left) ;
	public final TruffleParser.arith_expr_return arith_expr() throws RecognitionException {
		TruffleParser.arith_expr_return retval = new TruffleParser.arith_expr_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		List<Object> list_right=null;
		ParserRuleReturnScope left =null;
		ParserRuleReturnScope arith_op207 =null;
		RuleReturnScope right = null;
		RewriteRuleSubtreeStream stream_arith_op=new RewriteRuleSubtreeStream(adaptor,"rule arith_op");
		RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");


		    List ops = new ArrayList();
		    List toks = new ArrayList();

		try {
			// Truffle.g:1545:5: (left= term ( ( arith_op right+= term )+ | -> $left) )
			// Truffle.g:1545:7: left= term ( ( arith_op right+= term )+ | -> $left)
			{
			pushFollow(FOLLOW_term_in_arith_expr5525);
			left=term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_term.add(left.getTree());
			// Truffle.g:1546:9: ( ( arith_op right+= term )+ | -> $left)
			int alt101=2;
			int LA101_0 = input.LA(1);
			if ( (LA101_0==MINUS||LA101_0==PLUS) ) {
				alt101=1;
			}
			else if ( (LA101_0==EOF||(LA101_0 >= AMPER && LA101_0 <= AS)||LA101_0==ASSIGN||LA101_0==BACKQUOTE||(LA101_0 >= CIRCUMFLEX && LA101_0 <= CIRCUMFLEXEQUAL)||(LA101_0 >= COLON && LA101_0 <= COMMA)||LA101_0==DOUBLESLASHEQUAL||LA101_0==DOUBLESTAREQUAL||LA101_0==EQUAL||LA101_0==FOR||(LA101_0 >= GREATER && LA101_0 <= IF)||LA101_0==IN||LA101_0==IS||(LA101_0 >= LEFTSHIFT && LA101_0 <= LESSEQUAL)||LA101_0==MINUSEQUAL||LA101_0==NEWLINE||(LA101_0 >= NOT && LA101_0 <= ORELSE)||LA101_0==PERCENTEQUAL||LA101_0==PLUSEQUAL||(LA101_0 >= RBRACK && LA101_0 <= RCURLY)||(LA101_0 >= RIGHTSHIFT && LA101_0 <= SEMI)||LA101_0==SLASHEQUAL||LA101_0==STAREQUAL||(LA101_0 >= VBAR && LA101_0 <= VBAREQUAL)) ) {
				alt101=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 101, 0, input);
				throw nvae;
			}

			switch (alt101) {
				case 1 :
					// Truffle.g:1546:11: ( arith_op right+= term )+
					{
					// Truffle.g:1546:11: ( arith_op right+= term )+
					int cnt100=0;
					loop100:
					while (true) {
						int alt100=2;
						int LA100_0 = input.LA(1);
						if ( (LA100_0==MINUS||LA100_0==PLUS) ) {
							alt100=1;
						}

						switch (alt100) {
						case 1 :
							// Truffle.g:1546:12: arith_op right+= term
							{
							pushFollow(FOLLOW_arith_op_in_arith_expr5538);
							arith_op207=arith_op();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_arith_op.add(arith_op207.getTree());
							pushFollow(FOLLOW_term_in_arith_expr5542);
							right=term();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_term.add(right.getTree());
							if (list_right==null) list_right=new ArrayList<Object>();
							list_right.add(right.getTree());
							if ( state.backtracking==0 ) {
							               ops.add((arith_op207!=null?((TruffleParser.arith_op_return)arith_op207).op:null));
							               toks.add((arith_op207!=null?(arith_op207.start):null));
							           }
							}
							break;

						default :
							if ( cnt100 >= 1 ) break loop100;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(100, input);
							throw eee;
						}
						cnt100++;
					}

					}
					break;
				case 2 :
					// Truffle.g:1553:8: 
					{
					// AST REWRITE
					// elements: left
					// token labels: 
					// rule labels: retval, left
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1553:8: -> $left
					{
						adaptor.addChild(root_0, stream_left.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (!ops.isEmpty()) {
			        Token tok = (left!=null?(left.start):null);
			        if ((left!=null?((TruffleParser.term_return)left).lparen:null) != null) {
			            tok = (left!=null?((TruffleParser.term_return)left).lparen:null);
			        }
			        retval.tree = actions.makeBinOp(tok, (left!=null?((PNode)left.getTree()):null), ops, list_right, toks);
			    }
			    retval.lparen = (left!=null?((TruffleParser.term_return)left).lparen:null);
			}
		}
		catch (RewriteCardinalityException rce) {

			        PNode badNode = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), null);
			        retval.tree = badNode;
			        errorHandler.error("Internal Parser Error", badNode.getToken());
			    
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "arith_expr"


	public static class arith_op_return extends ParserRuleReturnScope {
		public operatorType op;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "arith_op"
	// Truffle.g:1565:1: arith_op returns [operatorType op] : ( PLUS | MINUS );
	public final TruffleParser.arith_op_return arith_op() throws RecognitionException {
		TruffleParser.arith_op_return retval = new TruffleParser.arith_op_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token PLUS208=null;
		Token MINUS209=null;

		PNode PLUS208_tree=null;
		PNode MINUS209_tree=null;

		try {
			// Truffle.g:1567:5: ( PLUS | MINUS )
			int alt102=2;
			int LA102_0 = input.LA(1);
			if ( (LA102_0==PLUS) ) {
				alt102=1;
			}
			else if ( (LA102_0==MINUS) ) {
				alt102=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 102, 0, input);
				throw nvae;
			}

			switch (alt102) {
				case 1 :
					// Truffle.g:1567:7: PLUS
					{
					root_0 = (PNode)adaptor.nil();


					PLUS208=(Token)match(input,PLUS,FOLLOW_PLUS_in_arith_op5650); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					PLUS208_tree = (PNode)adaptor.create(PLUS208);
					adaptor.addChild(root_0, PLUS208_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = operatorType.Add;
					      }
					}
					break;
				case 2 :
					// Truffle.g:1571:7: MINUS
					{
					root_0 = (PNode)adaptor.nil();


					MINUS209=(Token)match(input,MINUS,FOLLOW_MINUS_in_arith_op5666); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					MINUS209_tree = (PNode)adaptor.create(MINUS209);
					adaptor.addChild(root_0, MINUS209_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = operatorType.Sub;
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "arith_op"


	public static class term_return extends ParserRuleReturnScope {
		public Token lparen = null;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "term"
	// Truffle.g:1578:1: term returns [Token lparen = null] : left= factor ( ( term_op right+= factor )+ | -> $left) ;
	public final TruffleParser.term_return term() throws RecognitionException {
		TruffleParser.term_return retval = new TruffleParser.term_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		List<Object> list_right=null;
		ParserRuleReturnScope left =null;
		ParserRuleReturnScope term_op210 =null;
		RuleReturnScope right = null;
		RewriteRuleSubtreeStream stream_term_op=new RewriteRuleSubtreeStream(adaptor,"rule term_op");
		RewriteRuleSubtreeStream stream_factor=new RewriteRuleSubtreeStream(adaptor,"rule factor");


		    List ops = new ArrayList();
		    List toks = new ArrayList();

		try {
			// Truffle.g:1594:5: (left= factor ( ( term_op right+= factor )+ | -> $left) )
			// Truffle.g:1594:7: left= factor ( ( term_op right+= factor )+ | -> $left)
			{
			pushFollow(FOLLOW_factor_in_term5712);
			left=factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_factor.add(left.getTree());
			// Truffle.g:1595:9: ( ( term_op right+= factor )+ | -> $left)
			int alt104=2;
			int LA104_0 = input.LA(1);
			if ( (LA104_0==DOUBLESLASH||LA104_0==PERCENT||LA104_0==SLASH||LA104_0==STAR) ) {
				alt104=1;
			}
			else if ( (LA104_0==EOF||(LA104_0 >= AMPER && LA104_0 <= AS)||LA104_0==ASSIGN||LA104_0==BACKQUOTE||(LA104_0 >= CIRCUMFLEX && LA104_0 <= CIRCUMFLEXEQUAL)||(LA104_0 >= COLON && LA104_0 <= COMMA)||LA104_0==DOUBLESLASHEQUAL||LA104_0==DOUBLESTAREQUAL||LA104_0==EQUAL||LA104_0==FOR||(LA104_0 >= GREATER && LA104_0 <= IF)||LA104_0==IN||LA104_0==IS||(LA104_0 >= LEFTSHIFT && LA104_0 <= LESSEQUAL)||(LA104_0 >= MINUS && LA104_0 <= MINUSEQUAL)||LA104_0==NEWLINE||(LA104_0 >= NOT && LA104_0 <= ORELSE)||(LA104_0 >= PERCENTEQUAL && LA104_0 <= PLUSEQUAL)||(LA104_0 >= RBRACK && LA104_0 <= RCURLY)||(LA104_0 >= RIGHTSHIFT && LA104_0 <= SEMI)||LA104_0==SLASHEQUAL||LA104_0==STAREQUAL||(LA104_0 >= VBAR && LA104_0 <= VBAREQUAL)) ) {
				alt104=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 104, 0, input);
				throw nvae;
			}

			switch (alt104) {
				case 1 :
					// Truffle.g:1595:11: ( term_op right+= factor )+
					{
					// Truffle.g:1595:11: ( term_op right+= factor )+
					int cnt103=0;
					loop103:
					while (true) {
						int alt103=2;
						int LA103_0 = input.LA(1);
						if ( (LA103_0==DOUBLESLASH||LA103_0==PERCENT||LA103_0==SLASH||LA103_0==STAR) ) {
							alt103=1;
						}

						switch (alt103) {
						case 1 :
							// Truffle.g:1595:12: term_op right+= factor
							{
							pushFollow(FOLLOW_term_op_in_term5725);
							term_op210=term_op();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_term_op.add(term_op210.getTree());
							pushFollow(FOLLOW_factor_in_term5729);
							right=factor();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_factor.add(right.getTree());
							if (list_right==null) list_right=new ArrayList<Object>();
							list_right.add(right.getTree());
							if ( state.backtracking==0 ) {
							              ops.add((term_op210!=null?((TruffleParser.term_op_return)term_op210).op:null));
							              toks.add((term_op210!=null?(term_op210.start):null));
							          }
							}
							break;

						default :
							if ( cnt103 >= 1 ) break loop103;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(103, input);
							throw eee;
						}
						cnt103++;
					}

					}
					break;
				case 2 :
					// Truffle.g:1602:8: 
					{
					// AST REWRITE
					// elements: left
					// token labels: 
					// rule labels: retval, left
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_left=new RewriteRuleSubtreeStream(adaptor,"rule left",left!=null?left.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1602:8: -> $left
					{
						adaptor.addChild(root_0, stream_left.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.lparen = (left!=null?((TruffleParser.factor_return)left).lparen:null);
			    if (!ops.isEmpty()) {
			        Token tok = (left!=null?(left.start):null);
			        if ((left!=null?((TruffleParser.factor_return)left).lparen:null) != null) {
			            tok = (left!=null?((TruffleParser.factor_return)left).lparen:null);
			        }
			        retval.tree = actions.makeBinOp(tok, (left!=null?((PNode)left.getTree()):null), ops, list_right, toks);
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "term"


	public static class term_op_return extends ParserRuleReturnScope {
		public operatorType op;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "term_op"
	// Truffle.g:1606:1: term_op returns [operatorType op] : ( STAR | SLASH | PERCENT | DOUBLESLASH );
	public final TruffleParser.term_op_return term_op() throws RecognitionException {
		TruffleParser.term_op_return retval = new TruffleParser.term_op_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token STAR211=null;
		Token SLASH212=null;
		Token PERCENT213=null;
		Token DOUBLESLASH214=null;

		PNode STAR211_tree=null;
		PNode SLASH212_tree=null;
		PNode PERCENT213_tree=null;
		PNode DOUBLESLASH214_tree=null;

		try {
			// Truffle.g:1608:5: ( STAR | SLASH | PERCENT | DOUBLESLASH )
			int alt105=4;
			switch ( input.LA(1) ) {
			case STAR:
				{
				alt105=1;
				}
				break;
			case SLASH:
				{
				alt105=2;
				}
				break;
			case PERCENT:
				{
				alt105=3;
				}
				break;
			case DOUBLESLASH:
				{
				alt105=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 105, 0, input);
				throw nvae;
			}
			switch (alt105) {
				case 1 :
					// Truffle.g:1608:7: STAR
					{
					root_0 = (PNode)adaptor.nil();


					STAR211=(Token)match(input,STAR,FOLLOW_STAR_in_term_op5811); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					STAR211_tree = (PNode)adaptor.create(STAR211);
					adaptor.addChild(root_0, STAR211_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = operatorType.Mult;
					      }
					}
					break;
				case 2 :
					// Truffle.g:1612:7: SLASH
					{
					root_0 = (PNode)adaptor.nil();


					SLASH212=(Token)match(input,SLASH,FOLLOW_SLASH_in_term_op5827); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					SLASH212_tree = (PNode)adaptor.create(SLASH212);
					adaptor.addChild(root_0, SLASH212_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = operatorType.Div;
					      }
					}
					break;
				case 3 :
					// Truffle.g:1616:7: PERCENT
					{
					root_0 = (PNode)adaptor.nil();


					PERCENT213=(Token)match(input,PERCENT,FOLLOW_PERCENT_in_term_op5843); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					PERCENT213_tree = (PNode)adaptor.create(PERCENT213);
					adaptor.addChild(root_0, PERCENT213_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = operatorType.Mod;
					      }
					}
					break;
				case 4 :
					// Truffle.g:1620:7: DOUBLESLASH
					{
					root_0 = (PNode)adaptor.nil();


					DOUBLESLASH214=(Token)match(input,DOUBLESLASH,FOLLOW_DOUBLESLASH_in_term_op5859); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOUBLESLASH214_tree = (PNode)adaptor.create(DOUBLESLASH214);
					adaptor.addChild(root_0, DOUBLESLASH214_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.op = operatorType.FloorDiv;
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "term_op"


	public static class factor_return extends ParserRuleReturnScope {
		public PNode etype;
		public Token lparen = null;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "factor"
	// Truffle.g:1627:1: factor returns [PNode etype, Token lparen = null] : ( PLUS p= factor | MINUS m= factor | TILDE t= factor | power );
	public final TruffleParser.factor_return factor() throws RecognitionException {
		TruffleParser.factor_return retval = new TruffleParser.factor_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token PLUS215=null;
		Token MINUS216=null;
		Token TILDE217=null;
		ParserRuleReturnScope p =null;
		ParserRuleReturnScope m =null;
		ParserRuleReturnScope t =null;
		ParserRuleReturnScope power218 =null;

		PNode PLUS215_tree=null;
		PNode MINUS216_tree=null;
		PNode TILDE217_tree=null;

		try {
			// Truffle.g:1632:5: ( PLUS p= factor | MINUS m= factor | TILDE t= factor | power )
			int alt106=4;
			int LA106_0 = input.LA(1);
			if ( (LA106_0==PLUS) ) {
				alt106=1;
			}
			else if ( (LA106_0==MINUS) ) {
				alt106=2;
			}
			else if ( (LA106_0==TILDE) ) {
				alt106=3;
			}
			else if ( (LA106_0==BACKQUOTE||(LA106_0 >= LBRACK && LA106_0 <= LCURLY)||LA106_0==LPAREN||LA106_0==NAME) ) {
				alt106=4;
			}
			else if ( (LA106_0==PRINT) && ((printFunction))) {
				alt106=4;
			}
			else if ( (LA106_0==COMPLEX||LA106_0==FALSE||LA106_0==FLOAT||LA106_0==INT||LA106_0==NONE||LA106_0==STRING||LA106_0==TRUE) ) {
				alt106=4;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 106, 0, input);
				throw nvae;
			}

			switch (alt106) {
				case 1 :
					// Truffle.g:1632:7: PLUS p= factor
					{
					root_0 = (PNode)adaptor.nil();


					PLUS215=(Token)match(input,PLUS,FOLLOW_PLUS_in_factor5898); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					PLUS215_tree = (PNode)adaptor.create(PLUS215);
					adaptor.addChild(root_0, PLUS215_tree);
					}

					pushFollow(FOLLOW_factor_in_factor5902);
					p=factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, p.getTree());

					if ( state.backtracking==0 ) {
					            retval.etype = actions.makeUnaryOp(PLUS215, unaryopType.UAdd, (p!=null?((TruffleParser.factor_return)p).etype:null));
					      }
					}
					break;
				case 2 :
					// Truffle.g:1636:7: MINUS m= factor
					{
					root_0 = (PNode)adaptor.nil();


					MINUS216=(Token)match(input,MINUS,FOLLOW_MINUS_in_factor5918); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					MINUS216_tree = (PNode)adaptor.create(MINUS216);
					adaptor.addChild(root_0, MINUS216_tree);
					}

					pushFollow(FOLLOW_factor_in_factor5922);
					m=factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, m.getTree());

					if ( state.backtracking==0 ) {
						          retval.etype = actions.negate(MINUS216, (m!=null?((TruffleParser.factor_return)m).etype:null));
					      }
					}
					break;
				case 3 :
					// Truffle.g:1640:7: TILDE t= factor
					{
					root_0 = (PNode)adaptor.nil();


					TILDE217=(Token)match(input,TILDE,FOLLOW_TILDE_in_factor5938); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					TILDE217_tree = (PNode)adaptor.create(TILDE217);
					adaptor.addChild(root_0, TILDE217_tree);
					}

					pushFollow(FOLLOW_factor_in_factor5942);
					t=factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

					if ( state.backtracking==0 ) {
						          retval.etype = actions.makeUnaryOp(TILDE217, unaryopType.Invert, (t!=null?((TruffleParser.factor_return)t).etype:null));
					      }
					}
					break;
				case 4 :
					// Truffle.g:1644:7: power
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_power_in_factor5958);
					power218=power();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, power218.getTree());

					if ( state.backtracking==0 ) {
					          retval.etype = actions.castExpr((power218!=null?((PNode)power218.getTree()):null));
					          retval.lparen = (power218!=null?((TruffleParser.power_return)power218).lparen:null);
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.tree = retval.etype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "factor"


	public static class power_return extends ParserRuleReturnScope {
		public PNode etype;
		public Token lparen = null;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "power"
	// Truffle.g:1652:1: power returns [PNode etype, Token lparen = null] : atom (t+= trailer[$atom.start, $atom.tree] )* ( options {greedy=true; } :d= DOUBLESTAR factor )? ;
	public final TruffleParser.power_return power() throws RecognitionException {
		TruffleParser.power_return retval = new TruffleParser.power_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token d=null;
		List<Object> list_t=null;
		ParserRuleReturnScope atom219 =null;
		ParserRuleReturnScope factor220 =null;
		RuleReturnScope t = null;
		PNode d_tree=null;

		try {
			// Truffle.g:1657:5: ( atom (t+= trailer[$atom.start, $atom.tree] )* ( options {greedy=true; } :d= DOUBLESTAR factor )? )
			// Truffle.g:1657:7: atom (t+= trailer[$atom.start, $atom.tree] )* ( options {greedy=true; } :d= DOUBLESTAR factor )?
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_atom_in_power5997);
			atom219=atom();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, atom219.getTree());

			// Truffle.g:1657:12: (t+= trailer[$atom.start, $atom.tree] )*
			loop107:
			while (true) {
				int alt107=2;
				int LA107_0 = input.LA(1);
				if ( (LA107_0==DOT||LA107_0==LBRACK||LA107_0==LPAREN) ) {
					alt107=1;
				}

				switch (alt107) {
				case 1 :
					// Truffle.g:1657:13: t+= trailer[$atom.start, $atom.tree]
					{
					pushFollow(FOLLOW_trailer_in_power6002);
					t=trailer((atom219!=null?(atom219.start):null), (atom219!=null?((PNode)atom219.getTree()):null));
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

					if (list_t==null) list_t=new ArrayList<Object>();
					list_t.add(t.getTree());
					}
					break;

				default :
					break loop107;
				}
			}

			// Truffle.g:1657:51: ( options {greedy=true; } :d= DOUBLESTAR factor )?
			int alt108=2;
			int LA108_0 = input.LA(1);
			if ( (LA108_0==DOUBLESTAR) ) {
				alt108=1;
			}
			switch (alt108) {
				case 1 :
					// Truffle.g:1657:75: d= DOUBLESTAR factor
					{
					d=(Token)match(input,DOUBLESTAR,FOLLOW_DOUBLESTAR_in_power6017); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					d_tree = (PNode)adaptor.create(d);
					adaptor.addChild(root_0, d_tree);
					}

					pushFollow(FOLLOW_factor_in_power6019);
					factor220=factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, factor220.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          retval.lparen = (atom219!=null?((TruffleParser.atom_return)atom219).lparen:null);
			          //XXX: This could be better.
			          retval.etype = actions.castExpr((atom219!=null?((PNode)atom219.getTree()):null));
			          if (list_t != null) {
			              for(Object o : list_t) {
			                  retval.etype = actions.makePowerSpecific(retval.etype, o);
			              }
			          }
			          if (d != null) {
			              List right = new ArrayList();
			              right.add((factor220!=null?((PNode)factor220.getTree()):null));
			              retval.etype = actions.makeBinOp((atom219!=null?(atom219.start):null), retval.etype, operatorType.Pow, right);
			          }
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.tree = retval.etype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "power"


	public static class atom_return extends ParserRuleReturnScope {
		public Token lparen = null;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "atom"
	// Truffle.g:1680:1: atom returns [Token lparen = null] : ( LPAREN ( yield_expr | testlist_gexp -> testlist_gexp |) RPAREN | LBRACK ( listmaker[$LBRACK] -> listmaker |) RBRACK | LCURLY ( dictorsetmaker[$LCURLY] -> dictorsetmaker |) RCURLY |lb= BACKQUOTE testlist[expr_contextType.Load] rb= BACKQUOTE | name_or_print | NONE | TRUE | FALSE | INT | FLOAT | COMPLEX | (S+= STRING )+ );
	public final TruffleParser.atom_return atom() throws RecognitionException {
		TruffleParser.atom_return retval = new TruffleParser.atom_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token lb=null;
		Token rb=null;
		Token LPAREN221=null;
		Token RPAREN224=null;
		Token LBRACK225=null;
		Token RBRACK227=null;
		Token LCURLY228=null;
		Token RCURLY230=null;
		Token NONE233=null;
		Token TRUE234=null;
		Token FALSE235=null;
		Token INT236=null;
		Token FLOAT237=null;
		Token COMPLEX238=null;
		Token S=null;
		List<Object> list_S=null;
		ParserRuleReturnScope yield_expr222 =null;
		ParserRuleReturnScope testlist_gexp223 =null;
		ParserRuleReturnScope listmaker226 =null;
		ParserRuleReturnScope dictorsetmaker229 =null;
		ParserRuleReturnScope testlist231 =null;
		ParserRuleReturnScope name_or_print232 =null;

		PNode lb_tree=null;
		PNode rb_tree=null;
		PNode LPAREN221_tree=null;
		PNode RPAREN224_tree=null;
		PNode LBRACK225_tree=null;
		PNode RBRACK227_tree=null;
		PNode LCURLY228_tree=null;
		PNode RCURLY230_tree=null;
		PNode NONE233_tree=null;
		PNode TRUE234_tree=null;
		PNode FALSE235_tree=null;
		PNode INT236_tree=null;
		PNode FLOAT237_tree=null;
		PNode COMPLEX238_tree=null;
		PNode S_tree=null;
		RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_LCURLY=new RewriteRuleTokenStream(adaptor,"token LCURLY");
		RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_RCURLY=new RewriteRuleTokenStream(adaptor,"token RCURLY");
		RewriteRuleSubtreeStream stream_testlist_gexp=new RewriteRuleSubtreeStream(adaptor,"rule testlist_gexp");
		RewriteRuleSubtreeStream stream_yield_expr=new RewriteRuleSubtreeStream(adaptor,"rule yield_expr");
		RewriteRuleSubtreeStream stream_listmaker=new RewriteRuleSubtreeStream(adaptor,"rule listmaker");
		RewriteRuleSubtreeStream stream_dictorsetmaker=new RewriteRuleSubtreeStream(adaptor,"rule dictorsetmaker");


		    PNode etype = null;

		try {
			// Truffle.g:1690:5: ( LPAREN ( yield_expr | testlist_gexp -> testlist_gexp |) RPAREN | LBRACK ( listmaker[$LBRACK] -> listmaker |) RBRACK | LCURLY ( dictorsetmaker[$LCURLY] -> dictorsetmaker |) RCURLY |lb= BACKQUOTE testlist[expr_contextType.Load] rb= BACKQUOTE | name_or_print | NONE | TRUE | FALSE | INT | FLOAT | COMPLEX | (S+= STRING )+ )
			int alt113=12;
			int LA113_0 = input.LA(1);
			if ( (LA113_0==LPAREN) ) {
				alt113=1;
			}
			else if ( (LA113_0==LBRACK) ) {
				alt113=2;
			}
			else if ( (LA113_0==LCURLY) ) {
				alt113=3;
			}
			else if ( (LA113_0==BACKQUOTE) ) {
				alt113=4;
			}
			else if ( (LA113_0==NAME) ) {
				alt113=5;
			}
			else if ( (LA113_0==PRINT) && ((printFunction))) {
				alt113=5;
			}
			else if ( (LA113_0==NONE) ) {
				alt113=6;
			}
			else if ( (LA113_0==TRUE) ) {
				alt113=7;
			}
			else if ( (LA113_0==FALSE) ) {
				alt113=8;
			}
			else if ( (LA113_0==INT) ) {
				alt113=9;
			}
			else if ( (LA113_0==FLOAT) ) {
				alt113=10;
			}
			else if ( (LA113_0==COMPLEX) ) {
				alt113=11;
			}
			else if ( (LA113_0==STRING) ) {
				alt113=12;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 113, 0, input);
				throw nvae;
			}

			switch (alt113) {
				case 1 :
					// Truffle.g:1690:7: LPAREN ( yield_expr | testlist_gexp -> testlist_gexp |) RPAREN
					{
					LPAREN221=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_atom6069); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(LPAREN221);

					if ( state.backtracking==0 ) {
					          retval.lparen = LPAREN221;
					      }
					// Truffle.g:1694:7: ( yield_expr | testlist_gexp -> testlist_gexp |)
					int alt109=3;
					int LA109_0 = input.LA(1);
					if ( (LA109_0==YIELD) ) {
						alt109=1;
					}
					else if ( (LA109_0==BACKQUOTE||(LA109_0 >= LBRACK && LA109_0 <= LCURLY)||(LA109_0 >= LPAREN && LA109_0 <= MINUS)||LA109_0==NAME||LA109_0==NOT||LA109_0==PLUS||LA109_0==TILDE) ) {
						alt109=2;
					}
					else if ( (LA109_0==PRINT) && ((printFunction))) {
						alt109=2;
					}
					else if ( (LA109_0==COMPLEX||LA109_0==FALSE||LA109_0==FLOAT||LA109_0==INT||LA109_0==LAMBDA||LA109_0==NONE||LA109_0==STRING||LA109_0==TRUE) ) {
						alt109=2;
					}
					else if ( (LA109_0==RPAREN) ) {
						alt109=3;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 109, 0, input);
						throw nvae;
					}

					switch (alt109) {
						case 1 :
							// Truffle.g:1694:9: yield_expr
							{
							pushFollow(FOLLOW_yield_expr_in_atom6087);
							yield_expr222=yield_expr();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_yield_expr.add(yield_expr222.getTree());
							if ( state.backtracking==0 ) {
							            etype = (yield_expr222!=null?((TruffleParser.yield_expr_return)yield_expr222).etype:null);
							        }
							}
							break;
						case 2 :
							// Truffle.g:1698:9: testlist_gexp
							{
							pushFollow(FOLLOW_testlist_gexp_in_atom6107);
							testlist_gexp223=testlist_gexp();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_testlist_gexp.add(testlist_gexp223.getTree());
							// AST REWRITE
							// elements: testlist_gexp
							// token labels: 
							// rule labels: retval
							// token list labels: 
							// rule list labels: 
							// wildcard labels: 
							if ( state.backtracking==0 ) {
							retval.tree = root_0;
							RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

							root_0 = (PNode)adaptor.nil();
							// 1699:6: -> testlist_gexp
							{
								adaptor.addChild(root_0, stream_testlist_gexp.nextTree());
							}


							retval.tree = root_0;
							}

							}
							break;
						case 3 :
							// Truffle.g:1701:9: 
							{
							if ( state.backtracking==0 ) {
							            etype = actions.makeTuple(LPAREN221, new ArrayList<PNode>(), expr_stack.peek().ctype);
							        }
							}
							break;

					}

					RPAREN224=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_atom6150); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(RPAREN224);

					}
					break;
				case 2 :
					// Truffle.g:1706:7: LBRACK ( listmaker[$LBRACK] -> listmaker |) RBRACK
					{
					LBRACK225=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_atom6158); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LBRACK.add(LBRACK225);

					// Truffle.g:1707:7: ( listmaker[$LBRACK] -> listmaker |)
					int alt110=2;
					int LA110_0 = input.LA(1);
					if ( (LA110_0==BACKQUOTE||(LA110_0 >= LBRACK && LA110_0 <= LCURLY)||(LA110_0 >= LPAREN && LA110_0 <= MINUS)||LA110_0==NAME||LA110_0==NOT||LA110_0==PLUS||LA110_0==TILDE) ) {
						alt110=1;
					}
					else if ( (LA110_0==PRINT) && ((printFunction))) {
						alt110=1;
					}
					else if ( (LA110_0==COMPLEX||LA110_0==FALSE||LA110_0==FLOAT||LA110_0==INT||LA110_0==LAMBDA||LA110_0==NONE||LA110_0==STRING||LA110_0==TRUE) ) {
						alt110=1;
					}
					else if ( (LA110_0==RBRACK) ) {
						alt110=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 110, 0, input);
						throw nvae;
					}

					switch (alt110) {
						case 1 :
							// Truffle.g:1707:8: listmaker[$LBRACK]
							{
							pushFollow(FOLLOW_listmaker_in_atom6167);
							listmaker226=listmaker(LBRACK225);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_listmaker.add(listmaker226.getTree());
							// AST REWRITE
							// elements: listmaker
							// token labels: 
							// rule labels: retval
							// token list labels: 
							// rule list labels: 
							// wildcard labels: 
							if ( state.backtracking==0 ) {
							retval.tree = root_0;
							RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

							root_0 = (PNode)adaptor.nil();
							// 1708:6: -> listmaker
							{
								adaptor.addChild(root_0, stream_listmaker.nextTree());
							}


							retval.tree = root_0;
							}

							}
							break;
						case 2 :
							// Truffle.g:1710:8: 
							{
							if ( state.backtracking==0 ) {
							           etype = actions.makeList(LBRACK225, new ArrayList<PNode>(), expr_stack.peek().ctype);
							       }
							}
							break;

					}

					RBRACK227=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_atom6210); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RBRACK.add(RBRACK227);

					}
					break;
				case 3 :
					// Truffle.g:1715:7: LCURLY ( dictorsetmaker[$LCURLY] -> dictorsetmaker |) RCURLY
					{
					LCURLY228=(Token)match(input,LCURLY,FOLLOW_LCURLY_in_atom6218); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LCURLY.add(LCURLY228);

					// Truffle.g:1716:7: ( dictorsetmaker[$LCURLY] -> dictorsetmaker |)
					int alt111=2;
					int LA111_0 = input.LA(1);
					if ( (LA111_0==BACKQUOTE||(LA111_0 >= LBRACK && LA111_0 <= LCURLY)||(LA111_0 >= LPAREN && LA111_0 <= MINUS)||LA111_0==NAME||LA111_0==NOT||LA111_0==PLUS||LA111_0==TILDE) ) {
						alt111=1;
					}
					else if ( (LA111_0==PRINT) && ((printFunction))) {
						alt111=1;
					}
					else if ( (LA111_0==COMPLEX||LA111_0==FALSE||LA111_0==FLOAT||LA111_0==INT||LA111_0==LAMBDA||LA111_0==NONE||LA111_0==STRING||LA111_0==TRUE) ) {
						alt111=1;
					}
					else if ( (LA111_0==RCURLY) ) {
						alt111=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 111, 0, input);
						throw nvae;
					}

					switch (alt111) {
						case 1 :
							// Truffle.g:1716:8: dictorsetmaker[$LCURLY]
							{
							pushFollow(FOLLOW_dictorsetmaker_in_atom6227);
							dictorsetmaker229=dictorsetmaker(LCURLY228);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_dictorsetmaker.add(dictorsetmaker229.getTree());
							// AST REWRITE
							// elements: dictorsetmaker
							// token labels: 
							// rule labels: retval
							// token list labels: 
							// rule list labels: 
							// wildcard labels: 
							if ( state.backtracking==0 ) {
							retval.tree = root_0;
							RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

							root_0 = (PNode)adaptor.nil();
							// 1717:6: -> dictorsetmaker
							{
								adaptor.addChild(root_0, stream_dictorsetmaker.nextTree());
							}


							retval.tree = root_0;
							}

							}
							break;
						case 2 :
							// Truffle.g:1719:8: 
							{
							if ( state.backtracking==0 ) {
							           etype = actions.makeDict(LCURLY228, new ArrayList<PNode>(), new ArrayList<PNode>());
							       }
							}
							break;

					}

					RCURLY230=(Token)match(input,RCURLY,FOLLOW_RCURLY_in_atom6271); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RCURLY.add(RCURLY230);

					}
					break;
				case 4 :
					// Truffle.g:1724:8: lb= BACKQUOTE testlist[expr_contextType.Load] rb= BACKQUOTE
					{
					root_0 = (PNode)adaptor.nil();


					lb=(Token)match(input,BACKQUOTE,FOLLOW_BACKQUOTE_in_atom6282); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					lb_tree = (PNode)adaptor.create(lb);
					adaptor.addChild(root_0, lb_tree);
					}

					pushFollow(FOLLOW_testlist_in_atom6284);
					testlist231=testlist(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, testlist231.getTree());

					rb=(Token)match(input,BACKQUOTE,FOLLOW_BACKQUOTE_in_atom6289); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					rb_tree = (PNode)adaptor.create(rb);
					adaptor.addChild(root_0, rb_tree);
					}

					if ( state.backtracking==0 ) {
					           etype = actions.makeRepr(lb, actions.castExpr((testlist231!=null?((PNode)testlist231.getTree()):null)));
					       }
					}
					break;
				case 5 :
					// Truffle.g:1728:8: name_or_print
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_name_or_print_in_atom6307);
					name_or_print232=name_or_print();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, name_or_print232.getTree());

					if ( state.backtracking==0 ) {
					           etype = actions.makeName((name_or_print232!=null?(name_or_print232.start):null), (name_or_print232!=null?input.toString(name_or_print232.start,name_or_print232.stop):null), expr_stack.peek().ctype);
					     }
					}
					break;
				case 6 :
					// Truffle.g:1732:8: NONE
					{
					root_0 = (PNode)adaptor.nil();


					NONE233=(Token)match(input,NONE,FOLLOW_NONE_in_atom6325); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NONE233_tree = (PNode)adaptor.create(NONE233);
					adaptor.addChild(root_0, NONE233_tree);
					}

					if ( state.backtracking==0 ) {
					       	   etype = actions.makeNone(NONE233);
					       }
					}
					break;
				case 7 :
					// Truffle.g:1736:8: TRUE
					{
					root_0 = (PNode)adaptor.nil();


					TRUE234=(Token)match(input,TRUE,FOLLOW_TRUE_in_atom6344); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					TRUE234_tree = (PNode)adaptor.create(TRUE234);
					adaptor.addChild(root_0, TRUE234_tree);
					}

					if ( state.backtracking==0 ) {
					       	   etype = actions.makeTrue(TRUE234);
					       }
					}
					break;
				case 8 :
					// Truffle.g:1740:8: FALSE
					{
					root_0 = (PNode)adaptor.nil();


					FALSE235=(Token)match(input,FALSE,FOLLOW_FALSE_in_atom6363); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					FALSE235_tree = (PNode)adaptor.create(FALSE235);
					adaptor.addChild(root_0, FALSE235_tree);
					}

					if ( state.backtracking==0 ) {
					       	   etype = actions.makeFalse(FALSE235);
					       }
					}
					break;
				case 9 :
					// Truffle.g:1744:8: INT
					{
					root_0 = (PNode)adaptor.nil();


					INT236=(Token)match(input,INT,FOLLOW_INT_in_atom6382); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					INT236_tree = (PNode)adaptor.create(INT236);
					adaptor.addChild(root_0, INT236_tree);
					}

					if ( state.backtracking==0 ) {
					//	         etype = new Num(INT236, actions.makeInt(INT236));
					           etype = actions.makeInt(INT236);
					           etype.setToken(INT236);
					       }
					}
					break;
				case 10 :
					// Truffle.g:1754:8: FLOAT
					{
					root_0 = (PNode)adaptor.nil();


					FLOAT237=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_atom6421); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					FLOAT237_tree = (PNode)adaptor.create(FLOAT237);
					adaptor.addChild(root_0, FLOAT237_tree);
					}

					if ( state.backtracking==0 ) {
					//           etype = new Num(FLOAT237, actions.makeFloat(FLOAT237));
					           etype = actions.makeFloat(FLOAT237);
					       }
					}
					break;
				case 11 :
					// Truffle.g:1759:8: COMPLEX
					{
					root_0 = (PNode)adaptor.nil();


					COMPLEX238=(Token)match(input,COMPLEX,FOLLOW_COMPLEX_in_atom6439); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMPLEX238_tree = (PNode)adaptor.create(COMPLEX238);
					adaptor.addChild(root_0, COMPLEX238_tree);
					}

					if ( state.backtracking==0 ) {
					//            etype = new Num(COMPLEX238, actions.makeComplex(COMPLEX238));
					            etype = actions.makeComplex(COMPLEX238);
					       }
					}
					break;
				case 12 :
					// Truffle.g:1764:8: (S+= STRING )+
					{
					root_0 = (PNode)adaptor.nil();


					// Truffle.g:1764:8: (S+= STRING )+
					int cnt112=0;
					loop112:
					while (true) {
						int alt112=2;
						int LA112_0 = input.LA(1);
						if ( (LA112_0==STRING) ) {
							alt112=1;
						}

						switch (alt112) {
						case 1 :
							// Truffle.g:1764:9: S+= STRING
							{
							S=(Token)match(input,STRING,FOLLOW_STRING_in_atom6460); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							S_tree = (PNode)adaptor.create(S);
							adaptor.addChild(root_0, S_tree);
							}

							if (list_S==null) list_S=new ArrayList<Object>();
							list_S.add(S);
							}
							break;

						default :
							if ( cnt112 >= 1 ) break loop112;
							if (state.backtracking>0) {state.failed=true; return retval;}
							EarlyExitException eee = new EarlyExitException(112, input);
							throw eee;
						}
						cnt112++;
					}

					if ( state.backtracking==0 ) {
					           etype = actions.makeStr(actions.extractStringToken(list_S), actions.extractStrings(list_S, encoding, unicodeLiterals));
					       }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   if (etype != null) {
			       retval.tree = etype;
			   }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "atom"


	public static class listmaker_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "listmaker"
	// Truffle.g:1771:1: listmaker[Token lbrack] :t+= test[$expr::ctype] ( list_for[gens] | ( options {greedy=true; } : COMMA t+= test[$expr::ctype] )* ) ( COMMA )? ;
	public final TruffleParser.listmaker_return listmaker(Token lbrack) throws RecognitionException {
		TruffleParser.listmaker_return retval = new TruffleParser.listmaker_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token COMMA240=null;
		Token COMMA241=null;
		List<Object> list_t=null;
		ParserRuleReturnScope list_for239 =null;
		RuleReturnScope t = null;
		PNode COMMA240_tree=null;
		PNode COMMA241_tree=null;


		    List gens = new ArrayList();
		    PNode etype = null;

		try {
			// Truffle.g:1779:5: (t+= test[$expr::ctype] ( list_for[gens] | ( options {greedy=true; } : COMMA t+= test[$expr::ctype] )* ) ( COMMA )? )
			// Truffle.g:1779:7: t+= test[$expr::ctype] ( list_for[gens] | ( options {greedy=true; } : COMMA t+= test[$expr::ctype] )* ) ( COMMA )?
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_test_in_listmaker6503);
			t=test(expr_stack.peek().ctype);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

			if (list_t==null) list_t=new ArrayList<Object>();
			list_t.add(t.getTree());
			// Truffle.g:1780:9: ( list_for[gens] | ( options {greedy=true; } : COMMA t+= test[$expr::ctype] )* )
			int alt115=2;
			int LA115_0 = input.LA(1);
			if ( (LA115_0==FOR) ) {
				alt115=1;
			}
			else if ( (LA115_0==COMMA||LA115_0==RBRACK) ) {
				alt115=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 115, 0, input);
				throw nvae;
			}

			switch (alt115) {
				case 1 :
					// Truffle.g:1780:10: list_for[gens]
					{
					pushFollow(FOLLOW_list_for_in_listmaker6515);
					list_for239=list_for(gens);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, list_for239.getTree());

					if ( state.backtracking==0 ) {
					             Collections.reverse(gens);
					             List<PComprehension> c = gens;
					             etype = actions.makeListComp((retval.start), actions.castExpr(list_t.get(0)), c);
					         }
					}
					break;
				case 2 :
					// Truffle.g:1786:11: ( options {greedy=true; } : COMMA t+= test[$expr::ctype] )*
					{
					// Truffle.g:1786:11: ( options {greedy=true; } : COMMA t+= test[$expr::ctype] )*
					loop114:
					while (true) {
						int alt114=2;
						int LA114_0 = input.LA(1);
						if ( (LA114_0==COMMA) ) {
							int LA114_1 = input.LA(2);
							if ( (LA114_1==BACKQUOTE||LA114_1==COMPLEX||LA114_1==FALSE||LA114_1==FLOAT||LA114_1==INT||(LA114_1 >= LAMBDA && LA114_1 <= LCURLY)||(LA114_1 >= LPAREN && LA114_1 <= MINUS)||LA114_1==NAME||LA114_1==NONE||LA114_1==NOT||LA114_1==PLUS||LA114_1==PRINT||(LA114_1 >= STRING && LA114_1 <= TILDE)||LA114_1==TRUE) ) {
								alt114=1;
							}

						}

						switch (alt114) {
						case 1 :
							// Truffle.g:1786:35: COMMA t+= test[$expr::ctype]
							{
							COMMA240=(Token)match(input,COMMA,FOLLOW_COMMA_in_listmaker6547); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA240_tree = (PNode)adaptor.create(COMMA240);
							adaptor.addChild(root_0, COMMA240_tree);
							}

							pushFollow(FOLLOW_test_in_listmaker6551);
							t=test(expr_stack.peek().ctype);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

							if (list_t==null) list_t=new ArrayList<Object>();
							list_t.add(t.getTree());
							}
							break;

						default :
							break loop114;
						}
					}

					if ( state.backtracking==0 ) {
					               etype = actions.makeList(lbrack, actions.castExprs(list_t), expr_stack.peek().ctype);
					           }
					}
					break;

			}

			// Truffle.g:1790:11: ( COMMA )?
			int alt116=2;
			int LA116_0 = input.LA(1);
			if ( (LA116_0==COMMA) ) {
				alt116=1;
			}
			switch (alt116) {
				case 1 :
					// Truffle.g:1790:12: COMMA
					{
					COMMA241=(Token)match(input,COMMA,FOLLOW_COMMA_in_listmaker6580); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA241_tree = (PNode)adaptor.create(COMMA241);
					adaptor.addChild(root_0, COMMA241_tree);
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = etype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "listmaker"


	public static class testlist_gexp_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "testlist_gexp"
	// Truffle.g:1794:1: testlist_gexp :t+= test[$expr::ctype] ( ( options {k=2; } :c1= COMMA t+= test[$expr::ctype] )* (c2= COMMA )? {...}?| -> test | ( comp_for[gens] ) ) ;
	public final TruffleParser.testlist_gexp_return testlist_gexp() throws RecognitionException {
		TruffleParser.testlist_gexp_return retval = new TruffleParser.testlist_gexp_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token c1=null;
		Token c2=null;
		List<Object> list_t=null;
		ParserRuleReturnScope comp_for242 =null;
		RuleReturnScope t = null;
		PNode c1_tree=null;
		PNode c2_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleSubtreeStream stream_test=new RewriteRuleSubtreeStream(adaptor,"rule test");
		RewriteRuleSubtreeStream stream_comp_for=new RewriteRuleSubtreeStream(adaptor,"rule comp_for");


		    PNode etype = null;
		    List gens = new ArrayList();
		    

		try {
			// Truffle.g:1805:5: (t+= test[$expr::ctype] ( ( options {k=2; } :c1= COMMA t+= test[$expr::ctype] )* (c2= COMMA )? {...}?| -> test | ( comp_for[gens] ) ) )
			// Truffle.g:1805:7: t+= test[$expr::ctype] ( ( options {k=2; } :c1= COMMA t+= test[$expr::ctype] )* (c2= COMMA )? {...}?| -> test | ( comp_for[gens] ) )
			{
			pushFollow(FOLLOW_test_in_testlist_gexp6612);
			t=test(expr_stack.peek().ctype);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_test.add(t.getTree());
			if (list_t==null) list_t=new ArrayList<Object>();
			list_t.add(t.getTree());
			// Truffle.g:1806:9: ( ( options {k=2; } :c1= COMMA t+= test[$expr::ctype] )* (c2= COMMA )? {...}?| -> test | ( comp_for[gens] ) )
			int alt119=3;
			switch ( input.LA(1) ) {
			case COMMA:
				{
				alt119=1;
				}
				break;
			case RPAREN:
				{
				int LA119_2 = input.LA(2);
				if ( (( c1 != null || c2 != null )) ) {
					alt119=1;
				}
				else if ( (true) ) {
					alt119=2;
				}

				}
				break;
			case FOR:
				{
				alt119=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 119, 0, input);
				throw nvae;
			}
			switch (alt119) {
				case 1 :
					// Truffle.g:1806:11: ( options {k=2; } :c1= COMMA t+= test[$expr::ctype] )* (c2= COMMA )? {...}?
					{
					// Truffle.g:1806:11: ( options {k=2; } :c1= COMMA t+= test[$expr::ctype] )*
					loop117:
					while (true) {
						int alt117=2;
						int LA117_0 = input.LA(1);
						if ( (LA117_0==COMMA) ) {
							int LA117_1 = input.LA(2);
							if ( (LA117_1==BACKQUOTE||LA117_1==COMPLEX||LA117_1==FALSE||LA117_1==FLOAT||LA117_1==INT||(LA117_1 >= LAMBDA && LA117_1 <= LCURLY)||(LA117_1 >= LPAREN && LA117_1 <= MINUS)||LA117_1==NAME||LA117_1==NONE||LA117_1==NOT||LA117_1==PLUS||LA117_1==PRINT||(LA117_1 >= STRING && LA117_1 <= TILDE)||LA117_1==TRUE) ) {
								alt117=1;
							}

						}

						switch (alt117) {
						case 1 :
							// Truffle.g:1806:28: c1= COMMA t+= test[$expr::ctype]
							{
							c1=(Token)match(input,COMMA,FOLLOW_COMMA_in_testlist_gexp6636); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_COMMA.add(c1);

							pushFollow(FOLLOW_test_in_testlist_gexp6640);
							t=test(expr_stack.peek().ctype);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_test.add(t.getTree());
							if (list_t==null) list_t=new ArrayList<Object>();
							list_t.add(t.getTree());
							}
							break;

						default :
							break loop117;
						}
					}

					// Truffle.g:1806:61: (c2= COMMA )?
					int alt118=2;
					int LA118_0 = input.LA(1);
					if ( (LA118_0==COMMA) ) {
						alt118=1;
					}
					switch (alt118) {
						case 1 :
							// Truffle.g:1806:62: c2= COMMA
							{
							c2=(Token)match(input,COMMA,FOLLOW_COMMA_in_testlist_gexp6648); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_COMMA.add(c2);

							}
							break;

					}

					if ( !(( c1 != null || c2 != null )) ) {
						if (state.backtracking>0) {state.failed=true; return retval;}
						throw new FailedPredicateException(input, "testlist_gexp", " $c1 != null || $c2 != null ");
					}
					if ( state.backtracking==0 ) {
					               etype = actions.makeTuple((retval.start), actions.castExprs(list_t), expr_stack.peek().ctype);
					           }
					}
					break;
				case 2 :
					// Truffle.g:1811:11: 
					{
					// AST REWRITE
					// elements: test
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1811:11: -> test
					{
						adaptor.addChild(root_0, stream_test.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// Truffle.g:1812:11: ( comp_for[gens] )
					{
					// Truffle.g:1812:11: ( comp_for[gens] )
					// Truffle.g:1812:12: comp_for[gens]
					{
					if ( state.backtracking==0 ) {actions.beginScope();}
					pushFollow(FOLLOW_comp_for_in_testlist_gexp6704);
					comp_for242=comp_for(gens);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_comp_for.add(comp_for242.getTree());
					if ( state.backtracking==0 ) {
					               Collections.reverse(gens);
					               List<PComprehension> c = gens;
					               PNode e = actions.castExpr(list_t.get(0));
					//               if (e instanceof Context) {
					//                   ((Context)e).setContext(expr_contextType.Load);
					//               }
					               etype = actions.makeGeneratorExp((retval.start), e, c);
					               actions.endScope();
					           }
					}

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (etype != null) {
			        retval.tree = etype;
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "testlist_gexp"


	public static class lambdef_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "lambdef"
	// Truffle.g:1828:1: lambdef : LAMBDA ( varargslist )? COLON test[expr_contextType.Load] ;
	public final TruffleParser.lambdef_return lambdef() throws RecognitionException {
		TruffleParser.lambdef_return retval = new TruffleParser.lambdef_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token LAMBDA243=null;
		Token COLON245=null;
		ParserRuleReturnScope varargslist244 =null;
		ParserRuleReturnScope test246 =null;

		PNode LAMBDA243_tree=null;
		PNode COLON245_tree=null;


		    PNode etype = null;

		try {
			// Truffle.g:1835:5: ( LAMBDA ( varargslist )? COLON test[expr_contextType.Load] )
			// Truffle.g:1835:7: LAMBDA ( varargslist )? COLON test[expr_contextType.Load]
			{
			root_0 = (PNode)adaptor.nil();


			LAMBDA243=(Token)match(input,LAMBDA,FOLLOW_LAMBDA_in_lambdef6768); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			LAMBDA243_tree = (PNode)adaptor.create(LAMBDA243);
			adaptor.addChild(root_0, LAMBDA243_tree);
			}

			// Truffle.g:1835:14: ( varargslist )?
			int alt120=2;
			int LA120_0 = input.LA(1);
			if ( (LA120_0==DOUBLESTAR||LA120_0==LPAREN||LA120_0==NAME||LA120_0==STAR) ) {
				alt120=1;
			}
			switch (alt120) {
				case 1 :
					// Truffle.g:1835:15: varargslist
					{
					pushFollow(FOLLOW_varargslist_in_lambdef6771);
					varargslist244=varargslist();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, varargslist244.getTree());

					}
					break;

			}

			COLON245=(Token)match(input,COLON,FOLLOW_COLON_in_lambdef6775); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON245_tree = (PNode)adaptor.create(COLON245);
			adaptor.addChild(root_0, COLON245_tree);
			}

			pushFollow(FOLLOW_test_in_lambdef6777);
			test246=test(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, test246.getTree());

			if ( state.backtracking==0 ) {
			//          arguments a = (varargslist244!=null?((TruffleParser.varargslist_return)varargslist244).args:null);
			//          if (a == null) {
			//              a = new arguments(LAMBDA243, new ArrayList<PNode>(), null, null, new ArrayList<PNode>());
			//          }
			          etype = actions.makeLambda(LAMBDA243, (varargslist244!=null?((TruffleParser.varargslist_return)varargslist244).args:null), actions.castExpr((test246!=null?((PNode)test246.getTree()):null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.tree = etype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "lambdef"


	public static class trailer_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "trailer"
	// Truffle.g:1846:1: trailer[Token begin, PNode ptree] : ( LPAREN ( arglist |) RPAREN | LBRACK subscriptlist[$begin] RBRACK | DOT attr );
	public final TruffleParser.trailer_return trailer(Token begin, PNode ptree) throws RecognitionException {
		TruffleParser.trailer_return retval = new TruffleParser.trailer_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token LPAREN247=null;
		Token RPAREN249=null;
		Token LBRACK250=null;
		Token RBRACK252=null;
		Token DOT253=null;
		ParserRuleReturnScope arglist248 =null;
		ParserRuleReturnScope subscriptlist251 =null;
		ParserRuleReturnScope attr254 =null;

		PNode LPAREN247_tree=null;
		PNode RPAREN249_tree=null;
		PNode LBRACK250_tree=null;
		PNode RBRACK252_tree=null;
		PNode DOT253_tree=null;


		    PNode etype = null;

		try {
			// Truffle.g:1855:5: ( LPAREN ( arglist |) RPAREN | LBRACK subscriptlist[$begin] RBRACK | DOT attr )
			int alt122=3;
			switch ( input.LA(1) ) {
			case LPAREN:
				{
				alt122=1;
				}
				break;
			case LBRACK:
				{
				alt122=2;
				}
				break;
			case DOT:
				{
				alt122=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 122, 0, input);
				throw nvae;
			}
			switch (alt122) {
				case 1 :
					// Truffle.g:1855:7: LPAREN ( arglist |) RPAREN
					{
					root_0 = (PNode)adaptor.nil();


					LPAREN247=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_trailer6816); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LPAREN247_tree = (PNode)adaptor.create(LPAREN247);
					adaptor.addChild(root_0, LPAREN247_tree);
					}

					// Truffle.g:1856:7: ( arglist |)
					int alt121=2;
					int LA121_0 = input.LA(1);
					if ( (LA121_0==BACKQUOTE||(LA121_0 >= LBRACK && LA121_0 <= LCURLY)||(LA121_0 >= LPAREN && LA121_0 <= MINUS)||LA121_0==NAME||LA121_0==NOT||LA121_0==PLUS||LA121_0==TILDE) ) {
						alt121=1;
					}
					else if ( (LA121_0==PRINT) && ((printFunction))) {
						alt121=1;
					}
					else if ( (LA121_0==COMPLEX||LA121_0==DOUBLESTAR||LA121_0==FALSE||LA121_0==FLOAT||LA121_0==INT||LA121_0==LAMBDA||LA121_0==NONE||LA121_0==STAR||LA121_0==STRING||LA121_0==TRUE) ) {
						alt121=1;
					}
					else if ( (LA121_0==RPAREN) ) {
						alt121=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 121, 0, input);
						throw nvae;
					}

					switch (alt121) {
						case 1 :
							// Truffle.g:1856:8: arglist
							{
							pushFollow(FOLLOW_arglist_in_trailer6825);
							arglist248=arglist();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arglist248.getTree());

							if ( state.backtracking==0 ) {
							//           etype = new Call(begin, actions.castExpr(ptree), actions.castExprs((arglist248!=null?((TruffleParser.arglist_return)arglist248).args:null)), actions.makeKeywords((arglist248!=null?((TruffleParser.arglist_return)arglist248).keywords:null)), (arglist248!=null?((TruffleParser.arglist_return)arglist248).starargs:null), (arglist248!=null?((TruffleParser.arglist_return)arglist248).kwargs:null));
							           PNode func = actions.castExpr(ptree);
							           etype = actions.makeCall(begin, func, (arglist248!=null?((TruffleParser.arglist_return)arglist248).args:null), (arglist248!=null?((TruffleParser.arglist_return)arglist248).keywords:null), (arglist248!=null?((TruffleParser.arglist_return)arglist248).starargs:null), (arglist248!=null?((TruffleParser.arglist_return)arglist248).kwargs:null));
							           
							       }
							}
							break;
						case 2 :
							// Truffle.g:1864:8: 
							{
							if ( state.backtracking==0 ) {
							//           etype = new Call(begin, actions.castExpr(ptree), new ArrayList<PNode>(), new ArrayList<keyword>(), null, null);
							           etype = actions.makeCall(begin, actions.castExpr(ptree),  null, null,  null, null);
							       }
							}
							break;

					}

					RPAREN249=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_trailer6867); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					RPAREN249_tree = (PNode)adaptor.create(RPAREN249);
					adaptor.addChild(root_0, RPAREN249_tree);
					}

					}
					break;
				case 2 :
					// Truffle.g:1870:7: LBRACK subscriptlist[$begin] RBRACK
					{
					root_0 = (PNode)adaptor.nil();


					LBRACK250=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_trailer6875); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LBRACK250_tree = (PNode)adaptor.create(LBRACK250);
					adaptor.addChild(root_0, LBRACK250_tree);
					}

					pushFollow(FOLLOW_subscriptlist_in_trailer6877);
					subscriptlist251=subscriptlist(begin);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subscriptlist251.getTree());

					RBRACK252=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_trailer6880); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					RBRACK252_tree = (PNode)adaptor.create(RBRACK252);
					adaptor.addChild(root_0, RBRACK252_tree);
					}

					if ( state.backtracking==0 ) {
					          etype = actions.makeSubscript(begin, actions.castExpr(ptree), actions.castSlice((subscriptlist251!=null?((PNode)subscriptlist251.getTree()):null)), expr_stack.peek().ctype);
					      }
					}
					break;
				case 3 :
					// Truffle.g:1874:7: DOT attr
					{
					root_0 = (PNode)adaptor.nil();


					DOT253=(Token)match(input,DOT,FOLLOW_DOT_in_trailer6896); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOT253_tree = (PNode)adaptor.create(DOT253);
					adaptor.addChild(root_0, DOT253_tree);
					}

					pushFollow(FOLLOW_attr_in_trailer6898);
					attr254=attr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, attr254.getTree());

					if ( state.backtracking==0 ) {
					          PNode name = actions.makeName((attr254!=null?((PNode)attr254.getTree()):null), (attr254!=null?input.toString(attr254.start,attr254.stop):null), expr_contextType.Load);
					          etype = actions.makeAttribute(begin, actions.castExpr(ptree), name, expr_stack.peek().ctype);
					          ptree = etype;
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (etype != null) {
			        retval.tree = etype;
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "trailer"


	public static class subscriptlist_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "subscriptlist"
	// Truffle.g:1883:1: subscriptlist[Token begin] :sub+= subscript ( options {greedy=true; } :c1= COMMA sub+= subscript )* (c2= COMMA )? ;
	public final TruffleParser.subscriptlist_return subscriptlist(Token begin) throws RecognitionException {
		TruffleParser.subscriptlist_return retval = new TruffleParser.subscriptlist_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token c1=null;
		Token c2=null;
		List<Object> list_sub=null;
		RuleReturnScope sub = null;
		PNode c1_tree=null;
		PNode c2_tree=null;


		    PNode sltype = null;

		try {
			// Truffle.g:1890:5: (sub+= subscript ( options {greedy=true; } :c1= COMMA sub+= subscript )* (c2= COMMA )? )
			// Truffle.g:1890:7: sub+= subscript ( options {greedy=true; } :c1= COMMA sub+= subscript )* (c2= COMMA )?
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_subscript_in_subscriptlist6937);
			sub=subscript();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, sub.getTree());

			if (list_sub==null) list_sub=new ArrayList<Object>();
			list_sub.add(sub.getTree());
			// Truffle.g:1890:22: ( options {greedy=true; } :c1= COMMA sub+= subscript )*
			loop123:
			while (true) {
				int alt123=2;
				int LA123_0 = input.LA(1);
				if ( (LA123_0==COMMA) ) {
					int LA123_1 = input.LA(2);
					if ( (LA123_1==BACKQUOTE||LA123_1==COLON||LA123_1==COMPLEX||LA123_1==DOT||LA123_1==FALSE||LA123_1==FLOAT||LA123_1==INT||(LA123_1 >= LAMBDA && LA123_1 <= LCURLY)||(LA123_1 >= LPAREN && LA123_1 <= MINUS)||LA123_1==NAME||LA123_1==NONE||LA123_1==NOT||LA123_1==PLUS||LA123_1==PRINT||(LA123_1 >= STRING && LA123_1 <= TILDE)||LA123_1==TRUE) ) {
						alt123=1;
					}

				}

				switch (alt123) {
				case 1 :
					// Truffle.g:1890:46: c1= COMMA sub+= subscript
					{
					c1=(Token)match(input,COMMA,FOLLOW_COMMA_in_subscriptlist6949); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					c1_tree = (PNode)adaptor.create(c1);
					adaptor.addChild(root_0, c1_tree);
					}

					pushFollow(FOLLOW_subscript_in_subscriptlist6953);
					sub=subscript();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, sub.getTree());

					if (list_sub==null) list_sub=new ArrayList<Object>();
					list_sub.add(sub.getTree());
					}
					break;

				default :
					break loop123;
				}
			}

			// Truffle.g:1890:72: (c2= COMMA )?
			int alt124=2;
			int LA124_0 = input.LA(1);
			if ( (LA124_0==COMMA) ) {
				alt124=1;
			}
			switch (alt124) {
				case 1 :
					// Truffle.g:1890:73: c2= COMMA
					{
					c2=(Token)match(input,COMMA,FOLLOW_COMMA_in_subscriptlist6960); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					c2_tree = (PNode)adaptor.create(c2);
					adaptor.addChild(root_0, c2_tree);
					}

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          sltype = actions.makeSliceType(begin, c1, c2, list_sub);
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = sltype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "subscriptlist"


	public static class subscript_return extends ParserRuleReturnScope {
		public PNode sltype;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "subscript"
	// Truffle.g:1897:1: subscript returns [PNode sltype] : (d1= DOT DOT DOT | ( test[null] COLON )=>lower= test[expr_contextType.Load] (c1= COLON (upper1= test[expr_contextType.Load] )? ( sliceop )? )? | ( COLON )=>c2= COLON (upper2= test[expr_contextType.Load] )? ( sliceop )? | test[expr_contextType.Load] );
	public final TruffleParser.subscript_return subscript() throws RecognitionException {
		TruffleParser.subscript_return retval = new TruffleParser.subscript_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token d1=null;
		Token c1=null;
		Token c2=null;
		Token DOT255=null;
		Token DOT256=null;
		ParserRuleReturnScope lower =null;
		ParserRuleReturnScope upper1 =null;
		ParserRuleReturnScope upper2 =null;
		ParserRuleReturnScope sliceop257 =null;
		ParserRuleReturnScope sliceop258 =null;
		ParserRuleReturnScope test259 =null;

		PNode d1_tree=null;
		PNode c1_tree=null;
		PNode c2_tree=null;
		PNode DOT255_tree=null;
		PNode DOT256_tree=null;

		try {
			// Truffle.g:1902:5: (d1= DOT DOT DOT | ( test[null] COLON )=>lower= test[expr_contextType.Load] (c1= COLON (upper1= test[expr_contextType.Load] )? ( sliceop )? )? | ( COLON )=>c2= COLON (upper2= test[expr_contextType.Load] )? ( sliceop )? | test[expr_contextType.Load] )
			int alt130=4;
			int LA130_0 = input.LA(1);
			if ( (LA130_0==DOT) ) {
				alt130=1;
			}
			else if ( (LA130_0==NOT) ) {
				int LA130_2 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==PLUS) ) {
				int LA130_3 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==MINUS) ) {
				int LA130_4 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==TILDE) ) {
				int LA130_5 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==LPAREN) ) {
				int LA130_6 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==LBRACK) ) {
				int LA130_7 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==LCURLY) ) {
				int LA130_8 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==BACKQUOTE) ) {
				int LA130_9 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==NAME) ) {
				int LA130_10 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==PRINT) && ((printFunction))) {
				int LA130_11 = input.LA(2);
				if ( (((printFunction)&&synpred8_Truffle())) ) {
					alt130=2;
				}
				else if ( ((printFunction)) ) {
					alt130=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 130, 11, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA130_0==NONE) ) {
				int LA130_12 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==TRUE) ) {
				int LA130_13 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==FALSE) ) {
				int LA130_14 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==INT) ) {
				int LA130_15 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==FLOAT) ) {
				int LA130_16 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==COMPLEX) ) {
				int LA130_17 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==STRING) ) {
				int LA130_18 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==LAMBDA) ) {
				int LA130_19 = input.LA(2);
				if ( (synpred8_Truffle()) ) {
					alt130=2;
				}
				else if ( (true) ) {
					alt130=4;
				}

			}
			else if ( (LA130_0==COLON) && (synpred9_Truffle())) {
				alt130=3;
			}

			switch (alt130) {
				case 1 :
					// Truffle.g:1902:7: d1= DOT DOT DOT
					{
					root_0 = (PNode)adaptor.nil();


					d1=(Token)match(input,DOT,FOLLOW_DOT_in_subscript7003); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					d1_tree = (PNode)adaptor.create(d1);
					adaptor.addChild(root_0, d1_tree);
					}

					DOT255=(Token)match(input,DOT,FOLLOW_DOT_in_subscript7005); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOT255_tree = (PNode)adaptor.create(DOT255);
					adaptor.addChild(root_0, DOT255_tree);
					}

					DOT256=(Token)match(input,DOT,FOLLOW_DOT_in_subscript7007); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOT256_tree = (PNode)adaptor.create(DOT256);
					adaptor.addChild(root_0, DOT256_tree);
					}

					if ( state.backtracking==0 ) {
					          retval.sltype = actions.makeEllipsis(d1);
					      }
					}
					break;
				case 2 :
					// Truffle.g:1906:7: ( test[null] COLON )=>lower= test[expr_contextType.Load] (c1= COLON (upper1= test[expr_contextType.Load] )? ( sliceop )? )?
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_test_in_subscript7037);
					lower=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, lower.getTree());

					// Truffle.g:1907:41: (c1= COLON (upper1= test[expr_contextType.Load] )? ( sliceop )? )?
					int alt127=2;
					int LA127_0 = input.LA(1);
					if ( (LA127_0==COLON) ) {
						alt127=1;
					}
					switch (alt127) {
						case 1 :
							// Truffle.g:1907:42: c1= COLON (upper1= test[expr_contextType.Load] )? ( sliceop )?
							{
							c1=(Token)match(input,COLON,FOLLOW_COLON_in_subscript7043); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							c1_tree = (PNode)adaptor.create(c1);
							adaptor.addChild(root_0, c1_tree);
							}

							// Truffle.g:1907:51: (upper1= test[expr_contextType.Load] )?
							int alt125=2;
							int LA125_0 = input.LA(1);
							if ( (LA125_0==BACKQUOTE||(LA125_0 >= LBRACK && LA125_0 <= LCURLY)||(LA125_0 >= LPAREN && LA125_0 <= MINUS)||LA125_0==NAME||LA125_0==NOT||LA125_0==PLUS||LA125_0==TILDE) ) {
								alt125=1;
							}
							else if ( (LA125_0==PRINT) && ((printFunction))) {
								alt125=1;
							}
							else if ( (LA125_0==COMPLEX||LA125_0==FALSE||LA125_0==FLOAT||LA125_0==INT||LA125_0==LAMBDA||LA125_0==NONE||LA125_0==STRING||LA125_0==TRUE) ) {
								alt125=1;
							}
							switch (alt125) {
								case 1 :
									// Truffle.g:1907:52: upper1= test[expr_contextType.Load]
									{
									pushFollow(FOLLOW_test_in_subscript7048);
									upper1=test(expr_contextType.Load);
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, upper1.getTree());

									}
									break;

							}

							// Truffle.g:1907:89: ( sliceop )?
							int alt126=2;
							int LA126_0 = input.LA(1);
							if ( (LA126_0==COLON) ) {
								alt126=1;
							}
							switch (alt126) {
								case 1 :
									// Truffle.g:1907:90: sliceop
									{
									pushFollow(FOLLOW_sliceop_in_subscript7054);
									sliceop257=sliceop();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, sliceop257.getTree());

									}
									break;

							}

							}
							break;

					}

					if ( state.backtracking==0 ) {
					          retval.sltype = actions.makeSubscript((lower!=null?((PNode)lower.getTree()):null), c1, (upper1!=null?((PNode)upper1.getTree()):null), (sliceop257!=null?((PNode)sliceop257.getTree()):null));
					      }
					}
					break;
				case 3 :
					// Truffle.g:1911:7: ( COLON )=>c2= COLON (upper2= test[expr_contextType.Load] )? ( sliceop )?
					{
					root_0 = (PNode)adaptor.nil();


					c2=(Token)match(input,COLON,FOLLOW_COLON_in_subscript7085); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					c2_tree = (PNode)adaptor.create(c2);
					adaptor.addChild(root_0, c2_tree);
					}

					// Truffle.g:1912:16: (upper2= test[expr_contextType.Load] )?
					int alt128=2;
					int LA128_0 = input.LA(1);
					if ( (LA128_0==BACKQUOTE||(LA128_0 >= LBRACK && LA128_0 <= LCURLY)||(LA128_0 >= LPAREN && LA128_0 <= MINUS)||LA128_0==NAME||LA128_0==NOT||LA128_0==PLUS||LA128_0==TILDE) ) {
						alt128=1;
					}
					else if ( (LA128_0==PRINT) && ((printFunction))) {
						alt128=1;
					}
					else if ( (LA128_0==COMPLEX||LA128_0==FALSE||LA128_0==FLOAT||LA128_0==INT||LA128_0==LAMBDA||LA128_0==NONE||LA128_0==STRING||LA128_0==TRUE) ) {
						alt128=1;
					}
					switch (alt128) {
						case 1 :
							// Truffle.g:1912:17: upper2= test[expr_contextType.Load]
							{
							pushFollow(FOLLOW_test_in_subscript7090);
							upper2=test(expr_contextType.Load);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, upper2.getTree());

							}
							break;

					}

					// Truffle.g:1912:54: ( sliceop )?
					int alt129=2;
					int LA129_0 = input.LA(1);
					if ( (LA129_0==COLON) ) {
						alt129=1;
					}
					switch (alt129) {
						case 1 :
							// Truffle.g:1912:55: sliceop
							{
							pushFollow(FOLLOW_sliceop_in_subscript7096);
							sliceop258=sliceop();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, sliceop258.getTree());

							}
							break;

					}

					if ( state.backtracking==0 ) {
					          retval.sltype = actions.makeSubscript(null, c2, (upper2!=null?((PNode)upper2.getTree()):null), (sliceop258!=null?((PNode)sliceop258.getTree()):null));
					      }
					}
					break;
				case 4 :
					// Truffle.g:1916:7: test[expr_contextType.Load]
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_test_in_subscript7114);
					test259=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, test259.getTree());

					if ( state.backtracking==0 ) {
					          retval.sltype = actions.makeIndex((test259!=null?(test259.start):null), actions.castExpr((test259!=null?((PNode)test259.getTree()):null)));
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    retval.tree = retval.sltype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "subscript"


	public static class sliceop_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "sliceop"
	// Truffle.g:1923:1: sliceop : COLON ( test[expr_contextType.Load] -> test |) ;
	public final TruffleParser.sliceop_return sliceop() throws RecognitionException {
		TruffleParser.sliceop_return retval = new TruffleParser.sliceop_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token COLON260=null;
		ParserRuleReturnScope test261 =null;

		PNode COLON260_tree=null;
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleSubtreeStream stream_test=new RewriteRuleSubtreeStream(adaptor,"rule test");


		    PNode etype = null;

		try {
			// Truffle.g:1932:5: ( COLON ( test[expr_contextType.Load] -> test |) )
			// Truffle.g:1932:7: COLON ( test[expr_contextType.Load] -> test |)
			{
			COLON260=(Token)match(input,COLON,FOLLOW_COLON_in_sliceop7151); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_COLON.add(COLON260);

			// Truffle.g:1933:6: ( test[expr_contextType.Load] -> test |)
			int alt131=2;
			int LA131_0 = input.LA(1);
			if ( (LA131_0==BACKQUOTE||(LA131_0 >= LBRACK && LA131_0 <= LCURLY)||(LA131_0 >= LPAREN && LA131_0 <= MINUS)||LA131_0==NAME||LA131_0==NOT||LA131_0==PLUS||LA131_0==TILDE) ) {
				alt131=1;
			}
			else if ( (LA131_0==PRINT) && ((printFunction))) {
				alt131=1;
			}
			else if ( (LA131_0==COMPLEX||LA131_0==FALSE||LA131_0==FLOAT||LA131_0==INT||LA131_0==LAMBDA||LA131_0==NONE||LA131_0==STRING||LA131_0==TRUE) ) {
				alt131=1;
			}
			else if ( (LA131_0==COMMA||LA131_0==RBRACK) ) {
				alt131=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 131, 0, input);
				throw nvae;
			}

			switch (alt131) {
				case 1 :
					// Truffle.g:1933:7: test[expr_contextType.Load]
					{
					pushFollow(FOLLOW_test_in_sliceop7159);
					test261=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_test.add(test261.getTree());
					// AST REWRITE
					// elements: test
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (PNode)adaptor.nil();
					// 1934:5: -> test
					{
						adaptor.addChild(root_0, stream_test.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// Truffle.g:1936:8: 
					{
					if ( state.backtracking==0 ) {
					           etype = actions.makeName(COLON260, "None", expr_contextType.Load);
					       }
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (etype != null) {
			        retval.tree = etype;
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "sliceop"


	public static class exprlist_return extends ParserRuleReturnScope {
		public PNode etype;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "exprlist"
	// Truffle.g:1943:1: exprlist[expr_contextType ctype] returns [PNode etype] : ( ( expr[null] COMMA )=>e+= expr[ctype] ( options {k=2; } : COMMA e+= expr[ctype] )* ( COMMA )? | expr[ctype] );
	public final TruffleParser.exprlist_return exprlist(expr_contextType ctype) throws RecognitionException {
		TruffleParser.exprlist_return retval = new TruffleParser.exprlist_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token COMMA262=null;
		Token COMMA263=null;
		List<Object> list_e=null;
		ParserRuleReturnScope expr264 =null;
		RuleReturnScope e = null;
		PNode COMMA262_tree=null;
		PNode COMMA263_tree=null;

		try {
			// Truffle.g:1945:5: ( ( expr[null] COMMA )=>e+= expr[ctype] ( options {k=2; } : COMMA e+= expr[ctype] )* ( COMMA )? | expr[ctype] )
			int alt134=2;
			int LA134_0 = input.LA(1);
			if ( (LA134_0==PLUS) ) {
				int LA134_1 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==MINUS) ) {
				int LA134_2 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==TILDE) ) {
				int LA134_3 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==LPAREN) ) {
				int LA134_4 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==LBRACK) ) {
				int LA134_5 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==LCURLY) ) {
				int LA134_6 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==BACKQUOTE) ) {
				int LA134_7 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==NAME) ) {
				int LA134_8 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==PRINT) && ((printFunction))) {
				int LA134_9 = input.LA(2);
				if ( (((printFunction)&&synpred10_Truffle())) ) {
					alt134=1;
				}
				else if ( ((printFunction)) ) {
					alt134=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 134, 9, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA134_0==NONE) ) {
				int LA134_10 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==TRUE) ) {
				int LA134_11 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==FALSE) ) {
				int LA134_12 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==INT) ) {
				int LA134_13 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==FLOAT) ) {
				int LA134_14 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==COMPLEX) ) {
				int LA134_15 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}
			else if ( (LA134_0==STRING) ) {
				int LA134_16 = input.LA(2);
				if ( (synpred10_Truffle()) ) {
					alt134=1;
				}
				else if ( (true) ) {
					alt134=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 134, 0, input);
				throw nvae;
			}

			switch (alt134) {
				case 1 :
					// Truffle.g:1945:7: ( expr[null] COMMA )=>e+= expr[ctype] ( options {k=2; } : COMMA e+= expr[ctype] )* ( COMMA )?
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_expr_in_exprlist7230);
					e=expr(ctype);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, e.getTree());

					if (list_e==null) list_e=new ArrayList<Object>();
					list_e.add(e.getTree());
					// Truffle.g:1945:44: ( options {k=2; } : COMMA e+= expr[ctype] )*
					loop132:
					while (true) {
						int alt132=2;
						int LA132_0 = input.LA(1);
						if ( (LA132_0==COMMA) ) {
							int LA132_1 = input.LA(2);
							if ( (LA132_1==BACKQUOTE||LA132_1==COMPLEX||LA132_1==FALSE||LA132_1==FLOAT||LA132_1==INT||(LA132_1 >= LBRACK && LA132_1 <= LCURLY)||(LA132_1 >= LPAREN && LA132_1 <= MINUS)||LA132_1==NAME||LA132_1==NONE||LA132_1==PLUS||LA132_1==PRINT||(LA132_1 >= STRING && LA132_1 <= TILDE)||LA132_1==TRUE) ) {
								alt132=1;
							}

						}

						switch (alt132) {
						case 1 :
							// Truffle.g:1945:61: COMMA e+= expr[ctype]
							{
							COMMA262=(Token)match(input,COMMA,FOLLOW_COMMA_in_exprlist7242); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA262_tree = (PNode)adaptor.create(COMMA262);
							adaptor.addChild(root_0, COMMA262_tree);
							}

							pushFollow(FOLLOW_expr_in_exprlist7246);
							e=expr(ctype);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, e.getTree());

							if (list_e==null) list_e=new ArrayList<Object>();
							list_e.add(e.getTree());
							}
							break;

						default :
							break loop132;
						}
					}

					// Truffle.g:1945:84: ( COMMA )?
					int alt133=2;
					int LA133_0 = input.LA(1);
					if ( (LA133_0==COMMA) ) {
						alt133=1;
					}
					switch (alt133) {
						case 1 :
							// Truffle.g:1945:85: COMMA
							{
							COMMA263=(Token)match(input,COMMA,FOLLOW_COMMA_in_exprlist7252); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA263_tree = (PNode)adaptor.create(COMMA263);
							adaptor.addChild(root_0, COMMA263_tree);
							}

							}
							break;

					}

					if ( state.backtracking==0 ) {
					           retval.etype = actions.makeTuple((retval.start), actions.castExprs(list_e), ctype);
					           retval.etype = actions.recuFixWriteLocalSlots(retval.etype,0);
					       }
					}
					break;
				case 2 :
					// Truffle.g:1950:7: expr[ctype]
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_expr_in_exprlist7271);
					expr264=expr(ctype);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, expr264.getTree());

					if ( state.backtracking==0 ) {
					        retval.etype = actions.castExpr((expr264!=null?((PNode)expr264.getTree()):null));
					        retval.etype = actions.recuFixWriteLocalSlots(retval.etype,0);
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "exprlist"


	public static class del_list_return extends ParserRuleReturnScope {
		public List<PNode> etypes;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "del_list"
	// Truffle.g:1959:1: del_list returns [List<PNode> etypes] :e+= expr[expr_contextType.Del] ( options {k=2; } : COMMA e+= expr[expr_contextType.Del] )* ( COMMA )? ;
	public final TruffleParser.del_list_return del_list() throws RecognitionException {
		TruffleParser.del_list_return retval = new TruffleParser.del_list_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token COMMA265=null;
		Token COMMA266=null;
		List<Object> list_e=null;
		RuleReturnScope e = null;
		PNode COMMA265_tree=null;
		PNode COMMA266_tree=null;

		try {
			// Truffle.g:1961:5: (e+= expr[expr_contextType.Del] ( options {k=2; } : COMMA e+= expr[expr_contextType.Del] )* ( COMMA )? )
			// Truffle.g:1961:7: e+= expr[expr_contextType.Del] ( options {k=2; } : COMMA e+= expr[expr_contextType.Del] )* ( COMMA )?
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_expr_in_del_list7309);
			e=expr(expr_contextType.Del);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, e.getTree());

			if (list_e==null) list_e=new ArrayList<Object>();
			list_e.add(e.getTree());
			// Truffle.g:1961:37: ( options {k=2; } : COMMA e+= expr[expr_contextType.Del] )*
			loop135:
			while (true) {
				int alt135=2;
				int LA135_0 = input.LA(1);
				if ( (LA135_0==COMMA) ) {
					int LA135_1 = input.LA(2);
					if ( (LA135_1==BACKQUOTE||LA135_1==COMPLEX||LA135_1==FALSE||LA135_1==FLOAT||LA135_1==INT||(LA135_1 >= LBRACK && LA135_1 <= LCURLY)||(LA135_1 >= LPAREN && LA135_1 <= MINUS)||LA135_1==NAME||LA135_1==NONE||LA135_1==PLUS||LA135_1==PRINT||(LA135_1 >= STRING && LA135_1 <= TILDE)||LA135_1==TRUE) ) {
						alt135=1;
					}

				}

				switch (alt135) {
				case 1 :
					// Truffle.g:1961:54: COMMA e+= expr[expr_contextType.Del]
					{
					COMMA265=(Token)match(input,COMMA,FOLLOW_COMMA_in_del_list7321); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA265_tree = (PNode)adaptor.create(COMMA265);
					adaptor.addChild(root_0, COMMA265_tree);
					}

					pushFollow(FOLLOW_expr_in_del_list7325);
					e=expr(expr_contextType.Del);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, e.getTree());

					if (list_e==null) list_e=new ArrayList<Object>();
					list_e.add(e.getTree());
					}
					break;

				default :
					break loop135;
				}
			}

			// Truffle.g:1961:92: ( COMMA )?
			int alt136=2;
			int LA136_0 = input.LA(1);
			if ( (LA136_0==COMMA) ) {
				alt136=1;
			}
			switch (alt136) {
				case 1 :
					// Truffle.g:1961:93: COMMA
					{
					COMMA266=(Token)match(input,COMMA,FOLLOW_COMMA_in_del_list7331); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					COMMA266_tree = (PNode)adaptor.create(COMMA266);
					adaptor.addChild(root_0, COMMA266_tree);
					}

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          retval.etypes = actions.makeDeleteList(list_e);
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "del_list"


	public static class testlist_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "testlist"
	// Truffle.g:1968:1: testlist[expr_contextType ctype] : ( ( test[null] COMMA )=>t+= test[ctype] ( options {k=2; } : COMMA t+= test[ctype] )* ( COMMA )? | test[ctype] );
	public final TruffleParser.testlist_return testlist(expr_contextType ctype) throws RecognitionException {
		TruffleParser.testlist_return retval = new TruffleParser.testlist_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token COMMA267=null;
		Token COMMA268=null;
		List<Object> list_t=null;
		ParserRuleReturnScope test269 =null;
		RuleReturnScope t = null;
		PNode COMMA267_tree=null;
		PNode COMMA268_tree=null;


		    PNode etype = null;

		try {
			// Truffle.g:1977:5: ( ( test[null] COMMA )=>t+= test[ctype] ( options {k=2; } : COMMA t+= test[ctype] )* ( COMMA )? | test[ctype] )
			int alt139=2;
			int LA139_0 = input.LA(1);
			if ( (LA139_0==NOT) ) {
				int LA139_1 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==PLUS) ) {
				int LA139_2 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==MINUS) ) {
				int LA139_3 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==TILDE) ) {
				int LA139_4 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==LPAREN) ) {
				int LA139_5 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==LBRACK) ) {
				int LA139_6 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==LCURLY) ) {
				int LA139_7 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==BACKQUOTE) ) {
				int LA139_8 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==NAME) ) {
				int LA139_9 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==PRINT) && ((printFunction))) {
				int LA139_10 = input.LA(2);
				if ( (((printFunction)&&synpred11_Truffle())) ) {
					alt139=1;
				}
				else if ( ((printFunction)) ) {
					alt139=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 139, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA139_0==NONE) ) {
				int LA139_11 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==TRUE) ) {
				int LA139_12 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==FALSE) ) {
				int LA139_13 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==INT) ) {
				int LA139_14 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==FLOAT) ) {
				int LA139_15 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==COMPLEX) ) {
				int LA139_16 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==STRING) ) {
				int LA139_17 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}
			else if ( (LA139_0==LAMBDA) ) {
				int LA139_18 = input.LA(2);
				if ( (synpred11_Truffle()) ) {
					alt139=1;
				}
				else if ( (true) ) {
					alt139=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 139, 0, input);
				throw nvae;
			}

			switch (alt139) {
				case 1 :
					// Truffle.g:1977:7: ( test[null] COMMA )=>t+= test[ctype] ( options {k=2; } : COMMA t+= test[ctype] )* ( COMMA )?
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_test_in_testlist7384);
					t=test(ctype);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

					if (list_t==null) list_t=new ArrayList<Object>();
					list_t.add(t.getTree());
					// Truffle.g:1978:22: ( options {k=2; } : COMMA t+= test[ctype] )*
					loop137:
					while (true) {
						int alt137=2;
						alt137 = dfa137.predict(input);
						switch (alt137) {
						case 1 :
							// Truffle.g:1978:39: COMMA t+= test[ctype]
							{
							COMMA267=(Token)match(input,COMMA,FOLLOW_COMMA_in_testlist7396); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA267_tree = (PNode)adaptor.create(COMMA267);
							adaptor.addChild(root_0, COMMA267_tree);
							}

							pushFollow(FOLLOW_test_in_testlist7400);
							t=test(ctype);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, t.getTree());

							if (list_t==null) list_t=new ArrayList<Object>();
							list_t.add(t.getTree());
							}
							break;

						default :
							break loop137;
						}
					}

					// Truffle.g:1978:62: ( COMMA )?
					int alt138=2;
					int LA138_0 = input.LA(1);
					if ( (LA138_0==COMMA) ) {
						alt138=1;
					}
					switch (alt138) {
						case 1 :
							// Truffle.g:1978:63: COMMA
							{
							COMMA268=(Token)match(input,COMMA,FOLLOW_COMMA_in_testlist7406); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA268_tree = (PNode)adaptor.create(COMMA268);
							adaptor.addChild(root_0, COMMA268_tree);
							}

							}
							break;

					}

					if ( state.backtracking==0 ) {
					          etype = actions.makeTuple((retval.start), actions.castExprs(list_t), ctype);
					      }
					}
					break;
				case 2 :
					// Truffle.g:1982:7: test[ctype]
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_test_in_testlist7424);
					test269=test(ctype);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, test269.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (etype != null) {
			        retval.tree = etype;
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "testlist"


	public static class dictorsetmaker_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "dictorsetmaker"
	// Truffle.g:1989:1: dictorsetmaker[Token lcurly] :k+= test[expr_contextType.Load] ( ( COLON v+= test[expr_contextType.Load] ( comp_for[gens] | ( options {k=2; } : COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load] )* ) | ( COMMA k+= test[expr_contextType.Load] )* ) ( COMMA )? | comp_for[gens] ) ;
	public final TruffleParser.dictorsetmaker_return dictorsetmaker(Token lcurly) throws RecognitionException {
		TruffleParser.dictorsetmaker_return retval = new TruffleParser.dictorsetmaker_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token COLON270=null;
		Token COMMA272=null;
		Token COLON273=null;
		Token COMMA274=null;
		Token COMMA275=null;
		List<Object> list_k=null;
		List<Object> list_v=null;
		ParserRuleReturnScope comp_for271 =null;
		ParserRuleReturnScope comp_for276 =null;
		RuleReturnScope k = null;
		RuleReturnScope v = null;
		PNode COLON270_tree=null;
		PNode COMMA272_tree=null;
		PNode COLON273_tree=null;
		PNode COMMA274_tree=null;
		PNode COMMA275_tree=null;


		    List gens = new ArrayList();
		    PNode etype = null;

		try {
			// Truffle.g:1999:5: (k+= test[expr_contextType.Load] ( ( COLON v+= test[expr_contextType.Load] ( comp_for[gens] | ( options {k=2; } : COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load] )* ) | ( COMMA k+= test[expr_contextType.Load] )* ) ( COMMA )? | comp_for[gens] ) )
			// Truffle.g:1999:7: k+= test[expr_contextType.Load] ( ( COLON v+= test[expr_contextType.Load] ( comp_for[gens] | ( options {k=2; } : COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load] )* ) | ( COMMA k+= test[expr_contextType.Load] )* ) ( COMMA )? | comp_for[gens] )
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_test_in_dictorsetmaker7459);
			k=test(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, k.getTree());

			if (list_k==null) list_k=new ArrayList<Object>();
			list_k.add(k.getTree());
			// Truffle.g:2000:10: ( ( COLON v+= test[expr_contextType.Load] ( comp_for[gens] | ( options {k=2; } : COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load] )* ) | ( COMMA k+= test[expr_contextType.Load] )* ) ( COMMA )? | comp_for[gens] )
			int alt145=2;
			int LA145_0 = input.LA(1);
			if ( ((LA145_0 >= COLON && LA145_0 <= COMMA)||LA145_0==RCURLY) ) {
				alt145=1;
			}
			else if ( (LA145_0==FOR) ) {
				alt145=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 145, 0, input);
				throw nvae;
			}

			switch (alt145) {
				case 1 :
					// Truffle.g:2001:14: ( COLON v+= test[expr_contextType.Load] ( comp_for[gens] | ( options {k=2; } : COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load] )* ) | ( COMMA k+= test[expr_contextType.Load] )* ) ( COMMA )?
					{
					// Truffle.g:2001:14: ( COLON v+= test[expr_contextType.Load] ( comp_for[gens] | ( options {k=2; } : COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load] )* ) | ( COMMA k+= test[expr_contextType.Load] )* )
					int alt143=2;
					int LA143_0 = input.LA(1);
					if ( (LA143_0==COLON) ) {
						alt143=1;
					}
					else if ( (LA143_0==COMMA||LA143_0==RCURLY) ) {
						alt143=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 143, 0, input);
						throw nvae;
					}

					switch (alt143) {
						case 1 :
							// Truffle.g:2001:15: COLON v+= test[expr_contextType.Load] ( comp_for[gens] | ( options {k=2; } : COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load] )* )
							{
							COLON270=(Token)match(input,COLON,FOLLOW_COLON_in_dictorsetmaker7487); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COLON270_tree = (PNode)adaptor.create(COLON270);
							adaptor.addChild(root_0, COLON270_tree);
							}

							pushFollow(FOLLOW_test_in_dictorsetmaker7491);
							v=test(expr_contextType.Load);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, v.getTree());

							if (list_v==null) list_v=new ArrayList<Object>();
							list_v.add(v.getTree());
							// Truffle.g:2002:16: ( comp_for[gens] | ( options {k=2; } : COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load] )* )
							int alt141=2;
							int LA141_0 = input.LA(1);
							if ( (LA141_0==FOR) ) {
								alt141=1;
							}
							else if ( (LA141_0==COMMA||LA141_0==RCURLY) ) {
								alt141=2;
							}

							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								NoViableAltException nvae =
									new NoViableAltException("", 141, 0, input);
								throw nvae;
							}

							switch (alt141) {
								case 1 :
									// Truffle.g:2002:18: comp_for[gens]
									{
									pushFollow(FOLLOW_comp_for_in_dictorsetmaker7511);
									comp_for271=comp_for(gens);
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, comp_for271.getTree());

									if ( state.backtracking==0 ) {
									                     Collections.reverse(gens);
									                     List<PComprehension> c = gens;
									                     etype = actions.makeDictComp((retval.start), actions.castExpr(list_k.get(0)), actions.castExpr(list_v.get(0)), c);
									                 }
									}
									break;
								case 2 :
									// Truffle.g:2008:18: ( options {k=2; } : COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load] )*
									{
									// Truffle.g:2008:18: ( options {k=2; } : COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load] )*
									loop140:
									while (true) {
										int alt140=2;
										int LA140_0 = input.LA(1);
										if ( (LA140_0==COMMA) ) {
											int LA140_1 = input.LA(2);
											if ( (LA140_1==BACKQUOTE||LA140_1==COMPLEX||LA140_1==FALSE||LA140_1==FLOAT||LA140_1==INT||(LA140_1 >= LAMBDA && LA140_1 <= LCURLY)||(LA140_1 >= LPAREN && LA140_1 <= MINUS)||LA140_1==NAME||LA140_1==NONE||LA140_1==NOT||LA140_1==PLUS||LA140_1==PRINT||(LA140_1 >= STRING && LA140_1 <= TILDE)||LA140_1==TRUE) ) {
												alt140=1;
											}

										}

										switch (alt140) {
										case 1 :
											// Truffle.g:2008:34: COMMA k+= test[expr_contextType.Load] COLON v+= test[expr_contextType.Load]
											{
											COMMA272=(Token)match(input,COMMA,FOLLOW_COMMA_in_dictorsetmaker7558); if (state.failed) return retval;
											if ( state.backtracking==0 ) {
											COMMA272_tree = (PNode)adaptor.create(COMMA272);
											adaptor.addChild(root_0, COMMA272_tree);
											}

											pushFollow(FOLLOW_test_in_dictorsetmaker7562);
											k=test(expr_contextType.Load);
											state._fsp--;
											if (state.failed) return retval;
											if ( state.backtracking==0 ) adaptor.addChild(root_0, k.getTree());

											if (list_k==null) list_k=new ArrayList<Object>();
											list_k.add(k.getTree());
											COLON273=(Token)match(input,COLON,FOLLOW_COLON_in_dictorsetmaker7565); if (state.failed) return retval;
											if ( state.backtracking==0 ) {
											COLON273_tree = (PNode)adaptor.create(COLON273);
											adaptor.addChild(root_0, COLON273_tree);
											}

											pushFollow(FOLLOW_test_in_dictorsetmaker7569);
											v=test(expr_contextType.Load);
											state._fsp--;
											if (state.failed) return retval;
											if ( state.backtracking==0 ) adaptor.addChild(root_0, v.getTree());

											if (list_v==null) list_v=new ArrayList<Object>();
											list_v.add(v.getTree());
											}
											break;

										default :
											break loop140;
										}
									}

									if ( state.backtracking==0 ) {
									                     etype = actions.makeDict(lcurly, actions.castExprs(list_k), actions.castExprs(list_v));
									                 }
									}
									break;

							}

							}
							break;
						case 2 :
							// Truffle.g:2013:15: ( COMMA k+= test[expr_contextType.Load] )*
							{
							// Truffle.g:2013:15: ( COMMA k+= test[expr_contextType.Load] )*
							loop142:
							while (true) {
								int alt142=2;
								int LA142_0 = input.LA(1);
								if ( (LA142_0==COMMA) ) {
									int LA142_1 = input.LA(2);
									if ( (LA142_1==BACKQUOTE||LA142_1==COMPLEX||LA142_1==FALSE||LA142_1==FLOAT||LA142_1==INT||(LA142_1 >= LAMBDA && LA142_1 <= LCURLY)||(LA142_1 >= LPAREN && LA142_1 <= MINUS)||LA142_1==NAME||LA142_1==NONE||LA142_1==NOT||LA142_1==PLUS||LA142_1==PRINT||(LA142_1 >= STRING && LA142_1 <= TILDE)||LA142_1==TRUE) ) {
										alt142=1;
									}

								}

								switch (alt142) {
								case 1 :
									// Truffle.g:2013:16: COMMA k+= test[expr_contextType.Load]
									{
									COMMA274=(Token)match(input,COMMA,FOLLOW_COMMA_in_dictorsetmaker7625); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									COMMA274_tree = (PNode)adaptor.create(COMMA274);
									adaptor.addChild(root_0, COMMA274_tree);
									}

									pushFollow(FOLLOW_test_in_dictorsetmaker7629);
									k=test(expr_contextType.Load);
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, k.getTree());

									if (list_k==null) list_k=new ArrayList<Object>();
									list_k.add(k.getTree());
									}
									break;

								default :
									break loop142;
								}
							}

							if ( state.backtracking==0 ) {
							                  etype = actions.makeSet(lcurly, actions.castExprs(list_k));
							              }
							}
							break;

					}

					// Truffle.g:2018:14: ( COMMA )?
					int alt144=2;
					int LA144_0 = input.LA(1);
					if ( (LA144_0==COMMA) ) {
						alt144=1;
					}
					switch (alt144) {
						case 1 :
							// Truffle.g:2018:15: COMMA
							{
							COMMA275=(Token)match(input,COMMA,FOLLOW_COMMA_in_dictorsetmaker7679); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA275_tree = (PNode)adaptor.create(COMMA275);
							adaptor.addChild(root_0, COMMA275_tree);
							}

							}
							break;

					}

					}
					break;
				case 2 :
					// Truffle.g:2019:12: comp_for[gens]
					{
					pushFollow(FOLLOW_comp_for_in_dictorsetmaker7694);
					comp_for276=comp_for(gens);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comp_for276.getTree());

					if ( state.backtracking==0 ) {
					               Collections.reverse(gens);
					               List<PComprehension> c = gens;
					               PNode e = actions.castExpr(list_k.get(0));
					//               if (e instanceof Context) {
					//                   ((Context)e).setContext(expr_contextType.Load);
					//               }
					               etype = actions.makeSetComp(lcurly, actions.castExpr(list_k.get(0)), c);
					           }
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    if (etype != null) {
			        retval.tree = etype;
			    }
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "dictorsetmaker"


	public static class classdef_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "classdef"
	// Truffle.g:2033:1: classdef : ( decorators )? CLASS NAME ( LPAREN ( testlist[expr_contextType.Load] )? RPAREN )? COLON suite[false] ;
	public final TruffleParser.classdef_return classdef() throws RecognitionException {
		TruffleParser.classdef_return retval = new TruffleParser.classdef_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token CLASS278=null;
		Token NAME279=null;
		Token LPAREN280=null;
		Token RPAREN282=null;
		Token COLON283=null;
		ParserRuleReturnScope decorators277 =null;
		ParserRuleReturnScope testlist281 =null;
		ParserRuleReturnScope suite284 =null;

		PNode CLASS278_tree=null;
		PNode NAME279_tree=null;
		PNode LPAREN280_tree=null;
		PNode RPAREN282_tree=null;
		PNode COLON283_tree=null;


		    StatementNode stype = null;

		try {
			// Truffle.g:2040:5: ( ( decorators )? CLASS NAME ( LPAREN ( testlist[expr_contextType.Load] )? RPAREN )? COLON suite[false] )
			// Truffle.g:2040:7: ( decorators )? CLASS NAME ( LPAREN ( testlist[expr_contextType.Load] )? RPAREN )? COLON suite[false]
			{
			root_0 = (PNode)adaptor.nil();


			// Truffle.g:2040:7: ( decorators )?
			int alt146=2;
			int LA146_0 = input.LA(1);
			if ( (LA146_0==AT) ) {
				alt146=1;
			}
			switch (alt146) {
				case 1 :
					// Truffle.g:2040:7: decorators
					{
					pushFollow(FOLLOW_decorators_in_classdef7747);
					decorators277=decorators();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, decorators277.getTree());

					}
					break;

			}

			CLASS278=(Token)match(input,CLASS,FOLLOW_CLASS_in_classdef7750); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			CLASS278_tree = (PNode)adaptor.create(CLASS278);
			adaptor.addChild(root_0, CLASS278_tree);
			}

			NAME279=(Token)match(input,NAME,FOLLOW_NAME_in_classdef7752); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			NAME279_tree = (PNode)adaptor.create(NAME279);
			adaptor.addChild(root_0, NAME279_tree);
			}

			// Truffle.g:2040:30: ( LPAREN ( testlist[expr_contextType.Load] )? RPAREN )?
			int alt148=2;
			int LA148_0 = input.LA(1);
			if ( (LA148_0==LPAREN) ) {
				alt148=1;
			}
			switch (alt148) {
				case 1 :
					// Truffle.g:2040:31: LPAREN ( testlist[expr_contextType.Load] )? RPAREN
					{
					LPAREN280=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_classdef7755); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					LPAREN280_tree = (PNode)adaptor.create(LPAREN280);
					adaptor.addChild(root_0, LPAREN280_tree);
					}

					// Truffle.g:2040:38: ( testlist[expr_contextType.Load] )?
					int alt147=2;
					int LA147_0 = input.LA(1);
					if ( (LA147_0==BACKQUOTE||(LA147_0 >= LBRACK && LA147_0 <= LCURLY)||(LA147_0 >= LPAREN && LA147_0 <= MINUS)||LA147_0==NAME||LA147_0==NOT||LA147_0==PLUS||LA147_0==TILDE) ) {
						alt147=1;
					}
					else if ( (LA147_0==PRINT) && ((printFunction))) {
						alt147=1;
					}
					else if ( (LA147_0==COMPLEX||LA147_0==FALSE||LA147_0==FLOAT||LA147_0==INT||LA147_0==LAMBDA||LA147_0==NONE||LA147_0==STRING||LA147_0==TRUE) ) {
						alt147=1;
					}
					switch (alt147) {
						case 1 :
							// Truffle.g:2040:38: testlist[expr_contextType.Load]
							{
							pushFollow(FOLLOW_testlist_in_classdef7757);
							testlist281=testlist(expr_contextType.Load);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, testlist281.getTree());

							}
							break;

					}

					RPAREN282=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_classdef7761); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					RPAREN282_tree = (PNode)adaptor.create(RPAREN282);
					adaptor.addChild(root_0, RPAREN282_tree);
					}

					}
					break;

			}

			COLON283=(Token)match(input,COLON,FOLLOW_COLON_in_classdef7765); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			COLON283_tree = (PNode)adaptor.create(COLON283);
			adaptor.addChild(root_0, COLON283_tree);
			}

			pushFollow(FOLLOW_suite_in_classdef7767);
			suite284=suite(false);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, suite284.getTree());

			if ( state.backtracking==0 ) {
			          Token t = CLASS278;
			          if ((decorators277!=null?(decorators277.start):null) != null) {
			              t = (decorators277!=null?(decorators277.start):null);
			          }
			//          stype = new ClassDef(t, actions.cantBeNoneName(NAME279),
			//              actions.makeBases(actions.castExpr((testlist281!=null?((PNode)testlist281.getTree()):null))),
			//              actions.castStmts((suite284!=null?((TruffleParser.suite_return)suite284).stypes:null)),
			//              actions.castExprs((decorators277!=null?((TruffleParser.decorators_return)decorators277).etypes:null)));
			            stype = actions.makeClassDef(t,NAME279,(testlist281!=null?((PNode)testlist281.getTree()):null),(suite284!=null?((TruffleParser.suite_return)suite284).stypes:null),(decorators277!=null?((TruffleParser.decorators_return)decorators277).etypes:null));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			   retval.tree = stype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "classdef"


	public static class arglist_return extends ParserRuleReturnScope {
		public List args;
		public List keywords;
		public PNode starargs;
		public PNode kwargs;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "arglist"
	// Truffle.g:2057:1: arglist returns [List args, List keywords, PNode starargs, PNode kwargs] : ( argument[arguments, kws, gens, true, false] ( COMMA argument[arguments, kws, gens, false, false] )* ( COMMA ( STAR s= test[expr_contextType.Load] ( COMMA argument[arguments, kws, gens, false, true] )* ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )? | DOUBLESTAR k= test[expr_contextType.Load] )? )? | STAR s= test[expr_contextType.Load] ( COMMA argument[arguments, kws, gens, false, true] )* ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )? | DOUBLESTAR k= test[expr_contextType.Load] );
	public final TruffleParser.arglist_return arglist() throws RecognitionException {
		TruffleParser.arglist_return retval = new TruffleParser.arglist_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token COMMA286=null;
		Token COMMA288=null;
		Token STAR289=null;
		Token COMMA290=null;
		Token COMMA292=null;
		Token DOUBLESTAR293=null;
		Token DOUBLESTAR294=null;
		Token STAR295=null;
		Token COMMA296=null;
		Token COMMA298=null;
		Token DOUBLESTAR299=null;
		Token DOUBLESTAR300=null;
		ParserRuleReturnScope s =null;
		ParserRuleReturnScope k =null;
		ParserRuleReturnScope argument285 =null;
		ParserRuleReturnScope argument287 =null;
		ParserRuleReturnScope argument291 =null;
		ParserRuleReturnScope argument297 =null;

		PNode COMMA286_tree=null;
		PNode COMMA288_tree=null;
		PNode STAR289_tree=null;
		PNode COMMA290_tree=null;
		PNode COMMA292_tree=null;
		PNode DOUBLESTAR293_tree=null;
		PNode DOUBLESTAR294_tree=null;
		PNode STAR295_tree=null;
		PNode COMMA296_tree=null;
		PNode COMMA298_tree=null;
		PNode DOUBLESTAR299_tree=null;
		PNode DOUBLESTAR300_tree=null;


		    List arguments = new ArrayList();
		    List kws = new ArrayList();
		    List gens = new ArrayList();

		try {
			// Truffle.g:2064:5: ( argument[arguments, kws, gens, true, false] ( COMMA argument[arguments, kws, gens, false, false] )* ( COMMA ( STAR s= test[expr_contextType.Load] ( COMMA argument[arguments, kws, gens, false, true] )* ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )? | DOUBLESTAR k= test[expr_contextType.Load] )? )? | STAR s= test[expr_contextType.Load] ( COMMA argument[arguments, kws, gens, false, true] )* ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )? | DOUBLESTAR k= test[expr_contextType.Load] )
			int alt156=3;
			int LA156_0 = input.LA(1);
			if ( (LA156_0==BACKQUOTE||(LA156_0 >= LBRACK && LA156_0 <= LCURLY)||(LA156_0 >= LPAREN && LA156_0 <= MINUS)||LA156_0==NAME||LA156_0==NOT||LA156_0==PLUS||LA156_0==TILDE) ) {
				alt156=1;
			}
			else if ( (LA156_0==PRINT) && ((printFunction))) {
				alt156=1;
			}
			else if ( (LA156_0==COMPLEX||LA156_0==FALSE||LA156_0==FLOAT||LA156_0==INT||LA156_0==LAMBDA||LA156_0==NONE||LA156_0==STRING||LA156_0==TRUE) ) {
				alt156=1;
			}
			else if ( (LA156_0==STAR) ) {
				alt156=2;
			}
			else if ( (LA156_0==DOUBLESTAR) ) {
				alt156=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 156, 0, input);
				throw nvae;
			}

			switch (alt156) {
				case 1 :
					// Truffle.g:2064:7: argument[arguments, kws, gens, true, false] ( COMMA argument[arguments, kws, gens, false, false] )* ( COMMA ( STAR s= test[expr_contextType.Load] ( COMMA argument[arguments, kws, gens, false, true] )* ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )? | DOUBLESTAR k= test[expr_contextType.Load] )? )?
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_argument_in_arglist7809);
					argument285=argument(arguments, kws, gens, true, false);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, argument285.getTree());

					// Truffle.g:2064:51: ( COMMA argument[arguments, kws, gens, false, false] )*
					loop149:
					while (true) {
						int alt149=2;
						int LA149_0 = input.LA(1);
						if ( (LA149_0==COMMA) ) {
							int LA149_1 = input.LA(2);
							if ( (LA149_1==BACKQUOTE||LA149_1==COMPLEX||LA149_1==FALSE||LA149_1==FLOAT||LA149_1==INT||(LA149_1 >= LAMBDA && LA149_1 <= LCURLY)||(LA149_1 >= LPAREN && LA149_1 <= MINUS)||LA149_1==NAME||LA149_1==NONE||LA149_1==NOT||LA149_1==PLUS||LA149_1==PRINT||(LA149_1 >= STRING && LA149_1 <= TILDE)||LA149_1==TRUE) ) {
								alt149=1;
							}

						}

						switch (alt149) {
						case 1 :
							// Truffle.g:2064:52: COMMA argument[arguments, kws, gens, false, false]
							{
							COMMA286=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist7813); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA286_tree = (PNode)adaptor.create(COMMA286);
							adaptor.addChild(root_0, COMMA286_tree);
							}

							pushFollow(FOLLOW_argument_in_arglist7815);
							argument287=argument(arguments, kws, gens, false, false);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, argument287.getTree());

							}
							break;

						default :
							break loop149;
						}
					}

					// Truffle.g:2065:11: ( COMMA ( STAR s= test[expr_contextType.Load] ( COMMA argument[arguments, kws, gens, false, true] )* ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )? | DOUBLESTAR k= test[expr_contextType.Load] )? )?
					int alt153=2;
					int LA153_0 = input.LA(1);
					if ( (LA153_0==COMMA) ) {
						alt153=1;
					}
					switch (alt153) {
						case 1 :
							// Truffle.g:2065:12: COMMA ( STAR s= test[expr_contextType.Load] ( COMMA argument[arguments, kws, gens, false, true] )* ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )? | DOUBLESTAR k= test[expr_contextType.Load] )?
							{
							COMMA288=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist7831); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA288_tree = (PNode)adaptor.create(COMMA288);
							adaptor.addChild(root_0, COMMA288_tree);
							}

							// Truffle.g:2066:15: ( STAR s= test[expr_contextType.Load] ( COMMA argument[arguments, kws, gens, false, true] )* ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )? | DOUBLESTAR k= test[expr_contextType.Load] )?
							int alt152=3;
							int LA152_0 = input.LA(1);
							if ( (LA152_0==STAR) ) {
								alt152=1;
							}
							else if ( (LA152_0==DOUBLESTAR) ) {
								alt152=2;
							}
							switch (alt152) {
								case 1 :
									// Truffle.g:2066:17: STAR s= test[expr_contextType.Load] ( COMMA argument[arguments, kws, gens, false, true] )* ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )?
									{
									STAR289=(Token)match(input,STAR,FOLLOW_STAR_in_arglist7849); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									STAR289_tree = (PNode)adaptor.create(STAR289);
									adaptor.addChild(root_0, STAR289_tree);
									}

									pushFollow(FOLLOW_test_in_arglist7853);
									s=test(expr_contextType.Load);
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, s.getTree());

									// Truffle.g:2066:52: ( COMMA argument[arguments, kws, gens, false, true] )*
									loop150:
									while (true) {
										int alt150=2;
										int LA150_0 = input.LA(1);
										if ( (LA150_0==COMMA) ) {
											int LA150_1 = input.LA(2);
											if ( (LA150_1==BACKQUOTE||LA150_1==COMPLEX||LA150_1==FALSE||LA150_1==FLOAT||LA150_1==INT||(LA150_1 >= LAMBDA && LA150_1 <= LCURLY)||(LA150_1 >= LPAREN && LA150_1 <= MINUS)||LA150_1==NAME||LA150_1==NONE||LA150_1==NOT||LA150_1==PLUS||LA150_1==PRINT||(LA150_1 >= STRING && LA150_1 <= TILDE)||LA150_1==TRUE) ) {
												alt150=1;
											}

										}

										switch (alt150) {
										case 1 :
											// Truffle.g:2066:53: COMMA argument[arguments, kws, gens, false, true]
											{
											COMMA290=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist7857); if (state.failed) return retval;
											if ( state.backtracking==0 ) {
											COMMA290_tree = (PNode)adaptor.create(COMMA290);
											adaptor.addChild(root_0, COMMA290_tree);
											}

											pushFollow(FOLLOW_argument_in_arglist7859);
											argument291=argument(arguments, kws, gens, false, true);
											state._fsp--;
											if (state.failed) return retval;
											if ( state.backtracking==0 ) adaptor.addChild(root_0, argument291.getTree());

											}
											break;

										default :
											break loop150;
										}
									}

									// Truffle.g:2066:105: ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )?
									int alt151=2;
									int LA151_0 = input.LA(1);
									if ( (LA151_0==COMMA) ) {
										alt151=1;
									}
									switch (alt151) {
										case 1 :
											// Truffle.g:2066:106: COMMA DOUBLESTAR k= test[expr_contextType.Load]
											{
											COMMA292=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist7865); if (state.failed) return retval;
											if ( state.backtracking==0 ) {
											COMMA292_tree = (PNode)adaptor.create(COMMA292);
											adaptor.addChild(root_0, COMMA292_tree);
											}

											DOUBLESTAR293=(Token)match(input,DOUBLESTAR,FOLLOW_DOUBLESTAR_in_arglist7867); if (state.failed) return retval;
											if ( state.backtracking==0 ) {
											DOUBLESTAR293_tree = (PNode)adaptor.create(DOUBLESTAR293);
											adaptor.addChild(root_0, DOUBLESTAR293_tree);
											}

											pushFollow(FOLLOW_test_in_arglist7871);
											k=test(expr_contextType.Load);
											state._fsp--;
											if (state.failed) return retval;
											if ( state.backtracking==0 ) adaptor.addChild(root_0, k.getTree());

											}
											break;

									}

									}
									break;
								case 2 :
									// Truffle.g:2067:17: DOUBLESTAR k= test[expr_contextType.Load]
									{
									DOUBLESTAR294=(Token)match(input,DOUBLESTAR,FOLLOW_DOUBLESTAR_in_arglist7892); if (state.failed) return retval;
									if ( state.backtracking==0 ) {
									DOUBLESTAR294_tree = (PNode)adaptor.create(DOUBLESTAR294);
									adaptor.addChild(root_0, DOUBLESTAR294_tree);
									}

									pushFollow(FOLLOW_test_in_arglist7896);
									k=test(expr_contextType.Load);
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, k.getTree());

									}
									break;

							}

							}
							break;

					}

					if ( state.backtracking==0 ) {
					          if (arguments.size() > 1 && gens.size() > 0) {
					              actions.errorGenExpNotSoleArg((retval.start));
					          }
					          retval.args =arguments;
					          retval.keywords =kws;
					          retval.starargs =actions.castExpr((s!=null?((PNode)s.getTree()):null));
					          retval.kwargs =actions.castExpr((k!=null?((PNode)k.getTree()):null));
					      }
					}
					break;
				case 2 :
					// Truffle.g:2079:7: STAR s= test[expr_contextType.Load] ( COMMA argument[arguments, kws, gens, false, true] )* ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )?
					{
					root_0 = (PNode)adaptor.nil();


					STAR295=(Token)match(input,STAR,FOLLOW_STAR_in_arglist7943); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					STAR295_tree = (PNode)adaptor.create(STAR295);
					adaptor.addChild(root_0, STAR295_tree);
					}

					pushFollow(FOLLOW_test_in_arglist7947);
					s=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, s.getTree());

					// Truffle.g:2079:42: ( COMMA argument[arguments, kws, gens, false, true] )*
					loop154:
					while (true) {
						int alt154=2;
						int LA154_0 = input.LA(1);
						if ( (LA154_0==COMMA) ) {
							int LA154_1 = input.LA(2);
							if ( (LA154_1==BACKQUOTE||LA154_1==COMPLEX||LA154_1==FALSE||LA154_1==FLOAT||LA154_1==INT||(LA154_1 >= LAMBDA && LA154_1 <= LCURLY)||(LA154_1 >= LPAREN && LA154_1 <= MINUS)||LA154_1==NAME||LA154_1==NONE||LA154_1==NOT||LA154_1==PLUS||LA154_1==PRINT||(LA154_1 >= STRING && LA154_1 <= TILDE)||LA154_1==TRUE) ) {
								alt154=1;
							}

						}

						switch (alt154) {
						case 1 :
							// Truffle.g:2079:43: COMMA argument[arguments, kws, gens, false, true]
							{
							COMMA296=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist7951); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA296_tree = (PNode)adaptor.create(COMMA296);
							adaptor.addChild(root_0, COMMA296_tree);
							}

							pushFollow(FOLLOW_argument_in_arglist7953);
							argument297=argument(arguments, kws, gens, false, true);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, argument297.getTree());

							}
							break;

						default :
							break loop154;
						}
					}

					// Truffle.g:2079:95: ( COMMA DOUBLESTAR k= test[expr_contextType.Load] )?
					int alt155=2;
					int LA155_0 = input.LA(1);
					if ( (LA155_0==COMMA) ) {
						alt155=1;
					}
					switch (alt155) {
						case 1 :
							// Truffle.g:2079:96: COMMA DOUBLESTAR k= test[expr_contextType.Load]
							{
							COMMA298=(Token)match(input,COMMA,FOLLOW_COMMA_in_arglist7959); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							COMMA298_tree = (PNode)adaptor.create(COMMA298);
							adaptor.addChild(root_0, COMMA298_tree);
							}

							DOUBLESTAR299=(Token)match(input,DOUBLESTAR,FOLLOW_DOUBLESTAR_in_arglist7961); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							DOUBLESTAR299_tree = (PNode)adaptor.create(DOUBLESTAR299);
							adaptor.addChild(root_0, DOUBLESTAR299_tree);
							}

							pushFollow(FOLLOW_test_in_arglist7965);
							k=test(expr_contextType.Load);
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, k.getTree());

							}
							break;

					}

					if ( state.backtracking==0 ) {
					          retval.starargs =actions.castExpr((s!=null?((PNode)s.getTree()):null));
					          retval.keywords =kws;
					          retval.kwargs =actions.castExpr((k!=null?((PNode)k.getTree()):null));
					      }
					}
					break;
				case 3 :
					// Truffle.g:2085:7: DOUBLESTAR k= test[expr_contextType.Load]
					{
					root_0 = (PNode)adaptor.nil();


					DOUBLESTAR300=(Token)match(input,DOUBLESTAR,FOLLOW_DOUBLESTAR_in_arglist7984); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					DOUBLESTAR300_tree = (PNode)adaptor.create(DOUBLESTAR300);
					adaptor.addChild(root_0, DOUBLESTAR300_tree);
					}

					pushFollow(FOLLOW_test_in_arglist7988);
					k=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, k.getTree());

					if ( state.backtracking==0 ) {
					          retval.kwargs =actions.castExpr((k!=null?((PNode)k.getTree()):null));
					      }
					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "arglist"


	public static class argument_return extends ParserRuleReturnScope {
		public boolean genarg;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "argument"
	// Truffle.g:2092:1: argument[List arguments, List kws, List gens, boolean first, boolean afterStar] returns [boolean genarg] : t1= test[expr_contextType.Load] ( ( ASSIGN t2= test[expr_contextType.Load] ) | comp_for[$gens] |) ;
	public final TruffleParser.argument_return argument(List arguments, List kws, List gens, boolean first, boolean afterStar) throws RecognitionException {
		TruffleParser.argument_return retval = new TruffleParser.argument_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token ASSIGN301=null;
		ParserRuleReturnScope t1 =null;
		ParserRuleReturnScope t2 =null;
		ParserRuleReturnScope comp_for302 =null;

		PNode ASSIGN301_tree=null;

		try {
			// Truffle.g:2094:5: (t1= test[expr_contextType.Load] ( ( ASSIGN t2= test[expr_contextType.Load] ) | comp_for[$gens] |) )
			// Truffle.g:2094:7: t1= test[expr_contextType.Load] ( ( ASSIGN t2= test[expr_contextType.Load] ) | comp_for[$gens] |)
			{
			root_0 = (PNode)adaptor.nil();


			pushFollow(FOLLOW_test_in_argument8027);
			t1=test(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, t1.getTree());

			// Truffle.g:2095:9: ( ( ASSIGN t2= test[expr_contextType.Load] ) | comp_for[$gens] |)
			int alt157=3;
			switch ( input.LA(1) ) {
			case ASSIGN:
				{
				alt157=1;
				}
				break;
			case FOR:
				{
				alt157=2;
				}
				break;
			case COMMA:
			case RPAREN:
				{
				alt157=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 157, 0, input);
				throw nvae;
			}
			switch (alt157) {
				case 1 :
					// Truffle.g:2095:10: ( ASSIGN t2= test[expr_contextType.Load] )
					{
					// Truffle.g:2095:10: ( ASSIGN t2= test[expr_contextType.Load] )
					// Truffle.g:2095:11: ASSIGN t2= test[expr_contextType.Load]
					{
					ASSIGN301=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_argument8040); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					ASSIGN301_tree = (PNode)adaptor.create(ASSIGN301);
					adaptor.addChild(root_0, ASSIGN301_tree);
					}

					pushFollow(FOLLOW_test_in_argument8044);
					t2=test(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, t2.getTree());

					}

					if ( state.backtracking==0 ) {
					              PNode newkey = actions.castExpr((t1!=null?((PNode)t1.getTree()):null));
					              //Loop through all current keys and fail on duplicate.
					              for(Object o: kws) {
					                  List list = (List)o;
					                  Object oldkey = list.get(0);
					//                  if (oldkey instanceof Name && newkey instanceof Name) { //TODO: need to check if keyword != old keyword
					//                      if (((Name)oldkey).getId().equals(((Name)newkey).getId())) {
					//                          errorHandler.error("keyword arguments repeated", (t1!=null?((PNode)t1.getTree()):null));
					//                      }
					//                  }
					              }
					              List<PNode> exprs = new ArrayList<PNode>();
					              exprs.add(newkey);
					              exprs.add(actions.castExpr((t2!=null?((PNode)t2.getTree()):null)));
					              kws.add(exprs);
					          }
					}
					break;
				case 2 :
					// Truffle.g:2113:11: comp_for[$gens]
					{
					if ( state.backtracking==0 ) {actions.beginScope();}
					pushFollow(FOLLOW_comp_for_in_argument8072);
					comp_for302=comp_for(gens);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comp_for302.getTree());

					if ( state.backtracking==0 ) {
					              if (!first) {
					                  actions.errorGenExpNotSoleArg((comp_for302!=null?((PNode)comp_for302.getTree()):null));
					              }
					              retval.genarg = true;
					              Collections.reverse(gens);
					              List<PComprehension> c = gens;
					              arguments.add(actions.makeGeneratorExp((t1!=null?(t1.start):null), actions.castExpr((t1!=null?((PNode)t1.getTree()):null)), c));
					              actions.endScope();
					          }
					}
					break;
				case 3 :
					// Truffle.g:2125:11: 
					{
					if ( state.backtracking==0 ) {
					              if (kws.size() > 0) {
					                  errorHandler.error("non-keyword arg after keyword arg", (t1!=null?((PNode)t1.getTree()):null).getToken());
					              } else if (afterStar) {
					                  errorHandler.error("only named arguments may follow *expression", (t1!=null?((PNode)t1.getTree()):null).getToken());
					              }
					              arguments.add((t1!=null?((PNode)t1.getTree()):null));
					          }
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "argument"


	public static class list_iter_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "list_iter"
	// Truffle.g:2137:1: list_iter[List gens, List ifs] : ( list_for[gens] | list_if[gens, ifs] );
	public final TruffleParser.list_iter_return list_iter(List gens, List ifs) throws RecognitionException {
		TruffleParser.list_iter_return retval = new TruffleParser.list_iter_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		ParserRuleReturnScope list_for303 =null;
		ParserRuleReturnScope list_if304 =null;


		try {
			// Truffle.g:2138:5: ( list_for[gens] | list_if[gens, ifs] )
			int alt158=2;
			int LA158_0 = input.LA(1);
			if ( (LA158_0==FOR) ) {
				alt158=1;
			}
			else if ( (LA158_0==IF) ) {
				alt158=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 158, 0, input);
				throw nvae;
			}

			switch (alt158) {
				case 1 :
					// Truffle.g:2138:7: list_for[gens]
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_list_for_in_list_iter8137);
					list_for303=list_for(gens);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, list_for303.getTree());

					}
					break;
				case 2 :
					// Truffle.g:2139:7: list_if[gens, ifs]
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_list_if_in_list_iter8146);
					list_if304=list_if(gens, ifs);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, list_if304.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "list_iter"


	public static class list_for_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "list_for"
	// Truffle.g:2143:1: list_for[List gens] : FOR exprlist[expr_contextType.Store] IN testlist[expr_contextType.Load] ( list_iter[gens, ifs] )? ;
	public final TruffleParser.list_for_return list_for(List gens) throws RecognitionException {
		TruffleParser.list_for_return retval = new TruffleParser.list_for_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token FOR305=null;
		Token IN307=null;
		ParserRuleReturnScope exprlist306 =null;
		ParserRuleReturnScope testlist308 =null;
		ParserRuleReturnScope list_iter309 =null;

		PNode FOR305_tree=null;
		PNode IN307_tree=null;


		    List ifs = new ArrayList();

		try {
			// Truffle.g:2147:5: ( FOR exprlist[expr_contextType.Store] IN testlist[expr_contextType.Load] ( list_iter[gens, ifs] )? )
			// Truffle.g:2147:7: FOR exprlist[expr_contextType.Store] IN testlist[expr_contextType.Load] ( list_iter[gens, ifs] )?
			{
			root_0 = (PNode)adaptor.nil();


			FOR305=(Token)match(input,FOR,FOLLOW_FOR_in_list_for8172); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			FOR305_tree = (PNode)adaptor.create(FOR305);
			adaptor.addChild(root_0, FOR305_tree);
			}

			pushFollow(FOLLOW_exprlist_in_list_for8174);
			exprlist306=exprlist(expr_contextType.Store);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, exprlist306.getTree());

			IN307=(Token)match(input,IN,FOLLOW_IN_in_list_for8177); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN307_tree = (PNode)adaptor.create(IN307);
			adaptor.addChild(root_0, IN307_tree);
			}

			pushFollow(FOLLOW_testlist_in_list_for8179);
			testlist308=testlist(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, testlist308.getTree());

			// Truffle.g:2147:79: ( list_iter[gens, ifs] )?
			int alt159=2;
			int LA159_0 = input.LA(1);
			if ( (LA159_0==FOR||LA159_0==IF) ) {
				alt159=1;
			}
			switch (alt159) {
				case 1 :
					// Truffle.g:2147:80: list_iter[gens, ifs]
					{
					pushFollow(FOLLOW_list_iter_in_list_for8183);
					list_iter309=list_iter(gens, ifs);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, list_iter309.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          Collections.reverse(ifs);
			          gens.add(actions.makeComprehension(FOR305, (exprlist306!=null?((TruffleParser.exprlist_return)exprlist306).etype:null), actions.castExpr((testlist308!=null?((PNode)testlist308.getTree()):null)), ifs));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "list_for"


	public static class list_if_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "list_if"
	// Truffle.g:2155:1: list_if[List gens, List ifs] : IF test[expr_contextType.Load] ( list_iter[gens, ifs] )? ;
	public final TruffleParser.list_if_return list_if(List gens, List ifs) throws RecognitionException {
		TruffleParser.list_if_return retval = new TruffleParser.list_if_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token IF310=null;
		ParserRuleReturnScope test311 =null;
		ParserRuleReturnScope list_iter312 =null;

		PNode IF310_tree=null;

		try {
			// Truffle.g:2156:5: ( IF test[expr_contextType.Load] ( list_iter[gens, ifs] )? )
			// Truffle.g:2156:7: IF test[expr_contextType.Load] ( list_iter[gens, ifs] )?
			{
			root_0 = (PNode)adaptor.nil();


			IF310=(Token)match(input,IF,FOLLOW_IF_in_list_if8213); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IF310_tree = (PNode)adaptor.create(IF310);
			adaptor.addChild(root_0, IF310_tree);
			}

			pushFollow(FOLLOW_test_in_list_if8215);
			test311=test(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, test311.getTree());

			// Truffle.g:2156:38: ( list_iter[gens, ifs] )?
			int alt160=2;
			int LA160_0 = input.LA(1);
			if ( (LA160_0==FOR||LA160_0==IF) ) {
				alt160=1;
			}
			switch (alt160) {
				case 1 :
					// Truffle.g:2156:39: list_iter[gens, ifs]
					{
					pushFollow(FOLLOW_list_iter_in_list_if8219);
					list_iter312=list_iter(gens, ifs);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, list_iter312.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			        ifs.add(actions.castExpr((test311!=null?((PNode)test311.getTree()):null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "list_if"


	public static class comp_iter_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "comp_iter"
	// Truffle.g:2163:1: comp_iter[List gens, List ifs] : ( comp_for[gens] | comp_if[gens, ifs] );
	public final TruffleParser.comp_iter_return comp_iter(List gens, List ifs) throws RecognitionException {
		TruffleParser.comp_iter_return retval = new TruffleParser.comp_iter_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		ParserRuleReturnScope comp_for313 =null;
		ParserRuleReturnScope comp_if314 =null;


		try {
			// Truffle.g:2164:5: ( comp_for[gens] | comp_if[gens, ifs] )
			int alt161=2;
			int LA161_0 = input.LA(1);
			if ( (LA161_0==FOR) ) {
				alt161=1;
			}
			else if ( (LA161_0==IF) ) {
				alt161=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 161, 0, input);
				throw nvae;
			}

			switch (alt161) {
				case 1 :
					// Truffle.g:2164:7: comp_for[gens]
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_comp_for_in_comp_iter8250);
					comp_for313=comp_for(gens);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comp_for313.getTree());

					}
					break;
				case 2 :
					// Truffle.g:2165:7: comp_if[gens, ifs]
					{
					root_0 = (PNode)adaptor.nil();


					pushFollow(FOLLOW_comp_if_in_comp_iter8259);
					comp_if314=comp_if(gens, ifs);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comp_if314.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "comp_iter"


	public static class comp_for_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "comp_for"
	// Truffle.g:2169:1: comp_for[List gens] : FOR exprlist[expr_contextType.Store] IN or_test[expr_contextType.Load] ( comp_iter[gens, ifs] )? ;
	public final TruffleParser.comp_for_return comp_for(List gens) throws RecognitionException {
		TruffleParser.comp_for_return retval = new TruffleParser.comp_for_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token FOR315=null;
		Token IN317=null;
		ParserRuleReturnScope exprlist316 =null;
		ParserRuleReturnScope or_test318 =null;
		ParserRuleReturnScope comp_iter319 =null;

		PNode FOR315_tree=null;
		PNode IN317_tree=null;


		    List ifs = new ArrayList();

		try {
			// Truffle.g:2173:5: ( FOR exprlist[expr_contextType.Store] IN or_test[expr_contextType.Load] ( comp_iter[gens, ifs] )? )
			// Truffle.g:2173:7: FOR exprlist[expr_contextType.Store] IN or_test[expr_contextType.Load] ( comp_iter[gens, ifs] )?
			{
			root_0 = (PNode)adaptor.nil();


			FOR315=(Token)match(input,FOR,FOLLOW_FOR_in_comp_for8285); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			FOR315_tree = (PNode)adaptor.create(FOR315);
			adaptor.addChild(root_0, FOR315_tree);
			}

			pushFollow(FOLLOW_exprlist_in_comp_for8287);
			exprlist316=exprlist(expr_contextType.Store);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, exprlist316.getTree());

			IN317=(Token)match(input,IN,FOLLOW_IN_in_comp_for8290); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IN317_tree = (PNode)adaptor.create(IN317);
			adaptor.addChild(root_0, IN317_tree);
			}

			pushFollow(FOLLOW_or_test_in_comp_for8292);
			or_test318=or_test(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, or_test318.getTree());

			// Truffle.g:2173:78: ( comp_iter[gens, ifs] )?
			int alt162=2;
			int LA162_0 = input.LA(1);
			if ( (LA162_0==FOR||LA162_0==IF) ) {
				alt162=1;
			}
			switch (alt162) {
				case 1 :
					// Truffle.g:2173:78: comp_iter[gens, ifs]
					{
					pushFollow(FOLLOW_comp_iter_in_comp_for8295);
					comp_iter319=comp_iter(gens, ifs);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comp_iter319.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          Collections.reverse(ifs);
			          gens.add(actions.makeComprehension(FOR315, (exprlist316!=null?((TruffleParser.exprlist_return)exprlist316).etype:null), actions.castExpr((or_test318!=null?((PNode)or_test318.getTree()):null)), ifs));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "comp_for"


	public static class comp_if_return extends ParserRuleReturnScope {
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "comp_if"
	// Truffle.g:2181:1: comp_if[List gens, List ifs] : IF test[expr_contextType.Load] ( comp_iter[gens, ifs] )? ;
	public final TruffleParser.comp_if_return comp_if(List gens, List ifs) throws RecognitionException {
		TruffleParser.comp_if_return retval = new TruffleParser.comp_if_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token IF320=null;
		ParserRuleReturnScope test321 =null;
		ParserRuleReturnScope comp_iter322 =null;

		PNode IF320_tree=null;

		try {
			// Truffle.g:2182:5: ( IF test[expr_contextType.Load] ( comp_iter[gens, ifs] )? )
			// Truffle.g:2182:7: IF test[expr_contextType.Load] ( comp_iter[gens, ifs] )?
			{
			root_0 = (PNode)adaptor.nil();


			IF320=(Token)match(input,IF,FOLLOW_IF_in_comp_if8324); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			IF320_tree = (PNode)adaptor.create(IF320);
			adaptor.addChild(root_0, IF320_tree);
			}

			pushFollow(FOLLOW_test_in_comp_if8326);
			test321=test(expr_contextType.Load);
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, test321.getTree());

			// Truffle.g:2182:38: ( comp_iter[gens, ifs] )?
			int alt163=2;
			int LA163_0 = input.LA(1);
			if ( (LA163_0==FOR||LA163_0==IF) ) {
				alt163=1;
			}
			switch (alt163) {
				case 1 :
					// Truffle.g:2182:38: comp_iter[gens, ifs]
					{
					pushFollow(FOLLOW_comp_iter_in_comp_if8329);
					comp_iter322=comp_iter(gens, ifs);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comp_iter322.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			        ifs.add(actions.castExpr((test321!=null?((PNode)test321.getTree()):null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "comp_if"


	public static class yield_expr_return extends ParserRuleReturnScope {
		public StatementNode etype;
		PNode tree;
		@Override
		public PNode getTree() { return tree; }
	};


	// $ANTLR start "yield_expr"
	// Truffle.g:2189:1: yield_expr returns [StatementNode etype] : YIELD ( testlist[expr_contextType.Load] )? ;
	public final TruffleParser.yield_expr_return yield_expr() throws RecognitionException {
		TruffleParser.yield_expr_return retval = new TruffleParser.yield_expr_return();
		retval.start = input.LT(1);

		PNode root_0 = null;

		Token YIELD323=null;
		ParserRuleReturnScope testlist324 =null;

		PNode YIELD323_tree=null;

		try {
			// Truffle.g:2195:5: ( YIELD ( testlist[expr_contextType.Load] )? )
			// Truffle.g:2195:7: YIELD ( testlist[expr_contextType.Load] )?
			{
			root_0 = (PNode)adaptor.nil();


			YIELD323=(Token)match(input,YIELD,FOLLOW_YIELD_in_yield_expr8370); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			YIELD323_tree = (PNode)adaptor.create(YIELD323);
			adaptor.addChild(root_0, YIELD323_tree);
			}

			// Truffle.g:2195:13: ( testlist[expr_contextType.Load] )?
			int alt164=2;
			int LA164_0 = input.LA(1);
			if ( (LA164_0==BACKQUOTE||(LA164_0 >= LBRACK && LA164_0 <= LCURLY)||(LA164_0 >= LPAREN && LA164_0 <= MINUS)||LA164_0==NAME||LA164_0==NOT||LA164_0==PLUS||LA164_0==TILDE) ) {
				alt164=1;
			}
			else if ( (LA164_0==PRINT) && ((printFunction))) {
				alt164=1;
			}
			else if ( (LA164_0==COMPLEX||LA164_0==FALSE||LA164_0==FLOAT||LA164_0==INT||LA164_0==LAMBDA||LA164_0==NONE||LA164_0==STRING||LA164_0==TRUE) ) {
				alt164=1;
			}
			switch (alt164) {
				case 1 :
					// Truffle.g:2195:13: testlist[expr_contextType.Load]
					{
					pushFollow(FOLLOW_testlist_in_yield_expr8372);
					testlist324=testlist(expr_contextType.Load);
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, testlist324.getTree());

					}
					break;

			}

			if ( state.backtracking==0 ) {
			          retval.etype = actions.makeYield(YIELD323, actions.castExpr((testlist324!=null?((PNode)testlist324.getTree()):null)));
			      }
			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (PNode)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
			if ( state.backtracking==0 ) {
			    //needed for y2+=yield_expr
			    retval.tree = retval.etype;
			}
		}

		catch (RecognitionException re) {
		    reportError(re);
		    errorHandler.recover(this, input,re);
		    retval.tree = (PNode)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "yield_expr"

	// $ANTLR start synpred1_Truffle
	public final void synpred1_Truffle_fragment() throws RecognitionException {
		// Truffle.g:539:7: ( LPAREN fpdef[null] COMMA )
		// Truffle.g:539:8: LPAREN fpdef[null] COMMA
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred1_Truffle1326); if (state.failed) return;

		pushFollow(FOLLOW_fpdef_in_synpred1_Truffle1328);
		fpdef(null);
		state._fsp--;
		if (state.failed) return;

		match(input,COMMA,FOLLOW_COMMA_in_synpred1_Truffle1331); if (state.failed) return;

		}

	}
	// $ANTLR end synpred1_Truffle

	// $ANTLR start synpred2_Truffle
	public final void synpred2_Truffle_fragment() throws RecognitionException {
		// Truffle.g:618:8: ( testlist[null] augassign )
		// Truffle.g:618:9: testlist[null] augassign
		{
		pushFollow(FOLLOW_testlist_in_synpred2_Truffle1782);
		testlist(null);
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_augassign_in_synpred2_Truffle1785);
		augassign();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred2_Truffle

	// $ANTLR start synpred3_Truffle
	public final void synpred3_Truffle_fragment() throws RecognitionException {
		// Truffle.g:632:7: ( testlist[null] ASSIGN )
		// Truffle.g:632:8: testlist[null] ASSIGN
		{
		pushFollow(FOLLOW_testlist_in_synpred3_Truffle1901);
		testlist(null);
		state._fsp--;
		if (state.failed) return;

		match(input,ASSIGN,FOLLOW_ASSIGN_in_synpred3_Truffle1904); if (state.failed) return;

		}

	}
	// $ANTLR end synpred3_Truffle

	// $ANTLR start synpred4_Truffle
	public final void synpred4_Truffle_fragment() throws RecognitionException {
		// Truffle.g:740:7: ( test[null] COMMA )
		// Truffle.g:740:8: test[null] COMMA
		{
		pushFollow(FOLLOW_test_in_synpred4_Truffle2416);
		test(null);
		state._fsp--;
		if (state.failed) return;

		match(input,COMMA,FOLLOW_COMMA_in_synpred4_Truffle2419); if (state.failed) return;

		}

	}
	// $ANTLR end synpred4_Truffle

	// $ANTLR start synpred5_Truffle
	public final void synpred5_Truffle_fragment() throws RecognitionException {
		// Truffle.g:761:7: ( test[null] COMMA test[null] )
		// Truffle.g:761:8: test[null] COMMA test[null]
		{
		pushFollow(FOLLOW_test_in_synpred5_Truffle2515);
		test(null);
		state._fsp--;
		if (state.failed) return;

		match(input,COMMA,FOLLOW_COMMA_in_synpred5_Truffle2518); if (state.failed) return;

		pushFollow(FOLLOW_test_in_synpred5_Truffle2520);
		test(null);
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred5_Truffle

	// $ANTLR start synpred6_Truffle
	public final void synpred6_Truffle_fragment() throws RecognitionException {
		// Truffle.g:1075:7: ( ( decorators )? DEF )
		// Truffle.g:1075:8: ( decorators )? DEF
		{
		// Truffle.g:1075:8: ( decorators )?
		int alt165=2;
		int LA165_0 = input.LA(1);
		if ( (LA165_0==AT) ) {
			alt165=1;
		}
		switch (alt165) {
			case 1 :
				// Truffle.g:1075:8: decorators
				{
				pushFollow(FOLLOW_decorators_in_synpred6_Truffle3609);
				decorators();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		match(input,DEF,FOLLOW_DEF_in_synpred6_Truffle3612); if (state.failed) return;

		}

	}
	// $ANTLR end synpred6_Truffle

	// $ANTLR start synpred7_Truffle
	public final void synpred7_Truffle_fragment() throws RecognitionException {
		// Truffle.g:1273:9: ( IF or_test[null] ORELSE )
		// Truffle.g:1273:10: IF or_test[null] ORELSE
		{
		match(input,IF,FOLLOW_IF_in_synpred7_Truffle4369); if (state.failed) return;

		pushFollow(FOLLOW_or_test_in_synpred7_Truffle4371);
		or_test(null);
		state._fsp--;
		if (state.failed) return;

		match(input,ORELSE,FOLLOW_ORELSE_in_synpred7_Truffle4374); if (state.failed) return;

		}

	}
	// $ANTLR end synpred7_Truffle

	// $ANTLR start synpred8_Truffle
	public final void synpred8_Truffle_fragment() throws RecognitionException {
		// Truffle.g:1906:7: ( test[null] COLON )
		// Truffle.g:1906:8: test[null] COLON
		{
		pushFollow(FOLLOW_test_in_synpred8_Truffle7024);
		test(null);
		state._fsp--;
		if (state.failed) return;

		match(input,COLON,FOLLOW_COLON_in_synpred8_Truffle7027); if (state.failed) return;

		}

	}
	// $ANTLR end synpred8_Truffle

	// $ANTLR start synpred9_Truffle
	public final void synpred9_Truffle_fragment() throws RecognitionException {
		// Truffle.g:1911:7: ( COLON )
		// Truffle.g:1911:8: COLON
		{
		match(input,COLON,FOLLOW_COLON_in_synpred9_Truffle7075); if (state.failed) return;

		}

	}
	// $ANTLR end synpred9_Truffle

	// $ANTLR start synpred10_Truffle
	public final void synpred10_Truffle_fragment() throws RecognitionException {
		// Truffle.g:1945:7: ( expr[null] COMMA )
		// Truffle.g:1945:8: expr[null] COMMA
		{
		pushFollow(FOLLOW_expr_in_synpred10_Truffle7220);
		expr(null);
		state._fsp--;
		if (state.failed) return;

		match(input,COMMA,FOLLOW_COMMA_in_synpred10_Truffle7223); if (state.failed) return;

		}

	}
	// $ANTLR end synpred10_Truffle

	// $ANTLR start synpred11_Truffle
	public final void synpred11_Truffle_fragment() throws RecognitionException {
		// Truffle.g:1977:7: ( test[null] COMMA )
		// Truffle.g:1977:8: test[null] COMMA
		{
		pushFollow(FOLLOW_test_in_synpred11_Truffle7371);
		test(null);
		state._fsp--;
		if (state.failed) return;

		match(input,COMMA,FOLLOW_COMMA_in_synpred11_Truffle7374); if (state.failed) return;

		}

	}
	// $ANTLR end synpred11_Truffle

	// Delegated rules

	public final boolean synpred4_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred4_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred9_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred9_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred7_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred7_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred8_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred8_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred5_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred5_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred10_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred10_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred11_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred11_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred1_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred1_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred6_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred6_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred3_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred3_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred2_Truffle() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred2_Truffle_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}


	protected DFA53 dfa53 = new DFA53(this);
	protected DFA137 dfa137 = new DFA137(this);
	static final String DFA53_eotS =
		"\4\uffff";
	static final String DFA53_eofS =
		"\4\uffff";
	static final String DFA53_minS =
		"\2\32\2\uffff";
	static final String DFA53_maxS =
		"\2\76\2\uffff";
	static final String DFA53_acceptS =
		"\2\uffff\1\1\1\2";
	static final String DFA53_specialS =
		"\4\uffff}>";
	static final String[] DFA53_transitionS = {
			"\1\1\43\uffff\1\2",
			"\1\1\23\uffff\1\3\17\uffff\1\2",
			"",
			""
	};

	static final short[] DFA53_eot = DFA.unpackEncodedString(DFA53_eotS);
	static final short[] DFA53_eof = DFA.unpackEncodedString(DFA53_eofS);
	static final char[] DFA53_min = DFA.unpackEncodedStringToUnsignedChars(DFA53_minS);
	static final char[] DFA53_max = DFA.unpackEncodedStringToUnsignedChars(DFA53_maxS);
	static final short[] DFA53_accept = DFA.unpackEncodedString(DFA53_acceptS);
	static final short[] DFA53_special = DFA.unpackEncodedString(DFA53_specialS);
	static final short[][] DFA53_transition;

	static {
		int numStates = DFA53_transitionS.length;
		DFA53_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA53_transition[i] = DFA.unpackEncodedString(DFA53_transitionS[i]);
		}
	}

	protected class DFA53 extends DFA {

		public DFA53(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 53;
			this.eot = DFA53_eot;
			this.eof = DFA53_eof;
			this.min = DFA53_min;
			this.max = DFA53_max;
			this.accept = DFA53_accept;
			this.special = DFA53_special;
			this.transition = DFA53_transition;
		}
		@Override
		public String getDescription() {
			return "924:12: ( (d+= DOT )* dotted_name | (d+= DOT )+ )";
		}
	}

	static final String DFA137_eotS =
		"\100\uffff";
	static final String DFA137_eofS =
		"\2\2\76\uffff";
	static final String DFA137_minS =
		"\2\5\76\uffff";
	static final String DFA137_maxS =
		"\2\140\76\uffff";
	static final String DFA137_acceptS =
		"\2\uffff\1\2\46\uffff\1\1\5\uffff\1\1\20\uffff";
	static final String DFA137_specialS =
		"\100\uffff}>";
	static final String[] DFA137_transitionS = {
			"\1\2\3\uffff\1\2\1\uffff\1\2\2\uffff\1\2\1\uffff\1\2\1\1\12\uffff\1\2"+
			"\1\uffff\1\2\11\uffff\1\2\4\uffff\1\2\12\uffff\1\2\4\uffff\1\2\1\uffff"+
			"\1\2\10\uffff\1\2\1\uffff\1\2\2\uffff\1\2\3\uffff\3\2\1\uffff\1\2\1\uffff"+
			"\1\2\10\uffff\1\2",
			"\1\2\3\uffff\1\2\1\uffff\1\51\2\uffff\1\2\1\uffff\2\2\1\uffff\1\57\10"+
			"\uffff\1\2\1\uffff\1\2\6\uffff\1\57\1\uffff\1\57\1\2\4\uffff\1\2\3\uffff"+
			"\1\57\1\uffff\3\57\2\uffff\1\2\2\uffff\2\57\1\2\1\57\1\2\1\57\1\uffff"+
			"\1\57\5\uffff\1\2\1\57\1\2\1\57\1\uffff\1\2\3\uffff\3\2\1\uffff\1\2\1"+
			"\uffff\1\2\2\57\3\uffff\1\57\2\uffff\1\2",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			""
	};

	static final short[] DFA137_eot = DFA.unpackEncodedString(DFA137_eotS);
	static final short[] DFA137_eof = DFA.unpackEncodedString(DFA137_eofS);
	static final char[] DFA137_min = DFA.unpackEncodedStringToUnsignedChars(DFA137_minS);
	static final char[] DFA137_max = DFA.unpackEncodedStringToUnsignedChars(DFA137_maxS);
	static final short[] DFA137_accept = DFA.unpackEncodedString(DFA137_acceptS);
	static final short[] DFA137_special = DFA.unpackEncodedString(DFA137_specialS);
	static final short[][] DFA137_transition;

	static {
		int numStates = DFA137_transitionS.length;
		DFA137_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA137_transition[i] = DFA.unpackEncodedString(DFA137_transitionS[i]);
		}
	}

	protected class DFA137 extends DFA {

		public DFA137(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 137;
			this.eot = DFA137_eot;
			this.eof = DFA137_eof;
			this.min = DFA137_min;
			this.max = DFA137_max;
			this.accept = DFA137_accept;
			this.special = DFA137_special;
			this.transition = DFA137_transition;
		}
		@Override
		public String getDescription() {
			return "()* loopback of 1978:22: ( options {k=2; } : COMMA t+= test[ctype] )*";
		}
	}

	public static final BitSet FOLLOW_NEWLINE_in_single_input119 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_EOF_in_single_input122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_stmt_in_single_input138 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_NEWLINE_in_single_input140 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_EOF_in_single_input143 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_compound_stmt_in_single_input159 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_NEWLINE_in_single_input161 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_EOF_in_single_input164 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NEWLINE_in_file_input216 = new BitSet(new long[]{0xD83A67A001989D00L,0x0000001663009A47L});
	public static final BitSet FOLLOW_stmt_in_file_input226 = new BitSet(new long[]{0xD83A67A001989D00L,0x0000001663009A47L});
	public static final BitSet FOLLOW_EOF_in_file_input245 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEADING_WS_in_eval_input299 = new BitSet(new long[]{0xD83A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_NEWLINE_in_eval_input303 = new BitSet(new long[]{0xD83A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_testlist_in_eval_input307 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_NEWLINE_in_eval_input311 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_EOF_in_eval_input315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAME_in_dotted_attr367 = new BitSet(new long[]{0x0000000004000002L});
	public static final BitSet FOLLOW_DOT_in_dotted_attr378 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_dotted_attr382 = new BitSet(new long[]{0x0000000004000002L});
	public static final BitSet FOLLOW_NAME_in_name_or_print447 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PRINT_in_name_or_print461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AT_in_decorator792 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_dotted_attr_in_decorator794 = new BitSet(new long[]{0x8800000000000000L});
	public static final BitSet FOLLOW_LPAREN_in_decorator802 = new BitSet(new long[]{0x583A00A020080800L,0x0000000023440A05L});
	public static final BitSet FOLLOW_arglist_in_decorator812 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_RPAREN_in_decorator856 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_NEWLINE_in_decorator878 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_decorator_in_decorators906 = new BitSet(new long[]{0x0000000000000402L});
	public static final BitSet FOLLOW_decorators_in_funcdef944 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_DEF_in_funcdef947 = new BitSet(new long[]{0x4000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_name_or_print_in_funcdef949 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_parameters_in_funcdef951 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_funcdef953 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_funcdef955 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_parameters988 = new BitSet(new long[]{0x4800000020000000L,0x0000000000440000L});
	public static final BitSet FOLLOW_varargslist_in_parameters997 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_RPAREN_in_parameters1041 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fpdef_in_defparameter1074 = new BitSet(new long[]{0x0000000000000202L});
	public static final BitSet FOLLOW_ASSIGN_in_defparameter1078 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_defparameter1080 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_defparameter_in_varargslist1126 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_varargslist1137 = new BitSet(new long[]{0x4800000000000000L});
	public static final BitSet FOLLOW_defparameter_in_varargslist1141 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_varargslist1153 = new BitSet(new long[]{0x0000000020000002L,0x0000000000400000L});
	public static final BitSet FOLLOW_STAR_in_varargslist1166 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_varargslist1170 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_varargslist1173 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_DOUBLESTAR_in_varargslist1175 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_varargslist1179 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLESTAR_in_varargslist1195 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_varargslist1199 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STAR_in_varargslist1237 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_varargslist1241 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_varargslist1244 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_DOUBLESTAR_in_varargslist1246 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_varargslist1250 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLESTAR_in_varargslist1268 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_varargslist1272 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAME_in_fpdef1309 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_fpdef1336 = new BitSet(new long[]{0x4800000000000000L});
	public static final BitSet FOLLOW_fplist_in_fpdef1338 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_RPAREN_in_fpdef1340 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_fpdef1356 = new BitSet(new long[]{0x4800000000000000L});
	public static final BitSet FOLLOW_fplist_in_fpdef1359 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_RPAREN_in_fpdef1361 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fpdef_in_fplist1390 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_fplist1407 = new BitSet(new long[]{0x4800000000000000L});
	public static final BitSet FOLLOW_fpdef_in_fplist1411 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_fplist1417 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_stmt_in_stmt1453 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_compound_stmt_in_stmt1469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_small_stmt_in_simple_stmt1505 = new BitSet(new long[]{0x8000000000000000L,0x0000000000080000L});
	public static final BitSet FOLLOW_SEMI_in_simple_stmt1515 = new BitSet(new long[]{0x583A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_small_stmt_in_simple_stmt1519 = new BitSet(new long[]{0x8000000000000000L,0x0000000000080000L});
	public static final BitSet FOLLOW_SEMI_in_simple_stmt1524 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_NEWLINE_in_simple_stmt1528 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expr_stmt_in_small_stmt1551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_del_stmt_in_small_stmt1566 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_pass_stmt_in_small_stmt1581 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_flow_stmt_in_small_stmt1596 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_import_stmt_in_small_stmt1611 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_global_stmt_in_small_stmt1626 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_assert_stmt_in_small_stmt1653 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_print_stmt_in_small_stmt1672 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nonlocal_stmt_in_small_stmt1687 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NONLOCAL_in_nonlocal_stmt1722 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_nonlocal_stmt1726 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_nonlocal_stmt1737 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_nonlocal_stmt1741 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_testlist_in_expr_stmt1792 = new BitSet(new long[]{0x2100000050004020L,0x0000000100A20500L});
	public static final BitSet FOLLOW_augassign_in_expr_stmt1808 = new BitSet(new long[]{0x0000000000000000L,0x0000001000000000L});
	public static final BitSet FOLLOW_yield_expr_in_expr_stmt1812 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_augassign_in_expr_stmt1852 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_testlist_in_expr_stmt1856 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_testlist_in_expr_stmt1911 = new BitSet(new long[]{0x0000000000000202L});
	public static final BitSet FOLLOW_ASSIGN_in_expr_stmt1938 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_testlist_in_expr_stmt1942 = new BitSet(new long[]{0x0000000000000202L});
	public static final BitSet FOLLOW_ASSIGN_in_expr_stmt1987 = new BitSet(new long[]{0x0000000000000000L,0x0000001000000000L});
	public static final BitSet FOLLOW_yield_expr_in_expr_stmt1991 = new BitSet(new long[]{0x0000000000000202L});
	public static final BitSet FOLLOW_testlist_in_expr_stmt2039 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PLUSEQUAL_in_augassign2081 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUSEQUAL_in_augassign2099 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STAREQUAL_in_augassign2117 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SLASHEQUAL_in_augassign2135 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PERCENTEQUAL_in_augassign2153 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AMPEREQUAL_in_augassign2171 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VBAREQUAL_in_augassign2189 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CIRCUMFLEXEQUAL_in_augassign2207 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFTSHIFTEQUAL_in_augassign2225 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RIGHTSHIFTEQUAL_in_augassign2243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLESTAREQUAL_in_augassign2261 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLESLASHEQUAL_in_augassign2279 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PRINT_in_print_stmt2319 = new BitSet(new long[]{0x583A00A000080802L,0x0000000023010A05L});
	public static final BitSet FOLLOW_printlist_in_print_stmt2330 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RIGHTSHIFT_in_print_stmt2349 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_printlist2_in_print_stmt2353 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_printlist2433 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_printlist2445 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_printlist2449 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_printlist2457 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_printlist2478 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_printlist22535 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_printlist22547 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_printlist22551 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_printlist22559 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_printlist22580 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DELETE_in_del_stmt2617 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_del_list_in_del_stmt2619 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PASS_in_pass_stmt2655 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_break_stmt_in_flow_stmt2681 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_continue_stmt_in_flow_stmt2689 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_return_stmt_in_flow_stmt2697 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_raise_stmt_in_flow_stmt2705 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_yield_stmt_in_flow_stmt2713 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_BREAK_in_break_stmt2741 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CONTINUE_in_continue_stmt2777 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RETURN_in_return_stmt2813 = new BitSet(new long[]{0x583A00A000080802L,0x0000000023000A05L});
	public static final BitSet FOLLOW_testlist_in_return_stmt2822 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_yield_expr_in_yield_stmt2887 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RAISE_in_raise_stmt2923 = new BitSet(new long[]{0x583A00A000080802L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_raise_stmt2928 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_raise_stmt2932 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_raise_stmt2936 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_raise_stmt2948 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_raise_stmt2952 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_import_name_in_import_stmt2985 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_import_from_in_import_stmt2993 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IMPORT_in_import_name3021 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_dotted_as_names_in_import_name3023 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FROM_in_import_from3060 = new BitSet(new long[]{0x4000000004000000L});
	public static final BitSet FOLLOW_DOT_in_import_from3065 = new BitSet(new long[]{0x4000000004000000L});
	public static final BitSet FOLLOW_dotted_name_in_import_from3068 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_DOT_in_import_from3074 = new BitSet(new long[]{0x0000400004000000L});
	public static final BitSet FOLLOW_IMPORT_in_import_from3078 = new BitSet(new long[]{0x4800000000000000L,0x0000000000400000L});
	public static final BitSet FOLLOW_STAR_in_import_from3089 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_import_as_names_in_import_from3114 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_import_from3137 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_import_as_names_in_import_from3141 = new BitSet(new long[]{0x0000000000020000L,0x0000000000040000L});
	public static final BitSet FOLLOW_COMMA_in_import_from3143 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_RPAREN_in_import_from3146 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_import_as_name_in_import_as_names3195 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_import_as_names3198 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_import_as_name_in_import_as_names3203 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_NAME_in_import_as_name3244 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_AS_in_import_as_name3247 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_import_as_name3251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dotted_name_in_dotted_as_name3291 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_AS_in_dotted_as_name3294 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_dotted_as_name3298 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dotted_as_name_in_dotted_as_names3334 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_dotted_as_names3337 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_dotted_as_name_in_dotted_as_names3342 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_NAME_in_dotted_name3376 = new BitSet(new long[]{0x0000000004000002L});
	public static final BitSet FOLLOW_DOT_in_dotted_name3379 = new BitSet(new long[]{0x400CE764819091C0L,0x0000001660009877L});
	public static final BitSet FOLLOW_attr_in_dotted_name3383 = new BitSet(new long[]{0x0000000004000002L});
	public static final BitSet FOLLOW_GLOBAL_in_global_stmt3419 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_global_stmt3423 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_global_stmt3426 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_global_stmt3430 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_EXEC_in_exec_stmt3468 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_expr_in_exec_stmt3470 = new BitSet(new long[]{0x0000800000000002L});
	public static final BitSet FOLLOW_IN_in_exec_stmt3474 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_exec_stmt3478 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_exec_stmt3482 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_exec_stmt3486 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ASSERT_in_assert_stmt3527 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_assert_stmt3531 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_assert_stmt3535 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_assert_stmt3539 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_if_stmt_in_compound_stmt3568 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_while_stmt_in_compound_stmt3576 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_for_stmt_in_compound_stmt3584 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_try_stmt_in_compound_stmt3592 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_with_stmt_in_compound_stmt3600 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_funcdef_in_compound_stmt3617 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_classdef_in_compound_stmt3625 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IF_in_if_stmt3653 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_if_stmt3655 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_if_stmt3658 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_if_stmt3662 = new BitSet(new long[]{0x0000000080000002L,0x0000000000000020L});
	public static final BitSet FOLLOW_elif_clause_in_if_stmt3665 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_else_clause_in_elif_clause3710 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ELIF_in_elif_clause3726 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_elif_clause3728 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_elif_clause3731 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_elif_clause3733 = new BitSet(new long[]{0x0000000080000002L,0x0000000000000020L});
	public static final BitSet FOLLOW_elif_clause_in_elif_clause3745 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORELSE_in_else_clause3805 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_else_clause3807 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_else_clause3811 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WHILE_in_while_stmt3848 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_while_stmt3850 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_while_stmt3853 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_while_stmt3857 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
	public static final BitSet FOLLOW_ORELSE_in_while_stmt3861 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_while_stmt3863 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_while_stmt3867 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FOR_in_for_stmt3906 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_exprlist_in_for_stmt3908 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_IN_in_for_stmt3911 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_testlist_in_for_stmt3913 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_for_stmt3916 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_for_stmt3920 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000020L});
	public static final BitSet FOLLOW_ORELSE_in_for_stmt3932 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_for_stmt3934 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_for_stmt3938 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TRY_in_try_stmt3981 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_try_stmt3983 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_try_stmt3987 = new BitSet(new long[]{0x0000004400000000L});
	public static final BitSet FOLLOW_except_clause_in_try_stmt4000 = new BitSet(new long[]{0x0000004400000002L,0x0000000000000020L});
	public static final BitSet FOLLOW_ORELSE_in_try_stmt4004 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_try_stmt4006 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_try_stmt4010 = new BitSet(new long[]{0x0000004000000002L});
	public static final BitSet FOLLOW_FINALLY_in_try_stmt4016 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_try_stmt4018 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_try_stmt4022 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FINALLY_in_try_stmt4045 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_try_stmt4047 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_try_stmt4051 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WITH_in_with_stmt4100 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_with_item_in_with_stmt4104 = new BitSet(new long[]{0x0000000000030000L});
	public static final BitSet FOLLOW_COMMA_in_with_stmt4114 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_with_item_in_with_stmt4118 = new BitSet(new long[]{0x0000000000030000L});
	public static final BitSet FOLLOW_COLON_in_with_stmt4122 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_with_stmt4124 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_with_item4161 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_AS_in_with_item4165 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_expr_in_with_item4167 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EXCEPT_in_except_clause4206 = new BitSet(new long[]{0x583A00A000090800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_except_clause4211 = new BitSet(new long[]{0x0000000000030080L});
	public static final BitSet FOLLOW_set_in_except_clause4215 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_except_clause4225 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_except_clause4232 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_except_clause4234 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_stmt_in_suite4280 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NEWLINE_in_suite4296 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_INDENT_in_suite4298 = new BitSet(new long[]{0x583A67A001989D00L,0x0000001663009A47L});
	public static final BitSet FOLLOW_stmt_in_suite4307 = new BitSet(new long[]{0x583A67A001D89D00L,0x0000001663009A47L});
	public static final BitSet FOLLOW_DEDENT_in_suite4327 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_or_test_in_test4357 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_IF_in_test4379 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_or_test_in_test4383 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_ORELSE_in_test4386 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_test4390 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_lambdef_in_test4435 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_and_test_in_or_test4470 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_OR_in_or_test4486 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_and_test_in_or_test4490 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_not_test_in_and_test4571 = new BitSet(new long[]{0x0000000000000042L});
	public static final BitSet FOLLOW_AND_in_and_test4587 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_not_test_in_and_test4591 = new BitSet(new long[]{0x0000000000000042L});
	public static final BitSet FOLLOW_NOT_in_not_test4675 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_not_test_in_not_test4679 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_in_not_test4696 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expr_in_comparison4745 = new BitSet(new long[]{0x0604980100000002L,0x000000000000000CL});
	public static final BitSet FOLLOW_comp_op_in_comparison4759 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_expr_in_comparison4763 = new BitSet(new long[]{0x0604980100000002L,0x000000000000000CL});
	public static final BitSet FOLLOW_LESS_in_comp_op4844 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GREATER_in_comp_op4860 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EQUAL_in_comp_op4876 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GREATEREQUAL_in_comp_op4892 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LESSEQUAL_in_comp_op4908 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOTEQUAL_in_comp_op4944 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IN_in_comp_op4960 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_comp_op4976 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_IN_in_comp_op4978 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IS_in_comp_op4994 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IS_in_comp_op5010 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_NOT_in_comp_op5012 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_xor_expr_in_expr5064 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
	public static final BitSet FOLLOW_VBAR_in_expr5079 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_xor_expr_in_expr5083 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
	public static final BitSet FOLLOW_and_expr_in_xor_expr5162 = new BitSet(new long[]{0x0000000000002002L});
	public static final BitSet FOLLOW_CIRCUMFLEX_in_xor_expr5177 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_and_expr_in_xor_expr5181 = new BitSet(new long[]{0x0000000000002002L});
	public static final BitSet FOLLOW_shift_expr_in_and_expr5259 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AMPER_in_and_expr5274 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_shift_expr_in_and_expr5278 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_arith_expr_in_shift_expr5361 = new BitSet(new long[]{0x0080000000000002L,0x0000000000010000L});
	public static final BitSet FOLLOW_shift_op_in_shift_expr5375 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_arith_expr_in_shift_expr5379 = new BitSet(new long[]{0x0080000000000002L,0x0000000000010000L});
	public static final BitSet FOLLOW_LEFTSHIFT_in_shift_op5463 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_RIGHTSHIFT_in_shift_op5479 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_term_in_arith_expr5525 = new BitSet(new long[]{0x1000000000000002L,0x0000000000000200L});
	public static final BitSet FOLLOW_arith_op_in_arith_expr5538 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_term_in_arith_expr5542 = new BitSet(new long[]{0x1000000000000002L,0x0000000000000200L});
	public static final BitSet FOLLOW_PLUS_in_arith_op5650 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_arith_op5666 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_factor_in_term5712 = new BitSet(new long[]{0x0000000008000002L,0x0000000000500080L});
	public static final BitSet FOLLOW_term_op_in_term5725 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_factor_in_term5729 = new BitSet(new long[]{0x0000000008000002L,0x0000000000500080L});
	public static final BitSet FOLLOW_STAR_in_term_op5811 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SLASH_in_term_op5827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PERCENT_in_term_op5843 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLESLASH_in_term_op5859 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PLUS_in_factor5898 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_factor_in_factor5902 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MINUS_in_factor5918 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_factor_in_factor5922 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TILDE_in_factor5938 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_factor_in_factor5942 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_power_in_factor5958 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_atom_in_power5997 = new BitSet(new long[]{0x0810000024000002L});
	public static final BitSet FOLLOW_trailer_in_power6002 = new BitSet(new long[]{0x0810000024000002L});
	public static final BitSet FOLLOW_DOUBLESTAR_in_power6017 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_factor_in_power6019 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_atom6069 = new BitSet(new long[]{0x583A00A000080800L,0x0000001023040A05L});
	public static final BitSet FOLLOW_yield_expr_in_atom6087 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_testlist_gexp_in_atom6107 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_RPAREN_in_atom6150 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LBRACK_in_atom6158 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023002A05L});
	public static final BitSet FOLLOW_listmaker_in_atom6167 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_RBRACK_in_atom6210 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LCURLY_in_atom6218 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023004A05L});
	public static final BitSet FOLLOW_dictorsetmaker_in_atom6227 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_RCURLY_in_atom6271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_BACKQUOTE_in_atom6282 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_testlist_in_atom6284 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_BACKQUOTE_in_atom6289 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_name_or_print_in_atom6307 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NONE_in_atom6325 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TRUE_in_atom6344 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FALSE_in_atom6363 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_atom6382 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FLOAT_in_atom6421 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COMPLEX_in_atom6439 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_in_atom6460 = new BitSet(new long[]{0x0000000000000002L,0x0000000001000000L});
	public static final BitSet FOLLOW_test_in_listmaker6503 = new BitSet(new long[]{0x0000010000020002L});
	public static final BitSet FOLLOW_list_for_in_listmaker6515 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_listmaker6547 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_listmaker6551 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_listmaker6580 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_testlist_gexp6612 = new BitSet(new long[]{0x0000010000020002L});
	public static final BitSet FOLLOW_COMMA_in_testlist_gexp6636 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_testlist_gexp6640 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_testlist_gexp6648 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comp_for_in_testlist_gexp6704 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LAMBDA_in_lambdef6768 = new BitSet(new long[]{0x4800000020010000L,0x0000000000400000L});
	public static final BitSet FOLLOW_varargslist_in_lambdef6771 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_lambdef6775 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_lambdef6777 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_trailer6816 = new BitSet(new long[]{0x583A00A020080800L,0x0000000023440A05L});
	public static final BitSet FOLLOW_arglist_in_trailer6825 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_RPAREN_in_trailer6867 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LBRACK_in_trailer6875 = new BitSet(new long[]{0x583A00A004090800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_subscriptlist_in_trailer6877 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_RBRACK_in_trailer6880 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOT_in_trailer6896 = new BitSet(new long[]{0x400CE764819091C0L,0x0000001660009877L});
	public static final BitSet FOLLOW_attr_in_trailer6898 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subscript_in_subscriptlist6937 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_subscriptlist6949 = new BitSet(new long[]{0x583A00A004090800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_subscript_in_subscriptlist6953 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_subscriptlist6960 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOT_in_subscript7003 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_DOT_in_subscript7005 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_DOT_in_subscript7007 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_subscript7037 = new BitSet(new long[]{0x0000000000010002L});
	public static final BitSet FOLLOW_COLON_in_subscript7043 = new BitSet(new long[]{0x583A00A000090802L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_subscript7048 = new BitSet(new long[]{0x0000000000010002L});
	public static final BitSet FOLLOW_sliceop_in_subscript7054 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COLON_in_subscript7085 = new BitSet(new long[]{0x583A00A000090802L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_subscript7090 = new BitSet(new long[]{0x0000000000010002L});
	public static final BitSet FOLLOW_sliceop_in_subscript7096 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_subscript7114 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COLON_in_sliceop7151 = new BitSet(new long[]{0x583A00A000080802L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_sliceop7159 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expr_in_exprlist7230 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_exprlist7242 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_expr_in_exprlist7246 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_exprlist7252 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expr_in_exprlist7271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expr_in_del_list7309 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_del_list7321 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_expr_in_del_list7325 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_del_list7331 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_testlist7384 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_testlist7396 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_testlist7400 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_testlist7406 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_testlist7424 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_dictorsetmaker7459 = new BitSet(new long[]{0x0000010000030002L});
	public static final BitSet FOLLOW_COLON_in_dictorsetmaker7487 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_dictorsetmaker7491 = new BitSet(new long[]{0x0000010000020002L});
	public static final BitSet FOLLOW_comp_for_in_dictorsetmaker7511 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_dictorsetmaker7558 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_dictorsetmaker7562 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_dictorsetmaker7565 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_dictorsetmaker7569 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_dictorsetmaker7625 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_dictorsetmaker7629 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_dictorsetmaker7679 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comp_for_in_dictorsetmaker7694 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_decorators_in_classdef7747 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_CLASS_in_classdef7750 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_NAME_in_classdef7752 = new BitSet(new long[]{0x0800000000010000L});
	public static final BitSet FOLLOW_LPAREN_in_classdef7755 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023040A05L});
	public static final BitSet FOLLOW_testlist_in_classdef7757 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_RPAREN_in_classdef7761 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_classdef7765 = new BitSet(new long[]{0xD83A46A001181900L,0x0000001023009A47L});
	public static final BitSet FOLLOW_suite_in_classdef7767 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_argument_in_arglist7809 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_arglist7813 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_argument_in_arglist7815 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_arglist7831 = new BitSet(new long[]{0x0000000020000002L,0x0000000000400000L});
	public static final BitSet FOLLOW_STAR_in_arglist7849 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_arglist7853 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_arglist7857 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_argument_in_arglist7859 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_arglist7865 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_DOUBLESTAR_in_arglist7867 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_arglist7871 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLESTAR_in_arglist7892 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_arglist7896 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STAR_in_arglist7943 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_arglist7947 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_arglist7951 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_argument_in_arglist7953 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_COMMA_in_arglist7959 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_DOUBLESTAR_in_arglist7961 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_arglist7965 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DOUBLESTAR_in_arglist7984 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_arglist7988 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_argument8027 = new BitSet(new long[]{0x0000010000000202L});
	public static final BitSet FOLLOW_ASSIGN_in_argument8040 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_argument8044 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comp_for_in_argument8072 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_list_for_in_list_iter8137 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_list_if_in_list_iter8146 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FOR_in_list_for8172 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_exprlist_in_list_for8174 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_IN_in_list_for8177 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_testlist_in_list_for8179 = new BitSet(new long[]{0x0000210000000002L});
	public static final BitSet FOLLOW_list_iter_in_list_for8183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IF_in_list_if8213 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_list_if8215 = new BitSet(new long[]{0x0000210000000002L});
	public static final BitSet FOLLOW_list_iter_in_list_if8219 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comp_for_in_comp_iter8250 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comp_if_in_comp_iter8259 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FOR_in_comp_for8285 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A01L});
	public static final BitSet FOLLOW_exprlist_in_comp_for8287 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_IN_in_comp_for8290 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_or_test_in_comp_for8292 = new BitSet(new long[]{0x0000210000000002L});
	public static final BitSet FOLLOW_comp_iter_in_comp_for8295 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IF_in_comp_if8324 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_comp_if8326 = new BitSet(new long[]{0x0000210000000002L});
	public static final BitSet FOLLOW_comp_iter_in_comp_if8329 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_YIELD_in_yield_expr8370 = new BitSet(new long[]{0x583A00A000080802L,0x0000000023000A05L});
	public static final BitSet FOLLOW_testlist_in_yield_expr8372 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred1_Truffle1326 = new BitSet(new long[]{0x4800000000000000L});
	public static final BitSet FOLLOW_fpdef_in_synpred1_Truffle1328 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_COMMA_in_synpred1_Truffle1331 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_testlist_in_synpred2_Truffle1782 = new BitSet(new long[]{0x2100000050004020L,0x0000000100A20500L});
	public static final BitSet FOLLOW_augassign_in_synpred2_Truffle1785 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_testlist_in_synpred3_Truffle1901 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_ASSIGN_in_synpred3_Truffle1904 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_synpred4_Truffle2416 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_COMMA_in_synpred4_Truffle2419 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_synpred5_Truffle2515 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_COMMA_in_synpred5_Truffle2518 = new BitSet(new long[]{0x583A00A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_test_in_synpred5_Truffle2520 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_decorators_in_synpred6_Truffle3609 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_DEF_in_synpred6_Truffle3612 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IF_in_synpred7_Truffle4369 = new BitSet(new long[]{0x583200A000080800L,0x0000000023000A05L});
	public static final BitSet FOLLOW_or_test_in_synpred7_Truffle4371 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_ORELSE_in_synpred7_Truffle4374 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_synpred8_Truffle7024 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_COLON_in_synpred8_Truffle7027 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COLON_in_synpred9_Truffle7075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expr_in_synpred10_Truffle7220 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_COMMA_in_synpred10_Truffle7223 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_test_in_synpred11_Truffle7371 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_COMMA_in_synpred11_Truffle7374 = new BitSet(new long[]{0x0000000000000002L});
}
