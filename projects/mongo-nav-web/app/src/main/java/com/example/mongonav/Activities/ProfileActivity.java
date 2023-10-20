package com.example.mongonav.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mongonav.BaseActivity;
import com.example.mongonav.Element_Gem.GameActivity;
import com.example.mongonav.Login.LoginActivity;
import com.example.mongonav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    // Firebase elements
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private FirebaseFirestore dataUsers;
    private FirebaseUser user;

    // Getting all the Text Views
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvPassword;

    // Creating the buttons for the class
    private Button btnGame;
    private Button btnBack;
    private Button btnChange;
    private Button btnDelete;

    // Getting the email throughout the application
    private String email;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent i = getIntent();
        email = i.getStringExtra("email");


        // Connecting to the firebase
        // FireBase initialisation
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        // Getting the firestore database to the instance
        dataUsers = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();

        getUserData(email);

        tvUsername = findViewById(R.id.editUsername);
        tvEmail = findViewById(R.id.editEmail);
        tvPassword = findViewById(R.id.editPassword);

        // BUTTONS
        btnGame = findViewById(R.id.btn_game);
        btnBack = findViewById(R.id.btn_back);
        btnChange = findViewById(R.id.btn_change);
        btnDelete = findViewById(R.id.btn_delete);

        btnGame.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, GameActivity.class);
            intent.putExtra("email", email.trim());
            startActivity(intent);
            ProfileActivity.this.finish();
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, BaseActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            ProfileActivity.this.finish();
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserData();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserData(email);
            }
        });

    }

    public void getUserData(String email) {

        DocumentReference docRefUser = dataUsers.collection("users").document(email);
        docRefUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    username = documentSnapshot.getString("username");
                    password = documentSnapshot.getString("password");

                    tvEmail.setText(email);
                    tvUsername.setText(username);
                } else {
                    Toast.makeText(ProfileActivity.this, "Unable to get the user's data", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void deleteUserData(String email) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                DocumentReference docRefUser = dataUsers.collection("users").document(email);
                docRefUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                           
                                        if (task.isSuccessful()){
                                            Toast.makeText(ProfileActivity.this, "The Account has been successfully deleted!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            ProfileActivity.this.finish();
                                        }
                                        else{
                                            Toast.makeText(ProfileActivity.this, "The Account was unable to delete", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        
                        
                    }
                });
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

    public void changeUserData() {

        // Creating an alert dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");

        // getting the edit texts for the dialog pop up
        final EditText changePassword = new EditText(this);
        changePassword.setHint("Change Password");

        changePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // Creating a layout to load the message box
        LinearLayout lay = new LinearLayout(this);
        lay.addView(changePassword);

        builder.setView(lay);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Getting the new password from the user
                String newPass = changePassword.getText().toString().trim();

                AuthCredential credential = EmailAuthProvider.getCredential(email, password);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Password Has Been Changed", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(ProfileActivity.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

                dataUsers.collection("users").document(email).update(
                        "password", newPass
                );

                Intent intent = new Intent(ProfileActivity.this, BaseActivity.class);
                intent.putExtra("email", email.trim());
                startActivity(intent);
                ProfileActivity.this.finish();

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
