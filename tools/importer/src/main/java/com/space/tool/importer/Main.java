package com.space.tool.importer;

import com.space.engine.core.Launcher;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Properties;

//
// inputes path to folder where game.properties are localized. From props finds paths to save assets;
public class Main {
    static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("Needed arguments: source destination");
        }

        Path propertiesPath  = Path.of(args[0]).normalize();
        Path sourcePath      = Path.of(args[1]).normalize();

        if (!(sourcePath.toString().endsWith(".gltf") || sourcePath.toString().endsWith(".glb"))) {
            throw new Exception("unsupported format or invalid path");
        }

        Properties props = new Properties();

        props.load(new FileInputStream(propertiesPath.toFile()));

        Launcher.launch(new Importer(sourcePath), props);
    }
}
