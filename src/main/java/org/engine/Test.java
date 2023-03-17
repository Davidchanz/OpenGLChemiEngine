package org.engine;

import org.UnityMath.Vector2;
import org.engine.cameras.FirstPerson3DCamera;
import org.engine.cameras.PerspectiveCamera;
import org.engine.cameras.ThirdPerson3DCamera;
import org.engine.events.GLKeyEvent;
import org.engine.maths.Vector3f;
import org.engine.cameras.Camera;
import org.engine.objects.GameObject;
import org.engine.shapes.*;
import org.engine.utils.Color;
import org.engine.objects.ShapeObject;

import javax.swing.*;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeMap;

public class Test {
    public static void main(String[] args) {

        Scene scene = new Scene(800, 800, 800, 800, Color.TRANSPARENT);
        /*for(int i = 0; i < 10; i++){
            ShapeObject o = new ShapeObject();
            o.add(new Rectangle(10, new Vector3f(i*11,0,0), Color.RED));
            scene.add(o);
        }*/
        //scene.setCamera(new FirstPerson3DCamera());

        ShapeObject circle = new ShapeObject();
        Triangle c = new Triangle(100f, new Vector3f(0,0,0), Color.WHITE, "src/main/resources/textures/beautiful.png");
       /* Line c1 = new Line(new Vector3f(0,0,0), new Vector3f(0,800,0), Color.BLUE);
        Line c2 = new Line(new Vector3f(0,0,0), new Vector3f(0,0,800), Color.GREEN);*/

        circle.add(c);
        /*circle.add(c1);
        circle.add(c2);*/

        scene.add(circle);
        System.out.println(circle.getCenter());

        Timer t = new Timer(15, actionEvent -> {
            //circle.move(new Vector3f(1,0,0));
                //circle.rotate(new Vector3f(circle.getRotation().getX()+1,circle.getRotation().getY()+0,circle.getRotation().getZ()+0), new Vector3f(0,0,0));
            }
        );
        t.start();


        scene.start();
        /*TreeMap<Integer, ShapeObject[][]> objectFields = new TreeMap<>();

        for (int i = 0; i < 5; i++){
            objectFields.put(i, new ShapeObject[800][800]);
            var buffer = objectFields.get(i);
            for (var j : buffer) {
                Arrays.fill(j, new ShapeObject()); //TODO
            }
        }

        new Timer(1, actionEvent -> {
            var id = new Random().nextInt(5);
            var x = new Random().nextInt(800);
            var y = new Random().nextInt(800);
            var buffer = objectFields.get(id);
            if (buffer != null) {
                *//*if(buffer[x][y] != null)
                    buffer[x][y].remove();*//*//TODO
                buffer[x][y] = new ShapeObject();
            }
        }).start();
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/


       /* Scene scene = new Scene(2000, 2000, 800, 800, Color.WHITE);
        ShapeObject rect = new ShapeObject();;
        for (int i = 0; i < 1; i++) {
            var r = new Rectangle(10, new Vector3f(i * 10, 0, 0), Color.RED);
            rect.add(r);
            //rect.add(new Rectangle(50, new Vector3f(50,0,0), Color.WHITE, "src/main/resources/textures/ant.png"));
            scene.add(rect);
        }
        *//*ShapeObject r = new ShapeObject();
        r.add(new Rectangle(10, new Vector3f(100, 0, 0), Color.WHITE, "src/main/resources/textures/ant.png"));
        scene.add(r);*//*
        for (int i = 0; i < 10; i++) {
            ShapeObject e = new ShapeObject();
            var r = new Rectangle(10, new Vector3f(i * 10, 0, 0), Color.BLACK);
            e.add(r);
            //rect.add(new Rectangle(50, new Vector3f(50,0,0), Color.WHITE, "src/main/resources/textures/ant.png"));
            scene.add(e);
        }*/
        //r.setWidth(100);
        //r.resize();

        //ThirdPerson3DCamera camera = new ThirdPerson3DCamera(new Vector3f(0, 0, 1.712f), new Vector3f(0, 0, 0));
        //scene.setCamera(camera);
        //camera.setTargetObject(rect);


        /*Cube c = new Cube("", 1, new Vector3f(0,0,0));
        scene.add(c);*/
        //ArrayList<ShapeObject> objects = new ArrayList<>();
        //Vector2 dir = new Vector2();
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

        /*scene.addMouseButtonPressedListener(glMouseEvent -> {
            //System.out.println(glMouseEvent.getButton());
            System.out.println(glMouseEvent.getX());
            //if (glMouseEvent.isButtonDown(0))
                //System.out.println("qqqqqq");
        });

        scene.addMouseButtonPressedListener(glMouseEvent -> {
            if (glMouseEvent.isButtonDown(1))
                System.out.println("wwwwwwwww");
        });

        scene.addMouseDraggedListener(glMouseEvent -> {
            System.out.println(glMouseEvent.getX());
        });

        scene.addKeyPressedListener(glKeyEvent -> {
            switch (glKeyEvent.getKey()) {
                case GLKeyEvent.W -> {
                    rect.move(new Vector3f(0, 1, 0));
                    //rect.rotate(Vector3f.add(rect.getRotation(), new Vector3f(0,0,90)), new Vector3f(rect.getPosition().getX(), rect.getPosition().getY(), rect.getPosition().getZ()));
                }
                case GLKeyEvent.A -> rect.move(new Vector3f(-1, 0, 0));
                case GLKeyEvent.S -> rect.move(new Vector3f(0, -1, 0));
                case GLKeyEvent.D -> rect.move(new Vector3f(1, 0, 0));
            }
            if (glKeyEvent.getKey() == GLKeyEvent.A)
                System.out.println("A");
        });

        scene.addKeyPressedListener(glKeyEvent -> {
            if (glKeyEvent.getKey() == GLKeyEvent.B)
                System.out.println("B");
        });

        scene.start();*/
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
