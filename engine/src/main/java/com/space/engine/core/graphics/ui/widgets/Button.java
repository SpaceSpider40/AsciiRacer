package com.space.engine.core.graphics.ui.widgets;

import com.space.engine.core.graphics.ui.UiElement;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class Button extends UiElement {

    @Setter
    @Getter
    private String text;

    @Setter
    private Consumer<Button> onPress;

    protected Button(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update(double deltaTime) {

    }
}
