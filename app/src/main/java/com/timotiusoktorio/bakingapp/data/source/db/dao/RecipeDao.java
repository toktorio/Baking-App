package com.timotiusoktorio.bakingapp.data.source.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.timotiusoktorio.bakingapp.data.model.FullRecipe;
import com.timotiusoktorio.bakingapp.data.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes")
    List<FullRecipe> queryAll();

    @Query("SELECT * FROM recipes WHERE id LIKE :id")
    Recipe query(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Recipe recipe);

    @Delete
    int delete(Recipe recipe);
}