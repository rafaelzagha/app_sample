package com.example.app_sample.ui.home.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private Filters.Cuisine[] cuisines;

    public GridAdapter(Context context) {
        this.context = context;
        this.cuisines = Filters.Cuisine.values();
    }

    @Override
    public int getCount() {
        return cuisines.length;
    }

    @Override
    public Filters.Cuisine getItem(int position) {
        return cuisines[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_cuisine, parent, false);
        ImageView img = convertView.findViewById(R.id.img_dish);
        TextView name = convertView.findViewById(R.id.category_name);
        img.setImageResource(getItem(position).img());
        name.setText(getItem(position).name());
        return convertView;
    }
}
