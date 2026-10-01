package com.space.engine.core.exceptions;

public class AssetLoadFail extends AssetException {
    public AssetLoadFail(String message) {
        super(message);
    }
    public AssetLoadFail(String message, Exception e){
        super(message, e);
    }
}
