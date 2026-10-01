package com.space.game;

import com.space.engine.core.Debug;
import com.space.engine.core.Game;
import com.space.engine.core.assets.AssetId;
import com.space.engine.core.exceptions.AssetException;

public class Racer extends Game {

    @Override
    public void onInit() {
        Debug.log("Racer init!");

    }

    @Override
    public void onStart() {
        Debug.log("Racer start!");

        try {

            var world = getEngine().assets().load(new AssetId("worlds/sandbox.world"));

            getEngine().loadWorld("worlds/sandbox.world");

        } catch (AssetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onStep() {
    }

    @Override
    public void onShutdown() {
    }

}
