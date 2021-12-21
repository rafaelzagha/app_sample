package com.example.app_sample;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_sample.adapters.CardStackAdapter;
import com.example.app_sample.utils.Utils;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import io.alterac.blurkit.BlurLayout;

public class HomeFragment extends Fragment {

    CardStackView csv;
    CardStackAdapter cardStackAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        csv = view.findViewById(R.id.csv);

        cardStackAdapter = new CardStackAdapter(Utils.getCategories(), requireContext());

        CardStackLayoutManager cardStackLayoutManager =  new CardStackLayoutManager(requireContext());
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL);
        cardStackLayoutManager.setCanScrollVertical(false);
        //cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setStackFrom(StackFrom.Right);

        csv.setAdapter(cardStackAdapter);
        csv.setLayoutManager(cardStackLayoutManager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }
}