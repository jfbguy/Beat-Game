package edu.mills.cs280.audiorunner;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Handles the scores generated at the end of the game.
 * @author Dave 
 *
 */
public class Scores extends Activity {
	private ScoresData scores;
	String playerName; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.high_scores);
		scores = new ScoresData(this);

		 if (BrowseMusic.exitFlag) {
			 addScore(BrowseMusic.songName, BrowseMusic.highScore);
			 BrowseMusic.exitFlag = false;
		 }
		 else {
			showSongList();
	     }

		Button settings = (Button) findViewById(R.id.Return_Button);
		settings.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent thisIntent = new Intent(view.getContext(), MainMenu.class);
				startActivityForResult(thisIntent, 0);
				finish();
			}
		});

	}

	
	/**
	 * Adds a new High score to High Scores Database
	 * 
	 * @param song The song's name
	 * @param score The score to be added to the database
	 */
	private void addScore(final String song, final int score) {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Your Name");
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				ArrayList<Integer> songScores = getScores(song);
				ArrayList<String> playerNames = getNames(song);
				playerName = input.getText().toString();
				
				boolean newScoreFlag;
				if (songScores.get(0) == 0) {
					newScoreFlag = true;
				} else {
					newScoreFlag = false;
				}
					
				
				if (score > songScores.get(0)) {
					songScores.add(0, score);
					playerNames.add(0, playerName);
				}

				else {
					for (int i = 0; i <= 8; i++) {
						if (songScores.get(i) >= score && songScores.get(i + 1) < score) {
							songScores.add(i + 1, score);
							playerNames.add(i + 1, playerName);
							break;
						}
					}
				}
				
				SQLiteDatabase db = scores.getWritableDatabase();
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
				values.put("name0", playerNames.get(0));
				values.put("name1", playerNames.get(1));
				values.put("name2", playerNames.get(2));
				values.put("name3", playerNames.get(3));
				values.put("name4", playerNames.get(4));
				values.put("name5", playerNames.get(5));
				values.put("name6", playerNames.get(6));
				values.put("name7", playerNames.get(7));
				values.put("name8", playerNames.get(8));
				values.put("name9", playerNames.get(9));

				if (newScoreFlag) {
					db.insert("scoresTable", "title = '" + song + "'", values);
				} else {
					db.update("scoresTable", values, "title=?", new String[] { song });
				}
				db.close();
				showSongList();

			}
		});
		alert.show();
		
	}
	
	/**
	 * Returns a list of scores that have been added to the scoreList array.  
	 * @param song The song's name
	 * @return the generated score list
	 */
	private ArrayList<Integer> getScores(String song) {
		ArrayList<Integer> scoreList = new ArrayList<Integer>();
		final String[] QUERY = { _ID, "title", "score0", "score1", "score2",
				"score3", "score4", "score5", "score6", "score7", "score8",
				"score9" };
		final String QUERYSONG = "title = '" + song + "'";
		SQLiteDatabase db = scores.getReadableDatabase();
		Cursor cursor = db.query("scoresTable", QUERY, QUERYSONG, null, null,
				null, null);

		try {
			startManagingCursor(cursor);
			cursor.moveToFirst();
			for (int i = 0; i < 10; i++) {
				scoreList.add(Integer.parseInt(cursor.getString(i + 2)));
			}
		}

		catch (Exception e) {
			for (int i = 0; i < 10; i++) {
				scoreList.add(0);
			}
		}
		db.close();
		return scoreList;
	}

	/**
	 * Returns a list of player's names that have been added to the nameList array. 
	 * @param song The song's name	
	 * @return the generated name list
	 */
	private ArrayList<String> getNames(String song) {

		ArrayList<String> nameList = new ArrayList<String>();

		final String[] QUERY = { _ID, "title", "name0", "name1", "name2",
				"name3", "name4", "name5", "name6", "name7", "name8", "name9" };

		final String QUERYSONG = "title = '" + song + "'";
		SQLiteDatabase db = scores.getReadableDatabase();
		Cursor cursor = db.query("scoresTable", QUERY, QUERYSONG, null, null, null, null);

		try {
			startManagingCursor(cursor);
			cursor.moveToFirst();
			for (int i = 0; i < 10; i++) {
				nameList.add(cursor.getString(i + 2));
			}
		}

		catch (Exception e) {
			nameList.add("Senator Dave");
			nameList.add("His Royal Highness Josef Krein");
			nameList.add("Doctor Trevorkian");
			nameList.add("Judge Jackie");
			nameList.add("Yoyomyo");
			nameList.add("Super Cyberbunny Runner");
			nameList.add("Smoke Force");
			nameList.add("Look mom, I made the high scores table");
			nameList.add("I'll huff and puff and ... nevermind");
			nameList.add("National Security Agency");
		}
		db.close();
		return nameList;
	}
	
	private void showSongList() {
		final String[] QUERY = { _ID, "title" };
		final String[] FROM = { "title" };
		final int[] TO = { R.id.songName };
		ArrayList<String> songArray = new ArrayList<String>();
		ListView songList = (ListView) findViewById(R.id.songlist);
		SQLiteDatabase db = scores.getReadableDatabase();

		Cursor cursor = db.query("scoresTable", QUERY, null, null, null, null, "title ASC");
		startManagingCursor(cursor);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			songArray.add(cursor.getString(1));
			cursor.moveToNext();
		}
		db.close();

		final ArrayList<String> SONG_ARRAY_FINAL = songArray;
		songList.setAdapter(new SimpleCursorAdapter(this, R.layout.item, cursor, FROM, TO));

		songList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String selectedSong = SONG_ARRAY_FINAL.get(position);
				ArrayList<Integer> showScore = getScores(selectedSong);
				ArrayList<String> showName = getNames(selectedSong);

				TextView score0 = (TextView) findViewById(R.id.score0);
				String score0String =  showName.get(0) + " : " +Integer.toString(showScore.get(0));
				score0.setText(score0String);	
				TextView score1 = (TextView) findViewById(R.id.score1);
				String score1String =  showName.get(1) + " : " +Integer.toString(showScore.get(1));
				score1.setText(score1String);
				TextView score2 = (TextView) findViewById(R.id.score2);
				String score2String =  showName.get(2) + " : " +Integer.toString(showScore.get(2));
				score2.setText(score2String);
				TextView score3 = (TextView) findViewById(R.id.score3);
				String score3String =  showName.get(3) + " : " +Integer.toString(showScore.get(3));
				score3.setText(score3String);
				TextView score4 = (TextView) findViewById(R.id.score4);
				String score4String =  showName.get(4) + " : " +Integer.toString(showScore.get(4));
				score4.setText(score4String);
				TextView score5 = (TextView) findViewById(R.id.score5);
				String score5String =  showName.get(5) + " : " +Integer.toString(showScore.get(5));
				score5.setText(score5String);
				TextView score6 = (TextView) findViewById(R.id.score6);
				String score6String =  showName.get(6) + " : " +Integer.toString(showScore.get(6));
				score6.setText(score6String);
				TextView score7 = (TextView) findViewById(R.id.score7);
				String score7String =  showName.get(7) + " : " +Integer.toString(showScore.get(7));
				score7.setText(score7String);	
				TextView score8 = (TextView) findViewById(R.id.score8);
				String score8String =  showName.get(8) + " : " +Integer.toString(showScore.get(8));
				score8.setText(score8String);
				TextView score9 = (TextView) findViewById(R.id.score9);
				String score9String =  showName.get(9) + " : " +Integer.toString(showScore.get(9));
				score9.setText(score9String);	
			}
		});
	}
	
	public void onResume() {
		super.onResume();
	}
	
	public void onDestroy() {
		super.onDestroy();
	}
	
	public void onPause() {
		super.onPause();
	}
}
