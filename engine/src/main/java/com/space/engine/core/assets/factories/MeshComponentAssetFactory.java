package com.space.engine.core.assets.factories;

import com.space.engine.core.actors.components.MeshComponent;
import com.space.engine.core.assets.AssetContext;
import com.space.engine.core.assets.AssetFactory;
import com.space.engine.core.assets.data.components.MeshData;

public class MeshComponentAssetFactory implements AssetFactory<MeshData, MeshComponent> {
    @Override
    public MeshComponent create(MeshData data, AssetContext context) {
        return new MeshComponent(data.getVertices(), data.getIndices());
    }
}
