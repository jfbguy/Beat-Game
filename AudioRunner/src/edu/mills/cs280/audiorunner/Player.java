package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends Sprite {


	private static final int FRAME_COUNT = 6;
	private static final int SPRITE_SIZE = 64;
	private static final float GRAVITY = 0.5f;
	private static final int JUMPSPEED = 10;
	private static final int GROUND = 0;
	private static final int AIR = 1;

	private int anim_countdown = 10;
	private float vy;
	private int animCount,animNum;
	private int status;

	/**
	 * Constructor
	 * 
	 * @param  Texture, Holds The Sprite Texture for player
	 */
	public Player(Texture texture) {
		this.setTexture(texture);
		vy = 0;
		anim_countdown = 25/ScreenHandler.getSpeed();
		animCount = 0;
		animNum = 0;
	}

	/**
	 * Draw player's current frame of animation
	 * 
	 * @param  SpriteBatch, Draw within the current SpriteBatch
	 */
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.begin();
		spriteBatch.draw(getTexture(), getX(), getY(),
				getSpriteX(), getSpriteY(),
				Player.getSize(), Player.getSize());
		spriteBatch.end();
	}

	/**
	 * Updates the Animation of the Player
	 * Incrememnts the animation count, and decides if the sprite should switch
	 * to the next frame of animation
	 */
	public void animate(){
		animCount++;

		if(animCount % anim_countdown == 0) {

			if(animNum == FRAME_COUNT-1 ) {
				animCount = 0;
				animNum = 0;
			}
			animNum++;
		}

		if(status == AIR){
			setPosition(getX(), getY()+vy);
			vy -= GRAVITY;
		}
	}

	/**
	 * Get the X position with in the Sprite's Texture where the current
	 * frame of animation is located. 
	 * 
	 * @return int current X position within the sprite's texture
	 */
	public int getSpriteX(){
		return animNum*SPRITE_SIZE;
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
	 * Adds a vertical velocty amount to player's velocity
	 * 
	 * @param int Y velocity you wish to add to current Y velocity
	 */
	public void addVY(int vy){
		this.vy += vy;
	}

	/**
	 * Get the Y position with in the Sprite's Texture wher ethe current
	 * frame of animation is located. 
	 * 
	 * @param int Y velocity you sish to add to current Y velocity
	 */
	public void setVY(int vy){
		this.vy = vy;
	}

	/**
	 * Gets the current texture dimension size in pixels
	 * 
	 * @return int the size of player's sprite, EX: 64 would be a 64x64 Sprite
	 */
	public static int getSize()
	{
		return SPRITE_SIZE;
	}

	/**
	 * Set player's status to GROUND.  The player will now stop falling
	 */
	public void setGround(){
		status = GROUND;
	}

	/**
	 * Set player's status to AIR.  The player will now be in the air, gravity will effect the player
	 */
	public void setAir(){
		status = AIR;
	}

	/**
	 * Calculate Physics on player, Gravity always effects player if they are in the air
	 */
	public void physics(){
		if(status == AIR && getY() < 40){
			vy = 0;
			status = GROUND;
			setPosition(getX(),41);
		}

	}

	/**
	 * Player Jumps.  Adds a posiytive velocity to player's vertical velocity
	 */
	public void jump(){
		if(status != AIR){
			addVY(JUMPSPEED);
			status = AIR;
		}
	}
}
