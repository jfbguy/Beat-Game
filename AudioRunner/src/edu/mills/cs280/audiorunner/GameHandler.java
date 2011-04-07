package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameHandler implements ApplicationListener {
	private static final float STARTX = 200.0f;	//Default starting position for player
	private static final float STARTY = 50.0f;
	private static final float PLAYER_WIDTH = 64;
	private static final float PLAYER_HEIGHT = 64;
	private static final float VOLUME = .01f;

	private SpriteBatch spriteBatch;
	private Player player;
	private Music music;
	private ScoreBoard scoreBoard;
	private BoostMeter boostMeter;
	private String trackLocation;
	private boolean touched;

	private ScreenHandler screenHandler;

	@Override
	public void create() {
		touched = false;

		//Initiate player
		player = new Player("data/runner.png",STARTX,STARTY,PLAYER_WIDTH,PLAYER_HEIGHT);
		player.setPosition(STARTX, STARTY);

		//Music Stuff
		trackLocation = "data/music/Freezepop - Starlight (Karacter Remix).mp3";
		music = Gdx.audio.newMusic (Gdx.files.internal(trackLocation));
		MusicHandler.setMusic(music);

		//Screen Elements
		spriteBatch = new SpriteBatch();
		screenHandler = new ScreenHandler(5);
		scoreBoard = new ScoreBoard();
		boostMeter = new BoostMeter();
		Particle.BufferParticles();
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
		//Play Music
		if(!music.isPlaying()){
			music.setVolume(VOLUME);	//volume should be set by settings
			music.play();
		}
		MusicHandler.updateTime();

		if(MusicHandler.getTransitionScale() != 0){

			//LEVEL LOGIC
			screenHandler.updateScreen();
			boostMeter.updateBoost();

			//Physics
			player.update(screenHandler,scoreBoard);

			//PLAYER LOGIC
			player.animate();

			//Input
			if(Gdx.input.isTouched()){
				if(!player.inAir() ){
					if(touched == false){
						touched = true;

						scoreBoard.jumpScoring(player,screenHandler,boostMeter);
					}
				}
			}
			else{
				touched = false;
			}

			//Clear Screen
			Gdx.graphics.getGL10().glClearColor(0,0,0,1);
			Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

			//draw Screen
			screenHandler.draw(spriteBatch, player);

			//draw UI
			scoreBoard.draw(spriteBatch);
			boostMeter.draw(spriteBatch);
		}
		else{
			int debug = 0;
			debug++;
		}

		//DEBUG TESTS
		System.out.println("FPS: "+Gdx.graphics.getFramesPerSecond());

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {
		music.play();
	}

}
