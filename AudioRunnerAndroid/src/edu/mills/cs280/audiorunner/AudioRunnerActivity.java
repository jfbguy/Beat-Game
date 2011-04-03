package edu.mills.cs280.audiorunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

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
    	
        initialize(new GameHandler(music_file), false);
    }

}