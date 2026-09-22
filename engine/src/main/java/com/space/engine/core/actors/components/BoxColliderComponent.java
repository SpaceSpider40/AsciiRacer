package com.space.engine.core.actors.components;

import com.space.engine.core.math.Vector3;

public class BoxColliderComponent extends ActorComponent {

    public final Vector3 halfExtents = Vector3.zero();

    public float boundingRadius;

    public BoxColliderComponent(Vector3 halfExtents) {
        halfExtents.set(halfExtents);
        boundingRadius = halfExtents.length();
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
