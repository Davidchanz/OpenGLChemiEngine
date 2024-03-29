package org.engine.shapes;

import org.engine.graphics.Material;
import org.engine.graphics.Mesh;
import org.engine.Scene;
import org.engine.graphics.Vertex;
import org.engine.maths.Vector2f;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.utils.Color;
import org.engine.utils.Transformation;

public class Rectangle extends GameObject {
    public Rectangle(float size, Vector3f position, Color color){
        this(new Vector3f(-size/Scene.WIDTH,-size/Scene.HEIGHT, 1f), new Vector3f(size/Scene.WIDTH,-size/Scene.HEIGHT, 1f), new Vector3f(size/Scene.WIDTH,size/Scene.HEIGHT, 1f), new Vector3f(-size/Scene.WIDTH,size/Scene.HEIGHT, 1f), position, color);
        this.setWidth((int)size);//ini height
        this.setHeight((int)size);//ini width
    }

    public Rectangle(float size, Vector3f position, Color color, String texturePath){
        this(new Vector3f(-size/Scene.WIDTH,-size/Scene.HEIGHT, 1f), new Vector3f(size/Scene.WIDTH,-size/Scene.HEIGHT, 1f), new Vector3f(size/Scene.WIDTH,size/Scene.HEIGHT, 1f), new Vector3f(-size/Scene.WIDTH,size/Scene.HEIGHT, 1f), position, color, texturePath);
        this.setWidth((int)size);//ini height
        this.setHeight((int)size);//ini width
    }

    public Rectangle(Vector3f P0, Vector3f P1, Vector3f P2, Vector3f P3, Vector3f position, Color color, String texturePath){
        this.setColor(color);
        Vector3f pos = new Vector3f(position.getX()/Scene.WIDTH, position.getY()/Scene.HEIGHT, position.getZ());
        Mesh mesh = new Mesh(new Vertex[] {
                //Back face
                new Vertex(P3, new Vector2f(0.0f, 0.0f), color),
                new Vertex(P0, new Vector2f(0.0f, 1.0f), color),
                new Vertex(P1, new Vector2f(1.0f, 1.0f), color),
                new Vertex(P2, new Vector2f(1.0f, 0.0f), color),

        }, new int[] {
                //Back face
                0, 1, 2,
                2, 3, 0,
        }, new Material(texturePath));
        this.ini(pos, new Vector3f(0,0,0), new Vector3f(1,1,1), mesh);
    }

    public Rectangle(Vector3f P0, Vector3f P1, Vector3f P2, Vector3f P3, Vector3f position, Color color){
        this.setColor(color);
        Vector3f pos = new Vector3f(position.getX()/Scene.WIDTH, position.getY()/Scene.HEIGHT, position.getZ());
        Mesh mesh = new Mesh(new Vertex[] {
                //Back face
                new Vertex(P3, new Vector2f(0.0f, 0.0f), color),
                new Vertex(P0, new Vector2f(0.0f, 1.0f), color),
                new Vertex(P1, new Vector2f(1.0f, 1.0f), color),
                new Vertex(P2, new Vector2f(1.0f, 0.0f), color),
        }, new int[] {
                //Back face
                0, 1, 2,
                2, 3, 0,
        });
        this.ini(pos, new Vector3f(0,0,0), new Vector3f(1,1,1), mesh);
    }

    public Rectangle(Vector3f P0, Vector3f P1, Vector3f P2, Vector3f P3, Vector3f position, Vector3f normal, Color color){
        Mesh mesh = new Mesh(new Vertex[] {
                //Back face
                new Vertex(P3, new Vector2f(+0.0f, 0.0f), color),
                new Vertex(P0, new Vector2f(+0.0f, 1.0f), color),
                new Vertex(P1, new Vector2f(+1.0f, 1.0f), color),
                new Vertex(P2, new Vector2f(+1.0f, 0.0f), color),
        }, new int[] {
                //Back face
                0, 1, 2,
                2, 3, 0,
        }, new Material("src/main/resources/textures/1.png"));
        Vector3f pos = new Vector3f(position.getX()/Scene.WIDTH, position.getY()/Scene.HEIGHT, position.getZ());

        this.ini(pos, new Vector3f(0,0,0), new Vector3f(1,1,1), normal, mesh);
    }

    @Override
    public void resize() {
        var P0 = new Vector3f(-this.getWidth()/Scene.WIDTH,-this.getHeight()/Scene.WIDTH, 1f);
        var P1 = new Vector3f(this.getWidth()/Scene.WIDTH,-this.getHeight()/Scene.WIDTH, 1f);
        var P2 = new Vector3f(this.getWidth()/Scene.WIDTH,this.getHeight()/Scene.WIDTH, 1f);
        var P3 = new Vector3f(-this.getWidth()/Scene.WIDTH,this.getHeight()/Scene.WIDTH, 1f);
        Vector3f pos = new Vector3f(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());
        Mesh mesh = new Mesh(new Vertex[] {
                new Vertex(P3, new Vector2f(0.0f, 0.0f), this.getColor()),
                new Vertex(P0, new Vector2f(0.0f, 1.0f), this.getColor()),
                new Vertex(P1, new Vector2f(1.0f, 1.0f), this.getColor()),
                new Vertex(P2, new Vector2f(1.0f, 0.0f), this.getColor()),

        }, new int[] {
                //Back face
                0, 1, 2,
                2, 3, 0,
        }, this.getMesh().getMaterial());
        this.ini(pos, new Vector3f(0,0,0), new Vector3f(1,1,1), mesh);
    }

    @Override
    public Transformation transform() {
        return null;
    }
}
