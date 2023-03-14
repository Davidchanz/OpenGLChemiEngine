package org.engine;

import org.engine.cameras.PerspectiveCamera;
import org.engine.events.GLKeyEvent;
import org.engine.events.GLMouseEvent;
import org.engine.graphics.Renderer;
import org.engine.graphics.Shader;
import org.engine.io.Input;
import org.engine.io.Window;
import org.engine.listeners.CloseListener;
import org.engine.listeners.KeyPressedListener;
import org.engine.listeners.MouseButtonPressedListener;
import org.engine.maths.Vector3f;
import org.engine.cameras.Camera;
import org.engine.utils.Color;
import org.engine.objects.ShapeObject;
import org.lwjgl.glfw.GLFW;

import java.util.*;
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
    private final List<MouseButtonPressedListener> mouseButtonPressedListeners;
    private final List<CloseListener> closeListeners;
    private final List<KeyPressedListener> keyPressedListeners;
    private Camera camera = new PerspectiveCamera(new Vector3f(0, 0, 1.712f), new Vector3f(0, 0, 0));
    private final Color backgroundColor;
    private final AtomicBoolean busy = new AtomicBoolean(false);
    public static boolean is3DCameraEnable;
    public static AtomicBoolean isReady = new AtomicBoolean(false);

    public Scene(int w, int h, Color background){
        WIDTH = w;
        HEIGHT = h;

        is3DCameraEnable = false;
        this.backgroundColor = background;
        this.gameObjects = new ArrayList<>();
        this.addObjects = Collections.synchronizedList(new ArrayList<>());
        this.removeObjects = Collections.synchronizedList(new ArrayList<>());
        this.removeBuffer = Collections.synchronizedList(new ArrayList<>());
        this.addBuffer = Collections.synchronizedList(new ArrayList<>());

        this.mouseButtonPressedListeners = new ArrayList<>();
        this.closeListeners = new ArrayList<>();
        this.keyPressedListeners = new ArrayList<>();
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

    @Override
    public void run() {
        init();
        ifAdd();
        ifRemove();
        update();
        render();
        eventListeners();
        isReady.set(true);
        while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            ifAdd();
            ifRemove();
            update();
            render();
            eventListeners();
        }
        close();
        this.closeListeners.forEach(CloseListener::listen);
    }

    private void eventListeners() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
        if(Input.isButtonDown(Input.getButton())) {
            var tmp = new Vector3f((float) Input.getMouseX(), (float) Input.getMouseY(), 0);
            var mousePos = toSceneDimension(tmp);
            var button = Input.getLastPressedButton();
            this.mouseButtonPressedListeners.forEach(mouseListener -> {
                mouseListener.listen(new GLMouseEvent(button, mousePos.getX(), mousePos.getY()));
            });
        }
        if(Input.isKeyDown(Input.getKey())){
            var key = Input.getLastPressedKey();
            this.keyPressedListeners.forEach(keyPressedListener -> {
                keyPressedListener.listen(new GLKeyEvent(key));
            });
        }
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
        if(is3DCameraEnable){
            if(Input.isIsWindowFocused())
                camera.update();
        }
        else
            camera.update();
        //if(Input.isIsWindowFocused() && is3DCameraEnable)camera.update();//TODO
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

    public void addMouseEventListener(MouseButtonPressedListener listener){
        this.mouseButtonPressedListeners.add(listener);
    }

    public void addCloseListener(CloseListener listener){
        this.closeListeners.add(listener);
    }

    public void addKeyPressedListener(KeyPressedListener listener){
        this.keyPressedListeners.add(listener);
    }

    public synchronized void setCamera(Camera camera){
        this.camera = camera;
        is3DCameraEnable = this.camera.isIs3DEnabled();
    }

    public boolean isReady() {
        return isReady.get();
    }
}
