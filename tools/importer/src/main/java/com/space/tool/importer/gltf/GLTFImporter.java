package com.space.tool.importer.gltf;

import com.space.engine.core.assets.data.components.MeshData;
import com.space.engine.core.exceptions.imports.GLTFException;

import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import de.javagl.jgltf.model.io.GltfModelReader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GLTFImporter {
    private final static GltfModelReader modelReader = new GltfModelReader();

    public static List<MeshData> load(Path path) throws IOException, GLTFException {
        GltfModel model = modelReader.read(path);

        List<MeshData> meshes = new ArrayList<>();
        for (MeshModel meshModel : model.getMeshModels()) {
            for (MeshPrimitiveModel primitive : meshModel.getMeshPrimitiveModels()) {
                meshes.add(readPrimitive(meshModel.getName(), primitive));
            }
        }

        return meshes;
    }

    private static MeshData readPrimitive(String name, MeshPrimitiveModel model) throws GLTFException {
        Map<String, AccessorModel> attributes = model.getAttributes();

        float[] vertices = readVertices(attributes);
        float[] normals = readNormals(attributes);
        float[] uvs = readUVs(attributes);
        int[] indices = readIndices(model, vertices);

        var builder = MeshData.builder()
                .name(name)
                .vertices(vertices)
                .indices(indices)
                .uvs(uvs);

        if (normals.length > 0) {
            builder.normals(normals);
        }

        return builder.build();
    }

    private static float[] readVertices(Map<String, AccessorModel> attributes) throws GLTFException {
        AccessorModel position = attributes.get("POSITION");

        if (position == null)
            throw new GLTFException("Primitive has no POSITION data");

        if (position.getElementType() != ElementType.VEC3)
            throw new GLTFException(
                    String.format("%s must be %s, got %s", "POSITION", ElementType.VEC3, position.getElementType()));

        return readFloats(position);
    }

    private static float[] readNormals(Map<String, AccessorModel> attributes) throws GLTFException {
        AccessorModel normal = attributes.get("NORMAL");

        if (normal == null)
            return new float[0];

        if (normal.getElementType() != ElementType.VEC3)
            throw new GLTFException(
                    String.format("%s must be %s, got %s", "NORMAL", ElementType.VEC3, normal.getElementType()));

        return readFloats(normal);
    }

    private static float[] readUVs(Map<String, AccessorModel> attributes) throws GLTFException {
        AccessorModel uv = attributes.get("TEXCOORD_0");

        if (uv == null)
            return new float[0];

        if (uv.getElementType() != ElementType.VEC2)
            throw new GLTFException(
                    String.format("%s must be %s, got %s", "TEXCOORD_0", ElementType.VEC2, uv.getElementType()));

        return readFloats(uv);
    }

    private static int[] readIndices(MeshPrimitiveModel primitive, float[] vertices) throws GLTFException {
        AccessorModel indexAccessor = primitive.getIndices();

        if (primitive.getMode() != GltfConstants.GL_TRIANGLES) {
            throw new GLTFException("Only TRIANGLES primitives are supported");
        }

        if (indexAccessor != null) {
            if (indexAccessor.getElementType() != ElementType.SCALAR)
                throw new GLTFException(
                        String.format("%s must be %s, got %s", "INDICES", ElementType.SCALAR,
                                indexAccessor.getElementType()));

            int type = indexAccessor.getComponentType();

            if (type != GltfConstants.GL_UNSIGNED_BYTE &&
                    type != GltfConstants.GL_UNSIGNED_SHORT &&
                    type != GltfConstants.GL_UNSIGNED_INT) {
                throw new GLTFException("Invalid index component type: " + type);
            }

            AccessorData data = AccessorDatas.create(indexAccessor);

            ByteBuffer buffer = data.createByteBuffer().order(ByteOrder.nativeOrder());

            int[] indices = new int[indexAccessor.getCount()];

            for (int i = 0; i < indices.length; i++) {
                indices[i] = switch (type) {
                    case GltfConstants.GL_UNSIGNED_BYTE -> Byte.toUnsignedInt(buffer.get());
                    case GltfConstants.GL_UNSIGNED_SHORT -> Short.toUnsignedInt(buffer.getShort());
                    case GltfConstants.GL_UNSIGNED_INT -> {
                        long v = Integer.toUnsignedLong(buffer.getInt());

                        if (v > Integer.MAX_VALUE) {
                            throw new GLTFException("Index exceeds Java int range");
                        }

                        yield (int) v;
                    }
                    default -> throw new AssertionError();
                };
            }

            return indices;

        } else {
            int   vertexCount = vertices.length / 3;
            int[] indices     = new int[vertexCount];

            for (int i = 0; i < vertexCount; i++) {
                indices[i] = i;
            }

            return indices;
        }
    }

    private static float[] readFloats(AccessorModel accessor) throws GLTFException {
        AccessorData data = AccessorDatas.create(accessor);

        ByteBuffer buffer = data.createByteBuffer().order(ByteOrder.nativeOrder());

        int components = accessor.getElementType().getNumComponents();

        int count = accessor.getCount();

        float[] result = new float[count * components];

        for (int i = 0; i < result.length; i++) {
            result[i] = readFloat(
                    buffer,
                    accessor.getComponentType(),
                    accessor.isNormalized());
        }

        return result;
    }

    private static float readFloat(ByteBuffer buffer, int type, boolean normalized) throws GLTFException {
        return switch (type) {
            case GltfConstants.GL_FLOAT -> buffer.getFloat();
            case GltfConstants.GL_BYTE -> {
                byte v = buffer.get();

                yield normalized ? Math.max(v / 127.0f, -1.0f) : v;
            }
            case GltfConstants.GL_UNSIGNED_BYTE -> {
                int v = Byte.toUnsignedInt(buffer.get());

                yield normalized ? v / 255.0f : v;
            }
            case GltfConstants.GL_SHORT -> {
                short v = buffer.getShort();

                yield normalized ? Math.max(v / 23767.0f, -1f) : v;
            }
            case GltfConstants.GL_UNSIGNED_SHORT -> {
                int v = Short.toUnsignedInt(buffer.getShort());

                yield normalized ? v / 65535.0f : v;
            }
            case GltfConstants.GL_INT -> {
                int v = buffer.getInt();

                yield normalized ? Math.max(v / 2147483647.0f, -1f) : v;
            }
            case GltfConstants.GL_UNSIGNED_INT -> {
                Long v = Integer.toUnsignedLong(buffer.getInt());

                yield normalized ? (float) (v / 4294967295.0) : v;
            }
            default -> throw new GLTFException("Unsupported componnet type: " + type);
        };
    }
}
