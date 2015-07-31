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
package edu.uci.python.test.grammar;

import static edu.uci.python.test.PythonTests.*;

import org.junit.*;

public class MultiAssignTests {

    @Test
    public void multiAssign() {
        String source = "a, b = [3, 4]\n" + //
                        "a, b = b, a\n" + //
                        "print(a, b)\n";
        assertPrints("4 3\n", source);
    }

    @Test
    public void explicitTupleAssignment() {
        String source = "(a, b) = [1, 2]\n" + //
                        "print(a, b)\n";
        assertPrints("1 2\n", source);
    }

    @Test
    public void explicitListAssignment() {
        String source = "list_l = [7, 8]\n" + //
                        "[a, b] = list_l\n" + //
                        "print(a, b)\n";
        assertPrints("7 8\n", source);
    }

    @Test
    public void nestedUnpacking() {
        String source = "(a, b), [c, d] = [[1, 2], [3, 4]]\n" + //
                        "print(a, b, c, d)\n";
        assertPrints("1 2 3 4\n", source);
    }
}
