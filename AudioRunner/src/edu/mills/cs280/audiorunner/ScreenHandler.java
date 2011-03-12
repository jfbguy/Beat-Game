package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

public class ScreenHandler{
	private static final int SPEED = 5;
	private static final int DEFAULT_LEVEL_HEIGHT = 60;
	private static final float FOREPARALLAX = 1.5f;
	private static final float BACKPARALLAX = .3f;
	private static final float FARPARALLAX = 0.02f;
	private static final int NUM_OF_TEXTURES = 4;
	private static final int MOUNTAIN = 0;	//Texture Constants, each needs to be different!
	private static final int SUN = 1;
	private static final int POWERLINES = 2;
	private static final int GROUND = 3;


	ImmediateModeRenderer renderer;
	private int drawStarter;
	private int worldPosition;
	private int[] groundLevels;
	private Texture[] textures;
	private SpriteLayer foreGround;  //<X Position in Level, texture identifier>
	//private SpriteLayer midGround; 
	private SpriteLayer backGround;
	private SpriteLayer farGround;

	/**
	 * Constructor
	 */
	public ScreenHandler(){
		drawStarter = 0;
		worldPosition = 0;

		//load default level start
		groundLevels = new int[Gdx.graphics.getWidth()];
		for(int i = 0; i < groundLevels.length; i++){
			groundLevels[i] = DEFAULT_LEVEL_HEIGHT;
		}

		//load sprites
		textures = new Texture[NUM_OF_TEXTURES];
		textures[MOUNTAIN] = new Texture(Gdx.files.internal("data/mountain.png"));
		textures[SUN] = new Texture(Gdx.files.internal("data/sun.png"));
		textures[POWERLINES] = new Texture(Gdx.files.internal("data/powerlines.png"));
		textures[GROUND] = new Texture(Gdx.files.internal("data/gradient_BW_1D.png"));

		foreGround = new SpriteLayer(FOREPARALLAX);
		//midGround = new SpriteLayer();
		backGround = new SpriteLayer(BACKPARALLAX);
		farGround = new SpriteLayer(FARPARALLAX);

		//************************************************************************************************
		//************** DEBUG level load ****************************************************************
		///***** Will be replaced with what the audio algorithm does *************************************
		//************************************************************************************************
		Vector2 temp = new Vector2();
		for(int i = 0; i < 80; i++){
			temp.set(i*textures[POWERLINES].getWidth(),0);
			Sprite tSprite = new Sprite(textures[POWERLINES]);
			tSprite.setPosition(temp.getX(), temp.getY());
			foreGround.put(temp.getX(),tSprite);
		}
		
		for(int i = 0; i < 10; i++){
			temp.set(i*400,0);
			Sprite tSprite = new Sprite(textures[MOUNTAIN]);
			tSprite.setPosition(temp.getX(), temp.getY());
			backGround.put(temp.getX(),tSprite);
		}
		temp.set(400,100);
		Sprite tSprite = new Sprite(textures[SUN]);
		tSprite.setPosition(temp.getX(), temp.getY());
		farGround.put(temp.getX(),tSprite);
		//************************************************************************************
		
		
		//Load start of Level Layers
		farGround.loadStart(worldPosition);
		backGround.loadStart(worldPosition);
		foreGround.loadStart(worldPosition);
	}

	/**
	 * Updates current screen with level data from processed audio.
	 * For the moment, it just updatesthe world position.
	 * Need to add added platforms, sprites, change ground, etc
	 */
	public void updateScreen(){	//Updates level depending on music and how player is doing
		worldPosition += SPEED;
		
		/*	//Updates next pixel of Ground
		if(music.isPlaying()){
			groundLevels[drawStarter] = (int) (music.getPosition()+1 % 400);	//TEMPORARY
		}
		else{
			groundLevels[drawStarter] = DEFAULT_LEVEL_HEIGHT;
		}

		drawStarter = (drawStarter + 1) % Gdx.graphics.getWidth();*/
	}
	
	/**
	 * Draws ForeGround, layer closest to user
	 * 
	 * @param  SpriteBatch, Draw within the current SpriteBatch
	 */
	public void drawForeGround(SpriteBatch spriteBatch){
		spriteBatch.begin();
		foreGround.draw(spriteBatch,worldPosition);
		spriteBatch.end();
	}
	
	/**
	 * Draws FarGround, layer furthest away from user
	 * 
	 * @param  SpriteBatch, Draw within the current SpriteBatch
	 */
	public void drawFarGround(SpriteBatch spriteBatch){
		spriteBatch.begin();
		farGround.draw(spriteBatch,worldPosition);
		spriteBatch.end();
	}
	
	/**
	 * Draws BackGround, layer 2nd furthest away from user
	 * 
	 * @param  SpriteBatch, Draw within the current SpriteBatch
	 */
	public void drawBackGround(SpriteBatch spriteBatch){
		spriteBatch.begin();
		backGround.draw(spriteBatch,worldPosition);
		spriteBatch.end();
	}

	/**
	 * Draws Ground, Player runs ontop of
	 * 
	 * @param  SpriteBatch, Draw within the current SpriteBatch
	 */
	public void drawGround(SpriteBatch spriteBatch){
		Sprite sprite = new Sprite(textures[GROUND]);
		spriteBatch.begin();
		int posX = 0;
		for(int i = 0; i < Gdx.graphics.getWidth(); i++) {
			posX = (drawStarter+i)%Gdx.graphics.getWidth();
			sprite.setPosition(i, 0);
			sprite.setBounds(i, 0, 1, groundLevels[posX]);
			sprite.draw(spriteBatch);
		}
		spriteBatch.end();
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
