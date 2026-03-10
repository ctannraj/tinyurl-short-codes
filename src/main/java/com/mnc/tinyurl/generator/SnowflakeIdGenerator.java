package com.mnc.tinyurl.generator;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SnowflakeIdGenerator {

    private final long machineId;

    private static final long EPOCH = 1704067200000L; // Jan 1 2024

    private static final long MACHINE_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1;

    private static final long MACHINE_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = MACHINE_ID_BITS + SEQUENCE_BITS;

    private static final long SEQUENCE_MASK = (1L << SEQUENCE_BITS) - 1;

    private volatile long lastTimestamp = -1L;

    private final AtomicLong sequence = new AtomicLong(0);

    public SnowflakeIdGenerator() {
        this.machineId = new Random().nextInt((int) MAX_MACHINE_ID);
    }

    public long nextId() {

        long timestamp = currentTime();

        synchronized (this) {

            if (timestamp < lastTimestamp) {
                throw new RuntimeException("Clock moved backwards");
            }

            if (timestamp == lastTimestamp) {

                long seq = (sequence.incrementAndGet()) & SEQUENCE_MASK;

                if (seq == 0) {
                    timestamp = waitNextMillis(timestamp);
                }

            } else {
                sequence.set(0);
            }

            lastTimestamp = timestamp;

            long seq = sequence.get();

            return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                    | (machineId << MACHINE_SHIFT)
                    | seq;
        }
    }

    private long waitNextMillis(long timestamp) {
        while (timestamp <= lastTimestamp) {
            timestamp = currentTime();
        }
        return timestamp;
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }
}
