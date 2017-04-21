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

/**
 * Created by Denis on 21.04.2017.
 */

public class DbListActivityBase extends AppCompatActivity {
    CheckBoxBinder mBinder;
    ListView listView;
    String whereStr;
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
        whereStr = getIntent().getStringExtra("whereParam");
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    public SimpleCursorAdapter initListView() {
        mBinder = new CheckBoxBinder();
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = HistoryDbHelper.instance(this).getReadableDatabase();


        Cursor cursor = null;
        //


        String orderBy = HistoryContract.TextEntry._ID;
        // Делаем запрос
        cursor = db.query(
                HistoryContract.TextEntry.TABLE_NAME,   // таблица
                null,            // столбцы
                whereStr,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                orderBy + " DESC");              // порядок сортировки


        // Set the Adapter
        listView = (ListView) findViewById(R.id.listView_text);
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_row, cursor, HistoryDbHelper.instance(this).projection, resourceIds, 0);
        cursorAdapter.setViewBinder(mBinder);
        listView.setAdapter(cursorAdapter);
        return cursorAdapter;
    }
    public void updateList(){
        SQLiteDatabase db = HistoryDbHelper.instance(this).getReadableDatabase();


        Cursor cursor = null;
        //


        String orderBy = HistoryContract.TextEntry._ID;
        // Делаем запрос
        cursor = db.query(
                HistoryContract.TextEntry.TABLE_NAME,   // таблица
                null,            // столбцы
                whereStr,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                orderBy + " DESC");              // порядок сортировки
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
