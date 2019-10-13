package com.rise.mealplanner.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import com.rise.mealplanner.util.FontCache;

/**
 * Created by rise on 30/6/16.
 */
public class ActionBarCustomTitleTypeface extends MetricAffectingSpan {

    private Typeface mTypeface;

    public ActionBarCustomTitleTypeface(Context context, String typefaceName) {

        mTypeface = FontCache.get("fonts/Montserrat-Regular.otf", context);
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
