package com.example.dimitrivc.finalproject;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * SoreFragment
 *
 * Doesn't work, unfortunately. It's prupose is to show, after the user has clicked on the Actionbar
 * icon in QuizActivity, all the scores and all the emailadresses of all the users. Howeever, it
 * doesn't. I tried serveral versions of the code, as shown in comments below. I'm quite sure that
 * something like what I have tried is supposed to work. Some of the different things I tried, I
 * have also tried in an activity, Main4Activity, but since that didn't work either I deleted that.
 *
 */
public class ScoreFragment extends DialogFragment {

    // to read and write
    private DatabaseReference mDatabase;

    // to check current auth state
    private FirebaseAuth mAuth;

    // to store emailadresses and scores
    private ArrayList<String> forListView = new ArrayList<>();

    public ScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // to read emails and scores to put in fragment
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // to check current auth state to get currentuser
        mAuth = FirebaseAuth.getInstance();

        final TextView data = getView().findViewById(R.id.textViewData);


        mDatabase.child("userscores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                // V1
                DataSnapshot ds = dataSnapshot.child("scores");
                ds.getChildren();

                for(DataSnapshot child : ds.getChildren()) {

                    // V1.1
                    Score classtest = child.getValue(Score.class);
                    forListView.add(classtest.email + " : " + String.valueOf(classtest.score));

                    // V1.2
                    String email = ds.child("score").child("email").getValue(String.class);
                    Integer score = ds.child("score").child("score").getValue(Integer.class);
                    forListView.add(email + " : " + String.valueOf(score));

                    // V1.3
                    String email2 = ds.child("email").getValue(String.class);
                    Integer score2 = ds.child("score").getValue(Integer.class);
                    forListView.add(email2 + " : " + String.valueOf(score2));
                }


                // V2
                for(DataSnapshot ds2 : dataSnapshot.getChildren()) {

                    // V2.1
                    Score classtest = ds2.getValue(Score.class);
                    forListView.add(classtest.email + " : " + String.valueOf(classtest.score));

                    // V2.2
                    String email = ds.child("score").child("email").getValue(String.class);
                    Integer score = ds.child("score").child("score").getValue(Integer.class);
                    forListView.add(email + " : " + String.valueOf(score));

                    // V2.3
                    String email2 = ds.child("email").getValue(String.class);
                    Integer score2 = ds.child("score").getValue(Integer.class);
                    forListView.add(email2 + " : " + String.valueOf(score2));

                }



                // V3: datasnapshot omzetten naar JSONObject
//              mDatabase.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        for(DataSnapshot snap2 : dataSnapshot.getChildren()){
//                            String userId = snap2.getKey();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                          to do
//                    }
//                });
//                String for_json = after(String.valueOf(dataSnapshot), "DataSnapshot ");
//                //data.setText(for_json);
//                try {
//                    JSONObject test2 = new JSONObject(for_json);
//                    JSONObject test = (JSONObject) test2.get("value");
//                    JSONObject bla = (JSONObject) test.get("?");
//                }
//                catch (JSONException e) { }



                // V4
//                Iterable emailAndScores = dataSnapshot.getChildren();
//                Iterator iterator = emailAndScores.iterator();
//                while (iterator.hasNext()){
//
//                    // get current object from iterator
//                    Object score = iterator.next();
//
//                    assertThat(score, hasProperty(name));
//                    if (score.score != null){
//
//                    }
//                    Score.setText(String.valueOf(score.score));
//                    Email.setText(score.email);
//                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Score failed, log a message
                Log.w("getting score failed", "loadPost:onCancelled", databaseError.toException());
            }
        });

        // for V1 en V2
        ArrayAdapter<String> bla = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, forListView);

        // setAdapter to listView
        ListView scores = getView().findViewById(R.id.listScores);
        scores.setAdapter(bla);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score, container, false);
    }


    // voor V3
//    // source: https://www.dotnetperls.com/between-before-after-java
//    static String after(String value, String a) {
//        // Returns a substring containing all characters after a string.
//        int posA = value.lastIndexOf(a);
//        if (posA == -1) {
//            return "";
//        }
//        int adjustedPosA = posA + a.length();
//        if (adjustedPosA >= value.length()) {
//            return "";
//        }
//        return value.substring(adjustedPosA);
//    }

}
