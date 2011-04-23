package edu.mills.cs280.audiorunner;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

public class ScreenHandler{

	private static final float SPEED_MULTIPLIER = 6;
	private static final float ONSCREEN_BUFFER = .2f*Gdx.graphics.getWidth();
	public static final float GROUND_HEIGHT = Gdx.graphics.getHeight()*.1f;
	public static final float CEILING_HEIGHT = Gdx.graphics.getHeight()*.7f;

	private final float[] PARALLAX = {1.5f,1.0f,.4f,.06f,.001f};

	private final String MOUNTAIN_FILE = "data/mountain.png";
	private final String SUN_FILE = "data/sun.png";
	private final String POWERLINES_FILE = "data/powerlines.png";
	private final String GROUND_FILE = "data/gradient_BW_1D.png";
	private final String PLATFORM_FILE = "data/purple.png";
	private final String SCOREITEM_FILE = "data/yellow.png";

	private final int NUM_OF_TEXTURES = 6;		//UPDATE THIS IF YOU ADD A TEXTURE!!!
	private final int MOUNTAIN = 0;	//Texture Constants, each needs to be different!
	private final int SUN = 1;
	private final int POWERLINES = 2;
	private final int GROUND = 3;
	private final int PLATFORM = 4;
	private final int SCOREITEM = 5;

	private final int NUM_OF_PIXMAPS = 2;		//UPDATE THIS IF YOU ADD A PIXMAP!!!
	private final int SCOREITEM_PIXMAP = 0;
	private final int PLATFORM_PIXMAP = 1;

	//TODO Synch platform occurrences with music
	private final int PLATFORM_STEP_SIZE = 200;//These will be defined by screensize/music synch
	private final int PLATFORMS_START = 1000;
	private final int PLATFORM_HEIGHT = 100;//This will be defined by...something

	private static float mSpeed;
	private static float mCurrentFrameSpeed;
	ImmediateModeRenderer mRenderer;
	private static Vector2 mWorldPosition;
	private static int mWorldLeadin;
	private Texture[] mTextures;
	private Pixmap[] mPixmaps;
	private SpriteLayer[] mSpriteLayers;
	private CollisionLayer platformLayer;
	private CollisionLayer scoreItemLayer;
	//private LinkedList<Particle> particles;

	/**
	 * Constructor
	 */
	public ScreenHandler(int numOfLayers, List<Float> eventList){
		mRenderer = new ImmediateModeRenderer();

		mSpeed = MusicData.getFrameDuration()/(1000/TimeHandler.FRAMERATE)*SPEED_MULTIPLIER;
		mWorldPosition = new Vector2(0,0);
		mWorldLeadin = (int) (Gdx.graphics.getWidth()-mSpeed/2);
		mWorldPosition.x += mWorldLeadin;
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
		mTextures[MOUNTAIN] = new Texture(Gdx.files.internal(MOUNTAIN_FILE));
		mTextures[SUN] = new Texture(Gdx.files.internal(SUN_FILE));
		mTextures[POWERLINES] = new Texture(Gdx.files.internal(POWERLINES_FILE));
		mTextures[GROUND] = new Texture(Gdx.files.internal(GROUND_FILE));
		mTextures[PLATFORM] = new Texture(Gdx.files.internal(PLATFORM_FILE));
		mTextures[SCOREITEM] = new Texture(Gdx.files.internal(SCOREITEM_FILE));

		//load pixmaps - these are needed for pixel perfect collisions
		mPixmaps = new Pixmap[NUM_OF_PIXMAPS];
		mPixmaps[SCOREITEM_PIXMAP] = new Pixmap(Gdx.files.internal(SCOREITEM_FILE));
		mPixmaps[PLATFORM_PIXMAP] = new Pixmap(Gdx.files.internal(SCOREITEM_FILE));

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
			ScoreItem scoreItem = new ScoreItem((float)temp.getX(),(float)temp.getY(),10f,10f,mTextures[SCOREITEM],mPixmaps[SCOREITEM_PIXMAP],10);
			scoreItem.setPosition(temp.getX(), temp.getY());
			scoreItemLayer.put(temp.getX(),scoreItem);
		}

		//platforms
		loadPlatforms();

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

	public ScreenHandler(int numOfLayers){
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
			ScoreItem scoreItem = new ScoreItem((float)temp.getX(),(float)temp.getY(),10f,10f,mTextures[SCOREITEM],mPixmaps[SCOREITEM_PIXMAP],10);
			scoreItem.setPosition(temp.getX(), temp.getY());
			scoreItemLayer.put(temp.getX(),scoreItem);
		}

		//platforms
		for(int i = 0; i < 10; i++){
			temp.set(i*200+1000,50);
			Platform platform = new Platform((float)temp.getX(),(float)temp.getY(),100f,10f,mTextures[PLATFORM],mPixmaps[PLATFORM_PIXMAP]);
			platformLayer.put(temp.getX(),platform);
			temp.set(i*200+1000,100);
			platform = new Platform((float)temp.getX(),(float)temp.getY(),100f,10f,mTextures[PLATFORM],mPixmaps[PLATFORM_PIXMAP]);
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
	public void updateScreen(Player player){	//Updates level depending on music and how player is doing
		mCurrentFrameSpeed = getSpeed()*TimeHandler.getTransitionScale();
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

		//HOW TO DRAW A LINE!!!!
		mRenderer.begin(GL10.GL_LINES);
		mRenderer.color(1, 0, 0, 1);
		mRenderer.vertex(0, 0, 0);
		mRenderer.color(1, 0, 0, 1);
		mRenderer.vertex(300, 300, 0);
		mRenderer.end();

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
	 * Populates the game with platforms connected to the peaks generated by the decoder
	 * 
	 * 
	 */
	public void loadPlatforms(){
		List<Float> peaks = MusicData.getPeaks();
		Iterator<Float> iter = peaks.iterator();
		
		/*
		float avg = 0.0f;
		int peakCounter = 0;
		while(iter.hasNext()){
			peakCounter++;
			peak = iter.next();
			if(peak > 0.0f){
				System.out.println(peak);
				avg += peak;
			}
		}
		avg = (avg/(float)(peakCounter));*/
		
		float peak = 0.0f;
		float frameDuration = MusicData.getDuration();
		int frameCounter = 0;
		while(iter.hasNext()){
			frameCounter++;
			peak = iter.next();
			if(peak > 800.0f){
				Platform platform = new Platform(frameCounter*mSpeed,100.0f,10f,peak/5f,mTextures[PLATFORM],mPixmaps[PLATFORM_PIXMAP]);
				platformLayer.put((int)(frameCounter*mSpeed),platform);
			}
		}

	}

	public static float getSpeed(){
		return mSpeed;
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
