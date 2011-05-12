package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Debugging Class for print text overlay on top of screen
 * 
 * @author jklein
 */
public class DebugText {
	private static BitmapFont font;
	private static SpriteBatch spriteBatch;
	private static Boolean activated = false;
	
	private DebugText(){
		
	}
	
	/**
	 * Initializes the static class
	 * 
	 * @param spriteBatchIN
	 */
	public static void SetupDebugText(SpriteBatch spriteBatchIN){
		font = new BitmapFont();
		spriteBatch = spriteBatchIN;
		activated = true;
	}
	
	/**
	 * 
	 * 
	 * @param x xPosition to write text on screen
	 * @param y yPosition to write text on screen
	 * @param text text to write on screen
	 */
	public static void writeText(int x, int y, String text){
		if(activated){
			spriteBatch.begin();
			font.setColor(1,0,0,1);
            font.draw(spriteBatch, text, x, y);
            spriteBatch.end();
		}
	}
}
