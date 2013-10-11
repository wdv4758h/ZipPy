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
package edu.uci.python.parser;

import java.util.*;

import org.python.antlr.*;
import org.python.antlr.base.*;
import org.python.core.*;

import edu.uci.python.runtime.datatypes.*;

public class TranslationUtil {

    public static List<PythonTree> castToPythonTreeList(List<stmt> argsInit) {
        List<PythonTree> pythonTreeList = new ArrayList<>();

        for (stmt s : argsInit) {
            pythonTreeList.add(s);
        }

        return pythonTreeList;
    }

    public static String isCompatibleException(RuntimeException excep) {
        String retVal = null;
        if (excep instanceof PException) {
            PException ex = (PException) excep;
            if (ex.getExceptionObject() instanceof PyType) {
                PyType thrownException = (PyType) ex.getExceptionObject();
                boolean isException = thrownException.getModule().toString().compareTo("exceptions") == 0;
                if (isException) {
                    retVal = thrownException.getName();
                }
            }
        }
        if (excep instanceof ArithmeticException && excep.getMessage().endsWith("divide by zero")) {
            retVal = "ZeroDivisionError";
        }

        return retVal;
    }

}
