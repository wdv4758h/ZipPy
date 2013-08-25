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
package edu.uci.python.nodes;

import java.math.*;
import java.util.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.truffle.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.datatypes.*;

@TypeSystemReference(PythonTypes.class)
public abstract class PNode extends Node implements Visualizable {

    public Object execute(VirtualFrame frame) {
        return new Exception("This node should not be executed here! Name: "/* + node.getText() */);
    }

    public int executeInt(VirtualFrame frame) throws UnexpectedResultException {
        Object o = execute(frame);
        if (o instanceof Integer) {
            return (int) o;
        } else {
            throw new UnexpectedResultException(o);
        }
    }

    public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
        Object o = execute(frame);
        if (o instanceof Double) {
            return (double) o;
        } else if (o instanceof Integer) {
            return (int) o;
        } else {
            throw new UnexpectedResultException(o);
        }
    }

    public char executeCharacter(VirtualFrame frame) throws UnexpectedResultException {
        Object o = execute(frame);
        if (o instanceof Character) {
            return (char) o;
        } else {
            throw new UnexpectedResultException(o);
        }
    }

    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        Object o = execute(frame);

        if (o == Boolean.TRUE) {
            return true;
        } else if (o == Boolean.FALSE) {
            return false;
        } else {
            throw new UnexpectedResultException(o);
        }
    }

    public BigInteger executeBigInteger(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectBigInteger(execute(frame));
    }

    public String executeString(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectString(execute(frame));
    }

    public PComplex executePComplex(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPComplex(execute(frame));
    }

    public PDictionary executePDictionary(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPDictionary(execute(frame));
    }

    public PList executePList(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPList(execute(frame));
    }

    public PTuple executePTuple(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPTuple(execute(frame));
    }

    public PSequence executePSequence(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPSequence(execute(frame));
    }

    public PSet executePSet(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPSet(execute(frame));
    }

    public PFrozenSet executePFrozenSet(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPFrozenSet(execute(frame));
    }

    public PBaseSet executePBaseSet(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPBaseSet(execute(frame));
    }

    public PIntegerArray executePIntegerArray(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPIntegerArray(execute(frame));
    }

    public PDoubleArray executePDoubleArray(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPDoubleArray(execute(frame));
    }

    public PCharArray executePCharArray(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPCharArray(execute(frame));
    }

    public PArray executePArray(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPArray(execute(frame));
    }

    public PSlice executePSlice(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPSlice(execute(frame));
    }

    public PObject executePObject(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPObject(execute(frame));
    }

    public PCallable executePCallable(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.PYTHONTYPES.expectPCallable(execute(frame));
    }

    public void executeVoid(VirtualFrame frame) {
        execute(frame);
    }

    public static final PNode DUMMY_NODE = new PNode() {

        @Override
        public Object execute(VirtualFrame frame) {
            throw new RuntimeException("This is a dummy node");
        }

    };

    public static final PNode EMPTY_NODE = new PNode() {

        @Override
        public Object execute(VirtualFrame frame) {
            return null;
        }

    };

    @Override
    public void visualize(int level) {
        for (int i = 0; i < level; i++) {
            ASTInterpreter.trace("    ");
        }

        ASTInterpreter.trace(this);
    }

    // // Dependencies for grammar

    private int charStartIndex = -1;
    private int charStopIndex = -1;
    private CommonTree node;

    public PNode() {
// if (Options.debug)
// System.out.println("new PNode: ()");
        node = new CommonTree();
    }

    public PNode(Token t) {
        if (Options.debug)
            if (t != null)
                System.out.println("new PNode: Token :" + t.toString());
        node = new CommonTree(t);
    }

    public void setToken(Token t) {
        if (Options.debug)
            System.out.println("set Token: Token :" + ((t != null) ? t.toString() : "null"));
        node = new CommonTree(t);
    }

    public void setToken(PNode tree) {
        if (Options.debug)
            System.out.println("new PNode: " + tree.toString());
        node = new CommonTree(tree.getNode());
        charStartIndex = tree.getCharStartIndex();
        charStopIndex = tree.getCharStopIndex();
    }

    public PNode(PNode tree) {
        if (Options.debug)
            System.out.println("new PNode: " + tree.toString());
        node = new CommonTree(tree.getNode());
        charStartIndex = tree.getCharStartIndex();
        charStopIndex = tree.getCharStopIndex();
    }

    public CommonTree getNode() {
        return node;
    }

    public Token getToken() {
        return node.getToken();
    }

    // public PNode dupNode()
    // {
    // return new PNode(this);
    // }

    public boolean isNil() {
        return node.isNil();
    }

    public int getAntlrType() {
        return node.getType();
    }

    public String getText() {
        return node.getText();
    }

    public int getLine() {
        if (node.getToken() == null || node.getToken().getLine() == 0) {
            if (getChildCount() > 0) {
                return getChild(0).getLine();
            }
            return 1;
        }
        return node.getToken().getLine();
    }

    public int getCharPositionInLine() {
        Token token = node.getToken();
        if (token == null || token.getCharPositionInLine() == -1) {
            if (getChildCount() > 0) {
                return getChild(0).getCharPositionInLine();
            }
            return 0;
        } else if (token != null && token.getCharPositionInLine() == -2) {
            // XXX: yucky fix because CPython's ast uses -1 as a real value
            // for char pos in certain circumstances (for example, the
            // char pos of multi-line strings. I would just use -1,
            // but ANTLR is using -1 in special ways also.
            return -1;
        }
        return token.getCharPositionInLine();
    }

    public int getTokenStartIndex() {
        return node.getTokenStartIndex();
    }

    public void setTokenStartIndex(int index) {
        node.setTokenStartIndex(index);
    }

    public int getTokenStopIndex() {
        return node.getTokenStopIndex();
    }

    public void setTokenStopIndex(int index) {
        node.setTokenStopIndex(index);
    }

    public int getCharStartIndex() {
        if (charStartIndex == -1 && node.getToken() != null) {
            return ((CommonToken) node.getToken()).getStartIndex();
        }
        return charStartIndex;
    }

    public void setCharStartIndex(int index) {
        charStartIndex = index;
    }

    /*
     * Adding one to stopIndex from Tokens. ANTLR defines the char position as being the array index
     * of the actual characters. Most tools these days define document offsets as the positions
     * between the characters. If you imagine drawing little boxes around each character and think
     * of the numbers as pointing to either the left or right side of a character's box, then 0 is
     * before the first character - and in a Document of 10 characters, position 10 is after the
     * last character.
     */
    public int getCharStopIndex() {

        if (charStopIndex == -1 && node.getToken() != null) {
            return ((CommonToken) node.getToken()).getStopIndex() + 1;
        }
        return charStopIndex;
    }

    public void setCharStopIndex(int index) {
        charStopIndex = index;
    }

    public int getChildIndex() {
        return node.getChildIndex();
    }

    // Truffle: see truffle.api.nodes.Node
    public void setParent(PNode t) {
        t.adoptChild(this);
    }

    public void setChildIndex(int index) {
        node.setChildIndex(index);
    }

    /**
     * Converts a list of Name to a dotted-name string. Because leading dots are indexable
     * identifiers (referring to parent directories in relative imports), a Name list may include
     * leading dots, but not dots between names.
     */

// @Override
// public String toString()
// {
// if (isNil())
// {
// return "None";
// }
// if (getAntlrType() == Token.INVALID_TOKEN_TYPE)
// {
// return "<errornode>";
// }
// if (node.getToken() == null)
// {
// return null;
// }
//
// return super.toString()+" : "+node.getToken().getText() + "(" + this.getLine() + "," +
// this.getCharPositionInLine() + ")";
// }

    public String toStringTree() {
        if (children == null || children.size() == 0) {
            return this.toString();// + "[" + this.info() + "]";
        }
        StringBuffer buf = new StringBuffer();
        if (!isNil()) {
            buf.append("(");
            buf.append(this.toString());// + "[" + this.info() + "]");
            buf.append(' ');
        }
        for (int i = 0; children != null && i < children.size(); i++) {
            PNode t = children.get(i);
            if (i > 0) {
                buf.append(' ');
            }
            buf.append(t.toStringTree());
        }
        if (!isNil()) {
            buf.append(")");
        }
        return buf.toString();
    }

    protected String dumpThis(String s) {
        return s;
    }

    protected String dumpThis(Object o) {
        if (o instanceof PNode) {
            return ((PNode) o).toStringTree();
        }
        return String.valueOf(o);
    }

    protected String dumpThis(Object[] s) {
        StringBuffer sb = new StringBuffer();
        if (s == null) {
            sb.append("null");
        } else {
            sb.append("(");
            for (int i = 0; i < s.length; i++) {
                if (i > 0)
                    sb.append(", ");
                sb.append(dumpThis(s[i]));
            }
            sb.append(")");
        }

        return sb.toString();
    }

    // XXX: From here down copied from org.antlr.runtime.tree.BaseTree
    protected List<PNode> children;
    protected Iterator<Node> childrenOfTruffle;

    public Iterator<Node> getIterChildreofTruffle() {
        return getChildren().iterator();
    }

    public PNode getChild(int i) {
        // if(Options.debug)
        // System.out.println("getChild is not Truffle!!!! CHANGE it ... This is:"+this.toString());
        PNode retVal = null;

        if (children != null && i < children.size()) {
            retVal = children.get(i);
        }
        return retVal;
    }

    public Node getChildTruffle(int i) {
        Node retVal = null;
        Node current = null;
        childrenOfTruffle = getChildren().iterator();
        int count = 0;

        while (childrenOfTruffle.hasNext() && count <= i) {
            current = childrenOfTruffle.next();
            count++;
        }
        if (i == count)
            retVal = current;

        return retVal;
    }

    /**
     * Get the children internal List; note that if you directly mess with the list, do so at your
     * own risk.
     */
    // Truffle see truffle.api.nodes.Node
    // public List<PNode> getChildren() {
    // return children;
    // }

    public PNode getFirstChildWithType(int type) {
        for (int i = 0; children != null && i < children.size(); i++) {
            PNode t = children.get(i);
            if (t.getAntlrType() == type) {
                return t;
            }
        }
        return null;
    }

    public int getChildCount() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }

    public void addChild(PNode t) {

        if (t == null) {
            return; // do nothing upon addChild(null)
        }
        // if(Options.debug)
        // System. out.println("addChild is not Truffle!!!! CHANGE it ... This is:"+this.toString()
        // + " Add Node:"+t.toString());
        PNode childTree = t;
        if (childTree.isNil()) { // t is an empty node possibly with children
            if (this.children != null && this.children == childTree.children) {
                throw new RuntimeException("attempt to add child list to itself");
            }
            // just add all of childTree's children to this
            if (childTree.children != null) {
                if (this.children != null) { // must copy, this has children
                                             // already
                    int n = childTree.children.size();
                    for (int i = 0; i < n; i++) {
                        PNode c = childTree.children.get(i);
                        this.children.add(c);
                        // handle double-link stuff for each child of nil root
                        c.setParent(this);
                        c.setChildIndex(children.size() - 1);
                    }
                } else {
                    // no children for this but t has children; just set pointer
                    // call general freshener routine
                    this.children = childTree.children;
                    this.freshenParentAndChildIndexes();
                }
            }
        } else { // child is not nil (don't care about children)
            if (children == null) {
                children = createChildrenList(); // create children list on
                                                 // demand
            }
            children.add(t);
            childTree.setParent(this);
            childTree.setChildIndex(children.size() - 1);
        }
    }

    public Object deleteChild(int i) {
        if (children == null) {
            return null;
        }
        PNode killed = children.remove(i);
        // walk rest and decrement their child indexes
        this.freshenParentAndChildIndexes(i);
        return killed;
    }

    /** Add all elements of kids list as children of this node. */
    public void addChildren(List<PNode> kids) {
        for (int i = 0; i < kids.size(); i++) {
            PNode t = kids.get(i);
            addChild(t);
        }
    }

    public void setChild(int i, PNode t) {
        if (t == null) {
            return;
        }
        if (t.isNil()) {
            throw new IllegalArgumentException("Can't set single child to a list");
        }
        if (children == null) {
            children = createChildrenList();
        }
        children.set(i, t);
        t.setParent(this);
        t.setChildIndex(i);
    }

    protected List<PNode> createChildrenList() {
        return new ArrayList<PNode>();
    }

    /** Set the parent and child index values for all child of t. */
    public void freshenParentAndChildIndexes() {
        freshenParentAndChildIndexes(0);
    }

    public void freshenParentAndChildIndexes(int offset) {
        int n = getChildCount();
        for (int c = offset; c < n; c++) {
            PNode child = getChild(c);
            child.setChildIndex(c);
            child.setParent(this);
        }
    }

    public int getType() {
        return node.getType();
    }

    protected FrameSlot slot;

    public void setSlot(FrameSlot slot) {
        if (Options.debug)
            System.out.println("Before set slot: " + ((this.slot != null) ? this.slot.toString() : "null"));

        this.slot = slot;

        if (Options.debug)
            System.out.println("After set slot: " + ((this.slot != null) ? this.slot.toString() : "null"));
    }

    public FrameSlot getSlot() {
        return this.slot;
    }
}
