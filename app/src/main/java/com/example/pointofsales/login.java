package com.example.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {
    EditText ed1, ed2;
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ed1 = findViewById(R.id.user);
        ed2 = findViewById(R.id.pass);
        b1 = findViewById(R.id.btn1);
        b2 = findViewById(R.id.btn2);

        ed1.setText("");
        ed2.setText("");

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

        // Call the method to update admin data if needed
        updateAdminData();
    }

    private void updateAdminData() {
        try {
            SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS admin(id INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR,psw VARCHAR)");

            // Check if the admin record exists
            if (!hasAdminData()) {
                // If no data exists, insert the default admin credentials
                String sql = "INSERT INTO admin (user, psw) VALUES (?, ?)";
                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindString(1, "admin");
                statement.bindString(2, "admin");
                statement.execute();

                showToast("Admin details created");
            } else {
                showToast("Admin record already exists");
            }

            db.close();
        } catch (Exception ex) {
            showToast("Error updating admin data");
        }
    }

    private boolean hasAdminData() {
        SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM admin", null);
        boolean hasData = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return hasData;
    }

    public void login() {
        String username = ed1.getText().toString();
        String password = ed2.getText().toString();

        if (username.equals("admin") && password.equals("admin")) {
            Intent i = new Intent(login.this, Main.class);
            startActivityForResult(i, 1);
        } else if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Username or Password Blank", Toast.LENGTH_LONG).show();
        } else if (isValidAdmin(username, password) || isValidUser(username, password)) {
            Intent i = new Intent(login.this, Main.class);
            startActivityForResult(i, 1);
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

    private boolean isValidUser(String username, String password) {
        SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM credentials WHERE user=? AND psw=?", new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            ed1.setText("");
            ed2.setText("");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
