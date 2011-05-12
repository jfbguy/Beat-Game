package edu.mills.cs280.audiorunner;

/*
 * This class is based on com.badlogic.audio.samples.part7.threshold.javva by Badlogic Games
 */

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.analysis.FFT;

/**
 * This class gets a list of peaks (event data) from the mp3 decoder
 * @author Trevor Adams
 *
 */
public class PeakFinder{

	public static final int THRESHOLD_WINDOW_SIZE = 10;
	public static final float MULTIPLIER = 1.5f;

	private boolean useTestMusicData = false;

	/**
	 * Empty constructor, action performed in returnPeaks()
	 */
	public PeakFinder(){}
/**
 * 	Takes a list of of floats representing aggregate onset amplitudes
	and prunes them by creating an average threshold of amplitude
 * @return returns the peak onsets (musical events) from an mp3 (specified in MusicData)
 */

	public List<Float> returnPeaks()
	{
		try{
			AudioAnalyzer decoder = new AudioAnalyzer(MusicData.getFileLocation());
			FFT fft = new FFT( 1024, 44100 );
			fft.window( FFT.HAMMING );
			float[] samples = new float[1024];
			float[] spectrum = new float[1024 / 2 + 1];
			float[] lastSpectrum = new float[1024 / 2 + 1];
			List<Float> spectralFlux = new ArrayList<Float>( );
			List<Float> threshold = new ArrayList<Float>( );
			List<Float> prunedSpectralFlux = new ArrayList<Float>();
			List<Float> peaks = new ArrayList<Float>();
			Boolean flip = true;


			float value = 0.0f;
			float flux = 0;
			while( decoder.singleSamples( samples ) > 0 )
			{
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
					peaks.add( prunedSpectralFlux.get(i) );
				else
					peaks.add( (float)0 );				
			}     

			decoder.dispose();

			return peaks;
    
		}catch(Exception e){
			System.out.println("FILE FAILED TO LOAD");
			return null;
		}

	}
}
