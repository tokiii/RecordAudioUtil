package com.example.wuchanghe.recordaudioutil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 自定义surfaceView 实现录音时间轴
 * Created by wuchanghe on 2017/3/16 13:30.
 */

public class RecordTimeView extends SurfaceView implements SurfaceHolder.Callback{


    public RecordTimeView(Context context) {
        super(context);
    }

    public RecordTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
