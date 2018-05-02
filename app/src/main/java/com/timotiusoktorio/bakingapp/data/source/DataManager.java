package com.timotiusoktorio.bakingapp.data.source;

import com.timotiusoktorio.bakingapp.data.model.Recipe;

import java.util.List;

public interface DataManager {

    void fetchRecipes(int recipesPage, FetchRecipesCallback callback);

    void fetchRecipe(int recipeId, FetchRecipeCallback callback);

    void fetchFavoriteRecipe(FetchRecipeCallback callback);

    void saveRecipe(Recipe recipe);

    void removeRecipe(Recipe recipe);

    boolean getRecipesDataUpdatedFlag();

    void setRecipesDataUpdatedFlag(boolean flag);

    interface FetchRecipesCallback {

        void onSuccess(List<Recipe> recipes);

        void onFailure(String errorMessage);
    }

    interface FetchRecipeCallback {

        void onSuccess(Recipe recipe);

        void onFailure(String errorMessage);
    }
}