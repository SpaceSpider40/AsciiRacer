package com.space.engine.core.assets;

import com.google.gson.Gson;
import com.space.engine.core.assets.data.AssetData;

import java.nio.charset.StandardCharsets;

public class JsonAssetSerializer implements AssetSerializer {
    private final Gson gson = new Gson();

    @Override
    public <T extends AssetData> T decode(byte[] bytes, Class<T> dataType, AssetId id) {
        T data = gson.fromJson(new String(bytes, StandardCharsets.UTF_8), dataType);

        if (data == null) {
            throw new IllegalArgumentException("Invalid asset: " + id);
        }

        return data;
    }
}
