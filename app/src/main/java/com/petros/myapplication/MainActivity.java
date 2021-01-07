package com.petros.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String WOEID = "1";
    public static final String TRENDS_LIST = "Trends List";
    private API twitterAPI;
    Button getTrendsBtn;
    Button postBtn;
    Button storyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwitterCalls twitterCalls = new TwitterCalls();

        getTrendsBtn = findViewById(R.id.getTrendsBtn);
        getTrendsBtn.setOnClickListener(v -> {
            Toast.makeText(this,"Getting Trends...", Toast.LENGTH_SHORT).show();
            twitterCalls.callTrends(MainActivity.this, WOEID);
        });

        postBtn = findViewById(R.id.createPostButton);
        postBtn.setOnClickListener(v -> startActivity(new Intent(this, PostActivity.class)));

        storyBtn = findViewById(R.id.storyButton);
        storyBtn.setOnClickListener(v -> startActivity(new Intent(this, StoryActivity.class)));


    }
}