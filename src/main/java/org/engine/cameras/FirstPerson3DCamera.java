package org.engine.cameras;

import org.engine.io.Input;
import org.engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

public class FirstPerson3DCamera extends Camera {
    private int right;
    private int left;
    private int up;
    private int down;
    private int forward;
    private int backward;

    public FirstPerson3DCamera(Vector3f position, Vector3f rotation) {
        super(position, rotation);
        this.left = GLFW.GLFW_KEY_A;
        this.right = GLFW.GLFW_KEY_D;
        this.forward = GLFW.GLFW_KEY_W;
        this.backward = GLFW.GLFW_KEY_S;
        this.up = GLFW.GLFW_KEY_SPACE;
        this.down = GLFW.GLFW_KEY_LEFT_SHIFT;
        this.is3DEnabled = true;
    }

    @Override
    public void update() {
        this.newMouseX = Input.getMouseX();
        this.newMouseY = Input.getMouseY();

        float x = (float) Math.sin(Math.toRadians(this.rotation.getY())) * this.moveSpeed;
        float z = (float) Math.cos(Math.toRadians(this.rotation.getY())) * this.moveSpeed;

        if (Input.isKeyDown(this.left)) this.position = Vector3f.add(this.position, new Vector3f(-z, 0, x));
        if (Input.isKeyDown(this.right)) this.position = Vector3f.add(this.position, new Vector3f(z, 0, -x));
        if (Input.isKeyDown(this.forward)) this.position = Vector3f.add(this.position, new Vector3f(-x, 0, -z));
        if (Input.isKeyDown(this.backward)) this.position = Vector3f.add(this.position, new Vector3f(x, 0, z));
        if (Input.isKeyDown(this.up)) this.position = Vector3f.add(this.position, new Vector3f(0, this.moveSpeed, 0));
        if (Input.isKeyDown(this.down)) this.position = Vector3f.add(this.position, new Vector3f(0, -this.moveSpeed, 0));

        float dx = (float) (this.newMouseX - this.oldMouseX);
        float dy = (float) (this.newMouseY - this.oldMouseY);

        this.rotation = Vector3f.add(this.rotation, new Vector3f(-dy * this.mouseSensitivity, -dx * this.mouseSensitivity, 0));

        this.oldMouseX = this.newMouseX;
        this.oldMouseY = this.newMouseY;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getForward() {
        return forward;
    }

    public void setForward(int forward) {
        this.forward = forward;
    }

    public int getBackward() {
        return backward;
    }

    public void setBackward(int backward) {
        this.backward = backward;
    }
}
