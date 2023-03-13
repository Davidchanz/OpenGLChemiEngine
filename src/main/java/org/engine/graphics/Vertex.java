package org.engine.graphics;

import org.engine.maths.Vector2f;
import org.engine.maths.Vector3f;
import org.engine.utils.Color;

public class Vertex {
	private Vector3f position;
	private Vector2f textureCoord;
	private Color color;
	
	public Vertex(Vector3f position, Vector2f textureCoord, Color color) {
		this.position = position;
		this.textureCoord = textureCoord;
		this.color = color;
	}

	public void setColor(Color color){
		this.color = new Color(color.getRed(),color.getGreen(),color.getBlue(), color.getAlpha());
	}

	public Vertex(Vector3f position, Vector2f textureCoord) {
		this.position = position;
		this.textureCoord = textureCoord;
		this.color = new Color(0,0,0, 0);
	}

	public Vector3f getPosition() {
		return position;
	}
	public Vector2f getTextureCoord() {
		return textureCoord;
	}
	public Color getColor() {
		return color;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
}