package com.example.mongonav.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mongonav.BaseActivity;
import com.example.mongonav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;

    // FireBase variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore dataUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Getting the firestore database to the instance
        dataUsers = FirebaseFirestore.getInstance();

        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);

        signupLink.setOnClickListener(v -> {

            // Start the Signup activity
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            LoginActivity.this.finish();

                        } else {
                            onLoginFailed();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });

    }

    // If the login doesn't work with the firebase
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        LoginActivity.this.finish();
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }
}
