package org.engine.objects;

import org.engine.graphics.Mesh;
import org.engine.maths.Vector3f;
import org.engine.utils.Color;

public abstract class GameObject extends EngineObject implements Resize {
	private float width;
	private float height;
	//private Vector3f position;
	//private Vector3f rotation;
	private Vector3f scale;
	//private Vector3f center;
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
		this.setNormal(new Vector3f(0,0,0));
	}

	public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
		this();
		this.setPosition(position);
		this.setRotation(rotation);
		this.setScale(scale);
		this.mesh = mesh;
		this.setCenter(new Vector3f(0,0,0));
	}
	public void ini(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
		this.setPosition(position);
		this.setRotation(rotation);
		this.setScale(scale);
		this.mesh = mesh;
		this.computeCenter();
		this.isChanged = true;
	}

	public void ini(Vector3f position, Vector3f rotation, Vector3f scale, Vector3f normal, Mesh mesh) {
		this.setPosition(position);
		this.setRotation(rotation);
		this.setScale(scale);
		this.setNormal(normal);
		this.mesh = mesh;
		this.computeCenter();
		this.isChanged = true;
	}

	public void build(){
		this.isChanged = false;
		//this.normal = new Vector3f(0,0,0);
		this.mesh.create();
	}

	public void build(Vector3f position, Vector3f rotation, Vector3f scale, Vector3f normal, Mesh mesh) {
		this.setPosition(position);
		this.setRotation(rotation);
		this.setScale(scale);
		this.setNormal(normal);
		this.mesh = mesh;
		this.setCenter(new Vector3f(0,0,0));
		this.mesh.create();
	}

	public void build(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
		this.setPosition(position);
		this.setRotation(rotation);
		this.setScale(scale);
		this.setNormal(new Vector3f(0,0,0));
		this.mesh = mesh;
		this.setCenter(new Vector3f(0,0,0));
		this.mesh.create();
	}

	private void computeCenter(){
		float sumX=0;
		float sumY=0;
		float sumZ=0;
		var vertices = this.getMesh().getVertices();
		for(var vertex: vertices) {
			sumX += this.getPosition().getX() + vertex.getPosition().getX();//compute sum centers X
			sumY += this.getPosition().getY() + vertex.getPosition().getY();//compute sum centers Y
			sumZ += this.getPosition().getZ() + vertex.getPosition().getZ();//compute sum centers Z
		}
		this.setCenter(new Vector3f(sumX/vertices.length, sumY/vertices.length, sumZ/vertices.length));//ini center
	}

	public void destroy(){
		this.mesh.destroy();
	}

	public Vector3f getScale() {
		return scale;
	}

	public Mesh getMesh() {
		return mesh;
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

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
}