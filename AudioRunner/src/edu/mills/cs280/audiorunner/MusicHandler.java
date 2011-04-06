package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.audio.Music;

public class MusicHandler {
	static public final float FRAMERATE = 60;
	static private final float TIMESCALE = 1000/FRAMERATE;  //Milliseconds / Desire FrameRate
	
	static private Music music;
	static private float prevTime = 0.0f;
	static private float currTime = 0.0f;
	static private float timeDiff = 0.0f;
	
	private MusicHandler(){
	}
	
	public static void setMusic(Music music){
		MusicHandler.music = music;
	}
	
	public static float getTime(){
		return music.getPosition();
	}
	
	public static void updateTime(){
		if(MusicHandler.getTime() > 0){
			int debug = 0;
			currTime = MusicHandler.getTime();
			debug++;
		}
		
		currTime = MusicHandler.getTime();
		timeDiff = currTime - prevTime;
		prevTime = currTime;
	}
	
	public static float getTransitionScale(){
		if(timeDiff == 0){
			return 0;
		}
		return TIMESCALE*timeDiff;
	}
}
