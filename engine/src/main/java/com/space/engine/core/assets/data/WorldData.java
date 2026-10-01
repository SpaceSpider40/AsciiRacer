package com.space.engine.core.assets.data;

import java.util.Collection;
import java.util.UUID;

import com.space.engine.core.assets.ActorInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WorldData extends AssetData {
    private UUID uid;
    private String name;

    private ActorInstance[] actors;
}
