package com.petros.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private String id;
    private String text;
    private String userId;
    private String name;
    private String screenName;
    private String profileImageUrlHttps;

    public Post(String id, String text, String userId, String name, String screenName, String profileImageUrlHttps) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.name = name;
        this.screenName = screenName;
        this.profileImageUrlHttps = profileImageUrlHttps;
    }

    public Post() {
    }

    protected Post(Parcel in) {
        id = in.readString();
        text = in.readString();
        userId = in.readString();
        name = in.readString();
        screenName = in.readString();
        profileImageUrlHttps = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrlHttps() {
        return profileImageUrlHttps;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setProfileImageUrlHttps(String profileImageUrlHttps) {
        this.profileImageUrlHttps = profileImageUrlHttps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(screenName);
        dest.writeString(profileImageUrlHttps);
    }
}
