package com.space.engine.core;

import com.space.engine.core.actors.Actor;
import com.space.engine.core.actors.components.MaterialComponent;
import com.space.engine.core.actors.components.MeshComponent;
import com.space.engine.core.assets.AssetId;
import com.space.engine.core.assets.AssetManager;
import com.space.engine.core.assets.data.ActorData;
import com.space.engine.core.assets.data.CameraData;
import com.space.engine.core.assets.data.FreeCameraData;
import com.space.engine.core.assets.data.WorldData;
import com.space.engine.core.assets.data.components.MaterialData;
import com.space.engine.core.assets.data.components.MeshData;
import com.space.engine.core.assets.factories.*;
import com.space.engine.core.exceptions.AssetException;
import com.space.engine.core.graphics.AsciiRendererV3;
import com.space.engine.core.graphics.Color;
import com.space.engine.core.graphics.ui.DebugStatsUIElement;
import com.space.engine.core.graphics.ui.Label;
import com.space.engine.core.graphics.ui.UiCanvas;
import com.space.engine.core.graphics.ui.UiManager;
import com.space.engine.core.log.FileWriter;
import com.space.engine.core.physics.Physics;
import com.space.engine.core.util.TerminalSizer;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public final class EngineImpl implements Engine {

    public static final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("wind");

    private static final double TPS = 1000.0 / 60.0;
    private static final double FPS = 1000.0 / 60.0; // 0.08f

    private static final List<EngineState> initializableStates = List.of(
            EngineState.CREATED,
            EngineState.STOPPED,
            EngineState.FAILED);

    private final WorldManager worldManager = new WorldManager();
    private AssetManager assetManager;
    private UiManager uiManager;

    private Config config;

    private EngineState state = EngineState.CREATED;

    private double deltaTime = 0;
    private double renderTime = 0;
    private double physicsTime = 0;

    public EngineState state() {
        return state;
    }

    public void init(Properties properties) {
        if (!initializableStates.contains(state)) {
            return; // already initialized
        }
        state = EngineState.INITIALIZING;

        // todo: make some abstract class "window manager"
        System.out.print(TerminalCodes.showCursor(false));
        System.out.print(TerminalCodes.moveCursor(1, 1));
        System.out.print(TerminalCodes.clear());

        // todo: read config file
        try {
            config = ConfigScanner.scan();
        } catch (IOException e) {
            config = new Config(true);
            // throw new RuntimeException(e);
        }

        // todo move to separate method
        if (config.debug()) {
            try {
                Debug.writer = new FileWriter();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        initAssetManager(properties);

        try {
            Input.init();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Init redering
        var terminalSize = TerminalSizer.getTerminalSize();
        AsciiRendererV3.get().init(
                terminalSize.width(),
                terminalSize.height());
        // Init ui
        uiManager = new UiManager(this);

        // init physics0.00000001f
        if (!Physics.get().init(0.986f)) {
            throw new RuntimeException("Failed to initialize Physics!");
        }

        // register all buildin writers and readers

        // todo: if project file exist load last used scene

        state = EngineState.INITIALIZED;
    }

    public <T> T load(AssetId assetId, Class<T> type) {
        return null;
    }

    // todo: this should be read from some project file
    public void setProjectTitle(String title) {
        System.out.print("\033]2;" + title + "\033\\");
    }

    public World currentWorld() {
        return worldManager.getCurrentWorld();
    }

    public void loadWorld(String path) {
        try {
            World world = assetManager.load(new AssetId(path));

            worldManager.setCurrentWorld(path, world);
        } catch (AssetException e) {
            throw new RuntimeException(e);
        }
    }

    public AssetManager assets() {
        return assetManager;
    }

    public double dt() {
        return deltaTime;
    }

    public float dtf() {
        return (float) dt();
    }

    public int createEntity() {
        return -1;
    }

    public void destroyEntity(int entity) {

    }

    public void run(Game game) {
        state = EngineState.RUNNING;

        game.onStart();

        loop(game); // for now no the same thread
        // updateThread.run();
    }

    public void shutdown() {
        state = EngineState.STOPPING;

        cleanup();

        state = EngineState.STOPPED;
    }

    @SuppressWarnings("D")
    private void loop(Game game) {
        final double maxFrameTimeMs = 250.0; // todo: move to config

        double accumulator = 0;

        long previousTime = System.nanoTime();

        var size = TerminalSizer.getTerminalSize();
        var ele = new DebugStatsUIElement(
                size.width() - 16,
                0,
                16,
                3,
                () -> deltaTime,
                () -> renderTime,
                () -> physicsTime);

        var canvas = new UiCanvas(this);
        canvas.add(ele);
        uiManager.push(canvas);

        var label = new Label(size.width() / 2, size.height() / 2, "Test Label");
        canvas.add(label);

        // todo: temp call on start
        currentWorld().getActors().forEach(Actor::start);

        while (state == EngineState.RUNNING) {
            var frameStartTime = System.nanoTime();

            double frameTimeMs = (frameStartTime - previousTime) / 1_000_000.0;
            previousTime = frameStartTime;

            frameTimeMs = Math.min(frameTimeMs, maxFrameTimeMs);

            deltaTime = frameTimeMs;
            accumulator += frameTimeMs;

            // tick
            while (accumulator >= TPS) {
                game.onStep();
                step();
                accumulator -= TPS;
            }

            // update
            game.onUpdate();
            update();

            // render
            long renderStartTime = System.nanoTime();
            AsciiRendererV3.get().startDrawing(worldManager.getActiveCamera());

            // todo: temp
            worldManager.getCurrentWorld().getActors().forEach(a -> {

                if (a.hasComponent(MeshComponent.class)) {
                    var mesh = a.getComponent(MeshComponent.class);
                    var material = a.getComponent(MaterialComponent.class);
                    if (material == null) {
                        material = new MaterialComponent();
                    }

                    AsciiRendererV3.get().draw(a.getModelMatrix(), mesh, material);
                }
            });
            uiManager.draw();

            AsciiRendererV3.get().endDrawing();
            renderTime = (System.nanoTime() - renderStartTime) / 1_000_000.0;

            double elapsedMs = (System.nanoTime() - frameStartTime) / 1_000_000.0;
            double sleepMs = FPS - elapsedMs;

            if (sleepMs > 0.0) {
                try {
                    long sleepMillis = (long) sleepMs;
                    int sleepNanos = (int) ((sleepMs - sleepMillis) * 1_000_000.0);

                    Thread.sleep(sleepMillis, sleepNanos);
                } catch (InterruptedException e) {
                    shutdown();
                    Thread.currentThread().interrupt();
                }
            }
        }

        cleanup();
    }

    private void update() {
        Input.update();

        if (Input.IsKeyDown(Input.KEY_X))
            shutdown();// todo: temp exit key

        for (Actor actor : worldManager.getCurrentWorld().getActors()) {
            actor.update(deltaTime);
        }

        uiManager.update(deltaTime);
    }

    private void step() {
        // todo: check if terminal size has changed. if so restart the renderer
        for (Actor actor : worldManager.getCurrentWorld().getActors()) {
            actor.step(TPS);
        }

        long startPhysicsTime = System.nanoTime();
        Physics.get().step(TPS);
        physicsTime = (System.nanoTime() - startPhysicsTime) / 1_000_000.0;
    }

    private void cleanup() {
        Input.clean();

        System.out.print(TerminalCodes.showCursor(true));
        System.out.print(TerminalCodes.moveCursor(1, 1));
        System.out.print(TerminalCodes.clear());
        System.out.print(TerminalCodes.resetColors());
    }

    private void initAssetManager(Properties properties) {
        if (state != EngineState.INITIALIZING)
            return; // can invoke only while initializing

        assetManager = new AssetManager(properties);
        assetManager.register("world", WorldData.class, new WorldAssetFactory());
        assetManager.register("actor", ActorData.class, new ActorAssetFactory());
        assetManager.register("cameraactor", CameraData.class, new CameraAssetFactory());
        assetManager.register("freecameraactor", FreeCameraData.class, new FreeCameraAssetFactory());

        assetManager.register("mesh", MeshData.class, new MeshComponentAssetFactory());
        assetManager.register("material", MaterialData.class, new MaterialComponentAssetFactory());

    }

    // todo: move under window manager
    public static class TerminalCodes {
        public static String showCursor(boolean show) {
            return show ? "\033[?25h" : "\033[?25l";
        }

        public static String moveCursor(int x, int y) {
            return "\033[" + y + ";" + x + "H";
        }

        public static String clear() {
            return "\033[0J";
        }

        public static String resetColors() {
            return "\033[0m";
        }
    }
}
