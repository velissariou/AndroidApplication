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
    private static final String TWITTER_API_KEY = BuildConfig.TWITTER_API_KEY;
    private static final String TWITTER_API_SECRET = BuildConfig.TWITTER_API_SECRET;
    private static final String TWITTER_API_ACCESS_TOKEN = BuildConfig.TWITTER_API_ACCESS_TOKEN;
    private static final String TWITTER_API_ACCESS_TOKEN_SECRET = BuildConfig.TWITTER_API_ACCESS_TOKEN_SECRET;
    private static final String TWITTER_API_URL = "https://api.twitter.com/1.1/";
    private static final String TWITTER_API_URL_MEDIA_UPLOAD = "https://upload.twitter.com/1.1/";
    public static final String TRENDS_CALL = "Trends Call";
    private static final String WOEID = "1";
    public static final String TRENDS_LIST = "Trends List";
    private API twitterAPI;
    private ArrayList<String> trendsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twitterAPI = getAPI(TWITTER_API_URL,
                TWITTER_API_KEY,
                TWITTER_API_SECRET,
                TWITTER_API_ACCESS_TOKEN,
                TWITTER_API_ACCESS_TOKEN_SECRET);

        TwitterCalls twitterCalls = new TwitterCalls(twitterAPI);

        Button getTrendsBtn = (Button) findViewById(R.id.getTrendsBtn);
        getTrendsBtn.setOnClickListener(v -> {
            Toast.makeText(this,"Getting Trends...", Toast.LENGTH_SHORT).show();
            twitterCalls.callTrends(MainActivity.this,twitterAPI, WOEID);
        });

    }

    private API getAPI(String URL, String ConsumerKey, String ConsumerSecret, String AccessToken, String AccessSecret) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(ConsumerKey, ConsumerSecret);
        consumer.setTokenWithSecret(AccessToken, AccessSecret);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(API.class);
    }
}