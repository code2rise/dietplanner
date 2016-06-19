package com.rise.mealplanner.fragments;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.rise.mealplanner.R;
import com.rise.mealplanner.adapters.WeeksListAdapter;
import com.rise.mealplanner.customviews.SelectVegetableDialogFragment;
import com.rise.mealplanner.customviews.ShowVegetablesListDialogFragment;
import com.rise.mealplanner.db.DatabaseHelper;
import com.rise.mealplanner.interfaces.SelectVegetableInterface;
import com.rise.mealplanner.model.DietPlanInfo;
import com.rise.mealplanner.model.Meal;
import com.rise.mealplanner.model.Meals;
import com.rise.mealplanner.model.Vegetable;
import com.rise.mealplanner.model.Week;
import com.rise.mealplanner.util.CalendarGenerator;
import com.rise.mealplanner.util.HandyFunctions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeeklyDietFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyDietFragment extends Fragment implements SelectVegetableInterface {

    private View rootView = null;
    private GridLayout glWeeklyDietDetails = null;
    private LayoutInflater inflater = null;
    private Spinner spWeeks = null;

    private HandyFunctions handyFunctions = null;
    private DatabaseHelper mDatabaseHelper = null;
    private DietPlanInfo[] dashboardData = null;
    private SelectVegetableDialogFragment dialogFragment;

    private int selectedItem = -1;
    private int selectedWeekIndex;

    public WeeklyDietFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeeklyDietFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeeklyDietFragment newInstance() {
        WeeklyDietFragment fragment = new WeeklyDietFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabaseHelper = new DatabaseHelper(getActivity());
        this.inflater = inflater;
        this.handyFunctions = new HandyFunctions(getActivity());

        rootView = inflater.inflate(R.layout.fragment_diet_planner_weekly_layout, null);
        glWeeklyDietDetails = (GridLayout) rootView.findViewById(R.id.gl_weekly_diet_details);

        CalendarGenerator calendarGenerator = CalendarGenerator.getInstance();
        spWeeks = (Spinner) rootView.findViewById(R.id.spWeeks);
        ArrayList<Week> weeks = calendarGenerator.getWeeksList();
        WeeksListAdapter adapter = new WeeksListAdapter(getActivity(), weeks);
        spWeeks.setAdapter(adapter);
        spWeeks.setSelection(calendarGenerator.getCurrentWeekNumber());
        spWeeks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedWeekIndex = i;
                Week selectedWeek = CalendarGenerator.getInstance().getWeekDetails(selectedWeekIndex);
                displayWeeklyDietDashboard(selectedWeek);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectedWeekIndex = CalendarGenerator.getInstance().getCurrentWeekNumber();

        Week selectedWeek = CalendarGenerator.getInstance().getWeekDetails(selectedWeekIndex);
        displayWeeklyDietDashboard(selectedWeek);

        return rootView;
    }

    private void displayWeeklyDietDashboard(Week selectedWeek) {

        if(glWeeklyDietDetails != null) {
            glWeeklyDietDetails.removeAllViews();
        }

        dashboardData = getDashboardDietData(selectedWeek);
        for(int index=0; index<dashboardData.length; index++) {

            final View dashboardGridItemLayout;
            if(dashboardData[index].isHeader()) {
                dashboardGridItemLayout = inflater.inflate(R.layout.grid_header_item_layout, null);

                TextView tvTitle = (TextView) dashboardGridItemLayout.findViewById(R.id.tvTitle);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(dashboardData[index].getTitle());
            }
            else {
                dashboardGridItemLayout = inflater.inflate(R.layout.week_data_grid_view_item_layout, null);
                final ImageView imgAddVeg = (ImageView) dashboardGridItemLayout.findViewById(R.id.imgAddVeg);
                LinearLayout llSelectedVegContainer = (LinearLayout) dashboardGridItemLayout.findViewById(R.id.llSelectedVegContainer);

                Meal meal = dashboardData[index].getMeal();

                if(meal != null && meal.getVegetables() != null && meal.getVegetables().size() > 0) {
                    ArrayList<Vegetable> vegetables = meal.getVegetables();

                    llSelectedVegContainer.setVisibility(View.VISIBLE);
                    imgAddVeg.setVisibility(View.GONE);

                    llSelectedVegContainer.setPadding(getResources().getDimensionPixelSize(R.dimen.selected_veg_list_label_left_margin),
                            getResources().getDimensionPixelSize(R.dimen.selected_veg_list_label_top_margin),
                            getResources().getDimensionPixelSize(R.dimen.selected_veg_list_label_left_margin),
                            getResources().getDimensionPixelSize(R.dimen.selected_veg_list_label_top_margin));
                    llSelectedVegContainer.removeAllViews();

                    int count = 0;

                    while (count < vegetables.size()) {

                        Vegetable vegetable = vegetables.get(count);

                        TextView tvVegetableName = new TextView(getActivity());
                        tvVegetableName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                        tvVegetableName.setSingleLine(true);
                        tvVegetableName.setEllipsize(TextUtils.TruncateAt.END);
                        tvVegetableName.setText(vegetable.getTitle());
                        tvVegetableName.setPadding(handyFunctions.dpToPx(2), handyFunctions.dpToPx(0),
                                handyFunctions.dpToPx(2), handyFunctions.dpToPx(0));
                        tvVegetableName.setTextColor(getResources().getColor(R.color.white));

                        if(count % 3 == 0) {

                            Drawable mDrawable = handyFunctions.getVegetableBackground("#FA8258");
                            if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                tvVegetableName.setBackgroundDrawable(mDrawable);
                            } else {
                                tvVegetableName.setBackground(mDrawable);
                            }
                        }
                        else if(count % 2 == 0) {

                            Drawable mDrawable = handyFunctions.getVegetableBackground("#D358F7");
                            if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                tvVegetableName.setBackgroundDrawable(mDrawable);
                            } else {
                                tvVegetableName.setBackground(mDrawable);
                            }
                        }
                        else {

                            Drawable mDrawable = handyFunctions.getVegetableBackground("#04B486");
                            if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                tvVegetableName.setBackgroundDrawable(mDrawable);
                            } else {
                                tvVegetableName.setBackground(mDrawable);
                            }
                        }

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(handyFunctions.dpToPx(0), handyFunctions.dpToPx(1),
                                handyFunctions.dpToPx(0), handyFunctions.dpToPx(1));

                        llSelectedVegContainer.addView(tvVegetableName, layoutParams);

                        count++;
                    }
                }

                dashboardGridItemLayout.setTag(index);
                dashboardGridItemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedItem = (int) dashboardGridItemLayout.getTag();
                        dialogFragment = new SelectVegetableDialogFragment();
                        DietPlanInfo dietPlanInfo = null;
                        if(dashboardData != null) {
                            dietPlanInfo = dashboardData[selectedItem];

                            if(dietPlanInfo.getMeal() != null &&
                                    dietPlanInfo.getMeal().getVegetables() != null &&
                                    dietPlanInfo.getMeal().getVegetables().size() > 0) {
                                ShowVegetablesListDialogFragment showVegetablesListDialogFragment = new ShowVegetablesListDialogFragment();
                                showVegetablesListDialogFragment.setSelectedMeal(dietPlanInfo.getMeal());
                                showVegetablesListDialogFragment.setCommunicationInterface(WeeklyDietFragment.this);
                                showVegetablesListDialogFragment.show(getFragmentManager(), "Show Vegetables");
                            }
                            else {
                                dialogFragment.setSelectedMeal(dietPlanInfo.getMeal());
                                dialogFragment.setCommunicationInterface(WeeklyDietFragment.this);
                                dialogFragment.show(getFragmentManager(), "Select Vegetable");
                            }
                        }
                        else {
                            dialogFragment.setSelectedMeal(dietPlanInfo.getMeal());
                            dialogFragment.setCommunicationInterface(WeeklyDietFragment.this);
                            dialogFragment.show(getFragmentManager(), "Select Vegetable");
                        }
                    }
                });
            }

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f));
            layoutParams.width = 0;
            layoutParams.height = 0;
            layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.diet_dashboard_item_margin),
                    getResources().getDimensionPixelSize(R.dimen.diet_dashboard_item_margin),
                    getResources().getDimensionPixelSize(R.dimen.diet_dashboard_item_margin),
                    getResources().getDimensionPixelSize(R.dimen.diet_dashboard_item_margin));
            dashboardGridItemLayout.setLayoutParams(layoutParams);

            glWeeklyDietDetails.addView(dashboardGridItemLayout);
        }
    }

    private DietPlanInfo[] getDashboardDietData(Week selectedWeek) {

        DietPlanInfo[] dietPlanInfoList = new DietPlanInfo[32];

        DietPlanInfo gridTitle0 = new DietPlanInfo();
        gridTitle0.setTitle("Day");
        gridTitle0.setIsHeader(true);
        DietPlanInfo gridTitle1 = new DietPlanInfo();
        gridTitle1.setTitle("Breakfast");
        gridTitle1.setIsHeader(true);
        DietPlanInfo gridTitle2 = new DietPlanInfo();
        gridTitle2.setTitle("Lunch");
        gridTitle2.setIsHeader(true);
        DietPlanInfo gridTitle3 = new DietPlanInfo();
        gridTitle3.setTitle("Dinner");
        gridTitle3.setIsHeader(true);
        DietPlanInfo gridTitle4 = new DietPlanInfo();
        gridTitle4.setTitle("Sun");
        gridTitle4.setIsHeader(true);
        DietPlanInfo gridTitle5 = new DietPlanInfo();
        gridTitle5.setTitle("Mon");
        gridTitle5.setIsHeader(true);
        DietPlanInfo gridTitle6 = new DietPlanInfo();
        gridTitle6.setTitle("Tue");
        gridTitle6.setIsHeader(true);
        DietPlanInfo gridTitle7 = new DietPlanInfo();
        gridTitle7.setTitle("Wed");
        gridTitle7.setIsHeader(true);
        DietPlanInfo gridTitle8 = new DietPlanInfo();
        gridTitle8.setTitle("Thu");
        gridTitle8.setIsHeader(true);
        DietPlanInfo gridTitle9 = new DietPlanInfo();
        gridTitle9.setTitle("Fri");
        gridTitle9.setIsHeader(true);
        DietPlanInfo gridTitle10 = new DietPlanInfo();
        gridTitle10.setTitle("Sat");
        gridTitle10.setIsHeader(true);

        dietPlanInfoList[0] = gridTitle0;
        dietPlanInfoList[1] = gridTitle1;
        dietPlanInfoList[2] = gridTitle2;
        dietPlanInfoList[3] = gridTitle3;
        dietPlanInfoList[4] = gridTitle4;
        dietPlanInfoList[8] = gridTitle5;
        dietPlanInfoList[12] = gridTitle6;
        dietPlanInfoList[16] = gridTitle7;
        dietPlanInfoList[20] = gridTitle8;
        dietPlanInfoList[24] = gridTitle9;
        dietPlanInfoList[28] = gridTitle10;

//        Week currentWeek = CalendarGenerator.getInstance().getCurrentWeek();
        int gridItemCount = 32;
        int counter = 4;
        Calendar startOfWeek = Calendar.getInstance();
        startOfWeek.setTime(selectedWeek.getStartOfWeek());
        long dayOfWeek = startOfWeek.getTimeInMillis();

        while (counter <= gridItemCount) {
            if(counter % 4 == 0) {

                if(counter / 4 > 1 ) {
                    startOfWeek.add(Calendar.DAY_OF_WEEK, 1);
                    dayOfWeek = startOfWeek.getTimeInMillis();
                }

                counter++;
                continue;
            }

            DietPlanInfo dietPlanInfo = new DietPlanInfo();
            Meal meal = new Meal();
            meal.setMealDateTime(dayOfWeek);
            dietPlanInfo.setMeal(meal);

            dietPlanInfoList[counter] = dietPlanInfo;
            counter++;
        }

        dietPlanInfoList = mDatabaseHelper.getSelectedVegatbles(selectedWeek, dietPlanInfoList);

        return dietPlanInfoList;
    }

    @Override
    public void selectVegetables(Meal meal) {

        // 4 represents number of columns in grid view.
        int day = (selectedItem / 4);
        Calendar startOfWeek = Calendar.getInstance();
        startOfWeek.setTime(new Date(meal.getMealDateTime()));

        // Get selected meal timing
        Meals selectedMeal;
        if ((selectedItem-(day*4)) % 3 == 1) {
            selectedMeal = Meals.BREAKFAST;
        } else if ((selectedItem-(day*4)) % 3 == 2) {
            selectedMeal = Meals.LUNCH;
        }
        else {
            selectedMeal = Meals.DINNER;
        }

        // TODO Save these values to database table DietPlan
        long timestamp = startOfWeek.getTimeInMillis();

        Meal selectedMealInfo = new Meal();
        selectedMealInfo.setMealCode(selectedMeal.name());
        selectedMealInfo.setMealDateTime(timestamp);
        selectedMealInfo.setVegetables(meal.getVegetables());

        mDatabaseHelper.addSelectedVegetables(selectedMealInfo);

        if(glWeeklyDietDetails != null) {
            glWeeklyDietDetails.removeAllViews();

            Week selectedWeek = CalendarGenerator.getInstance().getWeekDetails(selectedWeekIndex);
            displayWeeklyDietDashboard(selectedWeek);
        }
    }
}
