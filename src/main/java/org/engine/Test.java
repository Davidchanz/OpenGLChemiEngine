package org.engine;

import org.engine.maths.Vector3f;
import org.engine.utils.Color;
import org.engine.shapes.Rectangle;
import org.engine.objects.ShapeObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Scene scene = new Scene(800, 800, Color.ALICEBLUE);
       /* ShapeObject rect = new ShapeObject();
        var r = new Rectangle(50, new Vector3f(0,0,0), Color.CYAN);
        rect.add(r);
        scene.add(rect);
        r.setWidth(100);
        r.resize();*/
        ArrayList<ShapeObject> objects = new ArrayList<>();

        Timer t1 = new Timer(100, actionEvent -> {
            for(int i = 0; i < 5; i++) {
                ShapeObject o = new ShapeObject();
                o.add(new Rectangle(5, new Vector3f(new Random().nextFloat(100f)-50,new Random().nextFloat(100f) - 50,0), Color.RED));
                scene.add(o);
                objects.add(o);
            }
        });

        Timer t2 = new Timer(100, actionEvent -> {
            for(int i = 0; i < 5; i++) {
                scene.remove(objects.get(objects.size()-1));
                objects.remove(objects.get(objects.size()-1));
            }
        });

        t1.start();
        t2.start();

        scene.start();
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
