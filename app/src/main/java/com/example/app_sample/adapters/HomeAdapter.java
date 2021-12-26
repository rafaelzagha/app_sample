package com.example.app_sample.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.app_sample.ui.home.DiscoverFragment;
import com.example.app_sample.ui.home.SwipeFragment;

import java.util.ArrayList;

public class HomeAdapter extends FragmentStateAdapter {

    ArrayList<Fragment> fragments;

    public HomeAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle ) {
        super(fragmentManager, lifecycle);
        fragments = new ArrayList<>();
        fragments.add(new SwipeFragment());
        fragments.add(new DiscoverFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
