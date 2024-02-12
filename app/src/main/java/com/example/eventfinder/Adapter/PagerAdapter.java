package com.example.eventfinder.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eventfinder.Fragment.FavouriteFragment;
import com.example.eventfinder.Fragment.SearchFragment;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SearchFragment();
            case 1:
                return new FavouriteFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
