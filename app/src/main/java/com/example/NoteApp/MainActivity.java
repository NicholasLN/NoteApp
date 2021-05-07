package com.example.NoteApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    static ArrayList<Folder> folderList = new ArrayList<Folder>();

    private ListView folderListView;

    //Creates new folder
    public void newFolder(View view)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Folder Name");
        View folderDialogInput = LayoutInflater.from(this).inflate(R.layout.folder_text_input,(ViewGroup)view.getRootView(), false);

        final EditText folderNameInput = (EditText) folderDialogInput.findViewById(R.id.folderName);
        final EditText folderPasswordInput = (EditText) folderDialogInput.findViewById(R.id.folderPassword);

        builder.setView(folderDialogInput);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String folderName = folderNameInput.getText().toString();
                if(folderName.length() == 0){
                    folderName = "New Folder";
                }
                String folderPassword = folderPasswordInput.getText().toString();
                int folderID = Folder.addFolder(folderName, folderPassword, getApplicationContext());

                Intent intent = new Intent(getApplicationContext(), Folders.class);
                intent.putExtra("folderId",folderID);
                startActivity(intent);

                folderList.add(new Folder(folderID,getApplicationContext()));
                refreshFolders();

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

//        Intent intent = new Intent(getApplicationContext(), Folders.class);
//        startActivity(intent);
//        folderList.add("o");
//        listView.setAdapter(arrayAdapter);
//        refreshFolders();

    }

    public void refreshFolders(){
        FolderListAdapter adapter = new FolderListAdapter(this, R.layout.folder_list_adapter, folderList);
        folderListView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        folderListView = this.findViewById(R.id.folderListView);

        SQLiteHelper db = new SQLiteHelper(getApplicationContext());

        Map<Integer, List<Integer>> folders = db.fetchAllFoldersAndNotes();
        for(Map.Entry<Integer, List<Integer>> entry: folders.entrySet()){
            int folderID = entry.getKey();
            Folder folder = new Folder(folderID, this.getApplicationContext());
            folderList.add(folder);
        }

        refreshFolders();
        db.close();

    }
}
