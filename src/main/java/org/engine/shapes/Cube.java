package org.engine.shapes;

import org.engine.maths.Vector3f;
import org.engine.objects.ShapeObject;
import org.engine.utils.Color;
import org.engine.utils.Face;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**Class for 3DObject*/
public class Cube extends ShapeObject {
    /**object's plane set*/
    public Cube(String name, int id, Vector3f position){
        super(name, id);
        setPosition(position);
        loadShape("src/main/resources/objects/1.obj");
    }

    /**Function for find center of all plane*/
    public void findCenter(){
        float sumX=0;
        float sumY=0;
        float sumZ=0;
        for(var i: body){
            sumX+= i.getCenter().getX() /*+ i.position.x*/;
            sumY+= i.getCenter().getY() /*+ i.position.y*/;
            sumZ+= i.getCenter().getZ() /*+ i.position.z*/;
        }
        setCenter(new Vector3f(sumX/this.body.size(), sumY/this.body.size(), sumZ/this.body.size()));//todo compute
    }
    public void changeColor(Color color){
        for (var i: body){
            Arrays.stream(i.getMesh().getVertices()).forEach((x)->{x.setColor(color);});
            i.getMesh().create();
        }
    }

    /**load object from .obj file*/
    public void loadShape(String file) {
        try {
            FileReader fr = new FileReader(file);
            Scanner sc = new Scanner(fr);
            String buff = "";

            ArrayList<Vector3f> vertex = new ArrayList<>();
            ArrayList<Vector3f> normal = new ArrayList<>();
            ArrayList<Face[]> Faces = new ArrayList<>();
            while(sc.hasNext()){
                buff = sc.nextLine();
                if (buff.startsWith("v") && !buff.startsWith("vt") && !buff.startsWith("vn")) {
                    //read vertices
                    Scanner buffSc = new Scanner(buff);
                    buffSc.useDelimiter(" ");
                    buffSc.next();
                    float X = buffSc.nextFloat();
                    float Y = buffSc.nextFloat();
                    float Z = buffSc.nextFloat();
                    Vector3f newPoint = new Vector3f(X,Y,Z);
                    vertex.add(newPoint);
                } else if (buff.startsWith("vn") && !buff.startsWith("vt")) {
                    //read normal
                    Scanner buffSc = new Scanner(buff);
                    buffSc.useDelimiter(" ");
                    buffSc.next();
                    double X = buffSc.nextDouble();
                    double Y = buffSc.nextDouble();
                    double Z = buffSc.nextDouble();
                    Vector3f newNormal = new Vector3f((float) X,(float)Y,(float)Z);
                    normal.add(newNormal);
                } else if (buff.startsWith("vt")) {

                } else if(buff.startsWith("f")){
                    //read face
                    Scanner buffSc = new Scanner(buff);
                    buffSc.useDelimiter(" ");
                    buffSc.next();
                    Face[] face = new Face[4];
                    String f = "";
                    for(int i = 0; i < 4; ++i) {
                        if(buffSc.hasNext()){
                            f = buffSc.next();
                            Scanner fSc = new Scanner(f);
                            fSc.useDelimiter("/");
                            int vertId = fSc.nextInt();
                            fSc.nextInt();
                            int normId = fSc.nextInt();
                            face[i] = new Face(vertId - 1, -1, normId - 1);
                        }
                        else {
                            Scanner fSc = new Scanner(f);
                            fSc.useDelimiter("/");
                            int vertId = fSc.nextInt();
                            fSc.nextInt();
                            int normId = fSc.nextInt();
                            face[i] = new Face(vertId - 1, -1, normId - 1);
                        }
                    }
                    Faces.add(face);
                }
            }
            int q = 0;
            Color c = Color.MAGENTA;
            String s = "MAGENTA";
            for(var face: Faces){
                //set plane from face
                Vector3f d1 = vertex.get(face[0].vertex);
                Vector3f d2 = vertex.get(face[1].vertex);
                Vector3f d3 = vertex.get(face[2].vertex);
                Vector3f d4 = vertex.get(face[3].vertex);
                Vector3f norm = normal.get(face[0].normal);
                if(q == 0) {
                    c = Color.BLUE;
                    s = "BLUE";
                }
                if(q == 1) {
                    c = Color.RED;
                    s = "RED";
                }
                if(q == 2) {
                    s = "GREEN";
                    c = Color.GREEN;
                }
                if(q == 3) {
                    s = "YELLOW";
                    c = Color.YELLOW;
                }
                if(q == 4) {
                    s = "WHITE";
                    c = Color.WHITE;
                }
                if(q == 5) {
                    s = "ORANGE";
                    c = Color.MAGENTA;
                }
                var r = new Rectangle(d1, d2, d3, d4, new Vector3f(getPosition()), norm, c);
                //System.out.println(name + " " + r.getCenter());
                this.add(r);
                q++;
            }
            findCenter();
            //findCenter();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void setPositionStraight(Vector3f position){
        this.setPosition(position);//move position on dir
        //this.center = Vector3f.add(this.center, dir);//move center on dir TODO
        for(var i: this.body){
            i.setPosition(position);//move all AbstractShapes
        }
    }
}
