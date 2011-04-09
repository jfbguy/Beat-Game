package edu.mills.cs280.audiorunner;

public class FrameLocker {
	private static final long MILLISECONDS_PER_FRAME = 1000/60;
	
	private static long currTime = System.currentTimeMillis();
	private static long prevTime = 0L;
	
	private FrameLocker(){
	}
	
	public static Boolean legalFrame(){
		currTime = System.currentTimeMillis();
		long diff = currTime - prevTime;
		if(diff >= MILLISECONDS_PER_FRAME){
			prevTime = currTime;
			return true;
		}
		
		return false;
	}
}
