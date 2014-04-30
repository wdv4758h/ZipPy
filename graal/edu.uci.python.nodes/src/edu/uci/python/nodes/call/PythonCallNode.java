/*
 * Copyright (c) 2014, Regents of the University of California
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
package edu.uci.python.nodes.call;

import static edu.uci.python.nodes.truffle.PythonTypesUtil.*;
import static edu.uci.python.nodes.call.PythonCallUtil.*;

import org.python.core.*;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;

import edu.uci.python.nodes.*;
import edu.uci.python.nodes.literal.*;
import edu.uci.python.nodes.object.*;
import edu.uci.python.nodes.truffle.*;
import edu.uci.python.runtime.*;
import edu.uci.python.runtime.builtin.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.object.*;
import edu.uci.python.runtime.standardtype.*;

public abstract class PythonCallNode extends PNode {

    @Child protected PNode primaryNode;
    @Child protected PNode calleeNode;
    @Children protected final PNode[] argumentNodes;
    @Children protected final PNode[] keywordNodes;

    protected final String calleeName;
    protected final boolean passPrimaryAsTheFirstArgument;
    protected final PythonContext context;

    public PythonCallNode(PythonContext context, String calleeName, PNode primary, PNode callee, PNode[] arguments, PNode[] keywords, boolean passPrimary) {
        this.context = context;
        this.calleeName = calleeName;
        this.primaryNode = primary;
        this.calleeNode = callee;
        this.argumentNodes = arguments;
        this.keywordNodes = keywords;
        this.passPrimaryAsTheFirstArgument = passPrimary;
    }

    public static PythonCallNode create(PythonContext context, PNode calleeNode, PNode[] argumentNodes, PNode[] keywords) {
        PNode primaryNode;
        String calleeName;

        if (calleeNode instanceof HasPrimaryNode) {
            HasPrimaryNode hasPrimary = (HasPrimaryNode) calleeNode;
            primaryNode = NodeUtil.cloneNode(hasPrimary.extractPrimary());
            calleeName = ((HasPrimaryNode) calleeNode).getAttributeId();
        } else {
            primaryNode = EmptyNode.INSTANCE;
            calleeName = "~unknown";
        }

        return new UninitializedCallNode(context, primaryNode, calleeName, calleeNode, argumentNodes, keywords);
    }

    /**
     * Pack primary into the evaluated arguments array if passPrimary is true.
     *
     */
    @ExplodeLoop
    protected static final Object[] executeArguments(VirtualFrame frame, boolean passPrimary, Object primary, PNode[] arguments) {
        final int length = passPrimary ? arguments.length + 1 : arguments.length;
        final Object[] evaluated = new Object[length];
        final int offset;

        if (passPrimary) {
            evaluated[0] = primary;
            offset = 1;
        } else {
            offset = 0;
        }

        for (int i = 0; i < arguments.length; i++) {
            evaluated[i + offset] = arguments[i].execute(frame);
        }

        return evaluated;
    }

    @ExplodeLoop
    public static final Object[] executeArguments(VirtualFrame frame, PNode[] arguments) {
        Object[] evaluated = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            evaluated[i] = arguments[i].execute(frame);
        }

        return evaluated;
    }

    @ExplodeLoop
    protected static final PKeyword[] executeKeywordArguments(VirtualFrame frame, PNode[] arguments) {
        PKeyword[] evaluated = arguments.length == 0 ? PKeyword.EMPTY_KEYWORDS : new PKeyword[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            evaluated[i] = (PKeyword) arguments[i].execute(frame);
        }

        return evaluated;
    }

    protected Object rewriteAndExecuteCall(VirtualFrame frame, Object primary, Object callee) {
        if (callee instanceof PyObject) {
            PyObject pyobj = (PyObject) callee;
            logJythonRuntime(pyobj);
            return replace(new CallJythonNode(context, pyobj.toString(), primaryNode, calleeNode, argumentNodes, keywordNodes)).executeCall(frame, pyobj);
        }

        PythonCallable callable;
        try {
            callable = PythonTypesGen.PYTHONTYPES.expectPythonCallable(callee);
        } catch (UnexpectedResultException e) {
            throw Py.TypeError("'" + getPythonTypeName(e.getResult()) + "' object is not callable");
        }

        /**
         * Non built-in constructors use CallConstructorNode. <br>
         * Built-in constructors use regular BoxedCallNode with no special calling convention.
         */
        if (isPrimaryBoxed(primary) && callee instanceof PythonClass && !(callee instanceof PythonBuiltinClass)) {
            PythonClass clazz = (PythonClass) callee;
            CallDispatchBoxedNode dispatch = CallDispatchBoxedNode.create(context, (PythonObject) primary, calleeName, callable, NodeUtil.cloneNode(calleeNode), PKeyword.EMPTY_KEYWORDS);
            CallConstructorNode specialized = new CallConstructorNode(context, callable.getName(), primaryNode, calleeNode, argumentNodes, keywordNodes, dispatch);
            return replace(specialized).executeCall(frame, (PythonObject) primary, clazz);
        }

        boolean passPrimaryAsArgument = haveToPassPrimary(primary);
        callable.arityCheck(passPrimaryAsArgument ? argumentNodes.length + 1 : argumentNodes.length, keywordNodes.length, getKeywordNames());
        Object[] arguments = executeArguments(frame, passPrimaryAsArgument, primary, argumentNodes);
        PKeyword[] keywords = PythonCallNode.executeKeywordArguments(frame, keywordNodes);

        if (isPrimaryNone(primary)) {
            CallDispatchNoneNode dispatch = CallDispatchNoneNode.create(callable, keywords);
            replace(new NoneCallNode(context, callable.getName(), primaryNode, calleeNode, argumentNodes, keywordNodes, dispatch));
            return dispatch.executeCall(frame, callable, arguments, keywords);
        }

        if (isPrimaryBoxed(primary)) {
            CallDispatchBoxedNode dispatch = CallDispatchBoxedNode.create(context, (PythonObject) primary, calleeName, callable, calleeNode, keywords);
            replace(new BoxedCallNode(context, callable.getName(), primaryNode, calleeNode, argumentNodes, keywordNodes, dispatch, passPrimaryAsArgument));
            return dispatch.executeCall(frame, (PythonObject) primary, arguments, keywords);
        }

        CallDispatchUnboxedNode dispatch = CallDispatchUnboxedNode.create(primary, callable, calleeNode, keywords);
        replace(new UnboxedCallNode(context, callable.getName(), primaryNode, calleeNode, argumentNodes, keywordNodes, dispatch, passPrimaryAsArgument));
        return dispatch.executeCall(frame, primary, arguments, keywords);
    }

    private String[] getKeywordNames() {
        String[] keywordNames = new String[keywordNodes.length];

        for (int i = 0; i < keywordNodes.length; i++) {
            KeywordLiteralNode keywordLiteral = (KeywordLiteralNode) keywordNodes[i];
            keywordNames[i] = keywordLiteral.getName();
        }

        return keywordNames;
    }

    private static boolean isPrimaryBoxed(Object primary) {
        return primary instanceof PythonObject;
    }

    private boolean isPrimaryNone(Object primary) {
        return primaryNode == EmptyNode.INSTANCE && primary == PNone.NONE;
    }

    private boolean haveToPassPrimary(Object primary) {
        return !isPrimaryNone(primary) && !(primary instanceof PythonClass) && !(primary instanceof PythonModule) && !(primary instanceof PyObject);
    }

    public static final class BoxedCallNode extends PythonCallNode {

        @Child protected CallDispatchBoxedNode dispatchBoxedNode;

        public BoxedCallNode(PythonContext context, String calleeName, PNode primary, PNode callee, PNode[] arguments, PNode[] keywords, CallDispatchBoxedNode dispatch, boolean passPrimary) {
            super(context, calleeName, primary, callee, arguments, keywords, passPrimary);
            dispatchBoxedNode = dispatch;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            PythonObject primary;

            try {
                primary = primaryNode.executePythonObject(frame);
            } catch (UnexpectedResultException e) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new IllegalStateException();
            }

            Object[] arguments = executeArguments(frame, passPrimaryAsTheFirstArgument, primary, argumentNodes);
            PKeyword[] keywords = PythonCallNode.executeKeywordArguments(frame, keywordNodes);
            return dispatchBoxedNode.executeCall(frame, primary, arguments, keywords);
        }
    }

    public static final class UnboxedCallNode extends PythonCallNode {

        @Child protected CallDispatchUnboxedNode dispatchNode;

        public UnboxedCallNode(PythonContext context, String calleeName, PNode primary, PNode callee, PNode[] arguments, PNode[] keywords, CallDispatchUnboxedNode dispatch, boolean passPrimary) {
            super(context, calleeName, primary, callee, arguments, keywords, passPrimary);
            dispatchNode = dispatch;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            Object primary = primaryNode.execute(frame);
            Object[] arguments = executeArguments(frame, passPrimaryAsTheFirstArgument, primary, argumentNodes);
            PKeyword[] keywords = PythonCallNode.executeKeywordArguments(frame, keywordNodes);
            return dispatchNode.executeCall(frame, primary, arguments, keywords);
        }
    }

    public static final class NoneCallNode extends PythonCallNode {

        @Child protected CallDispatchNoneNode dispatchNode;

        public NoneCallNode(PythonContext context, String calleeName, PNode primary, PNode callee, PNode[] arguments, PNode[] keywords, CallDispatchNoneNode dispatch) {
            super(context, calleeName, primary, callee, arguments, keywords, false);
            this.dispatchNode = dispatch;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            PythonCallable callee;

            try {
                callee = calleeNode.executePythonCallable(frame);
            } catch (UnexpectedResultException e) {
                throw new IllegalStateException("Call to " + e.getMessage() + " not supported.");
            }

            Object[] arguments = executeArguments(frame, argumentNodes);
            PKeyword[] keywords = PythonCallNode.executeKeywordArguments(frame, keywordNodes);
            return dispatchNode.executeCall(frame, callee, arguments, keywords);
        }
    }

    public static final class CallConstructorNode extends PythonCallNode {

        @Child protected CallDispatchBoxedNode dispatchNode;

        public CallConstructorNode(PythonContext context, String calleeName, PNode primary, PNode callee, PNode[] arguments, PNode[] keywords, CallDispatchBoxedNode dispatch) {
            super(context, calleeName, primary, callee, arguments, keywords, true);
            dispatchNode = dispatch;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            PythonObject primary;

            try {
                primary = primaryNode.executePythonObject(frame);
            } catch (UnexpectedResultException e) {
                throw new IllegalStateException();
            }

            PythonClass callee;

            try {
                callee = calleeNode.executePythonClass(frame);
            } catch (UnexpectedResultException e) {
                throw new IllegalStateException("Call to " + e.getMessage() + " not supported.");
            }

            return executeCall(frame, primary, callee);
        }

        protected Object executeCall(VirtualFrame frame, PythonObject primary, PythonClass clazz) {
            PythonObject newInstance = new PythonObject(clazz);
            Object[] arguments = executeArguments(frame, true, newInstance, argumentNodes);
            PKeyword[] keywords = PythonCallNode.executeKeywordArguments(frame, keywordNodes);
            dispatchNode.executeCall(frame, primary, arguments, keywords);
            newInstance.switchToPrivateLayout();
            return newInstance;
        }
    }

    public static final class CallJythonNode extends PythonCallNode {

        public CallJythonNode(PythonContext context, String calleeName, PNode primary, PNode callee, PNode[] arguments, PNode[] keywords) {
            super(context, calleeName, primary, callee, arguments, keywords, false);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            PyObject callee;

            try {
                callee = calleeNode.executePyObject(frame);
            } catch (UnexpectedResultException e) {
                throw new IllegalStateException("Call to " + e.getMessage() + " not supported.");
            }

            return executeCall(frame, callee);
        }

        protected Object executeCall(VirtualFrame frame, PyObject callee) {
            Object[] arguments = executeArguments(frame, argumentNodes);
            PyObject[] pyargs = adaptToPyObjects(arguments);
            return unboxPyObject(callee.__call__(pyargs));
        }
    }

    public static final class UninitializedCallNode extends PythonCallNode {

        public UninitializedCallNode(PythonContext context, PNode primary, String calleeName, PNode callee, PNode[] arguments, PNode[] keywords) {
            super(context, calleeName, primary, callee, arguments, keywords, false);
            this.calleeNode = callee;
        }

        public PNode getCallee() {
            return calleeNode;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            CompilerDirectives.transferToInterpreterAndInvalidate();

            Object primary = primaryNode.execute(frame);
            return rewriteAndExecuteCall(frame, primary, calleeNode.execute(frame));
        }
    }

}
