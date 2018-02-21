package com.example.valchapple.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by valchapple on 2/21/18.
 */

public class SQLiteDBHelper extends SQLiteOpenHelper {
    // Constructor
    public SQLiteDBHelper(Context context) {
        super(context, SQLiteDB.LocationTable.DB_NAME,
                null, SQLiteDB.LocationTable.DB_VERSION);
    }

    // Create Database

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteDB.LocationTable.SQL_CREATE_LOCATION_TABLE);

        // Sample Values
        ContentValues sample = new ContentValues();
        sample.put(SQLiteDB.LocationTable.COLUMN_NAME_TEXT_STRING, "asdf");
        sample.put(SQLiteDB.LocationTable.COLUMN_NAME_LATITUDE, "30.0");
        sample.put(SQLiteDB.LocationTable.COLUMN_NAME_LONGITUDE, "3.0");
        db.insert(SQLiteDB.LocationTable.TABLE_NAME, null, sample);
    }

    // Drop Table on Upgrade and start from scratch
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLiteDB.LocationTable.SQL_DROP_LOCATION_TABLE);
        onCreate(db);
    }
}
