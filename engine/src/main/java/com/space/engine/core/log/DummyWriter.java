package com.space.engine.core.log;

public class DummyWriter implements LogWriter {
    @Override
    public void write(String msg) {
        //do nothing
    }

    @Override
    public void write(String msg, Exception e) {
        //do nothing
    }

    @Override
    public void write(Level level, String msg) {
        //do nothing
    }
}
