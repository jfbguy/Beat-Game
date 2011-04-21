package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameHandler implements ApplicationListener {
	private static final float PLAYER_WIDTH = 64;
	private static final float PLAYER_HEIGHT = 64;
	private static final float VOLUME = .01f;

	private SpriteBatch spriteBatch;
	private Player player;
	private ScoreBoard scoreBoard;
	private BoostMeter boostMeter;
	private boolean touched;
	private ScreenHandler screenHandler;
	private AudioAnalyzer audioAnalyzer;

	public GameHandler(){
	}

	public GameHandler(String musicFile){
		MusicData.setFile(musicFile);
	}
	@Override
	public void create() {

		touched = false;

		//Initiate player
		player = new Player("data/runner.png",Gdx.graphics.getWidth()*.3f,ScreenHandler.GROUND_HEIGHT,PLAYER_WIDTH,PLAYER_HEIGHT);

		if(MusicData.getFileLocation() == null){
			MusicData.setFile("data/music/Freezepop - Starlight (Karacter Remix).mp3");
			MusicData.music = Gdx.audio.newMusic (Gdx.files.internal(MusicData.getFileLocation()));
		}else{
			MusicData.music = Gdx.audio.newMusic (Gdx.files.external(MusicData.getFileLocation()));
		}
		
		audioAnalyzer = new AudioAnalyzer(MusicData.getFileLocation());



		//Screen Elements
		spriteBatch = new SpriteBatch();
		screenHandler = new ScreenHandler(5,MusicData.getPeaks());
		scoreBoard = new ScoreBoard();
		boostMeter = new BoostMeter();
		Particle.BufferParticles();
	}

	@Override
	public void dispose() {
		MusicData.music.dispose();
	}

	@Override
	public void pause() {
		MusicData.music.pause();
	}

	@Override
	public void render() {
		TimeHandler.updateTime();
		
		if(!MusicData.music.isPlaying()){
			MusicData.music.play();
		}
		
		screenHandler.addPlatforms(audioAnalyzer.returnPeaks(TimeHandler.getTimePerFrame()));
		
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
		MusicData.music.play();
	}

}