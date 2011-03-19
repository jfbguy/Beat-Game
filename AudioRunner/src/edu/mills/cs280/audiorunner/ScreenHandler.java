package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

public class ScreenHandler{
	private static final int SPEED = 5;
	private static final int DEFAULT_LEVEL_HEIGHT = 60;
	private static final float[] PARALLAX = {1.5f,1.0f,.4f,.3f,.02f};
	private static final int NUM_OF_mTextures = 4;
	private static final int MOUNTAIN = 0;	//Texture Constants, each needs to be different!
	private static final int SUN = 1;
	private static final int POWERLINES = 2;
	private static final int GROUND = 3;


	ImmediateModeRenderer mRenderer;
	private int mDrawStarter;
	private int mWorldPosition;
	private int[] mGroundLevels;
	private Texture[] mTextures;
	private SpriteLayer[] mSpriteLayers;

	/**
	 * Constructor
	 */
	public ScreenHandler(int numOfLayers){
		mDrawStarter = 0;
		mWorldPosition = 0;
		mSpriteLayers = new SpriteLayer[numOfLayers];
		
		//Declare all SpriteLayers
		for(int i = 0; i < mSpriteLayers.length; i++){
			mSpriteLayers[i] = new SpriteLayer(PARALLAX[i]);
		}

		//load default level start
		mGroundLevels = new int[Gdx.graphics.getWidth()];
		for(int i = 0; i < mGroundLevels.length; i++){
			mGroundLevels[i] = DEFAULT_LEVEL_HEIGHT;
		}

		//load sprites
		mTextures = new Texture[NUM_OF_mTextures];
		mTextures[MOUNTAIN] = new Texture(Gdx.files.internal("data/mountain.png"));
		mTextures[SUN] = new Texture(Gdx.files.internal("data/sun.png"));
		mTextures[POWERLINES] = new Texture(Gdx.files.internal("data/powerlines.png"));
		mTextures[GROUND] = new Texture(Gdx.files.internal("data/gradient_BW_1D.png"));

		//************************************************************************************************
		//************** DEBUG level load ****************************************************************
		///***** Will be replaced with what the audio algorithm does *************************************
		//************************************************************************************************
		Vector2 temp = new Vector2();
		for(int i = 0; i < 80; i++){
			temp.set(i*mTextures[POWERLINES].getWidth(),0);
			Sprite tSprite = new Sprite(mTextures[POWERLINES]);
			tSprite.setPosition(temp.getX(), temp.getY());
			mSpriteLayers[0].put(temp.getX(),tSprite);
		}
		
		for(int i = 0; i < 20; i++){
			temp.set(i*400,0);
			Sprite tSprite = new Sprite(mTextures[MOUNTAIN]);
			tSprite.setPosition(temp.getX(), temp.getY());
			mSpriteLayers[2].put(temp.getX(),tSprite);
		}
		temp.set(400,100);
		Sprite tSprite = new Sprite(mTextures[SUN]);
		tSprite.setPosition(temp.getX(), temp.getY());
		mSpriteLayers[4].put(temp.getX(),tSprite);
		//************************************************************************************
		
		
		//Load start of Level Layers
		for(int i = 0; i < mSpriteLayers.length; i++){
			mSpriteLayers[i].loadStart(mWorldPosition);
		}
	}

	/**
	 * Updates current screen with level data from processed audio.
	 * For the moment, it just updatesthe world position.
	 * Need to add added platforms, sprites, change ground, etc
	 */
	public void updateScreen(){	//Updates level depending on music and how player is doing
		mWorldPosition += SPEED;
		
	}
	
	/**
	 * Draws Everything on Screen
	 * 
	 * @param
	 */
	
	public void draw(SpriteBatch spriteBatch, Player player){
		spriteBatch.begin();
		
		//Draw first half of layers rounded down
		for(int i = mSpriteLayers.length-1; i >= mSpriteLayers.length/2; i--){
			mSpriteLayers[i].draw(spriteBatch,mWorldPosition);
		}
		
		//Draw Ground
		drawGround(spriteBatch);
		
		//Draw Player
		spriteBatch.draw(player.getTexture(), player.getX(), player.getY(),
				player.getSpriteX(), player.getSpriteY(),
				Player.getSize(), Player.getSize());
		
		//Draw second half of layers rounded down
		for(int i = mSpriteLayers.length/2-1; i >= 0; i--){
			mSpriteLayers[i].draw(spriteBatch,mWorldPosition);
		}
		
		spriteBatch.end();
	}

	/**
	 * Draws Ground, Player runs ontop of
	 * 
	 * @param  SpriteBatch, Draw within the current SpriteBatch
	 */
	private void drawGround(SpriteBatch spriteBatch){
		Sprite sprite = new Sprite(mTextures[GROUND]);
		int posX = 0;
		for(int i = 0; i < Gdx.graphics.getWidth(); i++) {
			posX = (mDrawStarter+i)%Gdx.graphics.getWidth();
			sprite.setPosition(i, 0);
			sprite.setBounds(i, 0, 1, mGroundLevels[posX]);
			sprite.draw(spriteBatch);
		}
	}
	
	/**
	 * Draws Ground, Player runs ontop of
	 * 
	 * @return int Current Speed of player.  Pretty much speed tyhe level is scrolling
	 */
	public static int getSpeed(){
		return SPEED;
	}

}
