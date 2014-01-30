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
package edu.uci.python.runtime.datatype;

import java.util.*;
import java.util.concurrent.*;

import com.lmax.disruptor.*;
import com.lmax.disruptor.TimeoutException;
import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;

import edu.uci.python.runtime.*;
import edu.uci.python.runtime.exception.*;
import edu.uci.python.runtime.function.*;

public class PParallelGenerator extends PGenerator {

    private boolean isFirstEntry = true;
    private final PythonContext context;

    private final BlockingQueue<Object> blockingQueue;
    private final SingleProducerCircularBuffer buffer;
    private final Queue<Object> queue;

    // Disruptor
    private RingBuffer<ObjectEvent> ringBuffer;
    private SequenceBarrier sequenceBarrier;
    private Sequence sequence;

    public static final int QUEUE_CHOICE = 2;
    public static final int BLOCKING_QUEUE_CHOICE = 1;

    public static PParallelGenerator create(String name, PythonContext context, CallTarget callTarget, FrameDescriptor frameDescriptor, MaterializedFrame declarationFrame, Object[] arguments) {
        PArguments parallelArgs;

        switch (QUEUE_CHOICE) {
            case 0:
                SingleProducerCircularBuffer buffer = new SingleProducerCircularBuffer();
                parallelArgs = new PArguments.ParallelGeneratorArguments(declarationFrame, buffer, arguments);
                return new PParallelGenerator(name, context, callTarget, frameDescriptor, parallelArgs, buffer);
            case 1:
                BlockingQueue<Object> blockingQueue = createBlockingQueue();
                parallelArgs = new PArguments.ParallelGeneratorArguments(declarationFrame, blockingQueue, arguments);
                return new PParallelGenerator(name, context, callTarget, frameDescriptor, parallelArgs, blockingQueue);
            case 2:
                Queue<Object> queue = new ConcurrentLinkedQueue<>();
                parallelArgs = new PArguments.ParallelGeneratorArguments(declarationFrame, queue, arguments);
                return new PParallelGenerator(name, context, callTarget, frameDescriptor, parallelArgs, queue);
            default:
                throw new IllegalStateException();
        }
    }

    protected PParallelGenerator(String name, PythonContext context, CallTarget callTarget, FrameDescriptor frameDescriptor, PArguments arguments, BlockingQueue<Object> blockingQueue) {
        super(name, callTarget, frameDescriptor, arguments);
        this.context = context;
        this.blockingQueue = blockingQueue;
        this.buffer = null;
        this.queue = null;
    }

    protected PParallelGenerator(String name, PythonContext context, CallTarget callTarget, FrameDescriptor frameDescriptor, PArguments arguments, SingleProducerCircularBuffer buffer) {
        super(name, callTarget, frameDescriptor, arguments);
        this.context = context;
        this.blockingQueue = null;
        this.buffer = buffer;
        this.queue = null;
    }

    protected PParallelGenerator(String name, PythonContext context, CallTarget callTarget, FrameDescriptor frameDescriptor, PArguments arguments, Queue<Object> queue) {
        super(name, callTarget, frameDescriptor, arguments);
        this.context = context;
        this.blockingQueue = null;
        this.buffer = null;
        this.queue = queue;
    }

    public final void generates() {
        context.getExecutorService().execute(new Runnable() {

            public void run() {
                try {
                    callTarget.call(null, arguments);
                    blockingQueue.put(StopIterationException.INSTANCE);
                } catch (InterruptedException e) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public final Object __next__() throws StopIterationException {
        switch (QUEUE_CHOICE) {
            case 0:
                return doWithCircularBuffer();
            case 1:
                return doWithBlockingQueue();
            case 2:
                return doWithConcurrentLinkedQueue();
            case 3:
                return doWithDisruptor();
            default:
                throw new RuntimeException();
        }
    }

    private static BlockingQueue<Object> createBlockingQueue() {
        switch (BLOCKING_QUEUE_CHOICE) {
            case 0:
                return new LinkedBlockingQueue<>();
            case 1:
                return new ArrayBlockingQueue<>(32);
            case 2:
                return new SynchronousQueue<>();
            default:
                throw new RuntimeException();
        }
    }

    private Object doWithConcurrentLinkedQueue() {
        if (isFirstEntry) {
            isFirstEntry = false;
            context.getExecutorService().execute(new Runnable() {

                public void run() {
                    callTarget.call(null, arguments);
                    while (!queue.offer(StopIterationException.INSTANCE)) {
                        // spin
                    }
                }

            });
        }

        Object result = null;
        while (result == null) {
            result = queue.poll();
        }

        if (result == StopIterationException.INSTANCE) {
            throw StopIterationException.INSTANCE;
        } else {
            return result;
        }
    }

    private Object doWithBlockingQueue() {
        if (isFirstEntry) {
            isFirstEntry = false;
            context.getExecutorService().execute(new Runnable() {

                public void run() {
                    try {
                        callTarget.call(null, arguments);
                        blockingQueue.put(StopIterationException.INSTANCE);
                    } catch (InterruptedException e) {
                        CompilerDirectives.transferToInterpreterAndInvalidate();
                        e.printStackTrace();
                    }
                }

            });
        }

        Object result = null;
        try {
            result = blockingQueue.take();
        } catch (InterruptedException e) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            e.printStackTrace();
        }

        if (result == StopIterationException.INSTANCE) {
            throw StopIterationException.INSTANCE;
        } else {
            return result;
        }
    }

    private Object doWithCircularBuffer() {
        if (isFirstEntry) {
            isFirstEntry = false;
            context.getExecutorService().execute(new Runnable() {

                public void run() {
                    callTarget.call(null, arguments);
                    buffer.setAsTerminated();
                }

            });
        }

        return buffer.take();
    }

    private Object doWithDisruptor() {
        if (ringBuffer == null) {
            ringBuffer = RingBuffer.createSingleProducer(EMPTY_EVENTS, 32);
            sequenceBarrier = ringBuffer.newBarrier();
            sequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
            ringBuffer.addGatingSequences(sequence);

            context.getExecutorService().execute(new Runnable() {

                public void run() {
                    final RingBuffer<ObjectEvent> rb = ringBuffer;

                    Object value;

                    try {
                        while (true) {
                            value = callTarget.call(null, arguments);
                            long next = rb.next();
                            rb.get(next).setValue(value);
                            rb.publish(next);
                        }
                    } catch (StopIterationException e) {
                        long next = rb.next();
                        rb.get(next).setValue(StopIterationException.INSTANCE);
                        rb.publish(next);
                    }
                }

            });
        }

        sequenceBarrier.clearAlert();
        long nextSequence = sequence.get() + 1L;

        try {
            sequenceBarrier.waitFor(nextSequence);
        } catch (AlertException | InterruptedException | TimeoutException e) {
            CompilerDirectives.transferToInterpreter();
            throw new RuntimeException();
        }

        Object result = ringBuffer.get(nextSequence).getValue();
        sequence.set(nextSequence);

        if (result == StopIterationException.INSTANCE) {
            throw StopIterationException.INSTANCE;
        } else {
            return result;
        }
    }

    public static class ObjectEvent {

        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    }

    public static final EventFactory<ObjectEvent> EMPTY_EVENTS = new ObjectEventFactory();

    public static class ObjectEventFactory implements EventFactory<ObjectEvent> {

        public ObjectEvent newInstance() {
            return new ObjectEvent();
        }

    }

}
