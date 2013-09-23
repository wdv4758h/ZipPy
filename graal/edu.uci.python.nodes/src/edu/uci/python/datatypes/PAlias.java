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
package edu.uci.python.datatypes;

import java.util.*;

import org.antlr.runtime.*;

import com.oracle.truffle.api.frame.*;

import edu.uci.python.nodes.*;

public class PAlias extends PNode {

    private String name;
    private String asname;

    public PAlias(Token token, String name, String asname) {
        super(token);
        this.name = name;
        this.asname = asname;
    }

    public PAlias(PNode name, PNode asname) {
        super();
        this.name = name.getText();
        if (asname != null) {
            this.asname = asname.getText();
        }
    }

    public PAlias(List<PNode> nameNodes, String snameNodes, PNode asname) {
        this.name = snameNodes;
        if (asname != null) {
            this.asname = asname.getText();
        }
    }

    public String getInternalName() {
        return name;
    }

    public String getInternalAsname() {
        return asname;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        // TODO Auto-generated method stub
        return null;
    }
}
