package com.space.engine.core;

import com.space.engine.core.graphics.Color;
import com.space.engine.core.log.DummyWriter;
import com.space.engine.core.log.LogWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

/*
 * Display debug messages on screen. Engine errors or user-defined messages.
 */
public final class Debug {
    public record Message(
            String msg,
            Color color
    ) {
    }

    public static final long DISPLAY_TIME = 5000000000L;

    public static LogWriter writer = new DummyWriter();

    private static final ConcurrentSkipListMap<Long, Message> messages      = new ConcurrentSkipListMap<>();
    private static final AtomicLong                           lastTimestamp = new AtomicLong();

    public static void log(String message) {
        if (message.isBlank()) return;

        messages.put(getUniqueTimestamp(), new Message(message, Color.WHITE));
        writer.write(LogWriter.Level.INFO, message);
    }

    public static void warn(String message) {
        if (message.isBlank()) return;

        messages.put(getUniqueTimestamp(), new Message(message, Color.YELLOW));
        writer.write(LogWriter.Level.WARN, message);
    }

    public static void err(String message) {
        if (message.isBlank()) return;

        messages.put(getUniqueTimestamp(), new Message(message, Color.RED));
        writer.write(LogWriter.Level.ERROR, message);
    }

    public static Collection<Message> getMessages() {
        ArrayList<Long> keysToRemove = new ArrayList<>();
        for (Long messageTime : messages.keySet()) {
            if (System.nanoTime() - messageTime <= DISPLAY_TIME) {
                continue;
            }

            //message is old, remove it from display
            keysToRemove.add(messageTime);
        }

        keysToRemove.forEach(messages::remove);

        return messages.values();
    }

    private static long getUniqueTimestamp() {
        long now = System.nanoTime();
        while (true) {
            long last = lastTimestamp.get();
            long next = Math.max(now, last + 1);
            if (lastTimestamp.compareAndSet(last, next)) {
                return next;
            }
        }
    }
}
