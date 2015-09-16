package com.rise.dietplanner.model;

import java.util.ArrayList;

/**
 * Created by rise on 30/8/15.
 */
public class DietPlanInfo {

    private boolean isHeader = false;
    private String title = "";
    private ArrayList<Vegetable> selectedVegetableArrayList = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public ArrayList<Vegetable> getSelectedVegetableArrayList() {
        return selectedVegetableArrayList;
    }

    public void setSelectedVegetableArrayList(ArrayList<Vegetable> selectedVegetableArrayList) {
        this.selectedVegetableArrayList = selectedVegetableArrayList;
    }
}
