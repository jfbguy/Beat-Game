package edu.mills.cs280.audiorunner;

import java.util.Collection;
import java.util.LinkedList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends Collidable {

	private final float PLATFORM_CATCH = .4f;
	private final int FRAME_COUNT = 6;
	private final int SPRITE_SIZE = 64;
	private final float GRAVITY = 0.5f;
	private final int JUMPSPEED = 10;
	private final int GROUND = 0;
	private final int AIR = 1;
	private final int PLATFORM = 2;

	private int mAnimDelay = 10;
	private float mVertVelocity;
	private int mAnimCounter;
	private int mFrameNum;
	private int mStatus;

	/**
	 * Constructor
	 * 
	 * @param  Texture, Holds The Sprite Texture for player
	 */
	public Player(String texture) {
		this.loadTexture(texture);
		mVertVelocity = 0;
		mAnimDelay = 25/ScreenHandler.getSpeed();
		mAnimCounter = 0;
		mFrameNum = 0;
	}

	/**
	 * Constructor
	 * 
	 * @param  Texture, Holds The Sprite Texture for player
	 * @param float, x position
	 * @param float, y position
	 * @param float, width
	 * @param float, height
	 */
	public Player(String texture, float x, float y, float width, float height) {
		this.loadTexture(texture);
		this.setBounds(x, y, width, height);
		mVertVelocity = 0;
		mAnimDelay = 25/ScreenHandler.getSpeed();
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

		spriteBatch.draw(getTexture(),
				getX(),	getY(),
				this.getWidth(),this.getHeight(),
				getSpriteX(), getSpriteY(),
				SPRITE_SIZE, SPRITE_SIZE,
				false,false);	
	}

	/**
	 * Updates the Animation of the Player
	 * Incrememnts the animation count, and decides if the sprite should switch
	 * to the next frame of animation
	 */
	public void animate(){
		mAnimCounter++;

		if(mAnimCounter % mAnimDelay == 0) {

			if(mFrameNum == FRAME_COUNT-1 ) {
				mAnimCounter = 0;
				mFrameNum = 0;
			}
			mFrameNum++;
		}

		if(mStatus == AIR){
			setPosition(getX(), getY()+mVertVelocity);
			mVertVelocity -= GRAVITY;
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
	 * Adds a vertical velocity amount to player's velocity
	 * 
	 * @param int Y velocity you wish to add to current Y velocity
	 */
	public void addVY(int mVertVelocity){
		this.mVertVelocity += mVertVelocity;
	}

	/**
	 * Get the Y position with in the Sprite's Texture wher ethe current
	 * frame of animation is located. 
	 * 
	 * @param int Y velocity you sish to add to current Y velocity
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

	public void physics(ScreenHandler screenHandler){

		if(mVertVelocity < 0 || mStatus == PLATFORM)
		{
			mStatus = AIR;
			Collection<LinkedList<Collidable>> platformLists = screenHandler.getPlatforms().getOnScreen().values();
			for(LinkedList<Collidable> platformList : platformLists){
				for(Collidable platform : platformList){
					if(this.getY() >= platform.getY()+platform.getHeight() && this.getY()+mVertVelocity <= platform.getY()+platform.getHeight()){
						if(this.getX() + this.getWidth() * PLATFORM_CATCH >= platform.getX() - screenHandler.getWorldPosition()
								&& (this.getX() + this.getWidth() * PLATFORM_CATCH <= platform.getX() + platform.getWidth()- screenHandler.getWorldPosition())
								|| (this.getX() + this.getWidth() * (1-PLATFORM_CATCH) >= platform.getX() - screenHandler.getWorldPosition()
								&& this.getX() + this.getWidth() * (1-PLATFORM_CATCH) <= platform.getX() + platform.getWidth() - screenHandler.getWorldPosition())){
							mStatus = PLATFORM;
							mVertVelocity = 0;
						}
					}
				}
			}
		}
		//Check for platforms
		/*for(int i = 0; i < this.getWidth(); i++){
			if(platformLayer.contains((int)(screenHandler.getWorldPosition()+this.getX())+i)){
				for(Platform platform : platformLayer.get((int)(screenHandler.getWorldPosition()+this.getX())+i)){
					if((this.getPixmap().getPixel(i, 0) != 0) && (platform.getPixmap().getPixel(i, 0) != 0)){
						setGround();
					}
				}
			}
		}*/

		//GROUND
		if(mStatus == AIR && getY() < 40){
			mVertVelocity = 0;
			mStatus = GROUND;
			setPosition(getX(),41);
		}

	}

	/**
	 * Player Jumps.  Adds a posiytive velocity to player's vertical velocity
	 */
	public void jump(){
		if(mStatus != AIR){
			addVY(JUMPSPEED);
			mStatus = AIR;
		}
	}
}
