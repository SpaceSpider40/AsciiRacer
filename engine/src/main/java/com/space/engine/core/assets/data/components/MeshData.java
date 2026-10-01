package com.space.engine.core.assets.data.components;

import com.space.engine.core.assets.data.AssetData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class MeshData extends AssetData {
    protected float[] vertices;
    protected int[] indices;
}
