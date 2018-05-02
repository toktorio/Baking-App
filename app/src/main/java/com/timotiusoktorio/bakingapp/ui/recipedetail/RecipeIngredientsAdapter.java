package com.timotiusoktorio.bakingapp.ui.recipedetail;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.Ingredient;
import com.timotiusoktorio.bakingapp.databinding.ListItemIngredientsBinding;

import java.util.List;

class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;

    RecipeIngredientsAdapter(@NonNull List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemIngredientsBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_ingredients, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.binding.setIngredient(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ListItemIngredientsBinding binding;

        ViewHolder(ListItemIngredientsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}