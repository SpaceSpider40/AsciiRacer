package com.space.engine.core.graphics;

import com.space.engine.core.Debug;
import com.space.engine.core.actors.CameraActor;
import com.space.engine.core.actors.components.MaterialComponent;
import com.space.engine.core.actors.components.MeshComponent;
import com.space.engine.core.graphics.ui.UiElement;
import com.space.engine.core.math.Matrix44;
import com.space.engine.core.math.SMath;
import com.space.engine.core.math.Vector3;

import java.util.Arrays;

public class AsciiRendererV3 {
    private static final AsciiRendererV3 INSTANCE = new AsciiRendererV3();

    private final Matrix44 eyeMatrix = new Matrix44();

    private final StringBuilder outBuilder = new StringBuilder();
    private final String ramp = ".,-~:;=!*#$@";

    private final Vector3 lightDir = new Vector3(1.0f, -2.0f, -1.5f).normalize();
    private final float ambient = 0.1f;

    private final Matrix44 mvMatrix = new Matrix44();

    private CameraActor camera;

    private char[] frameBuffer = new char[0];
    private float[] depthBuffer = new float[0];
    private int[] colorBuffer = new int[0];

    private int width;
    private int height;

    private AsciiRendererV3() {

    }

    public static AsciiRendererV3 get() {
        return INSTANCE;
    }

    public void init(int width, int height) {
        this.width = width;
        this.height = height;

        int length = width * height;
        this.frameBuffer = new char[length];
        this.depthBuffer = new float[length];
        this.colorBuffer = new int[length];

        Debug.log("Renderer initialized with " + length + " pixels");
    }

    public void startDrawing(CameraActor camera) {
        startDrawing(camera, Color.BLACK);
    }

    /**
     * Prepares the rendering system to start a new frame by clearing the buffers
     * and updating the camera's view matrix based on its position and orientation.
     *
     * @param camera the camera actor whose transform is used to compute the view
     *               matrix
     */
    public void startDrawing(CameraActor camera, Color clearColor) {
        // clear
        Arrays.fill(frameBuffer, ' ');
        Arrays.fill(depthBuffer, 0);
        Arrays.fill(colorBuffer, clearColor.pack());

        this.eyeMatrix.setIdentity();

        // set up eye matrix
        this.camera = camera;

        eyeMatrix.setViewMatrix(
                camera.transform.position.x,
                camera.transform.position.y,
                camera.transform.position.z,
                camera.transform.rotation.x,
                camera.transform.rotation.y,
                camera.transform.rotation.z);
    }

    public void draw(Matrix44 modelMatrix, MeshComponent mesh, MaterialComponent material) {

        float[] mm = modelMatrix.m;

        mvMatrix.setIdentity();
        eyeMatrix.mul(modelMatrix, mvMatrix);

        float[] mv = mvMatrix.m;

        for (int i = 0; i < mesh.indices.length; i += 3) {
            int i0 = mesh.indices[i];
            int i1 = mesh.indices[i + 1];
            int i2 = mesh.indices[i + 2];

            int v0 = i0 * 3;
            int v1 = i1 * 3;
            int v2 = i2 * 3;

            float vx0 = mesh.vertices[v0];
            float vy0 = mesh.vertices[v0 + 1];
            float vz0 = mesh.vertices[v0 + 2];

            float vx1 = mesh.vertices[v1];
            float vy1 = mesh.vertices[v1 + 1];
            float vz1 = mesh.vertices[v1 + 2];

            float vx2 = mesh.vertices[v2];
            float vy2 = mesh.vertices[v2 + 1];
            float vz2 = mesh.vertices[v2 + 2];

            // apply model view matrix same operations as Matrix44.mulPoint() but extracted
            // to prevent any object allocations
            float tx = mv[0] * vx0 + mv[1] * vy0 + mv[2] * vz0 + mv[3];
            float ty = mv[4] * vx0 + mv[5] * vy0 + mv[6] * vz0 + mv[7];
            float tz = mv[8] * vx0 + mv[9] * vy0 + mv[10] * vz0 + mv[11];
            vx0 = tx;
            vy0 = ty;
            vz0 = tz;

            tx = mv[0] * vx1 + mv[1] * vy1 + mv[2] * vz1 + mv[3];
            ty = mv[4] * vx1 + mv[5] * vy1 + mv[6] * vz1 + mv[7];
            tz = mv[8] * vx1 + mv[9] * vy1 + mv[10] * vz1 + mv[11];
            vx1 = tx;
            vy1 = ty;
            vz1 = tz;

            tx = mv[0] * vx2 + mv[1] * vy2 + mv[2] * vz2 + mv[3];
            ty = mv[4] * vx2 + mv[5] * vy2 + mv[6] * vz2 + mv[7];
            tz = mv[8] * vx2 + mv[9] * vy2 + mv[10] * vz2 + mv[11];
            vx2 = tx;
            vy2 = ty;
            vz2 = tz;

            int triangleIndex = i / 3;

            float nx = mesh.normals[triangleIndex * 3];
            float ny = mesh.normals[triangleIndex * 3 + 1];
            float nz = mesh.normals[triangleIndex * 3 + 2];
            // normals transformation, same as Matrix44.mulVector() but without allocations
            float tnx = mm[0] * nx + mm[1] * ny + mm[2] * nz;
            float tny = mm[4] * nx + mm[5] * ny + mm[6] * nz;
            float tnz = mm[8] * nx + mm[9] * ny + mm[10] * nz;

            float lenSq = tnx * tnx + tny * tny + tnz * tnz;
            // normalizing normals
            if (lenSq != 0.0f) {
                float invLen = 1.0f / (float) Math.sqrt(lenSq);
                nx = tnx * invLen;
                ny = tny * invLen;
                nz = tnz * invLen;
            }

            // backface culling in view space
            float edge1x = vx1 - vx0;
            float edge1y = vy1 - vy0;
            float edge1z = vz1 - vz0;

            float edge2x = vx2 - vx0;
            float edge2y = vy2 - vy0;
            float edge2z = vz2 - vz0;

            float faceNx = edge1y * edge2z - edge1z * edge2y;
            float faceNy = edge1z * edge2x - edge1x * edge2z;
            float faceNz = edge1x * edge2y - edge1y * edge2x;

            float facing = faceNx * vx0 + faceNy * vy0 + faceNz * vz0;

            if (facing >= 0.0f) {
                continue;
            }

            // near plane clipping //todo buggy for big objects find better way
            if (vz0 < camera.near || vz1 < camera.near || vz2 < camera.near)
                continue;
            // far plane clipping //todo buggy for big objects find better way
            if (vz0 > camera.far || vz1 > camera.far || vz2 > camera.far)
                continue;

            // simple global light
            float diffuse = SMath.VectorDot(nx, ny, nz, lightDir.x, lightDir.y, lightDir.z);
            float intensity = (Math.max(0.0f, diffuse) * (1.0f - ambient)) + ambient;
            char ch = ramp.charAt((int) (intensity * ramp.length() - 1));

            // perspective
            float ooz = 1.0f / vz0;
            vx0 = projectX(vx0, ooz);
            vy0 = projectY(vy0, ooz);

            ooz = 1.0f / vz1;
            vx1 = projectX(vx1, ooz);
            vy1 = projectY(vy1, ooz);

            ooz = 1.0f / vz2;
            vx2 = projectX(vx2, ooz);
            vy2 = projectY(vy2, ooz);

            // bounding box
            int minX = (int) Math.max(0, Math.min(vx0, Math.min(vx1, vx2)));
            int maxX = (int) Math.min(width - 1, Math.max(vx0, Math.max(vx1, vx2)));
            int minY = (int) Math.max(0, Math.min(vy0, Math.min(vy1, vy2)));
            int maxY = (int) Math.min(height - 1, Math.max(vy0, Math.max(vy1, vy2)));

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    float w0Edge = edgeFunction(vx1, vy1, vx2, vy2, x, y);
                    float w1Edge = edgeFunction(vx2, vy2, vx0, vy0, x, y);
                    float w2Edge = edgeFunction(vx0, vy0, vx1, vy1, x, y);

                    if ((w0Edge >= 0 && w1Edge >= 0 && w2Edge >= 0) ||
                            (w0Edge <= 0 && w1Edge <= 0 && w2Edge <= 0)) {
                        float totalArea = w0Edge + w1Edge + w2Edge;
                        if (Math.abs(totalArea) < 0.0001f)
                            continue;

                        float b0 = w0Edge / totalArea;
                        float b1 = w1Edge / totalArea;
                        float b2 = w2Edge / totalArea;

                        float pixelZ = b0 * vz0 + b1 * vz1 + b2 * vz2;
                        float pz = 1.0f / pixelZ;

                        int bufferIdx = x + y * width;
                        if (pz > depthBuffer[bufferIdx]) {
                            depthBuffer[bufferIdx] = pz;
                            frameBuffer[bufferIdx] = ch;
                            colorBuffer[bufferIdx] = material.albedo.pack();
                        }
                    }
                }
            }
        }
    }

    public void draw(UiElement element) {
        int elementX = element.getX();
        int elementY = element.getY();
        int elementWidth = element.getWidth();
        int elementHeight = element.getHeight();
        char[] elementChars = element.getChars();
        int[] elementColors = element.getColors();

        for (int localY = 0; localY < elementHeight; localY++) {
            int screenY = elementY + localY;

            if (screenY < 0 || screenY >= height) {
                continue;
            }

            for (int localX = 0; localX < elementWidth; localX++) {
                int screenX = elementX + localX;

                if (screenX < 0 || screenX >= width) {
                    continue;
                }

                int elementIndex = localX + localY * elementWidth;
                int frameIndex = screenX + screenY * width;

                char c = elementChars[elementIndex];

                if (c == '\0') {
                    continue;
                }

                frameBuffer[frameIndex] = c;
                depthBuffer[frameIndex] = Float.MAX_VALUE - 1;
                colorBuffer[frameIndex] = elementColors[elementIndex];
            }
        }
    }

    public void endDrawing() {
        drawDebug();
        print();
    }

    private void drawDebug() {
        int messageY = 0;
        for (Debug.Message message : Debug.getMessages()) {
            int messageX = 0;

            // if (messageY - 1 >= height) break;

            for (char c : message.msg().toCharArray()) {
                if (messageX >= width)
                    break;

                int bufferIdx = messageX + messageY * width;

                if (bufferIdx >= frameBuffer.length)
                    break;

                frameBuffer[bufferIdx] = c;
                depthBuffer[bufferIdx] = Float.MAX_VALUE;
                colorBuffer[bufferIdx] = message.color().pack();

                messageX++;
            }
            messageY++;
        }
    }

    private void print() {
        int bufferLength = width * height;
        outBuilder.setLength(0);
        for (int i = 0; i < bufferLength; i++) {

            if (i % width == 0) {
                outBuilder.append("\033[")
                        .append((i / width) + 1)
                        .append(";")
                        .append((i % width) + 1)
                        .append("H");
            }

            int color = colorBuffer[i];
            int prevColor = -1;
            if (i - 1 >= 0)
                prevColor = colorBuffer[i - 1];

            if (prevColor == -1 || color != prevColor) {
                outBuilder.append("\033[38;2;")
                        .append(Color.unpackR(color))
                        .append(";")
                        .append(Color.unpackG(color))
                        .append(";")
                        .append(Color.unpackB(color))
                        .append("m");
            }

            outBuilder.append(frameBuffer[i]);

        }

        System.out.print(outBuilder);// todo: minor improvement change from string builder to byte array and use a
        // system.out.write
        System.out.flush();
    }

    private float projectX(float x, float ooz) {
        return (width / 2.0f + (camera.fov * 2.2f) * x * ooz);
    }

    private float projectY(float y, float ooz) {
        return (height / 2.0f - camera.fov * y * ooz);
    }

    private float edgeFunction(float x0, float y0, float x1, float y1, float px, float py) {
        return (px - x0) * (y1 - y0) - (py - y0) * (x1 - x0);
    }
}
