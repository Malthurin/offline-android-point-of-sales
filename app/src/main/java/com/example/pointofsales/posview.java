package com.example.pointofsales;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class posview extends AppCompatActivity {
    ListView lst1;
    ArrayList<String> titles = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posview);

        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);

            lst1 = findViewById(R.id.lst1);
            c = db.rawQuery("SELECT * FROM posss", null);
            int proid = c.getColumnIndex("proid");
            int proname = c.getColumnIndex("proname");
            int qty = c.getColumnIndex("qty");
            int price = c.getColumnIndex("price");
            int total = c.getColumnIndex("total");
            titles.clear();

            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
            lst1.setAdapter(arrayAdapter);

            if (c.moveToFirst()) {
                do {
                    String data = String.valueOf(c.getString(proid)) + "\t" + String.valueOf(c.getString(proname)) + "\t" + String.valueOf(c.getString(qty)) + "\t" + String.valueOf(c.getString(price)) + "\t" + String.valueOf(c.getString(total));
                    titles.add(data);
                } while (c.moveToNext());

                arrayAdapter.notifyDataSetChanged();
                lst1.invalidateViews();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }
}
