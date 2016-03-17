package com.rise.dietplanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
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
            vegetable.setId(String.valueOf(mCursor.getInt(mCursor.getColumnIndex("Id"))));
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

    public void addSelectedVegetables(int meal, long timestamp, ArrayList<Vegetable> vegetables) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();

        ContentValues mealContentValues = new ContentValues();
        mealContentValues.put("MealCode", meal);
        mealContentValues.put("Timestamp", timestamp);
        long mealId = database.insert("Meals", null, mealContentValues);

        int count = 0;
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

    public void getSelectedVegatbles(Week week) {

        SQLiteDatabase database = getReadableDatabase();
        Date startDate = week.getStartOfWeek();
        Date endDate = week.getEndOfWeek();

        Calendar startOfWeek = Calendar.getInstance();
        startOfWeek.setTime(startDate);

        Calendar endOfWeek = Calendar.getInstance();
        endOfWeek.setTime(endDate);

        if(startOfWeek.before(endOfWeek)) {

            while(startOfWeek.before(endOfWeek)) {

                String query = "SELECT * FROM Vegetables, Meals, MealVegetables Where Meals.Id = MealVegetables.MealId AND Vegetables.Id = MealVegetables.VegId AND Meals.Timestamp = ?";
                Cursor selectedVegetableCursor = database.rawQuery(query, new String[]{String.valueOf(startOfWeek.getTimeInMillis())});

                int count = 0;
                while(count < selectedVegetableCursor.getCount()) {
                    selectedVegetableCursor.moveToNext();
                    int mealCode = selectedVegetableCursor.getInt(selectedVegetableCursor.getColumnIndex("MealCode"));
                    count++;
                }

                startOfWeek.add(Calendar.DAY_OF_WEEK, 1);
            }
        }
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
            database.update("DietPlan", contentValues, "Id=?", new String[] {""+dietId});
            count++;
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
