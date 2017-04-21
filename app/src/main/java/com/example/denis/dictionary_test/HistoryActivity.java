package com.example.denis.dictionary_test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.denis.dictionary_test.data.HistoryContract;
import com.example.denis.dictionary_test.data.HistoryDbHelper;

import static com.example.denis.dictionary_test.data.HistoryContract.TextEntry.COLUMN_FAVORITE;

public class HistoryActivity extends DbListActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history);
        super.onCreate(savedInstanceState);
    }

    public void onDeleteClick(View view) {
        SQLiteDatabase db = HistoryDbHelper.instance(this).getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(HistoryContract.TextEntry.COLUMN_INHISTORY, 0);
        db.delete(HistoryContract.TextEntry.TABLE_NAME,COLUMN_FAVORITE + " = " + 0,null);
        db.update(HistoryContract.TextEntry.TABLE_NAME,value, null , null );
        updateList();
    }


    public void onFavPush(View view) {
        Intent intent = new Intent(HistoryActivity.this, FavoriteListActivity.class);
        intent.putExtra("whereParam", HistoryContract.TextEntry.COLUMN_FAVORITE + " = " + 1);
        startActivity(intent);
    }

}

