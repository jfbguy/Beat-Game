package edu.mills.cs280.audiorunner;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
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
import android.util.Log;

public class BrowseMusic extends Activity {
	/** Called when the activity is first created. */
	private ListView musiclist;
	private TextView selectedSong;
	private Button startGame;
	private Cursor musiccursor;
	private int music_column_index;
	private int count;
	//MediaPlayer mMediaPlayer;
	public String filename;

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
				MediaStore.Video.Media.DURATION };
		musiccursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				proj, null, null, null);
		count = musiccursor.getCount();
		
		musiclist = (ListView) findViewById(R.id.PhoneMusicList);
		//musiclist.layout(0, 0,getWindowManager().getDefaultDisplay().getWidth(), (int) (0.7*getWindowManager().getDefaultDisplay().getHeight()) );
		selectedSong = (TextView) findViewById(R.id.selected_song);
		
		Log.d("screen height: ", " "+(getWindowManager().getDefaultDisplay().getHeight()) );
		
		startGame = (Button) findViewById(R.id.Start_Game_Button);
		startGame.setEnabled(false);
		musiclist.setAdapter(new MusicAdapter(getApplicationContext()));
		musiclist.setOnItemClickListener(musicgridlistener);
		
		//start the game activity
		startGame.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				Intent gameIntent = new Intent(v.getContext(), AudioRunnerActivity.class);
                gameIntent.putExtra("song", filename);
				startActivityForResult(gameIntent, 0);
			}
		});
	}

	private OnItemClickListener musicgridlistener = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position, long id) {
			System.gc();
			music_column_index = musiccursor
			.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			musiccursor.moveToPosition(position);
			//TODO: filename tells where the music is
			//needs to pass it onto GameHandler
			filename = musiccursor.getString(music_column_index);
			selectedSong.setText(filename);
			startGame.setEnabled(true);
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
			tv.setTextSize(20);
			String id = null;
			
			music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
			musiccursor.moveToPosition(position);
			
			id = musiccursor.getString(music_column_index);
			music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
			musiccursor.moveToPosition(position);
			id += "  Duration:" + musiccursor.getString(music_column_index);
			
			//reuse the view for each item on the list
			if (convertView == null) {				
				tv.setText(id);
			} else{
				tv = (TextView) convertView;
				tv.setText(id);
			}
			return tv;
		}
	}
}