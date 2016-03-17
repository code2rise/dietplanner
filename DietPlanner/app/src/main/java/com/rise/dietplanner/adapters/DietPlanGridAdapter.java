package com.rise.dietplanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.model.DietPlanInfo;
import com.rise.dietplanner.model.Vegetable;
import com.rise.dietplanner.util.HandyFunctions;

import java.util.ArrayList;

/**
 * Created by rise on 30/8/15.
 */
public class DietPlanGridAdapter extends BaseAdapter {

    private Context mContext = null;
    private DietPlanInfo[] dashboardData = null;
    private LayoutInflater inflater = null;
    private HandyFunctions handyFunctions = null;

    public DietPlanGridAdapter(Context mContext, DietPlanInfo[] dashboardData) {

        this.mContext = mContext;
        this.dashboardData = dashboardData;
        inflater = LayoutInflater.from(mContext);
        handyFunctions = new HandyFunctions(mContext);
    }

    @Override
    public int getCount() {

        return dashboardData.length;
    }

    @Override
    public Object getItem(int i) {

        return dashboardData[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.week_data_grid_view_item_layout, null);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    handyFunctions.dpToPx(54));

            view.setLayoutParams(layoutParams);
        }

        DietPlanInfo dietPlanInfo = (DietPlanInfo) getItem(i);
        ImageView imgAddVeg = (ImageView) view.findViewById(R.id.imgAddVeg);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        LinearLayout llSelectedVegContainer = (LinearLayout) view.findViewById(R.id.llSelectedVegContainer);
        llSelectedVegContainer.setPadding(handyFunctions.dpToPx(5), handyFunctions.dpToPx(2),
                                          handyFunctions.dpToPx(5), handyFunctions.dpToPx(2));
        llSelectedVegContainer.removeAllViews();

        if (dietPlanInfo.isHeader()) {
            tvTitle.setText(dietPlanInfo.getTitle());
            tvTitle.setVisibility(View.VISIBLE);
            view.setBackgroundColor(Color.LTGRAY);
        } else {

            ArrayList<Vegetable> vegetables = dietPlanInfo.getSelectedVegetableArrayList();
            if (vegetables != null && vegetables.size() > 0) {
                StringBuilder sb = new StringBuilder();
                int count = 0;

                while (count < vegetables.size()) {

                    Vegetable vegetable = vegetables.get(count);

                    TextView tvVegetableName = new TextView(mContext);
                    tvVegetableName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tvVegetableName.setSingleLine(true);
                    tvVegetableName.setEllipsize(TextUtils.TruncateAt.END);
                    tvVegetableName.setText(vegetable.getTitle());
                    tvVegetableName.setPadding(handyFunctions.dpToPx(2), handyFunctions.dpToPx(0),
                            handyFunctions.dpToPx(2), handyFunctions.dpToPx(0));
                    tvVegetableName.setTextColor(mContext.getResources().getColor(R.color.white));

                    if(count % 3 == 0) {

                        Drawable mDrawable = getVegetableBackground("#FA8258");
                        if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            tvVegetableName.setBackgroundDrawable(mDrawable);
                        } else {
                            tvVegetableName.setBackground(mDrawable);
                        }
                    }
                    else if(count % 2 == 0) {

                        Drawable mDrawable = getVegetableBackground("#D358F7");
                        if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            tvVegetableName.setBackgroundDrawable(mDrawable);
                        } else {
                            tvVegetableName.setBackground(mDrawable);
                        }
                    }
                    else {

                        Drawable mDrawable = getVegetableBackground("#04B486");
                        if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            tvVegetableName.setBackgroundDrawable(mDrawable);
                        } else {
                            tvVegetableName.setBackground(mDrawable);
                        }
                    }

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(handyFunctions.dpToPx(0), handyFunctions.dpToPx(1),
                            handyFunctions.dpToPx(0), handyFunctions.dpToPx(1));

                    llSelectedVegContainer.addView(tvVegetableName, layoutParams);

                    count++;
                }

                llSelectedVegContainer.setVisibility(View.VISIBLE);
                imgAddVeg.setVisibility(View.GONE);
            } else {
                imgAddVeg.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    private Drawable getVegetableBackground(String colorString) {

        Drawable mDrawable = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDrawable = mContext.getResources().getDrawable(R.drawable.selected_veg_indicator_drawable, mContext.getTheme());
        }
        else {
            mDrawable = mContext.getResources().getDrawable(R.drawable.selected_veg_indicator_drawable);
        }

        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
        mDrawable.setColorFilter(Color.parseColor(colorString), mMode);

        return mDrawable;
    }
}
