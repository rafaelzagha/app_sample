package com.example.app_sample.ui.profile.cookbooks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Cookbook;

import java.util.List;

public class CookbooksAdapter extends RecyclerView.Adapter<CookbooksAdapter.ViewHolder> {

    private CookbooksFragment fragment;
    private Context context;
    private List<Cookbook> cookbooks;

    public CookbooksAdapter(CookbooksFragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getContext();
    }

    @NonNull
    @Override
    public CookbooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cookbook, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CookbooksAdapter.ViewHolder holder, int position) {
        Cookbook cookbook = cookbooks.get(position);

        holder.name.setText(cookbook.getName());
        if(cookbook.getRecipes() != null)
            holder.length.setText(cookbook.getRecipes().size() + " items");
        else
            holder.length.setText("0 items");

        fragment.getCookbookImages(cookbook.getId()).observe(fragment.getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if(strings.size() > 0){
                    RecipeRepository.loadCenterCrop(context, strings.get(0), holder.img1);
                }
                else{
                    holder.img1.setImageResource(R.color.light_grey);
                }
                if(strings.size() > 1){
                    RecipeRepository.loadCenterCrop(context, strings.get(1), holder.img2);
                }
                else{
                    holder.img2.setImageResource(R.color.light_grey);
                }
                if(strings.size() > 2){
                    RecipeRepository.loadCenterCrop(context, strings.get(2), holder.img3);
                }
                else{
                    holder.img3.setImageResource(R.color.light_grey);
                }
            }
        });


        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("id", cookbook.getId());
            NavHostFragment.findNavController(fragment).navigate(R.id.action_profileFragment_to_cookbookPageFragment, bundle);
        });

    }

    @Override
    public int getItemCount() {
        if(cookbooks != null)
            return cookbooks.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img1, img2, img3;
        TextView name, length;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.image1);
            img2 = itemView.findViewById(R.id.image2);
            img3 = itemView.findViewById(R.id.image3);
            name = itemView.findViewById(R.id.name);
            length = itemView.findViewById(R.id.size);
        }
    }

    public void setCookbooks(List<Cookbook> cookbooks) {
        this.cookbooks = cookbooks;
        notifyDataSetChanged();
    }
}
