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
package edu.uci.python.nodes.access;

import org.python.core.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.api.nodes.NodeInfo.Kind;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.truffle.*;

public abstract class ReadLocalVariableNode extends FrameSlotNode implements ReadNode {

    @Child protected ReadLocalVariableNode next;

    public ReadLocalVariableNode(FrameSlot frameSlot) {
        super(frameSlot);
    }

    protected ReadLocalVariableNode(ReadLocalVariableNode prev) {
        this(prev.frameSlot);
    }

    public static ReadLocalVariableNode create(FrameSlot frameSlot) {
        return new ReadLocalVariableUninitializedNode(frameSlot);
    }

    @Override
    public PNode makeWriteNode(PNode rhs) {
        return WriteLocalVariableNodeFactory.create(frameSlot, rhs);
    }

    @Override
    public final boolean getBoolean(Frame frame) {
        try {
            return frame.getBoolean(frameSlot);
        } catch (FrameSlotTypeException ex) {
            throw new IllegalStateException();
        }
    }

    @Override
    public final int getInteger(Frame frame) {
        try {
            return frame.getInt(frameSlot);
        } catch (FrameSlotTypeException ex) {
            throw new IllegalStateException();
        }
    }

    @Override
    public final double getDouble(Frame frame) {
        try {
            return frame.getDouble(frameSlot);
        } catch (FrameSlotTypeException ex) {
            throw new IllegalStateException();
        }
    }

    @Override
    public final Object executeWrite(VirtualFrame frame, Object value) {
        throw new UnsupportedOperationException();
    }

    protected Object executeNext(VirtualFrame frame) {
        if (next == null) {
            CompilerDirectives.transferToInterpreter();
            next = adoptChild(new ReadLocalVariableUninitializedNode(frameSlot));
        }

        return next.execute(frame);
    }

    @NodeInfo(kind = Kind.UNINITIALIZED)
    private static final class ReadLocalVariableUninitializedNode extends ReadLocalVariableNode {

        ReadLocalVariableUninitializedNode(FrameSlot slot) {
            super(slot);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            CompilerDirectives.transferToInterpreter();
            ReadLocalVariableNode readNode;

            if (!isNotIllegal() && !frameSlot.getIdentifier().equals("<return_val>")) {
                throw Py.UnboundLocalError("local variable '" + frameSlot.getIdentifier() + "' referenced before assignment");
            }

            if (frame.isObject(frameSlot)) {
                readNode = new ReadLocalVariableObjectNode(this);
            } else if (frame.isInt(frameSlot)) {
                readNode = new ReadLocalVariableIntNode(this);
            } else if (frame.isDouble(frameSlot)) {
                readNode = new ReadLocalVariableDoubleNode(this);
            } else if (frame.isBoolean(frameSlot)) {
                readNode = new ReadLocalVariableBooleanNode(this);
            } else {
                throw new UnsupportedOperationException("frame slot kind?");
            }

            return replace(readNode).execute(frame);
        }
    }

    @NodeInfo(kind = Kind.SPECIALIZED)
    private static final class ReadLocalVariableBooleanNode extends ReadLocalVariableNode {

        ReadLocalVariableBooleanNode(ReadLocalVariableNode copy) {
            super(copy);
        }

        @Override
        public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
            if (frameSlot.getKind() == FrameSlotKind.Boolean) {
                return getBoolean(frame);
            } else {
                return PythonTypesGen.PYTHONTYPES.expectBoolean(executeNext(frame));
            }
        }

        @Override
        public Object execute(VirtualFrame frame) {
            if (frameSlot.getKind() == FrameSlotKind.Boolean) {
                return getBoolean(frame);
            } else {
                return executeNext(frame);
            }
        }
    }

    @NodeInfo(kind = Kind.SPECIALIZED)
    private static final class ReadLocalVariableIntNode extends ReadLocalVariableNode {

        ReadLocalVariableIntNode(ReadLocalVariableNode copy) {
            super(copy);
        }

        @Override
        public int executeInt(VirtualFrame frame) throws UnexpectedResultException {
            if (frameSlot.getKind() == FrameSlotKind.Int) {
                return getInteger(frame);
            } else {
                return PythonTypesGen.PYTHONTYPES.expectInteger(executeNext(frame));
            }
        }

        @Override
        public Object execute(VirtualFrame frame) {
            if (frameSlot.getKind() == FrameSlotKind.Int) {
                return getInteger(frame);
            } else {
                return executeNext(frame);
            }
        }
    }

    @NodeInfo(kind = Kind.SPECIALIZED)
    private static final class ReadLocalVariableDoubleNode extends ReadLocalVariableNode {

        ReadLocalVariableDoubleNode(ReadLocalVariableNode copy) {
            super(copy);
        }

        @Override
        public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
            if (frameSlot.getKind() == FrameSlotKind.Double) {
                return getDouble(frame);
            } else {
                return PythonTypesGen.PYTHONTYPES.expectDouble(executeNext(frame));
            }
        }

        @Override
        public Object execute(VirtualFrame frame) {
            if (frameSlot.getKind() == FrameSlotKind.Double) {
                return getDouble(frame);
            } else {
                return executeNext(frame);
            }
        }
    }

    @NodeInfo(kind = Kind.SPECIALIZED)
    private static final class ReadLocalVariableObjectNode extends ReadLocalVariableNode {

        ReadLocalVariableObjectNode(ReadLocalVariableNode copy) {
            super(copy);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            if (frame.isObject(frameSlot)) {
                return getObject(frame);
            } else {
                return executeNext(frame);
            }
        }
    }

}
