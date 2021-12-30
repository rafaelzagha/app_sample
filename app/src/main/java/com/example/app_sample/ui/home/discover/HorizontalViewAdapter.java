package com.example.app_sample.ui.home.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Category;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.utils.Utils;

import java.util.ArrayList;

public class HorizontalViewAdapter extends RecyclerView.Adapter<HorizontalViewAdapter.TestViewHolder> {

    private Filters.MealType[] items;
    private LayoutInflater layoutInflater;
    private int layout;

    public HorizontalViewAdapter(Context context, int layout) {
        layoutInflater = LayoutInflater.from(context);
        this.layout = layout;
        this.items = Filters.MealType.values();

    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(this.layout, parent, false);
        return new TestViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        if(layout == R.layout.item_category){
            holder.tv.setText(items[position].name());
            holder.img.setImageResource(items[position].img());
        }
    }



    @Override
    public int getItemCount() {
        return items.length;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder{

        private TextView tv;
        private ImageView img;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_cat);
            img = itemView.findViewById(R.id.img_cat);

        }
    }
}