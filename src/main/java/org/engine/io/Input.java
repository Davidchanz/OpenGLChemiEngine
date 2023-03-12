package org.engine.io;

import org.lwjgl.glfw.*;

public class Input {
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static double mouseX, mouseY;
	private static double scrollX, scrollY;
	
	private GLFWKeyCallback keyboard;
	private GLFWCursorPosCallback mouseMove;
	private GLFWMouseButtonCallback mouseButtons;
	private GLFWScrollCallback mouseScroll;
	private GLFWWindowFocusCallback windowFocus;
	private static int pressedButton;
	private static int lastPressedButton;
	private static int pressedKey;
	private static int lastPressedKey;
	private static boolean isWindowFocused;
	
	public Input() {
		pressedButton = -1;
		pressedKey = -1;
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(key != -1) {//TODO what? key unknown
					keys[key] = (action != GLFW.GLFW_RELEASE);
					pressedKey = key;
				}
			}
		};

		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double newMouseX, double newMouseY) {
				mouseX = newMouseX;
				mouseY = newMouseY;
			}
		};
		
		mouseButtons = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (action != GLFW.GLFW_RELEASE);
				pressedButton = button;
			}
		};
		
		mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetx, double offsety) {
				scrollX += offsetx;
				scrollY += offsety;
			}
		};

		windowFocus = new GLFWWindowFocusCallback() {
			@Override
			public void invoke(long l, boolean b) {
				GLFW.glfwSetInputMode(l, GLFW.GLFW_CURSOR, b ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
				isWindowFocused = b;
			}
		};
	}
	
	public static boolean isKeyDown(int key) {
		if(key < 0 || key >= keys.length)
			return false;
		return keys[key];
	}
	
	public static boolean isButtonDown(int button) {
		if(button < 0 || button >= buttons.length)
			return false;
		return buttons[button];
	}

	public static boolean isIsWindowFocused() {
		return isWindowFocused;
	}

	public void destroy() {
		keyboard.free();
		mouseMove.free();
		mouseButtons.free();
		mouseScroll.free();
	}

	public static double getMouseX() {
		return mouseX;
	}

	public static double getMouseY() {
		return mouseY;
	}
	
	public static double getScrollX() {
		return scrollX;
	}

	public static double getScrollY() {
		return scrollY;
	}

	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	public GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}
	
	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}

	public static int getButton() {
		lastPressedButton = pressedButton;
		pressedButton = -1;
		return lastPressedButton;
	}

	public static int getLastPressedButton() {
		lastPressedButton = pressedButton;
		pressedButton = -1;
		return lastPressedButton;
	}

	public static int getKey() {
		lastPressedKey = pressedKey;
		pressedKey = -1;
		return lastPressedKey;
	}

	public static int getLastPressedKey() {
		return lastPressedKey;
	}

	public GLFWWindowFocusCallback getWindowFocus() {
		return windowFocus;
	}
}