package org.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.util.Objects;

public class EmptyMaterial extends Material{
    public EmptyMaterial(String path) {
        super(path);
    }

    public void create() {
        try {
            this.texture = TextureLoader.getTexture(path.split("[.]")[1], Objects.requireNonNull(Material.class.getResourceAsStream(path)), GL11.GL_NEAREST);
            this.width = texture.getWidth();
            this.height = texture.getHeight();
            this.textureID = texture.getTextureID();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Can't find texture at " + path);
        }
    }
}
