package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class ScoreItem extends Collidable{
	private int points;
	
	public ScoreItem(float x, float y, float width, float height, Texture texture, Pixmap pixmap, int points){
		this.loadTexture(pixmap, texture);
		this.setBounds(x, y, width, height);
		this.points = points;
	}
	
	public void scored(ScoreBoard scoreBoard){
		scoreBoard.addFloaterScore((int)getX(),(int)getY(),points);
	}
}
