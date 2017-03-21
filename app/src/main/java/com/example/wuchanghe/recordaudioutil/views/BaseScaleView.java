package com.example.wuchanghe.recordaudioutil.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import com.example.wuchanghe.recordaudioutil.R;

/**
 * @author LichFaker on 16/3/12.
 * @Email lichfaker@gmail.com
 */
public abstract class BaseScaleView extends View {

    public static final int[] ATTR = {
            R.attr.lf_scale_view_min,
            R.attr.lf_scale_view_max,
            R.attr.lf_scale_view_margin,
            R.attr.lf_scale_view_height,
    };

    public static final @StyleableRes
    int LF_SCALE_MIN = 0;
    public static final @StyleableRes
    int LF_SCALE_MAX = 1;
    public static final @StyleableRes
    int LF_SCALE_MARGIN = 2;
    public static final @StyleableRes
    int LF_SCALE_HEIGHT = 3;
    public static final @StyleableRes
    int LF_SCALE_CURRENT = 4;

    protected int mMax; //最大刻度
    protected int mMin; // 最小刻度
    protected int mCountScale; //滑动的总刻度

    protected int mScaleScrollViewRange;

    protected int mScaleMargin; //刻度间距
    protected int mScaleHeight; //刻度线的高度
    protected int mScaleMaxHeight; //整刻度线高度

    protected int mRectWidth; //总宽度
    protected int mRectHeight; //高度

    protected Scroller mScroller;
    protected int mScrollLastX;

    protected int mTempScale; // 用于判断滑动方向
    protected int mMidCountScale; //中间刻度

    protected OnScrollListener mScrollListener;

    public interface OnScrollListener {
        void onScaleScroll(int scale);
    }

    public BaseScaleView(Context context) {
        super(context);
        init(null);
    }

    public BaseScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BaseScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseScaleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        // 获取自定义属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs, ATTR);
        mMin = ta.getInteger(LF_SCALE_MIN, 0);
        mMax = ta.getInteger(LF_SCALE_MAX, 4000);
        mScaleMargin = ta.getDimensionPixelOffset(LF_SCALE_MARGIN, 15);
        mScaleHeight = ta.getDimensionPixelOffset(LF_SCALE_HEIGHT, 20);
        ta.recycle();

        mScroller = new Scroller(getContext());

        initVar();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画笔
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        // 抗锯齿
        paint.setAntiAlias(true);
        // 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        paint.setDither(true);
        // 空心
        paint.setStyle(Paint.Style.STROKE);
        // 文字居中
        paint.setTextAlign(Paint.Align.CENTER);

        onDrawLine(canvas, paint);
        onDrawScale(canvas, paint); //画刻度
        onDrawPointer(canvas, paint); //画指针

        super.onDraw(canvas);
    }

    protected abstract void initVar();

    // 画线
    protected abstract void onDrawLine(Canvas canvas, Paint paint);

    // 画刻度
    protected abstract void onDrawScale(Canvas canvas, Paint paint);

    // 画指针
    protected abstract void onDrawPointer(Canvas canvas, Paint paint);

    // 滑动到指定刻度
    public abstract void scrollToScale(int val);

    public void setCurScale(int val) {
        if (val >= mMin && val <= mMax) {
            scrollToScale(val);
            postInvalidate();
        }
    }

    /**
     * 使用Scroller时需重写
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        // 判断Scroller是否执行完毕
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 通过重绘来不断调用computeScroll
            invalidate();
        }
    }

    public void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
    }

    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnScrollListener(OnScrollListener listener) {
        this.mScrollListener = listener;
    }
}
