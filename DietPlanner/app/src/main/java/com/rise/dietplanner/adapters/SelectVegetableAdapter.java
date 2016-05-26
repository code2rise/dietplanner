package com.rise.dietplanner.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.model.Vegetable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = inflater.inflate(R.layout.select_vegetable_list_item_layout, null);
        }

        final Vegetable veg = (Vegetable) getItem(i);
        TextView tvVegetableName = (TextView) view.findViewById(R.id.tvVegetableName);
        tvVegetableName.setText(veg.getTitle());

        ImageView imgVegetablePhoto = (ImageView) view.findViewById(R.id.imgVegetablePhoto);
        if(veg.getImageUrl() != null && !veg.getImageUrl().equals("")) {
            String protocol = "file:///android_asset/";
            if(veg.getImageUrl().contains(protocol)) {

                try {
                    String imageUrl = veg.getImageUrl().substring(protocol.length());
                    AssetManager assetManager = mContext.getAssets();
                    InputStream inputStream = assetManager.open(imageUrl);
                    Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
                    imgVegetablePhoto.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Bitmap imageBitmap = BitmapFactory.decodeFile(veg.getImageUrl());
                imgVegetablePhoto.setImageBitmap(imageBitmap);
            }
        }

        CheckBox cbVegetableSelected = (CheckBox) view.findViewById(R.id.cbVegetableSelected);
        if (veg.isSelected()) {
            cbVegetableSelected.setOnCheckedChangeListener(null);
            cbVegetableSelected.setChecked(true);
        }
        else {
            cbVegetableSelected.setOnCheckedChangeListener(null);
            cbVegetableSelected.setChecked(false);
        }

        cbVegetableSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                Vegetable veg = vegetables.get(i);
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
