package com.space.engine.core;

import com.space.engine.core.assets.AssetManager;

import java.util.Properties;

public interface Engine {
    public EngineState state();

    // RUNTIME
    public void init(Properties properties);

    public void run(Game game);

    public void shutdown();

    // CONTENT
    public World currentWorld();
    public void loadWorld(String path);

    public AssetManager assets();

    public double dt();

    public float dtf();

    // ECS
    public int createEntity();

    public void destroyEntity(int entity);


    //PHYSICS
}
