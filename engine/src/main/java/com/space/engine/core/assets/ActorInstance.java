package com.space.engine.core.assets;

import com.space.engine.core.math.Transform;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActorInstance {
    private String prefab;
    private String name;
    private Transform transform;
}
