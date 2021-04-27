package com.example.NoteApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NOTE_HEADER = "NOTE_HEADER";
    public static final String COLUMN_NOTE_PASSWORD = "NOTE_PASSWORD";
    public static final String COLUMN_NOTE_CONTENT = "NOTE_CONTENT";
    public static final String COLUMN_NOTE_LAST_MODIFIED = "NOTE_LAST_MODIFIED";
    public static final String TABLE_USER_NOTES = "USER_NOTES";


    public SQLiteHelper(@Nullable Context context) {
        super(context, "notes.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = String.format("CREATE TABLE %s " +
                "( %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                  "%s TEXT, " +
                  "%s TEXT, " +
                  "%s TEXT, " +
                  "%s TEXT )",
                TABLE_USER_NOTES, COLUMN_ID, COLUMN_NOTE_HEADER, COLUMN_NOTE_PASSWORD, COLUMN_NOTE_CONTENT, COLUMN_NOTE_LAST_MODIFIED);

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
