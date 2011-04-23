package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.audio.Music;

public class MusicData {
	private static Music music;
	private static String fileLocation;
	private static List<Float> peaks;
	private static ArrayList<float[]> samples;
	private static float duration; //the length of the music in milisec

	private MusicData(){

	}
	
	public static void setMusic(Music inMusic){
		music = inMusic;
	}
	
	public static float getPosition(){
		return music.getPosition();
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
	
	public static void setDuration(float songDuration){
		duration = songDuration;
	}

	public static float getDuration(){
		return duration;
	}

	public static float getFrameDuration(){
		return duration/samples.size();
	}

	public static void loadBufferedData(){
		


	}
}
