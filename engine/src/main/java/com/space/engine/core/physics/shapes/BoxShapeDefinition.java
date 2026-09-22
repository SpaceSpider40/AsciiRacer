package com.space.engine.core.physics.shapes;

import com.space.engine.core.math.Vector3;

public record BoxShapeDefinition(Vector3 halfExtents) implements ShapeDefinition {
}
