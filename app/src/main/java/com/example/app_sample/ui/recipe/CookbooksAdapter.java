package com.example.app_sample.ui.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Cookbook;
import com.example.app_sample.data.local.models.Recipes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CookbooksAdapter extends RecyclerView.Adapter<CookbooksAdapter.MyViewHolder> {

    private AddToCookbookDialog dialog;
    private List<Cookbook> cookbooks;
    private Context context;
    private RecipeFragment fragment;

    public CookbooksAdapter(AddToCookbookDialog dialog) {
        this.dialog = dialog;
        this.context = dialog.getContext();
        this.fragment = (RecipeFragment) dialog.getParentFragment();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cookbook_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cookbook cookbook = cookbooks.get(position);

        holder.name.setText(cookbook.getName());

        dialog.getCookbookImages(cookbook.getId()).observe(dialog.getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (strings.size() > 0) {
                    RecipeRepository.loadCenterCrop(context, strings.get(0), holder.img1);
                } else {
                    holder.img1.setImageResource(R.color.light_grey);
                }
                if (strings.size() > 1) {
                    RecipeRepository.loadCenterCrop(context, strings.get(1), holder.img2);
                } else {
                    holder.img2.setImageResource(R.color.light_grey);
                }
                if (strings.size() > 2) {
                    RecipeRepository.loadCenterCrop(context, strings.get(2), holder.img3);
                } else {
                    holder.img3.setImageResource(R.color.light_grey);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.addToCookbook(cookbook.getId(), dialog.recipe).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        fragment.snack("Recipe added to cookbook successfully");
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        if (cookbooks != null)
            return cookbooks.size();
        else
            return 0;
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img1, img2, img3;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.image1);
            img2 = itemView.findViewById(R.id.image2);
            img3 = itemView.findViewById(R.id.image3);
            name = itemView.findViewById(R.id.name);
        }
    }

    public void setCookbooks(List<Cookbook> cookbooks) {
        this.cookbooks = cookbooks;
        notifyDataSetChanged();
    }
}
