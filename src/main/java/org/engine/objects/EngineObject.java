package org.engine.objects;

import org.engine.Scene;
import org.engine.maths.Vector3f;
import org.engine.utils.Transformation;

public abstract class EngineObject implements Transform{
    protected int id;//id
    protected Vector3f rotation;//rotation
    protected Vector3f center;//center
    protected Vector3f position;//position
    private Scene scene;

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

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
