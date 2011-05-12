package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * The platform class. Extends collidable.  COnstructor only.
 * @author Trevor Adams
 *
 */
public class Platform extends Collidable{
	
	public Platform(float x, float y, float width, float height, Texture texture, Pixmap pixmap){
		this.loadTexture(pixmap, texture);
		this.setBounds(x, y, width, height);
	}
	
}
