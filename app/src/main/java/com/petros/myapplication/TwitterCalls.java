package com.petros.myapplication;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class TwitterCalls {
    private API twitterAPI;
    private static final String TRENDS_CALL = "Trends_Call_Debug";


    public TwitterCalls(API twitterAPI) {
        this.twitterAPI = twitterAPI;
    }

    public List<TrendsList> callTrends(API twitterAPI) {
        final List<TrendsList> trendsLists = new ArrayList<>();
        Call<List<TrendsList>> call = twitterAPI.getTrendsList();

        call.enqueue(new Callback<List<TrendsList>>() {
            @Override
            public void onResponse(Call<List<TrendsList>> call, Response<List<TrendsList>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TRENDS_CALL, response.message());
                } else {
                    for (TrendsList trendsList : response.body()) {
                        if (response.body() == null) {
                            throw new AssertionError();
                        }
                        Log.d(TRENDS_CALL, "Received JSON file.");
                        trendsLists.add(trendsList);
                    }
                }
            }
            @Override
            public void onFailure (Call<List<TrendsList>> call, Throwable t){
                Log.d(TRENDS_CALL, t.getMessage());
            }
        });
        return  trendsLists;
    }

}
