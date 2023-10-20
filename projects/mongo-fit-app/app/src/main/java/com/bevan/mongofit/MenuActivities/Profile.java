package com.bevan.mongofit.MenuActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bevan.mongofit.Databases.dbHelper;
import com.bevan.mongofit.Landing;
import com.bevan.mongofit.LoginANDregister.Login;
import com.bevan.mongofit.R;

public class Profile extends AppCompatActivity {

    // Calling the db helper class
    com.bevan.mongofit.Databases.dbHelper dbHelper;

    // Getting the text views
    TextView user;
    TextView age;
    TextView gender;
    TextView height;
    TextView weight;
    TextView goalWeight;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // getting an instance of db helper class
        dbHelper = new dbHelper(this);

        // parsing the username value from the login page
        Intent i = getIntent();
        username = i.getStringExtra("username").trim();

        String[] genderAndAge = dbHelper.getAgeAndGender(username);
        String[] weightAndHeight = dbHelper.getWeightAndHeight(username);
        user = findViewById(R.id.tvUsername);
        age = findViewById(R.id.tvAge);
        gender = findViewById(R.id.tvGender);
        height = findViewById(R.id.tvHeight);
        weight = findViewById(R.id.tvWeight);
        goalWeight = findViewById(R.id.tvGoalWeight);

        // setting the text for the users profile
        user.setText(username);
        age.setText(genderAndAge[0]);
        gender.setText(genderAndAge[1]);

        // if the database is equal to metric then show metric else show imperial
        if (weightAndHeight[3].equals("Metric")) {
            height.setText(weightAndHeight[0] + " cm");
            weight.setText(weightAndHeight[1] + " kg");
            goalWeight.setText(weightAndHeight[2] + " kg");
        } else if (weightAndHeight[3].equals("Imperial")) {
            height.setText(weightAndHeight[0] + " inches");
            weight.setText(weightAndHeight[1] + " lb");
            goalWeight.setText(weightAndHeight[2] + " lb");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Profile.this, Landing.class);
        intent.putExtra("username", username);
        startActivity(intent);
        Profile.this.finish();
    }


    public void deleteAccount(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                String username = user.getText().toString().trim();
                dbHelper.deleteUser(username);
                Intent i = new Intent(Profile.this, Login.class);
                startActivity(i);

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void updateAccount(View view) {


        // Creating an alert dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Account");

        // getting the edit texts for the dialog pop up
        final EditText editAge = new EditText(this);
        editAge.setHint("Edit Age");

        final EditText editHeight = new EditText(this);
        editHeight.setHint("Edit Height");

        final EditText editWeight = new EditText(this);
        editWeight.setHint("Edit Weight");

        final EditText editGoalWeight = new EditText(this);
        editGoalWeight.setHint("Edit Goal Weight");

        final EditText editPassword = new EditText(this);
        editPassword.setHint("Change Password");


        // Setting the edit texts to there right characters
        editAge.setInputType(InputType.TYPE_CLASS_NUMBER);
        editHeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        editWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        editGoalWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // Creating a layout to load the message box
        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(editAge);
        lay.addView(editHeight);
        lay.addView(editWeight);
        lay.addView(editGoalWeight);
        lay.addView(editPassword);
        builder.setView(lay);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // getting the details for the DB helper
                String[] genderAndAge = dbHelper.getAgeAndGender(username);
                String[] weightAndHeight = dbHelper.getWeightAndHeight(username);
                String[] getPassword = dbHelper.getPassword(username);

                // Declaring the variables and assigning them to the edit texts
                String edAge = editAge.getText().toString();
                String edHeight = editHeight.getText().toString();
                String edWeight = editWeight.getText().toString();
                String edGoalWeight = editGoalWeight.getText().toString();
                String edPassword = editPassword.getText().toString();

                if (edAge.equals("")) {
                    edAge = genderAndAge[0];
                }
                if (edHeight.equals("")) {
                    edHeight = weightAndHeight[0];
                }
                if (edWeight.equals("")) {
                    edWeight = weightAndHeight[1];
                }
                if (edGoalWeight.equals("")) {
                    edGoalWeight = weightAndHeight[2];
                }
                if (edPassword.equals("")) {
                    edPassword = getPassword[0];
                }

                // updating the database and updating those values with the dbhelper
                dbHelper.updateInfo(username, edAge, edPassword);
                dbHelper.updateWeightAndHeight(username, edWeight, edGoalWeight, edHeight);

                // closing down the profile class
                Intent intent = getIntent();
                intent.putExtra("username", username);
                finish();
                startActivity(intent);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
