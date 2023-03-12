package org.engine;

import org.engine.events.GLMouseEvent;
import org.engine.graphics.Renderer;
import org.engine.graphics.Shader;
import org.engine.io.Input;
import org.engine.io.Window;
import org.engine.listeners.SceneMouseButtonPressedListener;
import org.engine.maths.Vector3f;
import org.engine.objects.Camera;
import org.engine.objects.GameObject;
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
    private final List<SceneMouseButtonPressedListener> mouseButtonPressedListenersListeners;


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
        this.mouseButtonPressedListenersListeners = new ArrayList<>();
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

        //Rectangle s1 = new Rectangle(new Vector3f(0,0,0), new Vector3f(0,0,0));
        //Rectangle s2 = new Rectangle(new Vector3f(1,0,0), new Vector3f(0,0,0));
        //Rectangle s3 = new Rectangle(new Vector3f(0,1,0), new Vector3f(0,0,0));

        //ob.add(s1);
        //ob.add(s2);
        //ob.add(s3);
        //rubic = new Rubic(1, 0,0,0);
        //.addAll(rubic.get());
        //this.addAll( new Rubic(1, 0,0,0).get());
       /* ShapeObject rect = new ShapeObject();
        rect.add(new Rectangle(100, new Vector3f(0,0,0), Color.CYAN));
        rect.add(new Rectangle(50, new Vector3f(200,0,0), Color.RED));
        this.add(rect);*/
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
            this.mouseButtonPressedListenersListeners.forEach(sceneMouseListener -> {
                var tmp = new Vector3f((float) Input.getMouseX(), (float) Input.getMouseY(), 0);
                var mousePos = toSceneDimension(tmp);
                if(Input.isButtonDown(Input.getButton()))sceneMouseListener.listen(new GLMouseEvent(Input.getButton(), mousePos.getX(), mousePos.getY()));
            });
        }
        close();
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
        /*while (!this.removeObjects.isEmpty()){
            if(this.removeObjects.get(0) != null) {
                this.gameObjects.remove(this.removeObjects.get(0));
                this.removeObjects.get(0).destroy();
                this.removeObjects.remove(0);
            }
        }*/
    }

    private void ifAdd() {
        if(this.busy.get())
            return;
        this.busy.set(true);
        this.gameObjects.addAll(this.addObjects);
        this.addObjects.clear();
        this.busy.set(false);
        /*while (!this.addObjects.isEmpty()){
            this.gameObjects.add(this.addObjects.get(0));
            //if(this.addObjects.get(0) != null)this.addObjects.get(0).build();
            this.addObjects.remove(0);
        }*/
    }

    private synchronized void update() {
        window.update();
        //camera.update(/*this.gameObjects.get(0).body.get(0)*/);
    }

    private synchronized void render() {
        /*synchronized (this.gameObjects) {
            Arrays.stream(this.gameObjects.toArray(new ShapeObject[0])).toList().forEach(shapeObject -> {
                //synchronized (shapeObject) {
                //shapeObject.addAll(shapeObject.newObjects);
                //shapeObject.newObjects.clear();
                // }
                if(shapeObject == null)
                    return;
                synchronized (shapeObject) {
                    Arrays.stream(shapeObject.body.toArray(new GameObject[0])).toList().forEach(object -> {
                        if (object.isChanged())
                            object.build();
                        renderer.renderMesh(object, camera);
                    });
                }
            });
        }*/
        this.busy.set(true);
            this.gameObjects.forEach(shapeObject -> {
                //synchronized (shapeObject) {
                //shapeObject.addAll(shapeObject.newObjects);
                //shapeObject.newObjects.clear();
                // }
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
        /*point.x = point.x + WIDTH/2f;
        point.y = -(point.y - HEIGHT/2f);*/
    }

    public static Vector3f toSceneDimension(Vector3f point){
        return new Vector3f(point.getX() - WIDTH/2f, HEIGHT/2f - point.getY(), point.getZ());
        /*point.x = point.x - WIDTH/2f;
        point.y = HEIGHT/2f - point.y;*/
    }

    public void addMouseEventListener(SceneMouseButtonPressedListener listener){
        this.mouseButtonPressedListenersListeners.add(listener);
    }
}
