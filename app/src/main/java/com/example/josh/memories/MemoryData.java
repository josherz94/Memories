package com.example.josh.memories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MemoryData {

    public static final String DEBUG_TAG = "CardsData";

    private SQLiteDatabase db;
    private SQLiteOpenHelper memoryDbHelper;

    private static final String[] COLUMNS = {
            MemoryDbHelper.COLUMN_ID,
            MemoryDbHelper.COLUMN_TITLE,
            MemoryDbHelper.COLUMN_DATETIME,
            MemoryDbHelper.COLUMN_DESCRIPTION,
            MemoryDbHelper.COLUMN_IMAGE,
            MemoryDbHelper.COLUMN_TIMESTAMP,
    };

    public MemoryData(Context context) {
        this.memoryDbHelper = new MemoryDbHelper(context);
    }

    public void open() {
        db = memoryDbHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "memoryDbHelper opened");
    }

    public void close() {
        if (memoryDbHelper != null) {
            memoryDbHelper.close();
            Log.d(DEBUG_TAG, "cardDbHelper closed");
        }
    }

    public ArrayList<Memory> getAll() {
        ArrayList<Memory> memories = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(MemoryDbHelper.TABLE_MEMORY, COLUMNS,null, null, null,
                    null, MemoryDbHelper.COLUMN_TIMESTAMP + " DESC");
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                        Memory memory = new Memory();
                        memory.setId(cursor.getLong(cursor.getColumnIndex(MemoryDbHelper.COLUMN_ID)));
                        memory.setTitle(cursor.getString(cursor.getColumnIndex(MemoryDbHelper.COLUMN_TITLE)));
                        memory.setDate(cursor.getString(cursor.getColumnIndex(MemoryDbHelper.COLUMN_DATETIME)));
                        memory.setDescription(cursor.getString(cursor.getColumnIndex(MemoryDbHelper.COLUMN_DESCRIPTION)));
                        memory.setImage(cursor.getBlob(cursor.getColumnIndex(MemoryDbHelper.COLUMN_IMAGE)));
                        memories.add(memory);
                }
            }
            Log.d(DEBUG_TAG, "Total rows = " + cursor.getCount());
        } catch (Exception e){
            Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return memories;
    }

    public Memory create(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(MemoryDbHelper.COLUMN_TITLE, memory.getTitle());
        values.put(MemoryDbHelper.COLUMN_DATETIME, memory.getDate());
        values.put(MemoryDbHelper.COLUMN_DESCRIPTION, memory.getDescription());
        values.put(MemoryDbHelper.COLUMN_IMAGE, memory.getImage());
        long id = db.insert(MemoryDbHelper.TABLE_MEMORY, null, values);
        memory.setId(id);
        Log.d(DEBUG_TAG, "Insert id is " + String.valueOf(memory.getId()));
        return memory;
    }

    public void update(long position, String title, String date, String description, byte[] byteArray) {
        String whereClause = MemoryDbHelper.COLUMN_ID + "=" + position;
        Log.d(DEBUG_TAG, "Update position is " + String.valueOf(position));
        ContentValues values = new ContentValues();
        values.put(MemoryDbHelper.COLUMN_TITLE, title);
        values.put(MemoryDbHelper.COLUMN_DATETIME, date);
        values.put(MemoryDbHelper.COLUMN_DESCRIPTION, description);
        values.put(MemoryDbHelper.COLUMN_IMAGE, byteArray);
        db.update(MemoryDbHelper.TABLE_MEMORY, values, whereClause, null);
    }

    public void delete(long cardId) {
        String whereClause = MemoryDbHelper.COLUMN_ID + "=" + cardId;
        Log.d(DEBUG_TAG, "Delete position is " + String.valueOf(cardId));
        db.delete(MemoryDbHelper.TABLE_MEMORY, whereClause, null);
    }
}
