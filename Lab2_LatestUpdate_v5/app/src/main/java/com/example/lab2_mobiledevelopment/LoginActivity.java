package com.example.lab2_mobiledevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Tag;

import java.time.Instant;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    private FirebaseAuth stu_auth;

    // UI references
    private AutoCompleteTextView stu_emailView;
    private EditText stu_passwordView;
    FirebaseUser stu_firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        stu_auth = FirebaseAuth.getInstance();

        stu_emailView = (AutoCompleteTextView) findViewById(R.id.email);
        stu_passwordView = (EditText) findViewById(R.id.login_password);

        stu_firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // if the user signed in already it will go to the mainactivity.
        if(stu_firebaseUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void Login(View view){
        //Reset errors
        stu_emailView.setError(null);
        stu_passwordView.setError(null);

        // Store values at the time when user try to login

        String stu_email = stu_emailView.getText().toString();
        String stu_password = stu_passwordView.getText().toString();

        // check for a valid password
        if(stu_email.matches("")){
            Toast.makeText(this, "You need to enter an email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(stu_password.matches("")){
            Toast.makeText(this, "You need to enter a password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(stu_password) && !checkValidPassword(stu_password)){
            stu_passwordView.setError("Password must be at least 5 characters");
        }


        if(!checkValidEmail(stu_email) && !TextUtils.isEmpty(stu_email)){
            stu_emailView.setError("Your email is not valid");

        }


        stu_auth.signInWithEmailAndPassword(stu_email, stu_password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to user
                        // If signin is successful, the auth state listener will be notified and logic
                        // to handle signed in

                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public boolean checkValidPassword(String stu_password){
        return  stu_password.length() >= 5;
    }

    public boolean checkValidEmail(String stu_email){
        return Patterns.EMAIL_ADDRESS.matcher(stu_email).matches();
    }

    public void redirectToSignUpPage(View view){
        Intent redirect = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(redirect);
    }

}
