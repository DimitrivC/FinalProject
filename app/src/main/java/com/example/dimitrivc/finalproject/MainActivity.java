package com.example.dimitrivc.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final Intent intent = new Intent(this, Main2Activity.class);

        final TextView mTextView = findViewById(R.id.textView);
        final TextView TextView2 = findViewById(R.id.textView2);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final ListView listCategories = findViewById(R.id.listView);
        //String url = "https://resto.mprog.nl/categories";
        String url = "https://opentdb.com/api.php?amount=1&type=multiple";
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
                                mTextView.setText(jsonArray.getJSONObject(0).getString("category"));
                                TextView2.setText(jsonArray.getJSONObject(0).getString("question"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    listdata.add(jsonArray.getString(i));


                                    listdata.add(jsonArray.getJSONObject(0).getString("correct_answer"));
                                    JSONArray jsonArray1 =  jsonArray.getJSONObject(0).getJSONArray("incorrect_answers");

                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        listdata.add(jsonArray1.get(j).toString());
                                    }

                                    //

                                    // voor elk antwoord een item van de listview.
                                    // in de array die je met key results krijgt, zitten weer objects.
                                }
                            }

                            listCategories.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTextView.setText("@string/error");
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }



}
