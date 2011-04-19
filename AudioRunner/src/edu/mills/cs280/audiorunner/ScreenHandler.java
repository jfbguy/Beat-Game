package edu.mills.cs280.audiorunner;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

public class ScreenHandler{
	
	//private static final int SCREEN_SPEED_FACTOR = 200;
	//private static final int SPEED = Gdx.graphics.getWidth()/SCREEN_SPEED_FACTOR;
	private static final int SPEED = 6;
	private static final float ONSCREEN_BUFFER = .2f*Gdx.graphics.getWidth();
	public static final float GROUND_HEIGHT = Gdx.graphics.getHeight()*.1f;
	public static final float CEILING_HEIGHT = Gdx.graphics.getHeight()*.7f;
	
	private final float[] PARALLAX = {1.5f,1.0f,.4f,.06f,.001f};
	private final int NUM_OF_TEXTURES = 6;		//UPDATE THIS IF YOU ADD A TEXTURE!!!
	private final int MOUNTAIN = 0;	//Texture Constants, each needs to be different!
	private final int SUN = 1;
	private final int POWERLINES = 2;
	private final int GROUND = 3;
	private final int PLATFORM = 4;
	private final int SCOREITEM = 5;
	
	//TODO Synch platform occurrences with music
	private final int PLATFORM_STEP_SIZE = 200;//These will be defined by screensize/music synch
	private final int PLATFORMS_START = 1000;
	private final int PLATFORM_HEIGHT = 100;//This will be defined by...something
	
	private static float mCurrentFrameSpeed;
	ImmediateModeRenderer mRenderer;
	private static Vector2 mWorldPosition;
	private Texture[] mTextures;
	private SpriteLayer[] mSpriteLayers;
	private CollisionLayer platformLayer;
	private CollisionLayer scoreItemLayer;
	//private LinkedList<Particle> particles;

	/**
	 * Constructor
	 */
	public ScreenHandler(int numOfLayers, List<Float> eventList){
		mWorldPosition = new Vector2(0,0);
		mSpriteLayers = new SpriteLayer[numOfLayers];
		platformLayer = new CollisionLayer(PARALLAX[1]);
		scoreItemLayer = new CollisionLayer(PARALLAX[1]);
		//particles = new LinkedList<Particle>();

		//Declare all SpriteLayers
		for(int i = 0; i < mSpriteLayers.length; i++){
			mSpriteLayers[i] = new SpriteLayer(PARALLAX[i]);
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
			mSpriteLayers[3].put(temp.getX(),tSprite);
		}

		//Score Items
		for(int i = 0; i < 300; i++){
			temp.set(i*30,150);
			ScoreItem scoreItem = new ScoreItem((float)temp.getX(),(float)temp.getY(),10f,10f,"data/yellow.png",10);
			scoreItem.setPosition(temp.getX(), temp.getY());
			scoreItemLayer.put(temp.getX(),scoreItem);
		}
		
		//platforms
		//TODO make platforms more interesting
		for(int i = 0; i < eventList.size(); i++){
			if(eventList.get(i)>0){
				temp.set(PLATFORMS_START + (i * PLATFORM_STEP_SIZE), PLATFORM_HEIGHT);
				Platform platform = new Platform((float)temp.getX(),(float)temp.getY(),100f,10f,"data/purple.png");
				platformLayer.put(temp.getX(),platform);
			}
		}
		
//		for(int i = 0; i < 10; i++){
//			temp.set(i*200+1000,50);
//			Platform platform = new Platform((float)temp.getX(),(float)temp.getY(),100f,10f,"data/purple.png");
//			platformLayer.put(temp.getX(),platform);
//			temp.set(i*200+1000,100);
//			platform = new Platform((float)temp.getX(),(float)temp.getY(),100f,10f,"data/purple.png");
//			platformLayer.put(temp.getX(),platform);
//		}

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
	public void updateScreen(Player player){	//Updates level depending on music and how player is doing
		mCurrentFrameSpeed = SPEED*MusicHandler.getTransitionScale();
		mWorldPosition.x += mCurrentFrameSpeed;
		
		if(player.getY() > mWorldPosition.y + CEILING_HEIGHT){
			mWorldPosition.y = (int) (player.getY() - CEILING_HEIGHT);
		}
		else if(player.getY() < mWorldPosition.y + GROUND_HEIGHT){
			mWorldPosition.y = (int) (player.getY() - GROUND_HEIGHT);
		}

	}

	/**
	 * Draws Everything on Screen
	 * 
	 * @param
	 */
	public void draw(SpriteBatch spriteBatch, Player player){
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
		Particle.updateParticles();
		Particle.draw(spriteBatch, mWorldPosition);

		//Draw second half of layers rounded down
		for(int i = mSpriteLayers.length/2-1; i >= 0; i--){
			mSpriteLayers[i].draw(spriteBatch,mWorldPosition);
		}
	}

	/**
	 * Draws Ground, Player runs ontop of
	 * 
	 * @param  SpriteBatch, Draw within the current SpriteBatch
	 */
	private void drawGround(SpriteBatch spriteBatch){
		spriteBatch.begin();		
		spriteBatch.draw(mTextures[GROUND],
				0.0f,							//Draw at X position
				0-mWorldPosition.y,											//Draw at Y position
				Gdx.graphics.getWidth(),GROUND_HEIGHT,							//Size of collidable
				0, 0,																//Get part of texture
				mTextures[GROUND].getWidth(), mTextures[GROUND].getHeight(),//Size of gotten part
				false,false);	
		spriteBatch.end();
	}

	public CollisionLayer getPlatforms(){
		return platformLayer;
	}

	public CollisionLayer getScoreItems(){
		return scoreItemLayer;
	}

	/**
	 * Draws Ground, Player runs ontop of
	 * 
	 * @return int Current Speed of player.  Pretty much speed tyhe level is scrolling
	 */
	public static int getSpeed(){
		return SPEED;
	}
	
	public static float getCurrentFrameSpeed(){
		return mCurrentFrameSpeed;
	}
	
	public static Vector2 getWorldPosition(){
		return mWorldPosition;
	}

	public static boolean onScreen(Vector2 pos){
		if(pos.x-mWorldPosition.x < -ONSCREEN_BUFFER || pos.x-mWorldPosition.x > Gdx.graphics.getWidth() + ONSCREEN_BUFFER){
			return false;
		}
		if(pos.y-mWorldPosition.y < 0 || pos.y-mWorldPosition.y > Gdx.graphics.getHeight()){
			return false;
		}

		return true;
	}

	public static boolean onScreen(float x,float y){
		if(x-mWorldPosition.x < -ONSCREEN_BUFFER || x-mWorldPosition.x > Gdx.graphics.getWidth() + ONSCREEN_BUFFER){
			return false;
		}
		if(y-mWorldPosition.y < 0 || y-mWorldPosition.y > Gdx.graphics.getHeight()){
			return false;
		}

		return true;
	}

}
