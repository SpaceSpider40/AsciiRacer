package com.space.engine.core.actors.components;

import com.space.engine.core.actors.Actor;
import lombok.Getter;

public class ChildComponent<T extends Actor> extends ActorComponent{

    @Getter
    private final T child;

    public ChildComponent(T child){
        this.child = child;
    }

    @Override
    protected void onStart() {
        child.start();
    }

    @Override
    protected void onUpdate(double deltaTime) {

        if (getOwner() instanceof Actor parent){
            child.transform.add(parent.transform);
        }

        child.update(deltaTime);
    }

    @Override
    protected void onStep(double deltaTime) {
        child.step(deltaTime);
    }

    @Override
    protected void onEnd() {
        child.end();
    }
}
