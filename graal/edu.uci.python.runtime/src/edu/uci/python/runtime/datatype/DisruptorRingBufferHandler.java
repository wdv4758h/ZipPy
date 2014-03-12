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

import com.lmax.disruptor.*;
import com.oracle.truffle.api.*;

public class DisruptorRingBufferHandler {

    private static final EventFactory<ObjectEvent> EMPTY_EVENTS = new ObjectEventFactory();
    private static final int BUFFER_SIZE = 32;
    private static final boolean PUBLISH_IN_BATCH = true;

    private final String generatorName;
    protected final RingBuffer<ObjectEvent> ringBuffer;
    private final SequenceBarrier sequenceBarrier;
    private final Sequence sequence;

    public static DisruptorRingBufferHandler create(String generatorName) {
        if (PUBLISH_IN_BATCH) {
            return new RingBufferBatchHandler(generatorName);
        } else {
            return new DisruptorRingBufferHandler(generatorName);
        }
    }

    public DisruptorRingBufferHandler(String generatorName) {
        this.generatorName = generatorName;
        ringBuffer = RingBuffer.createSingleProducer(EMPTY_EVENTS, BUFFER_SIZE);
        sequenceBarrier = ringBuffer.newBarrier();
        sequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
        ringBuffer.addGatingSequences(sequence);
    }

    public void put(Object value) {
        long next = ringBuffer.next();
        ringBuffer.get(next).setValue(value);
        ringBuffer.publish(next);
    }

    public void putAndDrain(Object poison) {
        put(poison);
    }

    public Object take() {
        final long nextSequence = sequence.get() + 1L;

        try {
            sequenceBarrier.waitFor(nextSequence);
        } catch (AlertException | InterruptedException | TimeoutException e) {
            CompilerDirectives.transferToInterpreter();
            throw new RuntimeException();
        }

        Object result = ringBuffer.get(nextSequence).getValue();
        sequence.set(nextSequence);
        return result;
    }

    @Override
    public String toString() {
        return "ring buffer " + generatorName;
    }

    public static final class RingBufferBatchHandler extends DisruptorRingBufferHandler {

        public RingBufferBatchHandler(String generatorName) {
            super(generatorName);
        }

        private static final int BATCH_SIZE = 8;
        private long next;
        private long hi;
        private long lo;

        @Override
        public void put(Object value) {
            if (next == hi) {
                hi = ringBuffer.next(BATCH_SIZE);
                next = lo = hi - (BATCH_SIZE - 1);
            } else {
                next++;
            }

            ringBuffer.get(next).setValue(value);

            if (next == hi) {
                ringBuffer.publish(lo, hi);
            }
        }

        @Override
        public void putAndDrain(Object poison) {
            next++;
            ringBuffer.get(next).setValue(poison);
            ringBuffer.publish(lo, next);
        }
    }

    public static class ObjectEvent {

        private Object value;

        public Object getValue() {
            assert value != null;
            return value;
        }

        public void setValue(Object value) {
            assert value != null;
            this.value = value;
        }
    }

    public static class ObjectEventFactory implements EventFactory<ObjectEvent> {

        public ObjectEvent newInstance() {
            return new ObjectEvent();
        }
    }

}