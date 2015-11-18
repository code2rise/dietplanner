package com.rise.dietplanner.customviews;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rise.dietplanner.R;
import com.rise.dietplanner.db.DatabaseHelper;

import java.io.File;

/**
 * Created by rise on 28/8/15.
 */
public class AddVegetableDialogFragment extends DialogFragment implements View.OnClickListener {

    private View rootView = null;
    private Button btnAdd = null;
    private Button btnCancel = null;
    private EditText etVegetableName = null;
    private AutoCompleteTextView etNutrients = null;
    private ImageView imgEditPhoto = null;
    private ImageView imgVegetablePhoto = null;
    private ICaptureImage iCaptureImage = null;
    private DatabaseHelper mDatabaseHelper = null;
    private String capturedImagePath = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.add_veg_dialog_fragment_layout, null);
        getDialog().setTitle(getResources().getString(R.string.add_vegetable_dialog_box_title));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        imgEditPhoto = (ImageView) rootView.findViewById(R.id.imgEditPhoto);
        imgVegetablePhoto = (ImageView) rootView.findViewById(R.id.imgVegetablePhoto);
        etVegetableName = (EditText) rootView.findViewById(R.id.etVegetableName);
        etNutrients = (AutoCompleteTextView) rootView.findViewById(R.id.etNutrients);

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imgEditPhoto.setOnClickListener(this);

        mDatabaseHelper = new DatabaseHelper(getActivity());

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd: {

                Toast.makeText(getActivity(), "Add Vegetables!!", Toast.LENGTH_SHORT).show();
                String vegetableName = etVegetableName.getText().toString();
                String vegetableNutrients = etNutrients.getText().toString();
                mDatabaseHelper.addVegetable(vegetableName, vegetableNutrients, capturedImagePath);

                getDialog().dismiss();
                break;
            }
            case R.id.btnCancel: {

                getDialog().dismiss();
                break;
            }
            case R.id.imgEditPhoto: {

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
