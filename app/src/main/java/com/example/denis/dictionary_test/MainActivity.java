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
    Spinner sourceLang;
    Spinner translateLang;
    String lang = "ru-en";
    CheckBox mCheckBox;

    String translateUrl ="https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170320T132501Z.4f44e2bf3d674771.67b1878acb487684e676c7c4f2fa3badebb57954&text=";
    String url ="https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=trnsl.1.1.20170320T132501Z.4f44e2bf3d674771.67b1878acb487684e676c7c4f2fa3badebb57954&ui=ru";
    String defaultSourceLang = "Русский";
    String defaultTranslateLang = "Английский";
    String param = "whereParam";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mCheckBox = (CheckBox)findViewById(R.id.checkBox2);
        mTextView = (TextView)findViewById(R.id.textView2);
        Button fab = (Button) findViewById(R.id.button3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putExtra(param, HistoryContract.TextEntry.COLUMN_INHISTORY + " = " + 1);
                startActivity(intent);
            }
        });

        mTextView = (TextView)findViewById(R.id.textView2);
        sourceLang = (Spinner) findViewById(R.id.spinner);
        translateLang = (Spinner) findViewById(R.id.spinner2);
        String[] sourceLangArray = {defaultSourceLang};
        String[] translateLangArray = {defaultTranslateLang};
        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(sourceLangArray));
        ArrayList<String> lst2 = new ArrayList<String>(Arrays.asList(translateLangArray));

        // Create an ArrayAdapter using the string array and a default sourceLang layout
        final ArrayAdapter<String> spinnerSourceArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst);
        final ArrayAdapter<String> spinnerTranslateArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst2);

        // Specify the layout to use when the list of choices appears
        spinnerSourceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTranslateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the sourceLang
        sourceLang.setAdapter(spinnerSourceArrayAdapter);
        translateLang.setAdapter(spinnerTranslateArrayAdapter);
        sourceLang.setOnItemSelectedListener(this);
        translateLang.setOnItemSelectedListener(this);
        RequestQueue queue = Volley.newRequestQueue(this);


        //Request a languages list from the provided URL.
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
                            spinnerSourceArrayAdapter.clear();
                            spinnerTranslateArrayAdapter.clear();
                            spinnerSourceArrayAdapter.addAll(sorted);
                            spinnerTranslateArrayAdapter.addAll(sorted);
                            sourceLang.setSelection(spinnerSourceArrayAdapter.getPosition(defaultSourceLang));
                            translateLang.setSelection(spinnerTranslateArrayAdapter.getPosition(defaultTranslateLang));
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
    }


    public void onItemSelected(AdapterView<?> parent,
                               View itemSelected, int selectedItemPosition, long selectedId) {
        if(map != null)
            lang = map.get(sourceLang.getSelectedItem().toString()) + "-" + map.get(translateLang.getSelectedItem().toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void OnClick(View view) {
        EditText mEdit = (EditText)findViewById(R.id.editText);
        final String text = mEdit.getText().toString();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            translateUrl += URLEncoder.encode(text, "UTF-8");
            translateUrl += "&lang=" + lang;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, translateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(response);
                            String translatedText = jObject.getJSONArray("text").getString(0);
                            mTextView.setText(translatedText);
                            insertText(text, translatedText, lang);
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

    //Swap spinners languages
    public void swap(View view) {
        Integer tem = translateLang.getSelectedItemPosition();
        translateLang.setSelection(sourceLang.getSelectedItemPosition());
        sourceLang.setSelection(tem);
    }

    public void insertText(String text, String translated, String dir) {

        //Create and open database for reading
        SQLiteDatabase db = HistoryDbHelper.instance(this).getReadableDatabase();

        //Make request
        Cursor cursor = db.query(
                HistoryContract.TextEntry.TABLE_NAME,
                null,
                HistoryContract.TextEntry.COLUMN_FAVORITE + " = " + 1 +
                        " AND " + HistoryContract.TextEntry.COLUMN_TEXT       + " = " + "\"" + text        + "\"" +
                        " AND " + HistoryContract.TextEntry.COLUMN_TRANSLATED + " = " + "\"" + translated  + "\"" +
                        " AND " + HistoryContract.TextEntry.COLUMN_DIRECTION  + " = " + "\"" + dir         + "\"",
                null,
                null,
                null,
                null);

        //Insert new word to the database
        db = HistoryDbHelper.instance(this).getWritableDatabase();
        mCheckBox.setChecked(cursor.getCount() != 0);
        ContentValues values = new ContentValues();
        values.put(HistoryContract.TextEntry.COLUMN_TEXT, text);
        values.put(HistoryContract.TextEntry.COLUMN_TRANSLATED, translated);
        values.put(HistoryContract.TextEntry.COLUMN_DIRECTION, dir);
        values.put(HistoryContract.TextEntry.COLUMN_FAVORITE, cursor.getCount() != 0);
        values.put(HistoryContract.TextEntry.COLUMN_INHISTORY, 1);

        long newRowId = db.insert(HistoryContract.TextEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCheckBoxClick(View view) {
        SQLiteDatabase db = HistoryDbHelper.instance(this).getWritableDatabase();
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


