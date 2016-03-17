package com.rise.dietplanner.customviews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.adapters.SelectVegetableAdapter;
import com.rise.dietplanner.db.DatabaseHelper;
import com.rise.dietplanner.fragments.HomeFragment;
import com.rise.dietplanner.model.Vegetable;

import java.util.ArrayList;

/**
 * Created by rise on 16/9/15.
 */
public class SelectVegetableDialogFragment extends DialogFragment implements View.OnClickListener {

    private View rootView = null;
    private ListView lvVegetableList = null;
    private Button btnOk = null;
    private Button btnCancel = null;
    private SelectVegetableInterface selectVegetableInterface;
    private DatabaseHelper mDatabaseHelper = null;
    private ArrayList<Vegetable> selectedVegetables = new ArrayList<>();
    private SelectVegetableAdapter selectVegetableAdapter = null;

    public SelectVegetableDialogFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.select_veg_dialog_fragment_layout, null);
        getDialog().setTitle(getResources().getString(R.string.add_vegetable_dialog_box_title));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        lvVegetableList = (ListView) rootView.findViewById(R.id.lvVegetableList);

        ArrayList<Vegetable> vegetablesList = new ArrayList<>();
        mDatabaseHelper = new DatabaseHelper(getActivity());
        vegetablesList = mDatabaseHelper.getVegetablesList();

        selectVegetableAdapter = new SelectVegetableAdapter(getActivity(), vegetablesList);
        lvVegetableList.setAdapter(selectVegetableAdapter);

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

                selectVegetableInterface.selectVegetables(selectVegetableAdapter.getSelectedVegetables());
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

    public interface SelectVegetableInterface {

        public void selectVegetables(ArrayList<Vegetable> vegetablesInfo);
    }
}
