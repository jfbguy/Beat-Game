package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
/**
 * 
 * Visualizes the music.
 * It is a visual test of the decoder.
 * It adds art to the game.
 * 
 * @author ymiao
 *
 */
public class MusicVisualizer{
	private static ArrayList<float[]> samples;
	private static boolean activate = false;
	private static float[] vSample = new float[128];
	private static Texture tex;
	private static Sprite[] bubbles = new Sprite[vSample.length];

	private MusicVisualizer(){

	}

	/**
	 * assign the value of some important data used by the visualizer
	 * samples are the song information
	 * tex is the what the visualizer uses to draw
	 */
	public static void setupMusicVisualizer(){
		samples = MusicData.getSamples();
		tex = new Texture(Gdx.files.internal("data/particle.png"));
		for(int i = 0; i < bubbles.length; i++){
			bubbles[i] = new Sprite(tex);
		}
		activate = true;
	}

	private static float scaler = 0;
	private static int ratio = 4;
	private static float min = 0;
	private static float sum = 0;

	/**
	 * The draw method calculates
	 * the location and the size of the bubbles 
	 * based on the music peak samples
	 * 
	 * @param sb the SpriteBatch to draw on
	 */
	public static void draw(SpriteBatch sb){
		if(activate){
			int frameNumber = (int)(MusicData.getPosition()/MusicData.getFrameDuration());
			float[] frame = samples.get(frameNumber);

			if (frameNumber%10==0){//10 is the update frequency
				//System.arraycopy(frame, 0, vSample, 0, vSample.length);

				sum = 0;
				min = 0;
				for(float f: frame){
					//System.out.print(f+",");
					sum += f; 
					if(f<min){
						min = f;
					}
				}
				//System.out.println();
				scaler = (sum/frame.length)+Math.abs(min); //sum+min*frame.length/frame.length
				ratio = (int)(Gdx.graphics.getWidth()/frame.length);//vSample.length is 128

			}

			sb.begin();
			float data;
			float y;

			for(int i = 0 ; i < frame.length; i++){
				data = (frame[i]+Math.abs(min))/scaler;
				y = 0.1f*Gdx.graphics.getHeight()*data;

				Sprite bubble = bubbles[i];
				
				sb.setColor((float)Math.random(),(float)Math.random(),(float)Math.random(),0.5f);
				
				int apartDistance = (int) (15*Math.random())+10; // something between 15 and 25
				
				if(i%(apartDistance)==0){	//this step sets how far apart bubbles are
					sb.draw(bubble,
							(float)i*ratio,
							y+Gdx.graphics.getHeight()/2,
							y, y);
				}
				sb.setColor(1.0f, 1.0f, 1.0f,1.0f);
			}
			sb.end();
		}
	}

	/*
	public static void draw1(ImmediateModeRenderer r){
		if(activate){
			//TODO java.lang.IndexOutOfBoundsException: Invalid index 382, size is 382
			int frameNumber = (int)(MusicData.getPosition()/MusicData.getFrameDuration());
			float[] frame = samples.get(frameNumber);

			if (frameNumber%3==0){
				//update VSample

				//System.out.println("frame length:"+frame.length);
				System.arraycopy(frame, 0, vSample, 0, vSample.length);

				sum = 0;
				min = 0;
				for(float f: vSample){
					//System.out.print(f+",");
					sum += f; 
					if(f<min){
						min = f;
					}
				}
				//System.out.println();
				//System.out.println("***************min****************:"+min);
				//System.out.println("***************sum****************:"+sum);

				scaler = (sum/vSample.length)+Math.abs(min); //sum+min*frame.length/frame.length
				//System.out.println("***************scaler****************:"+scaler);
				ratio = (int)(Gdx.graphics.getWidth()/vSample.length);//vSample.length is 128
				//System.out.println("***************ratio****************:"+ratio);
			}
			Gdx.gl.glLineWidth(4);

			float data = 0;
			for(int i = 0 ; i < vSample.length ; i++){	
				data = (vSample[i]+Math.abs(min))/scaler;
				//System.out.println(i+": "+data);
				r.color(1, 0, 0, 1);
				r.begin(GL10.GL_LINES);
				r.vertex(i*ratio, 0, 0);
				float y = 0.5f*Gdx.graphics.getHeight()*data;
				r.vertex(i*ratio, y, 0);	
				r.end();	
				//System.out.println(i+": "+y);
			}

		}
	}*/
}
