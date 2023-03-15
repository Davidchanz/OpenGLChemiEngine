package org.engine.cameras;

import org.engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

public class StaticCamera extends Camera{

    public StaticCamera(Vector3f position, Vector3f rotation) {
        super(position, rotation);
        this.is3DEnabled = false;
    }

    public StaticCamera(){
        this(cameraCenter, new Vector3f(0,0,0));
    }

    @Override
    public void update() {

    }
}
