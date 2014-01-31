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

public class TryTests {

    @Test
    public void tryNoFinallyZeroDivide() {
        String source = "try:\n" + //
                        "    result = 1 / 0\n" + //
                        "except ZeroDivisionError:\n" + //
                        "    print(\"division by zero!\")\n" + //
                        "else:\n" + //
                        "    print(\"result is \", result)\n";

        assertPrints("division by zero!\n", source);
    }

    @Test
    public void tryDivide() {
        String source = "try:\n" + //
                        "    result = 1 / 1\n" + //
                        "except ZeroDivisionError:\n" + //
                        "    print(\"division by zero!\")\n" + //
                        "else:\n" + //
                        "    print(\"result is \", result)\n" + //
                        "finally:\n" + //
                        "    print(\"executing finally clause\")\n";

        assertPrints("result is  1.0\n" + "executing finally clause\n", source);
    }

    @Test
    public void tryZeroDivide() {
        String source = "try:\n" + //
                        "    result = 1 / 0\n" + //
                        "except ZeroDivisionError:\n" + //
                        "    print(\"division by zero!\")\n" + //
                        "else:\n" + //
                        "    print(\"result is \", result)\n" + //
                        "finally:\n" + //
                        "    print(\"executing finally clause\")\n";

        assertPrints("division by zero!\n" + "executing finally clause\n", source);
    }

    @Test
    public void tryZeroDivideInsideFunction() {
        String source = "def foo():\n" + //
                        "    result = 1 / 0\n" + //
                        "\n" + //
                        "try:\n" + //
                        "    foo()\n" + //
                        "except ZeroDivisionError:\n" + //
                        "    print(\"division by zero!\")\n" + //
                        "else:\n" + //
                        "    print(\"result is \", result)\n" + //
                        "finally:\n" + //
                        "    print(\"executing finally clause\")\n";

        assertPrints("division by zero!\n" + "executing finally clause\n", source);
    }

    @Test
    public void raiseWithoutArg() {
        String source = "def divide(x, y):\n" + //
                        "    try:\n" + //
                        "        result = x / y\n" + //
                        "        raise KeyboardInterrupt\n" + //
                        "    except KeyboardInterrupt as err:\n" + //
                        "        try:\n" + //
                        "            result = x / (y+1)\n" + //
                        "            foo()\n" + //
                        "        except KeyboardInterrupt as exp:\n" + //
                        "            print(\"last KeyboardInterrupt!\",exp)\n" + //
                        "        else:\n" + //
                        "            print(\"exception result is \", result)\n" + //
                        "        finally:\n" + //
                        "            print(\"executing finally clause raised without argument\")\n" + //
                        "    except ZeroDivisionError as z:\n" + //
                        "        print(\"ZeroDivisionError!\", z)\n" + //
                        "    else:\n" + //
                        "        print(\"result is \", result)\n" + //
                        "    finally:\n" + //
                        "       print(\"executing finally clause\")\n" + //
                        "\n" + //
                        "def foo():\n" + //
                        "    raise\n" + //
                        "\n" + //
                        "divide(1,1)\n";

        assertPrints("last KeyboardInterrupt! KeyboardInterrupt\n\n" + "executing finally clause raised without argument\n" + "executing finally clause\n", source);
    }

    @Test
    public void exceptWithoutArg() {
        String source = "def foo():\n" + //
                        "    result = 1 / 0\n" + //
                        "\n" + //
                        "try:\n" + //
                        "    foo()\n" + //
                        "except:\n" + //
                        "    print(\"division by zero!\")\n" + //
                        "else:\n" + //
                        "    print(\"result is \", result)\n" + //
                        "finally:\n" + //
                        "    print(\"executing finally clause\")\n";

        assertPrints("division by zero!\n" + "executing finally clause\n", source);
    }

    @Test
    public void exceptWithoutArg2() {
        String source = "def foo():\n" + //
                        "    raise KeyboardInterrupt\n" + //

                        "def bar():\n" + //
                        "    try:\n" + //
                        "        foo()\n" + //
                        "    except AssertionError:\n" + //
                        "        print(\"EXCEPT ASSERTION ERROR\")\n" + //
                        "    except:\n" + //
                        "        print(\"EXCEPT WITHOUT AN EXPRESSION\")\n" + //

                        "bar()\n";

        assertPrints("EXCEPT WITHOUT AN EXPRESSION\n", source);
    }

    @Test
    public void raiseAssertion() {
        String source = "\n" + //
                        "def foo():\n" + //
                        "    raise AssertionError(\"Problem\")\n" + //

                        "def bar():\n" + //
                        "   try:\n" + //
                        "        foo()\n" + //
                        "   except AssertionError:\n" + //
                        "        print(\"EXCEPTED ASSERTION ERROR\")\n" + //

                        "bar()\n" + //
                        "bar()\n";

        assertPrints("EXCEPTED ASSERTION ERROR\nEXCEPTED ASSERTION ERROR\n", source);
    }

    @Test
    public void scriptTryTest() {
        Path script = Paths.get("raise_try_test.py");
        assertPrints("KeyboardInterrupt! KeyboardInterrupt\n\n" + "executing finally clause\n", script);
    }

}
