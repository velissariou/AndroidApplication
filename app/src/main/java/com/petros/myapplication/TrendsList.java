package com.petros.myapplication;

import java.util.List;

public class TrendsList {
    private List<Trends> trends;
    private String as_of;
    private String created_at;
    private List<Locations> locations;

    public TrendsList(List<Trends> trends, String as_of, String created_at, List<Locations> locations) {
        this.trends = trends;
        this.as_of = as_of;
        this.created_at = created_at;
        this.locations = locations;
    }

    public List<Trends> getTrends() {
        return trends;
    }

    public String getAs_of() {
        return as_of;
    }

    public String getCreated_at() {
        return created_at;
    }

    public List<Locations> getLocations() {
        return locations;
    }

    public class Trends {
        private String name;
        private String url;
        private String promoted_content;
        private String query;
        private int tweet_volume;

        public Trends(String name, String url, String promoted_content, String query, int tweet_volume) {
            this.name = name;
            this.url = url;
            this.promoted_content = promoted_content;
            this.query = query;
            this.tweet_volume = tweet_volume;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getPromoted_content() {
            return promoted_content;
        }

        public String getQuery() {
            return query;
        }

        public int getTweet_volume() {
            return tweet_volume;
        }
    }

    public class Locations {
        private String name;
        private int woeid;

        public Locations(String name, int woeid) {
            this.name = name;
            this.woeid = woeid;
        }

        public String getName() {
            return name;
        }

        public int getWoeid() {
            return woeid;
        }
    }


}
