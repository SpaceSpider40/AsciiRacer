package com.space.tool.importer;

import com.space.engine.core.Debug;
import com.space.engine.core.Engine;
import com.space.engine.core.Game;
import com.space.engine.core.actors.components.MeshComponent;
import com.space.engine.core.assets.AssetId;
import com.space.engine.core.assets.data.components.MeshData;
import com.space.engine.core.exceptions.AssetException;
import com.space.engine.core.exceptions.imports.GLTFException;
import com.space.engine.core.graphics.TerminalWindow;
import com.space.tool.importer.gltf.GLTFImporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Arguments:
 * 1. Path to project (game.properties)
 * 2. Path to gltf file
 * 3. Path to destination asset folder (relative of /assets)
 *
 * java -jar .\tools\AEImporter.jar C:\Projects\Games\AsciiRacer\dist\game.properties C:\Projects\Games\AsciiRacer\dist\assets\raw\AsciiRacer_Tree_1.glb
 */
public class Importer extends Game {

    private final Path source;

    public Importer(Path src) {
        source      = src;
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onStart() {

        //remove extension
        String fileName = source.getFileName().toString().substring(0, source.getFileName().toString().indexOf('.'));

        try {
            Debug.log("Start Import");
            List<MeshData> meshes = GLTFImporter.load(source);

            int index = 0;
            for (MeshData mesh : meshes) {
                String name = "mesh_" + fileName + "_" + index++ + ".mesh";
                var comp = new MeshComponent(mesh.getVertices(), mesh.getIndices());

                getEngine().assets().save(new AssetId(name), comp);
            }

        } catch (IOException | GLTFException e) {
            getEngine().shutdown();
            throw new RuntimeException(e);
        } catch (AssetException e) {
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
