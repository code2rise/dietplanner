package com.rise.dietplanner.customviews;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.adapters.SelectVegetableAdapter;
import com.rise.dietplanner.adapters.SelectedVegetablesViewPagerAdapter;
import com.rise.dietplanner.db.DatabaseHelper;
import com.rise.dietplanner.interfaces.SelectVegetableInterface;
import com.rise.dietplanner.model.DietPlanInfo;
import com.rise.dietplanner.model.Vegetable;

import java.util.ArrayList;

/**
 * Created by rise on 16/9/15.
 */
public class ShowVegetablesListDialogFragment extends DialogFragment implements View.OnClickListener {

    private View rootView = null;
    private ImageView imgEdit = null;
    private ViewPager vegetableListPager = null;
    private SelectVegetableInterface selectVegetableInterface;
    private DatabaseHelper mDatabaseHelper = null;
    private DietPlanInfo selectedDietInfo = null;
    private ArrayList<Vegetable> selectedVegetables = new ArrayList<>();
    private SelectVegetableAdapter selectVegetableAdapter = null;
    private ArrayList<Vegetable> vegetablesList = new ArrayList<>();

    public ShowVegetablesListDialogFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.selected_veg_dialog_fragment_layout, null);
        getDialog().setTitle(getResources().getString(R.string.selected_vegetables_title));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        vegetableListPager = (ViewPager) rootView.findViewById(R.id.vegetable_list_pager);
        vegetableListPager.setLayoutParams(new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.show_selected_veg_details_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.show_selected_veg_details_dialog_height)));
        SelectedVegetablesViewPagerAdapter adapter = new SelectedVegetablesViewPagerAdapter(getActivity(), selectedVegetables);
        vegetableListPager.setAdapter(adapter);

        imgEdit = (ImageView) rootView.findViewById(R.id.btn_edit);
        imgEdit.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_edit: {

                // TODO Update the vegetable database here.
                SelectVegetableDialogFragment dialogFragment = new SelectVegetableDialogFragment();
                dialogFragment.setSelectedVegetables(selectedDietInfo);
                dialogFragment.setCommunicationInterface(selectVegetableInterface);
                dialogFragment.show(getFragmentManager(), "Select Vegetable");

//                selectVegetableInterface.selectVegetables(selectVegetableAdapter.getSelectedVegetables());
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

    public void setSelectedVegetable(DietPlanInfo dietPlanInfo) {

        if(dietPlanInfo != null && dietPlanInfo.getMeal() != null) {
            selectedDietInfo = dietPlanInfo;
            selectedVegetables = dietPlanInfo.getMeal().getVegetables();
        }
    }
}
