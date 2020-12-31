package com.petros.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class MainActivity extends AppCompatActivity {

    public static final String TRENDS_CALL = "Trends Call";
    private static final String WOEID = "1";
    public static final String TRENDS_LIST = "Trends List";
    private API twitterAPI;
    private ArrayList<String> trendsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwitterCalls twitterCalls = new TwitterCalls();
        twitterAPI = twitterCalls.getTwitterAPI();

        Button getTrendsBtn = (Button) findViewById(R.id.getTrendsBtn);
        getTrendsBtn.setOnClickListener(v -> {
            Toast.makeText(this,"Getting Trends...", Toast.LENGTH_SHORT).show();
            twitterCalls.callTrends(MainActivity.this,twitterAPI, WOEID);
        });

    }
}