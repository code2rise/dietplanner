package com.rise.mealplanner.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rise.mealplanner.R;
import com.rise.mealplanner.model.Vegetable;
import com.rise.mealplanner.util.HandyFunctions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Rupesh Chavan on 16/9/15.
 */
public class SelectVegetableAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private Context mContext;
    private ArrayList<Vegetable> vegetables;
    private ArrayList<Vegetable> selectedVegetables = null;
    private HandyFunctions handyFunctions = null;

    public SelectVegetableAdapter(Context mContext, ArrayList<Vegetable> vegetables, ArrayList<Vegetable> selectedVegetables)  {
        this.mContext = mContext;
        this.vegetables = vegetables;
        this.inflater = LayoutInflater.from(mContext);
        this.selectedVegetables = selectedVegetables;
        this.handyFunctions = new HandyFunctions(mContext);
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
        else {
            imgVegetablePhoto.setImageResource(R.drawable.ic_veg);
        }

        final RelativeLayout rlVegetableContainer = (RelativeLayout) view.findViewById(
                R.id.rlVegetableContainer);
        if(veg.isSelected()) {
            rlVegetableContainer.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        }
        else {
            rlVegetableContainer.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        rlVegetableContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean isVegetableExist = false;

                Vegetable veg = vegetables.get(i);
                for(Vegetable vegetable : selectedVegetables) {
                    if (vegetable.getId() == veg.getId()) {
                        isVegetableExist = true;
                        break;
                    }
                }

                if (!isVegetableExist) {
                    veg.setSelected(true);
                    selectedVegetables.add(veg);
                    rlVegetableContainer.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                }
                else {
                    int index = 0;
                    for(Vegetable vegetable : selectedVegetables) {
                        if (vegetable.getId() == veg.getId()) {
                            veg.setSelected(false);
                            selectedVegetables.remove(index);
                            break;
                        }

                        index++;
                    }

                    rlVegetableContainer.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                }
            }
        });

        return view;
    }

    public ArrayList<Vegetable> getSelectedVegetables() {

        return selectedVegetables;
    }
}
