package com.rise.dietplanner.app;

/**
 * Created by rise on 11/3/16.
 */
public class MealPlannerApp {

    public static String TAG = "MealPlanner";
    private static MealPlannerApp ourInstance = new MealPlannerApp();

    public static MealPlannerApp getInstance() {
        return ourInstance;
    }

    private MealPlannerApp() {
    }
}
