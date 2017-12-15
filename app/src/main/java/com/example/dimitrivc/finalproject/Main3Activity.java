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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// for the creation of a new account, after which they will be directed to Main2Activity, for the quiz
public class Main3Activity extends AppCompatActivity {

    // to check current auth state, to create new user
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // to check current auth state, to create new user
        mAuth = FirebaseAuth.getInstance();

    } // EIND ONCREATE

    public void getMailPassAndCreateUser(View view) {

        EditText E_mail = findViewById(R.id.editTextNewEmail);
        EditText Password = findViewById(R.id.editTextNewPassword);

        String email = E_mail.getText().toString();
        Log.d("email string", email);
        String password = Password.getText().toString();
        Log.d("password string", password);

        createUser(email, password);
    }

    public void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("user created", "createUserWithEmail:success");
                            final FirebaseUser currentUser = mAuth.getCurrentUser();
                            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                            // set user's score to 0 and get email
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    // get access to current user for userId and email
                                    FirebaseUser currentUser = mAuth.getCurrentUser();

                                    // get userId user
                                    String userId = currentUser.getUid();

                                    // get email user
                                    String email = currentUser.getEmail();

                                    // create score object with a score of 0, and the users email
                                    Score score = new Score(1, email);

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
                            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("creation user failed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Main3Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    // to ensure that when back pressed, they go to MainActivity
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }


} // EIND ACTIVITY
