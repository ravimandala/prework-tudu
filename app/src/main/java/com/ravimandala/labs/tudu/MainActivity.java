package com.ravimandala.labs.tudu;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> tuDus;
    ArrayAdapter<String> tuDusAdapter;
    ListView lvTuDus;
    private final int EDIT_REQUEST_CODE = 20;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            if (position >= 0 && position < tuDus.size()) {
                tuDus.set(position, data.getStringExtra("editedText"));
                writeTuDus();
                tuDusAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvTuDus = (ListView) findViewById(R.id.lvTuDus);
        tuDus = new ArrayList<>();
        readTuDus();
        tuDusAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, tuDus);
        lvTuDus.setAdapter(tuDusAdapter);
        setListViewListener();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onAddTuDu(View v) {
        EditText etNewTuDu = (EditText) findViewById(R.id.etNewTuDu);
        String tuDuText = etNewTuDu.getText().toString();

        if(tuDuText.trim().length() != 0) {
            tuDusAdapter.add(tuDuText);
            etNewTuDu.setText("");
            writeTuDus();
        }
    }

    private void setListViewListener() {
        lvTuDus.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        tuDus.remove(position);
                        writeTuDus();
                        tuDusAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
        lvTuDus.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(MainActivity.this, EditActivity.class);
                        i.putExtra("text2Edit", tuDus.get(position));
                        i.putExtra("position", position);
                        startActivityForResult(i, EDIT_REQUEST_CODE);
                    }
                }
        );
    }

    private void readTuDus() {
        File filesDir = getFilesDir();
        File tuDuFile = new File(filesDir, "tudu.txt");
        try {
            tuDus = new ArrayList<String>(FileUtils.readLines(tuDuFile));
        } catch (IOException ioe) {
            tuDus = new ArrayList<String>();
        }
    }

    private void writeTuDus() {
        File filesDir = getFilesDir();
        File tuDuFile = new File(filesDir, "tudu.txt");
        try {
            FileUtils.writeLines(tuDuFile, tuDus);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ravimandala.labs.tudu/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ravimandala.labs.tudu/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
