package com.timotiusoktorio.bakingapp.data.source.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.timotiusoktorio.bakingapp.data.model.Step;

@Dao
public interface StepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Step step);

    @Delete
    void delete(Step step);
}