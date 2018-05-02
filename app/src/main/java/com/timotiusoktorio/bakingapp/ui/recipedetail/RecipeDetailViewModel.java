package com.timotiusoktorio.bakingapp.ui.recipedetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.source.DataManager;

public class RecipeDetailViewModel extends AndroidViewModel {

    private final DataManager dataManager;
    private Recipe recipe;
    private MutableLiveData<Boolean> favoriteFlag;

    RecipeDetailViewModel(@NonNull Application application, @NonNull DataManager dataManager) {
        super(application);
        this.dataManager = dataManager;
    }

    public void setRecipe(@NonNull Recipe recipe) {
        this.recipe = recipe;
    }

    public void resetFavoritePreferencesFlag() {
        dataManager.setRecipesDataUpdatedFlag(false);
    }

    public LiveData<Boolean> getFavoriteFlag() {
        if (favoriteFlag == null) {
            favoriteFlag = new MutableLiveData<>();
            checkFavorite();
        }
        return favoriteFlag;
    }

    private void checkFavorite() {
        dataManager.fetchRecipe(recipe.getId(), new DataManager.FetchRecipeCallback() {
            @Override
            public void onSuccess(Recipe recipe) {
                favoriteFlag.setValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                favoriteFlag.setValue(false);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    public void onFavoriteClick() {
        favoriteFlag.setValue(!favoriteFlag.getValue());
        if (favoriteFlag.getValue()) dataManager.saveRecipe(recipe);
        else dataManager.removeRecipe(recipe);
        dataManager.setRecipesDataUpdatedFlag(true);
    }
}