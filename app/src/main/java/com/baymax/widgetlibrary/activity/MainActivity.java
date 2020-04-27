package com.baymax.widgetlibrary.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;

import com.baymax.base.activity.BaseTabBarActivity;
import com.baymax.widgetlibrary.R;
import com.baymax.widgetlibrary.fragment.ArrowLayoutFragment;
import com.baymax.widgetlibrary.fragment.ExpandLayoutFragment;
import com.baymax.widgetlibrary.fragment.MenuLayoutFragment;
import com.baymax.widgetlibrary.fragment.RatingStarViewFragment;
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
        fragments.add(new RatingStarViewFragment());
        fragments.add(new ExpandLayoutFragment());
        fragments.add(new RoundLayoutFragment());
        fragments.add(new MenuLayoutFragment());
        fragments.add(new ArrowLayoutFragment());
        return fragments;
    }

    @Override
    protected List<String> createPagerTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("RatingStarView");
        titles.add("ExpandLayout");
        titles.add("RoundLayout");
        titles.add("MenuLayout");
        titles.add("ArrowLayout");
        return titles;
    }

    @Override
    protected void onRightViewClicked(View view) {
        super.onRightViewClicked(view);
        PopupWindow popupWindow = makePopupWindow();
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        //popupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, -xy[0] / 2, xy[1] + button.getWidth());
        popupWindow.showAsDropDown(view,20, 20);
    }

    /**
     * 创建PopupWindow
     */
    private PopupWindow makePopupWindow()
    {
        PopupWindow popupWindow = new PopupWindow(this);

        View contentView = LayoutInflater.from(this).inflate(R.layout.item_menu_selected, null);
        popupWindow.setContentView(contentView);
        popupWindow.setBackgroundDrawable(null);
        //设置PopupWindow可获得焦点
        popupWindow.setFocusable(true);
        //设置PopupWindow可触摸
        popupWindow.setTouchable(true);
        //设置非PopupWindow区域可触摸
        popupWindow.setOutsideTouchable(true);
        return popupWindow;
    }
}
