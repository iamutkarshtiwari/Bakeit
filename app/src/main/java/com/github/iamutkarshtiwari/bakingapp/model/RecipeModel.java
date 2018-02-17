package com.github.iamutkarshtiwari.bakingapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by utkarshtiwari on 08/02/2018.
 */
public class RecipeModel implements Serializable {

    int id;
    String name,servings,image;

    public RecipeModel(int id, String name, String servings,String image) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.servings = servings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }
}
