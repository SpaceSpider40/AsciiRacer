package com.space.engine.core.assets;

import java.io.IOException;

public interface AssetSource {
    byte[] read(AssetId id) throws IOException;
    void write(AssetId id, byte[] data) throws IOException;
}
