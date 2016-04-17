package com.rise.dietplanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rise.dietplanner.R;
import com.rise.dietplanner.model.Meal;

import java.util.ArrayList;

/**
 * Created by rise on 16/4/16.
 */
public class DailyDietListRecyclerviewAdapter extends RecyclerView.Adapter<DailyDietListRecyclerviewAdapter.CustomerViewHolder> {

    private Context mContext = null;
    private LayoutInflater inflater = null;
    private ArrayList<Meal> meals = null;

    public DailyDietListRecyclerviewAdapter(Context mContext, ArrayList<Meal> meals) {
        this.mContext = mContext;
        this.meals = meals;
        inflater = LayoutInflater.from(mContext);
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {

        RecyclerView dietVegetableListRecyclerview = null;

        public CustomerViewHolder(View itemView) {
            super(itemView);

            dietVegetableListRecyclerview = (RecyclerView) itemView.findViewById(
                    R.id.diet_vegetable_list_recyclerview);
            dietVegetableListRecyclerview.setLayoutManager(
                    new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mealView = inflater.inflate(R.layout.daily_diet_vegetable_recyclerview_layout, parent, false);
        CustomerViewHolder viewHolder = new CustomerViewHolder(mealView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {

        Meal meal = meals.get(position);

        holder.dietVegetableListRecyclerview.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                mContext.getResources().getDimensionPixelSize(R.dimen.daily_diet_card_view_height)));

        DietVegListRecyclerviewAdapter adapter = new DietVegListRecyclerviewAdapter(mContext, meal);
        holder.dietVegetableListRecyclerview.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
}
