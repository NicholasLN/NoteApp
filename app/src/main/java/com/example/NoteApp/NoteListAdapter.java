package com.example.NoteApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class NoteListAdapter extends ArrayAdapter<Note> {

    private static final String TAG = "NoteListAdapter";

    private Context mContext;
    int mResource;
    ArrayList<Note> aNotes;

    public NoteListAdapter(@NonNull Context context, int resource, ArrayList<Note> notes) {
        super(context, resource, notes);
        mContext = context;
        mResource = resource;
        aNotes = notes;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = getItem(position);

        String noteTitle = note.getHeader();
        // True for minimized content.
        String noteContent = note.getContent(true);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.noteListAdapterLayout);
        TextView noteHeader = (TextView) convertView.findViewById(R.id.noteHeader);
        TextView noteMsg = (TextView) convertView.findViewById(R.id.noteContent);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        Date unixToDate = new Date(note.getLastModified()*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy\nhh:ss aa");
        sdf.setTimeZone(TimeZone.getDefault());
        date.setText(sdf.format(unixToDate));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoteEditor.class);
                intent.putExtra("noteId",note.getID());
                mContext.startActivity(intent);

            }
        });
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_password_warning)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // SQL function to delete folder.
                                Note.deleteNote(note.getID(), mContext);

                                aNotes.remove(getItem(position));

                                // This refreshes the data, so that the folder gets wiped from MainActivity.
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("No", null).show();

                return false;
            }
        });


        noteHeader.setText(noteTitle);
        noteMsg.setText(new SpannableString(Html.fromHtml(noteContent, HtmlCompat.FROM_HTML_MODE_LEGACY)));
        return convertView;

    }
}
