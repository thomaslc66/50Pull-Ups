package com.pullups.android.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pullups.android.R;
import com.pullups.android.TextView.RobotoFont;

/**
 * Created by Thomas on 07.07.2015.
 */
public class CustomFontRobotoHelper {

    /**
     * Sets a font on a textview based on the custom com.pullups.thomas.pullup:font attribute
     * If the custom font attribute isn't found in the attributes nothing happens
     * @param textview
     * @param context
     * @param attrs
     */
    public static void setCustomFont(TextView textview, Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RobotoFont);
        String font = a.getString(R.styleable.RobotoFont_fontRoboto);
        setCustomFont(textview, font, context);
        a.recycle();
    }

    /**
     * Sets a font on a textview
     * @param textview
     * @param font
     * @param context
     */
    public static void setCustomFont(TextView textview, String font, Context context) {
        if(font == null) {
            return;
        }
        Typeface tf = RobotoFont.get(font, context);
        if(tf != null) {
            textview.setTypeface(tf);
        }
    }

}
