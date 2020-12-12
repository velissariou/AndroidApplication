package com.petros.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    @GET("trends/place.json?id=1")
    Call<List<TrendsList>> getTrendsList();

}
