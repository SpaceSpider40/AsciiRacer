package com.space.engine.core.graphics.ui;

import com.space.engine.core.Engine;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UiCanvas {
    public static final int MAX_ELEMENTS = 50;

    private final List<UiElement> elements = new ArrayList<>();
    private final Engine engine;

    public UiCanvas(Engine engine) {
        this.engine = engine;
    }

    public void add(UiElement element){
        if(elements.size() >= MAX_ELEMENTS) return;

        elements.add(element);
    }
}
