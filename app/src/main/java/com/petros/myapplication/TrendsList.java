package com.petros.myapplication;

import java.util.List;

public class TrendsList {
    private List<Trends> trends;
    private String asOf;
    private String createdAt;
    private List<Locations> locations;

    public TrendsList(List<Trends> trends, String asOf, String createdAt, List<Locations> locations) {
        this.trends = trends;
        this.asOf = asOf;
        this.createdAt = createdAt;
        this.locations = locations;
    }

    public List<Trends> getTrends() {
        return trends;
    }

    public String getAsOf() {
        return asOf;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<Locations> getLocations() {
        return locations;
    }

    public class Trends {
        private String name;
        private String url;
        private String promotedContent;
        private String query;
        private int tweetVolume;

        public Trends(String name, String url, String promotedContent, String query, int tweetVolume) {
            this.name = name;
            this.url = url;
            this.promotedContent = promotedContent;
            this.query = query;
            this.tweetVolume = tweetVolume;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getPromotedContent() {
            return promotedContent;
        }

        public String getQuery() {
            return query;
        }

        public int getTweetVolume() {
            return tweetVolume;
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
