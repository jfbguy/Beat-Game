package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

public class GameHandler implements ApplicationListener {
	private static final float VOLUME = .01f;

	private SpriteBatch spriteBatch;
	private ImmediateModeRenderer renderer;
	private Player player;
	private static float playerWidth;
	private static float playerHeight;
	Music music;
	private ScoreBoard scoreBoard;
	private BoostMeter boostMeter;
	private String trackLocation;
	private boolean touched;
	private ScreenHandler screenHandler;
	

	public GameHandler(){
		trackLocation = MusicData.getFileLocation();
	}

	@Override
	public void create() {
		touched = false;

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
		if(!music.isPlaying()){
			music.play();
		}
		
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
		MusicVisualizer.draw(renderer);
		
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

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {
	}

}
