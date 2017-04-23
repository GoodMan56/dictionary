package com.example.denis.dictionary_test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.denis.dictionary_test.data.CheckBoxBinder;
import com.example.denis.dictionary_test.data.HistoryContract;
import com.example.denis.dictionary_test.data.HistoryDbHelper;

public class DbListActivityBase extends AppCompatActivity {
    CheckBoxBinder mBinder;
    ListView listView;
    String whereStr;
    String param = "whereParam";
    SimpleCursorAdapter cursorAdapter;

    // Ruesource id
    int[] resourceIds = {
            R.id.text,
            R.id.translated,
            R.id.direction,
            R.id.checkBox
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        whereStr = getIntent().getStringExtra(param);
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    public SimpleCursorAdapter initListView() {
        mBinder = new CheckBoxBinder();

        //Create and open database for reading
        SQLiteDatabase db = HistoryDbHelper.instance(this).getReadableDatabase();
        String orderBy = HistoryContract.TextEntry._ID;

        //Making request
        Cursor cursor = db.query(
                HistoryContract.TextEntry.TABLE_NAME,
                null,
                whereStr,
                null,
                null,
                null,
                orderBy + " DESC");


        // Set the Adapter
        listView = (ListView) findViewById(R.id.listView_text);
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_row, cursor, HistoryDbHelper.instance(this).projection, resourceIds, 0);
        cursorAdapter.setViewBinder(mBinder);
        listView.setAdapter(cursorAdapter);
        return cursorAdapter;
    }

    //Update words listview
    public void updateList(){
        SQLiteDatabase db = HistoryDbHelper.instance(this).getReadableDatabase();
        String orderBy = HistoryContract.TextEntry._ID;
        Cursor cursor = db.query(
                HistoryContract.TextEntry.TABLE_NAME,
                null,
                whereStr,
                null,
                null,
                null,
                orderBy + " DESC");
        SimpleCursorAdapter sca = (SimpleCursorAdapter) listView.getAdapter();
        sca.changeCursor(cursor);
        sca.notifyDataSetChanged();
        listView.requestLayout();
    }

    public void onFavClick(View view) {
        View listRow = (View) view.getParent().getParent();
        TextView text = (TextView) listRow.findViewById(R.id.text);
        TextView translated = (TextView) listRow.findViewById(R.id.translated);
        TextView direction = (TextView) listRow.findViewById(R.id.direction);
        SQLiteDatabase db = HistoryDbHelper.instance(this).getWritableDatabase();
        ContentValues value = new ContentValues();
        ContentValues zeroValue = new ContentValues();
        value.put(HistoryContract.TextEntry.COLUMN_FAVORITE, 1);
        zeroValue.put(HistoryContract.TextEntry.COLUMN_FAVORITE, 0);

        boolean checked = ((CheckBox) view).isChecked();
        db.update(HistoryContract.TextEntry.TABLE_NAME, checked ? value : zeroValue , HistoryContract.TextEntry.COLUMN_TEXT + " = " + "\"" + text.getText() + "\"" +
                " AND " + HistoryContract.TextEntry.COLUMN_TRANSLATED + " = " + "\"" + translated.getText() + "\"" +
                " AND " + HistoryContract.TextEntry.COLUMN_DIRECTION + " = " + "\"" +direction.getText() + "\"", null );
        updateList();
    }
}
