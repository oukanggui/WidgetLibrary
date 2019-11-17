package com.baymax.widgetlibrary.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.baymax.base.fragment.BaseFragment;
import com.baymax.widget.ExpandLayout;
import com.baymax.widgetlibrary.R;

/**
 * @author Baymax
 * @date 2019-11-17
 * 描述：ExpandLayout的演示Fragment
 */
public class ExpandLayoutFragment extends BaseFragment {
    private ExpandLayout mExpandLayoutDefault, mExpandLayoutIcon, mExpandLayoutText;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_layout_expand;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mExpandLayoutDefault = view.findViewById(R.id.my_expand_default);
        mExpandLayoutIcon = view.findViewById(R.id.my_expand_icon);
        mExpandLayoutText = view.findViewById(R.id.my_expand_text);
        //要展示的文字内容
        String contentStr = "啊哈啊哈啊哈啊哈哈哈啊哈啊哈哈哈啊哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈或或或或或或或或或或或或或或或或或或或或或或或或或";
        mExpandLayoutDefault.setContent(contentStr, new ExpandLayout.OnExpandStateChangeListener() {
            @Override
            public void onExpand() {
                mExpandLayoutDefault.setContentTextColor(ContextCompat.getColor(mActivity, android.R.color.holo_blue_light));
            }

            @Override
            public void onCollapse() {
                mExpandLayoutDefault.setExpandTextColor(ContextCompat.getColor(mActivity, android.R.color.holo_red_dark));
            }
        });
        String contentStr1 = "啊哈啊哈啊哈啊 Android、\n\n\nAndroid、哈哈哈啊哈啊AndroidAndroid、哈哈Android、Android、哈啊哈啊哈哈哈哈 哈哈哈哈哈哈哈哈哈哈哈或   或或或或或或或或或 或或或或或或或或或或   或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或";
        mExpandLayoutIcon.setContent(contentStr1, new ExpandLayout.OnExpandStateChangeListener() {
            @Override
            public void onExpand() {
                mExpandLayoutIcon.setCollapseLessIcon(R.drawable.splitter_less);
            }

            @Override
            public void onCollapse() {
                mExpandLayoutIcon.setExpandMoreIcon(R.drawable.splitter_more);
            }
        });
        String contentStr2 = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy";
        mExpandLayoutText.setContent(contentStr2, new ExpandLayout.OnExpandStateChangeListener() {
            @Override
            public void onExpand() {
                Toast.makeText(mActivity, "onExpand", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCollapse() {
                Toast.makeText(mActivity, "onCollapse", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
