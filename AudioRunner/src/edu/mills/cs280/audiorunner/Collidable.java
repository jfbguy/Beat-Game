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

	public boolean collides(Collidable collidable){

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

		Vector2 bottomLeft = new Vector2();
		Vector2 topRight = new Vector2();

		//Check Overlapping Rectangle
		if(this.getX() < collidable.getX()){
			bottomLeft.x = (int) collidable.getX();
			topRight.x = (int) this.getX();
		}
		else{
			bottomLeft.x = (int) this.getX();
			topRight.x = (int) collidable.getX();
		}

		if(this.getY() < collidable.getY()){
			bottomLeft.y = (int) collidable.getY();
			topRight.y = (int) this.getY();
		}
		else{
			bottomLeft.y = (int) this.getY();
			topRight.y = (int) collidable.getY();
		}

		for(int i = bottomLeft.x; i < topRight.x; i++){
			for(int j = bottomLeft.y; j < topRight.y; i++){
				if((this.getPixmap().getPixel(i, j) != 0) && (collidable.getPixmap().getPixel(i, j) != 0)){
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

	public boolean pixelCollide(){


		return false;
	}
}
