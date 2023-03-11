package org.engine.graphics;

import org.engine.maths.Vector2f;
import org.engine.maths.Vector3f;
import org.engine.utils.Color;

public class Vertex {
	private Vector3f position;
	private Vector2f textureCoord;
	private Vector3f color;
	
	public Vertex(Vector3f position, Vector2f textureCoord, Vector3f color) {
		this.position = position;
		this.textureCoord = textureCoord;
		this.color = color;
	}

	public void setColor(Color color){
		this.color = new Vector3f(color.getRed(),color.getGreen(),color.getBlue());
	}

	public Vertex(Vector3f position, Vector2f textureCoord) {
		this.position = position;
		this.textureCoord = textureCoord;
		this.color = new Vector3f(0,0,0);
	}

	public Vector3f getPosition() {
		return position;
	}
	public Vector2f getTextureCoord() {
		return textureCoord;
	}
	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
}