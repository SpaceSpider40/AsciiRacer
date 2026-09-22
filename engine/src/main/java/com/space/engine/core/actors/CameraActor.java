package com.space.engine.core.actors;

public class CameraActor extends Actor {

    public final float near;
    public final float far;
    public final float fov;

    public CameraActor(
            float near,
            float far,
            float fov
    ) {
        this.near = near;
        this.far = far;
        this.fov = fov;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onUpdate(double deltaTime) {

    }

    @Override
    protected void onStep(double deltaTime) {
    }

    @Override
    protected void onEnd() {

    }
}
