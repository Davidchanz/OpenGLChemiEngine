package org.engine.shapes;

import org.engine.Scene;
import org.engine.graphics.Material;
import org.engine.graphics.Mesh;
import org.engine.graphics.Vertex;
import org.engine.maths.Vector2f;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.utils.Color;
import org.engine.utils.Transformation;

import java.util.ArrayList;

public class Circle extends GameObject {
    public Circle(float radius, Vector3f position, Color color, String texturePath){
        this.setColor(color);
        this.setWidth(radius);
        this.setHeight(radius);
        radius = radius/Scene.WIDTH;
        Vector3f pos = new Vector3f(position.getX()/ Scene.WIDTH, position.getY()/Scene.HEIGHT, position.getZ()/Scene.WIDTH);
        int fragments = 36;
        ArrayList<Vertex> result = new ArrayList<>();
        float increment = 2.0f * (float)Math.PI / fragments;
        for (float currAngle = 0.0f; currAngle <= 2.0f * Math.PI; currAngle += increment) {
            float t = 0.5f;
            float s = -0.1f;
            result.add(new Vertex(
                    new Vector3f(
                            radius * (float)Math.cos(currAngle) /*+ x*/,
                            radius * (float)Math.sin(currAngle) /*+ y*/,
                            0),
                    new Vector2f(
                            s+t + t * (float)Math.cos(currAngle),
                            s+t - t * (float)Math.sin(currAngle)
                    ),
                    color));
        }
        Mesh mesh = new Mesh(result.toArray(new Vertex[0]),
                new int[] {
                //Back face
                0
        }, new Material(texturePath));
        this.ini(pos, new Vector3f(0,0,0), new Vector3f(1,1,1), mesh);
    }

    public Circle(float radius, Vector3f position, Color color){
        this.setColor(color);
        this.setWidth(radius);
        this.setHeight(radius);
        radius = radius/Scene.WIDTH;
        Vector3f pos = new Vector3f(position.getX()/ Scene.WIDTH, position.getY()/Scene.HEIGHT, position.getZ()/Scene.WIDTH);
        int fragments = 36;
        ArrayList<Vertex> result = new ArrayList<>();
        float increment = 2.0f * (float)Math.PI / fragments;
        for (float currAngle = 0.0f; currAngle <= 2.0f * Math.PI; currAngle += increment)
        {
            float t = 0.5f;
            float s = -0.1f;
            result.add(new Vertex(
                    new Vector3f(
                            radius * (float)Math.cos(currAngle),
                            radius * (float)Math.sin(currAngle),
                            0),
                    new Vector2f(
                            s+t + t * (float)Math.cos(currAngle),
                            s+t - t * (float)Math.sin(currAngle)
                    ),
                    color));
        }

        Mesh mesh = new Mesh(result.toArray(new Vertex[0]),
                new int[] {
                        //Back face
                        0
                });

        this.ini(pos, new Vector3f(0,0,0), new Vector3f(1,1,1), mesh);
    }

    @Override
    public void resize() {

    }

    @Override
    public Transformation transform() {
        return null;
    }
}
