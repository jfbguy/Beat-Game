package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DebugText {
	private static BitmapFont font;
	private static SpriteBatch spriteBatch;
	private static Boolean activated = false;
	
	private DebugText(){
		
	}
	
	public static void SetupDebugText(SpriteBatch spriteBatchIN){
		font = new BitmapFont();
		spriteBatch = spriteBatchIN;
		activated = true;
	}
	
	public static void writeText(int x, int y, String text){
		if(activated){
			spriteBatch.begin();
			font.setColor(1,0,0,1);
            font.draw(spriteBatch, text, x, y);
            spriteBatch.end();
		}
	}
}
