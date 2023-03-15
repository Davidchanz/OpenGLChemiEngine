package org.engine.utils;

import org.engine.maths.Vector3f;

public class Transformation {
    private Vector3f rotation;//rotation
    private Vector3f center;//center
    private Vector3f position;//position

    public Transformation(){}

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getCenter() {
        return center;
    }

    public Vector3f getPosition() {
        return position;
    }
}
