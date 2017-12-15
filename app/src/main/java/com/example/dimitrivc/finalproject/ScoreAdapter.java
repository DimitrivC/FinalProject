package com.example.dimitrivc.finalproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by DimitrivC on 11-12-2017.
 */

public class ScoreAdapter extends ArrayAdapter {

    public ScoreAdapter(@NonNull Context context, int layout, int resource) {
        super(context, R.layout.row_scores, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get access to textviews to set email and score
        final TextView Email = convertView.findViewById(R.id.textViewUsers);
        final TextView Score = convertView.findViewById(R.id.textViewScores);

        Log.d("test3", "tsdghdf");


        return convertView;
    } // EINDE GETVIEW

} // EINDE ADAPTER
