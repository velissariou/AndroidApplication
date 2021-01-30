package com.petros.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private String id;
    private String caption;
    private String mediaUrl;
    private String mediaType;
    private String likeCount;
    private String commentsCount;
    private String timestamp;

    public Post(String id, String caption, String mediaUrl, String mediaType, String likeCount, String commentsCount, String timestamp) {
        this.id = id;
        this.caption = caption;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.likeCount = likeCount;
        this.commentsCount = commentsCount;
        this.timestamp = timestamp;
    }

    public Post() {
    }

    protected Post(Parcel in) {
        id = in.readString();
        caption = in.readString();
        mediaUrl = in.readString();
        mediaType = in.readString();
        likeCount = in.readString();
        commentsCount = in.readString();
        timestamp = in.readString();
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

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static Creator<Post> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(caption);
        dest.writeString(mediaUrl);
        dest.writeString(mediaType);
        dest.writeString(likeCount);
        dest.writeString(commentsCount);
        dest.writeString(timestamp);
    }
}
