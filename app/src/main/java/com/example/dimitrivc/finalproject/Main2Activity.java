package com.example.dimitrivc.finalproject;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
Main2Activity

This is the main part of the app. Here, if a user is logged in, they can answer multiple choice
quiz questions. If correct, this increases their score by one, if incorrect this decreases their
score by one. The questions and answers are dynamically implemented via an API
(https://opentdb.com/api_config.php), and the user scores are saved in Firebase and updated in
Firebase immediately. In this activity, a user can click on an Actionbar Icon, which is supposed to
show all the scores and emails from all the users, via ScoreFragment. A user can also logout, and
is than redirected to MainActivity, or is the back button is pressed, they will leave the app
immediately.

 */

public class Main2Activity extends AppCompatActivity {

    // to check current auth state
    private FirebaseAuth mAuth;

    // to read and write
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // to check current auth state (used in onStart and onDataChange)
        mAuth = FirebaseAuth.getInstance();

        // to read and write (used in onDataChange and Listener)
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // get current score user and show it in TextView
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get userId current user.
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String userId = currentUser.getUid();

                // Get score object
                Score score = dataSnapshot.child("userscores").child(userId).getValue(Score.class);

                // get access to TextView to show score
                TextView TextViewScore = findViewById(R.id.textViewScore2);

                // set score in TextView
                if (score != null) {
                    TextViewScore.setText(String.valueOf(score.score));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Score failed, log a message
                Log.w("getting score failed", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);

        // get access to TextViews for the question and correct answer (test).
        final TextView TextViewCategory = findViewById(R.id.textViewCategory);
        final TextView TextViewQuestion = findViewById(R.id.textViewQuestion);
        //final TextView TextViewCorrectAnswer = findViewById(R.id.textViewCorrectAnswer);

        // to determine random position of correct quize answer in listview
        Random rand = new Random();
        final int value = rand.nextInt(3);

        // to get question and answers from API
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // get access to listView to put answers to question
        final ListView listAnswers = findViewById(R.id.listView);
        String url = "https://opentdb.com/api.php?amount=1&type=multiple";
        // Source: https://opentdb.com/api_config.php
        final List<String> listdata = new ArrayList<>();

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        listdata);

        // to get the question and answers
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            // convert JSONObject to JSONArray
                            JSONArray jsonArray = response.getJSONArray("results");
                            if (jsonArray != null) {
                                TextViewQuestion.setText(jsonArray.getJSONObject(0).getString("question"));
                                //TextViewCorrectAnswer.setText(jsonArray.getJSONObject(0).getString("correct_answer"));

                                JSONArray jsonArray1 =  jsonArray.getJSONObject(0).getJSONArray("incorrect_answers");

                                // to store the correct answer on a random point between the incorrect
                                //answers
                                if (value < 3) {
                                    String item = jsonArray1.getString(value);
                                    jsonArray1.put(3, item);
                                }

                                jsonArray1.put(value, jsonArray.getJSONObject(0).getString("correct_answer"));

                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    listdata.add(jsonArray1.get(j).toString());
                                }
                            }

                            listAnswers.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", String.valueOf(error));
                        TextViewCategory.setText("@string/error2");
                    }
                });
        requestQueue.add(jsonObjectRequest);

        // listener to check user's answer to quiz question
        listAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                // if answer is correct, increase user's score by one
                if (position == value) {
                    TextViewCategory.setText("correct");

                    // get user's score from database and increase by one
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // get userId current user.
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String userId = currentUser.getUid();

                            // Get Score object
                            Score score = dataSnapshot.child("userscores").child(userId).getValue(Score.class);

                            // increase score by one
                            score.score += 1;

                            // set new score in database
                            mDatabase.child("userscores").child(userId).setValue(score);

                            // restart activity to show new question
                            finish();
                            startActivity(getIntent());
                        }
                        // if getting score failed, log a message
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("getting score failed", "loadPost:onCancelled", databaseError.toException());
                        }
                        });
                }
                // if user's answer incorrect, decrease his score by one
                else {
                    TextViewCategory.setText("incorrect");

                    // get user's score from database and decrease by one
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // get userId current user.
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String userId = currentUser.getUid();

                            // Get Score object
                            Score score = dataSnapshot.child("userscores").child(userId).getValue(Score.class);

                            // decrease score by one
                            score.score -= 1;

                            // set new score in database
                            mDatabase.child("userscores").child(userId).setValue(score);

                            // restart activity to show new question
                            finish();
                            startActivity(getIntent());
                        }
                        // if getting score failed, log a message
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("getting score failed", "loadPost:onCancelled", databaseError.toException());
                        }
                    });
                }
            }
        }); // EIND SETONITEMCLICKLISTENER

    }// EINDE ONCREATE

    // if user clicks on Log out button
    public void logOut(View view) {

        // sign out user
        FirebaseAuth.getInstance().signOut();

        // go to MainActivity
        startActivity(new Intent(this, MainActivity.class));
    }

    // check if user is signed in
    @Override
    public void onStart() {
        super.onStart();

        // get access to current user to check if signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // if user is not signed in, go back to MainActivity
        if (currentUser == null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    // to show Actionbar Icon for ScoreFragment
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.score_dialog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // to handle events for Actionbar icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.score_overview:

                // show fragment with all user emails and scores
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ScoreFragment fragment = new ScoreFragment();
                fragment.show(ft, "dialog");

        }
        return super.onOptionsItemSelected(item);
    }

    // to ensure that when back pressed, they leave app
    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}// EINDE ACTIVITY
