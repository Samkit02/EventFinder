package com.example.eventfinder.Client;

import com.example.eventfinder.Model.Event;
import com.example.eventfinder.Model.EventDetailsModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface APIs {
    String BASE_URL = "https://assignment8-382521.wl.r.appspot.com";

    @GET("newShowEventTable")
    Call<List<Event>> getEventsTable(@QueryMap Map<String, String> params);

    @GET("newShowEventDetails")
    Call<EventDetailsModel> getEventsDetailsTable(@QueryMap Map<String, String> params);
}
