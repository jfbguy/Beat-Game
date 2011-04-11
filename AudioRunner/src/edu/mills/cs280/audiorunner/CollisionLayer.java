package edu.mills.cs280.audiorunner;

import java.util.Hashtable;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CollisionLayer {
	private static final int MAX_SPRITE_SIZE = 256;
	private static final int SPEED = 5;

	private Hashtable<Integer,LinkedList<Collidable>> layer;
	private Hashtable<Integer,LinkedList<Collidable>> onScreenLayer;
	private float parallax;

	/**
	 * Constructor
	 */
	public CollisionLayer(){
		layer = new Hashtable<Integer,LinkedList<Collidable>>();
		parallax = 1.0f;
	}

	/**
	 * Constructor
	 * 
	 * @param float Parallax value, higher number means faster scrolling
	 */
	public CollisionLayer(float parallax){
		layer = new Hashtable<Integer,LinkedList<Collidable>>();
		onScreenLayer = new Hashtable<Integer,LinkedList<Collidable>>();
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
	 * Adds collidable to layer at xPosition
	 * 
	 * @param Integer xPosition of added collidable
	 * @param Platform Platform to add to layer
	 */
	public void put(Integer xPos,Collidable collidable){
		if(!layer.contains(xPos)){
			layer.put(xPos, new LinkedList<Collidable>());
		}

		layer.get(xPos).add(collidable);
	}

	/**
	 * Removes list of collidables at xPos from layer
	 * 
	 * @param Integer xPosition of list to remove
	 */
	public void remove(int xPos){
		layer.remove(xPos);
	}

	/**
	 * Checks is there is a list of collidables at xPos in layer and returns true if there is
	 * 
	 * @param Integer xPosition of list to check for
	 * @return Returns true if there is a collidable at xPos in layer
	 */
	public boolean contains(int xPos){
		if(layer.contains(xPos)){
			return true;
		}
		return false;
	}

	/**
	 * Gets the Linked List of collidables at xPos in layer
	 * 
	 * @param Integer xPosition of linked list to grab
	 * @return Returns list of Platforms in layer at xPos
	 */
	public LinkedList<Collidable> get(int xPos){
		return layer.get(xPos);
	}
	
	public Hashtable<Integer,LinkedList<Collidable>> getOnScreen(){
		return onScreenLayer;
	}

	public void loadStart(int worldPosition){
		int parallaxPosition = (int)(worldPosition*parallax);
		for(int i = -MAX_SPRITE_SIZE; i < Gdx.graphics.getWidth(); i++){
			LinkedList<Collidable> temp;
			if(layer.containsKey(i+parallaxPosition)){
				temp = layer.get(i+parallaxPosition);
				onScreenLayer.put(i+parallaxPosition, temp);
			}
		}
	}

	public void draw(SpriteBatch spriteBatch, Vector2 worldPosition){
		LinkedList<Collidable> temp;
		int parallaxPositionX = (int)(worldPosition.x*parallax);
		int parallaxPositionY = (int)(worldPosition.y*parallax);	

		//Remove past Platforms from Screen
		for(int i = -(SPEED+MAX_SPRITE_SIZE); i < -MAX_SPRITE_SIZE; i++){
			if(onScreenLayer.containsKey(i+parallaxPositionX)){
				onScreenLayer.remove(i+parallaxPositionX);
			}
		}

		//Add new onScreen Platforms
		for(int i = Gdx.graphics.getWidth()-SPEED; i < Gdx.graphics.getWidth()+SPEED; i++){ 
			if(layer.containsKey(i+parallaxPositionX+Gdx.graphics.getWidth())){	
				if(!onScreenLayer.containsKey(i+parallaxPositionX+Gdx.graphics.getWidth())){
					temp = layer.get(i+parallaxPositionX+Gdx.graphics.getWidth());
					onScreenLayer.put(i+parallaxPositionX+Gdx.graphics.getWidth(),temp);
				}
			}
		}

		spriteBatch.begin();
		//Draw Screen
		for(LinkedList<Collidable> list : onScreenLayer.values()){
			for(Collidable collidable : list)
			{
				spriteBatch.draw(collidable.getTexture(),
						(float) (collidable.getX()-parallaxPositionX),							//Draw at X position
						(float) (collidable.getY()-parallaxPositionY),											//Draw at Y position
						collidable.getWidth(),collidable.getHeight(),							//Size of collidable
						0, 0,																//Get part of texture
						collidable.getTexture().getWidth(), collidable.getTexture().getHeight(),//Size of gotten part
						false,false);	
			}
		}
		spriteBatch.end();
	}

}