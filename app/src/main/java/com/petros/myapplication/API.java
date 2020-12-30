package com.petros.myapplication;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface API {
    @GET("trends/place.json")
    Call<List<TrendsList>> getTrendsList(@Query("id") String woeid);

    @FormUrlEncoded
    @GET("search/tweets.json")
    Call<Tweets> getTweets(@Query("q") String query);

    @FormUrlEncoded
    @POST("statuses/update.json")
    Call<ResponseBody> postTweet(
            @Query("status") String status,
            @Query("media_ids") Integer mediaID
    );

    @Multipart
    @POST("media/upload.json")
    Call<MediaResponse> getMediaID(
            @Part("media") MultipartBody.Part file,
            @Query("media_category") String type
            );

}
