package org.engine.objects;

import org.engine.Resize;
import org.engine.graphics.Mesh;
import org.engine.maths.Vector3f;
import org.engine.utils.Color;

public abstract class GameObject extends EngineObject implements Resize {
	private float width;
	private float height;
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	private Vector3f center;
	private Vector3f normal;
	private Mesh mesh;
	private ShapeObject parent;
	private Color color;
	private boolean isChanged;

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		if(this.mesh != null)
			this.mesh.setColor(color);
		this.isChanged = true;
	}

	public GameObject(){
		this.isChanged = false;
		this.normal = new Vector3f(0,0,0);
	}

	public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
		this();
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.mesh = mesh;
		this.center = new Vector3f(0,0,0);
	}
	public void ini(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.mesh = mesh;
		this.center = new Vector3f(0,0,0);
		this.isChanged = true;
	}

	public void build(){
		this.isChanged = false;
		//this.normal = new Vector3f(0,0,0);
		this.mesh.create();
	}

	public void build(Vector3f position, Vector3f rotation, Vector3f scale, Vector3f normal, Mesh mesh) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.normal = normal;
		this.mesh = mesh;
		this.center = new Vector3f(0,0,0);
		this.mesh.create();
	}

	public void build(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.normal = new Vector3f(0,0,0);
		this.mesh = mesh;
		this.center = new Vector3f(0,0,0);
		this.mesh.create();
	}

	public void destroy(){
		this.mesh.destroy();
	}
	
	/*public void update() {
		position.setZ(position.getZ() - 0.05f);
	}*/

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public Mesh getMesh() {
		return mesh;
	}
	public void setRotation(Vector3f rotation){
		this.rotation = Vector3f.add(this.rotation, rotation);
	}
	public void setPosition(Vector3f position){
		this.position = Vector3f.add(this.position, position);
	}
	public void setPositionStraght(Vector3f position){
		this.position = position;
	}
	public void setCenter(Vector3f center){
		this.center = center;
	}
	public Vector3f getCenter() {
		return center;
	}

	public ShapeObject getParent() {
		return parent;
	}

	public void setParent(ShapeObject parent) {
		this.parent = parent;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean changed) {
		isChanged = changed;
	}
}