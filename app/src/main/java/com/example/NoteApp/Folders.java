package com.example.NoteApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;

public class Folders extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    int folderId;

    public void newNote(View view)
    {
        Intent intent = new Intent(getApplicationContext(), NoteEditor.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //TODO replace with sql
        ListView listView = findViewById(R.id.listView);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.NoteApp", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);


        Intent lastIntent = getIntent();
        folderId = lastIntent.getIntExtra("folderId", -1);
        //TODO get folder id and populate notes based on that id. Make box where folder can be named

        if (set == null) {
            notes.add("Example note");
        } else {
            notes = new ArrayList(set);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        //Opens clicked on note
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Going from MainActivity to NotesEditorActivity
                Intent intent = new Intent(getApplicationContext(), NoteEditor.class);
                intent.putExtra("noteId", i);
                startActivity(intent);
            }
        });

        //Deletes clicked on note
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;
                // To delete the data from the App
                new AlertDialog.Builder(Folders.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this folder?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(Folders.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            }
        });
    }

        public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Folders.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}