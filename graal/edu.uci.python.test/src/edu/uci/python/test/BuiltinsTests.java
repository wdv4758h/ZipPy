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

import static edu.uci.python.test.PythonTests.*;

import java.nio.file.*;

import org.junit.*;

public class BuiltinsTests {

// @Test
    public void simple() {
        Path script = Paths.get("builtins_test.py");
        assertPrints("False\n" + "True\n" + "10\n" + "10.25\n" + "2.23606797749979\n" + "True\n" + "False\n" + "False\n" + "True\n" + "True\n" + "False\n" + "A\n" + "(2+3j)\n" + "(3.4+4.9j)\n"
                        + "(2+0j)\n" + "0j\n" + "(0, 1000)\n" + "(1, 2000)\n" + "(2, 3000)\n" + "2.0\n" + "1.23\n" + "-12345.0\n" + "0.001\n" + "1000000.0\n" + "0.0\n" + "3\n" + "2\n" + "4\n"
                        + "2147483648\n" + "0\n" + "5\n" + "3\n" + "4\n" + "2\n" + "20\n" + "20.8\n" + "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]\n" + "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n"
                        + "[0, 5, 10, 15, 20, 25]\n" + "h\n" + "e\n" + "l\n" + "l\n" + "o\n" + "10\n" + "20\n" + "30\n" + "True\n", script);
    }
}
