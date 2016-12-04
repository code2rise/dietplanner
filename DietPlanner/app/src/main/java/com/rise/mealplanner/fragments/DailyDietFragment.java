package com.rise.mealplanner.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rise.mealplanner.R;
import com.rise.mealplanner.adapters.DailyDietListRecyclerviewAdapter;
import com.rise.mealplanner.customviews.OnSwipeTouchListener;
import com.rise.mealplanner.db.DatabaseHelper;
import com.rise.mealplanner.interfaces.SelectVegetableInterface;
import com.rise.mealplanner.model.Meal;
import com.rise.mealplanner.util.HandyFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyDietFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyDietFragment extends Fragment implements View.OnClickListener, SelectVegetableInterface {

    private View rootView = null;
    private TextView tvSelectedDay = null;
    private RecyclerView dailyDietListRecyclerview = null;
    private DatabaseHelper databaseHelper = null;
    private SimpleDateFormat dateFormatter = null;
    private ArrayList<Meal> meals = new ArrayList<>();

    public DailyDietFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DailyDietFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyDietFragment newInstance() {
        DailyDietFragment fragment = new DailyDietFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_daily_diet, container, false);
        tvSelectedDay = (TextView) rootView.findViewById(R.id.tv_selected_day);
        dailyDietListRecyclerview = (RecyclerView) rootView.findViewById(R.id.daily_diet_list);
        dailyDietListRecyclerview.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        dateFormatter = new SimpleDateFormat("E, d MMMM yyyy");
        databaseHelper = new DatabaseHelper(getActivity());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY , 0);
        calendar.set(Calendar.MINUTE , 0);
        calendar.set(Calendar.SECOND , 0);
        calendar.set(Calendar.MILLISECOND, 0);

        tvSelectedDay.setText(dateFormatter.format(calendar.getTime()));
        tvSelectedDay.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {

            }

            public void onSwipeRight() {

                try {
                    Date date = dateFormatter.parse(tvSelectedDay.getText().toString());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.set(Calendar.HOUR_OF_DAY , 0);
                    cal.set(Calendar.MINUTE , 0);
                    cal.set(Calendar.SECOND , 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    cal.add(Calendar.DAY_OF_YEAR, -1);
                    tvSelectedDay.setText(dateFormatter.format(cal.getTime()));

                    meals = databaseHelper.getMealInformation(cal.getTime());

                    if(meals.size() > 0) {
                        DailyDietListRecyclerviewAdapter adapter = new DailyDietListRecyclerviewAdapter(
                                getActivity(), meals);
                        dailyDietListRecyclerview.setAdapter(adapter);
                        dailyDietListRecyclerview.setVisibility(View.VISIBLE);
                    }
                    else {
                        dailyDietListRecyclerview.setVisibility(View.GONE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            public void onSwipeLeft() {

                try {
                    Date date = dateFormatter.parse(tvSelectedDay.getText().toString());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.set(Calendar.HOUR_OF_DAY , 0);
                    cal.set(Calendar.MINUTE , 0);
                    cal.set(Calendar.SECOND , 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    tvSelectedDay.setText(dateFormatter.format(cal.getTime()));

                    meals = databaseHelper.getMealInformation(cal.getTime());

                    if(meals.size() > 0) {
                        DailyDietListRecyclerviewAdapter adapter = new DailyDietListRecyclerviewAdapter(
                                getActivity(), meals);
                        dailyDietListRecyclerview.setAdapter(adapter);
                        dailyDietListRecyclerview.setVisibility(View.VISIBLE);
                    }
                    else {
                        dailyDietListRecyclerview.setVisibility(View.GONE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            public void onSwipeBottom() {

            }
        });

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        meals = databaseHelper.getMealInformation(calendar.getTime());

        if(meals.size() > 0) {
            DailyDietListRecyclerviewAdapter adapter = new DailyDietListRecyclerviewAdapter(
                    getActivity(), meals);
            dailyDietListRecyclerview.setAdapter(adapter);
            dailyDietListRecyclerview.setVisibility(View.VISIBLE);
        }
        else {
            dailyDietListRecyclerview.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btnSelectMealVegetable: {
//
//                SelectVegetableDialogFragment dialogFragment = new SelectVegetableDialogFragment();
//                DietPlanInfo dietPlanInfo = null;
//                dialogFragment.setSelectedVegetables(dietPlanInfo);
//                dialogFragment.setCommunicationInterface(DailyDietFragment.this);
//                dialogFragment.show(getFragmentManager(), "Select Vegetable");
//
//                break;
//            }
            default: {
                break;
            }
        }
    }

    @Override
    public void selectVegetables(Meal meal) {

    }
}
