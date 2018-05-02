package com.timotiusoktorio.bakingapp.data.source.network;

import com.timotiusoktorio.bakingapp.data.source.DataManager;

public interface ApiHelper {

    void fetchRecipes(DataManager.FetchRecipesCallback callback);
}