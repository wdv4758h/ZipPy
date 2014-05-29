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
package edu.uci.python.nodes.generator;

import java.util.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.frame.*;
import edu.uci.python.nodes.literal.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.sequence.*;

public abstract class ComprehensionNode extends FrameSlotNode {

    @Child protected PNode comprehension;

    public ComprehensionNode(FrameSlot frameSlot, PNode comprehension) {
        super(frameSlot);
        this.comprehension = comprehension;
    }

    @Override
    public final Object executeWrite(VirtualFrame frame, Object value) {
        throw new UnsupportedOperationException();
    }

    @NodeInfo(shortName = "list_comprehension")
    public static final class ListComprehensionNode extends ComprehensionNode {

        @Child protected PNode list;
        @Child protected PNode write;

        public ListComprehensionNode(FrameSlot frameSlot, PNode comprehension) {
            super(frameSlot, comprehension);
            list = new ListLiteralNode.UninitializedListLiteralNode(new PNode[]{});
            write = WriteLocalVariableNodeFactory.create(frameSlot, EmptyNode.create());
        }

        @Override
        public Object execute(VirtualFrame frame) {
            final PList newList = (PList) list.execute(frame);
            ((WriteNode) write).executeWrite(frame, newList);
            comprehension.execute(frame);
            return newList;
        }
    }

    public static final class TupleComprehensionNode extends ComprehensionNode {

        public TupleComprehensionNode(FrameSlot frameSlot, PNode comprehension) {
            super(frameSlot, comprehension);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            final ArrayList<Object> list = new ArrayList<>();
            setObject(frame, list);
            comprehension.execute(frame);
            return new PTuple(list.toArray());
        }
    }

    @NodeChild(value = "rightNode", type = PNode.class)
    public abstract static class ArrayListAddNode extends FrameSlotNode {

        public ArrayListAddNode(FrameSlot frameSlot) {
            super(frameSlot);
        }

        protected ArrayListAddNode(ArrayListAddNode node) {
            this(node.frameSlot);
        }

        @Specialization
        public Object doObject(VirtualFrame frame, Object right) {
            getList(frame).add(right);
            return right;
        }

        @SuppressWarnings("unchecked")
        private ArrayList<Object> getList(Frame frame) {
            return CompilerDirectives.unsafeCast(getObject(frame), ArrayList.class, true);
        }
    }

    public static final class SetComprehensionNode extends ComprehensionNode {

        public SetComprehensionNode(FrameSlot frameSlot, PNode comprehension) {
            super(frameSlot, comprehension);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            final TreeSet<Object> set = new TreeSet<>();
            setObject(frame, set);
            comprehension.execute(frame);
            return new PSet(set);
        }
    }

    @NodeChild(value = "rightNode", type = PNode.class)
    public abstract static class TreeSetAddNode extends FrameSlotNode {

        public TreeSetAddNode(FrameSlot frameSlot) {
            super(frameSlot);
        }

        protected TreeSetAddNode(TreeSetAddNode node) {
            this(node.frameSlot);
        }

        @Specialization
        public Object doObject(VirtualFrame frame, Object right) {
            getSet(frame).add(right);
            return right;
        }

        @SuppressWarnings("unchecked")
        private TreeSet<Object> getSet(Frame frame) {
            return CompilerDirectives.unsafeCast(getObject(frame), TreeSet.class, true);
        }
    }

    public static final class DictComprehensionNode extends ComprehensionNode {

        public DictComprehensionNode(FrameSlot frameSlot, PNode comprehension) {
            super(frameSlot, comprehension);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            final Map<Object, Object> map = new TreeMap<>();
            setObject(frame, map);
            comprehension.execute(frame);
            return new PDict(map);
        }
    }

    @NodeChildren({@NodeChild(value = "key", type = PNode.class), @NodeChild(value = "value", type = PNode.class)})
    public abstract static class MapPutNode extends FrameSlotNode {

        public MapPutNode(FrameSlot frameSlot) {
            super(frameSlot);
        }

        protected MapPutNode(MapPutNode node) {
            this(node.frameSlot);
        }

        @Specialization
        public Object doObject(VirtualFrame frame, Object key, Object value) {
            getMap(frame).put(key, value);
            return value;
        }

        @SuppressWarnings("unchecked")
        private TreeMap<Object, Object> getMap(Frame frame) {
            return CompilerDirectives.unsafeCast(getObject(frame), TreeMap.class, true);
        }
    }

    public static final class ComprehensionGuardNode extends PNode {

        private final FrameFactory frameFactory;
        private final FrameDescriptor frameDescriptor;
        @Child protected PNode comprehension;

        public ComprehensionGuardNode(FrameFactory frameFactory, FrameDescriptor frameDescriptor, PNode comprehension) {
            this.frameFactory = frameFactory;
            this.frameDescriptor = frameDescriptor;
            this.comprehension = comprehension;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            return comprehension.execute(frameFactory.create(frameDescriptor, null, PArguments.empty()));
        }
    }

}
