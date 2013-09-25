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
import org.python.google.common.collect.*;

import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.antlr.*;
import edu.uci.python.datatypes.*;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.expressions.*;
import edu.uci.python.nodes.literals.*;
import edu.uci.python.nodes.truffle.*;
import edu.uci.python.types.*;

public class Variables {

    private NodeFactory factory = null;

    public Variables(NodeFactory factory) {
        this.factory = factory;
    }

    public List<String> makeNames(List<?> names) {
        List<String> s = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            s.add(((Token) names.get(i)).getText());
        }
        return s;
    }

    public PNode makeNameNode(Token t) {
        if (t == null) {
            return null;
        }
        return makeName(t, t.getText(), ContextType.Load);
    }

    public List<PNode> makeNameNodes(List<?> names) {
        List<PNode> s = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            s.add(makeNameNode((Token) names.get(i)));
        }
        return s;
    }

    public PNode fixWriteLocalSlot(PNode broken) {
        PNode retVal = broken;
        Token writeLocalToken = null;
        FrameSlot slot = null;
        if (retVal instanceof WriteLocalNode && (retVal.getSlot() == null || ((WriteLocalNode) retVal).getSlot() == null)) {
            writeLocalToken = retVal.getToken();
            slot = ParserEnvironment.def(writeLocalToken.getText());
            retVal = GrammarUtil.make(writeLocalToken, slot, factory.createWriteLocal(PNode.DUMMY_NODE, slot));
        }
        return retVal;
    }

    public PNode fixGlobalReadToLocal(PNode tree) {
        PNode retVal = tree;
        PNode newNode = null;
        Iterable<Node> list = null;

        if (retVal instanceof ReadGlobalNode) {
            retVal = makeNameRead(retVal.getToken());
        }
        if (retVal != null) {
            list = tree.getChildren();

            for (Node n : list) {
                if (!(n instanceof ListComprehensionNode) && !(n instanceof GeneratorNode)) {
                    newNode = fixGlobalReadToLocal((PNode) n);
                    GrammarUtil.replace(n, newNode);
                }
            }
        }
        return retVal;
    }

    public PNode readLocalToGlobal(PNode readNode) {
        PNode retVal = readNode;
        FrameSlot slot = null;
        PNode newNode = null;

        if (retVal instanceof ReadLocalNode) {
            if (ParserEnvironment.scopeLevel > 1) {
                slot = ParserEnvironment.probeEnclosingScopes(retVal.getText());
            }

            if (slot != null) {
                if (slot instanceof EnvironmentFrameSlot) {
                    retVal = GrammarUtil.make(readNode.getToken(), slot, factory.createReadEnvironment(slot, ((EnvironmentFrameSlot) slot).getLevel()));
                }
            } else {
                retVal = GrammarUtil.make(readNode.getToken(), factory.createReadGlobal(retVal.getText()));
            }
        }

        if (retVal != null) {
            Iterable<Node> list = retVal.getChildren();

            for (Node n : list) {
                newNode = readLocalToGlobal((PNode) n);
                GrammarUtil.replace(n, newNode);
            }
        }
        return retVal;
    }

    public PNode writeLocalToRead(PNode node) {
        PNode retVal = node;
        FrameSlot slot = null;

        if (node instanceof WriteLocalNode) {
            slot = ParserEnvironment.find(node.getText());
            if (node.getSlot() != null) {
                retVal = factory.createReadLocal(node.getSlot());
            } else if (slot != null) {
                retVal = factory.createReadLocal(slot);
            } else {
                retVal = factory.createReadGlobal(node.getText());
            }
        }
        return GrammarUtil.make(node.getToken(), slot, retVal);
    }

    public PNode makeName(PNode tree, String id, ContextType ctx) {
        return GrammarUtil.make(tree.getToken(), makeName(tree.getToken(), id, ctx));
    }

    public PNode makeNameRead(Token t) {
        PNode retVal = null;
        FrameSlot slot = ParserEnvironment.find(t.getText());

        if (slot == null && ParserEnvironment.scopeLevel > 1) {
            slot = ParserEnvironment.probeEnclosingScopes(t.getText());
        }
        if (slot != null) {
            if (slot instanceof EnvironmentFrameSlot) {
                retVal = GrammarUtil.make(t, factory.createReadEnvironment(slot, ((EnvironmentFrameSlot) slot).getLevel()));
            } else {
                retVal = GrammarUtil.make(t, factory.createReadLocal(slot));
            }
            retVal.setSlot(slot);
        } else {
            retVal = GrammarUtil.make(t, factory.createReadGlobal(t.getText()));
        }
        return retVal;
    }

    private PNode makeNameWrite(Token t, PNode rhs) {
        PNode retVal = null;
        String id = t.getText();
        FrameSlot slot = null;

        if ((ParserEnvironment.scopeLevel == 1 && GlobalScope.getInstance().isGlobalOrBuiltin(id)) || ParserEnvironment.localGlobals.contains(id)) {
            retVal = GrammarUtil.make(t, factory.createWriteGlobal(id, rhs));
        } else {
            // slot = ParserEnvironment.def(id);
            retVal = GrammarUtil.make(t, slot, factory.createWriteLocal(rhs, slot));
        }
        return retVal;
    }

    public PNode makeName(Token t, String id, ContextType ctx) {
        PNode retVal = null;
        FrameSlot slot = null;

        if (ctx == ContextType.Param) {
            if (ParserEnvironment.scopeLevel == 1) {
                // Module global scope
                /**
                 * Variables in module's scope are also treated as globals This is why slot is not
                 * set for variables in module's scope WriteGlobal or ReadGlobal
                 */
                if (!GlobalScope.getInstance().isGlobalOrBuiltin(id)) {
                    slot = ParserEnvironment.def(id);
                }
            } else if (!ParserEnvironment.localGlobals.contains(id)) {
                // function scope
                slot = ParserEnvironment.def(id);
            }

            ReadArgumentNode right = new ReadArgumentNode(slot.getIndex());
            retVal = factory.createWriteLocal(right, slot);
        } else if (ctx != ContextType.Load) {
            retVal = makeNameWrite(t, PNode.DUMMY_NODE);
        } else {
            retVal = makeNameRead(t);
        }
        return GrammarUtil.make(t, retVal);
    }

    public PNode makeDictComp(Token t, PNode key, PNode paramValue, List<PComprehension> generators) {
        String tmp = "_{" + t.getLine() + "_" + t.getCharPositionInLine() + "}";
        ParserEnvironment.def(tmp);
        fixGlobalReadToLocal(paramValue);
        return GrammarUtil.make(t, PNode.EMPTY_NODE);
    }

    public PNode makeDict(Token t, List<PNode> keys, List<PNode> values) {
        return GrammarUtil.make(t, factory.createDictLiteral(keys, values));
    }

    public PNode makeStr(Token t, Object s) {
        return GrammarUtil.make(t, factory.createStringLiteral((String) s));
    }

    public PNode makeComprehension(Token t, PNode target, PNode iterator, List<PNode> ifs) {
        return new PComprehension(t, target, iterator, ifs);
    }

    public PNode processPComprehension(List<PComprehension> generators, PNode body) {
        PNode retVal = null;
        assert body != null;
        List<PComprehension> reversed = Lists.reverse(generators);

        for (int i = 0; i < reversed.size(); i++) {
            PComprehension comp = reversed.get(i);

            // target and iterator
            Amendable incomplete = (Amendable) fixWriteLocalSlot(comp.getInternalTarget());
            PNode target = incomplete.updateRhs(factory.createRuntimeValueNode());
            PNode iterator = comp.getInternalIter();

            // Just deal with one condition.
            List<PNode> conditions = comp.getInternalIfs();
            PNode condition = (conditions == null || conditions.isEmpty()) ? null : conditions.get(0);

            if (i == 0) {
                // inner most
                retVal = factory.createInnerComprehension(target, iterator, factory.toBooleanCastNode(condition), body);
            } else if (i < reversed.size() - 1) {
                // inner
                retVal = factory.createInnerComprehension(target, iterator, factory.toBooleanCastNode(condition), retVal);
            } else {
                // outer
                retVal = factory.createOuterComprehension(target, iterator, factory.toBooleanCastNode(condition), retVal);
            }
        }
        assert retVal != null;
        return retVal;
    }

    public PNode makeTuple(Token t, List<PNode> elts, ContextType ctx) {
        assert ctx == ContextType.Load : "Left hand side node should not reach here!";
        PNode retVal = GrammarUtil.make(t, factory.createTupleLiteral(elts));
        ((TupleLiteralNode) retVal).setElts(elts);
        return retVal;
    }

    public PNode makeSetComp(Token t, PNode paramElt, List<PComprehension> generators) {
        String tmp = "_{" + t.getLine() + "_" + t.getCharPositionInLine() + "}";
        ParserEnvironment.def(tmp);
        fixGlobalReadToLocal(paramElt);
        return GrammarUtil.make(t, PNode.EMPTY_NODE);
    }

    public PNode makeListComp(Token t, PNode paramElt, List<PComprehension> generators) {
        String tmp = "_[" + t.getLine() + "_" + t.getCharPositionInLine() + "]";
        FrameSlot slot = ParserEnvironment.def(tmp);

        PNode elt = fixGlobalReadToLocal(paramElt);
        assert generators.size() <= 1 : "More than one generator!";
        ComprehensionNode comprehension = (ComprehensionNode) processPComprehension(generators, elt);
        return GrammarUtil.make(t, slot, factory.createListComprehension(comprehension));
    }

    public PNode makeList(Token t, List<PNode> elts, ContextType ctx) {
        ListLiteralNode retVal = (ListLiteralNode) GrammarUtil.make(t, factory.createListLiteral(elts));
        retVal.setElts(elts);
        return retVal;
    }

    public PNode makeSubscript(Token t, PNode pPrimary, PNode paramSlice, ContextType ctx) {
        PNode retVal = null;
        PNode slice = paramSlice;
        PNode primary = pPrimary;
        if (ctx == ContextType.Load) {
            retVal = factory.createSubscriptLoad(primary, slice);
        } else if (ctx == ContextType.Store) {
            primary = makeNameRead(primary.getToken());
            retVal = factory.createSubscriptStore(primary, slice, PNode.DUMMY_NODE);
        } else {
            retVal = factory.createSubscriptLoad(primary, slice);
        }
        return GrammarUtil.make(t, retVal);
    }

    public PNode makeIndex(Token t, PNode value) {
        return GrammarUtil.make(t, factory.createIndex(value));
    }

    public PNode makeSliceType(Token begin, Token c1, Token c2, List<?> sltypes) {
        boolean isTuple = false;
        if (c1 != null || c2 != null) {
            isTuple = true;
        }
        PNode s = null;
        boolean extslice = false;

        if (isTuple) {
            List<PNode> etypes = new ArrayList<>();
            for (Object o : sltypes) {
                if (o instanceof IndexNode) {
                    IndexNode i = (IndexNode) o;
                    etypes.add(i.getOperand());
                } else {
                    extslice = true;
                    break;
                }
            }
            if (!extslice) {
                PNode t = makeTuple(begin, etypes, ContextType.Load);
                s = makeIndex(begin, t);
            }
        } else if (sltypes.size() == 1) {
            s = GrammarUtil.castSlice(sltypes.get(0));
        } else if (sltypes.size() != 0) {
            extslice = true;
        }
        if (extslice) {
            List<PNode> st = GrammarUtil.castSlices(sltypes);
            UnCovered.uncoveredException("External Slice");
        }
        return s;
    }

    public PNode makeSubscript(PNode lower, Token colon, PNode upper, PNode sliceop) {

        boolean isSlice = ((colon != null) || (sliceop != null));
        PNode retVal = null;
        PNode s = (lower != null) ? GrammarUtil.castExpr(lower) : null;
        PNode e = ((colon != null) && (upper != null)) ? GrammarUtil.castExpr(upper) : null;
        PNode o = (sliceop != null) ? GrammarUtil.castExpr(sliceop) : null;
        Token tok = (s == null) ? colon : s.getToken();

        if (isSlice) {
            if (s == null || s instanceof NoneLiteralNode) {
                s = factory.createIntegerLiteral(Integer.MIN_VALUE);
            }
            if (e == null || e instanceof NoneLiteralNode) {
                e = factory.createIntegerLiteral(Integer.MIN_VALUE);
            }
            if (o == null || o instanceof NoneLiteralNode) {
                o = factory.createIntegerLiteral(1);
            }
            retVal = factory.createSlice(s, e, o);

        } else {
            retVal = makeIndex(tok, s);
        }
        return GrammarUtil.make(tok, retVal);
    }

    public List<PNode> makeModuleNameNode(List<?> dots, List<PNode> names) {
        List<PNode> result = new ArrayList<>();
        if (dots != null) {
            for (Object o : dots) {
                Token tok = (Token) o;
                result.add(makeName(tok, tok.getText(), ContextType.Load));
            }
        }
        if (null != names) {
            result.addAll(names);
        }
        return result;
    }

    public List<PNode> makeDottedName(Token top, List<?> paramAttrs) {
        List<PNode> attrs = GrammarUtil.castExprs(paramAttrs);
        List<PNode> result = new ArrayList<>();
        result.add(makeName(top, top.getText(), ContextType.Load));
        if (attrs != null) {
            for (PNode attr : attrs) {
                Token token = attr.getToken();
                result.add(makeName(token, token.getText(), ContextType.Load));
            }
        }
        return result;
    }

    public PNode recurseSetContext(PNode tree, ContextType context) {
        Iterable<Node> list = null;
        PNode retVal = tree;
        PNode newNode = null;
        PNode value = null;
        PNode slice = null;
        FrameSlot slot = null;

        if (context == ContextType.Load) {
            if (tree instanceof WriteGlobalNode) {
                if (GlobalScope.getInstance().isGlobalOrBuiltin(((WriteGlobalNode) tree).getName())) {
                    retVal = GrammarUtil.make(tree.getToken(), factory.createReadGlobal(((WriteGlobalNode) tree).getName()));
                } else {
                    slot = ParserEnvironment.find(tree.getText());
                    if (tree.getSlot() != null) {
                        retVal = GrammarUtil.make(tree.getToken(), factory.createReadLocal(tree.getSlot()));
                    } else if (slot != null) {
                        retVal = GrammarUtil.make(tree.getToken(), factory.createReadLocal(slot));
                    }
                }
            } else if (tree instanceof WriteLocalNode) {
                retVal = GrammarUtil.make(tree.getToken(), writeLocalToRead(tree));
            } else if (tree instanceof SubscriptStoreNode) {
                value = ((SubscriptStoreNode) tree).getPrimary();
                slice = ((SubscriptStoreNode) tree).getSlice();
                retVal = GrammarUtil.make(tree.getToken(), factory.createSubscriptLoad(value, slice));
            } else if (tree instanceof ListComprehensionNode) {
                retVal = GrammarUtil.make(tree.getToken(), fixGlobalReadToLocal(tree));
            }
        } else {
            if (tree instanceof ReadGlobalNode) {
                retVal = GrammarUtil.make(tree.getToken(), factory.createWriteGlobal(tree.getText(), PNode.DUMMY_NODE));
            } else if (tree instanceof ReadLocalNode) {
                retVal = GrammarUtil.make(tree.getToken(), factory.createWriteLocal(PNode.DUMMY_NODE, tree.getSlot()));
            } else if (tree instanceof SubscriptLoadNode) {
                value = ((SubscriptLoadNode) tree).getPrimary();
                slice = ((SubscriptLoadNode) tree).getSlice();
                retVal = GrammarUtil.make(tree.getToken(), factory.createSubscriptStore(value, slice, PNode.DUMMY_NODE));
            }

        }

        if (tree != null && !(tree instanceof ListComprehensionNode) && !(tree instanceof GeneratorNode)) {
            // && (!(tree instanceof DictComp)) && (!(tree instanceof SetComp)))
            if (retVal == tree) {
                retVal = fixWriteLocalSlot(tree);
            }
            list = retVal.getChildren();

            for (Node n : list) {
                newNode = recurseSetContext((PNode) n, context);
                GrammarUtil.replace(n, newNode);
            }

        }
        return retVal;
    }
}
