package edu.mills.cs280.audiorunner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * VolumeSeekBar class handles the volume seek bar in the settings page of the
 * application. The AudioManager class provides access to volume mode control.
 * 
 * @author jvizcain
 * 
 */

public class VolumeSeekBar extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
		final TextView seekBarValue = (TextView) findViewById(R.id.seekbarvalue);

		final AudioManager audioManager;

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager
		.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		SeekBar volControl = (SeekBar) findViewById(R.id.seekbar);
		volControl.setMax(maxVolume);
		volControl.setProgress(curVolume);
		volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {


				/**
				 * Notifies that the progress level of the seekbar has
				 * changed.
				 * @param seekBar The seekbar whose progess has changed
				 * @param progress The current progress level. This will be in the range 0 to max 
				 * where max was set by setMax(int)
				 * @param fromUser If the progress was changed by user this will be true
				 */

				seekBarValue.setText(String.valueOf(progress));
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						progress, 0);
			}

			/**
			 * Notifies that the user has started the touch gesture
			 * @param seekBar is where the gesture began
			 */
			
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			/**
			 * Notifies that the user has finished the touch gesture
			 * @param seekBar The seekBar where the gesture began
			 */
			
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		Button settings = (Button) findViewById(R.id.Highscores_Button);
		settings.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent thisIntent = new Intent(view.getContext(), Scores.class);
				startActivityForResult(thisIntent, 0);
			}
		});

	}

}
