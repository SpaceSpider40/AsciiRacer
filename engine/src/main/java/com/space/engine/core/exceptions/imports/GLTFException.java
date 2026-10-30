package com.space.engine.core.exceptions.imports;

public class GLTFException extends Exception {

    public static final String Tag = "[GLTF]";

    public GLTFException(String message) {
        super(Tag + " " + message);
    }
}
