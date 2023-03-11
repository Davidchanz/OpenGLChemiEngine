package org.engine;

import org.engine.Scene;
import org.engine.maths.Vector3f;
import org.engine.utils.Color;
import org.engine.shapes.Rectangle;
import org.engine.objects.ShapeObject;

public class Test {
    public static void main(String[] args) {
        Scene scene = new Scene(800, 800, Color.ALICEBLUE);
        ShapeObject rect = new ShapeObject();
        var r = new Rectangle(50, new Vector3f(0,0,0), Color.CYAN);
        rect.add(r);
        scene.add(rect);
        r.setWidth(100);
        r.resize();
        /*while (true){
            System.out.println("asdf");
            rect.body.get(0).setWidth(100);
            rect.body.get(0).resize();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/

        //rect.move(new Vector3f(200,0,0));
    }

}
