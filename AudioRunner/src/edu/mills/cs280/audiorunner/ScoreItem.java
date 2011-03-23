package edu.mills.cs280.audiorunner;

public class ScoreItem extends Collidable{
	private int points;
	
	public ScoreItem(float x, float y, float width, float height, String texture, int points){
		this.loadTexture(texture);
		this.setBounds(x, y, width, height);
		this.points = points;
	}
	
	public void scored(ScreenHandler screenHandler, ScoreBoard scoreBoard){
		scoreBoard.addFloaterScore((int)getX()-screenHandler.getWorldPosition(),(int)getY(),points);
	}
}
