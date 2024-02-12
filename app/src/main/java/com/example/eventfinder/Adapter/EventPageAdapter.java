package com.example.eventfinder.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eventfinder.Fragment.ArtistFragment;
import com.example.eventfinder.Fragment.EventDetailsFragment;
import com.example.eventfinder.Fragment.FavouriteFragment;
import com.example.eventfinder.Fragment.ResultsFragment;
import com.example.eventfinder.Fragment.SearchFragment;
import com.example.eventfinder.Fragment.VenueFragment;

import java.util.ArrayList;

public class EventPageAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> arrayList = new ArrayList<>();

    public EventPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void addFragment(Fragment fragment) {
        arrayList.add(fragment);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return arrayList.get(position);
    }
}
