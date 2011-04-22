package edu.mills.cs280.audiorunner;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoadMusic extends Activity{
	
	public String musicFile;
	private ProgressBar progressIndicator;
	private LinearLayout linProgressBar;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);
    	
    	Bundle extras = getIntent().getExtras();
    	if(extras !=null)
    	{
    		musicFile = extras.getString("song");
    	}
        
    	linProgressBar = (LinearLayout) findViewById(R.id.lin_progress_bar);
        linProgressBar.setVisibility(View.VISIBLE);
        progressIndicator = (ProgressBar) findViewById(R.id.progressbar);
        
        new decodeMusic().execute();

    }
	

	private class decodeMusic extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			MusicData.setFile(musicFile);
			//PeakFinder songData = new PeakFinder();
			//songData.setTestable(true);
			//MusicData.setpeaks(songData.returnPeaks());
			
			System.out.println("TRYING TO LOAD!!!!");
			
			try {
		        FileInputStream fis = new FileInputStream("AudioRunnerAndroid/assets/data/testPeaks.ar");
		        ObjectInputStream in = new ObjectInputStream(fis);
		        @SuppressWarnings("unchecked")
				List<Float> peaks = (List<Float>)in.readObject();
		        in.close();
		        
		        for(float f : peaks){
		        	System.out.println(f+",");
		        }
		        
		        MusicData.setpeaks(peaks);
		        
		      }
		      catch (Exception e) {
		          System.out.println("111111111111EXCEEEEEEEEEEEEEEEEEEPTION!!!" + e);
		      }
		      
		      try {
			        FileInputStream fis = new FileInputStream("AudioRunnerAndroid/assets/data/testSamples.ar");
			        ObjectInputStream in = new ObjectInputStream(fis);
			        @SuppressWarnings("unchecked")
					ArrayList<float[]> samples = (ArrayList<float[]>)in.readObject();
			        in.close();
			        MusicData.setSamples(samples);
			        
			      }
			      catch (Exception e) {
			          System.out.println("222222222222222EXCEEEEEEEEEEEEEEEEEEPTION!!!" + e);
			      }
			
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			Toast.makeText(getBaseContext(), 
                    "Decoding finished", 
                    Toast.LENGTH_SHORT).show();
			
			Intent gameIntent = new Intent(getApplicationContext(), AudioRunnerActivity.class);
            gameIntent.putExtra("song", musicFile);
			startActivityForResult(gameIntent, 0);
            finish();
		}

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			
		}

	}
}
