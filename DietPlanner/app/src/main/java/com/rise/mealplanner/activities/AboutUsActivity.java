package com.rise.mealplanner.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.webkit.WebView;

import com.rise.mealplanner.R;
import com.rise.mealplanner.customviews.ActionBarCustomTitleTypeface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AboutUsActivity extends AppCompatActivity {

    private WebView wvCredits = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        SpannableString appTitle = new SpannableString("Meal Planner");
        appTitle.setSpan(new ActionBarCustomTitleTypeface(this, "fonts/Montserrat-Regular.otf"),
                0, appTitle.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(appTitle);

        wvCredits = (WebView) findViewById(R.id.wv_credits);
        try {
            InputStream creditsHtmlStream = getResources().getAssets().open("credits.html");
            BufferedReader r = new BufferedReader(new InputStreamReader(creditsHtmlStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }

            wvCredits.loadDataWithBaseURL("file:///android_asset/", total.toString(), "text/html", "utf-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
