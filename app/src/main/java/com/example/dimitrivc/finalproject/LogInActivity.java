package com.example.dimitrivc.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

/**
LogInActivity

First activity the user encounters (unless they're logged in). This activity gives them the
opportunity to login (using Firebase) with e-mail and password, or if they don't have an account
yet, they can go to CreateUserActivity to create an account. If they log in (or if they are logged in
already), they will be directed to QuizActivity.

 */

public class LogInActivity extends AppCompatActivity {

    // to check current auth state
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        // to check current auth state
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        mAuth = FirebaseAuth.getInstance();

    }

    // to check if user is signed in
    @Override
    public void onStart() {
        super.onStart();
        // get access to user to check if signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // if user is signed in already, go to QuizActivity
        if (currentUser != null){
            startActivity(new Intent(this, QuizActivity.class));
        }
    }

    // method for button to get email, password, and go to method signIn
    public void getMailPassandSignIn(View view) {

        // get access to textView
        EditText E_mail = findViewById(R.id.editTextEmail);
        EditText Password = findViewById(R.id.editTextPassword);

        // get email and password as given by user
        String email = E_mail.getText().toString();
        String password = Password.getText().toString();

        // pass email and password on to signIn
        signIn(email, password);
    }

    // to sign user in, and if successful to send to QuizActivity
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d("signed in", "signInWithEmail:success");

                            // redirect user to QuizActiviy
                            startActivity(new Intent(getApplicationContext(), QuizActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("sign in failed", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // method for Button to create new account, directs user to CreateUserActivity
    public void goToCreateUserActivity(View view) {

        startActivity(new Intent(this, CreateUserActivity.class));
    }

    // to ensure that when back pressed, the app closes
    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
