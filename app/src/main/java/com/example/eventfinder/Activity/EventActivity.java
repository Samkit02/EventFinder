package com.example.eventfinder.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventfinder.Adapter.ResultsAdapter;
import com.example.eventfinder.Client.RetrofitClient;
import com.example.eventfinder.Listener.OnFragmentInteractionListener;
import com.example.eventfinder.MainActivity;
import com.example.eventfinder.Model.Event;
import com.example.eventfinder.R;
import com.example.eventfinder.Util.EventSpinner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity implements ResultsAdapter.ItemClicks {

    private List<Event> events;
    Gson gson;
    SharedPreferences mPrefs;
    ResultsAdapter resultsAdapter;
    private EventSpinner eventSpinner;
    private Context mContext;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private Button back;
    private TextView noEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mPrefs = EventActivity.this.getSharedPreferences("pref", Context.MODE_PRIVATE);

        gson = new Gson();

        events = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        noEvents = findViewById(R.id.no_events);

        getEvents();

        back = findViewById(R.id.back);
        back.setBackgroundResource(0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getEvents() {
        events.clear();
        showProgress(true);
        SharedPreferences preferences = EventActivity.this.getSharedPreferences("pref", Context.MODE_PRIVATE);

        Map<String, String> data = new HashMap<>();

        data.put("keyword", preferences.getString("keyword", "Taylor Swift"));
        data.put("dist", preferences.getString("distance", "10"));
        data.put("cat", preferences.getString("category", "Music"));
        data.put("LOC", preferences.getString("location", ""));
        if (preferences.getBoolean("locationSwitch",false)) {
            data.put("LAT", "34.0522342");
            data.put("LONG", "-118.2436849");
        }
        else {
            data.put("LAT", "");
            data.put("LONG", "");
        }


        Call<List<Event>> apiCall = RetrofitClient.getInstance().getApIs().getEventsTable(data);
        Log.e("apicall", apiCall.toString());
        apiCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                showProgress(false);
                List<Event> events = response.body();
                Log.e("events", Arrays.toString(events.toArray()));
                recyclerView.setVisibility(View.VISIBLE);
                noEvents.setVisibility(View.GONE);
                Set<String> favIds = getFavoriteIds();
                for (int i = 0; i < events.size(); i++) {
                    events.get(i).isFavorite = favIds.contains(events.get(i).getId());
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(EventActivity.this));
                resultsAdapter = new ResultsAdapter(events, EventActivity.this,EventActivity.this);
                recyclerView.setAdapter(resultsAdapter);
                resultsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(EventActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter(List<Event> events) {

    }

    private void showProgress(boolean show){
        if(eventSpinner==null)
            eventSpinner = new EventSpinner();

        if(show)
            eventSpinner.show(getSupportFragmentManager(), EventActivity.class.getName());
        else
            eventSpinner.dismissAllowingStateLoss();

    }

    @Override
    public void likeClick(int position) {
        events.get(position).isFavorite = true;
        addToFavorites(events.get(position));
        resultsAdapter.notifyDataSetChanged();
    }

    @Override
    public void dislikeClick(int position) {
        events.get(position).isFavorite = false;
        removeFromFavorites(events.get(position));
        resultsAdapter.notifyDataSetChanged();
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
        Toast.makeText(EventActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}