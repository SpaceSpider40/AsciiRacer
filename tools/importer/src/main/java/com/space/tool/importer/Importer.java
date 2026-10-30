package com.space.tool.importer;

import com.space.engine.core.Debug;
import com.space.engine.core.Game;
import com.space.engine.core.assets.AssetId;
import com.space.engine.core.assets.data.components.MeshData;
import com.space.engine.core.exceptions.AssetException;
import com.space.engine.core.exceptions.imports.GLTFException;
import com.space.tool.importer.gltf.GLTFImporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Importer extends Game {

    private final Path source;
    private final Path destination;

    public Importer(Path src, Path dest) {
        source      = src;
        destination = dest;
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onStart() {
        try {
            Debug.log("Start Import");
            List<MeshData> meshes = GLTFImporter.load(source);

            Debug.warn("Imported " + meshes.size() + " meshes");

            int index = 0;
            for (MeshData mesh : meshes) {
                String name = destination.toAbsolutePath().resolve("importedmesh-" + index++).toString() + ".mesh";
                getEngine().assets().save(new AssetId(name), mesh);
            }

        } catch (IOException | GLTFException | AssetException e) {
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
