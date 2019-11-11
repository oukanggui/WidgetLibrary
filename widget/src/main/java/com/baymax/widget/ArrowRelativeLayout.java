package com.baymax.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author Baymax
 * @date 2019-11-10
 * 描述：带三角的指示器相对布局
 */
public class ArrowRelativeLayout extends RelativeLayout {
    public static final int POSTION_TOP = 0;
    public static final int POSTION_BOTTOM = 1;
    public static final int DIRECTION_MIDDLE = 0;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = 2;

    /**
     * 三角形的宽度
     */
    private float mArrowWidth = 20;
    /**
     * 三角形的高度
     */
    private float mArrowHeight = 20;
    /**
     * 三角形距离布局左边的偏移量，默认居中显示
     */
    private float mArrowOffset = -1;
    /**
     * 三角形的指示方向，向左、居中(默认)、向右
     */
    private int mArrowDirection = DIRECTION_MIDDLE;
    /**
     * 三角形的位置，目前支持在布局上方、在布局下方，默认在上方
     */
    private int mArrowPosition = POSTION_TOP;
    /**
     * 圆角的X半径
     */
    private float mRadiuX;
    /**
     * 圆角的Y半径
     */
    private float mRadiuY;
    /**
     * 填充的颜色值
     */
    private int mBackgroundColor = Color.RED;

    /**
     * 画笔
     */
    private Paint mPaint;

    public ArrowRelativeLayout(Context context) {
        super(context);
        init(null);
    }

    public ArrowRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ArrowRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public ArrowRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(AttributeSet attrs) {
        initPaint();
        initAttrs(attrs);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray ta = getResources().obtainAttributes(attrs, R.styleable.ArrowRelativeLayout);
        if (ta != null) {
            mArrowWidth = ta.getDimension(R.styleable.ArrowRelativeLayout_arrow_width, 20);
            mArrowHeight = ta.getDimension(R.styleable.ArrowRelativeLayout_arrow_height, 20);
            mArrowOffset = ta.getDimension(R.styleable.ArrowRelativeLayout_arrow_offset, -1);
            mArrowDirection = ta.getInt(R.styleable.ArrowRelativeLayout_arrow_direction, DIRECTION_MIDDLE);
            mArrowPosition = ta.getInt(R.styleable.ArrowRelativeLayout_arrow_position, POSTION_TOP);
            mRadiuX = ta.getDimension(R.styleable.ArrowRelativeLayout_radiu_x, 0);
            mRadiuY = ta.getDimension(R.styleable.ArrowRelativeLayout_radiu_y, 0);
            mBackgroundColor = ta.getColor(R.styleable.ArrowRelativeLayout_background_color, Color.GRAY);
            ta.recycle();
        }
        // 根据方向重设TOP、BOTTOM的Padding值，追加三角形箭头高度mArrowHeight，以便控件内容显示在矩形区域内
        if (mArrowPosition == POSTION_TOP) {
            setPadding(getPaddingLeft(), getPaddingTop() + (int) Math.ceil(mArrowHeight), getPaddingRight(), getPaddingBottom());
        } else {
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + (int) Math.ceil(mArrowHeight));
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPaint.setColor(mBackgroundColor);
        // 画背景
        drawRoundRectArea(canvas, getWidth(), getHeight());
        // 画箭头
        drawArrowArea(canvas, getWidth(), getHeight());
        super.dispatchDraw(canvas);
    }

    /**
     * 画矩形背景
     *
     * @param canvas
     * @param width
     * @param height
     */
    private void drawRoundRectArea(Canvas canvas, int width, int height) {
        //框定文本显示的区域，根据不同的方向，选择不同的矩形面积,给三角形显示的控件
        RectF rectF;
        if (mArrowPosition == POSTION_TOP) {
            rectF = new RectF(0, mArrowHeight, width, height);
        } else {
            rectF = new RectF(0, 0, width, height - mArrowHeight);
        }
        canvas.drawRoundRect(rectF, mRadiuX, mRadiuY, mPaint);
    }

    /**
     * 画三角箭头
     *
     * @param canvas
     * @param width
     * @param height
     */
    private void drawArrowArea(Canvas canvas, int width, int height) {
        Path path = new Path();
        // 根据三角箭头箭头方向和位置决定三角形三点的坐标
        if (mArrowOffset < 0) {
            mArrowOffset = width / 2.0f - mArrowWidth / 2.0f;
        }
        if ((mArrowOffset + mArrowWidth) > width) {
            mArrowOffset = width - mArrowWidth;
        }
        float topPointX;
        float topPointY;
        float leftPointX = mArrowOffset;
        float rightPointX = leftPointX + mArrowWidth;
        float leftPointY;
        float rightPointY;
        // 根据箭头方向确定三角形顶点的x坐标
        switch (mArrowDirection) {
            case DIRECTION_LEFT:
                topPointX = leftPointX - mArrowHeight;
                break;
            case DIRECTION_RIGHT:
                topPointX = rightPointX + mArrowHeight;
                break;
            case DIRECTION_MIDDLE:
            default:
                topPointX = leftPointX + mArrowWidth / 2;
                break;
        }
        // 根据三角形的位置，确定三角形三点的y坐标
        if (mArrowPosition == POSTION_TOP) {
            // 三角形在上方
            topPointY = 0;
            leftPointY = rightPointY = mArrowHeight;
        } else {
            topPointY = height;
            leftPointY = rightPointY = height - mArrowHeight;
        }
        // 三角形顶点
        path.moveTo(topPointX, topPointY);
        // 三角形左边的点
        path.lineTo(leftPointX, leftPointY);
        // 三角形右边的点
        path.lineTo(rightPointX, rightPointY);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    /**
     * 设置背景填充颜色
     *
     * @param backgroundColor
     */
    public void setFillBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        invalidate();
    }
}
