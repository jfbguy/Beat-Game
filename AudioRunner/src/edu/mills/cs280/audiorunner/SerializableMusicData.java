package edu.mills.cs280.audiorunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableMusicData implements Serializable{

	private static final long serialVersionUID = 78548954648347675L;
	private static final String FILE_NAME = "data/testMusicData.ar";
	
	private  ArrayList<float[]> samples = new ArrayList<float[]>();
	private  List<Float> peaks = new ArrayList<Float>();

	
	public SerializableMusicData(){
		
	}
	
	public void addSamples(float[] decodedSamples){
		samples.add(decodedSamples);
	}
	
	public void setPeaks(List<Float> decodedPeaks){
		peaks = decodedPeaks;
	}
	
	public ArrayList<float[]> getSamples(){
		return samples;
	}
	
	public List<Float> getPeaks(){
		return peaks;
	}

	public static String getDefaultName(){
		return FILE_NAME;
	}
}
