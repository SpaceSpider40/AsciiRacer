package com.space.engine.core.actors;

import com.space.engine.core.actors.components.ActorComponent;
import com.space.engine.core.math.Matrix44;
import com.space.engine.core.math.Transform;
import lombok.Getter;
import lombok.Setter;

public class Actor extends Entity {

    public final Transform transform = new Transform();

    private final Transform lastTransform = new Transform();
    private final Matrix44 modelMatrix = new Matrix44();

    @Setter
    @Getter
    private String name;

    public final void start() {
        for (ActorComponent comp : getComponents()){
            comp.start();
        }

        onStart();
    }

    public final void update(double deltaTime) {
        for (ActorComponent comp : getComponents()){
            comp.update(deltaTime);
        }

        onUpdate(deltaTime);
    }

    public final void step(double deltaTime) {
        for (ActorComponent comp : getComponents()){
            comp.step(deltaTime);
        }

        onStep(deltaTime);
    }

    public final void end() {
        for (ActorComponent comp : getComponents()){
            comp.end();
        }

        onEnd();
    }

    public final Matrix44 getModelMatrix(){
        if (transform == lastTransform){
            return modelMatrix;
        }

        modelMatrix.setIdentity();
        modelMatrix.setModelMatrix(
                transform.position,
                transform.rotation,
                transform.scale
        );

        lastTransform.set(transform);

        return modelMatrix;
    }

    protected void onStart(){

    }

    protected void onUpdate(double deltaTime){

    }

    protected void onStep(double deltaTime){

    }

    protected void onEnd(){

    }
}
