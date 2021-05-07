package com.example.NoteApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class FolderListAdapter extends ArrayAdapter<Folder>{
    private static final String TAG = "FolderListAdapter";

    private Context mContext;
    int mResource;
    ArrayList<Folder> afolders;

    public FolderListAdapter(Context context, int resource, ArrayList<Folder> folders) {
        super(context, resource, folders);
        afolders = folders;
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String name = getItem(position).getName();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);
        TextView folderName = (TextView) convertView.findViewById(R.id.noteHeader);
        folderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int folderID = getItem(position).getID();
                String folderPassword = getItem(position).getPassword();

                // If there is no password for the folder
                if(folderPassword.isEmpty()) {
                    Intent intent = new Intent(mContext, Folders.class);
                    intent.putExtra("folderId", folderID);
                    mContext.startActivity(intent);
                }
                // If there is a password..
                else{
                    // Create a dialog asking for password
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    // Password input fragment
                    View passwordDialogInput = LayoutInflater.from(mContext).inflate(R.layout.folder_password_input,(ViewGroup)v.getRootView(), false);

                    final EditText passwordInputView = (EditText) passwordDialogInput.findViewById(R.id.passwordInput);
                    builder
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_baseline_lock_24)
                            .setTitle("ENTER FOLDER PASSWORD")
                            .setView(passwordDialogInput)
                            .setPositiveButton("Enter Password", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String enteredPassword = passwordInputView.getText().toString();
                                    if (enteredPassword.equals(folderPassword)) {
                                        Intent intent = new Intent(mContext, Folders.class);
                                        intent.putExtra("folderId", folderID);
                                        mContext.startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(mContext,"Wrong Password!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                    builder.show();
                }
            }
        });
        folderName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_password_warning)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this folder?\nThis will delete all notes within this folder.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // SQL function to delete folder.
                                Folder.deleteFolder(getItem(position).getID(), mContext);

                                afolders.remove(getItem(position));

                                // This refreshes the data, so that the folder gets wiped from MainActivity.
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("No", null).show();

                return false;
            }
        });

        folderName.setText(name);

        return convertView;

    }


}
