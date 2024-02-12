package com.example.eventfinder.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventfinder.Activity.EventActivity;
import com.example.eventfinder.Adapter.KeywordSearchAdapter;
import com.example.eventfinder.Listener.OnFragmentInteractionListener;
import com.example.eventfinder.MainActivity;
import com.example.eventfinder.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;
    private String[] category = {"All", "Music", "Sports", "Arts & Theatre", "Film", "Miscellaneous"};
    private EditText distance, location;
    private AutoCompleteTextView keyword;
    private Spinner categorySpinner;
    private SwitchCompat locationSwitch;
    private Context mContext;
    private Button search, clear;
    private String selectedValue;
    LocationManager locationManager;
    double latitude, longitude;

    SharedPreferences preferences;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View currentView = inflater.inflate(R.layout.fragment_search, container, false);

        preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);


        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // This method will be called when the location changes
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                // Do something with the latitude and longitude
            }
        };

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            // Permission is already granted
            // Perform the operation that requires this permission
        } else {
            // Permission is not granted yet, ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, 1001);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        Log.e("Location", latitude + "," + longitude);

        keyword = currentView.findViewById(R.id.keyword);
        distance = currentView.findViewById(R.id.distance);
        location = currentView.findViewById(R.id.location);

        search = currentView.findViewById(R.id.search);
        search.setBackgroundColor(Color.argb(255,80,195,27));

        clear = currentView.findViewById(R.id.clear);
        clear.setBackgroundColor(Color.argb(255,236,116,46));

        categorySpinner = currentView.findViewById(R.id.spinner);
        categorySpinner.setOnItemSelectedListener(this);

        ArrayAdapter ad = new ArrayAdapter(
                mContext,
                android.R.layout.simple_spinner_item,
                category);

        ad.setDropDownViewResource(R.layout.spinner);

        categorySpinner.setAdapter(ad);

        locationSwitch = currentView.findViewById(R.id.categorySwitch);

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    location.setText(latitude + "" + longitude);
                    locationSwitch.getThumbDrawable().setColorFilter(getResources().getColor(R.color.toolbar_text), PorterDuff.Mode.MULTIPLY);
                    locationSwitch.getTrackDrawable().setColorFilter(getResources().getColor(R.color.track), PorterDuff.Mode.MULTIPLY);
                    location.setVisibility(View.GONE);
                }
                else {
                    locationSwitch.getThumbDrawable().setColorFilter(getResources().getColor(R.color.switch_thumb), PorterDuff.Mode.MULTIPLY);
                    locationSwitch.getTrackDrawable().setColorFilter(getResources().getColor(R.color.switch_track), PorterDuff.Mode.MULTIPLY);
                    location.setVisibility(View.VISIBLE);
                }
            }
        });

        keyword.setAdapter(new KeywordSearchAdapter(mContext, android.R.layout.simple_list_item_1));

        keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedValue = (String) parent.getItemAtPosition(position);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyword.getText().toString().isEmpty() || distance.getText().toString().isEmpty() || location.getText().toString().isEmpty()){
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);

                    Snackbar snackbar = Snackbar.make(currentView, "Please fill all fields", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {

                    //mListener.changeFragment(1);
                    Intent intent = new Intent(getActivity(), EventActivity.class);
                    startActivity(intent);

                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("keyword", keyword.getText().toString());
                    editor.putString("distance", distance.getText().toString());
                    editor.putBoolean("locationSwitch", locationSwitch.isChecked());
                    if (locationSwitch.isChecked()) {
                        editor.putString("latitude", String.valueOf(latitude));
                        editor.putString("longitude", String.valueOf(longitude));
                    }
                    editor.putString("location", location.getText().toString());

                    editor.apply();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword.setText("");
                distance.setText("10");
                categorySpinner.setSelection(0);
                location.setText("");
                locationSwitch.setChecked(false);
            }
        });

        return currentView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        TextView selectedText = (TextView) parent.getChildAt(0);
        selectedText.setTextColor(Color.WHITE);
        SharedPreferences.Editor editor = preferences.edit();
        switch (selectedItem) {
            case "All":
                selectedItem = "Default";
                break;
            case "Music":
                selectedItem = "Music";
                break;
            case "Sports":
                selectedItem = "Sports";
                break;
            case "Arts & Theatre":
                selectedItem = "ArtsNTheatre";
                break;
            case "Film":
                selectedItem = "Film";
                break;
            case "Miscellaneous":
                selectedItem = "";
                break;
            default:
                break;
        }

        editor.putString("category", selectedItem);
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }
}