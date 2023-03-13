package org.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import org.engine.io.Window;
import org.engine.maths.Matrix4f;
import org.engine.objects.Camera;
import org.engine.objects.GameObject;

public class Renderer {
	private Shader shader;
	private Window window;
	
	public Renderer(Window window, Shader shader) {
		this.shader = shader;
		this.window = window;
	}
	
	public void renderMesh(GameObject object, Camera camera) {
		GL30.glBindVertexArray(object.getMesh().getVAO());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, object.getMesh().getIBO());

		//shader.setUniform("texture0", object.getMesh().getMaterial().getTextureID());
		//if(object.isTextured()) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, object.getMesh().getMaterial().getTextureID());
		//}
		//activeTextures(object);

		shader.bind();
		shader.setUniform("model", Matrix4f.transform(object.getPosition(), object.getRotation(), object.getScale()));
		shader.setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
		shader.setUniform("projection", window.getProjectionMatrix());
		shader.setUniform("forward", Matrix4f.translate(object.getParent().getPosition().neg()));
		shader.setUniform("back", Matrix4f.translate(object.getParent().getPosition()));
		GL11.glDrawElements(GL11.GL_TRIANGLES, object.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);
		shader.unbind();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	public void activeTextures(GameObject object){
		var vertices = object.getMesh().getVertices();
		/*for (int i = 0; i < vertices.length; i+=4) {
			shader.setUniform("texture"+i, vertices[i].getMaterial().getTextureID());
			GL13.glActiveTexture(GL13.GL_TEXTURE0+i);
			GL13.glBindTexture(GL11.GL_TEXTURE_2D, vertices[i].getMaterial().getTextureID());
		}*/
		/*shader.setUniform("texture4", vertices[4].getMaterial().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, vertices[4].getMaterial().getTextureID());

		shader.setUniform("texture20", vertices[20].getMaterial().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE20);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, vertices[20].getMaterial().getTextureID());*/
	}
}