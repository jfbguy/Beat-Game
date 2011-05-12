package edu.mills.cs280.audiorunner;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * MainMenu is the welcome screen. 
 * It starts BrowseMusic Activity once the select-music button is clicked. 
 * @author ymiao, dlu, jvizcaino
 *
 */
public class MainMenu extends Activity{
	private static final boolean menuMusic = false;
	static MediaPlayer mp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		mp = MediaPlayer.create(getBaseContext(), R.raw.kickshock);
		try {
			mp.prepare();

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(menuMusic){
			mp.start();
			mp.setLooping(true);
		}

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

	/**
	 * Called when the activity resumes
	 */
	public void onResume() {
		super.onResume();
	}

	/**
	 * Called when the activity is paused
	 */
	public void onPause() {
		super.onPause();
	}

	/**
	 * Called when the activity is destroyed or finished. In this case the song
	 * will stop playing because its calling media player. 
	 */
	public void onDestroy() {
		super.onDestroy();
		mp.release();
	}


}


