package com.space.engine.core.assets.factories;

import com.space.engine.core.Debug;
import com.space.engine.core.World;
import com.space.engine.core.actors.Actor;
import com.space.engine.core.assets.ActorInstance;
import com.space.engine.core.assets.AssetContext;
import com.space.engine.core.assets.AssetFactory;
import com.space.engine.core.assets.AssetId;
import com.space.engine.core.assets.data.WorldData;
import com.space.engine.core.exceptions.AssetException;

public class WorldAssetFactory implements AssetFactory<WorldData, World> {
    @Override
    public World construct(WorldData data, AssetContext context) {
        // todo: validate if file is not malformed
        World world = new World(data.getName());

        if (data.getActors() != null) {

            int loadedActors = 0;
            for (ActorInstance instance : data.getActors()) {
                if (instance.getPrefab() == null)
                    continue;

                var id = new AssetId(instance.getPrefab());
                try {
                    Actor actor = context.load(id);

                    if (instance.getTransform() != null) {
                        actor.transform.set(instance.getTransform());
                    }

                    if (instance.getName() != null) {
                        actor.setName(instance.getName());
                    }

                    world.addActor(actor);
                    loadedActors++;
                } catch (AssetException e) {
                    Debug.err("Failed to load actor: " + e);
                }
            }

            Debug.log("Loaded " + loadedActors + " actors");
        }

        return world;
    }

    @Override
    public WorldData construct(World object, AssetContext context) {
        // TODO Auto-generated method stub
        return null;
    }
}
