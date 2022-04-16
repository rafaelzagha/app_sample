package com.example.app_sample.ui.profile.cookbooks;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Cookbook;
import com.example.app_sample.ui.profile.ProfileViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;


public class CookbooksFragment extends Fragment {

    private ProfileViewModel viewModel;
    private LinearLayout emptyCard;
    private RecyclerView recyclerView;
    private CookbooksAdapter adapter;
    private MaterialButton create;

    public CookbooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cookbooks, container, false);

        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        adapter = new CookbooksAdapter(this);
        recyclerView = view.findViewById(R.id.recyclerview);
        emptyCard = view.findViewById(R.id.empty);
        create = view.findViewById(R.id.create);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        viewModel.getCookbooks().observe(getViewLifecycleOwner(), new Observer<List<Cookbook>>() {
            @Override
            public void onChanged(List<Cookbook> cookbooks) {
                emptyCard.setVisibility(cookbooks.isEmpty()? View.VISIBLE : View.GONE);
                adapter.setCookbooks(cookbooks);
            }
        });

        create.setOnClickListener(v -> NavHostFragment.findNavController(CookbooksFragment.this).navigate(R.id.action_profileFragment_to_newCookbookFragment));

        return view;
    }

    public LiveData<List<String>> getCookbookImages(String id){
        return viewModel.getCookbookImages(id);
    }
}