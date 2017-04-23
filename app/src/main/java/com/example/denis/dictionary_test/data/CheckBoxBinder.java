package com.example.denis.dictionary_test.data;

import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;

import com.example.denis.dictionary_test.R;

//Translating the value from the database to the checkbox state
public class CheckBoxBinder implements SimpleCursorAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        if(columnIndex == 4){
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            checkBox.setChecked(cursor.getInt(columnIndex)!=0);
            return true;
        }
        return false;
    }
}
