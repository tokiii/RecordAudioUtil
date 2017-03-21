package com.example.wuchanghe.recordaudioutil;

import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 音频录制工具类
 * Created by wuchanghe on 2017/3/13 9:15.
 */

public class AudioRecord {

    private MediaRecorder mRecorder;
    //文件夹位置
    private String mAudioPath;
    //录音文件保存路径
    private String mCurrentFilePathString;
    //是否准备好开始录音
    private boolean isPrepared;
    private int startTime = 0;
    private Timer timer;
    private final int RECORDING = 11;
    private AudioRecordListener audioRecordListener;
    private String mAudioName;// 音频的名称

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RECORDING:
                if (audioRecordListener != null) {
                    audioRecordListener.onAudioRecordingListener(String.valueOf(msg.obj));
                }
                    break;

            }
        }
    };


    public void setAudioRecordListener(AudioRecordListener audioRecordListener) {
        this.audioRecordListener = audioRecordListener;
    }


    /**
     * 单例化这个类
     */
    private static AudioRecord mInstance;

    private AudioRecord(String dir, String name) {
        mAudioPath = dir;
        mAudioName = name;
    }

    public static AudioRecord getInstance(String dir, String name) {
        if (mInstance == null) {
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioRecord(dir, name);
                }
            }
        }
        return mInstance;

    }


    /**
     * 开始录音
     */
    public void startRecord() {
        try {
            isPrepared = false;
            File dir = new File(mAudioPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileNameString = generalFileName();
            File file = new File(dir, fileNameString);
            mCurrentFilePathString = file.getAbsolutePath();
            mRecorder = new MediaRecorder();
            mRecorder.setOutputFile(file.getAbsolutePath());
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            mRecorder.prepare();
            mRecorder.start();
            timer = new Timer();
            startClick();
            // 准备结束
            isPrepared = true;
            // 已经准备好了，可以录制了
            if (audioRecordListener != null) {
                audioRecordListener.onAudioStartRecordListener(mCurrentFilePathString);
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 随机生成文件的名称
     *
     * @return
     */
    private String generalFileName() {
        return mAudioName + ".amr";
    }

    // 获得声音的level
    public int getVoiceLevel(int maxLevel) {
        // mRecorder.getMaxAmplitude()这个是音频的振幅范围，值域是1-32767
        if (isPrepared) {
            try {
                // 取证+1，否则去不到7
                return maxLevel * mRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {

            }
        }

        return 1;
    }

    // 释放资源
    public void release() {
        if (timer != null && timerTask != null) {
            timer.cancel();
            if (!timerTask.cancel())
            timerTask.cancel();
        }
        // 严格按照api流程进行
        if (mRecorder == null) return;
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        if (audioRecordListener != null) {
            audioRecordListener.onAudioRecordingComplete();
        }

    }


    /**
     * 停止录音
     */
    public void stop() {
        release();
    }

    // 取消,因为prepare时产生了一个文件，所以cancel方法应该要删除这个文件，
    // 这是与release的方法的区别
    public void cancel() {
        release();
        if (mCurrentFilePathString != null) {
            File file = new File(mCurrentFilePathString);
            file.delete();
            mCurrentFilePathString = null;
        }

    }

    public String getCurrentFilePath() {
        return mCurrentFilePathString;
    }

    public void startClick() {
        startTime = -1;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                startTime ++;
                Message message = new Message();
                message.what = RECORDING;
                message.obj = getStringTime(startTime/10);
                timeHandler.sendMessage(message);
            }
        };
        timer.schedule(timerTask,0,100);
    }

    private TimerTask timerTask;


    private String getStringTime(int cnt) {
        int hour = cnt/3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA,"%02d:%02d:%02d",hour,min,second);
    }

    public interface AudioRecordListener {
        void onAudioStartRecordListener(String path);
        void onAudioRecordingListener(String time);
        void onAudioRecordingComplete();
    }

}
