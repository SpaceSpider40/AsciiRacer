package com.space.engine.core.graphics.ui;

import com.space.engine.core.graphics.Color;

public class Label extends UIElement {

  private final String text;

  public Label(int x, int y, String text) {
    this(x, y, text.length() * 10, 20, text);
  }

  public Label(int x, int y, int width, int height, String text) {
    super(x, y, width, height);

    this.text = text;

    putText(x, y, text, Color.RED);
  }

  @Override
  public void update(double deltaTime) {
  }
}
