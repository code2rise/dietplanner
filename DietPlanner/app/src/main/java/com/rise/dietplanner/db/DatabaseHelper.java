package com.rise.dietplanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.rise.dietplanner.model.DietPlanInfo;
import com.rise.dietplanner.model.Meal;
import com.rise.dietplanner.model.Meals;
import com.rise.dietplanner.model.Nutrient;
import com.rise.dietplanner.model.Vegetable;
import com.rise.dietplanner.model.Week;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rise on 28/9/15.
 */
public class DatabaseHelper extends SQLiteAssetHelper {

    private static String DB_PATH = "";
    private static final String DB_NAME = "diet_planner";
    private static final int DB_VERSION = 1;
    private Context mContext = null;
    private SQLiteDatabase mDataBase;

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    /*public void insertVegetablesInDatabase() {

        String[] vegetables = mContext.getResources().getStringArray(R.array.vegetables_list);
        int index=0;

        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();
        while (index<vegetables.length) {

            ContentValues contentValues = new ContentValues();
            contentValues.put("VegetableName", vegetables[index]);

            database.insert("Vegetables", "VegetableName", contentValues);
            index++;
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }*/

    public void addVegetable(String vegetableName, String vegetableNutrients, String vegetableImagePath) {

        SQLiteDatabase database = this.getWritableDatabase();

        database.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put("VegetableName", vegetableName);
        contentValues.put("VegNameDevnagari", vegetableName);
//        contentValues.put("VegetableNutrient", vegetableNutrients);
        contentValues.put("VegetableImagePath", vegetableImagePath);

        long vegId = database.insert("Vegetables", "null", contentValues);

        ContentValues vegNutrientContentValues = new ContentValues();
        vegNutrientContentValues.put("NutrientName", vegetableNutrients);
        vegNutrientContentValues.put("VegId", vegId);
        database.insert("VegNutrients", "null", vegNutrientContentValues);

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public ArrayList<Vegetable> getVegetablesList() {

        SQLiteDatabase database = this.getReadableDatabase();

        ArrayList<Vegetable> vegetablesList = new ArrayList<>();
        String query = "SELECT * FROM Vegetables";
        Cursor mCursor = database.rawQuery(query, new String[]{});

        while (mCursor.moveToNext()) {
            Vegetable vegetable = new Vegetable();
            vegetable.setId(mCursor.getInt(mCursor.getColumnIndex("Id")));
            vegetable.setTitle(mCursor.getString(mCursor.getColumnIndex("VegetableName")));
            vegetable.setImageUrl(mCursor.getString(mCursor.getColumnIndex("VegetableImagePath")));

            String vegNutrientQuery = "SELECT * FROM VegNutrients";
            Cursor vegNutrientCursor = database.rawQuery(vegNutrientQuery, new String[]{});

            StringBuilder nutrientInfoString = new StringBuilder();
            while (vegNutrientCursor.moveToNext()) {

                if(nutrientInfoString.length() > 0) {
                    nutrientInfoString.append(", ");
                }
                nutrientInfoString.append(vegNutrientCursor.getString(vegNutrientCursor.getColumnIndex("NutrientName")));
            }

            vegetable.setNutrientInfo(nutrientInfoString.toString());

            vegetablesList.add(vegetable);
        }

        mCursor.close();
        return vegetablesList;
    }

    public ArrayList<Nutrient> getNutrientsList() {

        ArrayList<Nutrient> nutrientsList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        String query = "SELECT * FROM Nutrients";
        Cursor mCursor = database.rawQuery(query, new String[]{});

        while (mCursor.moveToNext()) {
            Nutrient nutrient = new Nutrient();
            nutrient.setNutrientId(String.valueOf(mCursor.getInt(mCursor.getColumnIndex("Id"))));
            nutrient.setNutrientName(mCursor.getString(mCursor.getColumnIndex("NutrientName")));

            nutrientsList.add(nutrient);
        }

        mCursor.close();
        return nutrientsList;
    }

//    public void addSelectedVegetables(int meal, long timestamp, ArrayList<Vegetable> vegetables) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        database.beginTransaction();
//
//        ContentValues mealContentValues = new ContentValues();
//        mealContentValues.put("MealCode", meal);
//        mealContentValues.put("Timestamp", timestamp);
//        long mealId = database.insert("Meals", null, mealContentValues);
//
//        int count = 0;
//        while (count < vegetables.size()) {
//            Vegetable vegetable = vegetables.get(count);
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("VegId", vegetable.getId());
//            contentValues.put("MealId", mealId);
//            database.insert("MealVegetables", null, contentValues);
//            count++;
//        }
//
//        database.setTransactionSuccessful();
//        database.endTransaction();
//    }

    public void addSelectedVegetables(Meal selectedMealInfo) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();

        database.delete("Meals", "MealDateTime=?", new String[] {String.valueOf(selectedMealInfo.getMealDateTime())});

        Calendar calendar = Calendar.getInstance();
        ContentValues mealContentValues = new ContentValues();
        mealContentValues.put("MealCode", selectedMealInfo.getMealCode());
        mealContentValues.put("MealDateTime", selectedMealInfo.getMealDateTime());
        mealContentValues.put("Timestamp", calendar.getTimeInMillis());
        long mealId = database.insert("Meals", null, mealContentValues);

        int count = 0;
        ArrayList<Vegetable> vegetables = selectedMealInfo.getVegetables();
        while (count < vegetables.size()) {
            Vegetable vegetable = vegetables.get(count);
            ContentValues contentValues = new ContentValues();
            contentValues.put("VegId", vegetable.getId());
            contentValues.put("MealId", mealId);
            database.insert("MealVegetables", null, contentValues);
            count++;
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public DietPlanInfo[] getSelectedVegatbles(Week week, DietPlanInfo[] dietPlanInfoList) {

        int dayInWeek;
        ArrayList<Meal> weeklyMeals = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        Date startDate = week.getStartOfWeek();
        Date endDate = week.getEndOfWeek();

        Calendar startOfWeek = Calendar.getInstance();
        startOfWeek.setTime(startDate);

        Calendar endOfWeek = Calendar.getInstance();
        endOfWeek.setTime(endDate);

        if(startOfWeek.before(endOfWeek)) {

            dayInWeek = 1;
            while( !startOfWeek.after(endOfWeek) ) {

                String query = "SELECT * FROM meals Where Meals.MealDateTime = ?";
                Cursor getMealListCursor = database.rawQuery(query, new String[]{String.valueOf(startOfWeek.getTimeInMillis())});

                int count = 0;
                while(count < getMealListCursor.getCount()) {

                    getMealListCursor.moveToNext();
                    int mealId = getMealListCursor.getInt(getMealListCursor.getColumnIndex("Id"));
                    String mealCode = getMealListCursor.getString(getMealListCursor.getColumnIndex("MealCode"));

                    Meal meal = new Meal();
                    meal.setMealId(mealId);
                    meal.setMealCode(mealCode);
                    meal.setMealDateTime(startOfWeek.getTimeInMillis());

                    String getVegetablesListQuery = "SELECT Vegetables.Id, Vegetables.VegetableName " +
                            "FROM MealVegetables, vegetables Where " +
                            "MealVegetables.VegId = vegetables.Id AND MealVegetables.MealId = ?";
                    Cursor getVegetablesListCursor = database.rawQuery(getVegetablesListQuery,
                            new String[]{String.valueOf(mealId)});

                    ArrayList<Vegetable> vegetableList = new ArrayList<>();
                    while (getVegetablesListCursor.moveToNext()) {

                        int vegId = getVegetablesListCursor.getInt(getVegetablesListCursor.getColumnIndex("Id"));
                        String vegName = getVegetablesListCursor.getString(getVegetablesListCursor.getColumnIndex("VegetableName"));

                        Vegetable vegetable = new Vegetable();
                        vegetable.setId(vegId);
                        vegetable.setTitle(vegName);
                        vegetableList.add(vegetable);
                    }

                    meal.setVegetables(vegetableList);
                    weeklyMeals.add(meal);

                    int index = 0;
                    if(meal.getMealCode().equals(Meals.BREAKFAST.name())) {
                        index = 1;
                    } else if(meal.getMealCode().equals(Meals.LUNCH.name())) {
                        index = 2;
                    } else if(meal.getMealCode().equals(Meals.DINNER.name())) {
                        index = 3;
                    }

                    int mealIndex = (dayInWeek * 4) + index;
                    DietPlanInfo dietPlanInfo = new DietPlanInfo();
                    dietPlanInfo.setMeal(meal);
                    dietPlanInfoList[mealIndex] = dietPlanInfo;

                    count++;
                }

                startOfWeek.add(Calendar.DAY_OF_WEEK, 1);
                dayInWeek++;
            }
        }

        return dietPlanInfoList;
    }

    public void updateSelectedVegetables(ArrayList<Vegetable> vegetables, long timestamp, int dietId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();

        int count = 0;
        while (count < vegetables.size()) {
            Vegetable vegetable = vegetables.get(count);
            ContentValues contentValues = new ContentValues();
            contentValues.put("VegId", vegetable.getId());
            contentValues.put("Timestamp", timestamp);
            database.update("DietPlan", contentValues, "Id=?", new String[] {String.valueOf(dietId)});
            count++;
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
