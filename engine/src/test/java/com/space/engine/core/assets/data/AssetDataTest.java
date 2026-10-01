package com.space.engine.core.assets.data;

import com.space.engine.core.math.Transform;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AssetDataTest {

    @Test
    void testCameraDataBuilderAndInheritance() {
        UUID uid = UUID.randomUUID();
        Transform transform = new Transform();
        CameraData cameraData = CameraData.builder()
                .uid(uid)
                .name("MainCamera")
                .transform(transform)
                .near(0.1f)
                .far(100.0f)
                .fov(60.0f)
                .build();

        assertEquals(uid, cameraData.getUid());
        assertEquals("MainCamera", cameraData.getName());
        assertEquals(transform, cameraData.getTransform());
        assertEquals(0.1f, cameraData.getNear());
        assertEquals(100.0f, cameraData.getFar());
        assertEquals(60.0f, cameraData.getFov());
        assertEquals(1, cameraData.getVersion());
    }

    @Test
    void testFreeCameraDataBuilderAndInheritance() {
        UUID uid = UUID.randomUUID();
        Transform transform = new Transform();
        FreeCameraData freeCameraData = FreeCameraData.builder()
                .uid(uid)
                .name("FreeCam")
                .transform(transform)
                .near(0.5f)
                .far(500.0f)
                .fov(75.0f)
                .walkSpeed(12.0f)
                .lookSpeed(3.5f)
                .build();

        assertEquals(uid, freeCameraData.getUid());
        assertEquals("FreeCam", freeCameraData.getName());
        assertEquals(transform, freeCameraData.getTransform());
        assertEquals(0.5f, freeCameraData.getNear());
        assertEquals(500.0f, freeCameraData.getFar());
        assertEquals(75.0f, freeCameraData.getFov());
        assertEquals(12.0f, freeCameraData.getWalkSpeed());
        assertEquals(3.5f, freeCameraData.getLookSpeed());
    }

    @Test
    void testWorldDataBuilder() {
        UUID uid = UUID.randomUUID();
        WorldData worldData = WorldData.builder()
                .uid(uid)
                .name("TestWorld")
                .build();

        assertEquals(uid, worldData.getUid());
        assertEquals("TestWorld", worldData.getName());
    }
}
