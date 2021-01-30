package com.petros.myapplication;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface API {
    @GET("trends/place.json")
    Call<List<TrendsList>> getTrendsList(@Query("id") String woeid);

    @GET("search/tweets.json")
    Call<Tweets> getTweets(@Query("q") String query);

    @POST("statuses/update.json")
    Call<ResponseBody> postTweet(
            @Query("status") String status,
            @Query("media_ids") String mediaId);

    @Multipart
    @POST("media/upload.json")
    Call<MediaResponse> getMediaID(
            @Part MultipartBody.Part file,
            @Query("media_category") String type);

    @GET("ig_hashtag_search")
    Call<JSONObject> getHashtagId(@Query("user_id") String userId,
                                  @Query("q") String query,
                                  @Query("access_token") String accessToken);

    @GET("{hashtag_id}/top_media")
    Call<List<InstagramPost>> getInstagramPosts(@Path(value="hashtag_id", encoded=true) String hashtagId,
                                                @Query("user_id") String userId,
                                                @Query("fields") String fields);
}
