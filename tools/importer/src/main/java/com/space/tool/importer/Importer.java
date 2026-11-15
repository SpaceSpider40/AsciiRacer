package com.space.tool.importer;

import com.space.engine.core.Debug;
import com.space.engine.core.Game;
import com.space.engine.core.assets.AssetId;
import com.space.engine.core.assets.data.components.MeshData;
import com.space.engine.core.exceptions.AssetException;
import com.space.engine.core.exceptions.imports.GLTFException;
import com.space.engine.core.graphics.TerminalWindow;
import com.space.engine.core.graphics.ui.Label;
import com.space.engine.core.graphics.ui.UiCanvas;
import com.space.engine.core.graphics.ui.widgets.ProgressBar;
import com.space.tool.importer.gltf.GLTFImporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Arguments:
 * 1. Path to project (game.properties)
 * 2. Path to gltf file
 * 3. Path to destination asset folder (relative of /assets)
 */
public class Importer extends Game {

    private final Path source;
    private final Path destination;


    private UiCanvas ui;

    private Label statusLabel;
    private ProgressBar progressBar;


    public Importer(Path src, Path dest) {
        source      = src;
        destination = dest;
    }

    @Override
    public void onInit() {

        var size = TerminalWindow.get().size();

        ui = new UiCanvas(getEngine());
        getEngine().ui().push(ui);

        String status = "Importing...";
        statusLabel = new Label((size.width() / 2) - (status.length()/2), size.height() / 2, status);

        progressBar = new ProgressBar(0, (size.height()/2)+1, size.width(), 1);

        ui.add(statusLabel);
        ui.add(progressBar);
    }

    @Override
    public void onStart() {
        try {
            Debug.log("Start Import");
            List<MeshData> meshes = GLTFImporter.load(source);

            progressBar.setProgress(.1f);

            Debug.warn("Imported " + meshes.size() + " meshes");
            progressBar.setProgress(.2f);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

//            int index = 0;
//            for (MeshData mesh : meshes) {
//                String name = destination.toAbsolutePath().resolve("importedmesh-" + index++).toString() + ".mesh";
//                getEngine().assets().save(new AssetId(name), mesh);
//            }
            progressBar.setProgress(.8f);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            progressBar.setProgress(1f);

        } catch (IOException | GLTFException e) {
            getEngine().shutdown();
            throw new RuntimeException(e);
        }

        getEngine().shutdown();
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onStep() {

    }

    @Override
    public void onShutdown() {

    }
}
