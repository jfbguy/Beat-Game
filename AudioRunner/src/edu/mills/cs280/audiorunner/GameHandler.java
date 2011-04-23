package edu.mills.cs280.audiorunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameHandler implements ApplicationListener {
	private static final float PLAYER_WIDTH = 64;
	private static final float PLAYER_HEIGHT = 64;
	private static final float VOLUME = .01f;

	private SpriteBatch spriteBatch;
	private Player player;
	Music music;
	private ScoreBoard scoreBoard;
	private BoostMeter boostMeter;
	private String trackLocation;
	private boolean touched;
	private ScreenHandler screenHandler;

	public GameHandler(){
	}

	public GameHandler(String musicFile){
		this.trackLocation = musicFile;
	}
	@Override
	public void create() {
		touched = false;

		//Initiate player
		player = new Player(Gdx.graphics.getWidth()*.3f,ScreenHandler.GROUND_HEIGHT,PLAYER_WIDTH,PLAYER_HEIGHT);

		if(trackLocation == null){
			trackLocation = "data/music/Freezepop - Starlight (Karacter Remix).mp3";
			music = Gdx.audio.newMusic (Gdx.files.internal(trackLocation));
		}else{
			music = Gdx.audio.newMusic (Gdx.files.external(MusicData.getFileLocation()));
		}
		

		//Screen Elements
		spriteBatch = new SpriteBatch();
		screenHandler = new ScreenHandler(5,MusicData.getPeaks());
		scoreBoard = new ScoreBoard();
		boostMeter = new BoostMeter();
		Particle.BufferParticles();
		
		
		//Debug
		DebugText.SetupDebugText(spriteBatch);
	}

	@Override
	public void dispose() {
		music.dispose();
	}

	@Override
	public void pause() {
		music.pause();
	}

	@Override
	public void render() {
		TimeHandler.updateTime();
		
		//DebugText.writeText(20, y, text)
		
		if(!music.isPlaying()){
			music.play();
		}
		
		//LEVEL LOGIC
		screenHandler.updateScreen(player);
		boostMeter.updateBoost();


		//Input
		if(Gdx.input.isTouched()){
			if(!player.inAir() ){
				if(touched == false){
					touched = true;
					if(player.rectTouch(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY(),ScreenHandler.getWorldPosition())){
						player.boostJump(scoreBoard,screenHandler,boostMeter);
					}
					else{
						player.jump(scoreBoard,screenHandler,boostMeter);
					}
				}
			}
		}
		else{
			touched = false;
		}


		//PLAYER LOGIC
		player.update(screenHandler,scoreBoard);


		//Clear Screen
		Gdx.graphics.getGL10().glClearColor(0,0,0,1);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		//draw Screen
		screenHandler.draw(spriteBatch, player);


		//draw UI
		scoreBoard.draw(spriteBatch);
		boostMeter.draw(spriteBatch);

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {
		music.notify();
	}

}