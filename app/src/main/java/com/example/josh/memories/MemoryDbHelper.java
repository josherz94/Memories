package com.example.josh.memories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


    public class MemoryDbHelper extends SQLiteOpenHelper {
        private static final String DEBUG_TAG = "MemoryDbHelper";
        private static final String DB_NAME = "memory.db";
        private static final int DB_VERSION = 1;

        public static final String TABLE_MEMORY = "MEMORY";
        public static final String COLUMN_ID = "_ID";
        public static final String COLUMN_TITLE = "TITLE";
        public static final String COLUMN_DATETIME = "DATETIME";
        public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
        public static final String COLUMN_IMAGE = "IMAGE";
        public static final String COLUMN_TIMESTAMP = "TIMESTAMP";

        private static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_MEMORY + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_DATETIME + " INTEGER, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_IMAGE + " BLOB, " +
                        COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";

        public MemoryDbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE);
            Log.i(DEBUG_TAG, "Table has been created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMORY);
            onCreate(db);
        }


}
