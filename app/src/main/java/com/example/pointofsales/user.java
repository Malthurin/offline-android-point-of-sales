package com.example.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class user extends AppCompatActivity {
    EditText ed1, ed2, ed3, ed4;
    Button b1, b2, b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ed1 = findViewById(R.id.user);
        ed2 = findViewById(R.id.psw);
        ed3 = findViewById(R.id.admin);
        ed4 = findViewById(R.id.psw1);
        b1 = findViewById(R.id.btn1);
        b2 = findViewById(R.id.btn2);
        b3 = findViewById(R.id.btn3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAdminData();
            }
        });
    }

    private void insertUser() {
        String user = ed1.getText().toString();
        String password = ed2.getText().toString();

        if (isValidCredentials(user, password)) {
            try {
                SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS credentials(id INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR,psw VARCHAR)");

                String sql = "INSERT INTO credentials (user, psw) VALUES (?, ?)";
                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindString(1, user);
                statement.bindString(2, password);
                statement.execute();

                showToast("User added");
                clearFields();

            } catch (Exception ex) {
                showToast("User Add Failed");
            }
        } else {
            showToast("Username or password does not meet required");
        }
    }

    private void deleteUser() {
        String userToDelete = ed1.getText().toString();
        String passwordToDelete = ed2.getText().toString();

        if (isValidCredentials(userToDelete, passwordToDelete) && userExists(userToDelete, passwordToDelete)) {
            try {
                SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);
                db.delete("credentials", "user=? AND psw=?", new String[]{userToDelete, passwordToDelete});

                showToast("User deleted: " + userToDelete);
                clearFields();

            } catch (Exception ex) {
                showToast("Error deleting user");
            }
        } else {
            showToast("Invalid credentials or user not found");
        }
    }

    private void updateAdminData() {
        String adminUsername = ed3.getText().toString();
        String adminPassword = ed4.getText().toString();

        if (isValidCredentials(adminUsername, adminPassword)) {
            try {
                SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS admin(id INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR,psw VARCHAR)");

                // Delete the existing admin credentials
                db.delete("admin", null, null);

                // Insert the new admin credentials
                String sql = "INSERT INTO admin (user, psw) VALUES (?, ?)";
                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindString(1, adminUsername);
                statement.bindString(2, adminPassword);
                statement.execute();

                showToast("Admin successfully changed");
                clearFields();

            } catch (Exception ex) {
                showToast("Error updating admin data");
            }
        } else {
            showToast("Admin username or password does not meet required");
        }
    }

    private boolean isValidCredentials(String username, String password) {
        return username.length() >= 4 && password.length() >= 4;
    }

    private boolean userExists(String username, String password) {
        SQLiteDatabase db = openOrCreateDatabase("superpos", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM credentials WHERE user=? AND psw=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        ed1.setText("");
        ed2.setText("");
        ed3.setText("");
        ed4.setText("");
        ed1.requestFocus();
    }
}
