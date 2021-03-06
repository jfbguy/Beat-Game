package edu.mills.cs280.audiorunner;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main palyer class, decides where to draw player and most interactions with game
 * 
 * @author jklein
 *
 */
public class Player extends Collidable {

	private final float PLATFORM_CATCH = .4f;
	private final int FRAME_COUNT = 5;
	private final float FRAME_DELAY = TimeHandler.FRAMERATE/FRAME_COUNT/3;
	private final int SPRITE_SIZE = 128;
	private final float GRAVITY = 1.0f;
	public final int JUMPSPEED = 10;
	public final int TERMINAL_VELOCITY = -5;
	private final int GROUND = 0;
	private final int AIR = 1;
	private final int PLATFORM = 2;

	private int mStartX;
	private float mAnimDelay;
	private float mVertVelocity;
	private float mAnimCounter;
	private int mFrameNum;
	private int mStatus;
	private Texture mTexture;
	private Pixmap mPixmap;

	/**
	 * Constructor
	 * 
	 * @param  Texture, Holds The Sprite Texture for player
	 * @param float, x position
	 * @param float, y position
	 * @param float, width
	 * @param float, height
	 */
	public Player(float x, float y, float width, float height) {
		mTexture = new Texture(Gdx.files.internal("data/bunnysprite.png"));
		mPixmap = new Pixmap(Gdx.files.internal("data/bunnysprite.png"));
		this.loadTexture(mPixmap, mTexture);
		mStartX = (int)x;
		this.setBounds(x, y, width, height);
		mVertVelocity = 0;
		mAnimDelay = FRAME_DELAY;
		mAnimCounter = 0;
		mFrameNum = 0;
	}

	public Boolean onTop(Platform platform){
		if(mVertVelocity < 0){

		}

		return false;
	}

	/**
	 * Draw player's current frame of animation
	 * 
	 * @param  SpriteBatch, Draw within the current SpriteBatch
	 */
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.begin();
		spriteBatch.draw(getTexture(),
				getX()-MusicData.getPosition(),getY()-ScreenHandler.getWorldPosition().y,
				this.getWidth(),this.getHeight(),
				getSpriteX(), getSpriteY(),
				SPRITE_SIZE, SPRITE_SIZE,
				false,false);
		spriteBatch.end();
	}

	/**
	 * Updates the Animation of the Player
	 * Increments the animation count, and decides if the sprite should switch
	 * to the next frame of animation
	 */
	public void animate(){
		mAnimCounter += (1.0f*TimeHandler.getTransitionScale());
		if(mAnimCounter >= mAnimDelay) {
			if(mFrameNum == FRAME_COUNT-1 ) {
				mFrameNum = 0;
			}
			else{
				mFrameNum++;
			}
			mAnimCounter = 0;
		}
	}

	/**
	 * Get the X position with in the Sprite's Texture where the current
	 * frame of animation is located. 
	 * 
	 * @return int current X position within the sprite's texture
	 */
	public int getSpriteX(){
		return mFrameNum*SPRITE_SIZE;
	}

	/**
	 * Get the Y position with in the Sprite's Texture where the current
	 * frame of animation is located. 
	 * 
	 * @return int current Y position within the sprite's texture
	 */
	public int getSpriteY(){
		return 0;
	}

	/**
	 * Gets the vertical velocity
	 * 
	 * @return int, Y velocity
	 */
	public float getVY(){
		return mVertVelocity;
	}

	/**
	 * Get the Y position with in the Sprite's Texture wher ethe current
	 * frame of animation is located. 
	 * 
	 * @param int Y velocity you wish to add to current Y velocity
	 */
	public void setVY(int mVertVelocity){
		this.mVertVelocity = mVertVelocity;
	}

	/**
	 * Gets the current texture dimension size in pixels
	 * 
	 * @return int the size of player's sprite, EX: 64 would be a 64x64 Sprite
	 */
	public int getSize()
	{
		return SPRITE_SIZE;
	}

	/**
	 * Set player's mStatus to GROUND.  The player will now stop falling
	 */
	public void setGround(){
		mStatus = GROUND;
	}

	/**
	 * Set player's mStatus to AIR.  The player will now be in the air, gravity will effect the player
	 */
	public void setAir(){
		mStatus = AIR;
	}

	/**
	 * Checks if the player is on the Ground
	 * 
	 * @return Returns true if player is onthe Ground
	 */
	public Boolean inAir(){
		if(mStatus == AIR){
			return true;
		}
		return false;
	}

	/**
	 * Calculate Physics on player, Gravity always effects player if they are in the air
	 */

	public void update(ScreenHandler screenHandler, ScoreBoard scoreBoard){
		setPosition(mStartX+MusicData.getPosition(), getY());
		if(mStatus == AIR){
			setPosition(getX(), getY()+mVertVelocity*TimeHandler.getTransitionScale());
			mVertVelocity -= GRAVITY*TimeHandler.getTransitionScale();
		}
		if(mVertVelocity < 0 || mStatus == PLATFORM)
		{
			mStatus = AIR;
			Collection<LinkedList<Collidable>> platformLists = screenHandler.getPlatforms().getOnScreen().values();
			for(LinkedList<Collidable> platformList : platformLists){
				for(Collidable platform : platformList){
					if(this.getY() >= platform.getY()+platform.getHeight() && this.getY()+mVertVelocity <= platform.getY()+platform.getHeight()){
						if(this.getX() + this.getWidth() * PLATFORM_CATCH >= platform.getX()
								&& (this.getX() + this.getWidth() * PLATFORM_CATCH <= platform.getX() + platform.getWidth())
								|| (this.getX() + this.getWidth() * (1-PLATFORM_CATCH) >= platform.getX()
										&& this.getX() + this.getWidth() * (1-PLATFORM_CATCH) <= platform.getX() + platform.getWidth())){
							mStatus = PLATFORM;
							mVertVelocity = 0;
						}
					}
				}
			}
		}

		//Check if touches any scoreItems
		Collection<LinkedList<Collidable>> scoreItemLists = screenHandler.getScoreItems().getOnScreen().values();
		for(LinkedList<Collidable> scoreItemList : scoreItemLists){
			Iterator<Collidable> iter = scoreItemList.iterator();
			while(iter.hasNext()){
				Collidable tempCollidable = (Collidable) iter.next();
				if(this.pixelCollides(tempCollidable)){
					((ScoreItem)tempCollidable).scored(scoreBoard);	//Score!
					//create explosion at touch point
					Particle.createExplosion(tempCollidable.getX(),tempCollidable.getY());
					scoreBoard.addFloaterScore((int)(tempCollidable.getX()-MusicData.getPosition()),(int)tempCollidable.getY(),(int)tempCollidable.getY()/10);
					//screenHandler.explosionParticles(tempCollidable.getX(),tempCollidable.getY());
					iter.remove();		//Remove ScoreItem after being collected
				}
			}
		}


		//GROUND  ####THIS WILL PROBABLY CHANGE, just hardcoded for now####
		if(mStatus == AIR && getY() < ScreenHandler.GROUND_HEIGHT){
			mVertVelocity = 0;
			mStatus = GROUND;
			setPosition(getX(),ScreenHandler.GROUND_HEIGHT);
		}

		animate();

	}

	/**
	 * Player Jumps.  Adds a positive velocity to player's vertical velocity
	 * 
	 * @param jumpSpeed velocity to add to player's vertical velocity
	 */
	public void jump(float jumpSpeed){
		if(mStatus != AIR){
			mVertVelocity += jumpSpeed;
			if(mVertVelocity < TERMINAL_VELOCITY){
				mVertVelocity = TERMINAL_VELOCITY;
			}
			mStatus = AIR;
		}
	}

	/**
	 * Regular jump, decide if boost is added according if there is a peak at this moment in the song
	 * 
	 * @param scoreBoard needed if we add a floater score
	 * @param screenHandler 
	 * @param boostMeter for adding boost
	 */
	public void jump(ScoreBoard scoreBoard, ScreenHandler screenHandler, BoostMeter boostMeter){

		int jumpscore = (int)MusicData.getPeaks().get((int)(MusicData.getPosition()/MusicData.getFrameDuration())).floatValue();
		for(int i = (int)(MusicData.getPosition()/MusicData.getFrameDuration()-MusicData.getFrameDuration()*2);
		i < (int)(MusicData.getPosition()/MusicData.getFrameDuration()+MusicData.getFrameDuration()*2);
		i += MusicData.getFrameDuration()){
			if(jumpscore > 0.0f){
				jumpscore = 7;
				boostMeter.addBoost(jumpscore);
				scoreBoard.addFloaterScore((int)(this.getX()-MusicData.getPosition()),(int)this.getY(),jumpscore);
				Particle.createJumpParticles(this,jumpscore);
			}
		}

		this.jump(this.JUMPSPEED);
		//Score add according to jump, will change with music scoring method
		//and create particles to show if jump is special
	}

	/**
	 * Boost jump, If there is boost, multiply the jump with its power
	 * 
	 * @param scoreBoard needed if we add a floater score
	 * @param screenHandler 
	 * @param boostMeter for adding boost,and depleting the meter
	 */
	public void boostJump(ScoreBoard scoreBoard, ScreenHandler screenHandler, BoostMeter boostMeter){
		if(boostMeter.getBoost() > 0.0f){
			int jumpscore = (int)MusicData.getPeaks().get((int)(MusicData.getPosition()/MusicData.getFrameDuration())).floatValue();
			float boost = boostMeter.boost();
			jumpscore = (int) (this.JUMPSPEED*boost);
			//Score add according to jump, will change with music scoring method
			scoreBoard.addFloaterScore((int)(this.getX()-MusicData.getPosition()),(int)this.getY(),jumpscore);
			//and create particles to show if jump is special
			Particle.createJumpParticles(this,jumpscore);
			this.jump(jumpscore);
		}
		else{
			this.jump(this.JUMPSPEED);
		}

	}
}
