package com.rise.mealplanner.model;

/**
 * Created by rise on 30/8/15.
 */
public class DietPlanInfo {

    private boolean isHeader = false;
    private String title = "";
    private Meal meal;

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

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }
}
