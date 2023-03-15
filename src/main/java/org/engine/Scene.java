package org.engine;

import org.engine.cameras.PerspectiveCamera;
import org.engine.cameras.StaticCamera;
import org.engine.events.GLKeyEvent;
import org.engine.events.GLMouseEvent;
import org.engine.graphics.Renderer;
import org.engine.graphics.Shader;
import org.engine.io.Input;
import org.engine.io.Window;
import org.engine.listeners.CloseListener;
import org.engine.listeners.KeyPressedListener;
import org.engine.listeners.MouseButtonPressedListener;
import org.engine.listeners.MouseDraggedListener;
import org.engine.maths.Matrix4f;
import org.engine.maths.Vector3f;
import org.engine.cameras.Camera;
import org.engine.objects.EngineObject;
import org.engine.objects.GameObject;
import org.engine.utils.BorderType;
import org.engine.utils.Color;
import org.engine.objects.ShapeObject;
import org.engine.utils.Transformation;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.test.spaceinvaders.Game;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scene implements Runnable{
    public static int WIDTH, HEIGHT;
    private Thread game;
    private Window window;
    private Renderer renderer;
    private Shader shader;
    private final List<ShapeObject> gameObjects;
    private final List<ShapeObject> invisibleGameObjects;
    private final List<ShapeObject> addObjects;
    private final List<ShapeObject> removeObjects;
    private final List<ShapeObject> removeBuffer;
    private final List<ShapeObject> addBuffer;
    private final List<MouseButtonPressedListener> mouseButtonPressedListeners;
    private final List<CloseListener> closeListeners;
    private final List<KeyPressedListener> keyPressedListeners;
    private final List<MouseDraggedListener> mouseDraggedListeners;
    private final List<ShapeObject> visibleObjectsBuffer;
    private final List<ShapeObject> invisibleObjectsBuffer;
    private Camera camera;
    private final Color backgroundColor;
    private final AtomicBoolean busy = new AtomicBoolean(false);
    public static boolean is3DCameraEnable;
    public static AtomicBoolean isReady = new AtomicBoolean(false);
    private int windowW, windowH;
    private TreeMap<Integer, GameObject[][]> objectFields;
    private List<ShapeObject> objectBuffer;
    private AtomicBoolean isObjectVisibleChanged = new AtomicBoolean(false);

    public Scene(int w, int h, int windowW, int windowH, Color background){
        WIDTH = w;
        HEIGHT = h;
        this.windowW = windowW;
        this.windowH = windowH;

        this.setCamera(new StaticCamera());
        this.backgroundColor = background;
        this.gameObjects = new ArrayList<>();
        this.addObjects = Collections.synchronizedList(new ArrayList<>());
        this.removeObjects = Collections.synchronizedList(new ArrayList<>());
        this.removeBuffer = Collections.synchronizedList(new ArrayList<>());
        this.addBuffer = Collections.synchronizedList(new ArrayList<>());

        this.mouseButtonPressedListeners = new ArrayList<>();
        this.closeListeners = new ArrayList<>();
        this.keyPressedListeners = new ArrayList<>();
        this.mouseDraggedListeners = new ArrayList<>();

        this.objectFields = new TreeMap<>();
        this.objectBuffer = Collections.synchronizedList(new ArrayList<>());

        this.invisibleGameObjects = new ArrayList<>();
        this.visibleObjectsBuffer = Collections.synchronizedList(new ArrayList<>());
        this.invisibleObjectsBuffer = Collections.synchronizedList(new ArrayList<>());
    }

    public void start() {
        game = new Thread(this, "game");
        game.start();
    }

    public void init() {
        this.window = new Window(this.windowW, this.windowH, "Game");
        this.shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragmentTexture.glsl");
        this.renderer = new Renderer(window, shader);
        this.window.setBackgroundColor(this.backgroundColor);
        this.window.create();
        this.shader.create();
    }

    public void add(ShapeObject object){
        if(!this.busy.get()) {
            this.busy.set(true);
            object.setScene(this);
            this.addObjects.addAll(this.addBuffer);
            this.addBuffer.clear();
            this.addObjects.add(object);
            this.busy.set(false);
        }else {
            this.addBuffer.add(object);
        }
    }

    public void addAll(List<? extends ShapeObject> objects){
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
        this.init();
        this.draw();
        isReady.set(true);
        while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            this.draw();
        }
        this.close();
        this.closeListeners.forEach(CloseListener::listen);
    }

    private void draw(){
        this.ifAdd();
        this.ifRemove();
        this.update();
        this.updateVisibility();
        this.updateInvisibleObjects();
        this.render();
        this.eventListeners();
    }

    private void updateInvisibleObjects() {
        this.busy.set(true);

        new Thread(()->{
            this.invisibleGameObjects.parallelStream().forEach(this::ifUpdateShapeObject);
        }).start();

        this.busy.set(false);
    }

    private void updateVisibility() {
        if(this.isObjectVisibleChanged.get()){
            this.busy.set(true);

            this.gameObjects.removeAll(this.invisibleObjectsBuffer);
            //this.invisibleGameObjects.addAll(this.invisibleObjectsBuffer);
            this.invisibleObjectsBuffer.clear();

            //this.invisibleGameObjects.removeAll(this.visibleObjectsBuffer);
            this.gameObjects.addAll(this.visibleObjectsBuffer);
            this.visibleObjectsBuffer.clear();

            this.isObjectVisibleChanged.set(false);
            this.busy.set(false);
        }
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
        if(Input.isButtonDown(Input.getDraggedButton())) {
            var x = Input.getLastMouseX();
            var y = Input.getLastMouseY();
            /*System.out.println("X: "+Input.getMouseX());
            System.out.println("LastX: "+Input.getLastMouseX());*/
            if(x != Input.getMouseX() || y != Input.getMouseY()) {
                var tmp = new Vector3f((float) Input.getMouseX(), (float) Input.getMouseY(), 0);
                var mousePos = toSceneDimension(tmp);
                var button = Input.getDraggedButton();
                this.mouseDraggedListeners.forEach(mouseListener -> {
                    mouseListener.listen(new GLMouseEvent(button, mousePos.getX(), mousePos.getY()));
                });
            }
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

        this.removeObjects.forEach(object -> {
            if(object.isVisible())
                this.gameObjects.remove(object);
            else
                this.invisibleGameObjects.remove(object);
        });
        this.removeObjects.forEach(this::removeShapeFromBuffer);
        this.removeObjects.forEach(ShapeObject::destroy);
        this.removeObjects.clear();
        this.busy.set(false);
    }

    private void ifAdd() {
        if(this.busy.get())
            return;
        this.busy.set(true);
        this.addObjects.addAll(this.addBuffer);
        this.addBuffer.clear();
        this.addObjects.forEach(object -> {
            if(object.isVisible())
                this.gameObjects.add(object);
            else
                this.invisibleGameObjects.add(object);
        });
        this.addObjects.forEach(this::addShapeObjectInBufferNow);
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
                this.ifUpdateShapeObject(shapeObject);
                shapeObject.body.forEach(object -> {
                    if(object.isChanged())
                        object.build();
                    renderer.renderMesh(object, camera);
                });
            });
        this.busy.set(false);
        window.swapBuffers();
    }

    private void ifUpdateShapeObject(ShapeObject shapeObject){
        if(shapeObject.isRemove()){
            this.remove(shapeObject);
            return;
        }
        if(shapeObject.isAddObject()) {
            shapeObject.addAllIf();
        }
        if(shapeObject.isRemoveObject()) {
            shapeObject.removeAllIf();
        }
    }

    public void updateObjectBuffer(int... ids){
        if(this.busy.get())
            return;
        this.busy.set(true);
        for(int i = 0; i < ids.length; i++) {
            this.clearObjectsBuffer(i);
            this.addShapeObjectInBufferPermanent(i, Arrays.stream(this.gameObjects.toArray(new ShapeObject[0])).toList());
        }
        this.busy.set(false);
    }

    public void removeGameObjectFromBuffer(GameObject object){
        if(this.isOutSceneBorder(object.getCenter()) != BorderType.NO_BORDER)
            return;
        var sceneCoord = new Vector3f(object.getCenter());
        sceneCoord = Scene.toGLDimension(sceneCoord);
        sceneCoord = Scene.toScreenDimension(sceneCoord);
        if(this.objectFields.get(object.getParent().getId()) != null) {
            this.objectFields.get(object.getId())[(int) sceneCoord.getX()][(int) sceneCoord.getY()] = null;
        }
    }

    private void removeShapeFromBuffer(ShapeObject object){
        if(this.isOutSceneBorder(object.getCenter()) != BorderType.NO_BORDER)
            return;
        var sceneCoord = new Vector3f(object.getCenter());
        sceneCoord = Scene.toGLDimension(sceneCoord);
        sceneCoord = Scene.toScreenDimension(sceneCoord);
        if(this.objectFields.get(object.getId()) != null) {
            this.objectFields.get(object.getId())[(int) sceneCoord.getX()][(int) sceneCoord.getY()] = null;
        }
    }

    private void clearObjectsBuffer(int id) {
        if(this.objectFields.get(id) != null)
            for (var i : this.objectFields.get(id)) {
                Arrays.fill(i, null);
            }
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

    /**
     * return 0 if no In Scne border
     * return 1 if outOfBorder right
     * return 2 if outOfBorder up
     * return 3 if outOfBorder left
     * return 4 if outOfBorder down
     * */
    public BorderType isOutSceneBorder(Vector3f position){
        var screenPos = toGLDimension(position);
        if(screenPos.getX() <= -WIDTH/2f)//left
            return BorderType.BORDER_LEFT;
        else if(screenPos.getX() >= WIDTH/2f)//right
            return BorderType.BORDER_RIGHT;
        else if(screenPos.getY() <= -HEIGHT/2f)//down
            return BorderType.BORDER_DOWN;
        else if(screenPos.getY() >= HEIGHT/2f)//up
            return BorderType.BORDER_UP;
        return BorderType.NO_BORDER;
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

    public void addMouseButtonPressedListener(MouseButtonPressedListener listener){
        this.mouseButtonPressedListeners.add(listener);
    }

    public void addCloseListener(CloseListener listener){
        this.closeListeners.add(listener);
    }

    public void addKeyPressedListener(KeyPressedListener listener){
        this.keyPressedListeners.add(listener);
    }
    public void addMouseDraggedListener(MouseDraggedListener listener){
        this.mouseDraggedListeners.add(listener);
    }

    public synchronized void setCamera(Camera camera){
        this.camera = camera;
        is3DCameraEnable = this.camera.isIs3DEnabled();
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean isReady() {
        return isReady.get();
    }

    public void resetCamera(){
        this.camera.resetCamera();
    }

    private void addShapeObjectInBufferPermanent(int id, Collection<ShapeObject> objects){
        objects.forEach(object -> {
            if(object != null && object.getId() == id)
                this.addShapeObjectInBufferNow(object);
        });
    }

    public void addGameObjectInBufferPermanent(Collection<GameObject> objects){
        objects.forEach(object -> {
            if(object != null)
                this.addGameObjectInBufferNow(object);
        });
    }

    private void addGameObjectInBufferNow(GameObject gameObject){
        if(/*gameObject.getParent() != null && */!gameObject.getParent().isBuffered())
            return;
        var sceneCoord = new Vector3f(gameObject.getCenter());
        sceneCoord = Scene.toGLDimension(sceneCoord);
        sceneCoord = Scene.toScreenDimension(sceneCoord);
        var parent = gameObject.getParent();
        for (int x = (int) sceneCoord.getX() - (int) parent.getSpriteSize().x; x <= (int) sceneCoord.getX() + (int) parent.getSpriteSize().x; x++) {
            for (int y = (int) sceneCoord.getY() - (int) parent.getSpriteSize().y; y <= (int) sceneCoord.getY() + (int) parent.getSpriteSize().y; y++) {
                if (x < 0 || x > Scene.WIDTH || y < 0 || y > Scene.HEIGHT)
                    continue;
                var buffer = this.objectFields.get(parent.getId());
                if (buffer != null) {
               /* if(buffer[x][y] != null)
                    buffer[x][y].remove();*///TODO
                    buffer[x][y] = gameObject;
                } else {
                    this.objectFields.put(parent.getId(), new GameObject[Scene.WIDTH + 1][Scene.HEIGHT + 1]);
                    buffer = this.objectFields.get(parent.getId());
                    for (var i : buffer) {
                        Arrays.fill(i, null); //TODO
                    }
                    buffer[x][y] = gameObject;
                }
            }
        }
    }

    private void addShapeObjectInBufferNow(ShapeObject object){
        if(!object.isBuffered())
            return;
        for(var gameObject: object.body) {
            var sceneCoord = new Vector3f(gameObject.getCenter());
            sceneCoord = Scene.toGLDimension(sceneCoord);
            sceneCoord = Scene.toScreenDimension(sceneCoord);
            for (int x = (int) sceneCoord.getX() - (int) object.getSpriteSize().x; x <= (int) sceneCoord.getX() + (int) object.getSpriteSize().x; x++) {
                for (int y = (int) sceneCoord.getY() - (int) object.getSpriteSize().y; y <= (int) sceneCoord.getY() + (int) object.getSpriteSize().y; y++) {
                    if (x < 0 || x > Scene.WIDTH || y < 0 || y > Scene.HEIGHT)
                        continue;
                    var buffer = this.objectFields.get(object.getId());
                    if (buffer != null) {
                   /* if(buffer[x][y] != null)
                        buffer[x][y].remove();*///TODO
                        buffer[x][y] = gameObject;
                    } else {
                        this.objectFields.put(object.getId(), new GameObject[Scene.WIDTH + 1][Scene.HEIGHT + 1]);
                        buffer = this.objectFields.get(object.getId());
                        for (var i : buffer) {
                            Arrays.fill(i, null); //TODO
                        }
                        buffer[x][y] = gameObject;
                    }
                }
            }
        }
    }

    public void addObjectInBuffer(ShapeObject object){
        if(!this.busy.get()) {
            this.busy.set(true);
            this.objectBuffer.forEach(this::addShapeObjectInBufferNow);
            this.objectBuffer.clear();
            this.addShapeObjectInBufferNow(object);
            this.busy.set(false);
        }else {
            this.objectBuffer.add(object);
        }
    }

    public GameObject getObject(int id, Vector3f pos){
        var buffer = this.objectFields.get(id);
        if(buffer != null){
            Vector3f scenePos = new Vector3f(pos);
            scenePos = Scene.toGLDimension(scenePos);
            scenePos = Scene.toScreenDimension(scenePos);
            if (scenePos.getX() < 0 || scenePos.getX() > Scene.WIDTH || scenePos.getY() < 0 || scenePos.getY() > Scene.HEIGHT)
                return null;
            if(buffer[(int)scenePos.getX()][(int)scenePos.getY()] != null)
                return buffer[(int)scenePos.getX()][(int)scenePos.getY()];
            return null;
        }else
            return null;
    }

    public <T> T getObject(int id, Vector3f pos, T type){
        var buffer = this.objectFields.get(id);
        if(buffer != null){
            Vector3f scenePos = new Vector3f(pos);
            scenePos = Scene.toGLDimension(scenePos);
            scenePos = Scene.toScreenDimension(scenePos);
            if (scenePos.getX() < 0 || scenePos.getX() > Scene.WIDTH || scenePos.getY() < 0 || scenePos.getY() > Scene.HEIGHT)
                return null;
            if(buffer[(int)scenePos.getX()][(int)scenePos.getY()] != null)
                return (T)buffer[(int)scenePos.getX()][(int)scenePos.getY()];
            return null;
        }else
            return null;
    }

    public Matrix4f transform(Vector3f object){//TODO
        var position = Matrix4f.identity();
        position.set(0, 0, object.getX());
        position.set(1, 0, object.getY());
        position.set(2, 0, object.getZ());
        position.set(3, 0, 1);
        var model = Matrix4f.transform(new Vector3f(object), new Vector3f(0,0,0), new Vector3f(1,1,1));
        var view = Matrix4f.view(camera.getPosition(), camera.getRotation());
        //System.out.println("camera " + camera.getPosition());
        var projection = window.getProjectionMatrix();
        //System.out.println("Model: \n" + model);
        //System.out.println("View: \n" + view);
        //System.out.println("Projection: \n" + projection);
        //var mp = Matrix4f.multiply(model, position);
        //var vmp = Matrix4f.multiply(view, mp);
        //var pvmp = Matrix4f.multiply(projection, mp);
        var gl_Position = Matrix4f.multiply(projection, Matrix4f.multiply(view, Matrix4f.multiply(model, position)));
        return gl_Position;
    }

    public void setObjectVisible(ShapeObject object) {
        if(!this.busy.get()){
            this.busy.set(true);
            //this.invisibleGameObjects.remove(object);
            //this.invisibleGameObjects.removeAll(this.visibleObjectsBuffer);
            this.visibleObjectsBuffer.add(object);
            this.gameObjects.addAll(this.visibleObjectsBuffer);
            this.visibleObjectsBuffer.clear();
            this.isObjectVisibleChanged.set(false);
            this.busy.set(false);
        }else {
            this.visibleObjectsBuffer.add(object);
            this.isObjectVisibleChanged.set(true);
        }
    }

    public void setObjectInvisible(ShapeObject object) {
        if(!this.busy.get()){
            this.busy.set(true);
            this.invisibleObjectsBuffer.add(object);
            this.gameObjects.removeAll(this.invisibleObjectsBuffer);
            //this.invisibleGameObjects.addAll(this.invisibleObjectsBuffer);
            this.invisibleObjectsBuffer.clear();
            this.isObjectVisibleChanged.set(false);
            this.busy.set(false);
        }else {
            this.invisibleObjectsBuffer.add(object);
            this.isObjectVisibleChanged.set(true);
        }
    }

    public int getObjectFieldSize(){
        return this.objectFields.size();
    }
}
