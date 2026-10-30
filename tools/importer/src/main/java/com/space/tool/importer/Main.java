package com.space.tool.importer;

import com.space.engine.core.Launcher;

import java.nio.file.Path;

//
// inputes path to folder where game.properties are localized. From props finds paths to save assets;
public class Main {
    static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("Needed arguments: source destination");
        }

        Path source      = Path.of(args[0]).normalize();
        Path destination = Path.of(args[1]).normalize();

        if (!(source.toString().endsWith(".gltf") || source.toString().endsWith(".glb"))) {
            throw new Exception("unsupported format or invalid path");
        }

        Launcher.launch(new Importer(source, destination));
    }
}
