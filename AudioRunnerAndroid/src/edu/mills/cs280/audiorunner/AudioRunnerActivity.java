package edu.mills.cs280.audiorunner;

import android.os.Bundle;

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
    	
    	if(musicFile!=null){
    		initialize(new GameHandler(musicFile), false);
    	}else{
    		initialize(new GameHandler(), false);
    	}
    }

}
