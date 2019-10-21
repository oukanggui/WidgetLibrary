package com.baymax.widgetlibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.Toast;

import com.baymax.widget.ExpandLayout;

/**
 * 测试主Activity界面
 */
public class MainActivity extends AppCompatActivity {
    private ExpandLayout expandLayout, expandLayoutIcon, expandLayoutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandLayout = findViewById(R.id.my_expand_default);
        expandLayoutIcon = findViewById(R.id.my_expand_icon);
        expandLayoutText = findViewById(R.id.my_expand_text);
        //要展示的文字内容
        String contentStr = "啊哈啊哈啊哈啊哈哈哈啊哈啊哈哈哈啊哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈或或或或或或或或或或或或或或或或或或或或或或或或或";
        expandLayout.setContent(contentStr, new ExpandLayout.OnExpandStateChangeListener() {
            @Override
            public void onExpand() {
                expandLayout.setContentTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_light));
            }

            @Override
            public void onCollapse() {
                expandLayout.setExpandTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_dark));
            }
        });
        String contentStr1 = "啊哈啊哈啊哈啊哈哈哈啊哈啊哈哈哈啊哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈或或或或或或或或或或或或或或或或或或或或或或或或或xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy";
        expandLayoutIcon.setContent(contentStr1, new ExpandLayout.OnExpandStateChangeListener() {
            @Override
            public void onExpand() {
                expandLayoutIcon.setCollapseLessIcon(R.drawable.splitter_less);
            }

            @Override
            public void onCollapse() {
                expandLayoutIcon.setExpandMoreIcon(R.mipmap.ic_more);
            }
        });
        String contentStr2 = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy";
        expandLayoutText.setContent(contentStr2, new ExpandLayout.OnExpandStateChangeListener() {
            @Override
            public void onExpand() {
                Toast.makeText(MainActivity.this, "onExpand", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCollapse() {
                Toast.makeText(MainActivity.this, "onCollapse", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
