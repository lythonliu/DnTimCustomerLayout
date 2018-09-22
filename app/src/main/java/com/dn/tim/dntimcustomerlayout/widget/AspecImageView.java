/*
 * Created by 动脑科技-Tim on 17-7-19 下午10:36
 * Copyright (c) 2017. All rights reserved
 *
 * Last modified 17-7-19 下午10:36
 */

package com.dn.tim.dntimcustomerlayout.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class AspecImageView extends ImageView{
    public AspecImageView(Context context) {
        this(context,null);
    }

    public AspecImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AspecImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredSizeWidth;
        float aspect;

        Drawable d = getDrawable();
        if (d == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        desiredSizeWidth = d.getIntrinsicWidth();
        aspect = (float) d.getIntrinsicWidth() / (float) d.getIntrinsicHeight();

        int widthsSize = View.resolveSize(desiredSizeWidth, widthMeasureSpec);

        int heightSize = (int) (widthsSize/ aspect);
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.AT_MOST || specMode == MeasureSpec.EXACTLY) {
            if (heightSize > specSize) {
                heightSize = specSize;
                widthsSize = (int) (heightSize * aspect);
            }
        }
        setMeasuredDimension(widthsSize, heightSize);
    }
}
