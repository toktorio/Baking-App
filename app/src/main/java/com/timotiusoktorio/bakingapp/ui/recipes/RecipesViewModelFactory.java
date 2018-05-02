package com.timotiusoktorio.bakingapp.ui.recipes;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timotiusoktorio.bakingapp.data.source.DataManager;
import com.timotiusoktorio.bakingapp.di.scope.ActivityScope;

import javax.inject.Inject;

@ActivityScope
public class RecipesViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final DataManager dataManager;

    @Inject
    public RecipesViewModelFactory(Application application, DataManager dataManager) {
        this.application = application;
        this.dataManager = dataManager;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public RecipesViewModel create(@NonNull Class modelClass) {
        return new RecipesViewModel(application, dataManager);
    }
}