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

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;

import edu.uci.python.nodes.*;

public abstract class FrameSlotNode extends PNode {

    protected final FrameSlot frameSlot;

    public FrameSlotNode(FrameSlot slot) {
        this.frameSlot = slot;
    }

    public final FrameSlot getSlot() {
        return frameSlot;
    }

    protected final void setObject(Frame frame, Object value) {
        frame.setObject(frameSlot, value);
    }

    protected final int getInteger(Frame frame) {
        try {
            return frame.getInt(frameSlot);
        } catch (FrameSlotTypeException ex) {
            throw new IllegalStateException();
        }
    }

    protected final boolean getBoolean(Frame frame) {
        try {
            return frame.getBoolean(frameSlot);
        } catch (FrameSlotTypeException ex) {
            throw new IllegalStateException();
        }
    }

    protected final double getDouble(Frame frame) {
        try {
            return frame.getDouble(frameSlot);
        } catch (FrameSlotTypeException ex) {
            throw new IllegalStateException();
        }
    }

    protected final Object getObject(Frame frame) {
        try {
            return frame.getObject(frameSlot);
        } catch (FrameSlotTypeException e) {
            throw new IllegalStateException();
        }
    }

    protected final boolean isNotIllegal() {
        return frameSlot.getKind() != FrameSlotKind.Illegal;
    }

    protected final boolean isNoneKind() {
        return isKind(FrameSlotKind.None);
    }

    protected final boolean isBooleanKind() {
        return isKind(FrameSlotKind.Boolean);
    }

    protected final boolean isIntegerKind() {
        return isKind(FrameSlotKind.Int) || booleanToInt();
    }

    protected final boolean isDoubleKind() {
        if (isKind(FrameSlotKind.Double) || intToDouble()) {
            return true;
        }
        if (frameSlot.getKind() != FrameSlotKind.Double) {
            CompilerDirectives.transferToInterpreter();
            frameSlot.setKind(FrameSlotKind.Double);
        }
        return true;
    }

    protected final boolean isIntOrObjectKind() {
        return isKind(FrameSlotKind.Int) || isKind(FrameSlotKind.Object);
    }

    protected final boolean isObjectKind() {
        if (frameSlot.getKind() != FrameSlotKind.Object) {
            CompilerDirectives.transferToInterpreter();
            frameSlot.setKind(FrameSlotKind.Object);
        }
        return true;
    }

    private boolean isKind(FrameSlotKind kind) {
        return frameSlot.getKind() == kind || initialSetKind(kind) || noneToKind(kind);
    }

    private boolean initialSetKind(FrameSlotKind kind) {
        if (frameSlot.getKind() == FrameSlotKind.Illegal) {
            CompilerDirectives.transferToInterpreter();
            frameSlot.setKind(kind);
            return true;
        }
        return false;
    }

    private boolean noneToKind(FrameSlotKind kind) {
        if (frameSlot.getKind() == FrameSlotKind.None) {
            CompilerDirectives.transferToInterpreter();
            frameSlot.setKind(kind);
            return true;
        }
        return false;
    }

    private boolean booleanToInt() {
        if (frameSlot.getKind() == FrameSlotKind.Boolean) {
            CompilerDirectives.transferToInterpreter();
            frameSlot.setKind(FrameSlotKind.Int);
            return true;
        }
        return false;
    }

    private boolean intToDouble() {
        if (frameSlot.getKind() == FrameSlotKind.Int) {
            CompilerDirectives.transferToInterpreter();
            frameSlot.setKind(FrameSlotKind.Double);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + frameSlot + ")";
    }

    /**
     * To be Overridden by {@link WriteNode}s. {@link ReadNode}s should throw Unsupported Error.
     */
    public abstract Object executeWrite(VirtualFrame frame, Object value);

}