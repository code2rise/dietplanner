package com.rise.dietplanner.model;

import java.util.ArrayList;

/**
 * Created by rise on 16/9/15.
 */
public class Vegetable {
    private int id;
    private String title;
    private ArrayList<Nutrient> nutrientsList = new ArrayList<>();
    private String imageUrl = "";
//    private ArrayList<String> vegImageUrls = new ArrayList<>();
    private boolean isSelected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public ArrayList<String> getVegImageUrls() {
//        return vegImageUrls;
//    }
//
//    public void setVegImageUrls(ArrayList<String> vegImageUrls) {
//        this.vegImageUrls = vegImageUrls;
//    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public ArrayList<Nutrient> getNutrientsList() {
        return nutrientsList;
    }

    public void setNutrientsList(ArrayList<Nutrient> nutrientsList) {
        this.nutrientsList = nutrientsList;
    }

    public String toString() {
        return this.title;
    }
}
