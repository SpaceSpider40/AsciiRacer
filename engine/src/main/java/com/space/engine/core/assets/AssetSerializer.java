package com.space.engine.core.assets;

import com.space.engine.core.assets.data.AssetData;

public interface AssetSerializer {
    <T extends AssetData> T decode(byte[] bytes, Class<T> dataType, AssetId id);

    <T extends AssetData> byte[] encode(T data, Class<? extends AssetData> dataType, AssetId id);
}
