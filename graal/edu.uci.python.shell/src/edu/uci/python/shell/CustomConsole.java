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
package edu.uci.python.shell;

import java.io.*;
import java.io.BufferedReader;
import java.nio.charset.*;
import java.util.regex.*;

import org.antlr.runtime.*;
import org.python.antlr.base.*;
import org.python.core.*;
import org.python.core.io.*;
import org.python.util.*;

import edu.uci.python.antlr.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.translation.*;
import edu.uci.python.nodes.truffle.*;
import edu.uci.python.runtime.Options;

public class CustomConsole extends JLineConsole {

    private static int MARKLIMIT = 100000;

    @Override
    public void execfile(java.io.InputStream s, String name) {
        setSystemState();

        PNode root = null;
        ASTInterpreter.init((PyStringMap) getLocals(), false);

        if (Options.translationMode) {
            root = parseToAST(s, name, CompileMode.exec, cflags);
        } else {
            try {
                root = parseZipPy(s, CompileMode.exec, name, cflags);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (Options.PrintAST) {
            visualizeAST(root, "Before Specialization");
        }

        ASTInterpreter.interpret(root, false);

        if (Options.PrintAST) {
            visualizeAST(root, "After Specialization");
        }

        Py.flushLine();
    }

    /**
     * Truffle: Parse input program to AST that is ready to interpret itself.
     */
    public static PNode parseToAST(InputStream istream, String filename, CompileMode kind, CompilerFlags cflags) {
        mod node = ParserFacade.parse(istream, kind, filename, cflags);
        TranslationEnvironment environment = new TranslationEnvironment(node);
        PythonTreeProcessor ptp = new PythonTreeProcessor(environment);
        node = ptp.process(node);

        PythonTreeTranslator ptt = new PythonTreeTranslator(environment);
        PNode rootNode = ptt.translate(node);
        return rootNode;
    }

    public static PNode parseZipPy(InputStream stream, CompileMode kind, String filename, CompilerFlags cflags) throws Throwable {
        PNode tree = null;

        BufferedReader bufReader = setupBufferedReader(stream, cflags, filename);

        bufReader.mark(MARKLIMIT);

        if (kind != null) {
            CharStream cstream = new NoCloseReaderStream(bufReader);
            TruffleParser truffleparser = setupTruffleParser(cstream, filename, cflags.encoding, false);

            TruffleParser.file_input_return r = truffleparser.file_input();
            tree = r.getTree();

            if (Options.debug) {
                // CheckStyle: stop system..print check
                System.out.println("Execution Starts from here: " + tree);
                // CheckStyle: resume system..print check
            }

            return tree;
        } else {
            throw new Exception("parse kind must be eval, exec, or single");
        }
    }

    private static boolean adjustForBOM(InputStream stream) throws IOException {
        stream.mark(3);
        int ch = stream.read();
        if (ch == 0xEF) {
            if (stream.read() != 0xBB) {
                throw new ParseException("Incomplete BOM at beginning of file");
            }
            if (stream.read() != 0xBF) {
                throw new ParseException("Incomplete BOM at beginning of file");
            }
            return true;
        }
        stream.reset();
        return false;
    }

    private static String matchEncoding(String inputStr) {
        Matcher matcher = Pattern.compile("#.*coding[:=]\\s*([-\\w.]+)").matcher(inputStr);
        boolean matchFound = matcher.find();

        if (matchFound && matcher.groupCount() == 1) {
            String groupStr = matcher.group(1);
            return groupStr;
        }
        return null;
    }

    private static String findEncoding(BufferedReader br) throws IOException {
        String encoding = null;
        for (int i = 0; i < 2; i++) {
            String strLine = br.readLine();
            if (strLine == null) {
                break;
            }
            String result = matchEncoding(strLine);
            if (result != null) {
                encoding = result;
                break;
            }
        }
        return encoding;
    }

    private static BufferedReader setupBufferedReader(InputStream stream, CompilerFlags cflags, String filename) throws IOException {
        InputStream input = null;
        input = new BufferedInputStream(stream);
        boolean bom = adjustForBOM(input);
        stream.mark(MARKLIMIT);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "ISO-8859-1"), 512);
        String encoding = findEncoding(br);
        StreamIO rawIO = null;

        if (encoding != null && (encoding.equals("Latin-1") || encoding.equals("latin-1"))) {
            encoding = "ISO8859_1";
        } else {
            if (bom) {
                encoding = "utf-8";
            } else if (cflags != null && cflags.encoding != null) {
                encoding = cflags.encoding;
            }
        }
        if (cflags.source_is_utf8) {
            if (encoding != null) {
                throw new ParseException("encoding declaration in Unicode string");
            }
            encoding = "utf-8";
        }
        cflags.encoding = encoding;

        rawIO = new StreamIO(input, true);
        org.python.core.io.BufferedReader bufferedIO = new org.python.core.io.BufferedReader(rawIO, 0);
        UniversalIOWrapper textIO = new UniversalIOWrapper(bufferedIO);
        input = new TextIOInputStream(textIO);
        Charset cs = null;
        try {
            if (encoding == null) {
                cs = Charset.forName("ascii");
            } else {
                cs = Charset.forName(encoding);
            }
        } catch (Exception exc) {
            exc = new Exception("Unknown encoding: " + encoding + " " + filename);
            exc.printStackTrace();
        }
        CharsetDecoder dec = cs.newDecoder();
        dec.onMalformedInput(CodingErrorAction.REPORT);
        dec.onUnmappableCharacter(CodingErrorAction.REPORT);
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(input, dec));
        return bufReader;
    }

    protected static TruffleParser setupTruffleParser(CharStream charStream, String filename, String encoding, boolean single) {
        TruffleLexer lexer = new TruffleLexer(charStream);
        ErrorHandlerMsg errorHandlerMsg = new ErrorHandlerMsg();
        lexer.setErrorHandler(errorHandlerMsg);
        lexer.single = single;
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TruffleTokenSource indentedSource = new TruffleTokenSource(tokens, filename, single);
        tokens = new CommonTokenStream(indentedSource);
        TruffleParser parser = new TruffleParser(tokens, encoding);
        parser.setErrorHandler(errorHandlerMsg);
        parser.setTreeAdaptor(new TrufflePNodeAdaptor());
        return parser;
    }

    public static void visualizeAST(PNode tree, String phase) {
        if (!Options.PrintAST) {
            return;
        }

        // CheckStyle: stop system..print check
        System.out.println("============= " + phase + " ============= ");
        // CheckStyle: resume system..print check

        // There are two ways to visualize an AST.
        ((ModuleNode) tree).visualize(0);
    }

}
