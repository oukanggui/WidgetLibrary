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
        String contentStr = "我是正常的全中文文字，可以点击我展开查看更多或收起，我是图标+文字的默认模式";
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
        String contentStr1 = "我是图标样式，Android、Android、Android，中间有换行符\n\n\nAndroid、你好AndroidAndroid、哈哈Android、Android、Android你好";
        mExpandLayoutIcon.setContent(contentStr1);
        String contentStr2 = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
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
