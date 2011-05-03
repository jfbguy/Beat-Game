package edu.mills.cs280.audiorunner;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoresData extends SQLiteOpenHelper {

   public ScoresData(Context ctx) {
      super(ctx, "scores.db", null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL("CREATE TABLE scoresTable (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, " +
      		"score0 INTEGER, score1 INTEGER, score2 INTEGER, score3 INTEGER, score4 INTEGER, score5 INTEGER, " +
      		"score6 INTEGER, score7 INTEGER, score8 INTEGER, score9 INTEGER);");
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	   throw new UnsupportedOperationException();
   }
}