package edu.mills.cs280.audiorunner;

import static android.provider.BaseColumns._ID;
import java.util.ArrayList;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class Scores extends Activity {

	private ScoresData scores;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.high_scores);
		scores = new ScoresData(this);
		showSongList();
	}

	private void addScore(String song, int score) {
		SQLiteDatabase db = scores.getWritableDatabase();
		ArrayList<Integer> songScores = getScores(song);
		if (score > songScores.get(0)) {
			songScores.add(0, score);
		}
		else {
			for(int i=0; i < 9; i++){
				if (songScores.get(i) > score && songScores.get(i+1) < score) {
					songScores.add(i+1, score);
					break;
				}
			}
		}
		
		ContentValues values = new ContentValues();
		values.put("title", song);
		values.put("score0", songScores.get(0));
		values.put("score1", songScores.get(1));
		values.put("score2", songScores.get(2));
		values.put("score3", songScores.get(3));
		values.put("score4", songScores.get(4));
		values.put("score5", songScores.get(5));
		values.put("score6", songScores.get(6));
		values.put("score7", songScores.get(7));
		values.put("score8", songScores.get(8));
		values.put("score9", songScores.get(9));
			
		System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		System.out.println(songScores);
		
		if (songScores.get(9) == 0 || songScores.get(9) == 1000){
			db.insert("scoresTable", "title = '" + song + "'", values);
		}
		else {
			db.update("scoresTable", values, "title=?", new String[] {song});
		}
	}


	private ArrayList<Integer> getScores(String song){

		ArrayList<Integer> scoreList = new ArrayList<Integer>();


		final String[] QUERY = { _ID, "title", "score0", "score1", "score2", "score3", "score4", 
				"score5", "score6", "score7", "score8", "score9" };
		final String QUERYSONG = "title = '" + song + "'";	
		SQLiteDatabase db = scores.getReadableDatabase();
		Cursor cursor = db.query("scoresTable", QUERY, QUERYSONG , null, null, null, null);
		
		try{			
			startManagingCursor(cursor);
			cursor.moveToFirst();
			for(int i = 0; i < 10; i++){		
				scoreList.add(Integer.parseInt(cursor.getString(i+2)));
			}
					
		}
		
		catch(Exception e){
			for(int i=9; i >= 0; i--){
				scoreList.add(i*1000);
			}
		}
		return scoreList;
	}


	private void showSongList() {
		final String[] QUERY = { _ID, "title" };
		final String[] FROM = {"title"};
		final int[] TO = {R.id.songName};  
		ArrayList<String> songArray = new ArrayList<String>();
		ListView songList = (ListView) findViewById(R.id.songlist);
		SQLiteDatabase db = scores.getReadableDatabase();
		
		Cursor cursor = db.query("scoresTable", QUERY, null, null, null, null, "title ASC");
		startManagingCursor(cursor); 
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){	
			songArray.add(cursor.getString(1));
			cursor.moveToNext();
		}

		final ArrayList<String> SONGARRAYFINAL = songArray;  //done because inner class can not refer to non final
		songList.setAdapter( new SimpleCursorAdapter(this, R.layout.item, cursor, FROM, TO) );

		songList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String selectedSong = SONGARRAYFINAL.get(position);
				ArrayList<Integer> showScore = getScores(selectedSong);
				
				TextView score0 = (TextView) findViewById(R.id.score0);
				score0.setText(Integer.toString(showScore.get(0)));	
				TextView score1 = (TextView) findViewById(R.id.score1);
				score1.setText(Integer.toString(showScore.get(1)));	
				TextView score2 = (TextView) findViewById(R.id.score2);
				score2.setText(Integer.toString(showScore.get(2)));	
				TextView score3 = (TextView) findViewById(R.id.score3);
				score3.setText(Integer.toString(showScore.get(3)));	
				TextView score4 = (TextView) findViewById(R.id.score4);
				score4.setText(Integer.toString(showScore.get(4)));	
				TextView score5 = (TextView) findViewById(R.id.score5);
				score5.setText(Integer.toString(showScore.get(5)));	
				TextView score6 = (TextView) findViewById(R.id.score6);
				score6.setText(Integer.toString(showScore.get(6)));	
				TextView score7 = (TextView) findViewById(R.id.score7);
				score7.setText(Integer.toString(showScore.get(7)));		
				TextView score8 = (TextView) findViewById(R.id.score8);
				score8.setText(Integer.toString(showScore.get(8)));	
				TextView score9 = (TextView) findViewById(R.id.score9);
				score9.setText(Integer.toString(showScore.get(9)));	
			}
		});
	}
}






