package com.example.app_sample.ui.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Equipment;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.utils.Constants;

import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    Context context;
    List<Equipment> equipment;

    public EquipmentAdapter(Context context, List<Equipment> equipment) {
        this.context = context;
        this.equipment = equipment;
    }

    @NonNull
    @Override
    public EquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_equipment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentAdapter.ViewHolder holder, int position) {
        Equipment e = equipment.get(position);
        RecipeRepository.loadImage(context, Constants.EQUIPMENT_URL + e.getImage(), holder.img);
        holder.txt.setText(e.getName());
    }

    @Override
    public int getItemCount() {
        return equipment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            txt = itemView.findViewById(R.id.name);
        }
    }
}
