package com.example.NoteApp;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteHelper extends SQLiteOpenHelper {


    public SQLiteHelper(Context context) {
        super(context, "noteApp.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Folder Table
        String createFoldersTable= "CREATE TABLE folders (folderID INTEGER PRIMARY KEY AUTOINCREMENT, password TEXT, name TEXT)";
        String createNotesTable = "CREATE TABLE notes (noteID INTEGER PRIMARY KEY AUTOINCREMENT, folderID INTEGER, header TEXT, content TEXT, lastModified INTEGER)";
        db.execSQL(createFoldersTable);
        db.execSQL(createNotesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int addFolder(String name, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String statement = String.format("INSERT INTO folders (name, password) VALUES ('%s', '%s')", name, password);
        db.execSQL(statement);

        return (int)getLastIdFromMyTable();
    }
    public void deleteFolder(int folderID){
        SQLiteDatabase db = this.getWritableDatabase();
        String statement = String.format("DELETE FROM folders WHERE folderID = %s",folderID);
        db.execSQL(statement);
    }

    /*
    https://stackoverflow.com/questions/4017903/get-last-inserted-value-from-sqlite-database-android
     */
    public long getLastIdFromMyTable()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement st = db.compileStatement("SELECT last_insert_rowid() from folders");
        return st.simpleQueryForLong();
    }

    public HashMap<String, Object> fetchNote(int noteID){
        HashMap<String, Object> noteArray = new HashMap<String, Object>();


        SQLiteDatabase db = this.getReadableDatabase();
        String rawStatement = "SELECT * FROM notes WHERE noteID = " + noteID;
        Cursor cursor = db.rawQuery(rawStatement, null);

        cursor.moveToFirst();

        noteArray.put("noteID",cursor.getInt(0));
        noteArray.put("folderID",cursor.getInt(1));
        noteArray.put("header",cursor.getString(2));
        noteArray.put("content",cursor.getString(3));
        noteArray.put("lastModified",cursor.getString(4));

        return noteArray;
    }
    public HashMap<String, Object> fetchFolder(int folderID){
        HashMap<String, Object> folderArray = new HashMap<String, Object>();

        // Initialize DB and Query
        SQLiteDatabase db = this.getReadableDatabase();
        String rawStatement = "SELECT * FROM folders WHERE folderID = " + folderID;

        // Open cursor, for selecting information.
        Cursor cursor = db.rawQuery(rawStatement, null);
        cursor.moveToFirst();
        // Populate array with folder information
        folderArray.put("folderID",cursor.getInt(0));
        folderArray.put("password",cursor.getString(1));
        folderArray.put("name",cursor.getString(2));

        return folderArray;

    }

    public Map<Integer, List<Integer>> fetchAllFoldersAndNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        String rawStatement = "SELECT folderID FROM folders";
        Cursor cursor = db.rawQuery(rawStatement,null);

        Map<Integer, List<Integer>> folderArray = new HashMap<Integer, List<Integer>>();

        try {
            // Go through all folder IDs.
            while (cursor.moveToNext()) {
                // Column 0 = folder ID
                int folderID = cursor.getInt(0);
                String subStatement = "SELECT noteID FROM notes WHERE folderID = " + folderID;
                Cursor subCursor = db.rawQuery(subStatement, null);

                List<Integer> notesInFolder = new ArrayList<>();

                while(subCursor.moveToNext()){
                    notesInFolder.add(subCursor.getInt(0));
                }
                subCursor.close();

                folderArray.put(folderID, notesInFolder);
            }
        }
        finally {
            // Close cursor to prevent unused instance. Otherwise, there will be a slight memory leak and waste of resources.
            cursor.close();
            Log.i("SQLiteHelper",folderArray.toString());
        }
        return folderArray;


    }
}
