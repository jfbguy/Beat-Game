package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.badlogic.gdx.audio.Music;

import javazoom.jl.decoder.SampleBuffer;

public class MusicData {
	private static String fileLocation;
	private static Hashtable<Integer,Float> peaks;
	
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
}
