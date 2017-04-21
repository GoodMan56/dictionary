package com.example.denis.dictionary_test.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import static com.example.denis.dictionary_test.data.HistoryContract.TextEntry;
import static com.example.denis.dictionary_test.data.HistoryContract.TextEntry.TABLE_NAME;


/**
 * Created by Denis on 28.03.2017.
 */


    public class HistoryDbHelper extends  SQLiteOpenHelper{


    //Имя файла базы данных
    private static final String DATABASE_NAME = "dictionary.db";

    private static final int DATABASE_VERSION = 1;

    private static HistoryDbHelper mInstance;

    public HistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String CREATE_SQL = "CREATE TABLE " + TABLE_NAME +
                "(" +
                TextEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                TextEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                TextEntry.COLUMN_TRANSLATED + " TEXT NOT NULL, " +
                TextEntry.COLUMN_DIRECTION+ " TEXT NOT NULL, " +
                TextEntry.COLUMN_FAVORITE + " INTEGER NOT NULL DEFAULT 0, " +
                TextEntry.COLUMN_INHISTORY + " INTEGER NOT NULL DEFAULT 0, UNIQUE (" +
                TextEntry.COLUMN_TEXT + ", " +
                TextEntry.COLUMN_TRANSLATED + ", " +
                TextEntry.COLUMN_DIRECTION + ") ON CONFLICT REPLACE);";

        // Запускаем создание таблицы
        db.execSQL(CREATE_SQL);
    }

    // Зададим условие для выборки - список столбцов
    public String[] projection = {
            HistoryContract.TextEntry.COLUMN_TEXT,
            HistoryContract.TextEntry.COLUMN_TRANSLATED,
            HistoryContract.TextEntry.COLUMN_DIRECTION,
            HistoryContract.TextEntry.COLUMN_FAVORITE,
            HistoryContract.TextEntry.COLUMN_INHISTORY};


    public static HistoryDbHelper instance(Context context) {
        if (mInstance == null) {
            mInstance = new HistoryDbHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TextEntry.TABLE_NAME;


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Запись в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + "на версию " + newVersion);
        //Удаление старой таблицы, создание новой
        db.execSQL(SQL_DELETE_ENTRIES);
        //Создаем таблицу
        onCreate(db);
    }

}
