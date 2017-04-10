package com.example.denis.dictionary_test.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.denis.dictionary_test.R;

/**
 * Created by Denis on 10.04.2017.
 */

public class favoriteList extends AppCompatActivity {
    HistoryDbHelper mDbHelper;
    CheckBoxBinder mBinder = new CheckBoxBinder();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        int[] resourceIds = {
                R.id.text,
                R.id.translated,
                R.id.direction,
                R.id.checkBox
        };
        mDbHelper = new HistoryDbHelper(this);
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        Cursor cursor = null;
        //


        String orderBy = HistoryContract.TextEntry._ID;
        // Делаем запрос
        cursor = db.query(
                HistoryContract.TextEntry.TABLE_NAME,   // таблица
                null,            // столбцы
                HistoryContract.TextEntry.COLUMN_FAVORITE + " = " + 1,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                orderBy + " DESC");              // порядок сортировки


        // Set the Adapter
        listView = (ListView) findViewById(R.id.listview_fav);
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_row, cursor, mDbHelper.projection, resourceIds, 0);
        if(listView == null)
            Log.e("ListFav", "GOVNO");
        cursorAdapter.setViewBinder(mBinder);
        listView.setAdapter(cursorAdapter);
    }
    public void onDeleteFav(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(HistoryContract.TextEntry.TABLE_NAME,HistoryContract.TextEntry.COLUMN_FAVORITE + " = " + 1,null);
        updateList();
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
                HistoryContract.TextEntry.COLUMN_FAVORITE + " = " + 1,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                orderBy + " DESC");              // порядок сортировки
        SimpleCursorAdapter sca = (SimpleCursorAdapter) listView.getAdapter();
        sca.changeCursor(cursor);
        sca.notifyDataSetChanged();
        listView.requestLayout();

    }
}
