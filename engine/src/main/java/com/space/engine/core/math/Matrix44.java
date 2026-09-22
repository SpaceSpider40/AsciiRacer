package com.space.engine.core.math;

import java.util.Arrays;

public class Matrix44 {
    public final float[] m = new float[16];

    public Matrix44() {
        setIdentity();
    }

    public void setIdentity() {
        Arrays.fill(m, 0);
        m[0]  = 1;
        m[5]  = 1;
        m[10] = 1;
        m[15] = 1;
    }

    public Matrix44 mul(Matrix44 other) {
        Matrix44 res = new Matrix44();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                res.m[r * 4 + c] = this.m[r * 4 + 0] * other.m[0 * 4 + c] +
                        this.m[r * 4 + 1] * other.m[1 * 4 + c] +
                        this.m[r * 4 + 2] * other.m[2 * 4 + c] +
                        this.m[r * 4 + 3] * other.m[3 * 4 + c];
            }
        }

        return res;
    }

    public Matrix44 mul(Matrix44 other, Matrix44 out) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                out.m[r * 4 + c] = this.m[r * 4 + 0] * other.m[0 * 4 + c] +
                        this.m[r * 4 + 1] * other.m[1 * 4 + c] +
                        this.m[r * 4 + 2] * other.m[2 * 4 + c] +
                        this.m[r * 4 + 3] * other.m[3 * 4 + c];
            }
        }
        return out;
    }

    public Vector3 mulPoint(Vector3 v) {
        float x = m[0] * v.x + m[1] * v.y + m[2] * v.z + m[3];
        float y = m[4] * v.x + m[5] * v.y + m[6] * v.z + m[7];
        float z = m[8] * v.x + m[9] * v.y + m[10] * v.z + m[11];
        float w = m[12] * v.x + m[13] * v.y + m[14] * v.z + m[15];

        if (Math.abs(w - 1) > 0.0001f && Math.abs(w) > 0.0001f) {
            x /= w;
            y /= w;
            z /= w;
        }
        return new Vector3(x, y, z);
    }

    public Vector3 mulPoint(float[] vec) {
        float x = m[0] * vec[0] + m[1] * vec[1] + m[2] * vec[2] + m[3];
        float y = m[4] * vec[0] + m[5] * vec[1] + m[6] * vec[2] + m[7];
        float z = m[8] * vec[0] + m[9] * vec[1] + m[10] * vec[2] + m[11];
        float w = m[12] * vec[0] + m[13] * vec[1] + m[14] * vec[2] + m[15];

        if (Math.abs(w - 1) > 0.0001f && Math.abs(w) > 0.0001f) {
            x /= w;
            y /= w;
            z /= w;
        }
        return new Vector3(x, y, z);
    }

    public void mulPoint(float[] inVec, float[] outVec) {
        float x = m[0] * inVec[0] + m[1] * inVec[1] + m[2] * inVec[2] + m[3];
        float y = m[4] * inVec[0] + m[5] * inVec[1] + m[6] * inVec[2] + m[7];
        float z = m[8] * inVec[0] + m[9] * inVec[1] + m[10] * inVec[2] + m[11];
        float w = m[12] * inVec[0] + m[13] * inVec[1] + m[14] * inVec[2] + m[15];

        if (Math.abs(w - 1) > 0.0001f && Math.abs(w) > 0.0001f) {
            x /= w;
            y /= w;
            z /= w;
        }
        outVec[0] = x;
        outVec[1] = y;
        outVec[2] = z;
    }

    public Vector3 mulVector(Vector3 v) {
        float x = m[0] * v.x + m[1] * v.y + m[2] * v.z;
        float y = m[4] * v.x + m[5] * v.y + m[6] * v.z;
        float z = m[8] * v.x + m[9] * v.y + m[10] * v.z;

        return new Vector3(x, y, z);
    }

    /**
     * Sets the view matrix for a 3D camera based on the specified camera position
     * and orientation (rotation).
     *
     * @param cameraX   The x-coordinate of the camera position.
     * @param cameraY   The y-coordinate of the camera position.
     * @param cameraZ   The z-coordinate of the camera position.
     * @param rotationX The rotation angle around the x-axis in degrees.
     * @param rotationY The rotation angle around the y-axis in degrees.
     * @param rotationZ The rotation angle around the z-axis in degrees.
     * @return This {@code Matrix44} instance updated with the calculated view matrix.
     */
    public Matrix44 setViewMatrix(
            float cameraX,
            float cameraY,
            float cameraZ,
            float rotationX,
            float rotationY,
            float rotationZ) {
        float x = (float) Math.toRadians(-rotationX);
        float y = (float) Math.toRadians(-rotationY);
        float z = (float) Math.toRadians(-rotationZ);

        float cx = (float) Math.cos(x);
        float sx = (float) Math.sin(x);
        float cy = (float) Math.cos(y);
        float sy = (float) Math.sin(y);
        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);

        m[0] = cz * cy - sz * sx * sy;
        m[1] = -sz * cx;
        m[2] = cz * sy + sz * sx * cy;

        m[4] = sz * cy + cz * sx * sy;
        m[5] = cz * cx;
        m[6] = sz * sy - cz * sx * cy;

        m[8]  = -cx * sy;
        m[9]  = sx;
        m[10] = cx * cy;

        m[3] = -(m[0] * cameraX + m[1] * cameraY + m[2]  * cameraZ);
        m[7] = -(m[4] * cameraX + m[5] * cameraY + m[6]  * cameraZ);
        m[11] = -(m[8] * cameraX + m[9] * cameraY + m[10] * cameraZ);

        m[12] = 0;
        m[13] = 0;
        m[14] = 0;
        m[15] = 1;

        return this;
    }

    public Matrix44 setModelMatrix(
            Vector3 position,
            Vector3 rotation,
            Vector3 scale) {

        float x = (float) Math.toRadians(rotation.x);
        float y = (float) Math.toRadians(rotation.y);
        float z = (float) Math.toRadians(rotation.z);

        float cx = (float) Math.cos(x);
        float sx = (float) Math.sin(x);
        float cy = (float) Math.cos(y);
        float sy = (float) Math.sin(y);
        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);

        m[0] = (cy * cz + sy * sx * sz) * scale.x;
        m[1] = (-cy * sz + sy * sx * cz) * scale.y;
        m[2] = (sy * cx) * scale.z;
        m[3] = position.x;

        m[4] = (cx * sz) * scale.x;
        m[5] = (cx * cz) * scale.y;
        m[6] = (-sx) * scale.z;
        m[7] = position.y;

        m[8]  = (-sy * cz + cy * sx * sz) * scale.x;
        m[9]  = (sy * sz + cy * sx * cz) * scale.y;
        m[10] = (cy * cx) * scale.z;
        m[11] = position.z;

        m[12] = 0;
        m[13] = 0;
        m[14] = 0;
        m[15] = 1;

        return this;
    }

    public static Matrix44 createTranslation(float x, float y, float z) {
        Matrix44 mat = new Matrix44();
        mat.m[3]  = x;
        mat.m[7]  = y;
        mat.m[11] = z;
        return mat;
    }

    public static Matrix44 createRotationX(float angle) {
        Matrix44 mat = new Matrix44();
        float    cos = (float) Math.cos(angle);
        float    sin = (float) Math.sin(angle);
        mat.m[5]  = cos;
        mat.m[6]  = -sin;
        mat.m[9]  = sin;
        mat.m[10] = cos;

        return mat;
    }

    public static Matrix44 createRotationY(float angle) {
        Matrix44 mat = new Matrix44();
        float    cos = (float) Math.cos(angle);
        float    sin = (float) Math.sin(angle);
        mat.m[0]  = cos;
        mat.m[2]  = sin;
        mat.m[8]  = -sin;
        mat.m[10] = cos;

        return mat;
    }

    public static Matrix44 createRotationZ(float angle) {
        Matrix44 mat = new Matrix44();
        float    cos = (float) Math.cos(angle);
        float    sin = (float) Math.sin(angle);
        mat.m[0] = cos;
        mat.m[1] = -sin;
        mat.m[4] = sin;
        mat.m[5] = cos;
        return mat;
    }

    public static Matrix44 createScale(float x, float y, float z) {
        Matrix44 mat = new Matrix44();
        mat.m[0]  = x;
        mat.m[5]  = y;
        mat.m[10] = z;
        return mat;
    }

    public static Matrix44 createModelMatrix(
            Vector3 position,
            Vector3 rotation,
            Vector3 scale) {
        Matrix44 mat = new Matrix44();

        float x = (float) Math.toRadians(rotation.x);
        float y = (float) Math.toRadians(rotation.y);
        float z = (float) Math.toRadians(rotation.z);

        float cx = (float) Math.cos(x);
        float sx = (float) Math.sin(x);
        float cy = (float) Math.cos(y);
        float sy = (float) Math.sin(y);
        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);

        mat.m[0] = (cy * cz + sy * sx * sz) * scale.x;
        mat.m[1] = (-cy * sz + sy * sx * cz) * scale.y;
        mat.m[2] = (sy * cx) * scale.z;
        mat.m[3] = position.x;

        mat.m[4] = (cx * sz) * scale.x;
        mat.m[5] = (cx * cz) * scale.y;
        mat.m[6] = (-sx) * scale.z;
        mat.m[7] = position.y;

        mat.m[8]  = (-sy * cz + cy * sx * sz) * scale.x;
        mat.m[9]  = (sy * sz + cy * sx * cz) * scale.y;
        mat.m[10] = (cy * cx) * scale.z;
        mat.m[11] = position.z;

        mat.m[12] = 0;
        mat.m[13] = 0;
        mat.m[14] = 0;
        mat.m[15] = 1;

        return mat;
    }

    public static Matrix44 createViewMatrix(
            Vector3 cameraPosition,
            Vector3 cameraRotation) {
        Matrix44 invTranslation = Matrix44.createTranslation(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);

        Matrix44 invRotationX = Matrix44.createRotationX((float) Math.toRadians(-cameraRotation.x));
        Matrix44 invRotationY = Matrix44.createRotationY((float) Math.toRadians(-cameraRotation.y));
        Matrix44 invRotationZ = Matrix44.createRotationZ((float) Math.toRadians(-cameraRotation.z));

        Matrix44 invRotation = invRotationZ.mul(invRotationX).mul(invRotationY);

        return invTranslation.mul(invRotation);
    }

    public static Matrix44 createViewMatrix(
            float[] cameraPosition,
            float[] cameraRotation) {
        Matrix44 invTranslation = Matrix44.createTranslation(-cameraPosition[0], -cameraPosition[1],
                -cameraPosition[2]);

        Matrix44 invRotationX = Matrix44.createRotationX((float) Math.toRadians(-cameraRotation[0]));
        Matrix44 invRotationY = Matrix44.createRotationY((float) Math.toRadians(-cameraRotation[1]));
        Matrix44 invRotationZ = Matrix44.createRotationZ((float) Math.toRadians(-cameraRotation[2]));

        Matrix44 invRotation = invRotationZ.mul(invRotationX).mul(invRotationY);

        return invTranslation.mul(invRotation);
    }

    public static Matrix44 createViewMatrix(
            float cameraX,
            float cameraY,
            float cameraZ,
            float rotationX,
            float rotationY,
            float rotationZ) {
        Matrix44 mat = new Matrix44();

        float x = (float) Math.toRadians(-rotationX);
        float y = (float) Math.toRadians(-rotationY);
        float z = (float) Math.toRadians(-rotationZ);

        float cx = (float) Math.cos(x);
        float sx = (float) Math.sin(x);
        float cy = (float) Math.cos(y);
        float sy = (float) Math.sin(y);
        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);

        mat.m[0] = cz * cy - sz * sx * sy;
        mat.m[1] = -sz * cx;
        mat.m[2] = cz * sy + sz * sx * cy;
        mat.m[3] = -cameraX;

        mat.m[4] = sz * cy + cz * sx * sy;
        mat.m[5] = cz * cx;
        mat.m[6] = sz * sy - cz * sx * cy;
        mat.m[7] = -cameraY;

        mat.m[8]  = -cx * sy;
        mat.m[9]  = sx;
        mat.m[10] = cx * cy;
        mat.m[11] = -cameraZ;

        mat.m[12] = 0;
        mat.m[13] = 0;
        mat.m[14] = 0;
        mat.m[15] = 1;

        return mat;
    }
}
