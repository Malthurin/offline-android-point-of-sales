package com.example.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CategoryEdit extends AppCompatActivity {

    EditText ed1,ed2,ed3;
    Button b1,b2,b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoryedit);

        ed1 = findViewById(R.id.catid);
        ed2 = findViewById(R.id.category);
        ed3 = findViewById(R.id.categorydes);
        b1 = findViewById(R.id.btn1);
        b2 = findViewById(R.id.btn2);
        b3 = findViewById(R.id.btn3);

        Intent i = getIntent();

        String id = i.getStringExtra("id").toString();
        String category = i.getStringExtra("category").toString();
        String des = i.getStringExtra("catdes").toString();


        ed1.setText(id);
        ed2.setText(category);
        ed3.setText(des);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Main.class);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit();
            }
        });
    }

    public void Edit() {
        try {
            String catid = ed1.getText().toString();
            String categoryname = ed2.getText().toString();
            String catdescription = ed3.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);

            String sql = "UPDATE category SET category = ?, catdes = ? WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, categoryname);
            statement.bindString(2, catdescription);
            statement.bindString(3, catid);
            statement.execute();
            Toast.makeText(this, "Category Updated", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), Main.class);
            startActivity(i);
        } catch (Exception ex) {
            Toast.makeText(this, "Category Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
    public void delete()
    {
        try {
            String catid = ed1.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);

            String sql = "delete from category WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, catid);
            statement.execute();
            Toast.makeText(this, "Category Deleted", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), Main.class);
            startActivity(i);
        } catch (Exception ex) {
            Toast.makeText(this, "Category Update Failed", Toast.LENGTH_SHORT).show();
        }

    }

}