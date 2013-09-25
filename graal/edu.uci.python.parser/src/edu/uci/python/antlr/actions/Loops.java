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
package edu.uci.python.antlr.actions;

import java.util.*;

import org.antlr.runtime.*;

import com.oracle.truffle.api.frame.*;

import edu.uci.python.antlr.*;
import edu.uci.python.datatypes.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.expressions.*;
import edu.uci.python.nodes.statements.*;
import edu.uci.python.runtime.*;

public class Loops {

    private NodeFactory factory = null;
    private Variables var = null;
    private Assignments ass = null;

    public Loops(NodeFactory factory) {
        this.factory = factory;
        this.var = new Variables(factory);
        this.ass = new Assignments(factory);
    }

    public StatementNode makeWhile(Token t, PNode test, List<?> body, List<?> orelse) {
        if (test == null) {
            return ErrorHandler.errorStmt(t);
        }
        List<PNode> o = GrammarUtil.castStmts(orelse);
        List<PNode> b = GrammarUtil.castStmts(body);
        BlockNode bodyPart = factory.createBlock(b);
        BlockNode orelsePart = factory.createBlock(o);
        return (StatementNode) GrammarUtil.make(t, factory.createWhile(factory.toBooleanCastNode(test), bodyPart, orelsePart));
    }

    private StatementNode dirtySpecialization(PNode target, PNode iter, BlockNode bodyPart, BlockNode orelsePart) {
        StatementNode forNode;
        if (Options.OptimizeNode) {
            if (iter instanceof CallBuiltInWithOneArgNoKeywordNode && ((CallBuiltInWithOneArgNoKeywordNode) iter).getName().equals("range")) {
                forNode = factory.createForRangeWithOneValue(target, ((CallBuiltInWithOneArgNoKeywordNode) iter).getArgument(), bodyPart, orelsePart);
            } else if (iter instanceof CallBuiltInWithTwoArgsNoKeywordNode && ((CallBuiltInWithTwoArgsNoKeywordNode) iter).getName().equals("range")) {
                forNode = factory.createForRangeWithTwoValues(target, ((CallBuiltInWithTwoArgsNoKeywordNode) iter).getArg0(), ((CallBuiltInWithTwoArgsNoKeywordNode) iter).getArg1(), bodyPart,
                                orelsePart);
            } else {
                forNode = factory.createFor(target, iter, bodyPart, orelsePart);
            }
        } else {
            forNode = factory.createFor(target, iter, bodyPart, orelsePart);
        }
        return forNode;
    }

    public StatementNode makeFor(Token t, PNode target, PNode iter, List<?> paramBody, List<?> paramOrelse) {
        if (target == null || iter == null) {
            return ErrorHandler.errorStmt(t);
        }
        List<PNode> lhs = new ArrayList<>();
        lhs.add(target);
        List<PNode> targets = ass.walkLeftHandSideList(lhs);
        Amendable incomplete = (Amendable) targets.remove(0);

        List<PNode> b = GrammarUtil.castStmts(paramBody);
        b.addAll(0, targets);
        List<PNode> body = new ArrayList<>();

        for (PNode n : b) {
            body.add(var.fixGlobalReadToLocal(n));
        }
        BlockNode bodyPart = factory.createBlock(body);

        List<PNode> o = GrammarUtil.castStmts(paramOrelse);
        List<PNode> orelse = new ArrayList<>();
        for (PNode n : o) {
            orelse.add(var.fixGlobalReadToLocal(n));
        }
        BlockNode orelsePart = factory.createBlock(orelse);

        PNode runtimeValue = factory.createRuntimeValueNode();
        PNode iteratorWrite = incomplete.updateRhs(runtimeValue);

        return (StatementNode) GrammarUtil.make(t, dirtySpecialization(iteratorWrite, iter, bodyPart, orelsePart));
    }

    public StatementNode makeBreak(Token t) {
        return (StatementNode) GrammarUtil.make(t, factory.createBreak());
    }

    public PNode makeGeneratorExp(Token t, PNode paramElt, List<PComprehension> generators) {
        PComprehension generator = generators.get(0);
        generator.setInternalTarget(var.fixWriteLocalSlot(generator.getInternalTarget()));
        PNode elt = ass.reAssignElt(paramElt);
        ComprehensionNode comprehension = (ComprehensionNode) var.processPComprehension(generators, elt);
        GeneratorNode gnode = factory.createGenerator(comprehension, factory.createReadLocal(ParserEnvironment.getReturnSlot()));
        FrameDescriptor fd = ParserEnvironment.currentFrame;
        return GrammarUtil.make(t, factory.createGeneratorExpression(gnode, fd));
    }
}
