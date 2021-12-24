package com.example.app_sample.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.app_sample.R;
import com.example.app_sample.adapters.GridAdapter;
import com.example.app_sample.adapters.TestAdapter;

public class DiscoverFragment extends Fragment {

    RecyclerView rv, rv2;
    TestAdapter ta, ta2;
    GridView gv;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gv = view.findViewById(R.id.grid);
        rv = view.findViewById(R.id.rv_popular);
        rv2 = view.findViewById(R.id.rv_categories);
        ta = new TestAdapter(getContext(), R.layout.item_popular);
        rv.setAdapter(ta);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);

        //SnapHelper helper = new LinearSnapHelper();
        //helper.attachToRecyclerView(rv);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        ta2 =  new TestAdapter(getContext(), R.layout.item_category);
        rv2.setAdapter(ta2);
        rv2.setLayoutManager(layoutManager2);

        setupGrid();


    }

    private void setupGrid() {
        GridAdapter ga = new GridAdapter(getContext());
        gv.setAdapter(ga);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }
}