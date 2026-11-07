package com.space.engine.core.graphics;

import com.space.engine.core.graphics.ui.UiElement;
import com.space.engine.core.math.Triangle;
import com.space.engine.core.math.Vector3;

public interface Renderer {
    void init(int width, int height);

    void camera(float[] position, float[] rotation, float close, float far, float fov);

    /**
     * submits ui for rendering
     * @param obj the array of ui elements should be sorted.
     */
    void submit(UiElement obj);
    void submit(RenderObject obj);

    // Each object has to have position 2D, width, heitht

    void draw();

    record VisualInstance(
            Vector3 position,
            Vector3 rotation,
            Vector3 scale,
            Triangle[] tris,
            Color[] colors) {
    }
}
