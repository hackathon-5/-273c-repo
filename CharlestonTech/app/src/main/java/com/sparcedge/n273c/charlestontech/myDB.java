package com.sparcedge.n273c.charlestontech;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Evea on 8/29/2015.
 */


public class myDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myEvents.db";
    public static final String TABLE_EVENTS = "Events";
    public static final String COLUMN_ID = "mName";
    public static final String COLUMN_EVENTNAME = "eventname";

    public myDB(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EVENTNAME + " TEXT );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS + ";");
        onCreate(db);
    }
    public ArrayList<String> databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> al = new ArrayList<String>();
        String query = "SELECT * FROM " + TABLE_EVENTS + " WHERE 1 ORDER BY " + COLUMN_EVENTNAME + " ASC;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("eventname")) != null) {
                al.add(c.getString(c.getColumnIndex("eventname")));
            }
            c.moveToNext();
        }
        db.close();
        return al;
    }
}
