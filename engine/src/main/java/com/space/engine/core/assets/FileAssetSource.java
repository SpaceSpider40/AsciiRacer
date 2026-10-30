package com.space.engine.core.assets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileAssetSource implements AssetSource{
    private final Path root;

    public FileAssetSource(Path root){
        this.root = root;
    }

    @Override
    public byte[] read(AssetId id) throws IOException {
        Path file = root.resolve(id.path() + ".json").normalize();

        if (!file.startsWith(root.normalize())){
            throw new IOException("Asset path escapes root: " + id);
        }

        return Files.readAllBytes(file);
    }

    @Override
    public void write(AssetId id, byte[] data) throws IOException {
        Path file = root.resolve(id.path() + ".json").normalize();

        if (!file.startsWith(root.normalize())){
            throw new IOException("Asset path escapes root: " + id);
        }

        Files.write(file,data);
    }
}
