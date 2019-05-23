package com.rise.mealplanner.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rise.mealplanner.R;
import com.rise.mealplanner.fragments.SelectVegetableDialogFragment;
import com.rise.mealplanner.customviews.snappyrecycler.SnappyLinearLayoutManager;
import com.rise.mealplanner.customviews.snappyrecycler.SnappyRecyclerView;
import com.rise.mealplanner.db.DatabaseHelper;
import com.rise.mealplanner.interfaces.SelectVegetableInterface;
import com.rise.mealplanner.model.Meal;
import com.rise.mealplanner.model.Meals;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rise on 16/4/16.
 */
public class DailyDietListRecyclerviewAdapter extends
        RecyclerView.Adapter<DailyDietListRecyclerviewAdapter.CustomerViewHolder> implements
        SelectVegetableInterface {

    private Context mContext = null;
    private LayoutInflater inflater = null;
    private ArrayList<Meal> meals = null;
    private DatabaseHelper mDatabaseHelper = null;

    public DailyDietListRecyclerviewAdapter(Context mContext, ArrayList<Meal> meals) {
        this.mContext = mContext;
        this.meals = meals;
        inflater = LayoutInflater.from(mContext);
        mDatabaseHelper = new DatabaseHelper(mContext);
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {

        CardView addMealCardviewLayout = null;
        SnappyRecyclerView dietVegetableListRecyclerview = null;
        TextView tvPrepareMeal = null;

        public CustomerViewHolder(View itemView) {
            super(itemView);

            tvPrepareMeal = (TextView) itemView.findViewById(R.id.tv_prepare_meal);
            addMealCardviewLayout = (CardView) itemView.findViewById(R.id.add_meal_cardview_layout);
            dietVegetableListRecyclerview = (SnappyRecyclerView) itemView.findViewById(
                    R.id.diet_vegetable_list_recyclerview);
            dietVegetableListRecyclerview.setLayoutManager(
                    new SnappyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mealView = inflater.inflate(R.layout.daily_diet_vegetable_recyclerview_layout, parent, false);
        CustomerViewHolder viewHolder = new CustomerViewHolder(mealView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, final int position) {

        final Meal meal = meals.get(position);

        holder.dietVegetableListRecyclerview.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        if (meal.getVegetables() != null && meal.getVegetables().size() > 0) {
            holder.addMealCardviewLayout.setVisibility(View.GONE);
            holder.dietVegetableListRecyclerview.setVisibility(View.VISIBLE);

            DietVegListRecyclerviewAdapter adapter = new DietVegListRecyclerviewAdapter(mContext, this, meal);
            holder.dietVegetableListRecyclerview.setAdapter(adapter);
        } else {
            holder.addMealCardviewLayout.setVisibility(View.VISIBLE);
            holder.dietVegetableListRecyclerview.setVisibility(View.GONE);

            // Get selected meal timing
            Meals selectedMeal;
            if (position % 3 == 0) {
                selectedMeal = Meals.BREAKFAST;
            } else if (position % 3 == 1) {
                selectedMeal = Meals.LUNCH;
            } else {
                selectedMeal = Meals.DINNER;
            }

            meal.setMealCode(selectedMeal.name());

            String mealCode = meal.getMealCode();
            holder.tvPrepareMeal.setText(mContext.getString(R.string.add_daily_meal_msg)
                    .concat(" ")
                    .concat(mealCode.substring(0, 1).toUpperCase() +
                            mealCode.substring(1).toLowerCase())
                    .concat("!"));

            holder.addMealCardviewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayVegetableSelectionPopup(meal);
                }
            });
        }
    }

    private void displayVegetableSelectionPopup(Meal meal) {
        SelectVegetableDialogFragment dialogFragment = new SelectVegetableDialogFragment();
        dialogFragment.setSelectedMeal(meal);
        dialogFragment.setCommunicationInterface(DailyDietListRecyclerviewAdapter.this);
        dialogFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "Select Vegetable");
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    @Override
    public void selectVegetables(Meal selectedMeal) {

        // Update meal information in the database.
        mDatabaseHelper.addSelectedVegetables(selectedMeal);
        meals = mDatabaseHelper.getMealInformation(new Date(selectedMeal.getMealDateTime()));
        mDatabaseHelper.close();

        notifyDataSetChanged();
    }
}
