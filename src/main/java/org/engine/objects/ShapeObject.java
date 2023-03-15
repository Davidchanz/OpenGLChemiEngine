package org.engine.objects;

import org.UnityMath.Vector2;
import org.engine.Scene;
import org.engine.maths.Vector3f;
import org.engine.utils.Transformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//todo

/**Class for scene object which especially is a list of AbstractShape elements*/
public class ShapeObject extends EngineObject {
    public ArrayList<GameObject> body;//set of Abstract shape
    public String name;//name
    public Vector3f normal;
    private Vector2 spriteSize;

    public final ArrayList<GameObject> newObjects = new ArrayList<>();
    private boolean isVisible;
    private boolean isBuffered;
    private boolean isRemove;
    private boolean isAddObject;
    private boolean isRemoveObject;
    private final List<GameObject> addBuffer = Collections.synchronizedList(new ArrayList<>());
    private final List<GameObject> removeBuffer = Collections.synchronizedList(new ArrayList<>());
    /**ShapeObject constructor
     * ini all members default*/
    public ShapeObject(){
        this.setId(0);//ini id
        this.name = "Template";//ini name
        this.body = new ArrayList<>();//ini body
        this.setRotation(new Vector3f(0,0,0));
        this.setCenter(new Vector3f(0,0,0));
        this.setPosition(new Vector3f(0,0,0));
        this.isVisible = true;
        this.isBuffered = true;
        this.isRemove = false;
        this.isAddObject = false;
        this.isRemoveObject = false;
        this.setSpriteSize(new Vector2(0,0));
    }
    /**ShapeObject constructor
     * ini name and id*/
    public ShapeObject(String name, int id){
        this();//invoke default constructor
        this.setId(id);//ini id
        this.name = name;//ini name
    }
    /**ShapeObject constructor
     * ini id name adn position*/
    public ShapeObject(String name, int id, Vector3f pos){
        this(name, id);//ini name id constructor
        this.setPosition( new Vector3f(pos));//ini position
    }
    /**Method move*/
    public void move(Vector3f dir){
        dir = new Vector3f(dir.getX()/ Scene.WIDTH, dir.getY()/Scene.HEIGHT, dir.getZ()/Scene.WIDTH);
        this.setPosition(Vector3f.add(this.getPosition(), dir));//move position on dir
        //this.center = Vector3f.add(this.center, dir);//move center on dir TODO
        for(var i: this.body){
            i.setPosition(Vector3f.add(i.getPosition(), dir));//move all AbstractShapes
        }
    }

    public void remove(GameObject o){
        if(this.getScene() == null){
            this.removeIf(o);
        }else {
            this.isRemoveObject = true;
            this.removeBuffer.add(o);
        }
    }

    public void removeAll(Collection<GameObject> objects){
        objects.forEach(this::remove);
    }

    public void removeIf(GameObject o){
        o.destroy();
        this.body.remove(o);//add new shape in body
        float sumX=0;
        float sumY=0;
        float sumZ=0;
        for(var i: this.body.toArray(new GameObject[0])){
            sumX+= i.getCenter().getX() + i.getPosition().getX();//compute sum centers X
            sumY+= i.getCenter().getY() + i.getPosition().getY();;//compute sum centers Y
            sumZ+= i.getCenter().getZ() + i.getPosition().getZ();;//compute sum centers Z
        }
        this.setCenter(new Vector3f(sumX/(this.body.size()), sumY/(this.body.size()), sumZ/(this.body.size())));//ini center
        this.setPosition(Vector3f.subtract(this.getPosition(), o.getPosition()));//ini position//TODO
    }

    public void add(GameObject o){
        if(this.getScene() == null){
            this.addIf(o);
        }else {
            this.isAddObject = true;
            o.setParent(this);
            this.addBuffer.add(o);
        }
    }

    public void addIf(GameObject o){
        o.setId(this.getId());//ini new shape id
        o.setParent(this);//ini shape parent
        o.setRotation(this.getRotation());//Vector3f.add(o.getRotation(), this.getRotation());
        this.body.add(o);//add new shape in body
        //this.spriteSize = new Vector2(o.getWidth(), o.getHeight());//TODO
        float sumX=0;
        float sumY=0;
        float sumZ=0;
        for(var i: this.body.toArray(new GameObject[0])){
            sumX+= i.getCenter().getX() + i.getPosition().getX();//compute sum centers X
            sumY+= i.getCenter().getY() + i.getPosition().getY();;//compute sum centers Y
            sumZ+= i.getCenter().getZ() + i.getPosition().getZ();;//compute sum centers Z
        }
        this.setCenter(new Vector3f(sumX/(this.body.size()), sumY/(this.body.size()), sumZ/(this.body.size())));//ini center
        this.setPosition(Vector3f.add(this.getPosition(), o.getPosition()));//ini position
    }

    /**Add new element collection on object*/
    public void addAll(Collection<GameObject> o){
        o.forEach(this::add);//invoke add single add() for all new shapes in collections
    }
    public void rotate(Vector3f rotation, Vector3f center){
        this.setRotation(rotation);
        for(var i: this.body){
            i.setCenter(center);
            i.setRotation(rotation);
        }
    }

    public void build() {
        for (GameObject gameObject : this.body) gameObject.build();
    }

    public void destroy(){
        for (GameObject gameObject : this.body) gameObject.destroy();
    }

    @Override
    public void setScene(Scene scene) {
        super.setScene(scene);
        this.body.forEach(gameObject -> gameObject.setScene(scene));
    }

    public Vector2 getSpriteSize() {
        return spriteSize;
    }

    public void setSpriteSize(Vector2 spriteSize) {
        this.spriteSize = new Vector2(spriteSize);
    }

    @Override
    public Transformation transform() {
       /* var position = this.getPosition();
        var model = Matrix4f.transform(object.getPosition(), object.getRotation(), object.getScale()));
        var view = Matrix4f.view(camera.getPosition(), camera.getRotation()));
        var projection = window.getProjectionMatrix());*/
        return null;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
        if(this.getScene() != null)
            if(this.isVisible){
                this.getScene().setObjectVisible(this);
            }else {
                this.getScene().setObjectInvisible(this);
            }
    }

    public void remove(){
        this.isRemove = true;
        if(this.getScene() != null)
            this.getScene().remove(this);
    }

    public boolean isBuffered() {
        return isBuffered;
    }

    public void setBuffered(boolean buffered) {
        isBuffered = buffered;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    public boolean isAddObject() {
        return isAddObject;
    }

    public void setAddObject(boolean addObject) {
        isAddObject = addObject;
    }

    public boolean isRemoveObject() {
        return isRemoveObject;
    }

    public void setRemoveObject(boolean removeObject) {
        isRemoveObject = removeObject;
    }

    public void addAllIf() {
        //this.addBuffer.forEach(this::addIf);//TODO
        this.getScene().addGameObjectInBufferPermanent(this.addBuffer);
        this.addBuffer.clear();
        this.isAddObject = false;
    }

    public void removeAllIf() {
        this.removeBuffer.forEach(this::removeIf);
        this.removeBuffer.forEach(gameObject -> this.getScene().removeGameObjectFromBuffer(gameObject));
        this.removeBuffer.clear();
        this.isRemoveObject = false;
    }
}
