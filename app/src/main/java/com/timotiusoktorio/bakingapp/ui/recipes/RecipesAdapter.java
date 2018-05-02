package com.timotiusoktorio.bakingapp.ui.recipes;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.databinding.ListItemRecipesBinding;

import java.util.ArrayList;
import java.util.List;

class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private final OnItemClickListener listener;
    private final List<Recipe> recipes;

    RecipesAdapter(@NonNull OnItemClickListener listener) {
        this.listener = listener;
        this.recipes = new ArrayList<>();
    }

    void updateData(@NonNull List<Recipe> newRecipes) {
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemRecipesBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_recipes, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.binding.setRecipe(recipe);

        String imageUrl = recipe.getImage();
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
            Picasso.get().load(imageUrl).fit().centerCrop()
                    .placeholder(R.drawable.recipe_image_placeholder)
                    .into(holder.binding.ivRecipe);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ListItemRecipesBinding binding;

        ViewHolder(ListItemRecipesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        final Recipe recipe = recipes.get(position);
                        listener.onItemClick(recipe);
                    }
                }
            });
        }
    }

    interface OnItemClickListener {

        void onItemClick(@NonNull Recipe recipe);
    }
}