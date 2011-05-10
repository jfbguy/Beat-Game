package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Platform extends Collidable{
	
	public Platform(float x, float y, float width, float height, Texture texture, Pixmap pixmap){
		this.loadTexture(pixmap, texture);
		this.setBounds(x, y, width, height);
	}
	
}
