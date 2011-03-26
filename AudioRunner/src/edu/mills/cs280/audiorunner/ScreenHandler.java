package edu.mills.cs280.audiorunner;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

public class ScreenHandler{
	private static final int SPEED = 5;
	private final int DEFAULT_LEVEL_HEIGHT = 60;
	private final float[] PARALLAX = {1.5f,1.0f,.4f,.3f,.02f};
	private final int NUM_OF_TEXTURES = 6;		//UPDATE THIS IF YOU ADD A TEXTURE!!!
	private final int MOUNTAIN = 0;	//Texture Constants, each needs to be different!
	private final int SUN = 1;
	private final int POWERLINES = 2;
	private final int GROUND = 3;
	private final int PLATFORM = 4;
	private final int SCOREITEM = 5;


	ImmediateModeRenderer mRenderer;
	private int mDrawStarter;
	private static Vector2 mWorldPosition;
	private int[] mGroundLevels;
	private Texture[] mTextures;
	private SpriteLayer[] mSpriteLayers;
	private CollisionLayer platformLayer;
	private CollisionLayer scoreItemLayer;
	private LinkedList<Particle> particles;

	/**
	 * Constructor
	 */
	public ScreenHandler(int numOfLayers){
		mDrawStarter = 0;
		mWorldPosition = new Vector2(0,0);
		mSpriteLayers = new SpriteLayer[numOfLayers];
		platformLayer = new CollisionLayer(PARALLAX[1]);
		scoreItemLayer = new CollisionLayer(PARALLAX[1]);
		particles = new LinkedList<Particle>();

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
		mTextures = new Texture[NUM_OF_TEXTURES];
		mTextures[MOUNTAIN] = new Texture(Gdx.files.internal("data/mountain.png"));
		mTextures[SUN] = new Texture(Gdx.files.internal("data/sun.png"));
		mTextures[POWERLINES] = new Texture(Gdx.files.internal("data/powerlines.png"));
		mTextures[GROUND] = new Texture(Gdx.files.internal("data/gradient_BW_1D.png"));
		mTextures[PLATFORM] = new Texture(Gdx.files.internal("data/purple.png"));
		mTextures[SCOREITEM] = new Texture(Gdx.files.internal("data/yellow.png"));

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

		//Score Items
		for(int i = 0; i < 80; i++){
			temp.set(i*30,150);
			ScoreItem scoreItem = new ScoreItem((float)temp.getX(),(float)temp.getY(),10f,10f,"data/yellow.png",10);
			scoreItem.setPosition(temp.getX(), temp.getY());
			scoreItemLayer.put(temp.getX(),scoreItem);
		}

		//platforms
		for(int i = 0; i < 10; i++){
			temp.set(i*200+1000,50);
			Platform platform = new Platform((float)temp.getX(),(float)temp.getY(),100f,10f,"data/purple.png");
			platformLayer.put(temp.getX(),platform);
			temp.set(i*200+1000,100);
			platform = new Platform((float)temp.getX(),(float)temp.getY(),100f,10f,"data/purple.png");
			platformLayer.put(temp.getX(),platform);
		}

		//sun
		temp.set(400,100);
		Sprite tSprite = new Sprite(mTextures[SUN]);
		tSprite.setPosition(temp.getX(), temp.getY());
		mSpriteLayers[4].put(temp.getX(),tSprite);
		//************************************************************************************
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
		mWorldPosition.x += SPEED;

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
		player.draw(spriteBatch);

		//Draw Platforms
		platformLayer.draw(spriteBatch, mWorldPosition);

		//Draw Score Items
		scoreItemLayer.draw(spriteBatch, mWorldPosition);

		//Draw Particles
		Particle.updateParticles(particles);
		Particle.draw(particles, spriteBatch, mWorldPosition);

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

	public CollisionLayer getPlatforms(){
		return platformLayer;
	}

	public CollisionLayer getScoreItems(){
		return scoreItemLayer;
	}

	public void jumpParticles(Player player){
		Random rand = new Random();
		for(int i = 0; i < 10; i++){
			particles.add(new Particle(mTextures[SCOREITEM],
					new Vector2((int)(player.getX()+player.getWidth()/2),(int)player.getY()), 
					new Vector2(rand.nextInt(20)-10,rand.nextInt(20)-10),
					(float)Gdx.graphics.getHeight()*.05f, Particle.FALLING));
		}
	}

	public void explosionParticles(float x, float y){
		Random rand = new Random();
		for(int i = 0; i < 10; i++){
			particles.add(new Particle(mTextures[SCOREITEM],
					new Vector2((int)x,(int)y),
					new Vector2((rand.nextInt(40)-20),rand.nextInt(40)-20),
					(float)Gdx.graphics.getHeight()*.01f,Particle.EXPLODING));
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
	
	public static Vector2 getWorldPosition(){
		return mWorldPosition;
	}

	public static boolean onScreen(Vector2 pos){
		if(pos.x-mWorldPosition.x < 0 || pos.x-mWorldPosition.x > Gdx.graphics.getWidth()){
			return false;
		}
		if(pos.y-mWorldPosition.y < 0 || pos.y-mWorldPosition.y > Gdx.graphics.getHeight()){
			return false;
		}

		return true;
	}

	public static boolean onScreen(float x,float y){
		if(x-mWorldPosition.x < 0 || x-mWorldPosition.x > Gdx.graphics.getWidth()){
			return false;
		}
		if(y-mWorldPosition.y < 0 || y-mWorldPosition.y > Gdx.graphics.getHeight()){
			return false;
		}

		return true;
	}

}
