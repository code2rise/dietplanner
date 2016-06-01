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
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DietPlannerActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener, AddVegetableDialogFragment.ICaptureImage {

    private static final String HOME_FRAGMENT = "HOME_FRAGMENT";
    private static final String WEEKLY_DIET_FRAGMENT = "WEEKLY_DIET_FRAGMENT";
    private static final String DAILY_DIET_FRAGMENT = "DAILY_DIET_FRAGMENT";
    private static final int SETTINGS_FRAGMENT = 4;

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "MealPlanner/Images";
    private Uri fileUri; // file url to store image/video

    public static final String PREFERENCE_NAME = "DIET_PLANNER";
    private AddVegetableDialogFragment dialogFragment = null;
    private ImageUtility imageUtility = null;
    private HandyFunctions handyFunctions = null;
    private DatabaseHelper databaseHelper = null;
    private File capturedImagePath = null;

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

        WeeklyDietFragment fragment = WeeklyDietFragment.newInstance();
        switchFragment(fragment, WEEKLY_DIET_FRAGMENT);
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
        else if(id == R.id.action_weekly_view) {

            WeeklyDietFragment fragment = WeeklyDietFragment.newInstance();
            switchFragment(fragment, WEEKLY_DIET_FRAGMENT);
            return true;
        }
        else if(id == R.id.action_daily_view) {

            DailyDietFragment fragment = DailyDietFragment.newInstance();
            switchFragment(fragment, DAILY_DIET_FRAGMENT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment, String fragmentTag) {

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);

        if(currentFragment == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flFragmentContainer, fragment, fragmentTag);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if request code is same we pass as argument in startActivityForResult
        if(requestCode==1){

            if(resultCode == RESULT_OK) {

                Bitmap decodedBitmap = imageUtility.decodeFile(capturedImagePath);
                String capturedImagePath = imageUtility.saveCapturedImage(decodedBitmap);

                //set image bitmap to image view
                dialogFragment.setImgVegetableImage(decodedBitmap, capturedImagePath);
            }
            else if(resultCode == RESULT_CANCELED) {
                // Do nothing
            }
            else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void captureImage() {

        /* create an instance of intent
         * pass action android.media.action.IMAGE_CAPTURE
         * as argument to launch camera
        */
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        /*create instance of File with name img.jpg*/
        capturedImagePath = getVegetableImagePath();
        /*put uri as extra in intent object*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(capturedImagePath));
        /*start activity for result pass intent as argument and request code */
        startActivityForResult(intent, 1);
    }

    private File getVegetableImagePath() {
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        File appDirectory = new File(Environment.getExternalStorageDirectory() + File.separator +
                "MealPlanner" + File.separator);
        if( !appDirectory.exists() ) {
            appDirectory.mkdir();
        }

        File capturedImageFile = new File(appDirectory.getAbsolutePath() + timeStamp + ".jpg");

        return capturedImageFile;
    }
}
