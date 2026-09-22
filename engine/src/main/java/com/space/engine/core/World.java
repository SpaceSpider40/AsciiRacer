package com.space.engine.core;

import com.space.engine.core.actors.Actor;
import com.space.engine.core.actors.CameraActor;
import com.space.engine.core.actors.FreeCameraActor;
import com.space.engine.core.actors.components.ActorComponent;
import com.space.engine.core.actors.components.RigidBodyComponent;
import com.space.engine.core.physics.Physics;

import java.util.ArrayList;
import java.util.Collection;

public class World implements Step, Update {
    private final String name;

    private final ArrayList<Actor> actors = new ArrayList<>();

    private final CameraActor debugCamera = new FreeCameraActor(0.01f, 1000f, 60);  //free camera until overwritten by world camera

    private CameraActor activeCamera;

    public World(String name) {
        this.name = name;

        activeCamera = debugCamera;
        this.addActor(debugCamera);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public Collection<Actor> getActors() {
        return actors;
    }

    public void setActiveCamera(CameraActor camera) {
        actors.remove(activeCamera);
        activeCamera = camera;
        actors.add(activeCamera);
    }

    public void activateDebugCamera() {
        setActiveCamera(debugCamera);
    }

    public CameraActor getActiveCamera() {
        return activeCamera;
    }

    @Override
    public String toString() {
        return "World[" + name + "]";
    }

    @Override
    public void step(double delta) {

    }

    @Override
    public void update(double delta) {

    }
}
