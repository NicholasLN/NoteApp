package com.example.NoteApp;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;

public class Folders extends AppCompatActivity {

    ArrayList<Note> notes;
    int folderId;
    ListView noteListView;

    public void newNote(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Note Header");
        View noteDialogInput = LayoutInflater.from(this).inflate(R.layout.note_text_input,(ViewGroup)view.getRootView(), false);

        final EditText noteNameInput = (EditText) noteDialogInput.findViewById(R.id.noteName);

        builder.setView(noteDialogInput);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newNoteId = Note.createNewNote(folderId, noteNameInput.getText().toString(),getApplicationContext());

                Intent intent = new Intent(getApplicationContext(), NoteEditor.class);
                intent.putExtra("noteId",newNoteId);
                startActivity(intent);

                notes.add(new Note(newNoteId,getApplicationContext()));
                refreshNotes();
                dialog.dismiss();

            }
        });
        builder.show();


    }

    public void refreshNotes(){
        NoteListAdapter noteListAdapter = new NoteListAdapter(this, R.layout.note_list_adapter, notes);
        noteListView.setAdapter(noteListAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        // Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //

        folderId = getIntent().getIntExtra("folderId",0);
        notes = Folder.getNotesForFolder(folderId, getApplicationContext());
        noteListView = findViewById(R.id.noteListView);

        refreshNotes();
    }





    // ACTION BAR


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}