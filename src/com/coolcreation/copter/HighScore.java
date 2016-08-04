package com.coolcreation.copter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HighScore extends SQLiteOpenHelper {
	
	
static final String dbName="myDB";
	
static final String Table_name="High_Score";
	static final String column_name="highScore";
	
	public HighScore(Context mainMenuScene) {
		super(mainMenuScene,dbName,null, 1);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS"+Table_name+"("+column_name+"Text"+")");
		
		ContentValues cv=new ContentValues();
		
		cv.put(column_name, 0);
		
		db.insert(Table_name, null,cv);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS"+Table_name);
		onCreate(db);
	}
	
	
	
	public String get_high_score()
	{
		
	SQLiteDatabase myDB=this.getReadableDatabase();
	
		Cursor myCursor=myDB.rawQuery("SELECT"+column_name+"Table_name", null);
		myCursor.moveToFirst();
		
		int index=myCursor.getColumnIndex(column_name);
		String myAnswer=myCursor.getString(index);
		
		myCursor.close();
		return myAnswer;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
