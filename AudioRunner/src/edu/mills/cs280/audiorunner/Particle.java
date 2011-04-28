package edu.mills.cs280.audiorunner;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Particle{
	private final static float SPEED = .5f;
	private final static float GRAVITY = -.6f;
	public static final int FALLING = 0;
	public static final int DIRECTIONAL = 1;
	public static final int EXPLODING = 2;

	//Explosion Constants
	private static final float EXPLOSION_SPREAD = .1f;
	private static final float EXPLOSION_FADE = .05f;
	private static final float EXPLOSION_GROWTH = .05f;
	private static final float EXPLOSION_PARTICLE_SIZE = .01f;	//% of Screen
	private static final int EXPLOSION_PARTICLE_AMOUNT = 100;
	private static final float FADE_OUT_THRESHOLD = .03f;	//Value of alpha when to delete particle
	
	//Falling Constants
	private static final int FALLING_SPREAD = (int)(Gdx.graphics.getWidth()*.01f);
	private static final float FALLING_PARTICLE_SIZE = Gdx.graphics.getWidth()*.005f;

	private static Stack<Particle> PARTICLE_BUFFER = new Stack<Particle>();
	private static LinkedList<Particle> PARTICLES = new LinkedList<Particle>();
	private static Texture PARTICLE_TEXTURE = new Texture(Gdx.files.internal("data/particle.png"));
	//private static 

	private float x, y;
	private float vX, vY;
	private float targetX,targetY;
	private int type;
	private float size;
	private float alpha;
	private Texture mTexture;

	private Particle(){

	}

	public static void BufferParticles(){
		for(int i = 0; i < 2000; i++){
			PARTICLE_BUFFER.add(new Particle());
		}
	}

	//Directional & Falling Particles
	/*private Particle(float x, float y,float targetX, float targetY, float size, int type){
		switch(type){
		case DIRECTIONAL:
			break;
		case FALLING:
			break;
		case EXPLODING:
			break;
		}
		this.setTexture(PARTICLE_TEXTURE);
		this.x = x;
		this.y = y;
		this.targetX = targetX;
		this.targetY = targetY;
		this.size = size;
		this.type = type;
		alpha = 1;
	}*/

	//Directional & Falling Particles
	private static void AddParticle(float x, float y,float targetX, float targetY, float size, int type){
		Particle add = PARTICLE_BUFFER.pop();
		switch(type){
		case DIRECTIONAL:
			break;
		case FALLING:
			break;
		case EXPLODING:
			break;
		}
		add.setTexture(PARTICLE_TEXTURE);
		add.x = x;
		add.y = y;
		add.targetX = targetX;
		add.targetY = targetY;
		add.size = size;
		add.type = type;
		add.alpha = 1;

		PARTICLES.add(add);
	}

	public static void updateParticles(){
		float spdScale = TimeHandler.getTransitionScale();

		Iterator<Particle> iter = PARTICLES.iterator();
		while(iter.hasNext()){
			Particle p = iter.next();
			switch(p.type){
			case DIRECTIONAL:
				double moveAngle = Math.atan2((p.targetY - p.getY()),(p.targetX - p.getX()));
				p.setPosition((float)(p.getX()+(Math.cos(moveAngle)*spdScale)),
						(float)(p.getY()+(Math.sin(moveAngle)*spdScale)));

				if(Math.abs(p.targetX - p.getX()) < SPEED && Math.abs(p.targetY - p.getY()) < SPEED)
				{
					PARTICLE_BUFFER.push(p);
					iter.remove();
				}
				break;
			case FALLING:
				if(ScreenHandler.onScreen(p.getX(),p.getY())){
					p.targetY += GRAVITY*spdScale;	//gravity
					p.setPosition((p.getX() + p.targetX),(p.getY() + p.targetY));
				}
				else{
					PARTICLE_BUFFER.push(p);
					iter.remove();
				}
				break;
			case EXPLODING:
				if(p.alpha < FADE_OUT_THRESHOLD || !ScreenHandler.onScreen(p.getX(),p.getY())){
					PARTICLE_BUFFER.push(p);
					iter.remove();
				}
				else{
					p.targetX *= (1-EXPLOSION_SPREAD*spdScale);
					p.targetY *= (1-EXPLOSION_SPREAD*spdScale);
					p.size *= (1+EXPLOSION_GROWTH*spdScale);
					p.setPosition((p.getX() + p.targetX),(p.getY() + p.targetY));
					p.alpha *= (1-EXPLOSION_FADE*spdScale);
				}
				break;
			}

		}


	}

	public static void createExplosion(float x, float y){
		Random rand = new Random();
		for(int i = 0; i < EXPLOSION_PARTICLE_AMOUNT; i++){
			AddParticle(
					(int)x,(int)y,
					rand.nextFloat()*(10)-5,rand.nextFloat()*(10)-5,
					(float)Gdx.graphics.getHeight()*EXPLOSION_PARTICLE_SIZE,Particle.EXPLODING);
		}
	}

	public static void createJumpParticles(Player player, int jumpscore){
		Random rand = new Random();
		
		int randX = 0;
		int randY = 0;
		for(int i = 0; i < jumpscore; i++){
			randX = rand.nextInt(FALLING_SPREAD);
			randY = rand.nextInt(FALLING_SPREAD);
			AddParticle(
					(int)(player.getX()+player.getWidth()/2),(int)player.getY(), 
					randX-FALLING_SPREAD/2,randY+FALLING_SPREAD/2,
					FALLING_PARTICLE_SIZE, Particle.FALLING);
		}
	}

	public static void draw(SpriteBatch spriteBatch, Vector2 worldPosition){
		spriteBatch.begin();
		Iterator<Particle> iter = PARTICLES.iterator();
		while(iter.hasNext()){
			Particle p = iter.next();
			spriteBatch.setColor(1.0f, 1.0f, 1.0f, p.alpha);	//set transperancy of particle
			switch(p.type){
			case FALLING:
				spriteBatch.draw(p.getTexture(),
						p.getX()-MusicData.getPosition(),p.getY()-worldPosition.y,
						p.size,p.size,
						0, 0,
						p.getTexture().getWidth(), p.getTexture().getHeight(),
						false,false);
				break;
			case EXPLODING:
				spriteBatch.draw(p.getTexture(),
						p.getX()-MusicData.getPosition(),p.getY()-worldPosition.y,
						p.size,p.size,
						0, 0,
						p.getTexture().getWidth(), p.getTexture().getHeight(),
						false,false);
				break;
			}
		}
		spriteBatch.end();
		spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);	//reset transperancies to normal
	}

	public void setTexture(Texture texture){
		this.mTexture = texture;
	}

	public Texture getTexture(){
		return this.mTexture;
	}

	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}
}