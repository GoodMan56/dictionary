package com.example.denis.dictionary_test.data;

import android.provider.BaseColumns;

import static com.example.denis.dictionary_test.data.HistoryContract.TextEntry.TABLE_NAME;

/**
 * Created by Denis on 28.03.2017.
 */

public final class HistoryContract {
    private HistoryContract(){
    };
    public static final class TextEntry implements BaseColumns{
        public final static String _ID = BaseColumns._ID;
        public final static String TABLE_NAME = "texts";
        public final static String COLUMN_TEXT = "text";
        public final static String COLUMN_TRANSLATED = "translated";
        public final static String COLUMN_DIRECTION = "direction";
        public final static String COLUMN_FAVORITE = "favorite";
    }
}
