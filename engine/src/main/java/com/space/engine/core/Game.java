package com.space.engine.core;

import lombok.Getter;

public abstract class Game {
    @Getter
    private Engine engine;

    void setEngine(Engine engine) {
        this.engine = engine;
    }

    public abstract void onInit();

    public abstract void onStart();

    public abstract void onUpdate();

    public abstract void onStep();

    public abstract void onShutdown();
}
