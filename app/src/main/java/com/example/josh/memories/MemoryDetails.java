package com.example.josh.memories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MemoryDetails extends AppCompatActivity {
    private ArrayList<String> items;
    private EditText titleEdit, dateEdit, descriptionEdit;
    private Intent intent;
    private String title, date, description;
    public long id;
    private final static String DEBUG_TAG = "MemoryDetails:";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_details);

        titleEdit = findViewById(R.id.title_details);
        dateEdit = findViewById(R.id.date_details);
        descriptionEdit = findViewById(R.id.description_details);
        Button saveButton = findViewById(R.id.save_button);
        Button deleteButton = findViewById(R.id.delete_button);

        intent = getIntent();
        title = intent.getStringExtra(MemoryList.TITLE);
        date = intent.getStringExtra(MemoryList.DATETIME);
        description = intent.getStringExtra(MemoryList.DESCRIPTION);
        id = intent.getLongExtra("id", -1);
        titleEdit.setText(title);
        dateEdit.setText(date);
        descriptionEdit.setText(description);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(MemoryList.TITLE, String.valueOf(titleEdit.getText()));
                intent.putExtra(MemoryList.DATETIME, String.valueOf(dateEdit.getText()));
                intent.putExtra(MemoryList.DESCRIPTION, String.valueOf(descriptionEdit.getText()));
                Log.d(DEBUG_TAG, "value of title:" + String.valueOf(titleEdit));
                Log.d(DEBUG_TAG, "value of date:" + String.valueOf(dateEdit));
                Log.d(DEBUG_TAG, "value of description:" + String.valueOf(descriptionEdit));
                setResult(MemoryList.RESULT_SAVE, intent);
                supportFinishAfterTransition();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putLong(MemoryList.DELETE, id);
                Intent x = new Intent();
                intent.putExtras(extras);
                setResult(MemoryList.RESULT_DELETE, x);
                supportFinishAfterTransition();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

