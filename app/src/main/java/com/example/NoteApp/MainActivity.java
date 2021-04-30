package com.example.NoteApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    static ArrayList<String> folders = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    //Creates new folder
    public void newFolder(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Folders.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Note note = new Note(1,getApplicationContext());


        //TODO replace shared preferences with sql
        ListView listView = findViewById(R.id.listView);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.folders", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("folders", null);

        if (set == null) {
            folders.add("Example Folder");
        } else {
            folders = new ArrayList(set);

        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, folders);
        listView.setAdapter(arrayAdapter);

        //Goes to clicked on folder
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Going from MainActivity to NotesEditorActivity
                Intent intent = new Intent(getApplicationContext(), Folders.class);
                intent.putExtra("folderId", i);
                startActivity(intent);
            }
        });

        //Deletes clicked on folder.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;
                // To delete the data from the App
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                folders.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.folders", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(MainActivity.folders);
                                sharedPreferences.edit().putStringSet("folders", set).apply();
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            }
        });
    }
}
