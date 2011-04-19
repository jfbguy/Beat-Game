package edu.mills.cs280.audiorunner;

import java.util.List;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
 
public class AudioRunnerActivity extends AndroidApplication {
	
	public String music_file;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Bundle extras = getIntent().getExtras();
    	if(extras !=null)
    	{
    		music_file = extras.getString("song");
    	}
    	
    	MusicData.setFile(music_file);
		PeakFinder songData = new PeakFinder(MusicData.getFileLocation());
		List<Float> peaks = songData.returnPeaks();
		MusicData.setpeaks(peaks);
        initialize(new GameHandler(music_file), false);
    }

}