package edu.mills.cs280.audiorunner;

import com.badlogic.gdx.audio.Music;

public class MusicHandler {
	static public final float FRAMERATE = 30;
	static private final float TIMESCALE = 1.0f/FRAMERATE;  //Milliseconds / Desire FrameRate

	static private Music music;
	static private float prevTime = 0.0f;
	static private float currTime = 0.0f;
	static private float timeDiff = 0.0f;
	
	static private float avgDiff = 0;

	private MusicHandler(){
	}

	public static void setMusic(Music music){
		MusicHandler.music = music;
	}

	public static float getTime(){
		return music.getPosition();
	}

	public static void updateTime(){		
		currTime = MusicHandler.getTime();
		if(currTime != prevTime){
			timeDiff = currTime - prevTime;
			avgDiff = (avgDiff + timeDiff)/2.0f;
			prevTime = currTime;
		}
		else{
			currTime += avgDiff;
			timeDiff = currTime - prevTime;
			prevTime = currTime;
		}
	}

	public static float getTransitionScale(){
		
		return timeDiff/TIMESCALE;
	}
}
