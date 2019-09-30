package com.baymax.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

/**
 * @author oukanggui
 * @date 2019/9/26
 * 描述：支持圆角、描边以及背景色设置的Button
 */
public class RoundButton extends AppCompatButton {

    /**
     * 背景填充矩形
     */
    RectF mFillRect;
    /**
     * 画笔
     */
    Paint mPaint;
    /**
     * 背景填充颜色
     */
    int mBackgroundColor;
    /**
     * 圆角x,y轴半径
     */
    float mRadiusX = 0, mRadiusY = 0;
    /**
     * 边框颜色
     */
    int mStrokeColor = -1;
    /**
     * 边框颜色集合
     */
    int mStrokeColorSet = -1;
    /**
     * 边框宽度
     */
    float mStrokeWidth = 5;
    /**
     * 字体初始化颜色
     */
    int mTextColorInit = -1;
    /**
     * 默认圆角x,y半径
     */
    private static final int DEFAULT_RADIUS_XY = 5;

    /**
     * @param context
     */

    public RoundButton(Context context) {
        super(context);
        init(context, null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundButton);
            if (a != null) {
                mBackgroundColor = a.getColor(R.styleable.RoundButton_backgroundColorFill, 0);
                mStrokeWidth = a.getDimension(R.styleable.RoundButton_strokeWidth, 1);
                mStrokeColor = a.getColor(R.styleable.RoundButton_strokeColor, 0);
                mStrokeColorSet = mStrokeColor;
                mRadiusX = a.getDimension(R.styleable.RoundButton_rx, DEFAULT_RADIUS_XY);
                mRadiusY = a.getDimension(R.styleable.RoundButton_ry, DEFAULT_RADIUS_XY);
                a.recycle();
            }
        }
        mTextColorInit = getCurrentTextColor();
        mPaint = new Paint();
        mPaint.setColor(mBackgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            if (mStrokeColorSet != -1) {
                mStrokeColor = mStrokeColorSet;
            }
            if (mTextColorInit != -1) {
                setTextColor(mTextColorInit);
            }
        } else {
            int color = getContext().getResources().getColor(android.R.color.darker_gray);
            if (mStrokeColorSet != -1) {
                mStrokeColor = color;
            }
            if (mTextColorInit != -1) {
                setTextColor(color);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawInnerFillBackground(canvas);
        drawOuterStroke(canvas);
        super.onDraw(canvas);
    }

    /**
     * 画内部填充背景
     *
     * @param canvas
     */
    private void drawInnerFillBackground(Canvas canvas) {
        if (mBackgroundColor != 0) {
            if (mFillRect == null || mFillRect.width() != getMeasuredWidth()) {
                mFillRect = new RectF(0, 0, getMeasuredWidth() - 0, getMeasuredHeight() - 0);
            }
            mPaint.setColor(mBackgroundColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(mFillRect, mRadiusX, mRadiusY, mPaint);
        }
    }

    /**
     * 画外部边框
     *
     * @param canvas
     */
    private void drawOuterStroke(Canvas canvas) {
        if (mStrokeColor != 0) {
            mPaint.setColor(mStrokeColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
            RectF strokeRect = new RectF(mStrokeWidth / 2.0f, mStrokeWidth / 2.0f, getMeasuredWidth() - mStrokeWidth / 2.0f, getMeasuredHeight() - mStrokeWidth / 2.0f);
            canvas.drawRoundRect(strokeRect, mRadiusX, mRadiusY, mPaint);
        }
    }

    public void setRadiusX(float radiusX) {
        mRadiusX = radiusX;
    }

    public void setRadiusY(float radiusY) {
        mRadiusY = radiusY;
    }

    public void setmStrokeWidth(float width) {
        mStrokeWidth = width;
    }

    public void setFillBackgroundColor(int fillBackgroundColor) {
        this.mBackgroundColor = fillBackgroundColor;
    }


    public void setStrokeColor(int color) {
        mStrokeColor = color;
        mStrokeColorSet = mStrokeColor;
        mBackgroundColor = 0;
    }

}
