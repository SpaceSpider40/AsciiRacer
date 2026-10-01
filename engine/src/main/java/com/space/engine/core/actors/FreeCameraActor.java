package com.space.engine.core.actors;

import com.space.engine.core.Input;
import com.space.engine.core.math.Vector3;
import lombok.Setter;

public class FreeCameraActor extends CameraActor {

    private final Vector3 moveVector = Vector3.zero();
    private final Vector3 lookVector = Vector3.zero();

    @Setter
    private float walkSpeed = 0.01f;
    @Setter
    private float lookSpeed = 0.05f;

    public FreeCameraActor(float near, float far, float fov) {
        super(near, far, fov);
    }

    @Override
    protected void onUpdate(double deltaTime) {
        super.onUpdate(deltaTime);

        move((float) deltaTime);
        look((float) deltaTime);
    }

    private void move(float deltaTime) {
        //MOVEMENT//
        moveVector.setZero();

        if (Input.IsKeyDown(Input.KEY_W)) {
            moveVector.z += 1;
        }
        if (Input.IsKeyDown(Input.KEY_S)) {
            moveVector.z -= 1;
        }
        if (Input.IsKeyDown(Input.KEY_A)) {
            moveVector.x -= 1;
        }
        if (Input.IsKeyDown(Input.KEY_D)) {
            moveVector.x += 1;
        }
        if (Input.IsKeyDown(Input.KEY_Q)){
            moveVector.y -= 1;
        }
        if (Input.IsKeyDown(Input.KEY_E)){
            moveVector.y += 1;
        }

        moveVector.normalize();

        moveVector.rotateX(transform.rotation.x);
        moveVector.rotateY(transform.rotation.y);
        moveVector.rotateZ(transform.rotation.z);

        moveVector.mul(walkSpeed * deltaTime);

        transform.position.add(moveVector);
    }

    private void look(float deltaTime) {
        lookVector.setZero();

        if (Input.IsKeyDown(Input.KEY_I)) {
            lookVector.x -= 1;
        }
        if (Input.IsKeyDown(Input.KEY_K)) {
            lookVector.x += 1;
        }
        if (Input.IsKeyDown(Input.KEY_J)) {
            lookVector.y -= 1;
        }
        if (Input.IsKeyDown(Input.KEY_L)) {
            lookVector.y += 1;
        }

        lookVector.normalize();
        lookVector.mul(lookSpeed * deltaTime);

        transform.rotation.add(lookVector);
    }
}
