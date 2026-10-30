package com.space.engine.core.assets;

import com.space.engine.core.assets.data.AssetData;

public interface AssetFactory<T extends AssetData, O> {
    O construct(T data, AssetContext context);

    T construct(O object, AssetContext context);
}
