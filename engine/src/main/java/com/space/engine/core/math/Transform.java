package com.space.engine.core.math;

public class Transform {
    public Vector3 position;
    public Vector3 rotation;
    public Vector3 scale;

    public Transform() {
        position = new Vector3();
        rotation = new Vector3();
        scale    = new Vector3(1, 1, 1);
    }

    public void set(Transform other) {
        position.set(other.position);
        rotation.set(other.rotation);
        scale.set(other.scale);
    }

    public void clear() {
        position.setZero();
        rotation.setZero();
        scale.set(1, 1, 1);
    }

    public void add(Transform other) {
        position.add(other.position);
        rotation.add(other.rotation);
        scale.add(other.scale);
    }

    public void sub(Transform other) {
        position.sub(other.position);
        rotation.sub(other.rotation);
        scale.sub(other.scale);
    }
}
