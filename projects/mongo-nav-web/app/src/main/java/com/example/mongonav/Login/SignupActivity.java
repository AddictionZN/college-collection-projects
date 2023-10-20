package com.example.mongonav.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mongonav.Models.User;
import com.example.mongonav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    // Firebase elements
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private FirebaseFirestore dataUsers;

    EditText usernameText;
    EditText emailText;
    EditText passwordText;
    EditText reEnterPasswordText;
    Button btnRegister;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Connecting to the firebase
        // FireBase initialisation
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        dataUsers = FirebaseFirestore.getInstance();

        usernameText = findViewById(R.id.input_username);
        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        reEnterPasswordText = findViewById(R.id.input_reEnterPassword);
        btnRegister = findViewById(R.id.btn_signup);
        loginLink = findViewById(R.id.link_login);

        btnRegister.setOnClickListener(v -> createUser());

        loginLink.setOnClickListener(v -> {
            // Finish the registration screen and return to the Login activity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
    }

    public void createUser() {

        if (!validate()) {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        } else {

            auth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                String id = users.push().getKey();

                                //saving users to database
                                User user = new User();
                                user.setUsername(usernameText.getText().toString().trim());
                                user.setEmail(emailText.getText().toString().trim());
                                user.setPassword(passwordText.getText().toString().trim());
                                user.setTransport("Car");
                                user.setSystem("Metric");
                                user.setMode("Light");

                                DocumentReference dbUser = dataUsers.collection("users").document(emailText.getText().toString().trim());

                                dbUser.set(user).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Registration is complete", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        SignupActivity.this.finish();
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    // Validating all the possible errors in the registration.
    public boolean validate() {
        boolean valid = true;

        String username = usernameText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String confirmPassword = reEnterPasswordText.getText().toString().trim();

        // Username Errors
        if (username.isEmpty()) {
            usernameText.setError("Enter a valid email address");
            valid = false;
        } else {
            usernameText.setError(null);
        }

        // Email Errors
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }
        // Password Errors
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        // Re-entered Password Errors
        if (password.isEmpty() || !password.equals(confirmPassword)) {
            reEnterPasswordText.setError("Please enter the save password");
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
        }

        return valid;
    }
}





