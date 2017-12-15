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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
CreateUserActivity

This activity is for users to create a new account if they don't have an account yet. This is done
using Firebase. If a user makes a new account, automatically a personal Score object is made and
stored in Firebase. They will get a starting score of 0 (which they can increase or decrease in
QuizActivity by answering quiz questions), and also their emailadress is stored, so this can be
shown in ScoreFragment, with their score. After a new user is succesfully created, they are
redirected to QuizActivity.

 */

public class CreateUserActivity extends AppCompatActivity {

    // to check current auth state, to create new user
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_activity);

        // to check current auth state, to create new user
        mAuth = FirebaseAuth.getInstance();

    }

    public void getMailPassAndCreateUser(View view) {

        // get access to textViews for email and password
        EditText E_mail = findViewById(R.id.editTextNewEmail);
        EditText Password = findViewById(R.id.editTextNewPassword);

        // get email and password chosen by user
        String email = E_mail.getText().toString();
        String password = Password.getText().toString();

        // pass email and password on to createUser
        createUser(email, password);
    }

    // to create a new user for Firebase
    public void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d("user created", "createUserWithEmail:success");

                            // get database reference to make new Score object
                            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                            // make new Score object for user, and store in Firebase
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    // get access to current user for userId and email
                                    FirebaseUser currentUser = mAuth.getCurrentUser();

                                    // get userId user to store new object
                                    String userId = currentUser.getUid();

                                    // get email user
                                    String email = currentUser.getEmail();

                                    // create score object with a starting score of 0, and the users email
                                    Score score = new Score(0, email);

                                    // set new score in database
                                    mDatabase.child("userscores").child(userId).setValue(score);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Getting Score failed, log a message
                                    Log.w("getting score failed", "loadPost:onCancelled", databaseError.toException());
                                }
                            });
                            //mDatabase.addValueEventListener(postListener);

                            // After success, forward user to quiz page.
                            startActivity(new Intent(getApplicationContext(), QuizActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("creation user failed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateUserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // to ensure that when back pressed, they go to LogInActivity
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LogInActivity.class));
    }

}
