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
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends DialogFragment {

    // to read and write: net wat anders dan als je bij assistent -> save and... 4 kijkt
    private DatabaseReference mDatabase;

    // to check current auth state
    private FirebaseAuth mAuth;

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

        // doesn't work, unfortunately.

        mDatabase.child("userscores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot ds = dataSnapshot.child("scores");
                ds.getChildren();

                for(DataSnapshot child : ds.getChildren()) {

                    //Log.d("email", String.valueOf(ds));
                    //Log.d("score", String.valueOf(score));

                    // werkt niet, zou denk ik ook niet moeten werken?
                    Score classtest = child.getValue(Score.class);
                    Log.d("classtest", String.valueOf(classtest));
                    forListView.add(classtest.email + " : " + String.valueOf(classtest.score));


                    // werkt niet, zou denk ik ook niet moeten werken
//                    String email = ds.child("score").child("email").getValue(String.class);
//                    Integer score = ds.child("score").child("score").getValue(Integer.class);
//                    forListView.add(email);
//                    forListView.add(String.valueOf(score));

                    // werkt niet, maar dit zou toch wel moeten?
                    String email = ds.child("email").getValue(String.class);
                    forListView.add(email);
                    Integer score = ds.child("score").getValue(Integer.class);
                    forListView.add(String.valueOf(score));

                }

///////////////////////////////////////////////////////////
//                mDatabase.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        data.setText(String.valueOf(dataSnapshot));
//
//                        for(DataSnapshot snap2 : dataSnapshot.getChildren()){
//                            String userId = snap2.getKey();
//                            // deze string is: userscores.
//                            data.setText(userId);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

//                String for_json = after(String.valueOf(dataSnapshot), "DataSnapshot ");
//                //data.setText(for_json);
//                try {
//                    JSONObject test2 = new JSONObject(for_json);
//
//                    //data.setText(String.valueOf(test2));
//
//                    JSONObject test = (JSONObject) test2.get("value");
//
//                    // ipv ??? moet er de hashed passwords of zo?
//                    JSONObject bla = (JSONObject) test.get("????????");
//                    // en hoe moet ik dan daar overheen itereren? Met een iterator zoals beneden?
//
//                    //data.setText(String.valueOf(bla));
//
//                    // value krijgen van userscores
//
//                }
//                catch (JSONException e) { }
/////////////////////////////////////////////////////

//            Iterable emailAndScores = dataSnapshot.getChildren();
//                Iterator iterator = emailAndScores.iterator();
//                while (iterator.hasNext()){
//
//                    // get current object from iterator
//                    Object score = iterator.next();
//
////                    assertThat(score, hasProperty(name));
//////                    if (score.score != null){
//////
//////                    }
//////                    Score.setText(String.valueOf(score.score));
//////                    Email.setText(score.email);
//                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Score failed, log a message
                Log.w("getting score failed", "loadPost:onCancelled", databaseError.toException());
            }
        }); // EIND ADD SETONITEMCLICKLISTENER voor snapdatabase



        // dit ipv alles van de andere adapter
        ArrayAdapter<String> bla = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, forListView);


        // setAdapter to listview
        ListView scores = getView().findViewById(R.id.listScores);
        scores.setAdapter(bla);

    } // EINDE ONVIEWSTATERESTORED

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    //////////////////////
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
/////////////////////////


} // EINDE FRAGMENT
