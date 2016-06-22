package com.rise.mealplanner.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rise.mealplanner.R;
import com.rise.mealplanner.customviews.SelectVegetableDialogFragment;
import com.rise.mealplanner.interfaces.SelectVegetableInterface;
import com.rise.mealplanner.model.Meal;
import com.rise.mealplanner.model.Nutrient;
import com.rise.mealplanner.model.Vegetable;
import com.rise.mealplanner.util.HandyFunctions;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rise on 16/4/16.
 */
public class DietVegListRecyclerviewAdapter extends RecyclerView.Adapter<DietVegListRecyclerviewAdapter.CustomViewHolder> {

    private Context mContext = null;
    private LayoutInflater inflater = null;
    private Meal meal = null;
    private SelectVegetableInterface selectVegetableInterface = null;

    public DietVegListRecyclerviewAdapter(Context mContext, SelectVegetableInterface selectVegetableInterface, Meal meal) {
        this.mContext = mContext;
        this.meal = meal;
        this.selectVegetableInterface = selectVegetableInterface;

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
        cardViewParent.width = cardViewParent.width - new HandyFunctions(mContext).dpToPx(20);

        String protocol = "file:///android_asset/";
        if(vegetable.getImageUrl() != null && vegetable.getImageUrl().length() > 0) {
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
        }
        else {
            holder.imgVegetablePic.setImageResource(R.drawable.ic_veg);
        }

        holder.tvVegetableName.setText(vegetable.getTitle());

        StringBuilder nutrients = new StringBuilder();
        for (Nutrient nutrient : vegetable.getNutrientsList()) {
            if(nutrients.length() > 0) {
                nutrients.append(", ");
            }

            nutrients.append(nutrient.getNutrientName());
        }

        if(nutrients.length() > 0) {
            holder.tvNutrientsList.setText(nutrients.toString());
        }
        else {
            holder.tvNutrientsList.setText("Nutrient Information Unavailable!");
        }

        holder.tvMealName.setText(meal.getMealCode());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayVegetableSelectionPopup(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meal.getVegetables().size();
    }

    private void displayVegetableSelectionPopup(Meal meal) {
        SelectVegetableDialogFragment dialogFragment = new SelectVegetableDialogFragment();
        dialogFragment.setSelectedMeal(meal);
        dialogFragment.setCommunicationInterface(selectVegetableInterface);
        dialogFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "Select Vegetable");
    }
}
