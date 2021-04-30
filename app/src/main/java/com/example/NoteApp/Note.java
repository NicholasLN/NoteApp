package com.example.NoteApp;

import android.content.Context;

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
    public String getHeader(){
        return (String)noteInformation.get("header");
    }
    public String getContent(){
        return (String)noteInformation.get("content");
    }
    public Integer getLastModified(){
        return (Integer)noteInformation.get("lastModified");
    }


}
