package com.example.app_sample.ui.home.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

public class SwipeFragment extends Fragment implements CardStackListener {

    MaterialButton retry;
    TextView errorMessage;
    LinearLayout errorLayout;
    CardStackView csv;
    CardStackAdapter cardStackAdapter;
    CardStackLayoutManager cardStackLayoutManager;
    SwipeViewModel viewModel;
    CardView rewind, clear, save;
    CircularProgressIndicator indicator;

    public SwipeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe, container, false);

        viewModel = ViewModelProviders.of(requireActivity()).get(SwipeViewModel.class);
        errorLayout = view.findViewById(R.id.error_layout);
        retry = view.findViewById(R.id.retry);
        errorMessage = view.findViewById(R.id.error_message);
        csv = view.findViewById(R.id.csv);
        indicator = view.findViewById(R.id.indicator);
        rewind = view.findViewById(R.id.rewind);
        clear = view.findViewById(R.id.clear);
        save = view.findViewById(R.id.save);

        cardStackAdapter = new CardStackAdapter(this);
        cardStackLayoutManager = new CardStackLayoutManager(requireContext(), this);
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL);
        cardStackLayoutManager.setCanScrollVertical(false);

        csv.setAdapter(cardStackAdapter);
        csv.setLayoutManager(cardStackLayoutManager);

        SwipeAnimationSetting left = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();

        SwipeAnimationSetting right = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();

        clear.setOnClickListener(v -> {
            if (cardStackLayoutManager.getTopPosition() != cardStackAdapter.getItemCount() - 1) {
                cardStackLayoutManager.setSwipeAnimationSetting(left);
                csv.swipe();
            }
        });

        save.setOnClickListener(v -> {
            if (cardStackLayoutManager.getTopPosition() != cardStackAdapter.getItemCount() - 1) {
                cardStackLayoutManager.setSwipeAnimationSetting(right);
                csv.swipe();
            }

        });

        rewind.setOnClickListener(v -> csv.rewind());


        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipesApiResponse -> {
            if (recipesApiResponse.getRecipes() != null) {
                csv.setVisibility(View.VISIBLE);
                indicator.setVisibility(View.INVISIBLE);
                cardStackAdapter.setRecipes(recipesApiResponse.getRecipes());
                cardStackLayoutManager.scrollToPosition(viewModel.getPosition());
                cardStackLayoutManager.setCanScrollHorizontal(cardStackLayoutManager.getTopPosition() != cardStackAdapter.getItemCount() - 1);
            }

        });

        viewModel.getError().observe(getViewLifecycleOwner(), string -> {
            if(string != null){
                showError(string);
            }
        });

        retry.setOnClickListener(v -> {
            indicator.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.INVISIBLE);
            viewModel.newRequest();
        });

        return view;
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

        if (cardStackLayoutManager.getTopPosition() == cardStackAdapter.getItemCount() - 1)
            cardStackLayoutManager.setCanScrollHorizontal(false);
        else {
            if (direction == Direction.Right)
                viewModel.saveRecipe(cardStackAdapter.getRecipe(viewModel.getPosition()));
            else if (direction == Direction.Left)
                viewModel.deleteRecipe(cardStackAdapter.getRecipe(viewModel.getPosition()).getId());

        }
        viewModel.incrementPosition();

    }

    @Override
    public void onCardRewound() {
        cardStackLayoutManager.setCanScrollHorizontal(true);
        csv.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
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

    public void goToRecipePage(Recipes.Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_recipeFragment, bundle);
    }

    public void loadMore() {
        csv.setVisibility(View.INVISIBLE);
        indicator.setVisibility(View.VISIBLE);
        viewModel.newRequest();
    }

    private void showError(String message) {
        csv.setVisibility(View.INVISIBLE);
        indicator.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }

}