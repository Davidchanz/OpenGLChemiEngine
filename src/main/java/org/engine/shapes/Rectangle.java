package org.engine.shapes;

import org.engine.graphics.Material;
import org.engine.graphics.Mesh;
import org.engine.Scene;
import org.engine.graphics.Vertex;
import org.engine.maths.Vector2f;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.utils.Color;

public class Rectangle extends GameObject {
    public Rectangle(float size, Vector3f position, Color color){
        this(new Vector3f(-size/Scene.WIDTH,-size/Scene.HEIGHT, 1f), new Vector3f(size/Scene.WIDTH,-size/Scene.HEIGHT, 1f), new Vector3f(size/Scene.WIDTH,size/Scene.HEIGHT, 1f), new Vector3f(-size/Scene.WIDTH,size/Scene.HEIGHT, 1f), position, color);
        this.setWidth((int)size);//ini height
        this.setHeight((int)size);//ini width
    }

    public Rectangle(Vector3f P0, Vector3f P1, Vector3f P2, Vector3f P3, Vector3f position, Color color){
        this.setColor(color);
        Vector3f pos = new Vector3f(position.getX()/Scene.WIDTH, position.getY()/Scene.HEIGHT, position.getZ()/Scene.WIDTH);
        Vector3f c = new Vector3f(color.getRed(),color.getGreen(),color.getBlue());
        Mesh mesh = new Mesh(new Vertex[] {
                //Back face
                new Vertex(P0, new Vector2f(0.0f, 0.0f), c),
                new Vertex(P1, new Vector2f(0.0f, 1.0f), c),
                new Vertex(P2, new Vector2f(1.0f, 1.0f), c),
                new Vertex(P3, new Vector2f(1.0f, 0.0f), c),

        }, new int[] {
                //Back face
                0, 1, 2,
                2, 3, 0,
        }, color);
        this.ini(pos, new Vector3f(0,0,0), new Vector3f(1,1,1), mesh);

        float sumX=0;
        float sumY=0;
        float sumZ=0;

        sumX+= P0.getX() + P1.getX() + P2.getX() + P3.getX() /*+ getPosition().getX()*/;//compute sum centers X
        sumY+= P0.getY() + P1.getY() + P2.getY() + P3.getY() /*+ getPosition().getY()*/;;//compute sum centers Y
        sumZ+= P0.getZ() + P1.getZ() + P2.getZ() + P3.getZ() /*+ getPosition().getZ()*/;;//compute sum centers Z

        setCenter(new Vector3f(sumX/4, sumY/4, sumZ/4));//ini center
    }

    public Rectangle(Vector3f P0, Vector3f P1, Vector3f P2, Vector3f P3, Vector3f position, Vector3f normal, Color color){
        Vector3f c = new Vector3f(color.getRed(),color.getGreen(),color.getBlue());
        Mesh mesh = new Mesh(new Vertex[] {
                //Back face
                new Vertex(P0, new Vector2f(0.0f, 0.0f), c),
                new Vertex(P1, new Vector2f(0.0f, 1.0f), c),
                new Vertex(P2, new Vector2f(1.0f, 1.0f), c),
                new Vertex(P3, new Vector2f(1.0f, 0.0f), c),

        }, new int[] {
                //Back face
                0, 1, 2,
                2, 3, 0,
        }, new Material("/textures/1.png"));
        Vector3f pos = new Vector3f(position.getX()/Scene.WIDTH, position.getY()/Scene.HEIGHT, position.getZ()/Scene.WIDTH);

        this.ini(pos, new Vector3f(0,0,0), new Vector3f(1,1,1), mesh);

        float sumX=0;
        float sumY=0;
        float sumZ=0;

        sumX+= P0.getX() + P1.getX() + P2.getX() + P3.getX() /*+ getPosition().getX()*/;//compute sum centers X
        sumY+= P0.getY() + P1.getY() + P2.getY() + P3.getY() /*+ getPosition().getY()*/;;//compute sum centers Y
        sumZ+= P0.getZ() + P1.getZ() + P2.getZ() + P3.getZ() /*+ getPosition().getZ()*/;;//compute sum centers Z

         setCenter(new Vector3f(sumX/4, sumY/4, sumZ/4));//ini center
        /*add(new GameObject(new Vector3f(1,0,0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh));
        add(new GameObject(new Vector3f(-1,0,0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh));

        add(new GameObject(new Vector3f(0,1,0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh));
        add(new GameObject(new Vector3f(1,1,0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh));
        add(new GameObject(new Vector3f(-1,1,0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh));

        add(new GameObject(new Vector3f(0,-1,0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh));
        add(new GameObject(new Vector3f(1,-1,0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh));
        add(new GameObject(new Vector3f(-1,-1,0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh));*/

    }

    @Override
    public void resize() {
        var P0 = new Vector3f(-this.getWidth()/Scene.WIDTH,-this.getHeight()/Scene.WIDTH, 1f);
        var P1 = new Vector3f(this.getWidth()/Scene.WIDTH,-this.getHeight()/Scene.WIDTH, 1f);
        var P2 = new Vector3f(this.getWidth()/Scene.WIDTH,this.getHeight()/Scene.WIDTH, 1f);
        var P3 = new Vector3f(-this.getWidth()/Scene.WIDTH,this.getHeight()/Scene.WIDTH, 1f);
        Vector3f pos = new Vector3f(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());
        Vector3f c = new Vector3f(this.getColor().getRed(), this.getColor().getGreen(), this.getColor().getBlue());
        Mesh mesh = new Mesh(new Vertex[] {

                //Back face
                new Vertex(P0, new Vector2f(0.0f, 0.0f), c),
                new Vertex(P1, new Vector2f(0.0f, 1.0f), c),
                new Vertex(P2, new Vector2f(1.0f, 1.0f), c),
                new Vertex(P3, new Vector2f(1.0f, 0.0f), c),

        }, new int[] {
                //Back face
                0, 1, 2,
                2, 3, 0,
        }, this.getColor());
        this.ini(pos, new Vector3f(0,0,0), new Vector3f(1,1,1), mesh);
        this.setResized(true);
    }
}
