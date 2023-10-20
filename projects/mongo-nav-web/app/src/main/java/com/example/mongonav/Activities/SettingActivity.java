package com.example.mongonav.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mongonav.BaseActivity;
import com.example.mongonav.Login.LoginActivity;
import com.example.mongonav.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingActivity extends AppCompatActivity {

    // Firebase Elements
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private FirebaseFirestore dataUsers;
    private FirebaseUser user;

    // Declaring the action events
    private Button btnBack;
    private Button btnSubmit;

    private Spinner spnTranspot;
    private Spinner spnSystem;
    private Spinner spnMode;

    // Delcaring the Text View
    private TextView tvTransport;
    private TextView tvSystem;
    private TextView tvMode;


    // Declaring the variables for the system and transport
    private String selectTransport;
    private String selectSystem;
    private String selectMode;


    // Getting the email throughout the application
    private String email;
    private String transport;
    private String system;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent i = getIntent();
        email = i.getStringExtra("email");

        // FireBase initialisation
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        // Getting the firestore database to the instance
        dataUsers = FirebaseFirestore.getInstance();

        // Getting the user data
        getUserData(email);

        // Init the spinners
        spnTranspot = findViewById(R.id.spnTransport);
        spnSystem = findViewById(R.id.spnSystem);
        spnMode = findViewById(R.id.spnMode);

        // Init the buttons
        btnBack = findViewById(R.id.btn_back);
        btnSubmit = findViewById(R.id.btn_submit);

        // Init the TextViews
        tvTransport = findViewById(R.id.tvTransport);
        tvSystem = findViewById(R.id.tvSystem);
        tvMode = findViewById(R.id.tvMode);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettings();
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, BaseActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            SettingActivity.this.finish();
        });
    }

    public void changeSettings() {

        // Getting the value of the selected items
        selectTransport = spnTranspot.getSelectedItem() + "";
        selectSystem = spnSystem.getSelectedItem() + "";
        selectMode = spnMode.getSelectedItem() + "";

        dataUsers.collection("users").document(email).update(
                "transport", selectTransport,
                "system", selectSystem,
                "mode", selectMode
        );

        Toast.makeText(SettingActivity.this, "Settings have been changed!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SettingActivity.this, BaseActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        SettingActivity.this.finish();
    }


    public void getUserData(String email) {

        DocumentReference docRefUser = dataUsers.collection("users").document(email);
        docRefUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    transport = documentSnapshot.getString("transport");
                    system = documentSnapshot.getString("system");
                    mode = documentSnapshot.getString("mode");

                    tvTransport.setText(transport);
                    tvSystem.setText(system);
                    tvMode.setText(mode);

                } else {
                    Toast.makeText(SettingActivity.this, "Unable to get the users data", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
