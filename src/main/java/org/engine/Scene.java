package org.engine;

import org.engine.events.GLMouseEvent;
import org.engine.graphics.Renderer;
import org.engine.graphics.Shader;
import org.engine.io.Input;
import org.engine.io.Window;
import org.engine.listeners.SceneCloseListener;
import org.engine.listeners.SceneMouseButtonPressedListener;
import org.engine.maths.Vector3f;
import org.engine.objects.Camera;
import org.engine.utils.Color;
import org.engine.objects.ShapeObject;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scene implements Runnable{
    public static int WIDTH, HEIGHT;
    private Thread game;
    private Window window;
    private Renderer renderer;
    private Shader shader;
    private final List<ShapeObject> gameObjects;
    private final List<ShapeObject> addObjects;
    private final List<ShapeObject> removeObjects;
    private final List<ShapeObject> removeBuffer;
    private final List<ShapeObject> addBuffer;
    private final List<SceneMouseButtonPressedListener> sceneMouseButtonPressedListeners;
    private final List<SceneCloseListener> sceneCloseListeners;
    private Camera camera = new Camera(new Vector3f(0, 0, 1.712f), new Vector3f(0, 0, 0));
    private final Color backgroundColor;
    private final AtomicBoolean busy = new AtomicBoolean(false);

    public Scene(int w, int h, Color background){
        WIDTH = w;
        HEIGHT = h;
        this.backgroundColor = background;
        this.gameObjects = new ArrayList<>();
        this.addObjects = Collections.synchronizedList(new ArrayList<>());
        this.removeObjects = Collections.synchronizedList(new ArrayList<>());
        this.removeBuffer = Collections.synchronizedList(new ArrayList<>());
        this.addBuffer = Collections.synchronizedList(new ArrayList<>());

        this.sceneMouseButtonPressedListeners = new ArrayList<>();
        this.sceneCloseListeners = new ArrayList<>();
    }

    public void start() {
        game = new Thread(this, "game");
        game.start();
    }

    public void init() {
        this.window = new Window(WIDTH, HEIGHT, "Game");
        this.shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragmentTexture.glsl");
        this.renderer = new Renderer(window, shader);
        this.window.setBackgroundColor(this.backgroundColor);
        this.window.create();
        this.shader.create();
    }

    public void add(ShapeObject object){
        if(!this.busy.get()) {
            this.busy.set(true);
            this.addObjects.addAll(this.addBuffer);
            this.addBuffer.clear();
            this.addObjects.add(object);
            this.busy.set(false);
        }else {
            this.addBuffer.add(object);
        }
    }

    public void addAll(List<ShapeObject> objects){
        objects.forEach(this::add);
    }

    public void remove(ShapeObject object){
        if(!this.busy.get()) {
            this.busy.set(true);
            this.removeObjects.addAll(this.removeBuffer);
            this.removeBuffer.clear();
            this.removeObjects.add(object);
            this.busy.set(false);
        }else {
            this.removeBuffer.add(object);
        }
    }

    public void run() {
        init();
        while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            ifAdd();
            ifRemove();
            update();
            render();
            if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
            this.sceneMouseButtonPressedListeners.forEach(sceneMouseListener -> {
                var tmp = new Vector3f((float) Input.getMouseX(), (float) Input.getMouseY(), 0);
                var mousePos = toSceneDimension(tmp);
                if(Input.isButtonDown(Input.getButton())) {
                    sceneMouseListener.listen(new GLMouseEvent(Input.getLastPressedButton(), mousePos.getX(), mousePos.getY()));
                }
            });
        }
        close();
        this.sceneCloseListeners.forEach(SceneCloseListener::listen);
    }

    private void ifRemove() {
        if(this.busy.get())
            return;
        this.busy.set(true);
        this.removeObjects.addAll(this.removeBuffer);
        this.removeBuffer.clear();
        this.gameObjects.removeAll(this.removeObjects);
        this.removeObjects.forEach(ShapeObject::destroy);
        this.removeObjects.clear();
        this.busy.set(false);
    }

    private void ifAdd() {
        if(this.busy.get())
            return;
        this.busy.set(true);
        this.gameObjects.addAll(this.addObjects);
        this.addObjects.clear();
        this.busy.set(false);
    }

    private synchronized void update() {
        window.update();
        //camera.update(/*this.gameObjects.get(0).body.get(0)*/);
    }

    private synchronized void render() {
        this.busy.set(true);
            this.gameObjects.forEach(shapeObject -> {
                    shapeObject.body.forEach(object -> {
                        if (object.isChanged())
                            object.build();
                        renderer.renderMesh(object, camera);
                    });
            });
        this.busy.set(false);
        window.swapBuffers();
    }

    private void close() {
        window.destroy();
        this.gameObjects.forEach(shapeObject -> {
            shapeObject.body.forEach(object -> {
                object.getMesh().destroy();
            });
        });
        shader.destroy();
    }

    public boolean isOutSceneBorder(Vector3f position){
        var screenPos = toGLDimension(position);
        if(screenPos.getX() <= -WIDTH/2f || screenPos.getX() >= WIDTH/2f || screenPos.getY() <= -HEIGHT/2f || screenPos.getY() >= HEIGHT/2f)
            return true;
        else
            return false;
    }

    public static Vector3f toGLDimension(Vector3f vec){
        return new Vector3f(vec.getX() * WIDTH, vec.getY() * HEIGHT, vec.getZ());
    }

    public static Vector3f toScreenDimension(Vector3f point){
        return new Vector3f(point.getX() + WIDTH/2f, -(point.getY() - HEIGHT/2f), point.getZ());
    }

    public static Vector3f toSceneDimension(Vector3f point){
        return new Vector3f(point.getX() - WIDTH/2f, HEIGHT/2f - point.getY(), point.getZ());
    }

    public void addMouseEventListener(SceneMouseButtonPressedListener listener){
        this.sceneMouseButtonPressedListeners.add(listener);
    }

    public void addSceneCloseListener(SceneCloseListener listener){
        this.sceneCloseListeners.add(listener);
    }
}
