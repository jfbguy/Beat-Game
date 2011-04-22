package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
import java.util.List;

import javazoom.jl.decoder.SampleBuffer;

public class MusicData {
	private static String fileLocation;
	private static List<Float> peaks;
	private static ArrayList<float[]> samples;
	private static int duration; //the length of the music in milisec
	
	private MusicData(){
		
	}
	
	public static void setSamples(ArrayList<float[]> loadedSamples){
		samples = loadedSamples;
	}
	
	public static void setFile(String file){
		fileLocation = file;
	}
	
	public static void setpeaks(List<Float> inPeaks){
		peaks = inPeaks;
	}
	
	public static List<Float> getPeaks(){
		return peaks;
	}
	
	public static ArrayList<float[]> getSamples(){
		return samples;
	}
	
	public static String getFileLocation(){
		return fileLocation;
	}
	
	public static int getDuration(){
		return duration;
	}
	
	public static float getFrameDuration(){
		return duration/samples.size();
	}
}
