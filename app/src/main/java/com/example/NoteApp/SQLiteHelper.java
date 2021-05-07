package com.example.NoteApp;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.time.Instant;
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

    public ArrayList<Integer> getNotes(int folderId){
        ArrayList<Integer> notes = new ArrayList<Integer>();

        SQLiteDatabase db = getReadableDatabase();
        String statement = String.format("SELECT * FROM notes WHERE folderID = %s",folderId);
        Cursor cursor = db.rawQuery(statement, null);

        try{
            while(cursor.moveToNext()){
                int noteID = cursor.getInt(0);
                notes.add(noteID);
            }
        }
        finally {
            db.close();
        }
        return notes;
    }


    public int addFolder(String name, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        // In the future, for better security, this should be prepared to prevent SQL injection.
        String statement = String.format("INSERT INTO folders (name, password) VALUES ('%s', '%s')", name, password);
        db.execSQL(statement);

        return (int)getLastIdFromMyTable("folders");
    }
    public void deleteFolder(int folderID){
        SQLiteDatabase db = this.getWritableDatabase();
        String statement = String.format("DELETE FROM folders WHERE folderID = %s",folderID);
        db.execSQL(statement);
        db.close();
    }
    public void deleteNote(int noteID){
        SQLiteDatabase db = this.getWritableDatabase();
        String statement = String.format("DELETE FROM notes WHERE noteID = %s",noteID);
        db.execSQL(statement);
        db.close();
    }

    public int addNote(int folderId, String header){
        SQLiteDatabase db = this.getWritableDatabase();

        // In the future, for better security, this should be prepared to prevent SQL injection.
        // Instant.now().getEpochSecond() gets the Unix timestamp.
        String statement = String.format("INSERT INTO notes (folderID, header,content,lastModified) VALUES (%s, '%s','',%s)", folderId, header, Instant.now().getEpochSecond());
        db.execSQL(statement);

        return (int)getLastIdFromMyTable("notes");

    }

    /*
    https://stackoverflow.com/questions/4017903/get-last-inserted-value-from-sqlite-database-android
     */
    public long getLastIdFromMyTable(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement st = db.compileStatement("SELECT last_insert_rowid() from "+tableName+"");
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
        //db.execSQL("INSERT INTO notes (folderID, header, content, lastModified) VALUES (10, 'directions to the gulag', 'siberia', 9999)");
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
