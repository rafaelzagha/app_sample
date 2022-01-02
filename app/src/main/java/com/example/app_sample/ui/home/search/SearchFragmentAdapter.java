package com.example.app_sample.ui.home.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.app_sample.ui.home.swipe.SwipeFragment;

import java.util.ArrayList;

public class SearchFragmentAdapter extends FragmentStateAdapter {

    ArrayList<Fragment> fragments;

    public SearchFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragments = new ArrayList<>();
        fragments.add(new ResultsFragment());
        fragments.add(new SwipeFragment());
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
