package com.rise.dietplanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rise.dietplanner.R;
import com.rise.dietplanner.model.Vegetable;

import java.util.ArrayList;

/**
 * Created by rise on 28/9/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "diet_planner";
    private static final int DB_VERSION = 1;
    private Context mContext = null;

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Vegetables(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, VegetableName TEXT NOT NULL, VegetableNutrient TEXT, VegetableImagePath TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertVegetablesInDatabase() {

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
    }

    public void addVegetable(String vegetableName, String vegetableNutrients, String vegetableImagePath) {

        SQLiteDatabase database = this.getWritableDatabase();

        database.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put("VegetableName", vegetableName);
        contentValues.put("VegetableNutrient", vegetableNutrients);
        contentValues.put("VegetableImagePath", vegetableImagePath);

        database.insert("Vegetables", "null", contentValues);
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
            vegetable.setNutrientInfo(mCursor.getString(mCursor.getColumnIndex("VegetableNutrient")));
            vegetable.setImageUrl(mCursor.getString(mCursor.getColumnIndex("VegetableImagePath")));

            vegetablesList.add(vegetable);
        }

        mCursor.close();
        return vegetablesList;
    }

}
