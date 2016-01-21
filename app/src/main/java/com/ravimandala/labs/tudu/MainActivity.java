package com.ravimandala.labs.tudu;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ravimandala.labs.tudu.db.model.TuDuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> tuDuList;
    ArrayAdapter<String> tuDusAdapter;
    ListView lvTuDus;
    private final int EDIT_REQUEST_CODE = 20;
    private final String TAG = "Ravi";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            if (position >= 0 && position < tuDuList.size()) {
                String editedText = data.getStringExtra("editedText");
                editTuDu(tuDuList.get(position), editedText);
                tuDuList.set(position, editedText);
                tuDusAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(getApplication());
        setContentView(R.layout.activity_main);
        lvTuDus = (ListView) findViewById(R.id.lvTuDus);
        tuDuList = new ArrayList<>();
        readTuDus();
        tuDusAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, tuDuList);
        lvTuDus.setAdapter(tuDusAdapter);
        EditText etNewTuDu = (EditText) findViewById(R.id.etNewTuDu);
        etNewTuDu.requestFocus();

        final Button btnAddTudu = (Button) findViewById(R.id.btnAddTuDu);
        setListViewListener();
        etNewTuDu.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnAddTudu.performClick();
                    return true;
                }
                return false;
            }
        });

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
            writeTuDu(tuDuText);
        }
    }

    private void setListViewListener() {
        lvTuDus.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        deleteTuDu(tuDuList.get(position));
                        tuDuList.remove(position);
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
                        i.putExtra("text2Edit", tuDuList.get(position));
                        i.putExtra("position", position);
                        startActivityForResult(i, EDIT_REQUEST_CODE);
                    }
                }
        );
    }

    private void readTuDus() {
        List<TuDuItem> tuDuItems = new Select().from(TuDuItem.class).execute();
        Log.d(TAG, "Number of TuDus retrieved from DB: " + tuDuItems.size());
        for (TuDuItem tuDuRow: tuDuItems) {
            tuDuList.add(tuDuRow.getDescription());
        }
    }

    private void writeTuDu(String tuDu) {
        TuDuItem tuDuItem = new TuDuItem(tuDu);
        Log.d(TAG, "Saving TuDu to DB: " + tuDuItem.toString());
        tuDuItem.save();
        List<TuDuItem> tuDuItems = new Select().from(TuDuItem.class).execute();
        Log.d(TAG, "Number of items we have now: " + tuDuItems.size());
    }

    private void editTuDu(String oldTuDu, String newTuDu) {
        TuDuItem tuDuItem = new TuDuItem(oldTuDu);
        tuDuItem.setDescription(newTuDu);
        Log.d(TAG, "Saving edited TuDu to DB: " + tuDuItem.toString());
        tuDuItem.save();
    }

    private void deleteTuDu(String tuDu) {
        TuDuItem tuDuItem = new TuDuItem(tuDu);
        Log.d(TAG, "Deleting TuDu from DB: " + tuDuItem.toString());
        tuDuItem.delete();
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
