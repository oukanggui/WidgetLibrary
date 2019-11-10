package com.baymax.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author Baymax
 * @date 2019-11-09
 * 描述：带箭头的TextView
 */
public class ArrowTextView extends AppCompatTextView {
    /**
     * Padding值
     */
    private int mPadding = 20;
    /**
     * 填充的颜色值
     */
    private int mFillBackgroundColor = Color.GRAY;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 三角形的宽度
     */
    private int mArrowWidht = 40;
    /**
     * 三角形的高度
     */
    private int mArrowHeight = 20;
    public ArrowTextView(Context context) {
        super(context);
        init();
    }

    public ArrowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArrowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //setPadding(mPadding,mPadding,mPadding,mPadding);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mFillBackgroundColor);
        // 画背景
        drawRectArea(canvas,getWidth(),getHeight());
        // 画箭头
        drawArrowArea(canvas,getWidth(),getHeight());
        super.onDraw(canvas);
    }

    /**
     * 画矩形背景
     * @param canvas
     * @param width
     * @param height
     */
    private void drawRectArea(Canvas canvas,int width,int height){
        //框定文本显示的区域
        canvas.drawRect(new RectF(getPaddingLeft() - mPadding,getPaddingTop() - mPadding,
                width - getPaddingRight() + mPadding,height - getPaddingBottom()+mPadding),mPaint);
    }

    /**
     * 画三角箭头
     * @param canvas
     * @param width
     * @param height
     */
    private void drawArrowArea(Canvas canvas,int width,int height){
        Path path = new Path();
        //以下是绘制文本的那个箭头
        path.moveTo(width / 2 - 20, height);// 三角形顶点
        path.lineTo(width / 2, height - getPaddingBottom());   //三角形左边的点
        path.lineTo(width / 2 + 40, height - getPaddingBottom());   //三角形右边的点
        path.close();
        canvas.drawPath(path, mPaint);
    }

    /**
     * 设置背景填充颜色
     * @param fillBackgroundColor
     */
    public void setFillBackgroundColor(int fillBackgroundColor) {
        this.mFillBackgroundColor = fillBackgroundColor;
        invalidate();
    }
}
