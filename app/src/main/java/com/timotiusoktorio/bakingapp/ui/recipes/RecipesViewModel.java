package com.timotiusoktorio.bakingapp.ui.recipes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.source.DataManager;
import com.timotiusoktorio.bakingapp.data.source.RecipeDataManager;

import java.util.List;

import timber.log.Timber;

public class RecipesViewModel extends AndroidViewModel {

    public final ObservableBoolean mShowRecipesFlag = new ObservableBoolean(false);
    public final ObservableBoolean mShowLoadingFlag = new ObservableBoolean(false);
    public final ObservableBoolean mShowErrorFlag = new ObservableBoolean(false);
    public final ObservableField<String> mErrorMessage = new ObservableField<>();

    private final MutableLiveData<List<Recipe>> mRecipes = new MutableLiveData<>();
    private final MutableLiveData<String> mSnackbarMessage = new MutableLiveData<>();
    private final DataManager mDataManager;
    private int mRecipesPage;

    RecipesViewModel(Application application, DataManager dataManager) {
        super(application);
        mDataManager = dataManager;
    }

    public void setRecipesPage(int recipesPage) {
        mRecipesPage = recipesPage;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    public LiveData<String> getSnackbarMessage() {
        return mSnackbarMessage;
    }

    public void fetchRecipes(boolean refresh) {
        if (mRecipes.getValue() != null && !refresh) {
            Timber.i("fetchRecipes: data was already fetched");
            return;
        }

        showLoading();
        mDataManager.fetchRecipes(mRecipesPage, new RecipeDataManager.FetchRecipesCallback() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                mRecipes.postValue(recipes);
                showRecipes();
            }

            @Override
            public void onFailure(String errorMessage) {
                mErrorMessage.set(errorMessage);
                showError();
            }
        });
    }

    public void refreshRecipes() {
        boolean recipesDataUpdated = mDataManager.getRecipesDataUpdatedFlag();
        if (recipesDataUpdated) {
            fetchRecipes(true);
        }
    }

    private void showRecipes() {
        mShowRecipesFlag.set(true);
        mShowLoadingFlag.set(false);
        mShowErrorFlag.set(false);
    }

    private void showLoading() {
        mShowRecipesFlag.set(false);
        mShowLoadingFlag.set(true);
        mShowErrorFlag.set(false);
    }

    private void showError() {
        mShowRecipesFlag.set(false);
        mShowLoadingFlag.set(false);
        mShowErrorFlag.set(true);
    }
}