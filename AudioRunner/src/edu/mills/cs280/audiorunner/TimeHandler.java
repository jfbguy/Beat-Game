package edu.mills.cs280.audiorunner;

public class TimeHandler {
	static public final float FRAMERATE = 60.0f;
	static public final float TIMESCALE = 1000/FRAMERATE;  //Milliseconds / Desire FrameRate

	static private long prevTime = 0L;
	static private long currTime = 0L;
	static private long timeDiff = 0L;

	private TimeHandler(){
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
	
	public static float getTimeOfCurrFrame(){
		return System.currentTimeMillis() - currTime;
	}
}
