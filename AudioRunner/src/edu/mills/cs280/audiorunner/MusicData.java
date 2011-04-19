package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
import java.util.List;

import javazoom.jl.decoder.SampleBuffer;

public class MusicData {
	private static String fileLocation;
	private static ArrayList<SampleBuffer> musicData;
	private static List<Float> peaks;
	
	private MusicData(){
		
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
	
	public static void addMusicData(SampleBuffer buffer){
		if(musicData == null){
			musicData = new ArrayList<SampleBuffer>();
		}
		musicData.add(buffer);
	}
	
	public static String getFileLocation(){
		return fileLocation;
	}
}