package com.example.eventfinder.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventfinder.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class VenueFragment extends Fragment implements OnMapReadyCallback {

    private TextView venueName, venueAdd, venueCity, venueContact, openHours, generalRules, childRules;
    private SharedPreferences preferences;
    private GoogleMap gMap;
    String markerLat, markerLong;
    private NestedScrollView nestedView;
    Marker marker;

    public VenueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_venue, container, false);

        venueName = view.findViewById(R.id.venueName);
        venueAdd = view.findViewById(R.id.venueAddress);
        venueCity = view.findViewById(R.id.venueCity);
        venueContact = view.findViewById(R.id.venueContact);
        openHours = view.findViewById(R.id.openHours);
        generalRules = view.findViewById(R.id.generalRules);
        childRules = view.findViewById(R.id.childRules);

        nestedView = view.findViewById(R.id.nestedView);

        venueAdd.setSelected(true);
        venueCity.setSelected(true);
        venueContact.setSelected(true);

        venueName.setText(preferences.getString("venueName",""));
        venueAdd.setText(preferences.getString("venueAdd",""));
        venueCity.setText(preferences.getString("venueCity",""));
        venueContact.setText(preferences.getString("venueContact",""));
        openHours.setText(preferences.getString("openHours",""));
        generalRules.setText(preferences.getString("generalRules",""));
        childRules.setText(preferences.getString("childRules",""));

        generalRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (generalRules.getMaxLines() != Integer.MAX_VALUE)
                    generalRules.setMaxLines(Integer.MAX_VALUE);
                else
                    generalRules.setMaxLines(3);
            }
        });

        childRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childRules.getMaxLines() != Integer.MAX_VALUE)
                    childRules.setMaxLines(Integer.MAX_VALUE);
                else
                    childRules.setMaxLines(3);
            }
        });



        SupportMapFragment mapFragment
                = (SupportMapFragment)
                getChildFragmentManager()
                        .findFragmentById(R.id.googleMaps);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        markerLat = preferences.getString("markerLat", "");
        markerLong = preferences.getString("markerLong", "");
        gMap = googleMap;

        gMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng loc = new LatLng(Double.parseDouble(markerLat), Double.parseDouble(markerLong));

        gMap.addMarker(new MarkerOptions().position(loc).title("Event Location"));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                loc, 15);
        gMap.animateCamera(location);
//        gMap.setOnMapClickListener(latLng -> {
//            String geoUri = "http://maps.google.com/maps?q=loc:" + markerLat + "," + markerLong;
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
//            startActivity(mapIntent);
//        });
//        gMap.setOnMarkerClickListener(marker -> {
//            String geoUri = "http://maps.google.com/maps?q=loc:" + markerLat + "," + markerLong;
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
//            startActivity(mapIntent);
//            return false;
//        });
    }
}