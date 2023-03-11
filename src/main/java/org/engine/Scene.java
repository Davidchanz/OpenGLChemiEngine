package org.engine;

import org.engine.events.GLMouseEvent;
import org.engine.graphics.Renderer;
import org.engine.graphics.Shader;
import org.engine.io.Input;
import org.engine.io.Window;
import org.engine.listeners.SceneMouseButtonPressedListener;
import org.engine.maths.Vector3f;
import org.engine.objects.Camera;
import org.engine.utils.Color;
import org.engine.objects.ShapeObject;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Scene implements Runnable{
    public static int WIDTH, HEIGHT;
    public Thread game;
    public Window window;
    public Renderer renderer;
    public Shader shader;
    public List<ShapeObject> gameObjects;
    public List<ShapeObject> addObjects;
    public List<ShapeObject> removeObjects;
    public List<SceneMouseButtonPressedListener> mouseButtonPressedListenersListeners;
    public Camera camera = new Camera(new Vector3f(0, 0, 1.712f), new Vector3f(0, 0, 0));
    private Color backgroundColor;

    public Scene(int w, int h, Color background){
        WIDTH = w;
        HEIGHT = h;
        this.backgroundColor = background;
        this.gameObjects = new ArrayList<>();
        this.addObjects = new ArrayList<>();
        this.removeObjects = new ArrayList<>();
        this.mouseButtonPressedListenersListeners = new ArrayList<>();
        this.start();
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
        this.addObjects.add(object);
    }

    public void addAll(List<ShapeObject> objects){
        objects.forEach(this::add);
    }

    public void remove(ShapeObject object){
        this.removeObjects.add(object);
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

    private synchronized void ifRemove() {
        while (!this.removeObjects.isEmpty()){
            this.gameObjects.remove(this.removeObjects.get(0));
            if(this.removeObjects.get(0) != null)this.removeObjects.get(0).destroy();
            this.removeObjects.remove(0);
        }
    }

    private synchronized void ifAdd() {
        while (!this.addObjects.isEmpty()){
            this.gameObjects.add(this.addObjects.get(0));
            if(this.addObjects.get(0) != null)this.addObjects.get(0).build();
            this.addObjects.remove(0);
        }
    }

    private synchronized void update() {
        window.update();
        //camera.update(/*this.gameObjects.get(0).body.get(0)*/);
    }

    private synchronized void render() {
        this.gameObjects.forEach(shapeObject -> {
            shapeObject.body.forEach(object -> {
                if(object.isResized())
                    object.build();
                renderer.renderMesh(object, camera);
            });
        });
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
