package org.engine.cameras;

import org.engine.graphics.Shader;
import org.engine.io.Input;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;
import org.engine.objects.ShapeObject;
import org.lwjgl.glfw.GLFW;

public class ThirdPerson3DCamera extends Camera {
    private ShapeObject targetObject;

    public ThirdPerson3DCamera(Vector3f position, Vector3f rotation) {
        super(position, rotation);
        this.is3DEnabled = false;
    }

    public ThirdPerson3DCamera(Vector3f position, Vector3f rotation, ShapeObject targetObject) {
        this(position, rotation);
        this.targetObject = targetObject;
    }

    public ThirdPerson3DCamera(){
        this(cameraCenter, new Vector3f(0,0,0));
    }

    @Override
    public void update() {
        if(this.targetObject == null)
            return;
        this.newMouseX = Input.getMouseX();
        this.newMouseY = Input.getMouseY();

        float dx = (float) (this.newMouseX - this.oldMouseX);
        float dy = (float) (this.newMouseY - this.oldMouseY);

        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {//TODO predicat
            this.rotateCamera(dx, dy);
        }
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            this.zoomCamera(dy);
        }

        //System.out.println(distance);

        float horizontalDistance = (float) (this.distance * Math.cos(Math.toRadians(this.verticalAngle)));
        float verticalDistance = (float) (this.distance * Math.sin(Math.toRadians(this.verticalAngle)));

        float xOffset = (float) (horizontalDistance * Math.sin(Math.toRadians(-this.horizontalAngle)));
        float zOffset = (float) (horizontalDistance * Math.cos(Math.toRadians(-this.horizontalAngle)));

        this.position.set(this.targetObject.getPosition().getX() + xOffset, this.targetObject.getPosition().getY() - verticalDistance, this.targetObject.getPosition().getZ() + zOffset);

        this.rotation.set(this.verticalAngle, -this.horizontalAngle, 0);

        this.oldMouseX = this.newMouseX;
        this.oldMouseY = this.newMouseY;
    }

    private void zoomCamera(float value){
        if (this.distance > 0) {
            this.distance += value * this.mouseSensitivity / 4;
        } else {
            this.distance = 0.1f;
        }
    }

    private void rotateCamera(float dx, float dy){
        this.verticalAngle -= dy * this.mouseSensitivity;
        this.horizontalAngle += dx * this.mouseSensitivity;
    }

    public ShapeObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(ShapeObject targetObject) {
        this.targetObject = targetObject;
    }
}
