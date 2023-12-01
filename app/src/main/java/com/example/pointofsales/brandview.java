package com.example.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class brandview extends AppCompatActivity {

    ListView lst1;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brandview);
        lst1 = findViewById(R.id.lst1);
        SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);

        final Cursor c = db.rawQuery("select * from brand",null);
        int id = c.getColumnIndex("id");
        int brand = c.getColumnIndex("brand");
        int brandedes = c.getColumnIndex("branddes");

        titles.clear();

        arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,titles);
        lst1.setAdapter(arrayAdapter);

        final ArrayList<bran> brann = new ArrayList<bran>();
        if (c.moveToFirst())
        {
            do {
                bran br = new bran();
                br.id = c.getString(id);
                br.brand = c.getString(brand);
                br.des = c.getString(brandedes);
                brann.add(br);

                titles.add(c.getString(id) + "\t" + c.getString(brand) + "\t" + c.getString(brandedes));

            }while(c.moveToNext());
            arrayAdapter.notifyDataSetChanged();
            lst1.invalidateViews();

        }

        lst1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String aaa = titles.get(position).toString();
                bran br = brann.get(position);
                Intent i = new Intent(getApplicationContext(), brandedit.class);
                i.putExtra("id",br.id);
                i.putExtra("brand",br.brand);
                i.putExtra("branddes",br.des);
                startActivity(i);
            }
        });

    }
}