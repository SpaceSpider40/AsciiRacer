package com.space.engine.core.assets.data;

import java.util.UUID;

import com.space.engine.core.math.Transform;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActorData extends AssetData {
    private UUID uid;

    private String name;

    private Transform transform;

    // todo: components data
}
