package com.rise.dietplanner.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rise.dietplanner.R;
import com.rise.dietplanner.customviews.SelectVegetableDialogFragment;
import com.rise.dietplanner.customviews.SnappyLinearLayoutManager;
import com.rise.dietplanner.customviews.SnappyRecyclerView;
import com.rise.dietplanner.interfaces.SelectVegetableInterface;
import com.rise.dietplanner.model.DietPlanInfo;
import com.rise.dietplanner.model.Meal;
import com.rise.dietplanner.model.Meals;
import com.rise.dietplanner.model.Vegetable;
import com.rise.dietplanner.model.Week;
import com.rise.dietplanner.util.CalendarGenerator;
import com.rise.dietplanner.util.HandyFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by rise on 16/4/16.
 */
public class DailyDietListRecyclerviewAdapter extends
        RecyclerView.Adapter<DailyDietListRecyclerviewAdapter.CustomerViewHolder> implements
        SelectVegetableInterface {

    private Context mContext = null;
    private LayoutInflater inflater = null;
    private ArrayList<Meal> meals = null;

    public DailyDietListRecyclerviewAdapter(Context mContext, ArrayList<Meal> meals) {
        this.mContext = mContext;
        this.meals = meals;
        inflater = LayoutInflater.from(mContext);
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
                mContext.getResources().getDimensionPixelSize(R.dimen.daily_diet_card_view_height)));

        if (meal.getVegetables() != null) {
            holder.addMealCardviewLayout.setVisibility(View.GONE);
            holder.dietVegetableListRecyclerview.setVisibility(View.VISIBLE);

            DietVegListRecyclerviewAdapter adapter = new DietVegListRecyclerviewAdapter(mContext, this, meal);
            holder.dietVegetableListRecyclerview.setAdapter(adapter);
        } else {
            holder.addMealCardviewLayout.setVisibility(View.VISIBLE);
            holder.dietVegetableListRecyclerview.setVisibility(View.GONE);

            // Get selected meal timing
            Meals selectedMeal;
            if (position % 3 == 1) {
                selectedMeal = Meals.BREAKFAST;
            } else if (position % 3 == 2) {
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
//        // Current week number
//        CalendarGenerator calendarGenerator = CalendarGenerator.getInstance();
//        Week week = calendarGenerator.getWeekDetails(selectedWeekIndex);
//
//        // 4 represents number of columns in grid view.
//        int day = (selectedItem / 4);
//        Calendar startOfWeek = Calendar.getInstance();
//        startOfWeek.setTime(week.getStartOfWeek());
//
//        // Subtracting 1 from calculated day as grid row index starts at 0.
//        startOfWeek.add(Calendar.DAY_OF_WEEK, day-1);
//
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//        String date = formatter.format(startOfWeek.getTime());
//
//        // Get selected meal timing
//        Meals selectedMeal;
//        if ((selectedItem-(day*4)) % 3 == 1) {
//            selectedMeal = Meals.BREAKFAST;
//        } else if ((selectedItem-(day*4)) % 3 == 2) {
//            selectedMeal = Meals.LUNCH;
//        }
//        else {
//            selectedMeal = Meals.DINNER;
//        }
//
//        // TODO Save these values to database table DietPlan
//        long timestamp = startOfWeek.getTimeInMillis();
//        int meal = selectedItem-(day*4);
//
//        Meal selectedMealInfo = new Meal();
//        selectedMealInfo.setMealCode(selectedMeal.name());
//        selectedMealInfo.setMealDateTime(timestamp);
//        selectedMealInfo.setVegetables(vegetablesInfo);
//
//        mDatabaseHelper.addSelectedVegetables(selectedMealInfo);
//
//        if(glWeeklyDietDetails != null) {
//            glWeeklyDietDetails.removeAllViews();
//
//            Week selectedWeek = CalendarGenerator.getInstance().getWeekDetails(selectedWeekIndex);
//            displayWeeklyDietDashboard(selectedWeek);
//        }
    }
}
