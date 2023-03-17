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

public class Line extends GameObject {
    public Line(Vector3f start, Vector3f end, Color color){
        this.setColor(color);
        this.setWidth(Math.abs(start.getX() - end.getX()));
        this.setHeight(Math.abs(start.getY() - end.getY()));

        ArrayList<Vertex> result = new ArrayList<>();
        result.add(new Vertex(
                new Vector3f(
                        start.getX()/Scene.WIDTH,
                        start.getY()/Scene.HEIGHT,
                        start.getZ()/Scene.WIDTH
                ),
                new Vector2f(
                        0.5f,
                        0.5f
                ),
                color));
        result.add(new Vertex(
                new Vector3f(
                        end.getX()/Scene.WIDTH,
                        end.getY()/Scene.HEIGHT,
                        end.getZ()/Scene.WIDTH
                ),
                new Vector2f(
                        0.5f,
                        0.5f
                ),
                color));

        Mesh mesh = new Mesh(result.toArray(result.toArray(new Vertex[0])),
                new int[] {
                        //Back face
                        0
                });
        this.ini(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1), mesh);
    }

    @Override
    public void resize() {

    }

    @Override
    public Transformation transform() {
        return null;
    }
}
