package com.example.app_sample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.models.Category;
import com.example.app_sample.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private ArrayList<Category> items;
    private LayoutInflater layoutInflater;
    private int layout;

    public TestAdapter(Context context, int layout) {
        layoutInflater = LayoutInflater.from(context);
        this.layout = layout;
        this.items = Utils.getCategories();

    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(this.layout, parent, false);
        return new TestViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        if(layout == R.layout.category_item){
            holder.tv.setText(items.get(position).getName());
            holder.img.setImageResource(items.get(position).getImgId());
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
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
