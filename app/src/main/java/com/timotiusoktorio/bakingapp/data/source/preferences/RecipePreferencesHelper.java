package com.timotiusoktorio.bakingapp.data.source.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.di.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RecipePreferencesHelper implements PreferencesHelper {

    private final Context context;
    private final SharedPreferences sharedPreferences;

    @Inject
    public RecipePreferencesHelper(@ApplicationContext Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public boolean getRecipesDataUpdatedFlag() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_key_recipes_data_updated), false);
    }

    @Override
    public void setRecipesDataUpdatedFlag(boolean flag) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.pref_key_recipes_data_updated), flag).apply();
    }
}