package org.engine.events;

import org.lwjgl.glfw.GLFW;

public class GLMouseEvent {
    public final int LEFT_BUTTON = GLFW.GLFW_MOUSE_BUTTON_LEFT;
    public final int RIGHT_BUTTON = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    public final int MIDDLE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
    public int button;
    private double x, y;

    public GLMouseEvent(int button, double x, double y) {
        this.button = button;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getButton() {
        return button;
    }
}
