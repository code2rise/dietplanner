package com.rise.mealplanner.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.rise.mealplanner.util.FontCache;

/**
 * Created by rise on 15/6/16.
 */
public class CustomEditTextMontSerratLightFont extends EditText {

    public CustomEditTextMontSerratLightFont(Context context) {
        super(context);
        setFont(context);
    }

    public CustomEditTextMontSerratLightFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public CustomEditTextMontSerratLightFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomEditTextMontSerratLightFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setFont(context);
    }

    private void setFont(Context context) {
        Typeface font = FontCache.get("fonts/Montserrat-Light.otf", context);

        if(getTypeface().isBold()) {
            setTypeface(font, Typeface.BOLD);
        }
        else {
            setTypeface(font, Typeface.NORMAL);
        }
    }
}
