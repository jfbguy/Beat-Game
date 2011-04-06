package edu.mills.cs280.audiorunner;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

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
	private static final float EXPLOSION_SPREAD = .2f;
	private static final float EXPLOSION_FADE = .05f;
	private static final float EXPLOSION_GROWTH = .1f;
	private static final float EXPLOSION_PARTICLE_SIZE = .01f;	//% of Screen
	private static final int EXPLOSION_PARTICLE_AMOUNT = 30;
	private static final float FADE_OUT_THRESHOLD = .03f;	//Value of alpha when to delete particle

	//private static ArrayList<Particle> 
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

	//Directional & Falling Particles
	private Particle(float x, float y,float targetX, float targetY, float size, int type){
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
	}

	public static void updateParticles(){
		float spdScale = MusicHandler.getTransitionScale();

		if(spdScale != 0){
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
						iter.remove();
					}
					break;
				case FALLING:
					if(ScreenHandler.onScreen(p.getX(),p.getY())){
						p.targetY += GRAVITY*spdScale;	//gravity
						p.setPosition((p.getX() + p.targetX),(p.getY() + p.targetY));
					}
					else{
						iter.remove();
					}
					break;
				case EXPLODING:
					if(p.alpha < FADE_OUT_THRESHOLD || !ScreenHandler.onScreen(p.getX(),p.getY())){
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


	}

	public static void createExplosion(float x, float y){
		Random rand = new Random();
		for(int i = 0; i < EXPLOSION_PARTICLE_AMOUNT; i++){
			PARTICLES.add(new Particle(
					(int)x,(int)y,
					rand.nextFloat()*(10)-5,rand.nextFloat()*(10)-5,
					(float)Gdx.graphics.getHeight()*EXPLOSION_PARTICLE_SIZE,Particle.EXPLODING));
		}
	}

	public static void createJumpParticles(Player player){
		Random rand = new Random();
		for(int i = 0; i < 10; i++){
			PARTICLES.add(new Particle(
					(int)(player.getX()+player.getWidth()/2),(int)player.getY(), 
					rand.nextInt(10)-5,rand.nextInt(5)+5,
					(float)Gdx.graphics.getHeight()*.01f, Particle.FALLING));
		}
	}

	public static void draw(SpriteBatch spriteBatch, Vector2 worldPosition){

		//Gdx.gl.glEnable(GL10.GL_BLEND);
		//Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		spriteBatch.begin();
		Iterator<Particle> iter = PARTICLES.iterator();
		while(iter.hasNext()){
			Particle p = iter.next();
			spriteBatch.setColor(1.0f, 1.0f, 1.0f, p.alpha);	//set transperancy of particle
			switch(p.type){
			case FALLING:
				spriteBatch.draw(p.getTexture(),
						p.getX()-worldPosition.x,p.getY()-worldPosition.y,
						p.size,p.size,
						0, 0,
						p.getTexture().getWidth(), p.getTexture().getHeight(),
						false,false);
				break;
			case EXPLODING:
				spriteBatch.draw(p.getTexture(),
						p.getX()-worldPosition.x,p.getY()-worldPosition.y,
						p.size,p.size,
						0, 0,
						p.getTexture().getWidth(), p.getTexture().getHeight(),
						false,false);
				break;
			}
		}
		spriteBatch.end();
		spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);	//reset transperancies to normal

		//DEBUG!!!
		System.out.println("# of Particles: " + PARTICLES.size());
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