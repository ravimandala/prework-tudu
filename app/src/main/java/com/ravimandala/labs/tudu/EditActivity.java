package com.ravimandala.labs.tudu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.logging.Logger;

public class EditActivity extends AppCompatActivity {

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        String tuDuText = getIntent().getStringExtra("text2Edit");
        position = getIntent().getIntExtra("position", 0);
        EditText etEditedTuDu = (EditText) findViewById(R.id.editText);
        etEditedTuDu.setText(tuDuText);
        etEditedTuDu.setSelection(tuDuText.length());
    }

    public void onSaveClicked(View v) {
        EditText etEditedTuDu = (EditText) findViewById(R.id.editText);
        String tuDuText = etEditedTuDu.getText().toString();

        Intent i = new Intent();
        i.putExtra("editedText", tuDuText);
        i.putExtra("position", position);
        setResult(RESULT_OK, i);
        this.finish();
    }
}
