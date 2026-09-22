package com.space.engine.core.actors.components;

import com.github.stephengold.joltjni.Body;
import com.space.engine.core.Debug;
import com.space.engine.core.actors.Actor;
import com.space.engine.core.physics.MotionType;
import com.space.engine.core.physics.Physics;
import com.space.engine.core.physics.shapes.ShapeDefinition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * It should some day be separated from jolt.
 */
@Getter
@Setter
public class RigidBodyComponent extends ActorComponent {

    private MotionType motionType;

    private ShapeDefinition shape;
    private Body            body;

    private boolean isBodyActive;

    private float mass;
    @Setter(AccessLevel.NONE)
    private float restitution;
    @Setter(AccessLevel.NONE)
    private float friction;

    private float linearDamping;
    private float angularDamping;

    public void setRestitution(float restitution) {
        this.restitution = restitution;

        if (body != null) {
            body.setRestitution(restitution);
        }
    }

    public void setFriction(float friction) {
        this.friction = friction;

        if (body != null) {
            body.setFriction(friction);
        }
    }

    @Override
    protected void onStart() {
        if (getOwner() instanceof Actor actor) {
            Physics.get().CreateBody(this, actor.transform);
            Physics.get().AddRigidBody(this);
        } else {
            Debug.err("Owner of RigidBody must be a descendant of Actor class");
        }
    }

    @Override
    protected void onUpdate(double deltaTime) {
        if (getOwner() instanceof Actor actor) {
            Physics.get().synchronizeRigidBody(this, actor.transform);
        }
    }

    @Override
    protected void onStep(double deltaTime) {

    }

    @Override
    protected void onEnd() {
        Physics.get().RemoveRigidBody(this);
    }
}
