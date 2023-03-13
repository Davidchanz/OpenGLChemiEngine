package org.engine;

import org.UnityMath.Vector2;
import org.engine.events.GLKeyEvent;
import org.engine.maths.Vector3f;
import org.engine.objects.Camera;
import org.engine.utils.Color;
import org.engine.shapes.Rectangle;
import org.engine.objects.ShapeObject;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public static void main(String[] args) {
        Scene scene = new Scene(800, 800, Color.WHITE);
        for (int i = 0; i < 1000; i++) {
            ShapeObject rect = new ShapeObject();
            var r = new Rectangle(10, new Vector3f(i*10, 0, 0), Color.WHITE, "src/main/resources/textures/ant.png");
            rect.add(r);
            //rect.add(new Rectangle(50, new Vector3f(50,0,0), Color.WHITE, "src/main/resources/textures/ant.png"));
            scene.add(rect);
        }
        //r.setWidth(100);
        //r.resize();

        Camera camera = new Camera(new Vector3f(0, 0, 1.712f), new Vector3f(0, 0, 0));
        //scene.setCamera(camera);

        /*Cube c = new Cube("", 1, new Vector3f(0,0,0));
        scene.add(c);*/
        //ArrayList<ShapeObject> objects = new ArrayList<>();
        Vector2 dir = new Vector2();
        //dir.set(0,1);
        /*switch (*//*new Random().nextInt(4)*//*0){
            case 0 ->{//r
                dir.set(10,0);
                rect.rotate(new Vector3f(0,0, 0), rect.getPosition());
            }
            case 1 ->{//l
                dir.set(-10,0);
                rect.rotate(new Vector3f(0,0, 180), rect.getPosition());
            }
            case 2 ->{//u
                dir.set(0,-10);
                rect.rotate(new Vector3f(0,0, -90), rect.getPosition());
            }
            case 3 ->{//d
                dir.set(0,10);
                rect.rotate(new Vector3f(0,0, 90), rect.getPosition());
            }
        }*/
        /*AtomicInteger count = new AtomicInteger(1);

        Timer t1 = new Timer(1000, actionEvent -> {
            *//*for(int i = 0; i < 5; i++) {
                ShapeObject o = new ShapeObject();
                o.add(new Rectangle(5, new Vector3f(new Random().nextFloat(100f)-50,new Random().nextFloat(100f) - 50,0), Color.RED));
                scene.add(o);
                objects.add(o);
            }
            rect.move(new Vector3f(1,0,0));*//*
            if(count.getAndIncrement() % 5 == 0) {
                var degree = new Random().nextInt(90)-45;
                //System.out.println("dir angle before: "+dir.angleDeg());
                dir.rotateDeg(degree);
                System.out.println("dir angle past: "+dir.angleDeg());
                //System.out.println("degee: "+degree);
                //System.out.println("rect angle before: "+rect.getRotation().getZ());
                rect.rotate(Vector3f.add(rect.getRotation(), new Vector3f(0, 0, -degree)), new Vector3f(rect.getPosition().getX(), rect.getPosition().getY(), rect.getPosition().getZ()));
                System.out.println("rect angle past: "+rect.getRotation().getZ());
            }
            rect.move(new Vector3f(dir.x, dir.y, 0));
            //System.out.println(dir.angleDeg());
        });*/

        /*Timer t2 = new Timer(100, actionEvent -> {
            for(int i = 0; i < 5; i++) {
                scene.remove(objects.get(objects.size()-1));
                objects.remove(objects.get(objects.size()-1));
            }
        });*/

        //t1.start();
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

       /* scene.addKeyPressedListener(glKeyEvent -> {
            switch (glKeyEvent.getKey()){
                case GLKeyEvent.W -> {
                    rect.move(new Vector3f(0,1,0));
                    rect.rotate(Vector3f.add(rect.getRotation(), new Vector3f(0,0,90)), new Vector3f(rect.getPosition().getX(), rect.getPosition().getY(), rect.getPosition().getZ()));
                }
                case GLKeyEvent.A -> rect.move(new Vector3f(-1,0,0));
                case GLKeyEvent.S -> rect.move(new Vector3f(0,-1,0));
                case GLKeyEvent.D -> rect.move(new Vector3f(1,0,0));
            }
            if(glKeyEvent.getKey() == GLKeyEvent.A)
                System.out.println("A");
        });*/

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
