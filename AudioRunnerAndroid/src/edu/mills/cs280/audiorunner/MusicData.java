package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import javazoom.jl.decoder.SampleBuffer;

/**
 * Single location for storing all data about music. Betrays roots
 * as a serializable class to store music data offline and run
 * without taking time to decode.
 * 
 * @author tadams
 *
 */
public class MusicData {
	
	private static final int MILLISECS_PER_SEC = 1000;
	private static int finalScore = 0;
	private static String songName;
	private static String fileLocation;
	private static List<Float> peaks;
	private static ArrayList<float[]> samples;
	private static float duration; //the length of the music in milisec
	private static Music music;
	private static float currPosition;
	private static float prevPosition;
	private static float frameDuration = 0;
	private static AudioAnalyzer analyzer;
	private static int platforms = 0;
	public static int frameCounter = 0;

/**
 * Private constructor, this class is only called statically
 * 
 */
	
	//TODO Should we call this differently since class is effectively static?
	private MusicData(){

	}
	
	/**
	 * Save the final score for a game for High
	 * scores to retrieve
	 * @param score  - score value to save
	 */
	public static void setScore(int score) {
		finalScore = score;
	}
	
	/**
	 * Return saved score value
	 * @return return saved score value
	 */
	public static int getScore() {
		return finalScore;
	}
	
	/**
	 * Save name of song for High Scores to retrieve
	 * @param name - song title
	 */
	public static void setName(String name) {
		songName = name;
	}
	
	/**
	 * Retrieve saved song name
	 * @return song title
	 */
	public static String getName() {
		return songName;
	}
	
	/**
	 * Saves libgdx Music class for other operations
	 * @param inMusic	Libgdx Music class
	 */
	public static void setMusic(Music inMusic){
		music = inMusic;
	}

	/**
	 * Get position of song using libgdx Music class
	 * @return position of song in milliseconds
	 */
	public static float getPosition(){
		return currPosition;
	}
	
	/**
	 * 
	 * @return
	 */
	public static float getPrevPosition(){	//Milliseconds should be in thousands of seconds
		return prevPosition;
	}

	//public static void setSamples(ArrayList<float[]> loadedSamples){
	//	samples = loadedSamples;
	//}
	
	public static void addSamples(float[] addSamples){
		if(samples == null){
			samples = new ArrayList<float[]>();
		}
		samples.add(addSamples);
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
		prevPosition = currPosition;
		currPosition = music.getPosition()*MILLISECS_PER_SEC;
		MusicData.decode();
		MusicData.loadPlatforms(scr);
	}
}
