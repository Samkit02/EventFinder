package com.example.eventfinder.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventfinder.Adapter.EventPageAdapter;
import com.example.eventfinder.Adapter.PagerAdapter;
import com.example.eventfinder.Adapter.ResultsAdapter;
import com.example.eventfinder.Client.RetrofitClient;
import com.example.eventfinder.Fragment.ArtistFragment;
import com.example.eventfinder.Fragment.EventDetailsFragment;
import com.example.eventfinder.Fragment.VenueFragment;
import com.example.eventfinder.Model.Event;
import com.example.eventfinder.Model.EventDetailsModel;
import com.example.eventfinder.R;
import com.example.eventfinder.Util.EventSpinner;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity implements ResultsAdapter.ItemClicks {

    private List<Event> favorites;
    private Event events;
    Gson gson;
    SharedPreferences mPrefs;
    private TextView eventName;
    private String id;
    private EventSpinner eventSpinner;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ImageButton back, fb, twitter, favv;
    private int[] tabIcons = {
            R.drawable.info_icon,
            R.drawable.artist_icon,
            R.drawable.venue_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        mPrefs = EventDetailsActivity.this.getSharedPreferences("pref", Context.MODE_PRIVATE);

        gson = new Gson();

        events = gson.fromJson(getIntent().getStringExtra("event"), Event.class);
        favorites = getFavorites();

        getEvents();

        eventName = findViewById(R.id.eventName);
        tabLayout = findViewById(R.id.event_tab_layout);
        viewPager2 = findViewById(R.id.event_view_pager);

        back = findViewById(R.id.backToSearch);
        fb = findViewById(R.id.fb);
        twitter = findViewById(R.id.twitter);
        favv = findViewById(R.id.eventFav);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        eventName.setSelected(true);

        back.setBackgroundResource(0);
        fb.setBackgroundResource(0);
        twitter.setBackgroundResource(0);
        favv.setBackgroundResource(0);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = EventDetailsActivity.this.getSharedPreferences("pref", Context.MODE_PRIVATE);
                String url = preferences.getString("buyTicketsAt", "");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, url);

                boolean facebookAppFound = false;
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                        intent.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }

                if (!facebookAppFound) {
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }

                startActivity(intent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = EventDetailsActivity.this.getSharedPreferences("pref", Context.MODE_PRIVATE);
                String name = preferences.getString("name","");
                String url = "Check out" + name + "on TicketMaster!" + preferences.getString("buyTicketsAt", "");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, url);

                boolean twitterAppFound = false;
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter.android")) {
                        intent.setPackage(info.activityInfo.packageName);
                        twitterAppFound = true;
                        break;
                    }
                }

                if (!twitterAppFound) {
                    String sharerUrl = "https://twitter.com/intent/tweet?text=" + url;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }

                startActivity(intent);
            }
        });

        EventPageAdapter eventPageAdapter = new EventPageAdapter(getSupportFragmentManager(), getLifecycle());
        eventPageAdapter.addFragment(new EventDetailsFragment());
        eventPageAdapter.addFragment(new ArtistFragment());
        eventPageAdapter.addFragment(new VenueFragment());
        viewPager2.setAdapter(eventPageAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    switch (position){
                        case 0:
                            tab.setText("DETAILS");
                            break;
                        case 1:
                            tab.setText("ARTIST(S)");
                            break;
                        case 2:
                            tab.setText("VENUE");
                            break;
                    }
                });
        tabLayoutMediator.attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab selection
                int tabIconColor = ContextCompat.getColor(EventDetailsActivity.this, R.color.toolbar_text);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                tabLayout.invalidate();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselection
                int tabIconColor = ContextCompat.getColor(EventDetailsActivity.this, R.color.white);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                tabLayout.invalidate();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselection
            }
        });

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        if(events.isFavorite()){
            favv.setImageDrawable(favv.getContext().getDrawable(R.drawable.favorite_24));
        }else{
            favv.setImageDrawable(favv.getContext().getDrawable(R.drawable.favorite_border_24));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getEvents() {
        showProgress(true);

        Map<String, String> data = new HashMap<>();

        Intent i = getIntent();
        id = i.getStringExtra("id");
        data.put("id", id);

        Call<EventDetailsModel> apiCall = RetrofitClient.getInstance().getApIs().getEventsDetailsTable(data);
        Log.e("apicall", apiCall.toString());
        apiCall.enqueue(new Callback<EventDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<EventDetailsModel> call, @NonNull Response<EventDetailsModel> response) {
                EventDetailsModel events = response.body();
                setAdapter(events);
            }

            @Override
            public void onFailure(Call<EventDetailsModel> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.e("msg", t.getMessage());
            }
        });
    }

    private void setAdapter(EventDetailsModel events) {
        SharedPreferences preferences = EventDetailsActivity.this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        eventName.setText(events.getName());
        editor.putString("id", id);
        editor.putString("name", events.getName());
        editor.putString("artist", events.getArtist());
        editor.putString("venue", events.getVenue());
        editor.putString("date", events.getDate());
        editor.putString("time", events.getTime());
        editor.putString("genre", events.getGenre());
        editor.putString("priceRange", events.getMinPrice() + " - " + events.getMaxPrice() + " (USD)");
        editor.putString("ticketStatus", events.getStatus());
        editor.putString("buyTicketsAt", events.getUrl());
        editor.putString("eventImage", events.getSeatMap());

        showProgress(false);

        //venue data
        editor.putString("venueName", events.getVenueData().getName());
        editor.putString("venueAdd", events.getVenueData().getAddress());
        editor.putString("venueCity", events.getVenueData().getCity() + "," + events.getVenueData().getState());
        editor.putString("venueContact", events.getVenueData().getPhoneNo());
        editor.putString("childRules", events.getVenueData().getChildRules());
        editor.putString("generalRules", events.getVenueData().getGeneralRules());
        editor.putString("openHours", events.getVenueData().getOpenHours());
        editor.putString("markerLat", String.valueOf(events.getVenueData().getCenter().getLat()));
        editor.putString("markerLong", String.valueOf(events.getVenueData().getCenter().getLng()));

        editor.apply();
    }

    private void showProgress(boolean show){
        if(eventSpinner==null)
            eventSpinner = new EventSpinner();

        if(show)
            eventSpinner.show(getSupportFragmentManager(), EventDetailsActivity.class.getName());
        else
            eventSpinner.dismissAllowingStateLoss();

    }

    @Override
    public void likeClick(int position) {
        events.isFavorite = true;
        addToFavorites(events);
        //resultsAdapter.notifyDataSetChanged();
    }

    @Override
    public void dislikeClick(int position) {
        events.isFavorite = false;
        removeFromFavorites(events);
        //resultsAdapter.notifyDataSetChanged();
    }

    void addToFavorites(Event event) {
        List<Event> events = getFavorites();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        events.add(event);
        String json = gson.toJson(events);
        prefsEditor.putString("Favorites", json);
        prefsEditor.apply();
        showToast(event.getName() + " added to favorites");
    }

    void removeFromFavorites(Event event) {
        List<Event> events = getFavorites();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId().equals(event.getId())) {
                events.remove(i);
                break;
            }
        }
        String json = gson.toJson(events);
        prefsEditor.putString("Favorites", json);
        prefsEditor.apply();
        showToast(event.getName() + " removed from favorites");
    }

    List<Event> getFavorites() {
        List<Event> empty = new ArrayList<>();
        String json = mPrefs.getString("Favorites", gson.toJson(empty));
        return gson.fromJson(json, new TypeToken<List<Event>>() {}.getType());
    }

    Set<String> getFavoriteIds() {
        List<Event> favs = getFavorites();
        List<String> ids = new ArrayList<>();
        for (Event e : favs) {
            ids.add(e.getId());
        }
        return new HashSet<>(ids);
    }

    private void showToast(String msg) {
        if (msg == null) {
            msg = "Something went wrong!!";
        }
        Toast.makeText(EventDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}