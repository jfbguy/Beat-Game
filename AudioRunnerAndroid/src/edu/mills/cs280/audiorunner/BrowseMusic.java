package edu.mills.cs280.audiorunner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.LayoutInflater;
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
	public static boolean exitFlag = false;
	public static String songName;
	public static int highScore;
		
	private ListView musiclist;
	private TextView selectedSong,duration,name;
	private Button startGame, startGameDefault;
	private Cursor musicCursor;
	private int musicColumnIndex;
	private int count;
	//MediaPlayer mMediaPlayer;
	public String filename;
	public int songDuration;

	static final private int LOAD_ACTIVITY = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_list);
		if(hasStorage(false)){
			initMusicList();
		}else{
			Intent gameIntent = new Intent(getApplicationContext(), AudioRunnerActivity.class);
			startActivityForResult(gameIntent, 0);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==LOAD_ACTIVITY){
			System.gc();
		}
	}

	private void initMusicList() {
		//TODO: need to check if SD Card is avaialble
		System.gc();
		String[] proj = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DURATION };
		musicCursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				proj, null, null, null);
		selectedSong = (TextView) findViewById(R.id.selected_song);
		if(musicCursor == null){
			//TODO Ideally prompt user to a new screen
			selectedSong.setText("There is no song on your SD card");
			startGame.setEnabled(false);
		}else{
			count = musicCursor.getCount();
			musiclist = (ListView) findViewById(R.id.PhoneMusicList);
			//musiclist.layout(0, 0,getWindowManager().getDefaultDisplay().getWidth(), (int) (0.7*getWindowManager().getDefaultDisplay().getHeight()) );

			duration = (TextView) findViewById(R.id.song_duration);
			name = (TextView)findViewById(R.id.song_name);

			startGame = (Button) findViewById(R.id.Start_Game_Button);
			startGame.setEnabled(false);
			startGameDefault  = (Button) findViewById(R.id.Start_Game_Button_Default);

			musiclist.setAdapter(new MusicAdapter(getApplicationContext()));
			musiclist.setOnItemClickListener(musicgridlistener);

			//start the game activity
			startGame.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					Intent loadIntent = new Intent(getApplicationContext(), LoadMusic.class);
					loadIntent.putExtra("song", filename);
					MusicData.setFile(filename);
					Log.d("duration","********************"+songDuration+"*****************");

					MusicData.setDuration((float)songDuration);
					startActivityForResult(loadIntent, LOAD_ACTIVITY);
				}
			});

			startGameDefault.setOnClickListener(new View.OnClickListener() {	
				public void onClick(View v) {
					Intent loadIntent = new Intent(getApplicationContext(), LoadMusic.class);
					MusicData.setFile("/mnt/sdcard/music/Freezepop - Starlight (Karacter Remix).mp3");
					MusicData.setDuration(298000f);
					startActivityForResult(loadIntent, LOAD_ACTIVITY);
				}
			});
		}
	}

	private OnItemClickListener musicgridlistener = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position, long id) {
			System.gc();
			musicColumnIndex = musicCursor
			.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			musicCursor.moveToPosition(position);
			//TODO: filename tells where the music is
			//needs to pass it onto GameHandler
			filename = musicCursor.getString(musicColumnIndex);
			selectedSong.setText(filename);

			//song title
			musicColumnIndex = musicCursor
			.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
			musicCursor.moveToPosition(position);
			String songName = musicCursor.getString(musicColumnIndex);
			MusicData.setName(songName);
			name.setText(songName);

			//duration
			musicColumnIndex = musicCursor
			.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
			musicCursor.moveToPosition(position);
			songDuration = Integer.parseInt(musicCursor.getString(musicColumnIndex));
			duration.setText(convertTime(songDuration));

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
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item, null);
			}
			TextView tt = (TextView) v.findViewById(R.id.toptext);
			TextView bt = (TextView) v.findViewById(R.id.bottomtext);

			String title,artist = null;

			musicColumnIndex = musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
			musicCursor.moveToPosition(position);
			title = musicCursor.getString(musicColumnIndex);
			tt.setText(title);                            

			musicColumnIndex = musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
			musicCursor.moveToPosition(position);
			artist = musicCursor.getString(musicColumnIndex);
			bt.setText(artist);

			return v;
		}
	}

	public static boolean hasStorage(boolean requireWriteAccess) {  
		String state = Environment.getExternalStorageState();  

		if (Environment.MEDIA_MOUNTED.equals(state)) {  
			return true;  
		} else if (!requireWriteAccess  
				&& Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {  
			return true;  
		}  
		return false;  
	}  

	public String convertTime(int ms){
		int s = ms/1000;
		int min = s/60;
		int sec = s%60;
		return min+":"+(sec >= 10? sec: "0"+sec);
	}

	public boolean isMP3(String fileName){
		return fileName.contains("mp3");
	}

	public void onResume() {
		super.onResume();
		
		if (exitFlag) {
            Intent thisIntent = new Intent(getBaseContext(), Scores.class);
            startActivityForResult(thisIntent, 0);		
		}
		
	}
	
	public void onDestroy() {
		super.onDestroy();
	}
	
}