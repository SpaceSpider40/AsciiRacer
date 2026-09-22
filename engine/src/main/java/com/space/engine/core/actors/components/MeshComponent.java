package com.space.engine.core.actors.components;

public class MeshComponent extends ActorComponent {
    public final float[] vertices;
    public final float[] normals;

    public final int[] indices;

    public MeshComponent(float[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices  = indices;

        this.normals = calculateTriangleNormals(vertices, indices);
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onUpdate(double deltaTime) {

    }

    @Override
    protected void onStep(double deltaTime) {

    }

    @Override
    protected void onEnd() {

    }

    public int triangleCount() {
        if (indices.length == 0) return 0;
        return indices.length / 3;
    }

    public int vertexCount() {
        if (vertices.length == 0) return 0;
        return vertices.length / 3;
    }

    public static float[] calculateTriangleNormals(float[] vertices, int[] indices) {
        float[] normals = new float[indices.length];

        for (int i = 0; i < indices.length; i += 3) {
            int i0 = indices[i] * 3;
            int i1 = indices[i + 1] * 3;
            int i2 = indices[i + 2] * 3;

            float ax = vertices[i1] - vertices[i0];
            float ay = vertices[i1 + 1] - vertices[i0 + 1];
            float az = vertices[i1 + 2] - vertices[i0 + 2];

            float bx = vertices[i2] - vertices[i0];
            float by = vertices[i2 + 1] - vertices[i0 + 1];
            float bz = vertices[i2 + 2] - vertices[i0 + 2];

            float nx = ay * bz - az * by;
            float ny = az * bx - ax * bz;
            float nz = ax * by - ay * bx;

            float lengthSquared = nx * nx + ny * ny + nz * nz;

            if (lengthSquared != 0.0f) {
                float invLength = 1.0f / (float) Math.sqrt(lengthSquared);
                nx *= invLength;
                ny *= invLength;
                nz *= invLength;
            }

            normals[i]     = nx;
            normals[i + 1] = ny;
            normals[i + 2] = nz;
        }

        return normals;
    }
}
