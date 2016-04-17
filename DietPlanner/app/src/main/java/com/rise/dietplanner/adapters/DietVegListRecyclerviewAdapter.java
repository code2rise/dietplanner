package com.rise.dietplanner.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.model.Meal;
import com.rise.dietplanner.model.Vegetable;
import com.rise.dietplanner.util.HandyFunctions;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rise on 16/4/16.
 */
public class DietVegListRecyclerviewAdapter extends RecyclerView.Adapter<DietVegListRecyclerviewAdapter.CustomViewHolder> {

    private Context mContext = null;
    private LayoutInflater inflater = null;
    private Meal meal = null;

    public DietVegListRecyclerviewAdapter(Context mContext, Meal meal) {
        this.mContext = mContext;
        this.meal = meal;

        inflater = LayoutInflater.from(mContext);
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView imgVegetablePic = null;
        TextView tvVegetableName = null;
        TextView tvNutrientsList = null;
        TextView tvMealName = null;
        CardView mealVegetableDetailCardviewLayout = null;

        public CustomViewHolder(View itemView) {
            super(itemView);

            imgVegetablePic = (ImageView) itemView.findViewById(R.id.img_vegetable_pic);
            tvVegetableName = (TextView) itemView.findViewById(R.id.tv_vegetable_name);
            tvNutrientsList = (TextView) itemView.findViewById(R.id.tv_nutrients_list);
            tvMealName = (TextView) itemView.findViewById(R.id.tv_meal_name);
            mealVegetableDetailCardviewLayout = (CardView) itemView.findViewById(R.id.meal_vegetable_detail_cardview_layout);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View dietVegetableListView = inflater.inflate(R.layout.daily_diet_meal_layout, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(dietVegetableListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Vegetable vegetable = meal.getVegetables().get(position);

        ViewGroup.LayoutParams cardViewParent = holder.mealVegetableDetailCardviewLayout.getLayoutParams();
        cardViewParent.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels - (2 * mContext.getResources().getDisplayMetrics().density));
        cardViewParent.width = cardViewParent.width - new HandyFunctions(mContext).dpToPx(10);

        String protocol = "file:///android_asset/";
        if(vegetable.getImageUrl().contains(protocol)) {

            try {
                String imageUrl = vegetable.getImageUrl().substring(protocol.length());
                AssetManager assetManager = mContext.getAssets();
                InputStream inputStream = assetManager.open(imageUrl);
                Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
                holder.imgVegetablePic.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Bitmap imageBitmap = BitmapFactory.decodeFile(vegetable.getImageUrl());
            holder.imgVegetablePic.setImageBitmap(imageBitmap);
        }

        holder.tvVegetableName.setText(vegetable.getTitle());

//        holder.tvNutrientsList.setText(vegetable.getNutrientsList());
        holder.tvNutrientsList.setText("Nutrient Information Unavailable!");

        holder.tvMealName.setText(meal.getMealCode());
    }

    @Override
    public int getItemCount() {
        return meal.getVegetables().size();
    }
}