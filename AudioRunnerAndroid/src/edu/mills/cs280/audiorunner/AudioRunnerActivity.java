package edu.mills.cs280.audiorunner;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.badlogic.gdx.backends.android.AndroidApplication;

 
public class AudioRunnerActivity extends AndroidApplication {
		
	private String musicFile;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Bundle extras = getIntent().getExtras();
    	if(extras !=null)
    	{
    		musicFile = extras.getString("song");
    	}
    	
    	initialize(new GameHandler(musicFile), false);
    }

}
