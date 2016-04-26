package com.ravimandala.labs.tudu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

public class EditActivity extends AppCompatActivity {

    private String originalDescription;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        originalDescription = getIntent().getStringExtra("originalDescription");
        position = getIntent().getIntExtra("position", 0);
        EditText etEditedTuDu = (EditText) findViewById(R.id.editText);
        etEditedTuDu.setText(originalDescription);
        etEditedTuDu.setSelection(originalDescription.length());

        final Button btnSave = (Button) findViewById(R.id.btnSave);
        etEditedTuDu.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSave.performClick();
                    return true;
                }
                return false;
            }
        });

    }

    public void onSaveClicked(View v) {
        EditText etEditedTuDu = (EditText) findViewById(R.id.editText);
        String editedDescription = etEditedTuDu.getText().toString();

        Intent i = new Intent();
        i.putExtra("originalDescription", originalDescription);
        i.putExtra("editedDescription", editedDescription);
        i.putExtra("position", position);
        setResult(RESULT_OK, i);
        this.finish();
    }
}
