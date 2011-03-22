package edu.mills.cs280.audiorunner;

import java.util.Hashtable;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlatformLayer {
	private static final int MAX_SPRITE_SIZE = 256;
	private static final int SPEED = 5;

	private Hashtable<Integer,LinkedList<Platform>> layer;
	private Hashtable<Integer,LinkedList<Platform>> onScreenLayer;
	private float parallax;

	/**
	 * Constructor
	 */
	public PlatformLayer(){
		layer = new Hashtable<Integer,LinkedList<Platform>>();
		parallax = 1.0f;
	}

	/**
	 * Constructor
	 * 
	 * @param float Parallax value, higher number means faster scrolling
	 */
	public PlatformLayer(float parallax){
		layer = new Hashtable<Integer,LinkedList<Platform>>();
		onScreenLayer = new Hashtable<Integer,LinkedList<Platform>>();
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
	 * Adds platform to layer at xPosition
	 * 
	 * @param Integer xPosition of added platform
	 * @param Platform Platform to add to layer
	 */
	public void put(Integer xPos,Platform platform){
		if(!layer.contains(xPos)){
			layer.put(xPos, new LinkedList<Platform>());
		}

		layer.get(xPos).add(platform);
	}

	/**
	 * Removes list of platforms at xPos from layer
	 * 
	 * @param Integer xPosition of list to remove
	 */
	public void remove(int xPos){
		layer.remove(xPos);
	}

	/**
	 * Checks is there is a list of platforms at xPos in layer and returns true if there is
	 * 
	 * @param Integer xPosition of list to check for
	 * @return Returns true if there is a platform at xPos in layer
	 */
	public boolean contains(int xPos){
		if(layer.contains(xPos)){
			return true;
		}
		return false;
	}

	/**
	 * Gets the Linked List of platforms at xPos in layer
	 * 
	 * @param Integer xPosition of linked list to grab
	 * @return Returns list of Platforms in layer at xPos
	 */
	public LinkedList<Platform> get(int xPos){
		return layer.get(xPos);
	}
	
	public Hashtable<Integer,LinkedList<Platform>> getOnScreen(){
		return onScreenLayer;
	}

	public void loadStart(int worldPosition){
		int parallaxPosition = (int)(worldPosition*parallax);
		for(int i = -MAX_SPRITE_SIZE; i < Gdx.graphics.getWidth(); i++){
			LinkedList<Platform> temp;
			if(layer.containsKey(i+parallaxPosition)){
				temp = layer.get(i+parallaxPosition);
				onScreenLayer.put(i+parallaxPosition, temp);
			}
		}
	}

	public void draw(SpriteBatch spriteBatch, int worldPosition){
		LinkedList<Platform> temp;
		int parallaxPosition = (int)(worldPosition*parallax);				

		//Remove past Platforms from Screen
		for(int i = -(SPEED+MAX_SPRITE_SIZE); i < -MAX_SPRITE_SIZE; i++){
			if(onScreenLayer.containsKey(i+parallaxPosition)){
				onScreenLayer.remove(i+parallaxPosition);
			}
		}

		//Add new onScreen Platforms
		for(int i = Gdx.graphics.getWidth()-SPEED; i < Gdx.graphics.getWidth()+SPEED; i++){ 
			if(layer.containsKey(i+parallaxPosition+Gdx.graphics.getWidth())){	
				if(!onScreenLayer.containsKey(i+parallaxPosition+Gdx.graphics.getWidth())){
					temp = layer.get(i+parallaxPosition+Gdx.graphics.getWidth());
					onScreenLayer.put(i+parallaxPosition+Gdx.graphics.getWidth(),temp);
				}
			}
		}

		//Draw Screen
		for(LinkedList<Platform> list : onScreenLayer.values()){
			for(Platform platform : list)
			{
				spriteBatch.draw(platform.getTexture(),
						(float) (platform.getX()-parallaxPosition),							//Draw at X position
						(float) (platform.getY()),											//Draw at Y position
						platform.getWidth(),platform.getHeight(),							//Size of platform
						0, 0,																//Get part of texture
						platform.getTexture().getWidth(), platform.getTexture().getHeight(),//Size of gotten part
						false,false);	
			}
		}
	}

}