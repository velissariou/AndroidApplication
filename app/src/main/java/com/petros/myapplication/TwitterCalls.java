package com.petros.myapplication;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitterCalls {
    private String URL;
    private OkHttpClient okHttpClient;
    private List<TrendsList> trendsLists = new ArrayList<TrendsList>();

    private static final String TRENDS_CALL = "Trends_Call_Debug";

    public TwitterCalls(String URL, OkHttpClient okHttpClient) {
        this.URL = URL;
        this.okHttpClient = okHttpClient;
    }

    public List<TrendsList> callTrends(Retrofit retrofit){
        API twitterAPI = retrofit.create(API.class);
        Call<List<TrendsList>> trendsListCall = twitterAPI.getTrendsList();
        trendsListCall.enqueue(new Callback<List<TrendsList>>() {


            @Override
            public void onResponse(Call<List<TrendsList>> call, Response<List<TrendsList>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TRENDS_CALL, "Response not successful"+ response.code()+"");
                    return;
                }
                Log.d(TRENDS_CALL, "Got JSON OBJECT"+response.body().toString());
                trendsLists = response.body();
            }

            @Override
            public void onFailure(Call<List<TrendsList>> call, Throwable t) {
                Log.d(TRENDS_CALL, "Call failed:"+t.getMessage().toString());
            }
        });
        return trendsLists;
    }
}
