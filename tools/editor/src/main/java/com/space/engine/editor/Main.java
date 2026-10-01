package com.space.engine.editor;

import com.space.engine.core.Debug;
import com.space.engine.core.Launcher;

import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        //todo: pass world in args

        Launcher.launch(new Editor());

        var path = Paths.get(".");

        Debug.log("Current dir: " + path.toAbsolutePath());
    }
}
