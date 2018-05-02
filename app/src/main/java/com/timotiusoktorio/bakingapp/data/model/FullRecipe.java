package com.timotiusoktorio.bakingapp.data.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

@SuppressWarnings("unused")
public class FullRecipe {

    @Embedded
    private Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipeId", entity = Ingredient.class)
    private List<Ingredient> ingredients;

    @Relation(parentColumn = "id", entityColumn = "recipeId", entity = Step.class)
    private List<Step> steps;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
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
}