package com.rise.dietplanner.model;

import java.util.ArrayList;

/**
 * Created by rise on 16/9/15.
 */
public class Vegetable {
    private String id;
    private String title;
    private String nutrientInfo;
    private String imageUrl;
    private ArrayList<String> vegImageUrls = new ArrayList<>();
    private boolean isSelected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getVegImageUrls() {
        return vegImageUrls;
    }

    public void setVegImageUrls(ArrayList<String> vegImageUrls) {
        this.vegImageUrls = vegImageUrls;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNutrientInfo() {
        return nutrientInfo;
    }

    public void setNutrientInfo(String nutrientInfo) {
        this.nutrientInfo = nutrientInfo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
