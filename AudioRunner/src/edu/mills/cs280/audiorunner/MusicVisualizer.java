package edu.mills.cs280.audiorunner;

import java.util.ArrayList;
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
		//tex = new TextureRegion(new Texture(Gdx.files.internal("data/star.png")));
		activate = true;
	}

	
	public static void draw2(SpriteBatch sb){
		if(activate){
			float[] frame = samples.get((int)(MusicData.getPosition()/MusicData.getFrameDuration()));
			for(int i = 0; i < frame.length; i++){
				vSample[i] += frame[i];
				vSample[i] %= Gdx.graphics.getHeight();
				vSample[i] /= 4f;
			}
			
			float sum = 0;
			float min = 0;
			for(float f: vSample){
				//System.out.print(f+",");
				sum += Math.abs(f); 
				if(f<min){
					min = f;
				}
			}
			//System.out.println();
			float scaler = sum/vSample.length + min ; //sum+min*frame.length/frame.length
			int ratio = (int)Gdx.graphics.getWidth()/vSample.length;//vSample.length is 128			
			for(int i = 0 ; i < vSample.length ; i+=ratio){
				
				float data = vSample[i]/scaler;
				float y = 0.1f*Gdx.graphics.getHeight()*(data+min);
				sb.begin();		
				sb.draw(tex.getTexture(),
						i*ratio,//Draw at X position
						y+20);	
				sb.end();
			}
		}
	}
	
	public static void draw(ImmediateModeRenderer r){
		if(activate){
			//TODO java.lang.IndexOutOfBoundsException: Invalid index 382, size is 382
			float[] frame = samples.get((int)(MusicData.getPosition()/MusicData.getFrameDuration()));
			System.out.println("frame length:"+frame.length);
			System.arraycopy(frame, 0, vSample, 0, vSample.length);
			
			float sum = 0;
			float min = 0;
			for(float f: vSample){
				//System.out.print(f+",");
				sum += Math.abs(f); 
				if(f<min){
					min = f;
				}
			}
			//System.out.println();
			//System.out.println("***************min****************:"+min);
			//System.out.println("***************sum****************:"+sum);
			
			float scaler = (sum/vSample.length)+Math.abs(min); //sum+min*frame.length/frame.length
			//System.out.println("***************scaler****************:"+scaler);
			int ratio = (int)(Gdx.graphics.getWidth()/vSample.length);//vSample.length is 128
			//System.out.println("***************ratio****************:"+ratio);

			Gdx.gl.glLineWidth(10);
			
			float prevData = 0;
			float data = 0;
			for(int i = 0 ; i < vSample.length ; i++){	
				data = (vSample[i]+Math.abs(min))/scaler;
				System.out.println(i+": "+data);
				r.color(1, 1, 1, 1);
				r.begin(GL10.GL_LINES);
				r.vertex(i*ratio, 0, 0);
				float y = 0.5f*Gdx.graphics.getHeight()*(data+prevData);
				r.vertex(i*ratio, y, 0);	
				r.end();	
				//System.out.println(i+": "+y);
			}
		}
	}
}
