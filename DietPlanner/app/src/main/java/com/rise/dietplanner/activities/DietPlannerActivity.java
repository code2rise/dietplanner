package com.rise.dietplanner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.rise.dietplanner.R;
import com.rise.dietplanner.adapters.WeeksListAdapter;
import com.rise.dietplanner.customviews.AddVegetableDialogFragment;
import com.rise.dietplanner.db.DatabaseHelper;
import com.rise.dietplanner.fragments.DailyDietFragment;
import com.rise.dietplanner.fragments.HomeFragment;
import com.rise.dietplanner.fragments.WeeklyDietFragment;
import com.rise.dietplanner.model.Week;
import com.rise.dietplanner.util.CalendarGenerator;
import com.rise.dietplanner.util.HandyFunctions;
import com.rise.dietplanner.util.ImageUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class DietPlannerActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener, AddVegetableDialogFragment.ICaptureImage {

    private static final String HOME_FRAGMENT = "HOME_FRAGMENT";
    private static final String WEEKLY_DIET_FRAGMENT = "WEEKLY_DIET_FRAGMENT";
    private static final String DAILY_DIET_FRAGMENT = "DAILY_DIET_FRAGMENT";
    private static final int SETTINGS_FRAGMENT = 4;

    public static final String PREFERENCE_NAME = "DIET_PLANNER";
    private Spinner spWeeks = null;
    private AddVegetableDialogFragment dialogFragment = null;
    private ImageUtility imageUtility = null;
    private HandyFunctions handyFunctions = null;
    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_planner);

        databaseHelper = new DatabaseHelper(this);
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        boolean isFirstLogin = preferences.getBoolean("isFirstLogin", true);
        if(isFirstLogin) {
            //databaseHelper.insertVegetablesInDatabase();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstLogin", false);
            editor.commit();
        }

        imageUtility = new ImageUtility(getApplicationContext());
        handyFunctions = new HandyFunctions(getApplicationContext());

        CalendarGenerator calendarGenerator = CalendarGenerator.getInstance();
        spWeeks = (Spinner) findViewById(R.id.spWeeks);
        ArrayList<Week> weeks = calendarGenerator.getWeeksList();
        WeeksListAdapter adapter = new WeeksListAdapter(this, weeks);
        spWeeks.setAdapter(adapter);
        spWeeks.setSelection(calendarGenerator.getCurrentWeekNumber());
//        spWeeks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                WeeklyDietFragment fragment = WeeklyDietFragment.newInstance();
//                Bundle bundle = new Bundle();
//                bundle.putInt("weekIndex", i);
//                fragment.setArguments(bundle);
//                switchFragment(fragment, WEEKLY_DIET_FRAGMENT);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        int currentWeekIndex = CalendarGenerator.getInstance().getCurrentWeekNumber();
//        WeeklyDietFragment fragment = WeeklyDietFragment.newInstance();
//        Bundle bundle = new Bundle();
//        bundle.putInt("weekIndex", currentWeekIndex);
//        fragment.setArguments(bundle);
//        switchFragment(fragment, WEEKLY_DIET_FRAGMENT);

        DailyDietFragment fragment = DailyDietFragment.newInstance();
        switchFragment(fragment, DAILY_DIET_FRAGMENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diet_planner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        else
        if(id == R.id.action_add) {
            dialogFragment = new AddVegetableDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "Add Vegetable");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment, String fragmentTag) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flFragmentContainer, fragment, fragmentTag);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if request code is same we pass as argument in startActivityForResult
        if(requestCode==1){
            //create instance of File with same name we created before to get image from storage
//            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");

//            //Crop the captured image using an other intent
//            try {
//                /*the user's device may not support cropping*/
//                cropCapturedImage(Uri.fromFile(file));
//            }
//            catch(ActivityNotFoundException aNFE){
//                //display an error message if user device doesn't support
//                String errorMessage = "Sorry - your device doesn't support the crop action!";
//                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//                toast.show();
//            }

            //Create an instance of bundle and get the returned data
//            Bundle extras = data.getExtras();
            //get the cropped bitmap from extras
//            Bitmap originalBitmap = extras.getParcelable("data");

            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
            Bitmap decodedBitmap = imageUtility.decodeFile(file);
            String capturedImagePath = imageUtility.saveCapturedImage(decodedBitmap);

//            Bitmap bitmap = BitmapFactory.decodeFile(
//                    Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
            //set image bitmap to image view
            dialogFragment.setImgVegetableImage(decodedBitmap, capturedImagePath);
        }
//        if(requestCode==2){
//            //Create an instance of bundle and get the returned data
//            Bundle extras = data.getExtras();
//            //get the cropped bitmap from extras
//            Bitmap thePic = extras.getParcelable("data");
//            //set image bitmap to image view
//            dialogFragment.setImgVegetableImage(thePic);
//        }
    }

//    //create helping method cropCapturedImage(Uri picUri)
//    public void cropCapturedImage(Uri picUri){
//        //call the standard crop action intent
//        Intent cropIntent = new Intent("com.android.camera.action.CROP");
//        //indicate image type and Uri of image
//        cropIntent.setDataAndType(picUri, "image/*");
//        //set crop properties
//        cropIntent.putExtra("crop", "true");
//        //indicate aspect of desired crop
//        cropIntent.putExtra("aspectX", 1);
//        cropIntent.putExtra("aspectY", 1);
//        //indicate output X and Y
//        cropIntent.putExtra("outputX", 256);
//        cropIntent.putExtra("outputY", 256);
//        //retrieve data on return
//        cropIntent.putExtra("return-data", true);
//        //start the activity - we handle returning in onActivityResult
//        startActivityForResult(cropIntent, 2);
//    }

    @Override
    public void captureImage() {

        /* create an instance of intent
         * pass action android.media.action.IMAGE_CAPTURE
         * as argument to launch camera
        */
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        /*create instance of File with name img.jpg*/
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
        /*put uri as extra in intent object*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        intent.putExtra("return-data", true);
        /*start activity for result pass intent as argument and request code */
        startActivityForResult(intent, 1);
    }
}
