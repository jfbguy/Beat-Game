package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.media.opengl.GL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;

public class MusicVisualizer{
	private static ArrayList<float[]> samples;
	private static boolean activate = false;
	private static float[] vSample = new float[128];
	private static TextureRegion tex;

	private MusicVisualizer(){

	}

	public static void setupMusicVisualizer(){
		samples = MusicData.getSamples();
		tex = new TextureRegion(new Texture(Gdx.files.internal("data/purple.png")));
		activate = true;
	}

	private static float scaler = 0;
	private static int ratio = 4;
	private static float min = 0;
	private static float sum = 0;

	public static void draw(SpriteBatch sb){
		if(activate){
			int frameNumber = (int)(MusicData.getPosition()/MusicData.getFrameDuration());
			float[] frame = samples.get(frameNumber);

			if (frameNumber%4==0){
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
			for(int i = 0 ; i < frame.length ; i++){
				data = (frame[i]+Math.abs(min))/scaler;
				y = 0.05f*Gdx.graphics.getHeight()*data;
				
				sb.draw(tex,
						(float)i*ratio,
						y+Gdx.graphics.getHeight()/2,
						Gdx.graphics.getWidth()/40,
						(float)tex.getTexture().getHeight());
				//sb.draw(tex.getTexture(),	//texture region
				//		(float)i*ratio,  //x position
				//		0);	//y position); //rotation 
			}
			sb.end();
		}
	}

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
	}
}
