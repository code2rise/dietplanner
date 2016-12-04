package com.rise.mealplanner.customviews;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;

import com.rise.mealplanner.R;
import com.rise.mealplanner.adapters.SelectVegetableAdapter;
import com.rise.mealplanner.db.DatabaseHelper;
import com.rise.mealplanner.interfaces.SelectVegetableInterface;
import com.rise.mealplanner.model.Meal;
import com.rise.mealplanner.model.Vegetable;

import java.util.ArrayList;

/**
 * Created by rise on 16/9/15.
 */
public class SelectVegetableDialogFragment extends DialogFragment implements View.OnClickListener {

    private View rootView = null;
    private GridView gvVegetableList = null;
    private Button btnOk = null;
    private Button btnCancel = null;
    private SelectVegetableInterface selectVegetableInterface;
    private DatabaseHelper mDatabaseHelper = null;
    private Meal selectedMeal = null;
    private ArrayList<Vegetable> selectedVegetables = new ArrayList<>();
    private SelectVegetableAdapter selectVegetableAdapter = null;
    private ArrayList<Vegetable> vegetablesList = new ArrayList<>();

    public SelectVegetableDialogFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.select_veg_dialog_fragment_layout, null);
        getDialog().setTitle(getResources().getString(R.string.add_vegetable_dialog_box_title));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        gvVegetableList = (GridView) rootView.findViewById(R.id.gvVegetableList);

        if(selectedMeal != null && selectedMeal.getVegetables() != null) {
            selectedVegetables = selectedMeal.getVegetables();
        }

        mDatabaseHelper = new DatabaseHelper(getActivity());
        vegetablesList = mDatabaseHelper.getVegetablesList();
        for (Vegetable selectedVegetable : selectedVegetables) {
            for(Vegetable vegetable : vegetablesList) {
                if(selectedVegetable.getTitle().equals(vegetable.getTitle())) {
                    vegetable.setSelected(true);
                    break;
                }
            }
        }

        selectVegetableAdapter = new SelectVegetableAdapter(getActivity(), vegetablesList, selectedVegetables);
        gvVegetableList.setAdapter(selectVegetableAdapter);

        btnOk = (Button) rootView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnOk: {

                // TODO Update the vegetable database here.

                selectedMeal.setVegetables(selectVegetableAdapter.getSelectedVegetables());
                selectVegetableInterface.selectVegetables(selectedMeal);
                dismiss();
                break;
            }
            case R.id.btnCancel: {
                dismiss();
                break;
            }
        }
    }

    public void setCommunicationInterface(SelectVegetableInterface selectVegetableInterface) {

        this.selectVegetableInterface = selectVegetableInterface;
    }

    public Meal getSelectedMeal() {
        return selectedMeal;
    }

    public void setSelectedMeal(Meal selectedMeal) {
        this.selectedMeal = selectedMeal;
    }
}
