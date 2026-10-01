package com.space.engine.core.assets;

import com.space.engine.core.assets.data.AssetData;
import com.space.engine.core.exceptions.AssetException;

public interface AssetContext {
    <D extends AssetData, O> O load(AssetId id) throws AssetException;
}
