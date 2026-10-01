package com.space.engine.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Launcher {

    public static void launch(Game game) {
        EngineImpl engine = new EngineImpl();

        game.setEngine(engine);

        Properties properties = loadProperties(engine);

        engine.init(properties);

        //todo: set title

        game.onInit();

        //todo: load config

        engine.run(game);
    }

    private static Properties loadProperties(EngineImpl engine) {
        Properties props = new Properties();

        try {
            props.load(new FileInputStream("game.properties"));
        } catch (IOException e) {
            Debug.err("Failed to load game.properties");
        }

        engine.setProjectTitle(
                (String) props.getOrDefault("game.name", "Unnamed Game")
        );

        return props;
    }
}
