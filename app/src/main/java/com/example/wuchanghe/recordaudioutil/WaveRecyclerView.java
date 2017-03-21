package com.example.wuchanghe.recordaudioutil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by wuchanghe on 2017/3/20 9:06.
 */

public class WaveRecyclerView extends RecyclerView {

    private float mScaleView = 1.0F;


    public WaveRecyclerView(Context context) {
        super(context);
    }

    public WaveRecyclerView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 获取横向偏移量
     * @return
     */
    public int getHorizontalScrollOffset() {
        if (!((TimeAdapter)getAdapter()).isResized()) {
            return computeHorizontalScrollOffset();
        }

        int i = 0;
         int j = getAdapter().getItemCount();
        j = (int) (WaveProvider.WAVE_WIDTH * WaveProvider.NUM_OF_AMPLITUDE * j / this.mScaleView);
        int k = computeHorizontalScrollRange();
        if (k < j) {
            i = ((TimeAdapter)getAdapter()).getTotalWaveViewWidth() - k;
        }

        return computeHorizontalScrollOffset() + i;
    }


    public void scrollByPosition(int paramInt) {
        int i = getHorizontalScrollOffset();
        int j = paramInt - i;
        if (j != 0) {
            super.scrollBy(j, 0);
            Log.d("WaveRecyclerView", "scrollByPosition - position : " + paramInt + " currentX : " + i + " scrollBy : " + j);
            return;
        }
        Log.d("WaveRecyclerView", "scrollByPosition - current position");
    }


    public void smoothScrollByPosition(int paramInt) {
        int i = getHorizontalScrollOffset();
        int j = paramInt - i;
        super.smoothScrollBy(j, 0);
        Log.d("WaveRecyclerView", "smoothScrollByPosition - position : " + paramInt + " currentX : " + i + " scrollBy : " + j);
    }
}
