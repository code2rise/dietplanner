package com.rise.dietplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.rise.dietplanner.R;
import com.rise.dietplanner.model.Nutrient;

import java.util.ArrayList;

/**
 * Created by rise on 9/3/16.
 */
public class NutrientListAutoCompleteAdapter extends ArrayAdapter<Nutrient> {

    private Context mContext;
    private ArrayList<Nutrient> nutrientsList;
    private LayoutInflater inflater;
    private int layoutResourceId;
    private ArrayList<Nutrient> suggestions;
    private ArrayList<Nutrient> originalList;

    public NutrientListAutoCompleteAdapter(Context mContext, int layoutResourceId, ArrayList<Nutrient> nutrientsList) {

        super(mContext, layoutResourceId, nutrientsList);

        this.mContext = mContext;
        this.nutrientsList = nutrientsList;
        this.originalList = (ArrayList<Nutrient>) nutrientsList.clone();
        this.suggestions = new ArrayList<Nutrient>();
        this.layoutResourceId = layoutResourceId;
        inflater = LayoutInflater.from(mContext);
    }

    static class ViewHolder {
        public TextView tvNutrientName;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(layoutResourceId, viewGroup, false);
//            view.setPadding();

            viewHolder = new ViewHolder();
            viewHolder.tvNutrientName = (TextView) view.findViewById(R.id.tv_nutrient_name);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Nutrient nutrient = nutrientsList.get(i);
        viewHolder.tvNutrientName.setText(nutrient.getNutrientName());

        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        public String convertResultToString(Object resultValue) {
            String str = ((Nutrient) (resultValue)).getNutrientName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Nutrient nutrient : originalList) {
                    if (nutrient.getNutrientName().toLowerCase()
                            .startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(nutrient);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<Nutrient> filteredList = (ArrayList<Nutrient>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Nutrient c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
