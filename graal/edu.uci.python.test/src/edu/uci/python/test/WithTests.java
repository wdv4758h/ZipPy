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

import java.nio.file.*;

import org.junit.*;

import static edu.uci.python.test.PythonTests.*;

public class WithTests {

    @Test
    public void withWithException() {
        String source = "\n" + //
                        "a = 5\n" + //
                        "class Sample:\n" + //
                        "    def __enter__(self):\n" + //
                        "        print(\"In __enter__()\")\n" + //
                        "        return self\n" + //

                        "    def __exit__(self, type, value, trace):\n" + //
                        "         print(\"In __exit__()\");\n" + //
                        "         return 5;\n" + //

                        "    def do_something(self, x):\n" + //
                        "         raise KeyboardInterrupt\n" + //
                        "         return \"Foo\"\n" + //

                        "with Sample() as sample:\n" + //
                        "    print(\"sample:\", sample.do_something(a))\n" + //
                        "    a = 1;\n" + //
                        "#print(\"sample:\", sample.do_something(a))\n" + //

                        "print (a);\n" + //
                        "\n";

        assertPrints("In __enter__()\nIn __exit__()\n5\n", source);
    }

    @Test
    public void withByFuncCall() {
        String source = "\n" + //
                        "a = 5\n" + //
                        "class Sample:\n" + //
                        "    def __enter__(self):\n" + //
                        "        print(\"In __enter__()\")\n" + //
                        "        return self\n" + //

                        "    def __exit__(self, type, value, trace):\n" + //
                        "         print(\"In __exit__()\");\n" + //
                        "         return 5;\n" + //

                        "    def do_something(self, x):\n" + //
                        "         return \"Foo\"\n" + //

                        "def get_sample():\n" + //
                        "    return Sample()\n" + //

                        "with get_sample() as sample:\n" + //
                        "    print(\"sample:\", sample.do_something(a))\n" + //
                        "    a = 1;\n" + //
                        "print(\"sample:\", sample.do_something(a))\n" + //

                        "print (a);\n" + //

                        "\n";

        assertPrints("In __enter__()\nsample: Foo\nIn __exit__()\nsample: Foo\n1\n", source);
    }

    @Test
    public void scriptTryTest() {
        Path script = Paths.get("with-test.py");
        assertPrints("In __enter__()\nsample: Foo\nIn __exit__()\nsample: Foo\n1\n", script);
    }

}
