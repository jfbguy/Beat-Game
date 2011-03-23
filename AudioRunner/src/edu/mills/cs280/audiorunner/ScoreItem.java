package edu.mills.cs280.audiorunner;

public class ScoreItem extends Collidable{

	public ScoreItem(float x, float y, float width, float height, String texture){
		this.loadTexture(texture);
		this.setBounds(x, y, width, height);
	}
	
	public void scored(ScreenHandler screenhandler){
		
	}
}
