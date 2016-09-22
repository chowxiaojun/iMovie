package com.xiroid.imovie.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 可设置宽高比的ImageView
 */
public class AspectRatioImageView extends ImageView {
    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasuredWidth(), (int)(getMeasuredWidth() * 4 / 3));
    }
}
