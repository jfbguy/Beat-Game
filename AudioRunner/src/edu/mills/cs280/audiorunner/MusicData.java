package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.badlogic.gdx.audio.Music;

import javazoom.jl.decoder.SampleBuffer;

public class MusicData {
	private static String fileLocation;
	private static Hashtable<Integer,Float> peaks;
	private static float musicStep = 0.0f;
	private static float saveTime = 0.0f;
	private static float frameTime = 0.0f;
	
	public static Music music;
	
	private MusicData(){
		
	}
	
	public static void setFile(String file){
		fileLocation = file;
	}
	
	public static void addPeaks(Hashtable<Integer,Float> inPeaks){
		for(int key : inPeaks.keySet()){
			peaks.put(key,inPeaks.get(key));
		}
	}
	
	public static void setPeaks(Hashtable<Integer,Float> inPeaks){
		peaks = inPeaks;
	}
	
	public static Hashtable<Integer,Float> getPeaks(){
		return peaks;
	}
	
	public static String getFileLocation(){
		return fileLocation;
	}
	
	public static void setFrameTime(float setFrameTime){
		frameTime = setFrameTime;
	}
	
	public static float getFrameTime(){
		return frameTime;
	}
	
	public static int grabMusicFrames(){
		musicStep = music.getPosition()*1000 - saveTime;
		int frames = (int) (musicStep / getMusicStep());
		saveTime = music.getPosition()*1000 - floatModulus(musicStep,frameTime);
		System.out.println("********************************");
		System.out.println("********************************");
		System.out.println("DECODER:::GRAB Frames: " + frames);
		System.out.println("********************************");
		System.out.println("********************************");
		return frames;
		
	}
	
	public static float getMusicStep(){	
		return music.getPosition()*1000 - saveTime;
	}
	
	public static float floatModulus(float n, float d){
		while(n > 0){
			n -= d;
		}
		return n;
	}
}
