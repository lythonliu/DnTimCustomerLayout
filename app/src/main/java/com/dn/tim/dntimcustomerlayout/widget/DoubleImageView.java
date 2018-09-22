/*
 * Created by 动脑科技-Tim on 17-7-19 下午11:00
 * Copyright (c) 2017. All rights reserved
 *
 * Last modified 17-7-19 下午11:00
 */

package com.dn.tim.dntimcustomerlayout.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.dn.tim.dntimcustomerlayout.R;

public class DoubleImageView extends View{

    /*Image Contents*/
    private Drawable mLeftDrawable,mRightDrawable;
    /*Text Contents*/
    private CharSequence mText;
    private StaticLayout mTextLayout;
    /*Text Drawing*/
    private TextPaint mTextPaint;
    private Point mTextOrigin;
    private int mSpacing;
    public DoubleImageView(Context context) {
        this(context, null);
    }

    public DoubleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DoubleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
    }

    //第一步
    private void initView(Context context, AttributeSet attrs, int defStyle) {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextOrigin = new Point(0,0);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DoubleImageView, 0, defStyle);
        Drawable d = a.getDrawable(R.styleable.DoubleImageView_android_drawableLeft);
        if (d != null) {
            setLeftDrawable(d);
        }

        d = a.getDrawable(R.styleable.DoubleImageView_android_drawableRight);
        if (d != null) {
            setRightDrawable(d);
        }

        int spacing = a.getDimensionPixelOffset(R.styleable.DoubleImageView_android_spacing, 0);
        setSpacing(spacing);

        int color = a.getColor(R.styleable.DoubleImageView_android_textColor, 0);
        mTextPaint.setColor(color);

        int rawSize = a.getDimensionPixelSize(R.styleable.DoubleImageView_android_textSize, 0);
        mTextPaint.setTextSize(rawSize);

        CharSequence text = a.getText(R.styleable.DoubleImageView_android_text);
        setText(text);
        a.recycle();
    }

    public void setLeftDrawableResource(int resId) {
        Drawable d = getResources().getDrawable(resId);
        setLeftDrawable(d);
    }

    public void setLeftDrawable(Drawable left) {
        mLeftDrawable = left;
        updateContentBounds();
        invalidate();
    }

    public void setRightDrawableResource(int resId) {
        Drawable d = getResources().getDrawable(resId);
        setRightDrawable(d);
    }

    public void setRightDrawable(Drawable right) {
        mRightDrawable = right;
        updateContentBounds();
        invalidate();
    }

    public void setSpacing(int spacing) {
        mSpacing = spacing;
        updateContentBounds();
        invalidate();
    }

    public void setText(CharSequence text) {
        if (!TextUtils.equals(mText, text)) {
            mText = text;
            updateContentBounds();
            invalidate();
        }
    }

    //第四步 确定边界点
    private void updateContentBounds() {
        if (mText == null) {
            mText = "";
        }
        float textWidth = mTextPaint.measureText(mText, 0, mText.length()); //测量文字的长度
        //
        mTextLayout = new StaticLayout(mText, mTextPaint, (int)textWidth, Layout.Alignment.ALIGN_CENTER
                , 1f, 0f, true);

        int left = (getWidth() - getDesiredWidth()) /2;
        int top = (getHeight() - getDesireHeight()) / 2;

        if (mLeftDrawable != null) {
            mLeftDrawable.setBounds(left, top, left + mLeftDrawable.getIntrinsicWidth(), top + mLeftDrawable.getIntrinsicHeight());
            left += (mLeftDrawable.getIntrinsicWidth() * 0.33f);
            top += (mLeftDrawable.getIntrinsicHeight() * 0.33f);
        }

        if (mRightDrawable != null) {
            mRightDrawable.setBounds(left, top, left + mRightDrawable.getIntrinsicWidth(), top + mRightDrawable.getIntrinsicHeight());
            left = mRightDrawable.getBounds().right + mSpacing;
        }

        if (mTextLayout != null) {
            top = (getHeight() - mTextLayout.getHeight()) /2;
            mTextOrigin.set(left, top);
        }
    }
    //第二步 计算整个空间的宽
    private int getDesiredWidth() {
        int leftWidth;
        if (mLeftDrawable == null) {
            leftWidth = 0;
        } else {
            leftWidth = mLeftDrawable.getIntrinsicWidth();
        }

        int rightWidth;
        if (mRightDrawable == null) {
            rightWidth = 0;
        } else {
            rightWidth = mRightDrawable.getIntrinsicWidth();
        }

        int textWidth;
        if (mTextLayout == null) {
            textWidth = 0;
        } else {
            textWidth = mTextLayout.getWidth();
        }

        return (int) (leftWidth * 0.67f) + (int) (rightWidth * 0.67f) + mSpacing + textWidth;
    }

    //第三步
    private int getDesireHeight(){
        int leftHeight;
        if (mLeftDrawable == null) {
            leftHeight = 0;
        } else {
            leftHeight = mLeftDrawable.getIntrinsicHeight();
        }

        int rightHeight;
        if (mRightDrawable == null) {
            rightHeight = 0;
        } else {
            rightHeight = mRightDrawable.getIntrinsicHeight();
        }
        return (int)(leftHeight * 0.67f) + (int)(rightHeight * 0.67f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            updateContentBounds();
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLeftDrawable != null) {
            mLeftDrawable.draw(canvas);
        }
        if (mTextLayout != null) {
            canvas.save();
            canvas.translate(mTextOrigin.x, mTextOrigin.y);
            mTextLayout.draw(canvas);
            canvas.restore();
        }

        if (mRightDrawable != null) {
            mRightDrawable.draw(canvas);
        }
    }
}
