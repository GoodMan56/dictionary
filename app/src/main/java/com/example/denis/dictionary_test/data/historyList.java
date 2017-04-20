package com.example.denis.dictionary_test.data;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.denis.dictionary_test.R;

/**
 * Created by Denis on 01.04.2017.
 */



public class HistoryList extends Fragment {
    HistoryDbHelper mDbHelper ;
    CheckBoxBinder mBinder;
    ListView listView;
    SimpleCursorAdapter cursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View ret = configureUI(inflater, container, savedInstanceState);
        // Return the View
        return ret;
    }
    // Ruesource id

    int[] resourceIds = {
            R.id.text,
            R.id.translated,
            R.id.direction,
            R.id.checkBox
    };

    private View configureUI(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        final String METHOD = "configureUI(..., Bundle {" + savedInstanceState + "})";

        // Inflate the layout from the XML definition
        View ret = layoutInflater.inflate(R.layout.activity_history, container, false);

        // Initialize the ListView
        initListView(ret);

        // Return the View
        return ret;
    }



    public SimpleCursorAdapter initListView(View view) {
        mDbHelper = new HistoryDbHelper(getActivity());
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
        listView = (ListView) getView(view, R.id.listView_text);
        cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_row, cursor, mDbHelper.projection, resourceIds, 0);
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
    private View getView(View parentView, int requestedViewId) {
        View ret = parentView.findViewById(requestedViewId);
        if (ret == null) {
            throw new RuntimeException("View with ID: " + requestedViewId + " could not be found!");
        }
        return ret;
    }
}
