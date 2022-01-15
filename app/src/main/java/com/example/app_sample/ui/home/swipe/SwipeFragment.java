package com.example.app_sample.ui.home.swipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.api.ApiResponse;
import com.example.app_sample.ui.MainActivity;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

public class SwipeFragment extends Fragment implements CardStackListener {

    CardStackView csv;
    CardStackAdapter cardStackAdapter;
    CardStackLayoutManager cardStackLayoutManager;
    SwipeViewModel viewModel;
    CardView loadMore, rewind, favorite, addRecipe;
    CircularProgressIndicator indicator;

    public SwipeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(SwipeViewModel.class);
        loadMore = view.findViewById(R.id.load_more);
        csv = view.findViewById(R.id.csv);
        indicator = view.findViewById(R.id.indicator);
        rewind = view.findViewById(R.id.rewind);
        favorite = view.findViewById(R.id.favorite);
        addRecipe = view.findViewById(R.id.add);

        cardStackAdapter = new CardStackAdapter(requireContext());
        cardStackLayoutManager = new CardStackLayoutManager(requireContext(), this);
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL);
        cardStackLayoutManager.setCanScrollVertical(false);

        csv.setAdapter(cardStackAdapter);
        csv.setLayoutManager(cardStackLayoutManager);

        if (viewModel.getRecipes().getValue() != null && viewModel.getRecipes().getValue().getBody() != null) {
            cardStackAdapter.setRecipes(viewModel.getRecipes().getValue().getBody().getRecipes());
            indicator.setVisibility(View.INVISIBLE);
            cardStackLayoutManager.scrollToPosition(viewModel.getPosition());
        } else
            newRequest();

        loadMore.setOnClickListener(v -> newRequest());

        rewind.setOnClickListener(v -> {
            csv.setVisibility(View.VISIBLE);
            csv.rewind();
        });

        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipesApiResponse -> {
            if (recipesApiResponse != null) {
                indicator.setVisibility(View.INVISIBLE);
                cardStackAdapter.setRecipes(recipesApiResponse.body.getRecipes());
                csv.setVisibility(View.VISIBLE);

            }
        });

    }


    public void newRequest() {
        loadMore.setVisibility(View.INVISIBLE);
        indicator.setVisibility(View.VISIBLE);
        viewModel.newRequest().observe(getViewLifecycleOwner(), recipesApiResponse -> {
            if (recipesApiResponse != null) {
                if (recipesApiResponse.getBody() != null)
                    viewModel.addToRecipes(recipesApiResponse);
                else{
                    Toast.makeText(getActivity(), "Request Error " + recipesApiResponse.getCode(), Toast.LENGTH_SHORT).show();
                    indicator.setVisibility(View.INVISIBLE);
                }

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_swipe, container, false);
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (cardStackLayoutManager.getTopPosition() == cardStackAdapter.getItemCount()) {
            csv.setVisibility(View.INVISIBLE);
            loadMore.setVisibility(View.VISIBLE);
        }
        viewModel.incrementPosition();
    }

    @Override
    public void onCardRewound() {
        viewModel.decreasePosition();
    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }


}