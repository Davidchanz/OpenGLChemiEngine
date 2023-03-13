package org.engine.graphics;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Material {
    protected String path;
	protected Texture texture;
	protected float width, height;
	protected int textureID;
	
	public Material(String path) {
		this.path = path;
	}
	public static EmptyMaterial getEmpty(){
		return new EmptyMaterial("/textures/empty.png");
	}
	
	public void create() {
		/*try (FileInputStream f = new FileInputStream(path)){
			GL30.glGenTextures(1, textureIDs, 0); // Generate texture-ID array

			GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureIDs[0]);   // Bind to texture ID
			// Set up texture filters
			GL30.glTexParameterf(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
			GL30.glTexParameterf(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);

			// Construct an input stream to texture image "res\drawable\nehe.png"
			InputStream istream = context.getResources().openRawResource(R.drawable.nehe);
			Bitmap bitmap;
			try {
				// Read and decode input as bitmap
				bitmap = BitmapFactory.decodeStream(istream);
			} finally {
				try {
					istream.close();
				} catch (IOException e) {
				}
			}
		}catch (Exception e){
			System.out.println(Arrays.toString(e.getStackTrace()));
		}*/

		try (FileInputStream f = new FileInputStream(path)){
			this.texture = TextureLoader.getTexture(path.split("[.]")[1], Objects.requireNonNull(f), GL11.GL_NEAREST);
			this.width = texture.getWidth();
			this.height = texture.getHeight();
			this.textureID = texture.getTextureID();
		} catch (Exception e) {
			System.err.println("Can't find texture at " + path);
			var material = getEmpty();
			this.textureID = material.getTextureID();
			this.width = material.getWidth();
			this.height = material.getHeight();
		}
	}
	
	public void destroy() {
		GL13.glDeleteTextures(textureID);
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getTextureID() {
		return textureID;
	}
}