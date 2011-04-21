package edu.mills.cs280.audiorunner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.badlogic.gdx.audio.analysis.FFT;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

public class AudioAnalyzer extends Thread{

	private File file;
	private InputStream inputStream;
	private Bitstream bitstream;
	private Decoder decoder;
	
	public static final int THRESHOLD_WINDOW_SIZE = 10;
    public static final float MULTIPLIER = 1.5f;

	public AudioAnalyzer(String fileLocation){
		file = new File(fileLocation);
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file), 1024);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		bitstream = new Bitstream(inputStream);
		decoder = new Decoder();
	}
	
	public Hashtable<Integer,Float> returnPeaks(float timeLength)
    {
    	try{
	       FFT fft = new FFT( 1024, 44100 );
	       fft.window( FFT.HAMMING );
	       float[] samples = new float[1024];
	       float[] spectrum = new float[1024 / 2 + 1];
	       float[] lastSpectrum = new float[1024 / 2 + 1];
	       List<Float> spectralFlux = new ArrayList<Float>( );
	       List<Float> threshold = new ArrayList<Float>( );
	       List<Float> prunedSpectralFlux = new ArrayList<Float>();
	       Hashtable<Integer,Float> peaks = new Hashtable<Integer,Float>();
	       
	       for(int j = 0; j < timeLength; j++)
	       {
	    	  singleSamples( samples );
	          fft.forward( samples );
	          System.arraycopy( spectrum, 0, lastSpectrum, 0, spectrum.length ); 
	          System.arraycopy( fft.getSpectrum(), 0, spectrum, 0, spectrum.length );

	          float flux = 0;
	          for( int i = 0; i < spectrum.length; i++ )	
	          {
	             float value = (spectrum[i] - lastSpectrum[i]);
	             flux += value < 0? 0: value;
	          }
	          spectralFlux.add( flux );					
	       }	

	       for( int i = 0; i < spectralFlux.size(); i++ )
	       {
	          int start = Math.max( 0, i - THRESHOLD_WINDOW_SIZE );
	          int end = Math.min( spectralFlux.size() - 1, i + THRESHOLD_WINDOW_SIZE );
	          float mean = 0;
	          for( int j = start; j <= end; j++ )
	             mean += spectralFlux.get(j);
	          mean /= (end - start);
	          threshold.add( mean * MULTIPLIER );
	       }
	       
	       //now prune the threshold sizes
	       for( int i = 0; i < threshold.size(); i++ )
	       {
	          if( threshold.get(i) <= spectralFlux.get(i) )
	             prunedSpectralFlux.add( spectralFlux.get(i) - threshold.get(i) );
	          else
	             prunedSpectralFlux.add( (float)0 );
	       }

	       //and finally choose the peaks
	       for( int i = 0; i < prunedSpectralFlux.size() - 1; i++ )
	       {
	          if( prunedSpectralFlux.get(i) > prunedSpectralFlux.get(i+1) )
	             peaks.put((int)(1000*MusicData.music.getPosition()), prunedSpectralFlux.get(i) );
			
	       }
	//       System.out.println(prunedSpectralFlux.toString());
	//       System.out.println(peaks.toString());
	//       System.out.println("size = " + peaks.size());
	//      
	       
	       return peaks;

	//       Plot plot = new Plot( "Spectral Flux", 1024, 512 );
	//       plot.plot( spectralFlux, 1, Color.red );		
	//       plot.plot( threshold, 1, Color.green ) ;
	//       new PlaybackVisualizer( plot, 1024, new MP3Decoder( new FileInputStream( fileName ) ) );
	//       
    	}catch(Exception e){
    		//What should I do here???
    		System.out.println("FILE FAILED TO LOAD");
    		return null;
    	}
    	
    }

	public int singleSamples(float[] samples)
	{

		try {
			Header header;
			header = bitstream.readFrame();

			SampleBuffer output;
			try{
				output = (SampleBuffer)decoder.decodeFrame(header, bitstream);
			}catch(Exception e){
				return 0;
			}
			
			for(int i = 0; i < samples.length; i++){
				samples[i] = output.getBuffer()[i];
				//System.out.print(samples[i]);
			}

			bitstream.closeFrame();

			//System.out.println("");

			return 1;

		} catch (BitstreamException e) {
			//e.printStackTrace();
			return 0;
		}

	}

	public int readSamples(float[] samples)
	{

		boolean done = false;

		try {

			while(!done){
				Header header;
				header = bitstream.readFrame();

				SampleBuffer output;
				try{
					output = (SampleBuffer)decoder.decodeFrame(header, bitstream);
				}catch(Exception e){
					break;
				}


				//System.out.println(output.getBufferLength());

				//for(int i = 0; i < output.getBuffer().length; i++){
				//samples[i] = 
				//}
				for(short e : output.getBuffer()){
					System.out.print(e + " , ");
				}

				bitstream.closeFrame();

				System.out.println();
			}

			System.out.println("SUCCESS!!!");

			return 1;

		} catch (BitstreamException e) {
			//e.printStackTrace();
			return 0;
		}

	}
	
	public void dispose(){
		try {
			inputStream.close();
			bitstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BitstreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}