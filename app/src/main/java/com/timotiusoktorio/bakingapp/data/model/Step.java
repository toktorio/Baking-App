package com.timotiusoktorio.bakingapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("unused")
@Entity(tableName = "steps")
public class Step {

    @PrimaryKey
    private int id;
    private int recipeId;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step() {
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static Step fromJson(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, Step.class);
    }
}