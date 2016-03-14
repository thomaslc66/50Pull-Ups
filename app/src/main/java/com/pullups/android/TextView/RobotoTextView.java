package com.pullups.android.TextView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pullups.android.helper.CustomFontRobotoHelper;

/**
 * Created by Thomas on 07.07.2015.
 */
public class RobotoTextView extends TextView {

    public RobotoTextView(Context context) {
        super(context);
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontRobotoHelper.setCustomFont(this, context, attrs);
    }

    public RobotoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CustomFontRobotoHelper.setCustomFont(this, context, attrs);
    }
}
