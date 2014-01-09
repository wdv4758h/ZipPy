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
package edu.uci.python.test;

import java.io.*;
import java.nio.file.*;

import com.oracle.truffle.api.*;

import static org.junit.Assert.*;
import edu.uci.python.builtins.*;
import edu.uci.python.parser.*;
import edu.uci.python.runtime.*;
import edu.uci.python.shell.*;

public class PythonTests {

    public static void assertPrints(String expected, String code) {
        final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(byteArray);

// String path = "../../graal/edu.uci.python.test/src/tests";
        PythonContext context = getContext(printStream, System.err);
        Source source = context.getSourceManager().get("(test)", code);
        RunScript.runScript(new String[0], source, context);
        String result = byteArray.toString().replaceAll("\r\n", "\n");
        assertEquals(expected, result);
    }

    public static String parseTest(String code) {
        final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(byteArray);

// String path = "../../graal/edu.uci.python.test/src/tests";
        PythonContext context = getContext(printStream, System.err);
        Source source = context.getSourceManager().get("(test)", code);
        new CustomConsole().parseFile(context, source);
        return byteArray.toString().replaceAll("\r\n", "\n");
    }

    public static void assertError(String expected, String code) {
        final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(byteArray);
        String error = "no error!";

        try {
// String path = "../../graal/edu.uci.python.test/src/tests";
            PythonContext context = getContext(System.out, printStream);
            Source source = context.getSourceManager().get("(test)", code);
            RunScript.runTrowableScript(new String[0], source, context);
        } catch (Throwable err) {
            error = err.toString();
        }

        assertEquals(expected, error);
    }

    public static void assertPrints(String expected, Path scriptName) {
        final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(byteArray);

        String path = null;
        if (Files.isDirectory(Paths.get("graal/edu.uci.python.test/src/tests"))) {
            path = "graal/edu.uci.python.test/src/tests";
        } else if (Files.isDirectory(Paths.get("../../graal/edu.uci.python.test/src/tests"))) {
            path = "../../graal/edu.uci.python.test/src/tests";
        } else {
            throw new RuntimeException("Unable to locate edu.uci.python.test/src/tests/");
        }

        PythonContext context = getContext(printStream, System.err);
        Source source = context.getSourceManager().get(path + File.separatorChar + scriptName.toString());
        RunScript.runScript(new String[0], source, context);
        String result = byteArray.toString().replaceAll("\r\n", "\n");
        assertEquals(expected, result);
    }

    public static PythonContext getContext() {
        PythonOptions opts = new PythonOptions();
        PythonContext context = new PythonContext(opts, new PythonDefaultBuiltinsLookup(), new PythonParserImpl());
        return context;
    }

    public static PythonContext getContext(PrintStream stdout, PrintStream stderr) {
        PythonOptions opts = new PythonOptions();
        opts.setStandardOut(stdout);
        opts.setStandardErr(stderr);

        PythonContext context = new PythonContext(opts, new PythonDefaultBuiltinsLookup(), new PythonParserImpl());
        return context;
    }
}
