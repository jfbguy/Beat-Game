package edu.mills.cs280.audiorunner;

public class MusicHandler {
	static public final float FRAMERATE = 60.0f;
	static private final float TIMESCALE = 1000/FRAMERATE;  //Milliseconds / Desire FrameRate

	static private long prevTime = 0L;
	static private long currTime = 0L;
	static private long timeDiff = 0L;

	private MusicHandler(){
	}

	public static void updateTime(){
		if(prevTime == 0)
			prevTime = System.currentTimeMillis();
		currTime = System.currentTimeMillis();
		if(currTime != prevTime){
			timeDiff = currTime - prevTime;
			prevTime = currTime;
		}
	}

	public static float getTransitionScale(){
		return timeDiff/TIMESCALE;
	}
}