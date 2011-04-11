/* THIS CLASS IS BASED ON Threshold.java BY BADLOGIC GAMES
 * IT HAD BEEN MODIFIED BY US
 */

package edu.mills.cs280.audiorunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.filechooser.FileSystemView;

import javazoom.jlme.decoder.BitStream;
import javazoom.jlme.decoder.Decoder;
import javazoom.jlme.decoder.Header;

import com.badlogic.gdx.audio.analysis.FFT;

public class PeakFinder {
	
    public static final int THRESHOLD_WINDOW_SIZE = 10;//what determines the threshold?
    public static final float MULTIPLIER = 1.5f;
    private FileInputStream  inputStream; //filename and FILE are interchangeable if they are the same
    
    public PeakFinder(String file){
    	try {
			inputStream = new FileInputStream(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public List<Float> returnPeaks()
    {
    	try{
	       Decoder decoder = new Decoder(new Header(), new BitStream( inputStream  ) );							
	       FFT fft = new FFT( 1024, 44100 );
	       fft.window( FFT.HAMMING );
	       float[] samples = new float[1024];//why the magic number?
	       //It seems to me that spetrum will always be the same despite the duration of music.
	       float[] spectrum = new float[1024 / 2 + 1];//why the magic number?
	       float[] lastSpectrum = new float[1024 / 2 + 1];
	       
	       List<Float> spectralFlux = new ArrayList<Float>( );
	       List<Float> threshold = new ArrayList<Float>( );
	       List<Float> prunedSpectralFlux = new ArrayList<Float>();
	       List<Float> peaks = new ArrayList<Float>();
	       
	       //while( decoder.readSamples( samples ) > 0 )
	       while(decoder.decodeFrame() != null)
	       {			
	          fft.forward( samples );
	          System.arraycopy( spectrum, 0, lastSpectrum, 0, spectrum.length ); 
	          System.arraycopy( fft.getSpectrum(), 0, spectrum, 0, spectrum.length );
	  
	          float flux = 0;//what value does flux hold? what's the meaning of flux?
	          for( int i = 0; i < spectrum.length; i++ )	
	          {
	             float value = (spectrum[i] - lastSpectrum[i]);
	             flux += (value < 0? 0: value);
	          }
	          spectralFlux.add( flux );			
	       }		
	  
	       //populate the threshold array list
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
}
