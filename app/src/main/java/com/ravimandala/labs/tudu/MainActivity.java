package com.ravimandala.labs.tudu;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ravimandala.labs.tudu.db.TuDuCursorAdapter;
import com.ravimandala.labs.tudu.db.model.TuDuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TuDuCursorAdapter tuDusAdapter;
    ListView lvTuDus;
    private final int EDIT_REQUEST_CODE = 20;
    private final String TAG = "Ravi";
    private final String defaultPriority = "HIGH";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            if (position >= 0 && position < lvTuDus.getCount()) {
                String originalDescription = data.getStringExtra("originalDescription");
                String editedDescription = data.getStringExtra("editedDescription");
                editTuDu(originalDescription, editedDescription);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(getApplication());
        setContentView(R.layout.activity_main);
        lvTuDus = (ListView) findViewById(R.id.lvTuDus);
        Cursor tuDuCursor = TuDuItem.fetchResultCursor();
        tuDusAdapter = new TuDuCursorAdapter(getApplicationContext(), tuDuCursor);
        lvTuDus.setAdapter(tuDusAdapter);

        setListViewListener();

        EditText etNewTuDu = (EditText) findViewById(R.id.etNewTuDu);
        etNewTuDu.requestFocus();

        final Button btnAddTudu = (Button) findViewById(R.id.btnAddTuDu);
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
            etNewTuDu.setText("");
            writeTuDu(tuDuText);
        }
    }

    private void setListViewListener() {
        lvTuDus.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                        deleteTuDu(tuDuList.get(position));
//                        tuDuList.remove(position);
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
                        TextView tvDescription = (TextView) view.findViewById( R.id.tvDescription);
                        TextView tvPriority = (TextView) view.findViewById( R.id.tvPriority);
                        i.putExtra("originalDescription", tvDescription.getText());
                        i.putExtra("priority", defaultPriority);
                        i.putExtra("position", position);
                        startActivityForResult(i, EDIT_REQUEST_CODE);
                    }
                }
        );
    }

    private void writeTuDu(String tuDu) {
        TuDuItem tuDuItem = new TuDuItem(tuDu, defaultPriority);
        Log.d(TAG, "Saving TuDu to DB: " + tuDuItem.toString());
        tuDuItem.save();

        refreshCursor();
    }

    private void editTuDu(String oldTuDu, String newTuDu) {
        TuDuItem tuDuItem = new TuDuItem(oldTuDu, defaultPriority);
        tuDuItem.setDescription(newTuDu);
        Log.d(TAG, "Saving edited TuDu to DB: " + tuDuItem.toString());
        tuDuItem.save();

        refreshCursor();
    }

    private void deleteTuDu(String tuDu) {
        TuDuItem tuDuItem = new TuDuItem(tuDu, defaultPriority);
        Log.d(TAG, "Deleting TuDu from DB: " + tuDuItem.toString());
        tuDuItem.delete();

        refreshCursor();
    }

    private void refreshCursor() {
        Cursor tuDuCursor = TuDuItem.fetchResultCursor();
        tuDusAdapter.changeCursor(tuDuCursor);
        tuDusAdapter.notifyDataSetChanged();
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
