package com.space.engine.core.assets.factories;

import com.space.engine.core.actors.FreeCameraActor;
import com.space.engine.core.assets.AssetContext;
import com.space.engine.core.assets.AssetFactory;
import com.space.engine.core.assets.data.FreeCameraData;

public class FreeCameraAssetFactory implements AssetFactory<FreeCameraData, FreeCameraActor> {
    @Override
    public FreeCameraActor construct(FreeCameraData data, AssetContext context) {
        var camera = new FreeCameraActor(
                data.getNear(),
                data.getFar(),
                data.getFov());

        camera.setLookSpeed(data.getLookSpeed());
        camera.setWalkSpeed(data.getWalkSpeed());

        return camera;
    }

    @Override
    public FreeCameraData construct(FreeCameraActor object, AssetContext context) {
        // TODO Auto-generated method stub
        return null;
    }
}
