package com.bevan.mongofit.LoginANDregister;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bevan.mongofit.Databases.dbHelper;
import com.bevan.mongofit.Landing;
import com.bevan.mongofit.R;

public class Login extends AppCompatActivity {

    Button login;
    Button register;
    EditText username;
    EditText password;
    com.bevan.mongofit.Databases.dbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnRegister);

        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);

        dbHelper = new dbHelper(this);

    }

    public void onLogin(View view) {
        try {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString();

            if (username.equals("") || password.equals("")) {
                Toast.makeText(this, "Please fill in all text fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean exists = dbHelper.checkUserAndPassword(user, pass);
                if (exists == true) {

                    Intent intent = new Intent(Login.this, Landing.class);
                    intent.putExtra("username",user);
                    startActivity(intent);
                    Login.this.finish();
                    Toast.makeText(this, "Logged in!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            AlertDialog.Builder messageBox = new AlertDialog.Builder(Login.this);
            messageBox.setMessage(e.getMessage());
            messageBox.show();
        }
    }

    public void onRegister(View view) {
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
        Login.this.finish();
    }
}
