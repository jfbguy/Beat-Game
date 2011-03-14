package edu.mills.cs280.audiorunner;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;

public class AudioRunnerActivity extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        initialize(new AudioRunner(), false);  
    }
}