package com.example.clicea.circleprogresslayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

/**
 * Created by clicea on 26/04/18.
 */
public class CircleLayoutParams extends ViewGroup.MarginLayoutParams {
    /**
     * The gravity to apply with the View to which these layout parameters
     * are associated.
     */
    public int gravity = Gravity.CENTER;

    CircleLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    CircleLayoutParams(int width, int height) {
        super(width, height);
    }

    CircleLayoutParams(ViewGroup.LayoutParams source) {
        super(source);
    }
}