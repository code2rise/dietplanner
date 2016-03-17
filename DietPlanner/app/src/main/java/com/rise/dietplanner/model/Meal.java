package com.rise.dietplanner.model;

/**
 * Created by rise on 1/12/15.
 */
public enum Meal {

    BREAKFAST(1),
    LUNCH(2),
    DINNER(3);

    private final int mealCode;

    private Meal(int mealCode) {
        this.mealCode = mealCode;
    }
}
