package com.example.app_sample.ui.profile.cookbooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.app_sample.R;
import com.example.app_sample.data.remote.FirebaseManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
public class NewCookbookFragment extends Fragment {

    private MaterialButton create;
    private TextInputLayout inputLayout;
    private TextInputEditText editText;
    private Toolbar toolbar;
    private String name;

    public NewCookbookFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_new_cookbook, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        create = view.findViewById(R.id.create);
        inputLayout = view.findViewById(R.id.il_name);
        editText = view.findViewById(R.id.et_name);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        create.setOnClickListener(v -> {
            if (checkName()) {
                create.setEnabled(false);

                new FirebaseManager().createCookbook(toCaps(name));
                requireActivity().onBackPressed();

            }
        });

        return view;
    }

    private boolean checkName() {
        name = Objects.requireNonNull(editText.getText()).toString();
        inputLayout.setError(name.isEmpty() ? "Field required" : null);
        return !name.isEmpty();
    }

    private String toCaps(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

}