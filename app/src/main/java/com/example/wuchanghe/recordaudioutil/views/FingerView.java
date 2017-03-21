package com.example.wuchanghe.recordaudioutil.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义view实现滚动view
 * Created by wuchanghe on 2017/3/21 13:52.
 */

public class FingerView extends View {

    private float currentX = 20;
    private int height = 700;
    Paint p = new Paint();


    public FingerView(Context context) {
        super(context);
    }

    public FingerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FingerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setColor(Color.BLACK);
        p.setAntiAlias(true);
        canvas.drawLine(currentX, 0, currentX, height, p);
        canvas.drawCircle(currentX, height, 10, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = event.getX();
        postInvalidate();
        return true;
    }


}
