package com.timotiusoktorio.bakingapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey
    private int id;
    private String name;
    @Ignore
    private List<Ingredient> ingredients;
    @Ignore
    private List<Step> steps;
    private int servings;
    private String image;

    public Recipe() {
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static Recipe fromJson(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, Recipe.class);
    }
}