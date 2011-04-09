package edu.mills.cs280.audiorunner;

public class ScoreItem extends Collidable{
	private int points;
	
	public ScoreItem(float x, float y, float width, float height, String texture, int points){
		this.loadTexture(texture);
		this.setBounds(x, y, width, height);
		this.points = points;
	}
	
	public void scored(ScoreBoard scoreBoard){
		scoreBoard.addFloaterScore((int)getX(),(int)getY(),points);
	}
}
