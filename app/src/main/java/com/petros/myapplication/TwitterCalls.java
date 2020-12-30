package com.petros.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class TwitterCalls {
    private API twitterAPI;
    private static final String TRENDS_CALL = "Trends_Call_Debug";


    public TwitterCalls(API twitterAPI) {
        this.twitterAPI = twitterAPI;
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
                    intent.putStringArrayListExtra(MainActivity.TRENDS_LIST,trendsList);
                    context.startActivity(intent);
                }
            }
            @Override
            public void onFailure (@NotNull Call<List<TrendsList>> call, @NotNull Throwable t){
                Log.d(TRENDS_CALL, t.getMessage());
            }
        });
    }

}
