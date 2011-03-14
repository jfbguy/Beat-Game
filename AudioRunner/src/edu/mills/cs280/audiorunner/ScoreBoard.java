package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreBoard {
	private final int FONTSIZE = 64;				//Size of Font in Texture
	private final float SCREENPERCENTAGE = 0.1f;	//Percentage of screen to draw score on

	private Texture mNumbersTexture;
	private int mScore;
	private float mDrawSize;	//Size to draw font on screen

	public ScoreBoard() {
		mNumbersTexture = new Texture(Gdx.files.internal("data/numbers.png"));
		mScore = 0;
		mDrawSize = Gdx.graphics.getHeight() * SCREENPERCENTAGE;
	}

	/**
	 * Draws the score at top right of screen
	 * 
	 * @param  SpriteBatch, Draw within the current SpriteBatch
	 */
	public void draw(SpriteBatch spriteBatch){
		String mScoreArray = Integer.toString(mScore);

		spriteBatch.begin();
		for(int i = 0; i < mScoreArray.length(); i++){
			char tempChar = mScoreArray.charAt(i);
			spriteBatch.draw(mNumbersTexture,
				(float) (Gdx.graphics.getWidth()-mDrawSize*(mScoreArray.length()-i)),	//Draw at X position
				(float) (Gdx.graphics.getHeight()-mDrawSize),							//Draw at Y position
				mDrawSize,mDrawSize,													//Size of Number
				Character.digit(tempChar,10)*FONTSIZE, 0,								//Get part of texture
				FONTSIZE, FONTSIZE,														//Size of gotten part
				false,false);															//Flip X, Flip Y
		}
		spriteBatch.end();
	}

	/**
	 * Adds some points to the score
	 * 
	 * @param  Integer, Amount of points to add
	 */
	public void addScore(int points){
		mScore += points;
	}

}
