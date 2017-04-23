package com.example.denis.dictionary_test;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.denis.dictionary_test.data.HistoryContract;
import com.example.denis.dictionary_test.data.HistoryDbHelper;


public class FavoriteListActivity extends DbListActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_favorite);
        super.onCreate(savedInstanceState);
    }

    public void onDeleteFav(View view) {
        SQLiteDatabase db = HistoryDbHelper.instance(this).getWritableDatabase();
        db.delete(HistoryContract.TextEntry.TABLE_NAME,HistoryContract.TextEntry.COLUMN_FAVORITE + " = " + 1,null);
        updateList();
    }
}
