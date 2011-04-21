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

public class AudioAnalyzer{

	private File file;
	private InputStream inputStream;
	private Bitstream bitstream;
	private Decoder decoder;
	private Header header;

	public static final int THRESHOLD_WINDOW_SIZE = 10;
	public static final float MULTIPLIER = 1.5f;
	private static final int bufferLimit = 100;

	//Decoder variables
	private FFT fft;
	private float[] samples;
	private float[] spectrum;
	private float[] lastSpectrum;
	private List<Float> spectralFlux;
	private List<Float> threshold;
	private List<Float> prunedSpectralFlux;
	private Hashtable<Integer,Float> peaks;

	private float frameTime = 0;
	private int bufferCounter = 0;
	private ScreenHandler screenHandler;
	private boolean decodingDone;

	public AudioAnalyzer(String fileLocation, ScreenHandler screenHandler){
		file = new File(fileLocation);
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file), 1024);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		bitstream = new Bitstream(inputStream);
		decoder = new Decoder();

		try {
			header = bitstream.readFrame();
			MusicData.setFrameTime(header.ms_per_frame());	//set how long a frame of msuic is
		} catch (BitstreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.screenHandler = screenHandler;


		fft = new FFT( 1024, 44100 );
		fft.window( FFT.HAMMING );
		samples = new float[1024];
		spectrum = new float[1024 / 2 + 1];
		lastSpectrum = new float[1024 / 2 + 1];
		spectralFlux = new ArrayList<Float>( );
		threshold = new ArrayList<Float>( );
		prunedSpectralFlux = new ArrayList<Float>();
		peaks = new Hashtable<Integer,Float>();

		decodingDone = false;
	}

	/*public void run(){
		while(!decodingDone){
			if(MusicData.music.isPlaying()){
				decode(screenHandler);
			}
		}
	}*/

	public void decode(ScreenHandler screenHandler){
		if(!decodingDone){
			if(bufferCounter > bufferLimit){
				screenHandler.addPlatforms(returnPeaks());
				bufferCounter = 0;
				spectralFlux.clear();;
				threshold.clear();
				prunedSpectralFlux.clear();
				peaks.clear();
			}
			else{
				bufferFrame();
				bufferCounter++;
			}

			/*
		if(MusicData.getMusicStep()> frameTime){
			int frames = MusicData.grabMusicFrames();
			bufferFrame(1);
			bufferCounter++;

			System.out.println("********************************");
			System.out.println("DECODER:::bufferCounter- " + bufferCounter);
		}*/
		}


	}

	public void bufferFrame(){

		if(singleSamples( samples ) > 0){

			fft.forward( samples );
			System.arraycopy( spectrum, 0, lastSpectrum, 0, spectrum.length ); 
			System.arraycopy( fft.getSpectrum(), 0, spectrum, 0, spectrum.length );

		}
		else{
			decodingDone = true;
		}

	}

	public Hashtable<Integer,Float> returnPeaks()
	{
		try{
			//Replaces by buffer
			/*FFT fft = new FFT( 1024, 44100 );
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
			}*/
			
			float flux = 0;
			for( int i = 0; i < spectrum.length; i++ )	
			{
				float value = (spectrum[i] - lastSpectrum[i]);
				flux += value < 0? 0: value;
			}
			spectralFlux.add( flux );
			
			System.out.println("=============================>   1");

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
			
			System.out.println("=============================>   2");

			//now prune the threshold sizes
			for( int i = 0; i < threshold.size(); i++ )
			{
				if( threshold.get(i) <= spectralFlux.get(i) )
					prunedSpectralFlux.add( spectralFlux.get(i) - threshold.get(i) );
				else
					prunedSpectralFlux.add( (float)0 );
			}
			
			System.out.println("=============================>   3");
			System.out.println("=============================>   3");
			System.out.println("=============================>   3");
			System.out.println("=============================>   3");
			System.out.println("=============================>   3");
			MusicData.music.getPosition();
			System.out.println("=============================>   4");

			//and finally choose the peaks
			for( int i = 0; i < prunedSpectralFlux.size() - 1; i++ )
			{
				if( prunedSpectralFlux.get(i) > prunedSpectralFlux.get(i+1) )
					peaks.put((int)(MusicData.music.getPosition()), prunedSpectralFlux.get(i) );

			}
			//       System.out.println(prunedSpectralFlux.toString());
			//       System.out.println(peaks.toString());
			//       System.out.println("size = " + peaks.size());
			//
			System.out.println("***************************************************");
			System.out.println("**********************Success**********************");
			System.out.println("***************************************************");

			return peaks;

			//       Plot plot = new Plot( "Spectral Flux", 1024, 512 );
			//       plot.plot( spectralFlux, 1, Color.red );		
			//       plot.plot( threshold, 1, Color.green ) ;
			//       new PlaybackVisualizer( plot, 1024, new MP3Decoder( new FileInputStream( fileName ) ) );
			//       
		}catch(Exception e){
			System.out.println("FILE FAILED TO LOAD");
			return null;
		}

	}

	public float getFrameTime(){
		return frameTime;
	}

	public int singleSamples(float[] samples)
	{

		try {

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
				/*for(short e : output.getBuffer()){
					System.out.print(e + " , ");
				}*/

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