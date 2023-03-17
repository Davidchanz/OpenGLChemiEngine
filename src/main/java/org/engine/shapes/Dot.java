package org.engine.shapes;

import org.engine.Scene;
import org.engine.graphics.Mesh;
import org.engine.graphics.Vertex;
import org.engine.maths.Vector2f;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.utils.Color;
import org.engine.utils.Transformation;

import java.util.ArrayList;

public class Dot extends GameObject {
    public Dot(Vector3f position, Color color){
        this.setColor(color);
        this.setWidth(1);
        this.setHeight(1);

        ArrayList<Vertex> result = new ArrayList<>();
        result.add(new Vertex(
                new Vector3f(
                        position.getX()/ Scene.WIDTH,
                        position.getY()/Scene.HEIGHT,
                        position.getZ()/Scene.WIDTH
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
