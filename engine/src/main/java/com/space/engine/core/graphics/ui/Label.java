package com.space.engine.core.graphics.ui;

import com.space.engine.core.graphics.Color;

public class Label extends UiElement {

    private final String text;
    private final Color color = new Color(255, 0, 255);

    public Label(int x, int y, String text) {
        this(x, y, text.length(), 1, text);
    }

    public Label(int x, int y, int width, int height, String text) {
        super(x, y, width, height);

        this.text = text;

        putText(0, 0, text, color);
    }

    public Label(int x, int y, int width, int height, String text, Color color) {
        super(x, y, width, height);

        this.text = text;
        this.color.set(color);

        putText(0, 0, text, color);
    }

    @Override
    public void update(double deltaTime) {
    }
}
