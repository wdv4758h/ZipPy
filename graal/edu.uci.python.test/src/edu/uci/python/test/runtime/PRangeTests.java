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
package edu.uci.python.test.runtime;

import static edu.uci.python.test.PythonTests.*;
import static org.junit.Assert.*;

import java.nio.file.*;

import org.junit.*;

import edu.uci.python.runtime.datatypes.*;
import edu.uci.python.runtime.exception.*;
import edu.uci.python.runtime.iterator.*;

public class PRangeTests {

    @Test
    public void loopWithOnlyStop() {
        PRange range = new PRange(10);
        // Iterator iter = range.__iter__().iterator();
        int index = 0;
        PIterator iter = range.__iter__();

        try {
            while (true) {
                int item = (int) (iter.__next__());
                assertEquals(index, item);
                index++;
            }
        } catch (StopIterationException e) {
            // fall through
        }

// while (iter.hasNext()) {
// int item = (int) iter.next();
// assertEquals(index, item);
// index++;
// }
    }

    @Test
    public void loopWithStep() {
        PRange range = new PRange(0, 10, 2);
        // Iterator iter = range.__iter__().iterator();
        int index = 0;
        PIterator iter = range.__iter__();

        try {
            while (true) {
                int item = (int) (iter.__next__());
                assertEquals(index, item);
                index += 2;
            }
        } catch (StopIterationException e) {
            // fall through
        }

// while (iter.hasNext()) {
// int item = (int) iter.next();
// assertEquals(index, item);
// index += 2;
// }
    }

    @Test
    public void getItem() {
        PRange range = new PRange(10);
        assertEquals(3, range.getItem(3));
    }

    @Test
    public void getItemNegative() {
        PRange range = new PRange(10);
        assertEquals(7, range.getItem(-3));
    }

    @Test
    public void forRangeLoop() {
        Path script = Paths.get("range_test.py");
        assertPrints("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]\n", script);
    }
}
