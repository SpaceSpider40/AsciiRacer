package com.space.engine.core.assets;

import lombok.NonNull;

public record AssetId(String path) {

    public AssetId {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Asset path cannot be null or blank");
        }

        path = path.replace("\\", "/");

        if (path.startsWith("/")) {
            throw new IllegalArgumentException("Asset path must be relative");
        }

        if (path.contains("..")) {
            throw new IllegalArgumentException("Asset path cannot contain '..'");
        }

    }

    public String extension() {
        int dot = path.lastIndexOf('.');

        if (dot == -1 || dot == path.length() - 1) {
            return "";
        }

        return path.substring(dot + 1);
    }

    @Override
    @NonNull
    public String toString() {
        return path;
    }

}
