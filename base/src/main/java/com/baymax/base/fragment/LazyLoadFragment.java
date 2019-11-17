package com.baymax.base.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author oukanggui
 * @date 2018/11/14
 * 描述 懒加载Fragment
 * 在使用了ViewPager + Fragment的情况下，Fragment的生命周期因ViewPager的缓存机制而失去了具体的意义
 * 该抽象类自定义新的回调方法，当Fragment可见状态改变时或Fragment第一次可见时会回调的方法
 * @see #onFragmentVisibleChange(boolean)
 * @see #onFragmentFirstVisible()
 */

public abstract class LazyLoadFragment extends Fragment {

    protected String TAG = getClass().getSimpleName();
    /**
     * 标识是否是第一次进来
     */
    private boolean mIsFirstEnter = true;
    /**
     * 页面布局根View
     */
    private View mRootView;

    /**
     * 该方法的回调时机：
     * 1）在Fragment创建时会先回调一次（isVisibleToUser=false），此时Fragment还不可见，UI可能还没有初始化
     * 2）Fragment创建完可见时，该方法会再次回调一次（isVisibleToUser=true）
     * 3）Fragment从可见-->不可见时，该方法也会被回调（isVisibleToUser=false）
     * 总结：setUserVisibleHint()除了在Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
     * 如果我们需要在 Fragment 可见与不可见时干点事，用这个的话就会有多余的回调了，就需要重新封装一个
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.i(TAG, "setUserVisibleHint = " + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        //该方法有可能在fragment的生命周期外被调用
        //保证在View初始化的时候在进行处理
        if (mRootView == null) {
            return;
        }
        if (mIsFirstEnter && isVisibleToUser) {
            //第一次进来且可见的时候，回调Fragment首次可见
            onFragmentFirstVisible();
            mIsFirstEnter = false;
        }
        //过滤在Fragment创建时回调该方法的情况
        if (!mIsFirstEnter) {
            //如果不是第一次进来或者已处理完第一次可见（保证onFragmentFirstVisible()先回调），回调Fragment可见状态
            onFragmentVisibleChange(isVisibleToUser);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        //如果setUserVisibleHint()在rootView创建前调用时(此时setUserVisibleHint没有回调处理)，那么
        //就等到rootView创建完后才回调onFragmentFirstVisible()以及onFragmentVisibleChange(true)
        //保证这两个方法的回调发生在rootView创建完成之后，以便支持ui操作
        if (mRootView == null) {
            mRootView = view;
            if (getUserVisibleHint()) {
                if (mIsFirstEnter) {
                    onFragmentFirstVisible();
                    mIsFirstEnter = false;
                }
                onFragmentVisibleChange(true);
            }
        }
        super.onViewCreated(mRootView, savedInstanceState);
    }


    /**
     * 当Fragment可见状态变化时回调该方法：
     * 1)去除setUserVisibleHint()多余的回调场景，保证只有当Fragment可见状态发生变化时才回调
     * 2)回调时机在view创建完后，支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     * 可以在该方法中进行UI操作相关的操作，比如加载框的显示与隐藏、动画操作等
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    /**
     * 在Fragment首次可见时回调，可在这里进行加载数据，保证只在第一次打开Fragment时才会加载数据，
     * 实现懒加载，防止每次进入都重复加载数据
     * 该方法会在onFragmentVisibleChange()之前调用
     */
    protected void onFragmentFirstVisible() {

    }
}
