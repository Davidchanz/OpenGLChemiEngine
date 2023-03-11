package org.engine.objects;

import org.engine.maths.Vector3f;

public abstract class EngineObject {
    public int id;//id
    private Vector3f rotation;//rotation
    private Vector3f center;//center
    private Vector3f position;//position

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getCenter() {
        return center;
    }

    public void setCenter(Vector3f center) {
        this.center = center;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
}
