package com.timotiusoktorio.bakingapp.data.source.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.timotiusoktorio.bakingapp.data.model.Ingredient;
import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.model.Step;
import com.timotiusoktorio.bakingapp.data.source.db.dao.IngredientDao;
import com.timotiusoktorio.bakingapp.data.source.db.dao.RecipeDao;
import com.timotiusoktorio.bakingapp.data.source.db.dao.StepDao;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "baking-app-db";

    public abstract RecipeDao recipeDao();

    public abstract IngredientDao ingredientDao();

    public abstract StepDao stepDao();
}