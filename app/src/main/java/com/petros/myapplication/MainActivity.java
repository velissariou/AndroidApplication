package com.petros.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private static final String TRENDS_CALL = "Trends Call";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(TWITTER_API_KEY, TWITTER_API_SECRET);
        consumer.setTokenWithSecret(TWITTER_API_ACCESS_TOKEN, TWITTER_API_ACCESS_TOKEN_SECRET);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();

        Retrofit twitterRetrofit = new Retrofit.Builder()
                .baseUrl(TWITTER_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        TwitterCalls twitterCalls = new TwitterCalls(TWITTER_API_URL, okHttpClient);
        List<TrendsList> trendsList = twitterCalls.callTrends(twitterRetrofit);
        for(TrendsList trendsList1 : trendsList){
            List<TrendsList.Trends> trends = trendsList1.getTrends();
            for (TrendsList.Trends trend : trends) {
                Log.d(TRENDS_CALL, trend.getName());
            }
        }



    }
}