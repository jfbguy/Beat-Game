package com.badlogic.audio.samples.part7;

import java.awt.Color;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.audio.analysis.FFT;
import com.badlogic.audio.io.MP3Decoder;
import com.badlogic.audio.visualization.PlaybackVisualizer;
import com.badlogic.audio.visualization.Plot;

public class Threshold 
{
   public static final String FILE = "samples/explosivo.mp3";	
   public static final int THRESHOLD_WINDOW_SIZE = 10;
   public static final float MULTIPLIER = 1.5f;
 
   public static void main( String[] argv ) throws Exception
   {
      MP3Decoder decoder = new MP3Decoder( new FileInputStream( FILE  ) );							
      FFT fft = new FFT( 1024, 44100 );
      fft.window( FFT.HAMMING );
      float[] samples = new float[1024];
      float[] spectrum = new float[1024 / 2 + 1];
      float[] lastSpectrum = new float[1024 / 2 + 1];
      List<Float> spectralFlux = new ArrayList<Float>( );
      List<Float> threshold = new ArrayList<Float>( );
      List<Float> prunedSpectralFlux = new ArrayList<Float>();
      List<Float> peaks = new ArrayList<Float>();
      
      while( decoder.readSamples( samples ) > 0 )
      {			
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
            peaks.add( prunedSpectralFlux.get(i) );
         else
            peaks.add( (float)0 );				
      }

      
      Plot plot = new Plot( "Spectral Flux", 1024, 512 );
      plot.plot( spectralFlux, 1, Color.red );		
      plot.plot( threshold, 1, Color.green ) ;
      new PlaybackVisualizer( plot, 1024, new MP3Decoder( new FileInputStream( FILE ) ) );
      

   }
}