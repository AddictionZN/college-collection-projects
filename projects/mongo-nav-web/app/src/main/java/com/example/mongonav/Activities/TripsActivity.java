package com.example.mongonav.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mongonav.BaseActivity;
import com.example.mongonav.Models.History;
import com.example.mongonav.Models.ObjectSerializer;
import com.example.mongonav.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class TripsActivity extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;

    // Firebase elements
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private DatabaseReference tripsHistory;
    private FirebaseFirestore dataUsers;
    private FirebaseUser user;

    // Getting the email throughout the application
    private String email;

    // Buttons
    Button btnBack;

    // List Views
    ListView listViewHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        // Getting the string of the email address to pull of the data from the share preferences
        Intent i = getIntent();
        email = i.getStringExtra("email");

        // Getting the user to be set with the firestore database
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Getting the Shared Preferences
        SharedPreferences sharedPreferences = this.getSharedPreferences(email, Context.MODE_PRIVATE);

        // Creating string arrays for the lats and longs
        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();

        places.clear();
        latitudes.clear();
        longitudes.clear();
        locations.clear();

        try {

            // 1. Getting the Object Serializer.
            // 2. Converting the place, lat and long from the Shared Preferences and putting it into array List
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats",ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lons",ObjectSerializer.serialize(new ArrayList<String>())));


        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1. If size is null then add the a comment please choose a location or add a new place
        // 2. Then run through a for loop for the array list for the lat and long.
        if (places.size() > 0 && latitudes.size() > 0 && longitudes.size() > 0) {
            if (places.size() == latitudes.size() && places.size() == longitudes.size()) {
                for (int x=0; x < latitudes.size(); x++) {
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(x)), Double.parseDouble(longitudes.get(x))));
                }
            }
        } else {
            places.add("Add a new place...");
            locations.add(new LatLng(0,0));
        }

        // Getting the back Button to the resource
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(TripsActivity.this, BaseActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            TripsActivity.this.finish();

        });

        // Getting the list view
        listViewHistory = findViewById(R.id.listViewLocation);

        // Creating an array adapter for the xml and the class
        arrayAdapter = new ArrayAdapter(TripsActivity.this, android.R.layout.simple_list_item_1, places);

        // Creating that list view to the array adapter and producing the outcomes from the lists for lat, long and places
        listViewHistory.setAdapter(arrayAdapter);

        listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("place",position);
                intent.putExtra("email", email);
                startActivity(intent);
                TripsActivity.this.finish();

            }
        });
    }
}
