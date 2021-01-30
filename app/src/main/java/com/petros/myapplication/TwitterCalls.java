package com.petros.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    private static final String TRENDS_CALL = "Trends Call Debug";
    public static final String SEARCH_TWEETS_CALL = "Search Tweets Debug";
    private static final String POST_MEDIA = "Post media Debug";
    private static final String POST_TWEET = "Post tweet Debug";
    private static final String FACEBOOK_API_TOKEN = BuildConfig.FACEBOOK_API_TOKEN;
    private static final String INSTAGRAM_USER_ID = BuildConfig.INSTAGRAM_USER_ID;
    private static final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/v9.0";
    private static final String fields = "id,caption,media_url,media_type,like_count,comments_count,timestamp";

    public TwitterCalls() {
    }

    public API getAPI(String URL) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(TWITTER_API_KEY, TWITTER_API_SECRET);
        consumer.setTokenWithSecret(TWITTER_API_ACCESS_TOKEN, TWITTER_API_ACCESS_TOKEN_SECRET);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();

        if(URL.contains("twitter")){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit.create(API.class);
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit.create(API.class);
        }

    }

    public void callTrends(Context context, String woeid, String query) {
        API twitterAPI = getAPI(TWITTER_API_URL);
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
                            if(trends.getName().contains(query)){
                                trendsList.add(trends.getName());
                            }
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

    public void searchTweets(Context context,  String query){
        API twitterAPI = getAPI(TWITTER_API_URL);
        API facebookAPI = getAPI(FACEBOOK_GRAPH_API_URL);
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
                    Log.d(SEARCH_TWEETS_CALL, "Received Twitter JSON file.");
                    for(Tweets.Statuses statuses: response.body().getStatuses()) {
                        TwitterPost twitterPost  = new TwitterPost(
                                statuses.getIdStr(),
                                statuses.getText(),
                                statuses.getEntities().getMedia().get(0).getMediaUrlHttps(),
                                statuses.getEntities().getMedia().get(0).getType(),
                                statuses.getFavoriteCount().toString(),
                                String.valueOf(statuses.getReplyCount()),
                                statuses.getCreatedAt()
                        );
                        posts.add(twitterPost);
                    }
                    intent.putParcelableArrayListExtra(SEARCH_TWEETS_CALL,  posts);

                    Call<JSONObject> facebookCall = facebookAPI.getHashtagId(INSTAGRAM_USER_ID, query, FACEBOOK_API_TOKEN);
                    facebookCall.enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(@NotNull Call<JSONObject> call, @NotNull Response<JSONObject> response) {
                            if (!response.isSuccessful()) {
                                Log.d(SEARCH_TWEETS_CALL, response.message());
                            } else {
                                if (response.body() == null) {
                                    throw new AssertionError();
                                }
                                Log.d(SEARCH_TWEETS_CALL, "Received Instagram JSON file.");
                                String hashtagId = null;
                                try {
                                    hashtagId = response.body().getJSONObject("data").getString("id");
                                    Log.d(SEARCH_TWEETS_CALL, "id = " + hashtagId);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Call<List<InstagramPost>> call1 = facebookAPI.getInstagramPosts(hashtagId, INSTAGRAM_USER_ID, fields);
                                call1.enqueue(new Callback<List<InstagramPost>>() {
                                    @Override
                                    public void onResponse(@NotNull Call<List<InstagramPost>> call, @NotNull Response<List<InstagramPost>> response) {
                                        if (!response.isSuccessful()) {
                                            Log.d(SEARCH_TWEETS_CALL, response.message());
                                        } else {
                                            if (response.body() == null) {
                                                throw new AssertionError();
                                            }
                                            Log.d(SEARCH_TWEETS_CALL, "Received Instagram JSON file.");
                                            posts.addAll(response.body());
                                            intent.putParcelableArrayListExtra(SEARCH_TWEETS_CALL, posts);
                                            context.startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<List<InstagramPost>> call, @NotNull Throwable t) {
                                        Log.d(SEARCH_TWEETS_CALL, t.getMessage());
                                    }
                                });
                            }
                        }
                        @Override
                        public void onFailure(@NotNull Call<JSONObject> call, @NotNull Throwable t) {
                            Log.d(SEARCH_TWEETS_CALL, t.getMessage());
                        }
                    });
                }
            }
            @Override
            public void onFailure(@NotNull Call<Tweets> call, @NotNull Throwable t) {
                Log.d(SEARCH_TWEETS_CALL, t.getMessage());
            }
        });
    }

    public void postTweet(String text, Uri uri, Context context){

        if(uri != null){
            File originalFile = new File(getRealPathFromUri(uri, context));
            originalFile.setReadable(true);
            API uploadApi = getAPI(TWITTER_API_URL_MEDIA_UPLOAD);
            RequestBody filePart = RequestBody.create(MediaType.parse("multipart/form-data"), originalFile);
            MultipartBody.Part file = MultipartBody.Part.createFormData("media", originalFile.getName(), filePart);
            Call<MediaResponse> call = uploadApi.getMediaID(file, "tweet_image");
            call.enqueue(new Callback<MediaResponse>() {
                @Override
                public void onResponse(@NotNull Call<MediaResponse> call, @NotNull Response<MediaResponse> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(
                                context, "Unsuccessful request." + response.message(),
                                Toast.LENGTH_SHORT).show();
                        Log.d(POST_MEDIA, response.message());
                    }else{
                        if(response.body() == null) {
                            Toast.makeText(context, "Tweet could not be posted." + response.message(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d(POST_MEDIA, "Empty JSON response");
                            throw new AssertionError();
                        }
                        String mediaId = response.body().getMediaIdString();
                        API twitterAPI = getAPI(TWITTER_API_URL);
                        Call<ResponseBody> aCall = twitterAPI.postTweet(text, mediaId);
                        aCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                if(!response.isSuccessful()){
                                    Toast.makeText(context, "Unsuccessful request." + response.message(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(POST_MEDIA, response.message());
                                }else {
                                    if(response.body() == null) {
                                        Toast.makeText(context, "Tweet could not be posted." + response.message(),
                                                Toast.LENGTH_SHORT).show();
                                        Log.d(POST_MEDIA, "Empty JSON response");
                                        throw new AssertionError();
                                    }
                                    Toast.makeText(context, "Tweet posted successfully!", Toast.LENGTH_SHORT).show();
                                    Log.d(POST_MEDIA, "Tweet posted successfully!");
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                Toast.makeText(context, "Tweet could not be posted." + t.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                Log.d(POST_MEDIA, t.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MediaResponse> call, @NotNull Throwable t) {
                    Toast.makeText(context, "Media could not be uploaded." + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.d(POST_MEDIA, t.getMessage());
                }
            });

        }else{
            API twitterAPI = getAPI(TWITTER_API_URL);
            Call<ResponseBody> call = twitterAPI.postTweet(text, "");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(context, "Tweet could not be posted." + response.message(),
                                Toast.LENGTH_SHORT).show();
                        Log.d(POST_TWEET, response.message());
                    }else {
                        if(response.body() == null) {
                            Toast.makeText(context, "Tweet could not be posted." + response.message(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d(POST_TWEET, "Empty JSON response");
                            throw new AssertionError();
                        }
                        Toast.makeText(context, "Tweet posted successfully!", Toast.LENGTH_SHORT).show();
                        Log.d(POST_TWEET, "Tweet posted successfully!");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(context, "Tweet could not be posted." + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.d(POST_TWEET, t.getMessage());
                }
            });
        }
    }

    public String getRealPathFromUri(Uri uri, Context context){
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
