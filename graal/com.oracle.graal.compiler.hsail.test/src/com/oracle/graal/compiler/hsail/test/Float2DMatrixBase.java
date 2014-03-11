/*
 * Copyright (c) 2009, 2012, Oracle and/or its affiliates. All rights reserved.
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

package com.oracle.graal.compiler.hsail.test;

import com.oracle.graal.compiler.hsail.test.infra.GraalKernelTester;

/**
 * Base class used by other Float2DMatrix tests.
 */
public abstract class Float2DMatrixBase extends GraalKernelTester {

    float[][] matrixA;
    float[][] matrixB;
    @Result float[][] outMatrix;

    public void setupArrays(int range) {
        matrixA = new float[range][];
        matrixB = new float[range][];
        outMatrix = new float[range][];
        for (int j = 0; j < range; j++) {
            matrixA[j] = new float[range];
            matrixB[j] = new float[range];
            outMatrix[j] = new float[range];
            for (int k = 0; k < range; k++) {
                matrixA[j][k] = (j + k) % 7;
                matrixB[j][k] = (j + k + 1) % 8;
            }
        }
    }
}
