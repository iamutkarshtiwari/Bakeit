package com.github.iamutkarshtiwari.bakingapp.model;

import java.io.Serializable;

/**
 * Created by utkarshtiwari on 08/02/2018.
 */
public class RecipeStepsModel implements Serializable {
    int recipe,id;
    String shortDescription,description,videoURL,thumbnailURL;

    public RecipeStepsModel(int recipe,int id, String shortDescription,String description, String videoURL, String thumbnailURL) {
        this.description = description;
        this.recipe = recipe;
        this.shortDescription = shortDescription;
        this.thumbnailURL = thumbnailURL;
        this.videoURL = videoURL;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
