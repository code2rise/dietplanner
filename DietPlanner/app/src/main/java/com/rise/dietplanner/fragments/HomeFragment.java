package com.rise.dietplanner.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.rise.dietplanner.R;
import com.rise.dietplanner.adapters.DietPlanGridAdapter;
import com.rise.dietplanner.customviews.SelectVegetableDialogFragment;
import com.rise.dietplanner.db.DatabaseHelper;
import com.rise.dietplanner.interfaces.SelectVegetableInterface;
import com.rise.dietplanner.model.DietPlanInfo;
import com.rise.dietplanner.model.Meal;
import com.rise.dietplanner.model.Meals;
import com.rise.dietplanner.model.Vegetable;
import com.rise.dietplanner.model.Week;
import com.rise.dietplanner.util.CalendarGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        SelectVegetableInterface {

    private SelectVegetableDialogFragment dialogFragment;

    private int selectedItem = -1;

    private OnFragmentInteractionListener mListener;

//    private ArrayList<InfoModel>
    private View rootView = null;
    private GridView gvWeekData = null;
    private DietPlanInfo[] dashboardData = null;
    private DietPlanGridAdapter dietPlanGridAdapter = null;
    private DatabaseHelper mDatabaseHelper = null;
    private int weekNumber;

    public static HomeFragment newInstance(int weekNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("WeekNumber", weekNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) {
            weekNumber = bundle.getInt("WeekNumber");
        }
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        gvWeekData = (GridView) rootView.findViewById(R.id.gvWeekData);

        mDatabaseHelper = new DatabaseHelper(getActivity());
        dashboardData = getDashboardDietData();
        dietPlanGridAdapter = new DietPlanGridAdapter(getActivity(), dashboardData);
        gvWeekData.setAdapter(dietPlanGridAdapter);
        gvWeekData.setVerticalSpacing(1);
        gvWeekData.setHorizontalSpacing(1);
        gvWeekData.setOnItemClickListener(this);
        gvWeekData.setOnItemLongClickListener(this);

        return rootView;
    }


    private DietPlanInfo[] getDashboardDietData() {

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

        Week currentWeek = CalendarGenerator.getInstance().getCurrentWeek();
        int gridItemCount = 32;
        int counter = 4;
        while (counter <= gridItemCount) {
            if(counter % 4 == 0) {
                counter++;
                continue;
            }

            DietPlanInfo dietPlanInfo = new DietPlanInfo();
            dietPlanInfoList[counter] = dietPlanInfo;
            counter++;
        }

        dietPlanInfoList = mDatabaseHelper.getSelectedVegatbles(currentWeek, dietPlanInfoList);

        return dietPlanInfoList;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if(i < 4 || i % 4 == 0)
            return;

        dialogFragment = new SelectVegetableDialogFragment();
        if(dashboardData != null) {
            DietPlanInfo dietPlanInfo = dashboardData[i];
            dialogFragment.setSelectedVegetables(dietPlanInfo);
        }

        dialogFragment.setCommunicationInterface(this);
        dialogFragment.show(getFragmentManager(), "Select Vegetable");
        selectedItem = i;
    }

    @Override
    public void selectVegetables(ArrayList<Vegetable> vegetablesInfo) {

        // Current week number
        CalendarGenerator calendarGenerator = CalendarGenerator.getInstance();
        Week week = calendarGenerator.getCurrentWeek();

        // 4 represents number of columns in grid view.
        int day = (selectedItem / 4);
        Calendar startOfWeek = Calendar.getInstance();
        startOfWeek.setTime(week.getStartOfWeek());

        // Subtracting 1 from calculated day as grid row index starts at 0.
        startOfWeek.add(Calendar.DAY_OF_WEEK, day-1);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String date = formatter.format(startOfWeek.getTime());

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
        int meal = selectedItem-(day*4);

        Meal selectedMealInfo = new Meal();
        selectedMealInfo.setMealCode(selectedMeal.name());
        selectedMealInfo.setMealDateTime(timestamp);
        selectedMealInfo.setVegetables(vegetablesInfo);

        mDatabaseHelper.addSelectedVegetables(selectedMealInfo);

        dashboardData = getDashboardDietData();
        dietPlanGridAdapter.updateMealsDetails(dashboardData);
        dietPlanGridAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(getActivity(), "Position : " + i, Toast.LENGTH_SHORT).show();
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


}
