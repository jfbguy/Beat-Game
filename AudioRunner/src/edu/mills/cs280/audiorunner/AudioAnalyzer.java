package edu.mills.cs280.audiorunner;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.badlogic.gdx.audio.analysis.AudioTools;
import com.badlogic.gdx.audio.analysis.KissFFT;

public class AudioAnalyzer {
	String fileLocation;
	
	public AudioAnalyzer(String fileLocation){
		this.fileLocation = fileLocation;
	}
	
	public void analyze(){
		final float frequency = 440;
        float increment = (float)(2*Math.PI) * frequency / 44100; // angular increment for each sample
        float angle = 0;                                
        short samples[] = new short[1024];

        for( int i = 0; i < samples.length; i++ )
        {
                float value = (float)Math.sin( angle ); 
                samples[i] = (short)(value * 32767);
                angle += increment;
        }
        
        ShortBuffer samplesBuffer = AudioTools.allocateShortBuffer( 1024, 1 );
        ShortBuffer realBuffer = AudioTools.allocateShortBuffer(512,1 );
        ShortBuffer imagBuffer = AudioTools.allocateShortBuffer(512,1 );
        samplesBuffer.put( samples );
        FloatBuffer spectrum = AudioTools.allocateFloatBuffer( 513, 1 );
        KissFFT fft = new KissFFT( 1024 );
        
        fft.spectrum( samplesBuffer, spectrum );
        
        fft.getRealPart( realBuffer );
        fft.getImagPart( imagBuffer );
        short[] re = new short[512];
        short[] im = new short[512];
        float[] sp = new float[513];
        realBuffer.position(0);
        realBuffer.get(re);
        imagBuffer.position(0);
        imagBuffer.get(im);
        spectrum.position(0);
        spectrum.get(sp);
        
        for( int i = 0; i < 30; i++ )
        {
                System.out.println( sp[i] );
        }
	}
}
