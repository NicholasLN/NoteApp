package com.example.NoteApp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;

import java.util.HashMap;

public class Note{

    private Context context;
    private HashMap<String, Object> noteInformation;

    public Note(int noteID, Context applicationContext){
        this.context = applicationContext;

        SQLiteHelper database = new SQLiteHelper(context);
        noteInformation = database.fetchNote(noteID);
    }

    public int getFolder(){
        return (Integer)noteInformation.get("folder");
    }
    public int getID() { return (Integer)noteInformation.get("noteID"); }
    public String getHeader(){
        return (String)noteInformation.get("header");
    }
    public String getContent(){
        return (String)noteInformation.get("content");
    }
    public String getContent(boolean minimized){
        if(minimized){
            // Get first 50 characters of string for note preview.
            if(getContent().length() >= 50) {
                return getContent().substring(0, Math.min(getContent().length(), 50)) + "...";
            }
            else{
                return getContent();
            }
        }
        else{
            return getContent();
        }

    }
    public Integer getLastModified(){
        return (Integer)noteInformation.get("lastModified");
    }

    public static int createNewNote(int folderId, String noteHeader, Context applicationContext){
        SQLiteHelper db = new SQLiteHelper(applicationContext);
        int id = db.addNote(folderId, noteHeader);
        db.close();
        return id;
    }
    public static void deleteNote(int noteID, Context applicationContext){
        SQLiteHelper db = new SQLiteHelper(applicationContext);
        db.deleteNote(noteID);
        db.close();
    }

}
