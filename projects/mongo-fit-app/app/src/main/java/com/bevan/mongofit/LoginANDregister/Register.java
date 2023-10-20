package com.bevan.mongofit.LoginANDregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bevan.mongofit.Databases.dbHelper;
import com.bevan.mongofit.R;

public class Register extends AppCompatActivity {

    //Calling the db helper class
    com.bevan.mongofit.Databases.dbHelper dbHelper;

    //Declaring the variables
    EditText user;
    EditText age;
    Spinner gender;
    EditText password;
    EditText passwordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = findViewById(R.id.etUsername);
        age = findViewById(R.id.etAge);
        gender = findViewById(R.id.spinGender);
        password = findViewById(R.id.etPassword);
        passwordAgain = findViewById(R.id.etPassword2);
        dbHelper = new dbHelper(this);

        //This is to get the spinner to create the data
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
    }

    public void onClickRegister(View view) {
        String username = user.getText().toString().trim();
        String a = age.getText().toString().trim();
        String gen = gender.getSelectedItem().toString();
        String pass = password.getText().toString().trim();
        String pass2 = passwordAgain.getText().toString().trim();

        boolean check = dbHelper.checkUser(username);
        if (username.equals("") || a.equals("") || pass.equals("") || pass2.equals("")) {
            Toast.makeText(this, "Please fill in the all the details", Toast.LENGTH_SHORT).show();
        }else{
            if (check == true){
                Toast.makeText(this, "User already exists in the database", Toast.LENGTH_SHORT).show();
            }
            else {
                if (!pass.equals(pass2)) {
                    Toast.makeText(this, "Please enter password that is a match", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.RegisteringUser(username, a, gen, pass);
                    Intent i = new Intent(Register.this, Register2.class);
                    i.putExtra("username", username);
                    startActivity(i);
                    Register.this.finish();
                }
            }
        }
       
    }
}
