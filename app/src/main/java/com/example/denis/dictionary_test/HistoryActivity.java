package com.example.denis.dictionary_test;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.denis.dictionary_test.data.HistoryContract;
import com.example.denis.dictionary_test.data.HistoryDbHelper;
import com.example.denis.dictionary_test.data.HistoryList;

import static com.example.denis.dictionary_test.data.HistoryContract.TextEntry.COLUMN_FAVORITE;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    HistoryDbHelper mDbHelper;
    HistoryList mHlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new HistoryDbHelper(this);
        setContentView(R.layout.activity_fragment);
        final String METHOD = "onCreate(Bundle {" + savedInstanceState + "});";

        // Initialize the DB
        HistoryDbHelper.instance(this);

        setContentView(R.layout.activity_fragment);

        if (savedInstanceState == null) {
            Log.d(TAG, METHOD + "Creating fresh instance of this Activity...");
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
            if (fragment == null) {
                Log.d(TAG, METHOD + "Creating Fragment for the first time...");
                fragment = createFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHlist.updateList();
    }

    private Fragment createFragment() {
        mHlist = new HistoryList();
        return mHlist;
    }

    public void onDeleteClick(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(HistoryContract.TextEntry.COLUMN_INHISTORY, 0);
        db.delete(HistoryContract.TextEntry.TABLE_NAME,COLUMN_FAVORITE + " = " + 0,null);
        db.update(HistoryContract.TextEntry.TABLE_NAME,value, null , null );
        mHlist.updateList();
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

