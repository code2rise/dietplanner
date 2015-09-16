package com.rise.dietplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.model.DietPlanInfo;
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

        if(view == null) {
            view = inflater.inflate(R.layout.week_data_grid_view_item_layout, null);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    handyFunctions.dpToPx(48));

            view.setLayoutParams(layoutParams);
        }

        DietPlanInfo dietPlanInfo = (DietPlanInfo) getItem(i);

        if( dietPlanInfo.isHeader() ) {
            TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTitle.setText(dietPlanInfo.getTitle());
            tvTitle.setVisibility(View.VISIBLE);
        }
        else {

            ImageView imgAddVeg = (ImageView) view.findViewById(R.id.imgAddVeg);
            imgAddVeg.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
