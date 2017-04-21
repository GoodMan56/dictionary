package com.example.denis.dictionary_test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.denis.dictionary_test.data.CheckBoxBinder;
import com.example.denis.dictionary_test.data.HistoryContract;
import com.example.denis.dictionary_test.data.HistoryDbHelper;

import static com.example.denis.dictionary_test.data.HistoryContract.TextEntry.COLUMN_FAVORITE;

public class HistoryActivity extends AppCompatActivity {
    HistoryDbHelper mDbHelper;
    CheckBoxBinder mBinder;
    ListView listView;
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
        mDbHelper = new HistoryDbHelper(this);
        setContentView(R.layout.activity_history);

        // Initialize the DB
        HistoryDbHelper.instance(this);
        // Initialize the ListView
        initListView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    public SimpleCursorAdapter initListView() {
        mDbHelper = new HistoryDbHelper(this);
        mBinder = new CheckBoxBinder();
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        Cursor cursor = null;
        //


        String orderBy = HistoryContract.TextEntry._ID;
        // Делаем запрос
        cursor = db.query(
                HistoryContract.TextEntry.TABLE_NAME,   // таблица
                null,            // столбцы
                HistoryContract.TextEntry.COLUMN_INHISTORY + " = " + 1,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                orderBy + " DESC");              // порядок сортировки


        // Set the Adapter
        listView = (ListView) findViewById(R.id.listView_text);
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_row, cursor, mDbHelper.projection, resourceIds, 0);
        cursorAdapter.setViewBinder(mBinder);
        listView.setAdapter(cursorAdapter);
        return cursorAdapter;
    }

    public void updateList(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        Cursor cursor = null;
        //


        String orderBy = HistoryContract.TextEntry._ID;
        // Делаем запрос
        cursor = db.query(
                HistoryContract.TextEntry.TABLE_NAME,   // таблица
                null,            // столбцы
                HistoryContract.TextEntry.COLUMN_INHISTORY + " = " + 1,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                orderBy + " DESC");              // порядок сортировки
        SimpleCursorAdapter sca = (SimpleCursorAdapter) listView.getAdapter();
        sca.changeCursor(cursor);
        sca.notifyDataSetChanged();
        listView.requestLayout();

    }

    public void onDeleteClick(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(HistoryContract.TextEntry.COLUMN_INHISTORY, 0);
        db.delete(HistoryContract.TextEntry.TABLE_NAME,COLUMN_FAVORITE + " = " + 0,null);
        db.update(HistoryContract.TextEntry.TABLE_NAME,value, null , null );
        updateList();
    }

     public void onFavClick(View view) {
        View listRow = (View) view.getParent().getParent();
        TextView text = (TextView) listRow.findViewById(R.id.text);
        TextView translated = (TextView) listRow.findViewById(R.id.translated);
        TextView direction = (TextView) listRow.findViewById(R.id.direction);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        ContentValues zeroValue = new ContentValues();
        value.put(HistoryContract.TextEntry.COLUMN_FAVORITE, 1);
        zeroValue.put(HistoryContract.TextEntry.COLUMN_FAVORITE, 0);

        boolean checked = ((CheckBox) view).isChecked();
            db.update(HistoryContract.TextEntry.TABLE_NAME, checked ? value : zeroValue , HistoryContract.TextEntry.COLUMN_TEXT + " = " + "\"" + text.getText() + "\"" +
                    " AND " + HistoryContract.TextEntry.COLUMN_TRANSLATED + " = " + "\"" + translated.getText() + "\"" +
                    " AND " + HistoryContract.TextEntry.COLUMN_DIRECTION + " = " + "\"" +direction.getText() + "\"", null );
    }


    public void onFavPush(View view) {
        Intent intent = new Intent(HistoryActivity.this, FavoriteListActivity.class);
        startActivity(intent);
    }

}

