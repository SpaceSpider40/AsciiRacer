package com.space.engine.core.graphics.ui.widgets;

import com.space.engine.core.graphics.Color;
import com.space.engine.core.graphics.ui.UiElement;
import lombok.Getter;
import lombok.Setter;

public class ProgressBar extends UiElement {

    @Setter
    @Getter
    private char progressSymbol = '*';

    @Getter
    @Setter
    private float progress = 0;

    public ProgressBar(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update(double deltaTime) {
        clear();

        int progressInt = (int) (progress * getWidth());

        putText(0, 0, String.valueOf(progressSymbol).repeat(Math.max(0, progressInt)), new Color(255, 0, 0));
    }
}
