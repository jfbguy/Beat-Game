package edu.mills.cs280.audiorunner;

import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class BrowseMusic extends Activity {
	/** Called when the activity is first created. */
	ListView musiclist;
	Cursor musiccursor;
	int music_column_index;
	int count;
	MediaPlayer mMediaPlayer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_list);
		init_phone_music_grid();
	}

	private void init_phone_music_grid() {
		System.gc();
		String[] proj = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Video.Media.SIZE };
		musiccursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				proj, null, null, null);
		count = musiccursor.getCount();
		musiclist = (ListView) findViewById(R.id.PhoneMusicList);
		Button start_game = (Button) findViewById(R.id.Start_Game_Button);
		musiclist.setAdapter(new MusicAdapter(getApplicationContext()));
		musiclist.setOnItemClickListener(musicgridlistener);
		start_game.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), AudioRunnerActivity.class);
                startActivityForResult(myIntent, 0);
			}
		});
		mMediaPlayer = new MediaPlayer();
	}

	private OnItemClickListener musicgridlistener = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position, long id) {
			System.gc();
			music_column_index = musiccursor
			.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			musiccursor.moveToPosition(position);
			//TODO: filename tells where the music is
			//needs a global variable to hold this value.
			String filename = musiccursor.getString(music_column_index);
			
  	      Toast.makeText(getApplicationContext(), filename,
    	          Toast.LENGTH_SHORT).show();
/*
			try {
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.reset();
				}
				mMediaPlayer.setDataSource(filename);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}*/
		}
		
	};

	public class MusicAdapter extends BaseAdapter {
		private Context mContext;

		public MusicAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			System.gc();
			TextView tv = new TextView(mContext.getApplicationContext());
			String id = null;
			if (convertView == null) {
				
				music_column_index = musiccursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
				musiccursor.moveToPosition(position);
				
				id = musiccursor.getString(music_column_index);
				/*
				music_column_index = musiccursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
				musiccursor.moveToPosition(position);
				id += " Size(KB):" + musiccursor.getString(music_column_index);
				*/
				tv.setText(id);
			} else
				tv = (TextView) convertView;
			return tv;
		}
	}
}