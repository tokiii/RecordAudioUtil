package com.example.wuchanghe.recordaudioutil;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 音频播放工具类
 * Created by wuchanghe on 2017/3/13 10:40.
 */

public class AudioPlay {
    private MediaPlayer mPlayer;
    private boolean isPause;
    private final int PLAYING = 10;
    private OnMediaPlayStatusListener playStatusListener;
    private int playTime = -1;
    private Timer timer;// 时间定时器
    private String TAG = this.getClass().getSimpleName();

    // 计时器，返回播放进度
    private Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PLAYING:
                    if (playStatusListener != null) {
                        Log.i(TAG, "当前的播放进度/总进度：" + mPlayer.getCurrentPosition() + "  " + mPlayer.getDuration());
                        playStatusListener.onPlayDuration(((float)mPlayer.getCurrentPosition()) / ((float)mPlayer.getDuration()) * 100, calculateMusicLength(mPlayer.getCurrentPosition()));
                    }
                    break;
            }
        }
    };


    /**
     * 单例化这个类
     */
    private static AudioPlay mInstance;

    private AudioPlay() {

    }
    public static AudioPlay getInstance() {
        if (mInstance == null) {
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioPlay();
                }
            }
        }
        return mInstance;
    }


    /**
     * 设置监听器
     *
     * @param playStatusListener
     */
    public void setPlayStatusListener(OnMediaPlayStatusListener playStatusListener) {
        this.playStatusListener = playStatusListener;
    }

    /**
     * 播放声音
     *
     * @param filePathString
     */
    public void playSound(String filePathString) {
        playTime = -1;
        timer = new Timer();
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            //保险起见，设置报错监听
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mPlayer.reset();
                    return false;
                }
            });
        } else {
            mPlayer.reset();//就重置
        }

        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(filePathString);
            mPlayer.prepare();
            mPlayer.start();
            startClick();

            if (playStatusListener != null) {
                playStatusListener.onPlayStart(mPlayer.getDuration() ,calculateMusicLength(mPlayer.getDuration()));
            }

            // 设置播放完成监听
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (playStatusListener != null) {
                        if (timer != null && timerTask != null) {
                            timer.cancel();
                            if (!timerTask.cancel()) {
                                timerTask.cancel();
                            }
                        }
                        playStatusListener.onPlayComplete(mp, calculateMusicLength(mp.getDuration()));
                    }

                }
            });


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //停止函数
    public boolean pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            if (timerTask != null) {
                timer.cancel();
                timer.purge();
                timer = null;
                timerTask = null;
            }
            isPause = true;
        }

        return isPause;
    }

    //继续
    public boolean resume() {
        if (mPlayer != null && isPause) {
            mPlayer.start();
            timer = new Timer();
            startClick();
            isPause = false;
        }

        return isPause;
    }


    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }


    /**
     * 音频播放状态接口
     */
    public interface OnMediaPlayStatusListener {
        void onPlayComplete(MediaPlayer mediaPlayer, String musicLength);

        void onPlayDuration(float percent, String playTime);

        void onPlayStart(int audioTime, String audioLength);

    }


    public void startClick() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                timerHandler.sendEmptyMessage(PLAYING);
            }
        };
        timer.schedule(timerTask, 0, 100);
    }

    private TimerTask timerTask;

    /**
     * 返回是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    /**
     * 播放指定位置
     *
     * @param position
     */
    public void playPosition(int position) {
        if (mPlayer != null) {
            mPlayer.seekTo(position);
            mPlayer.start();
        }

    }


    /**
     * 计算音频长度
     *
     * @param timestamp
     * @return
     */
    public static String calculateMusicLength(long timestamp) {
        String format = "HH:mm:ss";
        SimpleDateFormat sd = new SimpleDateFormat(format);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return sd.format(new Date(timestamp));
    }


}
