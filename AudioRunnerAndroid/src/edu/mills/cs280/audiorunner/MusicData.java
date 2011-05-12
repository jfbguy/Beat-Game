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
 * @author jklein
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
	 * Gets time code of Music from previous frame
	 * 
	 * @return time code of music from previous frame
	 */
	public static float getPrevPosition(){	//Milliseconds should be in thousands of seconds
		return prevPosition;
	}
	
	/**
	 * Adds an array of samples to the samples array
	 * 
	 * @param addSamples samples to add
	 */
	public static void addSamples(float[] addSamples){
		if(samples == null){
			samples = new ArrayList<float[]>();
		}
		samples.add(addSamples);
	}

	/**
	 * Sets file location for current song
	 * 
	 * @param file file location
	 */
	public static void setFile(String file){	//Set file location and create analyzer
		fileLocation = file;
		analyzer = new AudioAnalyzer(file);
		peaks = new ArrayList<Float>();
	}

	/**
	 * Cal for decoder to decode a song
	 */
	public static void decode(){
		analyzer.decode();
	}

	/**
	 * Set the peaks
	 * 
	 * @param inPeaks peaks to be set
	 */
	public static void setPeaks(List<Float> inPeaks){
		peaks = inPeaks;
	}

	/**
	 * Add peaks
	 * 
	 * @param inPeaks peaks to add to peaks array
	 */
	public static void addPeaks(List<Float> inPeaks){
		peaks.addAll(inPeaks);
	}

	/**
	 * Gets the peaks
	 * 
	 * @return list of floats containing all of the peaks
	 */
	public static List<Float> getPeaks(){
		return peaks;
	}

	/**
	 * Loads any more peaks that have been decoded
	 * 
	 * @param screenHandler Required for calling loadplatform method within it
	 */
	public static void loadPlatforms(ScreenHandler screenHandler){	//**call to load peaks
		if(peaks.size() > platforms){
			screenHandler.loadPlatforms(peaks, platforms);
			platforms = peaks.size();
		}
	}

	/**
	 * Gets the samples array
	 * 
	 * @return array of all of the frame samples
	 */
	public static ArrayList<float[]> getSamples(){
		return samples;
	}

	/**
	 * Get File Location of Current Song
	 * 
	 * @return file location of song
	 */
	public static String getFileLocation(){
		return fileLocation;
	}

	/**
	 * Sets the duration of the song
	 * 
	 * @param songDuration duration of the current song in milliseconds
	 */
	public static void setDuration(float songDuration){
		duration = songDuration;
	}

	/**
	 * Gets the duration
	 * 
	 * @return returns the duration of the current song in milliseconds
	 */
	public static float getDuration(){
		return duration;
	}

	/**
	 * Sets the duration of each frame decoded in milliseconds
	 * 
	 * @param fd frame duration in milliseconds
	 */
	public static void setFrameDuration(float fd){
		frameDuration = fd;
	}

	/**
	 * Gets the duration of each frame decoded in milliseconds
	 * 
	 * @return frame duration in milliseconds
	 */
	public static float getFrameDuration(){
		//return duration/sampleCounter;
		return frameDuration;
	}

	/**
	 * Updates the music data so program knows the current location of the song and information on
	 * samples and peaks
	 * 
	 * @param scr ScreenHandler which is needed for loadplatform method
	 */
	public static void update(ScreenHandler scr){
		prevPosition = currPosition;
		currPosition = music.getPosition()*MILLISECS_PER_SEC;
		MusicData.decode();
		MusicData.loadPlatforms(scr);
	}
}
