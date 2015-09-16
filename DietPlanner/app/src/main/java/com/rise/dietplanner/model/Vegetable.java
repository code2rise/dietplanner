package com.rise.dietplanner.model;

import java.util.ArrayList;

/**
 * Created by rise on 16/9/15.
 */
public class Vegetable {
    private String id;
    private String title;
    private String imageUrl;
    private ArrayList<String> vegImageUrls = new ArrayList<>();

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
}
