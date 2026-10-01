package com.space.engine.core.assets.data;

import com.space.engine.core.math.Transform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ActorData extends AssetData {
    protected UUID uid;

    protected String name;

    protected Transform transform;

    protected String[] components;
}
