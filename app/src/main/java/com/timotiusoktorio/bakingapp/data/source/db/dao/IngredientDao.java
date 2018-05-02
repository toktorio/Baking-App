package com.timotiusoktorio.bakingapp.data.source.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.timotiusoktorio.bakingapp.data.model.Ingredient;

@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);
}