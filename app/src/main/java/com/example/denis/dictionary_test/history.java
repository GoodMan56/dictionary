package com.example.denis.dictionary_test;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.denis.dictionary_test.R;
import com.example.denis.dictionary_test.data.HistoryContract;
import com.example.denis.dictionary_test.data.HistoryDbHelper;
import com.example.denis.dictionary_test.data.historyList;

import static com.example.denis.dictionary_test.data.HistoryContract.TextEntry._ID;

public class history extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    HistoryDbHelper mDbHelper;
    historyList mHlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new HistoryDbHelper(this);
        //setContentView(R.layout.list_row);
        setContentView(R.layout.activity_fragment);
        //displayDatabaseInfo();
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



    private Fragment createFragment() {
        mHlist = new historyList();
        return mHlist;
    }

    private void displayDatabaseInfo() {

        //ListView listView = (ListView) findViewById(R.id.list1);




       // TextView displayTextView = (TextView) findViewById(R.id.textView4);



        /*try {
            //history mHelp = new history();
            if(displayTextView == null)
                Log.e("TextView", "Ошибка курсора");
            displayTextView.setText("Таблица содержит " + cursor.getCount() + " записей.\n\n");
            displayTextView.append(HistoryContract.TextEntry._ID + " - " +
                    HistoryContract.TextEntry.COLUMN_TEXT + " - " +
                    HistoryContract.TextEntry.COLUMN_TRANSLATED + " - " +
                    HistoryContract.TextEntry.COLUMN_DIRECTION + " - " +
                    HistoryContract.TextEntry.COLUMN_FAVORITE + "\n");

            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(HistoryContract.TextEntry._ID);
            int textColumnIndex = cursor.getColumnIndex(HistoryContract.TextEntry.COLUMN_TEXT);
            int translatedColumnIndex = cursor.getColumnIndex(HistoryContract.TextEntry.COLUMN_TRANSLATED);
            int directionColumnIndex = cursor.getColumnIndex(HistoryContract.TextEntry.COLUMN_DIRECTION);
            int favColumnIndex = cursor.getColumnIndex(HistoryContract.TextEntry.COLUMN_FAVORITE);

            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentText = cursor.getString(textColumnIndex);
                String currentTranslated = cursor.getString(translatedColumnIndex);
                String currentDirection = cursor.getString(directionColumnIndex);
                int currentFav = cursor.getInt(favColumnIndex);
                // Выводим значения каждого столбца
                /*displayTextView.append(("\n" + currentID + " - " +
                        currentText + " - " +
                        currentTranslated + " - " +
                        currentDirection + " - " +
                        currentFav));
                showText.setText(currentText);
                showTranslate.setText(currentTranslated);
                showDirection.setText(currentDirection);

            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
*/
    }


    public void onDeleteClick(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(HistoryContract.TextEntry.TABLE_NAME,null,null);

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
}

