package com.example.app_sample.ui.recipe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Steps;

import java.util.List;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.ViewHolder> {

    Context context;
    List<Steps.Step> steps;

    public InstructionsAdapter(Context context, List<Steps.Step> steps) {
        this.context = context;
        this.steps = steps;
        steps.add(null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_instruction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position < getItemCount()-1){
            holder.step.setVisibility(View.VISIBLE);
            holder.number.setVisibility(View.VISIBLE);
            holder.dot.setVisibility(View.VISIBLE);
            Steps.Step step = steps.get(position);
            holder.number.setText(String.valueOf(step.getNumber()));
            holder.step.setText(step.getStep());

            if(step.getEquipment().isEmpty()){
                holder.equipment.setVisibility(View.GONE);
                holder.tv_equipment.setVisibility(View.GONE);
            }
            else{
                holder.equipment.setVisibility(View.VISIBLE);
                holder.tv_equipment.setVisibility(View.VISIBLE);
                EquipmentAdapter equipmentAdapter = new EquipmentAdapter(context, step.getEquipment());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                holder.equipment.setAdapter(equipmentAdapter);
                holder.equipment.setLayoutManager(layoutManager);
            }
        }
        else {
            holder.tv_directions.setText("End of instructions");
            holder.step.setVisibility(View.GONE);
            holder.number.setVisibility(View.GONE);
            holder.dot.setVisibility(View.GONE);
            holder.equipment.setVisibility(View.GONE);
            holder.tv_equipment.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView number, step, tv_equipment, tv_directions;
        RecyclerView equipment;
        View dot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            step = itemView.findViewById(R.id.step);
            tv_equipment = itemView.findViewById(R.id.tv_equipment);
            equipment = itemView.findViewById(R.id.rv_equipment);
            dot = itemView.findViewById(R.id.dot);
            tv_directions = itemView.findViewById(R.id.tv_directions);
        }
    }
}
