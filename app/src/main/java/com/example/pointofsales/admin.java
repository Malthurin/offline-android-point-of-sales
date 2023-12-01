package com.example.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class admin extends AppCompatActivity {
    EditText ed1, ed2;
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ed1 = findViewById(R.id.admin);
        ed2 = findViewById(R.id.psw1);
        b1 = findViewById(R.id.btn1);
        b2 = findViewById(R.id.btn2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        String username = ed1.getText().toString();
        String password = ed2.getText().toString();

        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Username or Password Blank", Toast.LENGTH_LONG).show();
        } else if (isValidAdmin(username, password)) {
            Intent i = new Intent(admin.this, user.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Username or Password do not match", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValidAdmin(String username, String password) {
        SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS admin(id INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR,psw VARCHAR)");

        Cursor cursor = db.rawQuery("SELECT * FROM admin WHERE user=? AND psw=?", new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }
}
