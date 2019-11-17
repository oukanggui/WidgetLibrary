package com.baymax.widgetlibrary.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import com.baymax.base.activity.BaseTabBarActivity;
import com.baymax.widgetlibrary.fragment.ArrowLayoutFragment;
import com.baymax.widgetlibrary.fragment.ExpandLayoutFragment;
import com.baymax.widgetlibrary.fragment.MenuLayoutFragment;
import com.baymax.widgetlibrary.fragment.RoundLayoutFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试主Activity界面
 */
public class MainActivity extends BaseTabBarActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setTitle("WidgetLibrary");
        setTitleTextColor(Color.WHITE);
        showBackView(false);
        showRightView(true);
    }

    @Override
    protected List<Fragment> createFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ExpandLayoutFragment());
        fragments.add(new RoundLayoutFragment());
        fragments.add(new MenuLayoutFragment());
        fragments.add(new ArrowLayoutFragment());
        return fragments;
    }

    @Override
    protected List<String> createPagerTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("ExpandLayout");
        titles.add("RoundLayout");
        titles.add("MenuLayout");
        titles.add("ArrowLayout");
        return titles;
    }
}
