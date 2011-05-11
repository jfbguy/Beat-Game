package edu.mills.cs280.audiorunner;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Handles the start page (main menu) of the application. Plays a song while user navigates the application. 
 * Song will stop when user terminates or exits application. 
 * @author jvizcain
 *
 */
public class MainMenu extends Activity{
	static MediaPlayer mp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        mp = MediaPlayer.create(getBaseContext(), R.raw.kickshock);
		try {
			mp.prepare();

		}
		catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mp.start();
		mp.setLooping(true);
        
        Button start = (Button) findViewById(R.id.Start_Button);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	         	                    	
                Intent myIntent = new Intent(view.getContext(), BrowseMusic.class);
                startActivityForResult(myIntent, 0);
                
            }
        });
        
        Button settings = (Button) findViewById(R.id.Setting_Button);
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent thisIntent = new Intent(view.getContext(), VolumeSeekBar.class);
                startActivityForResult(thisIntent, 0);
            }
        });
        
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	mp.release();
    }
    

    
}
