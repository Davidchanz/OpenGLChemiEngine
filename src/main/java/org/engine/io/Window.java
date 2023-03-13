package org.engine.io;

import org.engine.utils.Color;
import org.engine.maths.Matrix4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL33;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
	private int width, height;
	private String title;
	private long window;
	private int frames;
	private static long time;
	private Input input;
	private Color background = Color.TRANSPARENT;
	private GLFWWindowSizeCallback sizeCallback;
	private boolean isResized;
	private boolean isFullscreen = false;
	private int[] windowPosX = new int[1], windowPosY = new int[1];
	private Matrix4f projection;
	
	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
		projection = Matrix4f.projection(70.0f, (float) width / (float) height, 0.1f, 1000.0f);
	}
	
	public void create() {
		if (!GLFW.glfwInit()) {
			System.err.println("ERROR: GLFW wasn't initializied");
			return;
		}
		
		input = new Input();
		window = GLFW.glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
		
		if (window == 0) {
			System.err.println("ERROR: Window wasn't created");
			return;
		}
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		windowPosX[0] = (videoMode.width() - width) / 2;
		windowPosY[0] = (videoMode.height() - height) / 2;
		GLFW.glfwSetWindowPos(window, windowPosX[0], windowPosY[0]);
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		createCallbacks();

		//GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glEnable(GL11.GL_BLEND);
		// Enables Cull Facing
		GL11.glEnable(GL11.GL_CULL_FACE);
		// Keeps front faces
		//GL11.glCullFace(GL11.GL_FRONT);
		// Uses counter clock-wise standard
		GL11.glFrontFace(GL11.GL_CCW);
		// Configures the blending function
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		/*GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);*/
		
		GLFW.glfwShowWindow(window);
		
		GLFW.glfwSwapInterval(1);
		
		time = System.currentTimeMillis();
	}
	
	private void createCallbacks() {
		sizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long window, int w, int h) {
				width = w;
				height = h;
				isResized = true;
			}
		};
		
		GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
		GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
		GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
		GLFW.glfwSetScrollCallback(window, input.getMouseScrollCallback());
		GLFW.glfwSetWindowSizeCallback(window, sizeCallback);
		GLFW.glfwSetWindowFocusCallback(window, input.getWindowFocus());
	}
	
	public void update() {
		if (isResized) {
			GL11.glViewport(0, 0, width, height);
			isResized = false;
		}
		//GL11.glClearColor(0.07f, 0.13f, 0.17f, 1.0f);
		GL11.glClearColor(background.getRed(), background.getGreen(), background.getBlue(), background.getAlpha());
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		GLFW.glfwPollEvents();
		frames++;
		if (System.currentTimeMillis() > time + 1000) {
			GLFW.glfwSetWindowTitle(window, title + " | FPS: " + frames);
			time = System.currentTimeMillis();
			frames = 0;
		}
	}
	
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public void destroy() {
		input.destroy();
		sizeCallback.free();
		GLFW.glfwWindowShouldClose(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}
	
	public void setBackgroundColor(Color color) {
		this.background = color;
	}

	public boolean isFullscreen() {
		return isFullscreen;
	}

	public void setFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;
		isResized = true;
		if (isFullscreen) {
			GLFW.glfwGetWindowPos(window, windowPosX, windowPosY);
			GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
		} else {
			GLFW.glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
		}
	}
	
	public void mouseState(boolean lock) {
		glfwSetInputMode(window, GLFW.GLFW_CURSOR, lock ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	public long getWindow() {
		return window;
	}

	public Matrix4f getProjectionMatrix() {
		return projection;
	}
}