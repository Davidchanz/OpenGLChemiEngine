package org.engine.objects;

import org.engine.Scene;
import org.engine.maths.Vector3f;

import java.util.ArrayList;
import java.util.Collection;

//todo

/**Class for scene object which especially is a list of AbstractShape elements*/
public class ShapeObject extends EngineObject {
    public ArrayList<GameObject> body;//set of Abstract shape
    public String name;//name
    public Vector3f normal;

    public final ArrayList<GameObject> newObjects = new ArrayList<>();
    /**ShapeObject constructor
     * ini all members default*/
    public ShapeObject(){
        this.id = 0;//ini id
        this.name = "Template";//ini name
        this.body = new ArrayList<>();//ini body
        this.setRotation(new Vector3f(0,0,0));
        this.setCenter(new Vector3f(0,0,0));
        this.setPosition(new Vector3f(0,0,0));
    }
    /**ShapeObject constructor
     * ini name and id*/
    public ShapeObject(String name, int id){
        this();//invoke default constructor
        this.id = id;//ini id
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
            i.setPosition(dir);//move all AbstractShapes
        }
    }
    public void add(GameObject o){
       /* new Thread(()->{
            synchronized (this.newObjects) {*/
        //o.id = this.id;//ini new shape id
                this.body.add(o);//add new shape in body
                o.setRotation(this.getRotation());//Vector3f.add(o.getRotation(), this.getRotation());
                float sumX=0;
                float sumY=0;
                float sumZ=0;
                for(var i: this.body.toArray(new GameObject[0])){
                    sumX+= i.getCenter().getX() + i.getPosition().getX();//compute sum centers X
                    sumY+= i.getCenter().getY() + i.getPosition().getY();;//compute sum centers Y
                    sumZ+= i.getCenter().getZ() + i.getPosition().getZ();;//compute sum centers Z
                }
                sumX+= o.getCenter().getX() + o.getPosition().getX();
                sumY+= o.getCenter().getY() + o.getPosition().getY();
                sumZ+= o.getCenter().getZ() + o.getPosition().getZ();
                this.setCenter(new Vector3f(sumX/(this.body.size()+1), sumY/(this.body.size()+1), sumZ/(this.body.size()+1)));//ini center
                this.setPosition(Vector3f.add(this.getPosition(), o.getPosition()));//ini position
                //for(var i: this.body){
                o.setParent(this);//ini shape parent
                //this.newObjects.add(o);
                //}
           /* }
        }).start();*/
    }
    /**Add new element on object*/
    /*public GameObject addNew(GameObject o){
        o.build();
        //o.id = this.id;//ini new shape id
        this.body.add(o);//add new shape in body
        for(var i: this.body){
            i.setParent(this);//ini shape parent
        }
        return o;
    }*/
    /**Add new element collection on object*/
    public void addAll(Collection<GameObject> o){
        for(var i : o){
            add(i);//invoke add single add() for all new shapes in collections
        }
    }
    public void rotate(Vector3f rotation, Vector3f center){
        this.setRotation(rotation);
        for(var i: this.body){
            i.setCenter(center);
            i.setRotation(rotation);
        }
    }

    public void build() {
        for(int i = 0; i < this.body.size(); i++)
            this.body.get(i).build();
    }

    public void destroy(){
        for(int i = 0; i < this.body.size(); i++)
            this.body.get(i).destroy();
    }
}
