package edu.mills.cs280.audiorunner;

import java.util.Hashtable;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteLayer {
	private static final int MAX_SPRITE_SIZE = 256;
	private static final int SPEED = 5;

	private Hashtable<Integer,LinkedList<Sprite>> layer;
	private Hashtable<Integer,LinkedList<Sprite>> onScreenLayer;
	private float parallax;

	/**
	 * Constructor
	 */
	public SpriteLayer(){
		layer = new Hashtable<Integer,LinkedList<Sprite>>();
		parallax = 1.0f;
	}

	/**
	 * Constructor
	 * 
	 * @param float Parallax value, higher number means faster scrolling
	 */
	public SpriteLayer(float parallax){
		layer = new Hashtable<Integer,LinkedList<Sprite>>();
		onScreenLayer = new Hashtable<Integer,LinkedList<Sprite>>();
		this.parallax = parallax;
	}

	/**
	 * Sets parallax value
	 * 
	 * @param float Parallax value, higher number means faster scrolling
	 */
	public void setParallax(float parallax){
		this.parallax = parallax;
	}

	/**
	 * Adds sprite to layer at xPosition
	 * 
	 * @param Integer xPosition of added sprite
	 * @param Sprite Sprite to add to layer
	 */
	public void put(Integer xPos,Sprite sprite){
		if(!layer.contains(xPos)){
			layer.put(xPos, new LinkedList<Sprite>());
		}

		if(layer.containsKey(xPos))
			layer.get(xPos).add(sprite);
	}

	/**
	 * Removes list of sprites at xPos from layer
	 * 
	 * @param Integer xPosition of list to remove
	 */
	public void remove(int xPos){
		layer.remove(xPos);
	}

	/**
	 * Checks is there is a list of sprites at xPos in layer and returns true if there is
	 * 
	 * @param Integer xPosition of list to check for
	 * @return Returns true if there is a sprite at xPos oiusution in layer
	 */
	public boolean contains(int xPos){
		if(layer.contains(xPos)){
			return true;
		}
		return false;
	}

	/**
	 * Gets the Linked List of sprites at xPos in layer
	 * 
	 * @param Integer xPosition of linked list to grab
	 * @return Returns list of Sprites in layer at xPos
	 */
	public LinkedList<Sprite> get(int xPos){
		return layer.get(xPos);
	}

	public void loadStart(Vector2 worldPosition){
		int parallaxPosition = (int)(worldPosition.x*parallax);
		for(int i = -MAX_SPRITE_SIZE; i < Gdx.graphics.getWidth(); i++){
			LinkedList<Sprite> temp;
			if(layer.containsKey(i+parallaxPosition)){
				temp = layer.get(i+parallaxPosition);
				onScreenLayer.put(i+parallaxPosition, temp);
			}
		}
	}

	public void draw(SpriteBatch spriteBatch, Vector2 worldPosition){
		LinkedList<Sprite> temp;
		int parallaxPosition = (int)(worldPosition.x*parallax);				

		//Remove past Sprites from Screen
		for(int i = -(SPEED+MAX_SPRITE_SIZE); i < -MAX_SPRITE_SIZE; i++){
			if(onScreenLayer.containsKey(i+parallaxPosition)){
				onScreenLayer.remove(i+parallaxPosition);
			}
		}

		//Add new onScreen Sprites
		for(int i = Gdx.graphics.getWidth()-SPEED; i < Gdx.graphics.getWidth()+SPEED; i++){ 
			if(layer.containsKey(i+parallaxPosition+Gdx.graphics.getWidth())){	
				if(!onScreenLayer.containsKey(i+parallaxPosition+Gdx.graphics.getWidth())){
					temp = layer.get(i+parallaxPosition+Gdx.graphics.getWidth());
					onScreenLayer.put(i+parallaxPosition+Gdx.graphics.getWidth(),temp);
				}
			}
		}

		//Draw Screen
		for(LinkedList<Sprite> list : onScreenLayer.values()){
			for(Sprite sprite : list)
			{
				spriteBatch.draw(sprite.getTexture(),sprite.getX()-parallaxPosition, sprite.getY());
			}
		}
	}

}
