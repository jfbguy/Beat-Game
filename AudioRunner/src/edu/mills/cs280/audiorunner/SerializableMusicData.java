package edu.mills.cs280.audiorunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/******************************************
 * SERIALIZED CLASS
 * DO NOT CHANGE ANYTHING HERE!!!!
 * ****************************************
 */


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
	
	public void save(){

	       FileOutputStream fos = null;
	       ObjectOutputStream out = null;
	       try
	       {
	    	   fos = new FileOutputStream(SerializableMusicData.getDefaultName());
	    	   out = new ObjectOutputStream(fos);
	    	   out.writeObject(this);
	    	   out.close();
	       }
	       catch(IOException ex)
	       {
	    	   ex.printStackTrace();
	       }
	}
	
	public void save(String fileName){

	       FileOutputStream fos = null;
	       ObjectOutputStream out = null;
	       try
	       {
	    	   fos = new FileOutputStream(fileName);
	    	   out = new ObjectOutputStream(fos);
	    	   out.writeObject(this);
	    	   out.close();
	       }
	       catch(IOException ex)
	       {
	    	   ex.printStackTrace();
	       }
	}
	
	public static SerializableMusicData load(){
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(SerializableMusicData.getDefaultName());
			in = new ObjectInputStream(fis);
			SerializableMusicData data = (SerializableMusicData)in.readObject();
			in.close();
			return data;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public static SerializableMusicData load(String fileName){
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(fileName);
			in = new ObjectInputStream(fis);
			SerializableMusicData data = (SerializableMusicData)in.readObject();
			in.close();
			return data;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
}
