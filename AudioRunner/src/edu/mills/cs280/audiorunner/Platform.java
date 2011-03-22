package edu.mills.cs280.audiorunner;

public class Platform extends Collidable{
	
	public Platform(float x, float y, float width, float height, String texture){
		this.loadTexture(texture);
		this.setBounds(x, y, width, height);
	}
	
}
