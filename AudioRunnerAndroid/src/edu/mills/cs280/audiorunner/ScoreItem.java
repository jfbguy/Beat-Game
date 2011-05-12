package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Score items can be touched to add more score to the palyer's score
 * 
 * @author jklein
 *
 */
public class ScoreItem extends Collidable{
	private int points;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param texture
	 * @param pixmap
	 * @param points
	 */
	public ScoreItem(float x, float y, float width, float height, Texture texture, Pixmap pixmap, int points){
		this.loadTexture(pixmap, texture);
		this.setBounds(x, y, width, height);
		this.points = points;
	}
	
	/**
	 * Adds floater points X and Y to scoreBoard
	 * @param scoreBoard the location of points 
	 */
	public void scored(ScoreBoard scoreBoard){
		scoreBoard.addFloaterScore((int)getX(),(int)getY(),points);
	}
}
