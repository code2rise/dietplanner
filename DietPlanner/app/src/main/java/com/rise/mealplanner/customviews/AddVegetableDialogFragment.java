package com.rise.mealplanner.customviews;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rise.mealplanner.R;
import com.rise.mealplanner.adapters.NutrientListAutoCompleteAdapter;
import com.rise.mealplanner.db.DatabaseHelper;
import com.rise.mealplanner.model.Nutrient;
import com.rise.mealplanner.model.Vegetable;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by rise on 28/8/15.
 */
public class AddVegetableDialogFragment extends DialogFragment implements View.OnClickListener {

    private View rootView = null;
    private Button btnAdd = null;
    private Button btnCancel = null;
    private EditText etVegetableName = null;
    private AutoCompleteTextView etNutrients = null;
//    private ImageView imgEditPhoto = null;
    private ImageView imgVegetablePhoto = null;
    private FlowLayout llNutrientAutoCompleteContainer = null;
    private ICaptureImage iCaptureImage = null;
    private DatabaseHelper mDatabaseHelper = null;
    private LayoutInflater inflater = null;
    private String capturedImagePath = "";
//    private ArrayList<Nutrient> selectedNutrientsList = new ArrayList<>();
    private HashSet<Nutrient> selectedNutrientsList = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        mDatabaseHelper = new DatabaseHelper(getActivity());
        this.inflater = inflater;

        rootView = inflater.inflate(R.layout.add_veg_dialog_fragment_layout, null);
        getDialog().setTitle(getResources().getString(R.string.add_vegetable_dialog_box_title));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
//        imgEditPhoto = (ImageView) rootView.findViewById(R.id.imgEditPhoto);
        imgVegetablePhoto = (ImageView) rootView.findViewById(R.id.imgVegetablePhoto);
        etVegetableName = (EditText) rootView.findViewById(R.id.etVegetableName);
        etNutrients = (AutoCompleteTextView) rootView.findViewById(R.id.etNutrients);
        llNutrientAutoCompleteContainer = (FlowLayout) rootView.findViewById(R.id.llNutrientAutoCompleteContainer);

        final ArrayList<Nutrient> nutrientsList = mDatabaseHelper.getNutrientsList();
        final NutrientListAutoCompleteAdapter adapter = new NutrientListAutoCompleteAdapter(getActivity(),
                R.layout.nutrient_autocomplete_list_item_layout, nutrientsList);
        etNutrients.setAdapter(adapter);
        etNutrients.setDropDownAnchor(llNutrientAutoCompleteContainer.getId());
        etNutrients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etNutrients.setText("");
                etNutrients.setHint("");

                if(llNutrientAutoCompleteContainer.getChildCount() > 0) {
                    addSelectedVegetableLabel(nutrientsList.get(i).getNutrientName());
                        selectedNutrientsList.add(nutrientsList.get(i));
                }
            }
        });

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imgVegetablePhoto.setOnClickListener(this);

        return rootView;
    }

    private void addSelectedVegetableLabel(String nutrientName) {

        TextView tvSelectedNutrient = (TextView) inflater.inflate(R.layout.selected_nutrient_background_layout, null);
        FlowLayout.LayoutParams selectedNutrientLayoutParams = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        selectedNutrientLayoutParams.setMargins(
                getActivity().getResources().getDimensionPixelSize(R.dimen.nutrient_list_autocomplete_item_margin),
                getActivity().getResources().getDimensionPixelSize(R.dimen.nutrient_list_autocomplete_item_margin),
                getActivity().getResources().getDimensionPixelSize(R.dimen.nutrient_list_autocomplete_item_margin),
                getActivity().getResources().getDimensionPixelSize(R.dimen.nutrient_list_autocomplete_item_margin));
        tvSelectedNutrient.setLayoutParams(selectedNutrientLayoutParams);
        tvSelectedNutrient.setText(nutrientName);
        llNutrientAutoCompleteContainer.addView(tvSelectedNutrient, llNutrientAutoCompleteContainer.getChildCount()-1);

        tvSelectedNutrient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llNutrientAutoCompleteContainer.removeView(view);

                if(llNutrientAutoCompleteContainer.getChildCount() == 1) {
                    etNutrients.setHint(getActivity().getResources().getString(R.string.add_nutrient_hint));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd: {

                //Toast.makeText(getActivity(), "Add Vegetables!!", Toast.LENGTH_SHORT).show();
                String vegetableName = etVegetableName.getText().toString();

                Vegetable vegetable = new Vegetable();
                vegetable.setTitle(vegetableName);
                vegetable.setNutrientsList(new ArrayList<Nutrient>(selectedNutrientsList));
                vegetable.setImageUrl(capturedImagePath);

                mDatabaseHelper.addVegetable(vegetable);

                getDialog().dismiss();
                break;
            }
            case R.id.btnCancel: {

                getDialog().dismiss();
                break;
            }
            case R.id.imgVegetablePhoto: {

                iCaptureImage.captureImage();
                break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {

        iCaptureImage = (ICaptureImage) activity;
        super.onAttach(activity);
    }

    public static interface ICaptureImage {

        public void captureImage();
    }

    public void setImgVegetableImage(Bitmap bitmap, String capturedImagePath) {

        imgVegetablePhoto.setImageBitmap(bitmap);
        this.capturedImagePath = capturedImagePath;
    }
}
