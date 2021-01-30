package com.petros.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String WOEID = "1";
    public static final String TRENDS_LIST = "Trends List";
    private String userQuery;
    private static String QUERY = "";
    Button getTrendsBtn;
    Button postBtn;
    Button searchTrendsBtn;
    EditText searchTrendsTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwitterCalls twitterCalls = new TwitterCalls();

        getTrendsBtn = findViewById(R.id.getTrendsBtn);
        getTrendsBtn.setOnClickListener(v -> {
            Toast.makeText(this,"Getting Trends...", Toast.LENGTH_SHORT).show();
            twitterCalls.callTrends(this, WOEID, QUERY);
        });

        postBtn = findViewById(R.id.createPostButton);
        postBtn.setOnClickListener(v -> startActivity(new Intent(this, PostActivity.class)));

        searchTrendsTxt = findViewById(R.id.searchTrendsText);
        searchTrendsBtn = findViewById(R.id.searchTrendsButton);
        searchTrendsBtn.setOnClickListener(v -> {
            userQuery = searchTrendsTxt.getText().toString();
            Toast.makeText(this, "Searching for Trends...", Toast.LENGTH_SHORT).show();
            twitterCalls.callTrends(this, WOEID, userQuery);
        });

    }
}

