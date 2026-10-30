package com.space.engine.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Launcher {

    public static void launch(Game game) {
        Properties properties = loadProperties();

        launch(game, properties);
    }

    /*
    Allows launching the application in the context of a different project. Useful for edittor/imporer.
     */
    public static void launch(Game game, Properties properties) {
        EngineImpl engine = new EngineImpl();

        game.setEngine(engine);

        engine.init(properties);
        engine.setProjectTitle(
                (String) properties.getOrDefault("game.name", "Unnamed Game")
        );

        game.onInit();

        //todo: load config

        engine.run(game);
    }

    private static Properties loadProperties() {
        Properties props = new Properties();

        try {
            props.load(new FileInputStream("game.properties"));
        } catch (IOException e) {
            Debug.err("Failed to load game.properties");
        }

        return props;
    }
}
