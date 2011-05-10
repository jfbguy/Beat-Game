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

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

import com.badlogic.gdx.audio.analysis.FFT;

public class AudioAnalyzer{

	private File file;
	private InputStream inputStream;
	private Bitstream bitstream;
	private Decoder decoder;
	private Header header;

	public static final int SAMPLE_FRACTION = 8;
	public static final int THRESHOLD_WINDOW_SIZE = 10;
	public static final float MULTIPLIER = 1.5f;
	private int bufferLimit = (int) (MusicData.getDuration()/MusicData.getFrameDuration());

	//Decoder variables
	private FFT fft;
	private float[] samples;
	private float[] spectrum;
	private float[] lastSpectrum;
	private List<Float> spectralFlux;
	private List<Float> threshold;
	private List<Float> prunedSpectralFlux;
	private ArrayList<Float> peaks;
	private float[] tempSamples;

	private int bufferCounter = 0;
	private boolean decodingDone;
	private int bufferAnalyze = 0;
	private Boolean flip = true;

	public AudioAnalyzer(String fileLocation){
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
			MusicData.setFrameDuration(header.ms_per_frame());	//set how long a frame of music is
		} catch (BitstreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		fft = new FFT( 1024, 44100 );
		fft.window( FFT.HAMMING );
		samples = new float[1024];
		spectrum = new float[1024 / 2 + 1];
		lastSpectrum = new float[1024 / 2 + 1];
		spectralFlux = new ArrayList<Float>( );
		threshold = new ArrayList<Float>( );
		prunedSpectralFlux = new ArrayList<Float>();
		peaks = new ArrayList<Float>();

		decodingDone = false;
	}

	public void decode(){
		if(!decodingDone){
			if(bufferCounter > bufferLimit){
				bufferAnalyze++;
				switch(bufferAnalyze){
				case 1:
					spectralFlux();
					break;
				case 2:
					prune();
					break;
				case 3:
					choosePeaks();
					MusicData.addPeaks(peaks);
					spectralFlux.clear();
					threshold.clear();
					prunedSpectralFlux.clear();
					peaks.clear();
					bufferAnalyze = 0;
					bufferCounter = 0;
					break;
				}
			}
			else{
				bufferFrame();
				bufferCounter++;
			}

		}


	}

	public void bufferFrame(){

		float value = 0.0f;
		float flux = 0.0f;
		if(singleSamples( samples ) > 0){
			fft.forward( samples );
			if(flip){
				System.arraycopy( fft.getSpectrum(), 0, spectrum, 0, spectrum.length );
				flux = 0;
				for( int i = 0; i < spectrum.length; i++ )	
				{
					value = (spectrum[i] - lastSpectrum[i]);
					flux += value < 0? 0: value;
				}
				spectralFlux.add( flux );
				flip = !flip;
			}
			else{
				System.arraycopy( fft.getSpectrum(), 0, lastSpectrum, 0, lastSpectrum.length );
				flux = 0;
				for( int i = 0; i < lastSpectrum.length; i++ )	
				{
					value = (lastSpectrum[i] - spectrum[i]);
					flux += value < 0? 0: value;
				}
				spectralFlux.add( flux );
				flip = !flip;
			}
			
			tempSamples = new float[samples.length/SAMPLE_FRACTION];
			for(int i = 0; i < tempSamples.length; i++){
				tempSamples[i] = samples[i*SAMPLE_FRACTION];
			}
			MusicData.addSamples(tempSamples);

		}
		else{
			decodingDone = true;
		}

	}

	public void spectralFlux(){
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
	}

	public void prune(){
		for( int i = 0; i < threshold.size(); i++ )
		{
			if( threshold.get(i) <= spectralFlux.get(i) ){
				prunedSpectralFlux.add( spectralFlux.get(i) - threshold.get(i) );
			}
			else
				prunedSpectralFlux.add( (float)0 );
		}
	}

	public void choosePeaks(){
		for( int i = 0; i < prunedSpectralFlux.size() - 1; i++ )
		{
			if( prunedSpectralFlux.get(i) > prunedSpectralFlux.get(i+1) ){
				peaks.add(prunedSpectralFlux.get(i) );
		}
			else{
				peaks.add((float) 0);
			}

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
				//TODO find a better way to exit than empty catch
				return 0;
			}

			//System.out.print("samples.length " + samples.length);
			for(int i = 0; i < samples.length; i++){
				samples[i] = output.getBuffer()[i];
				//System.out.print(samples[i]);
			}

			bitstream.closeFrame();

			//System.out.println("");

			return 1;

		} catch (BitstreamException e) {
			//e.printStackTrace();
			//TODO find a better way to exit than empty catch
			return 0;
		}

	}
	
	public void dispose(){
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
