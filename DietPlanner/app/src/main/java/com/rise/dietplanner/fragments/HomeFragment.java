package com.rise.dietplanner.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
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
import com.rise.dietplanner.model.DietPlanInfo;
import com.rise.dietplanner.model.Vegetable;

import java.util.ArrayList;

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
        SelectVegetableDialogFragment.SelectVegetableInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SelectVegetableDialogFragment dialogFragment;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int selectedItem = -1;

    private OnFragmentInteractionListener mListener;

//    private ArrayList<InfoModel>
    private View rootView = null;
    private GridView gvWeekData = null;
    private DietPlanInfo[] dashboardData = null;
    private DietPlanGridAdapter dietPlanGridAdapter = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        gvWeekData = (GridView) rootView.findViewById(R.id.gvWeekData);

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
        dialogFragment.setCommunicationInterface(this);
        dialogFragment.show(getFragmentManager(), "Select Vegetable");
        selectedItem = i;
    }

    @Override
    public void selectVegetables(ArrayList<Vegetable> vegetablesInfo) {

        DietPlanInfo dietPlanInfo = new DietPlanInfo();
        dietPlanInfo.setSelectedVegetableArrayList(vegetablesInfo);
        dietPlanInfo.setIsHeader(false);
        dashboardData[selectedItem] = dietPlanInfo;
        dietPlanGridAdapter.notifyDataSetChanged();

        Toast.makeText(getActivity(), "Vegetables Selected!!", Toast.LENGTH_LONG).show();
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
