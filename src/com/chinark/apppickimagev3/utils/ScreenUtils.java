package com.chinark.apppickimagev3.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * ScreenUtils
 * Created by hanj on 14-9-25.
 */
public class ScreenUtils {
    private static int screenW;
    private static int screenH;
    private static float screenDensity;

    public static void initScreen(Activity mActivity){
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        screenDensity = metric.density;
       // Log.v("initScreen",String.valueOf(screenW));
    }

    public static int getScreenW(){
    	Log.v("initScreen2",String.valueOf(screenW));
        return screenW;
    }

    public static int getScreenH(){
        return screenH;
    }

    public static float getScreenDensity(){
        return screenDensity;
    }

    /** 鏍规嵁鎵嬫満鐨勫垎杈ㄧ巼浠� dp 鐨勫崟浣� 杞垚涓� px(鍍忕礌) */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getScreenDensity() + 0.5f);
    }

    /** 鏍规嵁鎵嬫満鐨勫垎杈ㄧ巼浠� px(鍍忕礌) 鐨勫崟浣� 杞垚涓� dp */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }
}
