package com.xiroid.imovie.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xiroid.imovie.R;

/**
 * 可设置宽高比的ImageView
 */
public class AspectRatioImageView extends ImageView {

    private float aspectRatio;

    /**
     * 在代码中直接调用
     *
     * @param context
     */
    public AspectRatioImageView(Context context) {
        this(context, null);
    }

    /**
     * 在xml布局中调用
     *
     * @param context
     * @param attrs   自定义属性
     */
    public AspectRatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.AspectRatioImageView, defStyleAttr, 0);
        aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, 0);
        a.recycle();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.AspectRatioImageView, defStyleAttr, defStyleRes);
        aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, 0);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (aspectRatio == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            if (width == 0 && height == 0) {
                throw new IllegalArgumentException();
            }

            int hPadding = getPaddingLeft() + getPaddingRight();
            int vPadding = getPaddingTop() + getPaddingBottom();

            width -= hPadding;
            height -= vPadding;

            if (height > 0 && (width > height * aspectRatio)) {
                width = (int) (height * aspectRatio + .5);
            } else {
                height = (int) (width / aspectRatio + .5);
            }

            width += hPadding;
            height += vPadding;

            super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }
    }
}
