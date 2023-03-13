package org.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.util.Objects;

public class EmptyMaterial extends Material{
    public EmptyMaterial() {
        super("/textures/empty.png");
    }

    public void create() {
        Material material = textures.get(path);
        if(material == null){
            try{
                this.texture = TextureLoader.getTexture(path.split("[.]")[1], Objects.requireNonNull(Material.class.getResourceAsStream(path)), GL11.GL_NEAREST);
                this.width = texture.getWidth();
                this.height = texture.getHeight();
                this.textureID = texture.getTextureID();
                textures.put(path, this);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Can't find texture at " + path);
                System.exit(1);
            }
        }else {
            this.textureID = material.getTextureID();
            this.width = material.getWidth();
            this.height = material.getHeight();
        }


        /*try {
            this.texture = TextureLoader.getTexture(path.split("[.]")[1], Objects.requireNonNull(Material.class.getResourceAsStream(path)), GL11.GL_NEAREST);
            this.width = texture.getWidth();
            this.height = texture.getHeight();
            this.textureID = texture.getTextureID();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Can't find texture at " + path);
        }*/
    }
}
