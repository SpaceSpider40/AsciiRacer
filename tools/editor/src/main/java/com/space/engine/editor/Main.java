package com.space.engine.editor;

import com.space.engine.core.Debug;
import com.space.engine.core.Engine;
import com.space.engine.core.EngineImpl;
import com.space.engine.core.actors.components.RigidBodyComponent;
import com.space.engine.core.math.Vector3;
import com.space.engine.core.physics.MotionType;
import com.space.engine.core.physics.shapes.BoxShapeDefinition;
import com.space.engine.editor.actors.CubeActor;

public class Main {
    public static void main(String[] args) {
        Engine engine = new EngineImpl();
        engine.init();

//        var camera = new CameraActor(0.001f, 1000f, 60f);
//        camera.transform.position.set(0, 0.5f, -5);
//        camera.transform.rotation.set(45, 0,0);

//        AddCube(engine, new Vector3(0,0,0));

        int count = 0;
//        for (int x = -10; x <= 10; x++) {
//            for (int y = -10; y <= 10; y++) {
//                AddCube(engine, new Vector3(x, y, 10));
//                count++;
//            }
//        }

        var floor     = new CubeActor();
        var floorBody = new RigidBodyComponent();
        floorBody.setMotionType(MotionType.STATIC);
        floorBody.setShape(new BoxShapeDefinition(Vector3.one().div(2)));
        floorBody.setMass(1.0f);
        floor.addComponent(floorBody);

        engine.currentWorld().addActor(floor);
        count++;

        var box     = new CubeActor();
        var boxBody = new RigidBodyComponent();
        boxBody.setMotionType(MotionType.DYNAMIC);
        boxBody.setShape(new BoxShapeDefinition(Vector3.one().div(2)));
        boxBody.setMass(1.0f);
        box.addComponent(boxBody);

        box.transform.position.y = 5;

        engine.currentWorld().addActor(box);
        count++;

//        engine.currentWorld().addActor(camera);
        Debug.warn("Spawned: " + count + " cubes");
        // run the engine for now it is single thread so last instruciton
        engine.run();
    }

    private static void AddCube(Engine engine, Vector3 position) {
        var box = new CubeActor();

        box.transform.position.set(position);

        engine.currentWorld().addActor(box);
    }
}
