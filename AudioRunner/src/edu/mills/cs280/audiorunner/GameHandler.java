package edu.mills.cs280.audiorunner;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameHandler implements ApplicationListener {
	private static final int STARTX = 200;	//Default starting position for player
	private static final int STARTY = 50;
	private static final float VOLUME = .05f;

	private SpriteBatch spriteBatch;
	private Texture texture;
	private Player player;
	private Music music;
	private ScoreBoard scoreBoard;
	private String trackLocation;

	private ScreenHandler screenHandler;
	
	@Override
	public void create() {
		texture = new Texture(Gdx.files.internal("data/runner.png"));
		player = new Player(texture);
		player.setPosition(STARTX, STARTY);
		
		//Music Stuff
		trackLocation = "data/music/Freezepop - Starlight (Karacter Remix).mp3";
		music = Gdx.audio.newMusic (Gdx.files.internal(trackLocation));
		
		//Screen Elements
		spriteBatch = new SpriteBatch();
		screenHandler = new ScreenHandler();
		scoreBoard = new ScoreBoard();
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
		
		//Input
		
		if(Gdx.input.justTouched() && player.onGround()){
			player.jump();
			scoreBoard.addFloaterScore((int)player.getX(),(int)player.getY(),17);
		}
		
		//Physics
		player.physics();
		
		//Clear Screen
		Gdx.graphics.getGL10().glClearColor(0,0,0,1);
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		//LEVEL LOGIC
		screenHandler.updateScreen();
		
		//PLAYER LOGIC
		player.animate();
		
		//draw FarGround
		screenHandler.drawFarGround(spriteBatch);
		
		//draw BackGround
		screenHandler.drawBackGround(spriteBatch);
		
		//draw Ground
		screenHandler.drawGround(spriteBatch);
		
		//draw player
		spriteBatch.begin();
		spriteBatch.draw(player.getTexture(), player.getX(), player.getY(),
				player.getSpriteX(), player.getSpriteY(),
				Player.getSize(), Player.getSize());
		spriteBatch.end();
		
		//draw ForeGround
		screenHandler.drawForeGround(spriteBatch);
		
		//draw ScoreBoard
		scoreBoard.draw(spriteBatch);

	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void resume() {
		music.play();
	}

}
