package com.baymax.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author oukanggui
 * @date 2019/10/18
 * 描述：展开和收起布局，在TextView最后一行添加“图片+文字样式”展开和收起布局，而且是右对齐
 * 优化改造于：https://blog.csdn.net/u014620028/article/details/100145527
 */
public class ExpandLayout extends RelativeLayout {
    private static final String TAG = ExpandLayout.class.getSimpleName();
    private static final int STYLE_DEFAULT = 0;
    private static final int STYLE_ICON = 1;
    private static final int STYLE_TEXT = 2;
    private Context mContext;
    private View mRootView;
    private TextView mTvContent;
    private LinearLayout mLayoutExpandMore;
    private ImageView mIconExpand;
    private TextView mTvExpand;
    /**
     * 辅助TextView，保证末尾图标和文字与内容文字居中显示
     */
    private TextView mTvExpandHelper;

    private int mExpandIconResId;
    private int mCollapseIconResId;

    private String mExpandMoreStr;
    private String mCollapseLessStr;

    private static int mMinLineNum = 2;

    private int mContentTextSize;
    private int mExpandTextSize;

    private TextPaint mTextPaint;
    private TextPaint mExpandPaint;

    /**
     * 是否展开
     */
    private boolean mIsExpand = false;

    /**
     * 原内容文本
     */
    private String mOriginContentStr;
    /**
     * 缩略后展示的文本
     */
    private CharSequence mEllipsizeStr;

    /**
     * 主文字颜色
     */
    private int mContentTextColor;
    /**
     * 展开/收起文字颜色
     */
    private int mExpandTextColor;

    /**
     * 样式，默认为图标+文字样式
     */
    private int mExpandStyle = STYLE_DEFAULT;

    private OnExpandStateChangeListener mOnExpandStateChangeListener;

    /**
     * 监听器
     */
    public interface OnExpandStateChangeListener {
        /**
         * 展开时回调
         */
        void onExpand();

        /**
         * 收起时回调
         */
        void onCollapse();

    }

    public ExpandLayout(Context context) {
        this(context, null);
    }

    public ExpandLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandLayout);
        if (ta != null) {
            mMinLineNum = ta.getInt(R.styleable.ExpandLayout_minLineNum, 2);
            mExpandIconResId = ta.getResourceId(R.styleable.ExpandLayout_expandIconResId, 0);
            mCollapseIconResId = ta.getResourceId(R.styleable.ExpandLayout_collapseIconResId, 0);
            mExpandMoreStr = ta.getString(R.styleable.ExpandLayout_expandMoreText);
            mCollapseLessStr = ta.getString(R.styleable.ExpandLayout_collapseLessText);
            mContentTextSize = ta.getInt(R.styleable.ExpandLayout_contentTextSize, 18);
            mContentTextColor = ta.getColor(R.styleable.ExpandLayout_contentTextColor, 0);
            mExpandTextSize = ta.getInt(R.styleable.ExpandLayout_expandTextSize, 18);
            mExpandTextColor = ta.getColor(R.styleable.ExpandLayout_expandTextColor, 0);
            mExpandStyle = ta.getInt(R.styleable.ExpandLayout_expandStyle, STYLE_DEFAULT);
            ta.recycle();
        }
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.layout_expand, this);
        mTvContent = mRootView.findViewById(R.id.expand_content_tv);
        mLayoutExpandMore = mRootView.findViewById(R.id.expand_ll);
        mIconExpand = mRootView.findViewById(R.id.expand_iv);
        mTvExpand = mRootView.findViewById(R.id.expand_tv);
        mTvExpandHelper = mRootView.findViewById(R.id.expand_helper_tv);

        int textSize = dp2px(mContext, mContentTextSize);
        //初始化画笔，用于后续文字测量使用
        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(textSize);
        mExpandPaint = new TextPaint();
        mExpandPaint.setTextSize(dp2px(mContext, mExpandTextSize));

        mIconExpand.setBackgroundResource(mExpandIconResId);
        mTvExpand.setText(mExpandMoreStr);

        mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        // 辅助TextView，与内容TextView大小相等，保证末尾图标和文字与内容文字居中显示
        mTvExpandHelper.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mTvExpand.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp2px(mContext, mExpandTextSize));

        setContentTextColor(mContentTextColor);
        setExpandTextColor(mExpandTextColor);
        switch (mExpandStyle) {
            case STYLE_ICON:
                mIconExpand.setVisibility(VISIBLE);
                mTvExpand.setVisibility(GONE);
                break;
            case STYLE_TEXT:
                mIconExpand.setVisibility(GONE);
                mTvExpand.setVisibility(VISIBLE);
                break;
            default:
                mIconExpand.setVisibility(VISIBLE);
                mTvExpand.setVisibility(VISIBLE);
                break;
        }

        mRootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsExpand = !mIsExpand;
                if (mIsExpand) {
                    //展开
                    expand();
                    if (mOnExpandStateChangeListener != null) {
                        mOnExpandStateChangeListener.onExpand();
                    }
                } else {
                    //收缩
                    collapse();
                    if (mOnExpandStateChangeListener != null) {
                        mOnExpandStateChangeListener.onCollapse();
                    }
                }

            }
        });
    }

    /**
     * 设置文本内容
     *
     * @param contentStr
     */
    public void setContent(String contentStr) {
        setContent(contentStr, null);
    }

    /**
     * 设置文本内容
     *
     * @param contentStr
     * @param onExpandStateChangeListener 状态回调监听器
     */
    public void setContent(String contentStr, final OnExpandStateChangeListener onExpandStateChangeListener) {
        if (TextUtils.isEmpty(contentStr)) {
            return;
        }
        mOriginContentStr = contentStr;
        mOnExpandStateChangeListener = onExpandStateChangeListener;
        // 获取文字的宽度
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 用完后立即移除监听，防止多次回调的问题
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                Log.d(TAG, "onGlobalLayout,控件宽度 = " + getMeasuredWidth());
                measureEllipsizeText(getMeasuredWidth());
            }
        });
    }

    /**
     * 处理文本分行
     *
     * @param lineWidth
     */
    private void measureEllipsizeText(int lineWidth) {

        int oneLineWidth = lineWidth;
        //全部文字的宽度
        float textLength = mTextPaint.measureText(mOriginContentStr);

        //行数
        int lineNum = (int) Math.ceil(textLength / oneLineWidth);

        if (lineNum <= mMinLineNum) {
            //少于最小展示行数，不再展示更多相关布局
            mLayoutExpandMore.setVisibility(View.GONE);
            mTvContent.setVisibility(View.VISIBLE);
            mTvContent.setText(mOriginContentStr);
        } else {
            //多余最小展示行数，需要做特殊处理
            int iconWidth = 0;
            if (mExpandStyle == STYLE_DEFAULT || mExpandStyle == STYLE_ICON) {
                // ll布局中的内容，图标iv的宽是15dp，参考布局
                iconWidth = dp2px(mContext, 15);
            }
            float textWidth = 0f;
            if (mExpandStyle == STYLE_DEFAULT || mExpandStyle == STYLE_TEXT) {
                textWidth = mExpandPaint.measureText(mExpandMoreStr);
            }

            float llWidth = iconWidth + textWidth;

            // 这里最后减去的5dp，是为了文字和"展示"分开，可以根据根据实际应用效果调整此值
            float availableTextWidth = oneLineWidth * mMinLineNum - llWidth - dp2px(mContext, 5);
            mEllipsizeStr = TextUtils.ellipsize(mOriginContentStr, mTextPaint, availableTextWidth, TextUtils.TruncateAt.END);

            if (textLength + llWidth > oneLineWidth * lineNum) {
                //文字宽度+展开布局的宽度 > 一行最大展示宽度*行数
                //换行展示“收起”按钮及文字
                mOriginContentStr += "\n";
            }
            // 默认收缩
            collapse();
        }
    }

    /**
     * 设置内容文字颜色
     */
    public void setContentTextColor(int colorId) {
        if (colorId != 0) {
            mContentTextColor = colorId;
            mTvContent.setTextColor(colorId);
        }
    }

    /**
     * 设置更多/收起文字颜色
     */
    public void setExpandTextColor(int colorId) {
        if (colorId != 0) {
            mExpandTextColor = colorId;
            mTvExpand.setTextColor(colorId);
        }
    }

    /**
     * 设置展开更多图标
     *
     * @param resId
     */
    public void setExpandMoreIcon(int resId) {
        if (resId != 0) {
            mExpandIconResId = resId;
            // 当前处于展开状态时，立即更新图标
            if (mIsExpand) {
                mIconExpand.setImageResource(resId);
            }
        }
    }

    /**
     * 设置收缩图标
     *
     * @param resId
     */
    public void setCollapseLessIcon(int resId) {
        if (resId != 0) {
            mCollapseIconResId = resId;
            // 当前处于收缩状态时，立即更新图标
            if (!mIsExpand) {
                mIconExpand.setImageResource(resId);
            }
        }
    }

    /**
     * 展开
     */
    private void expand() {
        mTvContent.setMaxLines(Integer.MAX_VALUE);
        mTvContent.setText(mOriginContentStr);
        mIconExpand.setBackgroundResource(mCollapseIconResId);
        mTvExpand.setText(mCollapseLessStr);
    }

    /**
     * 收起
     */
    private void collapse() {
        mTvContent.setMaxLines(mMinLineNum);
        mTvContent.setText(mEllipsizeStr);
        mIconExpand.setBackgroundResource(mExpandIconResId);
        mTvExpand.setText(mExpandMoreStr);
    }

    private int dp2px(Context context, float dpValue) {
        final float densityScale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * densityScale + 0.5f);
    }
}
