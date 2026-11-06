package com.space.engine.core.graphics.ui;

import com.space.engine.core.Engine;
import com.space.engine.core.Update;
import com.space.engine.core.graphics.AsciiRendererV3;

import java.util.HashMap;
import java.util.Vector;

public class UiManager implements Update {

    public static final byte MAX_CANVAS_LAVER = Byte.MAX_VALUE - 1;

    // todo: change to stack based on simple arrary for perforamnce
    private final Vector<UiCanvas> canvases = new Vector<>(MAX_CANVAS_LAVER);
    private final Engine engine;

    public UiManager(Engine engine) {
        this.engine = engine;

        canvases.setSize(MAX_CANVAS_LAVER);
    }

    public void push(UiCanvas uiCanvas) {
        canvases.add(uiCanvas);
    }

    public void pop() {
        canvases.remove((byte) (canvases.size() - 1));
    }

    public void draw() {
        for (int i = 0; i < canvases.size(); i++) {
            UiCanvas canvas = canvases.get((byte) i);

            if (canvas == null)
                continue;

            for (UIElement element : canvas.getElements()) {
                AsciiRendererV3.get().draw(element);
            }
        }
    }

    @Override
    public void update(double delta) {
        for (int i = 0; i < canvases.size(); i++) {
            UiCanvas canvas = canvases.get((byte) i);

            if (canvas == null)
                continue;

            for (UIElement element : canvas.getElements()) {
                element.update(delta);
            }
        }
    }
}
