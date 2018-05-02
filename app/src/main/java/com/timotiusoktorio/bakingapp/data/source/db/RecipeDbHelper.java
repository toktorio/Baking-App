package com.timotiusoktorio.bakingapp.data.source.db;

import android.content.Context;
import android.os.AsyncTask;

import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.FullRecipe;
import com.timotiusoktorio.bakingapp.data.model.Ingredient;
import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.model.Step;
import com.timotiusoktorio.bakingapp.data.source.DataManager;
import com.timotiusoktorio.bakingapp.di.qualifier.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class RecipeDbHelper implements DbHelper {

    private final Context context;
    private final AppDatabase database;

    @Inject
    public RecipeDbHelper(@ApplicationContext Context context, AppDatabase database) {
        this.context = context;
        this.database = database;
    }

    @SuppressWarnings("StaticFieldLeak")
    @Override
    public void fetchRecipes(final DataManager.FetchRecipesCallback callback) {
        new AsyncTask<Void, Void, List<Recipe>>() {
            @Override
            protected List<Recipe> doInBackground(Void... voids) {
                List<Recipe> recipes = new ArrayList<>();
                List<FullRecipe> fullRecipes = database.recipeDao().queryAll();
                if (fullRecipes != null) {
                    Timber.d("fetchRecipes: total recipes found = %s", fullRecipes.size());
                    for (FullRecipe fullRecipe : fullRecipes) {
                        Recipe recipe = fullRecipe.getRecipe();
                        recipe.setIngredients(fullRecipe.getIngredients());
                        recipe.setSteps(fullRecipe.getSteps());
                        recipes.add(recipe);
                    }
                }
                return recipes;
            }

            @Override
            protected void onPostExecute(List<Recipe> recipes) {
                super.onPostExecute(recipes);
                if (recipes != null && !recipes.isEmpty()) {
                    callback.onSuccess(recipes);
                } else {
                    callback.onFailure(context.getString(R.string.message_get_recipes_failed_no_data));
                }
            }
        }.execute();
    }

    @SuppressWarnings("StaticFieldLeak")
    @Override
    public void fetchRecipe(int recipeId, final DataManager.FetchRecipeCallback callback) {
        new AsyncTask<Integer, Void, Recipe>() {
            @Override
            protected Recipe doInBackground(Integer... params) {
                return database.recipeDao().query(params[0]);
            }

            @Override
            protected void onPostExecute(Recipe recipe) {
                super.onPostExecute(recipe);
                if (recipe != null) callback.onSuccess(recipe);
                else callback.onFailure(context.getString(R.string.message_get_recipe_failed));
            }
        }.execute(recipeId);
    }

    @Override
    public void fetchFavoriteRecipe(DataManager.FetchRecipeCallback callback) {
        List<FullRecipe> fullRecipes = database.recipeDao().queryAll();
        if (fullRecipes != null && !fullRecipes.isEmpty()) {
            FullRecipe fullRecipe = fullRecipes.get(0);
            Recipe recipe = fullRecipe.getRecipe();
            recipe.setIngredients(fullRecipe.getIngredients());
            recipe.setSteps(fullRecipe.getSteps());
            callback.onSuccess(recipe);
        } else {
            callback.onFailure("No recipes found in database");
        }
    }

    @SuppressWarnings("StaticFieldLeak")
    @Override
    public void saveRecipe(final Recipe recipe) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Ingredient> ingredients = recipe.getIngredients();
                for (Ingredient ingredient : ingredients) {
                    ingredient.setRecipeId(recipe.getId());
                    database.ingredientDao().insert(ingredient);
                }

                List<Step> steps = recipe.getSteps();
                for (Step step : steps) {
                    step.setRecipeId(recipe.getId());
                    database.stepDao().insert(step);
                }

                long totalSaved = database.recipeDao().insert(recipe);
                Timber.d("saveRecipe: totalSaved = %s", totalSaved);
                return null;
            }
        }.execute();
    }

    @SuppressWarnings("StaticFieldLeak")
    @Override
    public void removeRecipe(final Recipe recipe) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Ingredient> ingredients = recipe.getIngredients();
                for (Ingredient ingredient : ingredients) {
                    database.ingredientDao().delete(ingredient);
                }

                List<Step> steps = recipe.getSteps();
                for (Step step : steps) {
                    database.stepDao().delete(step);
                }

                int totalRemoved = database.recipeDao().delete(recipe);
                Timber.d("removeRecipe: totalRemoved = %s", totalRemoved);
                return null;
            }
        }.execute();
    }
}