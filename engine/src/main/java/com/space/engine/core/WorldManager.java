package com.space.engine.core;

import com.space.engine.core.actors.CameraActor;

public class WorldManager {

    private World currentWorld;

    public World getCurrentWorld() {

        if (currentWorld == null) {
            currentWorld = createTemplateWorld();
        }

        return currentWorld;
    }

    public CameraActor getActiveCamera() {
        return currentWorld.getActiveCamera();
    }

    public void setCurrentWorld(String path, World world){
        currentWorld = world;
    }

    @SuppressWarnings("D")
    private World createTemplateWorld() {
        var world = new World("Empty World");

        return world;
    }
}
