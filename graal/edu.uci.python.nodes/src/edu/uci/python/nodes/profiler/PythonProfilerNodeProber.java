/*
 * Copyright (c) 2014, Regents of the University of California
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
package edu.uci.python.nodes.profiler;

import java.util.*;

import com.oracle.truffle.api.instrument.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.runtime.*;

/**
 * @author Gulfem
 */

public class PythonProfilerNodeProber implements ASTNodeProber {

    private final PythonContext context;
    private Map<PythonWrapperNode, ProfilerInstrument> wrapperToInstruments = new LinkedHashMap<>();
    private Map<PythonWrapperNode, ProfilerInstrument> callWrapperToInstruments = new LinkedHashMap<>();
    private Map<PythonWrapperNode, ProfilerInstrument> loopBodyWrapperToInstruments = new LinkedHashMap<>();
    private Map<PythonWrapperNode, ProfilerInstrument> ifWrapperToInstruments = new LinkedHashMap<>();
    private Map<PythonWrapperNode, ProfilerInstrument> thenWrapperToInstruments = new LinkedHashMap<>();
    private Map<PythonWrapperNode, ProfilerInstrument> elseWrapperToInstruments = new LinkedHashMap<>();

    public PythonProfilerNodeProber(PythonContext context) {
        this.context = context;
    }

    public Node probeAs(Node astNode, PhylumTag tag, Object... args) {
        return astNode;
    }

    public PythonWrapperNode probeAsStatement(PNode node) {
        PythonWrapperNode wrapper;
        if (node.getParent() != null && node.getParent() instanceof PythonWrapperNode) {
            wrapper = (PythonWrapperNode) node.getParent();
        } else {
            wrapper = new PythonWrapperNode(context, node);
            wrapper.getProbe().tagAs(StandardTag.STATEMENT);
            wrapper.assignSourceSection(node.getSourceSection());
        }

        ProfilerInstrument profilerInstrument = new ProfilerInstrument();
        wrapper.getProbe().addInstrument(profilerInstrument);
        wrapperToInstruments.put(wrapper, profilerInstrument);
        return wrapper;
    }

    public PythonWrapperNode probeAsCall(PNode node) {
        PythonWrapperNode wrapper;
        if (node.getParent() != null && node.getParent() instanceof PythonWrapperNode) {
            wrapper = (PythonWrapperNode) node.getParent();
        } else {
            wrapper = new PythonWrapperNode(context, node);
            wrapper.getProbe().tagAs(StandardTag.CALL);
            wrapper.assignSourceSection(node.getSourceSection());
        }

        ProfilerInstrument profilerInstrument = new ProfilerInstrument();
        wrapper.getProbe().addInstrument(profilerInstrument);
        callWrapperToInstruments.put(wrapper, profilerInstrument);
        return wrapper;
    }

    public PythonWrapperNode probeAsLoopBody(PNode node) {
        PythonWrapperNode wrapper;
        if (node.getParent() != null && node.getParent() instanceof PythonWrapperNode) {
            wrapper = (PythonWrapperNode) node.getParent();
        } else {
            wrapper = new PythonWrapperNode(context, node);
            wrapper.getProbe().tagAs(StandardTag.STATEMENT);
            wrapper.assignSourceSection(node.getSourceSection());
        }

        ProfilerInstrument profilerInstrument = new ProfilerInstrument();
        wrapper.getProbe().addInstrument(profilerInstrument);
        loopBodyWrapperToInstruments.put(wrapper, profilerInstrument);
        return wrapper;
    }

    public PythonWrapperNode probeAsIfStatement(PNode node) {
        PythonWrapperNode wrapper;
        if (node.getParent() != null && node.getParent() instanceof PythonWrapperNode) {
            wrapper = (PythonWrapperNode) node.getParent();
        } else {
            wrapper = new PythonWrapperNode(context, node);
            wrapper.getProbe().tagAs(StandardTag.STATEMENT);
            wrapper.assignSourceSection(node.getSourceSection());
        }

        ProfilerInstrument profilerInstrument = new ProfilerInstrument();
        wrapper.getProbe().addInstrument(profilerInstrument);
        ifWrapperToInstruments.put(wrapper, profilerInstrument);
        return wrapper;
    }

    public PythonWrapperNode probeAsThen(PNode node) {
        PythonWrapperNode wrapper;
        if (node.getParent() != null && node.getParent() instanceof PythonWrapperNode) {
            wrapper = (PythonWrapperNode) node.getParent();
        } else {
            wrapper = new PythonWrapperNode(context, node);
            wrapper.getProbe().tagAs(StandardTag.STATEMENT);
            wrapper.assignSourceSection(node.getSourceSection());
        }

        ProfilerInstrument profilerInstrument = new ProfilerInstrument();
        wrapper.getProbe().addInstrument(profilerInstrument);
        thenWrapperToInstruments.put(wrapper, profilerInstrument);
        return wrapper;
    }

    public PythonWrapperNode probeAsElse(PNode node) {
        PythonWrapperNode wrapper;
        if (node.getParent() != null && node.getParent() instanceof PythonWrapperNode) {
            wrapper = (PythonWrapperNode) node.getParent();
        } else {
            wrapper = new PythonWrapperNode(context, node);
            wrapper.getProbe().tagAs(StandardTag.STATEMENT);
            wrapper.assignSourceSection(node.getSourceSection());
        }

        ProfilerInstrument profilerInstrument = new ProfilerInstrument();
        wrapper.getProbe().addInstrument(profilerInstrument);
        elseWrapperToInstruments.put(wrapper, profilerInstrument);
        return wrapper;
    }

    public Map<PythonWrapperNode, ProfilerInstrument> getWrapperToInstruments() {
        return wrapperToInstruments;
    }

    public Map<PythonWrapperNode, ProfilerInstrument> getCallWrapperToInstruments() {
        return callWrapperToInstruments;
    }

    public Map<PythonWrapperNode, ProfilerInstrument> getLoopBodyWrapperToInstruments() {
        return loopBodyWrapperToInstruments;
    }

    public Map<PythonWrapperNode, ProfilerInstrument> getIfWrapperToInstruments() {
        return ifWrapperToInstruments;
    }

    public Map<PythonWrapperNode, ProfilerInstrument> getThenWrapperToInstruments() {
        return thenWrapperToInstruments;
    }

    public Map<PythonWrapperNode, ProfilerInstrument> getElseWrapperToInstruments() {
        return elseWrapperToInstruments;
    }

    public PythonContext getContext() {
        return context;
    }
}
