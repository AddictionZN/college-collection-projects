package com.bevan.mongofit.MenuActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bevan.mongofit.Landing;
import com.bevan.mongofit.R;

public class Help extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // parsing the username value from the login page
        Intent i = getIntent();
        username = i.getStringExtra("username").trim();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Help.this, Landing.class);
        intent.putExtra("username", username);
        startActivity(intent);
        Help.this.finish();
    }

}
