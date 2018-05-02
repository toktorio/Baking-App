package com.timotiusoktorio.bakingapp.di.module;

import android.app.Application;
import android.content.Context;

import com.timotiusoktorio.bakingapp.data.source.DataManager;
import com.timotiusoktorio.bakingapp.di.qualifier.ActivityContext;
import com.timotiusoktorio.bakingapp.ui.base.BaseActivity;
import com.timotiusoktorio.bakingapp.ui.recipedetail.RecipeDetailViewModelFactory;
import com.timotiusoktorio.bakingapp.ui.recipes.RecipesViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    public Context provideContext() {
        return activity;
    }

    @Provides
    public RecipesViewModelFactory provideRecipesViewModelFactory(Application application, DataManager dataManager) {
        return new RecipesViewModelFactory(application, dataManager);
    }

    @Provides
    public RecipeDetailViewModelFactory provideRecipeDetailViewModelFactory(Application application, DataManager dataManager) {
        return new RecipeDetailViewModelFactory(application, dataManager);
    }
}