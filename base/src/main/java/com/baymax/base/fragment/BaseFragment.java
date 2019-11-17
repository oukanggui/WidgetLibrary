package com.baymax.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;


/**
 * @author oukanggui
 * @date 2018/11/14
 * 描述 BaseFragment基类,主要功能如下：
 * 1、支持显示以及设置加载中、数据为空、加载出错重试等布局
 * 2、View布局初始化以及Fragment第一次可见时数据加载等
 */

public abstract class BaseFragment extends LazyLoadFragment {
    private View rootView;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResId(), container, false);
            initView(rootView, savedInstanceState);
            initData();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    /**
     * StateView的根布局，默认是整个界面，如果需要变换可以重写此方法
     */
    public View getStateViewRoot() {
        return rootView;
    }

    /**
     * 获取Fragment资源布局ID，子类Fragment重写该方法返回Fragment展示布局的资源id
     */
    public abstract int getLayoutResId();

    /**
     * 加载数据，在Fragment第一次可见时回调，子类Fragment实现该方法进行数据加载（网络、数据库等）
     */
    public abstract void loadData();

    /**
     * 初始化UI，子类Fragment实现该方法进行UI初始化相关工作
     */
    public abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 初始化数据，子类Fragment可以选择性实现该方法进行数据初始化相关工作
     */
    protected void initData() {
    }


    @Override
    protected void onFragmentFirstVisible() {
        //在Fragment第一次可见时，进行数据加载+
        loadData();
    }
}
