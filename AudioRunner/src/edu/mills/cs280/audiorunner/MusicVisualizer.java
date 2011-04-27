package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

public class MusicVisualizer{
	private static ArrayList<float[]> samples;
	private static float duration;
	private static boolean activate = false;

	private MusicVisualizer(){

	}

	public static void setupMusicVisualizer(){
		samples = MusicData.getSamples();
		
		activate = true;
	}

	public static void draw(ImmediateModeRenderer r){
		if(activate){
			float[] frame = samples.get((int)(MusicData.getPosition()/MusicData.getFrameDuration()));

			for(int i = 0 ; i < Gdx.graphics.getWidth() ; i+=10){
				if(i<0){
					r.color(0, 0, 0, 0);
				}else{
					r.color(1, 0, 0, 1);
				}
				r.begin(GL10.GL_LINES);
				r.vertex(i, 0, 0);
				r.vertex(i, 200, 0);
				r.end();
			}		
		}
	}
}
