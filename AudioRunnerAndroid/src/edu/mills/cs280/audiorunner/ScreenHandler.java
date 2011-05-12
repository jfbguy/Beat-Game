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

/**
 * This class handles the graphics and gameplay of the game itself.
 * Includes sprite layers, collision, level generation, score.
 * 
 * @author tadams
 * @author jfbguy
 */

public class ScreenHandler{

	private static final float SPEED_MULTIPLIER = 6;//TODO remove
	private static final float ONSCREEN_BUFFER = .2f*Gdx.graphics.getWidth();
	public static final float GROUND_HEIGHT = Gdx.graphics.getHeight()*.1f;
	public static final float CEILING_HEIGHT = Gdx.graphics.getHeight()*.7f;

	private final float[] PARALLAX = {1.0f,1.0f,.4f,.06f,.001f};

	private final String SUN_FILE = "data/sun.png";
	private final String PLATFORM_FILE = "data/longcloud.png";
	private final String TREE_FILE = "data/tree.png";
	private final String RAINBOW_FILE = "data/rainbow.png";
	private final String CARROT_FILE = "data/carrot.png";
	private final String GROUND_FILE = "data/gradient_BW_1D.png";
	private final String CYAN_FILE = "data/cyan.png";

	private final int NUM_OF_TEXTURES = 7;		//UPDATE THIS IF YOU ADD A TEXTURE!!!
	private final int SUN = 0;
	private final int GROUND = 1;
	private final int PLATFORM = 2;
	private final int TREE = 3;
	private final int RAINBOW = 4;
	private final int CARROT = 5;
	private final int CYAN = 6;
	
	private final int NUM_OF_PIXMAPS = 3;		//UPDATE THIS IF YOU ADD A PIXMAP!!!

	private final int PLATFORM_PIXMAP = 0;
	private final int CARROT_PIXMAP = 1;
	private final int CYAN_PIXMAP = 2;

	//TODO Synch platform occurrences with music
	private final int GAME_PARALLAX_LAYER = 1;
	private final int PLATFORM_STEP_SIZE = 200;//These will be defined by screensize/music synch
	private final int PLATFORMS_START = 1000;
	private final int PLATFORM_Y = 50;//This will be defined by...something
	private final float PLATFORM_HEIGHT = 25f;
	private final float PLATFORM_MIN_WIDTH = 100f;
	private final float PLATFORM_MAX_FRAMES = 5;
	private final float PLATFORM_MAX_WIDTH= 500;
	private final float PLATFORM_GAP = 30f;
	
	//carrot info
	private final float CARROT_Y_OFFSET = 30f;
	private final float CARROT_DIMENSIONS = 32f;
	private final int CARROT_POINTS = 10;
	private final float CARROT_DELAY = 30f;

	private static float mSpeed;
//	private static float mCurrentFrameSpeed;
	ImmediateModeRenderer mRenderer;
	private static Vector2 mWorldPosition;
	private static int mWorldLeadin;
	private Texture[] mTextures;
	private Pixmap[] mPixmaps;
	private SpriteLayer[] mSpriteLayers;
	private CollisionLayer platformLayer;
	private CollisionLayer scoreItemLayer;
	private int mSyncOffset;
	
	//private LinkedList<Particle> particles;

	/**
	 * Constructor: initializes variables for textures and creates level/ graphics
	 * @param # of sprite layers to draw
	 */
	public ScreenHandler(int numOfLayers){
		mRenderer = new ImmediateModeRenderer();

		//mSpeed = MusicData.getFrameDuration()/(1000/TimeHandler.FRAMERATE)*SPEED_MULTIPLIER;
		mSpeed = 5;
		mWorldPosition = new Vector2(0,0);
		mWorldLeadin = (int) (Gdx.graphics.getWidth()-mSpeed/2);
		mWorldPosition.x += mWorldLeadin;
		mSpriteLayers = new SpriteLayer[numOfLayers];
		platformLayer = new CollisionLayer(PARALLAX[1]);	//Platforms synched to music, Paralax 1 for loading
		scoreItemLayer = new CollisionLayer(PARALLAX[1]);
		//particles = new LinkedList<Particle>();

		//Declare all SpriteLayers
		for(int i = 0; i < mSpriteLayers.length; i++){
			mSpriteLayers[i] = new SpriteLayer(PARALLAX[i]);
		}

		//load sprites
		mTextures = new Texture[NUM_OF_TEXTURES];
		mTextures[SUN] = new Texture(Gdx.files.internal(SUN_FILE));
		mTextures[PLATFORM] = new Texture(Gdx.files.internal(PLATFORM_FILE));
		mTextures[TREE] = new Texture(Gdx.files.internal(TREE_FILE));
		mTextures[CARROT] = new Texture(Gdx.files.internal(CARROT_FILE));
		mTextures[RAINBOW] = new Texture(Gdx.files.internal(RAINBOW_FILE));
		mTextures[GROUND] = new Texture(Gdx.files.internal(GROUND_FILE));
		mTextures[CYAN] = new Texture(Gdx.files.internal(CYAN_FILE));
		
		//load pixmaps - these are needed for pixel perfect collisions
		mPixmaps = new Pixmap[NUM_OF_PIXMAPS];
		mPixmaps[PLATFORM_PIXMAP] = new Pixmap(Gdx.files.internal(PLATFORM_FILE));
		mPixmaps[CARROT_PIXMAP] = new Pixmap(Gdx.files.internal(CARROT_FILE));
		mPixmaps[CYAN_PIXMAP] = new Pixmap(Gdx.files.internal(CYAN_FILE));
		
		//sync offset, represents were music is synced to
		float mSyncOffset = Gdx.graphics.getWidth();

		//************************************************************************************************
		//************** DEBUG level load ****************************************************************
		///***** Will be replaced with what the audio algorithm does *************************************
		//************************************************************************************************
		Vector2 temp = new Vector2();
		for(int i = 0; i < 80; i++){
			temp.set(i*mTextures[TREE].getWidth(),0);
			Sprite tSprite = new Sprite(mTextures[TREE]);
			tSprite.setBounds(temp.getX(), temp.getY(), Gdx.graphics.getWidth()*.3f, Gdx.graphics.getHeight()*.7f);
			mSpriteLayers[0].put(temp.getX(),tSprite);
		}

		for(int i = 0; i < 20; i++){
			temp.set(i*400,0);
			Sprite tSprite = new Sprite(mTextures[RAINBOW]);
			tSprite.setBounds(temp.getX(), temp.getY(), Gdx.graphics.getWidth()*.5f, Gdx.graphics.getHeight()*1.0f);
			mSpriteLayers[3].put(temp.getX(),tSprite);
		}

		//platforms
		MusicData.loadPlatforms(this);

		//sun
		temp.set(400,100);
		Sprite tSprite = new Sprite(mTextures[SUN]);
		tSprite.setBounds(temp.getX(), temp.getY(), Gdx.graphics.getHeight()*.4f, Gdx.graphics.getHeight()*.4f);
		mSpriteLayers[4].put(temp.getX(),tSprite);
		//************************************************************************************
		//************************************************************************************

		//Load start of Level Layers
		for(int i = 0; i < mSpriteLayers.length; i++){
			mSpriteLayers[i].loadStart(mWorldPosition);
		}
	}

	/**
	 * Updates the world position, so sprites can be moved on screen.
	 * 
	 */
	public void updateScreen(Player player){	//Updates level depending on music and how player is doing
//		mCurrentFrameSpeed = getSpeed()*TimeHandler.getTransitionScale();
//		mWorldPosition.x += mCurrentFrameSpeed;

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
		/*
		mRenderer.begin(GL10.GL_LINES);
		mRenderer.color(1, 0, 0, 1);
		mRenderer.vertex(0, 0, 0);
		mRenderer.color(1, 0, 0, 1);
		mRenderer.vertex(300, 300, 0);
		mRenderer.end();
		*/
		
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
		Particle.draw(spriteBatch, mWorldPosition);
		Particle.updateParticles();

		//Draw second half of layers rounded down
		for(int i = mSpriteLayers.length/2-1; i >= 0; i--){
			mSpriteLayers[i].draw(spriteBatch,mWorldPosition);
		}

	}

	/**
	 * Draws Ground, Player runs on top of
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

	/**
	 * 
	 * @return the layer of platforms
	 */
	public CollisionLayer getPlatforms(){
		return platformLayer;
	}
	
/**
 * 
 * @return the layer of scoreitems
 */
	public CollisionLayer getScoreItems(){
		return scoreItemLayer;
	}
	
	public void loadPlatforms(List<Float> peaks, int frameNum){
		
		Iterator<Float> iter = peaks.iterator();	
		float frameDuration = MusicData.getFrameDuration(), stopCarrotsFor = 0;
		int framesHeld = 0, frameHeldAt = 0;
		//dummy platform, in case error causes to use without init
		Platform heldPlatform = new Platform(
				0,0,0,0,mTextures[PLATFORM],mPixmaps[PLATFORM_PIXMAP]);
		Float xPosition;
		
		for(int i = frameNum; i < peaks.size(); i++){
			
			//x position
			xPosition = (i*frameDuration + mSyncOffset);
//			xPosition = (i*frameDuration + mSyncOffset)*PARALLAX[GAME_PARALLAX_LAYER];

				
			//if detect a peak, finish any ongoing platforms and ready a new one
			if(peaks.get(i) > 0.0f){
//				System.out.print(peaks.get(i) + ",");
				float yAdjust = (peaks.get(i) % 50)*2;
				
				if(framesHeld > 0){
					float newWidth = ((i - frameHeldAt) * frameDuration) - PLATFORM_GAP - 1;
					newWidth = Math.max(newWidth,PLATFORM_MIN_WIDTH);
					heldPlatform.setSize(newWidth, PLATFORM_HEIGHT);
					platformLayer.put((int)(heldPlatform.getX()),heldPlatform);
					framesHeld = 0;
				}
				//only create a new platform if you weren't holding a platform
				//if this is NOT in an else loop, platforms overlap, not sure why
				//in meantime, we may uneccessarily cull some platforms in exchange for no overlap
				else{
					Platform platform = new Platform(
							xPosition,
							PLATFORM_Y + yAdjust,
							PLATFORM_MIN_WIDTH,
							PLATFORM_HEIGHT
							,mTextures[PLATFORM],mPixmaps[PLATFORM_PIXMAP]);
					heldPlatform = platform;
					framesHeld = 1;
					frameHeldAt = i;
				}
			}
			
			//if platform longer than max, end it and start anew
			if(framesHeld*frameDuration > PLATFORM_MAX_WIDTH){
				float newWidth = ((i - frameHeldAt) * frameDuration) -1;
				heldPlatform.setSize(PLATFORM_MAX_WIDTH/2,PLATFORM_HEIGHT);
				platformLayer.put((int)(heldPlatform.getX()),heldPlatform);
				framesHeld = 0;
			}
			
			//make carrots if there is a platform going
			if(framesHeld > 0 && stopCarrotsFor <=0){
				ScoreItem scoreItem = new ScoreItem(
						xPosition,
						heldPlatform.getY()+ CARROT_Y_OFFSET,
						CARROT_DIMENSIONS,
						CARROT_DIMENSIONS,
						mTextures[CARROT],
						mPixmaps[CARROT_PIXMAP],
						CARROT_POINTS);
				scoreItem.setBounds(xPosition,
						heldPlatform.getY() + CARROT_Y_OFFSET,
						Gdx.graphics.getHeight()*.1f,
						Gdx.graphics.getHeight()*.1f);
				Float x = frameDuration;
				scoreItemLayer.put(xPosition.intValue(),scoreItem);
				stopCarrotsFor = CARROT_DELAY;
			}
				
			//increment # frames that have passed since held began
			//decrement distance to stop carrots for
			if(framesHeld > 0){
				framesHeld++;
			}
			if(stopCarrotsFor>0){
				stopCarrotsFor -= frameDuration;
			}
		}
	}

	/**
	 * 
	 * @return speed
	 */
//	public static float getSpeed(){
//		return mSpeed;
//	}

//	public static float getCurrentFrameSpeed(){
//		return mCurrentFrameSpeed;
//	}

	/**
	 * @return Position of world
	 */
	public static Vector2 getWorldPosition(){
		return mWorldPosition;
	}

	/**
	 * Determine if a vector position is on the screen currently
	 * @param position, as a vector
	 * @return whether position is on screen
	 */
	public static boolean onScreen(Vector2 pos){
		if(pos.x-MusicData.getPosition() < -ONSCREEN_BUFFER || pos.x-MusicData.getPosition() > Gdx.graphics.getWidth() + ONSCREEN_BUFFER){
			return false;
		}
		if(pos.y-mWorldPosition.y < 0 || pos.y-mWorldPosition.y > Gdx.graphics.getHeight()){
			return false;
		}

		return true;
	}

	/**
	 * Determine if a position, represented as x,y coordinates are on screen
	 * @param x - x position
	 * @param y - y position
	 * @return whether position is on screen currently
	 */
	public static boolean onScreen(float x,float y){
		if(x-MusicData.getPosition() < -ONSCREEN_BUFFER || x-MusicData.getPosition() > Gdx.graphics.getWidth() + ONSCREEN_BUFFER){
			return false;
		}
		if(y-mWorldPosition.y < 0 || y-mWorldPosition.y > Gdx.graphics.getHeight()){
			return false;
		}

		return true;
	}

}
