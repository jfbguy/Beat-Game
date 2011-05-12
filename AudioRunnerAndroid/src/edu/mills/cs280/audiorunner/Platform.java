package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Expanded version of collidable with addition of setting bounds for multiple paltform sizes
 * 
 * @author jklein
 *
 */
public class Platform extends Collidable{
	
	/**
	 * Constructor
	 * 
	 * 
	 * @param x x position of platform
	 * @param y y position of platform
	 * @param width width of platform
	 * @param height height of platform
	 * @param texture texture of platform
	 * @param pixmap pixmap of platform
	 */
	public Platform(float x, float y, float width, float height, Texture texture, Pixmap pixmap){
		this.loadTexture(pixmap, texture);
		this.setBounds(x, y, width, height);
	}
	
}
