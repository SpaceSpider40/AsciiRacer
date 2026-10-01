package com.space.engine.core.assets.factories;

import com.space.engine.core.actors.components.MaterialComponent;
import com.space.engine.core.assets.AssetContext;
import com.space.engine.core.assets.AssetFactory;
import com.space.engine.core.assets.data.components.MaterialData;

public class MaterialComponentAssetFactory implements AssetFactory<MaterialData, MaterialComponent> {
    @Override
    public MaterialComponent create(MaterialData data, AssetContext context) {
        var mat = new MaterialComponent();
        mat.albedo.set(data.getAlbedo());
        return mat;
    }

}
