package edu.mills.cs280.audiorunner;

/**
 * Keeps track of current time to correctly decide on movement independent on frame rate
 * 
 * @author jklein
 *
 */
public class TimeHandler {
	static public final float FRAMERATE = 60.0f;
	static public final float TIMESCALE = 1000/FRAMERATE;  //Milliseconds / Desire FrameRate

	static private long prevTime = 0L;
	static private long currTime = 0L;
	static private long timeDiff = 0L;

	private TimeHandler(){
	}

	/**
	 * Updates time values
	 */
	public static void updateTime(){
		if(prevTime == 0)
			prevTime = System.currentTimeMillis();
		currTime = System.currentTimeMillis();
		if(currTime != prevTime){
			timeDiff = currTime - prevTime;
			prevTime = currTime;
		}
	}

	/**
	 * Get transition scale, percentage of movement that shoudl be done
	 * 
	 * @return float of transition scale
	 */
	public static float getTransitionScale(){
		return timeDiff/TIMESCALE;
	}
	
	/**
	 * gets current frame time in milliseconds
	 * 
	 * @return current time in milliseconds
	 */
	public static float getTimeOfCurrFrame(){
		return System.currentTimeMillis() - currTime;
	}
}
