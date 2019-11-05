package com.rise.mealplanner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rise.mealplanner.R;
import com.rise.mealplanner.customviews.ActionBarCustomTitleTypeface;
import com.rise.mealplanner.fragments.AddVegetableDialogFragment;
import com.rise.mealplanner.db.DatabaseHelper;
import com.rise.mealplanner.fragments.DailyDietFragment;
import com.rise.mealplanner.fragments.HomeFragment;
import com.rise.mealplanner.fragments.WeeklyDietFragment;
import com.rise.mealplanner.util.HandyFunctions;
import com.rise.mealplanner.util.ImageUtility;

import java.io.File;
import java.text.SimpleDateFormat;
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

        SpannableString appTitle = new SpannableString("Meal Planner");
        appTitle.setSpan(new ActionBarCustomTitleTypeface(this, "fonts/Montserrat-Regular.otf"),
                0, appTitle.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(appTitle);

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
        switchFragment(fragment, WEEKLY_DIET_FRAGMENT, false);
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
        else if(id == R.id.action_view) {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flFragmentContainer);

            if(fragment != null && fragment instanceof WeeklyDietFragment) {
                item.setIcon(R.drawable.ic_grid_view);
                DailyDietFragment dailyDietFragment = DailyDietFragment.newInstance();
                switchFragment(dailyDietFragment, DAILY_DIET_FRAGMENT, true);
            }
            else if(fragment != null && fragment instanceof DailyDietFragment) {
                item.setIcon(R.drawable.ic_list_view);
                WeeklyDietFragment weeklyDietFragment = WeeklyDietFragment.newInstance();
                switchFragment(weeklyDietFragment, WEEKLY_DIET_FRAGMENT, false);
            }

            return true;
        }
        else if(id == R.id.action_about_us) {
            Intent aboutUsActivityIntent = new Intent(DietPlannerActivity.this,
                    AboutUsActivity.class);
            startActivity(aboutUsActivityIntent);

            return true;
        }
        else if(id == R.id.action_feedback) {
            Intent launchEmailIntent = new Intent(Intent.ACTION_SENDTO);
            launchEmailIntent.setData(Uri.parse("mailto:"));
            launchEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"cr.rupesh@gmail.com"});
            launchEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "MealPlanner Feedback");
            startActivity(Intent.createChooser(launchEmailIntent, "Send Email"));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment, String fragmentTag, boolean isAddToBackstack) {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flFragmentContainer);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(currentFragment == null) {
            fragmentTransaction.add(R.id.flFragmentContainer, fragment, fragmentTag);
            if(isAddToBackstack) {
                fragmentTransaction.addToBackStack(fragmentTag);
            }
            fragmentTransaction.commit();
        }
        else {
            if(isAddToBackstack) {
                fragmentTransaction.add(R.id.flFragmentContainer, fragment, fragmentTag);
                fragmentTransaction.addToBackStack(fragmentTag);
                fragmentTransaction.commit();
            }
            else if(currentFragment instanceof DailyDietFragment) {
                getSupportFragmentManager().popBackStack();
                fragmentTransaction.replace(R.id.flFragmentContainer, fragment, fragmentTag);
                fragmentTransaction.commit();
            }
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

//                Bitmap decodedBitmap = imageUtility.decodeFile(capturedImagePath);
                Bitmap decodedBitmap = (Bitmap) data.getExtras().get("data");
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
        Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", capturedImagePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

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
