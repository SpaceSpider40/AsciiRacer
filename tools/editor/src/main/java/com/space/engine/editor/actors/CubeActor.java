package com.space.engine.editor.actors;

import com.space.engine.core.Debug;
import com.space.engine.core.actors.Actor;
import com.space.engine.core.actors.components.MeshComponent;

public class CubeActor extends Actor {

    public CubeActor() {

        float[] cubeVertices = {
                // x, y, z

                // 0: back-bottom-left
                -0.5f, -0.5f, -0.5f,

                // 1: back-bottom-right
                0.5f, -0.5f, -0.5f,

                // 2: back-top-right
                0.5f, 0.5f, -0.5f,

                // 3: back-top-left
                -0.5f, 0.5f, -0.5f,

                // 4: front-bottom-left
                -0.5f, -0.5f, 0.5f,

                // 5: front-bottom-right
                0.5f, -0.5f, 0.5f,

                // 6: front-top-right
                0.5f, 0.5f, 0.5f,

                // 7: front-top-left
                -0.5f, 0.5f, 0.5f
        };

        int[] cubeIndices = {
                // back face
                0, 2, 1,
                0, 3, 2,

                // front face
                4, 5, 6,
                4, 6, 7,

                // left face
                0, 4, 7,
                0, 7, 3,

                // right face
                1, 2, 6,
                1, 6, 5,

                // top face
                3, 7, 6,
                3, 6, 2,

                // bottom face
                0, 1, 5,
                0, 5, 4
        };

        addComponent(new MeshComponent(cubeVertices, cubeIndices));
    }

    @Override
    protected void onStart() {
        Debug.log("Cube actor start!");
    }

    @Override
    protected void onUpdate(double deltaTime) {
//        transform.rotation.y += 0.01f * (float) deltaTime;
    }

    @Override
    protected void onStep(double deltaTime) {

    }

    @Override
    protected void onEnd() {

    }
}
