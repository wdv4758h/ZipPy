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
// CheckStyle: stop system..print check
package edu.uci.python.antlr;

import java.util.*;

import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.impl.*;

import edu.uci.python.nodes.truffle.*;
import edu.uci.python.runtime.*;

public class ParserEnvironment {

    public static Stack<FrameDescriptor> frames = new Stack<>();
    public static FrameDescriptor globalFrame;
    public static FrameDescriptor currentFrame;
    public static int scopeLevel = 0;
    public static List<String> localGlobals = new ArrayList<>();

    public static FrameDescriptor frameDescriptor = new FrameDescriptor();

    public static final String TEMP_LOCAL_PREFIX = "temp_";
    public static final String RETURN_SLOT_ID = "<return_val>";

    public static void beginScope() {
        if (Options.debug) {
            System.out.println(">>>>======>>>>Before Begining level: " + scopeLevel + "   current frame: " + ((currentFrame != null) ? currentFrame.getSlots() : "null"));
        }
        scopeLevel++;

        if (currentFrame != null) {
            frames.push(currentFrame);
        }

        // temporary fix!
        currentFrame = new FrameDescriptor(DefaultFrameTypeConversion.getInstance());

        if (globalFrame == null) {
            globalFrame = currentFrame;
        }

        if (Options.debug) {
            System.out.println(">>>>>>>>>>>Begin level: " + scopeLevel + "   current frame: " + ((currentFrame != null) ? currentFrame.getSlots() : "null"));
        }
    }

    public static FrameDescriptor endScope() {
        if (Options.debug) {
            System.out.println("<<<<<<<<<<End level: " + scopeLevel + "   current frame: " + currentFrame.getSlots());
        }
        scopeLevel--;
        FrameDescriptor fd = currentFrame;
        if (!frames.empty()) {
            currentFrame = frames.pop();
        }

        // reset locally declared globals
        localGlobals.clear();
        return fd;
    }

    public static FrameSlot def(String name) {
        if (Options.debug) {
            System.out.println("Current frame: " + currentFrame.getSlots());
        }

        FrameSlot retVal = currentFrame.findOrAddFrameSlot(name);
        if (Options.debug) {
            System.out.println("FrameSlot Def: Name: " + name + " current frame: " + currentFrame.getSlots());
        }
        return retVal;
    }

    public static FrameSlot defGlobal(String name) {
        return globalFrame.findOrAddFrameSlot(name);
    }

    public static FrameSlot find(String name) {
        return currentFrame.findFrameSlot(name);
    }

    public static FrameSlot getReturnSlot() {
        return currentFrame.findOrAddFrameSlot(RETURN_SLOT_ID);
    }

    public static FrameSlot probeEnclosingScopes(String name) {
        FrameSlot retVal = null;
        int level = 0;
        for (int i = frames.size() - 1; i > 0 && retVal == null; i--) {
            FrameDescriptor fd = frames.get(i);
            level++;

            if (fd == globalFrame) {
                break;
            }

            FrameSlot candidate = fd.findFrameSlot(name);
            if (candidate != null) {
                retVal = EnvironmentFrameSlot.pack(candidate, level);
            }
        }

        if (Options.debug) {
            System.out.println("probeEnclosingScopes Name:" + name + " slot:" + retVal + "  Frames size: " + frames.size());
        }
        return retVal;
    }
}
