package edu.mills.cs280.audiorunner;

import android.content.Intent;


import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;

 /**
  * Starts game by calling GameHandler
  * 
  * @author jklein
  */
public class AudioRunnerActivity extends AndroidApplication implements OnExitListener{

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    		
    		GameHandler listener = new GameHandler(this);
    		initialize(listener, false);
    }
	
	@Override
    public void onExit()
    {
        this.finish();
    }
	
}
