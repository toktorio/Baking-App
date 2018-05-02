package com.timotiusoktorio.bakingapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("unused")
@Entity(tableName = "ingredients")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int recipeId;
    private double quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getFormattedIngredient() {
        if (measure == null || measure.equalsIgnoreCase("UNIT")) {
            return quantity + " " + ingredient;
        }
        return quantity + " " + measure + " " + ingredient;
    }

    @Override
    public String toString() {
        return toJson();
    }

    private String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static Ingredient fromJson(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, Ingredient.class);
    }
}