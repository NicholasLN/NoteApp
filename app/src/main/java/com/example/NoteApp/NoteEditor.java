package com.example.NoteApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;

public class NoteEditor extends AppCompatActivity {

    int noteID;
    Note note;
    EditText textEdit;



    private void saveText(){
        String htmlText = Html.toHtml((Spanned)textEdit.getText(), HtmlCompat.FROM_HTML_MODE_LEGACY);
        note.updateContent(htmlText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        textEdit = findViewById(R.id.editTextMultiLine);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        noteID = intent.getIntExtra("noteId",0);
        note = new Note(noteID, getApplicationContext());

        // Turn html back into text.
        SpannableString text = new SpannableString(Html.fromHtml(note.getContent()));
        textEdit.setText(text);

        TextView header = findViewById(R.id.noteHeaderEditor);
        header.setText(note.getHeader());


        textEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveText();
            }

            @Override
            public void afterTextChanged(Editable s) {
                saveText();
            }
        });

    }

    public void buttonBold(View view){
        Spannable spannableString = new SpannableStringBuilder(textEdit.getText());
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                textEdit.getSelectionStart(),
                textEdit.getSelectionEnd(),
                0);
        textEdit.setText(spannableString);
    }
    public void buttonItalic(View view){
        Spannable spannableString = new SpannableStringBuilder(textEdit.getText());
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC),
                textEdit.getSelectionStart(),
                textEdit.getSelectionEnd(),
                0);
        textEdit.setText(spannableString);
    }
    public void buttonUnderline(View view){
        Spannable spannableString = new SpannableStringBuilder(textEdit.getText());
        spannableString.setSpan(new UnderlineSpan(),
                textEdit.getSelectionStart(),
                textEdit.getSelectionEnd(),
                0);
        textEdit.setText(spannableString);
    }
    public void buttonClearFormat(View view){
        textEdit.setText(textEdit.getText().toString());
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveText();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}