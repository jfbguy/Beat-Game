package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BoostMeter {
	private final int FULL_BAR_WAIT = (int) (TimeHandler.FRAMERATE * 2);
	private final float REDUCTION_AMOUNT = .01f;
	private final float XPOS = Gdx.graphics.getWidth()*.01f;
	private final float YPOS = Gdx.graphics.getHeight()*.5f;
	private final int WIDTH = (int) (Gdx.graphics.getHeight()*.1);
	private final int HEIGHT = (int) (Gdx.graphics.getHeight()*.5);
	private final int MAX_METER_VALUE = 100;
	private final int REDUCTION_RATE = (int) (TimeHandler.FRAMERATE/5);

	Texture mBoostTexture;
	Texture mBorderTexture;
	private float mValue;
	private float mCoolDown = 0.0f;
	private float mReduction = 0.0f;

	public BoostMeter(){
		mValue = 0.0f;
		mBorderTexture = new Texture(Gdx.files.internal("data/boostmeterborder.png"));
		mBoostTexture = new Texture(Gdx.files.internal("data/yellow.png"));
	}

	public void updateBoost(){
		if(mCoolDown > 0){
			mCoolDown -= TimeHandler.getTransitionScale();
		}
		else if(mValue > 0){
			if(mReduction <= 0){
				mValue -= MAX_METER_VALUE*REDUCTION_AMOUNT*TimeHandler.getTransitionScale();
				mReduction = REDUCTION_RATE;
			}
			else{
				mReduction -= TimeHandler.getTransitionScale();
			}
		}
	}

	public void addBoost(int value){
		mValue += value;
		if(mValue > MAX_METER_VALUE){
			mValue = MAX_METER_VALUE;
		}
		mCoolDown = FULL_BAR_WAIT;
		mReduction = REDUCTION_RATE;
	}
	
	public float getBoost(){
		return mValue;
	}
	
	public float boost(){
		float rVal;
		if(mValue == MAX_METER_VALUE){
			rVal = 3.0f;
		}
		else{
			rVal = 1.0f + mValue / MAX_METER_VALUE;
		}
		mValue = 0;
		
		return rVal;
	}

	public void draw(SpriteBatch spriteBatch){
		spriteBatch.begin();
		spriteBatch.draw(mBoostTexture,
				XPOS,YPOS,
				WIDTH,HEIGHT*mValue/MAX_METER_VALUE,
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
