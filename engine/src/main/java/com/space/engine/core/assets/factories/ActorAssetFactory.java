package com.space.engine.core.assets.factories;

import com.space.engine.core.Debug;
import com.space.engine.core.actors.Actor;
import com.space.engine.core.assets.AssetContext;
import com.space.engine.core.assets.AssetFactory;
import com.space.engine.core.assets.AssetId;
import com.space.engine.core.assets.data.ActorData;
import com.space.engine.core.exceptions.AssetException;

public class ActorAssetFactory implements AssetFactory<ActorData, Actor> {
    @Override
    public Actor construct(ActorData data, AssetContext context) {
        var actor = new Actor();
        actor.transform.set(data.getTransform());

        if (data.getComponents() != null) {
            for (String compRef : data.getComponents()) {
                try {
                    actor.addComponent(context.load(new AssetId(compRef)));
                } catch (AssetException e) {
                    Debug.err("Failed to load actor component: " + compRef);
                }
            }
        }

        return actor;
    }

    @Override
    public ActorData construct(Actor object, AssetContext context) {
        // TODO Auto-generated method stub
        return null;
    }
}
