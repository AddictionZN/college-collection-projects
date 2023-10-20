package com.example.mongonav;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;

import com.example.mongonav.Activities.MapsActivity;
import com.example.mongonav.Activities.ProfileActivity;
import com.example.mongonav.Activities.SettingActivity;
import com.example.mongonav.Activities.TripsActivity;
import com.example.mongonav.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity{

    CardView ViewProfile, Change_Settings, Open_Maps, Previous_Trips, Logout;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent i = getIntent();
        email = i.getStringExtra("email");

        //Get instance
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        //Perform Conversion
        ViewProfile = findViewById(R.id.cardProfile);
        Change_Settings = findViewById(R.id.cardSettings);
        Open_Maps = findViewById(R.id.cardMaps);
        Previous_Trips = findViewById(R.id.cardTrips);
        Logout = findViewById(R.id.cardLogout);

        ViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(BaseActivity.this, ProfileActivity.class);
            intent.putExtra("email", email.trim());
            startActivity(intent);
            BaseActivity.this.finish();

        });

        Change_Settings.setOnClickListener(v -> {
            Intent intent = new Intent(BaseActivity.this, SettingActivity.class);
            intent.putExtra("email", email.trim());
            startActivity(intent);
            BaseActivity.this.finish();
        });

        Open_Maps.setOnClickListener(v -> {
            Intent intent = new Intent(BaseActivity.this, MapsActivity.class);
            intent.putExtra("email", email.trim());
            startActivity(intent);
            BaseActivity.this.finish();
        });

        Previous_Trips.setOnClickListener(v -> {
            Intent intent = new Intent(BaseActivity.this, TripsActivity.class);
            intent.putExtra("email", email.trim());
            startActivity(intent);
            BaseActivity.this.finish();
        });

        Logout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(intent);
            BaseActivity.this.finish();
        });

    }
}