package com.petros.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.List;

public class Tweets {
    private List<Statuses> statuses;
    private SearchMetadata searchMetadata;

    public Tweets() {
    }


    public class Statuses {
        private String createdAt;
        private long id;
        private String idStr;
        private String text;
        private boolean truncated;
        private Entities entities;
        private ExtendedEntities extendedEntities;
        private Metadata metadata;
        private String source;
        private long inReplyToStatusId;
        private String inReplyToStatusIdStr;
        private long inReplyToUserId;
        private String inReplyToUserIdStr;
        private String inReplyToScreenName;
        private User user;
        private Coordinates coordinates;
        private Places place;
        private long quotedStatusId;
        private String quotedStatusIdStr;
        private boolean isQuoteStatus;
        private Statuses quotedStatus;
        private Statuses retweetedStatus;
        private Integer quoteCount;
        private int reply_count;
        private int retweetCount;
        private Integer favoriteCount;
        private boolean favorited;
        private boolean retweeted;
        private boolean possiblySensitive;
        private String filterLevel;
        private String lang;
        private String geo;

        public Statuses() {
        }

        public class Entities {
            private List<Hashtags> hashtags;
            private List<Media> media;
            private List<URLs> urls;
            private List<UserMentions> userMentions;
            private List<Symbols> symbols;


            public class Hashtags {
                private int[] indices;
                private String text;
            }

            public class Media {
                private String displayUrl;
                private String expandedUrl;
                private long id;
                private String idStr;
                private int[] indices;
                private String mediaUrl;
                private String mediaUrlHttps;
                private Sizes sizes;
                private long sourceStatusId;
                private String sourceStatusIdStr;
                private String type;
                private String url;


                public class Sizes {
                    public Size thumb;
                    public Size large;
                    public Size medium;
                    public Size small;

                    public class Size{
                        private int w;
                        private int h;
                        private String resize;
                    }

                }
            }
            public class URLs{
                private String displayUrl;
                private String expandedUrl;
                private int[] indices;
                private String url;

            }

            public class UserMentions {
                private long id;
                private String idStr;
                private int[] indices;
                private String name;
                private String screenName;
            }

            private class Symbols {
                private int[] indices;
                private String text;
            }
        }

        public class Metadata {
        }

        public class User {
            private long id;
            private String idStr;
            private String name;
            private String screenName;
            private String location;
            private String url;
            private String description;
            @SerializedName("protected")
            private boolean a;
            private boolean verified;
            private int followersCount;
            private int friendsCount;
            private int listedCount;
            private int statusesCount;
            private String createdAt;
            private String profileBannerUrl;
            private String profileImageUrlHttps;
            private boolean defaultProfile;
            private boolean defaultProfileImage;
            private String[] withheldInCountries;
            private String withheldScope;
        }

        public class Coordinates {
            private Collection<Float> coordinates;
            private String type;
        }

        public class Places {
            private String id;
            private String url;
            private String placeType;
            private String name;
            private String fullName;
            private String countryCode;
            private String country;
            private BoundingBox boundingBox;

            private class BoundingBox {
                private Float[][][] coordinates;
                private String type;
            }
        }

        private class ExtendedEntities {
            private Entities.Media media;
        }
    }

    public class SearchMetadata {
        private String completedIn;
        private int maxId;
        private String maxIdStr;
        private String nextResults;
        private String query;
        private int count;
        private int sinceId;
        private String sinceIdStr;
    }
}
