package edu.mills.cs280.audiorunner;

import android.app.Activity;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

/**
 * Main Handler for Game
 * 
 * @author jklein
 *
 */
public class GameHandler implements ApplicationListener {
	OnExitListener onExitListen;
	
	private static final float VOLUME = .01f;

	private SpriteBatch spriteBatch;
	private ImmediateModeRenderer renderer;
	private Player player;
	private static float playerWidth;
	private static float playerHeight;
	private static boolean gameStarted = false;
	Music music;
	private ScoreBoard scoreBoard;
	private BoostMeter boostMeter;
	private String trackLocation;
	private boolean touched;
	private ScreenHandler screenHandler;

	/**
	 * Constructor, set the onExitLIstener
	 * 
	 * @param onExit listener for exiting game
	 */
	public GameHandler(OnExitListener onExit){
		trackLocation = MusicData.getFileLocation();
		onExitListen = onExit;
	}

	
	public void create() {
		touched = false;
		gameStarted = false;

		//Initiate player
		playerWidth = Gdx.graphics.getWidth()*.1f;
		playerHeight = Gdx.graphics.getWidth()*.1f;
		player = new Player(Gdx.graphics.getWidth()*.3f,ScreenHandler.GROUND_HEIGHT,playerWidth,playerHeight);

		if(trackLocation == null){
			trackLocation = "data/music/Freezepop - Starlight (Karacter Remix).mp3";
			//trackLocation = "/mnt/sdcard/music/Freezepop - Starlight (Karacter Remix).mp3";
			music = Gdx.audio.newMusic (Gdx.files.internal(trackLocation));
		}else{
			music = Gdx.audio.newMusic (Gdx.files.external(MusicData.getFileLocation()));
		}
		
		MusicData.setMusic(music);
		MusicVisualizer.setupMusicVisualizer();
		music.setLooping(false);

		//Screen Elements
		spriteBatch = new SpriteBatch();
		screenHandler = new ScreenHandler(5);
		scoreBoard = new ScoreBoard();
		boostMeter = new BoostMeter();
		renderer = new ImmediateModeRenderer();
		Particle.BufferParticles();
		
		//Debug
		DebugText.SetupDebugText(spriteBatch);
	}

	
	public void dispose() {
		music.dispose();
	}

	
	public void pause() {
		music.pause();
	}

	
	public void render() {
			
		if(!music.isPlaying()){
			if(gameStarted){
				MusicData.setScore(scoreBoard.getScore());
				onExitListen.onExit();
			}
			else{
				gameStarted = true;
				music.play();
			}
		}
		
		try{
		
		TimeHandler.updateTime();
		MusicData.update(screenHandler);
		
		//LEVEL LOGIC
		screenHandler.updateScreen(player);
		boostMeter.updateBoost();


		//Input
		if(Gdx.input.isTouched()){
			if(!player.inAir() ){
				if(touched == false){
					touched = true;
					if(player.rectTouch(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY(),MusicData.getPosition(),ScreenHandler.getWorldPosition().y)){
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
		
		//draw visualizer
		MusicVisualizer.draw(spriteBatch);
		
		//draw Screen
		screenHandler.draw(spriteBatch, player);


		//draw UI
		scoreBoard.draw(spriteBatch);
		boostMeter.draw(spriteBatch);
		
		//DEBUG**************
		int yPos = Gdx.graphics.getHeight();
		DebugText.writeText(50,yPos-20,"Song Time: " + Float.toString(MusicData.getPosition()));
		DebugText.writeText(50,yPos-40,"FrameDuration: " + Float.toString(MusicData.getFrameDuration()));
		DebugText.writeText(50,yPos-60,"Peak #: " + Float.toString(MusicData.getPeaks().size()));
		DebugText.writeText(50,yPos-80,"Platforms Loaded Up To: " + Float.toString(MusicData.getPeaks().size()*MusicData.getFrameDuration()));
		DebugText.writeText(50,yPos-100,"FPS: " + Float.toString(Gdx.graphics.getFramesPerSecond()));
		}
		catch(Exception e){
			
		}
	}

	
	public void resize(int width, int height) {

	}

	
	public void resume() {
	}

}
