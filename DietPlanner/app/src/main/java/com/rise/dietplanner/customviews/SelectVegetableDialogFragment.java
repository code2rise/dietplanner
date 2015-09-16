package com.rise.dietplanner.customviews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.adapters.SelectVegetableAdapter;
import com.rise.dietplanner.model.Vegetable;

import java.util.ArrayList;

/**
 * Created by rise on 16/9/15.
 */
public class SelectVegetableDialogFragment extends DialogFragment {

    private View rootView = null;
    private ListView lvVegetableList = null;
    private Button btnOk = null;
    private Button btnCancel = null;

    public SelectVegetableDialogFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.select_veg_dialog_fragment_layout, null);
        getDialog().setTitle(getResources().getString(R.string.add_vegetable_dialog_box_title));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        lvVegetableList = (ListView) rootView.findViewById(R.id.lvVegetableList);

        Vegetable veg1 = new Vegetable();
        veg1.setTitle("Corn");

        Vegetable veg2 = new Vegetable();
        veg2.setTitle("Spinach");

        Vegetable veg3 = new Vegetable();
        veg3.setTitle("Potato");

        ArrayList<Vegetable> vegetables = new ArrayList<>();
        vegetables.add(veg1);
        vegetables.add(veg2);
        vegetables.add(veg3);

        SelectVegetableAdapter adapter = new SelectVegetableAdapter(getActivity(), vegetables);
        lvVegetableList.setAdapter(adapter);

        btnOk = (Button) rootView.findViewById(R.id.btnOk);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);

        return rootView;
    }
}
