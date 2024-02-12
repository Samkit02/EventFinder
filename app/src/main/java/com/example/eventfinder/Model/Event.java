package com.example.eventfinder.Model;

public class Event {

    public boolean isFavorite;
    private String data;
    private String id;
    private String name;
    private String url;
    private String localDate;
    private String localTime;
    private String imgUrl;
    private String genre;
    private String venue;

    // Constructor
    public Event(boolean isFavorite, String data, String id, String name, String url, String localDate, String localTime, String imgUrl, String genre, String venue) {
        this.isFavorite = isFavorite;
        this.data = data;
        this.id = id;
        this.name = name;
        this.url = url;
        this.localDate = localDate;
        this.localTime = localTime;
        this.imgUrl = imgUrl;
        this.genre = genre;
        this.venue = venue;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    // Getters and setters
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}