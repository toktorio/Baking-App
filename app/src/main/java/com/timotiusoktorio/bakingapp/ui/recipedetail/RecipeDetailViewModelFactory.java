package com.timotiusoktorio.bakingapp.ui.recipedetail;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timotiusoktorio.bakingapp.data.source.DataManager;

import javax.inject.Inject;

public class RecipeDetailViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final DataManager dataManager;

    @Inject
    public RecipeDetailViewModelFactory(Application application, DataManager dataManager) {
        this.application = application;
        this.dataManager = dataManager;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public RecipeDetailViewModel create(@NonNull Class modelClass) {
        return new RecipeDetailViewModel(application, dataManager);
    }
}