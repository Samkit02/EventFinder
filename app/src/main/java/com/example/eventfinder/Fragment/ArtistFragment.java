package com.example.eventfinder.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventfinder.Activity.EventActivity;
import com.example.eventfinder.Activity.EventDetailsActivity;
import com.example.eventfinder.Adapter.ArtistAdapter;
import com.example.eventfinder.Adapter.ResultsAdapter;
import com.example.eventfinder.Client.RetrofitClient;
import com.example.eventfinder.Model.EventDetailsModel;
import com.example.eventfinder.R;
import com.example.eventfinder.Util.EventSpinner;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistFragment extends Fragment {

    private EventSpinner eventSpinner;
    private RecyclerView artistRecyclerView;
    private TextView no_artists;
    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getEvents();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist, container, false);


        artistRecyclerView = view.findViewById(R.id.artistRecyclerView);

        no_artists = view.findViewById(R.id.no_artists);

        return view;
    }

    private void getEvents() {
        showProgress(true);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        Map<String, String> data = new HashMap<>();

        String id = preferences.getString("id","");
        data.put("id", id);

        Call<EventDetailsModel> apiCall = RetrofitClient.getInstance().getApIs().getEventsDetailsTable(data);
        Log.e("apicall", apiCall.toString());
        apiCall.enqueue(new Callback<EventDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<EventDetailsModel> call, @NonNull Response<EventDetailsModel> response) {
                showProgress(false);
                EventDetailsModel events = response.body();

                if (events != null) {
                    artistRecyclerView.setVisibility(View.VISIBLE);
                    no_artists.setVisibility(View.GONE);
                    artistRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    ArtistAdapter resultsAdapter = new ArtistAdapter(events, getActivity());
                    artistRecyclerView.setAdapter(resultsAdapter);
                }
                else {
                    artistRecyclerView.setVisibility(View.GONE);
                    no_artists.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<EventDetailsModel> call, Throwable t) {
                Log.e("msg", t.getMessage());
            }
        });
    }

    private void showProgress(boolean show){
        if(eventSpinner==null)
            eventSpinner = new EventSpinner();

        if(show)
            eventSpinner.show(getChildFragmentManager(), EventDetailsActivity.class.getName());
        else
            eventSpinner.dismissAllowingStateLoss();

    }
}