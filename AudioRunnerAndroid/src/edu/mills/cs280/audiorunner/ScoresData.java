package edu.mills.cs280.audiorunner;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creates the scores database 
 * @author Dave
 *
 */
public class ScoresData extends SQLiteOpenHelper {

	/**
	 * Creates a helper object to create and manage the scores database. 
	 * This method always returns very quickly. 
	 * @param ctx obtains access to the database
	 */
	public ScoresData(Context ctx) {
		super(ctx, "scores.db", null, 1);
	}

	/**
	 * Initializes database activity. Called when the database is created for the first time. 
	 * This is where the creation of tables and the initial population of the tables should happen.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE scoresTable (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, "
				+ "score0 INTEGER, score1 INTEGER, score2 INTEGER, score3 INTEGER, score4 INTEGER, score5 INTEGER, "
				+ "score6 INTEGER, score7 INTEGER, score8 INTEGER, score9 INTEGER, name0 TEXT, name1 TEXT, name2 TEXT,"
				+ "name3 TEXT, name4 TEXT, name5 TEXT, name6 TEXT, name7 TEXT, name8 TEXT, name9 TEXT);");
	}

	/**
	 * Called when the database needs to be upgraded. 
	 * The implementation should use this method to drop tables, add tables, or do anything else it needs to 
	 * upgrade to the new version. 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new UnsupportedOperationException();
	}
}