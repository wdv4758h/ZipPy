/*
 * Copyright (c) 2009, 2014, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.nodes.java;

import com.oracle.graal.api.meta.*;
import com.oracle.graal.graph.spi.*;
import com.oracle.graal.nodeinfo.*;
import com.oracle.graal.nodes.*;
import com.oracle.graal.nodes.spi.*;
import com.oracle.graal.nodes.type.*;

/**
 * Implements a type check where the type being checked is loaded at runtime. This is used, for
 * instance, to implement an object array store check.
 */
@NodeInfo
public class CheckCastDynamicNode extends FixedWithNextNode implements Canonicalizable.Binary<ValueNode>, Lowerable {

    @Input private ValueNode object;
    @Input private ValueNode hub;

    /**
     * Determines the exception thrown by this node if the check fails: {@link ClassCastException}
     * if false; {@link ArrayStoreException} if true.
     */
    private final boolean forStoreCheck;

    /**
     * @param hub the type being cast to
     * @param object the object being cast
     */
    public static CheckCastDynamicNode create(ValueNode hub, ValueNode object, boolean forStoreCheck) {
        return new CheckCastDynamicNodeGen(hub, object, forStoreCheck);
    }

    CheckCastDynamicNode(ValueNode hub, ValueNode object, boolean forStoreCheck) {
        super(object.stamp());
        this.hub = hub;
        this.object = object;
        this.forStoreCheck = forStoreCheck;
    }

    public ValueNode object() {
        return object;
    }

    /**
     * Gets the runtime-loaded type being cast to.
     */
    public ValueNode hub() {
        return hub;
    }

    public ValueNode getX() {
        return object;
    }

    public ValueNode getY() {
        return hub;
    }

    public boolean isForStoreCheck() {
        return forStoreCheck;
    }

    @Override
    public void lower(LoweringTool tool) {
        tool.getLowerer().lower(this, tool);
    }

    @Override
    public boolean inferStamp() {
        return updateStamp(object().stamp());
    }

    @Override
    public ValueNode canonical(CanonicalizerTool tool, ValueNode forObject, ValueNode forHub) {
        if (StampTool.isObjectAlwaysNull(forObject)) {
            return forObject;
        }
        if (forHub.isConstant()) {
            ResolvedJavaType t = tool.getConstantReflection().asJavaType(forHub.asConstant());
            if (t != null) {
                return CheckCastNode.create(t, forObject, null, forStoreCheck);
            }
        }
        return this;
    }

    @NodeIntrinsic
    public static native <T> T checkCastDynamic(Class<T> type, Object object, @ConstantNodeParameter boolean forStoreCheck);
}
