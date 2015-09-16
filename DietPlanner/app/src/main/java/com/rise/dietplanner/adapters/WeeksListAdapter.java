package com.rise.dietplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.model.Week;
import com.rise.dietplanner.util.HandyFunctions;

import java.util.ArrayList;

/**
 * Created by rise on 29/8/15.
 */
public class WeeksListAdapter extends BaseAdapter {

    ArrayList<Week> weeksList = new ArrayList<>(52);
    Context mContext = null;
    LayoutInflater inflater = null;
    HandyFunctions handyFunctions = null;

    public WeeksListAdapter(Context mContext, ArrayList<Week> weeks) {
        weeksList = weeks;
        inflater = LayoutInflater.from(mContext);
        handyFunctions = new HandyFunctions(mContext);
    }

    @Override
    public int getCount() {
        return weeksList.size();
    }

    @Override
    public Object getItem(int i) {
        return weeksList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = inflater.inflate(R.layout.weeks_spinner_item_layout, null);
        }

        TextView tvWeekname = (TextView) view.findViewById(R.id.tvWeekname);
        TextView tvWeekDetails = (TextView) view.findViewById(R.id.tvWeekDetails);

        Week week = (Week) getItem(i);
        tvWeekname.setText("Week " + week.getWeekNumber());

        String startDate = handyFunctions.getDateFormatter().format(week.getStartOfWeek());
        String endDate = handyFunctions.getDateFormatter().format(week.getEndOfWeek());

        tvWeekDetails.setText(" (" + startDate + " - " + endDate +")");

        return view;
    }
}
