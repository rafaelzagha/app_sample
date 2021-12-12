package com.example.app_sample;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.app_sample.adapters.GridAdapter;
import com.example.app_sample.adapters.TestAdapter;
import com.example.app_sample.adapters.ViewPagerAdapter;
import com.example.app_sample.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {

    ViewPager vp;
    ViewPagerAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vp = view.findViewById(R.id.vp);
        adapter = new ViewPagerAdapter(requireContext(), Utils.getCategories());
        vp.setAdapter(adapter);

        vp.setClipToPadding(false);
        vp.setPageMargin(30);
        vp.setPadding(60,0,60,10);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }
}