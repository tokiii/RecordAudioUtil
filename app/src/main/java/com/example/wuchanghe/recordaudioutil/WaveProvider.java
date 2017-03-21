package com.example.wuchanghe.recordaudioutil;

public class WaveProvider
{
  public static int DURATION_INTERVAL = 70;
  public static float MS_PER_PX = 0.0F;
  public static int NUM_OF_AMPLITUDE = 90;
  public static float DURATION_PER_WAVEVIEW = DURATION_INTERVAL * NUM_OF_AMPLITUDE;
  public static float PX_PER_MS = 0.0F;
  public static final int VERSION_1 = 0;
  public static final int VERSION_2 = 2;
  public static final int WAVE_SPACE = 4;
  public static final int WAVE_THICKNESS = 4;
  public static int WAVE_WIDTH = 8;
  private static float mWaveViewWidth = 0.0F;
  
  static
  {
    PX_PER_MS = WAVE_WIDTH * 1.0F / DURATION_INTERVAL;
    MS_PER_PX = DURATION_INTERVAL / WAVE_WIDTH;
  }
  
  public static int getScaleFactor()
  {
    return DURATION_INTERVAL;
  }
  
  private static void init()
  {
    NUM_OF_AMPLITUDE = (int)(mWaveViewWidth / WAVE_WIDTH);
    PX_PER_MS = WAVE_WIDTH * 1.0F / DURATION_INTERVAL;
    MS_PER_PX = DURATION_INTERVAL * 1.0F / WAVE_WIDTH;
    DURATION_PER_WAVEVIEW = DURATION_INTERVAL * mWaveViewWidth / WAVE_WIDTH;
  }
  
  public static void setScaleFactor(int paramInt) {}
  
  public static void setWaveWidth(float paramFloat)
  {
    mWaveViewWidth = paramFloat;
    init();
  }
}
