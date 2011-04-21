package edu.mills.cs280.audiorunner;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
 
public class AudioRunnerActivity extends AndroidApplication {
	
	public String musicFile;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Bundle extras = getIntent().getExtras();
    	if(extras !=null)
    	{
    		musicFile = extras.getString("song");
    	}
    	
    	MusicData.setFile(musicFile);
		PeakFinder songData = new PeakFinder();
		List<Float> peaks = songData.returnPeaks();
		MusicData.setpeaks(peaks);
		System.out.println(5);
        initialize(new GameHandler(musicFile), false);

    }
	
	


}
