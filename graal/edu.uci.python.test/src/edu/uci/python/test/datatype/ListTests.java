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
package edu.uci.python.test.datatype;

import static edu.uci.python.test.PythonTests.*;

import org.junit.*;

public class ListTests {

    @Test
    public void simple() {
        String source = "llist = [1,2,3,4]\n" + //
                        "print(llist)\n";

        assertPrints("[1, 2, 3, 4]\n", source);
    }

    @Test
    public void iterate() {
        String source = "llist = [1,2,3,4]\n" + //
                        "for i in llist:\n" + //
                        "    print(i)\n";

        assertPrints("1\n2\n3\n4\n", source);
    }

    @Test
    public void getSlice() {
        String source = "llist = [1,2,3,4]\n" + //
                        "print(llist[1:3])\n";

        assertPrints("[2, 3]\n", source);
    }

    @Test
    public void setSlice() {
        String source = "llist = [1,2,3,4]\n" + //
                        "llist[1:3] = [42,43]\n" + //
                        "print(llist)\n";

        assertPrints("[1, 42, 43, 4]\n", source);
    }

    @Test
    public void concat() {
        String source = "llist = [1,2] + [42, 43]\n" + //
                        "print(llist)\n";

        assertPrints("[1, 2, 42, 43]\n", source);
    }

    @Test
    public void append() {
        String source = "llist = [1,2,3]\n" + //
                        "print(llist.append(4))\n";

        assertPrints("[1, 2, 3, 4]\n", source);
    }

    @Test
    public void extend() {
        String source = "llist = [1,2]\n" + //
                        "print(llist.extend([3, 4]))\n";

        assertPrints("[1, 2, 3, 4]\n", source);
    }

    @Test
    public void index() {
        String source = "llist = [1,2,3,4]\n" + //
                        "print(llist.index(3))\n";

        assertPrints("2\n", source);
    }

    @Test
    public void reverse() {
        String source = "llist = [1,2,3,4,5]\n" + //
                        "print(llist.reverse())\n";

        assertPrints("[5, 4, 3, 2, 1]\n", source);
    }

    @Test
    public void mul() {
        String source = "llist = [1,2,3]\n" + //
                        "print(llist * 2)\n";

        assertPrints("[1, 2, 3, 1, 2, 3]\n", source);
    }

    @Test
    public void delItem() {
        String source = "llist = [1,2,3,4]\n" + //
                        "llist.remove(3)\n" + //
                        "print(llist)\n";

        assertPrints("[1, 2, 4]\n", source);
    }

}
