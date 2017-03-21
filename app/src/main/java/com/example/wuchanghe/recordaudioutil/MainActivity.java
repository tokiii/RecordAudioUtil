package com.example.wuchanghe.recordaudioutil;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuchanghe.recordaudioutil.views.AudioBarGraph;
import com.example.wuchanghe.recordaudioutil.views.HorizontalScaleScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_record;
    private Button btn_play;
    private Button btn_pause;
    private TextView tv_record_file_path;
    private TextView tv_record_time;
    private TextView tv_music_max_length;
    private HorizontalScrollView scroll_view;
    private LinearLayout ll_container;
    private LinearLayout ll_circle_container;
    private Button btn_next;

    private AudioRecord audioRecord;
    private AudioPlay audioPlay;
    private boolean isRecord = true;
    PermissionHelper mHelper;
    private String TAG = this.getClass().getSimpleName();
    private String recordPath = "";
    private TextView tv_music_length;
    private SeekBar seek_music;
    private int audioTime = 0;//音频总长度
    private float percent;// 播放的进度
    private WaveRecyclerView rv_time;
    private TimeAdapter timeAdapter;
    private int count = 0;
    private List<View> integerList = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    private List<View> circleList = new ArrayList<>();

    private HorizontalScaleScrollView horizontalScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_record_file_path = (TextView) findViewById(R.id.tv_record_file_path);
        tv_music_length = (TextView) findViewById(R.id.tv_music_length);
        tv_record_time = (TextView) findViewById(R.id.tv_record_time);
        tv_music_max_length = (TextView) findViewById(R.id.tv_music_max_length);
        scroll_view = (HorizontalScrollView) findViewById(R.id.scroll_view);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        horizontalScale = (HorizontalScaleScrollView) findViewById(R.id.horizontalScale);
        ll_circle_container = (LinearLayout) findViewById(R.id.ll_circle_container);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent playIntent = new Intent(MainActivity.this, AudioPlayActivity.class);
//                startActivity(playIntent);\

            }
        });
        btn_pause = (Button) findViewById(R.id.btn_pause);
        seek_music = (SeekBar) findViewById(R.id.seek_music);
        rv_time = (WaveRecyclerView) findViewById(R.id.rv_time);
        timeAdapter = new TimeAdapter(this, views);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        linearLayoutManager.setReverseLayout(true);
        rv_time.setLayoutManager(linearLayoutManager);
//        rv_time.setAdapter(timeAdapter);

        addView();
        btn_pause.setOnClickListener(this);
        mHelper = new PermissionHelper(this)
        ;
        mHelper.requestPermissions("请授予[录音]，[读写]权限，否则无法录音",
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        Toast.makeText(MainActivity.this, "请授权,否则无法录音", Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION);
        btn_record = (Button) findViewById(R.id.btn_record);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_record.setOnClickListener(this);
        audio_bar = (AudioBarGraph) findViewById(R.id.audio_bar);
        btn_play.setOnClickListener(this);
        audioRecord = AudioRecord.getInstance(Environment.getExternalStorageDirectory().getAbsolutePath(), "ap-record");
        audioPlay = AudioPlay.getInstance();

        /**
         * 播放监听
         */
        audioPlay.setPlayStatusListener(new AudioPlay.OnMediaPlayStatusListener() {
            @Override
            public void onPlayComplete(MediaPlayer mediaPlayer, String musicLength) {
                btn_play.setText("播放完成");
                tv_music_length.setText(musicLength);
            }

            @Override
            public void onPlayDuration(float percent, String playTime) {
                btn_play.setText("正在播放");
                tv_music_length.setText(playTime);
                Log.i(TAG, "播放的百分比为：" + percent);
                seek_music.setProgress((int) percent);
            }

            @Override
            public void onPlayStart(int audioTime, String audioLength) {
                tv_music_max_length.setText(audioLength);
                seek_music.setMax(100);
            }
        });


        /**
         * 录音监听
         */
        audioRecord.setAudioRecordListener(new AudioRecord.AudioRecordListener() {
            @Override
            public void onAudioStartRecordListener(String path) {
                recordPath = path;// 获取录音的路径
            }

            @Override
            public void onAudioRecordingListener(String time) {
                tv_record_time.setText(time);
                int level = audioRecord.getVoiceLevel(50);
                Log.i(TAG, "声音的力度为：" + level);
//                timeAdapter.addItem(MainActivity.this, level * 20);
//                addView(level);
//                count++;
//                if (count % 10 == 0) {
//                    horizontalScale.setCurScale(count / 10);
//                }
//                rv_time.scrollToPosition(views.size() - 1);
                viewAnimation(level);

            }

            @Override
            public void onAudioRecordingComplete() {
                Log.i(TAG, "录音完成");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                if (!TextUtils.isEmpty(recordPath)) {
                    audioPlay.playSound(recordPath);
                }
                break;

            case R.id.btn_record:
                if (isRecord) {
                    audioRecord.startRecord();// 开始录音
                    btn_record.setText("正在录音");
                    isRecord = false;
                } else {
                    isRecord = true;
                    btn_record.setText("录音结束");
                    tv_record_file_path.setText(recordPath);
                    audioRecord.stop();
                }
                break;

            case R.id.btn_pause:
                if (audioPlay.isPlaying()) {
                    audioPlay.pause();
                    btn_pause.setText("继续");
                } else {
                    audioPlay.resume();
                    btn_pause.setText("暂停");
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioRecord != null) {
            audioRecord.stop();
        }

        if (audioPlay != null) {
            audioPlay.pause();
            audioPlay.release();
        }
    }


    private AudioBarGraph audio_bar;


    private void recordGraph(int height) {
        final float[] m = new float[100];

        if (height < 4) {
            for (int i = 0; i < m.length; i++) {
                m[i] = (float) ((float) (1 / height) * 300);
            }
        } else {
            for (int i = 0; i < m.length; i++) {
                m[i] = (float) ((Math.random() + 0.1) * 1 / (float) height * 400);
            }
        }

        audio_bar.setCurrentHeight(m);

    }


    private void addView() {
        for (int i = 0; i < 88; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
            layoutParams.setMargins(0, 0, 5, 0);
            View view = new View(this);
            view.setBackgroundColor(Color.WHITE);
            view.setLayoutParams(layoutParams);
            views.add(view);
            ll_container.addView(view);

        }



        for (int i = 0; i < 88; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
            layoutParams.setMargins(0, 0, 5, 13);
            View view = new View(this);
            view.setBackgroundColor(Color.GREEN);
            view.setLayoutParams(layoutParams);
            circleList.add(view);
            ll_circle_container.addView(view);
        }



    }

    private void viewAnimation(int level) {

//        for (final View view : views) {
//            ValueAnimator va ;
//            //显示view，高度从0变到height值
//            va = ValueAnimator.ofInt(view.getHeight(),(int) (Math.random() * 400));
//            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    //获取当前的height值
//                    int h =(Integer)valueAnimator.getAnimatedValue();
//                    //动态更新view的高度
//                    view.getLayoutParams().height = h;
//                    view.requestLayout();
//                }
//            });
//            va.setDuration(100);
//            //开始动画
//            va.start();
//        }
////

        for (int i = 0; i < ll_container.getChildCount(); i++) {
            final View view = ll_container.getChildAt(i);
            final View circleView = ll_circle_container.getChildAt(i);
            int height = (int) (Math.random() * level * 50);
            ValueAnimator va;
            //显示view，高度从0变到height值
            va = ValueAnimator.ofInt(view.getHeight(), height);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //获取当前的height值
                    int h = (Integer) valueAnimator.getAnimatedValue();
                    //动态更新view的高度
                    view.getLayoutParams().height = h;
                    view.invalidate();
                    view.requestLayout();
                }
            });
            va.setDuration(100);
            //开始动画
            va.start();



            ObjectAnimator animator = ObjectAnimator.ofFloat(circleView, "translationY", circleView.getTranslationY(), -(height));
            animator.setDuration(120);
            animator.start();




        }

//


    }

}
