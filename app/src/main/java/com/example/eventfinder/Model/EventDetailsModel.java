package com.example.eventfinder.Model;

import java.io.Serializable;
import java.util.List;

public class EventDetailsModel implements Serializable {
    private String id;
    private String name;
    private String url;
    private String date;
    private String artist;
    private String time;
    private String venue;
    private String genre;
    private String status;
    private int minPrice;
    private int maxPrice;
    private String currency;
    private String seatMap;
    private VenueData venueData;

    private List<Spotify> spotify;

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public String getArtist() {
        return artist;
    }

    public String getVenue() {
        return venue;
    }

    public String getGenre() {
        return genre;
    }

    public String getStatus() {
        return status;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public String getSeatMap() {
        return seatMap;
    }

    public VenueData getVenueData() {
        return venueData;
    }

    public List<Spotify> getSpotify() { return spotify; }

    public void setSpotify(List<Spotify> value) { this.spotify = value; }

    public class VenueData {
        private String name;
        private String address;
        private String city;
        private String state;
        private String phoneNo;
        private String openHours;
        private String generalRules;
        private String childRules;
        private Center center;

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public String getOpenHours() {
            return openHours;
        }

        public String getGeneralRules() {
            return generalRules;
        }

        public String getChildRules() {
            return childRules;
        }

        public Center getCenter() {
            return center;
        }
    }

    public class Center {
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }

    public class Spotify {
        private Artist artist;
        private List<Album> album;

        public Artist getArtist() { return artist; }

        public void setArtist(Artist value) { this.artist = value; }

        public List<Album> getAlbum() { return album; }

        public void setAlbum(List<Album> value) { this.album = value; }
    }

    public class Album {
        private String id;
        private String img;

        public String getID() { return id; }

        public void setID(String value) { this.id = value; }

        public String getImg() { return img; }

        public void setImg(String value) { this.img = value; }
    }

    public class Artist {
        private String url;
        private String id;
        private String img;
        private String name;
        private long popularity;
        private String followers;

        public String getURL() { return url; }

        public void setURL(String value) { this.url = value; }

        public String getID() { return id; }

        public void setID(String value) { this.id = value; }

        public String getImg() { return img; }

        public void setImg(String value) { this.img = value; }

        public String getName() { return name; }

        public void setName(String value) { this.name = value; }

        public long getPopularity() { return popularity; }

        public void setPopularity(long value) { this.popularity = value; }

        public String getFollowers() { return followers; }

        public void setFollowers(String value) { this.followers = value; }
    }
}
