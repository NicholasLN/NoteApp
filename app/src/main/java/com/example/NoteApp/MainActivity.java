package com.example.NoteApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.NoteApp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(MainActivity.this);
    }
}