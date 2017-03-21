package com.example.wuchanghe.recordaudioutil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;


/**
 * 录音adapter
 * Created by wuchanghe on 2017/3/16 15:00.
 */

public class TimeAdapter extends RecyclerView.Adapter {

    private List<View> integerList;
    private Context mContext;
    private boolean isResized;
    private float mScaleFactor = 1.0F;
    private String TAG = this.getClass().getSimpleName();

    public TimeAdapter(Context context, List<View> integerList) {
        this.integerList = integerList;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TimeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TimeHolder) {
            TimeHolder timeHolder = (TimeHolder) holder;
            View localWaveView = this.integerList.get(position);
            Object localObject = localWaveView.getParent();
            if (localObject != null) {
                Log.i(TAG, "清空本地localView");
                ((ViewGroup) localObject).removeView(localWaveView);
            }
            timeHolder.layout.removeAllViews();
            timeHolder.layout.addView(localWaveView);
            timeHolder.itemView.setTag(localWaveView);
        }
    }


    @Override
    public int getItemCount() {
        return integerList.size();
    }



    class TimeHolder extends RecyclerView.ViewHolder {

        public RelativeLayout layout;

        public TimeHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);

        }
    }


    public boolean isResized() {
        return this.isResized;
    }

    public int getTotalWaveViewWidth() {
        int j = 0;
        int i = 0;
        while (i < this.integerList.size()) {
            j += (this.integerList.get(i)).getWidth();
            i += 1;
        }
        return j;
    }


}
