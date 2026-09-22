package com.space.engine.core.math;

public final class SMath {
    public static float VectorDot(float[] v0, float[] v1) {
        return v0[0] * v1[0] + v0[1] * v1[1] + v0[2] * v1[2];
    }

    public static float VectorDot(float vx0, float vy0, float vz0, float vx1, float vy1, float vz1) {
        return vx0 * vx1 + vy0 * vy1 + vz0 * vz1;
    }
}
