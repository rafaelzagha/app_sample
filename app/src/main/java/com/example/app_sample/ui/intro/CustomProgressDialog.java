package com.example.app_sample.ui.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.app_sample.R;

@SuppressWarnings("FieldCanBeLocal")
public class CustomProgressDialog extends DialogFragment {

    private String message;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_progress, container, false);

        this.setCancelable(false);
        textView = view.findViewById(R.id.message);
        textView.setText(message);

        return view;
    }

    public void show(FragmentManager fm, String tag, String message){
        this.message = message;
        super.show(fm, tag);
    }





}
