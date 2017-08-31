package com.tenor.android.demo.search.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * An ImageView with a 16 by 9 frame.
 * Used for displaying GIF tags
 */
public class NineBySixteenImageView extends AppCompatImageView {
    public NineBySixteenImageView(Context context) {
        super(context);
    }

    public NineBySixteenImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NineBySixteenImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int) (width * (9f / 16f)));
    }
}
