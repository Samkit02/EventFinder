package com.example.eventfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eventfinder.Adapter.PagerAdapter;
import com.example.eventfinder.Fragment.ResultsFragment;
import com.example.eventfinder.Fragment.SearchFragment;
import com.example.eventfinder.Listener.OnFragmentInteractionListener;
import com.example.eventfinder.Util.EventSpinner;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private EventSpinner eventSpinner;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private static final int PERMISSION_REQUEST_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);

        PagerAdapter pagerAdapter = new PagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    switch (position){
                        case 0:
                            tab.setText("SEARCH");
                            break;
                        case 1:
                            tab.setText("FAVORITES");
                            break;
                    }
                }).attach();
    }

    public void changeFragment(int id){
        if (id == 1) {
            ResultsFragment rf = new ResultsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.relativeResultLayout, rf);
            ft.commit();
        }
        if (id == 2) {
            SearchFragment sf = new SearchFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.relativeResultLayout, new SearchFragment());
            ft.commit();
        }
    }

    public void showProgress(boolean show){
        if(eventSpinner==null)
            eventSpinner = new EventSpinner();

        if(show)
            eventSpinner.show(getSupportFragmentManager(), MainActivity.class.getName());
        else
            eventSpinner.dismissAllowingStateLoss();

    }
}