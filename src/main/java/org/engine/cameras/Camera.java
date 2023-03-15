package org.engine.cameras;

import org.engine.Scene;
import org.engine.maths.Vector3f;

public abstract class Camera implements CameraAction {
	protected Vector3f position, rotation;
	protected float moveSpeed = 0.05f, mouseSensitivity = 0.05f, distance = 2.0f, horizontalAngle = 0, verticalAngle = 0;
	protected double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;
	private Scene scene;
	protected boolean is3DEnabled;
	protected static final Vector3f cameraCenter = new Vector3f(0f,0f,1.712f);

	public Camera(Vector3f position, Vector3f rotation) {
		this.position = new Vector3f(position);
		this.rotation = new Vector3f(rotation);
		this.is3DEnabled = false;
	}

	public Camera(){
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.resetCamera();
		this.is3DEnabled = false;
	}

	public void setScene(Scene scene){
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public boolean isIs3DEnabled() {
		return is3DEnabled;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public float getMouseSensitivity() {
		return mouseSensitivity;
	}

	public void setMouseSensitivity(float mouseSensitivity) {
		this.mouseSensitivity = mouseSensitivity;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public void resetCamera() {
		this.setPosition(cameraCenter);
		this.setRotation(new Vector3f(0,0,0));
	}
}