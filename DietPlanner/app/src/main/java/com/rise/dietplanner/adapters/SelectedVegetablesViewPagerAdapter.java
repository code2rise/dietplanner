package com.rise.dietplanner.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.model.Nutrient;
import com.rise.dietplanner.model.Vegetable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by rise on 4/4/16.
 */
public class SelectedVegetablesViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Vegetable> vegetables;
    private View rootView;

    public SelectedVegetablesViewPagerAdapter(Context mContext, ArrayList<Vegetable> vegetables) {
        this.mContext = mContext;
        this.vegetables= vegetables;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return vegetables.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Vegetable vegetable = vegetables.get(position);
        rootView = inflater.inflate(R.layout.show_vegetable_detail_layout, container, false);

        TextView tvVegetableName = (TextView) rootView.findViewById(R.id.tvVegetableName);
        TextView tvNutrients = (TextView) rootView.findViewById(R.id.tvNutrients);
        ImageView imgVegetablePhoto = (ImageView) rootView.findViewById(R.id.imgVegetablePhoto);

        tvVegetableName.setText(vegetable.getTitle());

        StringBuilder nutrientList = new StringBuilder();
        for (Nutrient nutrient : vegetable.getNutrientsList()) {
            if(nutrientList.length() > 0) {
                nutrientList.append(", ");
            }
            nutrientList.append(nutrient.getNutrientName());
        }

        if(nutrientList.length() > 0) {
            tvNutrients.setText(nutrientList);
        }
        else {
            tvNutrients.setText("Nutrient information unavailable!");
        }


        String protocol = "file:///android_asset/";
        if(vegetable.getImageUrl().contains(protocol)) {

            try {
                String imageUrl = vegetable.getImageUrl().substring(protocol.length());
                AssetManager assetManager = mContext.getAssets();
                InputStream inputStream = assetManager.open(imageUrl);
                Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
                imgVegetablePhoto.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        Uri fileUri = Uri.parse(vegetable.getImageUrl());
//        Bitmap vegetableBitmap = BitmapFactory.decodeFile();
//        imgVegetablePhoto.setImageURI(fileUri);

        ((ViewPager) container).addView(rootView);

        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}
