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
package org.python.ast.nodes.statements;

import java.util.List;

import org.python.ast.datatypes.PArguments;
import org.python.ast.nodes.PNode;
import org.python.ast.nodes.ReadDefaultArgumentNode;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public final class ParametersWithDefaultsNode extends ParametersNode {

    protected final ReadDefaultArgumentNode[] defaultReads;

    @Children protected final PNode[] parameters;

    @Children protected final PNode[] defaultWrites;

    public ParametersWithDefaultsNode(PNode[] parameters, List<String> paramNames, ReadDefaultArgumentNode[] defaultReads, PNode[] defaultWrites) {
        super(paramNames);
        this.defaultReads = defaultReads;
        this.parameters = adoptChildren(parameters);
        this.defaultWrites = adoptChildren(defaultWrites);
    }

    @Override
    public void evaluateDefaults(VirtualFrame frame) {
        for (ReadDefaultArgumentNode rdan : defaultReads) {
            rdan.evaluateDefault(frame);
        }
    }

    /**
     * invoked when CallTarget is called, applies runtime arguments to the newly created
     * VirtualFrame.
     */
    @ExplodeLoop
    @Override
    public void executeVoid(VirtualFrame frame) {
        PArguments args = frame.getArguments(PArguments.class);
        Object[] values = args.getArgumentsArray();

        // apply defaults
        for (PNode write : defaultWrites) {
            write.executeVoid(frame);
        }

        // update parameters
        for (int i = 0; i < parameters.length; i++) {
            if (i < values.length) {
                Object val = values[i];

                if (val != null) {
                    parameters[i].executeVoid(frame);
                }
            }
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + parameterNames + ")";
    }

    @Override
    public void visualize(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        System.out.println(this);

        level++;

        for (PNode statement : defaultReads) {
            statement.visualize(level);
        }

        for (PNode statement : parameters) {
            statement.visualize(level);
        }

        for (PNode statement : defaultWrites) {
            statement.visualize(level);
        }
    }

}
