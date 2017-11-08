package com.captainhuang.throwline.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by huangshaohua on 2017/11/7.
 */

public class Util {

    /**
     * 获取屏幕宽
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕高
     */
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    @NonNull
    private static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager systemService = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        systemService.getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    public static float dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }
}
