/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.graal.compiler;

import java.util.concurrent.*;

import com.oracle.graal.debug.*;

/**
 * Facility for creating {@linkplain CompilerThread compiler threads}.
 */
public class CompilerThreadFactory implements ThreadFactory {

    /**
     * Capability to get a thread-local debug configuration for the current thread.
     */
    public interface DebugConfigAccess {
        /**
         * Get a thread-local debug configuration for the current thread. This will be null if
         * debugging is {@linkplain Debug#isEnabled() disabled}.
         */
        GraalDebugConfig getDebugConfig();
    }

    protected final String threadNamePrefix;
    protected final DebugConfigAccess debugConfigAccess;

    public CompilerThreadFactory(String threadNamePrefix, DebugConfigAccess debugConfigAccess) {
        this.threadNamePrefix = threadNamePrefix;
        this.debugConfigAccess = debugConfigAccess;
    }

    public Thread newThread(Runnable r) {
        return new CompilerThread(r, threadNamePrefix, debugConfigAccess);
    }

    /**
     * A compiler thread is a daemon thread that runs at {@link Thread#MAX_PRIORITY} and executes in
     * the context of a thread-local {@linkplain GraalDebugConfig debug configuration}.
     */
    public static class CompilerThread extends Thread {

        private final DebugConfigAccess debugConfigAccess;

        public CompilerThread(Runnable r, String namePrefix, DebugConfigAccess debugConfigAccess) {
            super(r);
            this.setName(namePrefix + "-" + this.getId());
            this.setPriority(Thread.MAX_PRIORITY);
            this.setDaemon(true);
            this.debugConfigAccess = debugConfigAccess;
        }

        @Override
        public void run() {
            GraalDebugConfig debugConfig = debugConfigAccess.getDebugConfig();
            try {
                super.run();
            } finally {
                if (debugConfig != null) {
                    for (DebugDumpHandler dumpHandler : debugConfig.dumpHandlers()) {
                        try {
                            dumpHandler.close();
                        } catch (Throwable t) {
                        }
                    }
                }
            }
        }
    }
}
