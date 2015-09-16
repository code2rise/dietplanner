package com.rise.dietplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    public SelectVegetableAdapter(Context mContext, ArrayList<Vegetable> vegetables)  {
        this.mContext = mContext;
        this.vegetables = vegetables;
        this.inflater = LayoutInflater.from(mContext);
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

        Vegetable veg = (Vegetable) getItem(i);
        TextView tvVegetableName = (TextView) view.findViewById(R.id.tvVegetableName);
        tvVegetableName.setText(veg.getTitle());

        return view;
    }
}
