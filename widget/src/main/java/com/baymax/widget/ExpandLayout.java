package com.baymax.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
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
public class ExpandLayout extends RelativeLayout implements View.OnClickListener {
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
    private int mMeasuredWidth;
    /**
     * 辅助TextView，保证末尾图标和文字与内容文字居中显示
     */
    private TextView mTvExpandHelper;

    private int mExpandIconResId;
    private int mCollapseIconResId;

    private String mExpandMoreStr;
    private String mCollapseLessStr;

    private int mMaxLines = 2;

    private int mContentTextSize;
    private int mExpandTextSize;

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

    /**
     * 展开/收缩布局对应图标的宽度,默认布局是15dp
     */
    private int mExpandIconWidth = 15;

    /**
     * 缩略文本展示时与展开/搜索布局的间距,默认是20dp
     */
    private int mSpaceMargin = 20;

    /**
     * 文本显示的lineSpacingExtra，对应于TextView的lineSpacingExtra属性
     */
    private float mLineSpacingExtra = 0.0f;

    /**
     * 文本显示的lineSpacingMultiplier，对应于TextView的lineSpacingMultiplier属性
     */
    private float mLineSpacingMultiplier = 1.0f;


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
            mMaxLines = ta.getInt(R.styleable.ExpandLayout_maxLines, 2);
            mExpandIconResId = ta.getResourceId(R.styleable.ExpandLayout_expandIconResId, 0);
            mCollapseIconResId = ta.getResourceId(R.styleable.ExpandLayout_collapseIconResId, 0);
            mExpandMoreStr = ta.getString(R.styleable.ExpandLayout_expandMoreText);
            mCollapseLessStr = ta.getString(R.styleable.ExpandLayout_collapseLessText);
            mContentTextSize = ta.getDimensionPixelSize(R.styleable.ExpandLayout_contentTextSize, sp2px(context, 14));
            mContentTextColor = ta.getColor(R.styleable.ExpandLayout_contentTextColor, 0);
            mExpandTextSize = ta.getDimensionPixelSize(R.styleable.ExpandLayout_expandTextSize, sp2px(context, 14));
            mExpandTextColor = ta.getColor(R.styleable.ExpandLayout_expandTextColor, 0);
            mExpandStyle = ta.getInt(R.styleable.ExpandLayout_expandStyle, STYLE_DEFAULT);
            mExpandIconWidth = ta.getDimensionPixelSize(R.styleable.ExpandLayout_expandIconWidth, dp2px(context, 15));
            mSpaceMargin = ta.getDimensionPixelSize(R.styleable.ExpandLayout_spaceMargin, dp2px(context, 20));
            mLineSpacingExtra = ta.getDimensionPixelSize(R.styleable.ExpandLayout_lineSpacingExtra, 0);
            mLineSpacingMultiplier = ta.getFloat(R.styleable.ExpandLayout_lineSpacingMultiplier, 1.0f);
            ta.recycle();
        }
        // mMaxLines应该保证大于等于1
        if (mMaxLines < 1) {
            mMaxLines = 1;
        }
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure,measureWidth = " + getMeasuredWidth());
        if (mMeasuredWidth <= 0 && getMeasuredWidth() > 0) {
            mMeasuredWidth = getMeasuredWidth();
            measureEllipsizeText(mMeasuredWidth);
        }
    }

    private void initView() {
        mRootView = inflate(mContext, R.layout.layout_expand, this);
        mTvContent = findViewById(R.id.expand_content_tv);
        mLayoutExpandMore = findViewById(R.id.expand_ll);
        mIconExpand = findViewById(R.id.expand_iv);
        mTvExpand = findViewById(R.id.expand_tv);
        mTvExpandHelper = findViewById(R.id.expand_helper_tv);

        if (mExpandIconResId != 0) {
            mIconExpand.setImageResource(mExpandIconResId);
        }
        mTvExpand.setText(mExpandMoreStr);

        mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContentTextSize);
        // 辅助TextView，与内容TextView大小相等，保证末尾图标和文字与内容文字居中显示
        mTvExpandHelper.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContentTextSize);
        mTvExpand.setTextSize(TypedValue.COMPLEX_UNIT_PX, mExpandTextSize);
        mTvContent.setLineSpacing(mLineSpacingExtra, mLineSpacingMultiplier);
        mTvExpandHelper.setLineSpacing(mLineSpacingExtra, mLineSpacingMultiplier);
        mTvExpand.setLineSpacing(mLineSpacingExtra, mLineSpacingMultiplier);

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
        if (TextUtils.isEmpty(contentStr) || mRootView == null) {
            return;
        }
        mOriginContentStr = contentStr;
        mOnExpandStateChangeListener = onExpandStateChangeListener;
        // 此处需要先设置mTvContent的text属性，防止在列表中，由于没有获取到控件宽度mMeasuredWidth，先执行onMeasure方法测量时，导致文本只能显示一行的问题
        // 提前设置好text，再执行onMeasure，则没有该问题
        mTvContent.setMaxLines(mMaxLines);
        mTvContent.setText(mOriginContentStr);
        // 获取文字的宽度
        if (mMeasuredWidth <= 0) {
            Log.d(TAG, "宽度尚未获取到，第一次加载");
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // 用完后立即移除监听，防止多次回调的问题
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    mMeasuredWidth = getMeasuredWidth();
                    Log.d(TAG, "onGlobalLayout,控件宽度 = " + mMeasuredWidth);
                    measureEllipsizeText(mMeasuredWidth);
                }
            });
        } else {
            Log.d(TAG, "宽度已获取到，非第一次加载");
            measureEllipsizeText(mMeasuredWidth);
        }
    }

    /**
     * 处理文本分行
     *
     * @param lineWidth
     */
    private void measureEllipsizeText(int lineWidth) {
        if (TextUtils.isEmpty(mOriginContentStr)) {
            return;
        }
        handleMeasureEllipsizeText(lineWidth);
    }

    /**
     * 使用StaticLayout处理文本分行和布局
     *
     * @param lineWidth 文本(布局)宽度
     */
    private void handleMeasureEllipsizeText(int lineWidth) {
        TextPaint textPaint = mTvContent.getPaint();
        StaticLayout staticLayout = new StaticLayout(mOriginContentStr, textPaint, lineWidth, Layout.Alignment.ALIGN_NORMAL, mLineSpacingMultiplier, mLineSpacingExtra, false);
        int lineCount = staticLayout.getLineCount();
        if (lineCount <= mMaxLines) {
            // 不足最大行数，直接设置文本
            //少于最小展示行数，不再展示更多相关布局
            mEllipsizeStr = mOriginContentStr;
            mLayoutExpandMore.setVisibility(View.GONE);
            mTvContent.setMaxLines(Integer.MAX_VALUE);
            mTvContent.setText(mOriginContentStr);
        } else {
            // 超出最大行数
            mRootView.setOnClickListener(this);
            mLayoutExpandMore.setVisibility(View.VISIBLE);
            // Step1:第mMinLineNum行的处理
            handleEllipsizeString(staticLayout, lineWidth);
            // Step2:最后一行的处理
            handleLastLine(staticLayout, lineWidth);
            if (mIsExpand) {
                expand();
            } else {
                // 默认收缩
                collapse();
            }
        }
    }

    /**
     * 处理缩略的字符串
     *
     * @param staticLayout
     * @param lineWidth
     */
    private void handleEllipsizeString(StaticLayout staticLayout, int lineWidth) {
        if (staticLayout == null) {
            return;
        }
        TextPaint textPaint = mTvContent.getPaint();
        // 获取到第mMinLineNum行的起始和结束位置
        int startPos = staticLayout.getLineStart(mMaxLines - 1);
        int endPos = staticLayout.getLineEnd(mMaxLines - 1);
        Log.d(TAG, "startPos = " + startPos);
        Log.d(TAG, "endPos = " + endPos);
        // 修正，防止取子串越界
        if (startPos < 0) {
            startPos = 0;
        }
        if (endPos > mOriginContentStr.length()) {
            endPos = mOriginContentStr.length();
        }
        if (startPos > endPos) {
            startPos = endPos;
        }
        String lineContent = mOriginContentStr.substring(startPos, endPos);
        float textLength = 0f;
        if (lineContent != null) {
            textLength = textPaint.measureText(lineContent);
        }
        Log.d(TAG, "第" + mMaxLines + "行 = " + lineContent);
        Log.d(TAG, "第" + mMaxLines + "行 文本长度 = " + textLength);

        String strEllipsizeMark = "...";
        // 展开控件需要预留的长度,预留宽度："..." + 展开布局与文本间距 +图标长度 + 展开文本长度
        float reservedWidth = mSpaceMargin + textPaint.measureText(strEllipsizeMark) + getExpandLayoutReservedWidth();
        Log.d(TAG, "需要预留的长度 = " + reservedWidth);
        int correctEndPos = endPos;
        if (reservedWidth + textLength > lineWidth) {
            // 空间不够，需要按比例截取最后一行的字符串，以确保展示的最后一行文本不会与可展开布局重叠
            float exceedSpace = reservedWidth + textLength - lineWidth;
            if (textLength != 0) {
                correctEndPos = endPos - (int) ((exceedSpace / textLength) * 1.0f * (endPos - startPos));
            }
        }
        Log.d(TAG, "correctEndPos = " + correctEndPos);
        String ellipsizeStr = mOriginContentStr.substring(0, correctEndPos);
        mEllipsizeStr = removeEndLineBreak(ellipsizeStr) + strEllipsizeMark;
    }

    /**
     * 处理最后一行文本
     *
     * @param staticLayout
     * @param lineWidth
     */
    private void handleLastLine(StaticLayout staticLayout, int lineWidth) {
        if (staticLayout == null) {
            return;
        }
        int lineCount = staticLayout.getLineCount();
        if (lineCount < 1) {
            return;
        }
        int startPos = staticLayout.getLineStart(lineCount - 1);
        int endPos = staticLayout.getLineEnd(lineCount - 1);
        Log.d(TAG, "最后一行 startPos = " + startPos);
        Log.d(TAG, "最后一行 endPos = " + endPos);
        // 修正，防止取子串越界
        if (startPos < 0) {
            startPos = 0;
        }
        if (endPos > mOriginContentStr.length()) {
            endPos = mOriginContentStr.length();
        }
        if (startPos > endPos) {
            startPos = endPos;
        }
        String lastLineContent = mOriginContentStr.substring(startPos, endPos);
        Log.d(TAG, "最后一行 内容 = " + lastLineContent);
        float textLength = 0f;
        TextPaint textPaint = mTvContent.getPaint();
        if (lastLineContent != null) {
            textLength = textPaint.measureText(lastLineContent);
        }
        Log.d(TAG, "最后一行 文本长度 = " + textLength);
        float reservedWidth = getExpandLayoutReservedWidth();
        if (textLength + reservedWidth > lineWidth) {
            //文字宽度+展开布局的宽度 > 一行最大展示宽度
            //换行展示“收起”按钮及文字
            mOriginContentStr += "\n";
        }
    }

    /**
     * 获取展开布局的展开收缩控件的预留宽度
     *
     * @return value = 图标长度 + 展开提示文本长度
     */
    private float getExpandLayoutReservedWidth() {
        int iconWidth = 0;
        if (mExpandStyle == STYLE_DEFAULT || mExpandStyle == STYLE_ICON) {
            // ll布局中的内容，图标iv的宽是15dp，参考布局
            iconWidth = mExpandIconWidth;
        }
        float textWidth = 0f;
        if (mExpandStyle == STYLE_DEFAULT || mExpandStyle == STYLE_TEXT) {
            textWidth = mTvExpand.getPaint().measureText(mExpandMoreStr);
        }
        // 展开控件需要预留的长度
        return iconWidth + textWidth;
    }

    /**
     * 清除行末换行符
     *
     * @param text
     * @return
     */
    private String removeEndLineBreak(CharSequence text) {
        if (text == null) {
            return null;
        }
        String str = text.toString();
        if (str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
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
            // 当前处于收缩状态时，立即更新图标
            if (!mIsExpand) {
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
            // 当前处于展开状态时，立即更新图标
            if (mIsExpand) {
                mIconExpand.setImageResource(resId);
            }
        }
    }

    /**
     * 展开
     */
    public void expand() {
        setIsExpand(true);
        mTvContent.setMaxLines(Integer.MAX_VALUE);
        mTvContent.setText(mOriginContentStr);
        mTvExpand.setText(mCollapseLessStr);
        if (mCollapseIconResId != 0) {
            mIconExpand.setImageResource(mCollapseIconResId);
        }
    }

    /**
     * 收起
     */
    public void collapse() {
        setIsExpand(false);
        mTvContent.setMaxLines(Integer.MAX_VALUE);
        mTvContent.setText(mEllipsizeStr);
        mTvExpand.setText(mExpandMoreStr);
        if (mExpandIconResId != 0) {
            mIconExpand.setImageResource(mExpandIconResId);
        }
    }

    public int getLineCount() {
        return mTvContent == null ? 0 : mTvContent.getLineCount();
    }

    public void setShrinkLines(int shrinkLines) {
        mMaxLines = shrinkLines;
    }

    public void setIsExpand(boolean isExpand) {
        mIsExpand = isExpand;
    }

    public boolean isExpand() {
        return mIsExpand;
    }

    /**
     * 转换dp为px
     *
     * @param context
     * @param dipValue
     * @return
     */
    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 转换sp为px
     *
     * @param context
     * @param spValue
     * @return
     */
    public int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        if (!mIsExpand) {
            //之前是收缩状态，点击后展开
            expand();
            if (mOnExpandStateChangeListener != null) {
                mOnExpandStateChangeListener.onExpand();
            }
        } else {
            //之前是展开状态，点击后收缩
            collapse();
            if (mOnExpandStateChangeListener != null) {
                mOnExpandStateChangeListener.onCollapse();
            }
        }
    }
}