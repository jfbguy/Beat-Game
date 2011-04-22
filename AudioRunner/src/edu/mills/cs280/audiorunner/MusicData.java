package edu.mills.cs280.audiorunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

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

	public static void loadBufferedData(){
		System.out.println("TRYING TO LOAD!!!!");

		try {
			InputStream fis = Gdx.files.internal("data/testpeaks.ar").read();
			ObjectInputStream in = new ObjectInputStream(fis);
			@SuppressWarnings("unchecked")
			List<Float> peaks = (List<Float>)in.readObject();
			in.close();

			for(float f : peaks){
				System.out.println(f+",");
			}

			MusicData.setpeaks(peaks);

		}
		catch (Exception e) {
			System.out.println("111111111111EXCEEEEEEEEEEEEEEEEEEPTION!!!" + e);
		}

		try {
			InputStream fis = Gdx.files.internal("data/testsamples.ar").read();
			ObjectInputStream in = new ObjectInputStream(fis);
			@SuppressWarnings("unchecked")
			ArrayList<float[]> samples = (ArrayList<float[]>)in.readObject();
			in.close();
			MusicData.setSamples(samples);

		}
		catch (Exception e) {
			System.out.println("222222222222222EXCEEEEEEEEEEEEEEEEEEPTION!!!" + e);
		}


	}
}
