package com.space.engine.core.assets;

import com.space.engine.core.assets.data.AssetData;

public interface AssetFactory<T extends AssetData, O> {
    O create(T data, AssetContext context);
}
