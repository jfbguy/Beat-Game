package edu.mills.cs280.audiorunner;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Particle extends Sprite{
	public final static float SPEED = .5f;
	public final static float GRAVITY = -.3f;
	public static final int FALLING = 0;
	public static final int DIRECTIONAL = 1;
	public static final int EXPLODING = 2;

	float targetX,targetY;
	int type;

	public Particle(Texture texture, Vector2 position,Vector2 target, int type){
		this.setTexture(texture);
		this.setBounds(position.x, position.y, 3, 3);
		this.targetX = target.getX();
		this.targetY = target.getY();
		this.type = type;
	}

	public static void updateParticles(LinkedList<Particle> particles){
		Iterator<Particle> iter = particles.iterator();
		while(iter.hasNext()){
			Particle p = iter.next();
			switch(p.type){
			case DIRECTIONAL:
					double moveAngle = Math.atan2((p.targetY - p.getY()),(p.targetX - p.getX()));
					p.setPosition((float)(p.getX()+(Math.cos(moveAngle)*SPEED)),
							(float)(p.getY()+(Math.sin(moveAngle)*SPEED)));

					if(Math.abs(p.targetX - p.getX()) < SPEED && Math.abs(p.targetY - p.getY()) < SPEED)
					{
						iter.remove();
					}
				break;
			case FALLING:
				if(ScreenHandler.onScreen(p.getX(),p.getY())){
					p.targetY += GRAVITY;	//gravity
					p.setPosition(p.getX() + p.targetX,p.getY() + p.targetY);
				}
				else{
					iter.remove();
				}
				break;
			case EXPLODING:
				if(p.targetX < 1 && p.targetY < 1){
					iter.remove();
				}
				else if(!ScreenHandler.onScreen(p.getX(),p.getY())){
					iter.remove();
				}
				else{
					p.targetX *= .9f;
					p.targetY *= .9f;
					p.setPosition(p.getX() + p.targetX,p.getY() + p.targetY);
				}
				break;
			}
		}
		
		
	}
	
	public static void draw(LinkedList<Particle> particles, SpriteBatch spriteBatch, int worldPosition){
		Iterator<Particle> iter = particles.iterator();
		while(iter.hasNext()){
			Particle p = iter.next();
			spriteBatch.draw(p.getTexture(),
					p.getX()-worldPosition,p.getY(),
					p.getWidth(),p.getHeight(),
					0, 0,
					10, 10,
					false,false);	
			
			int debug = 0;
			debug++;
		}
	}
}