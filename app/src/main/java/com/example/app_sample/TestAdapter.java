package com.example.app_sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private ArrayList<String> words;
    private LayoutInflater layoutInflater;

    public TestAdapter(Context context, ArrayList<String> words) {
        layoutInflater = LayoutInflater.from(context);
        this.words = words;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.recipes_card, parent, false);
        return new TestViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder{

        final TestAdapter testAdapter;

        public TestViewHolder(@NonNull View itemView, TestAdapter testAdapter) {
            super(itemView);
            this.testAdapter = testAdapter;
        }
    }
}
