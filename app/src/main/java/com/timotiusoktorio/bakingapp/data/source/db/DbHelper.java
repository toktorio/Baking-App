package com.timotiusoktorio.bakingapp.data.source.db;

import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.source.DataManager;

public interface DbHelper {

    void fetchRecipes(DataManager.FetchRecipesCallback callback);

    void fetchRecipe(int recipeId, DataManager.FetchRecipeCallback callback);

    void fetchFavoriteRecipe(DataManager.FetchRecipeCallback callback);

    void saveRecipe(Recipe recipe);

    void removeRecipe(Recipe recipe);
}