package org.engine;

import org.engine.events.GLKeyEvent;
import org.engine.maths.Vector3f;
import org.engine.objects.Camera;
import org.engine.shapes.Cube;
import org.engine.utils.Color;
import org.engine.shapes.Rectangle;
import org.engine.objects.ShapeObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Scene scene = new Scene(800, 800, Color.TRANSPARENT);
        ShapeObject rect = new ShapeObject();
        var r = new Rectangle(50, new Vector3f(0,0,0), new Color(1,1,1,0.5f), "src/main/resources/textures/ant.png");
        rect.add(r);
        rect.add(new Rectangle(50, new Vector3f(50,0,0), Color.WHITE, "src/main/resources/textures/ant.png"));
        scene.add(rect);
        //r.setWidth(100);
        r.resize();

        Camera camera = new Camera(new Vector3f(0, 0, 1.712f), new Vector3f(0, 0, 0));
        //scene.setCamera(camera);

        /*Cube c = new Cube("", 1, new Vector3f(0,0,0));
        scene.add(c);*/
        //ArrayList<ShapeObject> objects = new ArrayList<>();

        Timer t1 = new Timer(100, actionEvent -> {
            /*for(int i = 0; i < 5; i++) {
                ShapeObject o = new ShapeObject();
                o.add(new Rectangle(5, new Vector3f(new Random().nextFloat(100f)-50,new Random().nextFloat(100f) - 50,0), Color.RED));
                scene.add(o);
                objects.add(o);
            }
            rect.move(new Vector3f(1,0,0));*/
            System.out.println(rect.getPosition());
            rect.rotate(new Vector3f(0,0,1), rect.getPosition());
        });

        /*Timer t2 = new Timer(100, actionEvent -> {
            for(int i = 0; i < 5; i++) {
                scene.remove(objects.get(objects.size()-1));
                objects.remove(objects.get(objects.size()-1));
            }
        });*/

       // t1.start();
        //t2.start();

        scene.addMouseEventListener(glMouseEvent -> {
            System.out.println(glMouseEvent.getButton());
            if(glMouseEvent.isButtonDown(0))
                System.out.println("qqqqqq");
        });

        scene.addMouseEventListener(glMouseEvent -> {
            if(glMouseEvent.isButtonDown(1))
                System.out.println("wwwwwwwww");
        });

        scene.addKeyPressedListener(glKeyEvent -> {
            switch (glKeyEvent.getKey()){
                case GLKeyEvent.W -> rect.move(new Vector3f(0,1,0));
                case GLKeyEvent.A -> rect.move(new Vector3f(-1,0,0));
                case GLKeyEvent.S -> rect.move(new Vector3f(0,-1,0));
                case GLKeyEvent.D -> rect.move(new Vector3f(1,0,0));
            }
            if(glKeyEvent.getKey() == GLKeyEvent.A)
                System.out.println("A");
        });

        scene.addKeyPressedListener(glKeyEvent -> {
            if(glKeyEvent.getKey() == GLKeyEvent.B)
                System.out.println("B");
        });

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
