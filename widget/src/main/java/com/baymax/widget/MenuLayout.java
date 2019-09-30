package com.baymax.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Dimension;

/**
 * @author oukanggui
 * @date 2019/9/26
 * 描述：设置菜单条Layout
 */
public class MenuLayout extends RelativeLayout {

    /**
     * Item根布局
     */
    private View mItemLayout;
    /**
     * 左侧图标
     */
    private ImageView mIconLeft;
    /**
     * 右侧图标
     */
    private ImageView mIconRight;
    /**
     * 左侧文字
     */
    private TextView mTextLeft;
    /**
     * 右侧文字
     */
    private TextView mTextRight;
    /**
     * 底部分隔线
     */
    private View mLineView;

    public MenuLayout(Context context) {
        super(context);
        init(context, null);
    }

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化，进行控件和属性初始化工作
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        // MenuLayout作为R.layout.layout_menu_item的父View，最后内部通过addView的形式添加到MenuLayout中
        inflate(context, R.layout.layout_menu_item, this);
        mIconLeft = findViewById(R.id.menu_iv_left);
        mIconRight = findViewById(R.id.menu_iv_right);
        mTextLeft = findViewById(R.id.menu_tv_left);
        mTextRight = findViewById(R.id.menu_tv_right);
        mLineView = findViewById(R.id.menu_line);
        mItemLayout = findViewById(R.id.menu_item_layout);
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.MenuLayout);
            if (typedArray != null) {
                int backgroundColorId = typedArray.getColor(R.styleable.MenuLayout_itemBackgroundColor, 0);
                setMenuBackgroundColor(backgroundColorId);

                int leftIconResId = typedArray.getResourceId(R.styleable.MenuLayout_leftIcon, 0);
                setLeftIcon(leftIconResId);
                int rightIconResId = typedArray.getResourceId(R.styleable.MenuLayout_rightIcon, 0);
                setRightIcon(rightIconResId);

                String textLeft = typedArray.getString(R.styleable.MenuLayout_leftText);
                setLeftText(textLeft);
                String textRight = typedArray.getString(R.styleable.MenuLayout_rightText);
                setRightText(textRight);

                int leftTextColorId = typedArray.getColor(R.styleable.MenuLayout_leftTextColor, 0);
                setLeftTextColor(leftTextColorId);
                int rightTextColorId = typedArray.getColor(R.styleable.MenuLayout_rightTextColor, 0);
                setRightTextColor(rightTextColorId);

                int spLeftTextSize = typedArray.getInteger(R.styleable.MenuLayout_leftTextSize, 0);
                setLeftTextSize(spLeftTextSize);
                int spRightTextSize = typedArray.getInteger(R.styleable.MenuLayout_rightTextSize, 0);
                setRightTextSize(spRightTextSize);

                boolean isBottomLineVisible = typedArray.getBoolean(R.styleable.MenuLayout_lineVisible, true);
                if (mLineView != null) {
                    mLineView.setVisibility(isBottomLineVisible ? View.VISIBLE : View.GONE);
                }

                typedArray.recycle();
            }
        }
    }

    /**
     * 获取Menu Item根布局
     */
    public View getMenuView() {
        return mItemLayout;
    }

    /**
     * 获取左边图标对象
     *
     * @return
     */
    public ImageView getIconLeft() {
        return mIconLeft;
    }

    /**
     * 获取右边图标对象
     *
     * @return
     */
    public ImageView getIconRight() {
        return mIconRight;
    }

    /**
     * 通过资源id设置左边图片资源
     *
     * @param resId
     */
    public void setLeftIcon(int resId) {
        if (mIconLeft != null) {
            if (resId != 0) {
                setViewVisible(mIconLeft);
                mIconLeft.setImageResource(resId);
            } else {
                // 资源id == 0，默认为不配置
                setViewInvisible(mIconLeft);
            }
        }
    }

    /**
     * 通过Drawable设置左边图片资源
     *
     * @param drawable
     */
    public void setLeftIcon(Drawable drawable) {
        if (mIconLeft != null) {
            setViewVisible(mIconLeft);
            mIconLeft.setImageDrawable(drawable);
        }
    }

    /**
     * 通过资源id设置右边图片资源
     *
     * @param resId
     */
    public void setRightIcon(int resId) {
        if (mIconRight != null) {
            if (resId != 0) {
                setViewVisible(mIconRight);
                mIconRight.setImageResource(resId);
            } else {
                // 资源id == 0，默认为不配置
                setViewInvisible(mIconRight);
            }
        }
    }

    /**
     * 通过Drawable设置左边图片资源
     *
     * @param drawable
     */
    public void setRightIcon(Drawable drawable) {
        if (mIconRight != null) {
            setViewVisible(mIconRight);
            mIconRight.setImageDrawable(drawable);
        }
    }

    /**
     * 设置左边显示文字
     *
     * @param resId
     */
    public void setLeftText(int resId) {
        if (mTextLeft != null) {
            setViewVisible(mTextLeft);
            mTextLeft.setText(resId);
        }
    }

    /**
     * 设置左边显示文字
     *
     * @param text
     */
    public void setLeftText(String text) {
        if (mTextLeft != null) {
            setViewVisible(mTextLeft);
            mTextLeft.setText(text);
        }
    }

    /**
     * 设置右边显示文字
     *
     * @param resId
     */
    public void setRightText(int resId) {
        if (mTextRight != null) {
            setViewVisible(mTextRight);
            mTextRight.setText(resId);
        }
    }

    /**
     * 设置右边显示文字
     *
     * @param text
     */
    public void setRightText(String text) {
        if (mTextRight != null) {
            setViewVisible(mTextRight);
            mTextRight.setText(text);
        }
    }

    /**
     * 设置左边文字颜色
     *
     * @param colorId
     */
    public void setLeftTextColor(int colorId) {
        // colorId = 0,使用默认颜色
        if (mTextLeft != null && colorId != 0) {
            mTextLeft.setTextColor(colorId);
        }
    }

    /**
     * 设置右边文字颜色
     *
     * @param colorId
     */
    public void setRightTextColor(int colorId) {
        // colorId = 0,使用默认颜色
        if (mTextRight != null && colorId != 0) {
            mTextRight.setTextColor(colorId);
        }
    }

    /**
     * 设置左边字体大小
     *
     * @param spSize sp
     */
    public void setLeftTextSize(int spSize) {
        // pxSize == 0时，使用默认布局的字体大小
        if (mTextLeft != null && spSize > 0) {
            mTextLeft.setTextSize(Dimension.SP, spSize);
        }
    }

    /**
     * 设置右边字体大小
     *
     * @param spSize sp
     */
    public void setRightTextSize(int spSize) {
        // pxSize == 0时，使用默认布局的字体大小
        if (mTextRight != null && spSize > 0) {
            mTextRight.setTextSize(Dimension.SP, spSize);
        }
    }

    /**
     * 设置背景颜色
     *
     * @param colorId
     */
    public void setMenuBackgroundColor(int colorId) {
        if (mItemLayout != null && colorId != 0) {
            mItemLayout.setBackgroundColor(colorId);
        }
    }

    /**
     * 设置背景
     *
     * @param resId
     */
    public void setMenuBackground(int resId) {
        if (mItemLayout != null) {
            mItemLayout.setBackgroundResource(resId);
        }
    }

    /**
     * 设置View可见
     *
     * @param view
     */
    private void setViewVisible(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置View不可见(保留原位置信息)
     *
     * @param view
     */
    private void setViewInvisible(View view) {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }


}
