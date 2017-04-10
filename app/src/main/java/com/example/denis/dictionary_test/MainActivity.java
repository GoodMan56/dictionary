package com.example.denis.dictionary_test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.denis.dictionary_test.data.HistoryContract;
import com.example.denis.dictionary_test.data.HistoryDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import static com.example.denis.dictionary_test.data.HistoryContract.TextEntry._ID;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView mTextView;
    HashMap<String, String> map;
    Spinner spinner;
    Spinner spinner2;
    String lang = "ru-en";
    CheckBox mCheckBox;


    private HistoryDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button fab = (Button) findViewById(R.id.button3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, history.class);
                startActivity(intent);
            }
        });

        mDbHelper = new HistoryDbHelper(this);

        mTextView = (TextView)findViewById(R.id.textView2);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        final String[] spinnerArray = {"Русский"};
        String[] spinnerArray2 = {"Английский"};
        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(spinnerArray));
        ArrayList<String> lst2 = new ArrayList<String>(Arrays.asList(spinnerArray2));
// Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst);
        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst2);
// Specify the layout to use when the list of choices appears
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(spinnerArrayAdapter);
        spinner2.setAdapter(spinnerArrayAdapter2);
        spinner.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=trnsl.1.1.20170320T132501Z.4f44e2bf3d674771.67b1878acb487684e676c7c4f2fa3badebb57954&ui=ru";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(response);
                            jObject = jObject.getJSONObject("langs");
                            map = new HashMap<String, String>();
                            Iterator<?> keys = jObject.keys();
                            ArrayList<String> sorted = new ArrayList<String>();
                            while( keys.hasNext() ){
                                String key = (String)keys.next();
                                String value = jObject.getString(key);
                                map.put(value, key);
                                sorted.add(value);
                            }
                            Collections.sort(sorted);
                            spinnerArrayAdapter.clear();
                            spinnerArrayAdapter2.clear();
                            spinnerArrayAdapter.addAll(sorted);
                            spinnerArrayAdapter2.addAll(sorted);
                            spinner.setSelection(spinnerArrayAdapter.getPosition("Русский"));
                            spinner2.setSelection(spinnerArrayAdapter2.getPosition("Английский"));
                        } catch (JSONException e) {
                            mTextView.setText(e.getMessage());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        //setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });*/



    }


    public void onItemSelected(AdapterView<?> parent,
                               View itemSelected, int selectedItemPosition, long selectedId) {
        if(map != null)
            lang = map.get(spinner.getSelectedItem().toString()) + "-" + map.get(spinner2.getSelectedItem().toString());


    }
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void OnClick(View view) {
        EditText mEdit = (EditText)findViewById(R.id.editText);
        final String text = mEdit.getText().toString();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170320T132501Z.4f44e2bf3d674771.67b1878acb487684e676c7c4f2fa3badebb57954&text=";
        try {

            url += URLEncoder.encode(text, "UTF-8");
            url += "&lang=" + lang;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(response);
                            String translatedText = jObject.getJSONArray("text").getString(0);
                            mTextView.setText(translatedText);
                            mCheckBox = (CheckBox)findViewById(R.id.checkBox2);
                            //onCheckboxClicked(mCheckBox, text, translatedText,lang);

                            insertText(text, translatedText, lang, 0);
                            mCheckBox.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCheckBox.setVisibility(View.INVISIBLE);
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }


    public void Swap(View view) {
        Integer tem = spinner2.getSelectedItemPosition();
        spinner2.setSelection(spinner.getSelectedItemPosition());
        spinner.setSelection(tem);
    }

    public void insertText(String text, String translated, String dir, int fav) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;
        //
        String orderBy = HistoryContract.TextEntry._ID;
        // Делаем запрос
        cursor = db.query(
                HistoryContract.TextEntry.TABLE_NAME,   // таблица
                null,            // столбцы
                HistoryContract.TextEntry.COLUMN_FAVORITE + " = " + 1 +
                        " AND " + HistoryContract.TextEntry.COLUMN_TEXT       + " = " + "\"" + text        + "\"" +
                        " AND " + HistoryContract.TextEntry.COLUMN_TRANSLATED + " = " + "\"" + translated  + "\"" +
                        " AND " + HistoryContract.TextEntry.COLUMN_DIRECTION  + " = " + "\"" + dir         + "\"",                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                orderBy + " DESC");    // порядок сортировки
        // Gets the database in write mode
        db = mDbHelper.getWritableDatabase();
        mCheckBox.setChecked(cursor.getCount() != 0);
        // Создаем объект ContentValues
        ContentValues values = new ContentValues();
        values.put(HistoryContract.TextEntry.COLUMN_TEXT, text);
        values.put(HistoryContract.TextEntry.COLUMN_TRANSLATED, translated);
        values.put(HistoryContract.TextEntry.COLUMN_DIRECTION, dir);
        values.put(HistoryContract.TextEntry.COLUMN_FAVORITE, cursor.getCount() != 0);
        values.put(HistoryContract.TextEntry.COLUMN_INHISTORY, 1);

        long newRowId = db.insert(HistoryContract.TextEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            // Если ID  -1, значит произошла ошибка
            Toast.makeText(this, "Ошибка записи", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Под номером: " + newRowId, Toast.LENGTH_SHORT).show();
        }


    }





    /*public void onCheckboxClicked(View view, String text, String translatedText, String lang) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked)
                    insertText(text, translatedText, lang, 1);
            else
                    insertText(text, translatedText, lang, 0);
                break;
            case R.id.checkBox2:
                if (checked)
                    insertText(text, translatedText, lang, 1);
            else
                insertText(text, translatedText, lang, 0);
                break;
        }
    }*/

    public void onCheckBoxClick(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        ContentValues zeroValue = new ContentValues();
        value.put(HistoryContract.TextEntry.COLUMN_FAVORITE, 1);
        zeroValue.put(HistoryContract.TextEntry.COLUMN_FAVORITE, 0);

        boolean checked = ((CheckBox) view).isChecked();
        if(checked)
            db.update(HistoryContract.TextEntry.TABLE_NAME, value , _ID + " = (SELECT max("+_ID+") FROM " + HistoryContract.TextEntry.TABLE_NAME + ")", null );
        else
            db.update(HistoryContract.TextEntry.TABLE_NAME, zeroValue , _ID + " = (SELECT max("+_ID+") FROM " + HistoryContract.TextEntry.TABLE_NAME + ")", null );
    }
}


