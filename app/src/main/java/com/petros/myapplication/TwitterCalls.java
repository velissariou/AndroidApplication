package com.petros.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;


public class TwitterCalls {
    private static final String TWITTER_API_KEY = BuildConfig.TWITTER_API_KEY;
    private static final String TWITTER_API_SECRET = BuildConfig.TWITTER_API_SECRET;
    private static final String TWITTER_API_ACCESS_TOKEN = BuildConfig.TWITTER_API_ACCESS_TOKEN;
    private static final String TWITTER_API_ACCESS_TOKEN_SECRET = BuildConfig.TWITTER_API_ACCESS_TOKEN_SECRET;
    private static final String TWITTER_API_URL = "https://api.twitter.com/1.1/";
    private static final String TWITTER_API_URL_MEDIA_UPLOAD = "https://upload.twitter.com/1.1/";
    private static final String TRENDS_CALL = "Trends_Call_Debug";
    public static final String SEARCH_TWEETS_CALL = "Search_Tweets_Debug";

    public TwitterCalls() {
    }

    public API getTwitterAPI() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(TWITTER_API_KEY, TWITTER_API_SECRET);
        consumer.setTokenWithSecret(TWITTER_API_ACCESS_TOKEN, TWITTER_API_ACCESS_TOKEN_SECRET);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITTER_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(API.class);
    }

    public void callTrends(Context context, API twitterAPI, String woeid) {
        Intent intent = new Intent(context, TrendsListActivity.class);
        ArrayList<String> trendsList = new ArrayList<>();
        Call<List<TrendsList>> call = twitterAPI.getTrendsList(woeid);

        call.enqueue(new Callback<List<TrendsList>>() {
            @Override
            public void onResponse(@NotNull Call<List<TrendsList>> call, @NotNull Response<List<TrendsList>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TRENDS_CALL, response.message());
                } else {
                    for (TrendsList aTrendsList : response.body()) {
                        if (response.body() == null) {
                            throw new AssertionError();
                        }
                        Log.d(TRENDS_CALL, "Received JSON file.");
                        for(TrendsList.Trends trends : aTrendsList.getTrends()){
                            trendsList.add(trends.getName());
                        }
                    }
                    intent.putStringArrayListExtra(MainActivity.TRENDS_LIST, trendsList);
                    context.startActivity(intent);
                }
            }
            @Override
            public void onFailure (@NotNull Call<List<TrendsList>> call, @NotNull Throwable t){
                Log.d(TRENDS_CALL, t.getMessage());
            }
        });
    }

    public void searchTweets(Context context, API twitterAPI, String query){

        ArrayList<Post> posts = new ArrayList<>();
        Intent intent = new Intent(context, ViewPostsActivity.class);
        Call<Tweets> call = twitterAPI.getTweets(query);
        call.enqueue(new Callback<Tweets>() {
            @Override
            public void onResponse(@NotNull Call<Tweets> call, @NotNull Response<Tweets> response) {
                if (!response.isSuccessful()) {
                    Log.d(SEARCH_TWEETS_CALL, response.message());
                } else {
                    if(response.body() == null){
                        throw new AssertionError();
                    }
                    Log.d(SEARCH_TWEETS_CALL, "Received JSON file.");
                    for(Tweets.Statuses statuses: response.body().getStatuses()) {
                        Post post = new Post(statuses.getIdStr(),
                                statuses.getText(),
                                statuses.getUser().getIdStr(),
                                statuses.getUser().getName(),
                                statuses.getUser().getScreenName(),
                                statuses.getUser().getProfileImageUrlHttps()
                        );
                        posts.add(post);
                    }
                    intent.putParcelableArrayListExtra(SEARCH_TWEETS_CALL,  posts);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Tweets> call, @NotNull Throwable t) {
                Log.d(SEARCH_TWEETS_CALL, t.getMessage());
            }
        });

    }

}
