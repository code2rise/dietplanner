package com.rise.mealplanner.model;

import java.util.ArrayList;

/**
 * Created by rise on 19/3/16.
 */
public class Meal {

    private int mealId;
    private long mealDateTime = 0;
    private String mealCode = "";
    private ArrayList<Vegetable> vegetables;

    public long getMealDateTime() {
        return mealDateTime;
    }

    public void setMealDateTime(long mealDateTime) {
        this.mealDateTime = mealDateTime;
    }

    public String getMealCode() {
        return mealCode;
    }

    public void setMealCode(String mealCode) {
        this.mealCode = mealCode;
    }

    public ArrayList<Vegetable> getVegetables() {
        return vegetables;
    }

    public void setVegetables(ArrayList<Vegetable> vegetables) {
        this.vegetables = vegetables;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
}
