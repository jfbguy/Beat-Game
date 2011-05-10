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

public class VolumeSeekBar extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);   

		SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);
		final TextView seekBarValue = (TextView)findViewById(R.id.seekbarvalue);

		final AudioManager audioManager;	

		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		SeekBar volControl = (SeekBar)findViewById(R.id.seekbar);
		volControl.setMax(maxVolume);
		volControl.setProgress(curVolume);
		volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				seekBarValue.setText(String.valueOf(progress));
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				//seekBarValue.setText(getString(R.string.seekbar_tracking_on));
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//seekBarValue.setText(getString(R.string.seekbar_tracking_off));

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
