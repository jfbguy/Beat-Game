package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Collidable extends Sprite{
	Pixmap pixmap;

	public Collidable(){
	}

	public Collidable(String file){
		this.pixmap = new Pixmap(Gdx.files.internal(file));
	}

	public void loadTexture(String file){
		this.pixmap = new Pixmap(Gdx.files.internal(file));
		this.setTexture(new Texture(Gdx.files.internal(file)));
	}

	public Pixmap getPixmap(){
		return pixmap;
	}

	public boolean pixelCollides(Collidable collidable){
		// Rectangle Check
		if (this.getX() > collidable.getX() + collidable.getWidth()){
			return(false);
		}
		if (this.getX() + this.getWidth() < collidable.getX()){
			return(false);
		}
		if (this.getY() > collidable.getY() + collidable.getHeight()){
			return(false);
		}
		if (this.getY() + this.getHeight() < collidable.getY()){
			return(false);
		}
		
		// Find the bounds of the rectangle intersection
		int top = (int) Math.min(this.getY()+this.getHeight(), collidable.getY()+collidable.getHeight());
		int bottom = (int) Math.max(this.getY(), collidable.getY());
		int left = (int) Math.max(this.getX(), collidable.getX());
		int right = (int) Math.min(this.getX()+this.getWidth(), collidable.getX()+collidable.getWidth());

		for(int i = left; i < right; i++){
			for(int j = bottom; j < top; j++){
				if(this.getPixmap().getPixel((int)(i-this.getX()), (int)(j-this.getY())) != 0 &&
				collidable.getPixmap().getPixel((int)(i-collidable.getX()), (int)(j-collidable.getY())) != 0){
					return true;
				}
			}
		}

		return false;
	}
	
	public boolean rectCollides(Collidable collidable, int worldPosition){


		// Rectangle Check
		if (this.getX() > collidable.getX() + collidable.getWidth() - worldPosition){
			return(false);
		}
		if (this.getX() + this.getWidth() < collidable.getX() - worldPosition){
			return(false);
		}
		if (this.getY() > collidable.getY() + collidable.getHeight()){
			return(false);
		}
		if (this.getY() + this.getHeight() < collidable.getY()){
			return(false);
		}
		
		return true;
	}
	
	public boolean rectTouch(int x, int y, Vector2 worldPosition){
		// Rectangle Check
		if (this.getX() - worldPosition.x > x ){
			return(false);
		}
		if (this.getX() + this.getWidth() - worldPosition.x < x){
			return(false);
		}
		if (this.getY() - worldPosition.y > y){
			return(false);
		}
		if (this.getY() + this.getHeight() - worldPosition.y < y){
			return(false);
		}
		
		return true;
	}
}
