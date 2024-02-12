package com.example.eventfinder.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventfinder.Activity.EventDetailsActivity;
import com.example.eventfinder.R;

public class EventDetailsFragment extends Fragment {

    private TextView artistTeam, venue, date, time, genre, priceRange, ticketStatus, buyTicketsAt;
    private ImageView eventImage;
    SharedPreferences preferences;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        artistTeam = view.findViewById(R.id.artistTeam);
        venue = view.findViewById(R.id.venue);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        genre = view.findViewById(R.id.genre);
        priceRange = view.findViewById(R.id.priceRange);
        ticketStatus = view.findViewById(R.id.ticketStatus);
        buyTicketsAt = view.findViewById(R.id.buyTicketsAt);

        eventImage = view.findViewById(R.id.eventImage);

        artistTeam.setSelected(true);
        genre.setSelected(true);
        buyTicketsAt.setSelected(true);

        buyTicketsAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(preferences.getString("buyTicketsAt","")));
                startActivity(browserIntent);
            }
        });

        artistTeam.setText(preferences.getString("artist",""));
        venue.setText(preferences.getString("venue",""));
        date.setText(preferences.getString("date",""));
        time.setText(preferences.getString("time",""));
        genre.setText(preferences.getString("genre",""));
        priceRange.setText(preferences.getString("priceRange",""));
        ticketStatus.setText(preferences.getString("ticketStatus",""));
        SpannableString content = new SpannableString(preferences.getString("buyTicketsAt",""));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        buyTicketsAt.setText(content);

        Glide.with(this).
                load(preferences.getString("eventImage","")).placeholder(R.drawable.baseline_person_24).
                error(R.drawable.baseline_person_24).into(eventImage);

        checkStatus();

        return view;
    }

    private void checkStatus() {
        if (ticketStatus.getText().toString().equals("offsale")){
            ticketStatus.setBackgroundResource(R.drawable.rounded_corner_off);
        }
        else {
            ticketStatus.setBackgroundResource(R.drawable.rounded_corner);
        }
    }
}