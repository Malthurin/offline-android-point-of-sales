package com.example.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class productview extends AppCompatActivity {

    ListView lst1;
    ArrayList<String> titles = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productview);

        lst1 = findViewById(R.id.lst1);
        SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);

        // Retrieve data from the "product" table
        final Cursor c = db.rawQuery("SELECT * FROM product", null);
        int id = c.getColumnIndex("id");
        int proname = c.getColumnIndex("proname");
        int category = c.getColumnIndex("category");
        int brand = c.getColumnIndex("brand");
        int qty = c.getColumnIndex("qty");
        int price = c.getColumnIndex("price");

        titles.clear();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        lst1.setAdapter(arrayAdapter);

        final ArrayList<prod> produ = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                prod pr = new prod();
                pr.id = c.getString(id);
                pr.proname = c.getString(proname);
                pr.category = c.getString(category);
                pr.brand = c.getString(brand);
                pr.qty = c.getString(qty);
                pr.price = c.getString(price);

                // Check if quantity is greater than 0 before adding to the list and displaying
                if (!pr.qty.equals("0")) {
                    produ.add(pr);

                    String title = c.getString(id) + "\t" + c.getString(proname) + "\t" + c.getString(category) + "\t" + c.getString(brand) + "\t" + c.getString(qty) + "\t" + c.getString(price);
                    titles.add(title);
                } else {
                    // Quantity is 0, delete the entry from the table
                    db.delete("product", "id=?", new String[]{pr.id});
                }

            } while (c.moveToNext());

            arrayAdapter.notifyDataSetChanged();
            lst1.invalidateViews();
        }
    }
}
