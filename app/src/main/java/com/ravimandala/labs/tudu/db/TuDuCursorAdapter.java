package com.ravimandala.labs.tudu.db;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ravimandala.labs.tudu.MainActivity;
import com.ravimandala.labs.tudu.R;

/**
 * Created by ravi.mandala on 1/20/16.
 */
public class TuDuCursorAdapter extends CursorAdapter {

    Context myContext;

    public TuDuCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private class MyOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Custom Item Clicked!!!!", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        //Inflate the view and get references to your TextViews/EditText.
        TextView tvDescription = (TextView) view.findViewById( R.id.tvDescription);
        TextView tvPriority = (TextView) view.findViewById( R.id.tvPriority);

        //Set the onClickListener for each of the TextViews/EditTexts.
        tvDescription.setOnClickListener(new MyOnClickListener());
        tvPriority.setOnClickListener(new MyOnClickListener());

        return view;
    }

    public TuDuCursorAdapter(Context context, Cursor tuDuCursor) {
        super(context, tuDuCursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_tu_du, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        // Extract properties from cursor
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority"));
        // Populate fields with extracted properties
        tvDescription.setText(description);
        tvPriority.setText(String.valueOf(priority));
    }
}
