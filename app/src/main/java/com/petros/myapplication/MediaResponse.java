package com.petros.myapplication;


public class MediaResponse {
    private Integer mediaId;
    private String mediaIdString;
    private String mediaKey;
    private int size;
    private int expiresAfterSecs;
    private Image image;

    public MediaResponse(){

    }

    public Integer getMediaId() {
        return mediaId;
    }

    public String getMediaIdString() {
        return mediaIdString;
    }

    public class Image {
        private String imageType;
        private int w;
        private int h;
    }
}
