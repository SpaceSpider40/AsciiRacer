package com.space.engine.core.assets.factories;

import com.space.engine.core.actors.CameraActor;
import com.space.engine.core.assets.AssetContext;
import com.space.engine.core.assets.AssetFactory;
import com.space.engine.core.assets.data.CameraData;

public class CameraAssetFactory implements AssetFactory<CameraData, CameraActor> {
    @Override
    public CameraActor create(CameraData data, AssetContext context) {
        return new CameraActor(
                data.getNear(),
                data.getFar(),
                data.getFov()
        );
    }
}
