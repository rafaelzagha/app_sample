package com.example.app_sample.ui.home.swipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.api.ApiResponse;
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

        viewModel = ViewModelProviders.of(this).get(SwipeViewModel.class);
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

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReloadVisibility(false);
                setLoadingVisibility(true);
                csv.setVisibility(View.VISIBLE);
                viewModel.newRequest().observe(getViewLifecycleOwner(), new Observer<ApiResponse<Recipes>>() {
                    @Override
                    public void onChanged(ApiResponse<Recipes> recipesApiResponse) {
                        if(recipesApiResponse != null){
                            setLoadingVisibility(false);
                            cardStackAdapter.setRecipes(recipesApiResponse.body.getRecipes());
                        }
                    }
                });
            }
        });


        viewModel.newRequest().observe(getViewLifecycleOwner(), new Observer<ApiResponse<Recipes>>() {
            @Override
            public void onChanged(ApiResponse<Recipes> recipesApiResponse) {
                if(recipesApiResponse != null){
                    setReloadVisibility(false);
                    setLoadingVisibility(false);
                    cardStackAdapter.setRecipes(recipesApiResponse.body.getRecipes());
                }
            }
        });


        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csv.setVisibility(View.VISIBLE);
                csv.rewind();
            }
        });

        //Todo: add onclicks for the three buttons

        
        //cardStackAdapter.getRecipe(cardStackLayoutManager.getTopPosition())
    }

    public void setReloadVisibility(boolean set){
        if(set != (loadMore.getVisibility() == View.VISIBLE)){
            loadMore.setVisibility(set?View.VISIBLE:View.INVISIBLE);
            loadMore.animate().alpha(set?1:0).setDuration(500);
        }
    }

    public void setLoadingVisibility(boolean set){
        if(set != (indicator.getVisibility() == View.VISIBLE)){
            indicator.setVisibility(set?View.VISIBLE:View.INVISIBLE);
        }
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
        if(cardStackLayoutManager.getTopPosition() == cardStackAdapter.getItemCount() ){
            csv.setVisibility(View.INVISIBLE);
            setReloadVisibility(true);
        }
    }

    @Override
    public void onCardRewound() {

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