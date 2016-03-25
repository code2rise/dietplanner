package com.rise.dietplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.model.Vegetable;

import java.util.ArrayList;

/**
 * Created by Rupesh Chavan on 16/9/15.
 */
public class SelectVegetableAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private Context mContext;
    private ArrayList<Vegetable> vegetables;
    private ArrayList<Vegetable> selectedVegetables = null;

    public SelectVegetableAdapter(Context mContext, ArrayList<Vegetable> vegetables, ArrayList<Vegetable> selectedVegetables)  {
        this.mContext = mContext;
        this.vegetables = vegetables;
        this.inflater = LayoutInflater.from(mContext);
        this.selectedVegetables = selectedVegetables;
    }

    @Override
    public int getCount() {

        return vegetables.size();
    }

    @Override
    public Object getItem(int i) {

        return vegetables.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = inflater.inflate(R.layout.select_vegetable_list_item_layout, null);
        }

        final Vegetable veg = (Vegetable) getItem(i);
        TextView tvVegetableName = (TextView) view.findViewById(R.id.tvVegetableName);
        tvVegetableName.setText(veg.getTitle());

        CheckBox cbVegetableSelected = (CheckBox) view.findViewById(R.id.cbVegetableSelected);
        if (veg.isSelected()) {
            cbVegetableSelected.setChecked(true);
        }
        else {
            cbVegetableSelected.setChecked(false);
        }

        cbVegetableSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                    boolean isVegetableExist = false;
                    for(Vegetable vegetable : selectedVegetables) {
                        if (vegetable.getId() == veg.getId()) {
                            isVegetableExist = true;
                            break;
                        }
                    }

                    if (!isVegetableExist) {
                        veg.setSelected(true);
                        selectedVegetables.add(veg);
                    }
                } else {
                    int index = 0;
                    for(Vegetable vegetable : selectedVegetables) {
                        if (vegetable.getId() == veg.getId()) {
                            veg.setSelected(false);
                            selectedVegetables.remove(index);
                            break;
                        }

                        index++;
                    }
                }
            }
        });

        cbVegetableSelected.setChecked(veg.isSelected());

        return view;
    }

    public ArrayList<Vegetable> getSelectedVegetables() {

        return selectedVegetables;
    }
}
