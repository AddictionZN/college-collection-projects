package com.bevan.mongofit.MenuActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bevan.mongofit.Databases.dbHelper;
import com.bevan.mongofit.Landing;
import com.bevan.mongofit.R;

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity {

    // getting the values from the content page
    Button editSteps;
    Button editCalories;
    TextView calories;
    TextView steps;
    String username;
    com.bevan.mongofit.Databases.dbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // getting the username from the log in
        Intent i = getIntent();
        username = i.getStringExtra("username").trim();

        // getting the steps
        editSteps = findViewById(R.id.btnEditSteps);
        steps = findViewById(R.id.tvSteps);

        // getting the calories
        editCalories = findViewById(R.id.btnEditCalories);
        calories = findViewById(R.id.tvCalories);

        // Creating a dbHelper instance
        dbHelper = new dbHelper(this);

        // Checking if the user exists in the tblSteps table
        boolean check = dbHelper.checkUserInSteps(username);

        // if is true then get insert it into the database with the username and password else select it from the database
        if (check == true) {
            String[] getMaxSteps = dbHelper.getSteps(username);
            steps.setText(getMaxSteps[0]);

        } else {
            dbHelper.insertSteps(username, "10000");
            steps.setText("10000");
        }

        // Checking if the user exists in the tblCalories
        boolean checkCalories = dbHelper.checkUserInCalories(username);
        if (checkCalories == true) {
            String[] getMaxCalories = dbHelper.getCalories(username);
            calories.setText(getMaxCalories[0]);
        } else {
            dbHelper.insertCalories(username, "3000");
            calories.setText("3000");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Settings.this, Landing.class);
        intent.putExtra("username", username);
        startActivity(intent);
        Settings.this.finish();
    }

    public void onEditSteps(View view) {

        // creating a dialog box to show the changes for the users
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                steps.setText(value);
                dbHelper.updateSteps(value, username);

                Intent intent = new Intent(Settings.this, Landing.class);
                intent.putExtra("username", username);
                startActivity(intent);
                // closing down the settings class
                Settings.this.finish();
                Toast.makeText(Settings.this, "Settings have been changed", Toast.LENGTH_SHORT).show();

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.setMessage("Please enter a number");
        alert.show();
    }

    public void onEditCalories(View view) {

        // creating a dialog box to show the changes for the users
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                calories.setText(value);
                dbHelper.updateCalories(value,username);

                Intent intent = new Intent(Settings.this, Landing.class);
                intent.putExtra("username", username);
                startActivity(intent);
                // closing down the settings class
                Settings.this.finish();
                Toast.makeText(Settings.this, "Settings have been changed", Toast.LENGTH_SHORT).show();

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.setMessage("Please enter a number");
        alert.show();
    }
}
