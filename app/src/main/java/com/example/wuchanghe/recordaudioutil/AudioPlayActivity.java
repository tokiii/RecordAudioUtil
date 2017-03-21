package com.example.wuchanghe.recordaudioutil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.wuchanghe.recordaudioutil.views.AudioBarGraph;
import com.example.wuchanghe.recordaudioutil.views.FingerView;

/**
 * Created by wuchanghe on 2017/3/21 13:27.
 */

public class AudioPlayActivity extends AppCompatActivity {

    private LinearLayout ll_container;
    private AudioBarGraph audio_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        audio_bar = (AudioBarGraph) findViewById(R.id.audio_bar);
        FingerView fingerView = new FingerView(this);
        ll_container.addView(fingerView);


        final float[] m = new float[100];
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    for (int i = 0; i < m.length; i++) {
                        m[i] = (float) (Math.random() * 200);
                    }
                    audio_bar.setCurrentHeight(m);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
