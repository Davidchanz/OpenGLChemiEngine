package org.engine.events;

import org.engine.io.Input;
import org.lwjgl.glfw.GLFW;

public class GLMouseEvent {
    public static final int LEFT_BUTTON = GLFW.GLFW_MOUSE_BUTTON_LEFT;
    public static final int RIGHT_BUTTON = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    public static final int MIDDLE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
    private final int button;
    private final double x;
    private final double y;

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

    public boolean isLeftButtonDown(){
        return Input.isButtonDown(LEFT_BUTTON);
    }
    public boolean isMiddleButtonDown(){
        return Input.isButtonDown(MIDDLE_BUTTON);
    }
    public boolean isRightButtonDown(){
        return Input.isButtonDown(RIGHT_BUTTON);
    }

    public boolean isButtonDown(int button){
        return Input.isButtonDown(button);
    }
}
