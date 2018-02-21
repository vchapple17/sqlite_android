package com.example.valchapple.sqlite;

import android.provider.BaseColumns;

/**
 * Created by valchapple on 2/21/18.
 */

final public class SQLiteDB {
    // Constructor
    private SQLiteDB(){};

    // Table 1
    public final class LocationTable implements BaseColumns {
        // Constant class strings
        public static final String DB_NAME = "location_db";
        public static final String TABLE_NAME = "location_table";
        public static final String COLUMN_NAME_TEXT_STRING = "text_string";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final int DB_VERSION = 4;


        // CREATE string
        public static final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " +
                LocationTable.TABLE_NAME + "(" +
                LocationTable._ID + " INTEGER PRIMARY KEY NOT NULL," +
                LocationTable.COLUMN_NAME_TEXT_STRING + " VARCHAR(255), " +
                LocationTable.COLUMN_NAME_LATITUDE + " DOUBLE, " +
                LocationTable.COLUMN_NAME_LONGITUDE + " DOUBLE);";

        // INSERT string
        public static final String SQL_INSERT_LOCATION_TABLE = "INSERT INTO " +
                LocationTable.TABLE_NAME + "(" +
                LocationTable.COLUMN_NAME_TEXT_STRING + ", " +
                LocationTable.COLUMN_NAME_LATITUDE + ", " +
                LocationTable.COLUMN_NAME_LONGITUDE + ") VALUES (?, ?, ?);";

        // DROP string
        public static final String SQL_DROP_LOCATION_TABLE =
                "DROP TABLE IF EXISTS " + LocationTable.TABLE_NAME;
    }

}
