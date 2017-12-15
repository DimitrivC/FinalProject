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

public class MainActivity extends AppCompatActivity {

    // to check current auth state
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // to check current auth state
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        mAuth = FirebaseAuth.getInstance();

    } // EIND ONCREATE

    // to check if user is signed in, if so, go to Main2 (kan het essentiele deel code hiervan niet gewoon in onCreate?)
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // if user is signed in already, go to Main2Activity
        if (currentUser != null){
            startActivity(new Intent(this, Main2Activity.class));
        }
        // denk overbodige method
        //updateUI(currentUser);
    }

    // method for button to get email, password, and go to method signIn
    public void getMailPassandSignIn(View view) {

        EditText E_mail = findViewById(R.id.editTextEmail);
        EditText Password = findViewById(R.id.editTextPassword);

        String email = E_mail.getText().toString();
        String password = Password.getText().toString();

        signIn(email, password);
    }

    // this method is called from method above, to sign user in, and if succesful to send to Main2Activity
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signed in", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);

                            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("sign in failed", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    } // EIND SIGNIN

    // method for Button to create new account, forwards  you to Main3  to create new account
    public void goToMain3(View view) {

        //Intent intent = new Intent(this, Main3Activity.class);
        startActivity(new Intent(this, Main3Activity.class));
    }

    // test method
    // deze  is puur om makkelijk naar Main2 te kunnen gaan om die te testen.
    public void goToMain2(View view) {

        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }


    // to ensure that when back pressed, the app closes: DIT OOK AAN MAIN2 TOEVOEGEN??????????????
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

} // EIND ACTIVITY
