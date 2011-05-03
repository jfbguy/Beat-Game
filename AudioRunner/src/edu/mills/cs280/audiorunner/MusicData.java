package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import javazoom.jl.decoder.SampleBuffer;

public class MusicData {
	private static String fileLocation;
	private static List<Float> peaks;
	private static ArrayList<float[]> samples;
	private static float duration; //the length of the music in milisec
	private static Music music;
	private static float currPosition;
	private static float frameDuration = 0;
	private static AudioAnalyzer analyzer;
	private static int platforms = 0;
	public static int frameCounter = 0;


	private MusicData(){

	}

	public static void setMusic(Music inMusic){
		music = inMusic;
	}

	public static float getPosition(){	//Milliseconds should be in thousands of seconds
		return currPosition;
	}

	public static void setSamples(ArrayList<float[]> loadedSamples){
		samples = loadedSamples;
	}

	public static void setFile(String file){	//Set file location and create analyzer
		fileLocation = file;
		analyzer = new AudioAnalyzer(file);
		peaks = new ArrayList<Float>();
	}

	public static void decode(){
		analyzer.decode();
	}

	public static void setPeaks(List<Float> inPeaks){
		peaks = inPeaks;
	}

	public static void addPeaks(List<Float> inPeaks){
		peaks.addAll(inPeaks);
	}

	public static List<Float> getPeaks(){
		return peaks;
	}

	public static void loadPlatforms(ScreenHandler screenHandler){	//**call to load peaks
		if(peaks.size() > platforms){
			screenHandler.loadPlatforms(peaks, platforms);
			platforms = peaks.size();
		}
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

	public static void setFrameDuration(float fd){
		frameDuration = fd;
	}

	public static float getFrameDuration(){
		//return duration/sampleCounter;
		return frameDuration;
	}

	public static void loadBufferedData(){

	}

	public static void update(ScreenHandler scr){
		currPosition = music.getPosition()*1000;
		MusicData.decode();
		MusicData.loadPlatforms(scr);
	}
}
