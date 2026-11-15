package com.space.engine.core.graphics.ui.layout;

import com.space.engine.core.graphics.ui.UiElement;
import lombok.Getter;

import java.util.Arrays;

public class Box extends UiElement {

    public Box(int x, int y, int width, int height) {
        super(x, y, width, height);

        Arrays.fill(chars, ' ');
    }

    @Override
    public void update(double deltaTime) {

    }
}
