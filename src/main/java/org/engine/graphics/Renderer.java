package org.engine.graphics;

import org.engine.shapes.Circle;
import org.engine.shapes.Dot;
import org.engine.shapes.Line;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import org.engine.io.Window;
import org.engine.maths.Matrix4f;
import org.engine.cameras.Camera;
import org.engine.objects.GameObject;
import org.lwjgl.system.MemoryUtil;

public class Renderer {
	private Shader shader;
	private Window window;
	
	public Renderer(Window window, Shader shader) {
		this.shader = shader;
		this.window = window;
	}
	
	public void renderMesh(GameObject object, Camera camera) {
		GL30.glBindVertexArray(object.getMesh().getVAO());//
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, object.getMesh().getIBO());

		//shader.setUniform("texture0", object.getMesh().getMaterial().getTextureID());
		//if(object.isTextured()) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, object.getMesh().getMaterial().getTextureID());
		//GL13.glHint(GL13.GL_PERSPECTIVE_CORRECTION_HINT, GL13.GL_NICEST);

		//GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_MIN_FILTER, GL13.GL_NEAREST);
		//GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_MAG_FILTER, GL13.GL_NEAREST);
		//GL30.glGenerateMipmap(object.getMesh().getMaterial().getTextureID());
		//}
		//activeTextures(object);

		shader.bind();
		var m = shader.setUniform("model", Matrix4f.transform(object.getPosition(), object.getRotation(), object.getScale()));
		var v = shader.setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
		var p = shader.setUniform("projection", window.getProjectionMatrix());
		var f = shader.setUniform("forward", Matrix4f.translate(object.getParent().getPosition().neg()));
		var b = shader.setUniform("back", Matrix4f.translate(object.getParent().getPosition()));
		if(object instanceof Circle) {
			GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, object.getMesh().getVertices().length+2);
		} else if (object instanceof Line) {
			GL11.glDrawArrays(GL11.GL_LINES, 0, 2);
		} else if (object instanceof Dot) {
			GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		} else
			GL11.glDrawElements(GL11.GL_TRIANGLES, object.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);

		shader.unbind();

		MemoryUtil.memFree(m);
		MemoryUtil.memFree(v);
		MemoryUtil.memFree(p);
		MemoryUtil.memFree(f);
		MemoryUtil.memFree(b);

		//GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
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