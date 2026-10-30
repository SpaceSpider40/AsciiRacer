package com.space.engine.core.graphics.ui;

import com.space.engine.core.graphics.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class UIElement {
    protected int x, y;
    protected int width, height;

    @Setter
    protected byte layer;

    protected char[] chars;
    protected int[]  colors;

    @Setter
    protected boolean isVisible;

    protected UIElement(
            int x,
            int y,
            int width,
            int height
    ) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;

        chars  = new char[width * height];
        colors = new int[width * height];
    }

    public abstract void update(double deltaTime);

    protected void put(int x, int y, char c) {
        if (x < 0 || y < 0 || x >= width || y >= height) return;

        chars[x + y * width] = c;
    }

    protected void put(int x, int y, char c, int color) {
        put(x, y, c);

        colors[x + y * width] = color;
    }

    protected void putText(int x, int y, String text) {
        for (int i = 0; i < text.length(); i++) {
            put(x + i, y, text.charAt(i));
        }
    }

    protected void putText(int x, int y, String text, Color color){
        for (int i = 0; i < text.length(); i++) {
            put(x + i, y, text.charAt(i), color.pack());
        }
    }
}
