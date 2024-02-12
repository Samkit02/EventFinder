package com.example.eventfinder.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventfinder.Activity.EventActivity;
import com.example.eventfinder.Adapter.ResultsAdapter;
import com.example.eventfinder.Model.Event;
import com.example.eventfinder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouriteFragment extends Fragment implements ResultsAdapter.ItemClicks {

    private List<Event> events;
    Gson gson;
    SharedPreferences mPrefs;
    ResultsAdapter resultsAdapter;
    RecyclerView favRecyclerView;
    TextView no_favorites;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        no_favorites = view.findViewById(R.id.no_favorites);
        favRecyclerView = view.findViewById(R.id.favRecyclerView);
        gson = new Gson();
        events = new ArrayList<>();
        mPrefs = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        events.addAll(getFavorites());
        favRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultsAdapter = new ResultsAdapter(events, getActivity(), this);
        favRecyclerView.setAdapter(resultsAdapter);

        if(events.isEmpty()){
            no_favorites.setVisibility(View.VISIBLE);
            favRecyclerView.setVisibility(View.GONE);
        }else{
            no_favorites.setVisibility(View.GONE);
            favRecyclerView.setVisibility(View.VISIBLE);
        }


        return view;
    }

    @Override
    public void likeClick(int position) {

    }

    @Override
    public void dislikeClick(int position) {
        removeFromFavorites(events.get(position));
        events.remove(position);
        resultsAdapter.notifyItemRemoved(position);
        resultsAdapter.notifyDataSetChanged();
        if(events.isEmpty()){
            no_favorites.setVisibility(View.VISIBLE);
            favRecyclerView.setVisibility(View.GONE);
        }
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
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}