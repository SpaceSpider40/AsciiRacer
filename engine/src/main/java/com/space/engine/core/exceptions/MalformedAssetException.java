package com.space.engine.core.exceptions;

public class MalformedAssetException extends AssetException {
    public MalformedAssetException(String message) {
        super(message);
    }

    public MalformedAssetException(String message, Exception e) {
        super(message, e);
    }
}
