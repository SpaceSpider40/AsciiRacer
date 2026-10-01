package com.space.engine.core.log;

import lombok.Getter;

public interface LogWriter {
    @Getter
    enum Level {
        INFO("INFO"),
        WARN("WARN"),
        ERROR("ERROR");

        private final String value;

        private Level(String value) {
            this.value = value;
        }
    }

    void write(String msg);

    void write(String msg, Exception e);

    void write(Level level, String msg);
}
