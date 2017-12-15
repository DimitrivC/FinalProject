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

public class Main2Activity extends AppCompatActivity {

    // to check current auth state
    private FirebaseAuth mAuth;

    // to read and write: net wat anders dan als je bij assistent -> save and... 4 kijkt
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // to check current auth state (used in onStart below)
        mAuth = FirebaseAuth.getInstance();

        // to read and write: net wat anders dan als je bij assistent -> save and... 4 kijkt
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // show current score user and show it
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get userId current user.
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String userId = currentUser.getUid();

                // Get score object
                Score score = dataSnapshot.child("userscores").child(userId).getValue(Score.class);

                // get access to textview to show score
                TextView TextViewScore = findViewById(R.id.textViewScore2);

                // set score in textview
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

        // get access to TextViews for the category, question and correct answer (test).
        final TextView TextViewCategory = findViewById(R.id.textViewCategory);
        final TextView TextViewQuestion = findViewById(R.id.textViewQuestion);
        final TextView TextViewCorrectAnswer = findViewById(R.id.textViewCorrectAnswer);

        // to determine random position of correct quize answer in listview
        Random rand = new Random();
        final int value = rand.nextInt(3);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final ListView listAnswers = findViewById(R.id.listView);
        String url = "https://opentdb.com/api.php?amount=1&type=multiple";
        // Source: https://opentdb.com/api_config.php
        final List<String> listdata = new ArrayList<>();

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        listdata);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //mTextView.setText(response.toString());
                        try {

                            // convert JSONObject to JSONArray
                            JSONArray jsonArray = response.getJSONArray("results");
                            if (jsonArray != null) {
                                TextViewCategory.setText(jsonArray.getJSONObject(0).getString("category"));
                                TextViewQuestion.setText(jsonArray.getJSONObject(0).getString("question"));
                                TextViewCorrectAnswer.setText(jsonArray.getJSONObject(0).getString("correct_answer"));

                                JSONArray jsonArray1 =  jsonArray.getJSONObject(0).getJSONArray("incorrect_answers");

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

                            System.out.print(score.score);
                            Log.d("score boven", String.valueOf(score.score));
                            // increase score by one
                            score.score += 1;
                            System.out.print(score.score);
                            Log.d("score beneden", String.valueOf(score.score));

                            // set new score in database
                            mDatabase.child("userscores").child(userId).setValue(score);

                            // restart activity to show new question
                            finish();
                            startActivity(getIntent());
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Score failed, log a message
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
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Score failed, log a message
                            Log.w("getting score failed", "loadPost:onCancelled", databaseError.toException());
                        }
                    });
                }
            }
        }); // EIND SETONITEMCLICKLISTENER

    }// EINDE ONCREATE

    // to Log Out if user clicks on Log out button and to return to MainActivity
    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

    // to check if user is signed in, if not, go back to Main (kan het essentiele deel code hiervan niet gewoon in onCreate?, of is dat minder net?)
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // if user is not signed in, so null, go to back to MainActivity
        if (currentUser == null){
            startActivity(new Intent(this, MainActivity.class));
        }
        // denk overbodige method
        //updateUI(currentUser);
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
                //Log.d("test3", onOptionsItemSelected'')
                Log.d("dialog fragment", "onOptionsItemSelected: ");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ScoreFragment fragment = new ScoreFragment();
                fragment.show(ft, "dialog");

        }
        return super.onOptionsItemSelected(item);
    }


    // to ensure that when back pressed, they leave app
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}// EINDE ACTIVITY
