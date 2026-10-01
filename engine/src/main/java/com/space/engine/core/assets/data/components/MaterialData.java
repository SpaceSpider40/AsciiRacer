package com.space.engine.core.assets.data.components;

import com.space.engine.core.assets.data.AssetData;
import com.space.engine.core.graphics.Color;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class MaterialData extends AssetData {
    private Color albedo;
}
