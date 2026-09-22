package com.space.engine.core.assets.data;

import java.util.Collection;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorldData extends AssetData {
    private UUID uid;
    private String name;

    private Collection<ActorData> actors;
}
