package com.baymax.base.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baymax.base.R;


public abstract class BaseTitleBarActivity extends BaseActivity {

    private View mTitleLayout;
    private FrameLayout mContainer;
    private ImageView mBackView, mIvRight;
    private TextView mTitleView;

    @Override
    public void setContentView(View view) {
        setContentView(R.layout.activity_base_titlebar);
        mContainer = (FrameLayout) findViewById(R.id.base_fl_container);
        mContainer.addView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base_titlebar);
        if (layoutResID != R.layout.activity_base_titlebar) {
            View contentView = LayoutInflater.from(this).inflate(layoutResID, null);
            mContainer = findViewById(R.id.base_fl_container);
            mContainer.addView(contentView);
        }
        initTitleLayout();
    }

    /**
     * 初始化头部Title布局
     */
    private void initTitleLayout() {
        mTitleLayout = findViewById(R.id.base_layout_title);
        mTitleView = findViewById(R.id.base_tv_title);
        mBackView = findViewById(R.id.base_iv_back);
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackViewClicked();
            }
        });
        mIvRight = findViewById(R.id.base_iv_right);
        mIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightViewClicked();
            }
        });
    }

    /**
     * 设置Title
     *
     * @param color Title布局显示的背景颜色
     */
    public void setTitleBarBackgroundColor(int color){
        if (mTitleLayout != null){
            mTitleLayout.setBackgroundColor(color);
        }
    }

    /**
     * 设置Title
     *
     * @param drawable Title布局显示的背景drawable资源
     */
    public void setTitleBarBackgroundDrawable(int drawable){
        if (mTitleLayout != null){
            mTitleLayout.setBackgroundResource(drawable);
        }
    }

    /**
     * 设置Title
     *
     * @param title 显示的Title
     */
    public void setTitle(String title) {
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    /**
     * 设置Title字体颜色
     *
     * @param color
     */
    public void setTitleTextColor(int color){
        if (mTitleView != null) {
            mTitleView.setTextColor(color);
        }
    }

    /**
     * 设置标题栏左边图片显示的图片资源
     *
     * @param resId 待显示的图片资源ID
     */
    public void setBackViewRes(int resId) {
        setImageViewRes(mBackView, resId);
    }

    /**
     * 设置标题栏右边显示的图片资源
     *
     * @param resId 待显示的图片资源ID
     */
    public void setRightViewRes(int resId) {
        setImageViewRes(mIvRight, resId);
    }

    /**
     * 设置Title是否显示
     *
     * @param isShow 是否显示
     */
    public void showTitle(boolean isShow) {
        setViewVisibility(mTitleView, isShow);
    }

    /**
     * 设置返回BackView是否显示
     *
     * @param isShow 是否显示
     */
    public void showBackView(boolean isShow) {
        setViewVisibility(mBackView, isShow);
    }

    /**
     * 设置标题栏右边图标是否显示
     *
     * @param isShow 是否显示
     */
    public void showRightView(boolean isShow) {
        setViewVisibility(mIvRight, isShow);
    }

    /**
     * 设置View是否显示
     *
     * @param view   待处理的View
     * @param isShow 是否显示
     */
    private void setViewVisibility(View view, boolean isShow) {
        if (view != null) {
            view.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置ImageView显示的资源
     *
     * @param imageView 待处理的ImageView
     * @param resId     待显示的图片资源ID
     */
    private void setImageViewRes(ImageView imageView, int resId) {
        if (imageView != null) {
            imageView.setImageResource(resId);
        }
    }

    /**
     * 子页面重写该方法处理BackView点击时的处理逻辑，默认为finish当前页面
     */
    protected void onBackViewClicked() {
        finish();
    }

    /**
     * 子页面重写该方法处理RightView点击时的处理逻辑
     */
    protected void onRightViewClicked() {

    }

}
