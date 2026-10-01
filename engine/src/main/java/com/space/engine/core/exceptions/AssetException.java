package com.space.engine.core.exceptions;

public class AssetException extends Exception {
    public AssetException(String message) {
        super(message);
    }

    public AssetException(String message, Exception e) {
        super(message, e);
    }
}
