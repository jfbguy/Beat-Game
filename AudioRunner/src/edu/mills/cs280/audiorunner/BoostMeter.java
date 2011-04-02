package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BoostMeter {
	private final int FULL_BAR_WAIT = 120;
	private final int REDUCTION_RATE = 10;
	private final float REDUCTION_AMOUNT = .01f;
	private final float XPOS = Gdx.graphics.getWidth()*.01f;
	private final float YPOS = Gdx.graphics.getHeight()*.5f;
	private final int WIDTH = (int) (Gdx.graphics.getHeight()*.1);
	private final int HEIGHT = (int) (Gdx.graphics.getHeight()*.5);

	Texture mBoostTexture;
	Texture mBorderTexture;
	private float mValue;
	private int mCoolDown = 0;
	private int mReduction = 0;

	public BoostMeter(){
		mValue = 0;
		mBorderTexture = new Texture(Gdx.files.internal("data/boostmeterborder.png"));
		mBoostTexture = new Texture(Gdx.files.internal("data/yellow.png"));
	}

	public void updateBoost(){
		if(mCoolDown > 0){
			mCoolDown--;
		}
		else if(mValue != 0){
			if(mReduction == 0){
				mValue *= (1-REDUCTION_AMOUNT);
				mReduction = REDUCTION_RATE;
			}
			else{
				mReduction--;
			}
		}
	}

	public void addBoost(int value){
		mValue += value;
		if(mValue > 100){
			mValue = 100;
		}
		if(mValue == 100){
			mCoolDown = FULL_BAR_WAIT;
		}
		mReduction = REDUCTION_RATE;
	}

	public void draw(SpriteBatch spriteBatch){
		spriteBatch.begin();
		spriteBatch.draw(mBoostTexture,
				XPOS,YPOS,
				WIDTH,HEIGHT*mValue/100,
				0, 0,
				mBorderTexture.getWidth(), mBorderTexture.getHeight(),
				false,false);
		spriteBatch.draw(mBorderTexture,
				XPOS,YPOS,
				WIDTH,HEIGHT,
				0, 0,
				mBorderTexture.getWidth(), mBorderTexture.getHeight(),
				false,false);
		spriteBatch.end();
	}
}
