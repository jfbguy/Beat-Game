package edu.mills.cs280.audiorunner;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;

 //bizzarely, this class does not extend Activity...
public class AudioRunnerActivity extends AndroidApplication {

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    		initialize(new GameHandler(), false);
    }
	
	
	
}
