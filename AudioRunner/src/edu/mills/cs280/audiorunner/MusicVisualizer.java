package edu.mills.cs280.audiorunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

public class MusicVisualizer{
	private static ArrayList<float[]> samples;
	private static float duration;
	private static boolean activate = false;
	
	//test
	private static short[] data = new short[1024];

	private MusicVisualizer(){

	}

	public static void setupMusicVisualizer(){
		samples = MusicData.getSamples();
		
		activate = true;
	}

	public static void draw(ImmediateModeRenderer r){
		if(activate){
			//float[] frame = samples.get((int)(MusicData.getPosition()/MusicData.getFrameDuration()));
			if(snoop(data, 0) == 0)
				System.out.println("Success");
			r.color(1, 0, 0, 0);
			for(int i = 0 ; i < Gdx.graphics.getWidth() ; i+=10){
				r.begin(GL10.GL_LINES);
				r.vertex(i, 0, 0);
				r.vertex(i, data[i], 0);
				r.end();
			}		
		}
	}
	
	public static int snoop(short [] outData, int kind){    
        try {
            Class c = Music.class;
            Method m = c.getMethod("snoop", outData.getClass(), Integer.TYPE);
            m.setAccessible(true);
            m.invoke(c, outData, kind);
            return 0;
        } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return 1;
     }
}
	

}
