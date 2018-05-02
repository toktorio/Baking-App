package com.timotiusoktorio.bakingapp.data.source;

import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.source.db.DbHelper;
import com.timotiusoktorio.bakingapp.data.source.network.ApiHelper;
import com.timotiusoktorio.bakingapp.data.source.preferences.PreferencesHelper;
import com.timotiusoktorio.bakingapp.ui.recipes.RecipesPage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RecipeDataManager implements DataManager {

    private final ApiHelper apiHelper;
    private final DbHelper dbHelper;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public RecipeDataManager(ApiHelper apiHelper, DbHelper dbHelper, PreferencesHelper preferencesHelper) {
        this.apiHelper = apiHelper;
        this.dbHelper = dbHelper;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void fetchRecipes(int recipesPage, FetchRecipesCallback callback) {
        if (recipesPage == RecipesPage.RECIPES.getPage()) {
            apiHelper.fetchRecipes(callback);
        } else if (recipesPage == RecipesPage.FAVORITE_RECIPES.getPage()) {
            dbHelper.fetchRecipes(callback);
        }
    }

    @Override
    public void fetchRecipe(int recipeId, FetchRecipeCallback callback) {
        dbHelper.fetchRecipe(recipeId, callback);
    }

    @Override
    public void fetchFavoriteRecipe(FetchRecipeCallback callback) {
        dbHelper.fetchFavoriteRecipe(callback);
    }

    @Override
    public void saveRecipe(Recipe recipe) {
        dbHelper.saveRecipe(recipe);
    }

    @Override
    public void removeRecipe(Recipe recipe) {
        dbHelper.removeRecipe(recipe);
    }

    @Override
    public boolean getRecipesDataUpdatedFlag() {
        return preferencesHelper.getRecipesDataUpdatedFlag();
    }

    @Override
    public void setRecipesDataUpdatedFlag(boolean flag) {
        preferencesHelper.setRecipesDataUpdatedFlag(flag);
    }
}